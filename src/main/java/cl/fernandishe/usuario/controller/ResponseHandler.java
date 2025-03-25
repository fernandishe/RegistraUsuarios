package cl.fernandishe.usuario.controller;
import cl.fernandishe.usuario.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
@Controller
public class ResponseHandler<T> {
    public static <T> ResponseEntity<T> success(T data) {
        return ResponseEntity.ok(data);
    }

    public static ResponseEntity<Response> noContent() {
        Response response = new Response();
        response.setMensaje("No content available");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response); // Devuelve 204 No Content con el mensaje
    }

    public static ResponseEntity<Response> error(Exception e) {
        Response response = new Response();
        response.setMensaje("Error: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    public static ResponseEntity<Response> notFound() {
        Response response = new Response();
        response.setMensaje("Resource not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
