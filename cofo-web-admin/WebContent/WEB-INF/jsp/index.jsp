<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>Certificate of Occupancy Application Management</title>
</head>
<body>
<div class="container">
<security:authorize access="hasAnyRole('ROLE_SURVEYOR_GENERAL','ROLE_SCHEME_OFFICER','ROLE_ES_LUAC','ROLE_SSA_LANDS','ROLE_PS_LANDS','ROLE_GOVERNOR','ROLE_DEPUTY_REGISTRAR','ROLE_REGISTRAR')">
	<table>
		<tr>	
			<td>
				<s:url var="liveApprovalListUrl" action="liveApprovalList" namespace="/agent/ajax" />
				<sj:div id="liveApprovalDiv" href="%{liveApprovalListUrl}" reloadTopics="reloadLiveApprovalDiv" />
			</td>
		</tr>
	</table>

	<sj:dialog id="approvalDetailDialog" autoOpen="false" modal="true" title="Approval Details" width="1200" onCompleteTopics="initSignatureObj" />
	<sj:dialog id="approvalItemPhotoDialog" autoOpen="false" modal="true" title="Document Images" width="1200" />
	
	<script type="text/javascript">
	    var auto1 = setInterval(function() {
	          $('#liveApprovalDiv').publish('reloadLiveApprovalDiv');
	    }, 10000); // refresh every 10000 milliseconds
	    
	    $.subscribe('initSignatureObj', function(event, data) {
			$("#signature").jSignature({
				width : 600,
				height : 200
			});
			$("#signed").val('false');

			$("#signature").bind("change", function(event) {
				// 'event.target' will refer to DOM element with id "#signature"
				var d = $(event.target).jSignature("getData", "native");
				// if there are more than 2 strokes in the signature
				// or if there is just one stroke, but it has more than 20 points
				if (d.length > 2 || (d.length === 1 && d[0].x.length > 20)) {
					// we show "Submit" button
					// $(event.target).unbind('change')
					$("#signed").val('true');
				} else {
					$("#signed").val('false');
				}
			});

			$('#recordSaveButton').on("click", function() {
				var data = $('#signature').jSignature('getData');
				$("#signatureData").val(data);
			});

			$('#resetSignatureButton').on("click", function() {
				$("#signature").jSignature("reset");
			});
	    });
	</script>
</security:authorize>
<security:authorize access="hasAnyRole('ROLE_DATA_ENTRY')">
	<div class="container">
		<s:url var="cooApplicationUrl" action="cooApplicationRecord" namespace="/agent/ajax" />
		<s:url var="cooApplicationListUrl" action="cooApplicationList" namespace="/agent/ajax" />
		
		<div class="row">
			<div class="col-sm-6">
				<sj:a button="true" cssClass="btn btn-lg btn-primary btn-block" href="%{cooApplicationUrl}" indicator="indicator" openDialog="cooApplicationDialog">Application for the Issue of Certificate of Occupancy</sj:a>
			</div>
			<div class="col-sm-6">
				<sj:a button="true" cssClass="btn btn-lg btn-primary btn-block" href="%{cooApplicationListUrl}" indicator="indicator" openDialog="cooApplicationListDialog">List of C-of-O Records</sj:a>
			</div>
		</div>
		<sj:a button="true" cssClass="btn btn-lg btn-primary btn-block" openDialog="futureSupportDialog">Application for the Allocation of State Land</sj:a>
		<sj:a button="true" cssClass="btn btn-lg btn-primary btn-block" openDialog="futureSupportDialog">Application to Object to the Issue of Certificate of Occupancy</sj:a>
		<sj:a button="true" cssClass="btn btn-lg btn-primary btn-block" openDialog="futureSupportDialog">Application to Withdraw a Caution or Restriction</sj:a>
	</div>
	<sj:dialog id="cooApplicationDialog" autoOpen="false" modal="true" title="Manage C-of-O Record" width="1200" />
	<sj:dialog id="cooApplicationListDialog" autoOpen="false" modal="true" title="List of C-of-O Records" width="1200" />
	<sj:dialog id="futureSupportDialog" autoOpen="false" modal="true" title="Future Support" width="1200">
		This feature is not yet supported. It will be fully supported in the future.
	</sj:dialog>
</security:authorize>
<security:authorize access="!hasAnyRole('ROLE_DATA_ENTRY','ROLE_SCHEME_OFFICER','ROLE_ES_LUAC','ROLE_SURVEYOR_GENERAL','ROLE_SSA_LANDS','ROLE_PS_LANDS','ROLE_GOVERNOR','ROLE_DEPUTY_REGISTRAR','ROLE_REGISTRAR')">
	<sj:dialog id="permissionDialog" autoOpen="true" modal="true" title="Limited Permission" width="400" >Insufficient permissions. Contact your administrator if you should have access or Login.</sj:dialog>
</security:authorize>
<sj:dialog id="certDocListDialog" autoOpen="false" modal="true" title="Document List" width="1200" />
<sj:dialog id="addDocumentDialog" autoOpen="false" modal="true" title="Add Document Files" width="1200" />
<img id="indicator" style="position: absolute; top: 50%; left: 50%; display:none;" src="img/indicator.gif" alt="Processing..." />
</div>
</body>
</html>