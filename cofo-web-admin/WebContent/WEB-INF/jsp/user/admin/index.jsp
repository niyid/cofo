<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>
<head>
<title>Users</title>
</head>
<body>
<s:form method="get" action="users!search" namespace="/admin">
	Search: <s:textfield name="filter" value="%{filter}" cssStyle="width: 100px;" /> <s:submit value="Search" />
</s:form>

<a href="<s:url action="user/user!input" namespace="/admin" />">Add New User</a>
<a href="<s:url action="users" namespace="/admin" />">List all users</a>

<s:if test="paged.totalHits>0">
	<s:include value="/WEB-INF/jsp/paging.jsp">
		<s:param name="page" value="paged.page" />
		<s:param name="pages" value="paged.pages" />
		<s:param name="pageSize" value="paged.pageSize" />
		<s:param name="href" value="%{''}" />
	</s:include>

	<table class="data-listing">
		<colgroup>
			<col width="20%" />
			<col width="20%" />
			<col />
			<col width="20%" />
		</colgroup>
		<thead>
			<tr>
				<td>Name</td>
				<td>Mail</td>
				<td>Login</td>
				<td>Failed</td>
				<td class="ar">Tools</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="paged.results">
				<tr class="<s:if test="status==@com.vasworks.security.model.UserStatus@DISABLED">row-error</s:if>">
					<td><s:property value="lastName" />, <s:property value="firstName" /></td>
					<td><s:property value="mail" /></td>
					<td><s:date name="lastLogin" format="%{getText('date.format')} HH:mm:ss" /></td>
					<td><s:date name="lastLoginFailed" format="%{getText('date.format')} HH:mm:ss" /></td>
					<td class="ar"><s:url var="editUrl" action="user/user!switchto">
						<s:param name="id" value="%{id}" />
					</s:url> <s:a href="%{editUrl}">Switch to</s:a> <s:url var="removeUrl" action="user/user!delete">
						<s:param name="id" value="%{id}" />
					</s:url> <s:a onclick="javascript: return window.confirm('Are you sure you want to delete user?');" href="%{removeUrl}">Remove</s:a> <s:url var="editUrl"
						action="user/user!input">
						<s:param name="id" value="%{id}" />
					</s:url> <s:a href="%{editUrl}">Edit</s:a></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</s:if>
<s:else>No results!</s:else>
</body>
</html>