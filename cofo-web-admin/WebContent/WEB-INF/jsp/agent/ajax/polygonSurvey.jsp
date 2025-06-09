<%@ include file="/common/taglibs.jsp"%>

	<div id="mapCanvas" class="item rounded" style="width:600px; height:400px;"></div>
		<script type="text/javascript">
			$(function() {
				$('#mapCanvas').gmap().bind('init', function(evt, map) {
					$('#mapCanvas').gmap('getCurrentPosition', function(position, status) {
						if ( status === 'OK' ) {
							//var clientPosition = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
							var clientPosition = new google.maps.LatLng(7.435604782283255, 3.9171644300222397);
							$('#mapCanvas').gmap('addMarker', {'position': clientPosition, 'bounds': true, 'draggable' : true}, function(map, marker) {								
								findLocation(marker.getPosition(), marker);
							}).dragend( function(event) {
								findLocation(event.latLng, this);
							});														
							
							$('#mapCanvas').gmap('option', 'zoom', 20);
							drawAllPolygons(clientPosition);
						}
					});   
				});
			});
			
			function findLocation(location, marker) {
				$('#mapCanvas').gmap('search', {'location': location}, function(results, status) {
					if ( status === 'OK' ) {
						var locnStr = '';
						$.each(results[0].address_components, function(i,v) {
							if ( v.types[0] == "sublocality") {
								locnStr += (v.long_name);
							} else if ( v.types[0] == "administrative_area_level_1" || 
								 v.types[0] == "administrative_area_level_2" ) {
								locnStr += (', ' + v.long_name);
							} else if ( v.types[0] == "country") {
								locnStr += (', ' + v.long_name);
							}
							
							if ( v.types[0] == "route") {
								$('#streetName').val(v.long_name);
								$('#streetNameText').html(v.long_name);
							}
						});
						$('#locn').val(locnStr);
						$('#lat').val(location.lat());
						$('#lon').val(location.lng());
						marker.setTitle(locnStr);
					}
				});
			}
			
			<s:property value='polygonScript' />
		</script>
		<s:include value="geolocator.jsp" />
			