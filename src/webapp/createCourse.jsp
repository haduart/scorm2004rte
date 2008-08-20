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
<% init.setTranslations("error.userNotHaveAdministrationPermission,error.userNotAuthenticated,createcourse.title,createcourse.noscript,createcourse.accessibility.bar.title,createcourse.accessibility.bar.link1,createcourse.accessibility.bar.link2,createcourse.accessibility.bar.link3,createcourse.accessibility.bar.link4,createcourse.header.logo,createcourse.accessibility.navigationmenu,createcourse.navigationmenu.link1,createcourse.navigationmenu.link2,createcourse.accessibility.toolmenu,createcourse.toolmenu.title,createcourse.toolmenu.link1,createcourse.toolmenu.link2,createcourse.toolmenu.link3,createcourse.accessibility.content,createcourse.content.title,createcourse.content.help,createcourse.content.obligatoryField,createcourse.content.form.emptyField,createcourse.content.form.nonValidFile,createcourse.content.form.legend1,createcourse.content.form.legend2,createcourse.content.form.package,createcourse.content.form.importButton,createcourse.content.form.id,createcourse.content.form.vesion,createcourse.content.form.title,createcourse.content.form.description,createcourse.content.form.requirements,createcourse.content.form.directedTo,createcourse.content.form.educative,createcourse.content.form.author,createcourse.content.form.copyright,createcourse.content.form.duration,createcourse.content.form.yes,createcourse.content.form.no,createcourse.content.form.activateTracking,createcourse.content.form.january,createcourse.content.form.february,createcourse.content.form.march,createcourse.content.form.april,createcourse.content.form.may,createcourse.content.form.june,createcourse.content.form.july,createcourse.content.form.august,createcourse.content.form.september,createcourse.content.form.october,createcourse.content.form.november,createcourse.content.form.december,createcourse.content.form.timeStart,createcourse.content.form.timeFinish,createcourse.content.button,createcourse.accessibility.button,createcourse.footer.message"); %>
<c:set var="translator" value="<%= init.getTranslations() %>" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:out value="${init.stylesPath}" />scormrte_styles.css" />
		<link rel="icon" href="favicon.ico" type="image/x-icon" />
		<title><c:out value='${translator["createcourse.title"]}' /></title>
		<script type="text/javascript" language="javascript" src="<c:out value="${init.scriptsPath}" />scormrte_utils.js"></script>
	</head>
	<body>	
