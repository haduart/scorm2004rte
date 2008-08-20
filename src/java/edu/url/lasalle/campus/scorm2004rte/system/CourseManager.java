// $Id: CourseManager.java,v 1.35 2008/04/22 18:18:07 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.system;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Stack;
import java.util.StringTokenizer;


import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Abstract_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Leaf_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Root_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
	AdlseqControlMode;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
	MetadataInformation;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
	Objective;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
	SequencingRules.ruleActionType;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.ADLNav;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.CMIDataModel;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.CMIObjectives;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.TreeAnnotations;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.Constants.SequencingStatus;


/**
* $Id: CourseManager.java,v 1.35 2008/04/22 18:18:07 ecespedes Exp $
* <b>Títol:</b> CourseManager <br><br>
* <b>Descripció:</b> Aquesta classe gestionarà un curs. S'enten com
* a curs una organització activada. Així el CourseManager serà únic
* per un curs i gestionarà tant el número d'usuaris que hi estan 
* accedint com el desplegament de l'arbre, de manera que si un 
* sheduler l'avisa aquest alliberà la memòria destruint part de 
* l'arbre, i així fins a netejar completament el curs i sol·licitar
* que el propi CourseManager sigui eliminat per l'Administrador 
* de cursos (que és qui porta/gestiona el llistat de CourseManangers
* que hi ha actius en un moment determinat). 
*
* @author Eduard Céspedes i Borràs /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versió $Revision: 1.35 $ $Date: 2008/04/22 18:18:07 $
* $Log: CourseManager.java,v $
* Revision 1.35  2008/04/22 18:18:07  ecespedes
* Arreglat bugs en el seqüenciament i els objectius secundaris mapejats.
*
* Revision 1.34  2008/04/02 14:27:27  ecespedes
* Depurant objectives secundaris
*
* Revision 1.33  2008/03/31 13:56:19  ecespedes
* Depurant objectives secundaris
*
* Revision 1.32  2008/02/28 16:15:23  ecespedes
* Millorant el sistema (8)
*
* Revision 1.31  2008/02/25 09:12:58  ecespedes
* *** empty log message ***
*
* Revision 1.30  2008/02/18 15:50:47  ecespedes
* Implementats Threads per als CourseManagers
*
* Revision 1.29  2008/02/18 15:01:28  ecespedes
* Millorant el sistema (7)
*
* Revision 1.28  2008/02/15 12:02:19  ecespedes
* Millorant el sistema (6)
*
* Revision 1.27  2008/02/14 14:54:33  ecespedes
* Millorant el sistema (5)
*
* Revision 1.26  2008/02/12 15:43:22  ecespedes
* Millorant el sistema (4)
*
* Revision 1.25  2008/01/31 20:18:58  ecespedes
* Millorant el sistema (3)
*
* Revision 1.24  2008/01/31 16:20:29  ecespedes
* Millorant el sistema (2)
*
* Revision 1.23  2008/01/31 14:23:37  ecespedes
* Millorant el sistema.
*
* Revision 1.22  2008/01/30 19:35:08  ecespedes
* BUG Arreglat.
*
* Revision 1.21  2008/01/30 17:41:15  ecespedes
* Versió final: organització  serialitzada.
*
* Revision 1.20  2008/01/29 18:01:41  ecespedes
* Implementant versió serialitzada de l'organització.
*
* Revision 1.19  2008/01/24 15:41:11  ecespedes
* Dissenyant un nou sistema per gestionar les Bases de dades (DataAccess)
*
* Revision 1.18  2008/01/18 18:07:24  ecespedes
* Serialitzades TOTES les classes per tal de que els altres puguin fer proves
* en paral·lel amb el procés de desenvolupament del gestor de BD.
*
* Revision 1.17  2008/01/10 06:58:29  ecespedes
* Últimes modificacions de l'overallSequencingProcess.
*
* Revision 1.16  2008/01/08 17:42:46  ecespedes
* Actualitzant el model de dades a partir del seqüenciament.
*
* Revision 1.15  2008/01/08 11:50:21  ecespedes
* Ultimes modificacions de l'OverallSequencingProcess
*
* Revision 1.14  2008/01/07 15:59:22  ecespedes
* Implementat 'deliveryControls' i el 'hideLMSUI' del Presentation.
*
* Revision 1.13  2008/01/04 11:35:32  ecespedes
* Implementant l'OverallSequencingProcessTest sobre el linear_Choice.
*
* Revision 1.12  2008/01/03 15:05:32  ecespedes
* Arreglats elements del CMIDataModel i començant a crear un nou test.
*
* Revision 1.11  2007/12/28 16:35:27  ecespedes
* Implementat l'OverallSequencingProcess.
*
* Revision 1.10  2007/12/27 15:02:24  ecespedes
* Reunificant totes les crides del seqüenciament.
*
* Revision 1.9  2007/12/21 17:12:44  ecespedes
* Implementant CourseManager.OverallSequencingProcess
*
* Revision 1.8  2007/12/17 15:27:48  ecespedes
* Fent MapInfo. Bug en els Leaf_Items
*
* Revision 1.7  2007/12/10 22:03:32  ecespedes
* Implementant les funcions per buscar un item concret i retornar-lo
* al CourseManager perquè el tracti.
*
* Revision 1.6  2007/11/08 16:33:09  ecespedes
* Començant a Implementar el OverallSequencingProcess, que serà
* les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
* realment el seqüenciament SCORM1.3
*
* Revision 1.5  2007/11/07 13:15:08  ecespedes
* Creat tot el sistema que controla els DataAccess, per tal
* que el CourseAdministrator pugui subministrar-lo quan un
* UserObjective li demani.
*
* Revision 1.4  2007/11/06 16:33:27  ecespedes
* Fets els testos de benchmark per saber la quantitat de memòria
* que necessita el sistema i com respondrien les optimitzacions.
*
* Revision 1.3  2007/11/05 19:42:39  ecespedes
* Començant a implementar el motor del seqüenciament.
* S'aniran fent testos per tal de veure diversos 'request'
* per tal d'emular el que seria un usuari que interactua.
*
* Revision 1.2  2007/10/31 12:57:01  ecespedes
* Començant a crear tot el sistema de gestió dels arbres:
* + Creant l'interfífice DataAccess que ens servirà tan
* 	pel parser com pel SGBD.
* - Falta que el ParserHandler/GestorBD implementi l'interfície.
*
* Revision 1.1  2007/10/30 14:07:42  ecespedes
* Arreglat tots els bugs relacionats amb l'UserObjective.
* Començant a crear el sistema de gestió dels cursos:
* - CourseAdministrator i CourseManager.
*
*/

public class CourseManager extends Thread {
		
	/**
	 * Variable que representa el Thread actual.
	 */
	private volatile Thread blinker;
	
	/**
	 * Hem hagut de sobreescriure el mètode start() del java.lang.Thread per 
	 * arrancar el Thread de forma "segura" i així assegurar-nos que quan 
	 * volguem el podrem parar amb la mateixa seguretat.
	 * 
	 * http://java.sun.com/j2se/1.4.2/docs/
	 * 		guide/misc/threadPrimitiveDeprecation.html
	 */
	public void start() {
        blinker = new Thread(this);
        blinker.start();
        if (Constants.DEBUG_NAVIGATION) {
			System.out.println("[NAVIGATION] CourseManager(" 
					+ getIdentifier() + ").start()");
		}
    }
	/**
	 * Mètode stop inplementat per assegurar que el thread s'atura adequadament
	 * i que funciona. No és pot utilitzar directament el mètode .stop() perquè 
	 * està deprecated. 
	 * 
	 * http://java.sun.com/j2se/1.4.2/docs/
	 * 		guide/misc/threadPrimitiveDeprecation.html
	 *
	 */
    public final void stopCourseManagerThread() {
    	blinker = null; //blinker.stop();  // UNSAFE!
    }    

