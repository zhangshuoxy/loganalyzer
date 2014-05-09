<#assign script>
<script src="${request.contextPath}/theme/js/mousetrap.min.js"></script>
<script>
$(function() {
	Mousetrap.bind('enter', function() {
		console.log("enter");
	});
	Mousetrap.bind(['k', 'up'], function(e) {
		console.log("previous");
	});
	Mousetrap.bind(['j', 'down'], function(e) {
		console.log("next");
	});
});

function showMore() {
	var url = location.pathname + "?lastRecordTime=" + $("tr:last td:first").attr("id");
	var btn = $("#show-more");
	btn.button('loading');
	
	$.get(url, function(data) {
		if (data.trim()) {
			$("tbody").append(data);
			btn.button('reset');
		} else {
			btn.text("All loaded!");
			btn.prop("disabled", true);
		}
	});
}
</script>
</#assign>
<#import "layout/layout.ftl" as layout> 
<@layout.default "Log Analyzer Dashboard" "${script}">
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">Exceptions - <a href="${request.contextPath}/dashboard/${server}">${server}</a></h1>
			<h4>
			<#if location??>
				<a href="${request.contextPath}/exception/${server}/${fromDate}/to/${toDate}/${exception}.html">${exception}</a> - ${location}
			<#else>
				${exception}
			</#if> (${fromDate} - ${toDate})
			</h4>
		</div>
	</div>
	
	<div class="row">
		<div class="col-lg-12">
			<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="dataTable">
				<thead>
					<tr>
						<th>timestamp</th>
						<#if !location??>
						<th>location</th>
						</#if>
						<th>message</th>
						<th>details</th>
					</tr>
				</thead>
				<tbody valign="top">
					<#include "exceptions-data.ftl" />
				</tbody>
			</table>
			<button class="btn btn-default btn-lg btn-block" id="show-more" style="font-size:15px;" data-loading-text="<i class='fa fa-spinner fa-spin'></i>  loading..." onclick="showMore()">show more...</button>
		</div>
	</div>
</@layout.default>
