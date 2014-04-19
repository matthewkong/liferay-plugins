<#setting number_format = "computer">

<#macro insertRole
	_userId
>
	insert into Users_Roles values (${soUserRoleModel.roleId}, ${_userId});
</#macro>