define(function(require) {
	var defineComponent = require('flight/lib/component');
	return defineComponent(task);

	function task() {
		this.requestList = function() {
			var parent = this;
			$.ajax({
				url : '/api/tasks/',
				success : function(data) {

					data.forEach(function(task) {
						task.executionDiff = Date.now() - task.lastExecution;
						task.nextExecution = task.lastExecution + task.interval;
						if (task.lastExecution)
							task.score = task.executionDiff / task.interval
									* 100;
						else
							task.score = Infinity;

					});

					parent.trigger('data.task.provideList', {
						tasks : data,
					});
				},
			});

		};

		this.saveTask = function(event, task) {
			var self = this;
			$.ajax({
				url : '/api/tasks/',
				type : 'POST',
				contentType : 'application/json',
				data : JSON.stringify(task),
				success : function() {
					self.trigger('data.task.requestList');
				}
			});

		};

		this.after('initialize', function() {
			this.on('data.task.requestList', this.requestList);
			this.on('data.task.save', this.saveTask);

		});
	}
});
