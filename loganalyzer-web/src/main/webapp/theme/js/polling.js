(function($) {
	var running = false;
	var request;
	
	$.polling = function(url, callback) {
		setInterval(function() {
			if (running === false) {
				running = true;
				poll(url, callback);
			}
		}, 1000);
	};
	
	function poll(url, callback) {
		if (request) {
			request.abort(); // abort any pending request
		}
		request = $.ajax({
			url: url,
			type: "get",
			success: callback
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.log("Error: " + textStatus, errorThrown);
		}).always(function() {
			running = false;
		});
	}
})(jQuery);
