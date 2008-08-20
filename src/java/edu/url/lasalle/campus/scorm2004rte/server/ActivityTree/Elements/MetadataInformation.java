// $Id: MetadataInformation.java,v 1.5 2008/01/31 14:23:37 ecespedes Exp $
/*
 * Copyright (c) [yyyy] [TITULAR]
 * This file is part of [SSSSS].  
 * 
 * [SSSS] is free software; you can redistribute it and/or modify 
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  
 * 02110-1301, USA.  
 */

package edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements;

/**
* $Id: MetadataInformation.java,v 1.5 2008/01/31 14:23:37 ecespedes Exp $
* <b>Títol:</b> MetadataInformation<br><br>
* <b>Descripció:</b> En aquesta classe tindrem l'informació que <br>
* hem extret de la metadata (dintre del imsmanifest.xml).<br><br> 
*
* @author Eduard Céspedes i Borràs/Enginyeria La Salle/ecespedes@salle.url.edu
* @version Versió $Revision: 1.5 $ $Date: 2008/01/31 14:23:37 $
* $Log: MetadataInformation.java,v $
* Revision 1.5  2008/01/31 14:23:37  ecespedes
* Millorant el sistema.
*
* Revision 1.4  2008/01/29 18:01:41  ecespedes
* Implementant versió serialitzada de l'organització.
*
* Revision 1.3  2008/01/25 15:05:27  ecespedes
* Implementant les interficies per les BD.
*
*/
public class MetadataInformation {
	
	//----------- <general> ------------
	/**
	 * <manifest  ... identifier="DCSE_2007_2on_quadrimestre">
	 */
	public String identifier;

	/**
	 * <manifest  ... version="1.1" >
	 */
	public String version;
	
	/**
	 * Dintre del metadata:
	 *  <lom>
	 *    <general>
	 *  	<title>
	 */
	public String title = "";
	/**
	 * Dintre del metadata:
	 *  <lom>
	 *    <general>
	 *  	<description>
	 */
	public String description = "";

	//----------- <technical> ------------
	/**
	 * Dintre del metadata:
	 *  <lom>
	 *    <technical>
	 *  	Agafar-ho tot menys els <format>
	 */
	public String requirement = "Sense Requeriments específics"; //fet
	//----------- <educational> ------------
	/**
	 * Dintre del metadata:
	 * <lom>
	 * 	 <educational>
	 *     <intendedEndUserRole>
	 */
	public String intendedEndUserRole = "Universitari";
	/**
	 * Dintre del metadata:
	 * <lom>
	 * 	 <educational>
	 *     <context>
	 */
	public String educationalContext = "Universitat";
	/**
	 * <lom>
	 * 	  <educational>
	 * 		<typicalLearningTime>
	 */
	public String typicalLearningTime; //fet? <duration>P0Y6M</duration> ?!?
	/**
	 * <lom>
	 * 	 <educational>
	 * 		<language>
	 */	
	public String educationalLanguage;
	//----------- <lifeCycle> ------------
	/**
	 * Dintre del metadata:
	 * <lom>
	 * 	 <lifeCycle>
	 *     <contribute>
	 *     Agafar tots els <entity> i tractar els:
	 *     		BEGIN:VCARD&#13;&#10;VERSION:2.1&#13;&#10;
	 *     		FN:José Julio Ruíz Toroleón&#13;&#10;END:VCARD
	 *     
	 *     perquè quedi: José Julio Ruíz Toroleón
	 */
	public String author = "Arquitectura i Enginyeria LaSalle";
	/**
	 * Dintre del metadata:
	 * <lom>
	 * 	 <rights>
	 *     <copyrightAndOtherRestrictions>
	 *        <value> yes </value>
	 *     </copyrightAndOtherRestrictions>
	 *     <description>
	 *     Si es "yes" aleshores agafem la descripció.      
	 */
	public String copyright = "Sense Copyright";

}

