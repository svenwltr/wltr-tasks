'use strict';

$(function() {
	$.ajax({
		url : "/api/session/",
		complete : function(jqXHR, textStatus) {
			if (jqXHR.status != 200)
				document.location = '/login.html';
			else
				loadTasks();

		}
	});
});

function loadTasks() {
	$.ajax({
		url : '/api/tasks/',
		success : function(data) {
			printTasks(data);
			// window.setTimeout(loadTasks, 5000);
		}
	});

};

function printTasks(tasks) {
	var $new = buildTaskGroup();
	$new.attr('id', 'task-list');

	tasks.forEach(function(task) {
		var item = buildTaskItem();

		task.executionDiff = Date.now() - task.lastExecution;
		task.score = task.executionDiff / task.interval * 100;

		item.$title.text(task.title);
		item.$description.text(task.description);
		item.$score.text(Math.round(task.score));

		item.$root.click(function() {
			clickTask(item, task);
		});

		$new.append(item.$root);

	});

	$('#task-list').replaceWith($new);

}

function buildTaskGroup() {
	var $group = $('<div>');
	$group.addClass("list-group");
	return $group;

}

function buildTaskItem() {
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

function clickTask(item, task) {
	deselectTasks();

	$('#inputId').val(task.id);
	$('#inputTitle').val(task.title);
	$('#inputDescription').val(task.description);
	$('#inputInterval').val(moment.duration(task.interval).humanize());

	item.$mark.show();
}

$(function() {
	$(document).keyup(function(e) {
		if (e.keyCode == 27) /* ESC */
			deselectTasks();
	});
});

function deselectTasks() {
	$('.task-mark').hide();

	$('#inputId').val('');
	$('#inputTitle').val('');
	$('#inputDescription').val('');
	$('#inputInterval').val('');

}

$(function() {
	$('#inputSubmit').click(function() {
		var task = {};
		task.id = $('#inputId').val();
		task.title = $('#inputTitle').val();
		task.description = $('#inputDescription').val();
		task.interval = parseDuration($('#inputInterval').val());
		
		$.ajax({
			url : '/api/tasks/',
			type : 'POST',
			contentType : 'application/json',
			data : JSON.stringify(task)
		});
		
	});
});

function parseDuration(value) {
	var args = [];

	value.split(/(\d+|\w+)/).forEach(function(s) {
		s = s.trim();
		if (!s)
			return;

		var n = parseInt(s);

		if (n)
			args.push(n);
		else
			args.push(moment.normalizeUnits(s));

	});

	var units = {
		millisecond : 0,
		second : 0,
		minute : 0,
		hour : 0,
		day : 0,
		week : 0,
		month : 0,
		year : 0
	};

	while (args.length > 0) {
		var number = 0;

		while (args.length > 0) {
			var t = args.shift();
			if (!isNaN(t)) {
				number = t;
				break;
			}
		}

		while (args.length > 0) {
			var t = args.shift();
			if (t in units) {
				units[t] = number;
				break;
			}
		}
	}
	
	var result = 0;
	result += units.millisecond;
	result += units.second * 1000;
	result += units.minute * 1000 * 60;
	result += units.hour   * 1000 * 60 * 60;
	result += units.day    * 1000 * 60 * 60 * 24;
	result += units.week   * 1000 * 60 * 60 * 24 * 7;
	result += units.month  * 1000 * 60 * 60 * 24 * 30;
	result += units.year   * 1000 * 60 * 60 * 24 * 365;
	
	return result;
	
}