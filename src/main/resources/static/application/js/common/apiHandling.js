var apiHandling = (function() {
	var processRequest = function(method, apiUrl, csrfToken, requestData) {
		return $.ajax({
			cache: false,
			method: method,
			headers: {
				'X-CSRF-TOKEN': csrfToken
			},
			url: apiUrl,
			contentType: "application/json",
			data: JSON.stringify(requestData)
		});
	};

	return {
		processRequest: processRequest
	}
})();