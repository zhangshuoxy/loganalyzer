<#assign script>
<script src="${request.contextPath}/theme/js/highcharts/highcharts.js"></script>
<script src="${request.contextPath}/theme/js/highcharts/modules/data.js"></script>
<script>
<#if exceptions?has_content>
$(function() {
	$('#exceptions-timeline').highcharts({
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
			<#list exceptionCounts?keys as exception>
	        <#assign ecs = exceptionCounts[exception]>
	        {name : '${exception}', data : [
		        <#list ecs as ec>
		        ['${ec.timestamp?number_to_datetime}', ${ec.count}],
				</#list>
			]},
	        </#list>
		]
	});

	$('#most-occured-exceptions').highcharts({
		data : {
			table : document.getElementById('exceptions')
		},
		chart : {
			type : 'column',
			inverted: true
		},
		title : {
			text : 'Exceptions and Counts'
		},
		yAxis : {
			allowDecimals : true,
			title : {
				text : 'Counts'
			}
		},
		plotOptions: {
            series: {
                cursor: 'pointer',
                point: {
                    events: {
                        click: function() {
                            window.location.href = $(this.name).attr('href');
                        }
                    }
                }
            }
        },
		tooltip : {
			formatter : function() {
				return this.point.name + '<br/><b>' + this.series.name + ': ' + this.point.y + '</b>';
			}
		}
	});
});
</#if>
</script>
</#assign>
<#import "layout/layout.ftl" as layout>
<@layout.default "Log Analyzer Dashboard" "${script}">
	<div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">${server} (top 30)</h1>
            <h4>(${startDate?string("yyyy-MM-dd")} - ${endDate?string("yyyy-MM-dd")})</h4>
        </div>
    </div>
    
	<#if exceptions?has_content>
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">Exceptions Timeline</div>
				<div class="panel-body">
					<div id="exceptions-timeline" style="height: 800px; margin: 0 auto"></div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">Top Exceptions Chart</div>
				<div class="panel-body">
					<div id="most-occured-exceptions" style="height: 800px; margin: 0 auto"></div>
					<table id="exceptions" cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th>Exception</th>
								<th>Counts</th>
							</tr>
						</thead>
						<tbody>
						<#list exceptions?keys as exception>
				        <#assign count = exceptions[exception]>
				        	<tr>
								<th><a href="${request.contextPath}/dashboard/${server}/${startDate?string('yyyy-MM-dd')}/to/${endDate?string('yyyy-MM-dd')}/${exception}.html">${exception}</a></th>
								<td>${count?string("0")}</td>
							</tr>
				        </#list>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<#else>
	<div class="row">
		<div class="col-lg-10 col-lg-offset-1">
			<div class="panel panel-default">
				<div class="panel-heading">No Exceptions Found</div>
				<div class="panel-body">
					<p>
						No exceptions found on server: <code>${server}</code> in this time period: 
						<code>${startDate?string("yyyy-MM-dd")} - ${endDate?string("yyyy-MM-dd")}</code>
					</p>
				</div>
			</div>
		</div>
	</div>
	</#if>
</@layout.default>
