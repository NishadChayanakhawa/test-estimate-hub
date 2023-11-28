Feature: Test type configuration tests
	Scenario: Add test type
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "TestType/addTestType.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/testType"
		Then Response status code should be 201
		And Save value at Json Path "id" in response, to variable "addedTestTypeId"
		
	Scenario: Add test type with duplicate name
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "TestType/addTestType.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/testType"
		Then Response status code should be 409
	
	Scenario: Update test type
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "TestType/updateTestType.json"
		And In request body template, replace "${id}" with value of variable "addedTestTypeId"
		When PUT request is submitted to "http://localhost:8999/api/configuration/testType"
		Then Response status code should be 200
		
	Scenario: Get test type
		When GET request is submitted to "http://localhost:8999/api/configuration/testType/{{addedTestTypeId}}"
		Then Response status code should be 200
		
	Scenario: Get all test types
		When GET request is submitted to "http://localhost:8999/api/configuration/testType"
		Then Response status code should be 200
		
	Scenario: Get non-existing test type
		When GET request is submitted to "http://localhost:8999/api/configuration/testType/999"
		Then Response status code should be 410
		
	Scenario: Add invalid test type
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "TestType/invalidTestType.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/testType"
		Then Response status code should be 400
		
	Scenario: Delete test type
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "deleteRecord.json"
		And In request body template, replace "${id}" with value of variable "addedTestTypeId"
		When DELETE request is submitted to "http://localhost:8999/api/configuration/testType"
		Then Response status code should be 200