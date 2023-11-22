Feature: Change type configuration tests
	Scenario: Add change type
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "ChangeType/addChangeType.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/changeType"
		Then Response status code should be 201
		And Save value at Json Path "id" in response, to variable "addedChangeTypeId"
		
	Scenario: Add change type with duplicate name
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "ChangeType/addChangeType.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/changeType"
		Then Response status code should be 409
	
	Scenario: Update change type
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "ChangeType/updateChangeType.json"
		And In request body template, replace "${id}" with value of variable "addedChangeTypeId"
		When PUT request is submitted to "http://localhost:8999/api/configuration/changeType"
		Then Response status code should be 200
		
	Scenario: Get change type
		When GET request is submitted to "http://localhost:8999/api/configuration/changeType/{addedChangeTypeId}"
		Then Response status code should be 200
		
	Scenario: Get all change types
		When GET request is submitted to "http://localhost:8999/api/configuration/changeType"
		Then Response status code should be 200
		
	Scenario: Get non-existing change type
		When GET request is submitted to "http://localhost:8999/api/configuration/changeType/999"
		Then Response status code should be 410
		
	Scenario: Add invalid change type
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "ChangeType/invalidChangeType.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/changeType"
		Then Response status code should be 400
		
	Scenario: Delete change type
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "deleteRecord.json"
		And In request body template, replace "${id}" with value of variable "addedChangeTypeId"
		When DELETE request is submitted to "http://localhost:8999/api/configuration/changeType"
		Then Response status code should be 200