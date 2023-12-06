var estimationFormProcessing = (function() {
	//csrf token
	var csrfToken;
	//api path
	const API_PATH = "/api/useCase";
	//record name
	const RECORD_NAME = "Use Case";
	
	//common xpaths
	var xpaths = {
		"record": {
			"template": "#recordListTemplate",
			"table": {
				"table": "table#recordTable",
				"body": "tbody#recordTableBody",
				"header": "tr#recordTableHeader"
			}
		},
		"modals": {
			"delete": "div#deleteConfirmationModal",
			"put": "div#putRecordModal"
		},
		"buttons": {
			"confirmDelete": "button#confirmDeleteOperation",
			"save": "button#saveRecord",
			"edit": "button[id^='editRecordButton_']",
			"delete": "button[id^='deleteRecordButton_']"
		},
		"forms": {
			"put": "form#putRecordForm",
			"delete" : "form#deleteRecordForm"
		},
		"elements" : {
			"deleteRecordId" : "input#deleteRecordId",
			"deleteRecordIdentifierDisplay": "span#deleteRecordIdentifierDisplay"
		}
	};
	
	var calculateEstimate = function(event) {
		event.preventDefault();
		logging.log("Calculating estimation");
		var calculateEstimationRequest={
			"id" : $("#changeId").val()
		};
		$("#calculateEstimatesButton").indicateButtonProcessing();
		apiHandling.processRequest("post", "/api/change/estimate", csrfToken,calculateEstimationRequest)
			.done(data => calculateEstimate_success(data))
			.catch(error => calculateEstimate_failure(error));
	};
	
	var calculateEstimate_success=function(change) {
		logging.log(change);
		$("#changeEstimateBody").html("");
		$("#estimateTemplate").tmpl(change.estimationSummaryRecords).appendTo("#changeEstimateBody");
		$("td#designEfforts").html(change.designEfforts);
		$("td#executionEfforts").html(change.executionEfforts);
		$("td#planningEfforts").html(change.planningEfforts);
		$("td#preparationEfforts").html(change.preparationEfforts);
		$("td#managementEfforts").html(change.managementEfforts);
		$("th#totalEfforts").html(change.totalEfforts);
		
		logging.log(change.requirements);
		$("#accordionEstimationSummary").html("");
		$("#estimateDetailsTemplate").tmpl(change.requirements).appendTo("#accordionEstimationSummary");
		$("#calculateEstimatesButton").indicateButtonProcessingCompleted();
		$("#estimationReviewModal").modal('show');
	};
	
	var calculateEstimate_failure=function(error) {
		logging.log(error);
		$("#calculateEstimatesButton").indicateButtonProcessingCompleted();
	};
	
	/**
	 * 
	 * 
	 */
	
	
	
	var showDeleteConfimationModal=function(event) {
		event.preventDefault();
		logging.log("Showing delete modal");
		var recordId=$(this).attr('id').split("_")[1];
		logging.log("Record id: " + recordId);
		$(xpaths.elements.deleteRecordId).val(recordId);
		var recordIdentifier="";
		$("td[id^='recordIdentifier_" + recordId + "_']").each(function() {
			logging.log($(this).html());
			if(recordIdentifier!="") {
				recordIdentifier=recordIdentifier + " - ";				
			}
			recordIdentifier=recordIdentifier + $(this).html();
		});
		$(xpaths.elements.deleteRecordIdentifierDisplay).html(recordIdentifier);
		$(xpaths.modals.delete).modal('show');
	};
	
	var deleteRecord=function(event) {
		event.preventDefault();
		logging.log("Deleting record");
		$(xpaths.buttons.confirmDelete).indicateButtonProcessing();
		var deleteRequestBody=$(xpaths.forms.delete).serializeObject();
		logging.log("Delete request: ")
		logging.log(deleteRequestBody)
		apiHandling.processRequest("delete", API_PATH, csrfToken,deleteRequestBody)
			.done(data => deleteRecord_success(data))
			.catch(error => deleteRecord_failure(error));
	};
	
	var deleteRecord_success=function(data) {
		logging.log(data);
		var deletedRecordIdentifier=$(xpaths.elements.deleteRecordIdentifierDisplay).html();
		$(xpaths.modals.delete).modal('hide');
		toastr.success(RECORD_NAME + " '" + deletedRecordIdentifier + "' deleted successfully");
		$(xpaths.buttons.confirmDelete).indicateButtonProcessingCompleted();
		loadUseCases();
	};
	
	var deleteRecord_failure=function(data) {
		logging.log(data);
		$(xpaths.buttons.confirmDelete).indicateButtonProcessingCompleted();
		processUnexpectedError(error);
	};
	
	/**
	 *******************Edit Record***************************
	 */

	var showEditModal = function(event) {
		event.preventDefault();
		logging.log("Showing edit modal");
		//indicate that request in progress
		$(this).indicateButtonProcessing();
		var recordId=$(this).attr('id').split("_")[1];
		logging.log("Record id: " + recordId);
		apiHandling.processRequest("get", API_PATH + "/" + recordId, csrfToken)
			.done(data => showEditModal_success(data,$(this)))
			.catch(error => showEditModal_failure(error,$(this)));
	};
	
	var showEditModal_success=function(record,editButton) {
		logging.log(record);
		editButton.indicateButtonProcessingCompleted();
		teh.updateEditForm("#putRecordForm",record);
		$("select#businessFunctionalityId").val(record.businessFunctionalityId);
		$("select#testConfigurationComplexityCode").val(record.testConfigurationComplexityCode);
		$("select#testDataSetupComplexityCode").val(record.testDataSetupComplexityCode);
		$("select#testTransactionComplexityCode").val(record.testTransactionComplexityCode);
		$("select#testValidationComplexityCode").val(record.testValidationComplexityCode);
		$.each(record.applicableTestTypes,function(i,testType) {
			logging.log("Processing test type" + i);
			$("input#applicableTestTypes" + testType.id).attr('checked',true);
		});
		$("#putRecordModal").modal('show');
	};
	
	var showEditModal_failure=function(error,editButton) {
		logging.log(error);
		editButton.indicateButtonProcessingCompleted();
		processUnexpectedError(error);
	};
	
	/**
	 * 
	 */

	var showAddModal = function() {
		var requirementId = $(this).attr('id').split("_")[1];
		$("input#id").val("");
		$("input#requirementId").val(requirementId);
		$("#putRecordModal").modal('show');
	};

	var saveUseCase = function(event) {
		event.preventDefault();
		logging.log("Saving use case");
		var putUseCaseRequest = $("form#putRecordForm").serializeObject();
		logging.log(putUseCaseRequest);

		var applicableTestTypes = putUseCaseRequest.applicableTestTypes;
		putUseCaseRequest.applicableTestTypes = [];
		if (typeof applicableTestTypes == 'undefined') {
			toastr.error("Please select at least one test type");
		} else {
			$(this).indicateButtonProcessing();
			
			if ($.isArray(applicableTestTypes)) {
				$.each(applicableTestTypes, function(i, testType) {
					logging.log("Processing " + i + ":" + testType);
					putUseCaseRequest.applicableTestTypes.push({
						"id": testType
					});
				});
			} else {
				putUseCaseRequest.applicableTestTypes.push({
					"id": applicableTestTypes
				});
			}
			logging.log(applicableTestTypes);
			apiHandling.processRequest("put", API_PATH, csrfToken, putUseCaseRequest)
				.done(data => saveUseCase_success(data,$(this)))
				.catch(error => saveUseCase_failure(error,$(this)));
		}
	};

	var saveUseCase_success = function(useCase,putButton) {
		logging.log(useCase);
		putButton.indicateButtonProcessingCompleted();	
		$("#putRecordModal").modal('hide');
		//show success message
		teh.showSaveSuccessMessage('Use Case', useCase.summary);
		loadUseCases();
	};

	var saveUseCase_failure = function(error,putButton) {
		logging.log(error);
		putButton.indicateButtonProcessingCompleted();
		processUnexpectedError(error);
	};

	var loadUseCases = function() {
		$("table[id^='recordTable_']").each(function() {
			var requirementId = $(this).attr('id').split("_")[1];
			logging.log("Requirement ID: " + requirementId);
			$("#accordianButton_" + requirementId).indicateButtonProcessing();

			apiHandling.processRequest("get", API_PATH + "/byRequirement/" + requirementId, csrfToken)
				.done(data => loadUseCase_success(data,requirementId))
				.catch(error => loadUseCase_failure(error,requirementId));
		});
	};

	var loadUseCase_success = function(useCases,requirementId) {
		logging.log(useCases);
		$("#accordianButton_" + requirementId).indicateButtonProcessingCompleted();
		populateDataTable(useCases, "table#recordTable_" + requirementId, "tbody#recordTableBody_" + requirementId, "#recordListTemplate");
	};

	var loadUseCase_failure = function(error,requirementId) {
		logging.log(error);
		$("#accordianButton_" + requirementId).indicateButtonProcessingCompleted();
		processUnexpectedError(error);
	};

	var init = function() {
		//enable or disable logging
		logging.setLoggingSwitch(teh.shouldEnableLogging());
		//get csrf token
		csrfToken = $("input#csrf").val();
		//set toastr options
		toastr.options = getToastrOptions();
		$("button[id^='addRecordButton_']").click(showAddModal);
		$("form#putRecordForm").submit(saveUseCase);
		$("#putRecordModal").on("hidden.bs.modal", function() {
			logging.log('dismissed');
			teh.onModalDismiss("#putRecordForm");
			$("input[id^='applicableTestTypes'").each(function() {
				$(this).attr('checked',false);
			});
		});
		$("tbody[id^='recordTableBody_']").on('click',"button[id^='editRecordButton_']",showEditModal);
		//bind delete action
		$("tbody[id^='recordTableBody_']").on("click", xpaths.buttons.delete, showDeleteConfimationModal);
		
		//bind confirm delete action
		$(xpaths.buttons.confirmDelete).click(deleteRecord);
		loadUseCases();
		
		$("#calculateEstimatesButton").click(calculateEstimate);

		logging.log("Estimation form processing page initialized!!!");
	};

	return {
		init: init
	}
})();