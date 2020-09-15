package dev.danielrodriguez.filters;

import dev.danielrodriguez.annotations.AllowedRole;
import dev.danielrodriguez.exceptions.access.TokenNotFoundException;
import dev.danielrodriguez.models.dao.access.AccessTokenDao;
import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import static dev.danielrodriguez.constants.access.SecurityConstants.KEY;
import static dev.danielrodriguez.constants.access.SecurityConstants.HEADER;
import static dev.danielrodriguez.utils.Utils.getEndpoint;

/**
 * Filtro de peticiones para validar token y autorizar/denegar acceso a enpoints
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private AccessTokenDao accessTokenDao;

    @Autowired
    private Reflections reflections;

    /**
     * Filtro por peticion
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        try {
            //Para poder inyectar AccessTokenDao
            HttpServletRequest httpRequest = (HttpServletRequest) httpServletRequest;
            if (accessTokenDao == null) {
                ServletContext context = httpRequest.getSession().getServletContext();
                SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, context);
            }
            token = httpServletRequest.getHeader(HEADER);
            //valido si el token ya esta almacenado (que indica que el usuario ya esta loggeado en el sistema)
            if (accessTokenDao.findAccessTokenByToken(token).isPresent()) {
                Claims claims = getTokenData(httpServletRequest.getHeader(HEADER));
                if (claims.get("authorities") != null) {
                    if(hasAccessToEndpoint(httpRequest, claims)){
                        return;
                    }else{
                        SecurityContextHolder.clearContext();
                    }
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            //Elimino el token de la base de datos si ya expiró
            if(e instanceof ExpiredJwtException && !token.equals(null) && accessTokenDao.findAccessTokenByToken(token).isPresent()) {
                accessTokenDao.delete(accessTokenDao.findAccessTokenByToken(token).orElseThrow(()-> new TokenNotFoundException("No hay token que eliminar", true)));
            }
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    /**
     * Desencripta el token
     * @param token
     * @return Claims contenido del token
     */
    private Claims getTokenData(String token) {
        return Jwts.parser()
                .setSigningKey(KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Otorga autorizacion (Sin usar)
     * @param claims
     */
    private void setUpSpringAuthentication(Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> authorities = (List) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    /**
     * Metodo para validar que el usuario tenga permisos para acceder al endpoint
     *
     * @param httpRequest
     * @param claims
     * @return
     * @throws Exception
     */
    private boolean hasAccessToEndpoint(HttpServletRequest httpRequest, Claims claims) throws Exception {
        Map<String, Method> methodsAnnotated = getMethodsAnotatedAs(AllowedRole.class);
        if(null != methodsAnnotated.get(getEndpoint(httpRequest))){
            return methodsAnnotated.get(getEndpoint(httpRequest))
                    .getAnnotation(AllowedRole.class)
                    .role()
                    .equals(((List)claims.get("authorities")).stream().findFirst().orElse(""));
        }else{
            //el endpoint no esta anotado con AllowedRole
            return true;
        }
    }

    /**
     * Obtiene los metodos anotados con la anotacion que se le pase por parametros
     * @param type
     * @return
     * @throws Exception
     */
    private Map<String, Method> getMethodsAnotatedAs(Class<? extends Annotation> type) throws Exception {
        Set<Method> methodList = reflections.getMethodsAnnotatedWith(type);
        Map<String, Method> methodAndEndpoint = new HashMap<>();
        for(Method p : methodList) {
            methodAndEndpoint.put(getMapping(p), p);
        }
        return methodAndEndpoint;
    }

    /**
     * Metodo para obtener el mapping del endpoint
     * Se obtiene de las anotaciones de spring
     * @param method
     * @return
     * @throws Exception
     */
    private String getMapping(Method method) throws Exception {
        if(null != method.getAnnotation(RequestMapping.class)){
            return Arrays.stream(method.getAnnotation(RequestMapping.class).path()).findFirst().orElse("");
        }else if(null != method.getAnnotation(PostMapping.class)){
            return Arrays.stream(method.getAnnotation(PostMapping.class).path()).findFirst().orElse("");
        }else if(null != method.getAnnotation(GetMapping.class)){
            return Arrays.stream(method.getAnnotation(GetMapping.class).path()).findFirst().orElse("");
        }else if(null != method.getAnnotation(PutMapping.class)){
            return Arrays.stream(method.getAnnotation(PutMapping.class).path()).findFirst().orElse("");
        }else if(null != method.getAnnotation(DeleteMapping.class)){
            return Arrays.stream(method.getAnnotation(DeleteMapping.class).path()).findFirst().orElse("");
        }else{
            throw new Exception("Metodo no aceptado");
        }
    }
}
