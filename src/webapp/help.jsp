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
 * Author: Víctor Miras, Enginyeria La Salle, Universitat Ramon Llull
 * You may contact the author at tl09599@salle.url.edu
 * And the copyright holder at: semipresencial@salle.url.edu
 ***********************************************************************/
 --%> 
 <? xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<jsp:useBean id="init" class="edu.url.lasalle.campus.scorm2004rte.system.Initialization" scope="page" />
<% init.setTranslations("help.title,help.noscript,error.userNotAuthenticated,error.userNotHaveAccessPermission"); %>
<c:set var="translator" value="${init.translations}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:out value="${init.stylesPath}" />scormrte_styles.css" />
		<link rel="icon" href="favicon.ico" type="image/x-icon" />
		<title><c:out value='${translator["help.title"]}' /></title>
	</head>
	<body>
<%-- Comprova si l'usuari està autenticat a la plataforma --%>
<c:choose>
	<c:when test='<%= init.isAuthenticated() %>'>
		<%-- Comprova si l'usuari té permisos d'accés a la pàgina --%>
		<c:choose>
			<c:when test='<%= init.hasAccessPermission() %>'>		
				<%-- Imprimeix el contingut de l'ajuda segons la petició del client --%>
				<c:choose>
					<c:when test="${param.page eq 'viewcourse'}">
					<%-- Mostra l'ajuda corresponent a la pàgina "viewcourse.jsp" --%>
					
		<center><img src="<c:out value="${init.imagesPath}" />help1.jpg" /></center>		
			
					</c:when>
					<c:when test="${param.page eq 'viewcontents'}">
					<%-- Mostra l'ajuda corresponent a la pàgina "viewcontents.jsp" --%>
					
					</c:when>
					<c:when test="${param.page eq 'tracking1'}">
					<%-- Mostra l'ajuda corresponent a la taula #1 de la pàgina "tracking.jsp" --%>

					</c:when>
					<c:when test="${param.page eq 'tracking2'}">
					<%-- Mostra l'ajuda corresponent a la taula #3 de la pàgina "tracking.jsp" --%>

					</c:when>
					<c:when test="${param.page eq 'tracking3'}">
					<%-- Mostra l'ajuda corresponent a la taula #3 de la pàgina "tracking.jsp" --%>

					</c:when>
					<c:when test="${param.page eq 'createcourse'}">
					<%-- Mostra l'ajuda corresponent a la pàgina "createcourse.jsp" --%>

					</c:when>
				</c:choose>
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

	</body>
</html>