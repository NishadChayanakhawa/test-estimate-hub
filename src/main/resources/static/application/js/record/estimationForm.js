var estimationForm = (function() {
	//csrf token
	var csrfToken;
	//api path
	const API_PATH = "/api/change/useCases";

	var previousUseCaseRecord = null;

	var saveUseCases = function(event) {
		event.preventDefault();
		logging.log("Saving use cases");

		var saveUseCasesRequest = {
			"id": $("#masterRequirementId").val(),
			"useCases": []
		};

		$("textarea[name='useCaseJson']").each(function() {
			saveUseCasesRequest.useCases.push(JSON.parse($(this).val()));
		});

		logging.log(saveUseCasesRequest);
		apiHandling.processRequest("put", API_PATH, csrfToken, saveUseCasesRequest)
			.done(data => saveUseCases_success(data))
			.catch(error => console.log(error));
	};

	var saveUseCases_success = function(requirement) {
		console.log(requirement);
		console.log(requirement.useCases.length);
		$("#useCaseCountPill_" + requirement.id).html("Use Case(s): " + requirement.useCases.length);
		$("#useCaseModal").modal('hide');
	};

	var addUseCase = function(event) {
		event.preventDefault();
		logging.log("Adding use case:");
		var addUseCaseRequest = $("#addUseCaseForm").serializeObject();
		addUseCaseRequest.applicableTestTypes = [];
		$("input[name='applicableTestTypes']:checked").each(function() {
			addUseCaseRequest.applicableTestTypes.push({
				"id": $(this).attr('id').split("_")[1],
				"name": $(this).attr('aria-label')
			});
		});
		logging.log(addUseCaseRequest);

		if (previousUseCaseRecord == null) {
			logging.log("No previous record to edit");
			addUseCaseElement(addUseCaseRequest);
		} else {
			logging.log("editing previous record");
			//previousUseCaseRecord.remove();
			previousUseCaseRecord.children("td[name='useCaseSummary']").html(addUseCaseRequest.summary);
			previousUseCaseRecord.children("td[name='useCaseActions']").children("textarea[name='useCaseJson']").val(JSON.stringify(addUseCaseRequest));
			previousUseCaseRecord = null;
		}

		logging.log("Going back to use case list");
		$("#backToUseCaseListButton").click();
	};

	var addUseCaseElement = function(useCase) {
		$("#useCaseTemplate").tmpl(useCase).appendTo("#useCaseTableBody");
		$("textarea[name='useCaseJson'][class*='yet-to-update']").val(JSON.stringify(useCase));
		$("textarea[name='useCaseJson'][class*='yet-to-update']").removeClass('yet-to-update');
	};

	var showEditUserModal = function(event) {
		event.preventDefault();
		logging.log("Showing edit use case modal");
		var useCase = JSON.parse($(this).prev().val());
		logging.log(useCase);
		updateEditForm($("#useCaseFormModal"), useCase, false);
		$("#testConfigurationComplexityCode").val(useCase.testConfigurationComplexityCode);
		$("#testDataSetupComplexityCode").val(useCase.testDataSetupComplexityCode);
		$("#testTransactionComplexityCode").val(useCase.testTransactionComplexityCode);
		$("#testValidationComplexityCode").val(useCase.testValidationComplexityCode);
		$("#businessFunctionalityId").val(useCase.businessFunctionalityId);
		$.each(useCase.applicableTestTypes, function(i, testType) {
			logging.log("Test Type: " + i + testType.name);
			$("input[name='applicableTestTypes'][aria-label='" + testType.name + "']").attr('checked', true);
		});

		previousUseCaseRecord = $(this).parent().parent();
		$("#showUseCaseFormButton").click();
	}

	var showUseCaseModal = function(event) {
		event.preventDefault();
		var requirementId = $(this).attr('id').split("_")[1];
		logging.log("Requirement ID: " + requirementId);
		$("#requirementId").val(requirementId);
		$("#masterRequirementId").val(requirementId);
		apiHandling.processRequest("get", "/api/change/requirement/" + requirementId, csrfToken)
			.done(data => showUseCaseModal_success(data))
			.catch(error => console.log(error));
	};

	var showUseCaseModal_success = function(requirement) {
		logging.log(requirement);
		$("#useCaseTableBody").html("");
		$.each(requirement.useCases,function(i,useCase) {
			logging.log("Use Case:" + i + useCase);
			addUseCaseElement(useCase);
		});
		$("#useCaseModal").modal('show');
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

		$("button[id^='addUseCases_']").click(showUseCaseModal);
		$("#addUseCaseForm").submit(addUseCase);
		$("#useCaseFormModal").on("hidden.bs.modal", function() {
			logging.log("Dismissed use case form");
			$("#addUseCaseForm")[0].reset();
			$("input[name='applicableTestTypes']").attr('checked', false);
			$("#id").val("");
		});
		
		$("#useCaseTableBody").on("click", "button[name='editUseCaseButton']", showEditUserModal);
		$("#useCaseTableBody").on("click", "button[name='deleteUseCaseButton']", function() {
			$(this).parent().parent().remove();
		});
		$("#saveUseCasesButton").click(saveUseCases);

		logging.log("Estimation form initialized!!!");
	};

	return {
		init: init
	}
})();