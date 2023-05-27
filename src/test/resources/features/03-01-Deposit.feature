Feature: Deposit money onto account

  Background:
    Given the system has started up
    Given the database is clear

  Scenario: Create an account for a customer, deposit money then read the balance
    When an account is created with these details
    """
    {
      "balance": 0.0,
      "customerId": "b13fd77c-6243-4b1e-9a8d-b8494f621418",
      "accountType": "SAVINGS",
      "created": "2023-05-25"
    }
    """
    Then 100 is deposited onto this account on 2023-05-26
    When this account is retrieved
    Then below response should be returned
    """
    {
      "balance": 100.0,
      "customerId": "b13fd77c-6243-4b1e-9a8d-b8494f621418",
      "accountType": "SAVINGS",
      "created": "2023-05-25"
    }
    """

  Scenario: Trying to deposit onto an account that doesn't exists
    Then 100 is deposited onto e58ed763-928c-4155-bee9-fdbaaadc15f3 account on 2023-05-26
    Then this error should be returned
    """
    {
      "message": "Couldn't find ACCOUNT with id: e58ed763-928c-4155-bee9-fdbaaadc15f3",
      "statusCode": 404
    }
    """

  Scenario: Trying to deposit a negative balance
    When an account is created with these details
    """
    {
      "balance": 0.0,
      "customerId": "b13fd77c-6243-4b1e-9a8d-b8494f621418",
      "accountType": "SAVINGS",
      "created": "2023-05-25"
    }
    """
    Then -100 is deposited onto this account on 2023-05-26
    Then this error should be returned
    """
    {
      "message": "Field error in object 'depositRequestDTO' on field 'amount' must be greater than or equal to 0.0",
      "statusCode": 400
    }
    """

