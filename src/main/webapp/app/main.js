define(function(require) {

	require('data/auth').attachTo(document);
	require('data/task').attachTo(document);

	require('ui/auth').attachTo(document);
	require('ui/task-list').attachTo('#task-list');
	require('ui/task-form').attachTo('#task-form');
	require('ui/task-detail').attachTo('#task-detail');

	var defineComponent = require('flight/lib/component');
	return defineComponent(main);

	function main() {

		this.firstTaskLoad = function() {
			this.off('data.auth.provideList', this.firstTaskLoad);
			$('body').removeClass('loading');
			
		};
		
		this.onKeyup = function(event) {
			if(event.keyCode == 27) { /* ESC */
				this.trigger('ui.task.deselect');
			}
			
		};

		this.after('initialize', function() {
			this.on('data.task.provideList', this.firstTaskLoad);
			
			this.on(document, 'keyup', this.onKeyup);

			this.trigger('data.auth.checkAuth');
			this.trigger('data.task.requestList');

			var self = this;
			window.setInterval(function(){ self.trigger('ui.render'); }, 7*1000);
			window.setInterval(function(){ self.trigger('data.auth.checkAuth'); }, 61*1000);
			window.setInterval(function(){ self.trigger('data.task.requestList'); }, 59*1000);
			
		});

	}

});
