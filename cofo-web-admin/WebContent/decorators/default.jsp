<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<sj:head jqueryui="true" jquerytheme="flick" />
        <!-- This files are needed for AJAX Validation of XHTML Forms -->
        <script language="JavaScript" src="${pageContext.request.contextPath}/struts/utils.js" type="text/javascript"></script>
        <script language="JavaScript" src="${pageContext.request.contextPath}/struts/xhtml/validation.js" type="text/javascript"></script>
        <title><decorator:title default="Untitled page" /> | <fmt:message key="webapp.name" /></title>
<%@ include file="/common/meta.jsp"%>
<decorator:head />
<sb:head />
</head>
<body <decorator:getProperty property="body.id" writeEntireProperty="true"/> <decorator:getProperty property="body.class" writeEntireProperty="true"/>>

<div style="margin-bottom: 10px;" class="noprint">
	<div style="margin: 0px 10px 0px 10px;">
		<div class="noprint" style="border-top: solid 1px black; background-color: #ecf1f8; color: #4682b4; min-height: 16px; padding: 3px 10px 2px 10px; font-weight: bold; font-family: 'calibri', Garamond, 'Comic Sans';">
			<table width="100%">
				<tr>
					<td style="vertical-align: top;">
						<a href="<c:url value="/" />"><img src="<c:url value='/img/bayelsa-logo.png'/>" alt="Logo" style="float: left; width: 106px; height: 47px;" /></a>
					</td>
					<td style="vertical-align: top;">
						<h1 style="margin: 0px 0px 3px 0px; padding: 0px; font-size: 3.5em;">C-of-O Application &amp; Approval Manager</h1>
					</td>	
					<td align="right">
						<div>
							<table>
								<tr>
									<td>
										<div style="float: right; font-size: medium;"><s:property value="user.fullName" /></div>
									</td>
								</tr>
							</table>
						</div>
					</td>
					<td align="right">
						<security:authorize access="hasAnyRole('ROLE_DATA_ENTRY','ROLE_SCHEME_OFFICER','ROLE_ES_LUAC','ROLE_SURVEYOR_GENERAL','ROLE_SSA_LANDS','ROLE_PS_LANDS','ROLE_GOVERNOR','ROLE_DEPUTY_REGISTRAR','ROLE_REGISTRAR')">
							<a href="<c:url value="/j_spring_security_logout" />"><img src="<c:url value='/img/logout.png'/>" alt="Logout" style="float: left; width: 48px; height: 48px;" /></a>
						</security:authorize>
					</td>		
				</tr>
			</table>		
		</div>
		<div id="main" style="margin: 20px 0px 0px 20px;">
			<decorator:body />
		</div>	
	</div>
</div>
<jsp:include page="/common/footer.jsp" />
</body>
</html>
