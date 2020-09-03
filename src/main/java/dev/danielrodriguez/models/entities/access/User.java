package dev.danielrodriguez.models.entities.access;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entidad de usuario. Indica un usuario registrado en el sistema
 */
@Entity
@Table(name = "T01_USER")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "T01_ID")
    private Integer id;

    @Column(name = "T01_NAMES")
    private String names;

    @Column(name = "T01_LAST_NAMES")
    private String lastNames;

    @Column(name = "T01_EMAIL")
    private String email;

    @Column(name = "T01_USERNAME")
    private String userName;

    @JsonIgnore
    @Column(name = "T01_PASSWORD")
    private String password;

    @OneToOne
    private Role role;
}
