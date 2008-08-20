// $Id: Sequencing.java,v 1.28 2008/04/22 18:18:07 ecespedes Exp $
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Abstract_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Root_Item;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
	SequencingCondition.conditionType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.SequencingRules.conditionCombinationType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.SequencingRules.conditionRuleType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.SequencingRules.ruleActionType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Types.ChildActivitySetType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Types.ConditionType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Types.OperatorType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Types.RandomizationType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Types.RollupActionType;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.
	UserObjective;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.
	TreeAnnotations.viewType;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.system.Constants.SequencingStatus;

/** <!-- Javadoc -->
 * $Id: Sequencing.java,v 1.28 2008/04/22 18:18:07 ecespedes Exp $
 * <b>Títol:</b> Sequencing <br><br>
 * <b>Descripció:</b> Aquesta classe conté tots els elements <br> 
 * del seqüenciament.<br><br> 
 *  
 * @see SCORM2004 Sequencing & Navigation 3rt Edition
 * @author Eduard Céspedes i Borràs / LaSalle / ecespedes@salle.url.edu
 * @version Versió $Revision: 1.28 $ $Date: 2008/04/22 18:18:07 $
 * $Log: Sequencing.java,v $
 * Revision 1.28  2008/04/22 18:18:07  ecespedes
 * Arreglat bugs en el seqüenciament i els objectius secundaris mapejats.
 *
 * Revision 1.27  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral·lel amb el procés de desenvolupament del gestor de BD.
 *
 * Revision 1.26  2008/01/08 11:50:21  ecespedes
 * Ultimes modificacions de l'OverallSequencingProcess
 *
 * Revision 1.25  2008/01/07 15:59:22  ecespedes
 * Implementat 'deliveryControls' i el 'hideLMSUI' del Presentation.
 *
 * Revision 1.24  2007/12/20 20:45:53  ecespedes
 * Implementat l'Objective Map
 *
 * Revision 1.23  2007/12/17 15:27:48  ecespedes
 * Fent MapInfo. Bug en els Leaf_Items
 *
 * Revision 1.22  2007/12/14 12:56:13  ecespedes
 * Solucionat bugs i problemes derivats del nou concepte 'ObjectiveCluster'
 *
 * Revision 1.21  2007/12/13 15:25:12  ecespedes
 * Problemes amb el sistema d'arbre de clusters.
 * Falla l'ObjectiveStatusKnown.
 *
 * Revision 1.20  2007/12/11 16:00:43  ecespedes
 * Suprimint paràmetres: ObjectiveInterface el paràmetre RollupRule.
 *
 * Revision 1.19  2007/12/11 15:29:27  ecespedes
 * Arreglant bugs i optimitzant solucions.
 *
 * Revision 1.18  2007/12/10 22:03:32  ecespedes
 * Implementant les funcions per buscar un item concret i retornar-lo
 * al CourseManager perquè el tracti.
 *
 * Revision 1.17  2007/12/10 11:50:27  ecespedes
 * Començant a juntar les peces del procés de seqüenciament.
 *
 * Revision 1.16  2007/12/09 22:32:16  ecespedes
 * Els objectius dels clusters es tracten i es guarden de manera diferent.
 *
 * Revision 1.15  2007/12/05 13:36:59  ecespedes
 * Arreglat bug limitConditions i PreConditions
 *
 * Revision 1.14  2007/12/04 15:47:14  ecespedes
 * limitConditions implementat i preConditions testejat.
 *
 * Revision 1.13  2007/12/03 17:58:51  ecespedes
 * Implementat un attemptCount que ens servirà per l'attemptLimit.
 *
 * Revision 1.12  2007/11/30 13:29:18  ecespedes
 * SequencingRules implementades!
 *
 * Revision 1.11  2007/11/29 15:54:35  ecespedes
 * Implementant les SequencingRules (part 1)
 *
 * Revision 1.10  2007/11/27 15:34:25  ecespedes
 * Arreglats bugs relacionats amb el Rollup. Creat nou joc de testos.
 *
 * Revision 1.9  2007/11/26 15:12:36  ecespedes
 * Ja controlem el Progress Measure.
 *
 * Revision 1.8  2007/11/20 15:24:34  ecespedes
 * Les RollupRules ja estan implementades.
 * Modificació del ObjectiveInterface.
 *
 * Revision 1.7  2007/11/19 15:26:58  ecespedes
 * Treballant sobre el RollupRule (2 de 3)
 *
 * Revision 1.6  2007/11/19 07:34:15  ecespedes
 * Millores en els RollupRules.
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
public class Sequencing implements ObjectiveInterface, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8215365028982303477L;

	/**
	 * És l'identificador de l'Item en el que estem.
	 */	
	public Abstract_Item itemProperty = null;
	
	/**
	 * Només s’ha d’especificar quan formi part d’un <sequencingCollection />.
	 */
	public String ID;
	/**
	 * Fa referència a un identificador únic 
	 * (identificador únic que estarà dintre d’un <sequencingCollection />).
	 */
	public String IDRef = null;
	
	/**
	 * Objectes primaris, secundaris i mapInfos a objectes globals estaràn aquí.
	 */	
	public ObjectiveHandler objectiveHandler = null;
	/**
	 * Element DeliveryControls
	 * 3.13.1. Tracked 
	 * 		The Tracked element indicates whether any tracking status 
	 * 		information (refer to Section 4.2: Tracking Model) is being
	 * 		managed for the activity.  This element contains a boolean
	 * 		(True/False) value.  The default value for Tracked, if not 
	 * 		defined explicitly for the activity, is If the Tracked element 
	 * 		on an activity is defined as False, the LMS will behave as if it
	 * 		does not initialize, manage or access any tracking status 
	 * 		information for the activity.  All evaluations requiring 
	 * 		tracking status information will receive the default (“unknown”).
	 * 
	 * Nota! Per defecte el valor ha de ser "True" i si és marca
	 * com a "False" aleshores no farem cas de cap condició i retornarem
	 * a tot "UNKNOWN". 
	 *  	
	 */
	public boolean tracked = true;
	/**
	 * Totes les regles de sequenciament.
	 */
	public Collection < SequencingRules > sequencingRules =
		new ArrayList < SequencingRules > ();
	/**
	 * Totes les regles de Rollups.
	 */
	public Collection < RollupRule > rollupRule =
		new ArrayList < RollupRule >();
	/**
	 * Forma part del <b>RollupRules</b> i a vegada els tres paràmetres
	 * (rollupObjectiveSatisfied, rollupObjectiveMeasureWeight i 
	 * rollupProgressCompletion) creen el que s'anomena <i>Rollup 
	 * Controls</i>. <br /> <br />
	 * <b>rollupObjectiveSatisfied</b>, <i>default True</i>.<br />
	 * Indicates whether the activity contributes to the <br />
	 * evaluation of its parent’s Satisfied and Not Satisfied <br />
	 * Rollup Rules. 
	 */
	public boolean rollupObjectiveSatisfied = true;
	/**
	 * Forma part del <b>RollupRules</b> i a vegada els tres paràmetres
	 * (rollupObjectiveSatisfied, rollupObjectiveMeasureWeight 
	 * i rollupProgressCompletion) creen el que s'anomena <i>Rollup 
	 * Controls</i>. <br /><br />
	 * <b>rollupObjectiveMeasureWeight</b>, <i>default 1.0</i>.<br />
	 * A weighting factor applied to the Objective <br />
	 * Normalized Measure for the objective (which has <br />
	 * the Objective Contributes to Rollup equal to True) <br />
	 * associated with the activity used during rollup for <br />
	 * the parent activity. <br />
	 * AKA:objectiveMeasureWeight
	 */
	public double rollupObjectiveMeasureWeight = 1.0;
	/**
	 * Forma part del <b>RollupRules</b> i a vegada els tres paràmetres
	 * (rollupObjectiveSatisfied, rollupObjectiveMeasureWeight i
	 *  rollupProgressCompletion) creen el que s'anomena <i>Rollup 
	 * Controls</i>. <br /><br />
	 * <b>rollupProgressCompletion</b>, <i>default True</i>.<br />
	 * Indicates whether the activity contributes to the <br />
	 * evaluation of its parent’s Completed and Incomplete <br />
	 * Rollup Rules. 
	 */
	public boolean rollupProgressCompletion = true;
	/**
	 * El control mode. Si no n'hi ha serà null.
	 */
	public AdlseqControlMode adlseqControlMode = null;
	
	/**
	 * <b>attemptLimit</b> (opcional, default 0): <br />
	 * Aquest valor indica el màxim nombre d’intents per l’activitat. <br />
	 */
	public int attemptLimit = 0;
	/**
	 * <b>attemptAbsoluteDurationLimit</b> (opcional, default 0.0):<br /> 
	 * S’especifica la duració màxima que se li permet a un alumne <br />
	 * perdre en un intent d’una activitat. Aquest límit només s’aplica <br />
	 * mentres l’alumne està interactuant amb l’activitat, no mentres <br />
	 * l’activitat està suspesa. <br />
	 * <br />
	 * The Attempt Absolute Duration Limit element is included strictly <br />
	 * to enable the activity’s associated SCO’s 
	 * <b>cmi.max_time_allowed</b> <br /> 
	 * run-time data model element to be initialized.<br />
	 */
	public Duration attemptAbsoluteDurationLimit = null;
			
	/**
	 * <b>randomizationTiming </b> <i>(optional, default = never)</i>.
	 * This attribute indicates<br />
	 * when the ordering of the children of the activity should 
	 * occur [5].<br />  
	 * XML Data Type: xs:token. <br />
	 * The attribute contains a value of one of the following tokens:<br />
	 * <i>never, once, onEachNewAttempt</i> <br />
	 * <i>AKA: selectionTiming</i><br />
	 * 
	 * <b>Nota!</b>randomizationTiming i selectionTiming no son exactament<br />
	 * el mateix però per nosaltres ho serà. <br />
	 * @author Eduard Céspedes i Borràs
	 */
	public RandomizationType randomizationTiming = RandomizationType.never;
	/**
	 * Si randomizationTiming és "Once" aleshores utilitzarem aquesta variable
	 * per marcar si ja ha estat randomizat o no. 
	 * @author Eduard Céspedes i Borràs.
	 * 
	 * TODO: Hauria d'estar enmagatzemat en el UserObjective!
	 */
	public boolean isAlreadyRandomized = false;
	/**
	 *
	 * És un booleà per indicar si reordenem o no. O sigui, per saber si <br />
	 * l'Element <imsss:randomizationControls /> s'aplica a aquest 
	 * seqüenciament.<br />
	 * 
	 * <b>Nota!</b>Només és aplicable a clusters!<br />
	 * @author Eduard Céspedes i Borràs<br />
	 * <br />
	 * <b>reorderChildren </b><i>(optional,  default value = false)</i>.
	 * This attribute indicates<br />
	 * that the order of the child activities is randomized [5].<br />  
	 * XML Data Type: <br />
	 * xs:boolean. <br />
	 */
	public boolean reorderChildren = false;
	/**
	 * <b>SelectCount</b> <i>(optional, default 0)</i>: <br />
	 * Aquest atribut ens indica el número de fills que reordenarem <br />
	 * dintre de tot el conjunt. <br />
	 * Forma part del <b><imsss:randomizationControls /></b>
	 * @author Eduard Céspedes i Borràs
	 */
	public int selectCount = 0;
	
	/**
	 * <br />
	 * If an activity is included in the rollup evaluation but it’s <br /> 
	 * tracking status information being evaluated (Rollup Condition) <br />
	 * is “unknown”, then, in most cases, the rollup evaluation "unknown”<br />
	 *  value.  Through implementation and community feedback, will<br />
	 *  result in an ADL discovered this behavior was too strict <br />
	 * for many common rollup scenarios.  ADL has defined a set of Rollup <br />
	 * Consideration Controls as defined in Table 3.9a that further <br />
	 * refine the conditions under which an activity contributes to the <br />
	 * rollup of its parent. 
	 *  
	 */
	public RollupConsiderations rollupConsiderations =
		new RollupConsiderations();
	
	/**
	 * Constructor per defecte, més que rés perquè funcioni
	 * en el parser.
	 *
	 */
	public Sequencing() {
		
	}
	/**
	 * Constructor on l'hi passem directament l'Item
	 * "pare" des del que el llancem. 
	 * 
	 * @param nouItem : Abstract_Item Type
	 */
	public Sequencing(final Abstract_Item nouItem) {
		itemProperty = nouItem;
	}
	/**
	 * Precondition Actions:  apply when traversing the
	 * Activity Tree to identify a activity for delivery.
	 *  
	 * @param userObjective : UserObjective Type
	 * @return ruleActionType
	 */
	public final ruleActionType preConditionRule(
			final UserObjective userObjective) {
		
		boolean catchLimitCondition = false;
		
		Collection < SequencingRules > collectByCondition =
			sequencingRuleSearcher(conditionRuleType.preConditionRule);
		/**
		 * Aquest Hash ens serà útil si tenim més d'una 
		 * preConditionRule que es satisfà, aleshores haurem 
		 * d'agafar la que tingui la prioritat més alta, o sigui
		 * el que tingui la clau més petita.  
		 */
		HashMap < Integer, ruleActionType > ruleActionTypes =
			new HashMap < Integer, ruleActionType >();
		
		for (Iterator iterSeqRules = collectByCondition.iterator();
		iterSeqRules.hasNext();) {
			SequencingRules seqRule =
				(SequencingRules) iterSeqRules.next();
			
			boolean returnState = false;
			
			for (Iterator iterCondition = seqRule.condition.iterator();
			iterCondition.hasNext();) {
				SequencingCondition newCondition = 
					(SequencingCondition) iterCondition.next();
				/**
				 * Marquem que en cas d'haver-hi un limitCondition
				 * l'estem tractant amb una regla de seqüenciament.
				 */
				if (newCondition.condition 
						== conditionType.attemptLimitExceeded) {
					catchLimitCondition = true;
				}
				returnState = 
					evaluateConditionRule(userObjective, newCondition);
				if (returnState 
						&& (seqRule.conditionCombination 
								== conditionCombinationType.any)) {
					break;					
				} else if (!returnState 
						&& (seqRule.conditionCombination 
								== conditionCombinationType.all)) {
					break;					
				}
				
			}
			if (returnState) {
				/**
				 * Mirem quina acció és tracta i li assignem
				 * la prioritat. I per ser el preCondition
				 * podem executar dues accions diferents:
				 * { skip | stopForwardTraversal } &&
				 * { hiddenFromChoice | disabled }
				 */				
				if (seqRule.ruleAction 
						== ruleActionType.hiddenFromChoice) {
					changeVisibility(itemProperty.getIdentifier(),
							userObjective, viewType.notVisible);
					ruleActionTypes.put(
							3, ruleActionType.skip);
							//3, ruleActionType.hiddenFromChoice);
				} else if ((seqRule.ruleAction 
						== ruleActionType.disabled)
						&& (userObjective.treeAnnotations.get(
								itemProperty.getIdentifier()).currentView
								!= viewType.notVisible)) {
					changeVisibility(itemProperty.getIdentifier(),
							userObjective, viewType.disabled);	
					ruleActionTypes.put(
							3, ruleActionType.skip);
							//4, ruleActionType.disabled);
				} else if (seqRule.ruleAction 
						== ruleActionType.stopForwardTransversal) {
					ruleActionTypes.put(
							2, ruleActionType.stopForwardTransversal);
				} else if (seqRule.ruleAction 
						== ruleActionType.skip) {
					ruleActionTypes.put(
							1, ruleActionType.skip);					
				}
			}			
		}
		/**
		 * Si ningú no ha deshabilitat el contingut aleshores doncs
		 * ho mirem nosaltres a veure si tenim les condicions perquè 
		 * tinguem un limitConditions.
		 * Nola! recordar que un limitConditions sense seqüenciament
		 * que el controli actuarà per defecte deshabilitant el
		 * contingut.
		 */
		if (!catchLimitCondition) {
			if ((attemptLimit > 0)
					|| (attemptAbsoluteDurationLimit != null)) {
				if (limitCondition(userObjective)) {				
					if (userObjective.treeAnnotations.get(
							itemProperty.getIdentifier()).currentView
							!= viewType.notVisible) {
								changeVisibility(itemProperty.getIdentifier(),
										userObjective, viewType.disabled);	
								ruleActionTypes.put(
										3, ruleActionType.skip);
										//4, ruleActionType.disabled);
							}
				}
			}			
		}		
		
		if (ruleActionTypes.size() > 0) {
			return ruleActionTypes.get(
					ruleActionTypes.keySet().iterator().next());
		}		
		return null;
	}
	/**
	 * Post condition Actions:  apply when an attempt on 
	 * an activity terminates. 
	 * 
	 * @param userObjective : UserObjective Type
	 * @return ruleActionType
	 */
	public final ruleActionType postConditionRule(
			final UserObjective userObjective) {		
		
		Collection < SequencingRules > collectByCondition =
			sequencingRuleSearcher(conditionRuleType.postConditionRule);
		/**
		 * Aquest Hash ens serà útil si tenim més d'una 
		 * postConditionRule que es satisfà, aleshores haurem 
		 * d'agafar la que tingui la prioritat més alta, o sigui
		 * el que tingui la clau més petita.  
		 */
		HashMap < Integer, ruleActionType > ruleActionTypes =
			new HashMap < Integer, ruleActionType >();
		
		for (Iterator iterSeqRules = collectByCondition.iterator();
		iterSeqRules.hasNext();) {
			SequencingRules seqRule =
				(SequencingRules) iterSeqRules.next();
			
			boolean returnState = false;
			
			for (Iterator iterCondition = seqRule.condition.iterator();
			iterCondition.hasNext();) {
				SequencingCondition newCondition = 
					(SequencingCondition) iterCondition.next();
				
				returnState = 
					evaluateConditionRule(userObjective, newCondition);
				if (returnState 
						&& (seqRule.conditionCombination 
								== conditionCombinationType.any)) {
					break;					
				} else if (!returnState 
						&& (seqRule.conditionCombination 
								== conditionCombinationType.all)) {
					break;					
				}
				
			}
			if (returnState) {
				/**
				 * Mirem quina acció és tracta i li assignem
				 * la prioritat:
				 * Continue < Previous < Retry < RetryAll 
				 * 		< ExitParent < ExitAll
				 */				
				if (seqRule.ruleAction 
						== ruleActionType.exitAll) {
					ruleActionTypes.put(
							1, ruleActionType.exitAll);
				} else if (seqRule.ruleAction 
						== ruleActionType.exitParent) {
					ruleActionTypes.put(
							2, ruleActionType.exitParent);					
				} else if (seqRule.ruleAction 
						== ruleActionType.retryAll) {
					ruleActionTypes.put(
							3, ruleActionType.retryAll);
				} else if (seqRule.ruleAction 
						== ruleActionType.retry) {
					ruleActionTypes.put(
							4, ruleActionType.retry);
				} else if (seqRule.ruleAction 
						== ruleActionType.previous) {
					ruleActionTypes.put(
							5, ruleActionType.previous);
				} else if (seqRule.ruleAction 
						== ruleActionType._continue) {
					ruleActionTypes.put(
							6, ruleActionType._continue);
				}
			}			
		}
		if (ruleActionTypes.size() > 0) {
			return ruleActionTypes.get(
					ruleActionTypes.keySet().iterator().next());
		}		
		return null;
	}
	/**
	 * Exit Actions:  apply after a descendant 
	 * activity’s attempt terminates. 
	 * 
	 * @param userObjective : UserObjective Type
	 * @return ruleActionType
	 */
	public final ruleActionType exitConditionRule(
			final UserObjective userObjective) {
		
		Collection < SequencingRules > collectByCondition =
			sequencingRuleSearcher(conditionRuleType.exitConditionRule);
		
		for (Iterator iterSeqRules = collectByCondition.iterator();
		iterSeqRules.hasNext();) {
			SequencingRules seqRule =
				(SequencingRules) iterSeqRules.next();
			
			boolean returnState = false;
			
			for (Iterator iterCondition = seqRule.condition.iterator();
			iterCondition.hasNext();) {
				SequencingCondition newCondition = 
					(SequencingCondition) iterCondition.next();
				
				returnState = 
					evaluateConditionRule(userObjective, newCondition);
				if (returnState 
						&& (seqRule.conditionCombination 
								== conditionCombinationType.any)) {
					break;					
				} else if (!returnState 
						&& (seqRule.conditionCombination 
								== conditionCombinationType.all)) {
					break;					
				}				
			}
			if (returnState) {
				return ruleActionType.exit;
			}						
		}				
		return null;
	}
	/**
	 * Aquesta funció ens buscarà en el RollupRules i ens retornarà 
	 * un Collection amb totes les regles que com a resultat de la seva
	 * condició fasin un action del tipus que estem buscant.
	 * 
	 * @param conditionType : RollupActionType
	 * @return Collection < RollupRule > : Conjunt de resposta.
	 */
	private Collection < SequencingRules > sequencingRuleSearcher(
			final conditionRuleType conditionType) {
		
		Collection < SequencingRules > collectByCondition =
			new ArrayList < SequencingRules > ();
		
		for (Iterator seqIterator = sequencingRules.iterator();
		seqIterator.hasNext();) {
			SequencingRules newSeq = (SequencingRules) seqIterator.next();
			if (newSeq.conditionRule == conditionType) {
				collectByCondition.add(newSeq);
			}
		}						
		return collectByCondition;		
	}
	/**
	 * Modifiquem la visibilitat que tenim guardada en el TreeAnnotations.
	 * 
	 * @param identificator : String Type.
	 * @param userObjective : UserObjective Type.
	 * @param newView : viewType.
	 */
	private void changeVisibility(
			final String identificator,
			final UserObjective userObjective,
			final viewType newView) {
		
		userObjective.treeAnnotations.get(
				identificator).currentView 
				= newView;
		
		if (Constants.DEBUG_NAVIGATION && Constants.DEBUG_WARNINGS_LOW) {
			System.out.println("\t\t[NAVIGATION] changeVisibility(" 
					+ identificator + ")-> " + newView.toString());
		}
		
		if (userObjective.treeAnnotations.get(
				identificator).sons.size() > 0) {
			for (Iterator iterSons = userObjective.treeAnnotations.get(
				identificator).sons.iterator();
			iterSons.hasNext();) {
				String nwSon = (String) iterSons.next();
				
				changeVisibility(nwSon, userObjective, newView);
				
			}
		}
		
	}
	/**
	 * A partir d'una conditionRule (SequencingCondition class)
	 * el que farem serà mirar quina condició és i després fer 
	 * una búsqueda per l'arbre per veure si la satisfà, retorna
	 * un booleà. 
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newCondition : SequencingCondition Type
	 * @return boolean Type
	 */
	private boolean evaluateConditionRule(
			final UserObjective userObjective,
			final SequencingCondition newCondition) {		
				
		/**
		 * Analitzem la condició i anem creant
		 * la RollupRule amb els paràmetres que 
		 * necessitem.
		 */
		SequencingStatus rState = null;
				
		boolean afirmative = true;
		if (newCondition.operator == OperatorType.not) {
			afirmative = false;
		}
		RollupCondition tmpRollupCondition = new RollupCondition();
		tmpRollupCondition.operator = newCondition.operator;		
		/**
		 * always
		 */	
		if (newCondition.condition == conditionType.always) {
			return afirmative;
			/**
			 * attempted
			 */			
		} else if (newCondition.condition == conditionType.attempted) {
			rState = attempted(userObjective);					
			/**
			 * completed
			 */	
		} else if (newCondition.condition == conditionType.completed) {
			rState =
				completed(userObjective, true);
			/**
			 * satisfied
			 */						
		} else if (newCondition.condition == conditionType.satisfied) {			
			rState =
				satisfied(userObjective, true);
			/**
			 * objectiveMeasureGreaterThan
			 */						
		} else if (newCondition.condition 
				== conditionType.objectiveMeasureGreaterThan) {			
			Double result =
				objectiveMeasure(userObjective);
						if ((result == null) && afirmative) {
				return false;
			} else if ((result == null) && !afirmative) {
				return true;
			} else {
				return afirmative && (result > newCondition.measureThreshold); 
			}
			/**
			 * objectiveMeasureLessThan
			 */
		} else if (newCondition.condition 
				== conditionType.objectiveMeasureLessThan) {
			Double result =
				objectiveMeasure(userObjective);
			if ((result == null) && afirmative) {
				return false;
			} else if ((result == null) && !afirmative) {
				return true;
			} else {
				return afirmative && (result < newCondition.measureThreshold); 
			}
			/**
			 * activityProgressKnown
			 */
		} else if (newCondition.condition 
				== conditionType.activityProgressKnown) {			
			rState =
				activityProgressKnown(userObjective);			
			/**
			 * objectiveMeasureKnown
			 */	
		} else if (newCondition.condition 
				== conditionType.objectiveMeasureKnown) {			
			rState =
				objectiveMeasureKnown(userObjective);
			/**
			 * objectiveStatusKnown
			 */			
		} else if (newCondition.condition 
				== conditionType.objectiveStatusKnown) {			
			rState =
				objectiveStatusKnown(userObjective);	
			/**
			 * attemptLimitExceeded
			 */
		} else if (newCondition.condition 
				== conditionType.attemptLimitExceeded) {						
			if (limitCondition(userObjective)) {
				return afirmative;
			} else {
				return !afirmative;
			}
		} else {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] evaluateConditionRule: '" 
						+ newCondition.condition.toString() 
						+ "' -> No controlat!");
			}			
		}
		if (rState == null) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] evaluateConditionRule: '" 
						+ newCondition.condition.toString() 
						+ "' -> No controlat!");
			}
			return false;			
		}
		
		/**
		 * TODO: Optimitzar!
		 */
		if ((rState == SequencingStatus.Passed)
				&& afirmative) {
			return true;
		} else if ((rState == SequencingStatus.Passed)
				&& !afirmative) {
			return false;
		} else {
			return ((rState != SequencingStatus.Passed)
					&& !afirmative);
		}
	}
	/**
	 * Funció privada que el que fà és generar un 
	 * objectiveHandler per defecte perquè "parsegi"
	 * les dades de l'usuari.
	 * 
	 * @return ObjectiveInterface Type
	 */
	public ObjectiveHandler getObjectiveHandler() { 
		if (objectiveHandler == null) {
			objectiveHandler = new ObjectiveHandler();
			if (itemProperty.getIsLeaf()) {
				Objective primaryObjective = new Objective();
				primaryObjective.hasMapInfo = false;
				primaryObjective.ObjectiveID = null;
				primaryObjective.itemID = 
					itemProperty.getIdentifier();
				objectiveHandler.setPrimaryObjective(primaryObjective);
				//return primaryObjective;
			} else {
				ObjectiveCluster primarCluster = new ObjectiveCluster();
				primarCluster.hasMapInfo = false;
				primarCluster.ObjectiveID = null;
				primarCluster.itemID = 
					itemProperty.getIdentifier();
				objectiveHandler.setPrimaryObjective(primarCluster);
				//return primarCluster;
			}			
		}
		return objectiveHandler;			
				
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
	 * L'element useCurrentAttemptObjectiveInfo del ControlMode:
	 * 		Serà per marcar si és pot utilitzar la informació sobre els
	 * 		accessos. Si està a false retornariem sempre "UNKNOWN".
	 *  
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	 */
	public final SequencingStatus attempted(
			final UserObjective userObjective) {
		/**
		 * TODO: !tracked
		 */
		if (!tracked) {
			return SequencingStatus.Unknown;
		}
		if (getControlMode().useCurrentAttemptObjectiveInfo) {			
			return SequencingStatus.Unknown;		
		}
		SequencingStatus rStatus =
			getObjectiveHandler().attempted(userObjective);
		
		/**
		 * Si està a null és que no està inicialitzat, 
		 * teòricament aquesta condició només s'hauria de donar
		 * al final del parsejat.
		 */
		if (rStatus == null) {
			rStatus = updateAttempted(userObjective);
		}		
		
		if (rStatus == null) {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] No hem trobat ni un " 
						+ "Attempted!");				
			}
			return SequencingStatus.Unknown;
		} else {
			return rStatus;
		}
	}
	/**
	 * Ens serveix per actualitzar el nostre ClusterObjective i 
	 * a la vegada forcem l'actualització del nostre pare, ja que
	 * possiblement el nostre canvi l'afecti.
	 *  
	 * @param userObjective : UserObjective Type 
	 * @return SequencingStatus : Retorna el nou valor o null.
	 */
	public final SequencingStatus updateAttempted(
			final UserObjective userObjective) {
		/**
		 * PAS 1: Recalculem el valor.
		 */
		SequencingStatus newValue = attemptedDownLevel(userObjective);
		/**
		 * PAS 2: Actualitzem el nostre UserObjective
		 */
		if (!getObjectiveHandler().
				updateAttempted(userObjective, newValue)) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-"
						+ itemProperty.getIdentifier()
						+ "] updateAttempted: "
						+ "-> getObjectiveHandler()." 
						+ "updateAttempted");
			}
			return null;
		}
		/**
		 * PAS 3: Que el nostre pare també s'actualitzi.
		 */
		if (itemProperty == null) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] updateAttempted: "
						+ "itemProperty == null");
			}
			return null;
		} else {
			/**
			 * Si és null només voldrà dir que ja estem a l'organització.
			 */
			if (itemProperty.father != null) {
				itemProperty.father.getSequencing().
				updateAttempted(userObjective);				
			}			
		}
		return newValue;
	}
	/**
	 * Aquesta funció ens farà el "rollup" de la variable 
	 * attempted, de manera que comprovarem si tots els fills
	 * estan attempted.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return SequencingStatus : El resultat de la consulta.
	 */
	private SequencingStatus attemptedDownLevel(
			final UserObjective userObjective) {
		
		SequencingStatus completeValue = null;
		if (itemProperty != null) {				
			if (itemProperty instanceof Root_Item) {				
				for (Iterator iChilds = getAvailableChildIterator();
					iChilds.hasNext();) {
					
					Abstract_Item nouChild = (Abstract_Item) iChilds.next();
					SequencingStatus valueChild = 
						nouChild.attempted(userObjective);
					
					completeValue =
						evaluateChildActivityOperator(completeValue,
								valueChild, ChildActivitySetType.all);
					/**
					 * Si la condició és una AND (AKA all) aleshores només
					 * que un fill sigui false el resultat serà de false.
					 */
					if (completeValue != null) {
						if (completeValue.equals(SequencingStatus.Failed)) {
							return completeValue;
						}
					}
				}
				if (completeValue == null) {
					return SequencingStatus.Unknown;
				} else {
					return completeValue;
				}
			} else {				
				return getObjectiveHandler().attempted(userObjective);
			}
		} else {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] Ens hem trobat un " 
						+ "itemProperty == null!.");				
			}
		}
		return SequencingStatus.Unknown;
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
	 *
	 */
	public final SequencingStatus satisfied(
			final UserObjective userObjective,
			final boolean afirmative) {
		/**
		 * TODO: !tracked
		 */
		if (!tracked) {
			return SequencingStatus.Unknown;
		}
		
		/**
		 * PAS0 : Mirar si és correcte fer Rollup.
		 */
		if (afirmative) {
			if (!evaluateRollupConsiderations(
					rollupConsiderations.requiredForSatisfied,
					userObjective)) {
				/**
				 * TODO: Hauriem de retornar null o Unknown??
				 */
				return SequencingStatus.Unknown;		
			}
		} else  {
			if (!evaluateRollupConsiderations(
					rollupConsiderations.requiredForNotSatisfied,
					userObjective)) {
				/**
				 * TODO: Hauriem de retornar null o Unknown??
				 */
				return SequencingStatus.Unknown;			
			}
		}
		
		SequencingStatus satisfiedValue =
			getObjectiveHandler().satisfied(userObjective, afirmative);
		
		/**
		 * Si està a null és que no està inicialitzat, 
		 * teòricament aquesta condició només s'hauria de donar
		 * al final del parsejat.
		 */
		if (satisfiedValue == null) {
			satisfiedValue = updateSatisfied(userObjective);
		}
		
		if (satisfiedValue == null) {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] No hem trobat ni un " 
						+ "valor del satisfied()");
			}
			return SequencingStatus.Unknown;
		}
		
		return satisfiedValue;	
	}
	/**
	 * Ens serveix per actualitzar el nostre ClusterObjective i 
	 * a la vegada forcem l'actualització del nostre pare, ja que
	 * possiblement el nostre canvi l'afecti.
	 *  
	 * @param userObjective : UserObjective Type 
	 * @return SequencingStatus : Retorna el nou valor o null.
	 */
	public final SequencingStatus updateSatisfied(
			final UserObjective userObjective) {
		/**
		 * PAS 1: Recalculem el valor.
		 */
		SequencingStatus newValue = satisfiedDownLevel(userObjective, true);
		/**
		 * PAS 2: Actualitzem el nostre UserObjective
		 */
		if (!getObjectiveHandler().updateSatisfied(userObjective, newValue)) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-"
						+ itemProperty.getIdentifier()
						+ "] updateSatisfied: "
						+ "-> getObjectiveHandler().updateSatisfied");
			}
			return null;
		}
		/**
		 * PAS 3: Que el nostre pare també s'actualitzi.
		 */
		if (itemProperty == null) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-"
						+ itemProperty.getIdentifier()
						+ "] updateSatisfied: "
						+ "itemProperty == null");
			}
			return null;
		} else {
			/**
			 * Si és null només voldrà dir que ja estem a l'organització.
			 */
			if (itemProperty.father != null) {
				itemProperty.father.getSequencing().
					updateSatisfied(userObjective);				
			}			
		}
		return newValue;
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si l'activitat està
	 * satisfeta (passed), no ha estat satisfeta (failed) o no ho 
	 * sabem (unknown).
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param afirmative : boolean Type { True:satisfied, False:notSatisfied }
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	 *
	 */
	private SequencingStatus satisfiedDownLevel(
			final UserObjective userObjective,
			final boolean afirmative) {				
		/**
		 * PAS0 : Mirar si és correcte fer Rollup.
		 */
		if (afirmative) {
			if (!evaluateRollupConsiderations(
					rollupConsiderations.requiredForSatisfied,
					userObjective)) {
				return null;			
			}
		} else  {
			if (!evaluateRollupConsiderations(
					rollupConsiderations.requiredForNotSatisfied,
					userObjective)) {
				return null;			
			}
		}
		
		 /**
		  * PAS1 : Mirar si hi ha RollupRules que afectin.
		  */ 
		Collection < RollupRule > collectByAction =
			new ArrayList < RollupRule > ();
		
		if (afirmative) {
			collectByAction =
				rollupActionSearcher(RollupActionType.satisfied);
		} else {
			collectByAction =
				rollupActionSearcher(RollupActionType.notSatisfied);
		}
		
		if (collectByAction.size() > 0) {
			/**
			 * PAS1.1 : Evaluar els RollupRules
			 */	
			for (Iterator iterCollAct = collectByAction.iterator();
			iterCollAct.hasNext();) {
				RollupActionType accio =
					evaluateRollupRule(
							(RollupRule) iterCollAct.next(),
							userObjective);
				if (accio != null) {
					return SequencingStatus.Passed;					
				}
				/*
				if (accio != null) {
					if (afirmative) {
						return SequencingStatus.Passed;
					} else {
						return SequencingStatus.Failed;
					}
				}
				*/								
			}			
		}			
		/**
		 * PAS2 : Si tenim un objective definit en el manifest i aquest té
		 * definit el satisfiedByMeasure == true aleshores l'estratègia per 
		 * saber si està satisfet serà totalment diferent. Ara el que
		 * buscarem serà si la puntuació és superior a la del 
		 * minNormalizedMeasure.
		 */		
		if (getSatisfiedByMeasure()) {
			if (objectiveMeasure(userObjective) != null) {
				if ((objectiveMeasure(userObjective)) 
						>= getMinNormalizedMeasure()) {
					return SequencingStatus.Passed;
				} else {
					return SequencingStatus.Failed;
				}				
			} else {
				return SequencingStatus.Failed;
			}			
		}	
		/**
		 *  PAS3 : Si root->fills, si leaf->objectiveHandler.
		 */		
		SequencingStatus completeValue = null;
		if (itemProperty != null) {				
			if (itemProperty instanceof Root_Item) {				
				for (Iterator iChilds = getAvailableChildIterator();
					iChilds.hasNext();) {
					
					Abstract_Item nouChild = (Abstract_Item) iChilds.next();
					SequencingStatus valueChild = 
						nouChild.satisfied(userObjective,
								afirmative);
					
					completeValue =
						evaluateChildActivityOperator(completeValue,
								valueChild,
								ChildActivitySetType.all);
					/**
					 * Si la condició és una AND (AKA all) aleshores només
					 * que un fill sigui false el resultat serà de false.
					 */
					if (completeValue != null) {
						if (completeValue.equals(SequencingStatus.Failed)) {
							return completeValue;
						}
					}										
				}
				if (completeValue == null) {
					return SequencingStatus.Unknown;
				} else {
					return completeValue;
				}				
			} else {
				return getObjectiveHandler()
					.satisfied(userObjective, afirmative);
			}
		} else {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] Ens hem trobat un " 
						+ "itemProperty == null!.");				
			}					
		}
		return SequencingStatus.Unknown;
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
		/**
		 * TODO: !tracked
		 */
		if (!tracked) {
			return SequencingStatus.Unknown;
		}
		/**
		 * PAS0 : Mirar si és correcte fer Rollup.
		 * TODO: Això segur que s'ha de fer aqui? 
		 */
		if (afirmative) {
			if (!evaluateRollupConsiderations(
					rollupConsiderations.requiredForCompleted,
					userObjective)) {
				/**
				 * TODO: Hauriem de retornar null o Unknown??
				 */
				return SequencingStatus.Unknown;			
			}			
		} else  {
			if (!evaluateRollupConsiderations(
					rollupConsiderations.requiredForIncomplete,
					userObjective)) {
				/**
				 * TODO: Hauriem de retornar null o Unknown??
				 */
				return SequencingStatus.Unknown;	
			}
		}
		
		SequencingStatus completeValue =
			getObjectiveHandler().completed(userObjective, afirmative);
		
		/**
		 * Si està a null és que no està inicialitzat, 
		 * teòricament aquesta condició només s'hauria de donar
		 * al final del parsejat.
		 */
		if (completeValue == null) {
			completeValue = updateCompleted(userObjective);
		}
		
		if (completeValue == null) {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] No hem trobat ni un " 
						+ "valor del completed()");
			}
			return SequencingStatus.Unknown;
		}
		
		return completeValue;	
	}
	/**
	 * Ens serveix per actualitzar el nostre ClusterObjective i 
	 * a la vegada forcem l'actualització del nostre pare, ja que
	 * possiblement el nostre canvi l'afecti.
	 *  
	 * @param userObjective : UserObjective Type 
	 * @return SequencingStatus : Retorna el nou valor o null.
	 */
	public final SequencingStatus updateCompleted(
			final UserObjective userObjective) {
		/**
		 * PAS 1: Recalculem el valor.
		 */
		SequencingStatus newValue = completedDownLevel(userObjective, true);
		/**
		 * PAS 2: Actualitzem el nostre UserObjective
		 */
		
		if (!getObjectiveHandler().updateCompleted(userObjective, newValue)) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-"
						+ itemProperty.getIdentifier()
						+ "] updateCompleted: "
						+ "-> getObjectiveHandler().updateCompleted");
			}
			return null;
		}
		/**
		 * PAS 3: Que el nostre pare també s'actualitzi.
		 */
		if (itemProperty == null) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-"
						+ itemProperty.getIdentifier()
						+ "] updateCompleted: "
						+ "itemProperty == null");
			}
			return null;
		} else {
			/**
			 * Si és null només voldrà dir que ja estem a l'organització.
			 */
			if (itemProperty.father != null) {
				itemProperty.father.getSequencing().
					updateCompleted(userObjective);
			}
		}
		return newValue;
	}
	/**
	 * Aquesta funció ens farà el "rollup" de la variable 
	 * completed, de manera que comprovarem si 
	 * tots els fills estan completed == Passed.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param afirmative : boolean Type { True:completed, False:incomplete } 
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	 */
	private SequencingStatus completedDownLevel(
			final UserObjective userObjective,
			final boolean afirmative) {
		/**
		 * PAS0 : Mirar si és correcte fer Rollup.
		 */
		if (afirmative) {
			if (!evaluateRollupConsiderations(
					rollupConsiderations.requiredForCompleted,
					userObjective)) {
				/**
				 * TODO: Hauriem de retornar null o Unknown??
				 */
				return SequencingStatus.Unknown;			
			}			
		} else  {
			if (!evaluateRollupConsiderations(
					rollupConsiderations.requiredForIncomplete,
					userObjective)) {				
				/**
				 * TODO: Hauriem de retornar null o Unknown??
				 */
				return SequencingStatus.Unknown;	
			}
		}
		 /**
		  * PAS1 : Mirar si hi ha RollupRules que afectin.
		  */ 
		Collection < RollupRule > collectByAction =
			new ArrayList < RollupRule > ();
		
		if (afirmative) {
			collectByAction =
				rollupActionSearcher(RollupActionType.completed);
		} else {
			collectByAction =
				rollupActionSearcher(RollupActionType.incomplete);
		}
		
		if (collectByAction.size() > 0) {
			/**
			 * PAS1.1 : Evaluar els RollupRules
			 */	
			for (Iterator iterCollAct = collectByAction.iterator();
			iterCollAct.hasNext();) {
				RollupActionType accio =
					evaluateRollupRule(
							(RollupRule) iterCollAct.next(),
							userObjective);
				
				if (accio != null) {
					return SequencingStatus.Passed;					
				}
				/*
				if (accio != null) {
					if (afirmative) {
						return SequencingStatus.Passed;
					} else {
						return SequencingStatus.Failed;
					}
				}
				*/								
			}			
		}		
		/**
		 * PAS2 : Si root->fills, si leaf->objectiveHandler.
		 */
		SequencingStatus completeValue = null;
		if (itemProperty != null) {				
			if (itemProperty instanceof Root_Item) {				
				for (Iterator iChilds = getAvailableChildIterator();
					iChilds.hasNext();) {
					
					Abstract_Item nouChild = (Abstract_Item) iChilds.next();
					SequencingStatus valueChild = 
						nouChild.completed(userObjective,
								afirmative);
					
					completeValue =
						evaluateChildActivityOperator(completeValue,
								valueChild, ChildActivitySetType.all);
					/**
					 * Si la condició és una AND (AKA all) aleshores només
					 * que un fill sigui false el resultat serà de false.
					 */
					if (completeValue != null) {
						if (completeValue.equals(SequencingStatus.Failed)) {
							return completeValue;
						}
					}					
				}
				if (completeValue == null) {
					return SequencingStatus.Unknown;
				} else {
					return completeValue;
				}
			} else {
				return getObjectiveHandler().
					completed(userObjective, afirmative);
			}
		} else {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] Ens hem trobat un " 
						+ "itemProperty == null!.");				
			}					
		}
		return SequencingStatus.Unknown;	
	}
	/**
	 * Aquesta funció és recursiva de manera que ens retornara l'element
	 * ControlMode si està definit pel nostre item/cluster. De no ser així
	 * li demanarà al seu pare i així fins que retorni o el ControlMode per
	 * defecte o alguna versió "superior" que ens afecti.
	 * 
	 * @return AdlseqControlMode Type.
	 */
	public AdlseqControlMode getControlMode() {
		if (adlseqControlMode == null) {
			if (itemProperty.father != null) {
				return itemProperty.father.getSequencing().getControlMode();
			} else {
				return new AdlseqControlMode();
			}
		} else {
			return adlseqControlMode;
		}
	}
	/**
	 * Ens ha de retorar un booleà indicant-nos si és coneix 
	 * la mesura de la progressió dels objectius (passed),
	 * no hi ha mesura (failed) o no ho sabem (unknown).
	 * 
	 * Si tenim un ControlMode amb el paràmetre 
	 * useCurrentAttemptProgressInfo == false aleshores retornarem
	 * sempre "UNKNOWN". 
	 *   
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	public final SequencingStatus activityProgressKnown(
			final UserObjective userObjective) {		
		/**
		 * TODO: !tracked
		 */
		if (!tracked) {
			return SequencingStatus.Unknown;
		}
		
		if (getControlMode().useCurrentAttemptProgressInfo) {
			/**
			 * TODO: Hauriem de retornar null o Unknown??
			 */
			return SequencingStatus.Unknown;		
		}
		
		SequencingStatus completeValue = 
			getObjectiveHandler().
				activityProgressKnown(userObjective);
		
		/**
		 * Si està a null és que no està inicialitzat, 
		 * teòricament aquesta condició només s'hauria de donar
		 * al final del parsejat.
		 */
		if (completeValue == null) {
			completeValue = updateActivityProgressKnown(userObjective);
		}
		
		if (completeValue == null) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] Ens hem trobat un " 
						+ "activityProgressKnown == null!.");				
			}
			return SequencingStatus.Unknown;
		} else {
			return completeValue;			
		}		
	}
	/**
	 * Ens serveix per actualitzar el nostre ClusterObjective i 
	 * a la vegada forcem l'actualització del nostre pare, ja que
	 * possiblement el nostre canvi l'afecti.
	 *  
	 * @param userObjective : UserObjective Type 
	 * @return SequencingStatus : Retorna el nou valor o null.
	 */
	public final SequencingStatus updateActivityProgressKnown(
			final UserObjective userObjective) {
		/**
		 * PAS 1: Recalculem el valor.
		 */
		SequencingStatus newValue = 
			activityProgressKnownDownLevel(userObjective);
		/**
		 * PAS 2: Actualitzem el nostre UserObjective
		 */
		if (!getObjectiveHandler().
				updateActivityProgressKnown(userObjective, newValue)) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-"
						+ itemProperty.getIdentifier()
						+ "] updateActivityProgressKnown: "
						+ "-> getObjectiveHandler()." 
						+ "updateActivityProgressKnown");
			}
			//return null;
		}
		/**
		 * PAS 3: Que el nostre pare també s'actualitzi.
		 */
		if (itemProperty == null) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-"
						+ itemProperty.getIdentifier()
						+ "] updateActivityProgressKnown: "
						+ "itemProperty == null");
			}
			return null;
		} else {
			/**
			 * Si és null només voldrà dir que ja estem a l'organització.
			 */
			if (itemProperty.father != null) {
				itemProperty.father.getSequencing().
				updateActivityProgressKnown(userObjective);				
			}			
		}
		return newValue;
	}
	/**
	 * Aquesta funció ens farà el "rollup" de la variable 
	 * activityProgressKnown, de manera que comprovarem si 
	 * tots els fills estan activityProgressKnown == Passed.
	 *  
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	private SequencingStatus activityProgressKnownDownLevel(
			final UserObjective userObjective) {
		
		SequencingStatus completeValue = null;
		if (itemProperty != null) {				
			if (itemProperty instanceof Root_Item) {				
				for (Iterator iChilds = getAvailableChildIterator();
					iChilds.hasNext();) {
					
					Abstract_Item nouChild = (Abstract_Item) iChilds.next();
					SequencingStatus valueChild = 
						nouChild.
							activityProgressKnown(userObjective);
											
					completeValue =
						evaluateChildActivityOperator(completeValue,
								valueChild, ChildActivitySetType.all);
					/**
					 * Si la condició és una AND (AKA all) aleshores només
					 * que un fill sigui false el resultat serà de false.
					 */
					if (completeValue != null) {
						if (completeValue.equals(SequencingStatus.Failed)) {
							return completeValue;
						}
					}
				}
				if (completeValue == null) {
					return SequencingStatus.Unknown;
				} else {
					return completeValue;
				}
			} else {
				return getObjectiveHandler().activityProgressKnown(
						userObjective);				
			}
		} else {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] Ens hem trobat un " 
						+ "itemProperty == null!.");				
			}					
		}
		return SequencingStatus.Unknown;	
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
					+ ID + "-" + IDRef + "]" 
					+ itemProperty.getIdentifier()
					+ " -> Sequencing -> objectiveMeasure()");
		}
		
		Double completeValue = 0.0;
		
		completeValue = getObjectiveHandler().objectiveMeasure(
				userObjective);
		/**
		 * Si està a null és que no està inicialitzat, 
		 * teòricament aquesta condició només s'hauria de donar
		 * al final del parsejat.
		 */
		if (completeValue == null) {
			if (objectiveMeasureKnown(userObjective).equals(
					SequencingStatus.Passed)) {
				completeValue = updateObjectiveMeasure(userObjective);
			} 
		}
		
		if (completeValue == null) {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] Ens hem trobat ni un " 
						+ "objectiveMeasure!.");
			}			
		}		
		return completeValue;
	}
	/**
	 * Ens serveix per actualitzar el nostre ClusterObjective i 
	 * a la vegada forcem l'actualització del nostre pare, ja que
	 * possiblement el nostre canvi l'afecti.
	 *  
	 * @param userObjective : UserObjective Type 
	 * @return SequencingStatus : Retorna el nou valor o null.
	 */
	public final Double updateObjectiveMeasure(
			final UserObjective userObjective) {
		/**
		 * PAS 1: Recalculem el valor.
		 */
		Double newValue = 
			objectiveMeasureDownLevel(userObjective);
		/**
		 * PAS 2: Actualitzem el nostre UserObjective
		 */
		//if (newValue == null) {
			//return null;
		//}
		if (!getObjectiveHandler().
				updateObjectiveMeasure(userObjective, newValue)) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-"
						+ itemProperty.getIdentifier()
						+ "] updateObjectiveMeasure: "
						+ "-> getObjectiveHandler()." 
						+ "updateObjectiveMeasure");
			}
			return null;
		}
		/**
		 * PAS 3: Que el nostre pare també s'actualitzi.
		 */
		if (itemProperty == null) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-"
						+ itemProperty.getIdentifier()
						+ "] updateObjectiveMeasure: "
						+ "itemProperty == null");
			}
			return null;
		} else {
			/**
			 * Si és null només voldrà dir que ja estem a l'organització.
			 */
			if (itemProperty.father != null) {
				itemProperty.father.getSequencing().
				updateObjectiveMeasure(userObjective);				
			}			
		}
		return newValue;
	}
	/**
	 * Si objectiveMeasureKnown és igual a 'passed' aleshores aquesta
	 * funció ens retornarà aquesta mesura, que serà un flotant.
	 *   
	 * @param userObjective : UserObjective Type.
	 * @return Double: Puntuació amb un màxim de quatre decimals.
	*/ 	
	private Double objectiveMeasureDownLevel(
			final UserObjective userObjective) {
		
		if (Constants.DEBUG_NAVIGATION 
				&& Constants.DEBUG_NAVIGATION_LOW) {
			System.out.println("[NAVIGATION][" 
					+ ID + "-" + IDRef + "]" 
					+ itemProperty.getIdentifier()
					+ " -> Sequencing -> objectiveMeasure()");
		}		
		/**
		 * PAS2 : Si root->fills, si leaf->objectiveHandler.
		 */
		int numberOfSons = 0;
		boolean anySon = false;
		Double completeValue = 0.0;
		if (itemProperty != null) {				
			if (itemProperty instanceof Root_Item) {				
				for (Iterator iChilds = getAvailableChildIterator();
					iChilds.hasNext();) {
					
					Abstract_Item nouChild = (Abstract_Item) iChilds.next();
					if ((nouChild.getRollupObjectiveMeasureWeight() > 0.0)) {
						
						numberOfSons++;
						if ((Constants.DEBUG_NAVIGATION 
								&& Constants.DEBUG_WARNINGS_LOW
								&& Constants.DEBUG_NAVIGATION_LOW)
								|| Constants.DEBUG_INFO) {
							System.out.println("\t(" 
									+ numberOfSons + ")L'item "
									+ nouChild.getIdentifier() 
									+ " contribueix a "
									+ "l'ObjectiveMeasureWeight(): " 
									+ nouChild.
										getRollupObjectiveMeasureWeight());
						}
						
						Double valueChild = 
							nouChild.objectiveMeasure(userObjective);
						
						if (valueChild != null) {						
							anySon = true;
							if ((Constants.DEBUG_NAVIGATION 
									&& Constants.DEBUG_WARNINGS_LOW
									&& Constants.DEBUG_NAVIGATION_LOW)
									|| Constants.DEBUG_INFO) {
								System.out.println("\t\tPartial: ("
										+ (valueChild * nouChild.
										getRollupObjectiveMeasureWeight()) 
										+ " + " 
										+ completeValue + ") = "
										+ ((valueChild * nouChild.
											getRollupObjectiveMeasureWeight())
												+ completeValue));
							}
							
							completeValue =
								((valueChild * nouChild.
										getRollupObjectiveMeasureWeight()) 
										+ completeValue);
							
						} else {
							if ((Constants.DEBUG_NAVIGATION 
									&& Constants.DEBUG_WARNINGS_LOW
									&& Constants.DEBUG_NAVIGATION_LOW)
									|| Constants.DEBUG_INFO) {
								System.out.println("@@" 
										+ nouChild.getIdentifier() 
										+ " -> NULL");
							}						
						}
						
					}
					
				}				
				if ((Constants.DEBUG_NAVIGATION 
						&& Constants.DEBUG_WARNINGS_LOW
						&& Constants.DEBUG_NAVIGATION_LOW)
						|| Constants.DEBUG_INFO) {
					System.out.println("\tReturn CompleteValue = " 
							+ (completeValue / numberOfSons));
				}
				
				if (!anySon) {
					if (Constants.DEBUG_ERRORS) {
						System.out.println("[ERROR] Ens hem trobat ni un " 
								+ "objectiveMeasure!.");				
					}
					return null;
				} else {
					return completeValue / numberOfSons;
				}
			} else {				
				completeValue = 
					getObjectiveHandler().
						objectiveMeasure(userObjective);
				if (completeValue != null) {
					return completeValue;
				}
			}
		} else {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] Ens hem trobat un " 
						+ "itemProperty == null!.");				
			}					
		}
		if (Constants.DEBUG_WARNINGS 
				&& Constants.DEBUG_WARNINGS_LOW
				&& Constants.DEBUG_NAVIGATION
				&& Constants.DEBUG_NAVIGATION_LOW) {
			System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] Ens hem trobat ni un " 
					+ "objectiveMeasure!.");				
		}
		return null;
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
		SequencingStatus completeValue = 
			getObjectiveHandler().
				objectiveMeasureKnown(userObjective);
		/**
		 * TODO: !tracked
		 */
		if (!tracked) {
			return SequencingStatus.Unknown;
		}
		/**
		 * Si està a null és que no està inicialitzat, 
		 * teòricament aquesta condició només s'hauria de donar
		 * al final del parsejat.
		 */
		if (completeValue == null) {
			completeValue = updateObjectiveMeasureKnown(userObjective);
		}		
		
		if (completeValue == null) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] Ens hem trobat ni un " 
						+ "objectiveMeasureKnown!.");				
			}	
			return SequencingStatus.Unknown;
		} else {
			return completeValue;
		}
	}
	/**
	 * Ens serveix per actualitzar el nostre ClusterObjective i 
	 * a la vegada forcem l'actualització del nostre pare, ja que
	 * possiblement el nostre canvi l'afecti.
	 *  
	 * @param userObjective : UserObjective Type 
	 * @return SequencingStatus : Retorna el nou valor o null.
	 */
	public final SequencingStatus updateObjectiveMeasureKnown(
			final UserObjective userObjective) {
		/**
		 * PAS 1: Recalculem el valor.
		 */
		SequencingStatus newValue = 
			objectiveMeasureKnownDownLevel(userObjective);
		/**
		 * PAS 2: Actualitzem el nostre UserObjective
		 */
		if (!getObjectiveHandler().
				updateObjectiveMeasureKnown(userObjective, newValue)) {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] updateObjectiveMeasureKnown: "
						+ "-> getObjectiveHandler()." 
						+ "updateObjectiveMeasureKnown");
			}
			return null;
		}
		/**
		 * PAS 3: Que el nostre pare també s'actualitzi.
		 */
		if (itemProperty == null) {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] updateObjectiveMeasureKnown: "
						+ "itemProperty == null");
			}
			return null;
		} else {
			/**
			 * Si és null només voldrà dir que ja estem a l'organització.
			 */
			if (itemProperty.father != null) {
				itemProperty.father.getSequencing().
				updateObjectiveMeasureKnown(userObjective);				
			}
		}
		return newValue;
	}
	/**
	 * Aquesta funció ens farà el "rollup" de la variable 
	 * objectiveMeasureKnown, de manera que comprovarem si 
	 * tots els fills estan objectiveMeasureKnown == Passed.
	 *  
	 * @param userObjective : UserObjective Type. 
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	private SequencingStatus objectiveMeasureKnownDownLevel(
			final UserObjective userObjective) {
		
		SequencingStatus completeValue = null;
		if (itemProperty != null) {				
			if (itemProperty instanceof Root_Item) {				
				for (Iterator iChilds = getAvailableChildIterator();
					iChilds.hasNext();) {
					
					Abstract_Item nouChild = (Abstract_Item) iChilds.next();
					SequencingStatus valueChild = 
						nouChild.objectiveMeasureKnown(userObjective);
											
					completeValue =
						evaluateChildActivityOperator(completeValue,
								valueChild, ChildActivitySetType.any);
					/**
					 * Si la condició és una AND (AKA all) aleshores només
					 * que un fill sigui false el resultat serà de false.
					 *
					if (completeValue.equals(SequencingStatus.Passed)) {
						return completeValue;
					}
					*/
				}
				if (completeValue == null) {
					return SequencingStatus.Unknown;
				} else {
					return completeValue;
				}
			} else {
				return getObjectiveHandler().
					objectiveMeasureKnown(userObjective);				
			}
		} else {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] Ens hem trobat un " 
						+ "itemProperty == null!.");				
			}		
		}
		return SequencingStatus.Unknown;	
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
		/**
		 * TODO: !tracked
		 */
		if (!tracked) {
			return SequencingStatus.Unknown;
		}
		SequencingStatus completeValue = 
			getObjectiveHandler().objectiveStatusKnown(userObjective);
		/**
		 * Si està a null és que no està inicialitzat, 
		 * teòricament aquesta condició només s'hauria de donar
		 * al final del parsejat.
		 */
		if (completeValue == null) {
			completeValue = updateObjectiveStatusKnown(userObjective);
		}
		
		if (completeValue == null) {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] Ens hem trobat ni un " 
						+ "objectiveStatusKnown!.");				
			}
			return SequencingStatus.Unknown;
		}
		return completeValue;			
	}
	/**
	 * Ens serveix per actualitzar el nostre ClusterObjective i 
	 * a la vegada forcem l'actualització del nostre pare, ja que
	 * possiblement el nostre canvi l'afecti.
	 *  
	 * @param userObjective : UserObjective Type 
	 * @return SequencingStatus : Retorna el nou valor o null.
	 */
	public final SequencingStatus updateObjectiveStatusKnown(
			final UserObjective userObjective) {
		/**
		 * PAS 1: Recalculem el valor.
		 */
		SequencingStatus newValue = 
			objectiveStatusKnownDownLevel(userObjective);
		/**
		 * PAS 2: Actualitzem el nostre UserObjective
		 */
		if (!getObjectiveHandler().
				updateObjectiveStatusKnown(userObjective, newValue)) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-"
						+ itemProperty.getIdentifier()
						+ "] updateObjectiveStatusKnown: "
						+ "-> getObjectiveHandler()." 
						+ "updateObjectiveStatusKnown");
			}
			return null;
		}
		/**
		 * PAS 3: Que el nostre pare també s'actualitzi.
		 */
		if (itemProperty == null) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR-"
						+ itemProperty.getIdentifier()
						+ "] updateObjectiveStatusKnown: "
						+ "itemProperty == null");
			}
			return null;
		} else {
			/**
			 * Si és null només voldrà dir que ja estem a l'organització.
			 */
			if (itemProperty.father != null) {
				itemProperty.father.getSequencing().
				updateObjectiveStatusKnown(userObjective);				
			}
		}
		return newValue;
	}
	/**
	 * Aquesta funció ens farà el "rollup" de la variable 
	 * objectiveStatusKnown, de manera que comprovarem si 
	 * tots els fills estan objectiveStatusKnown == Passed.
	 *  
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 
	private SequencingStatus objectiveStatusKnownDownLevel(
			final UserObjective userObjective) {
		
		SequencingStatus completeValue = null;
		if (itemProperty != null) {				
			if (itemProperty instanceof Root_Item) {				
				for (Iterator iChilds = getAvailableChildIterator();
					iChilds.hasNext();) {
					
					Abstract_Item nouChild = (Abstract_Item) iChilds.next();
					SequencingStatus valueChild = 
						nouChild.objectiveStatusKnown(userObjective);
											
					completeValue =
						evaluateChildActivityOperator(completeValue,
								valueChild, ChildActivitySetType.all);
					/**
					 * Si la condició és una AND (AKA all) aleshores només
					 * que un fill sigui false el resultat serà de false.
					 */
					if (completeValue != null) {
						if (completeValue.equals(SequencingStatus.Failed)) {
							return completeValue;
						}
					}					
				}
				if (completeValue == null) {
					return SequencingStatus.Unknown;
				} else {
					return completeValue;
				}
			} else {
				return getObjectiveHandler().objectiveStatusKnown(
						userObjective);				
			}
		} else {
			if (Constants.DEBUG_WARNINGS 
					&& Constants.DEBUG_WARNINGS_LOW
					&& Constants.DEBUG_NAVIGATION
					&& Constants.DEBUG_NAVIGATION_LOW) {
				System.out.println("[WARNING-"
						+ itemProperty.getIdentifier()
						+ "] Ens hem trobat un " 
						+ "itemProperty == null!.");				
			}					
		}
		return SequencingStatus.Unknown;		
		
	}
	/**
	 * Amb aquesta funció ens solucionem la vida a l'hora de passar aquest
	 * valor entre classes que implementin aquesta interfície.
	 * 
	 * @return Double Type.
	 */
	public final Double getMinNormalizedMeasure() {
		return getObjectiveHandler().getMinNormalizedMeasure();
	}
	/**
	 * Amb aquesta funció ens solucionem la vida a l'hora de passar aquest
	 * paràmetre entre classes que implementin aquesta interfície. 
	 * 
	 * @return boolean Type. 
	 */
	public final boolean getSatisfiedByMeasure() {
		return getObjectiveHandler().getSatisfiedByMeasure();
	}
	/**
	 * Aquesta funció ens evaluarà tant l'attemptLimit 
	 * com per l'attemptAbsoluteDurationLimit.
	 *  
	 * @param userObjective : El usuari actual
	 * @return boolean : True si s'exedeix algun límit. False 
	 * si tot és correcte.
	 */
	public final boolean limitCondition(
			final UserObjective userObjective) {
		if (attemptLimit > 0) {
			if (attemptCount(userObjective, itemProperty.getIdentifier()) 
					>= attemptLimit) {
				return true;								
			}
		} 
		if (attemptAbsoluteDurationLimit != null) {
			try {
				return  !(attemptCountAbsoluteDuration(
						userObjective, itemProperty.getIdentifier())
						.isShorterThan(attemptAbsoluteDurationLimit));
			} catch (DatatypeConfigurationException e) {
				if (Constants.DEBUG_ERRORS) {
					System.out.println("[ERROR] limitCondition:" 
							+ e.getMessage());
				}
				return true;
			}				
		}
		return false;
	}
	/**
	 * Aquesta funció ens servirà per l'AttemptLimit (de 
	 * l'imsss:limitConditions) i també per les condicions 
	 * de attemptLimitExceedede (ruleConditions). 
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param currentIdentifier : String identificador de l'item.
	 * @return int: contador d'accessos. 
	*/
	private int attemptCount(
			final UserObjective userObjective, 
			final String currentIdentifier) {
		
		if (!userObjective.treeAnnotations.
				get(currentIdentifier).isAccessed) {
			return 0;
		} else {
			Collection < String > sons =
				userObjective.treeAnnotations.get(currentIdentifier).sons;
			if (sons.isEmpty()) {
				return userObjective.dataModel.
					get(currentIdentifier).activityAttemptCount;
			} else {
				int attemptCount = 0;
				for (Iterator sonIterator = sons.iterator();
					sonIterator.hasNext();) {
					String newID = (String) sonIterator.next();
					attemptCount = attemptCount 
						+ attemptCount(userObjective,
								newID);					
				}
				return attemptCount;
			}
		}
	}
	/**
	 * Aquesta funció ens servirà per l'AttemptLimit (de 
	 * l'imsss:limitConditions) i també per les condicions 
	 * de attemptLimitExceedede (ruleConditions). 
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param currentIdentifier : String identificador de l'item.
	 * @return int: contador d'accessos. 
	 * @throws DatatypeConfigurationException : Per si el Datatype Falla.
	*/
	private Duration attemptCountAbsoluteDuration(
			final UserObjective userObjective, 
			final String currentIdentifier) 
			throws DatatypeConfigurationException {
		
		if (!userObjective.treeAnnotations.
				get(currentIdentifier).isAccessed) {
			return DatatypeFactory.newInstance().newDuration(0);
		} else {
			Collection < String > sons =
				userObjective.treeAnnotations.get(currentIdentifier).sons;
			if (sons.isEmpty()) {
				String totalTime = userObjective.dataModel.
					get(currentIdentifier).totalTime;
				if (totalTime == null) {
					return DatatypeFactory.newInstance().newDuration(0);
				} else {
					return DatatypeFactory.newInstance().newDuration(totalTime);
				}
			} else {
				Duration attemptDuration =
					DatatypeFactory.newInstance().newDuration(0);
				
				for (Iterator sonIterator = sons.iterator();
					sonIterator.hasNext();) {
					String newID = (String) sonIterator.next();
					Duration newDuration = 
						attemptCountAbsoluteDuration(userObjective, newID); 
					attemptDuration = attemptDuration.add(newDuration);
				}
				return attemptDuration;
			}
		}
	}	
	/**
	 * Fa les operacions "lògiques" clàssiques (OR, AND, NAND)  segons les 
	 * variables trileanes (passed, failed, unknown). 
	 * 
	 * @param completeValue : SequencingStatus Type
	 * @param valueChild : SequencingStatus Type
	 * @param activitySetType : ChildActivitySetType; Tipus de l'operació.
	 * @return SequencingStatus : Resultat de l'operació.
	 */
	private SequencingStatus evaluateChildActivityOperator(
			final SequencingStatus completeValue,
			final SequencingStatus valueChild,
			final ChildActivitySetType activitySetType) {
		
		SequencingStatus newCompleteValue = completeValue;
		SequencingStatus newValueChild = valueChild;
				
		if (newCompleteValue == null) {
			newCompleteValue = newValueChild;
		} else {
			if (activitySetType == ChildActivitySetType.any) {
				if (newValueChild == Constants.SequencingStatus.Passed) {
					return Constants.SequencingStatus.Passed;
				} else if ((newValueChild 
						== Constants.SequencingStatus.Unknown)
						&& (newCompleteValue 
								== Constants.SequencingStatus.Failed)) {
					newCompleteValue = Constants.SequencingStatus.Unknown;
				}				
			} else if (activitySetType == ChildActivitySetType.all) {
				if (newValueChild == Constants.SequencingStatus.Failed) {
					return Constants.SequencingStatus.Failed;
				} else if ((newValueChild 
						== Constants.SequencingStatus.Unknown)
						&& (newCompleteValue 
								== Constants.SequencingStatus.Passed)) {
					newCompleteValue = Constants.SequencingStatus.Unknown;
				}				
			} else if (activitySetType == ChildActivitySetType.none) {
				if (newValueChild == Constants.SequencingStatus.Failed) {
					return Constants.SequencingStatus.Passed;
				} else if ((newValueChild 
						== Constants.SequencingStatus.Unknown)
						&& (newCompleteValue 
								== Constants.SequencingStatus.Passed)) {
					newCompleteValue = Constants.SequencingStatus.Unknown;
				} else if (newCompleteValue 
						== Constants.SequencingStatus.Passed) {
					return Constants.SequencingStatus.Failed;
				}
				
			} else {
				/**
				 * TODO: Not Implemented yet!!
				 */
				if (Constants.DEBUG_WARNINGS) {
					System.out.println("[WARNING] El tipus " 
							+ "ChildActivitySetType = " 
							+ activitySetType.toString() 
							+ " no està tractat!");
				}
			}			
		}		
		return newCompleteValue;
	}
	
	/**
	 * Aquesta funció ens evaluarà una regla concreta, retornant-nos
	 * el action determinat, o un null si no s'ha complert.
	 * 
	 * @param concreteRollupRule : RollupRule Type.
	 * @param userObjective : UserObjective Type.
	 * @return RollupActionType : O null si no és satisfà la condició.
	 */
	private RollupActionType evaluateRollupRule(
			final RollupRule concreteRollupRule,
			final UserObjective userObjective) {
		
		RollupActionType actionType = concreteRollupRule.rollupAction;
		
		SequencingStatus allCorrectResponse = null;
		
		for (Iterator iterConditions = concreteRollupRule.condition.iterator();
		iterConditions.hasNext();) {
			RollupCondition newCondition =
				(RollupCondition) iterConditions.next();
			/**
			 * És afirmativa o negativa la funció?
			 */
			boolean afirmative = true;
			if (newCondition.operator == OperatorType.not) {
				afirmative = false;
			}
			/**
			 * Quina condició és? (satisfied, completed, attempted..)
			 */
			SequencingStatus correctCondition = null;
			/**
			 * ####################  ATTEMPTED ####################
			 */
			if (newCondition.condition == ConditionType.attempted) {
				
				SequencingStatus completeValue = null;
				if (itemProperty != null) {				
					if (itemProperty instanceof Root_Item) {				
						for (Iterator iChilds = getAvailableChildIterator();
							iChilds.hasNext();) {
							
							Abstract_Item nouChild =
								(Abstract_Item) iChilds.next();
							
							SequencingStatus valueChild = 
								nouChild.attempted(userObjective);
							
							completeValue =
								evaluateChildActivityOperator(
										completeValue,
										valueChild,
										concreteRollupRule.childActivitySet);
						}
						correctCondition = completeValue;
					} else {				
						correctCondition =
							getObjectiveHandler().attempted(userObjective);
					}
				} else {
					if (Constants.DEBUG_ERRORS) {
						System.out.println("[ERROR] Ens hem trobat un " 
								+ "itemProperty == null!.");				
					}					
				}
			/**
			 * 	####################  SATISFIED ####################
			 */				
			} else if (newCondition.condition == ConditionType.satisfied) {
				
				SequencingStatus completeValue = null;
				if (itemProperty != null) {				
					if (itemProperty instanceof Root_Item) {				
						for (Iterator iChilds = getAvailableChildIterator();
							iChilds.hasNext();) {
							
							Abstract_Item nouChild =
								(Abstract_Item) iChilds.next();
							
							SequencingStatus valueChild = 
								nouChild.satisfied(userObjective, afirmative);
							
							completeValue =
								evaluateChildActivityOperator(completeValue,
										valueChild,
										concreteRollupRule.childActivitySet);
						}
						correctCondition = completeValue;
					} else {
						correctCondition = getObjectiveHandler().satisfied(
								userObjective, afirmative);
					}
				} else {
					if (Constants.DEBUG_ERRORS) {
						System.out.println("[ERROR] Ens hem trobat un " 
								+ "itemProperty == null!.");				
					}					
				}
			/**
			 * ####################  COMPLETED ####################
			 */				
			} else if (newCondition.condition == ConditionType.completed) {
				
				SequencingStatus completeValue = null;
				if (itemProperty != null) {				
					if (itemProperty instanceof Root_Item) {				
						for (Iterator iChilds = getAvailableChildIterator();
							iChilds.hasNext();) {
							
							Abstract_Item nouChild =
								(Abstract_Item) iChilds.next();
							SequencingStatus valueChild = 
								nouChild.completed(userObjective, afirmative);
							
							completeValue =
								evaluateChildActivityOperator(completeValue,
										valueChild,
										concreteRollupRule.childActivitySet);
						}
						correctCondition = completeValue;
					} else {
						correctCondition = 
							getObjectiveHandler().completed(
									userObjective, afirmative);
					}
				} else {
					if (Constants.DEBUG_ERRORS) {
						System.out.println("[ERROR] Ens hem trobat un " 
								+ "itemProperty == null!.");				
					}					
				}
			/**
			 * #################  OBJECTIVE MEASURE KNOWN #################
			 */				
			} else if (newCondition.condition 
					== ConditionType.objectiveMeasureKnown) {
				
				SequencingStatus completeValue = null;
				if (itemProperty != null) {				
					if (itemProperty instanceof Root_Item) {				
						for (Iterator iChilds = getAvailableChildIterator();
							iChilds.hasNext();) {
							
							Abstract_Item nouChild =
								(Abstract_Item) iChilds.next();
							SequencingStatus valueChild = 
								nouChild.objectiveMeasureKnown(userObjective);
							
							completeValue =
								evaluateChildActivityOperator(completeValue,
										valueChild,
										concreteRollupRule.childActivitySet);
						}
						correctCondition = completeValue;
					} else {
						correctCondition = 
							getObjectiveHandler().
								objectiveMeasureKnown(userObjective);
					}
				} else {
					if (Constants.DEBUG_ERRORS) {
						System.out.println("[ERROR] Ens hem trobat un " 
								+ "itemProperty == null!.");				
					}					
				}
			/**
			 * ####################  OBJECTIVE STATUS KNOWN ####################
			 */
			} else if (newCondition.condition 
					== ConditionType.objectiveStatusKnown) {
				
				SequencingStatus completeValue = null;
				if (itemProperty != null) {				
					if (itemProperty instanceof Root_Item) {				
						for (Iterator iChilds = getAvailableChildIterator();
							iChilds.hasNext();) {
							
							Abstract_Item nouChild =
								(Abstract_Item) iChilds.next();
							SequencingStatus valueChild = 
								nouChild.objectiveStatusKnown(userObjective);
							
							completeValue =
								evaluateChildActivityOperator(completeValue,
										valueChild,
										concreteRollupRule.childActivitySet);
						}
						correctCondition = completeValue;
					} else {
						correctCondition =
							getObjectiveHandler().
								objectiveStatusKnown(userObjective);
					}
				} else {
					if (Constants.DEBUG_ERRORS) {
						System.out.println("[ERROR] Ens hem trobat un " 
								+ "itemProperty == null!.");				
					}					
				}
			/**
			 * #################  ACTIVITY PROGRESS KNOWN #################
			 */				
			} else if (newCondition.condition 
					== ConditionType.activityProgressKnown) {
				
				SequencingStatus completeValue = null;
				if (itemProperty != null) {				
					if (itemProperty instanceof Root_Item) {				
						for (Iterator iChilds = getAvailableChildIterator();
							iChilds.hasNext();) {
							
							Abstract_Item nouChild =
								(Abstract_Item) iChilds.next();
							SequencingStatus valueChild = 
								nouChild.activityProgressKnown(userObjective);
							
							completeValue =
								evaluateChildActivityOperator(completeValue,
										valueChild,
										concreteRollupRule.childActivitySet);
						}
						correctCondition = completeValue;
					} else {
						correctCondition =
							getObjectiveHandler().
								activityProgressKnown(userObjective);
					}
				} else {
					if (Constants.DEBUG_ERRORS) {
						System.out.println("[ERROR] Ens hem trobat un " 
								+ "itemProperty == null!.");				
					}					
				}
			/**
			 * ####################  attemptLimitExceeded ####################
			 */
			} else if (newCondition.condition 
					== ConditionType.attemptLimitExceeded) {
				
				if (itemProperty != null) {				
					if (limitCondition(userObjective)) {
						correctCondition = SequencingStatus.Passed;
					} else {
						correctCondition = SequencingStatus.Failed;
					}
				} else {
					if (Constants.DEBUG_ERRORS) {
						System.out.println("[ERROR] Ens hem trobat un " 
								+ "itemProperty == null!.");				
					}					
				}
			/**
			 * ####################  Altres ####################
			 */
			}  else {				
				if (Constants.DEBUG_WARNINGS) {
					System.out.println("[WARNING] El tipus " 
							+ "ConditionType = " 
							+ newCondition.condition.toString() 
							+ " no està tractat!");
				}
			}
			/**
			 * Tenim l'operador not? o noOp?
			 */
			if (!afirmative) {
				if (correctCondition == SequencingStatus.Passed) {
					correctCondition = SequencingStatus.Failed;
				} else if (correctCondition == SequencingStatus.Failed) {
					correctCondition = SequencingStatus.Passed;					
				}
			}
			/**
			 * Analitzem la resposta que hem obtingut del fet d'aplicar les
			 * regles i fem una AND o una OR (All, any) amb el valor acumulat.
			 */
			if (allCorrectResponse == null) {
				allCorrectResponse = correctCondition;
			} else {
				ChildActivitySetType tmpType = ChildActivitySetType.any;
				if (concreteRollupRule.conditionCombination 
						== RollupRule.conditionCombinationType.all) {
					tmpType = ChildActivitySetType.all;
				}
				
				allCorrectResponse = evaluateChildActivityOperator(
						allCorrectResponse, correctCondition, tmpType);
			}
			
		}
		/**
		 * Finalment si tot és correcte doncs retornem el 
		 * rollupAction corresponent. Si falla doncs retornem null.
		 */
		if (allCorrectResponse == SequencingStatus.Passed) {
			return actionType;
		} else {
			return null;
		}
		
	}
	/**
	 * Evaluarà el RollupConsiderations i retornarà un boolean
	 * depenen de si aquest seqüenciament contribueix a fer el rollup
	 * o no.
	 * 
	 * @param required :  Tipus d'avaluació que volem fer.
	 * @param userObjective : UserObjective Type
	 * @return boolean Type
	 */
	private boolean evaluateRollupConsiderations(
			final RollupConsiderations.rollupConsiderationsType required,
			final UserObjective userObjective) {
		if (required 
				== RollupConsiderations.rollupConsiderationsType.always) {
			return true;			
		} else if (required
				== RollupConsiderations.rollupConsiderationsType.
					ifNotSuspended) {
			return true;			
		} else if ((required
				== RollupConsiderations.rollupConsiderationsType.ifAttempted) 
				&& (attempted(userObjective) 
						== Constants.SequencingStatus.Passed)) {
			return true;			
		} else if (required
				== RollupConsiderations.rollupConsiderationsType.ifNotSkipped) {
			/**
			 * TODO:S'haurien de fer més probes per saber-ho...
			 */
			return true;
		}		
		return false;
	}	
	/**
	 * Ens retorna un iterador amb els fills de l'item al que pertany aquest
	 * seqüenciament. Serà molt útil quan tinguem d'avaluar el seqüenciament
	 * dels fills en un rollup o en un conditionRule.
	 * 
	 * @return Iterator < Abstract_Item >
	 */
	private Iterator < Abstract_Item > getAvailableChildIterator() {
		if (itemProperty != null) {
			Root_Item itemCluster = null;		
			if (itemProperty instanceof Root_Item) {
				itemCluster = (Root_Item) itemProperty;
				return itemCluster.getChildrenIterator();
			}			
		} else {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] Ens hem trobat un " 
						+ "itemProperty == null!.");				
			}					
		}
		return null;		
	}
	/**
	 * Aquesta funció ens buscarà en el RollupRules i ens retornarà 
	 * un Collection amb totes les regles que com a resultat de la seva
	 * condició fasin un action del tipus que estem buscant.
	 * 
	 * @param actionType : RollupActionType
	 * @return Collection < RollupRule > : Conjunt de resposta.
	 */
	private Collection < RollupRule > rollupActionSearcher(
			final RollupActionType actionType) {
		Collection < RollupRule > collectByAction =
			new ArrayList < RollupRule > ();
		for (Iterator ruleIterator = rollupRule.iterator();
		ruleIterator.hasNext();) {
			RollupRule newRollup = (RollupRule) ruleIterator.next();
			if (newRollup.rollupAction == actionType) {
				collectByAction.add(newRollup);
			}
		}						
		return collectByAction;		
	}		
}

