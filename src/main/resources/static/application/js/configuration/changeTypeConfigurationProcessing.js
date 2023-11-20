var changeTypeConfigurationProcessing = (function() {
	var csrfToken;
	var xpaths = {
		"changeTypeConfigurationContent": "div#changeTypeConfigurationContent",
		"addOrEditChangeTypeConfigurationRecordModal": "#addOrEditChangeTypeConfigurationRecordModal",
		"changeTypeConfigurationRecordListPlaceholder": "#changeTypeConfigurationRecordListPlaceholder",
		"saveChangeTypeConfigurationButton": "button#saveChangeTypeConfiguration",
		"saveChangeTypeConfigurationForm": "form#saveChangeTypeConfigurationForm",
		"changeTypeConfigurationRecordTable": "table#changeTypeConfigurationRecordTable",
		"changeTypeConfigurationRecordTableBody": "tbody#changeTypeConfigurationRecordTableBody",
		"changeTypeConfigurationRecordListTemplate": "#changeTypeConfigurationRecordListTemplate",
		"editChangeTypeConfigurationButtons": "button[id^='editChangeTypeConfiguration_']",
		"deleteChangeTypeConfigurationButtons": "button[id^='deleteChangeTypeConfiguration_']",
		"deleteUserConfirmationModal": "div#deleteUserConfirmationModal",
		"confirmDeleteChangeTypeConfigurationRecordButton": "button#confirmDeleteChangeTypeConfigurationRecord",
		"deleteChangeTypeConfigurationDeleteForm": "form#deleteChangeTypeConfigurationDeleteForm"
	};
	
	var onModalDismiss=function() {
		logging.log("Dismissed modal");
		$(".is-invalid").removeClass("is-invalid");
		$("form#saveChangeTypeConfigurationForm")[0].reset();
	};

	//DELETE Process

	var showDeleteModal = function(event) {
		event.preventDefault();
		var deleteButtonId = $(this).attr("id");
		var changeTypeConfigurationId = deleteButtonId.split("_")[1];
		var changeTypeDisplayName_=$("td#changeTypeDisplayName_" + changeTypeConfigurationId).html();
		$("span#changeTypeNameDisplay").html(changeTypeDisplayName_);
		$("input#deleteAction_id").val(changeTypeConfigurationId);
		$(xpaths["deleteUserConfirmationModal"]).modal("show");
	};

	var deleteChangeTypeConfigurationRecord = function(event) {
		event.preventDefault();
		$(this).indicateButtonProcessing();
		var changeTypeConfigurationId = $(xpaths["deleteChangeTypeConfigurationDeleteForm"]).serializeObject();
		apiHandling.processRequest("delete", "/api/configuration/changeType", csrfToken, changeTypeConfigurationId)
			.done(data => deleteChangeTypeConfigurationRecord_success(data,$("span#changeTypeNameDisplay").html()))
			.catch(error => console.debug(error));
	}

	var deleteChangeTypeConfigurationRecord_success = function(data,changeTypeName) {
		logging.log(data);
		$(this).indicateButtonProcessingCompleted();
		toastr.success("Change type '" + changeTypeName + "' deleted successfully");
		$(xpaths["deleteUserConfirmationModal"]).modal("hide");
		getChangeTypeConfigurationList();
	};

	//Load List

	var getChangeTypeConfigurationList = function() {
		//event.preventDefault();
		$(xpaths["changeTypeConfigurationRecordTableBody"]).html($(xpaths["ChangeTypeConfigurationRecordListPlaceholder"]).html());
		$(xpaths["changeTypeConfigurationRecordTableBody"]).indicateTableLoading(6);
		apiHandling.processRequest("get", "/api/configuration/changeType", csrfToken, null)
			.done(data => getChangeTypeConfigurationList_success(data))
			.catch(error => console.debug(error));
	};

	var getChangeTypeConfigurationList_success = function(changeTypeConfigurationRecords) {
		console.debug(changeTypeConfigurationRecords);
		populateDataTable(changeTypeConfigurationRecords,
			xpaths["changeTypeConfigurationRecordTable"],
			xpaths["changeTypeConfigurationRecordTableBody"],
			xpaths["changeTypeConfigurationRecordListTemplate"]);
		$(xpaths["changeTypeConfigurationRecordTableBody"]).indicateTableLoadingCompleted();
	};

	var populateDataTable = function(data, tableXPath, tableBodyXPath, templateXPath) {
		if ($.fn.DataTable.isDataTable(tableXPath)) {
			$(tableXPath).DataTable().destroy();
		}
		$(tableBodyXPath).html("");
		$(templateXPath).tmpl(data).appendTo(tableBodyXPath);
		$(tableXPath).DataTable();
		$(xpaths["ChangeTypeConfigurationContent"]).unblock();
	};

	//Add/Edit

	var saveChangeTypeConfiguration = function(event) {
		event.preventDefault();
		$(this).indicateButtonProcessing();
		var saveChangeTypeConfigurationData = $(xpaths["saveChangeTypeConfigurationForm"]).serializeObject();
		logging.log(saveChangeTypeConfigurationData);
		apiHandling.processRequest("put", "/api/configuration/changeType", csrfToken, saveChangeTypeConfigurationData)
			.done(data => saveChangeTypeConfiguration_success(data))
			.catch(error => saveChangeTypeConfiguration_failure(error));

	};

	var saveChangeTypeConfiguration_success = function(changeTypeConfiguration) {
		console.debug(changeTypeConfiguration);
		$("form#saveChangeTypeConfigurationForm").indicateButtonProcessingCompleted();
		toastr.success("Change Type Configuration '" + changeTypeConfiguration.name + "' saved");
		$(xpaths["addOrEditChangeTypeConfigurationRecordModal"]).modal('hide');
		getChangeTypeConfigurationList();
	};

	var saveChangeTypeConfiguration_failure = function(error) {
		console.debug(error);
		$("form#saveChangeTypeConfigurationForm").indicateButtonProcessingCompleted();
		processApiErrors(error.responseJSON.details);
	};

	var showEditModal = function(event) {
		event.preventDefault();
		$(this).indicateButtonProcessing();
		var editButtonId = $(this).attr("id");
		var changeTypeConfigurationId = editButtonId.split("_")[1];
		apiHandling.processRequest("get", "/api/configuration/changeType/" + changeTypeConfigurationId, csrfToken, null)
			.done(data => showEditModal_success(data,$(this)))
			.catch(error => console.debug(error));
	};

	var showEditModal_success = function(changeTypeConfigurationRecord,editButton) {
		updateEditForm($(xpaths["addOrEditChangeTypeConfigurationRecordModal"]),changeTypeConfigurationRecord,true);
		editButton.indicateButtonProcessingCompleted();
	};

	var resetAddRecordForm = function(event) {
		event.preventDefault();
		$("input#id").val(null);
		$(xpaths["saveChangeTypeConfigurationForm"])[0].reset();
		$(xpaths["addOrEditChangeTypeConfigurationRecordModal"]).modal("show");
	};

	var init = function() {
		logging.enable();
		$("form#saveChangeTypeConfigurationForm").submit(saveChangeTypeConfiguration);
		$(xpaths["changeTypeConfigurationContent"]).on("click", xpaths["editChangeTypeConfigurationButtons"], showEditModal);
		$(xpaths["changeTypeConfigurationContent"]).on("click", xpaths["deleteChangeTypeConfigurationButtons"], showDeleteModal);
		$(xpaths["confirmDeleteChangeTypeConfigurationRecordButton"]).click(deleteChangeTypeConfigurationRecord);

		$("button#addChangeTypeConfigurationRecordButton").on("click", resetAddRecordForm);
		$("#addOrEditChangeTypeConfigurationRecordModal").on("hidden.bs.modal",onModalDismiss);

		csrfToken = $("input#csrf").val();
		getChangeTypeConfigurationList();
		toastr.options = getToastrOptions();
	};

	return {
		init: init
	}
})();