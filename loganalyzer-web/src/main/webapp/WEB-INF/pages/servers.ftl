<#assign script>
<script>
function addServer(){
	$("#addModal").modal('toggle');
	$("#model-label").html("Add Server");
	$("#submitButton").html("Add Server");
	$("#name").val("");
	$("#nodeId").val("");
	$("#logURL").val("");
	$("#serverId").val("");
}

function deleteServer(serverID){
	$("#deleteModal").modal('toggle');
	$("#confirm-delete").attr("href","servers/delete/"+serverID);	
}

function updateServer(obj,serverID){
	$("#addModal").modal('toggle');
	var serverParams = $(obj).parent("td").siblings("td");
	$("#model-label").html("Update Server");
	$("#submitButton").html("Update Server");
	$("#name").val($(serverParams[0]).html());
	$("#nodeId").val($(serverParams[1]).html());
	$("#logURL").val($(serverParams[2]).html());
	$("#serverId").val(serverID);
}
</script>
</#assign>
<#import "layout/layout.ftl" as layout>
<@layout.default "Log Analyzer Dashboard" "${script}">
	<div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Servers</h1>
            
            <div class="text-right">
            	<button class="btn btn-primary btn-lg"  onclick="addServer()">Add a New Server</button>
            </div>
            
            <div>
			<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover" id="dataTable" >
				<thead>
					<tr>
						<th>Server Name</th>
						<th>Node ID</th>
						<th>URL</th>
						<th>Options</th>
					</tr>
				</thead>
				<tbody valign="top">
					<#list iterable as server>
					 	<tr>
							<td>${server.name}</td>
							<td>${server.nodeId}</td>
							<td>${server.logURL}</td>
							<td> 
								<button class="btn btn-primary btn-circle" onclick="updateServer(this,'${server.id}')"><i class="fa fa-list"></i></button>
								<button class="btn btn-warning btn-circle" onclick="deleteServer('${server.id}')"><i class="fa fa-times"></i></button>
							</td>
						</tr>
					</#list>
				</tbody>
			</table>
			</div>
			
			<div id="addModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="model-label" aria-hidden="true">
                <div class="modal-dialog">
                	<form class="form-horizontal" method="post" action="servers/add"> 
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="model-label">Add Server</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row">
								<div class="col-md-11">
									<div class="form-group has-feedback">
										<span class="control-label" for="name">Server Name</span>
										<input class="form-control" type="text" name="name" id="name" required autofocus>
									</div>
									<div class="form-group has-feedback">
										<span class="control-label" for="nodeId">Node Id</span>
										<input class="form-control" type="text" name="nodeId" id="nodeId" required>
									</div>
									<div class="form-group has-feedback">
										<span class="control-label" for="logURL">URL</span>
										<input class="form-control" type="url" name="logURL" id="logURL" placeholder="URL of the log file" required >
									</div>
									<input type="hidden" name="id" id="serverId" >
								</div>
							</div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary" id="submitButton">Save Server</button>
                        </div>
                    </div>
                    <form>
                </div>
            </div>
			
			<div id="deleteModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-body">
							<p>Confirm to delete?</p>
						</div>
						<div class="modal-footer">
							<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>
							<a type="button" id="confirm-delete" class="btn btn-primary">Yes</a>
						</div>
					</div>
				</div>
			</div>
        </div>
    </div>
</@layout.default>