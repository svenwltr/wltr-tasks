define(function(require) {

	var Task = require('utils/task');

	var defineComponent = require('flight/lib/component');
	return defineComponent(task);

	function task() {
		this.requestList = function() {
			var parent = this;
			$.ajax({
				url : '/api/tasks/',
				success : function(data) {
					var tasks = [];
					data.forEach(function(taskData) {
						tasks.push(new Task(taskData));
					});

					parent.trigger('data.task.provideList', {
						tasks : tasks,
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

		this.deleteTask = function(event, task) {
			var self = this;
			$.ajax({
				url : '/api/tasks/'+task.id+'/',
				type : 'DELETE',
				success : function() {
					self.trigger('data.task.requestList');
				}
			});

		};

		this.after('initialize', function() {
			this.on('data.task.requestList', this.requestList);
			this.on('data.task.save', this.saveTask);
			this.on('data.task.delete', this.deleteTask);

		});
	}
});
