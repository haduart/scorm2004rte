// $Id: TrackingObjective.java,v 1.4 2008/04/02 15:04:06 toroleo Exp $
/*
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
 */
package edu.url.lasalle.campus.scorm2004rte.server.jsp;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;


import edu.url.lasalle.campus.scorm2004rte.server.DataBase.GestorBD;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
		DynamicData.CMIObjectives;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
		DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.
		util.exception.DrawHTMLTableException;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;


/**
 * $Id: TrackingObjective.java,v 1.4 2008/04/02 15:04:06 toroleo Exp $
 * 
 * TrackingObjective.
 * Classe per mostrar el seguiment de l'estudiant 
 * en l'objectiu que es consulta d'una organització. 
 * 
 * @author M.Reyes La salle mtoro@salle.url.edu
 * @version 1.0 27/12/2007 $Revision: 1.4 $ $Date: 2008/04/02 15:04:06 $
 * $Log: TrackingObjective.java,v $
 * Revision 1.4  2008/04/02 15:04:06  toroleo
 * Modificació de les tabules del
 * seguiment per que no apareguin null
 * o Unkown
 *
 * Revision 1.3  2008/02/25 09:12:58  ecespedes
 * *** empty log message ***
 *
 * Revision 1.2  2008/01/31 14:23:37  ecespedes
 * Millorant el sistema.
 *
 * Revision 1.1  2007/12/28 14:46:12  toroleo
 * Implementat el seguiment de l'estudiant
 * en l'objectiu que es consulta
 * d'una organització.
 *
 *
 */
public class TrackingObjective {
	
	/**
	 * Constructor.
	 *
	 */
	public TrackingObjective() {
		
	}

	/**
	 * Obté el codi html de la taula del seguiment de 
	 * l'estudiant per l'objectiu seleccionat 
	 * d'una organització concreta.
	 * 
	 * @param aHMTranslator 	HashMap amb les traduccions dels textos.
	 * @param anIdCourse		Identificador del curs al que pertany 
	 * 							 l'organització.
	 * @param anIdOrganization	Identificador de l'organització de la 
	 * 							 qual es vol mostrar la informació 
	 * 							 de l'objectiu concret.
	 * @param anIdItem			Identificador de l'item al que pertany 
	 * 							 l'objectiu.
	 * @param anIdStudent		Identificador de l'estudiant del que es
	 * 							 vol mostrar el seguiment de l'objectiu.
	 * @param anIdObjective		Identificador de l'objectiu que es vol mostrar 
	 * 							 el seguiment.
	 * @return	Retorna un string amb el codi html per mostrar la taula del
	 * 			 seguiment de l'alumne per un objectiu d'una organització.
	 * @throws DrawHTMLTableException Es llença en cas d'haver algun problema
	 * 								   a l'hora de dibuixar la taula.	
	 */
	public final String draw(final HashMap aHMTranslator,
							 final String anIdCourse,
							 final String anIdOrganization,
							 final String anIdItem,
							 final String anIdStudent,
							 final String anIdObjective) 
						throws DrawHTMLTableException {

		//Aquí es guardarà el codi html de la taula a mostrar per pantalla
		String htmlTable = "";
		
		//es fa la conexió amb la base de dades
		GestorBD gestorBD = GestorBD.getInstance();
		
		
		//try {
						
		//Es fa el canvi de l'identificador de l'estudiant del 
		//sistema central a l'identificador al subsistema scorm.
		/*
		 * int idStudentScorm = 
		 * 		gestorBD.getIdStudentScormQuery(anIdStudent);
		 */
					
		//Es passa a tipus long  l'identificador de l'organització.
		long lgIdOrganization = Long.parseLong(anIdOrganization);
		
		//Es recullen les dades de l'estudiant per l'organització escullida.
		/*
		UserObjective userObjective = 
				gestorBD.getUserData(lgIdOrganization, idStudentScorm);
		*/
		UserObjective userObjective = 
			gestorBD.getUserData(lgIdOrganization, anIdStudent);
		
		
		//Es comprova si s'han trobat i/o recuperat dades del seguiment 
		//de l'usuari per l'organització passada
		if (userObjective == null) {
			//no hi han dades 
			
			//S'escriu el missatge d'error al fitxer de logs.
			String message = " Table could not be drawn."
					+ "  No information found about student"
					+ " monitoring for referenced organization.";

			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
							 TrackingObjective.class.getName(),
								message);
			
			throw new DrawHTMLTableException(message);	
		}
		
		CMIObjectives objective = null;
		
