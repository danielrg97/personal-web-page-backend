package dev.danielrodriguez.models.entities.access;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entidad de modulo, indica un recurso del sistema
 */
@Entity
@Table(name = "T03_MODULE")
@Getter
@Setter
@NoArgsConstructor
public class Module {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "T03_ID")
    private Integer id;

    @Column(name = "T03_NAME")
    private String name;

    @Column(name = "T03_COMMENT")
    private String comment;
}
