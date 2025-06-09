<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>Admin Dashboard</title>
</head>
<body>
	<table>
		<tr>
			<td>
				<s:url var="projectFormUrl" action="projectEdit" namespace="/ajax" />
				<sj:div id="projectFormDiv" href="%{projectFormUrl}" reloadTopics="reloadProjectDiv" />
			</td>
		</tr>
	</table>
<script type="text/javascript" >	
	$.subscribe('refreshProjectDiv', function(event,data) {
        $('#projectFormDiv').publish('reloadProjectDiv');
	});
	
</script>
</body>
</html>