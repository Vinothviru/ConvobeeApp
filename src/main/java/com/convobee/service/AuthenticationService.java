package com.convobee.service;

import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.convobee.api.rest.response.JWTResponse;
import com.convobee.api.rest.response.OAuthResponse;
import com.convobee.api.rest.response.builder.OauthResponseBuilder;
import com.convobee.authentication.AuthUserDetails;
import com.convobee.authentication.AuthUserDetailsService;
import com.convobee.constants.Constants;
import com.convobee.data.entity.Interests;
import com.convobee.data.entity.Users;
import com.convobee.data.mapper.InterestsMapper;
import com.convobee.data.mapper.UsersMapper;
import com.convobee.data.repository.UsersRepo;
import com.convobee.email.AccountVerificationEmailContext;
import com.convobee.email.DefaultEmailService;
import com.convobee.exception.InvalidTokenException;
import com.convobee.utils.DateTimeUtil;
import com.convobee.utils.EncryptionUtil;
import com.convobee.utils.JWTUtil;

/* https://stackoverflow.com/questions/33881648/springs-transactioninterceptor-overrides-my-exception */
@Transactional(rollbackFor = Exception.class)
@Service
public class AuthenticationService {
	
	Logger logger = LoggerFactory.getLogger(AuthenticationService.class); 

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
    
    @Value("${aes.secret.key}")
    private String aesSecretKey;

	public boolean signupAuthentication(UsersRequest usersRequest) throws Exception {
		//Inserting user in Users table
		Users user = usersMapper.mapUserFromRequest(usersRequest);
		usersService.createUser(user);
		//Inserting interests in Interests table
		List<Interests> interests = interestsMapper.mapInterestsFromRequest(user, usersRequest);
		usersService.createInterestsForUser(interests);
		String isFromOauthSignup = usersRequest.getBvfhdjsk();
		if(isFromOauthSignup!=null&&!isFromOauthSignup.isEmpty()&&!isFromOauthSignup.isBlank()) {
			return true;
		}
		boolean isSuccess;
		try {
			isSuccess = sendRegistrationConfirmationEmail(user);
		}
		catch(Exception e) {
			logger.debug("Exception occured in signupAuthentication method", e);
		    throw new Exception(Constants.ALREADY_REGISTERED_USER);
		}
		return isSuccess;
	}
	
	/* Unused method but may use in future */
    public boolean checkIfUserExist(String email) {
        boolean isAvailable =  usersRepo.findByMailid(email).get()!=null ? true : false;
        return isAvailable;
    }

    /* 
     * 
     * Used the below guide and followed the mail sending feature
     * https://www.javadevjournal.com/spring-boot/send-email-using-spring/ */
    public boolean sendRegistrationConfirmationEmail(Users user)  throws Exception{
		final AuthUserDetails userDetails = userDetailsService.loadUserByUsername(user.getMailid());
		final String jwt = jwtUtil.generateToken(userDetails);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(user);
        emailContext.setToken(jwt);
        emailContext.buildVerificationUrl(baseURL, jwt);
        try {
            emailService.sendMail(emailContext);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
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

    public OAuthResponse oauthSignup(OAuth2User principal) throws Exception {
    		String username = (principal.getAttribute("name")).toString();
        	String mailid = (principal.getAttribute("email")).toString();
        	try {
        		if(checkIfUserExist(mailid)) {
        			throw new Exception(Constants.ALREADY_REGISTERED_USER);
        		}
        	}
        	catch (Exception e) {
    			// TODO: handle exception
        		if(e.getMessage().equals(Constants.ALREADY_REGISTERED_USER)) {
        			throw e;
        		}
    		}
        	/* https://howtodoinjava.com/java/java-security/java-aes-encryption-example/ */
        	String encryptedText = EncryptionUtil.encrypt(mailid,aesSecretKey);
        	OAuthResponse oauthResponse = new OauthResponseBuilder().buildResponse(username, mailid, encryptedText);
        	return oauthResponse;

    }
    
	public JWTResponse loginAuthentication(AuthenticationRequest authenticationRequest) throws Exception {
		JWTResponse jwtResponse = new JWTResponse();
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
		jwtResponse.setJwt(jwt);
		return jwtResponse;
	}

	public JWTResponse oauthLoginAuthentication(OAuth2User principal) throws Exception {
		try {
			JWTResponse jwtResponse = new JWTResponse();
			String mailid = (principal.getAttribute("email")).toString();

			final AuthUserDetails userDetails = userDetailsService.loadUserByUsername(mailid);
			final String jwt = jwtUtil.generateToken(userDetails);
			jwtResponse.setJwt(jwt);
			return jwtResponse;
		}
		catch(BadCredentialsException e) {
			throw new Exception("Incorrect Username or Password", e);
		}
	}
}
