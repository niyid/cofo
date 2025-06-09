<%@ page language="java" isErrorPage="true"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/struts-tags" prefix="s"%>


<html>
<head>
<title><fmt:message key="errorPage.title" /></title>
<!--<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/theme.css'/>" />
-->
</head>

<body id="error">
<div id="page">
<div id="content" class="clearfix">
<div id="main">
<h1><fmt:message key="errorPage.heading" /></h1>
<%
	if (exception != null) {
%> <pre>
<%
	exception.printStackTrace(new java.io.PrintWriter(out));
%>
</pre> <%
 	} else if (request.getAttribute("javax.servlet.error.exception") != null) {
 %> <pre>
<%
	((Exception) request.getAttribute("javax.servlet.error.exception")).printStackTrace(new java.io.PrintWriter(out));
%>
</pre> <%
 	}
 %>
</div>
</div>
</div>
</body>
</html>
