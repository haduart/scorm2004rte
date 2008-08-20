// $Id: ObjectiveHandler.java,v 1.16 2008/04/22 18:18:07 ecespedes Exp $
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

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.system.DataAccess;
import edu.url.lasalle.campus.scorm2004rte.system.Constants.SequencingStatus;

/**
* $Id: ObjectiveHandler.java,v 1.16 2008/04/22 18:18:07 ecespedes Exp $
* <b>Títol:</b> ObjectiveHandler<br><br>
* <b>Descripció:</b> Aquesta classe és la que controla els objectives,<br>
* tan els primaris com els secundaris.<br><br> 
*
* @author Eduard Céspedes i Borràs /Enginyeria La Salle/ecespedes@salle.url.edu
* @version Versió $Revision: 1.16 $ $Date: 2008/04/22 18:18:07 $
* $Log: ObjectiveHandler.java,v $
* Revision 1.16  2008/04/22 18:18:07  ecespedes
* Arreglat bugs en el seqüenciament i els objectius secundaris mapejats.
*
* Revision 1.15  2008/01/18 18:07:24  ecespedes
* Serialitzades TOTES les classes per tal de que els altres puguin fer proves
* en paral·lel amb el procés de desenvolupament del gestor de BD.
*
* Revision 1.14  2007/12/17 15:27:48  ecespedes
* Fent MapInfo. Bug en els Leaf_Items
*
* Revision 1.13  2007/12/13 15:25:12  ecespedes
* Problemes amb el sistema d'arbre de clusters.
* Falla l'ObjectiveStatusKnown.
*
* Revision 1.12  2007/12/11 16:00:43  ecespedes
* Suprimint paràmetres: ObjectiveInterface el paràmetre RollupRule.
*
* Revision 1.11  2007/12/10 11:50:27  ecespedes
* Començant a juntar les peces del procés de seqüenciament.
*
* Revision 1.10  2007/12/09 22:32:16  ecespedes
* Els objectius dels clusters es tracten i es guarden de manera diferent.
*
* Revision 1.9  2007/11/27 15:34:25  ecespedes
* Arreglats bugs relacionats amb el Rollup. Creat nou joc de testos.
*
* Revision 1.8  2007/11/26 15:12:36  ecespedes
* Ja controlem el Progress Measure.
*
* Revision 1.7  2007/11/20 15:24:34  ecespedes
* Les RollupRules ja estan implementades.
* Modificació del ObjectiveInterface.
*
* Revision 1.6  2007/11/19 15:26:58  ecespedes
* Treballant sobre el RollupRule (2 de 3)
*
* Revision 1.5  2007/11/15 15:24:04  ecespedes
* Implementat el ObjectiveInterface en els Leaf_Item.
*
* Revision 1.4  2007/11/14 12:33:36  ecespedes
* Implementada l'interface: ObjectiveInterface, de manera que ara totes
* les classes vinculades al seqüenciament l'implementen.
*
* Revision 1.3  2007/11/13 15:59:59  ecespedes
* Treballant sobre el sistema per "linkar" els Objectives de l'estructura
* Sequencing de l'arbre amb els Objectives de l'usuari.
* Millorat TreeAnnotations (step 2 de 3)
*
* Revision 1.2  2007/11/12 13:00:08  ecespedes
* Arreglant elements del seqüenciament.
* Ja quasi està implementat el TreeAnnotation.
*
*/
public class ObjectiveHandler implements ObjectiveInterface, Serializable {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 2929859766677595108L;
	
	/**
	 * Aquesta variable ens servirà per saber si estem devant del 
	 * ObjectiveHandler actual o si és una actualització global i
	 * ens ha afectat de forma indirecta.
	 * D'aquesta manera quan ens actualitzin i tinguem algun objectiu mapejat 
	 * farem el "write" depenent d'aquesta variable. 
	 * 
	 * boolean Type.
	 * Default False.
	 */
	private boolean currentObjectiveHandler = false;
	
