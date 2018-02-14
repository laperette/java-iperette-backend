package iperette.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import iperette.domain.Account;
import iperette.repository.AccountRepository;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import static iperette.security.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	private AccountRepository accountRepo;

	public JWTAuthorizationFilter(AuthenticationManager authManager, AccountRepository accountRepo) {
		super(authManager);
		this.accountRepo = accountRepo;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		LOGGER.debug("authorizing ...");
		String header = req.getHeader(HEADER_STRING);

		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			LOGGER.debug("unauthorized");
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		if(authentication!=null) {
			LOGGER.info(SecurityContextHolder.getContext().getAuthentication().getPrincipal() + " est bien authentifié");
		}
		//SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
		chain.doFilter(req, res);
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + authentication);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			// parse the token.
			String email = Jwts.parser().setSigningKey(SECRET.getBytes())
					.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();
			if (email != null) {
				Account principal = accountRepo.findByEmail(email);
				LOGGER.info(email + " se connecte");
				if (principal != null) {
					return new UsernamePasswordAuthenticationToken(email, null,
							Arrays.asList(principal.getRole().getAuthority()));
				}
				return null;
			}
			return null;
		}
		return null;
	}
}