var generalConfigurationProcessing = (function() {
	var csrfToken;
	const API_PATH="/api/configuration/general";
	var xpaths = {
		"saveGeneralConfigurationButton" : "button#saveGeneralConfigurationButton",
		"generalConfigurationForm" : "form#generalConfigurationForm",
		"complexityWeightagePercentageForTestDataPreparation" : "input[name='complexityWeightagePercentageForTestDataPreparation']",
		"complexityWeightagePercentageForTestConfiguration" : "input[name='complexityWeightagePercentageForTestConfiguration']",
		"complexityWeightagePercentageForTestValidation" : "input[name='complexityWeightagePercentageForTestValidation']"
	};
	
	var loadGeneralConfiguration=function() {
		logging.log("Loading general configuration");
		$("button#saveGeneralConfigurationButton").html("Loading saved data");
		$(xpaths["generalConfigurationForm"]).indicateButtonProcessing();
		$("input.is-invalid").removeClass("is-invalid");
		apiHandling.processRequest("get",API_PATH,csrfToken)
			.done(data => loadGeneralConfiguration_success(data))
			.catch(error => loadGeneralConfiguration_failure(error));
	};
	
	var loadGeneralConfiguration_success=function(generalConfiguration) {
		logging.log(generalConfiguration);
		$.each(generalConfiguration,function(key,value) {
			logging.log("Processing key: " + key);
			if(typeof generalConfiguration[key]=='object') {
				logging.log("Processing JSON for " + key);
				$.each(generalConfiguration[key],function(complexity,productivity) {
					logging.log("Processing Complexity: " + complexity);	
					$("input#" + key + "-" + complexity).val(productivity);
				});				
			} else {
				logging.log("Processing Value: " + value);
				$("input#" + key).val(value);
			}
		});
		$("button#saveGeneralConfigurationButton").html("Save changes");
		$(xpaths["generalConfigurationForm"]).indicateButtonProcessingCompleted();
	};
	
	var loadGeneralConfiguration_failure=function(error) {
		$("button#saveGeneralConfigurationButton").html("Save changes");
		$(xpaths["generalConfigurationForm"]).indicateButtonProcessingCompleted();
		logging.log(error);
		toastr.error(error.responseJSON.path + ' ' + error.responseJSON.error);
	}
	
	var saveGeneralConfiguration=function(event) {
		event.preventDefault();
		logging.log("Saving general configuration");
		$(xpaths["generalConfigurationForm"]).indicateButtonProcessing();
		var generalConfigurationSaveRequest=$(xpaths["generalConfigurationForm"]).serializeObject();
		logging.log(generalConfigurationSaveRequest);
		apiHandling.processRequest("put",API_PATH,csrfToken,generalConfigurationSaveRequest)
			.done(data => saveGeneralConfiguration_success(data))
			.catch(error => saveGeneralConfiguration_failure(error));
	};
	
	var saveGeneralConfiguration_success=function(generalSetting) {
		logging.log(generalSetting);
		toastr.success("General setting saved successfully");
		$(xpaths["generalConfigurationForm"]).indicateButtonProcessingCompleted();
		loadGeneralConfiguration();
	}
	
	var saveGeneralConfiguration_failure=function(error) {
		$(xpaths["generalConfigurationForm"]).indicateButtonProcessingCompleted();
		logging.log(error);
		processApiErrors(error.responseJSON.details);
	}

	var init = function() {
		logging.enable();
		logging.log("General Configuration initialized!!!");
		csrfToken=$("input#csrf").val();
		toastr.options = getToastrOptions();
		
		$(xpaths["generalConfigurationForm"]).submit(saveGeneralConfiguration);
		loadGeneralConfiguration();
	};

	return {
		init: init
	}
})();