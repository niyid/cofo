<%@ include file="/common/taglibs.jsp"%>
<div class="container">
	<s:actionerror theme="bootstrap"/>
    <s:actionmessage theme="bootstrap"/>   

	<s:form label="Document File Record" enctype="multipart/form-data" method="post" cssClass="well" id="documentFileRecordForm" action="documentFileRecord!save" namespace="/agent/ajax" theme="bootstrap">
		<s:hidden name="certificateId" />
		<s:hidden name="certificateTypeId" />
		<sj:datepicker
        	id="issueDate"
        	name="issueDate"
            parentTheme="bootstrap"
            placeholder="Issue Date"
            cssClass="form-control"
            showOn="focus"
            displayFormat="dd/mm/yy"
            required="true"
        />
		<sj:datepicker
        	id="expirationDate"
        	name="expirationDate"
            parentTheme="bootstrap"
            placeholder="Expiration Date"
            cssClass="form-control"
            showOn="focus"
            displayFormat="dd/mm/yy"
            required="true"            
        />        		
		<s:select
			cssClass="form-control" 
			name="authorityId" 
			headerKey=""
			headerValue="Licensing Authority"
			list="licensingAuthorityLov"
			listKey="id"
			listValue="organizationName"
			emptyOption="false"
			required="true"
		/>
		<s:file placeHolder="Document File" name="uploadedFile" cssClass="form-control" required="true" />
		<s:textarea 
			cssClass="form-control"
			name="certificateDesc"
			placeholder="Description"
			rows="5"
			required="true"
		/>					
		<sj:submit cssClass="btn btn-lg btn-primary btn-block" button="true" value="Save" targets="addDocumentDialog" effect="highlight" effectDuration="500" indicator="indicator" />
	</s:form>
</div>