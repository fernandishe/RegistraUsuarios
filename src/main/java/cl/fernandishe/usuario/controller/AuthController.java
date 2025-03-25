package cl.fernandishe.usuario.controller;


import cl.fernandishe.usuario.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.fernandishe.usuario.utils.JwtTokenUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtTokenUtil jwtTokenUtil;
    @Value("${login.apikey}")
    private String apiKeyConfig;

    @Autowired
    public AuthController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(path = "token")
    public ResponseEntity<?> createAuthToken(@RequestHeader  String apiKey)
    {
        Response mensaje = new Response();
        if (apiKeyConfig.equals(apiKey)) {
            mensaje.setMensaje(jwtTokenUtil.generateToken(apiKey));
            return ResponseEntity.ok().body(mensaje);
        } else {
            mensaje.setMensaje("Invalid API key");
            return ResponseEntity.status(401).body(mensaje);
        }
    }
}


