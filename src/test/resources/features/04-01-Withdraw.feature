Feature: Withdraw money from account

  Background:
    Given the system has started up
    Given the database is clear

  Scenario: Create an account for a customer, add money then withdraw money then read the balance
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
    Then 90 is withdrawn from this account on 2023-05-26
    When this account is retrieved
    Then below response should be returned by read balance endpoint
    """
    {
      "balance": 10.0,
      "lastTransactionAmount": -90.0,
      "customerId": "b13fd77c-6243-4b1e-9a8d-b8494f621418",
      "accountType": "SAVINGS",
      "created": "2023-05-25"
    }
    """

  Scenario: Trying to withdraw from an account that doesn't exists
    Then 100 is withdrawn from e58ed763-928c-4155-bee9-fdbaaadc15f3 account on 2023-05-26
    Then this error should be returned
    """
    {
      "message": "Couldn't find ACCOUNT with id: e58ed763-928c-4155-bee9-fdbaaadc15f3",
      "statusCode": 404
    }
    """

  Scenario: Trying to withdraw a negative balance
    When an account is created with these details
    """
    {
      "balance": 0.0,
      "customerId": "b13fd77c-6243-4b1e-9a8d-b8494f621418",
      "accountType": "SAVINGS",
      "created": "2023-05-25"
    }
    """
    Then -100 is withdrawn from this account on 2023-05-26
    Then this error should be returned
    """
    {
      "message": "Field error in object 'withdrawRequestDTO' on field 'amount' must be greater than or equal to 0.0",
      "statusCode": 400
    }
    """

  Scenario: Create an account for a customer, add money, then try to withdraw more money then the balance
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
    Then 110 is withdrawn from this account on 2023-05-26
    Then this error should be returned
    """
    {
      "message": "Insufficient funds on ACCOUNT with id: , amount is: 110.0, balance is: 100.0",
      "statusCode": 406
    }
    """

