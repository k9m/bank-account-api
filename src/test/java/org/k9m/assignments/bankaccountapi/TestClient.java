package org.k9m.assignments.bankaccountapi;

import lombok.val;
import org.k9m.assignments.bankaccountapi.api.model.AccountDTO;
import org.k9m.assignments.bankaccountapi.api.model.AccountTypeDTO;
import org.k9m.assignments.bankaccountapi.api.model.CreateAccountRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class TestClient {

    @Autowired
    protected TestRestTemplate restTemplate;

    public String healthCheck(){
        return restTemplate.getForObject( "/actuator/health", String.class);
    }

    public ResponseEntity<AccountDTO> createAccount(UUID customerId, AccountTypeDTO accountType){
        val createAccount = new CreateAccountRequestDTO()
                .customerId(customerId)
                .accountType(accountType);

        return restTemplate.exchange(
                "/accounts",
                HttpMethod.PUT,
                new HttpEntity<>(createAccount, new HttpHeaders()),
                AccountDTO.class,
                Collections.emptyMap());
    }

}