<%-- Comprova si l'usuari està autenticat a la plataforma --%>
<c:choose>
	<c:when test='<%= init.isAuthenticated() %>'>
		<%-- Comprova si l'usuari té permisos d'administració --%>
		<c:choose>
			<c:when test='<%= init.hasAdministrationPermission() %>'>
		<!-- Contenidor principal que conté el contingut de la pàgina -->
		<div id="container">
			<!-- Accessibilitat de la pàgina -->
			<div id="accessibility">
				<map title="<c:out value='${translator["createcourse.accessibility.bar.title"]}' />" class="hide">
					[<a href="#toContent" title="<c:out value='${translator["createcourse.accessibility.bar.link1"]}' />" accesskey="c"><c:out value='${translator["createcourse.accessibility.bar.link1"]}' /></a>]
					[<a href="#toNavigationMenu" title="<c:out value='${translator["createcourse.accessibility.bar.link2"]}' />" accesskey="n"><c:out value='${translator["createcourse.accessibility.bar.link2"]}' /></a>]
					[<a href="#toToolMenu" title="<c:out value='${translator["createcourse.accessibility.bar.link3"]}' />" accesskey="t"><c:out value='${translator["createcourse.accessibility.bar.link3"]}' /></a>]
					[<a href="#toButtons" title="<c:out value='${translator["createcourse.accessibility.bar.link4"]}' />" accesskey="b"><c:out value='${translator["createcourse.accessibility.bar.link4"]}' /></a>]
				</map>
			</div>
			<!-- Capçalera de la pàgina -->
			<div id="header">
				<!-- Logotip de la pàgina -->
				<div id="hLogo">
					<img src="<c:out value="${init.imagesPath}" />logo.gif" title='${translator["createcourse.header.logo"]}' alt='${translator["createcourse.header.logo"]}' />				
				</div>
				<div id="hPageNav"></div>
			</div>
			<!-- Fil d'Ariadna -->
			<div id="siteNav">
				<a id="toNavigationMenu" name="toNavigationMenu" class="hide"></a>
				<h1 class="hide"><c:out value='${translator["createcourse.accessibility.navigationmenu"]}' /></h1>
				<ul id="siteLinkList">
					<li>
						<a href="welcome.jsp" title="<c:out value='${translator["createcourse.navigationmenu.link1"]}' />" accesskey="1"><c:out value='${translator["createcourse.navigationmenu.link1"]}' /></a>
					</li>
					<li class="selected">
						<a href="#"><c:out value='${translator["createcourse.navigationmenu.link2"]}' /></a>
					</li>
				</ul>
			</div>
			<!-- Cos de la pàgina -->
			<div id="pageContent">
				<!-- Menú d'eines -->
				<div id="pToolsMenu">
					<a id="toToolMenu" name="toToolMenu" class="hide"></a>
					<h1 class="hide"><c:out value='${translator["createcourse.accessibility.toolmenu"]}' /></h1>
					<!-- Títol del menú d'eines -->
					<div id="menuLogo"><c:out value='${translator["createcourse.toolmenu.title"]}' /></div>
					<!-- Contingut del menú d'eines -->
					<div id="menuContent">
						<ul>
							<li>
								<a href="#" class="selected"><c:out value='${translator["createcourse.toolmenu.link1"]}' /></a>
							</li>
							<li>
								<a href="welcome.jsp" title="<c:out value='${translator["createcourse.toolmenu.link2"]}' />" accesskey="w"><c:out value='${translator["createcourse.toolmenu.link2"]}' /></a>							
							</li>
							<li>
								<a href="#" title="<c:out value='${translator["createcourse.toolmenu.link3"]}' />" accesskey="a" target="_blank" onclick="openWindow('help.jsp?page=createcourse', 'Help', '<c:out value="${init.windowProperties} }" />', '<c:out value="${init.windowWidth} }" />', '<c:out value="${init.windowHeight} }" />'); return false"><c:out value='${translator["createcourse.toolmenu.link3"]}' /></a>							
							</li>
						</ul>
					</div>
				</div>
				<!-- Títol i eines del contingut de la pàgina -->
				<div id="pContent">
					<a id="toContent" name="toContent" class="hide"></a>
					<h1 class="hide"><c:out value='${translator["createcourse.accessibility.content"]}' /></h1>
					<!-- Títol de la pàgina -->
					<div id="contentTitle">
						<div class="title"><h2><c:out value='${translator["createcourse.content.title"]}' /></h2></div>
					<!-- Ajuda de la pàgina -->
					<div class="help">
						<a href="#" title="<c:out value='${translator["createcourse.content.help"]}' />" accesskey="h" target="_blank" onclick="openWindow('help.jsp?page=createcourse', 'Help', '<c:out value="${init.windowProperties} }" />', '<c:out value="${init.windowWidth} }" />', '<c:out value="${init.windowHeight} }" />'); return false">
							<img src="<c:out value="${init.imagesPath}" />help.gif" alt="<c:out value='${translator["createcourse.content.help"]}' />" />
						</a>
					</div>
				</div>
				<!-- Contingut de la pàgina -->
				<div id="contentBody">
					<!-- Capa funcionant com si fos un iframe amb el contingut de la pàgina -->
					<div id="contentIframe">											
				
				<c:choose>
					<c:when test="${param.page eq 1}">
						<script type="text/javascript">
						<!--										
							/*
							 * validateForm()
							 *
							 * Valida el formulari abans d'enviar-lo.
							 */
							function validateForm() {
								var form = document.getElementById("courseForm");
								var file = form.package.value;
								if (file == '') {
									alert("<c:out value='${translator["createcourse.content.form.emptyField"]}' />");
									return false;
								} else {
							        var extension = file.substring(file.length - 4);
							        extension = extension.toLowerCase();
							        if (extension != ".zip") {
							            alert("<c:out value='${translator["createcourse.content.form.nonValidFile"]}' />");
							            return false;
							        }
								}
								return true;
							}
						//-->
						</script>
						<form id="courseForm" enctype="multipart/form-data" action="createCourse.jsp?page=2" method="post" onsubmit="return validateForm()">
							<fieldset>		
								<legend><c:out value='${translator["createcourse.content.form.legend1"]}' /></legend>
								<div id="field">
									<div>
										<label><c:out value='${translator["createcourse.content.form.package"]}' /></label>
										<input type="file" name="package" class="writeField" /><input type="submit" value="<c:out value='${translator["createcourse.content.form.importButton"]}' />" />							
									</div>
								</div>
							</fieldset>
							<!-- <input type="hidden" name="page" value="2" /> -->
						</form>
					</c:when>
					<c:when test="${param.page eq 2}">
						<jsp:useBean id="utils" class="edu.url.lasalle.campus.scorm2004rte.system.JSPUtils" scope="page" />
						<% String courseId = utils.importCourse(request); %>
						<%= courseId %>
						<c:choose>
							<c:when test="${courseId ne ''}">
							
						<script type="text/javascript">
						<!--										
							/*
							 * validateForm()
							 *
							 * Valida el formulari abans d'enviar-lo.
							 */
							function validateForm() {
								var form = document.getElementById("courseForm");
								if (form.title.value == '' || form.description.value == '' || 
										form.requeriments.value == '') {
									alert("<c:out value='${translator["createcourse.content.form.emptyField"]}' />");
									return false;
								}
								return true;
							}
						//-->
						</script>
						<form id="courseForm" enctype="application/x-www-form-urlencoded" action="createCourse.jsp?page=3" method="post" onsubmit="return validateForm()">
							<fieldset>		
								<legend><c:out value='${translator["createcourse.content.form.legend2"]}' /></legend>
								<div id="fields">
									<div id="comment"><c:out value='${translator["createcourse.content.obligatoryField"]}' /></div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.package"]}' /></label>
										<input type="text" name="package" readonly value="" class="readonlyField" />
									</div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.id"]}' /></label>
										<input type="text" name="id" readonly value="<%= utils.getCourseManifestId(courseId) %>" class="readonlyField" />
									</div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.title"]}' /></label>
										<input type="text" name="title" value="<%= utils.getCourseTitle(courseId) %>" class="writeField" />
									</div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.description"]}' /></label>
										<textarea name="description" value="<%= utils.getCourseDescription(courseId) %>" class="writeField"></textarea>
									</div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.requirements"]}' /></label>
										<textarea name="requeriments" value="<%= utils.getCourseRequirement(courseId) %>" class="writeField"></textarea>
									</div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.directedTo"]}' /></label>
										<input type="text" name="directed" readonly value="<%= utils.getCourseEndUserRole(courseId) %>" class="readonlyField" />
									</div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.educative"]}' /></label>
										<input type="text" name="educative" readonly value="<%= utils.getCourseEducationalContext(courseId) %>" class="readonlyField" />
									</div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.author"]}' /></label>
										<input type="text" name="author" readonly value="<%= utils.getCourseAuthor(courseId) %>" class="readonlyField" />
									</div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.copyright"]}' /></label>
										<input type="text" name="copyright" readonly value="<%= utils.getCourseCopyright(courseId) %>" class="readonlyField" />
									</div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.duration"]}' /></label>
										<input type="text" name="duration" readonly value="<%= utils.getCourseTypicalLearningTime(courseId) %>" class="readonlyField" />
									</div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.timeStart"]}' /></label>
										<select name="timeStartDay" class="writeField">
											<option value="1" selected="selected">1</option>
											<option value="2">2</option>
											<option value="3">3</option>
											<option value="4">4</option>
											<option value="5">5</option>
											<option value="6">6</option>
											<option value="7">7</option>
											<option value="8">8</option>
											<option value="9">9</option>
											<option value="10">10</option>
											<option value="11">11</option>
											<option value="12">12</option>
											<option value="13">13</option>
											<option value="14">14</option>
											<option value="15">15</option>
											<option value="16">16</option>
											<option value="17">17</option>
											<option value="18">18</option>
											<option value="19">19</option>
											<option value="20">20</option>
											<option value="21">21</option>
											<option value="22">22</option>
											<option value="23">23</option>
											<option value="24">24</option>
											<option value="25">25</option>
											<option value="26">26</option>
											<option value="27">27</option>
											<option value="28">28</option>
											<option value="29">29</option>
											<option value="30">30</option>
											<option value="31">31</option>
										</select>&nbsp;
										<select name="timeStartMonth" class="writeField">
											<option value="1" selected="selected"><c:out value='${translator["createcourse.content.form.january"]}' /></option>
											<option value="2"><c:out value='${translator["createcourse.content.form.february"]}' /></option>
											<option value="3"><c:out value='${translator["createcourse.content.form.march"]}' /></option>
											<option value="4"><c:out value='${translator["createcourse.content.form.april"]}' /></option>
											<option value="5"><c:out value='${translator["createcourse.content.form.may"]}' /></option>
											<option value="6"><c:out value='${translator["createcourse.content.form.june"]}' /></option>
											<option value="7"><c:out value='${translator["createcourse.content.form.july"]}' /></option>
											<option value="8"><c:out value='${translator["createcourse.content.form.august"]}' /></option>
											<option value="9"><c:out value='${translator["createcourse.content.form.september"]}' /></option>
											<option value="10"><c:out value='${translator["createcourse.content.form.october"]}' /></option>
											<option value="11"><c:out value='${translator["createcourse.content.form.november"]}' /></option>
											<option value="12"><c:out value='${translator["createcourse.content.form.december"]}' /></option>
										</select>&nbsp;
										<select name="timeStartYear" class="writeField">
											<option value="2008" selected="selected">2008</option>
											<option value="2009">2009</option>
											<option value="2010">2010</option>
											<option value="2011">2011</option>
											<option value="2012">2012</option>
											<option value="2013">2013</option>
											<option value="2014">2014</option>
											<option value="2015">2015</option>
											<option value="2016">2016</option>
											<option value="2017">2017</option>
											<option value="2018">2018</option>
											<option value="2019">2019</option>
											<option value="2020">2020</option>
										</select>&nbsp;-&nbsp;
										<select name="timeStartHour" class="writeField">
											<option value="0" selected="selected">00</option>
											<option value="1">01</option>
											<option value="2">02</option>
											<option value="3">03</option>
											<option value="4">04</option>
											<option value="5">05</option>
											<option value="6">06</option>
											<option value="7">07</option>
											<option value="8">08</option>
											<option value="9">09</option>
											<option value="10">10</option>
											<option value="11">11</option>
											<option value="12">12</option>
											<option value="13">13</option>
											<option value="14">14</option>
											<option value="15">15</option>
											<option value="16">16</option>
											<option value="17">17</option>
											<option value="18">18</option>
											<option value="19">19</option>
											<option value="20">20</option>
											<option value="21">21</option>
											<option value="22">22</option>
											<option value="23">23</option>
										</select>&nbsp;:
										<select name="timeStartMinute" class="writeField">
											<option value="0" selected="selected">00</option>
											<option value="5">05</option>
											<option value="10">10</option>
											<option value="15">15</option>
											<option value="20">20</option>
											<option value="25">25</option>
											<option value="30">30</option>
											<option value="35">35</option>
											<option value="40">40</option>
											<option value="45">45</option>
											<option value="50">50</option>
											<option value="55">55</option>
										</select>										
									</div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.timeFinish"]}' /></label>
										<select name="timeFinishDay" class="writeField">
											<option value="1" selected="selected">1</option>
											<option value="2">2</option>
											<option value="3">3</option>
											<option value="4">4</option>
											<option value="5">5</option>
											<option value="6">6</option>
											<option value="7">7</option>
											<option value="8">8</option>
											<option value="9">9</option>
											<option value="10">10</option>
											<option value="11">11</option>
											<option value="12">12</option>
											<option value="13">13</option>
											<option value="14">14</option>
											<option value="15">15</option>
											<option value="16">16</option>
											<option value="17">17</option>
											<option value="18">18</option>
											<option value="19">19</option>
											<option value="20">20</option>
											<option value="21">21</option>
											<option value="22">22</option>
											<option value="23">23</option>
											<option value="24">24</option>
											<option value="25">25</option>
											<option value="26">26</option>
											<option value="27">27</option>
											<option value="28">28</option>
											<option value="29">29</option>
											<option value="30">30</option>
											<option value="31">31</option>
										</select>&nbsp;
										<select name="timeFinishMonth" class="writeField">
											<option value="1" selected="selected"><c:out value='${translator["createcourse.content.form.january"]}' /></option>
											<option value="2"><c:out value='${translator["createcourse.content.form.february"]}' /></option>
											<option value="3"><c:out value='${translator["createcourse.content.form.march"]}' /></option>
											<option value="4"><c:out value='${translator["createcourse.content.form.april"]}' /></option>
											<option value="5"><c:out value='${translator["createcourse.content.form.may"]}' /></option>
											<option value="6"><c:out value='${translator["createcourse.content.form.june"]}' /></option>
											<option value="7"><c:out value='${translator["createcourse.content.form.july"]}' /></option>
											<option value="8"><c:out value='${translator["createcourse.content.form.august"]}' /></option>
											<option value="9"><c:out value='${translator["createcourse.content.form.september"]}' /></option>
											<option value="10"><c:out value='${translator["createcourse.content.form.october"]}' /></option>
											<option value="11"><c:out value='${translator["createcourse.content.form.november"]}' /></option>
											<option value="12"><c:out value='${translator["createcourse.content.form.december"]}' /></option>
										</select>&nbsp;
										<select name="timeFinishYear" class="writeField">
											<option value="2008" selected="selected">2008</option>
											<option value="2009">2009</option>
											<option value="2010">2010</option>
											<option value="2011">2011</option>
											<option value="2012">2012</option>
											<option value="2013">2013</option>
											<option value="2014">2014</option>
											<option value="2015">2015</option>
											<option value="2016">2016</option>
											<option value="2017">2017</option>
											<option value="2018">2018</option>
											<option value="2019">2019</option>
											<option value="2020">2020</option>
										</select>&nbsp;-&nbsp;
										<select name="timeFinishHour" class="writeField">
											<option value="0" selected="selected">00</option>
											<option value="1">01</option>
											<option value="2">02</option>
											<option value="3">03</option>
											<option value="4">04</option>
											<option value="5">05</option>
											<option value="6">06</option>
											<option value="7">07</option>
											<option value="8">08</option>
											<option value="9">09</option>
											<option value="10">10</option>
											<option value="11">11</option>
											<option value="12">12</option>
											<option value="13">13</option>
											<option value="14">14</option>
											<option value="15">15</option>
											<option value="16">16</option>
											<option value="17">17</option>
											<option value="18">18</option>
											<option value="19">19</option>
											<option value="20">20</option>
											<option value="21">21</option>
											<option value="22">22</option>
											<option value="23">23</option>
										</select>&nbsp;:
										<select name="timeFinishMinute" class="writeField">
											<option value="0" selected="selected">00</option>
											<option value="5">05</option>
											<option value="10">10</option>
											<option value="15">15</option>
											<option value="20">20</option>
											<option value="25">25</option>
											<option value="30">30</option>
											<option value="35">35</option>
											<option value="40">40</option>
											<option value="45">45</option>
											<option value="50">50</option>
											<option value="55">55</option>
										</select>										
									</div>
									<div>
										<label><c:out value='${translator["createcourse.content.form.activateTracking"]}' /></label>
										<select size="1" name="tracking" class="writeField">
											<option value="yes" selected="selected"><c:out value='${translator["createcourse.content.form.yes"]}' /></option>
											<option value="no"><c:out value='${translator["createcourse.content.form.no"]}' /></option>
										</select>
									</div>
								</div>
							</fieldset>
							</c:when>
							<c:otherwise>
								ERROR DESPRES D'INVOCAR "importCourse" DE "JSPUtils.java"!!! 
								COURSEID = '-1'
							</c:otherwise>
						</c:choose>

						
					</c:when>
					
				</c:choose>
							
					</div>
				</div>
			</div>
			<!-- Botons del contingut -->
			<div id="pButtons">
				<a id="toButtons" name="toButtons" class="hide"></a>
				<h1 class="hide"><c:out value='${translator["createcourse.accessibility.button"]}' /></h1>
			<c:choose>
				<c:when test="${param.page eq 2}">
				<div id="buttonsContent">
							<input type="submit" title="<c:out value='${translator["createcourse.content.button"]}' />" value="<c:out value='${translator["createcourse.content.button"]}' />" class="button" accesskey="s" />
							<!-- <input type="hidden" name="page" value="3" /> -->
						</form>
				</div>
				</c:when>
			</c:choose>
				
			</div>
			<!-- Peu de la pàgina -->
			<div id="footer">
				<div id="fInfo"><c:out value='${translator["createcourse.footer.message"]}' /></div>
			</div>
		</div>					
			</c:when>
			<c:otherwise>
				<c:out value='${translator["error.userNotHaveAdministrationPermission"]}' />
			</c:otherwise>
		</c:choose>	
	</c:when>
	<c:otherwise>
		<c:out value='${translator["error.userNotAuthenticated"]}' />
	</c:otherwise>
</c:choose>

		<noscript>
			<p><c:out value='${translator["createcourse.noscript"]}' /></p>	
		</noscript>
	</body>
</html>		