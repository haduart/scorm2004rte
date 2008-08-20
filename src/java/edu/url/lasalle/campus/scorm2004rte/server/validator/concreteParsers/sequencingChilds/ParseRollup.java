// $Id: ParseRollup.java,v 1.4 2007/11/29 15:54:35 ecespedes Exp $
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.  
 */

package edu.url.lasalle.campus.scorm2004rte.server.validator.concreteParsers.sequencingChilds;

import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.RollupCondition;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.RollupRule;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Sequencing;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Types.ChildActivitySetType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Types.ConditionType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Types.OperatorType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Types.RollupActionType;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMTreeUtility;


public class ParseRollup {
	
	private Sequencing sequencing;
	
	public ParseRollup(Node sequencingNode, Sequencing sequencingObject){
		sequencing = sequencingObject;
		
		//Agafem els atributs...
		String rollupObjectiveSatisfied = DOMTreeUtility.getAttributeValue(sequencingNode, "rollupObjectiveSatisfied");
		if (rollupObjectiveSatisfied.length() == 0){
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
				System.out.println("Atribut rollupObjectiveSatisfied no trobat.");			
		}
		sequencing.rollupObjectiveSatisfied = rollupObjectiveSatisfied.equals(Constants.FALSE) ? false : true;
		
		String rollupProgressCompletion = DOMTreeUtility.getAttributeValue(sequencingNode, "rollupProgressCompletion");
		if (rollupProgressCompletion.length() == 0){
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
				System.out.println("Atribut rollupProgressCompletion no trobat.");			
		}
		sequencing.rollupProgressCompletion = rollupProgressCompletion.equals(Constants.FALSE) ? false : true;
		
		String objectiveMeasureWeight = DOMTreeUtility.getAttributeValue(sequencingNode, "objectiveMeasureWeight");
		if (objectiveMeasureWeight.length() == 0){
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
				System.out.println("Atribut objectiveMeasureWeight no trobat.");			
		}
		sequencing.rollupObjectiveMeasureWeight =
			new Double(objectiveMeasureWeight).doubleValue();
		
		//Agafem els fills...		
		Vector<Node> rollupVector = DOMTreeUtility.getALLNodes(sequencingNode);
		if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
			System.out.println("\t[RollupRules-START]");
		for (Iterator attrIterator = rollupVector.iterator(); 
				attrIterator.hasNext();)
		{
			Node rollupChildNode = (Node)attrIterator.next();
			if (rollupChildNode == null){
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
					System.out.println("[ERROR] a l'agafar un atribut dintre de les RollupRules!!");
			}
			else{
				if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
					System.out.println("\t\t[RollupRules]: "+rollupChildNode.getLocalName()+"\t"+rollupChildNode.getNodeValue());
				if (!rollupChildNode.getLocalName().equals(new String("rollupRule"))){
					if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
						System.out.println("[ERROR] Dintre d'un <RollupRules> només podem"+
							" tindre <rollupRules>!");					
				}
				else{
					RollupRule nouRollupRule = new RollupRule();
					ParseRollupRules nouParseRollupRules = new ParseRollupRules(rollupChildNode, nouRollupRule);
					if (nouParseRollupRules.rollupRule != null){
						sequencing.rollupRule.add(nouParseRollupRules.rollupRule);
					}
				}				
			}
		}
		if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
			System.out.println("\t[RollupRules-END]\trollupObjectiveSatisfied: "+sequencing.rollupObjectiveSatisfied 
				+"\trollupProgressCompletion: "+sequencing.rollupProgressCompletion+"\trollupObjectiveMeasureWeight: "
				+sequencing.rollupObjectiveMeasureWeight);
	}
	
	public Sequencing getSequencingObject(){
		return sequencing;
	}
	
