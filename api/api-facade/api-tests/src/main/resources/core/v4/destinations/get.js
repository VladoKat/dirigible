/*
 * Copyright (c) 2010-2019 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
var destinations = require('core/v4/destinations');

var destination = {};
destination.name1 = 'value1';
destinations.set('destination1', destination);
var result = destinations.get('destination1');

result.name1 === 'value1';
