define(function(require) {
	
	return {
		parseDuration : function (value) {
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

		}
	};
	
});