package dev.danielrodriguez.controllers.access;

import dev.danielrodriguez.delegation.access.AccessDelegate;
import dev.danielrodriguez.exceptions.access.AccessException;
import dev.danielrodriguez.exceptions.access.WrongCredentialException;
import dev.danielrodriguez.models.dto.access.LoginCredential;
import dev.danielrodriguez.models.entities.access.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static dev.danielrodriguez.constants.access.SecurityConstants.HEADER;


@RestController
public class AccessController {
    private AccessDelegate accessDelegate;

    /**
     * Constructor para inyeccion de dependencias
     * @param accessDelegate
     */
    @Autowired
    public AccessController(AccessDelegate accessDelegate){
        this.accessDelegate = accessDelegate;;
    }

    /**
     * Endpoint de autenticación de usuario, retorna el JWT si las credenciales son correctas
     * @param user
     * @return
     * @throws WrongCredentialException
     */
    @PostMapping(path = "/authenticate", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public AccessToken authenticate(@RequestBody LoginCredential user) throws WrongCredentialException {
        AccessToken token = accessDelegate.authenticate(user);
        return token;
    }

    /**
     * Endpoint para eliminar token si se desloggea
     * @return
     */
    @PostMapping(path = "/log-out", produces = "application/json")
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse response) throws AccessException {
        accessDelegate.logout(httpServletRequest.getHeader(HEADER));
    }

    //TODO: hacer que el @RolesAllowed funcione
    @RolesAllowed("admin")
    @GetMapping(path = "/prueba-de-acceso", produces="application/json")
    @ResponseBody
    public String endpointScope(){
        return "endpoint alcanzado";
    }
}
