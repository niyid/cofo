<%@ include file="/common/taglibs.jsp"%>
<table style="width:1150px;">
	<tr>
		<security:authorize access="hasRole('ROLE_SUPER_OPERATOR')">
			<td>
				<span style="white-space: nowrap;">
					<a href="<s:url action="user/delegate" />">Delegate</a>&nbsp;
				</span>
			</td>
			<td width="20" align="center"></td>
		</security:authorize>
		<security:authorize access="hasRole('ADMIN')">
			<td>
				<span style="white-space: nowrap;">
					<a href="<s:url action="/user" namespace="/admin" />">Search</a>&nbsp;
				</span>
			</td>
			<td width="20" align="center"></td>
		</security:authorize>
		<security:authorize access="hasRole('ROLE_USER')">
			<td align="right">
				<span style="white-space: nowrap;">
					<a href="<s:url value="/j_spring_security_logout" />">Logout</a>
				</span>
			</td>		
		</security:authorize>
	</tr>
</table>