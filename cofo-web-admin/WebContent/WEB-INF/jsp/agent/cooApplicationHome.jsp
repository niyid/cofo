<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>C-of-O Application</title>
</head>
<body>
	<table>
		<tr>
			<td width="40%">
				<s:url var="cooApplicationRecordFormUrl" action="cooApplicationRecord" namespace="/agent/ajax" />
				<sj:div id="cooApplicationRecordFormDiv" href="%{cooApplicationRecordFormUrl}" />
			</td>
			<td>
				<s:url var="cooApplicationListUrl" action="cooApplicationList" namespace="/agent/ajax" />
				<sj:div id="cooApplicationListDiv" href="%{cooApplicationListUrl}" reloadTopics="reloadCooApplicationListDiv" />
			</td>			
		</tr>
	</table>
<script type="text/javascript" >
	$.subscribe('refreshCooApplicationListDiv', function(event,data) {
        $('#cooApplicationListDiv').publish('reloadCooApplicationListDiv');
	});
</script>	
</body>
</html>