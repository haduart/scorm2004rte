// $Id: TrackingCourse.java,v 1.5 2008/04/02 15:04:06 toroleo Exp $
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;

import org.campusproject.osid.agent.Agent;
import org.osid.agent.AgentException;

import java.text.SimpleDateFormat;

import edu.url.lasalle.campus.scorm2004rte.osid.requests.AgentRequest;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.GestorBD;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.CourseAdministrator;
import edu.url.lasalle.campus.scorm2004rte.
		util.exception.DrawHTMLTableException;
import edu.url.lasalle.campus.scorm2004rte.util.exception.RequestException;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;


/**
 * $Id: TrackingCourse.java,v 1.5 2008/04/02 15:04:06 toroleo Exp $
 * 
 * TrackingCourse.
 * Classe per mostrar el seguiment dels estudiants 
 * en els cursos. 
 * 
 * @author M.Reyes Enginyeria La Salle mtoro@salle.url.edu
 * @version 1.0 20/11/2007 $Revision: 1.5 $ $Date: 2008/04/02 15:04:06 $
 * $Log: TrackingCourse.java,v $
 * Revision 1.5  2008/04/02 15:04:06  toroleo
 * Modificació de les tabules del
 * seguiment per que no apareguin null
 * o Unkown
 *
 * Revision 1.4  2008/02/01 18:16:24  toroleo
 * Adaptació a la nova base de dades.
 *
 * Revision 1.3  2008/02/01 03:07:32  ecespedes
 * Versió Beta Final amb la BD Serialitzada.
 *
 * Revision 1.2  2008/01/03 15:09:16  toroleo
 *  Ordenació de la taula per mostrar els
 *  alumnes per ordre alfabètic.
 *
 * Revision 1.1  2007/12/28 14:45:05  toroleo
 * Implementat el seguiment dels estudiants
 * en els cursos.
 *
 *
 */
public class TrackingCourse {
	
	//Maiye quitar comentarios
	
	/** Objecte per recuperar les dades de l'usuari. */
	private AgentRequest agentRequest = null;
	
	/**
	 * Constructor.
	 *
	 */
	public TrackingCourse() {
		
	}

