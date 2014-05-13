/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.microblogs.tools;

import com.liferay.microblogs.model.MicroblogsEntryConstants;
import com.liferay.microblogs.model.MicroblogsEntryModel;
import com.liferay.microblogs.model.impl.MicroblogsEntryModelImpl;
import com.liferay.portal.kernel.portlet.PortletClassLoaderUtil;
import com.liferay.portal.kernel.util.ClassLoaderPool;
import com.liferay.portal.model.CompanyModel;
import com.liferay.portal.tools.samplesqlbuilder.DataFactory;
import com.liferay.util.SimpleCounter;

import java.util.Date;
import java.util.Properties;

/**
 * @author Tina Tian
 */
public class MicroblogsDataFactory extends DataFactory {

	public MicroblogsDataFactory(Properties properties) throws Exception {
		super(properties);
	}

	public MicroblogsEntryModel getMicroblogsEntryModel() {
		PortletClassLoaderUtil.setServletContextName("microblog");

		Thread currentThread = Thread.currentThread();

		ClassLoader classLoader = currentThread.getContextClassLoader();

		ClassLoaderPool.register("microblog", classLoader);

		MicroblogsEntryModel microblogsEntryModel =
			new MicroblogsEntryModelImpl();

		SimpleCounter counter = getCounter();
		CompanyModel company = getCompanyModel();

		microblogsEntryModel.setMicroblogsEntryId(counter.get());
		microblogsEntryModel.setCompanyId(company.getCompanyId());
		microblogsEntryModel.setUserId(getSampleUserModel().getUserId());
		microblogsEntryModel.setCreateDate(new Date());
		microblogsEntryModel.setModifiedDate(new Date());
		microblogsEntryModel.setContent("This is test conent for microblog.");
		microblogsEntryModel.setType(MicroblogsEntryConstants.TYPE_EVERYONE);

		return microblogsEntryModel;
	}

}