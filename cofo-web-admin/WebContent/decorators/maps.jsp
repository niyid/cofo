<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<sjm:head jqueryui="true" jquerytheme="flick" />
	<script src="http://maps.google.com/maps/api/js?sensor=false" type="text/javascript"></script>
	<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/script/jquery.ui.map.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/script/jquery.ui.map.services.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/script/jquery.ui.map.extensions.js"></script>
    <title><decorator:title default="Untitled page" /> | <fmt:message key="webapp.name" /></title>
	<decorator:head />
	<%@ include file="/common/meta.jsp"%>
</head>
<body <decorator:getProperty property="body.id" writeEntireProperty="true"/> <decorator:getProperty property="body.class" writeEntireProperty="true"/>>
       
       <div data-role="page" id="main" data-theme="a">
            <div data-role="header">                
				 <table style="width: 100%">
					<tr>
						<td style="width: 30%;">
							<table>
								<tr>
									<td style="vertical-align: top; width: 44px;" rowspan="2"><a href="<c:url value="/" />"><img src="<c:url value='/img/swappaws_icon.png'/>" alt="Logo"
										style="float: left; width: 44px; height: 44px;" /></a>								
									</td>
									<td style="vertical-align: top;">
										<table>
											<tr>
												<td style="vertical-align: top;">
													<img src="<c:url value='/img/swappaws_banner.png'/>" alt="Banner" style="float: left; width: 150px; height: 22px;" />
												</td>
											</tr>
											<tr>
												<td><span class="slogan">...mine for thine.</span></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
						<td style="width: 39%; text-align: center;"><h1><decorator:title default="Untitled page" /></h1></td>						
						<td style="width: 31%; text-align: right;">
							<security:authorize access="hasRole('ROLE_USER')">
								<span style="white-space: nowrap;">
									<s:url var="logoutUrl" value="/j_spring_security_logout" />
									<sjm:a id="logoutButton" href="%{#logoutUrl}" button="true" buttonIcon="back" formIds="loginForm">Logout</sjm:a>
								</span>
							</security:authorize>
						</td>								
					</tr>
				</table>               
            </div><!-- /header -->
 
            <div data-role="content" id="mainDiv">
                <decorator:body />
            </div><!-- /content -->
 
            <div data-role="footer">
            </div><!-- /footer -->
        </div><!-- /page -->
</body>
</html>
