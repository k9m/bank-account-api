package org.k9m.assignments.bankaccountapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.k9m.assignments.bankaccountapi.api.model.AccountDTO;
import org.k9m.assignments.bankaccountapi.api.model.CreateAccountRequestDTO;
import org.k9m.assignments.bankaccountapi.api.model.ErrorObjectDTO;
import org.k9m.assignments.bankaccountapi.config.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
@ActiveProfiles("test")
public class Steps {

    @Autowired
    private TestClient testClient;
    @Autowired
    private ObjectMapper objectMapper;

    private ResponseEntity<AccountDTO> createResponse;

    private ApplicationException lastThrownException;

    @Given("the system has started up")
    public void systemStart() {
        assertThat(testClient.healthCheck()).contains("UP");
    }

    @When("an account is created with these details")
    @SneakyThrows
    public void createAccount(String json) {
        var createRequest = objectMapper.readValue(json, CreateAccountRequestDTO.class);
        createResponse = testClient.createAccount(createRequest);
        log.info("Account created: {}", createResponse.getBody());
    }

    @Then("^the response should be code (\\d+) with below body$")
    @SneakyThrows
    public void createAccount(int responseCode, String json) {
        var expectedAccount = objectMapper.readValue(json, AccountDTO.class);
        assertThat(createResponse.getStatusCode().value()).isEqualTo(responseCode);
        assertThat(createResponse.getBody())
                .usingRecursiveComparison()
                .ignoringFields("accountNumber")
                .isEqualTo(expectedAccount);
    }

    @When("this account is retrieved, then below response should be returned")
    @SneakyThrows
    public void retrieveLastSavedAccount(String json) {
        var expectedAccount = objectMapper.readValue(json, AccountDTO.class);
        var retrievedAccount = testClient.getAccount(createResponse.getBody().getAccountNumber());
        assertThat(retrievedAccount.getBody())
                .usingRecursiveComparison()
                .ignoringFields("accountNumber")
                .isEqualTo(expectedAccount);
    }

    @When("a wrong account is retrieved this error should be returned")
    @SneakyThrows
    public void lastThrownException(String json) {
        var expectedErrorObject = objectMapper.readValue(json, ErrorObjectDTO.class);
        try {
            var retrievedAccount = testClient.getAccount(UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"));
            log.info("Account created: {}", retrievedAccount.getBody());
        } catch (ApplicationException e) {
            lastThrownException = e;
        }
        assertThat(lastThrownException).isNotNull();
        assertThat(expectedErrorObject.getStatusCode()).isEqualTo(lastThrownException.getStatusCode().value());
        assertThat(expectedErrorObject.getMessage()).isEqualTo(lastThrownException.getMessage());
    }
}
