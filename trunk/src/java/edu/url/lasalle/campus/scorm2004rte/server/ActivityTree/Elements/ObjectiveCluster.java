// $Id: ObjectiveCluster.java,v 1.7 2008/01/18 18:07:24 ecespedes Exp $
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

import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.
	UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.Constants.SequencingStatus;

/**
 * $Id: ObjectiveCluster.java,v 1.7 2008/01/18 18:07:24 ecespedes Exp $ * 
 * <b>Títol:</b> ObjectiveCluster <br /><br />
 * <b>Descripció:</b> En aquesta classe hi guardarem un objective, ja sigui<br>
 * un definit sota demanda pel manifest o un que crearem automàticament per <br>
 * enmagatzemar l'estat del cluster i d'aquesta manera evitar-nos baixar a <br>
 * nivell dels fills per saber l'estat.<br><br>
 * 
 * @author Eduard Céspedes i Borràs /Enginyeria LaSalle/ ecespedes@salle.url.edu
 * 
 * @version 1.0 $Revision: 1.7 $ $Date: 2008/01/18 18:07:24 $
 * $Log: ObjectiveCluster.java,v $
 * Revision 1.7  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral·lel amb el procés de desenvolupament del gestor de BD.
 *
 * Revision 1.6  2007/12/21 17:12:44  ecespedes
 * Implementant CourseManager.OverallSequencingProcess
 *
 * Revision 1.5  2007/12/20 20:45:53  ecespedes
 * Implementat l'Objective Map
 *
 * Revision 1.4  2007/12/13 15:25:12  ecespedes
 * Problemes amb el sistema d'arbre de clusters.
 * Falla l'ObjectiveStatusKnown.
 *
 * Revision 1.3  2007/12/11 16:00:43  ecespedes
 * Suprimint paràmetres: ObjectiveInterface el paràmetre RollupRule.
 *
 * Revision 1.2  2007/12/11 15:29:27  ecespedes
 * Arreglant bugs i optimitzant solucions.
 *
 * Revision 1.1  2007/12/09 22:32:16  ecespedes
 * Els objectius dels clusters es tracten i es guarden de manera diferent.
 * 
 */
