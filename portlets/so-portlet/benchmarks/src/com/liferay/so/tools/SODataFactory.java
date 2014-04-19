/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.so.tools;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CompanyModel;
import com.liferay.portal.model.GroupModel;
import com.liferay.portal.model.LayoutFriendlyURLModel;
import com.liferay.portal.model.LayoutModel;
import com.liferay.portal.model.LayoutSetModel;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.model.LayoutSetPrototypeModel;
import com.liferay.portal.model.LayoutTypePortletConstants;
import com.liferay.portal.model.RoleModel;
import com.liferay.portal.model.UserModel;
import com.liferay.portal.model.impl.LayoutSetPrototypeModelImpl;
import com.liferay.portal.tools.samplesqlbuilder.DataFactory;
import com.liferay.portal.tools.samplesqlbuilder.SequentialUUID;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoColumnModel;
import com.liferay.portlet.expando.model.ExpandoRowModel;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.model.ExpandoTableModel;
import com.liferay.portlet.expando.model.ExpandoValueModel;
import com.liferay.portlet.expando.model.impl.ExpandoColumnImpl;
import com.liferay.portlet.expando.model.impl.ExpandoRowModelImpl;
import com.liferay.portlet.expando.model.impl.ExpandoTableImpl;
import com.liferay.portlet.expando.model.impl.ExpandoValueImpl;
import com.liferay.so.util.PortletKeys;
import com.liferay.so.util.RoleConstants;
import com.liferay.so.util.SocialOfficeConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Matthew Kong
 * @author Sherry Yang
 */
public class SODataFactory extends DataFactory {

	public SODataFactory(Properties properties) throws Exception {
		super(properties);

		initExpandos();
		initSOUserRoleModel();
		initLayoutSetPrototype();
	}

	public long getCompanyId() {
		CompanyModel companyModel = getCompanyModel();

		return companyModel.getCompanyId();
	}

	public long getDefaultUserId() {
		UserModel defaultUserModel = getDefaultUserModel();

		return defaultUserModel.getUserId();
	}

	public List<ExpandoColumnModel> getExpandoColumnModels() {
		return _expandoColumnModels;
	}

	public List<ExpandoRowModel> getExpandoRowModels() {
		return _expandoRowModels;
	}

	public List<ExpandoTableModel> getExpandoTableModels() {
		return _expandoTableModels;
	}

	public List<ExpandoValueModel> getExpandoValueModels() {
		return _expandoValueModels;
	}

	public long getGroupExpandoColumnId() {
		return _groupExpandoColumnId;
	}

	public long getGroupExpandoTableId() {
		return _groupExpandoTableId;
	}

	public long getGroupId(long userId) {
		return _userGroupIds.get(userId);
	}

	public String getGroupTypeSettings() {
		return _GROUP_TYPE_SETTINGS;
	}

	public List<LayoutModel> getLayoutModels() {
		_siteLayoutModels.addAll(_userSourcePrototypeLayoutModels);

		return _siteLayoutModels;
	}

	public List<LayoutSetModel> getLayoutSetModels() {
		return _layoutSetModels;
	}

	public List<GroupModel> getLayoutSetPrototypeGroupModels() {
		return _layoutSetPrototypeGroupModels;
	}

	public List<LayoutSetPrototypeModel> getLayoutSetPrototypeModels() {
		return _layoutSetPrototypeModels;
	}

	public List<LayoutModel> getSiteLayoutModels() {
		return _siteLayoutModels;
	}

	public RoleModel getSOUserRoleModel() {
		return _soUserRoleModel;
	}

	public Set<Long> getUserIds() {
		return _userGroupIds.keySet();
	}

	public List<LayoutModel> getUserSourcePrototypeLayoutModels() {
		return _userSourcePrototypeLayoutModels;
	}

