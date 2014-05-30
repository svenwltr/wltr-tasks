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
			progressBarSelector : '.progress-bar',
			progressSelector : '.progress',
			closeSelector : '[data-action="deselect"]',
		});

		this.deselectTask = function() {
			this.$node.slideUp();

		};

		this.selectTask = function(event, task) {

			this.select('idSelector').text(task.id);
			this.select('titleSelector').text(task.title);
			this.select('intervalSelector').text(
					moment.duration(task.interval).humanize());
			this.select('descriptionSelector').text(task.description);

			if (task.score == Infinity) {
				this.select('progressSelector').hide();
			} else {
				this.select('progressSelector').show();
				this.select('scoreSelector').text(Math.round(task.score) + '%');
				this.select('progressBarSelector').css('width',
						task.score + '%');

			}

			this.$node.slideDown();
			
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