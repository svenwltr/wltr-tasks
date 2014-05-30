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
			closeSelector : '[data-action="deselect"]',
		});

		this.deselectTask = function() {
			this.$node.hide();

		};

		this.selectTask = function(event, task) {

			this.select('idSelector').text(task.id);
			this.select('titleSelector').text(task.title);
			this.select('intervalSelector').text(moment.duration(task.interval).humanize());
			this.select('descriptionSelector').text(task.description);
			this.select('lastExecutionSelector').text(moment(task.lastExecution).fromNow()).attr('title', moment(task.lastExecution).calendar());
			this.select('nextExecutionSelector').text(moment(task.nextExecution).fromNow()).attr('title', moment(task.nextExecution).calendar());

			if (task.score == Infinity) {
				this.select('progressSelector').hide();
			} else {
				this.select('progressSelector').show();
				this.select('scoreSelector').text(Math.round(task.score) + '%');
				this.select('progressBarSelector').css('width',
						task.score + '%');

			}

			this.$node.show();
			
		};
		
		this.closeClicked = function() {
			this.trigger(document, 'ui.task.deselect');
			
		};

		this.after('initialize', function() {
			this.on(document, 'ui.task.deselect', this.deselectTask);
			this.on(document, 'ui.task.select', this.selectTask);
			
			this.on('click', {
				'closeSelector' : this.closeClicked,
			});

		});
	}

});
