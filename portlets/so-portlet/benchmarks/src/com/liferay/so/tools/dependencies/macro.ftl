<#setting number_format = "computer">

<#macro insertExpando
	_userId
>
	<#local rowId = dataFactory.getCounterNext()>

	<#local userExpandoRowModel = dataFactory.newExpandoRowModel(rowId, dataFactory.groupExpandoTableId, dataFactory.getGroupId(_userId))>

	insert into ExpandoRow values (${userExpandoRowModel.rowId}, ${userExpandoRowModel.companyId}, '${dataFactory.getDateString(userExpandoRowModel.modifiedDate)}', ${userExpandoRowModel.tableId}, ${userExpandoRowModel.classPK});

	<#local userExpandoValueModel = dataFactory.newExpandoValueModel(dataFactory.getCounterNext(), dataFactory.groupExpandoTableId, dataFactory.groupExpandoColumnId, rowId, dataFactory.getGroupClassNameId(), dataFactory.getGroupId(_userId), "true")>

	insert into ExpandoValue values (${userExpandoValueModel.valueId}, ${userExpandoValueModel.companyId}, ${userExpandoValueModel.tableId}, ${userExpandoValueModel.columnId}, ${userExpandoValueModel.rowId}, ${userExpandoValueModel.classNameId}, ${userExpandoValueModel.classPK}, '${userExpandoValueModel.data}');
</#macro>

<#macro insertRole
	_userId
>
	insert into Users_Roles values (${soUserRoleModel.roleId}, ${_userId});
</#macro>

<#macro insertUserLayouts
	_userId
>
	<#local groupId = dataFactory.getGroupId(_userId)>

	<#list dataFactory.userSourcePrototypeLayoutModels as userSourcePrototypeLayoutModel>
		<#local userLayoutModel = dataFactory.newUserLayoutModel(groupId, userSourcePrototypeLayoutModel)>

		insert into Layout values (${userLayoutModel.mvccVersion}, '${userLayoutModel.uuid}', ${userLayoutModel.plid}, ${userLayoutModel.groupId}, ${userLayoutModel.companyId}, ${userLayoutModel.userId}, '${userLayoutModel.userName}', '${dataFactory.getDateString(userLayoutModel.createDate)}', '${dataFactory.getDateString(userLayoutModel.modifiedDate)}', ${userLayoutModel.privateLayout?string}, ${userLayoutModel.layoutId}, ${userLayoutModel.parentLayoutId}, '${userLayoutModel.name}', '${userLayoutModel.title}', '${userLayoutModel.description}', '${userLayoutModel.keywords}', '${userLayoutModel.robots}', '${userLayoutModel.type}', '${userLayoutModel.typeSettings}', ${userLayoutModel.hidden?string}, '${userLayoutModel.friendlyURL}', ${userLayoutModel.iconImageId}, '${userLayoutModel.themeId}', '${userLayoutModel.colorSchemeId}', '${userLayoutModel.wapThemeId}', '${userLayoutModel.wapColorSchemeId}', '${userLayoutModel.css}', ${userLayoutModel.priority}, '${userLayoutModel.layoutPrototypeUuid}', ${userLayoutModel.layoutPrototypeLinkEnabled?string}, '${userLayoutModel.sourcePrototypeLayoutUuid}');

		<#local userLayoutFriendlyURLModel = dataFactory.newLayoutFriendlyURLModel(userLayoutModel)>

		insert into LayoutFriendlyURL values (${userLayoutFriendlyURLModel.mvccVersion}, '${userLayoutFriendlyURLModel.uuid}', ${userLayoutFriendlyURLModel.layoutFriendlyURLId}, ${userLayoutFriendlyURLModel.groupId}, ${userLayoutFriendlyURLModel.companyId}, ${userLayoutFriendlyURLModel.userId}, '${userLayoutFriendlyURLModel.userName}', '${dataFactory.getDateString(userLayoutFriendlyURLModel.createDate)}', '${dataFactory.getDateString(userLayoutFriendlyURLModel.modifiedDate)}', ${userLayoutFriendlyURLModel.plid}, ${userLayoutFriendlyURLModel.privateLayout?string}, '${userLayoutFriendlyURLModel.friendlyURL}', '${userLayoutFriendlyURLModel.languageId}');
	</#list>
</#macro>

<#macro updateGroup
	_userId
>
	update Group_ set typeSettings='${dataFactory.groupTypeSettings}' where groupId=${dataFactory.getGroupId(_userId)};
</#macro>

<#macro updateUserLayoutSet
	_userId
>
	<#list dataFactory.newUserLayoutSetModels(_userId) as layoutSetModel>
		update LayoutSet set themeId='${layoutSetModel.themeId}', colorSchemeId='${layoutSetModel.colorSchemeId}', pageCount=${layoutSetModel.pageCount}, settings_='${layoutSetModel.settings}', layoutSetPrototypeUuid='${layoutSetModel.layoutSetPrototypeUuid}', layoutSetPrototypeLinkEnabled=${layoutSetModel.layoutSetPrototypeLinkEnabled?string} where groupId=${layoutSetModel.groupId} and privateLayout=${layoutSetModel.privateLayout?string};
	</#list>
</#macro>