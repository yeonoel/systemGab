package kernel.tech.systemgab.springsecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 JWTUtil for user authentication and authorization
 *
 * @author yeonoel
 *
 */
@Component
public class JWTUtil {

    private final String SECRET_KEY;

    public JWTUtil(@Value("${application.security.jwt.secret-key}") String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    public String generateToken(String cardNumber) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, cardNumber);
    }

    /**
     * createToken to generate a token on login
     *
     * @param claims
     * @param subject
     * @return String
     *
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expire dans 1 jours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * validateToken Check token validity
     *
     * @param token
     * @param cardNumber
     * @return boolean
     *
     */
    public boolean validateToken(String token, String cardNumber) {
        final String username = extractCardNumber(token);
        return (username.equals(cardNumber) && !isTokenExpired(token));
    }

    public String extractCardNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}

