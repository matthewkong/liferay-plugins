<#list dataFactory.getUserIds() as userId>
	<@insertRole
		_userId = userId
	/>

	<@insertExpando
		_userId = userId
	/>

	<@updateGroup
		_userId = userId
	/>
</#list>