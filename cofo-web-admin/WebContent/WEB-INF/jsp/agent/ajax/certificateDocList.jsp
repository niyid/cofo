<%@ include file="/common/taglibs.jsp"%>

<div class="container">
	<h2>Required Documents List</h2>
	<div id="certDocListInnerDiv" class="table-responsive">
		<s:if test="certificates == null || certificates.isEmpty()">
			Something is wrong; this should not be empty.
		</s:if>
		<s:else>
			<table class="table table-striped">
				<thead>
					<tr>
						<th width="90%">Description</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
						<s:iterator value="certificates" var="resultRow">
							<tr>
								<td><s:property value="certificateType.description" /></td>
								<td>
									<s:url var="detailUrl" action="documentFileRecord" namespace="/agent/ajax" escapeAmp="false">
										<s:param name="certificateId" value="%{id}" />
										<s:param name="certificateTypeId" value="%{certificateType.id}" />
									</s:url>
									<sj:a button="false" buttonIcon="ui-icon-gear" href="%{detailUrl}" openDialog="addDocumentDialog">Add</sj:a>
								</td>
		
							</tr>
						</s:iterator>
				</tbody>
			</table>
		</s:else>
	</div>
</div>
