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
package org.eclipse.dirigible.repository.api;

import javax.ws.rs.core.Response.Status;

import org.eclipse.dirigible.commons.api.service.AbstractExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class RepositoryExceptionHandler.
 */
public class RepositoryExceptionHandler extends AbstractExceptionHandler<RepositoryException> {

	private static final Logger logger = LoggerFactory.getLogger(RepositoryExceptionHandler.class);

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.commons.api.service.AbstractExceptionHandler#getType()
	 */
	@Override
	public Class<? extends AbstractExceptionHandler<RepositoryException>> getType() {
		return RepositoryExceptionHandler.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.commons.api.service.AbstractExceptionHandler#getLogger()
	 */
	@Override
	protected Logger getLogger() {
		return logger;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.commons.api.service.AbstractExceptionHandler#getResponseStatus(java.lang.Throwable)
	 */
	@Override
	protected Status getResponseStatus(RepositoryException exception) {
		return Status.INTERNAL_SERVER_ERROR;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.commons.api.service.AbstractExceptionHandler#getResponseMessage(java.lang.Throwable)
	 */
	@Override
	protected String getResponseMessage(RepositoryException exception) {
		return exception.getMessage();
	}

}
