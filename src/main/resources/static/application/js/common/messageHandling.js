var messageHandling = (function() {
	var displayToastMessage = function(message, type) {
		const toastPlaceholder = document.getElementById("toastContainer");
		const wrapper = document.createElement("div");
		wrapper.setAttribute("class", "toast text-bg-" + type);
		wrapper.innerHTML = [
			`<div class="d-flex">`,
			`	<div class="toast-body">${message}</div>`,
			`	<button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>`,
			`	</div>`,
			`</div>`
		].join('');
		toastPlaceholder.append(wrapper);
		$(".toast").last().toast('show');
	};
	return {
		displayToastMessage: displayToastMessage
	}
})();