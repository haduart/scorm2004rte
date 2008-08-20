<%
/***********************************************************************
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
 * the root folder of this distribution.
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
 %>
<? xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<jsp:useBean id="init" class="edu.url.lasalle.campus.scorm2004rte.system.Initialization" scope="page" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<c:choose>
	<c:when test='<%= init.isAuthenticated() %>'>
		<jsp:useBean id="tableCourse" class="edu.url.lasalle.campus.scorm2004rte.server.jsp.TrackingCourse" scope="page" />
 	    <jsp:useBean id="tableStudent" class="edu.url.lasalle.campus.scorm2004rte.server.jsp.TrackingStudent" scope="page" />
  	    <jsp:useBean id="tableObjective" class="edu.url.lasalle.campus.scorm2004rte.server.jsp.TrackingObjective" scope="page" />
		<jsp:useBean id="utilities" class="edu.url.lasalle.campus.scorm2004rte.system.JSPUtils" scope="page" />
		<% init.setTranslations("tracking.title,tracking.noscript,tracking.accessibility.bar.title,tracking.accessibility.bar.link1,tracking.accessibility.bar.link2,tracking.accessibility.bar.link3,tracking.accessibility.bar.link4,tracking.header.logo,tracking.accessibility.navigationmenu,tracking.navigationmenu.link1,tracking.navigationmenu.link3,tracking.navigationmenu.link4,tracking.navigationmenu.link5,tracking.accessibility.toolmenu,tracking.toolmenu.title,tracking.toolmenu.link1,tracking.toolmenu.link2,tracking.toolmenu.link3,tracking.toolmenu.link4,tracking.toolmenu.link5,tracking.accessibility.content,tracking.content.title1,tracking.content.title2,tracking.content.help,tracking.accessibility.button,tracking.content.button,tracking.footer.message,tracking.content.table1.summary,tracking.content.table1.header1,tracking.content.table1.header2,tracking.content.table1.header3,tracking.content.table1.header4,tracking.content.table1.header5,tracking.content.table1.nvisits,tracking.content.table1.completed,tracking.content.table2.summary,tracking.content.table2.header1,tracking.content.table2.header2,tracking.content.table2.header3,tracking.content.table2.header4,tracking.content.table2.header5,tracking.content.table2.header6,tracking.content.table2.header7,tracking.content.table2.header8,tracking.content.table2.header9,tracking.content.table2.unknown,tracking.content.table2.completed,tracking.content.table2.incomplete,tracking.content.table2.attempted,tracking.content.table2.passed,tracking.content.table2.failed,tracking.content.table3.summary,tracking.content.table3.header1,tracking.content.table3.header2,tracking.content.table3.header3,tracking.content.table3.header4,tracking.content.table3.header5,error.userNotHaveAdministrationPermission,error.userNotAuthenticated"); %>
		<c:set var="translator" value="<%= init.getTranslations() %>" />
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
			<link rel="stylesheet" type="text/css" media="all" href="<c:out value="${init.stylesPath}" />scormrte_styles.css" />
			<link rel="icon" href="favicon.ico" type="image/x-icon" />
			<title><c:out value='${translator["tracking.title"]}' /></title>
			<script type="text/javascript" language="javascript" src="<c:out value="${init.scriptsPath}" />scormrte_scripts.js"></script>
		</head>
		<body>
			<c:choose>
				<c:when test='<%= init.hasAdministrationPermission() %>'>
					<noscript>
						<p><c:out value='${translator["tracking.noscript"]}' /></p>	
					</noscript>	
					<div id="container">
						<div id="accessibility">
							<map title="<c:out value='${translator["tracking.accessibility.bar.title"]}' />" class="hide">
								[<a href="#toContent" title="<c:out value='${translator["tracking.accessibility.bar.link1"]}' />" accesskey="c"><c:out value='${translator["tracking.accessibility.bar.link1"]}' /></a>]
								[<a href="#toNavigationMenu" title="<c:out value='${translator["tracking.accessibility.bar.link2"]}' />" accesskey="n"><c:out value='${translator["tracking.accessibility.bar.link2"]}' /></a>]
								[<a href="#toToolMenu" title="<c:out value='${translator["tracking.accessibility.bar.link3"]}' />" accesskey="t"><c:out value='${translator["tracking.accessibility.bar.link3"]}' /></a>]
								[<a href="#toButtons" title="<c:out value='${translator["tracking.accessibility.bar.link4"]}' />" accesskey="b"><c:out value='${translator["tracking.accessibility.bar.link4"]}' /></a>]
							</map>
						</div>
						<div id="header">
							<div id="hLogo">
								<img src="<c:out value="${init.imagesPath}" />logo.gif" title='${translator["tracking.header.logo"]}' alt='${translator["tracking.header.logo"]}' />				
							</div>
							<div id="hPageNav"></div>
						</div>
						<div id="siteNav">
							<a id="toNavigationMenu" name="toNavigationMenu" class="hide"></a>
							<h1 class="hide"><c:out value='${translator["tracking.accessibility.navigationmenu"]}' /></h1>
							<ul id="siteLinkList">
								<li>
									<a href="welcome.jsp" title="<c:out value='${translator["tracking.navigationmenu.link1"]}' />" accesskey="1"><c:out value='${translator["tracking.navigationmenu.link1"]}' /></a>
								</li>
								<li>
									<c:set var="courseTitle" value='<%= utilities.getCourseTitle(request.getParameter("course")) %>' />
									<a href="<c:out value='viewCourse.jsp?course=${param.course}' />" title="<c:out value='${courseTitle}' />" accesskey="2"><c:out value='${courseTitle}' /></a>
								</li>
								<c:choose>
									<c:when test="${param.level eq 1}">
										<c:set var="hrefExit" value="viewCourse.jsp?course=${param.course}" />
										<c:set var="contentTitle" value="${translator['tracking.content.title1']}" />
										<li class="selected">
											<a href="#"><c:out value='${translator["tracking.navigationmenu.link3"]}' /></a>			
										</li>
									</c:when>
									<c:when test="${param.level eq 2}">
										<c:set var="hrefExit" value="tracking.jsp?course=${param.course}&level=1" />
										<c:set var="contentTitle" value="${translator['tracking.content.title2']}" />
										<li>
											<a href="<c:out value='${hrefExit}' />" title="<c:out value='${translator["tracking.navigationmenu.link3"]}' />" accesskey="3"><c:out value='${translator["tracking.navigationmenu.link3"]}' /></a>			
										</li>
										<li class="selected">
											<a href="#"><c:out value='${translator["tracking.navigationmenu.link4"]}' /></a>			
										</li>
									</c:when>
									<c:when test="${param.level eq 3}">
										<c:set var="hrefExit" value="tracking.jsp?course=${param.course}&level=2&org=${param.org}&user=${param.user}" />
										<c:set var="contentTitle" value="${translator['tracking.content.title2']}" />
										<li>
											<a href="<c:out value='tracking.jsp?course=${param.course}&level=1' />" title="<c:out value='${translator["tracking.navigationmenu.link3"]}' />" accesskey="3"><c:out value='${translator["tracking.navigationmenu.link3"]}' /></a>			
										</li>
										<li>
											<a href="<c:out value='${hrefExit}' />" title="<c:out value='${translator["tracking.navigationmenu.link4"]}' />" accesskey="4"><c:out value='${translator["tracking.navigationmenu.link4"]}' /></a>			
										</li>
										<li class="selected">
											<a href="#"><c:out value='${translator["tracking.navigationmenu.link5"]}' /></a>			
										</li>
									</c:when>
								</c:choose>
							</ul>
						</div>
						<div id="pageContent">
							<div id="pToolsMenu">
								<a id="toToolMenu" name="toToolMenu" class="hide"></a>
								<h1 class="hide"><c:out value='${translator["tracking.accessibility.toolmenu"]}' /></h1>
								<div id="menuLogo"><c:out value='${translator["tracking.toolmenu.title"]}' /></div>
								<div id="menuContent">
									<ul>
										<li>
											<a href="#" title="<c:out value='${translator["tracking.toolmenu.link1"]}' />" class="selected"><c:out value='${translator["tracking.toolmenu.link1"]}' /></a>
										</li>
										<%-- <li>
											<a href="welcome.jsp" title="<c:out value='${translator["tracking.toolmenu.link4"]}' />" accesskey="w"><c:out value='${translator["tracking.toolmenu.link4"]}' /></a>							
										</li> --%>
										<li>
											<a href="#" title="<c:out value='${translator["tracking.toolmenu.link5"]}' />" accesskey="a" target="_blank" onclick="openWindow('help.jsp?page=tracking<c:out value="${param.level}" />', 'Help', '<c:out value="${init.helpProperties} }" />'); return false"><c:out value='${translator["tracking.toolmenu.link5"]}' /></a>							
										</li>
									</ul>
								</div>
							</div>
							<div id="pContent">
								<a id="toContent" name="toContent" class="hide"></a>
								<h1 class="hide"><c:out value='${translator["tracking.accessibility.content"]}' /></h1>
								<div id="contentTitle">
									<div class="title">
										<h2><c:out value="${contentTitle}" /></h2>
										<c:if test="${param.level gt 1}">
											&nbsp;<c:out value="${utilities.userName}" /> - <c:out value="${utilities.userId}" />
										</c:if>
									</div>
									<div class="help">
										<a href="#" title="<c:out value='${translator["tracking.content.help"]}' />" accesskey="h" target="_blank" onclick="openWindow('help.jsp?page=tracking<c:out value="${param.level}" />', 'Help', '<c:out value="${init.helpProperties} }" />'); return false">
											<img src="<c:out value="${init.imagesPath}" />help.gif" alt="<c:out value='${translator["tracking.content.help"]}' />" />
										</a>
									</div>
								</div>
								<div id="contentBody">
									<div id="contentIframe">			
										<c:choose>
											<c:when test="${param.level eq 1}">	
												
												<%= tableCourse.draw(init.getTranslations(), request.getParameter("course")) %>
											</c:when>
											<c:when test="${param.level eq 2}">	

												<%= tableStudent.draw(init.getTranslations(), request.getParameter("course"), request.getParameter("org"), request.getParameter("user")) %>
											</c:when>
											<c:when test="${param.level eq 3}">	
												
												<%= tableObjective.draw(init.getTranslations(), request.getParameter("course"), request.getParameter("org"), request.getParameter("itm"),request.getParameter("user"), request.getParameter("obj")) %>
											</c:when>
										</c:choose>
									</div>
								</div>
							</div>
							<div id="pButtons">
								<a id="toButtons" name="toButtons" class="hide"></a>
								<h1 class="hide"><c:out value='${translator["tracking.accessibility.button"]}' /></h1>
								<div id="buttonsContent">
									<form class="buttonsForm" method="post" action="#" enctype="application/x-www-form-urlencoded">
										<input type="button" title="<c:out value='${translator["tracking.content.button"]}' />" value="<c:out value='${translator["tracking.content.button"]}' />" class="button" accesskey="r" onclick="location.href='<c:out value="${hrefExit}" />'" />
									</form>
								</div>
							</div>
						</div>				
						<div id="footer">
							<div id="fInfo"><c:out value='${translator["tracking.footer.message"]}' /></div>
						</div>
					</div>					
				</c:when>
				<c:otherwise>
					<c:out value='${translator["error.userNotHaveAdministrationPermission"]}' />
				</c:otherwise>
			</c:choose>	
		</body>
		</html>
	</c:when>
	<c:otherwise>
		<c:out value='${translator["error.userNotAuthenticated"]}' />
	</c:otherwise>
</c:choose>