<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>User preferences</title>
<s:head />
</head>
<body>
<s:form method="POST" action="preferences!store">
<table>
	<s:iterator status="status" value="settings.keySet()">
		<tr>
			<td><s:property /></td>
			<td><s:textfield name="settings['%{top}']" value="%{settings[top]}" /></td>
		</tr>
	</s:iterator>
</table>
<s:submit value="Store" />
</s:form>
<h4>Direct</h4>
<s:property value="settings['test']" />
</body>
</html>