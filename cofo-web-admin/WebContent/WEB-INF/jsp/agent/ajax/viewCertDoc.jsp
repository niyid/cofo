<%@ include file="/common/taglibs.jsp"%>
<div id="certDocCarousel" class="carousel slide" data-ride="carousel">
  <!-- Indicators -->
  <ol class="carousel-indicators">
  <s:iterator value="docFiles" status="incr">
  	<li data-target='#certDocCarousel' data-slide-to='<s:property value="%{#incr.index}" />'></li>
  </s:iterator>
  </ol>

  <!-- Wrapper for slides -->
  <div class="carousel-inner" role="listbox">
  	<s:iterator value="docFiles">
	    <div class="item">
	      <img src='<s:url action="imageDisplay" namespace="/download"><s:param name="fileId" value="%{id}" /></s:url>' alt='<s:property value="description" />'>
	    </div>  	
  	</s:iterator>
  </div>

  <!-- Left and right controls -->
  <a class="left carousel-control" href="#certDocCarousel" role="button" data-slide="prev">
    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
    <span class="sr-only">Previous</span>
  </a>
  <a class="right carousel-control" href="#certDocCarousel" role="button" data-slide="next">
    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
    <span class="sr-only">Next</span>
  </a>
</div>