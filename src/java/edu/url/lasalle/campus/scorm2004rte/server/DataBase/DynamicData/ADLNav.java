// $Id: ADLNav.java,v 1.11 2008/01/30 17:41:15 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData;

/** <!-- Javadoc -->
 * $Id: ADLNav.java,v 1.11 2008/01/30 17:41:15 ecespedes Exp $ 
 * <b>Títol:</b> ADLNav <br><br>
 * <b>Descripció:</b> Aquesta classe és l'estructura que guarda la <br/>
 * resposta  de l'usuari (el seu request). Depenent d'això li <br/>
 * retornarem un item o un altre.<br/>
 * Serà utilitzat per les classes UserObjectives, Abstract_Item, <br /> 
 * Root_Item i Leaf_Item. 
 * <br>Aquesta classe s'ha de poder serialitzar perquè s'haurà de <br />
 * convertir en un objecte Blob dintre de MySQL.<br><br> 
 * <b>Companyia</b> Departament d'Aprenentage Semipresencial (LaSalleOnLine
 * Enginyeries).<br>
 * @see SCORM2004 Sequencing & Navigation 3rt Edition
 * @author Eduard Céspedes i Borràs / LaSalle / ecespedes@salleurl.edu
 * @version Versió $Revision: 1.11 $ $Date: 2008/01/30 17:41:15 $
 * $Log: ADLNav.java,v $
 * Revision 1.11  2008/01/30 17:41:15  ecespedes
 * Versió final: organització  serialitzada.
 *
 * Revision 1.10  2008/01/08 11:50:21  ecespedes
 * Ultimes modificacions de l'OverallSequencingProcess
 *
 * Revision 1.9  2008/01/07 15:59:21  ecespedes
 * Implementat 'deliveryControls' i el 'hideLMSUI' del Presentation.
 *
 * Revision 1.8  2008/01/04 11:35:32  ecespedes
 * Implementant l'OverallSequencingProcessTest sobre el linear_Choice.
 *
 * Revision 1.7  2008/01/03 15:05:32  ecespedes
 * Arreglats elements del CMIDataModel i començant a crear un nou test.
 *
 * Revision 1.6  2007/12/27 15:02:24  ecespedes
 * Reunificant totes les crides del seqüenciament.
 *
 * Revision 1.5  2007/12/13 15:25:12  ecespedes
 * Problemes amb el sistema d'arbre de clusters.
 * Falla l'ObjectiveStatusKnown.
 *
 * Revision 1.4  2007/12/10 11:50:27  ecespedes
 * Començant a juntar les peces del procés de seqüenciament.
 *
 * Revision 1.3  2007/11/08 16:33:09  ecespedes
 * Començant a Implementar el OverallSequencingProcess, que serà
 * les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
 * realment el seqüenciament SCORM1.3
 *
 * Revision 1.2  2007/10/30 14:07:42  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Començant a crear el sistema de gestió dels cursos:
 * - CourseAdministrator i CourseManager.
 *
 */
public class ADLNav {
	/**
	 * adl.nav.request:
	 * Navigation request to be processed immediately following Terminate().
	 * (request(continue, previous, choice, exit, exitAll, abandon, 
	 * 		abandonAll, suspendAll, _none_) (RW)
	 * Nota! : Quan l'usuari faci un choice, ja sigui directament
	 * 		a través de l'arbre o fent una petició en javascript en 
	 * 		el request tindrem algo de la forma:
	 * 				"{target=intro}choice"
	 * 		on intro serà l'identificador de l'item.
	 * 
	 * Nota2!: Ho haurà d'omplir l'SCO
	 */
	public String request = "_none_";
	/**
	 * adl.nav.request_valid.continue:
	 * Used by a SCO to determine if a Continue navigation will succeed. 
	 * (state(true, false, unknown) (RO)
	 *  
	 * Nota: Ho haurà d'omplir l'LMS 
	 */
	public String requestValidContinue = "true";
	/**
	 * adl.nav.request_valid.previous:
	 * Used by a SCO to determine if a Previous navigation will succeed. 
	 * (state(true, false, unknown) (RO)
	 * 
	 * Nota: Ho haurà d'omplir l'LMS 
	 */
	public String requestValidPrevious = "true";
	/**
	 * Indica si un fill d’aquesta activitat pot finalitzar.
	 * En altres paraules, si se el botó de "Exit" ha de ser
	 * visible i accessible per l'usuari o no.
	 * 
	 * opcional, default true
	 * 
	 * Nota: Ho haurà d'omplir l'LMS 
	 */
	public Boolean choiceExit = true;
	/**
	 * S'ha de mostrar el botó de 'Continue'?
	 * 
	 * Nota: Ho haurà d'omplir l'LMS 
	 */
	public Boolean choiceContinue = true;
	/**
	 * S'ha de mostrar el botó de 'Previous'?
	 * 
	 * Nota: Ho haurà d'omplir l'LMS 
	 */
	public Boolean choicePrevious = true;
	/**
	 * S'ha de mostrar el botó de 'TreeChoice'?
	 * 
	 * Nota: Ho haurà d'omplir l'LMS 
	 */
	public Boolean showTreeChoice = true;
	/**
	 * Aquest String serà diferent de null si hi ha hagut algun error
	 * per part de l'LMS. Aleshores l'API l'ha de mostrar a l'usuari.
	 * 
	 * Cas especial! lmsException = "Exit"
	 * 		En cas de tindre-ho vol dir que la postCondició ha
	 * 		indicat un "Exit" i per tant s'haurà de guardar tot
	 * 		i finalitzar.
	 */
	public String lmsException = "";
}
