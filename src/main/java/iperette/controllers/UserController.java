package iperette.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iperette.domain.Account;
import iperette.domain.Role;
import iperette.repository.AccountRepository;
import iperette.services.AccountService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private AccountService accountSvc;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<Account> getUsers() {
		return accountRepo.findAll();
	}

	@GetMapping(path = "/me")
	public Account getMe() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return accountRepo.findByEmail(email);
	}

	@GetMapping(path = "context")
	public Authentication getContext() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@PostMapping("/sign-up")
	public Map<String,String> signUp(@RequestBody Account user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole(Role.USER);
		if (accountRepo.findByEmail(user.getEmail())!=null) {
			return null;
		};
		accountRepo.save(user);
		Map<String,String> respToken = new HashMap<>();
		respToken.put("token", accountSvc.makeToken(user.getEmail(),user.getRole().toString()));
		return respToken;
	}

}