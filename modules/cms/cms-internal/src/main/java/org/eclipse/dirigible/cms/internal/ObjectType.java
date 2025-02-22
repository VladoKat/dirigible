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
package org.eclipse.dirigible.cms.internal;

public class ObjectType {

	private String type;

	public ObjectType(String type) {
		this.type = type;
	}

	public String getId() {
		return this.type;
	}

	public static final ObjectType FOLDER = new ObjectType(CmisConstants.OBJECT_TYPE_FOLDER);

	public static final ObjectType DOCUMENT = new ObjectType(CmisConstants.OBJECT_TYPE_DOCUMENT);

}
