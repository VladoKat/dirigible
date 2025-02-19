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
package org.eclipse.dirigible.core.migrations.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;

import org.eclipse.dirigible.core.migrations.api.MigrationsException;
import org.apache.commons.io.IOUtils;
import org.eclipse.dirigible.core.migrations.api.IMigrationsCoreService;
import org.eclipse.dirigible.core.migrations.definition.MigrationDefinition;
import org.eclipse.dirigible.core.migrations.service.MigrationsCoreService;
import org.eclipse.dirigible.core.migrations.synchronizer.MigrationsSynchronizer;
import org.eclipse.dirigible.core.test.AbstractGuiceTest;
import org.eclipse.dirigible.repository.api.IRepository;
import org.eclipse.dirigible.repository.api.IRepositoryStructure;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class MigrationsSynchronizerTest.
 */
public class MigrationsSynchronizerTest extends AbstractGuiceTest {

	/** The migrations core service. */
	@Inject
	private IMigrationsCoreService migrationsCoreService;

	/** The migrations publisher. */
	@Inject
	private MigrationsSynchronizer migrationsPublisher;

	/** The repository. */
	@Inject
	private IRepository repository;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		this.migrationsCoreService = getInjector().getInstance(MigrationsCoreService.class);
		this.migrationsPublisher = getInjector().getInstance(MigrationsSynchronizer.class);
		this.repository = getInjector().getInstance(IRepository.class);
	}

	/**
	 * Creates the migration test.
	 *
	 * @throws MigrationsException the migrations exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws MigrationsException the access exception
	 */
	@Test
	public void createMigrationTest() throws MigrationsException, IOException, MigrationsException {
//		migrationsPublisher.registerPredeliveredMigrations("/migrations/test.migrate");

		InputStream in = MigrationsArtifactTest.class.getResourceAsStream("/migrations/test.migrate");
		InputStream js = MigrationsArtifactTest.class.getResourceAsStream("/migrations/migration.js");
		try {
			String json = IOUtils.toString(in, StandardCharsets.UTF_8);
			String handler = IOUtils.toString(js, StandardCharsets.UTF_8);
			MigrationDefinition migration = migrationsCoreService.parseMigration(json);
			migration.setLocation("/migrations/test.migrate");
			repository.createResource(IRepositoryStructure.PATH_REGISTRY_PUBLIC + "/migrations/test.migrate", 
					migrationsCoreService.serializeMigration(migration).getBytes());
			repository.createResource(IRepositoryStructure.PATH_REGISTRY_PUBLIC + "/migrations/migration.js", handler.getBytes());

			migrationsPublisher.synchronize();

			MigrationDefinition migrationBack = migrationsCoreService.getMigration("/migrations/test.migrate");
			assertNotNull(migrationBack);
		} finally {
			if (in != null) {
				in.close();
			}
		}

		
	}

	/**
	 * Cleanup migration test.
	 *
	 * @throws MigrationsException the migrations exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws MigrationsException the access exception
	 */
	@Test
	public void cleanupMigrationTest() throws IOException, MigrationsException {
		createMigrationTest();

		repository.removeResource(IRepositoryStructure.PATH_REGISTRY_PUBLIC + "/migrations/test.migrate");
		repository.removeResource(IRepositoryStructure.PATH_REGISTRY_PUBLIC + "/migrations/migration.js");

		migrationsPublisher.synchronize();

		MigrationDefinition migrationBack = migrationsCoreService.getMigration(IRepositoryStructure.PATH_REGISTRY_PUBLIC + "/migrations/test.migrate");
		assertNull(migrationBack);
	}

}
