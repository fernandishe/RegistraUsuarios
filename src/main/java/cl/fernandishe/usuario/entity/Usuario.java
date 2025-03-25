package cl.fernandishe.usuario.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String password;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Phones> phones;
    private Date create;
    private Date modified;
    private Date last_login;
    private String token;
    private Boolean isactive;
    @PrePersist
    public void prePersist() {
        this.create = new Date();
        this.modified = new Date();
        this.last_login = new Date();
        this.isactive = true;
        this.token = generateToken();
    }

    @PreUpdate
    public void preUpdate() {
        this.modified = new Date();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
