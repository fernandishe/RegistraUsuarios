package cl.fernandishe.usuario.utils;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import io.jsonwebtoken.*;
import cl.fernandishe.usuario.security.JwtAuthenticationToken;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String secretKey;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = extractTokenFromRequest(request);
        if (token != null && !token.isEmpty()) {
            try {
                String username = Jwts.parser()
                        .setSigningKey(secretKey.getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();

                if (username != null) {
                    // Si el token es válido, lo configuramos en el contexto de seguridad
                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(username);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (ExpiredJwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Expirado");
                return;
            } catch (JwtException | IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Inválido");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Extracts the token after "Bearer "
        }
        return null;
    }
}
