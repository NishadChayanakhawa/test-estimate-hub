$.fn.validate = function() {
	var requiredValidationResult = true;
	var requiredInputs = $(this).find("input:required,select:required");
	requiredInputs.each(function() {
		if ($(this).val().trim() == '') {
			requiredValidationResult = false;
			if (!$(this).hasClass('is-invalid')) {
				$(this).addClass('is-invalid');
			}
		} else {
			if ($(this).hasClass('is-invalid')) {
				$(this).removeClass('is-invalid');
			}
		}
	});

	if (!requiredValidationResult) {
		toastr.error('Please fill required values.');
	}

	var numericValidationResult = true;
	var numericInputs = $(this).find("input[class*='constraint-numeric']");
	numericInputs.each(function() {
		if (!isNumeric($(this).val())) {
			numericValidationResult = false;
			if (!$(this).hasClass('is-invalid')) {
				$(this).addClass('is-invalid');
			}
		} else {
			if ($(this).hasClass('is-invalid')) {
				$(this).removeClass('is-invalid');
			}
		}
	});
	return requiredValidationResult && numericValidationResult;
}

$.fn.serializeObject = function() {
	var o = {};
	//    var a = this.serializeArray();
	$(this).find('input[type="hidden"], input[type="text"], input[type="password"], input[type="date"], input[type="checkbox"]:checked, input[type="radio"]:checked, select').each(function() {
		if ($(this).attr('type') == 'hidden') { //if checkbox is checked do not take the hidden field
			var $parent = $(this).parent();
			var $chb = $parent.find('input[type="checkbox"][name="' + this.name.replace(/\[/g, '\[').replace(/\]/g, '\]') + '"]');
			if ($chb != null) {
				if ($chb.prop('checked')) return;
			}
		}
		if (this.name === null || this.name === undefined || this.name === '')
			return;
		var elemValue = null;
		if ($(this).is('select'))
			elemValue = $(this).find('option:selected').val();
		else elemValue = this.value;
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name]];
			}
			o[this.name].push(elemValue || '');
		} else {
			o[this.name] = elemValue || '';
		}
	});
	
	//create json if applicable
	$.each(o,function(key,value){
		logging.log("Processing Key: " + key);
		if(key.indexOf("-")!=-1) {
			var parentKey=key.split("-")[0];
			var childKey=key.split("-")[1];
			logging.log("Parent Key: " + parentKey);
			logging.log("Child Key: " + childKey);
			if(!o.hasOwnProperty(parentKey)) {
				o[parentKey]={};
			}
			o[parentKey][childKey]=value;
			delete o[key];
		}
	});
	
	return o;
}

var isNumeric = function(str) {
	if (typeof str != "string") return false // we only process strings!  
	return !isNaN(str) && // use type coercion to parse the _entirety_ of the string (`parseFloat` alone does not do this)...
		!isNaN(parseFloat(str)) // ...and ensure strings of whitespace fail
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

$.fn.indicateTableLoading = function(columnCount) {
	//
	$("button").filter(":visible").filter(":enabled").each(function() {
		$(this).addClass("temp-disabled");
		$(this).attr('disabled', true);
	});
	$("a").filter(":visible").each(function() {
		$(this).addClass("temp-disabled");
		$(this).addClass("btn");
		$(this).addClass("disabled");
		$(this).addClass("border-0");
	});
	$(this).html("");
	$(this).addClass("placeholder-glow");
	var paceholderRowToAppend = "<tr class='placeholder-row'>";
	for (var iColumnCounter = 0; iColumnCounter < columnCount; iColumnCounter++) {
		paceholderRowToAppend = paceholderRowToAppend + "<td><span class='placeholder col-12'></span></td>"
	}
	paceholderRowToAppend = paceholderRowToAppend + "</tr>"
	$(this).append(paceholderRowToAppend);
	$(this).append(paceholderRowToAppend);
	$(this).append(paceholderRowToAppend);
}

$.fn.indicateButtonProcessing = function() {
	//
	$("button").filter(":visible").filter(":enabled").each(function() {
		$(this).addClass("temp-disabled");
		$(this).attr('disabled', true);
	});
	$("a").filter(":visible").each(function() {
		$(this).addClass("temp-disabled");
		$(this).addClass("btn");
		$(this).addClass("disabled");
		$(this).addClass("border-0");
	});
	logging.log($(this).prop('tagName'));
	if($(this).prop('tagName')=='FORM') {
		$(this).children("button[type='submit']").append("<span class='temp-spinner'> <i class='fa-solid fa-spinner fa-spin'></i></span>");		
	} else {
		$(this).append("<span class='temp-spinner'> <i class='fa-solid fa-spinner fa-spin'></i></span>");
	}
}

$.fn.indicateButtonProcessingCompleted = function() {
	$("a.temp-disabled").each(function() {
		$(this).removeClass("temp-disabled");
		$(this).removeClass("btn");
		$(this).removeClass("disabled");
		$(this).removeClass("border-0");
	});
	$("button.temp-disabled").each(function() {
		$(this).removeClass("temp-disabled");
		$(this).attr('disabled', false);
	});
	logging.log($(this).prop('tagName'));
	if($(this).prop('tagName')=='FORM') {
		$(this).children("button[type='submit']").children("span.temp-spinner").remove();	
	} else {
		$(this).children("span.temp-spinner").remove();
	}
	$(this).children("button[type='submit']").children("span.temp-spinner").remove();
}

$.fn.indicateTableLoadingCompleted = function() {
	$("a.temp-disabled").each(function() {
		$(this).removeClass("temp-disabled");
		$(this).removeClass("btn");
		$(this).removeClass("disabled");
		$(this).removeClass("border-0");
	});
	$("button.temp-disabled").each(function() {
		$(this).removeClass("temp-disabled");
		$(this).attr('disabled', false);
	});
	$(this).removeClass("placeholder-glow");
	$(this).children("tr.placeholder-row").remove();
}

var processApiErrors = function(errorDetails) {
	$(".is-invalid").removeClass("is-invalid");
	$.each(errorDetails, function(i, value) {
		var elementId = value.split(" ")[0];
		logging.log(i + ":" + value + ":" + elementId);
		$("#" + elementId).addClass("is-invalid");

		toastr.error(value);
	});
};

var updateEditForm = function(editModal, data, shouldShowModal) {
	$.each(data, function(key, value) {
		logging.log("Processing key " + key);
		logging.log(editModal.children("input#" + key));
		$("input#" + key).val(value);
	});
	if (shouldShowModal) {
		editModal.modal('show');
	}
};

var populateDataTable = function(data, tableXPath, tableBodyXPath, templateXPath) {
	if ($.fn.DataTable.isDataTable(tableXPath)) {
		$(tableXPath).DataTable().destroy();
	}
	$(tableBodyXPath).html("");
	$(templateXPath).tmpl(data).appendTo(tableBodyXPath);
	$(tableXPath).DataTable();
};

var processUnexpectedError=function(error) {
	toastr.error("<b><u>Unexpected error</u></b>: " + error.responseJSON.path + ' ' + error.responseJSON.error + "<br><br><b>Please report this issue to development team.</b>");
};