package cl.fernandishe.usuario.model;

import cl.fernandishe.usuario.entity.Usuario;
import lombok.Data;

import java.util.Date;

@Data
public class UsuarioResponse {
        private long id;
        private Date created;
        private Date modified;
        private Date lastLogin;
        private String token;
        private boolean isActive;

}
