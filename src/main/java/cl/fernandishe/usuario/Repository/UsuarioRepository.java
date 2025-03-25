package cl.fernandishe.usuario.Repository;

import cl.fernandishe.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findUsuariosByEmail(String email);
}
