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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<c:choose>
	<c:when test='<%= init.isAuthenticated() %>'>
		<c:choose>
			<c:when test='<%= init.hasAccessPermission() %>'>
			<jsp:useBean id="utilities" class="edu.url.lasalle.campus.scorm2004rte.system.JSPUtils" scope="page" />
			<% init.setTranslations("sequencingengine.content.message.normal,sequencingengine.content.message.end,error.userNotAuthenticated,error.userNotHaveAccessPermission"); %>
			<c:set var="translator" value="${init.translations}" />
			<jsp:useBean id="sequencing" class="edu.url.lasalle.campus.scorm2004rte.server.jsp.PrePostSequencing" scope="page" />
			<% sequencing.processRequest(request, utilities); %>
			<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
				<link rel="stylesheet" type="text/css" media="all" href="
					<c:out value="${init.stylesPath}" />scormrte_styles.css" />
				<script type="text/javascript">
				<!--
					function Objective() {
						this.id = "";
						this.score_raw = "";
						this.score_min = "";
						this.score_max = "";
						this.score_scaled = "";
						this.success_status = "unknown";
						this.completion_status = "unknown";
						this.progress_measure = "";
						this.description = "";
					}
					
					function load() {
						<%= sequencing.processLaunching(request, utilities) %>
					}
				//-->
				</script>	
			</head>
			<body onload="load();">
				<c:choose>
					<c:when test='<%= sequencing.isNormalTypeMessage() %>'>
						<c:out value='${translator["sequencingengine.content.message.normal"]}' />
					</c:when>
					<c:otherwise>
						<c:out value='${translator["sequencingengine.content.message.end"]}' />
					</c:otherwise>
				</c:choose>
			</body>
			</html>
			</c:when>
			<c:otherwise>
				<c:out value='${translator["error.userNotHaveAccessPermission"]}' />
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:out value='${translator["error.userNotAuthenticated"]}' />
	</c:otherwise>
</c:choose>		