	/**
	 * Ens servirà per marcar que ens estem centrant amb aquest 
	 * objectiveHandler.
	 * 
	 * @return Boolean Type: Retornem l'anterior estat del ObjectiveHandler.
	 */
	public final boolean setFocusOnObjectiveHandler() {
		boolean currentOH = currentObjectiveHandler;
		currentObjectiveHandler = true;
		return currentOH;
	}
	/**
	 * Ens servirà per marcar que ja NO ens estem centrant amb aquest 
	 * objectiveHandler.
	 * 
	 * @return Boolean Type: Retornem l'anterior estat del ObjectiveHandler.
	 */
	public final boolean unsetFocusOnObjectiveHandler() {
		boolean currentOH = currentObjectiveHandler;
		currentObjectiveHandler = false;
		return currentOH;
	}
	
	//		-------------- Memory improvement Methods -------------- {
	/**
	 * Variable que ens servirà per implementar els mètodes d'optimització.
	 * De manera que quan és faci una petició a una funció d'aquesta classe
	 * el primer que farà serà comprovar si loaded està a false, i en cas
	 * de ser així voldrà dir que no tenim totes les dades de la classe
	 * carregades en memòria, per tant, amb el dataAccess i l'identificador
	 * (objectiveID) podrem demanar les dades que ens falten.
	 * 
	 * boolean Type. Default False.
	 * Required.
	 */
	private boolean loaded = false;
	/**
	 * Tindrà l'identificador del primary objective dintre del
	 * dataAccess.
	 * 
	 * long Type. Default 0.
	 * Required.
	 */
	private long objectiveID = 0;
	/**
	 * Tindrem el dataAccess que haurem d'utilitzar per demanar
	 * dades quan ens faltin.
	 * 
	 * DataAccess Type. Default null.
	 * Required.
	 */
	public DataAccess dataAccess = null;
	// 		} -------------- END-Memory improvement Methods --------------
		
	/**
	 * Com a mínim sempre hem de tindre un PrimaryObjective.
	 * ... si és un cluster no te perquè tindre'l...
	 */
	private ObjectiveInterface primaryObjective = null;
	
	/**
	 * Tots els objectius secundaris aniran aquí.
	 */	
	private Map < String , ObjectiveInterface > objectives =
		new Hashtable < String, ObjectiveInterface > ();
		
	/**
	 * Funció que retorna l'objecte principal (PrimaryObjective).
	 *  
	 * @return Objective Type. Default Null.
	 */
	public final ObjectiveInterface getPrimaryObjective() {
		return primaryObjective;
	}
	/**
	 * Funció que ens retorna un objecte secundari segons 
	 * l'identificador indicat.
	 * 
	 * @param identifier : String Type
	 * @return ObjectiveInterface o Null si no el troba.
	 */
	public final ObjectiveInterface getSecondaryObjective(
			final String identifier) {
		return objectives.get(identifier);
	}	
	
	//		-------------- Parser Methods -------------- {
	/**
	 * Ens marca que la classe ja està carregada per complet.
	 * 
	 * Nota! Només pot utilitzar aquesta funció el Parser.
	 */
	public final void setLoaded() {
		loaded = true;
	}
	/**
	 * Assigna l'Objective que serà el PrimaryObjective.
	 * 
	 * Nota! Només pot utilitzar aquesta funció el Parser.
	 * 
	 * @param newObjective : Objective Type
	 */
	public final void setPrimaryObjective(
			final ObjectiveInterface newObjective) {
		primaryObjective = newObjective;	
	}
	/**
	 * Funció per afegir Objectives.
	 * Nota! Només pot utilitzar aquesta funció el Parser.
	 * 
	 * @param id : String Type -> Identificador
	 * @param newObjective : Objective Type.
	 */
	public final void addObjective(final String id,
			final ObjectiveInterface newObjective) {
		objectives.put(id, newObjective);
	}
	/**
	 * Retornem l'iterador, per si algú vol recorrer el vector d'objectives.
	 * 
	 * Nota! Només pot utilitzar aquesta funció el Parser.
	 * 
	 * @return Iterator< Objective > Type
	 */
	public final Iterator < ObjectiveInterface > getObjectivesIterator() {
		return objectives.values().iterator();
	}
	
