var changeProcessing = (function() {
	//csrf token
	var csrfToken;
	//api path
	const API_PATH = "/api/change";
	//record name
	const RECORD_NAME = "Change";
	//requirement record id
	var requirementRecordId;

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
	
	/**
	 * Add/remove requirement form
	 */
	
	var addRequirementForm=function(event) {
		event.preventDefault();
		logging.log("Adding requirement form!!!");
		requirementRecordId=requirementRecordId+1;
		var requirementRecordAddFormRequest={
			"id" : requirementRecordId
		};
		$("#requirementFormTemplate").tmpl(requirementRecordAddFormRequest).appendTo("#requirementFormsTableBody");
	};
	
	var deleteRequirementForm=function(event) {
		event.preventDefault();
		logging.log("Deleting requirement form!!!");
		var requirementFormId=$(this).attr('id').split("_")[1];
		logging.log("Requirement id:" + requirementFormId);
		$("tr#requirementForm_" + requirementFormId).remove();
	};
	
	/**
	 *******************Delete Record************************* 
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
		loadRecords();
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
		apiHandling.processRequest("get", API_PATH + "/" + recordId + "?depth=1", csrfToken)
			.done(data => showEditModal_success(data,$(this)))
			.catch(error => showEditModal_failure(error,$(this)));
	};
	
	var showEditModal_success=function(record,editButton) {
		logging.log(record);
		editButton.indicateButtonProcessingCompleted();
		teh.updateEditForm(xpaths.forms.put,record);
		$.each(record.impactedArea,function(i,applicationConfiguration) {
			logging.log("Impacted area: " + i + ":" + applicationConfiguration.id);
			$("#impactedArea > option[value=" + applicationConfiguration.id + "]").attr("selected",'selected');
		});
		$.each(record.requirements,function(i,requirement) {
			logging.log("Requirement: " + i + ":" + requirement.id);
			if(i>0) {
				$("#addRequirementButton").click();
			}
			$("#id_" + (i+1)).val(requirement.id);
			$("#changeId_" + (i+1)).val(requirement.changeId);
			$("#identifier_" + (i+1)).val(requirement.identifier);
			$("#summary_" + (i+1)).val(requirement.summary);
		});
		$(xpaths.modals.put).modal('show');
	};
	
	var showEditModal_failure=function(error,editButton) {
		logging.log(error);
		editButton.indicateButtonProcessingCompleted();
		processUnexpectedError(error);
	};

	/**
	 *******************Save Record****************************
	 */
	var saveRecord = function(event) {
		event.preventDefault();
		//indicate that saving is in progress
		$(xpaths.forms.put).indicateButtonProcessing();
		logging.log("Saving " + RECORD_NAME);
		logging.log("Record to save is:");
		//get save request body
		var saveRequestBody = $(xpaths.forms.put).serializeObject();
		//custom code
		saveRequestBody.impactedArea=[];
		$.each($("#impactedArea").val(),function(i,value) {
			logging.log(i + ":" + value);
			var impact={
				"id" : value
			};
			saveRequestBody.impactedArea.push(impact);
		});
		saveRequestBody.requirements=[];
		$("tr[id^='requirementForm_']").each(function() {
			var requirement=$(this).serializeObject();
			saveRequestBody.requirements.push(requirement);
		});
		
		saveRequestBody.id=saveRequestBody.id[0];
		saveRequestBody.identifier=saveRequestBody.identifier[0];
		saveRequestBody.summary=saveRequestBody.summary[0];
		
		
		logging.log(saveRequestBody);
		apiHandling.processRequest("put", API_PATH, csrfToken, saveRequestBody)
			.done(data => saveRecord_success(data))
			.catch(error => saveRecord_failure(error));
	};

	var saveRecord_success = function(savedRecord) {
		logging.log("Saved Record: ");
		logging.log(savedRecord);
		//indicate that saving is completed
		$(xpaths.forms.put).indicateButtonProcessingCompleted();
		//hide modal for put record
		$(xpaths.modals.put).modal('hide');
		//show success message
		teh.showSaveSuccessMessage(RECORD_NAME, savedRecord.identifier);
		loadRecords();
	};

	var saveRecord_failure = function(error) {
		logging.log("Error: ");
		logging.log(error);
		//process errors to mark invalid fields
		teh.processSaveApiErrors(error.responseJSON.details);
		//indicate processing is completed
		$(xpaths.forms.put).indicateButtonProcessingCompleted();
	};

	/**
	 *******************Load Records*************************** 
	 */

	/**
	 * loadRecords
	 * Loads existing records
	 */
	var loadRecords = function() {
		logging.log("Loading existing " + RECORD_NAME + " records!!!");
		//indicate that loading process has started
		$(xpaths.record.table.body).indicateTableLoading($(xpaths.record.table.header).children().length);
		//call 'GET' method for record api
		apiHandling.processRequest("get", API_PATH, csrfToken)
			.done(data => loadRecords_success(data))
			.catch(error => loadRecords_failure(error));
	};

	/**
	 * loadRecords_success
	 * execute when records are loaded through api call
	 */
	var loadRecords_success = function(records) {
		logging.log(records);
		//indicate that loading process is completed
		$(xpaths.record.table.body).indicateTableLoadingCompleted();
		//populate records in table and format the same as datatable
		populateDataTable(records, xpaths.record.table.table, xpaths.record.table.body, xpaths.record.template);
	};

	/**
	 * loadRecords_failure
	 * executed when record loading has failed
	 */
	var loadRecords_failure = function(error) {
		logging.log(error);
		//indicate that loading process is completed
		$(xpaths.record.table.body).indicateTableLoadingCompleted();
		//show appropriate toast message
		processUnexpectedError(error);
	};

	/**
	 * Initialize module
	 */
	var init = function() {
		//enable or disable logging
		logging.setLoggingSwitch(teh.shouldEnableLogging());
		//get csrf token
		csrfToken = $("input#csrf").val();
		//set toastr options
		toastr.options = getToastrOptions();
		//initialize requirement id to 1
		requirementRecordId=1;

		//bind save function to put record submission event
		$(xpaths.forms.put).submit(saveRecord);
		//bind to modal dismiss event
		$(xpaths.modals.put).on("hidden.bs.modal", function() {
			teh.onModalDismiss(xpaths.forms.put);
			$("#impactedArea > option").removeAttr("selected");
			$("tr[id^='requirementForm_']:not(tr#requirementForm_1)").remove();
			requirementRecordId=1;
			$("#id_1").val('');
			$("#changeId_1").val('');
		});
		//bind edit action
		$(xpaths.record.table.body).on("click", xpaths.buttons.edit, showEditModal);
		
		//bind delete action
		$(xpaths.record.table.body).on("click", xpaths.buttons.delete, showDeleteConfimationModal);
		
		//bind confirm delete action
		$(xpaths.buttons.confirmDelete).click(deleteRecord);
		
		$("#requirementFormsTableBody").on('click',"button[id^='deleteRequirement_']",deleteRequirementForm)
		
		$("#addRequirementButton").click(addRequirementForm);
		logging.log(RECORD_NAME + " module initialized!!!");

		//load records
		loadRecords();
	};

	return {
		init: init
	}
})();