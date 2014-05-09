<#assign script>
<script src="${request.contextPath}/theme/js/polling.js"></script>
<script>   
$(function() {
	$('#logModal').modal({backdrop: 'static', keyboard: false});
	$.polling("pull", function(data) {
		$('#logContainer').append(data);
	});
});
</script>
</#assign>
<#import "layout/layout.ftl" as layout> 
<@layout.blank "Log Analyzer Live Log" "${script}">
	<div id="logModal" class="modal" style="height: 100%;">
		<div class="modal-dialog-log">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="model-label">Exception Live Log</h4>
				</div>
				<div class="modal-body">
					<pre id="logContainer"></pre>
				</div>
			</div>
		</div>
	</div>
</@layout.blank>
