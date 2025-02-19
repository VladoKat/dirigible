/**
 * Copyright (c) 2010-2019 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
package org.eclipse.dirigible.database.api;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

import javax.sql.DataSource;

import org.eclipse.dirigible.commons.api.module.AbstractDirigibleModule;
import org.eclipse.dirigible.commons.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Module for managing Database instantiation and binding.
 */
public class DatabaseModule extends AbstractDirigibleModule {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseModule.class);

	private static final ServiceLoader<IDatabase> DATABASES = ServiceLoader.load(IDatabase.class);

	private static final String MODULE_NAME = "Database Module";

	/*
	 * (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		Configuration.load("/dirigible-database.properties");
		Configuration.load("/dirigible.properties");

		boolean databaseProviderIsSelected = false;
		String databaseProvider = Configuration.get(IDatabase.DIRIGIBLE_DATABASE_PROVIDER);
		String dataSourceName = Configuration.get(IDatabase.DIRIGIBLE_DATABASE_DATASOURCE_NAME_DEFAULT,
				IDatabase.DIRIGIBLE_DATABASE_DATASOURCE_DEFAULT);
		if (databaseProvider != null) {
			databaseProviderIsSelected = true;
		} else {
			databaseProvider = IDatabase.DIRIGIBLE_DATABASE_PROVIDER_LOCAL;
		}
		for (IDatabase next : DATABASES) {
			logger.trace(format("Installing Database Provider [{0}:{1}] ...", next.getType(), next.getName()));
			if (databaseProviderIsSelected && next.getType().equals(databaseProvider)) {
				// bind the selected if any
				bindDatasource(next, dataSourceName);
			} else if (!databaseProviderIsSelected) {
				// bind the first present, because there is no selected one
				bindDatasource(next, dataSourceName);
				break;
			}
			logger.trace(format("Done installing Database Provider [{0}:{1}].", next.getType(), next.getName()));
		}
	}

	private void bindDatasource(IDatabase next, String dataSourceName) {
		bind(IDatabase.class).toInstance(next);
		logger.trace(format("Binding Database - [{0}:{1}:{2}].", next.getType(), next.getName(), dataSourceName));
		try {
			logger.trace(format("Creating Datasource - [{0}:{1}:{2}] ...", next.getType(), next.getName(), dataSourceName));
			bind(DataSource.class).toInstance(next.getDataSource(dataSourceName));
			logger.info(format("Bound Datasource - [{0}:{1}:{2}].", next.getType(), next.getName(), dataSourceName));
			logger.trace(format("Done creating Datasource - [{0}:{1}:{2}].", next.getType(), next.getName(), dataSourceName));
		} catch (Exception e) {
			logger.error(format("Failed creating Datasource - [{0}:{1}:{2}].", next.getType(), next.getName(), dataSourceName), e);
		}
		logger.trace(format("Done binding Datasource - [{0}:{1}:{2}].", next.getType(), next.getName(), dataSourceName));
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.commons.api.module.AbstractDirigibleModule#getName()
	 */
	@Override
	public String getName() {
		return MODULE_NAME;
	}

	/**
	 * Gets the data source.
	 *
	 * @param type
	 *            the type
	 * @param datasource
	 *            the datasource
	 * @return the data source
	 */
	public static DataSource getDataSource(String type, String datasource) {
		DataSource dataSource = null;
		for (IDatabase next : DATABASES) {
			if (next.getType().equals(type)) {
				if (datasource == null) {
					dataSource = next.getDataSource();
				} else {
					dataSource = next.getDataSource(datasource);
				}
				break;
			}
		}
		return dataSource;
	}

	/**
	 * Gets the database types.
	 *
	 * @return the database types
	 */
	public static List<String> getDatabaseTypes() {
		List<String> result = new ArrayList<String>();
		for (IDatabase next : DATABASES) {
			result.add(next.getType());
		}
		return result;
	}

	/**
	 * Gets the data sources.
	 *
	 * @param type
	 *            the type
	 * @return the data sources
	 */
	public static Set<String> getDataSources(String type) {
		for (IDatabase next : DATABASES) {
			if (next.getType().equals(type)) {
				return next.getDataSources().keySet();
			}
		}
		return null;
	}

}
