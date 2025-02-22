/**
 * Copyright (c) 2010-2018 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
package org.eclipse.dirigible.database.sql.test.derby;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.dirigible.database.sql.SqlFactory;
import org.eclipse.dirigible.database.sql.dialects.derby.DerbySqlDialect;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateViewTest.
 */
public class CreateViewTest extends CreateTableTest {

	/**
	 * Creates the view as select.
	 */
	@Test
	public void createViewAsSelect() {
		createTableGeneric();
		String sql = SqlFactory.getNative(new DerbySqlDialect()).create().view("CUSTOMERS_VIEW").column("ID").column("FIRST_NAME").column("LAST_NAME")
				.asSelect(SqlFactory.getDefault().select().column("*").from("CUSTOMERS").build()).build();

		assertNotNull(sql);
		assertEquals("CREATE VIEW CUSTOMERS_VIEW ( ID , FIRST_NAME , LAST_NAME ) AS SELECT * FROM CUSTOMERS", sql);
	}

	/**
	 * Creates the view as values.
	 */
	@Test
	public void createViewAsValues() {
		String sql = SqlFactory.getNative(new DerbySqlDialect()).create().view("STATES").column("STATE")
				.asValues("'STARTED', 'STOPPED', 'FAILED', 'IN PROCESS'").build();

		assertNotNull(sql);
		assertEquals("CREATE VIEW STATES ( STATE ) AS VALUES 'STARTED', 'STOPPED', 'FAILED', 'IN PROCESS'", sql);
	}

}
