Feature: Read Balance

  Background:
    Given the system has started up
    Given the database is clear

  Scenario: Create an account for a customer then read the balance
    When an account is created with these details
    """
    {
      "balance": 0.0,
      "customerId": "b13fd77c-6243-4b1e-9a8d-b8494f621418",
      "accountType": "SAVINGS",
      "created": "2023-05-25"
    }
    """
    When this account is retrieved
    Then below response should be returned
    """
    {
      "balance": 0.0,
      "customerId": "b13fd77c-6243-4b1e-9a8d-b8494f621418",
      "accountType": "SAVINGS",
      "created": "2023-05-25"
    }
    """

  Scenario: An account that doesn't exist is getting retrieved
    When e58ed763-928c-4155-bee9-fdbaaadc15f3 account is retrieved
    Then this error should be returned
    """
    {
      "message": "Couldn't find ACCOUNT with id: e58ed763-928c-4155-bee9-fdbaaadc15f3",
      "statusCode": 404
    }
    """
