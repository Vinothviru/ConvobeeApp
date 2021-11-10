package com.convobee.authentication.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.convobee.authentication.AuthUserDetails;
import com.convobee.authentication.AuthUserDetailsService;
import com.convobee.constants.Constants;
import com.convobee.service.UsersService;
import com.convobee.utils.JWTUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.CompressionCodecResolver;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.lang.Strings;

@Component
public class JwtResquestFilter extends OncePerRequestFilter{

	@Autowired
	AuthUserDetailsService userDetailsService;
	
	@Autowired
	JWTUtil jwtUtil;
	
	@Autowired
	UsersService usersService;
		
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader(Constants.AUTHORIZATION);
		
		String username = null;
		String jwt = null;
		AuthUserDetails userDetails = null;
		if(authorizationHeader!=null && authorizationHeader.startsWith(Constants.BEARER))
		{
			jwt = authorizationHeader.substring(7);
//			ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, Object> claimsMap = objectMapper.readValue(jwt,Map.class);
//            Claims claims = new DefaultClaims(claimsMap);
			
//			try {
//				Date dt = parse(jwt); 
//				System.out.println("Varudha");
//			}
//			catch(Exception e) {
//				System.out.println("Varudha");
//				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "message goes here");
//				response.setStatus(HttpStatus.UNAUTHORIZED.value());
//				response.getWriter().print("Invalid token");
				//response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token");
//				throw new ServletException(Constants.JWT_EXPIRED);
//			}
			
			username = jwtUtil.extractUsername(jwt);
			userDetails = this.userDetailsService.loadUserByUsername(username);
			if(!userDetails.isEnabled()) {
				throw new IOException(Constants.INACTIVE_USER);
			}
			/* Already checked in DefaultJwtsParser, so this seems to be redundant check. Let it be, Will check later and remove this. */
			if(jwtUtil.isTokenExpired(jwt)) {
				throw new IOException(Constants.JWT_EXPIRED);
			}
			/* This check will be costly for every request, need to check whether this is needed or not */
			if(usersService.isBannedUser(jwtUtil.extractUserId(jwt))) {
				throw new IOException(Constants.BANNED_USER);
			}
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
		{
			if(jwtUtil.validateToken(jwt, userDetails)) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities()
					);
			usernamePasswordAuthenticationToken.setDetails(
					new WebAuthenticationDetailsSource().buildDetails(request)
					);
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			//System.out.println("USER ID via AuthDetails in = JwtResquestFilter" + userDetails.getUserid());
			}
		}
		chain.doFilter(request, response);
	}
	
	/*public Date parse(String jwt) throws ExpiredJwtException, MalformedJwtException, SignatureException, Exception, JsonProcessingException {

        Assert.hasText(jwt, "JWT String argument cannot be null or empty.");

        String base64UrlEncodedHeader = null;
        String base64UrlEncodedPayload = null;
        String base64UrlEncodedDigest = null;

        int delimiterCount = 0;

        StringBuilder sb = new StringBuilder(128);

        for (char c : jwt.toCharArray()) {

            if (c == JwtParser.SEPARATOR_CHAR) {

                CharSequence tokenSeq = Strings.clean(sb);
                String token = tokenSeq!=null?tokenSeq.toString():null;

                if (delimiterCount == 0) {
                    base64UrlEncodedHeader = token;
                } else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }

                delimiterCount++;
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        if (delimiterCount != 2) {
            String msg = "JWT strings must contain exactly 2 period characters. Found: " + delimiterCount;
            throw new MalformedJwtException(msg);
        }
        if (sb.length() > 0) {
            base64UrlEncodedDigest = sb.toString();
        }

        if (base64UrlEncodedPayload == null) {
            throw new MalformedJwtException("JWT string '" + jwt + "' is missing a body/payload.");
        }

        // =============== Header =================
        Header header = null;

        CompressionCodec compressionCodec = null;

        if (base64UrlEncodedHeader != null) {
            String origValue = TextCodec.BASE64URL.decodeToString(base64UrlEncodedHeader);
			ObjectMapper objectMapper = new ObjectMapper();
	          Map<String, Object> m = objectMapper.readValue(origValue,Map.class);

            if (base64UrlEncodedDigest != null) {
                header = new DefaultJwsHeader(m);
            } else {
                header = new DefaultHeader(m);
            }
            CompressionCodecResolver compressionCodecResolver = new DefaultCompressionCodecResolver();
            compressionCodec = compressionCodecResolver.resolveCompressionCodec(header);
        }

        // =============== Body =================
        String payload;
        if (compressionCodec != null) {
            byte[] decompressed = compressionCodec.decompress(TextCodec.BASE64URL.decode(base64UrlEncodedPayload));
            payload = new String(decompressed, Strings.UTF_8);
        } else {
            payload = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload);
        }

        Claims claims = null;

        if (payload.charAt(0) == '{' && payload.charAt(payload.length() - 1) == '}') { //likely to be json, parse it:
			ObjectMapper objectMapper = new ObjectMapper();
          Map<String, Object> claimsMap = objectMapper.readValue(payload,Map.class);
            claims = new DefaultClaims(claimsMap);
        }


        //since 0.3:

            SimpleDateFormat sdf;

            final Date now = new Date();
            long nowTime = now.getTime();

            //https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-30#section-4.1.4
            //token MUST NOT be accepted on or after any specified exp time:
            Date exp = claims.getExpiration();
            throw new IOException(Constants.JWT_EXPIRED);
            //return exp;
    }
*/	
}
