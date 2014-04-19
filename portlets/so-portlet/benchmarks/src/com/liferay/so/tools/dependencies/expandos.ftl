<#list dataFactory.expandoColumnModels as expandoColumnModel>
	insert into ExpandoColumn values (${expandoColumnModel.columnId}, ${expandoColumnModel.companyId}, ${expandoColumnModel.tableId}, '${expandoColumnModel.name}', ${expandoColumnModel.type}, '${expandoColumnModel.defaultData}', '${expandoColumnModel.typeSettings}');
</#list>

<#list dataFactory.expandoTableModels as expandoTableModel>
	insert into ExpandoTable values (${expandoTableModel.tableId}, ${expandoTableModel.companyId}, ${expandoTableModel.classNameId}, '${expandoTableModel.name}');
</#list>