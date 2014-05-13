<#assign counterModel = dataFactory.getSOCounterModel()>

update Counter set currentId = ${counterModel.currentId} where name = '${counterModel.name}';