    /**
     * Mètode modificat del java.util.Observer.update.
     * Farem que retorni un booleà indicant al MemorySheduler si hi ha hagut
     * alguna modificació o no. D'aquesta manera serà ell qui s'encarregarà de
     * llançar el Garbagge Collector.
     * 
     * @see Observer Pattern Design.
     * @param o : Observable Type.
     * @param arg : Object Type.
     * @return boolean : Retornarem true quan volguem que el MemorySheduler
     * que ens elimini de la seva "llista".
     */
	public final boolean update(final Observable o, final Object arg) {
	
		System.out.println("[CourseManager]" + getId() + "-" + getIdentifier());
		System.out.println("\tNumberUsers: " + userObjectivesMap.size());
		System.out.println("\tMetadata?: " + (metadata != null));
		System.out.println("\tActivityTree?: " + (organizationCluster != null));
		System.out.println("\tlastTimeAccess?: " + lastTimeAcces);
					
		
		/***/
		System.out.println("\tCalendar lastTimeAccess: " 
				+ lastTimeAcces.getTime());
		Calendar y = new GregorianCalendar();
				
		System.out.println("\tCalendar Ara: " + y.getTime());
		System.out.println("\tCalendar Before?: " + y.before(lastTimeAcces));
				
		Calendar cmpLast = new GregorianCalendar();
		cmpLast.setTime(lastTimeAcces.getTime());
		cmpLast.add(Calendar.SECOND, 30);
		
		System.out.println("\tCalendar lastTimeAccess: " 
				+ lastTimeAcces.getTime());
		System.out.println("\tCalendar cmpLast en dos minuts: " 
				+ cmpLast.getTime());
		System.out.println("\tCalendar dos minuts, Before?: " 
				+ cmpLast.before(y));
		
		if (cmpLast.before(y)				
				&& (organizationCluster != null)) {			
			removeAllTreeOrganization();
			System.out.println("Netejat el TreeOrganizations.");
			MemorySheduler.getInstance().activeGarbageCollector();
			return false;
		}
		
		if (cmpLast.before(y)				
				&& (metadata != null)) {			
			removeMetadata();
			System.out.println("Netejat el metadata.");
			MemorySheduler.getInstance().activeGarbageCollector();
			return false;
		}
		
		if (cmpLast.before(y)) {			
			removeAllTreeOrganization();
			System.out.println("Eliminant CourseManager()");
			/**
			boolean bolea = CourseAdministrator.getInstance().
				removeCourseManagerInstance(
						dataAccess.getDataAccessID(), getIdentifier());
			
			System.out.println("removeCourseManagerInstance->" + bolea);
			**/
			MemorySheduler.getInstance().activeGarbageCollector();
			return true;
		}
		
		/**
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		String DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss z";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String myGMTDate= sdf.format(cal.getTime());
		 */
		
		return false;
		
	}

	/**
	 * Contindrà l'objecte que ens proporcionarà les dades quan 
	 * haguem de fer un 'load'.
	 */
	public DataAccess dataAccess = null;
	
	/**
	 * Aquest identificador serà exactament l'identificador
	 * de l'organització a la Base de dades (o un altre DataAccess):
	 * (Primary Key: [integer Type] organization_id). 
	 * Així d'aquesta manera ens assegurarem que l'identificador sigui
	 * únic. 
	 * 
	 * (int Type, default 0)
	 */
	private long identifier = 0;
	/**
	 * D'aquest node penjarà tot el curs.
	 */
	private Root_Item organizationCluster = null;
	/**
	 * Ens dirà el nombre d'usuaris actius que estan treballant
	 * sobre l'arbre.
	 *
	private int currentNumberUsers = 0;
	*/
	/**
	 * TimeStamp de l'última petició que hem rebut.
	 * Ens serà útil a l'hora d'optimitzar la memòria i 
	 * podar l'arbre. 
	 */
	private Calendar lastTimeAcces = new GregorianCalendar();
	//private Date lastTimeAcces = new Date();
	/**
	 * La metadata del curs estarà enmagatzemada aquí. 
	 */
	private MetadataInformation metadata = null;

	/**
	 * És la variable que instanciarà l'objecte de CourseAdministrator.
	 */
	private static CourseAdministrator courseAdministrator = null;
		
	/**
	 * Serà el llistat de CourseManagers que gestiornarà aquest 
	 * administrador. 
	 */
	private Map < String, UserObjective > userObjectivesMap = null;
	/**
	 * Marcarem quin és l'últim Item, d'aquesta manera no l'haurem d'anar
	 * calculant cada vegada que fem l'OverallSequencingProcess per cada 
	 * usuari.
	 */
	private String lastItem = null;
	
	//------------------  Mètodes i Funcions ------------------	
	
	/**
	 * Assigna l'organització que tractarà el CourseManagement.
	 * Quan ens podem trobar amb organizationCluster == null? 
	 * Doncs quan estiguem creant el CourseManagement per primer cop
	 * (si hem utilitzat el constructor per defecte) o si fa molt
	 * de temps que resideix en memòria, tant que l'optimitzador 
	 * (el sheduler) ha anat eliminant l'arbre de la memòria fins 
	 * al punt que ja no en tenim (El següent pas ja és eliminar
	 * el propi CourseManager).
	 * 
	 * @param ourOrganization : Abstract_Item Type 
	 */
	public final void setOrganization(final Abstract_Item ourOrganization) {
		organizationCluster = (Root_Item) ourOrganization;
	}
	/**/
			
	/**
	 * Constructor on li passem el nou DataAcces des d'on agafarem 
	 * les dades.
	 * 
	 *  @param newDataAcces : DataAcces Type
	 *  @param newIdentifier : L'identificador dintre del DataAcces.
	 */
	public CourseManager(
			final DataAccess newDataAcces, final long newIdentifier) {
		userObjectivesMap =
			Collections.synchronizedMap(
					new HashMap < String, UserObjective > ());
		dataAccess = newDataAcces;
		identifier = newIdentifier;
	}
	
