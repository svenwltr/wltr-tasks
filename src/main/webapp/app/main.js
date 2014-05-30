define(function(require) {

	require('es5shim/es5-shim.min');
	require('es5shim/es5-sham.min');
	require('jquery');
	
	require('data/auth').attachTo(document);
	require('data/task').attachTo(document);
	require('ui/auth').attachTo(document);

	var defineComponent = require('flight/lib/component');
	return defineComponent(main);

	function main() {

		this.firstAuth = function() {
			var self = this;
			this.off('data.auth.checkAuthSucceeded', this.firstAuth);
			
			require(['ui/task-list', 'ui/task-form', 'ui/task-detail'], function(uiTaskList, uiTaskForm, uiTaskDetail){
				uiTaskList.attachTo('#task-list');
				uiTaskForm.attachTo('#task-form');
				uiTaskDetail.attachTo('#task-detail');
				self.trigger(document, 'data.task.requestList');
				
			});

		};
		
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
			this.on('data.auth.checkAuthSucceeded', this.firstAuth);
			this.on('data.task.provideList', this.firstTaskLoad);
			
			this.on(document, 'keyup', this.onKeyup);

			this.trigger('data.auth.checkAuth');
			
		});

	}

});
