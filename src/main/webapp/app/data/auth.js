define(function(require) {
	var defineComponent = require('flight/lib/component');
	return defineComponent(auth);

	function auth() {
		this.checkLogin = function() {
			var parent = this;

			$.ajax({
				url : "/api/session/",
				success : function(data) {
					parent.trigger('data.auth.checkAuthSucceeded', data);

				},
				error : function(jqXHR, textStatus, errorThrown) {
					parent.trigger('data.auth.checkAuthFailed');

				},
			});
		};

		this.after('initialize', function() {
			this.on('data.auth.checkAuth', this.checkLogin);
		});

	}

});