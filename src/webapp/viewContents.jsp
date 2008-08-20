<%--
/***********************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2007 Enginyeria La Salle. Universitat Ramon Llull.
 * This file is part of SCORM2004RTE.
 *
 * SCORM2004RTE is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details, currently published
 * at http://www.gnu.org/copyleft/gpl.html or in the gpl.txt in
 * the license folder of this distribution.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA 02110-1301, USA.
 *
 * Author: Marc Segarra, Enginyeria La Salle, Universitat Ramon Llull
 * You may contact the author at msegarra@salle.url.edu
 * And the copyright holder at: semipresencial@salle.url.edu
 ***********************************************************************/
 --%>
<? xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<jsp:useBean id="init" class="edu.url.lasalle.campus.scorm2004rte.system.Initialization" scope="page" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
edu.url.lasalle.campus.scorm2004rte.server.DataBase.GestorBD nouGestor = edu.url.lasalle.campus.scorm2004rte.server.DataBase.GestorBD.getInstance();
%>
<%
edu.url.lasalle.campus.scorm2004rte.system.DataAccessRepository.getInstance().registerDataAccess(nouGestor.getDataAccessID(), nouGestor);
%>
<c:choose>
	<c:when test='<%= init.isAuthenticated() %>'>
		<jsp:useBean id="utilities" class="edu.url.lasalle.campus.scorm2004rte.system.JSPUtils" scope="page" />
		<% init.setTranslations("viewcontent.title,viewcontent.noscript,viewcontent.accessibility.bar.title,viewcontent.accessibility.bar.link1,viewcontent.accessibility.bar.link2,viewcontent.accessibility.bar.link3,viewcontent.accessibility.bar.link4,viewcontent.header.logo,viewcontent.accessibility.navigationmenu,viewcontent.navigationmenu.link1,viewcontent.navigationmenu.link2,viewcontent.navigationmenu.link3,viewcontent.accessibility.toolmenu,viewcontent.toolmenu.title,viewcontent.api.error.0.title,viewcontent.api.error.0.desc,viewcontent.api.error.101.title,viewcontent.api.error.101.desc,viewcontent.api.error.102.title,viewcontent.api.error.102.desc,viewcontent.api.error.103.title,viewcontent.api.error.103.desc,viewcontent.api.error.104.title,viewcontent.api.error.104.desc,viewcontent.api.error.111.title,viewcontent.api.error.111.desc,viewcontent.api.error.112.title,viewcontent.api.error.112.desc,viewcontent.api.error.113.title,viewcontent.api.error.113.desc,viewcontent.api.error.122.title,viewcontent.api.error.122.desc,viewcontent.api.error.123.title,viewcontent.api.error.123.desc,viewcontent.api.error.132.title,viewcontent.api.error.132.desc,viewcontent.api.error.133.title,viewcontent.api.error.133.desc,viewcontent.api.error.142.title,viewcontent.api.error.142.desc,viewcontent.api.error.143.title,viewcontent.api.error.143.desc,viewcontent.api.error.201.title,viewcontent.api.error.201.desc,viewcontent.api.error.301.title,viewcontent.api.error.301.desc,viewcontent.api.error.351.title,viewcontent.api.error.351.desc,viewcontent.api.error.391.title,viewcontent.api.error.391.desc,viewcontent.api.error.401.title,viewcontent.api.error.401.desc,viewcontent.api.error.402.title,viewcontent.api.error.402.desc,viewcontent.api.error.403.title,viewcontent.api.error.403.desc,viewcontent.api.error.404.title,viewcontent.api.error.404.desc,viewcontent.api.error.405.title,viewcontent.api.error.405.desc,viewcontent.api.error.406.title,viewcontent.api.error.406.desc,viewcontent.api.error.407.title,viewcontent.api.error.407.desc,viewcontent.api.error.1000.title,viewcontent.api.error.1000.desc,viewcontent.accessibility.content,viewcontent.content.help,viewcontent.accessibility.button,viewcontent.adlnav.button.continue,viewcontent.adlnav.button.previous,viewcontent.adlnav.button.exit,viewcontent.content.button.diminish,viewcontent.content.button.increase,viewcontent.content.button.print,viewcontent.footer.message,error.userNotAuthenticated,error.userNotHaveAccessPermission"); %>	
		<c:set var="translator" value="<%= init.getTranslations() %>" />
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
			<link rel="stylesheet" type="text/css" media="all" href="<c:out value="${init.stylesPath}" />scormrte_styles.css" />
			<link rel="stylesheet" type="text/css" href="<c:out value="${init.stylesPath}" />dhtmlxtree.css" />
			<link rel="icon" href="favicon.ico" type="image/x-icon" />
			<title><c:out value='${translator["viewcontent.title"]}' /></title>
			<script  src="<c:out value="${init.scriptsPath}" />dhtmlxcommon.js"></script>
			<script  src="<c:out value="${init.scriptsPath}" />dhtmlxtree.js"></script>
			<script type="text/javascript" language="javascript" src="<c:out value="${init.scriptsPath}" />scormrte_utils.js"></script>
			<script type="text/javascript" language="javascript" src="<c:out value="${init.scriptsPath}" />scormrte_api.js"></script>
		</head>
		<body>
			<c:choose>
				<c:when test='<%= init.hasAccessPermission() %>'>
					<noscript>
						<p><c:out value='${translator["viewcontent.noscript"]}' /></p>	
					</noscript>
					<div id="container">
					
					<jsp:useBean id="navTree" class="edu.url.lasalle.campus.scorm2004rte.server.jsp.NavTree" scope="page" />
					
						<div id="accessibility">
							<map title="<c:out value='${translator["viewcontent.accessibility.bar.title"]}' />" class="hide">
								[<a href="#toContent" title="<c:out value='${translator["viewcontent.accessibility.bar.link1"]}' />" accesskey="c"><c:out value='${translator["viewcontent.accessibility.bar.link1"]}' /></a>]
								[<a href="#toButtons" title="<c:out value='${translator["viewcontent.accessibility.bar.link2"]}' />" accesskey="b"><c:out value='${translator["viewcontent.accessibility.bar.link2"]}' /></a>]
								[<a href="#toNavTree" title="<c:out value='${translator["viewcontent.accessibility.bar.link3"]}' />" accesskey="t"><c:out value='${translator["viewcontent.accessibility.bar.link3"]}' /></a>]
							</map>
						</div>
						<div id="header">
							<div id="hLogo">
								<img src="<c:out value="${init.imagesPath}" />logo.gif" title='${translator["viewcontent.header.logo"]}' alt='${translator["viewcontent.header.logo"]}' />				
							</div>
							<div id="hPageNav">
								<a id="toButtons" name="toButtons" class="hide"></a>
								<h1 class="hide"><c:out value='${translator["viewcontent.accessibility.button"]}' /></h1>
								<a href="#"><img src="<c:out value="${init.imagesPath}" />previous.gif" title='${translator["viewcontent.adlnav.botton.previous"]}' alt='${translator["viewcontent.adlnav.botton.previous"]}' id="previous" onclick="changeContent('previous'); return false" /></a> 
								<a href="#"><img src="<c:out value="${init.imagesPath}" />continue.gif" title='${translator["viewcontent.adlnav.botton.continue"]}' alt='${translator["viewcontent.adlnav.botton.continue"]}' id="continue" onclick="changeContent('continue'); return false" /></a>
								<a href="#"><img src="<c:out value="${init.imagesPath}" />exit.gif" title='${translator["viewcontent.adlnav.botton.exit"]}' alt='${translator["viewcontent.adlnav.botton.exit"]}' id="exit" onclick="changeContent('exit'); return false" /></a>
							</div>
						</div>
						<div id="pageContent">
							<div id="pNavTree">
								<a id="toNavTree" name="toNavTree" class="hide"></a>
								<h1 class="hide"><c:out value='${translator["viewcontent.accessibility.toolmenu"]}' /></h1>
								<div id="menuLogo"><c:out value='${translator["tracking.toolmenu.title"]}' /></div>
								<div id="treeContent">
									<div id="treeContainer"></div>
									<script>
									<!--
										API_1484_11 = new API();
										
										API_1484_11.orgId = '<%= request.getParameter("org") %>';
										API_1484_11.courseId = '<%= request.getParameter("course") %>';
										
										// descripció breu i detallat dels codis d'error de l'API Adapter
										API_1484_11.errorCodes.error[NO_ERROR] = new Error();
										API_1484_11.errorCodes.error[NO_ERROR].title = "<c:out value='${translator["viewcontent.api.error.0.title"]}' />";
										API_1484_11.errorCodes.error[NO_ERROR].description = "<c:out value='${translator["viewcontent.api.error.0.desc"]}' />";
										API_1484_11.errorCodes.error[GENERAL_EXCEPTION] = new Error();
										API_1484_11.errorCodes.error[GENERAL_EXCEPTION].title = "<c:out value='${translator["viewcontent.api.error.101.title"]}' />";
										API_1484_11.errorCodes.error[GENERAL_EXCEPTION].description = "<c:out value='${translator["viewcontent.api.error.101.desc"]}' />";
										API_1484_11.errorCodes.error[GENERAL_INITIALIZATION_FAILURE] = new Error();
										API_1484_11.errorCodes.error[GENERAL_INITIALIZATION_FAILURE].title = "<c:out value='${translator["viewcontent.api.error.102.title"]}' />";
										API_1484_11.errorCodes.error[GENERAL_INITIALIZATION_FAILURE].description = "<c:out value='${translator["viewcontent.api.error.102.desc"]}' />";
										API_1484_11.errorCodes.error[ALREADY_INITIALIZED] = new Error();
										API_1484_11.errorCodes.error[ALREADY_INITIALIZED].title = "<c:out value='${translator["viewcontent.api.error.103.title"]}' />";
										API_1484_11.errorCodes.error[ALREADY_INITIALIZED].description = "<c:out value='${translator["viewcontent.api.error.103.desc"]}' />";
										API_1484_11.errorCodes.error[CONTENT_INSTANCE_TERMINATED] = new Error();
										API_1484_11.errorCodes.error[CONTENT_INSTANCE_TERMINATED].title = "<c:out value='${translator["viewcontent.api.error.104.title"]}' />";
										API_1484_11.errorCodes.error[CONTENT_INSTANCE_TERMINATED].description = "<c:out value='${translator["viewcontent.api.error.104.desc"]}' />";
										API_1484_11.errorCodes.error[GENERAL_TERMINATION_FAILURE] = new Error();
										API_1484_11.errorCodes.error[GENERAL_TERMINATION_FAILURE].title = "<c:out value='${translator["viewcontent.api.error.111.title"]}' />";
										API_1484_11.errorCodes.error[GENERAL_TERMINATION_FAILURE].description = "<c:out value='${translator["viewcontent.api.error.111.desc"]}' />";
										API_1484_11.errorCodes.error[TERMINATION_BEFORE_INITIALIZATION] = new Error();
										API_1484_11.errorCodes.error[TERMINATION_BEFORE_INITIALIZATION].title = "<c:out value='${translator["viewcontent.api.error.112.title"]}' />";
										API_1484_11.errorCodes.error[TERMINATION_BEFORE_INITIALIZATION].description = "<c:out value='${translator["viewcontent.api.error.112.desc"]}' />";
										API_1484_11.errorCodes.error[TERMINATION_AFTER_TERMINATION] = new Error();
										API_1484_11.errorCodes.error[TERMINATION_AFTER_TERMINATION].title = "<c:out value='${translator["viewcontent.api.error.113.title"]}' />";
										API_1484_11.errorCodes.error[TERMINATION_AFTER_TERMINATION].description = "<c:out value='${translator["viewcontent.api.error.113.desc"]}' />";
										API_1484_11.errorCodes.error[RECEIVED_DATA_BEFORE_INITIALIZATION] = new Error();
										API_1484_11.errorCodes.error[RECEIVED_DATA_BEFORE_INITIALIZATION].title = "<c:out value='${translator["viewcontent.api.error.122.title"]}' />";
										API_1484_11.errorCodes.error[RECEIVED_DATA_BEFORE_INITIALIZATION].description = "<c:out value='${translator["viewcontent.api.error.122.desc"]}' />";
										API_1484_11.errorCodes.error[RECEIVED_DATA_AFTER_TERMINATION] = new Error();
										API_1484_11.errorCodes.error[RECEIVED_DATA_AFTER_TERMINATION].title = "<c:out value='${translator["viewcontent.api.error.123.title"]}' />";
										API_1484_11.errorCodes.error[RECEIVED_DATA_AFTER_TERMINATION].description = "<c:out value='${translator["viewcontent.api.error.123.desc"]}' />";
										API_1484_11.errorCodes.error[STORE_DATA_BEFORE_INITIALIZATION] = new Error();
										API_1484_11.errorCodes.error[STORE_DATA_BEFORE_INITIALIZATION].title = "<c:out value='${translator["viewcontent.api.error.132.title"]}' />";
										API_1484_11.errorCodes.error[STORE_DATA_BEFORE_INITIALIZATION].description = "<c:out value='${translator["viewcontent.api.error.132.desc"]}' />";
										API_1484_11.errorCodes.error[STORE_DATA_AFTER_TERMINATION] = new Error();
										API_1484_11.errorCodes.error[STORE_DATA_AFTER_TERMINATION].title = "<c:out value='${translator["viewcontent.api.error.133.title"]}' />";
										API_1484_11.errorCodes.error[STORE_DATA_AFTER_TERMINATION].description = "<c:out value='${translator["viewcontent.api.error.133.desc"]}' />";
										API_1484_11.errorCodes.error[COMMIT_BEFORE_INITIALIZATION] = new Error();
										API_1484_11.errorCodes.error[COMMIT_BEFORE_INITIALIZATION].title = "<c:out value='${translator["viewcontent.api.error.142.title"]}' />";
										API_1484_11.errorCodes.error[COMMIT_BEFORE_INITIALIZATION].description = "<c:out value='${translator["viewcontent.api.error.142.desc"]}' />";
										API_1484_11.errorCodes.error[COMMIT_AFTER_TERMINATION] = new Error();
										API_1484_11.errorCodes.error[COMMIT_AFTER_TERMINATION].title = "<c:out value='${translator["viewcontent.api.error.143.title"]}' />";
										API_1484_11.errorCodes.error[COMMIT_AFTER_TERMINATION].description = "<c:out value='${translator["viewcontent.api.error.143.desc"]}' />";
										API_1484_11.errorCodes.error[GENERAL_ARGUMENT_ERROR] = new Error();
										API_1484_11.errorCodes.error[GENERAL_ARGUMENT_ERROR].title = "<c:out value='${translator["viewcontent.api.error.201.title"]}' />";
										API_1484_11.errorCodes.error[GENERAL_ARGUMENT_ERROR].description = "<c:out value='${translator["viewcontent.api.error.201.desc"]}' />";
										API_1484_11.errorCodes.error[GENERAL_GET_FAILURE] = new Error();
										API_1484_11.errorCodes.error[GENERAL_GET_FAILURE].title = "<c:out value='${translator["viewcontent.api.error.301.title"]}' />";
										API_1484_11.errorCodes.error[GENERAL_GET_FAILURE].description = "<c:out value='${translator["viewcontent.api.error.301.desc"]}' />";
										API_1484_11.errorCodes.error[GENERAL_SET_FAILURE] = new Error();
										API_1484_11.errorCodes.error[GENERAL_SET_FAILURE].title = "<c:out value='${translator["viewcontent.api.error.351.title"]}' />";
										API_1484_11.errorCodes.error[GENERAL_SET_FAILURE].description = "<c:out value='${translator["viewcontent.api.error.351.desc"]}' />";
										API_1484_11.errorCodes.error[GENERAL_COMMIT_FAILURE] = new Error();
										API_1484_11.errorCodes.error[GENERAL_COMMIT_FAILURE].title = "<c:out value='${translator["viewcontent.api.error.391.title"]}' />";
										API_1484_11.errorCodes.error[GENERAL_COMMIT_FAILURE].description = "<c:out value='${translator["viewcontent.api.error.391.desc"]}' />";
										API_1484_11.errorCodes.error[UNDEFINED_DATA_MODEL_ELEMENT] = new Error();
										API_1484_11.errorCodes.error[UNDEFINED_DATA_MODEL_ELEMENT].title = "<c:out value='${translator["viewcontent.api.error.401.title"]}' />";
										API_1484_11.errorCodes.error[UNDEFINED_DATA_MODEL_ELEMENT].description = "<c:out value='${translator["viewcontent.api.error.401.desc"]}' />";
										API_1484_11.errorCodes.error[UNIMPLEMENTED_DATA_MODEL_ELEMENT] = new Error();
										API_1484_11.errorCodes.error[UNIMPLEMENTED_DATA_MODEL_ELEMENT].title = "<c:out value='${translator["viewcontent.api.error.402.title"]}' />";
										API_1484_11.errorCodes.error[UNIMPLEMENTED_DATA_MODEL_ELEMENT].description = "<c:out value='${translator["viewcontent.api.error.402.desc"]}' />";
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED] = new Error();
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED].title = "<c:out value='${translator["viewcontent.api.error.403.title"]}' />";
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED].description = "<c:out value='${translator["viewcontent.api.error.403.desc"]}' />";
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_IS_READ_ONLY] = new Error();
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_IS_READ_ONLY].title = "<c:out value='${translator["viewcontent.api.error.404.title"]}' />";
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_IS_READ_ONLY].description = "<c:out value='${translator["viewcontent.api.error.404.desc"]}' />";
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_IS_WRITE_ONLY] = new Error();
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_IS_WRITE_ONLY].title = "<c:out value='${translator["viewcontent.api.error.405.title"]}' />";
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_IS_WRITE_ONLY].description = "<c:out value='${translator["viewcontent.api.error.405.desc"]}' />";
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_TYPE_MISMATCH] = new Error();
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_TYPE_MISMATCH].title = "<c:out value='${translator["viewcontent.api.error.406.title"]}' />";
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_TYPE_MISMATCH].description = "<c:out value='${translator["viewcontent.api.error.406.desc"]}' />";
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_VALUE_OUT_OF_RANGE] = new Error();
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_VALUE_OUT_OF_RANGE].title = "<c:out value='${translator["viewcontent.api.error.407.title"]}' />";
										API_1484_11.errorCodes.error[DATA_MODEL_ELEMENT_VALUE_OUT_OF_RANGE].description = "<c:out value='${translator["viewcontent.api.error.407.desc"]}' />";
										API_1484_11.errorCodes.error[GENERAL_LMS_FAILURE] = new Error();
										API_1484_11.errorCodes.error[GENERAL_LMS_FAILURE].title = "<c:out value='${translator["viewcontent.api.error.1000.title"]}' />";
										API_1484_11.errorCodes.error[GENERAL_LMS_FAILURE].description = "<c:out value='${translator["viewcontent.api.error.1000.desc"]}' />";
										
										// crea l'arbre dinàmic i l'inicialitza
										<% navTree.initTree("treeContainer", init.getImagesPath()); %>
										<%= navTree.getTree() %>
										
										/*
										 * launchSelectedContent(string id)
										 * Input: Un string amb l'identificador d'un ítem.
										 *
										 * Llança el recurs de l'ítem seleccionat en l'arbre de navegació.
										 */
										function launchSelectedContent(id) {	
											if (tree.getUserData(id, "itemID") != null) {
												var iframe = document.getElementById("adlIframe");
												if (API_1484_11.type == "sco") {
													API_1484_11.dataModel.request = "{target=" + tree.getUserData(id, "itemID") + "}choice";
													// unload del recurs del tipus sco de l'iframe perquè finalitzi 
													// la comunicació amb l'API 
													iframe.src = "about:blank"; 
												} else {
													iframe.src = "sequencingEngine.jsp?course=<%= request.getParameter("course") %>&org=<%= request.getParameter("org") %>&type=" + API_1484_11.type + "&session_time=" + API_1484_11.utils.calculateSessionTime() + "&request={target=" + tree.getUserData(id, "itemID") + "}choice";
												}
											}
										}
										
										/*
										 * changeContent(string option)
										 * Input: Un string amb la petició de navegació (choice|continue|previous)
										 * 		de l'usuari.
										 *
										 * Llança el recurs de l'ítem seleccionat en l'arbre de navegació.
										 */
										function changeContent(option) {
											var iframe = document.getElementById("adlIframe");
											if (API_1484_11.type == "sco") {
												API_1484_11.dataModel.request = option;
												// unload del recurs del tipus sco de l'iframe perquè finalitzi 
												// la comunicació amb l'API 
												iframe.src = "about:blank";
											} else {
												iframe.src = "sequencingEngine.jsp?course=<%= request.getParameter("course") %>&org=<%= request.getParameter("org") %>&type=" + API_1484_11.type + "&session_time=" + API_1484_11.utils.calculateSessionTime() + "&request=" + option;
											}
											
											return true;
										}
										
										sizeText = 1;	// tamany actual del text
										MAX_SIZE = 2;	// tamany màxim 
										MIN_SIZE = .50;	// tamany mínim
										
										/*
										 * increaseText()
										 *
										 * Incrementa el tamany del text.
										 */
										function increaseText() {
											if(sizeText < MAX_SIZE) {
												sizeText += .25;
												window.frames['adlIframe'].document.body.style.fontSize = sizeText + "em";
											}
										}
										
										/*
										 * reduceText()
										 *
										 * Redueix el tamany del text.
										 */
										function reduceText() {
											if(sizeText > MIN_SIZE) {
												sizeText -= .25;												
												window.frames['adlIframe'].document.body.style.fontSize = sizeText + "em";
											}
										}
										
										/*
										 * printContent()
										 *
										 * Imprimeix el contingut d'aprenentatge.
										 */
										function printContent() {
											// si el navegador del client és diferent al FireFox, 
											// cal indicar el focus
											if (!_isFF) {
												window.frames['adlIframe'].focus();
											}
											window.frames['adlIframe'].print();
										}
									//-->
									</script>
								</div>
							</div>
							<div id="pContent">
								<a id="toContent" name="toContent" class="hide"></a>
								<h1 class="hide"><c:out value='${translator["viewcontent.accessibility.content"]}' /></h1>
								<div id="adlTitle">
									<div class="title">
										<h2><%= utilities.getOrganizationTitle(request.getParameter("org")) %></h2>
									</div>
									<div class="contentButtons">
										<a href="#" title="<c:out value='${translator["viewcontent.content.button.print"]}' />" accesskey="p" onclick="printContent(); return false;">
											<img src="<c:out value="${init.imagesPath}" />print.gif" alt="<c:out value='${translator["viewcontent.content.button.print"]}' />" />
										</a> | 
										<a href="#" title="<c:out value='${translator["viewcontent.content.button.diminish"]}' />" accesskey="d" onclick="reduceText(); return false;">
											<img src="<c:out value="${init.imagesPath}" />reduceText.gif" alt="<c:out value='${translator["viewcontent.content.button.diminish"]}' />" />
										</a> |
										<a href="#" title="<c:out value='${translator["viewcontent.content.button.increase"]}' />" accesskey="i" onclick="increaseText(); return false;">
											<img src="<c:out value="${init.imagesPath}" />enlargeText.gif" alt="<c:out value='${translator["viewcontent.content.button.increase"]}' />" />
										</a> |
										<a href="#" title="<c:out value='${translator["viewcontent.content.help"]}' />" accesskey="h" target="_blank" onclick="openWindow('help.jsp?page=viewcontents', 'Help', '<c:out value="${init.helpProperties} }" />'); return false">
											<img src="<c:out value="${init.imagesPath}" />help.gif" alt="<c:out value='${translator["viewcontent.content.help"]}' />" />
										</a> 
									</div>
								</div>
								<div id="adlBody">
										
										<iframe id="adlIframe" name="adlIframe" src="sequencingEngine.jsp?course=<%= request.getParameter("course") %>&org=<%= request.getParameter("org") %>&type=unknown"></iframe>
									
								</div>
							</div>
						</div>				
						<div id="footer">
							<div id="fInfo"><c:out value='${translator["viewcontent.footer.message"]}' /></div>
						</div>
					</div>					
				</c:when>
				<c:otherwise>
					<c:out value='${translator["error.userNotHaveAccessPermission"]}' />
				</c:otherwise>
			</c:choose>	
		</body>
		</html>
	</c:when>
	<c:otherwise>
		<c:out value='${translator["error.userNotAuthenticated"]}' />
	</c:otherwise>
</c:choose>		