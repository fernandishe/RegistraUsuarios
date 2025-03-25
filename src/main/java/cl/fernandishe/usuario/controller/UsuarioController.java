package cl.fernandishe.usuario.controller;

import cl.fernandishe.usuario.Service.UsuarioService;
import cl.fernandishe.usuario.entity.Usuario;
import cl.fernandishe.usuario.model.Response;
import cl.fernandishe.usuario.model.UsuarioResponse;
import cl.fernandishe.usuario.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtTokenUtil jwtTokenUtil;


    @Autowired
    public UsuarioController(UsuarioService usuarioService, JwtTokenUtil jwtTokenUtil) {
        this.usuarioService = usuarioService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // Endpoint para crear o actualizar un usuario
    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUsuario(@RequestBody Usuario usuario, @RequestHeader("Authorization") String token) throws Exception {
        Response<String> responseError = new Response<String>();
        try {

            if (token == null || !token.startsWith("Bearer ")) {
                responseError.setMensaje("Token no proporcionado o malformado.");
                return ResponseEntity.status(400).body(responseError);
            }

            String jwt = token.substring("Bearer ".length());
            if (jwtTokenUtil.validateToken(jwt, jwtTokenUtil.getUsernameFromToken(jwt))) {
                Response<UsuarioResponse> responseUsuario = new Response<UsuarioResponse>();
                usuario.setToken(jwt);
                Response<Usuario> responseNewUser = (Response<Usuario>) usuarioService.saveUsuario(usuario);
                Usuario nuevoUsuario = responseNewUser.getMensaje();
                UsuarioResponse usuarioResponse = new UsuarioResponse();
                usuarioResponse.setId(nuevoUsuario.getId());
                usuarioResponse.setCreated(nuevoUsuario.getCreate());
                usuarioResponse.setModified(nuevoUsuario.getModified());
                usuarioResponse.setLastLogin(nuevoUsuario.getLast_login());
                usuarioResponse.setToken(jwt);
                usuarioResponse.setActive(true);
                responseUsuario.setMensaje(usuarioResponse);
                return ResponseEntity.ok().body(responseUsuario);
            } else {
                responseError.setMensaje("Token inválido o expirado");
                return new ResponseEntity<>(responseError, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            responseError.setMensaje(e.getMessage());
            return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/obtenerUsuarios")
    public ResponseEntity<?> getAllUsuarios(@RequestHeader("Authorization") String token) {
        Response<String> responseError = new Response<>();
        Response<Usuario> responseUsuarios = new Response<>();
        if (jwtTokenUtil.validateToken(token, jwtTokenUtil.getUsernameFromToken(token))) {
            Iterable<Usuario> usuarios = usuarioService.getAllUsuarios();

            if (usuarios.iterator().hasNext()) {
                responseUsuarios.setMensaje(usuarios.iterator().next());
                return ResponseHandler.success(responseUsuarios);
            } else {
                return ResponseHandler.noContent();
            }
        }else{
            responseError.setMensaje("Token inválido o expirado");
            return new ResponseEntity<>(responseError, HttpStatus.UNAUTHORIZED);
        }
    }
        @GetMapping("/obtenerUsuario/{user_id}")
        public ResponseEntity<?> getUsuarioById(@PathVariable Long user_id, @RequestHeader("Authorization") String token) {
            try {
                if (jwtTokenUtil.validateToken(token, jwtTokenUtil.getUsernameFromToken(token))) {
                    Usuario usuario = usuarioService.getUsuarioById(user_id);
                    if (usuario != null) {
                        return ResponseHandler.success(usuario);
                    } else {
                        return ResponseHandler.success("No se encontró el usuario");
                    }
                }
                else{
                    return new ResponseEntity<>("Token inválido o expirado", HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception e) {
                return ResponseHandler.error(e);
            }
        }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            if (jwtTokenUtil.validateToken(token, jwtTokenUtil.getUsernameFromToken(token))) {
                return usuarioService.deleteUsuario(id);
            }else{
                return new ResponseEntity<>("Token inválido o expirado", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return ResponseHandler.error(e);
        }
    }
}

