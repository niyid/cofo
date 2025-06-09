<%@ include file="/common/taglibs.jsp"%>
	<div class="container">
      	<s:actionerror theme="bootstrap"/>
      	<s:actionmessage theme="bootstrap"/>      
	 	<s:form label="C-of-O Record" cssClass="well" id="cooApplicationRecordForm" action="cooApplicationRecord!save" method="post" namespace="/agent/ajax" theme="bootstrap">
	 
	 	<s:hidden name="entity.vitalInformation.contactInfo.id" />
		<div class="row">
			<div class="col-sm-12">
			 	<div class="checkbox">
					<label><input type="checkbox" name="entity.landDeveloped" />Land Developed?</label>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="checkbox">
					<label><input type="checkbox" name="entity.landOccupied" />Land Occupied?</label>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<s:select
				 	cssClass="form-control" 
					name="entity.cooType" 
					headerKey=""
					headerValue="Select C-of-O Type"
					list="cooTypeLov"
					listKey="name()"
					listValue="name()"
					emptyOption="false"
					required="true"
				/>						
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<s:textfield placeholder="Identity Number" name="entity.vitalInformation.id.idValue" cssClass="form-control" />
			</div>
			<div class="col-sm-6">
				<s:select
					cssClass="form-control" 
					name="entity.vitalInformation.id.idType" 
					headerKey=""
					headerValue="Select Identity Type"
					list="idTypeLov"
					listKey="name()"
					listValue="name()"
					emptyOption="false"
					required="true"
				/>						
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<s:select
					cssClass="form-control" 
					name="titleId" 
					headerKey=""
					headerValue="Select Title"
					list="titleLov"
					listKey="id"
					listValue="description"
					emptyOption="false"
					required="false"
				/>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-4">
				<s:select
					cssClass="form-control" 
					name="entity.vitalInformation.gender" 
					headerKey=""
					headerValue="Select Gender"
					list="genderLov"
					listKey="name()"
					listValue="name()"
					emptyOption="false"
					required="true"
				/>						
			</div>
			<div class="col-sm-4">
				<sj:datepicker
		        	id="birthDate"
		        	name="entity.vitalInformation.birthDate"
		            parentTheme="bootstrap"
		            placeholder="Birth Date"
		            cssClass="form-control"
		            showOn="focus"
		            displayFormat="dd/mm/yy"
		            required="true"
		        />
	        </div>
			<div class="col-sm-4">	
				<s:select
					cssClass="form-control" 
					name="entity.vitalInformation.maritalStatus" 
					headerKey=""
					headerValue="Select Marital Status"
					list="maritalStatusLov"
					listKey="name()"
					listValue="name()"
					emptyOption="false"
					required="true"
				/>		
			</div>
		</div>		
		<div class="row">
			<div class="col-sm-3">
				<s:textfield placeholder="Surname" name="entity.vitalInformation.lastName" cssClass="form-control" />
			</div>
			<div class="col-sm-3">
				<s:textfield placeholder="First Name" name="entity.vitalInformation.firstName" cssClass="form-control" />
			</div>
			<div class="col-sm-3">
				<s:textfield placeholder="Other Names" name="entity.vitalInformation.otherNames" cssClass="form-control" />
			</div>
			<div class="col-sm-3">
				<s:textfield placeholder="Maiden Name" name="entity.vitalInformation.maidenName" cssClass="form-control" />
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<s:textfield placeholder="Primary Cellphone" name="entity.vitalInformation.contactInfo.primaryPhone" cssClass="form-control" required="true" />
			</div>
			<div class="col-sm-6">
				<s:textfield placeholder="Secondary Cellphone" name="entity.vitalInformation.contactInfo.secondaryPhone" cssClass="form-control" required="false" />
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<s:textfield placeholder="Primary Email" name="entity.vitalInformation.contactInfo.primaryEmail" cssClass="form-control" required="true" />
			</div>
			<div class="col-sm-6">
				<s:textfield placeholder="Secondary Email" name="entity.vitalInformation.contactInfo.secondaryEmail" cssClass="form-control" required="false" />
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
			<s:select
				cssClass="form-control" 
				name="nationalityId" 
				headerKey=""
				headerValue="Select Nationality"
				list="countryLov"
				listKey="countryCode"
				listValue="countryName"
				emptyOption="false"
				required="true"
			/>				
			</div>
			<div class="col-sm-6">
				<s:select
					cssClass="form-control" 
					name="occupationId" 
					headerKey=""
					headerValue="Select Occupation"
					list="occupationLov"
					listKey="id"
					listValue="description"
					emptyOption="false"
					required="true"
				/>		
			</div>			
		</div>
		<div class="row">
			<div class="col-sm-4">
				<s:textfield placeholder="Address House Number" name="entity.vitalInformation.geoAddress.propertyNumber" cssClass="form-control" required="true" />
			</div>
			<div class="col-sm-4">
				<s:textfield placeholder="Address Line 1" name="entity.vitalInformation.geoAddress.areaName1" cssClass="form-control" required="true" />
			</div>
			<div class="col-sm-4">
				<s:textfield placeholder="Address Line 2" name="entity.vitalInformation.geoAddress.areaName2" cssClass="form-control" required="false" />
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<s:select
					cssClass="form-control" 
					name="countryStateId" 
					headerKey=""
					headerValue="Select State of Origin"
					list="countryStateLov"
					listKey="id"
					listValue="stateName"
					emptyOption="false"
					required="true"
				/>					
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<s:select
					cssClass="form-control" 
					name="lgaId" 
					headerKey=""
					headerValue="Select LGA"
					list="lgaLov"
					listKey="id"
					listValue="lgaName"
					emptyOption="false"
					required="true"
				/>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<s:select
					cssClass="form-control" 
					name="streetId" 
					headerKey=""
					headerValue="Select Street"
					list="streetLov"
					listKey="id"
					listValue="description"
					emptyOption="false"
					required="true"
				/>						
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<s:textarea 
					cssClass="form-control"
					name="entity.description"
					placeholder="Description"
					rows="5"
					required="true"
				/>
			</div>
		</div>
		<sj:submit cssClass="btn btn-lg btn-primary btn-block" button="true" value="Save" targets="cooApplicationDialog" effect="highlight" effectDuration="500" indicator="indicator" />
	</s:form>
</div>
<s:include value="/common/msg.jsp" />	