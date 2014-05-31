requirejs.config({
	baseUrl : "app/",
	paths : {
		html : "../html",
		data : "data",
		ui : "ui",

		flight : 'bower_components/flight',
		bootstrap : 'bower_components/bootstrap/dist/js/bootstrap.min',
		es5shim : 'bower_components/es5-shim',
		jquery : 'bower_components/jquery/dist/jquery',
		moment : 'bower_components/moment/moment',
		'moment-lang' : 'bower_components/moment/lang',

	},

	shim : {
		jquery : {
			deps : ['es5shim/es5-shim.min', 'es5shim/es5-sham.min'],
		},
		bootstrap : {
			deps : ['jquery'],
		},
	},
	
});

require([ 'flight/lib/debug', 'bootstrap' ], function(DEBUG) {
	DEBUG.events.logAll();
	DEBUG.enable(true);

	require([ 'main' ], function(main) {
		main.attachTo(document);
	});
	
});
