package iperette.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import iperette.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	Account findByEmail(String email);
}