	     //es recull la informació de l'objectiu passat
		if (userObjective.dataModel != null) {
			if (userObjective.dataModel.get(anIdItem) != null) {
				if (userObjective.dataModel.
						get(anIdItem).cmiObjectives != null) {
					objective = 
						userObjective.dataModel.get(anIdItem)
									.cmiObjectives.get(anIdObjective);

					//es comprova si existeix l'obojectiu
					if (objective == null) {
						//no exixteix
						
						//S'escriu el missatge d'error al fitxer de logs.
						String message = " Table could not be drawn."
								+ " Objective referenced is not exist.";
						LogControl log = LogControl.getInstance();
						log.writeMessage(Level.SEVERE, 
										 TrackingObjective.class.getName(),
										 message);
						
						throw new DrawHTMLTableException(message);
					} //del if (objective == null) 

				} else {
					//L'organització no té Objectius.
					
					//S'escriu el missatge d'error al fitxer de logs.
					String message = " Table could not be drawn."
							+ " Organization not have objectives.";
					LogControl log = LogControl.getInstance();
					log.writeMessage(Level.SEVERE, 
									 TrackingObjective.class.getName(),
									 message);
					
					throw new DrawHTMLTableException(message);
				} // del if (userObjective.dataModel.
				  //            get(anIdOrganization).cmiObjectives != null)
			} else {
				//L'organització no existeix
				
				//S'escriu el missatge d'error al fitxer de logs.
				String message = " Table could not be drawn."
						+ " Organization referenced is not exist.";
				LogControl log = LogControl.getInstance();
				log.writeMessage(Level.SEVERE, 
								 TrackingObjective.class.getName(),
								 message);
				
				throw new DrawHTMLTableException(message);
			} //del if (userObjective.dataModel
			  // 		.get(anIdOrganization) != null)
		} else {
			//No hi ha informació de les organitzacions de l'usuari
			
			//S'escriu el missatge d'error al fitxer de logs.
			String message = " Table could not be drawn."
					+ " Information of the organization"
					+ " referenced is not exist.";
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
							 TrackingObjective.class.getName(),
							 message);
			
