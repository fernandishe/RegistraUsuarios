package cl.fernandishe.usuario.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())                .compact();
    }

    public String getUsernameFromToken(String token) {
        JwtParser jwtParser = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .build();

        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        JwtParser jwtParser = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .build();

        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(getUsernameFromToken(token)) && !isTokenExpired(token));
    }
    public String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Extracts the token after "Bearer "
        }
        return null;
    }
}
