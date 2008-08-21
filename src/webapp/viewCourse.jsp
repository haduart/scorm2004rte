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
		<% init.setTranslations("viewcourse.title,viewcourse.noscript,viewcourse.accessibility.bar.title,viewcourse.accessibility.bar.link1,viewcourse.accessibility.bar.link2,viewcourse.accessibility.bar.link3,viewcourse.accessibility.bar.link4,viewcourse.header.logo,viewcourse.accessibility.navigationmenu,viewcourse.navigationmenu.link1,viewcourse.navigationmenu.link2,viewcourse.navigationmenu.link3,viewcourse.accessibility.toolmenu,viewcourse.toolmenu.title,viewcourse.toolmenu.link1,viewcourse.toolmenu.link2,viewcourse.toolmenu.link3,viewcourse.toolmenu.link4,viewcourse.toolmenu.link5,viewcourse.accessibility.content,viewcourse.content.title,viewcourse.content.help,viewcourse.content.table.title,viewcourse.content.table.description,viewcourse.content.table.requirement,viewcourse.content.table.author,viewcourse.content.table.learningtime,viewcourse.content.table.organizations,viewcourse.content.table.summary,viewcourse.content.completed,viewcourse.accessibility.button,viewcourse.content.button,viewcourse.footer.message,error.userNotHaveAccessPermission,error.userNotAuthenticated"); %>
		<c:set var="translator" value="<%= init.getTranslations() %>" />
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
			<link rel="stylesheet" type="text/css" media="all" href="<c:out value="${init.stylesPath}" />scormrte_styles.css" />
			<link rel="icon" href="favicon.ico" type="image/x-icon" />
			<title><c:out value='${translator["viewcourse.title"]}' /></title>
			<script type="text/javascript" language="javascript" src="<c:out value="${init.scriptsPath}" />scormrte_utils.js"></script>
		</head>
		<body>
			<c:choose>
				<c:when test='<%= init.hasAccessPermission() %>'>
					<%-- Missatge a mostrar si el navegador del client té el javascript desactivat --%>
					<noscript>
						<p><c:out value='${translator["viewcourse.noscript"]}' /></p>	
					</noscript>	
					<div id="container">
						<div id="accessibility">
							<map title="<c:out value='${translator["viewcourse.accessibility.bar.title"]}' />" class="hide">
								[<a href="#toContent" title="<c:out value='${translator["viewcourse.accessibility.bar.link1"]}' />" accesskey="c"><c:out value='${translator["viewcourse.accessibility.bar.link1"]}' /></a>]
								[<a href="#toNavigationMenu" title="<c:out value='${translator["viewcourse.accessibility.bar.link2"]}' />" accesskey="n"><c:out value='${translator["viewcourse.accessibility.bar.link2"]}' /></a>]
								[<a href="#toToolMenu" title="<c:out value='${translator["viewcourse.accessibility.bar.link3"]}' />" accesskey="t"><c:out value='${translator["viewcourse.accessibility.bar.link3"]}' /></a>]
								[<a href="#toButtons" title="<c:out value='${translator["viewcourse.accessibility.bar.link4"]}' />" accesskey="b"><c:out value='${translator["viewcourse.accessibility.bar.link4"]}' /></a>]
							</map>
						</div>
						<div id="header">
							<div id="hLogo">
								<img src="<c:out value="${init.imagesPath}" />logo.gif" title='${translator["viewcourse.header.logo"]}' alt='${translator["viewcourse.header.logo"]}' />				
							</div>
							<div id="hPageNav"></div>
						</div>
						<div id="siteNav">
							<a id="toNavigationMenu" name="toNavigationMenu" class="hide"></a>
							<h1 class="hide"><c:out value='${translator["viewcourse.accessibility.navigationmenu"]}' /></h1>
							<ul id="siteLinkList">
								<li>
									<a href="welcome.jsp" title="<c:out value='${translator["viewcourse.navigationmenu.link1"]}' />" accesskey="1"><c:out value='${translator["viewcourse.navigationmenu.link1"]}' /></a>
								</li>
								<li class="selected">
									<a href="#"><c:out value='${translator["viewcourse.navigationmenu.link2"]}' /> <%= utilities.getCourseTitle(request.getParameter("course")) %></a>
								</li>
							</ul>
						</div>
						<div id="pageContent">
							<div id="pToolsMenu">
								<a id="toToolMenu" name="toToolMenu" class="hide"></a>
								<h1 class="hide"><c:out value='${translator["viewcourse.accessibility.toolmenu"]}' /></h1>
								<div id="menuLogo"><c:out value='${translator["viewcourse.toolmenu.title"]}' /></div>
								<div id="menuContent">
									<ul>
									<c:if test='<%= init.hasAdministrationPermission() %>'>
										<li>
											<a href="tracking.jsp?course=<c:out value='${param.course}' />&level=1" title="<c:out value='${translator["viewcourse.toolmenu.link1"]}' />"><c:out value='${translator["viewcourse.toolmenu.link1"]}' /></a>
										</li>
									</c:if>
										<li>
											<a href="welcome.jsp" title="<c:out value='${translator["viewcourse.toolmenu.link4"]}' />" accesskey="w"><c:out value='${translator["viewcourse.toolmenu.link4"]}' /></a>							
										</li>
										<li>
											<a href="#" title="<c:out value='${translator["viewcourse.toolmenu.link5"]}' />" accesskey="a" target="_blank" onclick="openWindow('help.jsp?page=viewcourse', 'Help', '<c:out value="${init.windowProperties} }" />', '<c:out value="${init.windowWidth} }" />', '<c:out value="${init.windowHeight} }" />'); return false"><c:out value='${translator["viewcourse.toolmenu.link5"]}' /></a>							
										</li>
									</ul>
								</div>
							</div>
							<div id="pContent">
								<a id="toContent" name="toContent" class="hide"></a>
								<h1 class="hide"><c:out value='${translator["viewcourse.accessibility.content"]}' /></h1>
								<div id="contentTitle">
									<div class="title">
										<h2><%= utilities.getCourseTitle(request.getParameter("course")) %></h2>
									</div>
									<div class="help">
										<a href="#" title="<c:out value='${translator["viewcourse.content.help"]}' />" accesskey="h" target="_blank" onclick="openWindow('help.jsp?page=viewcourse', 'Help', '<c:out value="${init.windowProperties} }" />', '<c:out value="${init.windowWidth} }" />', '<c:out value="${init.windowHeight} }" />'); return false">
											<img src="<c:out value="${init.imagesPath}" />help.gif" alt="<c:out value='${translator["viewcourse.content.help"]}' />" />
										</a>
									</div>
								</div>
								<div id="contentBody">
									<div id="contentIframe">			
										<form class="buttonsForm" method="post" action="viewContents.jsp" enctype="application/x-www-form-urlencoded">	
											<input name="course" value="${param.course}" type="hidden" />
											<table class="forms" summary='${translator["viewcourse.content.table.summary"]}'>
												<tbody>
													<tr>
														<th><c:out value='${translator["viewcourse.content.table.title"]}' /></th>
														<td><%= utilities.getCourseTitle(request.getParameter("course")) %></td>	
													</tr>
													<tr>
														<th><c:out value='${translator["viewcourse.content.table.description"]}' /></th>
														<td><%= utilities.getCourseDescription(request.getParameter("course")) %></td>	
													</tr>
													<tr>
														<th><c:out value='${translator["viewcourse.content.table.requirement"]}' /></th>
														<td><%= utilities.getCourseRequirement(request.getParameter("course")) %></td>
													</tr>
													<tr>
														<th><c:out value='${translator["viewcourse.content.table.author"]}' /></th>
														<td><%= utilities.getCourseAuthor(request.getParameter("course")) %></td>
													</tr>
													<tr>
														<th><c:out value='${translator["viewcourse.content.table.organizations"]}' /></th>
														<td><c:set var="list" value="<%= utilities.getOrganizationsId(request.getParameter("course")) %>" />
															<% int i = 0; %>
															<c:forEach begin="1" end="<%= utilities.getOrganizationsId(request.getParameter("course")).size() %>">
																<% String orgId = utilities.getOrganizationsId(request.getParameter("course")).get(i).toString(); %>
																<input name="org" value="<%= orgId %>" type="radio" <% if (i == 0) { out.print("checked='checked'"); } %> /><%= utilities.getOrganizationTitle(orgId) %> - <c:out value='${translator["viewcourse.content.completed"]}' /> <%= utilities.getCompletedPercentUserOrganization(orgId) %>%
																<% i++; %>
															</c:forEach>
														</td>
													</tr>
												</tbody>
											</table>									
									</div>
								</div>
							</div>
							<div id="pButtons">
								<a id="toButtons" name="toButtons" class="hide"></a>
								<h1 class="hide"><c:out value='${translator["viewcourse.accessibility.button"]}' /></h1>
								<div id="buttonsContent">
											<input type="submit" title="<c:out value='${translator["viewcourse.content.button"]}' />" value="<c:out value='${translator["viewcourse.content.button"]}' />" class="button" accesskey="r" />
										</form>
								</div>
							</div>
						</div>				
						<div id="footer">
							<div id="fInfo"><c:out value='${translator["viewcourse.footer.message"]}' /></div>
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