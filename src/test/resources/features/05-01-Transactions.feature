Feature: List last transactions

  Background:
    Given the system has started up
    Given the database is clear

  Scenario: Create an account for a customer, make some transactions
    When an account is created with these details
    """
    {
      "balance": 0.0,
      "customerId": "b13fd77c-6243-4b1e-9a8d-b8494f621418",
      "accountType": "SAVINGS",
      "created": "2023-05-25"
    }
    """
    Then 10 is deposited onto this account on 2023-05-26
    Then 10 is deposited onto this account on 2023-05-26
    Then 10 is deposited onto this account on 2023-05-26
    Then 10 is deposited onto this account on 2023-05-26
    Then 10 is deposited onto this account on 2023-05-26
    Then 10 is deposited onto this account on 2023-05-26
    Then 10 is deposited onto this account on 2023-05-26
    Then 10 is deposited onto this account on 2023-05-26
    Then 10 is deposited onto this account on 2023-05-26
    Then 10 is deposited onto this account on 2023-05-26
    Then 10 is deposited onto this account on 2023-05-26
    Then 90 is withdrawn from this account on 2023-05-26

    When the last transactions are retrieved for this account
    Then below response should be returned by list transactions endpoint
    """
    {
      "transactions": [
        {
          "balance": 20.0,
          "transactionAmount": -90.0
        },
        {
          "balance": 110.0,
          "transactionAmount": 10.0
        },
        {
          "balance": 100.0,
          "transactionAmount": 10.0
        },
        {
          "balance": 90.0,
          "transactionAmount": 10.0
        },
        {
          "balance": 80.0,
          "transactionAmount": 10.0
        },
        {
          "balance": 70.0,
          "transactionAmount": 10.0
        },
        {
          "balance": 60.0,
          "transactionAmount": 10.0
        },
        {
          "balance": 50.0,
          "transactionAmount": 10.0
        },
        {
          "balance": 40.0,
          "transactionAmount": 10.0
        },
        {
          "balance": 30.0,
          "transactionAmount": 10.0
        }
      ]
    }
    """
