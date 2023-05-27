package org.k9m.assignments.bankaccountapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;
import org.k9m.assignments.bankaccountapi.api.model.AccountDTO;
import org.k9m.assignments.bankaccountapi.api.model.CreateAccountRequestDTO;
import org.k9m.assignments.bankaccountapi.api.model.DepositRequestDTO;
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

    @SneakyThrows
    public ResponseEntity<AccountDTO> getAccount(UUID id){
        return restTemplate.getForEntity( "/accounts/{id}", AccountDTO.class, id);
    }

    public ResponseEntity<AccountDTO> deposit(UUID accountNumber, DepositRequestDTO depositRequest){
        return restTemplate.exchange(
                "/accounts/" + accountNumber,
                HttpMethod.POST,
                new HttpEntity<>(depositRequest, new HttpHeaders()),
                AccountDTO.class,
                Collections.emptyMap());
    }

}
