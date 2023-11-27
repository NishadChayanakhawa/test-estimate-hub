@user-management-tests
Feature: User management tests
	Scenario: Add user
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/addUser.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 201
		And Save value at Json Path "id" in response, to variable "addedUserId"
		
	Scenario: Add user with duplicate username
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/addUserWithDuplicateUsername.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 409
		
	Scenario: Add user with duplicate email
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/addUserWithDuplicateEmail.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 409
		
	Scenario: Update user without password
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/updateWithoutPassword.json"
		And In request body template, replace "${id}" with value of variable "addedUserId"
		When PUT request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 200
		
	Scenario: Update user with password
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/updateWithPassword.json"
		And In request body template, replace "${id}" with value of variable "addedUserId"
		When PUT request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 200
		
	Scenario: Add user with invalid record
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/invalidUser.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 400
		
	Scenario: Get user
		When GET request is submitted to "http://localhost:8999/api/configuration/user/{{addedUserId}}"
		Then Response status code should be 200
		
	Scenario: Get all users
		When GET request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 200
		
	Scenario: Delete user
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "deleteRecord.json"
		And In request body template, replace "${id}" with value of variable "addedUserId"
		When DELETE request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 200
		
	Scenario: Get non-existing user
		When GET request is submitted to "http://localhost:8999/api/configuration/user/999"
		Then Response status code should be 410