	public void initExpandos() {
		_groupExpandoColumnId = getCounterNext();
		_groupExpandoTableId = getCounterNext();

		newExpandoColumnModel(
			_groupExpandoColumnId, _groupExpandoTableId, "socialOfficeEnabled",
			ExpandoColumnConstants.BOOLEAN, StringPool.BLANK, StringPool.BLANK);

		newExpandoTableModel(
			_groupExpandoTableId, getGroupClassNameId(),
			ExpandoTableConstants.DEFAULT_TABLE_NAME);

		_layoutSetPrototypeExpandoColumnId = getCounterNext();
		_layoutSetPrototypeExpandoTableId = getCounterNext();

		newExpandoColumnModel(
			_layoutSetPrototypeExpandoColumnId,
			_layoutSetPrototypeExpandoTableId,
			SocialOfficeConstants.LAYOUT_SET_PROTOTYPE_KEY,
			ExpandoColumnConstants.STRING, StringPool.BLANK, StringPool.BLANK);

		newExpandoTableModel(
			_layoutSetPrototypeExpandoTableId, _layoutSetPrototypeClassNameId,
			ExpandoTableConstants.DEFAULT_TABLE_NAME);
	}

	public void initLayoutSetPrototype() throws Exception {
		setupLayoutSetPrototypeSite();
		setupLayoutSetPrototypeUserPrivate();
		setupLayoutSetPrototypeUserPublic();
	}

	public void initSOUserRoleModel() {
		_soUserRoleModel = newRoleModel(
			RoleConstants.SOCIAL_OFFICE_USER, RoleConstants.TYPE_REGULAR);
	}

	public ExpandoRowModel newExpandoRowModel(
		long rowId, long tableId, long classPK) {

		ExpandoRowModel expandoRowModel = new ExpandoRowModelImpl();

		expandoRowModel.setRowId(rowId);
		expandoRowModel.setCompanyId(getCompanyId());
		expandoRowModel.setModifiedDate(new Date());
		expandoRowModel.setTableId(tableId);
		expandoRowModel.setClassPK(classPK);

		return expandoRowModel;
	}

	public ExpandoValueModel newExpandoValueModel(
		long valueId, long tableId, long columnId, long rowId, long classNameId,
		long classPK, String data) {

		ExpandoValueModel expandoValueModel = new ExpandoValueImpl();

		expandoValueModel.setValueId(valueId);
		expandoValueModel.setCompanyId(getCompanyId());
		expandoValueModel.setTableId(tableId);
		expandoValueModel.setColumnId(columnId);
		expandoValueModel.setRowId(rowId);
		expandoValueModel.setClassNameId(classNameId);
		expandoValueModel.setClassPK(classPK);
		expandoValueModel.setData(data);

		return expandoValueModel;
	}

	@Override
	public GroupModel newGroupModel(UserModel userModel) throws Exception {
		GroupModel groupModel = super.newGroupModel(userModel);

		_userGroupIds.put(userModel.getUserId(), groupModel.getGroupId());

		return groupModel;
	}

	@Override
	public LayoutFriendlyURLModel newLayoutFriendlyURLModel(
		LayoutModel layoutModel) {

		LayoutFriendlyURLModel layoutFriendlyURLModel =
			super.newLayoutFriendlyURLModel(layoutModel);

		layoutFriendlyURLModel.setPrivateLayout(layoutModel.getPrivateLayout());

		return layoutFriendlyURLModel;
	}

	public LayoutModel newUserLayoutModel(
		long groupId, LayoutModel sourcePrototypeLayout) {

		LayoutModel layoutModel = (LayoutModel)sourcePrototypeLayout.clone();
		layoutModel.setPlid(getCounterNext());
		layoutModel.setGroupId(groupId);
		layoutModel.setLayoutId(getCounterNext());

		StringBundler sb = new StringBundler(4);

		sb.append("last-merge-time=");
		sb.append(System.currentTimeMillis());
		sb.append(StringPool.SPACE);
		sb.append(sourcePrototypeLayout.getTypeSettings());

		layoutModel.setTypeSettings(sb.toString());

		layoutModel.setSourcePrototypeLayoutUuid(
			sourcePrototypeLayout.getUuid());

		return layoutModel;
	}

