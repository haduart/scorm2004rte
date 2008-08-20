// $Id: ParseConditionRule.java,v 1.3 2007/11/30 13:29:18 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.validator.
	concreteParsers.sequencingChilds;

import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;


import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Sequencing;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.SequencingCondition;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.SequencingRules;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Types.OperatorType;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMTreeUtility;

/**
* $Id: ParseConditionRule.java,v 1.3 2007/11/30 13:29:18 ecespedes Exp $
* <b>Títol:</b> ParseConditionRule<br><br>
* <b>Descripció:</b> Aquesta classe parseja les regles condicionals del<br>
*  manifest (SequencingRules), agafant i tractant tots els atributs.
*  <br><br> 
*
* @author Eduard Céspedes i Borràs /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versió $Revision: 1.3 $ $Date: 2007/11/30 13:29:18 $
* $Log: ParseConditionRule.java,v $
* Revision 1.3  2007/11/30 13:29:18  ecespedes
* SequencingRules implementades!
*
* Revision 1.2  2007/11/15 15:24:04  ecespedes
* Implementat el ObjectiveInterface en els Leaf_Item.
*
*/
public class ParseConditionRule {

	/**
	 * És la variable sobre la que muntarem les sequencingRules,
	 * i és la que retornarem amb el getSequencingObject().
	 * 
	 * Squencing Type.
	 */
	private Sequencing sequencing;
	/**
	 * Serà el parsejador de la condició del SequencingRule.
	 * 
	 * @param sequencingNode : Node Type. 
	 * @param sequencingObject : Sequencing Type.
	 */
	public ParseConditionRule(
			final Node sequencingNode, final Sequencing sequencingObject) {
		sequencing = sequencingObject;
				
		//Agafem els fills...		
		Vector < Node > conditionRuleVector =
			DOMTreeUtility.getALLNodes(sequencingNode);
		
		if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
			System.out.println("\t[ConditionRule-START]");
		}
		for (Iterator conditionRuleIterator = conditionRuleVector.iterator(); 
				conditionRuleIterator.hasNext();) {
			
			Node conditionRuleChildNode = (Node) conditionRuleIterator.next();
			if (conditionRuleChildNode == null) {
				
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) {
					System.out.println("[ERROR] a l'agafar un atribut" 
							+ " dintre de les <sequencingRules>!!");
				}
			} else {
				if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
					System.out.println("\t\t[ConditionRule]: "
							+ conditionRuleChildNode.getLocalName());
				}
				
				SequencingRules nouSequencingRules = new SequencingRules();
				
				SequencingRules.conditionRuleType conditionRule =
					searchConditionRuleType(
							conditionRuleChildNode.getLocalName());
				
