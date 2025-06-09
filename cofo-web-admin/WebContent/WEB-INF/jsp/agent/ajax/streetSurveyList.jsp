<%@ include file="/common/taglibs.jsp"%>

<table>
	<tr>
		<td class="windowHeader">
			<div>
				Surveys
			</div>
		</td>
	</tr>
	<tr>
		<td class="windowPanel">
			<div id="surveyInnerDiv" style="border: solid 1px #4f81bd; background: #ffffff; padding: 4px; height: 250px; overflow: auto;">
				<s:if test="surveys == null || surveys.isEmpty()">
					Empty list.
				</s:if>
				<s:else>
				<table class="data-listing">
					<thead>
						<tr>
							<td>Description</td>
							<td>Agent</td>
							<td>Date</td>
							<td>Street List</td>
						</tr>
					</thead>
					<s:iterator value="surveys" var="resultRow">
						<tr>
							<td><s:property value="description" /></td>
							<td><s:property value="user.displayName" /></td>
							<td><s:date name="scheduleDate" format="dd/MM/yyyy HH:mm" /></td>
							<td>
							<table class="data-listing">
								<thead>
									<tr>
										<td>Name</td>
										<td>Surveyed</td>
									</tr>
								</thead>
								<s:iterator value="streets">
									<tr>
										<td><s:property value="description" /></td>
										<td><s:property value="mapped ? 'YES' : 'NO'" /></td>
									</tr>
								</s:iterator>							
							</td>
						</tr>
					</s:iterator>
				</table>
				</s:else>
			</div>
		</td>
	</tr>
</table>