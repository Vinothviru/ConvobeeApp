package com.convobee.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.convobee.authentication.AuthUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
	@Autowired
	UserDetailsService userDetailsService;
	
	//Get user_id
	public int extractUserId(String token) {
		Claims claims = extractAllClaims(token);
		return (int) claims.get("userid");
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	private <T> T  extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	public String generateToken(AuthUserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userid", userDetails.getUserid());//add userid
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
				
				
	}
	
	public boolean validateToken(String token, AuthUserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
	}
	
}
