<#setting number_format = "computer">

<#assign microblogsEntryModel = dataFactory.getMicroblogsEntryModel() >

insert into MicroblogsEntry values (${microblogsEntryModel.microblogsEntryId}, ${microblogsEntryModel.companyId}, ${microblogsEntryModel.userId}, '${microblogsEntryModel.userName}', '${dataFactory.getDateString(microblogsEntryModel.modifiedDate)}', '${dataFactory.getDateString(microblogsEntryModel.modifiedDate)}', '${microblogsEntryModel.content}', ${microblogsEntryModel.type}, ${microblogsEntryModel.receiverUserId}, ${microblogsEntryModel.receiverMicroblogsEntryId}, ${microblogsEntryModel.socialRelationType});

${microblogsCSVWriter.write(microblogsEntryModel.microblogsEntryId + "," + microblogsEntryModel.content + "\n")}