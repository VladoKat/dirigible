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
package org.eclipse.dirigible.database.sql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.dirigible.database.sql.SqlFactory;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class InsertTest.
 */
public class InsertTest {
	
	/**
	 * Insert simple.
	 */
	@Test
	public void insertSimple() {
		String sql = SqlFactory.getDefault()
			.insert()
			.into("CUSTOMERS")
			.column("FIRST_NAME")
			.column("LAST_NAME")
			.build();
		
		assertNotNull(sql);
		assertEquals("INSERT INTO CUSTOMERS (FIRST_NAME, LAST_NAME) VALUES (?, ?)", sql);
	}
	
	/**
	 * Insert values.
	 */
	@Test
	public void insertValues() {
		String sql = SqlFactory.getDefault()
			.insert()
			.into("CUSTOMERS")
			.column("FIRST_NAME")
			.column("LAST_NAME")
			.value("?")
			.value("'Smith'")
			.build();

		assertNotNull(sql);
		assertEquals("INSERT INTO CUSTOMERS (FIRST_NAME, LAST_NAME) VALUES (?, 'Smith')", sql);
	}
	
	/**
	 * Insert select.
	 */
	@Test
	public void insertSelect() {
		String sql = SqlFactory.getDefault()
			.insert()
			.into("CUSTOMERS")
			.column("FIRST_NAME")
			.column("LAST_NAME")
			.select(SqlFactory.getDefault().select().column("*").from("SUPPLIERS").build())
			.build();

		assertNotNull(sql);
		assertEquals("INSERT INTO CUSTOMERS (FIRST_NAME, LAST_NAME) SELECT * FROM SUPPLIERS", sql);
	}

}
