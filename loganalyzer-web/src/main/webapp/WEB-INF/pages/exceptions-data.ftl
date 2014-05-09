					<#list exceptions.content as exception>
					<tr>
						<td nowrap id="${exception.id.timestamp?string('0')}">${exception.id.timestamp?number_to_datetime}</td>
						<#if !location??>
						<td>${exception.location}</td>
						</#if>
						<td>${exception.message}</td>
						<td>
							<button class="btn btn-primary btn-circle" data-target="#${exception.id.server}-${exception.id.timestamp?long?string('0')}" data-toggle="modal">
								<i class="fa fa-list"></i>
							</button>
							<div id="${exception.id.server}-${exception.id.timestamp?string('0')}" class="modal fade" aria-hidden="true">
								<div class="modal-dialog-wide">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
											<h4 class="modal-title" id="model-label">Exception Details</h4>
										</div>
										<div class="modal-body">
											<pre>${exception.details}</pre>
										</div>
										<div class="modal-footer">
											<button type="button" data-dismiss="modal" class="btn btn-default">Close</button>
										</div>
									</div>
								</div>
							</div>
						</td>
					</tr>
					</#list>