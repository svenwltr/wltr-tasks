define(function(require) {
	var defineComponent = require('flight/lib/component');
	return defineComponent(authUi);

	function authUi() {

		this.authSucceeded = function() {
			var parent = this;
			window.setTimeout(function() {
				parent.trigger('data.auth.checkAuth');
			}, 1000 * 60);
			
		};

		this.authFailed = function() {
			document.location = '/login.html';
		};

		this.after('initialize', function() {
			this.on('data.auth.checkAuthSucceeded', this.authSucceeded);
			this.on('data.auth.checkAuthFailed', this.authFailed);

		});
	}
});