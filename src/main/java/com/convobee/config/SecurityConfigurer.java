package com.convobee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.convobee.authentication.filter.JwtResquestFilter;
import com.convobee.authentication.filter.RestAccessDeniedHandler;
import com.convobee.authentication.filter.RestAuthenticationEntryPoint;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtResquestFilter jwtResquestFilter;
	
	@Autowired
	RestAccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	RestAuthenticationEntryPoint unauthorizedHandler;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		.antMatcher("/**")
		.authorizeRequests()
		.antMatchers("/").permitAll()
//		.antMatchers("/error").permitAll()
//		.antMatchers("/default").permitAll()
		.antMatchers("/addinterests").hasRole("ADMIN")
		.antMatchers("/addslot").hasRole("ADMIN")
		.antMatchers("/getdashboarddetails").hasAnyRole("USER","ADMIN")
		.antMatchers("/showslots").hasAnyRole("USER","ADMIN")
		.antMatchers("/rescheduleshowslots").hasAnyRole("USER","ADMIN")
		.antMatchers("/bookslot").hasAnyRole("USER","ADMIN")
		.antMatchers("/rescheduleslot").hasAnyRole("USER","ADMIN")
		.antMatchers("/deleteslot").hasAnyRole("USER","ADMIN")
		.antMatchers("/getjwtexpiration").hasAnyRole("USER","ADMIN")
		.antMatchers("/getupcomingsessions").hasAnyRole("USER","ADMIN")
		.antMatchers("/prevalidationofjoinsession").hasAnyRole("USER","ADMIN")
		.antMatchers("/initiatemeeting").hasRole("NODEADMIN")
		.antMatchers("/initiatemeetingforsecondcall").hasRole("NODEADMIN")
		.antMatchers("/changestatusofmeetingtostarted").hasAnyRole("USER","ADMIN")
		.antMatchers("/changestatusofmeetingtocompleted").hasAnyRole("USER","ADMIN")
		.antMatchers("/submitfeedbacktous").hasAnyRole("USER","ADMIN")
		.antMatchers("/submitfeedback").hasAnyRole("USER","ADMIN")
		.antMatchers("/getfeedbackhistory").hasAnyRole("USER","ADMIN")
		.antMatchers("/getfeedbackhistoryforconsecutiverequests").hasAnyRole("USER","ADMIN")
		.antMatchers("/getfeedbackhistoryforcustomrange").hasAnyRole("USER","ADMIN")
		.antMatchers("/viewfeedback").hasAnyRole("USER","ADMIN")
		.antMatchers("/getpiechart").hasAnyRole("USER","ADMIN")
		.antMatchers("/getgraphlinechart").hasAnyRole("USER","ADMIN")
		.antMatchers("/getgraphlinechartforyear").hasAnyRole("USER","ADMIN")
		.antMatchers("/getoverallgraphlinechart").hasAnyRole("USER","ADMIN")
		.antMatchers("/getuserdetails").hasAnyRole("USER","ADMIN")
		.antMatchers("/changepassword").hasAnyRole("USER","ADMIN")
		.antMatchers("/getshareprofilelink").hasAnyRole("USER","ADMIN")
		.antMatchers("/admin").hasRole("ADMIN")
		.antMatchers("/hello").hasAnyRole("USER","ADMIN")
		.antMatchers("/signup").permitAll()
		.antMatchers("/shareprofile").permitAll()
		.antMatchers("/verifyuser").permitAll()
		.antMatchers("/login").permitAll()
		.anyRequest().authenticated()
        .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(unauthorizedHandler)/* https://www.devglan.com/spring-security/exception-handling-in-spring-security */
//		
//		.and()
//        .exceptionHandling()
//        .defaultAuthenticationEntryPointFor(
//          loginUrlauthenticationEntryPoint(),
//          new AntPathRequestMatcher("/**"));
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
	
	
	/* https://www.baeldung.com/spring-security-multiple-entry-points */
//	@Bean
//	public AuthenticationEntryPoint loginUrlauthenticationEntryPoint(){
//	   //return unauthorizedHandler;
//		 return new LoginUrlAuthenticationEntryPoint("/default");
//	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
		//return NoOpPasswordEncoder.getInstance();
	}
	
	/* https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt */
	@Bean
	public DaoAuthenticationProvider authProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(getPasswordEncoder());
	    return authProvider;
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
