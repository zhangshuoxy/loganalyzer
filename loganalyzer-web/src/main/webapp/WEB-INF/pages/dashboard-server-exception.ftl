<#assign script>
<script src="${request.contextPath}/theme/js/highcharts/highcharts.js"></script>
<script src="${request.contextPath}/theme/js/highcharts/modules/data.js"></script>
<script>
$(function() {
	$('#exception-timeline').highcharts({
		chart : {
			type : 'spline'
		},
		title : {
			text : 'Exceptions timeline'
		},
		subtitle : {
			text : 'From ${fromDate} to ${toDate}'
		},
		xAxis : {
			type: 'category'
		},
		series : [
	        {name : '${exception}', data : [
		        <#list counts as count>
		        ['${count.timestamp?number_to_datetime}', ${count.count}],
				</#list>
			]},
		]
	});
	
	$('#thrown-location').highcharts({
		data : {
			table : document.getElementById('locations')
		},
		chart : {
			type : 'column',
			inverted: true
		},
		title : {
			text : 'Exceptions Thrown Location'
		},
		yAxis : {
			allowDecimals : true,
			title : {
				text : 'Count'
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
				return this.series.name + ': ' + this.point.y;
			}
		}
	});
});
</script>
</#assign>
<#import "layout/layout.ftl" as layout> 
<@layout.default "Log Analyzer Dashboard" "${script}">
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header"><a href="${request.contextPath}/dashboard/${server}">${server}</a> (top 30)</h1>
			<h4><a href="${request.contextPath}/exception/${server}/${fromDate}/to/${toDate}/${exception}.html">${exception}</a> (${fromDate} - ${toDate})</h4>
		</div>
	</div>
	
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">Exception Timeline</div>
				<div class="panel-body">
					<div id="exception-timeline" style="height: 600px; margin: 0 auto"></div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-lg-6">
			<div class="panel panel-default">
				<div class="panel-heading">Thrown Location</div>
				<div class="panel-body">
					<div id="thrown-location" style="height: 500px; margin: 0 auto"></div>
				</div>
			</div>
		</div>
		<div class="col-lg-6">
			<div class="panel panel-default">
				<div class="panel-heading">Thrown Location</div>
				<div class="panel-body">
					<table id="locations" cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th>Location</th>
								<th>Count</th>
							</tr>
						</thead>
						<tbody>
						<#list locations?keys as location>
				        <#assign count = locations[location]>
				        	<tr>
								<th><a href="${request.contextPath}/exception/${server}/${fromDate}/to/${toDate}/${exception}/${location}.html">${location}</a></th>
								<td>${count?string("0")}</td>
							</tr>
				        </#list>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</@layout.default>
