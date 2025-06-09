<%@ include file="/common/taglibs.jsp"%>

	<s:url var="liveApprovalTaskUrl" action="liveApprovalDetail" namespace="/agent/ajax">
		<s:param name="processFlowItemId" value="%{processFlowItemId}" />
		<s:param name="message" value="%{message}" />
	</s:url>
	<sj:div id="liveApprovalTaskDiv" href="%{liveApprovalTaskUrl}" reloadTopics="reloadLiveApprovalTaskDiv" />
<script type="text/javascript">
     var auto = setInterval(function() {
          $('#liveApprovalTaskDiv').publish('reloadLiveApprovalTaskDiv');
     }, 20000); // refresh every 10000 milliseconds
</script>