define(function(require) {
	var DurationUtils = require('utils/duration');
	var moment = require('moment');

	var defineComponent = require('flight/lib/component');

	return defineComponent(form);

	function form() {

		this.defaultAttrs({
			idSelector : '[name="id"]',
			titleSelector : '[name="title"]',
			descriptionSelector : '[name="description"]',
			intervalSelector : '[name="interval"]',
			submitSelector : '[type="submit"]',
		});

		this.setForm = function(task) {
			this.select('idSelector').val(task.id);
			this.select('titleSelector').val(task.title);
			this.select('intervalSelector').val(task.getHumanInterval());
			this.select('descriptionSelector').val(task.description);
		};

		this.deselectTask = function() {
			this.setForm({
				id : '',
				title : '',
				interval : '',
				description : '',
			});
			this.$node.show();

		};

		this.selectTask = function(event, task) {
			this.$node.hide();

		};

		this.editTask = function(event, task) {
			this.setForm(task);
			this.$node.show();

		};

		this.submitForm = function(event) {
			var task = {
				id : this.select('idSelector').val(),
				title : this.select('titleSelector').val(),
				description : this.select('descriptionSelector').val(),
				interval : DurationUtils.parse(this.select('intervalSelector')
						.val()),
			};

			this.trigger(document, 'data.task.save', task);

		};

		this.after('initialize', function() {
			this.on(document, 'ui.task.deselect', this.deselectTask);
			this.on(document, 'ui.task.select', this.selectTask);
			this.on(document, 'ui.task.edit', this.editTask);

			this.on('click', {
				'submitSelector' : this.submitForm,
			});

			this.$node.fadeIn();

		});
	}

});