	//		}-------------- END-Parser Methods --------------

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
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 
	public final SequencingStatus attempted(
			final UserObjective userObjective) {
		
		return primaryObjective.attempted(userObjective);
		
		
	}
	/**
	 * Aquesta funció actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
	public final boolean updateAttempted(
			final UserObjective userObjective,
			final SequencingStatus newValue) {
		if (primaryObjective instanceof ObjectiveCluster) {
			ObjectiveCluster clustOBJ = (ObjectiveCluster) primaryObjective;
			return clustOBJ.updateAttempted(userObjective, newValue);
		} else {
			if (currentObjectiveHandler) {
				if (((Objective) primaryObjective).hasMapInfo) {
					ObjectiveCluster primaryOC = new ObjectiveCluster();
					
					primaryOC.ObjectiveID = 
						((Objective) primaryObjective)
							.mapInfo.targetObjectiveID;
					primaryOC.itemID = userObjective.currentItem;
					
					primaryOC.updateAttempted(userObjective,
							((Objective) primaryObjective).attempted(
									userObjective));
					
				} //DEL if (((Objective) primaryObjective).hasMapInfo)
				
				for (Iterator iter = getObjectivesIterator(); iter.hasNext();) {
					ObjectiveInterface nouOI =
						(ObjectiveInterface) iter.next();
					if (nouOI instanceof Objective) {
						if (((Objective) nouOI).hasMapInfo) {
							((Objective) nouOI).attempted(userObjective);
						}
					} //DEL if (nouOI instanceof Objective)
				} //DEL while (getObjectivesIterator().hasNext();
			} //DEL if (currentObjectiveHandler)
		} //DEL if (primaryObjective instanceof ObjectiveCluster)
		return true;		
	}
	/**
	 * Si volem saber si una activitat està completada preguntarem 
	 * passant com a paràmetre afirmmative = TRUE, d'aquesta manera
	 * si ens retorna un Passed voldrà dir que sí que està completed.
	 * 
	 * Si volem saber si una activitat és incomplete preguntarem
	 * passant com a paràmetre afirmative = FALSE. D'aquesta manera
	 * si ens retorna un Passed voldrà dir que és incomplete.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param afirmative : boolean Type { True:completed, False:incomplete }
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	 */
	public final SequencingStatus completed(
			final UserObjective userObjective, 
			final boolean afirmative) {
		
		return primaryObjective.completed(
				userObjective, afirmative);
	}
	/**
	 * Aquesta funció actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
	public final boolean updateCompleted(
			final UserObjective userObjective,
			final SequencingStatus newValue) {
		//(objectives.get("obj_module_1") instanceof ObjectiveCluster)
		
		if (primaryObjective instanceof ObjectiveCluster) {
			ObjectiveCluster clustOBJ = (ObjectiveCluster) primaryObjective;
			return clustOBJ.updateCompleted(userObjective, newValue);
		} else {
			if (currentObjectiveHandler) {
				if (((Objective) primaryObjective).hasMapInfo) {
					ObjectiveCluster primaryOC = new ObjectiveCluster();
					
					primaryOC.ObjectiveID = 
						((Objective) primaryObjective)
							.mapInfo.targetObjectiveID;
					primaryOC.itemID = userObjective.currentItem;
					
					primaryOC.updateCompleted(userObjective,
							((Objective) primaryObjective).completed(
									userObjective, true));
					
				} //DEL if (((Objective) primaryObjective).hasMapInfo)
				
				for (Iterator iter = getObjectivesIterator(); iter.hasNext();) {
					ObjectiveInterface nouOI =
						(ObjectiveInterface) iter.next();
					if (nouOI instanceof Objective) {
						if (((Objective) nouOI).hasMapInfo) {
							((Objective) nouOI).completed(userObjective, true);
						}
					} //DEL if (nouOI instanceof Objective)
				} //DEL while (getObjectivesIterator().hasNext();
			} //DEL if (currentObjectiveHandler)
		} //DEL if (primaryObjective instanceof ObjectiveCluster)
				
				
		return true;		
	}
	/**
	 * Si volem saber si una activitat està satisfeta preguntarem 
	 * passant com a paràmetre afirmmative = TRUE, d'aquesta manera
	 * si ens retorna un Passed voldrà dir que sí que està satisfeta.
	 * 
	 * Si volem saber si una activitat NO està satisfeta preguntarem
	 * passant com a paràmetre afirmative = FALSE. D'aquesta manera
	 * si ens retorna un Passed voldrà dir que NO està satisfeta.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param afirmative : boolean Type { True:satisfied, False:notSatisfied }
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	 */
	public final SequencingStatus satisfied(
			final UserObjective userObjective, 
			final boolean afirmative) {
		
		return primaryObjective.satisfied(userObjective, afirmative);
	}
	/**
	 * Aquesta funció actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
	public final boolean updateSatisfied(
			final UserObjective userObjective,
			final SequencingStatus newValue) {
		if (primaryObjective instanceof ObjectiveCluster) {
			ObjectiveCluster clustOBJ = (ObjectiveCluster) primaryObjective;
			return clustOBJ.updateSatisfied(userObjective, newValue);
		} else {
			if (currentObjectiveHandler) {
				if (((Objective) primaryObjective).hasMapInfo) {
					ObjectiveCluster primaryOC = new ObjectiveCluster();
					
					primaryOC.ObjectiveID = 
						((Objective) primaryObjective)
							.mapInfo.targetObjectiveID;
					primaryOC.itemID = userObjective.currentItem;
					
					primaryOC.updateSatisfied(userObjective,
							((Objective) primaryObjective).satisfied(
									userObjective, true));
					
				} //DEL if (((Objective) primaryObjective).hasMapInfo)
				
				for (Iterator iter = getObjectivesIterator(); iter.hasNext();) {
					ObjectiveInterface nouOI =
						(ObjectiveInterface) iter.next();
					if (nouOI instanceof Objective) {
						if (((Objective) nouOI).hasMapInfo) {
							((Objective) nouOI).satisfied(userObjective, true);
						}
					} //DEL if (nouOI instanceof Objective)
				} //DEL while (getObjectivesIterator().hasNext();
			} //DEL if (currentObjectiveHandler)
		} //DEL if (primaryObjective instanceof ObjectiveCluster)
		
		return true;
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si és coneix 
	 * la mesura de la progressió dels objectius (passed),
	 * no hi ha mesura (failed) o no ho sabem (unknown).
	 *  
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	public final SequencingStatus activityProgressKnown(
			final UserObjective userObjective) {
		
		return primaryObjective.activityProgressKnown(
				userObjective);
	}
	/**
	 * Aquesta funció actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
	public final boolean updateActivityProgressKnown(
			final UserObjective userObjective,
			final SequencingStatus newValue) {
		if (primaryObjective instanceof ObjectiveCluster) {
			ObjectiveCluster clustOBJ = (ObjectiveCluster) primaryObjective;
			return clustOBJ.updateActivityProgressKnown(
					userObjective, newValue);
		} else {
			if (currentObjectiveHandler) {
				if (((Objective) primaryObjective).hasMapInfo) {
					ObjectiveCluster primaryOC = new ObjectiveCluster();
					
					primaryOC.ObjectiveID = 
						((Objective) primaryObjective)
							.mapInfo.targetObjectiveID;
					primaryOC.itemID = userObjective.currentItem;
					
					primaryOC.updateActivityProgressKnown(userObjective,
							((Objective) primaryObjective).activityProgressKnown(
									userObjective));
					
				} //DEL if (((Objective) primaryObjective).hasMapInfo)
				
				for (Iterator iter = getObjectivesIterator(); iter.hasNext();) {
					ObjectiveInterface nouOI =
						(ObjectiveInterface) iter.next();
					if (nouOI instanceof Objective) {
						if (((Objective) nouOI).hasMapInfo) {
							((Objective) nouOI).activityProgressKnown(userObjective);
						}
					} //DEL if (nouOI instanceof Objective)
				} //DEL while (getObjectivesIterator().hasNext();
			} //DEL if (currentObjectiveHandler)
		} //DEL if (primaryObjective instanceof ObjectiveCluster)
		return true;		
		
	}	
	/**
	 * Si objectiveMeasureKnown és igual a 'passed' aleshores aquesta
	 * funció ens retornarà aquesta mesura, que serà un flotant.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Double: Puntuació amb un màxim de quatre decimals.
	*/ 	
	public final Double objectiveMeasure(
			final UserObjective userObjective) {
		
		if (Constants.DEBUG_NAVIGATION
				&& Constants.DEBUG_NAVIGATION_LOW) {
			System.out.println("[NAVIGATION]"
					+ "ObjectiveHandler -> objectiveMeasure()");
		}
		return primaryObjective.objectiveMeasure(userObjective);
	}
	/**
	 * Aquesta funció actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newValue : Double Type, la puntuació nova.
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
	public final boolean updateObjectiveMeasure(
			final UserObjective userObjective,
			final Double newValue) {
		if (primaryObjective instanceof ObjectiveCluster) {
			ObjectiveCluster clustOBJ = (ObjectiveCluster) primaryObjective;
			return clustOBJ.updateObjectiveMeasure(userObjective, newValue);
		} else {
			if (currentObjectiveHandler) {
				if (((Objective) primaryObjective).hasMapInfo) {
					ObjectiveCluster primaryOC = new ObjectiveCluster();
					
					primaryOC.ObjectiveID = 
						((Objective) primaryObjective)
							.mapInfo.targetObjectiveID;
					primaryOC.itemID = userObjective.currentItem;
					
					primaryOC.updateObjectiveMeasure(userObjective,
							((Objective) primaryObjective).objectiveMeasure(
									userObjective));
					
				} //DEL if (((Objective) primaryObjective).hasMapInfo)
				
				for (Iterator iter = getObjectivesIterator(); iter.hasNext();) {
					ObjectiveInterface nouOI =
						(ObjectiveInterface) iter.next();
					if (nouOI instanceof Objective) {
						if (((Objective) nouOI).hasMapInfo) {
							((Objective) nouOI).objectiveMeasure(userObjective);
						}
					} //DEL if (nouOI instanceof Objective)
				} //DEL while (getObjectivesIterator().hasNext();
			} //DEL if (currentObjectiveHandler)
		} //DEL if (primaryObjective instanceof ObjectiveCluster)
		return true;		
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si és coneix 
	 * la mesura dels objectius (Objective Measure == passed),
	 * no hi ha mesura (failed) o no ho sabem (unknown).
	 *  
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	public final SequencingStatus objectiveMeasureKnown(
			final UserObjective userObjective) {
		
		return primaryObjective.objectiveMeasureKnown(userObjective);
	}
	/**
	 * Aquesta funció actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
	public final boolean updateObjectiveMeasureKnown(
			final UserObjective userObjective,
			final SequencingStatus newValue) {
		if (primaryObjective instanceof ObjectiveCluster) {
			ObjectiveCluster clustOBJ = (ObjectiveCluster) primaryObjective;
			return clustOBJ.updateObjectiveMeasureKnown(
					userObjective, newValue);
		} else {
			if (currentObjectiveHandler) {
				if (((Objective) primaryObjective).hasMapInfo) {
					ObjectiveCluster primaryOC = new ObjectiveCluster();
					
					primaryOC.ObjectiveID = 
						((Objective) primaryObjective)
							.mapInfo.targetObjectiveID;
					primaryOC.itemID = userObjective.currentItem;
					
					primaryOC.updateObjectiveMeasureKnown(userObjective,
							((Objective) primaryObjective).objectiveMeasureKnown(
									userObjective));
					
				} //DEL if (((Objective) primaryObjective).hasMapInfo)
				
				for (Iterator iter = getObjectivesIterator(); iter.hasNext();) {
					ObjectiveInterface nouOI =
						(ObjectiveInterface) iter.next();
					if (nouOI instanceof Objective) {
						if (((Objective) nouOI).hasMapInfo) {
							((Objective) nouOI).objectiveMeasureKnown(userObjective);
						}
					} //DEL if (nouOI instanceof Objective)
				} //DEL while (getObjectivesIterator().hasNext();
			} //DEL if (currentObjectiveHandler)
		} //DEL if (primaryObjective instanceof ObjectiveCluster)
		
		return true;		
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si és coneix 
	 * l'estat de l'objectiu (passed), si no el coneixem (failed)
	 * o si no ho sabem (unknown).
	 *  
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	public final SequencingStatus objectiveStatusKnown(
			final UserObjective userObjective) {
		
		return primaryObjective.objectiveStatusKnown(userObjective);
	}
	/**
	 * Aquesta funció actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
	public final boolean updateObjectiveStatusKnown(
			final UserObjective userObjective,
			final SequencingStatus newValue) {
		if (primaryObjective instanceof ObjectiveCluster) {
			ObjectiveCluster clustOBJ = (ObjectiveCluster) primaryObjective;
			return clustOBJ.updateObjectiveStatusKnown(
					userObjective, newValue);
		} else {
			if (currentObjectiveHandler) {
				if (((Objective) primaryObjective).hasMapInfo) {
					ObjectiveCluster primaryOC = new ObjectiveCluster();
					
					primaryOC.ObjectiveID = 
						((Objective) primaryObjective)
							.mapInfo.targetObjectiveID;
					primaryOC.itemID = userObjective.currentItem;
					
					primaryOC.updateObjectiveStatusKnown(userObjective,
							((Objective) primaryObjective).objectiveStatusKnown(
									userObjective));
					
				} //DEL if (((Objective) primaryObjective).hasMapInfo)
				
				for (Iterator iter = getObjectivesIterator(); iter.hasNext();) {
					ObjectiveInterface nouOI =
						(ObjectiveInterface) iter.next();
					if (nouOI instanceof Objective) {
						if (((Objective) nouOI).hasMapInfo) {
							((Objective) nouOI).objectiveStatusKnown(userObjective);
						}
					} //DEL if (nouOI instanceof Objective)
				} //DEL while (getObjectivesIterator().hasNext();
			} //DEL if (currentObjectiveHandler)
		} //DEL if (primaryObjective instanceof ObjectiveCluster)
		
		return true;		
	}
	/**
	 * Amb aquesta funció ens solucionem la vida a l'hora de passar aquest
	 * valor entre classes que implementin aquesta interfície.
	 * 
	 * @return Double Type.
	 */
	public final Double getMinNormalizedMeasure() {
		return primaryObjective.getMinNormalizedMeasure();
	}
	/**
	 * Amb aquesta funció ens solucionem la vida a l'hora de passar aquest
	 * paràmetre entre classes que implementin aquesta interfície. 
	 * 
	 * @return boolean Type. 
	 */
	public final boolean getSatisfiedByMeasure() {
		return primaryObjective.getSatisfiedByMeasure();
	}
}

