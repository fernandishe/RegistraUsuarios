package cl.fernandishe.usuario.Service;

import cl.fernandishe.usuario.Repository.UsuarioRepository;
import cl.fernandishe.usuario.controller.ResponseHandler;
import cl.fernandishe.usuario.entity.Usuario;
import cl.fernandishe.usuario.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsuarioService {
    private final UsuarioRepository UsuarioRepository;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    @Autowired
    public UsuarioService(UsuarioRepository UsuarioRepository) {
        this.UsuarioRepository = UsuarioRepository;
    }

    @Transactional
    public Response<?> saveUsuario(Usuario usuario) throws Exception {
        Response<Usuario> responseOk = new Response<>();
        if (!Pattern.matches(EMAIL_REGEX, usuario.getEmail())) {
            throw new Exception("El formato del correo electrónico es inválido.");
        }
        Usuario usuarioBD = UsuarioRepository.findUsuariosByEmail(usuario.getEmail());
        if(usuarioBD != null) {
            if(usuarioBD.getEmail().equals(usuario.getEmail())) {
                throw new Exception("El usuario ya existe");
            }
        }
        Usuario usuarioResponse = UsuarioRepository.save(usuario);
        responseOk.setMensaje(usuarioResponse);
        return responseOk;

    }

    public Iterable<Usuario> getAllUsuarios() {
        return UsuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        Optional<Usuario> usuario = UsuarioRepository.findById(id);
        return usuario.orElse(null);
    }

    public ResponseEntity<?> deleteUsuario(Long id) {
        Response response = new Response();
        try {
            Optional<Usuario> usuario = UsuarioRepository.findById(id);

            if (usuario.isPresent()) {
                UsuarioRepository.deleteById(id);
                response.setMensaje("Usuario eliminado exitosamente");
                return ResponseHandler.success(response);
            } else {
                return ResponseHandler.notFound();
            }
        } catch (Exception e) {
            return ResponseHandler.error(e);
        }
    }

}
