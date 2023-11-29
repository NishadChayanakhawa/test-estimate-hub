Feature: Application configuration tests
	Scenario Outline: Add application configuration record <recordNumber>
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/PreRequisites/ApplicationConfiguration/applicationConfiguration<recordNumber>.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/application"
		Then Response status code should be 201
		And Save value at Json Path "id" in response, to variable "applicationConfigurationId<recordNumber>"
		
		Examples: 
      | recordNumber	|
      |1							|
      |2							|
      |3							|
      
	Scenario Outline: Add test type record <recordNumber>
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/PreRequisites/TestType/testType<recordNumber>.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/testType"
		Then Response status code should be 201
		And Save value at Json Path "id" in response, to variable "testTypeId<recordNumber>"
		
		Examples: 
      | recordNumber	|
      |1							|
      |2							|
      |3							|
      
	Scenario: Add change type
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/PreRequisites/ChangeType/changeType.json"
		When PUT request is submitted to "http://localhost:8999/api/configuration/changeType"
		Then Response status code should be 201
		And Save value at Json Path "id" in response, to variable "changeTypeId"
		
	Scenario: Add release
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/PreRequisites/Release/release.json"
		When PUT request is submitted to "http://localhost:8999/api/release"
		Then Response status code should be 201
		And Save value at Json Path "id" in response, to variable "releaseId"
		
	Scenario: Add change
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/addChange.json"
		When PUT request is submitted to "http://localhost:8999/api/change"
		Then Response status code should be 201
		And Save value at Json Path "id" in response, to variable "changeId"
		
	Scenario: Get change with depth 0
		When GET request is submitted to "http://localhost:8999/api/change/{{changeId}}"
		Then Response status code should be 200
		
	Scenario: Add duplicate change
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/addChange.json"
		When PUT request is submitted to "http://localhost:8999/api/change"
		Then Response status code should be 409
		
	Scenario: Add change with end date before start date
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/addChangeEndDateBeforeStart.json"
		When PUT request is submitted to "http://localhost:8999/api/change"
		Then Response status code should be 400
		
	Scenario: Add change with invalid end date
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/addChangeInvalidEndDate.json"
		When PUT request is submitted to "http://localhost:8999/api/change"
		Then Response status code should be 400
		
	Scenario: Add change with invalid start date
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/addChangeInvalidStartDate.json"
		When PUT request is submitted to "http://localhost:8999/api/change"
		Then Response status code should be 400
		
	Scenario: Add change with duplicate requirement identifier
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/addChangeWithDuplicateRequirement.json"
		When PUT request is submitted to "http://localhost:8999/api/change"
		Then Response status code should be 409
		
	Scenario: Add change with duplicate impact
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/addChangeWithDuplicateImpact.json"
		When PUT request is submitted to "http://localhost:8999/api/change"
		Then Response status code should be 409
		
	Scenario: Get change with depth 1
		When GET request is submitted to "http://localhost:8999/api/change/{{changeId}}?depth=1"
		Then Response status code should be 200
		And Save value at Json Path "requirements[1].id" in response, to variable "requirementId2"
		
	Scenario: Update change
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/updateChange.json"
		When PUT request is submitted to "http://localhost:8999/api/change"
		Then Response status code should be 200
		
	Scenario: Get change after update
		When GET request is submitted to "http://localhost:8999/api/change/{{changeId}}"
		Then Response status code should be 200
		
	Scenario: Get all changes
		When GET request is submitted to "http://localhost:8999/api/change"
		Then Response status code should be 200
		
	Scenario: Add use case
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "UseCase/addUseCase.json"
		When PUT request is submitted to "http://localhost:8999/api/useCase"
		Then Response status code should be 201
		And Save value at Json Path "id" in response, to variable "useCaseId"
		
	Scenario: Update use case
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "UseCase/updateUseCase.json"
		When PUT request is submitted to "http://localhost:8999/api/useCase"
		Then Response status code should be 200
		
	Scenario: Update change after adding use case
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "Change/updateChangeAfterAddingCase.json"
		When PUT request is submitted to "http://localhost:8999/api/change"
		Then Response status code should be 200
		
	Scenario: Get change with depth 2
		When GET request is submitted to "http://localhost:8999/api/change/{{changeId}}?depth=2"
		Then Response status code should be 200
		
	Scenario: Get change with depth 3
		When GET request is submitted to "http://localhost:8999/api/change/{{changeId}}?depth=3"
		Then Response status code should be 200
		
	Scenario: Delete use case
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "deleteRecord.json"
		And In request body template, replace "${id}" with value of variable "useCaseId"
		When DELETE request is submitted to "http://localhost:8999/api/useCase"
		Then Response status code should be 200
		
	Scenario: Get change with depth 3 after deleting use case
		When GET request is submitted to "http://localhost:8999/api/change/{{changeId}}?depth=3"
		Then Response status code should be 200
		
	Scenario: Delete change
		Given In request header, set "Content-Type" to "application/json"
		And Request body template is loaded from file "deleteRecord.json"
		And In request body template, replace "${id}" with value of variable "changeId"
		When DELETE request is submitted to "http://localhost:8999/api/change"
		Then Response status code should be 200
		
	Scenario: Get not existing change
		When GET request is submitted to "http://localhost:8999/api/change/{{changeId}}"
		Then Response status code should be 410