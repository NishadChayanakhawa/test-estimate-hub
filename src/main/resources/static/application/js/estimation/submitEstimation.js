var submitEstimation = (function() {
	var csrfToken;
	var xpaths = {
		"requirementsAccordian": "div#requirementsAccordian",
		"addUseCaseButtons": "button[id^='addUseCaseButton_']",
		"useCaseFormTemplate": "#useCaseFormTemplate",
		"deleteUseCaseButtons": "button[id^='deleteUseCaseButton_']",
		"useCaseForms": "form#useCaseForms",
		"applicationSelections": "select[id^='useCaseApplication_']",
		"moduleSelections": "select[id^='useCaseModule_']",
		"subModuleSelections": "select[id^='useCaseSubModule_']",
		"appConfigurationOptionsTemplate": "#appConfigurationOptionsTemplate",
		"saveChangeButton": "button#saveChange"
	};
	var applicationConfigurationData;
	var applicationList = [];

	var onSubModuleSelection = function() {
		console.debug("SubModule changed!!!");
		var currentItemId = $(this).attr('id');
		var requirementId = currentItemId.split("_")[1];
		var useCaseId = currentItemId.split("_")[2];

		var selectedApplication = $("select#useCaseApplication_" + requirementId + "_" + useCaseId).val();
		var selectedModule = $("select#useCaseModule_" + requirementId + "_" + useCaseId).val();
		var selectedSubModule = $("select#useCaseSubModule_" + requirementId + "_" + useCaseId).val();
		console.debug(selectedApplication + '-' + selectedModule + '-' + selectedSubModule);

		$.each(applicationConfigurationData, function(i, applicationConfiguration) {
			if (applicationConfiguration.applicationName == selectedApplication &&
				applicationConfiguration.moduleName == selectedModule &&
				applicationConfiguration.subModuleName == selectedSubModule) {
				$("input#appConfigurationId_" + requirementId + "_" + useCaseId).val(applicationConfiguration.id);
			}
		});
	};

	var onApplicationSelectionChange = function() {
		console.debug("Application changed!!!");
		var currentItemId = $(this).attr('id');
		var requirementId = currentItemId.split("_")[1];
		var useCaseId = currentItemId.split("_")[2];
		var moduleSelectionSelect = $("select#useCaseModule_" + requirementId + "_" + useCaseId);
		var selectedApplication = $(this).val();
		console.debug("Application selected: " + selectedApplication);
		var moduleList = [];

		$.each(applicationConfigurationData, function(i, applicationConfiguration) {
			if ((jQuery.inArray(applicationConfiguration.moduleName, moduleList) == -1) && applicationConfiguration.applicationName == selectedApplication) {
				moduleList.push(applicationConfiguration.moduleName);
			}
		});

		console.debug(moduleList);

		moduleSelectionSelect.html("");
		moduleSelectionSelect.append("<option value=''>--Select Module--</option>");
		$.each(moduleList, function(i, moduleName) {
			moduleSelectionSelect.append("<option value='" + moduleName + "'>" + moduleName + "</option>");
		});
		moduleSelectionSelect.attr('disabled', false);

		var subModuleSelection = $("select#useCaseSubModule_" + requirementId + "_" + useCaseId);
		if (!subModuleSelection.prop('disabled')) {
			subModuleSelection.html("");
			subModuleSelection.attr('disabled', true);
		}

		$("input#appConfigurationId_" + requirementId + "_" + useCaseId).val("");
	};

	var onModuleSelectionChange = function() {
		console.debug("Module changed!!!");
		var currentItemId = $(this).attr('id');
		var requirementId = currentItemId.split("_")[1];
		var useCaseId = currentItemId.split("_")[2];
		var subModuleSelectionSelect = $("select#useCaseSubModule_" + requirementId + "_" + useCaseId);
		var selectedApplication = $("select#useCaseApplication_" + requirementId + "_" + useCaseId).val();
		var selectedModule = $(this).val();
		console.debug("Application selected: " + selectedApplication + " Module: " + selectedModule);
		var subModuleList = [];

		$.each(applicationConfigurationData, function(i, applicationConfiguration) {
			if ((jQuery.inArray(applicationConfiguration.moduleName, subModuleList) == -1) &&
				applicationConfiguration.applicationName == selectedApplication &&
				applicationConfiguration.moduleName == selectedModule) {
				subModuleList.push(applicationConfiguration.subModuleName);
			}
		});

		console.debug(subModuleList);

		subModuleSelectionSelect.html("");
		subModuleSelectionSelect.append("<option value=''>--Select Sub-Module--</option>");
		$.each(subModuleList, function(i, subModuleName) {
			subModuleSelectionSelect.append("<option value='" + subModuleName + "'>" + subModuleName + "</option>");
		});

		subModuleSelectionSelect.prop('disabled', false);
		$("input#appConfigurationId_" + requirementId + "_" + useCaseId).val("");
	};

	var populateApplicationList = function() {
		$.each(applicationConfigurationData, function(i, applicationConfiguration) {
			if (jQuery.inArray(applicationConfiguration.applicationName, applicationList) == -1) {
				applicationList.push(applicationConfiguration.applicationName);
			}
		});
		console.debug(applicationList);
	};

	var loadApplicationDataForSelection = function(shouldUpdateAddedSelects) {
		$(xpaths["applicationSelections"]).each(function() {
			var applicationDropDown = $(this);
			if (!applicationDropDown.hasClass('appListUpdated')) {
				applicationDropDown.html("");
				applicationDropDown.append("<option value=''>--Select Application--</option>");
				$.each(applicationList, function(i, applicationName) {
					applicationDropDown.append("<option value='" + applicationName + "'>" + applicationName + "</option>");
				});
				applicationDropDown.addClass("appListUpdated");
			}
		});
		if (shouldUpdateAddedSelects) {
			updateApplicationModuleAndSubModuleSelection();
		}
	};

	var loadApplicationConfigurationData = function() {
		apiHandling.processRequest("get", "/api/config/application", csrfToken, null)
			.done(data => loadApplicationConfigurationData_success(data))
			.catch(error => console.debug(error));
	};

	var loadApplicationConfigurationData_success = function(data) {
		applicationConfigurationData = data;
		populateApplicationList();
		loadApplicationDataForSelection(true);
		console.debug(applicationConfigurationData);
	};

	var submituseCaseForms = function(event) {
		event.preventDefault();
		console.debug("Submitting details");
		var useCasesData = [];
		if ($(xpaths["useCaseForms"]).validate()) {
			$("div[id^='useCaseForm_']").each(function() {
				var useCaseData = $(this).serializeObject();
				useCasesData.push(useCaseData);
			});
			console.debug(useCasesData);
			apiHandling.processRequest("put", "/api/useCase", csrfToken, useCasesData)
				.done(data => submituseCaseForms_success(data))
				.catch(error => console.debug(error));
		}
	};

	var submituseCaseForms_success = function(change) {
		console.debug(change);
		toastr.success("Use cases saved.");
	};

	var submitForReview = function(event) {
		event.preventDefault();
		console.debug("Submitting details");
		var useCasesData = [];
		if ($(xpaths["useCaseForms"]).validate()) {
			$("div[id^='useCaseForm_']").each(function() {
				var useCaseData = $(this).serializeObject();
				useCasesData.push(useCaseData);
			});
			console.debug(useCasesData);
			apiHandling.processRequest("post", "/api/useCase", csrfToken, useCasesData)
				.done(data => submitForReview_success(data))
				.catch(error => console.debug(error));
		}
	};
	
	var submitForReview_success = function(change) {
		console.debug(change);
		toastr.success("Estimation submitted for review.");
	};

	var addUseCase = function(event) {
		event.preventDefault();
		var requirementId = $(this).attr('id').split("_")[1];
		var currentUseCaseCount = parseInt($("input#useCaseCount_" + requirementId).val());
		//		var changeId=$("input[id^='useCaseChangeID_" + requirementId + '_' + currentUseCaseCount).val();
		var changeId = $("input#changeId").val();

		console.debug("Adding use case for Change# " + changeId);
		console.debug("Adding use case for Requirement# " + requirementId);
		console.debug("Current Use Case Count : " + currentUseCaseCount);

		var addUseCaseData = {
			'changeId': changeId,
			'requirementId': requirementId,
			'id': (currentUseCaseCount + 1)
		}

		$(xpaths["useCaseFormTemplate"]).tmpl(addUseCaseData).appendTo($("div#useCaseForms_" + requirementId));
		$("input#useCaseCount_" + requirementId).val(currentUseCaseCount + 1);
		loadApplicationDataForSelection(false);
	};

	var deleteUseCase = function(event) {
		event.preventDefault();
		var requirementId = $(this).attr('id').split("_")[1];
		var useCaseId = $(this).attr('id').split("_")[2];
		console.debug("Requirement: " + requirementId + " Use Case: " + useCaseId);
		$("div#useCaseForm_" + requirementId + "_" + useCaseId).remove();
	}

	var updateApplicationModuleAndSubModuleSelection = function() {
		console.debug(applicationConfigurationData);

		$("select[id^='useCaseApplication_']").each(function() {
			var currentElementId = $(this).attr('id');
			var idPart = currentElementId.split("_")[1] + '_' + currentElementId.split("_")[2];
			console.debug(idPart);
			var applicationConfigurationId = $("input#appConfigurationId_" + idPart).val();
			console.debug("Selected Application Configuration: " + applicationConfigurationId);

			var applicationRecord;
			$.each(applicationConfigurationData, function(i, applicationConfiguration) {
				if (applicationConfiguration.id == applicationConfigurationId) {
					applicationRecord = applicationConfiguration;
					return false;
				}
			});

			console.debug(applicationRecord);

			$("select#useCaseApplication_" + idPart).val(applicationRecord.applicationName);
			$("select#useCaseApplication_" + idPart).trigger("change");

			$("select#useCaseModule_" + idPart).val(applicationRecord.moduleName);
			$("select#useCaseModule_" + idPart).trigger("change");

			$("select#useCaseSubModule_" + idPart).val(applicationRecord.subModuleName);
			$("select#useCaseSubModule_" + idPart).trigger("change");
		});
	};

	var init = function() {
		console.debug("Initialized Estimation Submission!!!");
		csrfToken = $("input#csrf").val();
		loadApplicationConfigurationData();
		$(xpaths["requirementsAccordian"]).on("click", xpaths["addUseCaseButtons"], addUseCase);
		$(xpaths["requirementsAccordian"]).on("click", xpaths["deleteUseCaseButtons"], deleteUseCase);
		$(xpaths["requirementsAccordian"]).on("submit", xpaths["useCaseForms"], submituseCaseForms);
		$(xpaths["requirementsAccordian"]).on("change", xpaths["applicationSelections"], onApplicationSelectionChange);
		$(xpaths["requirementsAccordian"]).on("change", xpaths["moduleSelections"], onModuleSelectionChange);
		$(xpaths["requirementsAccordian"]).on("change", xpaths["subModuleSelections"], onSubModuleSelection);
		$(xpaths["saveChangeButton"]).click(submituseCaseForms);
		$("button#submitForReview").click(submitForReview);
	};

	return {
		init: init
	}
})();