				if (conditionRule != null) {
					nouSequencingRules.conditionRule = conditionRule;
					
					ParseXConditionRule nouParseXConditionRule =
						new ParseXConditionRule(
								conditionRuleChildNode, nouSequencingRules);
					
					if (nouParseXConditionRule.sequencingRules != null) {
						sequencing.sequencingRules.add(
								nouParseXConditionRule.sequencingRules);
					}					
				}				
			}
		}
		if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
			System.out.println("\t[ConditionRule-END]");
		}
	}
	/**
	 * Ens retorna el seqüenciament amb les seves 
	 * condicions afegides. 
	 * 
	 * @return Sequencing Type.
	 */
	public final Sequencing getSequencingObject() {
		return sequencing;
	}
	/**
	 * Classe privada que parsejara una condició específica.
	 * 
	 * @author Eduard Céspedes i Borràs (AKA ecespedes)
	 *
	 */
	private class ParseXConditionRule {
		/**
		 * Aquesta serà la variable que "retornarem".
		 */
		public SequencingRules sequencingRules = new SequencingRules();
		/**
		 * Constructor on li passem el node de la condició i la regla del
		 * seqüenciament. 
		 * 
		 * @param conditionRuleNode : Node Type.
		 * @param nouSequencingRules : SequencingRules Type.
		 */
		public ParseXConditionRule(
				final Node conditionRuleNode,
				final SequencingRules nouSequencingRules) {
			sequencingRules = nouSequencingRules;
						
			/**
			 * Ni el <preCondtionRule> ni el <postConditionRule> ni	
			 * l'<exitConditionRule> tenen atributs.
			 */
			
			/**
			 * 	Agafem els Elements...
			 */					
			Vector < Node > conditionRuleVector =
				DOMTreeUtility.getALLNodes(conditionRuleNode);
			
			if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
				System.out.println("\t\t\t["
						+ sequencingRules.conditionRule.toString()
						+ "-START]");
			}
			for (Iterator conditionRuleIterator =
					conditionRuleVector.iterator();
						conditionRuleIterator.hasNext();) {
				Node conditionRuleChildNode = 
					(Node) conditionRuleIterator.next();
				if (conditionRuleChildNode == null) {
					if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) {
						System.out.println("[ERROR] a l'agafar un " 
								+ "atribut dintre de les conditionRule!!");
					}
				} else {
					if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
						System.out.println("\t\t\t\t["
								+ sequencingRules.conditionRule.toString()
								+ " child]: "
								+ conditionRuleChildNode.getLocalName());
					}
					
					analizeSequencingRules(conditionRuleChildNode);
									
				}
			}
			if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
				System.out.println("\t\t\t["
						+ sequencingRules.conditionRule.toString()
						+ "-END]");
			}
			
		}		

		/**
		 * Analitzem el tipus de node "fill" de la condició.
		 * 
		 * @param nodeLocal : Node Type.
		 */
		private void analizeSequencingRules(Node nodeLocal){
			String nodeName = nodeLocal.getLocalName();	
			
			
			if (nodeName.equals("ruleConditions")) {
				/**
				 * Agafem l'atribut 'conditionCombination'
				 */
				String conditionCombination = 
					DOMTreeUtility.getAttributeValue(
							nodeLocal, "conditionCombination");
				if (conditionCombination.length() == 0) {
					if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
						System.out.println("Atribut [ruleConditions]" 
								+ ".conditionCombination no trobat.");
					}
				} else {
					SequencingRules.conditionCombinationType nouTipus =
						searchConditionCombinationType(conditionCombination);
					if (nouTipus != null) {
						sequencingRules.conditionCombination = nouTipus;
					}
				}
				
				/**
				 * agafar tots els rollupCondition que tinguem.
				 */
				Vector < Node > ruleConditionsVector =
					DOMTreeUtility.getALLNodes(nodeLocal);
				
				for (Iterator ruleConditionsIterator =
					ruleConditionsVector.iterator();
					ruleConditionsIterator.hasNext();) {
					
					/**
					 * RollupCondition nouRollupcondition = 
					 * 		new RollupCondition();
					 */
					SequencingCondition nouSequencingCondition =
						new SequencingCondition();
					
					Node ruleConditionChildNode = 
						(Node) ruleConditionsIterator.next();
					if (ruleConditionChildNode == null 
							|| !ruleConditionChildNode.getLocalName()
							.equals("ruleCondition")) {
						if (Constants.DEBUG_ERRORS 
								|| Constants.DEBUG_SEQUENCING) {
							System.out.println("[ERROR] a l'agafar " 
									+ "un atribut dintre de les " 
									+ "ruleConditions!!");
						}
					} else {
						/**
						 * Agafem l'atribut 'Operator'
						 */
						String operator =
							DOMTreeUtility.getAttributeValue(
									ruleConditionChildNode, "operator");
						
						if (operator.length() == 0) {
							if (Constants.DEBUG_WARNINGS_LOW 
									&& Constants.DEBUG_SEQUENCING) {
								System.out.println("Atribut [ruleCondition]" 
										+ ".operator no trobat.");
							}
						} else {
							if (operator.equals(OperatorType.not.toString())) {
								nouSequencingCondition.operator =
									OperatorType.not;
							} else {
								nouSequencingCondition.operator =
									OperatorType.NoOp;
							}
						}
						/**
						 * Agafem l'atribut 'mesureThreshold'
						 */
						String mesureThreshold =
							DOMTreeUtility.getAttributeValue(
									ruleConditionChildNode, "measureThreshold");
						
						if (mesureThreshold.length() == 0) {
							if (Constants.DEBUG_WARNINGS_LOW 
									&& Constants.DEBUG_SEQUENCING) {
								System.out.println("Atribut [ruleCondition]." 
										+ "mesureThreshold no trobat.");
							}
						} else {
							double valor = 
								new Double(mesureThreshold).doubleValue();
							
							if (valor > 1) {
								valor = 1.0;
							} else if (valor < 0) {
								valor = 0.0;
							}
							
							nouSequencingCondition.measureThreshold = valor;
						}

						/**
						 * Agafem l'atribut 'referencedObjective'
						 */
						String referencedObjective =
							DOMTreeUtility.getAttributeValue(
									ruleConditionChildNode,
									"referencedObjective");
						
						if (referencedObjective.length() == 0) {
							if (Constants.DEBUG_WARNINGS_LOW 
									&& Constants.DEBUG_SEQUENCING) {
								System.out.println("Atribut [ruleCondition]" 
										+ ".referencedObjective no trobat.");
							}
						} else {
							nouSequencingCondition.referencedObjective =
								referencedObjective;
						}
						
						/**
						 * Agafem l'atribut 'condition'
						 */
						String condition = 
							DOMTreeUtility.getAttributeValue(
									ruleConditionChildNode, "condition");
						
						if (condition.length() == 0) {
							if (Constants.DEBUG_WARNINGS_LOW 
									&& Constants.DEBUG_SEQUENCING) {
								System.out.println("Atribut [rollupCondition]" 
										+ ".condition no trobat.");
							}
						} else {
							
							//Tractem l'Enumeració ... és un merder...!!!
							SequencingCondition.conditionType[] conditionArray =
								SequencingCondition.conditionType.values();
							
							for (int i  = 0; i < conditionArray.length; i++) {
								if (conditionArray[i].toString()
										.equals(condition)) {
									nouSequencingCondition.condition =
										conditionArray[i];
									continue;
								}
							}
						}						
					}
					sequencingRules.condition.add(nouSequencingCondition);
					
					if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
						System.out.println("\t\t\t\t\t[SequencingCondition]:{"
								+ "\n\t\t\t\t\t\tmeasureThreshold: " 
								+ nouSequencingCondition.measureThreshold
								+ "\n\t\t\t\t\t\treferencedObjective: " 
								+ nouSequencingCondition.referencedObjective 
								+ "\n\t\t\t\t\t\tcondition : {" 
								+ nouSequencingCondition.operator.toString()
								+ "} " 
								+ nouSequencingCondition.condition.toString() 
								+ "} ");
					}
				}						

				//rollupConditions-END!!!
			} else if (nodeName.equals("ruleAction")) {
				int agafat = 0;
				Vector < Attr > ruleActionVector = 
					DOMTreeUtility.getALLAttribute(nodeLocal);
				
				for (Iterator ruleActionIterator = ruleActionVector.iterator(); 
					ruleActionIterator.hasNext();) {
					
					Attr ruleActionChildNode = (Attr) ruleActionIterator.next();
					if (ruleActionChildNode == null) {
						if (Constants.DEBUG_ERRORS 
								|| Constants.DEBUG_SEQUENCING) {
							System.out.println("[ERROR] a l'agafar un atribut" 
									+ " dintre de les ruleAction!!");
						}
					} else {
						agafat++;
						SequencingRules.ruleActionType valorActionType;
						
						if (ruleActionChildNode.getValue().equals("continue")) {
							valorActionType = 
								SequencingRules.ruleActionType._continue;
						} else {
							valorActionType = 
								searchRuleActionType(
										ruleActionChildNode.getValue());
						}
						
						sequencingRules.ruleAction = valorActionType;
						
						if (Constants.DEBUG_INFO 
								|| Constants.DEBUG_SEQUENCING) {
							System.out.println("\t\t\t\t\t[ruleAction]: "
									+ sequencingRules.ruleAction.toString());
						}
					}
				}
				if (agafat != 1) {
					if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) {
						System.out.println("[ERROR] Hem trobat "
								+ agafat + " ruleAction diferents.");
					}
				}						
			} else {
				if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING) {
					System.out.println("\t[WARNING] L'element '" + nodeName 
							+ "' no l'estem controlant.");			
				}
			}
			
			
		}
		/**
		 * Funció que recorre el Collection buscant una condició.
		 * 
		 * @param searchedType : String Type.
		 * @return conditionCombinationType Type.
		 */
		private final SequencingRules.conditionCombinationType 
				searchConditionCombinationType(
						final String searchedType) {
			
			//	Tractem l'Enumeració... és un merder!
			SequencingRules.conditionCombinationType sortida = null;
			SequencingRules.
				conditionCombinationType[] 
				                         conditionRuleTypeArray = 
				                        	 SequencingRules.
				                        	 conditionCombinationType.values();
			
			for (int i  = 0; i < conditionRuleTypeArray.length; i++) {
				if (conditionRuleTypeArray[i].toString().equals(searchedType)) {
					sortida = conditionRuleTypeArray[i];
					continue;
				}
			}
			return sortida;
		}
		
		private SequencingRules.ruleActionType searchRuleActionType(String searchedType){
			//	Tractem l'Enumeració... és un merder!
			SequencingRules.ruleActionType sortida = null;
			SequencingRules.ruleActionType[] conditionRuleTypeArray =  SequencingRules.ruleActionType.values();
			for (int i  = 0 ; i < conditionRuleTypeArray.length; i++){					
				if (conditionRuleTypeArray[i].toString().equals(searchedType)){
					sortida = conditionRuleTypeArray[i];
					continue;
				}
			}
			return sortida;
		}
		
	}
	
	private SequencingRules.conditionRuleType searchConditionRuleType(String searchedType){
		//	Tractem l'Enumeració... és un merder!
		SequencingRules.conditionRuleType sortida = null;
		SequencingRules.conditionRuleType[] conditionRuleTypeArray =  SequencingRules.conditionRuleType.values();
		for (int i  = 0 ; i < conditionRuleTypeArray.length; i++){					
			if (conditionRuleTypeArray[i].toString().equals(searchedType)){
				sortida = conditionRuleTypeArray[i];
				continue;
			}
		}
		return sortida;
	}

}

