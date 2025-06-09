<%@ include file="/common/taglibs.jsp"%>


<table class="inputform">
	<colgroup>
		<col width="50%" />
		<col width="50%" />
	</colgroup>
	<thead>
		<tr>
			<td><label>Role</label></td>
			<td><label>Application</label></td>
		</tr>
	</thead>
	<s:iterator value="roles" status="status">
		<tr>
			<td><s:textfield name="roles[%{#status.index}].role" value="%{role}" label="Role" /></td>
			<td><s:property value="application" /></td>
		</tr>
	</s:iterator>
</table>
