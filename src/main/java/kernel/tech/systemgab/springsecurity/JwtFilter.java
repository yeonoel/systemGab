package kernel.tech.systemgab.springsecurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;


/**
 Jwt security filter for all request
 *
 * @author yeonoel
 *
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    public JwtFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * create doFilterInternal to filtre all rquest.
     *
     * @param request
     * @return
     *
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String cardNumber = null;
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                cardNumber = jwtUtil.extractCardNumber(jwt);

                // Vérifier si on n'a pas déjà une authentification
                if (cardNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.validateToken(jwt, cardNumber)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                cardNumber, null, new ArrayList<>()
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                logger.error("Impossible de faire l'authentificatio du client: {}", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}