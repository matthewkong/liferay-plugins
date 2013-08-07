/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This file is part of Liferay Social Office. Liferay Social Office is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Liferay Social Office is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Liferay Social Office. If not, see http://www.gnu.org/licenses/agpl-3.0.html.
 */

package com.liferay.so.activities.portlet;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBMessageServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

/**
 * @author Matthew Kong
 */
public class ActivitiesPortlet extends MVCPortlet {

	@Override
	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

		String actionName = ParamUtil.getString(
			actionRequest, ActionRequest.ACTION_NAME);

		try {
			if (actionName.equals("updateComment")) {
				updateComment(actionRequest, actionResponse);
			}
			else {
				super.processAction(actionRequest, actionResponse);
			}
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	public void updateComment(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		String className = ParamUtil.getString(actionRequest, "className");
		long classPK = ParamUtil.getLong(actionRequest, "classPK");
		long mbMessageId = ParamUtil.getLong(actionRequest, "mbMessageId");
		long mbThreadId = ParamUtil.getLong(actionRequest, "smbThreadId");
		long parentMBMessageId = ParamUtil.getLong(
			actionRequest, "parentMBMessageId");
		String body = ParamUtil.getString(actionRequest, "body");

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		try {
			MBMessage mbMessage = null;

			long groupId = themeDisplay.getScopeGroupId();

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				MBMessage.class.getName(), actionRequest);

			if (cmd.equals(Constants.DELETE)) {
				MBMessageServiceUtil.deleteDiscussionMessage(
					groupId, className, classPK, className, classPK,
					themeDisplay.getUserId(), mbMessageId);
			}
			else if (cmd.equals(Constants.EDIT) && (mbMessageId > 0)) {
				mbMessage = MBMessageServiceUtil.updateDiscussionMessage(
					className, classPK, className, classPK,
					themeDisplay.getUserId(), mbMessageId, StringPool.BLANK,
					body, serviceContext);
			}
			else {
				mbMessage = MBMessageServiceUtil.addDiscussionMessage(
					groupId, className, classPK, className, classPK,
					themeDisplay.getUserId(), mbThreadId, parentMBMessageId,
					StringPool.BLANK, body, serviceContext);
			}

			if (mbMessage != null) {
				jsonObject.put("body", HtmlUtil.escape(mbMessage.getBody()));
				jsonObject.put("mbMessageId", mbMessage.getMessageId());
				jsonObject.put(
					"modifiedDate",
					Time.getRelativeTimeDescription(
						mbMessage.getModifiedDate(), themeDisplay.getLocale(),
						themeDisplay.getTimeZone()));

				User user = UserLocalServiceUtil.fetchUser(
					mbMessage.getUserId());

				if (user != null) {
					jsonObject.put(
						"userDisplayURL", user.getDisplayURL(themeDisplay));
					jsonObject.put(
						"userPortraitURL",
						HtmlUtil.escape(user.getPortraitURL(themeDisplay)));
				}

				jsonObject.put(
					"userName", HtmlUtil.escape(mbMessage.getUserName()));
			}

			jsonObject.put("success", Boolean.TRUE);
		}
		catch (Exception e) {
			jsonObject.put("success", Boolean.FALSE);
		}

		writeJSON(actionRequest, actionResponse, jsonObject);
	}

}