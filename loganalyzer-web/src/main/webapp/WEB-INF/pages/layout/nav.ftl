	<div class="navbar-default navbar-static-side" role="navigation">
		<div class="sidebar-collapse">
			<ul class="nav" id="side-menu">
				<li class="sidebar-search">
					<div class="input-group custom-search-form">
						<input type="text" class="form-control" placeholder="Search..."> <span class="input-group-btn">
							<button class="btn btn-default" type="button">
								<i class="fa fa-search"></i>
							</button>
						</span>
					</div>
				</li>
				<li><a href="${request.contextPath}/"><i class="fa fa-dashboard fa-fw"></i>Dashboard</a></li>
				<#list serverMap?keys as key>
				<#assign servers = serverMap[key]>
				<#if servers?size == 1>
				<#assign server = servers?first>
				<li><a href="${request.contextPath}/dashboard/${server.name}-${server.nodeId}"><i class="fa fa-desktop fa-fw"></i>${server.name}</a></li>
				<#else>
				<li><a href="#">${key}<span class="fa arrow"></span></a>
					<ul class="nav nav-second-level">
						<#list servers as server>
						<li><a href="${request.contextPath}/dashboard/${server.name}-${server.nodeId}"><i class="fa fa-desktop fa-fw"></i>${server.nodeId}</a></li>
						</#list>
					</ul>
				</li>
				</#if>
				</#list>
				<li><a href="#"><i class="fa fa-wrench fa-fw"></i>Settings<span class="fa arrow"></span></a>
					<ul class="nav nav-second-level">
						<li><a href="${request.contextPath}/servers">Server</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>