<#list dataFactory.getServletContextNames() as servletContextName>
	<#assign date = dataFactory.getDate()>
	insert into Release_ values (0, ${dataFactory.getCounterNext()}, '${date}', '${date}', '${servletContextName}', ${dataFactory.getBuildNumber(servletContextName)}, null, true, 0, '');
</#list>