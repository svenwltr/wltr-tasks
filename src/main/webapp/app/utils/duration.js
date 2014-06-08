define(function(require) {
	
	var moment = require('moment');
	
	var UNITS = ['years', 'months', 'weeks', 'days', 'hours', 'minutes', 'seconds', 'ms'];

	function parse(value) {
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
		result += units.hour * 1000 * 60 * 60;
		result += units.day * 1000 * 60 * 60 * 24;
		result += units.week * 1000 * 60 * 60 * 24 * 7;
		result += units.month * 1000 * 60 * 60 * 24 * 30;
		result += units.year * 1000 * 60 * 60 * 24 * 365;

		return result;
		
	};
	
	function humanize(time) {
		if(typeof yourVariable !== 'object' || Object.getPrototypeOf(time).constructor.name != 'Duration')
			time = moment.duration(time);
		
		var copy = moment.duration(time.asMilliseconds());
		var result = [];
		
		UNITS.forEach(function(unit){
			var value = Math.floor(copy.as(unit));
			
			if(value > 1) {
				copy = copy.subtract(value, unit);
				result.push(value + ' ' + unit);
			}
		}, this);
		
		if(result.length == 1)
			return result.pop();
		
		else if(result.length == 2)
			return result.join(' and ');
		
		else {
			var last = result.pop();
			return result.join(', ') + ' and ' + last;
			
		}
		
	};
	
	return {
		parse : parse,
		humanize : humanize,
	};
	
});