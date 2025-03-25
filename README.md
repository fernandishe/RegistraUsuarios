# RegistraUsuarios
Se debe compilar el fuente con maven: mvn clean install spring-boot:run o en su defecto configurar el comando en su ide de preferencia
una vez iniciado acceder a la documentación de swagger http://localhost:8089/swagger-ui/index.html (puerto según application.properties)

obtener el token jwt desde esta api
http://localhost:8089/api/auth/token enviando como header apiKey: 12345678 (o según configuración en application.properties), se valida contra lo enviado
llamar a la api http://localhost:8089/api/usuarios/crear agregando el header Authorization Bearer obtenido en el paso anterior
