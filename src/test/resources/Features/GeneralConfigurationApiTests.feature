Feature: General configuration tests
	Scenario: Get blank general configuration
		When GET request is submitted to "http://localhost:8999/api/configuration/general"
		Then Response status code should be 410
		
	Scenario: Add general configuration
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "GeneralConfiguration/newGeneralConfiguration.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/general"
		Then Response status code should be 200
		
	Scenario: Add invalid general configuration
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "GeneralConfiguration/invalidGeneralConfiguration.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/general"
		Then Response status code should be 400
		
	Scenario: Add invalid general configuration with negative value
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "GeneralConfiguration/invalidNegativeGeneralConfiguration.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/general"
		Then Response status code should be 400
		
	Scenario: Get general configuration
		When GET request is submitted to "http://localhost:8999/api/configuration/general"
		Then Response status code should be 200