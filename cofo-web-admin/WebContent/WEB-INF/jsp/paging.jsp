<%@ include file="/common/taglibs.jsp"%>
<c:if test="${0+param.page <= 0+param.pages}">
	<div style="margin: 0px 0px 2em; text-align: center;"><s:text name="search.paging.title" />: <c:if test="${param.page>10}">
		<a href="?${param.href}" style="color: Blue; font-weight: bold;"><s:text name="search.paging.first" /></a>
	</c:if><c:if test="${param.page>1}">
		<a href="?startAt=${(param.page-2)*param.pageSize}&${param.href}" style="color: Blue; font-weight: bold;"><s:text name="search.paging.previous" /></a>
	</c:if> <c:forEach var="i" begin="${param.page>10 ? param.page-10 : 1}" end="${param.pages-param.page>10 ? param.page+9 : param.pages}">
		<a href="?startAt=<c:out value="${(i-1)*param.pageSize}" />&${param.href}" <c:if test="${param.page==i}"> style="color: Red"</c:if>><c:out value="${i}" /></a>
	</c:forEach> <c:if test="${0+param.page<0+param.pages}">
		<a href="?startAt=${(param.page)*param.pageSize}&${param.href}" style="color: Blue; font-weight: bold;"><s:text name="search.paging.next" /></a>
	</c:if><c:if test="${0+param.page<0+param.pages}">
		<a href="?startAt=${(param.pages-1)*param.pageSize}&${param.href}" style="color: Blue; font-weight: bold;"><s:text name="search.paging.last" /></a>
	</c:if></div>
</c:if>