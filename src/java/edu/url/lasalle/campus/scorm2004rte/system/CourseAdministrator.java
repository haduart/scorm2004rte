// $Id: CourseAdministrator.java,v 1.8 2008/04/22 18:18:07 ecespedes Exp $
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.tools.HashKey;


/**
* $Id: CourseAdministrator.java,v 1.8 2008/04/22 18:18:07 ecespedes Exp $
* <b>T�tol:</b> CourseAdministrator <br><br>
* <b>Descripci�:</b> Aquesta classe administrar� els CourseManagers
* que estiguin actualment en mem�ria, de manera que si un nou usuari
* entra en el sistema li demanar� a ell el corresponent CourseManager
* i en cas de que aquest no es trob�s en mem�ria el crearia.
* 
* Singleton Pattern implemented.
*
* @author Eduard C�spedes i Borr�s / Enginyeria La Salle / haduart@gmail.com
* @version Versi� $Revision: 1.8 $ $Date: 2008/04/22 18:18:07 $
* $Log: CourseAdministrator.java,v $
* Revision 1.8  2008/04/22 18:18:07  ecespedes
* Arreglat bugs en el seq�enciament i els objectius secundaris mapejats.
*
* Revision 1.7  2008/03/31 13:56:19  ecespedes
* Depurant objectives secundaris
*
* Revision 1.6  2008/02/28 16:15:23  ecespedes
* Millorant el sistema (8)
*
* Revision 1.5  2008/02/25 09:12:58  ecespedes
* *** empty log message ***
*
* Revision 1.4  2008/02/18 15:50:47  ecespedes
* Implementats Threads per als CourseManagers
*
* Revision 1.3  2007/11/08 16:33:09  ecespedes
* Comen�ant a Implementar el OverallSequencingProcess, que ser�
* les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
* realment el seq�enciament SCORM1.3
*
* Revision 1.2  2007/11/07 13:15:08  ecespedes
* Creat tot el sistema que controla els DataAccess, per tal
* que el CourseAdministrator pugui subministrar-lo quan un
* UserObjective li demani.
*
* Revision 1.1  2007/10/30 14:07:42  ecespedes
* Arreglat tots els bugs relacionats amb l'UserObjective.
* Comen�ant a crear el sistema de gesti� dels cursos:
* - CourseAdministrator i CourseManager.
*
*/
public final class CourseAdministrator {
	/**
	 * �s la variable que instanciar� l'objecte de CourseAdministrator.
	 */
	private static CourseAdministrator courseAdministrator = null;
		
	/**
	 * Ser� el llistat de CourseManagers que gestiornar� aquest 
	 * administrador. 
	 */
	private Map < HashKey, CourseManager > courseManagersMap = null;
	
	/**
	 * �s el dataAccess que consultar� l'administrador (CourseAdministrator)
	 * per buscar un nou CourseManager.
	 * 
	 * TODO Fer que no sigui �nic sin� que tingui un conjunt de DataAccess.
	 */
	private DataAccess dataAccess = null;
	
	/**
	 * El constructor per defecte ser� privat ja que �s tracta 
	 * d'un singleton, per tant tindrem una �nica inst�ncia 
	 * d'aquesta classe en mem�ria. 
	 *
	 */
	private CourseAdministrator() {
		courseManagersMap =
			Collections.synchronizedMap(
					new HashMap < HashKey, CourseManager > ());
		
	}	
	/**
	 * Es recupera la inst�ncia de l'objecte CourseAdministrator 
	 * ja sigui creant ara la inst�ncia o recuperant la ja feta anteriorment.
	 * 
	 * @return Inst�ncia de l'objecte CourseAdministrator.
	 */
	public static synchronized CourseAdministrator getInstance() {
		
		if (courseAdministrator == null) {
			courseAdministrator = new CourseAdministrator();
			/**
			 * Arranquem el MemorySheduler.
			 */
			MemorySheduler.getInstance().start();
		}
		return courseAdministrator;
		
	}
	/**
	 * Podem necessitar en un moment dir-li al CourseAdministrator
	 * que a partir d'ara les peticions de cursos nous (que no 
	 * estan actualment en mem�ria) els busqui en aquest sistema
	 * de dades (el newDataAccess).
	 * 
	 * @param newDataAccess : DataAccess Type
	 */
	public void setDataAcces(final DataAccess newDataAccess) {
		if (courseManagersMap.size() > 0) {
			if (Constants.DEBUG_WARNINGS) {
				System.out.println("[WARNING] Estas modificant el DataAcces"
						+ " i tenim en funcionament " 
						+ courseManagersMap.size() + " CourseManagers actius.");
			}			
		} 
		dataAccess = newDataAccess;
	}
	
