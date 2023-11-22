var teh = (function() {
	var getUrlParameter = function getUrlParameter(sParam) {
		var sPageURL = window.location.search.substring(1),
			sURLVariables = sPageURL.split('&'),
			sParameterName,
			i;

		for (i = 0; i < sURLVariables.length; i++) {
			sParameterName = sURLVariables[i].split('=');

			if (sParameterName[0] === sParam) {
				return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
			}
		}
		return false;
	};

	var shouldEnableLogging = function() {
		return "@spring.profiles.active@" != "prod"
	};

	var processUnexpectedError = function(error) {
		toastr.error("<b><u>Unexpected error</u></b>: " + error.responseJSON.path + ' ' + error.responseJSON.error + "<br><br><b>Please report this issue to development team.</b>");
	};

	var populateDataTable = function(data, tableXPath, tableBodyXPath, templateXPath) {
		if ($.fn.DataTable.isDataTable(tableXPath)) {
			$(tableXPath).DataTable().destroy();
		}
		$(tableBodyXPath).html("");
		$(templateXPath).tmpl(data).appendTo(tableBodyXPath);
		$(tableXPath).DataTable();
	};

	var updateEditForm = function(editModalXPath, data) {
		$.each(data, function(key, value) {
			logging.log("Processing key " + key);
			logging.log($(editModalXPath).children("input#" + key));
			$("input#" + key).val(value);
		});
	};

	var showSaveSuccessMessage = function(recordType, recordIdentifier) {
		toastr.success(recordType + " '" + recordIdentifier + "' saved successfully");
	}

	var convertToProperCase = function(camelCaseText) {
		var text = camelCaseText;
		var result = text.replace(/([A-Z])/g, " $1");
		var finalResult = result.charAt(0).toUpperCase() + result.slice(1);
		return finalResult;
	}

	var processSaveApiErrors = function(errorDetails) {
		$(".is-invalid").removeClass("is-invalid");
		$.each(errorDetails, function(i, value) {
			var element = value.split(" ")[0];
			logging.log(element);
			var elementIds = element.split("-");
			$.each(elementIds, function(elementIdIndex, elementId) {
				logging.log(i + ":" + elementIdIndex + ":" + value + ":" + elementId);
				$("#" + elementId).addClass("is-invalid");
			});
			toastr.error(value.replace(element, convertToProperCase(element)));
		});
	};

	var onModalDismiss = function(formXPath) {
		logging.log("Dismissed modal");
		$(".is-invalid").removeClass("is-invalid");
		$(formXPath)[0].reset();
		$("input[name='id']").val("");
	};

	var getToastrOptions = function() {
		return {
			"closeButton": true,
			"debug": false,
			"newestOnTop": true,
			"progressBar": true,
			"positionClass": "toast-top-right",
			"preventDuplicates": false,
			"onclick": null,
			"showDuration": "300",
			"hideDuration": "1000",
			"timeOut": "5000",
			"extendedTimeOut": "1000",
			"showEasing": "swing",
			"hideEasing": "linear",
			"showMethod": "fadeIn",
			"hideMethod": "fadeOut"
		}
	};

	return {
		shouldEnableLogging: shouldEnableLogging,
		processUnexpectedError: processUnexpectedError,
		populateDataTable: populateDataTable,
		showSaveSuccessMessage: showSaveSuccessMessage,
		processSaveApiErrors: processSaveApiErrors,
		onModalDismiss: onModalDismiss,
		updateEditForm: updateEditForm,
		getUrlParameter: getUrlParameter,
		getToastrOptions: getToastrOptions
	}
})();