package com.convobee;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.data.entity.Users;
import com.convobee.data.repository.UsersRepo;

@SpringBootApplication
@RestController
public class ConvobeeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConvobeeAppApplication.class, args);
	}
	
	@Autowired
	UsersRepo userRepo;
	
	@GetMapping("/save")
	public String saveValues(){
		Users user = new Users();
		user.setUsername("Vinoth");
		user.setNickname("Vino");
		user.setPassword("pass");
		user.setMailid("v@gmail.com");
		user.setRole("ROLE_ADMIN");
		user.setCountry("India");
		user.setCity("Chennai");
		user.setEducationlevel("UG");
		user.setProficiencylevel(4);
		user.setSignuptype("Google");
		user.setReportcount(0);
		user.setIsfeedbackgiven(true);
		user.setCreatedat(Timestamp.valueOf("1970-01-01 00:00:01"));
		user.setModifiedat(Timestamp.valueOf("1970-01-01 00:00:01"));
		userRepo.save(user);
		return "Done buddy";
	}

}
