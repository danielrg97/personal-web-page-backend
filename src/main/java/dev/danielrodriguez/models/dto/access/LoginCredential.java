package dev.danielrodriguez.models.dto.access;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO de credenciales de acceso
 */
@Getter
@Setter
public class LoginCredential {
    private String username;
    private String password;
}
