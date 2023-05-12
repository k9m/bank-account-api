package org.k9m.assignments.bankaccountapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.k9m.assignments.bankaccountapi.api.model.AccountDTO;
import org.k9m.assignments.bankaccountapi.api.model.AccountTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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

    @Given("the system has started up")
    public void theSystemHasStartedUp() {
        assertThat(testClient.healthCheck()).contains("UP");
    }

    @When("^an account is created with customerId (.*) and type (.*) the response should be code (\\d+) with below body$")
    @SneakyThrows
    public void theSystemHasStartedUp(String customerId, AccountTypeDTO accountType, int responseCode, String json) {
        val expectedAccount = objectMapper.readValue(json, AccountDTO.class);
        expectedAccount.created(LocalDate.now());

        val response = testClient.createAccount(UUID.fromString(customerId), accountType);
        log.info("{}", response);

        assertThat(response.getStatusCode().value()).isEqualTo(responseCode);
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("accountNumber")
                .isEqualTo(expectedAccount);
    }

}
