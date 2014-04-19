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
import com.liferay.portal.model.LayoutModel;
import com.liferay.portal.model.LayoutSetModel;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.model.LayoutSetPrototypeModel;
import com.liferay.portal.model.LayoutTypePortletConstants;
import com.liferay.portal.model.RoleModel;
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
import com.liferay.so.util.RoleConstants;
import com.liferay.so.util.SocialOfficeConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author Matthew Kong
 */
public class SODataFactory extends DataFactory {

	public SODataFactory(Properties properties) throws Exception {
		super(properties);

		initExpandos();
		initSOUserRoleModel();
	}

	public long getCompanyId() {
		CompanyModel companyModel = getCompanyModel();

		return companyModel.getCompanyId();
	}

	public List<ExpandoColumnModel> getExpandoColumnModels() {
		return _expandoColumnModels;
	}

	public List<ExpandoTableModel> getExpandoTableModels() {
		return _expandoTableModels;
	}

	public RoleModel getSOUserRoleModel() {
		return _soUserRoleModel;
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
	private long _layoutSetPrototypeClassNameId = getClassNameId(
		LayoutSetPrototype.class.getName());
	private long _layoutSetPrototypeExpandoColumnId;
	private long _layoutSetPrototypeExpandoTableId;
	private List<LayoutSetPrototypeModel> _layoutSetPrototypeModels =
					new ArrayList<LayoutSetPrototypeModel>();
	private RoleModel _soUserRoleModel;

}