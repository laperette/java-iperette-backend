package iperette.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static iperette.security.SecurityConstants.*;

import iperette.domain.Account;
import iperette.services.AccountService;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;
	private AccountService accountSvc;
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	JWTAuthenticationFilter(AuthenticationManager authenticationManager, AccountService accountSvc) {
		this.authenticationManager = authenticationManager;
		this.accountSvc=accountSvc;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		LOGGER.debug("Trying to authenticate");
		try {
			Account creds = new ObjectMapper().readValue(req.getInputStream(), Account.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getEmail(),
					creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		LOGGER.debug("successful authentication");
		String role = auth.getAuthorities().toArray()[0].toString();
		String email = ((User) auth.getPrincipal()).getUsername();
		String token = accountSvc.makeToken(email,role);
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
	}
}