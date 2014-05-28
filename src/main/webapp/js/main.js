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
			//window.setTimeout(loadTasks, 5000);
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
		
		item.$root.click(function(){
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
	
	$('#inputTitle').val(task.title);
	$('#inputDescription').val(task.description);
	$('#inputInterval').val(moment.duration(task.interval).humanize());
	
	item.$mark.show();
}

$(function(){
	$(document).keyup(function(){
		deselectTasks();
	});
});

function deselectTasks() {
	$('.task-mark').hide();

	$('#inputTitle').val('');
	$('#inputDescription').val('');
	$('#inputInterval').val('');

}

