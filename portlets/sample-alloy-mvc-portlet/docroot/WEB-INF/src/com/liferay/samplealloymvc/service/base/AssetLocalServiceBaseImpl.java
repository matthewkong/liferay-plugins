/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.samplealloymvc.service.base;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistryUtil;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;

import com.liferay.samplealloymvc.model.Asset;
import com.liferay.samplealloymvc.service.AssetLocalService;
import com.liferay.samplealloymvc.service.persistence.AssetPersistence;
import com.liferay.samplealloymvc.service.persistence.CheckoutPersistence;
import com.liferay.samplealloymvc.service.persistence.DefinitionPersistence;
import com.liferay.samplealloymvc.service.persistence.TypePersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the asset local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.samplealloymvc.service.impl.AssetLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.samplealloymvc.service.impl.AssetLocalServiceImpl
 * @see com.liferay.samplealloymvc.service.AssetLocalServiceUtil
 * @generated
 */
@ProviderType
public abstract class AssetLocalServiceBaseImpl extends BaseLocalServiceImpl
	implements AssetLocalService, IdentifiableOSGiService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.samplealloymvc.service.AssetLocalServiceUtil} to access the asset local service.
	 */

	/**
	 * Adds the asset to the database. Also notifies the appropriate model listeners.
	 *
	 * @param asset the asset
	 * @return the asset that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public Asset addAsset(Asset asset) {
		asset.setNew(true);

		return assetPersistence.update(asset);
	}

	/**
	 * Creates a new asset with the primary key. Does not add the asset to the database.
	 *
	 * @param assetId the primary key for the new asset
	 * @return the new asset
	 */
	@Override
	public Asset createAsset(long assetId) {
		return assetPersistence.create(assetId);
	}

	/**
	 * Deletes the asset with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param assetId the primary key of the asset
	 * @return the asset that was removed
	 * @throws PortalException if a asset with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public Asset deleteAsset(long assetId) throws PortalException {
		return assetPersistence.remove(assetId);
	}

	/**
	 * Deletes the asset from the database. Also notifies the appropriate model listeners.
	 *
	 * @param asset the asset
	 * @return the asset that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public Asset deleteAsset(Asset asset) {
		return assetPersistence.remove(asset);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(Asset.class,
			clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return assetPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.samplealloymvc.model.impl.AssetModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end) {
		return assetPersistence.findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.samplealloymvc.model.impl.AssetModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end, OrderByComparator<T> orderByComparator) {
		return assetPersistence.findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return assetPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery,
		Projection projection) {
		return assetPersistence.countWithDynamicQuery(dynamicQuery, projection);
	}

	@Override
	public Asset fetchAsset(long assetId) {
		return assetPersistence.fetchByPrimaryKey(assetId);
	}

	/**
	 * Returns the asset with the primary key.
	 *
	 * @param assetId the primary key of the asset
	 * @return the asset
	 * @throws PortalException if a asset with the primary key could not be found
	 */
	@Override
	public Asset getAsset(long assetId) throws PortalException {
		return assetPersistence.findByPrimaryKey(assetId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(com.liferay.samplealloymvc.service.AssetLocalServiceUtil.getService());
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(Asset.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("assetId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		IndexableActionableDynamicQuery indexableActionableDynamicQuery = new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(com.liferay.samplealloymvc.service.AssetLocalServiceUtil.getService());
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(Asset.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName("assetId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {
		actionableDynamicQuery.setBaseLocalService(com.liferay.samplealloymvc.service.AssetLocalServiceUtil.getService());
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(Asset.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("assetId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {
		return assetLocalService.deleteAsset((Asset)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {
		return assetPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the assets.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.samplealloymvc.model.impl.AssetModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of assets
	 * @param end the upper bound of the range of assets (not inclusive)
	 * @return the range of assets
	 */
	@Override
	public List<Asset> getAssets(int start, int end) {
		return assetPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of assets.
	 *
	 * @return the number of assets
	 */
	@Override
	public int getAssetsCount() {
		return assetPersistence.countAll();
	}

	/**
	 * Updates the asset in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param asset the asset
	 * @return the asset that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public Asset updateAsset(Asset asset) {
		return assetPersistence.update(asset);
	}

	/**
	 * Returns the asset local service.
	 *
	 * @return the asset local service
	 */
	public AssetLocalService getAssetLocalService() {
		return assetLocalService;
	}

	/**
	 * Sets the asset local service.
	 *
	 * @param assetLocalService the asset local service
	 */
	public void setAssetLocalService(AssetLocalService assetLocalService) {
		this.assetLocalService = assetLocalService;
	}

	/**
	 * Returns the asset persistence.
	 *
	 * @return the asset persistence
	 */
	public AssetPersistence getAssetPersistence() {
		return assetPersistence;
	}

	/**
	 * Sets the asset persistence.
	 *
	 * @param assetPersistence the asset persistence
	 */
	public void setAssetPersistence(AssetPersistence assetPersistence) {
		this.assetPersistence = assetPersistence;
	}

	/**
	 * Returns the checkout local service.
	 *
	 * @return the checkout local service
	 */
	public com.liferay.samplealloymvc.service.CheckoutLocalService getCheckoutLocalService() {
		return checkoutLocalService;
	}

	/**
	 * Sets the checkout local service.
	 *
	 * @param checkoutLocalService the checkout local service
	 */
	public void setCheckoutLocalService(
		com.liferay.samplealloymvc.service.CheckoutLocalService checkoutLocalService) {
		this.checkoutLocalService = checkoutLocalService;
	}

	/**
	 * Returns the checkout persistence.
	 *
	 * @return the checkout persistence
	 */
	public CheckoutPersistence getCheckoutPersistence() {
		return checkoutPersistence;
	}

	/**
	 * Sets the checkout persistence.
	 *
	 * @param checkoutPersistence the checkout persistence
	 */
	public void setCheckoutPersistence(CheckoutPersistence checkoutPersistence) {
		this.checkoutPersistence = checkoutPersistence;
	}

	/**
	 * Returns the definition local service.
	 *
	 * @return the definition local service
	 */
	public com.liferay.samplealloymvc.service.DefinitionLocalService getDefinitionLocalService() {
		return definitionLocalService;
	}

	/**
	 * Sets the definition local service.
	 *
	 * @param definitionLocalService the definition local service
	 */
	public void setDefinitionLocalService(
		com.liferay.samplealloymvc.service.DefinitionLocalService definitionLocalService) {
		this.definitionLocalService = definitionLocalService;
	}

	/**
	 * Returns the definition persistence.
	 *
	 * @return the definition persistence
	 */
	public DefinitionPersistence getDefinitionPersistence() {
		return definitionPersistence;
	}

	/**
	 * Sets the definition persistence.
	 *
	 * @param definitionPersistence the definition persistence
	 */
	public void setDefinitionPersistence(
		DefinitionPersistence definitionPersistence) {
		this.definitionPersistence = definitionPersistence;
	}

	/**
	 * Returns the type local service.
	 *
	 * @return the type local service
	 */
	public com.liferay.samplealloymvc.service.TypeLocalService getTypeLocalService() {
		return typeLocalService;
	}

	/**
	 * Sets the type local service.
	 *
	 * @param typeLocalService the type local service
	 */
	public void setTypeLocalService(
		com.liferay.samplealloymvc.service.TypeLocalService typeLocalService) {
		this.typeLocalService = typeLocalService;
	}

	/**
	 * Returns the type persistence.
	 *
	 * @return the type persistence
	 */
	public TypePersistence getTypePersistence() {
		return typePersistence;
	}

	/**
	 * Sets the type persistence.
	 *
	 * @param typePersistence the type persistence
	 */
	public void setTypePersistence(TypePersistence typePersistence) {
		this.typePersistence = typePersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.kernel.service.ClassNameLocalService getClassNameLocalService() {
		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.kernel.service.ClassNameLocalService classNameLocalService) {
		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {
		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		Class<?> clazz = getClass();

		_classLoader = clazz.getClassLoader();

		PersistedModelLocalServiceRegistryUtil.register("com.liferay.samplealloymvc.model.Asset",
			assetLocalService);
	}

	public void destroy() {
		PersistedModelLocalServiceRegistryUtil.unregister(
			"com.liferay.samplealloymvc.model.Asset");
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return AssetLocalService.class.getName();
	}

	@Override
	public Object invokeMethod(String name, String[] parameterTypes,
		Object[] arguments) throws Throwable {
		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		if (contextClassLoader != _classLoader) {
			currentThread.setContextClassLoader(_classLoader);
		}

		try {
			return _clpInvoker.invokeMethod(name, parameterTypes, arguments);
		}
		finally {
			if (contextClassLoader != _classLoader) {
				currentThread.setContextClassLoader(contextClassLoader);
			}
		}
	}

	protected Class<?> getModelClass() {
		return Asset.class;
	}

	protected String getModelClassName() {
		return Asset.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = assetPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = com.liferay.samplealloymvc.service.AssetLocalService.class)
	protected AssetLocalService assetLocalService;
	@BeanReference(type = AssetPersistence.class)
	protected AssetPersistence assetPersistence;
	@BeanReference(type = com.liferay.samplealloymvc.service.CheckoutLocalService.class)
	protected com.liferay.samplealloymvc.service.CheckoutLocalService checkoutLocalService;
	@BeanReference(type = CheckoutPersistence.class)
	protected CheckoutPersistence checkoutPersistence;
	@BeanReference(type = com.liferay.samplealloymvc.service.DefinitionLocalService.class)
	protected com.liferay.samplealloymvc.service.DefinitionLocalService definitionLocalService;
	@BeanReference(type = DefinitionPersistence.class)
	protected DefinitionPersistence definitionPersistence;
	@BeanReference(type = com.liferay.samplealloymvc.service.TypeLocalService.class)
	protected com.liferay.samplealloymvc.service.TypeLocalService typeLocalService;
	@BeanReference(type = TypePersistence.class)
	protected TypePersistence typePersistence;
	@BeanReference(type = com.liferay.counter.kernel.service.CounterLocalService.class)
	protected com.liferay.counter.kernel.service.CounterLocalService counterLocalService;
	@BeanReference(type = com.liferay.portal.kernel.service.ClassNameLocalService.class)
	protected com.liferay.portal.kernel.service.ClassNameLocalService classNameLocalService;
	@BeanReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;
	@BeanReference(type = com.liferay.portal.kernel.service.ResourceLocalService.class)
	protected com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService;
	@BeanReference(type = com.liferay.portal.kernel.service.UserLocalService.class)
	protected com.liferay.portal.kernel.service.UserLocalService userLocalService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private ClassLoader _classLoader;
	private AssetLocalServiceClpInvoker _clpInvoker = new AssetLocalServiceClpInvoker();
}