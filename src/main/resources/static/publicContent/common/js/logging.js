var logging=(function() {
	var isLoggingEnabled=false;
	
	var enable=function() {
		console.info("Logging is enabled");
		isLoggingEnabled=true
	}
	
	var disable=function() {
		console.info("Logging is disabled");
		isLoggingEnabled=false
	}
	
	var log=function(message) {
		if(isLoggingEnabled) {
			console.log(message);
		}
	}
	
	var setLoggingSwitch=function(switchValue) {
		if(switchValue) {
			enable();
		} else {
			disable();
		}
	}
	
	return {
		enable : enable,
		disable : disable,
		setLoggingSwitch : setLoggingSwitch,
		log : log
	}
})();