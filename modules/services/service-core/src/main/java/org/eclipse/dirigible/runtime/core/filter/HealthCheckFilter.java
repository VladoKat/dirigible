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
package org.eclipse.dirigible.runtime.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.dirigible.commons.config.HealthStatus;

/**
 * The HTTP Context Filter.
 */
@WebFilter(urlPatterns = {"/services/v3/*", "/public/v3/*", "/services/v4/*", "/public/v4/*"}, filterName = "HealthCheckFilter", description = "Check the health status of the Dirigible instance")
public class HealthCheckFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Not used
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (!httpRequest.getPathInfo().startsWith("/web/resources")) {
			HealthStatus healthStatus = HealthStatus.getInstance();
			if (healthStatus.getStatus().equals(HealthStatus.Status.Ready)) {
				chain.doFilter(request, response);
				return;
			}
			httpResponse.sendRedirect("../../index-busy.html");
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// Not used
	}

}
