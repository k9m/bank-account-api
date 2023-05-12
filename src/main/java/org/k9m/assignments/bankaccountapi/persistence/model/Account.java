package org.k9m.assignments.bankaccountapi.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.val;
import org.k9m.assignments.bankaccountapi.api.model.AccountDTO;
import org.k9m.assignments.bankaccountapi.api.model.AccountTypeDTO;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "accounts")
@Data
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID accountNumber;

  private Double balance;

  private UUID customerId;

  private String accountType;

  private LocalDate created;

  public AccountDTO toApiModel(){
    val accountDTO = new AccountDTO();
    BeanUtils.copyProperties(this, accountDTO);
    accountDTO.setAccountType(AccountTypeDTO.fromValue(accountType));
    return accountDTO;
  }

}