	/**
	 * <b>ParseRollupRules</b>  <br />
	 * Serà una classe privada de ParseRollup per controlar els diversos <br /> 
	 * RollupRules amb els que ens podem trobar. <br />
	 * @author Eduard Céspedes
	 */	
	private class ParseRollupRules{
		public RollupRule rollupRule = null;
		
		public ParseRollupRules(Node rollupChildNode, RollupRule nouRollupRule){
			rollupRule = nouRollupRule;
			
			//Agafem els atributs...
			String childActivitySet = DOMTreeUtility.getAttributeValue(rollupChildNode, "childActivitySet");
			if (childActivitySet.length() == 0){
				if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
					System.out.println("Atribut childActivitySet no trobat.");			
			}
			else{
				//Tractem l'Enumeració... és un merder!
				ChildActivitySetType[] childActivityArray =  ChildActivitySetType.values();
				for (int i  = 0 ; i < childActivityArray.length; i++){					
					if (childActivityArray[i].toString().equals(childActivitySet)){
						rollupRule.childActivitySet = childActivityArray[i];
						continue;
					}
				}				
				if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
					System.out.println("\t\t\tchildActivitySet["+childActivitySet
							+"]\t"+rollupRule.childActivitySet.toString());											
			}
						
			String minimumCount = DOMTreeUtility.getAttributeValue(rollupChildNode, "minimumCount");
			if (minimumCount.length() == 0){
				if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
					System.out.println("Atribut minimumCount no trobat.");			
			}
			else{
				rollupRule.minimumCount = new Integer(minimumCount).intValue();
				if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
					System.out.println("\t\t\tminimumCount\t"+rollupRule.minimumCount);
											
			}
			
			String minimumPercent = DOMTreeUtility.getAttributeValue(rollupChildNode, "minimumPercent");
			if (minimumPercent.length() == 0){
				if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
					System.out.println("Atribut minimumPercent no trobat.");			
			}
			else{
				rollupRule.minimumPercent = new Double(minimumPercent).doubleValue();
				if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
					System.out.println("\t\t\tminimumPercent\t"+rollupRule.minimumPercent);
				if (rollupRule.minimumPercent > 1.0 ){
					if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
						System.out.println("[ERROR] El valor minimumPercent és un real"+
							" de fins a 4 decimals que està acotat entre 0.0 i 1.0.");
					rollupRule.minimumPercent = 1.0;
				}
				else if(rollupRule.minimumPercent < 0.0 ){
					if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
						System.out.println("[ERROR] El valor minimumPercent és un real"+
					" de fins a 4 decimals que està acotat entre 0.0 i 1.0.");
					rollupRule.minimumPercent = 0.0;
				}											
			}
			
			//	Agafem els Elements...					
			Vector<Node> rollupRulesVector = DOMTreeUtility.getALLNodes(rollupChildNode);
			if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
				System.out.println("\t\t\t[RollupRule-START]");
			for (Iterator rollupRulesIterator = rollupRulesVector.iterator(); 
			rollupRulesIterator.hasNext();)
			{
				Node rollupRulesChildNode = (Node)rollupRulesIterator.next();
				if (rollupChildNode == null){
					if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
						System.out.println("[ERROR] a l'agafar un atribut dintre de les RollupRules!!");
				}
				else{
					if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
						System.out.println("\t\t\t\t[RollupRules child]: "+rollupRulesChildNode.getLocalName());
					
					analizeRollupChild(rollupRulesChildNode);
									
				}
			}
			if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
				System.out.println("\t\t\t[RollupRule-END]");
			
		}		

