<%@ include file="/common/taglibs.jsp"%>


	<table>
		<tr>
			<td class="windowPanel">
			<div>
			<s:form id="scheduleSearchForm" action="scheduleSearch!search" namespace="/agent/ajax" theme="xhtml">
				<table>
					<tr>
						<td>
							<sj:datepicker name="searchStartDate" label="Start Date" labelSeparator=":" displayFormat="dd/mm/yy" required="false" />
						</td>
						<td>
							<s:select 
								name="searchCaptureType" 
								headerKey=""
								headerValue="----------Select-----------"
								list="captureTypeLov"
								listKey="name()"
								listValue="name()"
								emptyOption="false"
								label="Capture Type" 
								labelSeparator=":"
								required="false"
							/>
						</td>
						<td>
							<s:checkbox name="searchExport2Excel" label="Export" />
						</td>
					</tr>
					<tr>
						<td>
							<sj:submit button="true" value="Search" buttonIcon="ui-icon-gear" targets="scheduleListDialog" effect="highlight" effectDuration="500" indicator="indicator" />
						</td>
					</tr>
				</table>
				</s:form>		
				<table>
					<tr>
						<td>
						<div id="searchItemInnerDiv" style="border: solid 1px #4f81bd; background: #ffffff; padding: 4px; height: 250px; overflow: auto;">
							<s:if test="results == null || results.isEmpty()">
								No schedule items available.
							</s:if>
							<s:else>
							<table class="data-listing">
								<thead>
									<tr>
										<td>Description</td>
										<td>Start Date</td>
										<td>End Date</td>
										<td></td>
									</tr>
								</thead>
								<s:iterator value="results" var="resultRow">
									<tr>
										<td><s:property value="description" /></td>
										<td><s:date name="startDate" format="dd/MM/yyyy HH:mm" /></td>
										<td><s:date name="endDate" format="dd/MM/yyyy HH:mm" /></td>
										<td>
											<s:url var="detailUrl" action="scheduleDetail" namespace="/agent/ajax" escapeAmp="true">
												<s:param name="searchScheduleId" value="%{id}" />
												<s:param name="searchExport2Excel" value="%{searchExport2Excel}" />
												<s:param name="searchCaptureType" value="%{searchCaptureType}" />
											</s:url>
											<sj:a button="false" buttonIcon="ui-icon-gear" href="%{detailUrl}" openDialog="scheduleDetailDialog">Details</sj:a>
										</td>
									</tr>
								</s:iterator>
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
<s:include value="/common/msg.jsp" />	