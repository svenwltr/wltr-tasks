define(function(require) {

	var moment = require('moment');
	var DurationUtils = require('utils/duration');

	var Task = function(data) {
		this.id = data.id;
		this.userToken = data.userToken;
		this.title = data.title;
		this.description = data.description;
		this.lastExecution = data.lastExecution;
		this.interval = data.interval;
	};

	Task.prototype.getLastExecution = function() {
		return moment(this.lastExecution);
	};

	Task.prototype.getNextExecution = function() {
		return moment(this.lastExecution + this.interval);
	};

	Task.prototype.getTimeProgress = function() {
		return Date.now() - this.lastExecution;
	};

	Task.prototype.getInterval = function() {
		return moment.duration(this.interval);
	};
	
	Task.prototype.getHumanInterval = function() {
		return DurationUtils.humanize(this.interval);
	};

	Task.prototype.getExactScore = function() {
		if(this.lastExecution)
			return this.getTimeProgress() / this.interval * 100;
		else
			return Infinity;
	};

	Task.prototype.getScore = function() {
		return Math.round(this.getExactScore());

	};


	return Task;


});
