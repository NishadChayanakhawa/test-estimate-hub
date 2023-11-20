Feature: User management tests
	Scenario: Add user
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/addUser.json"
		And In request body template, replace "${username}" with "john"
		And In request body template, replace "${password}" with "johnssecret"
		And In request body template, replace "${firstName}" with "John"
		And In request body template, replace "${email}" with "john@company.com"
		When PUT request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 201
		And Save value at Json Path "id" in response, to variable "addedUserId"
		
	Scenario: Add user with duplicate username
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/addUser.json"
		And In request body template, replace "${username}" with "john"
		And In request body template, replace "${password}" with "johnssecret"
		And In request body template, replace "${firstName}" with "JohnD"
		And In request body template, replace "${email}" with "johnD@company.com"
		When PUT request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 409
		
	Scenario: Add user with duplicate email
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/addUser.json"
		And In request body template, replace "${username}" with "johnd"
		And In request body template, replace "${password}" with "johnssecret"
		And In request body template, replace "${firstName}" with "JohnD"
		And In request body template, replace "${email}" with "john@company.com"
		When PUT request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 409
		
	Scenario: Update user without password
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/updateWithoutPassword.json"
		And In request body template, replace "\"${id}\"" with value of variable "addedUserId"
		And In request body template, replace "${username}" with "jane"
		And In request body template, replace "${firstName}" with "Jane"
		And In request body template, replace "${email}" with "jane@company.com"
		When PUT request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 200
		
	Scenario: Update user with password
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/updateWithPassword.json"
		And In request body template, replace "\"${id}\"" with value of variable "addedUserId"
		And In request body template, replace "${username}" with "jane"
		And In request body template, replace "${password}" with "janessecret"
		And In request body template, replace "${firstName}" with "Jane"
		And In request body template, replace "${email}" with "jane@company.com"
		When PUT request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 200
		
	Scenario: Add user with invalid record
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/invalidUser.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 400
		
	Scenario: Get user
		When GET request is submitted to "http://localhost:8999/api/configuration/user/1"
		Then Response status code should be 200
		
	Scenario: Get all users
		When GET request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 200
		
	Scenario: Delete user
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "User/deleteUser.json"
		And In request body template, replace "\"${id}\"" with value of variable "addedUserId"
		When DELETE request is submitted to "http://localhost:8999/api/configuration/user"
		Then Response status code should be 200
		
	Scenario: Get non-existing user
		When GET request is submitted to "http://localhost:8999/api/configuration/user/999"
		Then Response status code should be 410