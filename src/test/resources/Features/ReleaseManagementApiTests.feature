Feature: Release management tests
	Scenario: Add a release
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Release/addRelease.json"
		When PUT request is submitted to "http://localhost:8999/api/release"
		Then Response status code should be 201
		And Save value at Json Path "id" in response, to variable "addedReleaseId"
		
	Scenario: Add duplicate release
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Release/addRelease.json"
		When PUT request is submitted to "http://localhost:8999/api/release"
		Then Response status code should be 409
		
	Scenario: Add invalid release
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Release/invalidRelease.json"
		When PUT request is submitted to "http://localhost:8999/api/release"
		Then Response status code should be 400
		
	Scenario: Update release
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Release/updateRelease.json"
		And In request body template, replace "\"${id}\"" with value of variable "addedReleaseId"
		When PUT request is submitted to "http://localhost:8999/api/release"
		Then Response status code should be 200
		
	Scenario: Get release
		When GET request is submitted to "http://localhost:8999/api/release/{addedReleaseId}"
		Then Response status code should be 200
		
	Scenario: Get releases
		When GET request is submitted to "http://localhost:8999/api/release"
		Then Response status code should be 200
		
	Scenario: Delete release
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "deleteRecord.json"
		And In request body template, replace "${id}" with value of variable "addedReleaseId"
		When DELETE request is submitted to "http://localhost:8999/api/release"
		Then Response status code should be 200