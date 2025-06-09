<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
	<div class="alert alert-warning">
        Your login attempt was not successful: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
    </div>
</c:if>
<div class="container">
	<s:actionerror theme="bootstrap" />
    <s:actionmessage theme="bootstrap" />
	<s:form label="Enter Credentials" cssClass="well form-horizontal" id="loginForm" action="j_spring_security_check" method="post" namespace="/" theme="bootstrap">
    	<s:textfield inputPrependIcon="envelope" name="j_username" id="j_username" cssClass="form-control" placeholder="Email Address" />
        <s:password name="j_password" id="j_password" cssClass="form-control" placeholder="Password" />
        <s:submit cssClass="btn btn-lg btn-primary btn-block" button="true" value="Login" />
    </s:form>
</div>
