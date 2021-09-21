package com.convobee.authentication;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.convobee.data.entity.Users;
import com.convobee.data.repository.UsersRepo;

@Service
public class AuthUserDetailsService implements UserDetailsService{
	@Autowired
	UsersRepo userRepository;

	@Override
	public AuthUserDetails loadUserByUsername(String mailid) throws UsernameNotFoundException {
		Optional<Users> user = userRepository.findByMailid(mailid);
		return user.map(AuthUserDetails::new).get();
	}
}
