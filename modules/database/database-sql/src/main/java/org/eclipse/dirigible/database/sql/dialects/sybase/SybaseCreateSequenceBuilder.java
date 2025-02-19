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
package org.eclipse.dirigible.database.sql.dialects.sybase;

import org.eclipse.dirigible.database.sql.ISqlDialect;
import org.eclipse.dirigible.database.sql.builders.sequence.CreateSequenceBuilder;

/**
 * The Sybase Create Sequence Builder.
 */
public class SybaseCreateSequenceBuilder extends CreateSequenceBuilder {

	/**
	 * Instantiates a new Sybase create sequence builder.
	 *
	 * @param dialect
	 *            the dialect
	 * @param sequence
	 *            the sequence
	 */
	public SybaseCreateSequenceBuilder(ISqlDialect dialect, String sequence) {
		super(dialect, sequence);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.dirigible.database.sql.builders.sequence.CreateSequenceBuilder#generateStart(java.lang.StringBuilder)
	 */
	@Override
	protected void generateStart(StringBuilder sql) {
		throw new IllegalStateException("Sybase ASE does not support Sequences");
	}

}
