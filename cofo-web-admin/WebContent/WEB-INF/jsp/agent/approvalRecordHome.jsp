<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>C-of-O Approval Workflow</title>
</head>
<body>
	<table>
		<tr>
			<td>
				<s:url var="approvalRecordFormUrl" action="approvalRecord" namespace="/agent/ajax" />
				<sj:div id="approvalRecordFormDiv" href="%{approvalRecordFormUrl}" />
			</td>
		</tr>
	</table>
</body>
</html>