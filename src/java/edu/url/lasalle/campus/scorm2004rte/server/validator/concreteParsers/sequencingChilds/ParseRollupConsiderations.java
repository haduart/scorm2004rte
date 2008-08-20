// $Id: ParseRollupConsiderations.java,v 1.1 2007/10/24 10:33:10 ecespedes Exp $
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

import org.w3c.dom.Node;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.RollupConsiderations;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMTreeUtility;

public class ParseRollupConsiderations {
	
	public RollupConsiderations rollupConsiderations = new RollupConsiderations();
	
	private boolean correct = false;
	
	public ParseRollupConsiderations(Node node){		
		correct = analizeRollupconsiderations(node);
	}	

	private boolean analizeRollupconsiderations(Node rollupConsiderationsNode){
		boolean correcte = true;
				
		//	parsegem els atributs, si és que en té. 
		String requiredForSatisfied = DOMTreeUtility.getAttributeValue(rollupConsiderationsNode, "requiredForSatisfied");		
		if (requiredForSatisfied.length() == 0){
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
				System.out.println("Atribut randomizationTiming no trobat.");
		}		
		else{			
			RollupConsiderations.rollupConsiderationsType searchType = searchRollupConsiderationsType(requiredForSatisfied);			
			if (searchType != null){
				rollupConsiderations.requiredForSatisfied =  searchType;				
			}
			else{
				correcte =  false;
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
					System.out.println("[ERROR Rollupconsiderations] requiredForSatisfied té" +
						" un valor no vàlid.");
			}
		}
		
		//Atribut requiredForNotSatisfied 
		String requiredForNotSatisfied = DOMTreeUtility.getAttributeValue(rollupConsiderationsNode, "requiredForNotSatisfied");		
		if (requiredForNotSatisfied.length() == 0){
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
				System.out.println("Atribut randomizationTiming no trobat.");
		}		
		else{			
			RollupConsiderations.rollupConsiderationsType searchType = searchRollupConsiderationsType(requiredForNotSatisfied);			
			if (searchType != null){
				rollupConsiderations.requiredForNotSatisfied =  searchType;				
			}
			else{
				correcte =  false;
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
					System.out.println("[ERROR Rollupconsiderations] requiredForNotSatisfied té" +
						" un valor no vàlid.");
			}
		}
		
		//Atribut requiredForCompleted 
		String requiredForCompleted = DOMTreeUtility.getAttributeValue(rollupConsiderationsNode, "requiredForCompleted");		
		if (requiredForCompleted.length() == 0){
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
				System.out.println("Atribut randomizationTiming no trobat.");
		}		
		else{			
			RollupConsiderations.rollupConsiderationsType searchType = searchRollupConsiderationsType(requiredForCompleted);			
			if (searchType != null){
				rollupConsiderations.requiredForCompleted =  searchType;				
			}
			else{
				correcte =  false;
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
					System.out.println("[ERROR Rollupconsiderations] requiredForCompleted té" +
						" un valor no vàlid.");
			}
		}
		
		//		Atribut requiredForIncomplete 
		String requiredForIncomplete = DOMTreeUtility.getAttributeValue(rollupConsiderationsNode, "requiredForIncomplete");		
		if (requiredForIncomplete.length() == 0){
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
				System.out.println("Atribut randomizationTiming no trobat.");
		}		
		else{			
			RollupConsiderations.rollupConsiderationsType searchType = searchRollupConsiderationsType(requiredForIncomplete);			
			if (searchType != null){
				rollupConsiderations.requiredForIncomplete =  searchType;				
			}
			else{
				correcte =  false;
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
					System.out.println("[ERROR Rollupconsiderations] requiredForIncomplete té" +
						" un valor no vàlid.");
			}
		}
		
		
		//	Atribut measureSatisfactionIfActive 
		String measureSatisfactionIfActive = DOMTreeUtility.getAttributeValue(rollupConsiderationsNode, "measureSatisfactionIfActive");		
		if (measureSatisfactionIfActive.length() == 0){
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
				System.out.println("Atribut randomizationTiming no trobat.");
		}		
		else{
			rollupConsiderations.measureSatisfactionIfActive = measureSatisfactionIfActive.equals(Constants.FALSE) ? false : true;			
		}			
			
		return correcte;		
	}
	
	private RollupConsiderations.rollupConsiderationsType searchRollupConsiderationsType(String searchedType){
		//	Tractem l'Enumeració... és un merder!
		RollupConsiderations.rollupConsiderationsType sortida = null;
		RollupConsiderations.rollupConsiderationsType[] rollupConsiderationsArray =  RollupConsiderations.rollupConsiderationsType.values();
		for (int i  = 0 ; i < rollupConsiderationsArray.length; i++){					
			if (rollupConsiderationsArray[i].toString().equals(searchedType)){
				sortida = rollupConsiderationsArray[i];
				continue;
			}
		}
		return sortida;
	}
	
	public boolean getIsAllCorrect(){
		return correct;		
	}	


	

}

