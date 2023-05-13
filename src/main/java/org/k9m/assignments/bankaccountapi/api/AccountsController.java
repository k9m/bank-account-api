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

import java.util.UUID;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountsController implements AccountsApi{

    private final AccountRepository accountRepository;

    @Override
    public ResponseEntity<AccountDTO> createAccount(CreateAccountRequestDTO createAccountRequest) {
        return new ResponseEntity<>(accountRepository.save(
                new Account()
                        .setCustomerId(createAccountRequest.getCustomerId())
                        .setAccountType(createAccountRequest.getAccountType().toString())
                        .setBalance(0.0)
                        .setCreated(createAccountRequest.getCreated()))
                .toApiModel(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AccountDTO> getAccount(UUID id) {
        return ResponseEntity.ok(accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find ACCOUNT with id: " + id)).toApiModel());
    }
}
