<%@ include file="/common/taglibs.jsp"%>

<table>
<s:iterator value="photoFiles">
<tr>
	<td>
		<img style='border: solid 1px #4f81bd;' src='<s:url action="imageDisplay" namespace="/download"><s:param name="fileId" value="%{id}" /></s:url>' />
	</td>
</tr>
</s:iterator>
</table>
