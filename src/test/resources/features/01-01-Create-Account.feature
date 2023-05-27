Feature: Create Account

  Background:
    Given the system has started up
    Given the database is clear

  Scenario: Create an account for a customer
    When an account is created with these details
    """
    {
      "customerId": "b13fd77c-6243-4b1e-9a8d-b8494f621418",
      "accountType": "SAVINGS",
      "created": "2023-05-25"
    }
    """
    Then the response should be code 201 with below body
    """
    {
      "balance": 0.0,
      "customerId": "b13fd77c-6243-4b1e-9a8d-b8494f621418",
      "accountType": "SAVINGS",
      "created": "2023-05-25"
    }
    """