		private void analizeRollupChild(final Node nodeLocal) {
			String nodeName = nodeLocal.getLocalName();	
			
			
			if (nodeName.equals("rollupConditions")) {
				//Agafem l'atribut conditionCombination
				String conditionCombination = 
					DOMTreeUtility.getAttributeValue(nodeLocal,
							"conditionCombination");
				if (conditionCombination.length() == 0) {
					if (Constants.DEBUG_WARNINGS_LOW 
							&& Constants.DEBUG_SEQUENCING) {
						System.out.println("Atribut [rollupConditions]."
								+ "conditionCombination no trobat.");
					}
				} else {
					if (conditionCombination.
							equals(RollupRule.
									conditionCombinationType.all.toString())) {
						rollupRule.conditionCombination =
							RollupRule.conditionCombinationType.all;
					} else {
						rollupRule.conditionCombination =
							RollupRule.conditionCombinationType.any;
					}
				}
				
				//agafar tots els rollupCondition que tinguem.
				Vector<Node> rollupConditionsVector = DOMTreeUtility.getALLNodes(nodeLocal);
				
				for (Iterator rollupConditionsIterator = rollupConditionsVector.iterator(); 
				rollupConditionsIterator.hasNext();)
				{
					RollupCondition nouRollupcondition = new RollupCondition();
					
					Node rollupConditionChildNode = (Node)rollupConditionsIterator.next();
					if (rollupConditionChildNode == null){
						if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
							System.out.println("[ERROR] a l'agafar un atribut dintre de les rollupConditions!!");
					}
					else{						
						//Agafem l'atribut 'Operator'
						String operator = DOMTreeUtility.getAttributeValue(rollupConditionChildNode, "operator");
						if (operator.length() == 0){
							if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
								System.out.println("Atribut [rollupCondition].operator no trobat.");			
						}
						else{
							if (operator.equals(OperatorType.not.toString())){
								nouRollupcondition.operator = OperatorType.not;							
							}
							else{
								nouRollupcondition.operator = OperatorType.NoOp;
							}
						}
						
						//Agafem l'atribut 'condition'
						String condition = DOMTreeUtility.getAttributeValue(rollupConditionChildNode, "condition");
						if (condition.length() == 0){
							if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
								System.out.println("Atribut [rollupCondition].condition no trobat.");			
						}
						else{							
							//Tractem l'Enumeració ... és un merder...!!!
							ConditionType[] conditionArray =  ConditionType.values();
							for (int i  = 0 ; i < conditionArray.length; i++){					
								if (conditionArray[i].toString().equals(condition)){
									nouRollupcondition.condition = conditionArray[i];
									continue;
								}
							}
						}						
					}
					rollupRule.condition.add(nouRollupcondition);
					if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
						System.out.println("\t\t\t\t\t[rollupCondition]:{"
								+ nouRollupcondition.operator.toString() + "} " 
								+ nouRollupcondition.condition.toString());
				}						

				//rollupConditions-END!!!
			}
			else if (nodeName.equals("rollupAction")) {
				int agafat = 0;
				Vector<Attr> rollupActionVector = DOMTreeUtility.getALLAttribute(nodeLocal);
				
				for (Iterator rollupActionIterator = rollupActionVector.iterator(); 
				rollupActionIterator.hasNext();)
				{
					Attr rollupActionChildNode = (Attr)rollupActionIterator.next();
					if (rollupActionChildNode == null){
						if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
							System.out.println("[ERROR] a l'agafar un atribut dintre de les rollupAction!!");
					}
					else{						
						agafat++;
						//Tractem l'Enumeració ... és un merder...!!!
						RollupActionType[] RollupActionArray =  RollupActionType.values();
						for (int i  = 0 ; i < RollupActionArray.length; i++){					
							if (RollupActionArray[i].toString().equals(rollupActionChildNode.getValue())){
								rollupRule.rollupAction = RollupActionArray[i];
								continue;
							}
						}
						if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
							System.out.println("\t\t\t\t\t[rollupAction]: "
									+ rollupRule.rollupAction.toString());
					}
				}
				if (agafat != 1){
					if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
						System.out.println("[ERROR] Hem trobat "+agafat+" rollupActions diferents.");
				}						
			}
			else{
				if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING)
					System.out.println("\t[WARNING] L'element '"+nodeName+
							"' no l'estem controlant.");			
			}
			
		}
		
		

	}		
}

