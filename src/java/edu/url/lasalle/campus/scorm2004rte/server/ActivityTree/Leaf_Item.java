// $Id: Leaf_Item.java,v 1.29 2008/04/22 18:18:07 ecespedes Exp $
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
 *   02110-1301, USA.  
 */

package edu.url.lasalle.campus.scorm2004rte.server.ActivityTree;

import java.io.Serializable;
import java.util.Stack;
import java.util.logging.Level;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Objective;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Resource;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Sequencing;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.system.DataAccess;
import edu.url.lasalle.campus.scorm2004rte.system.Constants.SequencingStatus;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;

/** <!-- Javadoc -->
 * $Id: Leaf_Item.java,v 1.29 2008/04/22 18:18:07 ecespedes Exp $ 
 * <b>Títol:</b> Leaf_Item <br /><br />
 * <b>Descripció:</b> Implementa una estructura 'arbre'<br /> 
 * d'intre de l'arbre d'activitats. <br />
 *  Extend la classe abstracta Abstract_Item per tal<br />
 *  d'implementar el patró composite.<br /><br />
 *  En aquest cas Leaf_Item representa els nodes 'fulla'. <br />
 * <b>Companyia</b> Departament d'Aprenentage Semipresencial (LaSalleOnLine
 * Enginyeries).<br>
 *
 * @author Eduard Céspedes i Borràs / LaSalle / ecespedes@salleurl.edu
 * @version Versió $Revision: 1.29 $ $Date: 2008/04/22 18:18:07 $
 * $Log: Leaf_Item.java,v $
 * Revision 1.29  2008/04/22 18:18:07  ecespedes
 * Arreglat bugs en el seqüenciament i els objectius secundaris mapejats.
 *
 * Revision 1.28  2008/04/02 14:27:27  ecespedes
 * Depurant objectives secundaris
 *
 * Revision 1.27  2008/01/30 17:41:15  ecespedes
 * Versió final: organització  serialitzada.
 *
 * Revision 1.26  2008/01/29 18:01:41  ecespedes
 * Implementant versió serialitzada de l'organització.
 *
 * Revision 1.25  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral·lel amb el procés de desenvolupament del gestor de BD.
 *
 * Revision 1.24  2007/12/27 15:02:24  ecespedes
 * Reunificant totes les crides del seqüenciament.
 *
 * Revision 1.23  2007/12/21 17:12:44  ecespedes
 * Implementant CourseManager.OverallSequencingProcess
 *
 * Revision 1.22  2007/12/17 15:27:48  ecespedes
 * Fent MapInfo. Bug en els Leaf_Items
 *
 * Revision 1.21  2007/12/14 12:56:13  ecespedes
 * Solucionat bugs i problemes derivats del nou concepte 'ObjectiveCluster'
 *
 * Revision 1.20  2007/12/11 16:00:43  ecespedes
 * Suprimint paràmetres: ObjectiveInterface el paràmetre RollupRule.
 *
 * Revision 1.19  2007/12/10 22:03:32  ecespedes
 * Implementant les funcions per buscar un item concret i retornar-lo
 * al CourseManager perquè el tracti.
 *
 * Revision 1.18  2007/12/10 11:50:27  ecespedes
 * Començant a juntar les peces del procés de seqüenciament.
 *
 * Revision 1.17  2007/12/09 22:32:16  ecespedes
 * Els objectius dels clusters es tracten i es guarden de manera diferent.
 *
 * Revision 1.16  2007/11/29 15:54:35  ecespedes
 * Implementant les SequencingRules (part 1)
 *
 * Revision 1.15  2007/11/27 15:34:25  ecespedes
 * Arreglats bugs relacionats amb el Rollup. Creat nou joc de testos.
 *
 * Revision 1.14  2007/11/26 15:12:36  ecespedes
 * Ja controlem el Progress Measure.
 *
 * Revision 1.13  2007/11/20 15:24:34  ecespedes
 * Les RollupRules ja estan implementades.
 * Modificació del ObjectiveInterface.
 *
 * Revision 1.12  2007/11/19 15:26:58  ecespedes
 * Treballant sobre el RollupRule (2 de 3)
 *
 * Revision 1.11  2007/11/15 15:24:04  ecespedes
 * Implementat el ObjectiveInterface en els Leaf_Item.
 *
 * Revision 1.10  2007/11/14 12:33:36  ecespedes
 * Implementada l'interface: ObjectiveInterface, de manera que ara totes
 * les classes vinculades al seqüenciament l'implementen.
 *
 * Revision 1.9  2007/11/09 12:58:58  ecespedes
 * Creant mètodes perquè així poguem recòrrer més ràpidament
 * l'estructura de l'arbre.
 *
 * Revision 1.8  2007/11/08 16:33:09  ecespedes
 * Començant a Implementar el OverallSequencingProcess, que serà
 * les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
 * realment el seqüenciament SCORM1.3
 *
 * Revision 1.7  2007/10/31 12:57:01  ecespedes
 * Començant a crear tot el sistema de gestió dels arbres:
 * + Creant l'interfífice DataAccess que ens servirà tan
 * 	pel parser com pel SGBD.
 * - Falta que el ParserHandler/GestorBD implementi l'interfície.
 *
 * Revision 1.6  2007/10/30 14:07:42  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Començant a crear el sistema de gestió dels cursos:
 * - CourseAdministrator i CourseManager.
 *
 */
