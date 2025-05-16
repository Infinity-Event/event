package com.capgemini.event.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.capgemini.event.entities.User;
import com.capgemini.event.repositories.UserRepo;


@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepo userRepo;

	public CustomUserDetailsService() {
	}

	@Autowired
	public CustomUserDetailsService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		User user = userRepo.findByEmailOrName(usernameOrEmail, usernameOrEmail)
			    .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getType()));


		return new org.springframework.security.core.userdetails.User(usernameOrEmail, user.getPassword(), authorities);
	}
}




