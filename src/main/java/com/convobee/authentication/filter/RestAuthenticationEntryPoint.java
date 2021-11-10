package com.convobee.authentication.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import com.convobee.api.rest.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
    	String uri = httpServletRequest.getRequestURI();
    	if(!uri.equals("/oauthlogin") && !uri.equals("/oauthsignup") && !uri.equals("/favicon.ico")) {
            ApiResponse response = new ApiResponse(401, "Unauthorised");
            response.setMessage("Unauthorised");
            OutputStream out = httpServletResponse.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, response);
            out.flush();
    	}
    	else if(uri.equals("/oauthlogin") || uri.equals("/oauthsignup")) {
    		ApiResponse response = new ApiResponse(200,"http:localhost:8080/oauth2/authorization/google");
            OutputStream out = httpServletResponse.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, response);
            out.flush();
    	}
    }
}
