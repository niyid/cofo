<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<sx:head />
<sj:head jqueryui="true" jquerytheme="flick"  />
        <!-- This files are needed for AJAX Validation of XHTML Forms -->
        <script language="JavaScript" src="${pageContext.request.contextPath}/struts/utils.js" type="text/javascript"></script>
        <script language="JavaScript" src="${pageContext.request.contextPath}/struts/xhtml/validation.js" type="text/javascript"></script>
        <title><decorator:title default="Untitled page" /> | <fmt:message key="webapp.name" /></title>
<%@ include file="/common/meta.jsp"%>
<decorator:head />
</head>
<body <decorator:getProperty property="body.id" writeEntireProperty="true"/> <decorator:getProperty property="body.class" writeEntireProperty="true"/>>
<div style="margin: 10px 10px 0px 10px;">
<div id="main"><%@ include file="/common/messages.jsp" %> <decorator:body /></div>
</div>
</body>
</html>
