package com.convobee.service;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.AuthenticationRequest;
import com.convobee.api.rest.request.UsersRequest;
import com.convobee.authentication.AuthUserDetails;
import com.convobee.authentication.AuthUserDetailsService;
import com.convobee.data.entity.Interests;
import com.convobee.data.entity.Users;
import com.convobee.data.mapper.InterestsMapper;
import com.convobee.data.mapper.UsersMapper;
import com.convobee.data.repository.UsersRepo;
import com.convobee.email.AccountVerificationEmailContext;
import com.convobee.email.DefaultEmailService;
import com.convobee.exception.InvalidTokenException;
import com.convobee.utils.DateTimeUtil;
import com.convobee.utils.JWTUtil;

@Transactional
@Service
public class AuthenticationService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	AuthUserDetailsService userDetailsService;

	@Autowired
	UsersMapper usersMapper;
	
	@Autowired
	InterestsMapper interestsMapper; 

	@Autowired
	UsersService usersService;
	
    @Autowired
    private DefaultEmailService emailService;
    
    @Autowired
    UsersRepo usersRepo;
    
    @Value("${site.base.url.https}")
    private String baseURL;

	public void signupAuthentication(UsersRequest usersRequest) throws Exception {
		//Inserting user in Users table
		Users user = usersMapper.mapUserFromRequest(usersRequest);
		usersService.createUser(user);
		//Inserting interests in Interests table
		List<Interests> interests = interestsMapper.mapInterestsFromRequest(user, usersRequest);
		usersService.createInterestsForUser(interests);
		sendRegistrationConfirmationEmail(user);
	}
	
    public boolean checkIfUserExist(String email) {
        return usersRepo.findByMailid(email)!=null ? true : false;
    }

    /* 
     * 
     * Used the below guide and followed the mail sending feature
     * https://www.javadevjournal.com/spring-boot/send-email-using-spring/ */
    public void sendRegistrationConfirmationEmail(Users user)  throws Exception{
		final AuthUserDetails userDetails = userDetailsService.loadUserByUsername(user.getMailid());
		final String jwt = jwtUtil.generateToken(userDetails);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(user);
        emailContext.setToken(jwt);
        emailContext.buildVerificationUrl(baseURL, jwt);
        try {
            emailService.sendMail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public boolean verifyUser(UsersRequest usersRequest) throws InvalidTokenException {
    	String token = usersRequest.getToken();
    	int userId;
    	try {
    		userId = jwtUtil.extractUserId(token);
    	}
    	catch(Exception e) {
    		throw new InvalidTokenException("Token is not valid"); 
    	}
    	Users user = usersRepo.findByUserid(userId);
        if(user==null || jwtUtil.isTokenExpired(token)){
            throw new InvalidTokenException("User is not valid");
        }
        if(user.isStatus()) {
        	throw new InvalidTokenException("User is already an verified user");
        }
        user.setStatus(true);
        user.setModifiedat(DateTimeUtil.getCurrentUTCTime());
        usersRepo.save(user); // let's same user details
        return true;
    }


	public String loginAuthentication(AuthenticationRequest authenticationRequest) throws Exception {
		final AuthUserDetails userDetails = usersService.authenticate(authenticationRequest);
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getMailid(), authenticationRequest.getPassword())
					);
		}
		catch(BadCredentialsException e) {
			throw new Exception("Incorrect Username or Password", e);
		}
		final String jwt = jwtUtil.generateToken(userDetails);
		return jwt;
	}

	public String oauthLoginAuthentication(OAuth2User principal) throws Exception {
		try {
			String mailid = (principal.getAttribute("email")).toString();

			final AuthUserDetails userDetails = userDetailsService.loadUserByUsername(mailid);
			final String jwt = jwtUtil.generateToken(userDetails);
			return jwt;
		}
		catch(BadCredentialsException e) {
			throw new Exception("Incorrect Username or Password", e);
		}
	}
}
