<%@ include file="/common/taglibs.jsp"%>
<div class="container">
	<s:actionerror theme="bootstrap"/>
    <s:actionmessage theme="bootstrap"/>   
    
	<div id="signatureParentDiv">
		<h1>Signature</h1>
		<div id="signature"></div>
		<sj:a cssClass="btn btn-lg btn-primary btn-block" id="resetSignatureButton" button="true">Clear Signature</sj:a>
	</div>
	<s:if test="entity.signature != null">
		<div>
			<h1>Signature on File</h1>
			<div>
				<img style='border: solid 1px #4f81bd;'
					src='<s:url action="signatureDisplay" namespace="/download"><s:param name="processFlowItemId" value="%{processFlowItemId}" /></s:url>' />
			</div>
		</div>
	</s:if>
	
	<s:form id="approvalRecordForm" action="approvalDetail!save"
		namespace="/agent/ajax" theme="bootstrap" method="post"
		enctype="multipart/form-data">
		<s:hidden name="processFlowItemId" />
		<s:hidden name="signatureId" />
		<s:hidden name="signatureData" id="signatureData" />
		<s:hidden name="signed" id="signed" />
		<sj:submit cssClass="btn btn-lg btn-primary btn-block" button="true"
			value="Save" targets="approvalDetailDialog" effect="highlight"
			effectDuration="500" indicator="indicator" />
	</s:form>
	
</div>