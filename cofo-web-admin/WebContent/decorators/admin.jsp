<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title><decorator:title default="Untitled page" /> | <fmt:message key="webapp.name" /> Administration</title>
<%@ include file="/common/meta.jsp"%>
<decorator:head />
</head>
<body <decorator:getProperty property="body.id" writeEntireProperty="true"/> <decorator:getProperty property="body.class" writeEntireProperty="true"/>>

<div style="margin-bottom: 10px;" class="noprint">
<table style="width: 100%">
	<col width="150px" />
	<col />
	<tr>
		<td style="vertical-align: top; padding-right: 10px;"><a href="<c:url value="/admin/" />"><img src="<c:url value='/img/logo.png'/>" alt="Logo" style="float: left; width: 100px; height: 67px;" /></a></td>
		<td style="vertical-align: top;">

		<h1 style="margin: 0px 0px 3px 0px; padding: 0px; font-size: 3.5em;"><fmt:message key="webapp.name" /> Administration</h1>
		<%--<div style="margin: 20px 0px 5px 0px; float: left; width: 300px;">
		<form method="get" action="<s:url action='search' />"><input name="q" style="width: 200px;" value="" /> <input type="submit" value="Search" /></form>
		</div> --%>
		<div style="margin: 1pt 0px 5px 0px; float: left;">
		<div style="margin-bottom: 5px;">&nbsp;</div>
		<div><jsp:include page="/common/adminmenu.jsp" /></div>
		</div>
		</td>
	</tr>
</table>
</div>

<div class="noprint" style="border-top: solid 1px black; background-color: #4fbd8b; color: #ffffff; min-height: 16px; padding: 3px 10px 2px 10px; font-weight: bold;"><div style="float: right"><s:property value="user.fullName" /></div><decorator:title
	default="Untitled page" /></div>

<div style="margin: 10px 10px 0px 10px;">

<div id="main"><%@ include file="/common/messages.jsp"%> <decorator:body /></div>
</div>

<jsp:include page="/common/footer.jsp" />
<script type="text/javascript" src="<s:url value="/script/jquery-idleTimeout.js" />"></script>
<script type="text/javascript" src="<s:url value="/script/gears/gears_init.js" />"></script>
</body>
</html>