public class Leaf_Item extends Abstract_Item implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9126946297025860459L;

	/**
	 * La idea és que utilitzarem l'Objective per "parsejar" les 
	 * dades CMI de l'usuari a valors vàlids pel seqüenciament. 
	 * Per tant, si és tracta d'un asset el primaryObjective apuntarà
	 * cap a ell (tot i que de l'asset només podrem comprovar si és
	 * attempted o not attempted).
	 * La segona opció és que tinguem un SCO però en el manifest no ens
	 * l'hagin especificat, aleshores crearem un Objective
	 * nosaltres de manera dinàmica i li passarem el UserObjective
	 * perquè el tracti i ens retorni els valors que necessitem:
	 * Satisfied, notSatisfied, Completed, Incomplted, Attempted...
	 * 
	 * Objective Type. Default null.
	 * Optional.
	 */
	private Objective primaryObjective = null;
	
	/**
	 * Variable per fer el registre de logs.
	 */
	//private LogControl log = LogControl.getInstance();
	
	/**
	 * És el resource de la fulla. 
	 * (Resource, default null)
	 */
	private Resource resource = null;
	
	/**
	 * És l'identificador del resource de la fulla. 
	 * (String, default null) 
	 */
	private String identifierref = null;
	
	/**
	 * Amb aquesta funció sabrem si és tracta d'un SCO o no.
	 * 
	 * @return boolean Type.
	 */
	public final boolean getIsSCO() {		
		return getResource().getIsSCO();		
	}
	/**
	 * Ens crea l'objecte Primary. Aquest Objective
	 * no tindrà indentificador propi, però al ser un
	 * SCO estem segur de que ha de tindre alguna dada
	 * (excepte en el cas de que encara no hagi estat
	 * accedit).
	 *
	 */
	private void createPrimaryObjective() {
		primaryObjective = new Objective();
		primaryObjective.hasMapInfo = false;
		primaryObjective.ObjectiveID = null;
		primaryObjective.itemID = getIdentifier();
	}
	/**
	 * Assignem un nou IdentifierRef a la fulla. Teòricament això
	 * només és donarà en el procés de parsejat o quan load == false;
	 * 
	 * @param nouIdentifierref És un String que representa el nou identificador.
	 */
	public final void setIdentifierref(final String nouIdentifierref) {
		identifierref = nouIdentifierref;
	}
	/**
	 * Ens retornarà l'identificador del resource que utilitza la fulla. 
	 * @return IdentifierRef (String)
	 */
	public final String getIdentifierref() {		
		return identifierref;
	}
	/**
	 * Assig un nou recurs automàticament a partir de l'identificador.
	 */	
	public final void setResource() {
		if (identifierref != null) {
			resource = getResourcesCollection().searchResource(identifierref);
			setLoaded(true);
		} else {
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_ITEMS) {
				System.out.println("[WARNING] Atribut IdentifierRef" 
						+ " no trobat!");
			}
		}
	}
	
	/**
	 * Retorna el resource que utilitza aquest item. 
	 * @return resource (Resource Type)
	 */
	public final Resource getResource() {
		if (getLoaded()) {
			return resource;			
		} else {
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_ITEMS) {
				System.out.println("[WARNING] S'ha de carregar el "
						+ "resource " + identifierref + "!");				
			}			
			setResource();
			return resource;
		}		
	}
	/**
	 * Funció recursiva. Busca el seu pare fins trobar algú que tingui
	 * seqüenciament. 
	 */
	public final void activateSequencing() {
		if (hasSequencing() || getIsSCO()) {
			if ((Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING) 
					|| (Constants.DEBUG_WARNINGS_LOW 
							&& Constants.DEBUG_ITEMS)) {
				/*
				log.writeMessage(Level.WARNING, Leaf_Item.class.getName(),
						"[WARNINGS_LOW][" + getIdentifier() 
						+ "]\tSeqüenciament Activat.");
				*/
				System.out.println("[WARNINGS_LOW][" + getIdentifier() 
						+ "]\tSeqüenciament Activat.");
						
			}			
			setSequencing(new Sequencing(this));
		}
	}
	/**
	 * Funció que utilitzarem per transmetre de forma recursiva el
	 * DataAccess utilitzat cap als seus fills. 
	 * 
	 * @param newDataAccess : DataAccess Type.
	 * @param newOrganizationID : L'identificador de l'organització.
	 */
	public final void transmitDataAccess(
			final DataAccess newDataAccess, final int newOrganizationID) {
		dataAccess = newDataAccess;
		organizationID = newOrganizationID;
	}
		
	public final UserObjective overallSequencingProcess(
			final UserObjective learner) {
		
		UserObjective learnerData = learner;
		
		return learnerData;
	}
	
	/**
	 * Amb aquest mètode el que farem serà implementar un mètode iteratiu
	 * que ens permetrà mourens ràpidament per l'arbre.
	 * 
	 * @param learner : UserObjective de l'estudiant actual.
	 * @param currentItemID : L'item del que procedim.
	 * @return Leaf_Item : El proper asset o sco.
	 */
	public final Leaf_Item nextItem(
			final UserObjective learner, final String currentItemID) {
		return father.nextItem(learner, currentItemID);		
	}
	/**
	 * Amb aquest mètode el que farem serà implementar un mètode iteratiu
	 * que ens permetrà mourens ràpidament per l'arbre.
	 * 
	 * @param learner : UserObjective de l'estudiant actual.
	 * @param currentItemID : L'item del que procedim.
	 * @return Leaf_Item : L'anterior asset o sco.
	 */
	public final Leaf_Item previousItem(
			final UserObjective learner, final String currentItemID) {
		return father.previousItem(learner, currentItemID);		
	}
	/**
	 * Aquest mètode el que farà serà actualitzar l'item/sco 
	 * actual i forçar a que el pare->avi .. -> root s'actualitzin.
	 * Això NO vol dir que s'actualitzi tot l'arbre, només s'actualitzarà
	 * la branca afectada i sobre cada node descendirà un nivell.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return boolean : Per saber si hi ha hagut algun error o si
	 * tot ha anat bé.
	 */
	public final boolean overallRollupProcess(
			final UserObjective userObjective) {
		if (getSequencing() != null) {
			
			if (userObjective.currentItem == getIdentifier()) {
				getSequencing().getObjectiveHandler().setFocusOnObjectiveHandler();
			}			
			getSequencing().updateActivityProgressKnown(userObjective);
			getSequencing().updateAttempted(userObjective);
			getSequencing().updateCompleted(userObjective);
			getSequencing().updateObjectiveMeasureKnown(userObjective);
			getSequencing().updateObjectiveMeasure(userObjective);			
			getSequencing().updateObjectiveStatusKnown(userObjective);
			getSequencing().updateSatisfied(userObjective);
			if (userObjective.currentItem == getIdentifier()) {
				getSequencing().getObjectiveHandler().unsetFocusOnObjectiveHandler();
			}
			
		} else {
			if (father == null) {
				return false;
			}
			father.getSequencing().updateActivityProgressKnown(userObjective);
			father.getSequencing().updateAttempted(userObjective);
			father.getSequencing().updateCompleted(userObjective);
			father.getSequencing().updateObjectiveMeasure(userObjective);
			father.getSequencing().updateObjectiveMeasureKnown(userObjective);
			father.getSequencing().updateObjectiveStatusKnown(userObjective);
			father.getSequencing().updateSatisfied(userObjective);
		}
		return true;
	}
	
		
	/**
	 * 
	 * --------------- Implementem l'ObjectiveInterface ---------------.
	 * 
	 */
		
	/**
	 * Ens ha de retorar un booleà indicant-nos si l'activitat ha
	 * estat accedida (passed), no ha estat accedida (failed) o 
	 * no ho sabem (unknown).
	 * 
	 * A aquesta funció li passarem tota la regla de RollupRule perquè
	 * cada 'node' pugui agafar tots els paràmetres necessaris per 
	 * tractar la condició: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos últims valors també haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 
	public final SequencingStatus attempted(
			final UserObjective userObjective) {
		
		if (getSequencing() != null) {
			return getSequencing().attempted(userObjective);
		} else {
			createPrimaryObjective();
			return primaryObjective.attempted(userObjective);
		}
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si l'activitat està
	 * completada (passed), està incompleta (failed) o no ho sabem
	 * (unknown).
	 * 
	 * A aquesta funció li passarem tota la regla de RollupRule perquè
	 * cada 'node' pugui agafar tots els paràmetres necessaris per 
	 * tractar la condició: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos últims valors també haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param afirmative : boolean Type { True:completed, False:incomplete }
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	 */
	public final SequencingStatus completed(
			final UserObjective userObjective, 
			final boolean afirmative) {
		
		if (getSequencing() != null) {
			return getSequencing().completed(
					userObjective, afirmative);
		} else {
			createPrimaryObjective();			
			return primaryObjective.completed(
					userObjective, afirmative);
		}
		//return null;
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si l'activitat està
	 * satisfeta (passed), no ha estat satisfeta (failed) o no ho 
	 * sabem (unknown).
	 * 
	 * A aquesta funció li passarem tota la regla de RollupRule perquè
	 * cada 'node' pugui agafar tots els paràmetres necessaris per 
	 * tractar la condició: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos últims valors també haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param afirmative : boolean Type { True:satisfied, False:notSatisfied }
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	 */
	public final SequencingStatus satisfied(
			final UserObjective userObjective,
			final boolean afirmative) {
		
		if (getSequencing() != null) {
			return getSequencing().satisfied(
					userObjective, afirmative);
		} else if (getIsSCO()){
			createPrimaryObjective();			
			return primaryObjective.satisfied(
					userObjective, afirmative);
		}
		return null;


	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si és coneix 
	 * la mesura de la progressió dels objectius (passed),
	 * no hi ha mesura (failed) o no ho sabem (unknown).
	 * 
	 * A aquesta funció li passarem tota la regla de RollupRule perquè
	 * cada 'node' pugui agafar tots els paràmetres necessaris per 
	 * tractar la condició: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos últims valors també haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	public final SequencingStatus activityProgressKnown(
			final UserObjective userObjective) {
		
		if (getSequencing() != null) {
			return getSequencing().activityProgressKnown(
					userObjective);
		} else if (getIsSCO()){
			createPrimaryObjective();			
			return primaryObjective.activityProgressKnown(
					userObjective);
		}
		return null;
	}
	/**
	 * Si objectiveMeasureKnown és igual a 'passed' aleshores aquesta
	 * funció ens retornarà aquesta mesura, que serà un flotant.
	 * 
	 * A aquesta funció li passarem tota la regla de RollupRule perquè
	 * cada 'node' pugui agafar tots els paràmetres necessaris per 
	 * tractar la condició: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos últims valors també haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Double: Puntuació amb un màxim de quatre decimals.
	*/ 	
	public final Double objectiveMeasure(
			final UserObjective userObjective) {
		
		if (Constants.DEBUG_NAVIGATION
				&& Constants.DEBUG_NAVIGATION_LOW) {
			System.out.println("[NAVIGATION][" 
					+ getIdentifier() + "][SCO? :" 
					+ getIsSCO() + "]"
					+ "Leaf -> objectiveMeasure()");
		}
		
		if (getSequencing() != null) {
			return getSequencing().objectiveMeasure(
					userObjective);
		} else if (getIsSCO()) {
			createPrimaryObjective();			
			return primaryObjective.objectiveMeasure(
					userObjective);
		}
		return null;
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si és coneix 
	 * la mesura dels objectius (Objective Measure == passed),
	 * no hi ha mesura (failed) o no ho sabem (unknown).
	 * 
	 * A aquesta funció li passarem tota la regla de RollupRule perquè
	 * cada 'node' pugui agafar tots els paràmetres necessaris per 
	 * tractar la condició: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos últims valors també haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	public final SequencingStatus objectiveMeasureKnown(
			final UserObjective userObjective) {
		
		if (getSequencing() != null) {
			return getSequencing().objectiveMeasureKnown(
					userObjective);
		} else if (getIsSCO()) {
			createPrimaryObjective();			
			return primaryObjective.objectiveMeasureKnown(
					userObjective);
		}
		return SequencingStatus.Unknown;
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si és coneix 
	 * l'estat de l'objectiu (passed), si no el coneixem (failed)
	 * o si no ho sabem (unknown).
	 * 
	 * A aquesta funció li passarem tota la regla de RollupRule perquè
	 * cada 'node' pugui agafar tots els paràmetres necessaris per 
	 * tractar la condició: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos últims valors també haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	public final SequencingStatus objectiveStatusKnown(
			final UserObjective userObjective) {
		
		if (getSequencing() != null) {
			return getSequencing().objectiveStatusKnown(
					userObjective);
		} else if (getIsSCO()) {
			createPrimaryObjective();			
			return primaryObjective.objectiveStatusKnown(
					userObjective);
		}
		return null;
	}
	/**
	 * Amb aquesta funció ens solucionem la vida a l'hora de passar aquest
	 * valor entre classes que implementin aquesta interfície.
	 * 
	 * @return Double Type.
	 */
	public final Double getMinNormalizedMeasure() {
		if (getSequencing() != null) {
			return getSequencing().getMinNormalizedMeasure();			
		} else {
			return null;
		}
	}
	/**
	 * Amb aquesta funció ens solucionem la vida a l'hora de passar aquest
	 * paràmetre entre classes que implementin aquesta interfície. 
	 * 
	 * @return boolean Type. 
	 */
	public final boolean getSatisfiedByMeasure() {
		if (getSequencing() != null) {
			return getSequencing().getSatisfiedByMeasure();			
		} else {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[Leaf_Item.getSatisfiedByMeasure()]"
						+ "getSequencing() == NULL!");
			}
			return false;
		}		
	}
	/**
	 * Amb aquesta funció retornarem el item que estem buscant, i gràcies
	 * a la pila que li pasem estem limitant la búsqueda. 
	 * 
	 * @param routeMap : Stack < String > routeMap
	 * @param learner : UserObjective Type.
	 * @return Leaf_Item : És l'item que buscavem o null.
	 */
	public final Abstract_Item searchMyItem(
			final UserObjective learner,
			final Stack < String > routeMap,
			final boolean postCondition) {
		if (Constants.DEBUG_ERRORS) {
			if (routeMap != null) {
				System.out.println("[ERROR-routeMap] No he trobat l'Item! -> " 
						+ routeMap.toString());				
			} else {
				System.out.println("[ERROR-routeMap] No he trobat " 
						+ "l'Item! -> NULL");
			}			
		}
		return null;		
	}
}

