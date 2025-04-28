package vti.authenticationservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vti.authenticationservice.response.dto.account.AccountDto;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(AccountDto account) {
        Map<String, Object> extraClaims = new HashMap<>();
//        set claim custom
        extraClaims.put("fullName", account.getFullName());

        extraClaims.put("role", account.getRole());
        return generateToken(extraClaims, account);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            AccountDto accountDto
    ) {
        return buildToken(extraClaims, accountDto, jwtExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            AccountDto accountDto,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(accountDto.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, AccountDto accountDto) {
        final String username = extractUsername(token);
        return (username.equals(accountDto.getUsername())) && !isTokenExpired(token);
    }


    // Lấy thời gian còn lại của token (tính bằng giây)
    public Long getExpirationTime(String token) {
        Date now = new Date();
        Date expiration = extractExpiration(token);
        long expiresIn = (expiration.getTime() - now.getTime()) / 1000;
        return expiresIn > 0 ? expiresIn : 0L;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
