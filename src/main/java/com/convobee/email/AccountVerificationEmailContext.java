package com.convobee.email;

import org.springframework.web.util.UriComponentsBuilder;

import com.convobee.data.entity.Users;

public class AccountVerificationEmailContext extends AbstractEmailContext {

    private String token;


    @Override
    public <T> void init(T context){
        //we can do any common configuration setup here
        // like setting up some base URL and context
        Users customer = (Users) context; // we pass the customer informati
        put("firstName", customer.getUsername());
        setTemplateLocation("email/emailverification");
        setSubject("Complete your registration");
        setFrom("vinothviru319@gmail.com");
        setTo(customer.getMailid());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    public void buildVerificationUrl(final String baseURL, final String token){
        final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/verifyuser").queryParam("token", token).toUriString();
        put("verificationURL", url);
    }
}
