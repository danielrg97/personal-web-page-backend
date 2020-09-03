package dev.danielrodriguez.models.entities.access;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Entidad de Token de acceso
 */
@Entity
@Table(name = "T04_ACCESS_TOKENS")
@Getter
@Setter
@NoArgsConstructor
public class AccessToken {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "T04_ID")
    private Integer id;

    @Column(name="T04_TOKEN")
    private String token;

    @Column(name="T04_CREATION_DATE")
    private Date creationDate;

    @OneToOne
    @JsonIgnore
    private User user;
}
