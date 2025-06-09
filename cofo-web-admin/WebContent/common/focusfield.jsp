<script type="text/javascript">
Event.observe(window, 'load', function() {
	var x=document.getElementsByClassName('autofocus');
	if (x!=null && x.length>0) 
		x[x.length-1].focus();
	else
		try { Form.focusFirstElement($('main').getElementsByTagName('form')[0]); } catch (e) { } 
}); 
</script>