public final class ObjectiveCluster implements ObjectiveInterface, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5826929093703858778L;
	/**
	 * És per no fer una relació amb el SequencingStatus.Unknown.
	 * int Type = 0
	 */
	private static final int UNKNOWN = 0;
	/**
	 * És per no fer una relació amb el SequencingStatus.Passed.
	 * int Type = 1
	 */
	private static final int PASSED = 1;
	/**
	 * És per no fer una relació amb el SequencingStatus.Failed.
	 * int Type = 2
	 */
	private static final int FAILED = 2;
	/**
	 * Aquest serà el valor inicial o en cas d'error s'assignarà 
	 * aquest valor a les variables. De manera que el seqüenciament
	 * quan rebi un -1 el que farà serà fer un rollup i actualitzar
	 * aquesta variable. 
	 * 
	 * int Type = -1
	 */
	private static final int NULL = -1;
	

	//	--		Variables		--    
	/**
	 * És l'identificador de l'Item en el que estem.
	 */
	public String itemID = null;	
	/**
	 * Identificador de l'objectiu.
	 * String Type.
	 */
	public String ObjectiveID = null;
	
	
	//------------------------- {
	/**
	 * Indica quan l'actual objectiu te un valor satisfactori. 
	 * Forma part de 'Objective Progress Information'
	 */
	public boolean ObjectiveProgressStatus = false;	
	/**
	 * Indica si l'objectiu està satisfet. 
	 * Forma part de 'Objective Progress Information'
	 */
	//public boolean ObjectiveSatisfiedStatus = false;
	
	// } -------------------------
	
	
	/**
	 * Indica que l'objectiu te un valor vàlid per la mesura.
	 * Forma part de 'Objective Progress Information'
	 * AKA: SatisfiedByMeasure
	 */
	public boolean satisfiedByMeasure = false;
	/**
	 * La mesura de l'objectiu. 
	 * Forma part de 'Objective Progress Information'
	 * AKA: minNormalizedMeasure
	 */
	public Double minNormalizedMeasure = null;
	/**
	 * Variable que utilitzarem per marcar si tenim o no una referència
	 * a una variable global. 
	 */
	public boolean hasMapInfo = false;
	/**
	 * El mapInfo està enmagatzemat aquí.
	 */
	public MapInfo mapInfo = null;
	
	
	
	//-------- Extendre per tornar implementar l'ObjectiveInterface --------
	
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
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	public SequencingStatus activityProgressKnown(
			final UserObjective userObjective) {
		int getType = 0;
		if (ObjectiveID != null) {
			getType = 
				userObjective.clusterTree.get(itemID)
				.objective.get(ObjectiveID).activityProgressKnown;			
		} else {
			getType =
				userObjective.clusterTree.get(itemID)
				.objective.get(itemID).activityProgressKnown;			
		}
		switch (getType) {
		case PASSED:
			return SequencingStatus.Passed;				
		case FAILED:
			return SequencingStatus.Failed;				
		case UNKNOWN:
			return SequencingStatus.Unknown;
		default:
			return null;				
		}
		
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
	public boolean updateActivityProgressKnown(
			final UserObjective userObjective,
			final SequencingStatus newValue) {
		int resultValue = NULL;
		boolean returnValue = true;
		
		if (newValue == null) {
			returnValue = false;
		} else {
			if (newValue.equals(SequencingStatus.Passed)) {
				resultValue = PASSED;
			} else if (newValue.equals(SequencingStatus.Failed)) {
				resultValue = FAILED;
			} else if (newValue.equals(SequencingStatus.Unknown)) {
				resultValue = UNKNOWN;
			} else {
				returnValue = false;
			}			
		}				
		if (ObjectiveID != null) {
			userObjective.clusterTree.get(itemID)
				.objective.get(ObjectiveID).activityProgressKnown 
				= resultValue;
		} else {
			userObjective.clusterTree.get(itemID)
				.objective.get(itemID).activityProgressKnown 
				= resultValue;
		}		
		return returnValue;
	}
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
	public SequencingStatus attempted(
			final UserObjective userObjective) {
		int getType = 0;
		if (ObjectiveID != null) {
			getType =
				userObjective.clusterTree.get(itemID)
				.objective.get(ObjectiveID).attempted;			
		} else {
			getType = 
				userObjective.clusterTree.get(itemID)
				.objective.get(itemID).attempted;			
		}
		switch (getType) {
		case PASSED:
			return SequencingStatus.Passed;				
		case FAILED:
			return SequencingStatus.Failed;				
		case UNKNOWN:
			return SequencingStatus.Unknown;
		default:
			return null;				
		}
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
	public boolean updateAttempted(
			final UserObjective userObjective,
			final SequencingStatus newValue) {
		int resultValue = NULL;
		boolean returnValue = true;
		
		if (newValue == null) {
			returnValue = false;
		} else {
			if (newValue.equals(SequencingStatus.Passed)) {
				resultValue = PASSED;
			} else if (newValue.equals(SequencingStatus.Failed)) {
				resultValue = FAILED;
			} else if (newValue.equals(SequencingStatus.Unknown)) {
				resultValue = UNKNOWN;
			} else {
				returnValue = false;
			}			
		}				
		if (ObjectiveID != null) {
			userObjective.clusterTree.get(itemID)
				.objective.get(ObjectiveID).attempted = resultValue;		
		} else {
			userObjective.clusterTree.get(itemID)
				.objective.get(itemID).attempted = resultValue;			
		}		
		return returnValue;		
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
	public SequencingStatus completed(
			final UserObjective userObjective, 
			final boolean afirmative) {
		int getType = 0;
		if (ObjectiveID != null) {
			getType = 
				userObjective.clusterTree.get(itemID)
				.objective.get(ObjectiveID).completed;			
		} else {
			getType =
				userObjective.clusterTree.get(itemID)
				.objective.get(itemID).completed;			
		}
		switch (getType) {
		case PASSED:
			if (afirmative) {
				return SequencingStatus.Passed;
			} else {
				return SequencingStatus.Failed;
			}
		case FAILED:
			if (afirmative) {
				return SequencingStatus.Failed;
			} else {
				return SequencingStatus.Passed;
			}				
		case UNKNOWN:
			return SequencingStatus.Unknown;
		default:
			return null;				
		}
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
	public boolean updateCompleted(
			final UserObjective userObjective,
			final SequencingStatus newValue) {
		int resultValue = NULL;
		boolean returnValue = true;
		
		if (newValue == null) {
			returnValue = false;
		} else {
			if (newValue.equals(SequencingStatus.Passed)) {
				resultValue = PASSED;
			} else if (newValue.equals(SequencingStatus.Failed)) {
				resultValue = FAILED;
			} else if (newValue.equals(SequencingStatus.Unknown)) {
				resultValue = UNKNOWN;
			} else {
				returnValue = false;
			}			
		}				
		if (ObjectiveID != null) {
			userObjective.clusterTree.get(itemID)
				.objective.get(ObjectiveID).completed = resultValue;		
		} else {
			userObjective.clusterTree.get(itemID)
				.objective.get(itemID).completed = resultValue;			
		}
		return returnValue;		
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
	public Double objectiveMeasure(
			final UserObjective userObjective) {
		Double getType = null;
		if (hasMapInfo && mapInfo != null) {
			if (mapInfo.readNormalizedMeasure 
					&& mapInfo.targetObjectiveID != null) {
				getType =
					userObjective.globalObjectiveMap.
						objective.get(mapInfo.targetObjectiveID).
							objectiveMeasure;
			}
		} else {
			if (ObjectiveID != null) {
				getType =
					userObjective.clusterTree.get(itemID)
					.objective.get(ObjectiveID).objectiveMeasure;			
			} else {
				getType =
					userObjective.clusterTree.get(itemID)
					.objective.get(itemID).objectiveMeasure;			
			}
		}
		
		return getType;
		
	}
	/**
	 * Aquesta funció actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param enewValue : Double Type, la puntuació.
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
	public boolean updateObjectiveMeasure(
			final UserObjective userObjective,
			final Double enewValue) {
		
		Double newValue = enewValue;
		if (hasMapInfo && mapInfo != null) {
			if (mapInfo.readNormalizedMeasure 
					&& mapInfo.targetObjectiveID != null) {
				newValue =
					userObjective.globalObjectiveMap.
					objective.get(mapInfo.targetObjectiveID).
						objectiveMeasure;
			}
		}
		
		if (newValue == null) {
			return false;
		}
		if (ObjectiveID != null) {
			userObjective.clusterTree.get(itemID)
			.objective.get(ObjectiveID).objectiveMeasure = newValue;
		} else {
			userObjective.clusterTree.get(itemID)
			.objective.get(itemID).objectiveMeasure = newValue;
		}
		
		if (hasMapInfo && mapInfo != null) {
			if (mapInfo.writeNormalizedMeasure 
					&& mapInfo.targetObjectiveID != null) {
				userObjective.globalObjectiveMap.
					objective.get(mapInfo.targetObjectiveID).
						objectiveMeasure = newValue;
			}
		}
		return true;
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
	public SequencingStatus objectiveMeasureKnown(
			final UserObjective userObjective) {
		int getType = 0;
		if (ObjectiveID != null) {
			getType =
				userObjective.clusterTree.get(itemID)
				.objective.get(ObjectiveID).objectiveMeasureKnown;			
		} else {
			getType =
				userObjective.clusterTree.get(itemID)
				.objective.get(itemID).objectiveMeasureKnown;			
		}
		switch (getType) {
		case PASSED:
			return SequencingStatus.Passed;				
		case FAILED:
			return SequencingStatus.Failed;				
		case UNKNOWN:
			return SequencingStatus.Unknown;
		default:
			return null;				
		}
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
	public boolean updateObjectiveMeasureKnown(
			final UserObjective userObjective,
			final SequencingStatus newValue) {
		int resultValue = NULL;
		boolean returnValue = true;
		
		if (newValue == null) {
			returnValue = false;
		} else {
			if (newValue.equals(SequencingStatus.Passed)) {
				resultValue = PASSED;
			} else if (newValue.equals(SequencingStatus.Failed)) {
				resultValue = FAILED;
			} else if (newValue.equals(SequencingStatus.Unknown)) {
				resultValue = UNKNOWN;
			} else {
				returnValue = false;
			}			
		}				
		if (ObjectiveID != null) {
			userObjective.clusterTree.get(itemID)
				.objective.get(ObjectiveID).objectiveMeasureKnown = resultValue;
		} else {
			userObjective.clusterTree.get(itemID)
				.objective.get(itemID).objectiveMeasureKnown = resultValue;
		}
		return returnValue;		
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
	public SequencingStatus objectiveStatusKnown(
			final UserObjective userObjective) {
		int getType = 0; 
		if (ObjectiveID != null) {
			getType =
				userObjective.clusterTree.get(itemID)
				.objective.get(ObjectiveID).objectiveStatusKnown;			
		} else {
			getType =
				userObjective.clusterTree.get(itemID)
				.objective.get(itemID).objectiveStatusKnown;			
		}
		switch (getType) {
		case PASSED:
			return SequencingStatus.Passed;				
		case FAILED:
			return SequencingStatus.Failed;				
		case UNKNOWN:
			return SequencingStatus.Unknown;
		default:
			return null;				
		}
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
	public boolean updateObjectiveStatusKnown(
			final UserObjective userObjective,
			final SequencingStatus newValue) {
		int resultValue = NULL;
		boolean returnValue = true;
		
		if (newValue == null) {
			returnValue = false;
		} else {
			if (newValue.equals(SequencingStatus.Passed)) {
				resultValue = PASSED;
			} else if (newValue.equals(SequencingStatus.Failed)) {
				resultValue = FAILED;
			} else if (newValue.equals(SequencingStatus.Unknown)) {
				resultValue = UNKNOWN;
			} else {
				returnValue = false;
			}			
		}				
		if (ObjectiveID != null) {
			userObjective.clusterTree.get(itemID)
				.objective.get(ObjectiveID).objectiveStatusKnown = resultValue;
		} else {
			userObjective.clusterTree.get(itemID)
				.objective.get(itemID).objectiveStatusKnown = resultValue;
		}
		return returnValue;		
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
	public SequencingStatus satisfied(
			final UserObjective userObjective, 
			final boolean afirmative) {
		int getType = 0;
		if (hasMapInfo && mapInfo != null) {
			if (mapInfo.readSatisfiedStatus 
					&& mapInfo.targetObjectiveID != null) {
				getType =
					userObjective.globalObjectiveMap.
						objective.get(mapInfo.targetObjectiveID).satisfied;
			}
		} else {
			if (ObjectiveID != null) {
				getType =
					userObjective.clusterTree.get(itemID)
					.objective.get(ObjectiveID).satisfied;			
			} else {
				getType =
					userObjective.clusterTree.get(itemID)
					.objective.get(itemID).satisfied;			
			}
		}
		
		switch (getType) {
		case PASSED:
			if (afirmative) {
				return SequencingStatus.Passed;
			} else {
				return SequencingStatus.Failed;
			}
		case FAILED:
			if (afirmative) {
				return SequencingStatus.Failed;
			} else {
				return SequencingStatus.Passed;
			}
		case UNKNOWN:
			return SequencingStatus.Unknown;
		default:
			return null;
		}
	}
	/**
	 * Aquesta funció actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param enewValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
	public boolean updateSatisfied(
			final UserObjective userObjective,
			final SequencingStatus enewValue) {
		
		SequencingStatus newValue = enewValue;
		int resultValue = NULL;
		boolean returnValue = true;
		if (hasMapInfo && mapInfo != null) {
			if (mapInfo.readSatisfiedStatus 
					&& mapInfo.targetObjectiveID != null) {
				newValue = satisfied(userObjective, true);
			}
		}
		if (newValue == null) {
			returnValue = false;
		} else {
			if (newValue.equals(SequencingStatus.Passed)) {
				resultValue = PASSED;
			} else if (newValue.equals(SequencingStatus.Failed)) {
				resultValue = FAILED;
			} else if (newValue.equals(SequencingStatus.Unknown)) {
				resultValue = UNKNOWN;
			} else {
				returnValue = false;
			}			
		}
		
		if (hasMapInfo && mapInfo != null) {
			if (mapInfo.writeSatisfiedStatus 
					&& mapInfo.targetObjectiveID != null) {
				userObjective.globalObjectiveMap.
						objective.get(mapInfo.targetObjectiveID).satisfied =
							resultValue;
			}
		}
		
		if (ObjectiveID != null) {
			userObjective.clusterTree.get(itemID)
				.objective.get(ObjectiveID).satisfied = resultValue;
		} else {
			userObjective.clusterTree.get(itemID)
				.objective.get(itemID).satisfied = resultValue;
		}
		
		return returnValue;		
	}
	/**
	 * Amb aquesta funció ens solucionem la vida a l'hora de passar aquest
	 * valor entre classes que implementin aquesta interfície.
	 * 
	 * @return Double Type.
	 */
	public Double getMinNormalizedMeasure() {
		return minNormalizedMeasure;
	}
	/**
	 * Amb aquesta funció ens solucionem la vida a l'hora de passar aquest
	 * paràmetre entre classes que implementin aquesta interfície. 
	 * 
	 * @return boolean Type. 
	 */
	public boolean getSatisfiedByMeasure() {
		return satisfiedByMeasure;
	}
}

