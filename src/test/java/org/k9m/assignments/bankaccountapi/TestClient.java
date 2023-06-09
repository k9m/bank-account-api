package org.k9m.assignments.bankaccountapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.k9m.assignments.bankaccountapi.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Collections;
import java.util.UUID;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class TestClient {

    @LocalServerPort
    @Getter
    private int serverPort;

    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ObjectMapper objectMapper;

    public String healthCheck(){
        return restTemplate.getForObject( "/actuator/health", String.class);
    }

    @PostConstruct
    private void init(){
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:" + serverPort));
    }

    public ResponseEntity<AccountDTO> createAccount(CreateAccountRequestDTO createAccount){
        return restTemplate.exchange(
                "/accounts",
                HttpMethod.PUT,
                new HttpEntity<>(createAccount, new HttpHeaders()),
                AccountDTO.class,
                Collections.emptyMap());
    }

    public ResponseEntity<AccountDTO> getAccount(UUID id){
        return restTemplate.getForEntity( "/accounts/{id}", AccountDTO.class, id);
    }

    public ResponseEntity<AccountDTO> deposit(UUID accountNumber, DepositRequestDTO depositRequest){
        return restTemplate.exchange(
                "/accounts/" + accountNumber + "/deposit",
                HttpMethod.POST,
                new HttpEntity<>(depositRequest, new HttpHeaders()),
                AccountDTO.class,
                Collections.emptyMap());
    }
    public ResponseEntity<AccountDTO> withdraw(UUID accountNumber, WithdrawRequestDTO withdrawRequest){
        return restTemplate.exchange(
                "/accounts/" + accountNumber + "/withdraw",
                HttpMethod.POST,
                new HttpEntity<>(withdrawRequest, new HttpHeaders()),
                AccountDTO.class,
                Collections.emptyMap());
    }

    public ResponseEntity<TransactionsDTO> getTransactions(UUID id){
        return restTemplate.getForEntity( "/accounts/{id}/transactions", TransactionsDTO.class, id);
    }

}