	public List<LayoutSetModel> newUserLayoutSetModels(long userId) {
		List<LayoutSetModel> userLayoutSetModels =
			new ArrayList<LayoutSetModel>();

		LayoutSetModel userPrivateLayoutSetModel = newSOLayoutSetModel(
			getGroupId(userId), true,
			_userPrivateLayoutSetPrototypeLayoutSize + 1);

		userPrivateLayoutSetModel.setSettings(
			"last-merge-time=" + System.currentTimeMillis());
		userPrivateLayoutSetModel.setLayoutSetPrototypeUuid(
			_userPrivateLayoutSetPrototypeModel.getUuid());
		userPrivateLayoutSetModel.setLayoutSetPrototypeLinkEnabled(true);

		userLayoutSetModels.add(userPrivateLayoutSetModel);

		LayoutSetModel userPublicLayoutSetModel = newSOLayoutSetModel(
			getGroupId(userId), false,
			_userPublicLayoutSetPrototypeLayoutSize + 1);

		userPublicLayoutSetModel.setSettings(
			"last-merge-time=" + System.currentTimeMillis());
		userPublicLayoutSetModel.setLayoutSetPrototypeUuid(
			_userPublicLayoutSetPrototypeModel.getUuid());
		userPublicLayoutSetModel.setLayoutSetPrototypeLinkEnabled(true);

		userLayoutSetModels.add(userPublicLayoutSetModel);

		return userLayoutSetModels;
	}

	protected ExpandoColumnModel newExpandoColumnModel(
		long columnId, long tableId, String name, int type, String defaultData,
		String typeSettings) {

		ExpandoColumnModel expandoColumnModel = new ExpandoColumnImpl();

		expandoColumnModel.setColumnId(columnId);
		expandoColumnModel.setCompanyId(getCompanyId());
		expandoColumnModel.setTableId(tableId);
		expandoColumnModel.setName(name);
		expandoColumnModel.setType(type);
		expandoColumnModel.setDefaultData(defaultData);
		expandoColumnModel.setTypeSettings(typeSettings);

		_expandoColumnModels.add(expandoColumnModel);

		return expandoColumnModel;
	}

	protected ExpandoTableModel newExpandoTableModel(
		long tableId, long classNameId, String name) {

		ExpandoTableModel expandoTableModel = new ExpandoTableImpl();

		expandoTableModel.setTableId(tableId);
		expandoTableModel.setCompanyId(getCompanyId());
		expandoTableModel.setClassNameId(classNameId);
		expandoTableModel.setName(name);

		_expandoTableModels.add(expandoTableModel);

		return expandoTableModel;
	}

	protected LayoutModel newLayoutModel(
		long groupId, String name, boolean privateLayout, String templateId,
		String column1, String column2) {

		LayoutModel layoutModel = newLayoutModel(
			groupId, name, StringPool.BLANK, StringPool.BLANK);

		layoutModel.setPrivateLayout(privateLayout);

		String friendlyURL = StringUtil.lowerCase(
			StringUtil.replace(name, StringPool.SPACE, StringPool.DASH));

		layoutModel.setFriendlyURL("/so/" + friendlyURL);

		UnicodeProperties typeSettingsProperties = new UnicodeProperties(true);

		typeSettingsProperties.setProperty(
			LayoutTypePortletConstants.LAYOUT_TEMPLATE_ID, templateId);
		typeSettingsProperties.setProperty("column-1", column1);

		if (Validator.isNotNull(column2)) {
			typeSettingsProperties.setProperty("column-2", column2);
		}

		String typeSettings = StringUtil.replace(
			typeSettingsProperties.toString(), "\n", "\\n");

		layoutModel.setTypeSettings(typeSettings);

		return layoutModel;
	}

