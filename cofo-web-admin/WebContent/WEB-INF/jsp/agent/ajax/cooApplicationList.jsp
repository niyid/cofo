<%@ include file="/common/taglibs.jsp"%>

<div class="container">
	 <h2>C-of-O List</h2>
			<div id="cooApplicationListInnerDiv" class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<th width="15%">ID</th>
							<th width="20%">Type</th>
							<th width="20%">Filed Date</th>
							<th width="20%">Status</th>
							<th width="25%"></th>
						</tr>
					</thead>
					<tbody>
					<s:iterator value="applications">
						<tr>
							<td><s:property value="id" /></td>
							<td><s:property value="cooType" /></td>
							<td><s:date name="acceptanceDate" format="dd/MM/yyyy" /></td>
							<td><s:property value="appStatus" /></td>
							<td>
								<s:url var="certDocListUrl" action="certificateDocList" namespace="/agent/ajax" escapeAmp="false">
									<s:param name="applicationId" value="%{id}" />
								</s:url>
								<sj:a button="false" buttonIcon="ui-icon-gear" href="%{certDocListUrl}" openDialog="certDocListDialog">Details</sj:a>
							</td>
						</tr>
					</s:iterator>
					</tbody>
				</table>
			</div>
</div>