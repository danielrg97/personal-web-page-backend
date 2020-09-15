package dev.danielrodriguez.models.entities.access;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Entidad de rol. Indica un rol de un usuario en el sistema
 */
@Entity
@Table(name = "T02_ROLE")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "T02_ID")
    private Integer id;

    @Column(name = "T02_NAME")
    private String name;

    @Column(name = "T02_COMMENT")
    private String comment;

    @JsonIgnore
    @OneToMany(targetEntity = Module.class)
    private List modules;
}
