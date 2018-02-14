package iperette.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
	ADMIN,USER;

	public GrantedAuthority getAuthority() {
		return new SimpleGrantedAuthority(this.name());
	}
}