			throw new DrawHTMLTableException(message);
		} //del if (userObjective.dataModel != null)
					
		//Es dibuixa la taula
		 //Es dibuixa la capcelera de la taula
		htmlTable = writeHeadTableStudents(aHMTranslator, anIdObjective);
		System.out.println("//////////////TrackingObjective////////////////");
		 //Es tracten les fileres de dades de la taula
		htmlTable =	processObjective(aHMTranslator, htmlTable, 
									 objective, anIdObjective);
		
		//Es tenca la taula
		htmlTable = htmlTable 
					+ 	"</tbody>"
					+ "</table>";
		/*
		} catch (SQLException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			String message = "SQLException. " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					TrackingObjective.class.getName(), message);
		}
		*/
		return htmlTable;
	}
	
	
	/**
	 * Es processa la informació de l'objectiu per ser mostrar
	 * a la taula html.
	 * 
	 * @param aHMTranslator		HashMap amb les traduccions dels textos.
	 * @param aHtmlTable		La taula html de les dades que es porta creada 
	 * 							 fins ara.
	 * @param anObjective		Objecte amb tota la informació de l'objectiu 
	 * 							 en concret.
	 * @param anIdObjecString	Identificador de l'objectiu del que es vol
	 * 							 mostrar la informació.
	 * @return  La taula html amb la filera de la informació de l'objectiu.
	 */
	private String processObjective(final HashMap aHMTranslator,
									final String aHtmlTable,
									final CMIObjectives anObjective,
									final String anIdObjecString) {
		String htmlTable = aHtmlTable;
		
		String nameObjective = "";
		String completed = "";
		String exit = "";
		String qualification = "";
		/*String qualification = (String) 
		   aHMTranslator.get("tracking.content.table2.unknown");*/
		String description = "";
		
		 //nom identificador de l'objectiu
		nameObjective = anIdObjecString;
		
		System.out.println("nameObjective: " + nameObjective);
		System.out.println("completionStatus: " + anObjective.completionStatus);
		 //estat de completat
		completed = translateStateExit(aHMTranslator, 
					anObjective.completionStatus);
		
		System.out.println("successStatus: " + anObjective.successStatus);
		 //com s'ha passat el curs
		exit = translateStateExit(aHMTranslator, 
					anObjective.successStatus);
		
		 //nota
		String min = "";
		String max = "";
		System.out.println("scoreRaw: " + anObjective.scoreRaw);
		if ((anObjective.scoreRaw != null) 
    		    && (!"unknown".equals(anObjective.scoreRaw))
    		    && (!(anObjective.scoreRaw.length() == 0))) {
			
			qualification = anObjective.scoreRaw;
			
			System.out.println("scoreMin: " + anObjective.scoreMin);
			if (anObjective.scoreMin != null
					&& (anObjective.scoreMin.length() > 0)) {
				min = anObjective.scoreMin;
			}
			
			System.out.println("scoreMax: " + anObjective.scoreMax);
			if (anObjective.scoreMax != null
				&& (anObjective.scoreMax.length() > 0)) {
				max = anObjective.scoreMax;
			}	
			if ((!"".equals(min)) && (!"".equals(max)) 
   				 && (!"".equals(qualification))) {
				qualification = min + "&nbsp;&lt;&nbsp;"
				+ qualification	+ "&nbsp;&gt;&nbsp; " 
				+ max;	
			}
			
		}
		
		System.out.println("min: " + min);
		System.out.println("max: " + max);
		System.out.println("qualification: " + qualification);
		
		System.out.println("description: " + anObjective.description);
 		 //descripció
		if ((anObjective.description != null) 
				&& (anObjective.description.length() > 0)
				&& (!"unknown".equalsIgnoreCase(anObjective.description))) {
			description = anObjective.description;
		}
	
		//s'escriu la linea hml de la taula corresponent
		//a la informació de l'objectiu.
		htmlTable = writeBodyTableStudents(htmlTable, nameObjective, 
								completed, exit, qualification, description);
		
		return htmlTable;
		
	}
	
	/**
	 * Tradueix els literals de l'estat y de terminació de 
	 * l'item a l'idioma utilitzat per l'usuari.
	 * 
	 * @param aHMTranslator HashMap amb les traduccions dels textos.
	 * @param aStatus		Literal a traduir.
	 * @return	Literal traduït.
	 */
	private String translateStateExit(final HashMap aHMTranslator, 
									  final String aStatus) {
		String status = "";
		
		if ((aStatus != null) && !"".equals(aStatus) 
			 && (!"unknown".equalsIgnoreCase(aStatus))) {
			String literal;
			if (aStatus.equals("not attempted")) {
				literal = "tracking.content.table2.attempted"; 
			} else {
				literal = "tracking.content.table2." + aStatus;
			}
			
			status = (String) aHMTranslator.get(literal);
		}
		return status;
		
	}
	
	/**
	 * Escriu la capcelera de la taula dels items de la
	 * organització per estudiant.
	 * 
	 * @param aHMTranslator 	HashMap amb les traduccions dels textos.
	 * @param anObjectiveName	Nom de l'objectiuo del que s'està
	 * 							 ensenyant les dades.
	 * @return Capcelera de la taula d'organització en codi html.
	 */
	private String writeHeadTableStudents(final HashMap aHMTranslator,
										  final String anObjectiveName) {
		
		String objective = "";
		String completed = "";
		String exit = "";
		String qualification = "";
		String description = "";
		String summary = "";		//descripció de la taula
		
	
		//es tradueixen les capceleres a l'idioma que fa servir l'usuari.
		summary = (String)
			aHMTranslator.get("tracking.content.table3.summary");
		objective = (String)
			aHMTranslator.get("tracking.content.table3.header1");	
		completed = (String)
			aHMTranslator.get("tracking.content.table3.header2");
		exit = (String)
			aHMTranslator.get("tracking.content.table3.header3");
		qualification = (String)
			aHMTranslator.get("tracking.content.table3.header4");
		description = (String)
			aHMTranslator.get("tracking.content.table3.header5"); 
		
		
		//s'escriu el codi html de la capcelera de la taula
		String htmlHead = "<table class='tracking'"
						+ " summary= ' " + summary + "'>"
        		 + "	<caption>" + anObjectiveName + "</caption>"
        		 + " 	<tbody>" 
				 + "		<tr>"
    			 + "			<th id=\"header1\"> "
    			 				+ objective + " </th>"
    			 + "			<th id=\"header2\"> "
    			 				+ completed  + " </th>"
    			 + "			<th id=\"header3\"> "
    			 				+ exit + " </th>" 
    			 + "			<th id=\"header4\"> "
    			 				+ qualification + " </th>" 
    			 + "			<th id=\"header5\"> "
    			 				+ description + " </th>"
    			 + "			</tr>";
		
  
		return htmlHead;
	}

	/**
	 * * Escriu una filera nova de dades a la taula de l'objectius de
	 * l'organització per l'estudiant.
	 * 
	 * @param aHtmlTable		La taula html de les dades que es porta creada 
	 * 							 fins ara.
	 * @param aNameObjective	Nom de l'objectiu.
	 * @param aCompleted		Indica si s'ha completat el contingut o no.
	 * @param aExit				Indica si s'ha finalitzat amb èxit el
	 * 							 contingut d'aprenentatge.
	 * @param aQualification	Puntuació de l'estudiant.
	 * @param aDescription 		Descripció de l'objectiu.
	 * 
	 * @return Codi html de com queda la taula després d'inserir la nova linea.
	 */
	private String writeBodyTableStudents(final String aHtmlTable,
										  final String aNameObjective,
										  final String aCompleted,
										  final String aExit,
										  final String aQualification,
										  final String aDescription) { 
		
		//s'escriu la linea amb les dades de l'objectiu 
		//per l'estudiant i l'organització passades
		String htmlTable = aHtmlTable + "<tr>"
						   + "<td headers=\"header1\">" + aNameObjective 
						   								+ "</td>"
						   + "<td headers=\"header2\">" + aCompleted + "</td>"
						   + "<td headers=\"header3\">" + aExit + "</td>"
						   + "<td headers=\"header4\">" + aQualification 
						   								+ "</td>"
						   + "<td headers=\"header5\">" + aDescription + "</td>"
						   + "</tr>";
		
		return htmlTable;
	}
}
