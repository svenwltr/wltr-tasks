define(function(require) {
	var Utils = require('utils');
	var moment = require('moment');

	var defineComponent = require('flight/lib/component');

	return defineComponent(detail);

	function detail() {

		this.defaultAttrs({
			idSelector : '[data-bind="id"]',
			titleSelector : '[data-bind="title"]',
			descriptionSelector : '[data-bind="description"]',
			intervalSelector : '[data-bind="interval"]',
			scoreSelector : '[data-bind="score"]',
			lastExecutionSelector : '[data-bind="last-execution"]',
			nextExecutionSelector : '[data-bind="next-execution"]',

			progressBarSelector : '.progress-bar',
			progressSelector : '.progress',

			doneSelector : '[data-action="done"]',
			setTimeSelector : '[data-action="set-time"]',
			editSelector : '[data-action="edit"]',
			deleteSelector : '[data-action="delete"]',
			closeSelector : '[data-action="close"]',
		});

		this.deselectTask = function() {
			this.$node.hide();

		};

		this.selectTask = function(event, task) {
			this.$node.data('task', task);
			this.render();
			this.$node.show();
			
		};

		this.update = function(event, data) {
			var old = this.$node.data('task');
			if(!old)
				return;

			var id = old.id;
			data.tasks.forEach(function(task){
				console.log(id, task.id);
				if(id == task.id)
					this.$node.data('task', task);
			}, this);

			this.render();

		};

		this.render = function() {
			var task = this.$node.data('task');

			if(!task)
				return;

			this.select('idSelector').text(task.id);
			this.select('titleSelector').text(task.title);
			this.select('intervalSelector').text(task.getInterval().humanize());
			this.select('descriptionSelector').text(task.description);
			this.select('lastExecutionSelector').text(task.getLastExecution().fromNow()).attr('title', task.getLastExecution().calendar());
			this.select('nextExecutionSelector').text(task.getNextExecution().fromNow()).attr('title', task.getNextExecution().calendar());

			if (task.getScore() == Infinity) {
				this.select('progressSelector').hide();
			} else {
				this.select('progressSelector').show();
				this.select('scoreSelector').text(task.getScore());
				this.select('progressBarSelector').css('width',
						task.getExactScore() + '%');

			}

		};
		
		this.clickedDone = function(event) {
			event.preventDefault();

			this.trigger(document, 'data.task.save', {
				id : this.$node.data('task').id,
				lastExecution : moment().valueOf(),
			});

		};

		this.clickedSetTime = function(event) {
			event.preventDefault();

		};

		this.clickedEdit = function(event) {
			this.trigger(document, 'ui.task.edit', this.$node.data('task'));
			event.preventDefault();

		};

		this.clickedDelete = function(event) {
			event.preventDefault();

		};

		this.clickedClose = function(event) {
			this.trigger(document, 'ui.task.deselect');
			event.preventDefault();
			
		};

		this.after('initialize', function() {
			this.on(document, 'ui.task.deselect', this.deselectTask);
			this.on(document, 'ui.task.select', this.selectTask);
			this.on(document, 'data.task.provideList', this.update);
			this.on(document, 'ui.render', this.render);
			
			this.on('click', {
				'doneSelector' : this.clickedDone,
				'setTimeSelector' : this.clickedSetTime,
				'editSelector' : this.clickedEdit,
				'deleteSelector' : this.clickedDelete,
				'closeSelector' : this.clickedClose,
			});

		});
	}

});
