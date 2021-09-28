package com.convobee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.convobee.authentication.filter.JwtResquestFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtResquestFilter jwtResquestFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
		.antMatcher("/**")
		.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/addslot").hasRole("ADMIN")
		.antMatchers("/showslots").hasAnyRole("USER","ADMIN")
		.antMatchers("/admin").hasRole("ADMIN")
		.antMatchers("/hello").hasAnyRole("USER","ADMIN")
		.antMatchers("/signup").permitAll()
		.antMatchers("/login").permitAll()
		.anyRequest().authenticated()
		.and()
		.oauth2Login()
		.and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.NEVER);

		http.addFilterBefore(jwtResquestFilter, UsernamePasswordAuthenticationFilter.class);

		/*http.csrf().disable()
						.authorizeRequests()		
						.antMatchers("/admin").hasRole("ADMIN")
						.antMatchers("/hello").hasAnyRole("USER","ADMIN")
						.antMatchers("/signup").permitAll()
						.antMatchers("/oauthsignup").permitAll()
						.antMatchers("/login").permitAll()
						.anyRequest().authenticated()
						.and()
						.oauth2Login();
						.and().sessionManagement()
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}

	//https://stackoverflow.com/questions/30366405/how-to-disable-spring-security-for-particular-url
	//	@Override
	//	public void configure(WebSecurity web) throws Exception {
	//	    web.ignoring().antMatchers("/oauthsignup");
	//	}


}