	protected GroupModel newLayoutSetPrototypeGroupModel(
			long groupId, long classNameId, long classPK, String name,
			boolean site)
		throws Exception {

		GroupModel groupModel = super.newGroupModel(
			groupId, classNameId, classPK, name, site);

		groupModel.setTypeSettings(_GROUP_TYPE_SETTINGS);
		groupModel.setFriendlyURL("/template-" + groupModel.getGroupId());

		return groupModel;
	}

	protected LayoutSetPrototypeModel newLayoutSetPrototypeModel(
		long layoutSetPrototypeId, long userId, String name, String description,
		boolean active) {

		LayoutSetPrototypeModel layoutSetPrototypeModel =
			new LayoutSetPrototypeModelImpl();

		layoutSetPrototypeModel.setUuid(SequentialUUID.generate());
		layoutSetPrototypeModel.setLayoutSetPrototypeId(layoutSetPrototypeId);
		layoutSetPrototypeModel.setCompanyId(getCompanyId());
		layoutSetPrototypeModel.setUserId(userId);
		layoutSetPrototypeModel.setCreateDate(new Date());
		layoutSetPrototypeModel.setModifiedDate(new Date());

		StringBundler sb = new StringBundler(5);

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root ");
		sb.append("available-locales=\"en_US\" default-locale=\"en_US\">");
		sb.append("<Name language-id=\"en_US\"> ");
		sb.append(name);
		sb.append("</Name></root>");

		layoutSetPrototypeModel.setName(sb.toString());

		sb = new StringBundler(5);

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root ");
		sb.append("available-locales=\"en_US\" default-locale=\"en_US\">");
		sb.append("<Description language-id=\"en_US\"> ");
		sb.append(name);
		sb.append("</Description></root>");

		layoutSetPrototypeModel.setDescription(description);

		UnicodeProperties typeSettingsProperties = new UnicodeProperties(true);

		typeSettingsProperties.setProperty(
			"layoutsUpdateable", StringPool.TRUE);
		typeSettingsProperties.setProperty(
			"customJspServletContextName", "so-hook");

		String typeSettings = StringUtil.replace(
			typeSettingsProperties.toString(), "\n", "\\n");

		layoutSetPrototypeModel.setSettings(typeSettings);

		layoutSetPrototypeModel.setActive(active);

		_layoutSetPrototypeModels.add(layoutSetPrototypeModel);

		return layoutSetPrototypeModel;
	}

	protected LayoutSetModel newSOLayoutSetModel(
		long groupId, boolean privateLayout, int pageCount) {

		LayoutSetModel layoutSetModel = newLayoutSetModel(
			groupId, privateLayout, pageCount);

		layoutSetModel.setThemeId("so_WAR_sotheme");
		layoutSetModel.setColorSchemeId("01");

		return layoutSetModel;
	}

