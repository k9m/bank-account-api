package org.k9m.assignments.bankaccountapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.k9m.assignments.bankaccountapi.api.model.*;
import org.k9m.assignments.bankaccountapi.persistence.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
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
    @Autowired
    private AccountRepository accountRepository;

    private ResponseEntity<AccountDTO> createResponse;

    private ResponseEntity<AccountDTO> retrieveResponse;
    private ResponseEntity<AccountDTO> depositResponse;
    private HttpClientErrorException lastThrownException;

    @Given("the system has started up")
    public void systemStart() {
        assertThat(testClient.healthCheck()).contains("UP");
    }

    @Given("the database is clear")
    public void theDatabaseIsClear() {
        accountRepository.deleteAll();
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
    public void assertCreateResponse(int responseCode, String json) {
        var expectedAccount = objectMapper.readValue(json, AccountDTO.class);
        assertThat(createResponse.getStatusCode().value()).isEqualTo(responseCode);
        assertThat(createResponse.getBody())
                .usingRecursiveComparison()
                .ignoringFields("accountNumber")
                .isEqualTo(expectedAccount);
    }

    @When("^(.*) account is retrieved$")
    @SneakyThrows
    public void retrieveAccount(String accountNumber) {
        try {
            retrieveResponse = "this".equals(accountNumber) ?
                    testClient.getAccount(createResponse.getBody().getAccountNumber()) :
                    testClient.getAccount(UUID.fromString(accountNumber)) ;
        } catch (HttpClientErrorException e) {
            lastThrownException = e;
        }
    }
    @Then("below response should be returned")
    @SneakyThrows
    public void assertRetrievedAccount(String json) {
        var expectedAccount = objectMapper.readValue(json, AccountDTO.class);
        assertThat(retrieveResponse.getBody())
                .usingRecursiveComparison()
                .ignoringFields("accountNumber")
                .isEqualTo(expectedAccount);
    }

    @Then("^(-?\\d+) is deposited onto (.*) account on (.*)$")
    public void deposit(double amount, String accountNumber, String depositDate) {
        try {
            retrieveResponse = "this".equals(accountNumber) ?
                    testClient.getAccount(createResponse.getBody().getAccountNumber()) :
                    testClient.getAccount(UUID.fromString(accountNumber)) ;
            depositResponse = testClient.deposit(
                    retrieveResponse.getBody().getAccountNumber(),
                    new DepositRequestDTO()
                            .amount(amount)
                            .created(LocalDate.parse(depositDate)));
        } catch (HttpClientErrorException e) {
            lastThrownException = e;
        }
    }
    @Then("^(-?\\d+) is withdrawn from (.*) account on (.*)$")
    public void withdraw(double amount, String accountNumber, String depositDate) {
        try {
            retrieveResponse = "this".equals(accountNumber) ?
                    testClient.getAccount(createResponse.getBody().getAccountNumber()) :
                    testClient.getAccount(UUID.fromString(accountNumber)) ;
            depositResponse = testClient.withdraw(
                    retrieveResponse.getBody().getAccountNumber(),
                    new WithdrawRequestDTO()
                            .amount(amount)
                            .created(LocalDate.parse(depositDate)));
        } catch (HttpClientErrorException e) {
            lastThrownException = e;
        }
    }

    @Then("this error should be returned")
    @SneakyThrows
    public void assertException(String json) {
        var expectedErrorObject = objectMapper.readValue(json, ErrorObjectDTO.class);

        assertThat(lastThrownException).isNotNull();
        final ErrorObjectDTO errorObject = objectMapper.readValue(lastThrownException.getResponseBodyAsString(), ErrorObjectDTO.class);
        assertThat(errorObject.getStatusCode()).isEqualTo(expectedErrorObject.getStatusCode());
        assertThat(errorObject.getMessage()).contains(expectedErrorObject.getMessage().split("[ ]+"));
    }
}
