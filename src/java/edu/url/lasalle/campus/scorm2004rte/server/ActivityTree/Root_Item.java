// $Id: Root_Item.java,v 1.26 2008/04/22 18:18:07 ecespedes Exp $
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 *  MA  02110-1301, USA.  
 */

package edu.url.lasalle.campus.scorm2004rte.server.ActivityTree;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Sequencing;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.SequencingRules.ruleActionType;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.system.DataAccess;
import edu.url.lasalle.campus.scorm2004rte.system.Constants.SequencingStatus;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;


/** <!-- Javadoc -->
 * $Id: Root_Item.java,v 1.26 2008/04/22 18:18:07 ecespedes Exp $ 
 * <b>Títol:</b> Root_Item <br /><br />
 * <b>Descripció:</b> Implementa una estructura 'arbre'<br /> 
 * d'intre de l'arbre d'activitats. <br />
 *  Extend la classe abstracta Abstract_Item per tal<br />
 *  d'implementar el patró composite.<br />
 *  En aquest cas Root_Item representa els nodes 'clusters'. <br />
 * <b>Companyia</b> Departament d'Aprenentage Semipresencial (LaSalleOnLine
 * Enginyeries).<br>
 *
 * @author Eduard Céspedes i Borràs / LaSalle / ecespedes@salleurl.edu
 * @version Versió $Revision: 1.26 $ $Date: 2008/04/22 18:18:07 $
 * $Log: Root_Item.java,v $
 * Revision 1.26  2008/04/22 18:18:07  ecespedes
 * Arreglat bugs en el seqüenciament i els objectius secundaris mapejats.
 *
 * Revision 1.25  2008/04/02 14:27:27  ecespedes
 * Depurant objectives secundaris
 *
 * Revision 1.24  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral·lel amb el procés de desenvolupament del gestor de BD.
 *
 * Revision 1.23  2007/12/28 16:35:27  ecespedes
 * Implementat l'OverallSequencingProcess.
 *
 * Revision 1.22  2007/12/27 15:02:24  ecespedes
 * Reunificant totes les crides del seqüenciament.
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
 * Revision 1.16  2007/11/27 15:34:25  ecespedes
 * Arreglats bugs relacionats amb el Rollup. Creat nou joc de testos.
 *
 * Revision 1.15  2007/11/26 15:12:36  ecespedes
 * Ja controlem el Progress Measure.
 *
 * Revision 1.14  2007/11/20 15:24:34  ecespedes
 * Les RollupRules ja estan implementades.
 * Modificació del ObjectiveInterface.
 *
 * Revision 1.13  2007/11/19 15:26:58  ecespedes
 * Treballant sobre el RollupRule (2 de 3)
 *
 * Revision 1.12  2007/11/19 07:34:15  ecespedes
 * Millores en els RollupRules.
 *
 * Revision 1.11  2007/11/14 12:33:36  ecespedes
 * Implementada l'interface: ObjectiveInterface, de manera que ara totes
 * les classes vinculades al seqüenciament l'implementen.
 *
 * Revision 1.10  2007/11/13 15:59:59  ecespedes
 * Treballant sobre el sistema per "linkar" els Objectives de l'estructura
 * Sequencing de l'arbre amb els Objectives de l'usuari.
 * Millorat TreeAnnotations (step 2 de 3)
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
public class Root_Item  extends Abstract_Item implements Serializable{
	/**
	 * Variable per fer el registre de logs.
	 */
	//private LogControl log = LogControl.getInstance();

	/**
	 * 
	 */
	private static final long serialVersionUID = 7128132422584354092L;
	/**
	 * Contindrà tots els items fills que pengen d'aquest cluster.
	 * Estaran ordenats segons l'String que serà l'identificador 
	 * de l'Item. 
	 */
	private Map < String, Abstract_Item > itemCollectionSTRING =
		new LinkedHashMap < String, Abstract_Item > ();
	/**
	 * Contindrà tots els items fills que pengen d'aquest cluster.
	 * Estaran ordenats segons un Integer que ens serà útil per saber
	 * la posició de l'item dintre del clúster;
	 * ex: Saber si és el primer element, si és l'últim o saber quin 
	 * és el següent element i l'anterior.
	 */
	private Map < Integer, String > itemCollectionIntTOString =
		new HashMap < Integer, String > ();
	/**
	 * Ens retorna l'iterador dels fills, més que res perquè hi hem 
	 * d'accedir des del Sequencing.
	 * 
	 * @return Iterator < Abstract_Item >
	 */
	public final Iterator < Abstract_Item > getChildrenIterator() {
		return itemCollectionSTRING.values().iterator();
	}
	/**
	 * Mètode per afegir un nou Item a l'actual cluster.
	 * Aquest mètode només hauria de ser cridat pel parser o pel
	 * SGBD.
	 * @param newItem : (Abstract_Item Type)
	 */
	public final void addItem(final Abstract_Item newItem) {
		itemCollectionSTRING.put(newItem.getIdentifier(), newItem);
		Integer itemColleInteger = itemCollectionIntTOString.size();
		itemCollectionIntTOString.put(
				itemColleInteger, newItem.getIdentifier());
	}
	/**
	 * Ens indica si tenim o no algun fill en el cluster. 
	 * Teòricament NO hauria d'haver MAI cap cluster buit. 
	 * @return boolean
	 */
	public final boolean isItemCollectionEmpty() {
		return (itemCollectionSTRING.size() == 0);		
	}
	/**
	 * Ens retorna el número d'items 'fills' que té el cluster.
	 * Ens serà especialment útil per optimitzacions.
	 * @return int
	 */
	public final int itemCollectionSize() {
		return itemCollectionSTRING.size();
	}	
	/**
	 * Només que hi hagi un item amb seqüenciament explícit propagarem <br />
	 * aquesta variable per tot l'arbre. 
	 */
	public final void activateSequencing() {
		if (!hasSequencing()) {
			if ((Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING) 
					|| (Constants.DEBUG_WARNINGS_LOW 
							&& Constants.DEBUG_ITEMS)) {
				/*
				log.writeMessage(Level.WARNING, Root_Item.class.getName(),
						"[WARNINGS_LOW][" + getIdentifier() 
						+ "]\tSeqüenciament Activat.");
						*/
				System.out.println("[WARNINGS_LOW][" + getIdentifier() 
						+ "]\tSeqüenciament Activat.");
			}
			
			setSequencing(new Sequencing(this));			
			
		}
		
		Collection < Abstract_Item > itemCollection =
			itemCollectionSTRING.values();
		
		for (Iterator itemCollectionIterator = itemCollection.iterator();
			itemCollectionIterator.hasNext();) 	{
			
			Abstract_Item newSonItem = 
				(Abstract_Item) itemCollectionIterator.next();
			
			newSonItem.activateSequencing();			
			
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
		
		Collection < Abstract_Item > itemCollection =
			itemCollectionSTRING.values();
		
		for (Iterator itemCollectionIterator = itemCollection.iterator();
		itemCollectionIterator.hasNext();) 	{
				
			Abstract_Item newSonItem = 
				(Abstract_Item) itemCollectionIterator.next();
		
			newSonItem.transmitDataAccess(dataAccess, organizationID);
		}
	}
		
	public final UserObjective overallSequencingProcess(
			final UserObjective learner) {
		
			
		UserObjective learnerData = learner;
		
		if (learnerData.currentItem == null) {
			if (Constants.DEBUG_INFO || Constants.DEBUG_NAVIGATION) {
				System.out.println("[NAVIGATION][" + getIdentifier() 
						+ "]currentItem == null");
			}
			/**
			 * TODO Anar a buscar el primer Asset o SCO.
			 */
		} else {
			if (learnerData.currentItem == getIdentifier()) {
				/**
				 * TODO Tenim el marró nosaltres...
				 * Haurem de mirar si tenim un previous, continous,
				 *  choice, suspend, exit ...  
				 */
			} else {
				
			}			
		}		
		
		return learnerData;
	}
	/**
	 * Ens retorna l'identificador numéric corresponent a un 
	 * identificador String. 
	 * 
	 * @param currentItemID : String Type; IdentificatorID.
	 * @return int Type
	 */
	private int convertStringIDToINT(final String currentItemID) {
		for (int i = 0; i < itemCollectionIntTOString.size(); i++) {
			if (itemCollectionIntTOString.get(i).equals(currentItemID)) {
				return i;
			}
		}
		return 0;
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
				
		int currentItemINTID = convertStringIDToINT(currentItemID);
		
		if (currentItemINTID == (itemCollectionSize() - 1)) {
			if (father != null) {
				return father.nextItem(learner, getIdentifier());
			} else {
				return null;
			}
		} else {
			Abstract_Item abItem =
				itemCollectionSTRING.get(
						itemCollectionIntTOString.get(currentItemINTID + 1));
			/**
			 * Analitzem si té seqüenciament i de tenir-ne mirarem
			 * si té precondició, i de tenir-ne mirame si és compleix
			 * i si la ens fa un 'skip'. De ser així buscariem recursivament
			 * el següent item.
			 */
			if (abItem.hasSequencing()) {
				if (abItem.getSequencing() != null) {
					ruleActionType rAT = 
						abItem.getSequencing().preConditionRule(learner);
					if (rAT != null) {
						if (rAT.equals(ruleActionType.skip)) {
							Leaf_Item newItem = 
								abItem.nextItem(
									learner, abItem.getIdentifier());
							
							while (newItem.father == abItem) {
								newItem = newItem.nextItem(
										learner, newItem.getIdentifier());
							}
							return newItem;
						} //DEL if (rAT.equals(ruleActionType.skip))
					} //DEL if (rAT != null)
				} //DEL if (abItem.getSequencing() != null)
			} //DEL if (abItem.hasSequencing())
			if (abItem instanceof Root_Item) {
				Root_Item rootAbItem = (Root_Item) abItem;
				return rootAbItem.firstLeafItem(learner);				
			} else {
				return (Leaf_Item) abItem;
			}
		}
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
		
		int currentItemINTID = convertStringIDToINT(currentItemID);
		
		if (currentItemINTID == 0) {
			if (father != null) {
				return father.previousItem(learner, getIdentifier());
			} else {
				return null;
			}
		} else {
			Abstract_Item abItem =
				itemCollectionSTRING.get(
						itemCollectionIntTOString.get(currentItemINTID - 1));
			/**
			 * Analitzem si té seqüenciament i de tenir-ne mirarem
			 * si té precondició, i de tenir-ne mirame si és compleix
			 * i si la ens fa un 'skip'. De ser així buscariem recursivament
			 * l'anterior item.
			 */
			if (abItem.hasSequencing()) {
				if (abItem.getSequencing() != null) {
					ruleActionType rAT = 
						abItem.getSequencing().preConditionRule(learner);
					if (rAT != null) {
						if (rAT.equals(ruleActionType.skip)) {
							return abItem.previousItem(
									learner, abItem.getIdentifier());
						}
					}
											
				}
			}
			if (abItem instanceof Root_Item) {
				Root_Item rootAbItem = (Root_Item) abItem;
				return rootAbItem.lastLeafItem(learner);			
			} else {
				return (Leaf_Item) abItem;
			}
		}
	}	
	/**
	 * Retorna el primer node del cluster.
	 * 
	 * @return Abstract_Item Type
	 */
	public final Abstract_Item firstAbstractItem() {
		return itemCollectionSTRING.get(itemCollectionIntTOString.get(0));
	}
	/**
	 * Retorna l'últim node del cluster.
	 * 
	 * @return Abstract_Item Type
	 */
	public final Abstract_Item lastAbstractItem() {
		int lastID = itemCollectionIntTOString.size() - 1; 
		return itemCollectionSTRING.get(itemCollectionIntTOString.get(lastID));
	}
	/**
	 * Ens retornarà el primer node fulla. Si el primer node de l'actual
	 * cluster és un altre cluster el que farem serà descendir per aquest
	 * segon cluster, i així sistemàticament fins a trobar amb el primer
	 * node fulla.
	 * 
	 * @param learner : UserObjective Type
	 * @return Leaf_Item Type
	 */
	public final Leaf_Item firstLeafItem(
			final UserObjective learner) {
		Abstract_Item fistAbItem = firstAbstractItem();
		/**
		 * Analitzem si té seqüenciament i de tenir-ne mirarem
		 * si té precondició, i de tenir-ne mirame si és compleix
		 * i si la ens fa un 'skip'. De ser així buscariem recursivament
		 * el següent item.
		 */
		if (fistAbItem.hasSequencing()) {
			if (fistAbItem.getSequencing() != null) {
				ruleActionType rAT = 
					fistAbItem.getSequencing().preConditionRule(learner);
				if (rAT != null) {
					if (rAT.equals(ruleActionType.skip)) {
						return fistAbItem.nextItem(
								learner, fistAbItem.getIdentifier());
					}
				}
										
			}
		}
		
		if (fistAbItem instanceof Leaf_Item) {
			return (Leaf_Item) fistAbItem;			
		} else {
			return ((Root_Item) fistAbItem).firstLeafItem(learner);			
		}
	}
	/**
	 * Ens retornarà el primer node fulla. Si el primer node de l'actual
	 * cluster és un altre cluster el que farem serà descendir per aquest
	 * segon cluster, i així sistemàticament fins a trobar amb el primer
	 * node fulla.
	 * 
	 * @return Leaf_Item Type
	 */
	public final Leaf_Item lastLeafItem(
			final UserObjective learner) {
		Abstract_Item fistAbItem = lastAbstractItem();
		/**
		 * Analitzem si té seqüenciament i de tenir-ne mirarem
		 * si té precondició, i de tenir-ne mirame si és compleix
		 * i si la ens fa un 'skip'. De ser així buscariem recursivament
		 * el següent item.
		 */
		if (fistAbItem.hasSequencing()) {
			if (fistAbItem.getSequencing() != null) {
				ruleActionType rAT = 
					fistAbItem.getSequencing().preConditionRule(learner);
				if (rAT != null) {
					if (rAT.equals(ruleActionType.skip)) {
						return fistAbItem.previousItem(
								learner, fistAbItem.getIdentifier());
					}
				}
										
			}
		}
		if (fistAbItem instanceof Leaf_Item) {
			return (Leaf_Item) fistAbItem;			
		} else {
			return ((Root_Item) fistAbItem).lastLeafItem(learner);			
		}
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
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 
	public final SequencingStatus attempted(
			final UserObjective userObjective) {
		
		if (getSequencing() != null) {
			return getSequencing().attempted(userObjective);
		} else {
			return null;
		}	
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si l'activitat està
	 * completada (passed), està incompleta (failed) o no ho sabem
	 * (unknown).
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param afirmative : boolean Type { True:completed, False:incomplete }
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	 */
	public final SequencingStatus completed(
			final UserObjective userObjective, 
			final boolean afirmative) {
		
		if (getSequencing() != null) {
			return getSequencing().completed(userObjective, afirmative);
		} else {
			return null;
		}	
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si l'activitat està
	 * satisfeta (passed), no ha estat satisfeta (failed) o no ho 
	 * sabem (unknown).
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
		} else {
			return null;
		}		
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

		if (getSequencing() != null) {
			return getSequencing().activityProgressKnown(userObjective);
		} else {
			return null;
		}	
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
			System.out.println("[NAVIGATION][" 
					+ getIdentifier() + "]" 
					+ "Root -> objectiveMeasure()");
		}

		if (getSequencing() != null) {
			return getSequencing().objectiveMeasure(userObjective);
		} else {
			return null;
		}	
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

		if (getSequencing() != null) {
			return getSequencing().objectiveMeasureKnown(
					userObjective);
		} else {
			return null;
		}	
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

		if (getSequencing() != null) {
			return getSequencing().objectiveStatusKnown(
					userObjective);
		} else {
			return null;
		}	
	}
	/**
	 * Amb aquesta funció ens solucionem la vida a l'hora de passar aquest
	 * valor entre classes que implementin aquesta interfície.
	 * 
	 * @return Double Type.
	 */
	public final Double getMinNormalizedMeasure() {
		return getSequencing().getMinNormalizedMeasure();
	}
	/**
	 * Amb aquesta funció ens solucionem la vida a l'hora de passar aquest
	 * paràmetre entre classes que implementin aquesta interfície. 
	 * 
	 * @return boolean Type. 
	 */
	public final boolean getSatisfiedByMeasure() {
		return getSequencing().getSatisfiedByMeasure();
	}
	/**
	 * Amb aquesta funció retornarem el item que estem buscant, i gràcies
	 * a la pila que li pasem estem limitant la búsqueda. 
	 * 
	 * @param routeMap : Stack < String > routeMap
	 * @param learner : UserObjective
	 * @return Leaf_Item : És l'item que buscavem o null.
	 */	
	public final Abstract_Item searchMyItem(
			final UserObjective learner,
			final Stack < String > routeMap,
			final boolean postCondition) {
		String item = routeMap.pop();
		if (item == null) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-routeMap] No he trobat l'Item!");
			}
			return null;
		}
		/**
		 * TODO: Idealment hauriem de mirar si hi ha preCondició
		 * tan en els nodes fills com en els pares!
		 */
		/**
		 * Si la pila està a 0 vol dir que el proper ja és el 
		 * fill i aleshores mirarem si realment és pot llençar o
		 * té alguna precondició que fà que salti cap al proper item.
		 */
		if (routeMap.size() == 0) {
			Abstract_Item abItem =
				itemCollectionSTRING.get(item);
			/**
			 * Analitzem si té seqüenciament i de tenir-ne mirarem
			 * si té precondició, i de tenir-ne mirame si és compleix
			 * i si la ens fa un 'skip'. De ser així buscariem recursivament
			 * el següent item.
			 */
			if (abItem.hasSequencing()) {
				if (abItem.getSequencing() != null) {
					ruleActionType rAT = 
						abItem.getSequencing().preConditionRule(learner);
					if (rAT != null) {
						if ((rAT.equals(ruleActionType.skip))
								&& postCondition) {
							return abItem.nextItem(
									learner, abItem.getIdentifier());
						}
					}
											
				}
			}
			return abItem;			
		}
		
		return itemCollectionSTRING.get(item).searchMyItem(
				learner, routeMap, postCondition);		
	}	
}
