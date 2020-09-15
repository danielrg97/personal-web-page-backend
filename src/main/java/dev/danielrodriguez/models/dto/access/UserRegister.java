package dev.danielrodriguez.models.dto.access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserRegister {
    private Integer id;

    private String names;

    private String lastNames;

    private String email;

    private String userName;

    private String password;

    private String passwordConfirm;
}