	/**
	 * Incrementem el nombre d'usuaris que estan accedint a 
	 * l'organització.
	 * @return currentNumberUsers : int Type.
	 *
	public final int increaseUsers() {
		return currentNumberUsers++;
	}
	*/
	/**
	 * Decrementem el nombre d'usuaris que estan accedint a 
	 * l'organització.
	 * @return currentNumberUsers : int Type.
	 *
	public final int decreaseUsers() {
		return currentNumberUsers--;
	}
	*/
	/**
	 * Retorna el número d'usuaris que hi ha actualment treballant
	 * amb el CourseManager.
	 * 
	 * @return int Type: currentNumberUsers
	 */
	public final int getCurrentNumberUsers() {
		return userObjectivesMap.size();
	}
	/**
	 * Actualitzem la variable 'lastTimeAcces'.
	 * 
	 * @return Date : lastTimeAcces.
	 */
	public final Calendar updateLastTimeAcces() {
		lastTimeAcces = new GregorianCalendar();
		return lastTimeAcces;
	}
	/**
	 * Ens retorna la data de l'últim accés de
	 * l'últim usuari.
	 * 
	 * @return Date : lastTimeAcces.
	 */
	public final Calendar returnLastTimeAcces() {
		return lastTimeAcces;
	}
	/**
	 * Netegem la informació sobre el metadata.
	 *
	 */
	public final void removeMetadata() {
		metadata  = null;
		/**
		 * Cridem al garbage collector.
		 */
		//System.gc();
	}
	/**
	 * Si per alguna qüestió hem d'eliminar tota l'estructura.
	 * Farem una crida al garbage collector perquè netegi una mica.
	 *
	 */
	public final void removeAllTreeOrganization() {
		organizationCluster = null;
		/**
		 * Cridem al garbage collector.
		 */
		//System.gc();
	}
	/**
	 * Ens retorna el node principal de l'organització...
	 * 
	 * @return Abstract_Item : organizationCluster
	 */
	public final Abstract_Item getOrganizationCluster() {
		if (organizationCluster == null) {
			organizationCluster = dataAccess.getFirstOrganization(identifier);
		}
		return organizationCluster;
	}
	/**
	 * D'aquesta manera calcularem quin és l'últim item del curs i ens 
	 * servirà per modificar el comportament del ADLNav segons això.
	 * Nota! Si és l'últim item tindrem el botó de "Next" activat i
	 *  el ValidRequestContinue == true.
	 *  
	 * @return String : L'identificador de l'últim item.
	 */
	public final String getLastItem() {
		if (lastItem == null) {
			for (Iterator nouIterator = 
				getUserDefault().treeAnnotations.keySet().iterator();
				nouIterator.hasNext();) {
				lastItem = (String) nouIterator.next();
			}
		}
		return lastItem;
	}
	/**
	 * Ens retorna la metadata.
	 * 
	 * @return MetadataInformation : metadata
	 */
	public final MetadataInformation getMetadata() {
		if (metadata == null) {
			metadata = dataAccess.getMetadata(identifier);
		}
		return metadata;
	}	
	/**
	 * Retorna l'identificador del curs.
	 * 
	 * @return int : identifier
	 */
	public final long getIdentifier() {
		return identifier;
	}
	/**
	 * Ens retorna l'estructura per defecte d'un usuari per la 
	 * organització.
	 * 
	 * @return UserObjective Type.
	 */
	public final UserObjective getUserDefault() {
		UserObjective myUserObjective = null;
		myUserObjective = userObjectivesMap.get("DefaultUSER");
		
		if (myUserObjective == null) {
			myUserObjective = dataAccess.getUserDefaultData(identifier);
			if (myUserObjective != null) {
				myUserObjective.adlnav = new ADLNav();
				userObjectivesMap.put("DefaultUSER", myUserObjective);
				System.out.println("[CourseManager]getUserDefault()->" 
						+ " CREATED!");
			} else {
				System.out.println("[CourseManager]getUserDefault()->" 
						+ " ERROR!");				
			}
			
		} else {
			System.out.println("[CourseManager]getUserDefault()->" 
					+ " RECOVERED!!!!!!!!!!!!!");
		} //DEL if (myUserObjective == null)
		return myUserObjective;
	}
	/**
	 * Ens retorna l'estructura per defecte d'un usuari per la 
	 * organització.
	 * 
	 * @param idUser : Identificador concret de l'usuari. 
	 * @return UserObjective Type.
	 */
	public final UserObjective getUser(final String idUser) {
		UserObjective myUserObjective = null;
		myUserObjective = userObjectivesMap.get(idUser);
		
		if (myUserObjective == null) {
			myUserObjective = dataAccess.getUserData(identifier, idUser);
			if (myUserObjective != null) {
				myUserObjective.adlnav = new ADLNav();
								
				/**
				 * Fem aquest trapi de passar-li un request 
				 * inventat perquè l'overallSequencing se 
				 * n'adoni de que és el primer cop i que per tant
				 * no és tracta d'un error. 
				 * El que busquem és que ens generi els requestValids
				 * tant pels botons exit, continue, previous i pel choice.
				 * 
				 */
				myUserObjective.adlnav.request = "FirstTIME";
				String currentItem = myUserObjective.currentItem;
				String currentHref = myUserObjective.currentHref;
				
				overallSequencingProcess(myUserObjective);
				
				myUserObjective.adlnav.request = "_none_";
				myUserObjective.currentItem = currentItem;
				myUserObjective.currentHref = currentHref;
				
						
				System.out.println("[CourseManager]getUser(" 
						+ idUser + ")->"
						+ " CREATED!");
				/**/
				myUserObjective.userOSIDIdentifier = idUser;
				userObjectivesMap.put(idUser, myUserObjective);
			} else {
				System.out.println("[CourseManager]getUser(" 
						+ idUser + ")->"
						+ " NOTFOUND!");
				myUserObjective = getUserDefault();
				
				/**
				 * Fem aquest trapi de passar-li un request 
				 * inventat perquè l'overallSequencing se 
				 * n'adoni de que és el primer cop i que per tant
				 * no és tracta d'un error. 
				 * El que busquem és que ens generi els requestValids
				 * tant pels botons exit, continue, previous i pel choice.
				 * 
				 */
				myUserObjective.adlnav.request = "FirstTIME";
				String currentItem = myUserObjective.currentItem;
				String currentHref = myUserObjective.currentHref;
				
				overallSequencingProcess(myUserObjective);
				
				myUserObjective.adlnav.request = "_none_";
				myUserObjective.currentItem = currentItem;
				myUserObjective.currentHref = currentHref;
				myUserObjective.userOSIDIdentifier = idUser;
				userObjectivesMap.put(idUser, myUserObjective);
				
			} //DEL if (myUserObjective != null)
		} else {
			System.out.println("[CourseManager]getUser(" 
					+ idUser + ")->" 
					+ " RECOVERED!");
		} // DEL if (myUserObjective == null)
		System.out.println("[CourseManager]currentHref=" 
				+ myUserObjective.currentHref);
		return myUserObjective;
	}
	/**
	 * Aquest mètode serà el més important ja que serà el que
	 * tractarà el ADLNav, analitzarà el seqüenciament, actualitzarà
	 * els valors del UserObjective i retornarà un ADLNav amb el 
	 * currentItem correcte.
	 * 
	 * El valor per retorn serà un string que serà justament el 
	 * file que s'haurà de llançar.
	 * 
	 * @param learner : UserObjective Type
	 * @return UserObjective :
	 */
	public final String overallSequencingProcess(
			final UserObjective learner) {
		/**
		 * PAS 0: Actualitzem el temps que ens indica l'últim accés
		 * 	d'un usuari (important per les optimitzacions de memoria).
		 */
		System.out.println("overallSequencingProcess(UserObjective "
				+ learner.userIdentifier + " - " 
				+ learner.userOSIDIdentifier + ")");
		
		updateLastTimeAcces();
						
		Leaf_Item nouItem = null;
		
		Leaf_Item lItem = null;
		
		if (learner.currentItem == null) {	
			//increaseUsers();
			if (learner.adlnav == null) {
				learner.adlnav = new ADLNav();
			}
			
			nouItem = 
				((Root_Item) getOrganizationCluster()).firstLeafItem(learner);
		} else {
			
			/**
			 * PAS 0.1: Comprovem que no hagi intentat accedir a una posició
			 * que no tocava!
			 * Example: requestValidContinue == "false" però ens envia un
			 * request = "Continue".
			 * En aquests casos escriurem en el lmsException del UserObjective
			 * l'excepció que ens hem trobat i després retornarem altre cop 
			 * la mateixa direcció actual (UserObjective.currentHref).
			 */
			if (learner.adlnav.request.equals("continue") 
					&& learner.adlnav.requestValidContinue.equals("false")) {
				learner.adlnav.lmsException =
					"[Error] adl.nav.request_valid.continue = 'false'!!";
				return learner.currentHref;
				
			} else if (learner.adlnav.request.equals("previous") 
					&& learner.adlnav.requestValidPrevious.equals("false")) {
				learner.adlnav.lmsException =
					"[Error] adl.nav.request_valid.previous = 'false'!!";
				return learner.currentHref;
			}
			
			/**
			 * PAS 0.2: Inicialitzem les variables dels "botons" en un estat
			 * consistent, ja que aquestes variables no és modificaran a menys
			 * que hi hagi algun error o el seqüenciament ens forci a fer-ho.
			 */
			learner.adlnav.choiceContinue = true;
			learner.adlnav.choicePrevious = true;
			learner.adlnav.choiceExit = true;
			learner.adlnav.showTreeChoice = true;
			learner.adlnav.requestValidContinue = "true";
			learner.adlnav.requestValidPrevious = "true";
			learner.adlnav.lmsException = "";
			
			/**
			 * PAS 1: Descobrir el camí òptim.
			 */
			Stack < String > route = 
				searchRouteMap(learner, learner.currentItem);
			if (route == null) {
				if (Constants.DEBUG_ERRORS) {
					System.out.println("\t[ERROR-overallSequencingProcess]" 
							+ "[CouseManager] -> searchRouteMap -> NULL");
				}
			} 	
			/**
			 * PAS 2: "Accedir" a l'Item amb el camí descobert.
			 */
			lItem =  searchMyItem(learner, route, false);
			if (lItem == null) {
				if (Constants.DEBUG_ERRORS) {
					System.out.println("\t[ERROR-overallSequencingProcess]" 
							+ "[CouseManager] -> searchMyItem -> NULL");
				}
			} else {
				if (Constants.DEBUG_NAVIGATION) {
					System.out.println("\t[overallSequencingProcess] "
							+ "[CouseManager] -> searchMyItem -> (SCO? " 
							+ lItem.getIsSCO() + ") "
							+ lItem.getIdentifier() + ", Title = '"
							+ lItem.getTitle() + "';");				
				}
			}
			/**
			 * TODO: PAS 3: Actualitzar les dades: CMIDataModel (si 
			 * 		hasMapInfo actualitzarem el GlobalObjective).
			 */
			CMIDataModel question = learner.dataModel.get(learner.currentItem);
			if (question.activityAttemptCount == 0) {
				question.firstAttemptedTime = new Date();
			}
			question.lastAttemptedTime = new Date();
			question.activityAttemptCount++; 
			learner.treeAnnotations.get(learner.currentItem).isAccessed = true;
			/**
			 * PAS 3.1 Evaluar el Completion Status a partir del Completion
			 * Threshold i del Progress Measure.
			 * 
			 * @see SCORM® 2004 Run-Time Environment (RTE) Version 1.3.2 - DRAFT
			 * 		->	4.2.4.1 Completion Status Evaluation 
			 */
			//if (lItem.getIsSCO()) {
			if (learner.dataModel.get(lItem.getIdentifier()).
					completionThreshold.length() > 0) {
				if (learner.dataModel.get(lItem.getIdentifier()).
						progressMeasure.length() == 0) {
					
					learner.dataModel.get(lItem.getIdentifier()).
					completionStatus = "unknown";
				} else {
					Integer completionThresh = new Integer(
							learner.dataModel.get(lItem.getIdentifier()).
							completionThreshold);
					Integer completionStatus = new Integer(
							learner.dataModel.get(lItem.getIdentifier()).
							completionStatus);
					if (completionStatus >= completionThresh) {
						learner.dataModel.get(lItem.getIdentifier()).
						completionStatus = "completed";
					} else {
						learner.dataModel.get(lItem.getIdentifier()).
						completionStatus = "incomplete";
					} //DEL if (completionStatus >= completionThresh)
				} //DEL if (learner ... progressMeasure.length() == 0)
			} //DEL if (learner. ... completionThreshold.length() > 0)
			/**
			 * PAS 3.1.1 Aquest pas és només per tindre el "total completat"
			 * del curs. Per això juguem amb la variable boleana 
			 * currentIsCompleted i amb el contador reamainInComplete.
			 */
			if (!learner.currentIsCompleted) {
				if (testCompletedItem(learner, lItem)) {
					learner.currentIsCompleted = true;
					learner.remainInComplete = learner.remainInComplete - 1;
					learner.dataModel.get(lItem.getIdentifier()).mode =
						"review";
				} 
			} //DEL if (!learner.currentIsCompleted)
			/**
			 * PAS 3.2: Ajustar el cmi.score.scaled i el cmi.score.raw
			 * perquè tots dos tinguin valors equivalents.
			 * Recordar que el cmi.score.raw està entre cmi.score.min 
			 * i cmi.score.max i que el cmi.score.scaled està entre -1 i 1. 
			 */			
			if (!learner.dataModel.get(lItem.getIdentifier())
					.scoreScaled.equals("unknown")
					&& !(learner.dataModel.get(lItem.getIdentifier())
					.scoreScaled.length() == 0)) {
				
				Double newDouble = 
					new Double(learner.dataModel.get(lItem.getIdentifier())
							.scoreScaled);
				if (learner.dataModel.get(lItem.getIdentifier())
						.scoreRaw.equals("unknown") 
						|| learner.dataModel.get(lItem.getIdentifier())
						.scoreRaw.length() == 0) {
					/**
					 * Normalitzarem entre 0 i 10
					 */
					if (newDouble <= 0) {

						newDouble = 5 - (((-1) * newDouble) * 5);
					} else {
						newDouble = 5 + ((newDouble) * 5);
					}
					learner.dataModel.get(lItem.getIdentifier()).scoreMin =
						"0";
					learner.dataModel.get(lItem.getIdentifier()).scoreRaw =
						newDouble.toString();
					learner.dataModel.get(lItem.getIdentifier()).scoreMax =
						"10";
				} //DEL if (learner. ... .scoreRaw.equals("unknown"))
			} else if (!learner.dataModel.get(lItem.getIdentifier())
					.scoreRaw.equals("unknown")
					&& !(learner.dataModel.get(lItem.getIdentifier())
							.scoreRaw.length() == 0)) {
				Double scoreMin;
				if (learner.dataModel.get(lItem.getIdentifier())
						.scoreMin.length() > 0 
						&& !(learner.dataModel.get(lItem.getIdentifier())
								.scoreMin.equals("unknown"))) {
					scoreMin = new Double(
							learner.dataModel.get(lItem.getIdentifier())
							.scoreMin);
				} else {
					scoreMin = new Double(0.0);
				} //DEL if (...dataModel.get(..)...scoreMin.length() > 0 
				
				/*				
				Double scoreMax = new Double(
						learner.dataModel.get(lItem.getIdentifier())
						.scoreMax);
				*/
				Double scoreMax;
				if (learner.dataModel.get(lItem.getIdentifier())
						.scoreMax.length() > 0 
						&& !(learner.dataModel.get(lItem.getIdentifier())
								.scoreMax.equals("unknown"))) {
					scoreMax = new Double(
							learner.dataModel.get(lItem.getIdentifier())
							.scoreMax);					
				} else {
					scoreMax = new Double(0.0);
				} //DEL if (...dataModel.get(..)...scoreMin.length() > 0 
				
				Double scoreRaw = new Double(
						learner.dataModel.get(lItem.getIdentifier())
						.scoreRaw);
				
				Double rangeValue = scoreMax - scoreMin;
				
				Double scaledValue;
				if (rangeValue != 0) {
					scaledValue =  ((scoreRaw / rangeValue) * 2) - 1;
				} else {
					scaledValue =  0.0;
				}
				
				learner.dataModel.get(lItem.getIdentifier()).scoreScaled =
					scaledValue.toString();
			} //DEL (!learner. ... .scoreScaled.equals("unknown"))
			/**
			 * PAS 3.3 Evaluar el cmi.success_status a partir del
			 * cmi.scaled_passing_score i del cmi.scaled_score.
			 * 
			 * @see SCORM® 2004 Run-Time Environment (RTE) Version 1.3.2 - DRAFT
			 * 		->	4.2.22.1  Success Status Evaluation  
			 */
			if (!learner.dataModel.get(lItem.getIdentifier())
					.scaledPassingScore.equals("")) {
								
				if (learner.dataModel.get(
						lItem.getIdentifier()).scoreScaled.equals("unknown")) {
					learner.dataModel.get(
							lItem.getIdentifier()).successStatus = "unknown";
				} else {
					Double valorSPS = new Double(
							learner.dataModel.get(
									lItem.getIdentifier()).scaledPassingScore);
					Double valorScoreS = new Double(
							learner.dataModel.get(
									lItem.getIdentifier()).scoreScaled);
					if (valorScoreS < valorSPS) {
						learner.dataModel.get(
								lItem.getIdentifier()).successStatus = "failed";
					} else {
						learner.dataModel.get(
								lItem.getIdentifier()).successStatus = "passed";
					} //DEL if (valorScoreS < valorSPS)
				} //DEL if (!learner... .scoreScaled.equals(""))
			} //DEL if (!learner... .scaledPassingScore.equals(""))
			/**
			 * PAS 3.4 Sumem el sessionTime en el totalTime.
			 * cmi.session_time i cmi.total_time. 
			 */
			try {
				System.out.println("[CourseManager]sessionTime->" 
						+ learner.dataModel.get(
								lItem.getIdentifier()).sessionTime);
								
				Duration sessionTime =
					DatatypeFactory.newInstance().newDuration(
							learner.dataModel.get(
									lItem.getIdentifier()).sessionTime);
				Duration totalTime =
					DatatypeFactory.newInstance().newDuration(
							learner.dataModel.get(
									lItem.getIdentifier()).totalTime);
				totalTime = totalTime.add(sessionTime);
								
				learner.dataModel.get(
						lItem.getIdentifier()).totalTime = totalTime.toString();
				
				System.out.println("[CourseManager]totalTime->" 
						+ learner.dataModel.get(
								lItem.getIdentifier()).totalTime);
				
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			
			//} // DEL if (lItem.getIsSCO)
			
			/**
			 * TODO: Afegi lo de l'actualitzaciós dels objectius secundaris!
			 *
			 * Fataria actualitzar el StudentsClass.last_item_title? 
			 * (mirar que no estigui fet)
			 */
			updateCMIObjectives(learner, lItem.getIdentifier());
			/**
			 * PAS 4: Actualitzar els ClusterMaps del pare->avi->...->
			 * 		organització de tal manera que només s'actualitzi
			 * 		per la branca que hem accedit.
			 */
			lItem.overallRollupProcess(learner);
			/**
			 * nouItem serà el Item que mostrarem a l'usuari després
			 * d'analitzar el seqüenciament.
			 */
			nouItem = null;
			/**
			 * PAS 5: Comprovar si té PostCondition o exitCondition.
			 * 
			 * Nota! En cas que la postCondició doni un "Exit" 
			 * aleshores acabarem de processar l'overallSequencingProces
			 * i al final (abans de fer el return) ho guardarem tot a la
			 * BD.
			 */
			nouItem = (Leaf_Item) testPostCondition(learner, lItem);
			
			if (nouItem == null) {
				if (Constants.DEBUG_NAVIGATION) {
					System.out.println("\t[overallSequencingProcess]" 
							+ "[CouseManager]"
							+ " -> postConditionRule -> NULL");
				}
				/**
				 * PAS 5.2: Si no té PostCondició mirarem el ADLNav a veuré
				 * que hi tenim.
				 * 			 
				 * (request(continue, previous, choice, exit, exitAll, abandon,
				 * 		abandonAll, suspendAll, _none_)
				 */
				if (learner.adlnav.request.equals("continue")) {
					nouItem =
						lItem.nextItem(learner, lItem.getIdentifier());
				} else if (learner.adlnav.request.equals("previous")) {
					nouItem =
						lItem.previousItem(learner, lItem.getIdentifier());
				} else if (learner.adlnav.request.equals("exit") 
						|| learner.adlnav.request.equals("exitAll")
						|| learner.adlnav.request.equals("abandon")
						|| learner.adlnav.request.equals("abandonAll")
						|| learner.adlnav.request.equals("suspendAll")) {
					/**
					 * En cas de que ens arribi un exit només hem de guardar-ho
					 * tot i retornar null, perquè no val la pena continuar 
					 * analitzant el "següent pas" si no n'hi ha cap.
					 */
					finalizeCourse(learner, lItem.getTitle());
					return null;
				} else if (learner.adlnav.request.equals("_none_")) {
					learner.adlnav.lmsException = "Hem rebut un _none_ " 
						+ "com a request!";
	            		
				} else {
					/**
					 * Mirarem si és un choice.
					 * "{target=intro}choice"
					 * si tot va bé al final tindrem en el idNewItem 
					 * l'identificador del choice que ha fet l'usuari.
					 */
					nouItem = testRequestChoice(learner);
					
				} //DEL if (learner.adlnav.request.equals("continue"))
			} //DEL if (nouItem == null)
		} //DEL if (learner.currentItem == null)
		
		/**
		 * No hi ha cap request que es digui "FirstTIME". 
		 * És una nota que em deixo per aconseguir que 
		 * la funció overallsequencingProcess em generi el 
		 * ADLNav correcte quan re-arranquem després d'haver
		 * suspes un UserObjective. 
		 * 
		 * (Nota: Recordar que el ADLNav no és serialitzable 
		 * i per tant no el guardem.)
		 */
		if (learner.adlnav.request.equals("FirstTIME") 
				&& learner.currentItem != null) {
			System.out.println("-[FirstTIME]-");
			nouItem = lItem;
		}
		if (nouItem == null) {
			/**
			 * TODO: Teòricament aquest cas NOMÉS s'hauria de donar
			 * quan l'usuari ha acabat el curs!
			 */			
			learner.currentHref = null;
			
			System.out.println("[CourseManager][OverallSequencingProcess]" 
					+ "learner.currentHref=" + learner.currentHref);
			finalizeCourse(learner, null);
						
			learner.adlnav.choiceExit = true;
			learner.adlnav.showTreeChoice = true;
			
			
			return null;
			
		} else {			
			AdlseqControlMode currentControlMode = null;			
			boolean hasSequencing = true;
			if (nouItem.getSequencing() == null) {
				if (nouItem.father != null) {
					if (nouItem.father.getSequencing() == null) {
						hasSequencing = false;
					} else {
						currentControlMode = 
							nouItem.father.getSequencing().getControlMode();
					}
				} else {
					hasSequencing = false;					
				} //DEL if (nouItem.father != null)
				if (!hasSequencing) {
					currentControlMode = new AdlseqControlMode();
				}
			} else {
				currentControlMode = 
					nouItem.getSequencing().getControlMode();
			} //DEL if (nouItem.getSequencing() == null)
			
			if (currentControlMode.forwardOnly) {
				learner.adlnav.choicePrevious = false;
				learner.adlnav.requestValidPrevious = "false";
			} else {
				/**
				 * És pot fer un previousItem?
				 */
				Leaf_Item hasPrevious =
					nouItem.previousItem(learner, nouItem.getIdentifier());
				if (hasPrevious == null) {
					learner.adlnav.choicePrevious = false;
					learner.adlnav.requestValidPrevious = "false";
				}
			} //DEL if (currentControlMode.forwardOnly)
			/**
			 * S'ha de mostrar l'arbre?
			 */
			learner.adlnav.showTreeChoice =
				currentControlMode.choice;	
			/**
			 * S'ha de mostrar el botó "Exit" ?
			 */
			learner.adlnav.choiceExit =
				currentControlMode.choiceExit;
			
			/**
			 * És pot fer un nextItem?
			 */
			Leaf_Item hasNext =
				nouItem.nextItem(learner, nouItem.getIdentifier());
			if (hasNext == null) {			
				learner.adlnav.choiceContinue = false;
				learner.adlnav.requestValidContinue = "false";
			}			
			if (Constants.DEBUG_NAVIGATION) {
				System.out.println("\tEl nou item serà: " 
						+ nouItem.getIdentifier());				
			}
			/**
			 * Controlar si l'item que estem llançant és realment l'últim.
			 * De ser així el ValidRequestContinue i el Botó de Continue estaran
			 * activats digui el que digui el seqüenciament o el ControlMode.
			 */
			if (nouItem.getIdentifier().equals(getLastItem())) {
				if (learner.currentHref != null) {
					learner.adlnav.choiceContinue = true;
					learner.adlnav.requestValidContinue = "true";
				} else {
					learner.adlnav.choiceExit = true;
					learner.adlnav.showTreeChoice = true;
				}
			} //DEL if (nouItem.getIdentifier().equals(getLastItem()))
			
			//TODO: OJO!! -----------------------------------------------------
			/**
			 * Mirarem com té el scaledPassingScore.
			 * Si no en té mirarem si el MinNormalizedMeasure està definit
			 * i en cas d'estar-ho inicialitzariem el scaledPassingScore
			 * amb aquest valor.
			 */
			if (nouItem.getSequencing() != null) {
				if (learner.dataModel.get(nouItem.getIdentifier()).
						scaledPassingScore.equals("")) {
					if (nouItem.getMinNormalizedMeasure() != null) {
						learner.dataModel.get(nouItem.getIdentifier()).
							scaledPassingScore =
								nouItem.getMinNormalizedMeasure().toString();
					}
				} //DEL if (learner...	scaledPassingScore.equals(""))
				
				/**
				 * Analitzarem el/s MapInfo/s i en cas de que n'hi hagi
				 * algun que estigui com a Read aleshores sobre-escriurem el 
				 * valor que toqui en el model de dades. 
				 */
				if (((Objective) nouItem.getSequencing().
						getObjectiveHandler().
						getPrimaryObjective()).hasMapInfo) {
					
					String objectID = ((Objective) nouItem.getSequencing().
							getObjectiveHandler().getPrimaryObjective()).
							ObjectiveID;
					
					if (((Objective) nouItem.getSequencing().
							getObjectiveHandler().getPrimaryObjective()).
							mapInfo.readNormalizedMeasure) {
						Double measure = nouItem.objectiveMeasure(learner);
						if (measure != null) {
							learner.dataModel.get(
									nouItem.getIdentifier()).scoreScaled =
								measure.toString();
							if (objectID != null) {
								learner.dataModel.get(
										nouItem.getIdentifier()).cmiObjectives
										.get(objectID).scoreScaled =
											measure.toString();
							} //DEL if (objectID != null)
						} //DEL if (measure != null)
					} //DEL if (...PrimaryObj...mapInfo.readNormalizedMeasure)
					
					if (((Objective) nouItem.getSequencing().
							getObjectiveHandler().getPrimaryObjective()).
							mapInfo.readSatisfiedStatus) {
						SequencingStatus value =
							nouItem.satisfied(learner, true);
						if (value != null) {
							learner.dataModel.get(
									nouItem.getIdentifier()).successStatus =
										value.toString();						
							if (objectID != null) {
								learner.dataModel.get(
										nouItem.getIdentifier()).cmiObjectives
										.get(objectID).successStatus =
											value.toString();
							} //DEL if (objectID != null)
						} //DEL if (value != null)
					} //DEL if (...PrimaryObj...mapInfo.readSatisfiedStatus)
				} //DEL if (getPrimaryObjective()).hasMapInfo)
				/**
				 * TODO: Llistem els objectius secundaris per assignar els
				 * objectius globals a aquests secundaris en cas de que 
				 * tinguin el Read "activat"
				 */
				for (Iterator objectIter =
					nouItem.getSequencing().getObjectiveHandler().
					getObjectivesIterator();
				objectIter.hasNext();) {
					Objective tmpObject = (Objective) objectIter.next();
					if (tmpObject.hasMapInfo) {
						if (tmpObject.mapInfo.readNormalizedMeasure) {
							Double measure = 
								tmpObject.objectiveMeasure(learner);
							if (measure != null) {
								learner.dataModel.get(
										nouItem.getIdentifier()).
										cmiObjectives.get(
												tmpObject.ObjectiveID)
												.scoreScaled =
													measure.toString();
							} //DEL if (measure != null)
						} //DEL if (tmpObject().mapInfo.readNormalizedMeasure)
						
						if (tmpObject.mapInfo.readSatisfiedStatus) {
							SequencingStatus value =
								nouItem.satisfied(learner, true);
							if (value != null) {
								learner.dataModel.get(
										nouItem.getIdentifier())
										.cmiObjectives.get(
												tmpObject.ObjectiveID)
												.successStatus =
													value.toString();
							} //DEL if (value != null)
						} //DEL if (tmpObject.mapInfo.readSatisfiedStatus)
					} //DEL if (tmpObject.hasMapInfo)
				} //DEL for ( ... objectIter.hasNext();)
			} //DEL if (nouItem.getSequencing() != null)
			
			//TODO: Fins AQUI!! -----------------------------------------------
		} //DEL if (nouItem == null)
		
		learner.currentItem = nouItem.getIdentifier();
		learner.currentHref = nouItem.getResource().getDefaultFile().href
			+ nouItem.getParameters();
		
		/**
		 * Inicialitzem el valor del scarePassingScore segons el valor 
		 * del minNormalizedMeasure (si és que està definit)
		 */
		Double scaleScoredPassing = nouItem.getMinNormalizedMeasure();
		if (scaleScoredPassing != null) {
			learner.dataModel.get(nouItem.getIdentifier()).scaledPassingScore =
				scaleScoredPassing.toString();
		}
		learner.currentIsCompleted = testCompletedItem(learner, nouItem);
		/**
		 * Nota! En cas que la postCondició doni un "Exit" 
		 * aleshores acabarem de processar l'overallSequencingProces
		 * i al final (abans de fer el return) ho guardarem tot a la
		 * BD.
		 * 
		 * @see PAS 5: Comprovar si té PostCondition o exitCondition.
		 *   
		 */
		if (learner.adlnav.lmsException.equals("Exit")) {
			finalizeCourse(learner, nouItem.getTitle());
		}
		return learner.currentHref;
	}
	/**
	 * 
	 * @param learner
	 */
	private void updateCMIObjectives(
			final UserObjective learner,
			final String itemIdentifier) {
		
		
		for (Iterator cmiObjectiveIter =
			learner.dataModel.get(
					itemIdentifier).cmiObjectives.keySet().iterator();
			cmiObjectiveIter.hasNext();) {
			
			String cmiOBJID = (String) cmiObjectiveIter.next();
						
			/**
			 * PAS 3.1 Evaluar el Completion Status a partir del Completion
			 * Threshold i del Progress Measure.
			 * 
			 * @see SCORM® 2004 Run-Time Environment (RTE) Version 1.3.2 - DRAFT
			 * 		->	4.2.4.1 Completion Status Evaluation 
			 */
			//if (lItem.getIsSCO()) {
			if (learner.dataModel.get(itemIdentifier).
					completionThreshold.length() > 0) {
				if (learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID).
						progressMeasure.length() == 0) {
					
					learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID).
					completionStatus = "unknown";
				} else {
					Integer completionThresh = new Integer(
							learner.dataModel.get(itemIdentifier).
							completionThreshold);
					Integer completionStatus = new Integer(
							learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID).
							completionStatus);
					if (completionStatus >= completionThresh) {
						learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID).
						completionStatus = "completed";
					} else {
						learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID).
						completionStatus = "incomplete";
					} //DEL if (completionStatus >= completionThresh)
				} //DEL if (learner ... progressMeasure.length() == 0)
			} //DEL if (learner. ... completionThreshold.length() > 0)
			
			/**
			 * PAS 3.2: Ajustar el cmi.score.scaled i el cmi.score.raw
			 * perquè tots dos tinguin valors equivalents.
			 * Recordar que el cmi.score.raw està entre cmi.score.min 
			 * i cmi.score.max i que el cmi.score.scaled està entre -1 i 1. 
			 */			
			if (!learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
					.scoreScaled.equals("unknown")
					&& !(learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
					.scoreScaled.length() == 0)) {
				
				Double newDouble = 
					new Double(learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
							.scoreScaled);
				if (learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
						.scoreRaw.equals("unknown") 
						|| learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
						.scoreRaw.length() == 0) {
					/**
					 * Normalitzarem entre 0 i 10
					 */
					if (newDouble <= 0) {
	
						newDouble = 5 - (((-1) * newDouble) * 5);
					} else {
						newDouble = 5 + ((newDouble) * 5);
					}
					learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID).scoreMin =
						"0";
					learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID).scoreRaw =
						newDouble.toString();
					learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID).scoreMax =
						"10";
				} //DEL if (learner. ... .scoreRaw.equals("unknown"))
			} else if (!learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
					.scoreRaw.equals("unknown")
					&& !(learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
							.scoreRaw.length() == 0)) {
				Double scoreMin;
				if (learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
						.scoreMin.length() > 0 
						&& !(learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
								.scoreMin.equals("unknown"))) {
					scoreMin = new Double(
							learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
							.scoreMin);					
				} else {
					scoreMin = new Double(0.0);
				} //DEL if (...dataModel.get(..)...scoreMin.length() > 0 
				
				/*				
				Double scoreMax = new Double(
						learner.dataModel.get(itemIdentifier)
						.scoreMax);
				*/
				Double scoreMax;
				if (learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
						.scoreMax.length() > 0 
						&& !(learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
								.scoreMax.equals("unknown"))) {
					scoreMax = new Double(
							learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
							.scoreMax);					
				} else {
					scoreMax = new Double(0.0);
				} //DEL if (...dataModel.get(..)...scoreMin.length() > 0 
				
				Double scoreRaw = new Double(
						learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID)
						.scoreRaw);
				
				Double rangeValue = scoreMax - scoreMin;
				
				Double scaledValue;
				if (rangeValue != 0) {
					scaledValue =  ((scoreRaw / rangeValue) * 2) - 1;
				} else {
					scaledValue =  0.0;
				}
				
				learner.dataModel.get(itemIdentifier).cmiObjectives.get(cmiOBJID).scoreScaled =
					scaledValue.toString();
			} //DEL (!learner. ... .scoreScaled.equals("unknown"))
			/**
			 * PAS 3.3 Evaluar el cmi.success_status a partir del
			 * cmi.scaled_passing_score i del cmi.scaled_score.
			 * 
			 * @see SCORM® 2004 Run-Time Environment (RTE) Version 1.3.2 - DRAFT
			 * 		->	4.2.22.1  Success Status Evaluation  
			 */
			if (!learner.dataModel.get(itemIdentifier)
					.scaledPassingScore.equals("")) {
								
				if (learner.dataModel.get(
						itemIdentifier).cmiObjectives.get(cmiOBJID).scoreScaled.equals("unknown")) {
					learner.dataModel.get(
							itemIdentifier).cmiObjectives.get(cmiOBJID).successStatus = "unknown";
				} else {
					Double valorSPS = new Double(
							learner.dataModel.get(
									itemIdentifier).scaledPassingScore);
					Double valorScoreS = new Double(
							learner.dataModel.get(
									itemIdentifier).cmiObjectives.get(cmiOBJID).scoreScaled);
					if (valorScoreS < valorSPS) {
						learner.dataModel.get(
								itemIdentifier).cmiObjectives.get(cmiOBJID).successStatus = "failed";
					} else {
						learner.dataModel.get(
								itemIdentifier).cmiObjectives.get(cmiOBJID).successStatus = "passed";
					} //DEL if (valorScoreS < valorSPS)
				} //DEL if (!learner... .scoreScaled.equals(""))
			} //DEL if (!learner... .scaledPassingScore.equals(""))
			
			
		} //DEL for (Iterator cmiObjectiveIter = ..keySet().iterator();
		
	}
	/**
	 * Aquesta funció actualitzarà totes les dades de l'usuari 
	 * a la BD (al DataAccess).
	 * 
	 * @param learner : UserObjective type.
	 * @param lastItemTitle : El títol de l'últim item visitat.
	 * 		En cas de que sigui null no sobre-escriurem la informació.
	 * @return boolean : True / false segons si ha anat tot bé.
	 */
	private boolean finalizeCourse(final UserObjective learner,
			final String lastItemTitle) {
		System.out.println("finalizeCourse(UserObjective " 
				+ learner.userIdentifier + " - " 
				+ learner.userOSIDIdentifier + ",final String " 
				+ lastItemTitle + ")");
		if (learner.userOSIDIdentifier != null) {
			/**
			 * Si l'usuari ha apretat a "Exit" i tenia un currentHref == null
			 * no guardarem ja que la seva sessió ja s'ha guardat quan li ha
			 * sortit el missatge.
			 */
			if ((learner.adlnav.request.equals("exit") 
					|| learner.adlnav.request.equals("exitAll")
					|| learner.adlnav.request.equals("abandon")
					|| learner.adlnav.request.equals("abandonAll")
					|| learner.adlnav.request.equals("suspendAll"))
					&& learner.currentHref == null) {
				System.out.println("[CourseManager][finalizeCourse] -> "
						+ "request(" + learner.adlnav.request + ") i " 
						+ " currentHref == null!!!");
				return true;				
			}
			int studentID = dataAccess.testUserGlobalID(
					getIdentifier(), learner.userOSIDIdentifier);
			if (studentID == 0) {
				/**
				 * Create User Data!
				 */
				studentID = dataAccess.insertStudentsClass(
						getIdentifier(), learner.userOSIDIdentifier);
				
				//ADLNav tmpADLnav = new ADLNav();
				//tmpADLnav = learner.adlnav;
				
				learner.adlnav = null;
				try {
					dataAccess.insertUserObjective(
							"coursestudent_" 
							+ getIdentifier(),
							studentID, (Object) learner);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//learner.adlnav = tmpADLnav;
				
			} //DEL if (studentID != 0)
			/**
			 * Actualitzem les variables.
			 * Serveix pels dos casos.
			 */
			SequencingStatus isCompleted =
				getOrganizationCluster().completed(learner, true);
			boolean bIsCompleted =
				(isCompleted == SequencingStatus.Passed);
			SequencingStatus isSatisfied =
				getOrganizationCluster().satisfied(learner, true);
			boolean bIsSatisfied =
				(isSatisfied == SequencingStatus.Passed);
			
			int complets = (learner.dataModel.size() 
					- learner.remainInComplete);
			
			Double percentatge = 
				(((double) (complets * 100)) / learner.dataModel.size());
			if (percentatge == null) {
				percentatge = 0.0;
			}
			
			Double progressMeasure =
				getOrganizationCluster().objectiveMeasure(learner);
			if (progressMeasure == null) {
				progressMeasure = 0.0;
			}
							
			if (lastItemTitle == null) {
				dataAccess.updateStudentsClassLASTITEM(getIdentifier(),
						studentID, bIsSatisfied, bIsCompleted,
						progressMeasure.doubleValue(),
						percentatge.doubleValue());
				
			} else {
				dataAccess.updateStudentsClass(getIdentifier(),
						studentID, bIsSatisfied, bIsCompleted,
						progressMeasure.doubleValue(), lastItemTitle,
						percentatge.doubleValue());
			}
			
			System.out.println("\n\n\t############ " 
					+ "updateUserObjectiveDataAccess");
			
			learner.adlnav = null;
			dataAccess.updateUserObjectiveDataAccess(
					identifier,
					learner.userOSIDIdentifier, 
					learner);
			
			userObjectivesMap.remove(learner.userOSIDIdentifier);
			
		} //DEL if (learner.userOSIDIdentifier != null)
				
		return true;
	}
	/**
	 * Amb aquesta funció mirarme el % de completat del curs i així d'aquesta
	 * manera encara que tingui el Delivery Tracking a false ho podrem saber. 
	 * 
	 * @param learner : UserObjective Type
	 * @param nouItem : L'item actual que estem testejant.
	 * @return : Boolean {True si està completed, False incompleted/unknown}
	 */
	private boolean testCompletedItem(
			final UserObjective learner, final Leaf_Item nouItem) {
		Boolean testd = false;
		testd = learner.dataModel.get(
				nouItem.getIdentifier()).
				completionStatus.equals("completed");
		if (!testd) {
			for (Iterator nouI = learner.dataModel.get(
				nouItem.getIdentifier()).cmiObjectives.values().iterator();
				nouI.hasNext();) {
				
				CMIObjectives cmi = (CMIObjectives) nouI.next();
				testd = cmi.completionStatus.equals("completed");
				if (learner.currentIsCompleted) {
					break;
				}
			} //DEL for ( ... nouI.hasNext();)
		} //DEL if (!learner.currentIsCompleted)
		
		return testd;
	}
	/**
	 * Aquesta funció testeja si hi ha un "choice" per part de 
	 * l'usuari, i de ser així comprova si el target existeix i 
	 * finalment va a buscar aquest item i el retorna, això sí, 
	 * comprovant abans si hi ha una preConditionRule i si aquesta
	 * es compleix i si ens fa un skip ... 
	 * 
	 * @param learner : UserObjective Type
	 * @return Leaf_Item: Default null.
	 */
	private Leaf_Item testRequestChoice(
			final UserObjective learner) {
		Leaf_Item nouItem = null; 
		String idNewItem = null;
		StringTokenizer tokenizer =
			new StringTokenizer(learner.adlnav.request, "{");
        String token = null;
        if (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            StringTokenizer tokenizer2 =
				new StringTokenizer(token, "=");
            if (tokenizer2.hasMoreTokens()) {
                token = tokenizer2.nextToken();
                if (token.equals("target")) {
                	if (tokenizer2.hasMoreTokens()) {
		                token = tokenizer2.nextToken();
		                tokenizer2 =
							new StringTokenizer(token, "}");
		                if (tokenizer2.hasMoreTokens()) {
			                idNewItem = tokenizer2.nextToken();
		                }
		                if (tokenizer2.hasMoreTokens()) {
			                token = tokenizer2.nextToken();
			                if (!token.equals("choice")) {
			                	idNewItem = null;
			                }
			                if (Constants.DEBUG_NAVIGATION) {
			                	System.out.print("\n");
				                System.out.print("\ttype: " 
				                		+ token);
				                System.out.print("\ttarget: " 
				                		+ idNewItem + "\n");
			                }
		                } //DEL if (tokenizer2.hasMoreTokens())
                	} //DEL if (tokenizer2.hasMoreTokens())
                } //DEL if (token.equals("target")) 
            } //DEL if (tokenizer2.hasMoreTokens())
        } //DEL if (tokenizer.hasMoreTokens())
        if (idNewItem == null) {
        	learner.adlnav.lmsException = "Identificador " 
        		+ "d'Item incorrecte!";
        } else {
        	/**
    		 * PAS 5.2.1: Descobrir el camí òptim.
    		 */
    		Stack < String > routeNI =
    			 searchRouteMap(learner, idNewItem);
    		boolean anyError = false;
    		if (routeNI == null) {
    			anyError = true;
    		} else {
    			if (routeNI.size() == 0) {	
    				if (Constants.DEBUG_NAVIGATION 
    						&& Constants.DEBUG_WARNINGS) {
    					System.out.println("\t[WARNING-testRequestChoice]"
    							+ "[CouseManager] -> " 
            					+ "searchRouteMap -> NULL");   					
    				}        			
        			anyError = true;
        		} else {
        			if (Constants.DEBUG_NAVIGATION) {
        				System.out.println("\t[testRequestChoice]"
            					+ "[CouseManager] -> " 
            					+ "searchRouteMap -> " 
            					+ routeNI.toString());        				
        			}
        		} //DEL if (routeNI.size() == 0)
    		} //DEL if (routeNI == null)
    		/**
    		 * PAS 5.2.2: "Accedir" a l'Item amb el camí descobert.
    		 */
    		if (!anyError) {
    			nouItem = searchMyItem(
    					learner, routeNI, true);
        		if (nouItem == null) {
        			System.out.println("\t[CouseManager] -> " 
        					+ "searchMyItem -> NULL");
        		} else {
        			System.out.println("\t[CouseManager] -> " 
        					+ "searchMyItem -> (SCO? " 
        					+ nouItem.getIsSCO() + ") " 
        					+ nouItem.getIdentifier() + ", Title = '"
        					+ nouItem.getTitle() + "';");
        		} //DEL if (nouItem == null)
    		} else {
    			learner.adlnav.lmsException = "Identificador " 
            		+ "d'Item incorrecte!";
    		} //DEL if (!anyError)
        } //DEL if (idNewItem == null)
        
        return nouItem;
	}
	/**
	 * Funció que testejarà la postCondició d'un item i ens retornarà
	 * null si no hi ha hagut cap postCondició que ens modifiqui el 
	 * seqüenciament o, en cas contrari, en el que ens trobem una 
	 * postCondició que SI que és satisfà, retornarem el item resultant.
	 * 
	 * PAS 5: Comprovar si té PostCondition o exitCondition.
	 * 
	 * 
	 * @param learner : UserObjective Type
	 * @param lItem : Leaf_Item Type
	 * @return Abstract_Item Type.
	 */
	private Abstract_Item testPostCondition(
			final UserObjective learner,
			final Abstract_Item lItem) {
		
		Abstract_Item nouItem = null;
		/**
		 * PAS 5: Comprovar si té PostCondition o exitCondition.
		 */
		ruleActionType rAT =  null;
		if (lItem.getSequencing() != null) {
			rAT = 
				lItem.getSequencing().postConditionRule(learner);
		} //DEL if (lItem.getSequencing() != null)
		if (rAT != null) {
			if (Constants.DEBUG_NAVIGATION)  {
				System.out.println("\t[NAVIGATION-testPostCondition]"
						+ "[CouseManager] -> postConditionRule -> "
						+ rAT.toString());				
			}
			
			/**
			 * PAS 5.1: Si té PostCondició analitzarem de que és tracta i
			 * buscarem el següent item.
			 * '[exitParent, exitAll, retry, retryAll, continue, previous]' 
			 */
			if (rAT.equals(ruleActionType._continue)) {
				nouItem =
					lItem.nextItem(learner, lItem.getIdentifier());
			} else if (rAT.equals(ruleActionType.previous)) {
				nouItem =
					lItem.previousItem(learner, lItem.getIdentifier());
			} else if (rAT.equals(ruleActionType.retry)) {
				if (lItem.father != null) {
					nouItem =
						((Root_Item) lItem.father).firstLeafItem(learner);
				} else {
					if (Constants.DEBUG_NAVIGATION 
							&& Constants.DEBUG_WARNINGS) {
						System.out.println("[WARNING] Ens hem trobat que '" 
								+ lItem.getIdentifier() + "' no te pare!!");
					}
				}
			} else if (rAT.equals(ruleActionType.retryAll)) {
				nouItem =
					((Root_Item)  getOrganizationCluster()).
						firstLeafItem(learner);
			} else if (rAT.equals(ruleActionType.exitParent)) {
				if (lItem.father != null) {
					nouItem =
						lItem.father.nextItem(learner,
								lItem.father.getIdentifier());
				} else {
					if (Constants.DEBUG_NAVIGATION 
							&& Constants.DEBUG_WARNINGS) {
						System.out.println("[WARNING] Ens hem trobat que '" 
								+ lItem.getIdentifier() + "' no te pare!!");
					}
					/**
					 * D'aquesta manera li indiquem al RT Environtment el 
					 * que ha passat. I pel proper que torni haurà de començar
					 * des del principi.
					 */
					learner.adlnav.lmsException = "Exit";
					nouItem =
						((Root_Item)  getOrganizationCluster()).
							firstLeafItem(learner);
					
				}
			} else if (rAT.equals(ruleActionType.exitAll)) {
				/**
				 * D'aquesta manera li indiquem al RT Environtment el 
				 * que ha passat. 
				 */
				learner.adlnav.lmsException = "Exit";
								
			} else {
				if (Constants.DEBUG_ERRORS) {
					System.out.println("[ERROR] No he reconegut l'acció :'" 
							+ rAT.toString() + "'.\nFarem com si no hi haguès" 
							+ " cap postCondició.");
				}
			}
		} //DEL if (rAT != null)
		return nouItem;
	}
	/**
	 * Amb aquesta funció retornarem el item que estem buscant, i gràcies
	 * a la pila que li pasem estem limitant la búsqueda. 
	 * 
	 * @param routeMap : Stack < String > routeMap
	 * @param learner : UserObjective Type
	 * @return Leaf_Item : És l'item que buscavem o null.
	 */	
	public final Leaf_Item searchMyItem(
			final UserObjective learner,
			final Stack < String > routeMap,
			final boolean postCondition) {
		Abstract_Item aItem = organizationCluster.searchMyItem(
				learner, routeMap, postCondition);
		if (aItem instanceof Leaf_Item) {
			Leaf_Item rItem = (Leaf_Item) aItem;
			return rItem;
		} else {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-searchMyItem] No he trobat l'Item!" 
						+ " -> " + routeMap.toString());
			}
			return null;
		}
	}
	/**
	 * Aquesta funció ens retornarà un array (una pila) de manerà que 
	 * tindrem enllaçat d'Strings amb el nom de tota la jerarquia començant
	 * per l'organització i acabant pel 'nameItem'.
	 * 
	 * @param learner : UserObjective Type
	 * @param nameItem : String Type.
	 * @return Stack < String > : routeMap
	 */
	public final Stack < String > searchRouteMap(
			final UserObjective learner,
			final String nameItem) {
		Stack < String > route = new Stack < String > ();
		
		route.push(nameItem);
		
		String lastItem = nameItem;
		while (true) {
			String returned = searchFather(learner, lastItem);
			if (returned == null) {
				return null;
			}
			if (returned.equals(getOrganizationCluster().getIdentifier())) {
				break;
			}
			
			route.push(returned);
			lastItem = returned;
		}
		
		
		return route;				
	}
	/**
	 * Retorna el pare d'un item simplement buscant pel TreeAnnotations.
	 * 
	 * @param learner : UserObjective
	 * @param nameItem : String Type
	 * @return String o null.
	 */
	private String searchFather(
			final UserObjective learner, final String nameItem) {
		
		String father = null;
		
		for (Iterator it = learner.treeAnnotations.values().iterator();
			it.hasNext();) {
			
			TreeAnnotations concreteTA = (TreeAnnotations) it.next();
			
			for (Iterator itt = concreteTA.sons.iterator(); itt.hasNext();) {
				String currentSon = (String) itt.next();
				if (currentSon.equals(nameItem)) {
					return concreteTA.itemID;
				}
			}
		}			
		return father;
	}

}
