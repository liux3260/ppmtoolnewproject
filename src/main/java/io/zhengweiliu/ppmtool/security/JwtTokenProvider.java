package io.zhengweiliu.ppmtool.security;

import io.jsonwebtoken.*;
import io.zhengweiliu.ppmtool.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.zhengweiliu.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static io.zhengweiliu.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {
    //Generate token

    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Date now= new Date(System.currentTimeMillis());
        Date expiryDate = new Date (now.getTime()+EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String,Object> claims = new HashMap<>();
        claims.put("id",(Long.toString(user.getId())));
        claims.put("username",user.getUsername());
        claims.put("fullName",user.getFullName());


        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,SECRET)
                .compact();
    }

    //Validate token

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }
        catch(SignatureException ex){
            System.out.println("Invalid JWT Signature");
        }
        catch (MalformedJwtException ex){
            System.out.println("Invalid JWT Token");
        }
        catch (ExpiredJwtException ex){
            System.out.println("Expired JWT Token");
        }
        catch (UnsupportedJwtException ex){
            System.out.println("Unsupported JWT Signature");
        }
        catch (IllegalArgumentException ex) {
            System.out.println("JWT claim string is empty");
        }
        return false;
    }
    //Get User Id from token

    public Long getUserIdFromJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String) claims.get("id");

        return Long.parseLong(id);
    }
}