	/**
	 * Obté el codi html de la taula dels estudiants que te un curs.
	 *  
	 * @param anIdCourse Identificador del curs del que 
	 * 					es vol llistar tots els alumnes.
	 * @param aHMTranslator HashMap amb les traduccions dels textos.
	 * @return Retorna un string amb el codi html per 
	 * 		   mostrar la taula dels alumnes d'un curs.
	 * @throws DrawHTMLTableException Es llença en cas d'haver algun
	 * 								  problema a l'hora de dibuixar
	 * 								  la taula.
	 */
	public final String draw(final HashMap aHMTranslator,
							 final String anIdCourse) 
						throws DrawHTMLTableException {
		
		//objecte amb les dades de l'estudiant
		//a mostrar a la taula.
		CourseStudent courseStudent = new CourseStudent();
		//Array d'objectes de dades de l'estudiant.
		ArrayList<CourseStudent> arrayCourseStudent = 
							new ArrayList<CourseStudent>();
		//login de l'estudiant
		String strIdStudent = "";
		//Array amb les dades d'una organitzación 
		//(identificador, nom, percentatge de completat). 
		//On a cada posició d'aquest ArrayList és un String[]
		//de la seüent manera:
		//String[Id_organization, title_organization, percentatge_completet]
		ArrayList<String[]> dataOrganization = new ArrayList<String[]>();
		//posició en la que s'inserten els elements en l'array anterior
		int posDataOrganization = 0;
		//Aquí es guardarà el codi html de la taula a mostrar per pantalla
		String htmlTable = "";		
		//indica si hi havia dades i s'ha d'escriure l'última fila de dades
		boolean printLast = false;
		

		//es crea l'objecte de peticio de dades d'agent
		agentRequest = new AgentRequest();
		
		//es fa la conexió amb la base de dades
		GestorBD gestorBD = GestorBD.getInstance();
		
		try {
				
			//es busca el nom del curs
			String nameCourse = 
				gestorBD.getNameCourseQuery(Integer.parseInt(anIdCourse));
			
			//es comprova que existeixi el curs
			if (nameCourse == null) {
				//no exixteix
				
				//S'escriu el missatge d'error al fitxer de logs.
				String message = " Table could not be drawn."
						+ " Course referenced is not exist.";
				LogControl log = LogControl.getInstance();
				log.writeMessage(Level.SEVERE, TrackingCourse.class.getName(),
								 message);
				
				throw new DrawHTMLTableException(message);
			}

			//Es dibuixa la taula
			 //Es dibuixa la capcelera de la taula
			htmlTable = writeHeadTableCourse(aHMTranslator, nameCourse);	
			
			//Es recuperen tots els estudiants que estan apuntats al curs
			ResultSet rs = 
				gestorBD.getCourseQuery(Integer.parseInt(anIdCourse));
							
			 //Es recullen les dades per omplir la taula
			while (rs.next()) {
				printLast = true;
			
				//----------
				//Recuperació del login del estudiant
				strIdStudent = "";
					
				strIdStudent = rs.getString("global_student_id");
				
				//----------
				//es comprova si s'ha canviat d'estudiant a processar
				if (!courseStudent.getIdStudent().equals(strIdStudent)) {
					//són estudiants diferents
					//o és el primer estudiant a processar.

					if (!"".equals(courseStudent.getIdStudent())) {
							//s'ha canviat d'estudiant
						
						//s'escriu la nova linea de la taula
						 //es busca el nom de l'estudiant
						courseStudent.setName(
								recoverName(courseStudent.getIdStudent()));
						
						 //es guarda l'array de dades de les organitzacions
						courseStudent.setDataOrganization(dataOrganization);
						
						 //s'afegeixen les dades de l'estudiant al array
					 	 //d'estudiants del curs.
						arrayCourseStudent.add(courseStudent);
						
						//s'inicialitzen les dades
						courseStudent = null;
						courseStudent = new CourseStudent();
						dataOrganization = null;
						dataOrganization = new ArrayList<String[]>();
						posDataOrganization = 0;
						
					} //del if (!"".equals(lastIdStudent))
					
					//es reinicia la nova linea
					courseStudent.setIdStudent(strIdStudent);
					
					
				} //del if (!lastIdStudent.equals(strIdStudent))
				
				//----------
				//acumulació de les dades a mostrar després per pantalla

				//----------
				//recuperació del número de visites i acumulació.
				courseStudent.setNumVisits(courseStudent.getNumVisits() 
											+ rs.getInt("visits"));
				
				//----------
				//recuperació de les dades de l'organització
				String[] organitzation = new String[3];
				 
				 //identificador de l'organització
				organitzation[0] = 
							rs.getString("organization_id");
				
				 //nom de l'organització
				//organitzation[1] = rs.getString("title");
	            UserObjective userObjective = CourseAdministrator.
                getInstance().getCourseManagerInstance(1, 
                        Long.parseLong(organitzation[0])).
                            getUser(strIdStudent);
	            organitzation[1] = userObjective.organizationName;
				
				 //Estat de completació
				organitzation[2] = 
					Float.toString(rs.getFloat("completed_percent"));
				
				 //s'afegeix l'organització
				dataOrganization.add(organitzation);
				posDataOrganization++;
								
				//----------
				//recuperació de la data i l'hora de l'ultima visita 
				//i el nom de l'últim scorm visitat. 
				//Es guarda la data i hora més recent i del scorm.
				Date date = rs.getDate("last_Access");
				Time time = rs.getTime("last_Access");
				Date lastDateVisit = courseStudent.getLastDateVisit();
				if ((date != null) && ((lastDateVisit == null)
						|| (lastDateVisit.compareTo(date) < 0)
						|| ((lastDateVisit.compareTo(date) == 0)
						&& (courseStudent.getLastTimeVisit().compareTo(time)
							 < 0)))) {
					//si hi ha data de visita de l'estudiant per l'organització
					//i encara no hi ha informació acumulada de l'estudiant
					//o	la nova data de visita es més recent que 
					//la que tenim guardada
					//o les dates son les mateixes però la nova 
					//hora es més recent
					courseStudent.setLastDateVisit(date);
					courseStudent.setLastTimeVisit(time);
					
					courseStudent.setPosOrgScorm(posDataOrganization - 1);
					courseStudent.setNameScorm(rs.getString("last_item_title"));
					}
			} //del while (rs.next())
			
			//s'escriu l'última linea de dades d'estudiant
			if (printLast) {
				
				//es busca el nom de l'estudiant
				courseStudent.setName(
						recoverName(courseStudent.getIdStudent()));
				
				//es guarda l'array de dades de les organitzacions
				courseStudent.setDataOrganization(dataOrganization);
				
				//s'afegeixen les dades de l'estudiant al array
				//d'estudiants del curs.
				arrayCourseStudent.add(courseStudent);
				
				//Maiye arreglar
				htmlTable = writeBodyTableCourse(aHMTranslator, htmlTable, 
											arrayCourseStudent, anIdCourse);
			}
			
			htmlTable = htmlTable 
						+ 	"</tbody>"
						+ "</table>";

		} catch (SQLException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			String message = "SQLException. " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					TrackingCourse.class.getName(), message);
		} catch (Exception e) {
			//S'escriu el missatge d'error al fitxer de logs.
			e.printStackTrace();
			String message = "Exception. " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					TrackingCourse.class.getName(), message);
		}
		
		return htmlTable;
	}
	
	/**
	 * A partir de l'identificador d'usuari es recuperen 
	 * les dades del estudiant.
	 * 
	 * @param anIdStudent Identificador de l'usuari 
	 * 				del que es volen recuperar les dades
	 * @return	Retorna el nom de l'estudiant.
	 */
	private String recoverName(final String anIdStudent) {
		
		String name = "";
		
		Agent dataAgent;
		
		try {
		
			//es comprova si s'ha recuperat un identificador d'usuari
			if (anIdStudent != null) { //s'ha recuperat
							
				//es fa la petició de recuperar les dades del estudiant
				dataAgent = agentRequest.getAgent(anIdStudent);
				
				if (dataAgent != null) {
					//s'ha trobat l'usuari
					
					//es recupera el nom de l'estudiant	
					name = dataAgent.getDisplayName();
				}
				
			} //del if (intIdStudent != 0)

			
		} catch (RemoteException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			String message = "RemoteException. " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.WARNING,
					TrackingCourse.class.getName(), message);
			
		} catch (RequestException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			String message = "RequestException. " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.WARNING, 
					TrackingCourse.class.getName(), message);
			
		} catch (AgentException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			String message = "AgentException. " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.WARNING,
					TrackingCourse.class.getName(), message);
	
		}
		
		return name;
	}
	
	/**
	 * Escriu la capcelera de la taula dels estudiants per curs.
	 * 
	 * @param aHMTranslator HashMap amb les traduccions dels textos.
	 * @param aCourseName	Nom del curs del que s'estan ensenyant les dades.
	 * @return Capcelera de la taula d'estudiants en codi html.
	 */
	private String writeHeadTableCourse(final HashMap aHMTranslator,
										  final String aCourseName) {

		String htmlHead = null;		//codi html de la capcelera de la taula
		String name = "";			//nom de l'alumne
		String login = "";			//login de l'alumne
		String lastVisit = "";		//últim contingut visitat
		String visits = "";			//número de visites que ha fet l'alumne
		String organization = "";	//organitzacions i percentatge de completats
		String summary = "";		//descripció de la taula
		
	
		//es tradueixen les capceleres a l'idioma que fa servir l'usuari.
		summary = (String)
			aHMTranslator.get("tracking.content.table1.summary");
		name = 
		 (String) aHMTranslator.get("tracking.content.table1.header1");
		login = (String) 
			aHMTranslator.get("tracking.content.table1.header2");
		lastVisit = (String)
			aHMTranslator.get("tracking.content.table1.header3");
		visits = (String)
			aHMTranslator.get("tracking.content.table1.header4");
		organization = (String)
			aHMTranslator.get("tracking.content.table1.header5");
		
		//s'escriu el codi html de la capcelera de la taula
		htmlHead = "<table class='tracking'"
						+ " summary= ' " + summary + "'>"
        		 + "	<caption>" + aCourseName + "</caption>"
        		 + " 	<tbody>" 
				 + "		<tr>"
    			 + "			<th id=\"header1\"> "
    			 				+ name + " </th>"
    			 + "			<th id=\"header2\"> "
    			 				+ login  + " </th>"
    			 + "			<th id=\"header3\"> "
    			 				+ lastVisit + " </th>" 
    			 + "			<th id=\"header4\"> "
    			 				+ visits + " </th>" 
    			 + "			<th id=\"header5\"> "
    			 				+ organization + " </th>"
    			 + "			</tr>";
		
  
		return htmlHead;
	}

	/**
	 * Escriu una filera nova de dades a la taula dels estudiants per curs.
	 * 
	 * @param aHMTranslator		Missatges traduits al llenguatge de l'usuari.
	 * @param aHtmlTable		Taula en html que es porta escrita fins ara.
	 * @param aArrCourseStudent	Array amb tots els estudiants del curs i
	 * 							 la informació d'aquests relacionada amb 
	 * 							 el curs.
	 * @param anIdCours			Identificador del curs amb el que s'està
	 * 						 	 treballant
	 * @return	Codi html de com queda la taula després d'inserir la nova linea.
	 */
	private String writeBodyTableCourse(final HashMap aHMTranslator,
						final String aHtmlTable, 
						final ArrayList<CourseStudent> aArrCourseStudent, 
						final String anIdCours) {
		
		String htmlTable = aHtmlTable;
		
		CourseStudent[] allCourseStudent = 
			new CourseStudent[aArrCourseStudent.size()];
		allCourseStudent = aArrCourseStudent.toArray(allCourseStudent);
		
		//s'ordena alfabeticament per nom d'estudiant les dades dels estudiants.
		Arrays.sort(allCourseStudent);
		
		//s'escriu la informació de cada estudiant en un
		//linea de la taula html.
		for (int i = 0; i < allCourseStudent.length; i++) {
		
			String numVisits = 
				Integer.toString(allCourseStudent[i].getNumVisits());
			
			//es recupera el nom de l'organizazió de l'últim scorm 
			//visitat per l'usuari
			String[] organization = allCourseStudent[i].getDataOrganization()
									.get(allCourseStudent[i].getPosOrgScorm());
			String titleOrganization = "";
			if (organization[1] != null) {
				titleOrganization = organization[1];
			}
			
			//es comprova que les dades estiguin instanciades i sino
			//es posen amb valor ""
			String nameScorm = "";
			if (allCourseStudent[i].getNameScorm() != null) {
				nameScorm = allCourseStudent[i].getNameScorm();
			}
			
			String lastDataVisit = "";
			if (allCourseStudent[i].getLastDateVisitAsString() != null) {
				lastDataVisit = allCourseStudent[i].getLastDateVisitAsString();
			}
			
			String lastTimeVisit = "";
			if (allCourseStudent[i].getLastTimeVisitAsString() != null) {
				lastTimeVisit = allCourseStudent[i].getLastTimeVisitAsString();
			}
			
			//literal per escriure "Visites"
			String literalVisits = (String)
				aHMTranslator.get("tracking.content.table1.nvisits"); 
			//literal per escriure "completat"
			String literalCompleted = (String)
				aHMTranslator.get("tracking.content.table1.completed");
			
			//s'escriu la linea amb les dades de l'estudiant pel curs passat
			htmlTable = htmlTable + "<tr>"
						+ "<td headers=\"header1\">" 
							+ allCourseStudent[i].getName() + "</td>"
						+ "<td headers=\"header2\">" 
							+ allCourseStudent[i].getIdStudent() 
							+ "</td>"
						+ "<td headers=\"header3\">" 
							+ nameScorm 
							+ " <br /><span class=\"mintext\">" 
							+ titleOrganization + "</span></td>"
						+ "<td headers=\"header4\">" + numVisits 
							+ " " + literalVisits + " <br/>"
							+ "<span class=\"mintext\">" 
							+ lastDataVisit 
						  	+ "<br/>" 
						  	+ lastTimeVisit
						  	+ "</span></td>"
						+ "<td headers=\"header5\">";


			//s'introdueixen les diferents organitzacions i 
			//percentatge de completat de l'estudiant
			ArrayList<String[]> dataOrganization = 
				allCourseStudent[i].getDataOrganization();
			for (int j = 0; j < dataOrganization.size(); j++) {
				//es recupera la informació de l'organització 
				//que s'esta processant
				organization = dataOrganization.get(j);
				
				String nameOrganization = "";
				if (organization[1] != null) { 
					nameOrganization = organization[1];
				}
				//s'escriu l'organizació a la taula
				htmlTable = htmlTable 
							+ " <a href=\"tracking.jsp?course=" + anIdCours 
								+ "&level=2&org=" + organization[0] 
								+ "&user=" 
								+ allCourseStudent[i].getIdStudent() + "\">" 
								+ nameOrganization + "</a>"
							+ " - <span class=\"italic\">" + organization[2] 
							    + "%  " + literalCompleted + " </span><br/>";
							
			}
			
			htmlTable = htmlTable + "</td></tr>";
		}
		
		return htmlTable;
	}

}