	/**
	 * Amb aquest m�tode l'usuari li demanar� a l'Administrador
	 * (CourseAdministrator) que li dongui el curs sol�licitat.
	 * 
	 * @param dataAccessID : L'identificador �nid del sistema de DataAccess.
	 * @param organizationID : L'ID de l'organitzaci� dins del dataAccessID.
	 * @return CourseManager : �s el curs que ens demana.
	 */
	public CourseManager getCourseManagerInstance(
			final int dataAccessID, final long organizationID) {
		
		CourseManager yourCourseManager = null;
		
		if (courseManagersMap.size() > 0) {
			
			yourCourseManager =
				courseManagersMap.get(
						new HashKey(dataAccessID, organizationID));
			
		}
		/**
		 * Si no el trobem l'haurem de buscar,crear i afegir-lo 
		 * al llistat de courseManagersMap per saber que el tenim en 
		 * mem�ria.
		 */
		if (yourCourseManager == null) {
			
			DataAccess nouDataAccess =
				DataAccessRepository.getInstance().getDataAccess(dataAccessID);
			
			if (nouDataAccess == null) {
				return null;				
			} else {
				yourCourseManager =
					new CourseManager(nouDataAccess , organizationID);
				yourCourseManager.start();
				//yourCourseManager.increaseUsers();
				courseManagersMap.put(
						new HashKey(dataAccessID, organizationID),
						yourCourseManager);
			}			
		} /* else {
			//yourCourseManager.increaseUsers();
		}*/
		MemorySheduler.getInstance().addObserver(yourCourseManager);
		return yourCourseManager;				
	}
	/**
	 * Aquesta funcio elimina un CourseManager del Hash i tamb� del 
	 * MemorySheduler.
	 * 
	 * @param dataAccessID : int Type
	 * @param organizationID : long Type
	 * @return boolean :True->s'ha eliminat correctament;False->no s'ha trobat.
	 */
	public boolean removeCourseManagerInstance(
			final int dataAccessID, final long organizationID) {
		
		courseManagersMap.get(
				new HashKey(dataAccessID, organizationID)).
					stopCourseManagerThread();
		
		return (courseManagersMap.remove(
						new HashKey(dataAccessID, organizationID)) != null);

	}
	/**
	 * Ens retorna l'usuari per defecte segons el curs i el model de 
	 * dades que tinguem... clar que d'alguna manera haurem de saber-ho
	 * abans.
	 * 
	 * @param dataAccessID : L'identificador �nid del sistema de DataAccess.
	 * @param organizationID : L'ID de l'organitzaci� dins del dataAccessID.
	 * @return UserObjective : �s el UserObjective que ens demana.
	 */
	public UserObjective getUserObjective(
			final int dataAccessID, final long organizationID) {
		
		CourseManager tCManage =
			getCourseManagerInstance(dataAccessID, organizationID);
		if (tCManage == null) {
			return null;
		} else {
			UserObjective tUsObj = tCManage.getUserDefault();
			return tUsObj;
		}
	}
	/**
	 * Aquesta funci� testejar� si existeix un curs SCORM per al DataAccess
	 * que tenim per defecte.
	 * @param nameIdentificator : String Type. 
	 * @return boolean Type: Retorn� true si existeix, false si no.
	 */
	public boolean existSCORMCourse(final String nameIdentificator) {
		if (dataAccess == null) {
			if (courseManagersMap.size() == 0) {
				if (Constants.DEBUG_ERRORS) {
					System.out.println(
							"[ERROR-CourseAdministrator]NO Tenim CAP" 
							+ " DataAccess definit per defecte ni cap " 
							+ "CourseManager!");
				}
				return false;
			}
			if (Constants.DEBUG_ERRORS) {
				System.out.println(
						"[ERROR-CourseAdministrator]NO Tenim CAP" 
						+ " DataAccess definit per defecte:" 
						+ "Estem utilitzant el DataAccess del primer" 
						+ " CourseManager!!!");
			}
			dataAccess =
				((CourseManager)
						courseManagersMap.values().iterator().next()).
							dataAccess;
		}
		return existSCORMCourse(
				nameIdentificator, dataAccess.getDataAccessID());		
	}
	/**
	 * Aquesta funci� testejar� si existeix un curs SCORM per al DataAccess
	 * que tenim per defecte.
	 * @param nameIdentificator : String Type. 
	 * @param dataAccessID : int Type. 
	 * @return boolean Type: Retorn� true si existeix, false si no.
	 */
	public boolean existSCORMCourse(
			final String nameIdentificator,
			final int dataAccessID) {		
		return DataAccessRepository.getInstance().
					getDataAccess(dataAccessID).
						existSCORMCourse(nameIdentificator);		
	}
	/**
	 * Aquesta funci� servir� per eliminar la part del seq�enciament d'un curs.
	 * O sigui que eliminarem tots els elements que conformen el motor i 
	 * l'estructura d'un curs: L'arbre d'activitats amb tots els elements de 
	 * seq�enciament i tot el DataModel de l'usuari per aquell curs. 
	 * D'aquesta maner� nom�s guardarem l'informaci� "final" d'aquest curs, 
	 * amb les notes i qualificacions finals dels usuaris.
	 * 
	 * @param organizationID : long Type. 
	 * @param dataAccessID : int Type. 
	 * @return boolean Type: Retorn� true si existeix, false si no.
	 */
	public boolean removeSequencingSCORMCourse(
			final long organizationID,
			final int dataAccessID) {
		
		return DataAccessRepository.getInstance().
			getDataAccess(dataAccessID).
			removeSequencingSCORMCourse(organizationID);
	}
	/**
	 * Aquesta funci� eliminar� tot un curs (amb totes les dades dels usuaris)
	 * de la base de dades. Per una opci� menys agressiva utilitzar la funci� 
	 * removeSequencingSCORMCourse(...).
	 * 
	 * @see removeSequencingSCORMCourse
	 * 
	 * @param organizationID : long Type. 
	 * @param dataAccessID : int Type. 
	 * @return boolean Type: Retorn� true si existeix, false si no.
	 */
	public boolean removeAllSCORMCourse(
			final long organizationID,
			final int dataAccessID) {
		
		return DataAccessRepository.getInstance().
			getDataAccess(dataAccessID).
			removeAllSCORMCourse(organizationID);
	}
	

}
