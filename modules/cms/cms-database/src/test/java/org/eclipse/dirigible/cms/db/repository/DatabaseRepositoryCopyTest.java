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
package org.eclipse.dirigible.cms.db.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.eclipse.dirigible.cms.db.CmsDatabaseRepository;
import org.eclipse.dirigible.repository.api.ICollection;
import org.eclipse.dirigible.repository.api.IEntity;
import org.eclipse.dirigible.repository.api.IRepository;
import org.eclipse.dirigible.repository.api.IResource;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class DatabaseRepositoryCopyTest.
 */
public class DatabaseRepositoryCopyTest {

	/** The repository src. */
	IRepository repositorySrc;

	/** The repository dst. */
	IRepository repositoryDst;

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		try {
			DataSource dataSource = DatabaseTestHelper.createDataSource("target/tests/derbySrc");
			repositorySrc = new CmsDatabaseRepository(dataSource);
			dataSource = DatabaseTestHelper.createDataSource("target/tests/derbyDst");
			repositoryDst = new CmsDatabaseRepository(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test text.
	 */
	@Test
	public void testText() {
		String PATH = "/testCollectionPath/toBeRemovedTextPath1.txt";
		IResource resource = null;
		try {
			String content = "test1";

			resource = repositorySrc.createResource(PATH, content.getBytes(), false, "text/plain"); //$NON-NLS-1$
			assertNotNull(resource);
			assertTrue(resource.exists());
			assertFalse(resource.isBinary());

			IResource resourceBack = repositorySrc.getResource(PATH);
			String path = resourceBack.getPath();

			assertEquals(PATH, path);

			copyRepository(repositorySrc, repositoryDst);

			resourceBack = repositoryDst.getResource(PATH);
			path = resourceBack.getPath();

			assertTrue(resourceBack.exists());
			assertEquals(PATH, path);

		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			try {
				if ((resource != null) && resource.exists()) {
					repositorySrc.removeResource(PATH);
					resource = repositorySrc.getResource(PATH);
					assertNotNull(resource);
					assertFalse(resource.exists());

					repositoryDst.removeResource(PATH);
					resource = repositoryDst.getResource(PATH);
					assertNotNull(resource);
					assertFalse(resource.exists());
				}
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		}
	}

	/**
	 * Copy repository.
	 *
	 * @param sourceRepository
	 *            the source repository
	 * @param targetRepository
	 *            the target repository
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void copyRepository(IRepository sourceRepository, IRepository targetRepository) throws IOException {
		ICollection root = sourceRepository.getRoot();
		copyCollection(root, targetRepository);
	}

	/**
	 * Copy collection.
	 *
	 * @param parent
	 *            the parent
	 * @param targetRepository
	 *            the target repository
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void copyCollection(ICollection parent, IRepository targetRepository) throws IOException {
		List<IEntity> entities = parent.getChildren();
		for (IEntity entity : entities) {
			if (entity instanceof ICollection) {
				ICollection collection = (ICollection) entity;
				copyCollection(collection, targetRepository);
			} else {
				IResource resource = (IResource) entity;
				try {
					targetRepository.createResource(resource.getPath(), resource.getContent(), resource.isBinary(), resource.getContentType(), true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
