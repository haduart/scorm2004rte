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
<%@ page import="edu.url.lasalle.campus.scorm2004rte.osid.requests.ConfigurationRequest" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="edu.url.lasalle.campus.scorm2004rte.util.main.InicializationDummy,
	org.osid.agent.AgentException, org.osid.configuration.ConfigurationException, 
	org.osid.id.IdException, org.osid.authorization.AuthorizationException,
	org.osid.dictionary.DictionaryException, 
	edu.url.lasalle.campus.scorm2004rte.server.DataBase.GestorBD,
	edu.url.lasalle.campus.scorm2004rte.system.DataAccessRepository" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>Initialization Page</title>
	</head>
	<body>
		<h1>welcomeApp: Install page</h1>
		<%
		try {
			
			//InicializationDummy.createAgents();
			InicializationDummy.iniDummy();
			//InicializationDummy.createGroups();
			//InicializationDummy.install();
			
			GestorBD nouGestor = GestorBD.getInstance();
		
			DataAccessRepository.
			getInstance().registerDataAccess(
				nouGestor.getDataAccessID(), nouGestor);

			ConfigurationRequest configurationRequest = ConfigurationRequest.getInstance();
	
			System.out.println("[OK?] Idioma: " + configurationRequest.getProperty("idLanguage"));
			System.out.println("[OK?] ID Curs: " + configurationRequest.getProperty("courseId"));
			System.out.println("[OK?] Request: " + configurationRequest.getProperty("request"));
			response.sendRedirect("viewCourse.jsp?course=" + configurationRequest.getProperty("courseId"));
		} catch (AgentException e) {
			System.err.println("[ERROR] AgentException: " + e.getMessage());
		} catch (IdException e) {
		    System.err.println("[ERROR] IdException: " + e.getMessage());
		} catch (AuthorizationException e) {
		    System.err.println("[ERROR] ConfigurationException: " + e.getMessage());
		}
		%>
	</body>
</html>