	protected void setupLayoutSetPrototypeSite() throws Exception {
		long siteLayoutSetPrototypeId = getCounterNext();

		newLayoutSetPrototypeModel(
			siteLayoutSetPrototypeId, getDefaultUserId(),
			"Default Social Office Site", StringPool.BLANK, true);

		long expandoRowId = getCounterNext();

		ExpandoValueModel expandoValueModel = newExpandoValueModel(
			getCounterNext(), _layoutSetPrototypeExpandoTableId,
			_layoutSetPrototypeExpandoColumnId, expandoRowId,
			_layoutSetPrototypeClassNameId, siteLayoutSetPrototypeId,
			SocialOfficeConstants.LAYOUT_SET_PROTOTYPE_KEY_SITE);

		_expandoValueModels.add(expandoValueModel);

		ExpandoRowModel expandoRowModel = newExpandoRowModel(
			expandoRowId, _layoutSetPrototypeExpandoTableId,
			siteLayoutSetPrototypeId);

		_expandoRowModels.add(expandoRowModel);

		long siteLayoutSetPrototypeGroupId = getCounterNext();

		_layoutSetPrototypeGroupModels.add(
			newLayoutSetPrototypeGroupModel(
				siteLayoutSetPrototypeGroupId, _layoutSetPrototypeClassNameId,
				siteLayoutSetPrototypeId,
				String.valueOf(siteLayoutSetPrototypeId), false));

		// Home

		String column1 =
			PortletKeys.SO_ANNOUNCEMENTS + StringPool.COMMA +
				PortletKeys.SO_ACTIVITIES;

		StringBundler sb = new StringBundler(8);

		sb.append("1_WAR_wysiwygportlet_INSTANCE_abcd,");
		sb.append(PortletKeys.BOOKMARKS);
		sb.append(StringPool.COMMA);
		sb.append(PortletKeys.RSS);
		sb.append("_INSTANCE_abcd,");
		sb.append(PortletKeys.RECENT_DOCUMENTS);
		sb.append(StringPool.COMMA);
		sb.append("1_WAR_eventsdisplayportlet");

		//Home

		_siteLayoutModels.add(
			newLayoutModel(
				siteLayoutSetPrototypeGroupId, "Home", true, "2_columns_iii",
				column1, sb.toString()));

		// Calendar

		_siteLayoutModels.add(
			newLayoutModel(
				siteLayoutSetPrototypeGroupId, "Calendar", true, "1_column",
				"1_WAR_calendarportlet", null));

		// Documents

		_siteLayoutModels.add(
			newLayoutModel(
				siteLayoutSetPrototypeGroupId, "Documents", true, "1_column",
				PortletKeys.DOCUMENT_LIBRARY, null));

		// Forums

		column1 =
			PortletKeys.BREADCRUMB + "_INSTANCE_abcd," +
				PortletKeys.MESSAGE_BOARDS;

		_siteLayoutModels.add(
			newLayoutModel(
				siteLayoutSetPrototypeGroupId, "Forums", true, "1_column",
				column1, null));

		// Blog

		_siteLayoutModels.add(
			newLayoutModel(
				siteLayoutSetPrototypeGroupId, "Blogs", true, "2_columns_iii",
				PortletKeys.BLOGS, PortletKeys.BLOGS_AGGREGATOR));

		// Wiki

		_siteLayoutModels.add(
			newLayoutModel(
				siteLayoutSetPrototypeGroupId, "Wiki", true, "1_column",
				PortletKeys.WIKI, null));

		// Members

		_siteLayoutModels.add(
			newLayoutModel(
				siteLayoutSetPrototypeGroupId, "Members", true, "1_column",
				PortletKeys.SO_INVITE_MEMBERS + ",4_WAR_contactsportlet",
				null));

		_layoutSetModels.add(
			newSOLayoutSetModel(siteLayoutSetPrototypeGroupId, true,
			_siteLayoutModels.size()));
		_layoutSetModels.add(
			newSOLayoutSetModel(siteLayoutSetPrototypeGroupId, false, 0));
	}

