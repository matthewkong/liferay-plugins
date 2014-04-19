<#setting number_format = "computer">

<#macro insertRole
	_userId
>
	insert into Users_Roles values (${soUserRoleModel.roleId}, ${_userId});
</#macro>

<#macro insertExpando
	_userId
>
	<#local rowId = dataFactory.getCounterNext()>

	<#local userExpandoRowModel = dataFactory.newExpandoRowModel(rowId, dataFactory.groupExpandoTableId, dataFactory.getGroupId(_userId))>

	insert into ExpandoRow values (${userExpandoRowModel.rowId}, ${userExpandoRowModel.companyId}, '${dataFactory.getDateString(userExpandoRowModel.modifiedDate)}', ${userExpandoRowModel.tableId}, ${userExpandoRowModel.classPK});

	<#local userExpandoValueModel = dataFactory.newExpandoValueModel(dataFactory.getCounterNext(), dataFactory.groupExpandoTableId, dataFactory.groupExpandoColumnId, rowId, dataFactory.getGroupClassNameId(), dataFactory.getGroupId(_userId), "true")>

	insert into ExpandoValue values (${userExpandoValueModel.valueId}, ${userExpandoValueModel.companyId}, ${userExpandoValueModel.tableId}, ${userExpandoValueModel.columnId}, ${userExpandoValueModel.rowId}, ${userExpandoValueModel.classNameId}, ${userExpandoValueModel.classPK}, '${userExpandoValueModel.data}');
</#macro>

<#macro updateGroup
	_userId
>
	update Group_ set typeSettings='${dataFactory.groupTypeSettings}' where groupId=${dataFactory.getGroupId(_userId)};
</#macro>