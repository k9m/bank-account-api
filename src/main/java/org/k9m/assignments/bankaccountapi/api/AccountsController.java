package org.k9m.assignments.bankaccountapi.api;

import lombok.RequiredArgsConstructor;
import org.k9m.assignments.bankaccountapi.api.model.AccountDTO;
import org.k9m.assignments.bankaccountapi.api.model.CreateAccountRequestDTO;
import org.k9m.assignments.bankaccountapi.persistence.AccountRepository;
import org.k9m.assignments.bankaccountapi.persistence.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountsController implements AccountsApi{

    private final AccountRepository accountRepository;

    @Override
    public ResponseEntity<AccountDTO> createAccount(CreateAccountRequestDTO createAccountRequestDTO) {
        return new ResponseEntity<>(accountRepository.save(
                new Account()
                        .setCustomerId(createAccountRequestDTO.getCustomerId())
                        .setAccountType(createAccountRequestDTO.getAccountType().toString())
                        .setBalance(0.0)
                        .setCreated(LocalDate.now()))
                .toApiModel(), HttpStatus.CREATED);
    }
}