	protected void setupLayoutSetPrototypeUserPrivate() throws Exception {
		long userPrivateLayoutSetPrototypeId = getCounterNext();

		_userPrivateLayoutSetPrototypeModel = newLayoutSetPrototypeModel(
			userPrivateLayoutSetPrototypeId, getDefaultUserId(),
			"Social Office User Private Home", StringPool.BLANK, true);

		long expandoRowId = getCounterNext();

		ExpandoValueModel expandoValueModel = newExpandoValueModel(
			getCounterNext(), _layoutSetPrototypeExpandoTableId,
			_layoutSetPrototypeExpandoColumnId, expandoRowId,
			_layoutSetPrototypeClassNameId, userPrivateLayoutSetPrototypeId,
			SocialOfficeConstants.LAYOUT_SET_PROTOTYPE_KEY_USER_PRIVATE);

		_expandoValueModels.add(expandoValueModel);

		ExpandoRowModel expandoRowModel = newExpandoRowModel(
			expandoRowId, _layoutSetPrototypeExpandoTableId,
			userPrivateLayoutSetPrototypeId);

		_expandoRowModels.add(expandoRowModel);

		long userPrivateLayoutSetPrototypeGroupId = getCounterNext();

		_layoutSetPrototypeGroupModels.add(
			newLayoutSetPrototypeGroupModel(
				userPrivateLayoutSetPrototypeGroupId,
				_layoutSetPrototypeClassNameId, userPrivateLayoutSetPrototypeId,
				String.valueOf(userPrivateLayoutSetPrototypeId), false));

		int originalSize = _userSourcePrototypeLayoutModels.size();

		// Dashboard

		_userSourcePrototypeLayoutModels.add(
			newLayoutModel(
				userPrivateLayoutSetPrototypeGroupId, "Dashboard", true,
				"2_columns_iii",
				"2_WAR_microblogsportlet,1_WAR_soannouncementsportlet," +
					"1_WAR_soportlet",
				"2_WAR_tasksportlet,1_WAR_eventsdisplayportlet"));

		// Contacts Center

		_userSourcePrototypeLayoutModels.add(
			newLayoutModel(
				userPrivateLayoutSetPrototypeGroupId, "Contacts Center", true,
				"1_column", "1_WAR_contactsportlet", null));

		// Microblogs

		_userSourcePrototypeLayoutModels.add(
			newLayoutModel(
				userPrivateLayoutSetPrototypeGroupId, "Microblogs", true,
				"1_column", "1_WAR_microblogsportlet", null));

		// Messages

		_userSourcePrototypeLayoutModels.add(
			newLayoutModel(
				userPrivateLayoutSetPrototypeGroupId, "Messages", true,
				"1_column", "1_WAR_privatemessagingportlet", null));

		// Documents

		_userSourcePrototypeLayoutModels.add(
			newLayoutModel(
				userPrivateLayoutSetPrototypeGroupId, "Documents", true,
				"1_column", PortletKeys.DOCUMENT_LIBRARY, null));

		// Tasks

		_userSourcePrototypeLayoutModels.add(
			newLayoutModel(
				userPrivateLayoutSetPrototypeGroupId, "Tasks", true, "1_column",
				"1_WAR_tasksportlet", null));

		_userPrivateLayoutSetPrototypeLayoutSize =
			_userSourcePrototypeLayoutModels.size() - originalSize;

		_layoutSetModels.add(
			newSOLayoutSetModel(
				userPrivateLayoutSetPrototypeGroupId, true,
				_userPrivateLayoutSetPrototypeLayoutSize));
		_layoutSetModels.add(
			newSOLayoutSetModel(
				userPrivateLayoutSetPrototypeGroupId, false, 0));
	}

