var userManagementProcessing = (function() {
	var csrfToken;
	var xpaths = {
		"userListTemplate": "#userListTemplate",
		"userListTableBody": "tbody#userListTableBody",
		"userListPlaceholder": "#userListPlaceholder",
		"editUserButtonGroup": "button[id^='editUser_']",
		"deleteUserButtonGroup": "button[id^='deleteUser_']",
		"editUserModal": "div#editUserModal",
		"edit_username": "input#edit_username",
		"edit_firstName": "input#edit_firstName",
		"edit_lastName": "input#edit_lastName",
		"edit_email": "input#edit_email",
		"editUserForm": "form#editUserForm",
		"saveUpdatedUser": "button#saveUpdatedUser",
		"saveNewUser": "button#saveNewUser",
		"addUserForm": "form#addUserForm",
		"addUserModal": "div#addUserModal",
		"deleteUserForm": "form#deleteUserForm",
		"delete_username": "input#delete_username",
		"usernameDisplay": "span#usernameDisplay",
		"deleteUserConfirmationModal": "div#deleteUserConfirmationModal",
		"confirmDeleteUser": "button#confirmDeleteUser",
		"delete_username": "input#delete_username"
	};
	var getUserList = function() {
		//event.preventDefault();
		$(xpaths["userListTableBody"]).html($(xpaths["userListPlaceholder"]).html());
		$("#userManagementContent").block({ message: '<h5><i class="fa-solid fa-spinner fa-spin"></i> Just a moment...</h5>' });
		apiHandling.processRequest("get", "/api/user", csrfToken, null)
			.done(data => getUserList_success(data))
			.catch(error => console.debug(error));
	};

	var getUserList_success = function(data) {
		console.debug(data);
		$(xpaths["userListTableBody"]).html("");
		$(xpaths["userListTemplate"]).tmpl(data).appendTo(xpaths["userListTableBody"]);
		$("table#userListTable").DataTable();
		$("#userManagementContent").unblock();
	};

	var updateEditUserModal = function(username) {
		apiHandling.processRequest("get", "/api/user/" + username, csrfToken, null)
			.done(data => updateEditUserModal_success(data))
			.catch(error => console.debug(error));
	};

	var updateEditUserModal_success = function(user) {
		console.debug(user);
		$("input[id^='edit_role']:checked").attr("checked", false);
		$(xpaths["editUserForm"])[0].reset();
		$(xpaths["edit_username"]).val(user.username);
		$(xpaths["edit_firstName"]).val(user.firstName);
		$(xpaths["edit_lastName"]).val(user.lastName);
		$(xpaths["edit_email"]).val(user.email);
		$(xpaths["editUserModal"]).modal("show");
		$("input#edit_role" + user.role).attr("checked", true);
	};

	var showEditUserModal = function(event) {
		event.preventDefault();
		console.debug("Showing edit modal");
		var currentEditButtonId = $(this).attr("id");
		var username = currentEditButtonId.substring(currentEditButtonId.search("_") + 1);
		updateEditUserModal(username);
	};

	var saveUpdatedUser = function(event) {
		event.preventDefault();
		if ($(xpaths["editUserForm"]).validate()) {
			var editUserData = $(xpaths["editUserForm"]).serializeObject();
			console.debug(editUserData);
			$("#userManagementContent").block({ message: '<h5><i class="fa-solid fa-spinner fa-spin"></i> Just a moment...</h5>' });
			$("#editUserModal").block({ message: '<h5><i class="fa-solid fa-spinner fa-spin"></i> Just a moment...</h5>' });
			apiHandling.processRequest("put", "/api/user", csrfToken, editUserData)
				.done(data => saveUpdatedUser_success(data))
				.catch(error => console.debug(error));
		}
	};

	var saveUpdatedUser_success = function(user) {
		console.debug(user);
		var message = "User " + user.username + " updated.";
		$(xpaths["editUserModal"]).modal("hide");
		$(xpaths["editUserForm"])[0].reset();
		toastr.success(message);
		$("#editUserModal").unblock();
		getUserList();
	};

	var saveNewUser = function(event) {
		event.preventDefault();
		console.debug("Saving User");
		if ($(xpaths["addUserForm"]).validate()) {
			var newUserData = $(xpaths["addUserForm"]).serializeObject();
			console.debug(newUserData);
			$("#userManagementContent").block({ message: '<h5><i class="fa-solid fa-spinner fa-spin"></i> Just a moment...</h5>' });
			$("#addUserModal").block({ message: '<h5><i class="fa-solid fa-spinner fa-spin"></i> Just a moment...</h5>' });

			apiHandling.processRequest("put", "/api/user", csrfToken, newUserData)
				.done(data => saveNewUser_success(data))
				.catch(error => console.debug(error));
		}
	};

	var saveNewUser_success = function(user) {
		console.debug(user);
		var message = "User " + user.username + " added.";
		$(xpaths["addUserModal"]).modal("hide");
		$(xpaths["addUserForm"])[0].reset();
		toastr.success(message);
		$("#addUserModal").unblock();
		getUserList();
	};

	var showDeleteConfirmationModal = function() {
		console.debug("Showing delete modal");
		var currentEditButtonId = $(this).attr("id");
		var username = currentEditButtonId.substring(currentEditButtonId.search("_") + 1);
		$(xpaths["deleteUserForm"])[0].reset();
		$(xpaths["delete_username"]).val(username);
		$(xpaths["usernameDisplay"]).html(username);
		$(xpaths["deleteUserConfirmationModal"]).modal("show");
	};

	var confirmDeleteUser = function(event) {
		event.preventDefault();
		var username = $(xpaths["delete_username"]).val();
		apiHandling.processRequest("delete", "/api/user/" + username, csrfToken, null)
			.done(data => confirmDeleteUser_success(username))
			.catch(error => console.debug(error));
	};

	var confirmDeleteUser_success = function(username) {
		var message = "User " + username + " deleted.";
		$(xpaths["deleteUserConfirmationModal"]).modal("hide");
		$(xpaths["delete_username"]).val();
		$(xpaths["usernameDisplay"]).html();
		toastr.success(message);
		getUserList();
	};

	var init = function() {
		getUserList();
		$(xpaths["userListTableBody"]).on("click", xpaths["editUserButtonGroup"], showEditUserModal);
		$(xpaths["userListTableBody"]).on("click", xpaths["deleteUserButtonGroup"], showDeleteConfirmationModal);

		$(xpaths["saveUpdatedUser"]).click(saveUpdatedUser);
		$(xpaths["saveNewUser"]).click(saveNewUser);
		$(xpaths["confirmDeleteUser"]).click(confirmDeleteUser);
		csrfToken = $("input#csrf").val();

		toastr.options = {
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
		init: init
	}
})();