package dev.danielrodriguez.filters;

import dev.danielrodriguez.models.dao.access.AccessTokenDao;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import static dev.danielrodriguez.constants.access.SecurityConstants.KEY;
import static dev.danielrodriguez.constants.access.SecurityConstants.HEADER;

/**
 * Filtro de peticiones para validar token y autorizar/denegar acceso a enpoints
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private AccessTokenDao accessTokenDao;

    /**
     * Filtro por peticion
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
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
            if (accessTokenDao.findAccessTokenByToken(token).size() != 0) {
                Claims claims = getTokenData(httpServletRequest.getHeader(HEADER));
                if (claims.get("authorities") != null) {
                    setUpSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            //Elimino el token de la base de datos si ya expiró
            if(e instanceof ExpiredJwtException && !token.equals(null) && !accessTokenDao.findAccessTokenByToken(token).get(0).equals(null)) {
                accessTokenDao.delete(accessTokenDao.findAccessTokenByToken(token).get(0));
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
     * Otorga autorizacion
     * @param claims
     */
    private void setUpSpringAuthentication(Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> authorities = (List) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);

    }
}
