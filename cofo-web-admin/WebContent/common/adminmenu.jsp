<%@ include file="/common/taglibs.jsp"%>

<a style="margin-right: 10px;" href="<c:url value="/" />">Public site</a>

<!-- Standard -->
<a style="margin-right: 10px;" href="<s:url namespace="/admin" action="users" />">Users</a>
<security:authorize access="hasRole('ROLE_USER')">
	<a style="margin-right: 10px;" href="<c:url value='/j_spring_security_logout' />" title="Log out">Log out</a>
</security:authorize>
