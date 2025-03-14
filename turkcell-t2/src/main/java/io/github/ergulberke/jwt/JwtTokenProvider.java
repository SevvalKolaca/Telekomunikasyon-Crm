package io.github.ergulberke.jwt;

import io.github.ergulberke.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "your_secret_key_here";
    private static final long EXPIRE_LENGTH = 3600000; // 1 hour

    public String createToken(String username, Role role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", "ROLE_" + role.name());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRE_LENGTH);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Role getRoleFromToken(String token) {
        String roleStr = getClaimFromToken(token, claims -> claims.get("role", String.class));
        System.out.println("Token içindeki raw role: " + roleStr);
        // "ROLE_" önekini kesin olarak kaldırmak için substring kullanıyoruz:
        String roleValue = roleStr.substring(5); // "ROLE_" 5 karakter, geriye "ADMIN" kalır.
        return Role.valueOf(roleValue);
    }
}
