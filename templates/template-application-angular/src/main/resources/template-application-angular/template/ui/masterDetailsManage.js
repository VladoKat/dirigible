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

exports.getSources = function(parameters) {
	var sources = [];
	sources = sources.concat(getMaster(parameters));
	sources = sources.concat(getDetails(parameters));
	return sources;
};

function getMaster(parameters) {
	return [{
		location: "/template-application-angular/ui/perspectives/views/master-manage/index.html.template", 
		action: "generate",
		rename: "ui/{{perspectiveName}}/views/master/{{fileName}}/index.html",
		engine: "velocity",
		collection: "uiManageMasterModels"
	}, {
		location: "/template-application-angular/ui/perspectives/views/master-manage/extensions/view.js.template", 
		action: "generate",
		rename: "ui/{{perspectiveName}}/views/master/{{fileName}}/extensions/view.js",
		collection: "uiManageMasterModels"
	}, {
		location: "/template-application-angular/ui/perspectives/views/master-manage/master/index.html.template", 
		action: "generate",
		rename: "ui/{{perspectiveName}}/views/master/{{fileName}}/master/index.html",
		engine: "velocity",
		collection: "uiManageMasterModels"
	}, {
		location: "/template-application-angular/ui/perspectives/views/master-manage/master/controller.js.template", 
		action: "generate",
		rename: "ui/{{perspectiveName}}/views/master/{{fileName}}/master/controller.js",
		engine: "velocity",
		collection: "uiManageMasterModels"
	}, {
		location: "/template-application-angular/ui/perspectives/views/master-manage/master/extensions/view.js.template", 
		action: "generate",
		rename: "ui/{{perspectiveName}}/views/master/{{fileName}}/master/extensions/view.js",
		collection: "uiManageMasterModels"
	}, {
		location: "/template-application-angular/ui/perspectives/views/master-manage/extensions/entity-view.extensionpoint.template", 
		action: "generate",
		rename: "ui/{{perspectiveName}}/views/master/{{fileName}}/master/extensions/view.extensionpoint",
		collection: "uiManageMasterModels"
	}, {
		location: "/template-application-angular/ui/perspectives/views/master-manage/extensions/view.extension.template", 
		action: "generate",
		rename: "ui/{{perspectiveName}}/views/master/{{fileName}}/extensions/view.extension",
		collection: "uiManageMasterModels"
	}, {
		location: "/template-application-angular/ui/perspectives/views/master-manage/master/extensions/entity-view-master.extension.template", 
		action: "generate",
		rename: "ui/{{perspectiveName}}/views/master/{{fileName}}/master/extensions/view-master.extension",
		collection: "uiManageMasterModels"
	}];
}

function getDetails(parameters) {
	return [{
		location: "/template-application-angular/ui/perspectives/views/master-manage/details/index.html.template", 
		action: "generate",
		rename: "ui/{{perspectiveName}}/views/master/details/{{fileName}}/index.html",
		engine: "velocity",
		collection: "uiManageDetailsModels"
	}, {
		location: "/template-application-angular/ui/perspectives/views/master-manage/details/controller.js.template", 
		action: "generate",
		rename: "ui/{{perspectiveName}}/views/master/details/{{fileName}}/controller.js",
		engine: "velocity",
		collection: "uiManageDetailsModels"
	}, {
		location: "/template-application-angular/ui/perspectives/views/master-manage/details/extensions/view.js.template", 
		action: "generate",
		rename: "ui/{{perspectiveName}}/views/master/details/{{fileName}}/extensions/view.js",
		collection: "uiManageDetailsModels"
	}, {
		location: "/template-application-angular/ui/perspectives/views/master-manage/details/extensions/entity-view-detail.extension.template", 
		action: "generate",
		rename: "ui/{{perspectiveName}}/views/master/details/{{fileName}}/extensions/view-detail.extension",
		engine: "velocity",
		collection: "uiManageDetailsModels"
	}];
}
