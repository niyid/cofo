<script type="text/javascript">
			$(function() {
				if (navigator.geolocation) {
				    navigator.geolocation.getCurrentPosition(showPosition, showError);
				}
			});
			
			function showPosition(position) {
				$('#locn').val('');
				$('#lat').val(position.coords.latitude);
				$('#lon').val(position.coords.longitude);
			}
			
			function showError(error) {
			  switch(error.code) 
			    {
			    case error.PERMISSION_DENIED:
			      alert("User denied the request for Geolocation.");
			      break;
			    case error.POSITION_UNAVAILABLE:
			      alert("Location information is unavailable.");
			      break;
			    case error.TIMEOUT:
			      alert("The request to get user location timed out.");
			      break;
			    case error.UNKNOWN_ERROR:
			      alert("An unknown error occurred.");
			      break;
			    }
			 }
		</script>