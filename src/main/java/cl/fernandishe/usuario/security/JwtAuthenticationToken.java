package cl.fernandishe.usuario.security;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken{
    private final String apiKey;

    public JwtAuthenticationToken(String apiKey) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }

}
