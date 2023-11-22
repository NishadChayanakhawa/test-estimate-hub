@controller-tests
Feature: Controller Tests
  
  Scenario Outline: Validate controller - <URL>
    When GET request is submitted to "http://localhost:8999<URL>"
    Then Response status code should be <RESPONSE_CODE>

    Examples: 
      | URL															|	RESPONSE_CODE	|
      |/login														|200						|
			|/home														|500						|
			|/configuration/userManagement		|500						|
			|/configuration/testType					|500						|
			|/configuration/changeType				|500						|
			|/configuration/general						|500						|
			|/configuration/application				|500						|
			|/release													|500						|