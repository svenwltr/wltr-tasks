requirejs.config({
	baseUrl : "app/",
	paths : {
		html : "../html",
		data : "data",
		ui : "ui",

		flight : 'bower_components/flight',
		bootstrap : 'bower_components/bootstrap',
		es5shim : 'bower_components/es5-shim',
		jquery : 'bower_components/jquery/dist/jquery',
		moment : 'bower_components/moment/moment',
		'moment-lang' : 'bower_components/moment/lang',

	},
	
});

require([ 'flight/lib/debug' ], function(DEBUG) {
	DEBUG.events.logAll();
	DEBUG.enable(true);

	require([ 'main' ], function(main) {
		main.attachTo(document);
	});
	
});
