<%@ include file="/common/taglibs.jsp"%>

<div class="container">
	<h2>Work-flow Approval List</h2>
	<div id="liveApprovalListInnerDiv" class="table-responsive">
		<s:if test="processFlowItems == null || processFlowItems.isEmpty()">
			<div class="alert alert-info">
				No work-flow items awaiting approval at this time.
			</div>
		</s:if>
		<s:else>
			<table class="table table-striped">
				<thead>
					<tr>
						<th>ID</td>
						<th>Scheduled Date</th>
						<th></th>
					</tr>
					</thead>
					<tbody>
						<s:iterator value="processFlowItems" var="resultRow">
							<tr>
								<td><s:property value="id" /></td>
								<td><s:date name="scheduledDate" format="dd/MM/yyyy" /></td>
								<td>
									<s:url var="detailUrl" action="approvalDetail!select" namespace="/agent/ajax" escapeAmp="false">
										<s:param name="processFlowItemId" value="%{id}" />
									</s:url>
									<sj:a button="false" buttonIcon="ui-icon-gear" href="%{detailUrl}" openDialog="approvalDetailDialog">Details</sj:a>
								</td>
		
							</tr>
						</s:iterator>
					</tbody>
			</table>
		</s:else>
	</div>
</div>
