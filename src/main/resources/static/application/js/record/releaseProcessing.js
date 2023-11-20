var releaseProcessing = (function() {
	var csrfToken;
	const API_PATH="/api/release";
	var xpaths = {
		"releaseContent" : "div#releaseContent",
		"addOrEditReleaseRecordModal" : "#addOrEditReleaseRecordModal",
		"releaseRecordListPlaceholder" : "#releaseRecordListPlaceholder",
		"saveReleaseButton" : "button#saveRelease",
		"saveReleaseForm" : "form#saveReleaseForm",
		"releaseRecordTable" : "table#releaseRecordTable",
		"releaseRecordTableBody" : "tbody#releaseRecordTableBody",
		"releaseRecordListTemplate" : "#releaseRecordListTemplate",
		"editReleaseButtons" : "button[id^='editRelease_']",
		"deleteReleaseButtons" : "button[id^='deleteRelease_']",
		"deleteUserConfirmationModal" : "div#deleteUserConfirmationModal",
		"confirmDeleteReleaseRecordButton" : "button#confirmDeleteReleaseRecord",
		"deleteReleaseDeleteForm" : "form#deleteReleaseDeleteForm"
	};
	
	var deleteRelease=function(event) {
		event.preventDefault();
		logging.log("Deleting release");
		$(xpaths["confirmDeleteReleaseRecordButton"]).indicateButtonProcessing();
		var deleteRequest=$(xpaths["deleteReleaseDeleteForm"]).serializeObject();
		logging.log(deleteRequest);
		apiHandling.processRequest("delete", API_PATH, csrfToken,deleteRequest)
			.done(data => deleteRelease_success(data))
			.catch(error => deleteRelease_failure(error));
	};
	
	var deleteRelease_success=function(data) {
		logging.log(data);
		$(xpaths["confirmDeleteReleaseRecordButton"]).indicateButtonProcessingCompleted();
		toastr.success("Release '" + $("span#releaseIdentifierDisplay").html() + "' deleted successfully");
		$(xpaths["deleteUserConfirmationModal"]).modal('hide');
		loadReleaseRecords();
	}
	
	var deleteRelease_failure=function(error) {
		logging.log(error);
		$(xpaths["confirmDeleteReleaseRecordButton"]).indicateButtonProcessingCompleted();
		toastr.error(error.responseJSON.path + ' ' + error.responseJSON.error);
	}
	
	var showDeleteModal=function(event) {
		event.preventDefault();
		logging.log("Showing delete modal!!!");
		var deleteButtonId = $(this).attr("id");
		var releaseId = deleteButtonId.split("_")[1];
		logging.log("Showing delete modal for release id: " + releaseId);
		$("span#releaseIdentifierDisplay").html($("td#releaseIdentifier_" + releaseId).html());
		$("input#deleteAction_id").val(releaseId);
		$(xpaths["deleteUserConfirmationModal"]).modal('show');
	};
	
	var showEditModal=function(event) {
		event.preventDefault();
		logging.log("Showing edit modal!!!");
		$(this).indicateButtonProcessing();
		var editButtonId = $(this).attr("id");
		var releaseId = editButtonId.split("_")[1];
		apiHandling.processRequest("get", API_PATH + "/" + releaseId, csrfToken)
			.done(data => showEditModal_success(data,$(this)))
			.catch(error => showEditModal_failure(error,$(this)));
	};
	
	var showEditModal_success=function(release,editButton) {
		logging.log(release);
		editButton.indicateButtonProcessingCompleted();
		updateEditForm($(xpaths["addOrEditReleaseRecordModal"]),release,true);
	};
	
	var showEditModal_failure=function(error,editButton) {
		logging.log(error);
		editButton.indicateButtonProcessingCompleted();
	};
	
	var saveRelease=function(event) {
		event.preventDefault();
		logging.log("Saving release!!!");
		$(this).indicateButtonProcessing();
		$("input.is-invalid").removeClass('is-invalid');
		var saveReleaseRequest=$(xpaths["saveReleaseForm"]).serializeObject();
		logging.log(saveReleaseRequest);
		apiHandling.processRequest("put",API_PATH,csrfToken,saveReleaseRequest)
			.done(data => saveRelease_success(data))
			.catch(error => saveRelease_failure(error));
	};
	
	var saveRelease_success=function(release) {
		logging.log(release);
		$(xpaths["saveReleaseButton"]).indicateButtonProcessingCompleted();
		toastr.success("Release '" + release.identifier + "' saved");
		$("#addOrEditReleaseRecordModal").modal('hide');
		loadReleaseRecords();
	};
	
	var saveRelease_failure=function(error) {
		logging.log(error);
		$(xpaths["saveReleaseButton"]).indicateButtonProcessingCompleted();
		processApiErrors(error.responseJSON.details);
	};
	
	var loadReleaseRecords=function() {
		logging.log("loading release records!!!");
		$(xpaths["releaseRecordTableBody"]).indicateTableLoading(5);
		apiHandling.processRequest("get",API_PATH,csrfToken)
			.done(data => loadReleaseRecords_success(data))
			.catch(error => loadReleaseRecords_failure(error));
	};
	
	var loadReleaseRecords_success=function(releaseRecords) {
		logging.log(releaseRecords);
		$(xpaths["releaseRecordTableBody"]).indicateTableLoadingCompleted();
		populateDataTable(releaseRecords,xpaths["releaseRecordTable"],xpaths["releaseRecordTableBody"],xpaths["releaseRecordListTemplate"]);
	};
	
	var loadReleaseRecords_failure=function(error) {
		logging.log(error);
		$(xpaths["releaseRecordTableBody"]).indicateTableLoadingCompleted();
		toastr.error(error.responseJSON.path + ' ' + error.responseJSON.error);
	};
	
	var onModalDismiss=function() {
		logging.log("Dismissed modal");
		$(".is-invalid").removeClass("is-invalid");
		$(xpaths["saveReleaseForm"])[0].reset();
	};

	var init=function() {
		csrfToken=$("input#csrf").val();
		toastr.options = getToastrOptions();
		logging.enable();
		$(xpaths["saveReleaseForm"]).submit(saveRelease);
		$("#addOrEditReleaseRecordModal").on("hidden.bs.modal",onModalDismiss);
		$(xpaths["releaseRecordTableBody"]).on("click",xpaths["editReleaseButtons"],showEditModal);
		$(xpaths["releaseRecordTableBody"]).on("click",xpaths["deleteReleaseButtons"],showDeleteModal);
		$(xpaths["deleteReleaseDeleteForm"]).submit(deleteRelease);
		loadReleaseRecords();
		logging.log("Release Management initialized!!!");
	};

	return {
		init: init
	}
})();