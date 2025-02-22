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
package org.eclipse.dirigible.core.workspace.api;

import org.eclipse.dirigible.repository.api.IResource;

/**
 * The Workspace's File interface.
 */
public interface IFile extends IResource {

	/**
	 * Gets the internal.
	 *
	 * @return the internal
	 */
	public IResource getInternal();

}
