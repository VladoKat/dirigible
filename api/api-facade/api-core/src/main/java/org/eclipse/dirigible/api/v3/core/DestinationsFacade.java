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
package org.eclipse.dirigible.api.v3.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.eclipse.dirigible.commons.api.helpers.GsonHelper;
import org.eclipse.dirigible.commons.config.Configuration;
import org.eclipse.dirigible.repository.api.IRepository;
import org.eclipse.dirigible.repository.api.IResource;
import org.eclipse.dirigible.repository.api.RepositoryReadException;
import org.eclipse.dirigible.repository.local.LocalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DestinationsFacade {
	
	/** The Constant DIRIGIBLE_DESTINATIONS_PROVIDER. */
	public static final String DIRIGIBLE_DESTINATIONS_PROVIDER = "DIRIGIBLE_DESTINATIONS_PROVIDER"; //$NON-NLS-1$
	
	/** The Constant DIRIGIBLE_DESTINATIONS_PROVIDER_LOCAL. */
	public static final String DIRIGIBLE_DESTINATIONS_PROVIDER_LOCAL = "local"; //$NON-NLS-1$
	
	/** The Constant DIRIGIBLE_DESTINATIONS_PROVIDER_MANAGED. */
	public static final String DIRIGIBLE_DESTINATIONS_PROVIDER_MANAGED = "managed"; //$NON-NLS-1$
	
	/** The Constant DIRIGIBLE_CONNECTIVITY_CONFIGURATION_JNDI_NAME. */
	public static final String DIRIGIBLE_CONNECTIVITY_CONFIGURATION_JNDI_NAME = "DIRIGIBLE_CONNECTIVITY_CONFIGURATION_JNDI_NAME"; //$NON-NLS-1$
	
	/** The Constant DIRIGIBLE_DESTINATIONS_INTERNAL_ROOT_FOLDER. */
	public static final String DIRIGIBLE_DESTINATIONS_INTERNAL_ROOT_FOLDER = "DIRIGIBLE_DESTINATIONS_INTERNAL_ROOT_FOLDER"; //$NON-NLS-1$

	/** The Constant DIRIGIBLE_DESTINATIONS_INTERNAL_ROOT_FOLDER_IS_ABSOLUTE. */
	public static final String DIRIGIBLE_DESTINATIONS_INTERNAL_ROOT_FOLDER_IS_ABSOLUTE = "DIRIGIBLE_DESTINATIONS_INTERNAL_ROOT_FOLDER_IS_ABSOLUTE"; //$NON-NLS-1$
	
	/** The Constant DESTINATIONS. */
	public static final String DESTINATIONS = "destinations"; //$NON-NLS-1$
	
	private static final Logger logger = LoggerFactory.getLogger(DestinationsFacade.class);
	
	/** Internal destinations repository */
	private static IRepository destinationsRepository;
	
	
	public static String get(String name) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NamingException, RepositoryReadException, IOException {
		String destinationProvider = Configuration.get(DIRIGIBLE_DESTINATIONS_PROVIDER);
		if (destinationProvider == null) {
			destinationProvider = DIRIGIBLE_DESTINATIONS_PROVIDER_LOCAL;
		}
		logger.debug(String.format("Destinations Provider for GET operation: %s", destinationProvider));
		if (DIRIGIBLE_DESTINATIONS_PROVIDER_LOCAL.equals(destinationProvider)) {
			String fullName = name + ".properties";
			IRepository repository = getDestinationsRepostiory();
			IResource resource = repository.getResource(fullName);
			if (resource.exists()) {
				Properties destinationPropeties = new Properties();
				destinationPropeties.load(new ByteArrayInputStream(resource.getContent()));
				return GsonHelper.GSON.toJson(destinationPropeties);
			} else {
				throw new IllegalArgumentException(String.format("Destination: %s does not exist", fullName));
			}
		} else if (DIRIGIBLE_DESTINATIONS_PROVIDER_MANAGED.equals(destinationProvider)) {
			Map destinationPropeties = initializeFromDestination(name);
			return GsonHelper.GSON.toJson(destinationPropeties);
		} else {
			throw new IllegalArgumentException(String.format("Unknown Destinations Provider: %s", destinationProvider));
		}
	}
	
	public static void set(String name, String content) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NamingException, RepositoryReadException, IOException {
		String destinationProvider = Configuration.get(DIRIGIBLE_DESTINATIONS_PROVIDER);
		if (destinationProvider == null) {
			destinationProvider = DIRIGIBLE_DESTINATIONS_PROVIDER_LOCAL;
		}
		logger.debug(String.format("Destinations Provider for SET operation: %s", destinationProvider));
		if (DIRIGIBLE_DESTINATIONS_PROVIDER_LOCAL.equals(destinationProvider)) {
			String fullName = name + ".properties";
			IRepository repository = getDestinationsRepostiory();
			IResource resource = repository.getResource(fullName);
			Properties destinationPropeties = GsonHelper.GSON.fromJson(content, Properties.class);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			destinationPropeties.store(out, null);
			if (resource.exists()) {
				resource.setContent(out.toByteArray());
			} else {
				repository.createResource(fullName, out.toByteArray());
			}
		} else if (DIRIGIBLE_DESTINATIONS_PROVIDER_MANAGED.equals(destinationProvider)) {
			throw new IllegalAccessException(String.format("While the Destinations Provider is of type 'managed' you cannot set destination properties via the API, but via the external destination service only"));
		} else {
			throw new IllegalArgumentException(String.format("Unknown Destinations Provider: %s", destinationProvider));
		}
	}
	
	private static synchronized IRepository getDestinationsRepostiory() {
		if (destinationsRepository == null) {
			String rootFolder = Configuration.get(DIRIGIBLE_DESTINATIONS_INTERNAL_ROOT_FOLDER);
			if (rootFolder == null) {
				rootFolder = "target/dirigible";
			}
			boolean absolute = Boolean.parseBoolean(Configuration.get(DIRIGIBLE_DESTINATIONS_INTERNAL_ROOT_FOLDER_IS_ABSOLUTE));
			String repositoryFolder = rootFolder + File.separator + DESTINATIONS;
			destinationsRepository = new LocalRepository(repositoryFolder, absolute);
		}
		return destinationsRepository;
	}
	

	public static Map initializeFromDestination(String destinationName) throws NamingException, NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		logger.debug(String.format("Lookup Destination: %s", destinationName));
		Object connectivityService = lookupConnectivityConfiguration();
		Method configurationMethod = connectivityService.getClass().getMethod("getConfiguration", String.class);
		Object destinationConfiguration = configurationMethod.invoke(connectivityService, destinationName);
		Method propertiesMethod = destinationConfiguration.getClass().getMethod("getAllProperties");
		Map destinationPropeties = (Map) propertiesMethod.invoke(destinationConfiguration);
		logger.debug(String.format("Destination Properties: %s", destinationPropeties.toString()));
		return destinationPropeties;
	}

	/**
	 * Retrieve the Connectivity Configuration from the target platform
	 *
	 * @return the managed connectivity service proxy
	 * @throws NamingException
	 */
	public static Object lookupConnectivityConfiguration() throws NamingException {
		final InitialContext ctx = new InitialContext();
		String key = Configuration.get(DIRIGIBLE_CONNECTIVITY_CONFIGURATION_JNDI_NAME);
		if (key != null) {
			return ctx.lookup(key);
		}
		return null;
	}

}
