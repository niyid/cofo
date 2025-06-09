   <%@ include file="/common/taglibs.jsp"%>
 <sjc:chart
        id="chartPie"
        cssStyle="width: 600px; height: 800px;"
        pie="true"
        pieLabel="true"
        title="%{captureType} - Status Ratios"
        label="%{captureType}"
    >
        <sjc:chartData
                id="pieSerie1"
                label="In Progress"
                data="%{countInProgress}"
        />
        <sjc:chartData
                id="pieSerie2"
                label="Completed"
                data="%{countCompleted}"
        />
        <sjc:chartData
                id="pieSerie3"
                label="Skipped"
                data="%{countSkipped}"
        />
        <sjc:chartData
                id="pieSerie4"
                label="Rejected"
                data="%{countRejected}"
        />
        <sjc:chartData
                id="pieSerie5"
                label="Verified"
                data="%{countVerified}"
        />
    </sjc:chart>