// $Id: TrackingStudent.java,v 1.7 2008/04/02 15:04:06 toroleo Exp $
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

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;


import java.text.SimpleDateFormat;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.campusproject.osid.agent.Agent;
import org.osid.agent.AgentException;
import org.osid.shared.SharedException;

import edu.url.lasalle.campus.scorm2004rte.osid.requests.AgentRequest;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.GestorBD;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
		DynamicData.CMIDataModel;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
		DynamicData.CMIObjectives;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
		DynamicData.TreeAnnotations;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
		DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.CourseAdministrator;
import edu.url.lasalle.campus.scorm2004rte.system.Initialization;
import edu.url.lasalle.campus.scorm2004rte.
		util.exception.DrawHTMLTableException;
import edu.url.lasalle.campus.scorm2004rte.util.exception.RequestException;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;


/**
 * $Id: TrackingStudent.java,v 1.7 2008/04/02 15:04:06 toroleo Exp $
 * 
 * TrackingStudent.
 * Classe per mostrar el seguiment de l'estudiant 
 * en l'organització que es consulta. 
 * 
 * @author M.Reyes La salle mtoro@salle.url.edu
 * @version 1.0 20/11/2007 $Revision: 1.7 $ $Date: 2008/04/02 15:04:06 $
 * $Log: TrackingStudent.java,v $
 * Revision 1.7  2008/04/02 15:04:06  toroleo
 * Modificació de les tabules del
 * seguiment per que no apareguin null
 * o Unkown
 *
 * Revision 1.6  2008/02/18 15:01:28  ecespedes
 * Millorant el sistema (7)
 *
 * Revision 1.5  2008/02/14 14:54:33  ecespedes
 * Millorant el sistema (5)
 *
 * Revision 1.4  2008/02/01 18:16:24  toroleo
 * Adaptació a la nova base de dades.
 *
 * Revision 1.3  2008/02/01 03:07:32  ecespedes
 * Versió Beta Final amb la BD Serialitzada.
 *
 * Revision 1.2  2008/01/31 10:10:26  msegarra
 * Actualitzat la classe perquè recuperi l'UserObjective
 * corresponent a l'Agent de l'usuari.
 *
 * Revision 1.1  2007/12/28 14:46:43  toroleo
 * Implementat el seguiment de l'estudiant
 * en l'organització que es consulta.
 *
 *
 */
public class TrackingStudent {
	
	/**
	 * Constructor.
	 *
	 */
	public TrackingStudent() {
		
	}

