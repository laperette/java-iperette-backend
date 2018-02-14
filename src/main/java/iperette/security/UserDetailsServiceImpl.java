package iperette.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import iperette.domain.Account;
import iperette.repository.AccountRepository;

import java.util.Arrays;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	BCryptPasswordEncoder bcrypt;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account accountExist = accountRepository.findByEmail(email);
		if (accountExist == null) {
			throw new UsernameNotFoundException(email);
		}
		return new User(accountExist.getEmail(), accountExist.getPassword(),
				Arrays.asList(accountExist.getRole().getAuthority()));
	}
}
