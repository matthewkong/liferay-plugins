<#list dataFactory.layoutModels as layoutModel>
	insert into Layout values (${layoutModel.mvccVersion}, '${layoutModel.uuid}', ${layoutModel.plid}, ${layoutModel.groupId}, ${layoutModel.companyId}, ${layoutModel.userId}, '${layoutModel.userName}', '${dataFactory.getDateString(layoutModel.createDate)}', '${dataFactory.getDateString(layoutModel.modifiedDate)}', ${layoutModel.privateLayout?string}, ${layoutModel.layoutId}, ${layoutModel.parentLayoutId}, '${layoutModel.name}', '${layoutModel.title}', '${layoutModel.description}', '${layoutModel.keywords}', '${layoutModel.robots}', '${layoutModel.type}', '${layoutModel.typeSettings}', ${layoutModel.hidden?string}, '${layoutModel.friendlyURL}', ${layoutModel.iconImageId}, '${layoutModel.themeId}', '${layoutModel.colorSchemeId}', '${layoutModel.wapThemeId}', '${layoutModel.wapColorSchemeId}', '${layoutModel.css}', ${layoutModel.priority}, '${layoutModel.layoutPrototypeUuid}', ${layoutModel.layoutPrototypeLinkEnabled?string}, '${layoutModel.sourcePrototypeLayoutUuid}');
	<#assign layoutFriendlyURLModel = dataFactory.newLayoutFriendlyURLModel(layoutModel)>

	insert into LayoutFriendlyURL values (${layoutFriendlyURLModel.mvccVersion}, '${layoutFriendlyURLModel.uuid}', ${layoutFriendlyURLModel.layoutFriendlyURLId}, ${layoutFriendlyURLModel.groupId}, ${layoutFriendlyURLModel.companyId}, ${layoutFriendlyURLModel.userId}, '${layoutFriendlyURLModel.userName}', '${dataFactory.getDateString(layoutFriendlyURLModel.createDate)}', '${dataFactory.getDateString(layoutFriendlyURLModel.modifiedDate)}', ${layoutFriendlyURLModel.plid}, ${layoutFriendlyURLModel.privateLayout?string}, '${layoutFriendlyURLModel.friendlyURL}', '${layoutFriendlyURLModel.languageId}');
</#list>

<#list dataFactory.layoutSetModels as layoutSetModel>
	insert into LayoutSet values (${layoutSetModel.mvccVersion}, ${layoutSetModel.layoutSetId}, ${layoutSetModel.groupId}, ${layoutSetModel.companyId}, '${dataFactory.getDateString(layoutSetModel.createDate)}', '${dataFactory.getDateString(layoutSetModel.modifiedDate)}', ${layoutSetModel.privateLayout?string}, ${layoutSetModel.logoId}, '${layoutSetModel.themeId}', '${layoutSetModel.colorSchemeId}', '${layoutSetModel.wapThemeId}', '${layoutSetModel.wapColorSchemeId}', '${layoutSetModel.css}', ${layoutSetModel.pageCount}, '${layoutSetModel.settings}', '${layoutSetModel.layoutSetPrototypeUuid}', ${layoutSetModel.layoutSetPrototypeLinkEnabled?string});
</#list>

<#list dataFactory.layoutSetPrototypeModels as layoutSetPrototypeModel>
	insert into LayoutSetPrototype values (${layoutSetPrototypeModel.mvccVersion}, '${layoutSetPrototypeModel.uuid}', ${layoutSetPrototypeModel.layoutSetPrototypeId}, ${layoutSetPrototypeModel.companyId}, ${layoutSetPrototypeModel.userId}, '${layoutSetPrototypeModel.userName}', '${dataFactory.getDateString(layoutSetPrototypeModel.createDate)}', '${dataFactory.getDateString(layoutSetPrototypeModel.modifiedDate)}', '${layoutSetPrototypeModel.name}', '${layoutSetPrototypeModel.description}', '${layoutSetPrototypeModel.settings}', ${layoutSetPrototypeModel.active?string});
</#list>