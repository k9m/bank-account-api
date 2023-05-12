package org.k9m.assignments.bankaccountapi.persistence;

import org.k9m.assignments.bankaccountapi.persistence.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {}