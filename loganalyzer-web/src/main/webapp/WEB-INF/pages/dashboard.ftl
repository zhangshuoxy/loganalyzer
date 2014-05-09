<#assign script>
<script src="${request.contextPath}/theme/js/highcharts/highcharts.js"></script>
<script src="${request.contextPath}/theme/js/highcharts/modules/data.js"></script>
<script>
<#if serverExceptionCounts?has_content>
$(function() {
	<#list serverExceptionCounts?keys as serverName>
	<#assign serverExceptionCount = serverExceptionCounts[serverName]>
	$('#exceptions-timeline-${serverName}').highcharts({
		chart : {
			type : 'spline'
		},
		title : {
			text : 'Exceptions timeline'
		},
		subtitle : {
			text : 'From ${startDate?datetime} to ${endDate?datetime}'
		},
		xAxis : {
			type: 'category'
		},
		series : [
			<#list serverExceptionCount?keys as server>
	        <#assign ecs = serverExceptionCount[server]>
	        {name : '${server}', data : [
		        <#list ecs as ec>
		        ['${ec.timestamp?number_to_datetime}', ${ec.count}],
				</#list>
			]},
	        </#list>
		]
	});
	</#list>
});
</#if>
</script>
</#assign>
<#import "layout/layout.ftl" as layout>
<@layout.default "Log Analyzer Dashboard" "${script}">
	<div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Dashboard</h1>
            <h4>(${startDate?string("yyyy-MM-dd")} - ${endDate?string("yyyy-MM-dd")})</h4>
        </div>
    </div>
    
    <#if serverExceptionCounts?has_content>
    <#list serverExceptionCounts?keys as serverName>
    <div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">Exceptions Timeline ${serverName}</div>
				<div class="panel-body">
					<div id="exceptions-timeline-${serverName}" style="height: 500px; margin: 0 auto"></div>
				</div>
			</div>
		</div>
	</div>
	</#list>
	<#else>
	<div class="row">
		<div class="col-lg-10 col-lg-offset-1">
			<div class="panel panel-default">
				<div class="panel-heading">No Exceptions Found</div>
				<div class="panel-body">
					<p>
						No exceptions found in this time period: <code>${startDate?string("yyyy-MM-dd")} - ${endDate?string("yyyy-MM-dd")}</code>
					</p>
				</div>
			</div>
		</div>
	</div>
	</#if>
</@layout.default>
