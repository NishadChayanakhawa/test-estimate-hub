var testTypeConfigurationProcessing = (function() {
	var csrfToken;
	var xpaths = {
		"testTypeConfigurationContent": "div#testTypeConfigurationContent",
		"addOrEditTestTypeConfigurationRecordModal": "#addOrEditTestTypeConfigurationRecordModal",
		"testTypeConfigurationRecordListPlaceholder": "#testTypeConfigurationRecordListPlaceholder",
		"saveTestTypeConfigurationButton": "button#saveTestTypeConfiguration",
		"saveTestTypeConfigurationForm": "form#saveTestTypeConfigurationForm",
		"testTypeConfigurationRecordTable": "table#testTypeConfigurationRecordTable",
		"testTypeConfigurationRecordTableBody": "tbody#testTypeConfigurationRecordTableBody",
		"testTypeConfigurationRecordListTemplate": "#testTypeConfigurationRecordListTemplate",
		"editTestTypeConfigurationButtons": "button[id^='editTestTypeConfiguration_']",
		"deleteTestTypeConfigurationButtons": "button[id^='deleteTestTypeConfiguration_']",
		"deleteUserConfirmationModal": "div#deleteUserConfirmationModal",
		"confirmDeleteTestTypeConfigurationRecordButton": "button#confirmDeleteTestTypeConfigurationRecord",
		"deleteTestTypeConfigurationDeleteForm": "form#deleteTestTypeConfigurationDeleteForm"
	};

	//DELETE Process
	var onModalDismiss=function() {
		logging.log("Dismissed modal");
		$(".is-invalid").removeClass("is-invalid");
		$(xpaths["saveTestTypeConfigurationForm"])[0].reset();
		$.each($("input[name='roles']"),function(index,element) {
			logging.log("Processing role element " + index + ":" + element);
			if($(this).attr("id")!="roleTESTER") {
				$(this).attr('checked',false);
			}
		});
		$("input#password").attr('required',true);
	};

	var showDeleteModal = function(event) {
		event.preventDefault();
		var deleteButtonId = $(this).attr("id");
		var testTypeConfigurationId = deleteButtonId.split("_")[1];
		$("input#deleteAction_id").val(testTypeConfigurationId);
		var username=$("td#testTypeDisplayName_" + testTypeConfigurationId).html();
		logging.log(username);
		$("span#testTypeNameDisplay").html(username);
		$(xpaths["deleteUserConfirmationModal"]).modal("show");
	};

	var deleteTestTypeConfigurationRecord = function(event) {
		event.preventDefault();
		$(xpaths["deleteTestTypeConfigurationDeleteForm"]).indicateButtonProcessing();
		var testTypeName=$("span#testTypeNameDisplay").html();
		var testTypeConfigurationId = $(xpaths["deleteTestTypeConfigurationDeleteForm"]).serializeObject();
		apiHandling.processRequest("delete", "/api/configuration/testType", csrfToken, testTypeConfigurationId)
			.done(data => deleteTestTypeConfigurationRecord_success(data,testTypeName))
			.catch(error => console.debug(error));
	}

	var deleteTestTypeConfigurationRecord_success = function(data,testTypeName) {
		logging.log(data);
		$(xpaths["deleteTestTypeConfigurationDeleteForm"]).indicateButtonProcessingCompleted();
		$(xpaths["deleteUserConfirmationModal"]).modal('hide');
		toastr.success("Test type '" + testTypeName + "' deleted successfully");
		getTestTypeConfigurationList();
	};

	//Load List

	var getTestTypeConfigurationList = function() {
		$(xpaths["testTypeConfigurationRecordTableBody"]).indicateTableLoading(5);
		apiHandling.processRequest("get", "/api/configuration/testType", csrfToken, null)
			.done(data => getTestTypeConfigurationList_success(data))
			.catch(error => getTestTypeConfigurationList_error(error));
	};

	var getTestTypeConfigurationList_success = function(testTypeConfigurationRecords) {
		logging.log(testTypeConfigurationRecords);
		populateDataTable(testTypeConfigurationRecords,
			xpaths["testTypeConfigurationRecordTable"],
			xpaths["testTypeConfigurationRecordTableBody"],
			xpaths["testTypeConfigurationRecordListTemplate"]);
		$(xpaths["testTypeConfigurationRecordTableBody"]).indicateTableLoadingCompleted();
	};

	var getTestTypeConfigurationList_error = function(error) {
		logging.log(error);
		$(xpaths["testTypeConfigurationRecordTableBody"]).indicateTableLoadingCompleted();
		toastr.error(error.responseJSON.path + ' ' + error.responseJSON.error);
	};

	//Add/Edit

	var saveTestTypeConfiguration = function(event) {
		event.preventDefault();
		$(this).indicateButtonProcessing();
		var saveTestTypeConfigurationData = $(xpaths["saveTestTypeConfigurationForm"]).serializeObject();
		console.debug(saveTestTypeConfigurationData);
		apiHandling.processRequest("put", "/api/configuration/testType", csrfToken, saveTestTypeConfigurationData)
			.done(data => saveTestTypeConfiguration_success(data))
			.catch(error => saveTestTypeConfiguration_failure(error, saveTestTypeConfigurationData));
	};

	var saveTestTypeConfiguration_success = function(testTypeConfiguration) {
		console.debug(testTypeConfiguration);
		$(xpaths["saveTestTypeConfigurationButton"]).indicateButtonProcessingCompleted();
		toastr.success("Test Type Configuration '" + testTypeConfiguration.name + "' saved.");
		$(xpaths["addOrEditTestTypeConfigurationRecordModal"]).modal('hide');
		getTestTypeConfigurationList();
	};

	var saveTestTypeConfiguration_failure = function(error, saveTestTypeConfigurationData) {
		console.debug("here");
		console.debug(error);
		$(xpaths["saveTestTypeConfigurationButton"]).indicateButtonProcessingCompleted();
		processApiErrors(error.responseJSON.details);
		console.debug("here as well");
	};

	var showEditModal = function(event) {
		event.preventDefault();
		$(this).indicateButtonProcessing();
		var editButtonId = $(this).attr("id");
		var testTypeConfigurationId = editButtonId.split("_")[1];
		apiHandling.processRequest("get", "/api/configuration/testType/" + testTypeConfigurationId, csrfToken, null)
			.done(data => showEditModal_success(data,$(this)))
			.catch(error => console.debug(error));
	};

	var showEditModal_success = function(testTypeConfigurationRecord,editButton) {
		updateEditForm($(xpaths["addOrEditTestTypeConfigurationRecordModal"]), testTypeConfigurationRecord, true);
		editButton.indicateButtonProcessingCompleted();
	};

	var resetAddRecordForm = function(event) {
		event.preventDefault();
		$("input#id").val(null);
		$(xpaths["saveTestTypeConfigurationForm"])[0].reset();
		$(xpaths["addOrEditTestTypeConfigurationRecordModal"]).modal("show");
	};

	var init = function() {
		logging.enable();
		$(xpaths["saveTestTypeConfigurationForm"]).submit(saveTestTypeConfiguration);
		$(xpaths["testTypeConfigurationContent"]).on("click", xpaths["editTestTypeConfigurationButtons"], showEditModal);
		$(xpaths["testTypeConfigurationContent"]).on("click", xpaths["deleteTestTypeConfigurationButtons"], showDeleteModal);
		$(xpaths["confirmDeleteTestTypeConfigurationRecordButton"]).click(deleteTestTypeConfigurationRecord);

		$("button#addTestTypeConfigurationRecordButton").on("click", resetAddRecordForm);
		$("#addOrEditTestTypeConfigurationRecordModal").on("hidden.bs.modal",onModalDismiss);
		csrfToken = $("input#csrf").val();
		getTestTypeConfigurationList();
		toastr.options = getToastrOptions();
	};

	return {
		init: init
	}
})();