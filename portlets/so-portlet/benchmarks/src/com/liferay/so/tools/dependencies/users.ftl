<#list dataFactory.getUserIds() as userId>
	<@insertRole
		_userId = userId
	/>
</#list>