//main function to be called on submit
function processData(form) {
	var json = $(form).serialize();

	console.log('json=' + json);
	if (navigator.onLine) {
		sendDataToServer(json);
	} else {
		saveDataLocally(json);
	}
}

// called on submit if device is online from processData()
function sendDataToServer(dataString) {
	$.mobile.showPageLoadingMsg();
	var myRequest = new XMLHttpRequest();

	myRequest.onreadystatechange = function() {

		if (myRequest.readyState == 4 && myRequest.connStatus == 200) {

			console.log('Sent to server: ' + dataString + '');
			window.localStorage.removeItem(dataString);
		} else if (myRequest.readyState == 4 && myRequest.connStatus != 200) {

			console.log('Server request could not be completed');
			saveDataLocally(dataString);
		}
	};

	$.mobile.hidePageLoadingMsg();
	// myRequest.open("GET", "", true);
	// myRequest.send();
}

// called on submit if device is offline from processData()
function saveDataLocally(dataString) {

	var timeStamp = new Date();
	timeStamp.getTime();

	try {
		localStorage.setItem(timeStamp, dataString);
		console.log('Saved locally: ' + dataString + '');
	} catch (e) {

		if (e == QUOTA_EXCEEDED_ERR) {
			console.log('Quota exceeded!');
		}
	}

	console.log(dataString);

	var length = window.localStorage.length;
	$('#local-count').text(length);
}

// called if device goes online or when app is first loaded and device is online
// only sends data to server if locally stored data exists
function sendLocalDataToServer() {

	$('#connStatus').attr('class', 'online');
	$('#connStatus').text('Online');

	var i = 0, dataString = '';

	while (i <= window.localStorage.length - 1) {

		dataString = localStorage.key(i);

		if (dataString) {
			sendDataToServer(localStorage.getItem(dataString));
			window.localStorage.removeItem(dataString);
		} else {
			i++;
		}
	}

	$('#local-count').text(window.localStorage.length);
}

// called when device goes offline
function notifyUserIsOffline() {
	$('#connStatus').attr('class', 'offline');
	$('#connStatus').text('Offline');
}

// called when DOM has fully loaded
function loaded() {
	console.log('Loaded!');
	console.log('Online: ' + navigator.onLine);
	// update local storage count
	var length = window.localStorage.length;
	$('#local-count').text(length);

	// if online
	if (navigator.onLine) {
		console.log('ConnStatus: ' + $('#connStatus').attr('class'));
		// update connection connStatus
		$('#connStatus').attr('class', 'online');
		$('#connStatus').text('Online');
		console.log('ConnStatus: ' + $('#connStatus').attr('class'));

		// if local data exists, send try post to server
		if (length !== 0) {
			sendLocalDataToServer();
		}
	}

	// listen for connection changes
	window.addEventListener('online', sendLocalDataToServer, false);
	window.addEventListener('offline', notifyUserIsOffline, false);
	
//	$("#enumerationFilterForm").submit(function( event ) {
//		processData(this);
//		event.preventDefault();
//	});
}

$(document).on('pageinit', function(){
	loaded();
});
window.addEventListener('load', loaded, true);