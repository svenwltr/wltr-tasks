define(function(require) {
	var defineComponent = require('flight/lib/component');
	return defineComponent(authUi);

	function authUi() {

		this.authFailed = function() {
			document.location = '/login.html';
		};

		this.after('initialize', function() {
			/*this.on('data.auth.checkAuthSucceeded', this.authSucceeded);*/
			this.on('data.auth.checkAuthFailed', this.authFailed);

		});
	}
});
