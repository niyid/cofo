<%@ include file="/common/taglibs.jsp"%>

	<table>
		<tr>
			<td class="windowHeader">Approval Detail</td>
		</tr>
		<tr>
			<td class="windowPanel">
			<div>
				<table>
					<tr>
						<td>
						<div id="liveApprovalItemInnerDiv" style="border: solid 1px #4f81bd; background: #ffffff; padding: 4px; height: 250px; overflow: auto;">
							<s:if test="items == null || items.isEmpty()">
								No schedule tasks available.
							</s:if>
							<s:else>
							<table class="data-listing">

								<s:if test="@com.vasworks.ibedc.model.CaptureApproval$CaptureType@STREET == captureType">
									<thead>
										<tr>
											<td>Street</td>
											<td>Agent</td>
											<td>Status</td>
											<td>Completion Time</td>
											<td>Verification Time</td>
											<td>Skipped Time</td>
											<td>Rejection Time</td>
											<td>Photos</td>
											<td>Verify</td>
											<td>Reject</td>
										</tr>
									</thead>
									<s:iterator value="items" var="resultRow">
										<tr>
											<td><s:property value="geoStreet.description" /></td>
											<td><s:property value="scheduleAgent.displayName" /></td>
											<td><s:property value="captureStatus" /></td>
											<td><s:date name="completionDate" format="dd/MM/yyyy HH:mm" /></td>
											<td><s:date name="verificationDate" format="dd/MM/yyyy HH:mm" /></td>
											<td><s:date name="skippedDate" format="dd/MM/yyyy HH:mm" /></td>
											<td><s:date name="rejectionDate" format="dd/MM/yyyy HH:mm" /></td>
											<td>
												<s:if test="photos != null && !photos.isEmpty()">
													<s:url var="schedulePhotoUrl" action="schedulePhotos" namespace="/agent/ajax">
														<s:param name="scheduleItemId" value="%{id}" />
													</s:url>
													<sj:a button="false" buttonIcon="ui-icon-gear" href="%{schedulePhotoUrl}" openDialog="scheduleItemPhotoDialog">Photos</sj:a>
												</s:if>
											</td>
											<td>
												<s:if test="completionDate != null && verificationDate == null">
													<s:url var="scheduleItemVerifyUrl" action="liveApprovalDetail!verify" namespace="/agent/ajax">
														<s:param name="scheduleItemId" value="%{id}" />
													</s:url>
													<sj:a button="false" buttonIcon="ui-icon-gear" href="%{scheduleItemVerifyUrl}" targets="liveApprovalTaskDiv">Verify</sj:a>
												</s:if>
											</td>
											<td>
												<s:if test="completionDate != null && verificationDate == null">
													<s:url var="scheduleItemRejectUrl" action="liveApprovalDetail!reject" namespace="/agent/ajax">
														<s:param name="scheduleItemId" value="%{id}" />
													</s:url>
													<sj:a button="false" buttonIcon="ui-icon-gear" href="%{scheduleItemRejectUrl}" targets="liveApprovalTaskDiv">Reject</sj:a>
												</s:if>
											</td>
										</tr>
									</s:iterator>
								</s:if>
								<s:if test="@com.vasworks.ibedc.model.CaptureApproval$CaptureType@ADDRESS == captureType">
									<thead>
										<tr>
											<td>Street</td>
											<td>Building Number</td>
											<td>Agent</td>
											<td>Status</td>
											<td>Completion Time</td>
											<td>Verification Time</td>
											<td>Skipped Time</td>
											<td>Rejection Time</td>
											<td>Photos</td>
											<td>Verify</td>
											<td>Reject</td>
										</tr>
									</thead>
									<s:iterator value="items" var="resultRow">
										<tr>
											<td><s:property value="geoStreet.description" /></td>
											<td><s:property value="buildingNumber" /></td>
											<td><s:property value="scheduleAgent.displayName" /></td>
											<td><s:property value="captureStatus" /></td>
											<td><s:date name="completionDate" format="dd/MM/yyyy HH:mm" /></td>
											<td><s:date name="verificationDate" format="dd/MM/yyyy HH:mm" /></td>
											<td><s:date name="skippedDate" format="dd/MM/yyyy HH:mm" /></td>
											<td><s:date name="rejectionDate" format="dd/MM/yyyy HH:mm" /></td>
											<td>
												<s:if test="photos != null && !photos.isEmpty()">
													<s:url var="schedulePhotoUrl" action="schedulePhotos" namespace="/agent/ajax">
														<s:param name="scheduleItemId" value="%{id}" />
													</s:url>
													<sj:a button="false" buttonIcon="ui-icon-gear" href="%{schedulePhotoUrl}" openDialog="scheduleItemPhotoDialog">Photos</sj:a>
												</s:if>
											</td>
											<td>
												<s:if test="completionDate != null && verificationDate == null">
													<s:url var="scheduleItemVerifyUrl" action="liveApprovalDetail!verify" namespace="/agent/ajax">
														<s:param name="scheduleItemId" value="%{id}" />
													</s:url>
													<sj:a button="false" buttonIcon="ui-icon-gear" href="%{scheduleItemVerifyUrl}" targets="liveApprovalTaskDiv">Verify</sj:a>
												</s:if>
											</td>
											<td>
												<s:if test="completionDate != null && verificationDate == null">
													<s:url var="scheduleItemRejectUrl" action="liveApprovalDetail!reject" namespace="/agent/ajax">
														<s:param name="scheduleItemId" value="%{id}" />
													</s:url>
													<sj:a button="false" buttonIcon="ui-icon-gear" href="%{scheduleItemRejectUrl}" targets="liveApprovalTaskDiv">Reject</sj:a>
												</s:if>
											</td>
										</tr>
									</s:iterator>
								</s:if>
								<s:if test="@com.vasworks.ibedc.model.CaptureApproval$CaptureType@POLYGON == captureType">
									<thead>
										<tr>
											<td>Polygon ID</td>
											<td>Agent</td>
											<td>Status</td>
											<td>Completion Time</td>
											<td>Verification Time</td>
											<td>Skipped Time</td>
											<td>Rejection Time</td>
											<td>Photos</td>
											<td>Verify</td>
											<td>Reject</td>
										</tr>
									</thead>
									<s:iterator value="items" var="resultRow">
										<tr>
											<td><s:property value="geoPolygon.id" /></td>
											<td><s:property value="scheduleAgent.displayName" /></td>
											<td><s:property value="captureStatus" /></td>
											<td><s:date name="completionDate" format="dd/MM/yyyy HH:mm" /></td>
											<td><s:date name="verificationDate" format="dd/MM/yyyy HH:mm" /></td>
											<td><s:date name="skippedDate" format="dd/MM/yyyy HH:mm" /></td>
											<td><s:date name="rejectionDate" format="dd/MM/yyyy HH:mm" /></td>
											<td>
												<s:if test="photos != null && !photos.isEmpty()">
													<s:url var="schedulePhotoUrl" action="schedulePhotos" namespace="/agent/ajax">
														<s:param name="scheduleItemId" value="%{id}" />
													</s:url>
													<sj:a button="false" buttonIcon="ui-icon-gear" href="%{schedulePhotoUrl}" openDialog="scheduleItemPhotoDialog">Photos</sj:a>
												</s:if>
											</td>
											<td>
												<s:if test="completionDate != null && verificationDate == null">
													<s:url var="scheduleItemVerifyUrl" action="liveApprovalDetail!verify" namespace="/agent/ajax">
														<s:param name="scheduleItemId" value="%{id}" />
													</s:url>
													<sj:a button="false" buttonIcon="ui-icon-gear" href="%{scheduleItemVerifyUrl}" targets="liveApprovalTaskDiv">Verify</sj:a>
												</s:if>
											</td>
											<td>
												<s:if test="completionDate != null && verificationDate == null">
													<s:url var="scheduleItemRejectUrl" action="liveApprovalDetail!reject" namespace="/agent/ajax">
														<s:param name="scheduleItemId" value="%{id}" />
													</s:url>
													<sj:a button="false" buttonIcon="ui-icon-gear" href="%{scheduleItemRejectUrl}" targets="liveApprovalTaskDiv">Reject</sj:a>
												</s:if>
											</td>
										</tr>
									</s:iterator>
								</s:if>
								<s:if test="@com.vasworks.ibedc.model.CaptureApproval$CaptureType@CUSTOMER == captureType">
									<thead>
										<tr>
											<td>Address</td>
											<td>Agent</td>
											<td>Status</td>
											<td>Completion Time</td>
											<td>Verification Time</td>
											<td>Skipped Time</td>
											<td>Rejection Time</td>
											<td>Photos</td>
											<td>Verify</td>
											<td>Reject</td>
										</tr>
									</thead>
									<s:iterator value="items" var="resultRow">
										<tr>
											<td><s:property value="geoAddress.id.toShortString()" /></td>
											<td><s:property value="scheduleAgent.displayName" /></td>
											<td><s:property value="captureStatus" /></td>
											<td><s:date name="completionDate" format="dd/MM/yyyy HH:mm" /></td>
											<td><s:date name="verificationDate" format="dd/MM/yyyy HH:mm" /></td>
											<td><s:date name="skippedDate" format="dd/MM/yyyy HH:mm" /></td>
											<td><s:date name="rejectionDate" format="dd/MM/yyyy HH:mm" /></td>
											<td>
												<s:if test="photos != null && !photos.isEmpty()">
													<s:url var="schedulePhotoUrl" action="schedulePhotos" namespace="/agent/ajax">
														<s:param name="scheduleItemId" value="%{id}" />
													</s:url>
													<sj:a button="false" buttonIcon="ui-icon-gear" href="%{schedulePhotoUrl}" openDialog="scheduleItemPhotoDialog">Photos</sj:a>
												</s:if>
											</td>
											<td>
												<s:if test="completionDate != null && verificationDate == null">
													<s:url var="scheduleItemVerifyUrl" action="liveApprovalDetail!verify" namespace="/agent/ajax">
														<s:param name="scheduleItemId" value="%{id}" />
													</s:url>
													<sj:a button="false" buttonIcon="ui-icon-gear" href="%{scheduleItemVerifyUrl}" targets="liveApprovalTaskDiv">Verify</sj:a>
												</s:if>
											</td>
											<td>
												<s:if test="completionDate != null && verificationDate == null">
													<s:url var="scheduleItemRejectUrl" action="liveApprovalDetail!reject" namespace="/agent/ajax">
														<s:param name="scheduleItemId" value="%{id}" />
													</s:url>
													<sj:a button="false" buttonIcon="ui-icon-gear" href="%{scheduleItemRejectUrl}" targets="liveApprovalTaskDiv">Reject</sj:a>
												</s:if>
											</td>
										</tr>
									</s:iterator>
								</s:if>
								
							</table>
							</s:else>
						</div>
						</td>
					</tr>
				</table>
				</div>
			</td>
		</tr>		
	</table>
	