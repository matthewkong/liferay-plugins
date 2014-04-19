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

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CompanyModel;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.tools.samplesqlbuilder.DataFactory;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoColumnModel;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.model.ExpandoTableModel;
import com.liferay.portlet.expando.model.impl.ExpandoColumnImpl;
import com.liferay.portlet.expando.model.impl.ExpandoTableImpl;
import com.liferay.so.util.SocialOfficeConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Matthew Kong
 */
public class SODataFactory extends DataFactory {

	public SODataFactory(Properties properties) throws Exception {
		super(properties);

		initExpandos();
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

	private List<ExpandoColumnModel> _expandoColumnModels =
		new ArrayList<ExpandoColumnModel>();
	private List<ExpandoTableModel> _expandoTableModels =
		new ArrayList<ExpandoTableModel>();
	private long _groupExpandoColumnId;
	private long _groupExpandoTableId;
	private long _layoutSetPrototypeClassNameId = getClassNameId(
		LayoutSetPrototype.class.getName());
	private long _layoutSetPrototypeExpandoColumnId;
	private long _layoutSetPrototypeExpandoTableId;

}