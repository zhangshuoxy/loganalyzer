<#macro default title="Log Analyzer" scripts="">
<!DOCTYPE html>
<html lang="en">
<#include "header.ftl" />

<body>
<div id="wrapper">
	<nav class="navbar navbar-default navbar-fixed-top" role="navigation" style="margin-bottom: 0">
	<#include "menu.ftl">
	<#include "nav.ftl">
	</nav>

	<div id="page-wrapper">
	<#nested/>
	</div>

	<#include "footer.ftl">
</div>

<script src="${request.contextPath}/theme/js/jquery-1.11.0.min.js"></script>
<script src="${request.contextPath}/theme/js/bootstrap.min.js"></script>
<script src="${request.contextPath}/theme/js/jquery.metisMenu.js"></script>
<script src="${request.contextPath}/theme/js/loganalyzer.js"></script>
${scripts}
</body>
</html>
</#macro>

<#macro blank title="Log Analyzer" scripts="">
<!DOCTYPE html>
<html lang="en">
<#include "header.ftl" />

<body>
<div id="container-fluid">
	<#nested/>
	<#include "footer.ftl">
</div>

<script src="${request.contextPath}/theme/js/jquery-1.11.0.min.js"></script>
<script src="${request.contextPath}/theme/js/bootstrap.min.js"></script>
<script src="${request.contextPath}/theme/js/jquery.metisMenu.js"></script>
<script src="${request.contextPath}/theme/js/loganalyzer.js"></script>
${scripts}
</body>
</html>
</#macro>