	/**
	 * Obté el codi html de la taula del seguiment de 
	 * l'estudiant en cadascun dels items que formen
	 * l'organització seleccionada.
	 * 
	 * @param aHMTranslator 	HashMap amb les traduccions dels textos.
	 * @param anIdCourse		Identificador del curs al que pertany 
	 * 							 l'organització.
	 * @param anIdOrganization	Identificador de l'organització de la 
	 * 							 qual es vol mostrar la informació 
	 * 							 dels seus items.
	 * @param anIdStudent		Identificador de l'estudiant del que es
	 * 							 vol mostrar el seguiment de l'organització.
	 * @return	Retorna un string amb el codi html per mostrar la taula del
	 * 			 seguiment de l'alumne per una organització.
	 * @throws DrawHTMLTableException Es llença en cas d'haver algun problema
	 * 								   a l'hora de dibuixar la taula.	
	 */
	public final String draw(final HashMap aHMTranslator,
							 final String anIdCourse,
							 final String anIdOrganization,
							 final String anIdStudent) 
						throws DrawHTMLTableException {

		
		//Aquí es guardarà el codi html de la taula a mostrar per pantalla
		String htmlTable = "";
		
		//es fa la conexió amb la base de dades
		GestorBD gestorBD = GestorBD.getInstance();
		
		
		try {
			
			//es busca el nom de l'organització
			int intIdOrganization = Integer.parseInt(anIdOrganization);
			
			String nameOrganization = 
				gestorBD.getNameOrganizationQuery(
						Long.parseLong(anIdOrganization));
			/*
			UserObjective userObjectiveOrg = CourseAdministrator.
            getInstance().getCourseManagerInstance(1, 
                    Long.parseLong(anIdOrganization)).
                        getUser(anIdStudent);
            String nameOrganization = userObjectiveOrg.organizationName;
			*/
			//es comprova si existeix l'organització
			if (nameOrganization == null) {
				//no exixteix
				
				//S'escriu el missatge d'error al fitxer de logs.
				String message = " Table could not be drawn."
						+ " Organization referenced is not exist.";
				LogControl log = LogControl.getInstance();
				log.writeMessage(Level.SEVERE, TrackingStudent.class.getName(),
								 message);
				
				throw new DrawHTMLTableException(message);				
			}
						
			//Es fa el canvi de l'identificador de l'estudiant del 
			//sistema central a l'identificador al subsistema scorm.
			//int idStudentScorm = gestorBD.getIdStudentScormQuery(anIdStudent);
			
            AgentRequest request = new AgentRequest(); 
            // es retorna l'agent amb les dades de l'usuari
            Agent agent = request.getAgent();
            
			//Es passa a tipus long  l'identificador de l'organització.
			long lgIdOrganization = Long.parseLong(anIdOrganization);
			
			//Es recullen les dades de l'estudiant per l'organització escullida.
			UserObjective userObjective = 
					gestorBD.getUserData(lgIdOrganization, 
                            agent.getId().getIdString());
			
			//Es comprova si s'han trobat i/o recuperat dades del seguiment 
			//de l'usuari per l'organització passada
			if (userObjective == null) {
				//no hi han dades 
				
				//S'escriu el missatge d'error al fitxer de logs.
				String message = " Table could not be drawn."
						+ "  No information found about student"
						+ " monitoring for referenced organization.";

				LogControl log = LogControl.getInstance();
				log.writeMessage(Level.SEVERE, TrackingStudent.class.getName(),
									message);
				
				throw new DrawHTMLTableException(message);	
			}
			
			//Es dibuixa la taula
			 //Es dibuixa la capcelera de la taula
			htmlTable = writeHeadTableStudents(aHMTranslator, nameOrganization);

			
			System.out.println("**********TrackingStudent***************");
			
			
			 //Es tracten les fileres de dades de la taula
			String idFirstItem = 
				userObjective.treeAnnotations.keySet().iterator().next();
			htmlTable =	processTree(userObjective, idFirstItem , 
									"", 1, true, htmlTable, aHMTranslator,
									anIdCourse, anIdStudent, anIdOrganization);
			
			//Es tenca la taula
			htmlTable = htmlTable 
						+ 	"</tbody>"
						+ "</table>";
		} catch (AgentException aex) {
            String message = "AgentException. " + aex.getMessage();
            LogControl log = LogControl.getInstance();
            log.writeMessage(Level.SEVERE, 
                    TrackingStudent.class.getName(), message);
        } catch (RemoteException rex) {
            String message = "RemoteException. " + rex.getMessage();
            LogControl log = LogControl.getInstance();
            log.writeMessage(Level.SEVERE, 
                    TrackingStudent.class.getName(), message);
        } catch (RequestException rqex) {
            String message = "RequestException. " + rqex.getMessage();
            LogControl log = LogControl.getInstance();
            log.writeMessage(Level.SEVERE, 
                    TrackingStudent.class.getName(), message);   
        } catch (SharedException e) {
        	String message = "RequestException. " + e.getMessage();
            LogControl log = LogControl.getInstance();
            log.writeMessage(Level.SEVERE, 
                    TrackingStudent.class.getName(), message); 
		} catch (IllegalArgumentException e) {
			String message = "RequestException. " + e.getMessage();
            LogControl log = LogControl.getInstance();
            log.writeMessage(Level.SEVERE, 
                    TrackingStudent.class.getName(), message); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return htmlTable;
	}
	
	/**
	 * Processa l'informació de cada item, i dels seus fills, i la inserta
	 * a la taula html.
	 * 
	 * @param anUsObj			Informació dels items d'una organització
	 * 							 per un estudiant.
	 * @param anIdItem				Identificador de l'item a tractar.
	 * @param aShowNumeration	Númeració a mostrar davant de l'item, 
	 * 							 menys l'últim dígit.
	 * @param aCount			Últim dígit de la númeració que s'ha de
	 * 							 mostrar davant de l'item.
	 * @param aFirstItem		Per indicar si s'està processant el primer item
	 * 							 o no.
	 * @param aHtmlTable		La taula html de les dades que es porta creada 
	 * 							 fins ara.
	 * @param aHMTranslator 	HashMap amb les traduccions dels textos.
	 * @param anIdCours 		Identificador del curs al que pertany 
	 * 							 l'organització.
	 * @param anIdStudent		Identificador de l'estudiant del que es
	 * 							 vol mostrar el seguiment de l'organització.
	 * @param anIdOrganization	Identificador de l'organització de la 
	 * 							 qual es vol mostrar la informació 
	 * 							 dels seus items.
	 * @return La taula html amb totes les fileres d'informació dels item.
	 */
	private String processTree(final UserObjective anUsObj,
							 final String anIdItem,
							 final String aShowNumeration,
							 final int aCount,
							 final boolean aFirstItem,
							 final String aHtmlTable,
							 final HashMap aHMTranslator,
							 final String anIdCours,
							 final String anIdStudent,
							 final String anIdOrganization) {
        
		String htmlTable = aHtmlTable;
		
		int newCount = aCount; 
        String newShowNumeration = aShowNumeration;
                
        //Es mira si es tracta del primer item
        //ja que aquest no s'ha d'inserir a la taula.
        if (!aFirstItem) { 
        	//no és el primer fill
        	
			//Procés de l'item.
	        newShowNumeration = aShowNumeration + Integer.toString(aCount);
	        
	        htmlTable = processItem(anUsObj, anIdItem, newShowNumeration,
	        				htmlTable, aHMTranslator, anIdCours,
	        				anIdStudent, anIdOrganization);
	        
	        
	        //Preparació de la numeráció pels fills
	        newShowNumeration = "&nbsp;&nbsp;" + newShowNumeration + ".";
	        
        } // del if (!firstItem)
        
        //Procés dels fills de l'item.
        TreeAnnotations tree = anUsObj.treeAnnotations.get(anIdItem);
        newCount = 0;
        
        //es comprova si l'item té fills
        if (tree.sons.size() > 0) {
        	//en té.
        	Iterator iterSons = tree.sons.iterator();
        	
        	//es tracta cada un dels fills de l'item.
        	while (iterSons.hasNext()) {
        		newCount++;
        		String son = (String) iterSons.next();
                htmlTable = processTree(anUsObj, son, 
                		newShowNumeration, newCount, false, htmlTable, 
                		aHMTranslator, anIdCours, anIdStudent,
                		anIdOrganization);
        	}
        }
        
        return htmlTable;
	}
	
	/**
	 * Processa la informació de l'item per ser mostrar a la taula html.
	 * 
	 * @param anUsObj			Informació dels items d'una organització
	 * 							 per un estudiant.
	 * @param anIdItem				Identificador de l'item a tractar.
	 * @param aNumeration		Númeració a mostrar davant de l'item.
	 * @param aHtmlTable		La taula html de les dades que es porta creada 
	 * 							 fins ara.
	 * @param aHMTranslator		HashMap amb les traduccions dels textos.
	 * @param anIdCours 		Identificador del curs al que pertany 
	 * 							 l'organització.
	 * @param anIdStudent		Identificador de l'estudiant del que es
	 * 							 vol mostrar el seguiment de l'organització.
	 * @param anIdOrganization	Identificador de l'organització de la 
	 * 							 qual es vol mostrar la informació 
	 * 							 dels seus items.
	 * @return	 La taula html amb totes les fileres d'informació dels item.
	 */
	private String processItem(final UserObjective anUsObj,
							   final String anIdItem,
							   final String aNumeration,
							   final String aHtmlTable,
							   final HashMap aHMTranslator,
							   final String anIdCours,
							   final String anIdStudent,
							   final String anIdOrganization) {
	
		
		String titleItem = anUsObj.treeAnnotations.get(anIdItem).title;
		if ((titleItem == null) 
			 || ("unknown".equalsIgnoreCase(titleItem))) {
			titleItem = "";
		}
		
		String htmlTable = aHtmlTable;
		String content = aNumeration + ". " + titleItem;
        String visits = "";
        String firstVisit = "";
        String lastVisit = "";
        String totalTime = "";
        String state = "";
        String exit = "";
        String qualification = "";
        /*String qualification = (String) 
		   aHMTranslator.get("tracking.content.table2.unknown");*/
        Iterator<CMIObjectives> itObjectives = null;
      
        //es comprova si l'item té fills
        //perqué si en té, només s'haurà de mostrar el nom d'aquest
        //sino tota la seva informació
        if (anUsObj.treeAnnotations.get(anIdItem).sons.size() <= 0) {
        	//no té fills, és una fulla.
        	
        	CMIDataModel dataModel = anUsObj.dataModel.get(anIdItem);
        	
            //Es comprova si l'item ha estat accedit o no. 
            if (anUsObj.treeAnnotations.get(anIdItem).isAccessed) {
            	//es recull la informació de l'estat, temps...

            	//Format en el que es vol mostrar les dates
        		SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        	      		    		 
    	         //número de visites
        		visits = Integer.toString(dataModel.activityAttemptCount);
            	System.out.println("TrackingStudent. visits d: " + visits);
            	
            	String timeVisit = "";
        		 //primera visita
            	if (dataModel.firstAttemptedTime != null) {
            		firstVisit = sdfDate.format(dataModel.firstAttemptedTime);
            		timeVisit = sdfTime.format(dataModel.firstAttemptedTime);
            		firstVisit = firstVisit + " " + timeVisit;
            	} 
            	
            	 //última visita
            	if (dataModel.lastAttemptedTime != null) {
            		lastVisit = sdfDate.format(dataModel.lastAttemptedTime);
            		timeVisit = sdfTime.format(dataModel.firstAttemptedTime);
            		lastVisit = lastVisit + " " + timeVisit;
            	}
        		System.out.println("TotalTime = " + dataModel.totalTime);		
        		 //temps acumulat
            	if (dataModel.totalTime != null) {
            		try {
            			DatatypeFactory dataFactory = 
            					DatatypeFactory.newInstance();
						Duration duration = 
								dataFactory.newDuration(dataModel.totalTime);
						String hours = Integer.toString(duration.getHours());
						if (hours.length() < 2) {
							hours = "0" + hours;
						}
						String minuts = Integer.toString(duration.getMinutes());
						if (minuts.length() < 2) {
							minuts = "0" + minuts;
						}
						String seconds = 
							Integer.toString(duration.getSeconds());
						if (seconds.length() < 2) {
							seconds = "0" + seconds;
						}
						totalTime =  hours + ":" + minuts + ":"	+ seconds;
					} catch (DatatypeConfigurationException e) {
						//S'escriu el missatge d'error al fitxer de logs.
						String message = "DatatypeConfigurationException. " 
										 + e.getMessage();
						LogControl log = LogControl.getInstance();
						log.writeMessage(Level.WARNING, 
								TrackingStudent.class.getName(), message);
					}
            	} //del if (dataModel.totalTime != null)
            	
            	System.out.println("totalTime procesado = " + totalTime);
            	
        		 //estat
        		state = translateStateExit(aHMTranslator, 
        					dataModel.completionStatus);
        		
        		 //com s'ha passat el curs
        		exit = translateStateExit(aHMTranslator, 
        					dataModel.successStatus);
        		        	
        		 //nota
        		String min = "";
        		String max = "";
        		System.out.println("scoreRaw = " + dataModel.scoreRaw);
        		if ((dataModel.scoreRaw != null) 
        		    && (!"unknown".equalsIgnoreCase(dataModel.scoreRaw))
        		    && (!(dataModel.scoreRaw.length() == 0))) {
    				
        			qualification = dataModel.scoreRaw;
        			
        			System.out.println("dataModel.scoreMin = " + dataModel.scoreMin);
        			if ((dataModel.scoreMin != null) 
        				&& (dataModel.scoreMin.length() > 0)) {
        				min = dataModel.scoreMin;
        			}
        			System.out.println("dataModel.scoreMax = " + dataModel.scoreMax);
        			if ((dataModel.scoreMax != null) 
        				 && (dataModel.scoreMax.length() > 0)) {
        				max = dataModel.scoreMax;
        			}
        			
        			if ((!"".equals(min)) && (!"".equals(max)) 
        				 && (!"".equals(qualification))) {
	        			qualification = min + "&nbsp;&lt;&nbsp;"
	        				+ qualification	+ "&nbsp;&gt;&nbsp; " 
	        				+ max;
        			}
        			
        		} //del if (dataModel.scoreRaw != null) {
        		
        		System.out.println("min = " + min);
        		System.out.println("max = " + max);
        		System.out.println("qualification = " + qualification);
            } 
            
            //es recull la informació dels objectius de l'item
            itObjectives = dataModel.cmiObjectives.values().iterator();
        	
        } //del  if (anUsObj.treeAnnotations.get(anId).sons.size()>0){
        
        htmlTable = writeBodyTableStudents(htmlTable, content, 
        				visits, firstVisit, lastVisit, totalTime, state, exit, 
        				qualification, itObjectives, anIdCours,
        				anIdStudent, anIdOrganization, anIdItem);
        
        //es recullen els objectius de l'item
		
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
		
		if ((aStatus != null) && (!"".equals(aStatus))
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
	 * @param anOrganizationName	Nom de l'organització de la que s'està
	 * 							 ensenyant les dades.
	 * @return Capcelera de la taula d'organització en codi html.
	 */
	private String writeHeadTableStudents(final HashMap aHMTranslator,
										  final String anOrganizationName) {

		String content = "";	
		String visits = "";
		String firstVisit = "";
		String lastVisit = "";
		String time = ""; 
		String state = "";
		String success = "";
		String qualification = "";
		String objectives = ""; 
		String summary = "";		//descripció de la taula
		
	
		//es tradueixen les capceleres a l'idioma que fa servir l'usuari.
		summary = (String)
			aHMTranslator.get("tracking.content.table2.summary");
		
		content = (String)
			aHMTranslator.get("tracking.content.table2.header1");	
		visits = (String)
			aHMTranslator.get("tracking.content.table2.header2");
		firstVisit = (String)
			aHMTranslator.get("tracking.content.table2.header3");
		lastVisit = (String)
			aHMTranslator.get("tracking.content.table2.header4");
		time = (String)
			aHMTranslator.get("tracking.content.table2.header5"); 
		state = (String)
			aHMTranslator.get("tracking.content.table2.header6");
		success = (String)
			aHMTranslator.get("tracking.content.table2.header7");
		qualification = (String)
			aHMTranslator.get("tracking.content.table2.header8");
		objectives = (String)
			aHMTranslator.get("tracking.content.table2.header9");
		
		
		//s'escriu el codi html de la capcelera de la taula
		String htmlHead = "<table class='tracking'"
						+ " summary= ' " + summary + "'>"
        		 + "	<caption>" + anOrganizationName + "</caption>"
        		 + " 	<tbody>" 
				 + "		<tr>"
    			 + "			<th id=\"header1\"> "
    			 				+ content + " </th>"
    			 + "			<th id=\"header2\"> "
    			 				+ visits  + " </th>"
    			 + "			<th id=\"header3\"> "
    			 				+ firstVisit + " </th>" 
    			 + "			<th id=\"header4\"> "
    			 				+ lastVisit + " </th>" 
    			 + "			<th id=\"header5\"> "
    			 				+ time + " </th>"
    			 + "			<th id=\"header6\"> "
    			 				+ state + " </th>"
    			 + "			<th id=\"header7\"> "
     			 				+ success + " </th>"
     			 + "			<th id=\"header8\"> "
     			 				+ qualification + " </th>"
     			 + "			<th id=\"header6\"> "
     			 				+ objectives + " </th>"
    			 + "			</tr>";
		
  
		return htmlHead;
	}

	/**
	 * * Escriu una filera nova de dades a la taula dels items de
	 * l'organització per l'estudiant.
	 * 
	 * @param aHtmlTable		La taula html de les dades que es porta creada 
	 * 							 fins ara.
	 * @param aContent			Titol de l'item.
	 * @param aVisits			Número de visites.
	 * @param aFirstVisit		Primera visita.
	 * @param aLastVisit		Última visita.
	 * @param aTotalTime		Temps total de la duració de les
	 * 							 visites de l'estudiant.
	 * @param aState			Indica si s'ha completat el contingut o no.
	 * @param aExit				Indica si s'ha finalitzat amb èxit el
	 * 							 contingut d'aprenentatge.
	 * @param aQualification	Puntuació de l'estudiant.
	 * @param anItObjectives	Iterador dels Objectius de l'item.
	 * @param anIdCours 		Identificador del curs al que pertany 
	 * 							 l'organització.
	 * @param anIdStudent		Identificador de l'estudiant del que es
	 * 							 vol mostrar el seguiment de l'organització.
	 * @param anIdOrganization	Identificador de l'organització de la 
	 * 							 qual es vol mostrar la informació 
	 * 							 dels seus items.
	 * @return Codi html de com queda la taula després d'inserir la nova linea.
	 */
	private String writeBodyTableStudents(final String aHtmlTable,
										  final String aContent,
										  final String aVisits,
										  final String aFirstVisit, 
										  final String aLastVisit,
										  final String aTotalTime, 
										  final String aState, 
										  final String aExit, 
										  final String aQualification,
								final Iterator<CMIObjectives> anItObjectives,
										  final String anIdCours,
										  final String anIdStudent,
										  final String anIdOrganization,
										  final String anIdItem) { 
		
		

		//s'escriu la linea amb les dades de l'item 
		//per l'estudiant i l'organització passades
		String htmlTable = aHtmlTable + "<tr>"
						   + "<td headers=\"header1\">" + aContent + "</td>"
						   + "<td headers=\"header2\">" + aVisits + "</td>"
						   + "<td headers=\"header3\">" + aFirstVisit + "</td>"
						   + "<td headers=\"header4\">" + aLastVisit + "</td>"
						   + "<td headers=\"header5\">" + aTotalTime + "</td>"
						   + "<td headers=\"header6\">" + aState + "</td>"
						   + "<td headers=\"header7\">" + aExit + "</td>"
						   + "<td headers=\"header8\">" + aQualification 
						   								+ "</td>"
						   + "<td headers=\"header9\">";

				
		//s'introdueixen els diferents objectius.
		 //es comprova si hi han objectius
		if (anItObjectives != null) { 
			while (anItObjectives.hasNext()) {
				//es recupera la informació de l'objectiu 
				//que s'esta processant
				CMIObjectives objective = anItObjectives.next();
				
				System.out.println("Objetivo: " + objective.id + " IdOrganization: " + anIdOrganization);
				//s'escriu l'objectiu a la taula
				htmlTable = htmlTable 
							+ " <a href=\"tracking.jsp?course=" + anIdCours 
								+ "&level=3&org=" + anIdOrganization
								+ "&itm=" + anIdItem 
								+ "&user=" + anIdStudent 
								+ "&obj=" + objective.id + "\">" 
								+ objective.id + "</a><br/>";
							
			}
		}
		htmlTable = htmlTable + "</td></tr>";
		
		return htmlTable;
	}
}