	protected void setupLayoutSetPrototypeUserPublic() throws Exception {
		long userPublicLayoutSetPrototypeId = getCounterNext();

		_userPublicLayoutSetPrototypeModel = newLayoutSetPrototypeModel(
			userPublicLayoutSetPrototypeId, getDefaultUserId(),
			"Social Office User Public Home", StringPool.BLANK, true);

		long expandoRowId = getCounterNext();

		ExpandoValueModel expandoValueModel = newExpandoValueModel(
			getCounterNext(), _layoutSetPrototypeExpandoTableId,
			_layoutSetPrototypeExpandoColumnId, expandoRowId,
			_layoutSetPrototypeClassNameId, userPublicLayoutSetPrototypeId,
			SocialOfficeConstants.LAYOUT_SET_PROTOTYPE_KEY_USER_PRIVATE);

		_expandoValueModels.add(expandoValueModel);

		ExpandoRowModel expandoRowModel = newExpandoRowModel(
			expandoRowId, _layoutSetPrototypeExpandoTableId,
			userPublicLayoutSetPrototypeId);

		_expandoRowModels.add(expandoRowModel);

		long userPublicLayoutSetPrototypeGroupId = getCounterNext();

		_layoutSetPrototypeGroupModels.add(
			newLayoutSetPrototypeGroupModel(
				userPublicLayoutSetPrototypeGroupId,
				_layoutSetPrototypeClassNameId, userPublicLayoutSetPrototypeId,
				String.valueOf(userPublicLayoutSetPrototypeId), false));

		int originalSize = _userSourcePrototypeLayoutModels.size();

		// Profile

		_userSourcePrototypeLayoutModels.add(
			newLayoutModel(
				userPublicLayoutSetPrototypeGroupId, "Profile", false,
				"1_2_columns_ii", "2_WAR_contactsportlet_INSTANCE_abcd",
				"2_WAR_microblogsportlet,2_WAR_contactsportlet_INSTANCE_efgh," +
					PortletKeys.SO_ACTIVITIES));

		// Contacts

		_userSourcePrototypeLayoutModels.add(
			newLayoutModel(
				userPublicLayoutSetPrototypeGroupId, "Contacts", false,
				"1_column",
				"2_WAR_contactsportlet_INSTANCE_abcd,1_WAR_contactsportlet",
				null));

		// Microblogs

		_userSourcePrototypeLayoutModels.add(
			newLayoutModel(
				userPublicLayoutSetPrototypeGroupId, "Microblogs", false,
				"1_column",
				"2_WAR_contactsportlet_INSTANCE_abcd,1_WAR_microblogsportlet",
				null));

		_userPublicLayoutSetPrototypeLayoutSize =
			_userSourcePrototypeLayoutModels.size() - originalSize;

		_layoutSetModels.add(
			newSOLayoutSetModel(userPublicLayoutSetPrototypeGroupId, true, 0));
		_layoutSetModels.add(
			newSOLayoutSetModel(
				userPublicLayoutSetPrototypeGroupId, false,
				_userPublicLayoutSetPrototypeLayoutSize));
	}

	private static final String _GROUP_TYPE_SETTINGS =
		"customJspServletContextName=so-hook";

	private List<ExpandoColumnModel> _expandoColumnModels =
		new ArrayList<ExpandoColumnModel>();
	private List<ExpandoRowModel> _expandoRowModels =
		new ArrayList<ExpandoRowModel>();
	private List<ExpandoTableModel> _expandoTableModels =
		new ArrayList<ExpandoTableModel>();
	private List<ExpandoValueModel> _expandoValueModels =
		new ArrayList<ExpandoValueModel>();
	private long _groupExpandoColumnId;
	private long _groupExpandoTableId;
	private List<LayoutSetModel> _layoutSetModels =
		new ArrayList<LayoutSetModel>();
	private long _layoutSetPrototypeClassNameId = getClassNameId(
		LayoutSetPrototype.class.getName());
	private long _layoutSetPrototypeExpandoColumnId;
	private long _layoutSetPrototypeExpandoTableId;
	private List<GroupModel> _layoutSetPrototypeGroupModels =
		new ArrayList<GroupModel>();
	private List<LayoutSetPrototypeModel> _layoutSetPrototypeModels =
		new ArrayList<LayoutSetPrototypeModel>();
	private List<LayoutModel> _siteLayoutModels = new ArrayList<LayoutModel>();
	private RoleModel _soUserRoleModel;
	private Map<Long, Long> _userGroupIds = new HashMap<Long, Long>();
	private int _userPrivateLayoutSetPrototypeLayoutSize;
	private LayoutSetPrototypeModel _userPrivateLayoutSetPrototypeModel;
	private int _userPublicLayoutSetPrototypeLayoutSize;
	private LayoutSetPrototypeModel _userPublicLayoutSetPrototypeModel;
	private List<LayoutModel> _userSourcePrototypeLayoutModels =
		new ArrayList<LayoutModel>();

}