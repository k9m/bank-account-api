package org.k9m.assignments.bankaccountapi.api;

import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.k9m.assignments.bankaccountapi.api.model.*;
import org.k9m.assignments.bankaccountapi.persistence.AccountRepository;
import org.k9m.assignments.bankaccountapi.persistence.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountsController implements AccountsApi{

    private final AccountRepository accountRepository;
    private final AuditReader auditReader;

    @Override
    public ResponseEntity<AccountDTO> createAccount(CreateAccountRequestDTO createAccountRequest) {
        return new ResponseEntity<>(accountRepository.save(
                new Account()
                        .setCustomerId(createAccountRequest.getCustomerId())
                        .setAccountType(createAccountRequest.getAccountType().toString())
                        .setBalance(0.0)
                        .setLastTransactionAmount(0.0)
                        .setCreated(createAccountRequest.getCreated()))
                .toApiModel(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AccountDTO> getAccount(UUID id) {
        return ResponseEntity.ok(findAccount(id).toApiModel());
    }

    @Override
    public ResponseEntity<AccountDTO> deposit(UUID id, DepositRequestDTO depositRequestDTO) {
        var account = findAccount(id);
        account.setBalance(account.getBalance() + depositRequestDTO.getAmount());
        account.setLastTransactionAmount(depositRequestDTO.getAmount());
        accountRepository.save(account);

        return ResponseEntity.ok(account.toApiModel());
    }

    @Override
    public ResponseEntity<AccountDTO> withdraw(UUID id, WithdrawRequestDTO withdrawRequestDTO) {
        var account = findAccount(id);
        var newBalance = account.getBalance() - withdrawRequestDTO.getAmount();
        if(newBalance > 0){
            account.setBalance(newBalance);
            account.setLastTransactionAmount(withdrawRequestDTO.getAmount() * -1);
            accountRepository.save(account);

            return ResponseEntity.ok(account.toApiModel());
        }
        else{
            throw new InsufficientFundsException(
                    String.format(
                            "Insufficient funds on ACCOUNT with id: %s, amount is: %.1f, balance is: %.1f",
                            id, withdrawRequestDTO.getAmount(), account.getBalance()));
        }
    }

    @Override
    public ResponseEntity<TransactionsDTO> transactions(UUID id) {
        AuditQuery q = auditReader.createQuery().forRevisionsOfEntity(Account.class, true, true);
        q.add(AuditEntity.id().eq(id));
        q.addOrder(AuditEntity.revisionNumber().desc());
        q.setMaxResults(10);

        List<Account> audit = q.getResultList();
        List<TransactionDTO> transactions = audit.stream().map(a -> new TransactionDTO()
                .balance(a.getBalance())
                .transactionAmount(a.getLastTransactionAmount())).toList();

        return ResponseEntity.ok(new TransactionsDTO()
                .accountNumber(id)
                .transactions(transactions));
    }

    private Account findAccount(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find ACCOUNT with id: " + id));
    }
}
