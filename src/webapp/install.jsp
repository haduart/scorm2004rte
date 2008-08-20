<%-- $Id: install.jsp,v 1.6 2008/02/08 12:25:19 msegarra Exp $
 -
 - Copyright (c) 2007 Enginyeria La Salle. Universitat Ramon Llull.
 - This file is part of SCORM2004RTE.
 -
 - SCORM2004RTE is free software; you can redistribute it and/or modify
 - it under the terms of the GNU General Public License as published by
 - the Free Software Foundation; either version 2 of the License, or
 - (at your option) any later version.
 -
 - This program is distributed in the hope that it will be useful,
 - but WITHOUT ANY WARRANTY; without even the implied warranty of
 - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 - General Public License for more details, currently published
 - at http://www.gnu.org/copyleft/gpl.html or in the gpl.txt in
 - the root folder of this distribution.
 -	
 - You should have received a copy of the GNU General Public License
 - along with this program; if not, write to the Free Software
 - Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 - MA 02110-1301, USA.
 --%>
 
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import = "org.campusproject.components.LoggingComponent"%>
<%@page import = "org.campusproject.components.ConfigurationAdminComponent"%>
<%@page import="org.osid.logging.LoggingException"%>
<%@page import="org.osid.shared.SharedException"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Install Page</title>
</head>
<body>
<h1>welcomeApp: Install page</h1>

<%

// This program is automaticaly executed when the admin installs the application into the campus. 
// Here is the place to put the init code needed for your OSIDs like, config parameters,
// languages tags or your authZ functions and qualifiers.

String mess = "";


	try {

	LoggingComponent log = new LoggingComponent(null);
    ConfigurationAdminComponent cc = new ConfigurationAdminComponent(null);

    String contextApp = "/scorm2004rte_md21";
    System.setProperty("nxContextApp", contextApp);
    System.setProperty("ccContextApp", contextApp);
    cc.addProperty("nxContextApp", contextApp, ConfigurationAdminComponent.APP_PROPERTY);
    log.info("INSTALL: nxContextApp");

		
	//parametros instancia --> seran sobrescritos 

	cc.addProperty("idLanguage","ca", ConfigurationAdminComponent.INSTANCE_PROPERTY);
	log.info("INSTALL: Instance parameter idLanguage=ca");	

	cc.addProperty("courseId","1", ConfigurationAdminComponent.INSTANCE_PROPERTY);
	log.info("INSTALL: Instance parameter courseId=1");	
	 
	//possibles valors del paràmetre: see | eliminate | create
	cc.addProperty("request","see", ConfigurationAdminComponent.INSTANCE_PROPERTY);
	log.info("INSTALL: Instance parameter request=see");	
		
		%>
			<p>Success!</p>
			<p>Installation completed.</p>
		<%
		
	} catch (LoggingException le) {
		mess = "LOGGING ERROR: " + le.getMessage();
		%>
			<p>ERROR. <%=mess%></p>
		<%
	} catch (SharedException se) {
		mess = "ERROR: " + se.getMessage();
		%>
			<p>ERROR. <%=mess%></p>
		<%
	}
%>
</body>
</html>