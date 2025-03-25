package cl.fernandishe.usuario.model;

import lombok.Data;

@Data
public class Response<T> {
    private T mensaje;
}
