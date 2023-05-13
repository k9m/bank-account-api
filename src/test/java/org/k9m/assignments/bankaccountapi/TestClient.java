package org.k9m.assignments.bankaccountapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.k9m.assignments.bankaccountapi.api.model.AccountDTO;
import org.k9m.assignments.bankaccountapi.api.model.CreateAccountRequestDTO;
import org.k9m.assignments.bankaccountapi.api.model.ErrorObjectDTO;
import org.k9m.assignments.bankaccountapi.config.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class TestClient {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public String healthCheck(){
        return restTemplate.getForObject( "/actuator/health", String.class);
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
        //TODO for some reason 404 is not treated as an exception anymore :/
        var r = restTemplate.getForEntity( "/accounts/{id}", String.class, id);
        if(r.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(r.getStatusCode()).body(objectMapper.readValue(r.getBody(), AccountDTO.class));
        }
        else{
            var errorDto = objectMapper.readValue(r.getBody(), ErrorObjectDTO.class);
            throw new ApplicationException(HttpStatus.valueOf(errorDto.getStatusCode()), errorDto.getMessage());
        }
    }

}
