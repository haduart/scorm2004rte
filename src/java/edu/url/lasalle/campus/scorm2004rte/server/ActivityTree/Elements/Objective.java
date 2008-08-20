// $Id: Objective.java,v 1.23 2008/04/22 18:18:07 ecespedes Exp $
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
	CMIDataModel;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.
	CMIObjectives;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.system.Constants.SequencingStatus;


/** <!-- Javadoc -->
 * $Id: Objective.java,v 1.23 2008/04/22 18:18:07 ecespedes Exp $
 * <b>Títol:</b> Objective <br><br>
 * <b>Descripció:</b> Aquesta classe farà el "linkatge" amb les dades<br> 
 * de l'usuari (UserObjectives, CMIDataModel i sobretot amb CMIObjectives).<br>
 *   
 * <br><br> 
 * @see SCORM2004 Sequencing & Navigation 3rt Edition
 * @author Eduard Céspedes i Borràs / LaSalle / ecespedes@salleurl.edu
 * @version Versió $Revision: 1.23 $ $Date: 2008/04/22 18:18:07 $
 * $Log: Objective.java,v $
 * Revision 1.23  2008/04/22 18:18:07  ecespedes
 * Arreglat bugs en el seqüenciament i els objectius secundaris mapejats.
 *
 * Revision 1.22  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral·lel amb el procés de desenvolupament del gestor de BD.
 *
 * Revision 1.21  2008/01/08 17:42:46  ecespedes
 * Actualitzant el model de dades a partir del seqüenciament.
 *
 * Revision 1.20  2008/01/08 11:50:21  ecespedes
 * Ultimes modificacions de l'OverallSequencingProcess
 *
 * Revision 1.19  2008/01/07 15:59:22  ecespedes
 * Implementat 'deliveryControls' i el 'hideLMSUI' del Presentation.
 *
 * Revision 1.18  2007/12/21 17:12:44  ecespedes
 * Implementant CourseManager.OverallSequencingProcess
 *
 * Revision 1.17  2007/12/20 20:45:53  ecespedes
 * Implementat l'Objective Map
 *
 * Revision 1.16  2007/12/17 15:27:48  ecespedes
 * Fent MapInfo. Bug en els Leaf_Items
 *
 * Revision 1.15  2007/12/13 15:25:12  ecespedes
 * Problemes amb el sistema d'arbre de clusters.
 * Falla l'ObjectiveStatusKnown.
 *
 * Revision 1.14  2007/12/11 16:00:43  ecespedes
 * Suprimint paràmetres: ObjectiveInterface el paràmetre RollupRule.
 *
 * Revision 1.13  2007/12/11 15:29:27  ecespedes
 * Arreglant bugs i optimitzant solucions.
 *
 * Revision 1.12  2007/12/10 11:50:27  ecespedes
 * Començant a juntar les peces del procés de seqüenciament.
 *
 * Revision 1.11  2007/12/09 22:32:16  ecespedes
 * Els objectius dels clusters es tracten i es guarden de manera diferent.
 *
 * Revision 1.10  2007/12/05 09:34:27  xgumara
 * Implementat mètode equals i hashCode.
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
public class Objective implements ObjectiveInterface, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6139565606550396854L;
	//		--		Constants		--	
	/**
	 * Constant String Type, "unknown".
	 */
    public static final String UNKNOWN           =   "unknown";
    /**
	 * Constant String Type, "failed".
	 */
    public static final String FAILED           =   "failed";
    /**
	 * Constant String Type, "passed".
	 */
    public static final String PASSED           =   "passed";
    /**
	 * Constant String Type, "incomplete".
	 */
    public static final String INCOMPLETE        =   "incomplete";
    /**
	 * Constant String Type, "completed".
	 */
    public static final String COMPLETED         =   "completed";
    /**
	 * Constant String Type, "not attempted".
	 */
    public static final String NOTATTEMPTED       =   "not attempted";
    
    
    //------ Constants per parsejar en cas de tindre un MAP -------
    /**
	 * És per no fer una relació amb el SequencingStatus.Unknown.
	 * int Type = 0
	 */
	private static final int UNKNOWN_MAP = 0;
	/**
	 * És per no fer una relació amb el SequencingStatus.Passed.
	 * int Type = 1
	 */
	private static final int PASSED_MAP = 1;
	/**
	 * És per no fer una relació amb el SequencingStatus.Failed.
	 * int Type = 2
	 */
	private static final int FAILED_MAP = 2;
	/**
	 * Aquest serà el valor inicial o en cas d'error s'assignarà 
	 * aquest valor a les variables. De manera que el seqüenciament
	 * quan rebi un -1 el que farà serà fer un rollup i actualitzar
	 * aquesta variable.
	 * 
	 * int Type = -1
	 */
	private static final int NULL_MAP = -1;
    
    
    //		--		Variables		--    
	/**
	 * És l'identificador de l'Item en el que estem.
	 */
	public String itemID = null;
	
	/**
	 * objectiveID (optional): És l’identificador associat amb una <br>
	 * activitat i serà únic. Si el primaryObjective conté un mapInfo, <br>
	 * aleshores aquest atribut és obligatori.
	 */
	public String ObjectiveID = null;
	
	/**
	 * Indica quan l'actual objectiu te un valor satisfactori. 
	 * Forma part de 'Objective Progress Information'
	 */
	//public boolean ObjectiveProgressStatus = false;	
	/**
	 * Indica si l'objectiu està satisfet. 
	 * Forma part de 'Objective Progress Information'
	 */
	//public boolean ObjectiveSatisfiedStatus = false;
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
	
	public MapInfo mapInfo = null;
	
	
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
	 * A aquesta funció li passarem tota la regla de RollupRule perquï¿½
	 * cada 'node' pugui agafar tots els paràmetres necessaris per 
	 * tractar la condició operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos últims valors també haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 
	public final SequencingStatus attempted(
			final UserObjective userObjective) {
		
		if (userObjective.dataModel.get(itemID).activityAttemptCount > 0) {
			return Constants.SequencingStatus.Passed;
		}
		/*
		if (ObjectiveID == null) {
			if (userObjective.dataModel.get(itemID).activityAttemptCount > 0) {
				return Constants.SequencingStatus.Passed;
			}
		} else {
			if (userObjective.dataModel.get(
					ObjectiveID).activityAttemptCount > 0) {
				return Constants.SequencingStatus.Passed;
			}
		}*/			
		return Constants.SequencingStatus.Failed;
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
		
		CMIDataModel userData = null;
		
		//if (ObjectiveID == null) {
		userData = userObjective.dataModel.get(itemID);
		//} else {
		//	userOData = userObjective.dataModel.get(itemID).
		//		cmiObjectives.get(ObjectiveID);
		//}		
		
		if (getAttemptProgressStatus(userData) 
				&& getAttemptCompletionStatus(userData)) {
			if (afirmative) {
				return SequencingStatus.Passed;
			} else {
				return SequencingStatus.Failed;
			}
		} else if (getAttemptProgressStatus(userData) 
				&& !getAttemptCompletionStatus(userData)) {
			if (afirmative) {
				return SequencingStatus.Failed;
			} else {
				return SequencingStatus.Passed;
			}
		} else {
			return Constants.SequencingStatus.Unknown;
		}
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si l'activitat estï¿½
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
		SequencingStatus returnValue = null;
		/**
		 * Està mapejat?? de ser així i fós per lectura aleshores no 
		 * analitzariem les dades de l'SCO i llegiriem directament les 
		 * del MAP.
		 */
		if (hasMapInfo && mapInfo != null) {
			if (mapInfo.readSatisfiedStatus 
					&& mapInfo.targetObjectiveID != null) {
				int getType = 
					userObjective.globalObjectiveMap.objective.get(
							mapInfo.targetObjectiveID).satisfied;
				switch (getType) {
				case PASSED_MAP:
					return SequencingStatus.Passed;				
				case FAILED_MAP:
					return SequencingStatus.Failed;				
				default:
					return SequencingStatus.Unknown;
				}
			}			
		}
		/**
		 * Cas especial, per guardar el valor de satisfet o no satisfet
		 * segons la mesura (SatisfiedByMeasure == true).
		 */
		if (satisfiedByMeasure && minNormalizedMeasure != null) {
			Double rMeasure = objectiveMeasure(userObjective);
			if (rMeasure != null) {
				if (rMeasure >= minNormalizedMeasure) {
					returnValue = SequencingStatus.Passed;
				} else {
					returnValue = SequencingStatus.Failed;
				}
			} else {
				returnValue = SequencingStatus.Unknown;
			}
		} else {
			CMIDataModel userData = null;
			//if (ObjectiveID == null) {
			userData = userObjective.dataModel.get(itemID);
			//} else {
				//userData = userObjective.dataModel.get(ObjectiveID);
			//}
			if (getObjectiveProgressStatus(userData) 
					&& getObjectiveSatisfiedStatus(userData)) {
				if (afirmative) {
					returnValue = SequencingStatus.Passed;
				} else {
					returnValue = SequencingStatus.Failed;
				}
			} else if (getObjectiveProgressStatus(userData) 
					&& !getObjectiveSatisfiedStatus(userData)) {
				if (afirmative) {
					returnValue = SequencingStatus.Failed;
				} else {
					returnValue = SequencingStatus.Passed;
				}
			} else {
				returnValue = Constants.SequencingStatus.Unknown;
			}
		}
		
		/**
		 * Està mapejat?? de ser així i fós per d'ESCRITURA aleshores 
		 * guardarem les dades de l'SCO a l'objecte del MAP.
		 */
		if (hasMapInfo && mapInfo != null) {
			if (mapInfo.writeSatisfiedStatus 
					&& mapInfo.targetObjectiveID != null) {
				
				int getType = 
					userObjective.globalObjectiveMap.objective.get(
							mapInfo.targetObjectiveID).satisfied;
				
				if (returnValue.equals(SequencingStatus.Passed)) {
					userObjective.globalObjectiveMap.objective.get(
							mapInfo.targetObjectiveID).satisfied =
								PASSED_MAP;
				} else if (returnValue.equals(SequencingStatus.Failed)) {
					userObjective.globalObjectiveMap.objective.get(
							mapInfo.targetObjectiveID).satisfied =
								FAILED_MAP;
				} else if (returnValue.equals(SequencingStatus.Unknown)) {
					userObjective.globalObjectiveMap.objective.get(
							mapInfo.targetObjectiveID).satisfied = 
								UNKNOWN_MAP;
				} else {
					userObjective.globalObjectiveMap.objective.get(
							mapInfo.targetObjectiveID).satisfied =
								NULL_MAP;
				}				
			}
		}
		return returnValue;
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si ï¿½s coneix 
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
	public final SequencingStatus activityProgressKnown(
			final UserObjective userObjective) {
		
		CMIDataModel userData = null;
		//if (ObjectiveID == null) {
			userData = userObjective.dataModel.get(itemID);
		//} else {
			//userData = userObjective.dataModel.get(itemID);
		//}
				
		if (attempted(userObjective).
				equals(SequencingStatus.Passed)) {
			if (userData.successStatus == null 
					&& ObjectiveID == null) {
				return SequencingStatus.Unknown;
			} 
			if (getAttemptProgressStatus(userData)) {
				return SequencingStatus.Passed;
			} else {
				return SequencingStatus.Failed;
			}
		} else {
			return SequencingStatus.Unknown;
		}
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
	 * @return Float: Puntuació amb un màxim de quatre decimals.
	*/ 	
	public final Double objectiveMeasure(
			final UserObjective userObjective) {
		
		Double returnType = null;
		CMIDataModel userData = null;
				
		if (Constants.DEBUG_NAVIGATION
				&& Constants.DEBUG_NAVIGATION_LOW) {
			System.out.println("[NAVIGATION]"
					+ "[" + itemID 
					+ "]Objective -> objectiveMeasure()");
		}
		
		if (hasMapInfo && mapInfo != null) {
			if (mapInfo.readNormalizedMeasure 
					&& mapInfo.targetObjectiveID != null) {
				Double getType = 
					userObjective.globalObjectiveMap.objective.get(
							mapInfo.targetObjectiveID).objectiveMeasure;
				return getType;
				
			}
		}
		if (objectiveMeasureKnown(userObjective)
				.equals(SequencingStatus.Passed)) {		
			
			//if (ObjectiveID == null) {
			userData = userObjective.dataModel.get(itemID);
			//} else {
				//userData = userObjective.dataModel.get(ObjectiveID);
			//}			
			returnType = getObjectiveMeasure(userData);
		} else {
			returnType = null;
		}
		
		if (hasMapInfo && mapInfo != null) {
			if (mapInfo.writeNormalizedMeasure 
					&& mapInfo.targetObjectiveID != null) {
				userObjective.globalObjectiveMap.objective.get(
							mapInfo.targetObjectiveID).objectiveMeasure =
								returnType;
				
			}
		}
		
		return returnType;
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
		
		CMIDataModel userData = null;
		
		//if (ObjectiveID == null) {
			userData = userObjective.dataModel.get(itemID);
		//} else {
			//userData = userObjective.dataModel.get(ObjectiveID);
		//}
		
		if (getObjectiveMeasureStatus(userData)) {
			return SequencingStatus.Passed;
		} else if (attempted(userObjective)
				.equals(SequencingStatus.Passed)) {
			return SequencingStatus.Failed;
		} else {
			return SequencingStatus.Unknown;			
		}
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si és coneix 
	 * l'estat de l'objectiu (passed), si no el coneixem (failed)
	 * o si no ho sabem (unknown).
	 * 
	 * A aquesta funció li passarem tota la regla de RollupRule perquè
	 * cada 'node' pugui agafar tots els parï¿½metres necessaris per 
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
				
		CMIDataModel userData = null;
		
		//if (ObjectiveID == null) {
			userData = userObjective.dataModel.get(itemID);
		//} else {
			//userData = userObjective.dataModel.get(ObjectiveID);
		//}
		
		if (getObjectiveProgressStatus(userData)) {
			return SequencingStatus.Passed;			
		} else {
			return SequencingStatus.Failed;
		}
		
	}
	/**
	 * Amb aquesta funció ens solucionem la vida a l'hora de passar aquest
	 * valor entre classes que implementin aquesta interfície.
	 * 
	 * @return Double Type.
	 */
	public final Double getMinNormalizedMeasure() {
		return minNormalizedMeasure;
	}
	/**
	 * Amb aquesta funció ens solucionem la vida a l'hora de passar aquest
	 * paràmetre entre classes que implementin aquesta interfície. 
	 * 
	 * @return boolean Type. 
	 */
	public final boolean getSatisfiedByMeasure() {
		return satisfiedByMeasure;
	}
	// 	---------------- Tractament Variables Tracking ---------------- {
	/**
	 * Ens retornarà la variable del "tracking" ObjectiveProgressStatus.
	 * 
	 * TODO: Actualitzar el cmi.completion_status quan rebem un SCO!!!
	 *  
	 * @see "4.2.4.1 Completion Status Evaluation". 
	 * SCORM 2004 Run-Time Environment (RTE) Version 1.3.2 - DRAFT 
	 *  
	 * @param userData : CMIDataModel Type.
	 * @return boolean Type.
	 */
	private boolean getObjectiveProgressStatus(
			final CMIDataModel userData) {
		
		if (ObjectiveID != null) {
			return getObjectiveProgressStatus(
					userData.cmiObjectives.get(ObjectiveID));
		} else if (!userData.successStatus.equals(UNKNOWN)) {
			return true;
		}
		/*if (!userData.successStatus.equals(UNKNOWN)) {
			return true;
		} else if (ObjectiveID != null) {
			return getObjectiveProgressStatus(
					userData.cmiObjectives.get(ObjectiveID));
		} */
		return false;		
	}
	/**
	 * Ens retornarà la variable del "tracking" ObjectiveProgressStatus.
	 * 
	 * @param userObj : CMIObjectives Type.
	 * @return boolean Type.
	 */
	private boolean getObjectiveProgressStatus(
			final CMIObjectives userObj) {
		if (userObj.successStatus == null) {
			return false;
		}
		return (!userObj.successStatus.equals(UNKNOWN));
		
	}	
	/**
	 * Ens retornarà la variable del "tracking" getObjectiveSatisfiedStatus.
	 *   
	 * @param userData : CMIDataModel Type.
	 * @return boolean Type.
	 * 
	 * @see SCORM 2004 Run-Time Environment (RTE) Version 1.3.2 - DRAFT
	 * @see SCORM 2004 Sequencing and Navigation (SN) Version 1.3.2 - DRAFT 
	 */
	private boolean getObjectiveSatisfiedStatus(
			final CMIDataModel userData) {
		if (ObjectiveID != null) {
			return getObjectiveSatisfiedStatus(
					userData.cmiObjectives.get(ObjectiveID));
		}
		if (userData.successStatus == null) {
			return false;
		}
		return (userData.successStatus.equals(PASSED)); 
				
	}
	/**
	 * Ens retornarà la variable del "tracking" getObjectiveSatisfiedStatus.
	 * 
	 * @param userObj : CMIObjectives Type.
	 * @return boolean Type.
	 */
	private boolean getObjectiveSatisfiedStatus(
			final CMIObjectives userObj) {
		if (userObj.successStatus == null) {
			return false;
		}
		return (userObj.successStatus.equals(PASSED));
		
	}
	
	/**
	 * Ens retornarà la variable del "tracking" getAttemptCompletionStatus.
	 *   
	 * @param userData : CMIDataModel Type.
	 * @return boolean Type.
	 * 
	 * @see SCORM 2004 Run-Time Environment (RTE) Version 1.3.2 - DRAFT
	 * @see SCORM 2004 Sequencing and Navigation (SN) Version 1.3.2 - DRAFT 
	 */
	private boolean getAttemptCompletionStatus(
			final CMIDataModel userData) {
		if (ObjectiveID != null) {
			return getAttemptCompletionStatus(
					userData.cmiObjectives.get(ObjectiveID));
		} else if (userData.completionStatus == null) {
			return false;
		}
		return (userData.completionStatus.equals(COMPLETED));
	}
	/**
	 * Ens retornarà la variable del "tracking" getAttemptCompletionStatus.
	 * 
	 * @param userObj : CMIObjectives Type.
	 * @return boolean Type.
	 */
	private boolean getAttemptCompletionStatus(
			final CMIObjectives userObj) {
		if (userObj.completionStatus == null) {
			return false;
		}
		return (userObj.completionStatus.equals(COMPLETED));
		
	}	
	/**
	 * Ens retornarà la variable del "tracking" getAttemptProgressStatus.
	 *   
	 * @param userData : CMIDataModel Type.
	 * @return boolean Type.
	 * 
	 * @see SCORM 2004 Run-Time Environment (RTE) Version 1.3.2 - DRAFT
	 * @see SCORM 2004 Sequencing and Navigation (SN) Version 1.3.2 - DRAFT 
	 */
	private boolean getAttemptProgressStatus(
			final CMIDataModel userData) {
		if (userData.successStatus == null 
				&& ObjectiveID == null) {
			return false;
		} else if (ObjectiveID != null) {
			return getAttemptProgressStatus(
					userData.cmiObjectives.get(ObjectiveID));
		} else if (!userData.successStatus.equals(UNKNOWN)) {
			return true;
		} 
		return false;		
	}
	/**
	 * Ens retornarà la variable del "tracking" getAttemptCompletionStatus.
	 * 
	 * @param userObj : CMIObjectives Type.
	 * @return boolean Type.
	 */
	private boolean getAttemptProgressStatus(
			final CMIObjectives userObj) {
		if (userObj.successStatus == null) {
			return false;
		} else {
			return (!userObj.successStatus.equals(UNKNOWN));
		}
		/*
		if (userObj.successStatus == null) {
			return false;
		}
		return (!userObj.successStatus.equals(UNKNOWN));
		*/
	}
	/**
	 * Ens retornarà la variable del "tracking" ObjectiveMeasureStatus.
	 *   
	 * @param userData : CMIDataModel Type.
	 * @return boolean Type.
	 * 
	 * @see SCORM 2004 Run-Time Environment (RTE) Version 1.3.2 - DRAFT
	 * @see SCORM 2004 Sequencing and Navigation (SN) Version 1.3.2 - DRAFT 
	 */
	private boolean getObjectiveMeasureStatus(
			final CMIDataModel userData) {
		
		if (userData.scoreScaled == null) {
			return false;
		} else if (ObjectiveID != null) {
			return getObjectiveMeasureStatus(
					userData.cmiObjectives.get(ObjectiveID));
		}
		if (!userData.scoreScaled.equals(UNKNOWN)) {
			return true;
		} 
		
		return ((!userData.scoreRaw.equals(UNKNOWN))
				&& (userData.scoreMin != null)
				&& (userData.scoreMax != null));
	}
	/**
	 * Ens retornarà la variable del "tracking" ObjectiveMeasureStatus.
	 *   
	 * @param userObj : CMIObjectives Type.
	 * @return boolean Type. 
	 */
	private boolean getObjectiveMeasureStatus(
			final CMIObjectives userObj) {
		if (userObj.scoreScaled == null) {
			return false;
		}
		if (!userObj.scoreScaled.equals(UNKNOWN)) {
			return true;
		} else {
			return  ((!userObj.scoreRaw.equals(UNKNOWN))
				&& (userObj.scoreMin != null)
				&& (userObj.scoreMax != null));
		}
	}
	/**
	 * Ens retornarà la variable del "tracking" ObjectiveMeasureStatus.
	 *   
	 * @param userData : CMIDataModel Type.
	 * @return boolean Type.
	 * 
	 * @see SCORM 2004 Run-Time Environment (RTE) Version 1.3.2 - DRAFT
	 * @see SCORM 2004 Sequencing and Navigation (SN) Version 1.3.2 - DRAFT 
	 */
	private Double getObjectiveMeasure(
			final CMIDataModel userData) {
		
		if (getObjectiveMeasureStatus(userData)) {
			if (!userData.scoreScaled.equals(UNKNOWN) 
					&& (userData.scoreScaled.length() > 0)) {
				Double newDouble = 
					new Double(userData.scoreScaled);
				return newDouble;
			} else if ((!userData.scoreRaw.equals(UNKNOWN))
					&& (userData.scoreMin != null)
					&& (userData.scoreMin.length() > 0)
					&& (userData.scoreMax != null)
					&& (userData.scoreMax.length() > 0)) {
				Double scoreMin = new Double(userData.scoreMin);
				Double scoreMax = new Double(userData.scoreMax);
				Double scoreRaw = new Double(userData.scoreRaw);
				
				Double rangeValue = scoreMax - scoreMin;
				
				return ((scoreRaw / rangeValue) * 2) - 1;
			} else {
				//if (ObjectiveID != null) {
					return getObjectiveMeasure(
							userData.cmiObjectives.get(ObjectiveID));
				//} else {
					//return getObjectiveMeasure(
						//	userData.cmiObjectives.get(itemID));
				//}				
			}			
		}
		return 0.0;		
	}
	/**
	 * Ens retornarà la variable del "tracking" ObjectiveMeasureStatus.
	 *   
	 * @param userObj : CMIObjectives Type.
	 * @return boolean Type. 
	 */
	private Double getObjectiveMeasure(
			final CMIObjectives userObj) {		
		if (!userObj.scoreScaled.equals(UNKNOWN)
				&& userObj.scoreScaled.length() > 0) {
			Double newDouble = 
				new Double(userObj.scoreScaled);
			return newDouble;
		} else if ((!userObj.scoreRaw.equals(UNKNOWN)
				&& (userObj.scoreRaw.length() > 0))
				&& (userObj.scoreMin != null)
				&& (userObj.scoreMin.length() > 0)
				&& (userObj.scoreMax != null)
				&& (userObj.scoreMax.length() > 0)) {
			Double scoreMin = new Double(userObj.scoreMin);
			Double scoreMax = new Double(userObj.scoreMax);
			Double scoreRaw = new Double(userObj.scoreRaw);				
			
			Double rangeValue = scoreMax - scoreMin;
			
			return ((scoreRaw / rangeValue) * 2) - 1;
		} else {
			return 0.0;
		}	
	}	
	// 	} ---------------- END-Tractament Variables Tracking ----------------
	
	
/* 
    public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (obj.getClass() != this.getClass())) {
                return false;
            }
            Objective objective = (Objective) obj;
            return (ObjectiveID.equals(objective.ObjectiveID)
                    && ObjectiveMeasureStatus 
                    	== objective.ObjectiveMeasureStatus
                    && ObjectiveNormalizedMeasure 
                    	== objective.ObjectiveNormalizedMeasure
                    && ObjectiveProgressStatus 
                    	== objective.ObjectiveProgressStatus
                    && ObjectiveSatisfiedStatus 
                    	== objective.ObjectiveSatisfiedStatus                   
                    && hasMapInfo == objective.hasMapInfo
                    && itemID.equals(itemID)                    
                    );
        }
        
        @Override
        public int hashCode() {            
            return System.identityHashCode(this);
        }
        */
}

