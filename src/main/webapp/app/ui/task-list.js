define(function(require) {
	var defineComponent = require('flight/lib/component');

	return defineComponent(list);

	function list() {

		this.defaultAttrs({
			itemSelector : '.list-group-item',
		});

		this.provideList = function(event, data) {
			var $list = createTaskList();

			data.tasks.forEach(function(task) {
				var meta = createTaskItem();

				meta.$title.text(task.title);
				meta.$description.text(task.description);
				if (task.score == Infinity)
					meta.$score.html('&infin;');
				else
					meta.$score.text(Math.round(task.score));

				$list.append(meta.$root);

				meta.$root.attr('data-task-id', task.id);
				meta.$root.data('task', task);
				meta.$root.data('meta', meta);

			}, this);

			this.$node.html($list);

		};

		this.deselectTask = function() {
			this.select('itemSelector').each(function(i, el) {
				$(el).data('meta').$mark.hide();
			});
		};

		this.selectTask = function(event, task) {
			this.deselectTask();
			var $item = $('[data-task-id="' + task.id + '"]');
			var meta = $item.data('meta');

			meta.$mark.show();

		};

		this.onItemClick = function(event, data) {
			var $el = $(data.el);
			var $mark = $el.data('meta').$mark;

			if ($mark.is(':visible'))
				this.trigger(document, 'ui.task.deselect');
			else
				this.trigger(document, 'ui.task.select', $el.data('task'));

		};

		this.after('initialize', function() {
			this.on(document, 'data.task.provideList', this.provideList);
			this.on(document, 'ui.task.deselect', this.deselectTask);
			this.on(document, 'ui.task.select', this.selectTask);

			this.on('click', {
				itemSelector : this.onItemClick,
			});

			this.$node.fadeIn();

		});
	}

	function createTaskList() {
		var $list = $('<div>');
		$list.addClass("list-group");
		return $list;

	}

	function createTaskItem() {
		var item = {};

		item.$root = $("<div>");
		item.$root.addClass("list-group-item");

		item.$score = $("<span>");
		item.$score.addClass("label label-primary pull-right");

		item.$mark = $("<span>");
		item.$mark.addClass('glyphicon glyphicon-chevron-right task-mark');
		item.$mark.hide();

		item.$title = $("<h4>");

		item.$description = $("<p>");

		item.$root.append(item.$score);
		item.$root.append(item.$mark);
		item.$root.append(' ');
		item.$root.append(item.$title);
		item.$root.append(item.$description);

		return item;

	}

});