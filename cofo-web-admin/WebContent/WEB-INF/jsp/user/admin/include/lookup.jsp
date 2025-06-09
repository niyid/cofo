<%@ include file="/common/taglibs.jsp"%>

<table class="inputform">
	<colgroup>
		<col width="100%" />
	</colgroup>
	<thead>
		<tr>
			<td><label>Identifier</label></td>
		</tr>
	</thead>
	<s:iterator value="lookups" status="status">
		<tr>
			<td><s:textfield name="lookups[%{#status.index}].identifier" value="%{identifier}" label="Identifier" /></td>
		</tr>
	</s:iterator>
</table>