<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Indexing</title>
</head>
<body>
<s:if test="indexer.running">
Indexer status: <s:property value="indexer.status" />
Table: <s:property value="indexer.indexTable" />
Progress: <s:property value="indexer.currentLot" />/<s:property value="indexer.totalLotCount" />
	<b><s:property value="100*indexer.currentLot/indexer.totalLotCount" />%</b>
	<s:form action="lucene/reindex!stop" method="POST">
		<s:submit value="Stop" />
	</s:form>
</s:if>
<s:else>
	<s:form action="lucene/reindex!reindex" method="POST" theme="simple">
		<label>Scope:</label>
		<s:select list="#{'Booking':'Bookings'}" name="tableName" value="%{indexer.indexTable}" label="Scope" />
		<s:submit value="Reindex" />
	</s:form>
</s:else>
</body>
</html>