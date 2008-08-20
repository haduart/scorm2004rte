// $Id: ParseRandomizeControls.java,v 1.1 2007/10/24 10:33:10 ecespedes Exp $
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

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Sequencing;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Types.RandomizationType;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMTreeUtility;


public class ParseRandomizeControls {
	public Sequencing sequencing;
	private boolean correct = false;
	
	public ParseRandomizeControls(Node node, Sequencing randSequencing){
		sequencing = randSequencing;
		correct = analizeRandomizeControls(node);
	}	

	private boolean analizeRandomizeControls(Node randomizeNode){
		boolean correcte = true;
		
		boolean randomizationTimingBool = false;	
		boolean selectionTimingBool = false;
		boolean selectCountBool = false;
		boolean reorderChildrenBool = false;
		RandomizationType selectionTimingType = null;
		
		//	parsegem els atributs, si és que en té. 
		String randomizationTiming = DOMTreeUtility.getAttributeValue(randomizeNode, "randomizationTiming");		
		if (randomizationTiming.length() == 0){
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
				System.out.println("Atribut randomizationTiming no trobat.");
		}		
		else{
			randomizationTimingBool = true;
			RandomizationType searchType = searchRandomizeType(randomizationTiming);			
			if (searchType != null){
				sequencing.randomizationTiming =  searchType;				
			}
			else
			{
				if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING)
					System.out.println("[ERROR RandomizationControls] randomizationTiming té" +
						" un valor no vàlid.");
			}
		}			 
		String reorderChildren = DOMTreeUtility.getAttributeValue(randomizeNode, "reorderChildren");
		if (reorderChildren.length() == 0){			
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
				System.out.println("Atribut reorderChildren no trobat.");
		}
		else{
			reorderChildrenBool = true;
			sequencing.reorderChildren = new Boolean(reorderChildren).booleanValue();
		}
		String selectCount = DOMTreeUtility.getAttributeValue(randomizeNode, "selectCount");
		if (selectCount.length() == 0){			
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
				System.out.println("Atribut selectCount no trobat.");
		}
		else{
			selectCountBool = true;
			sequencing.selectCount = new Integer(selectCount).intValue();
		}
		String selectionTiming = DOMTreeUtility.getAttributeValue(randomizeNode, "selectionTiming");
		if (selectCount.length() == 0){			
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_SEQUENCING)
				System.out.println("Atribut selectionTiming no trobat.");
		}
		else{			
			selectionTimingBool  = true;
			RandomizationType searchType = searchRandomizeType(selectionTiming);
			if (searchType != null)
				selectionTimingType =  searchType;
			else{
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
					System.out.println("[ERROR RandomizationControls] selectionTiming té" +
						" un valor no vàlid.");
			}
			
		}
		
		/*---------- Mirem que no hi hagi cap problema 
		 * 						i si podem ho arreglem -------------- */
		
		if (reorderChildrenBool && sequencing.reorderChildren == false){
			if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING)
				System.out.println("[WARNING] reorderChildren està assignat a false. "+
					"Els altres valors s'anul·laran o es deixaran per defecte");
			sequencing.selectCount  = 0;
			sequencing.randomizationTiming = RandomizationType.never;
		}
		else if ((!reorderChildrenBool) 
				&& (randomizationTimingBool || selectionTimingBool || selectCountBool)){
			reorderChildrenBool = true;
			sequencing.reorderChildren = true;									
		}
		else if (reorderChildrenBool){
			if (randomizationTimingBool && selectionTimingBool){
				if (sequencing.randomizationTiming != selectionTimingType){
					if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING)
						System.out.println("[WARNING/ERROR] Ni a nivell teòric ni a nivell\n\t" +
							"pràctic es pot apreciar la diferència entre l’atribut\n\t" +
							"RandomizationTiming i SelectionTiming per tant, a efectes\n\t" +
							"pràctics el que es recomana és tractar-los igual.\n\t" +
							"Igualarem selectionTiming i randomizationTiming.");					
				}
			}
			else if(!randomizationTimingBool && selectionTimingBool){
				if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING)
					System.out.println("[WARNING/ERROR][2] Ni a nivell teòric ni a nivell\n\t" +
						"pràctic es pot apreciar la diferència entre l’atribut\n\t" +
						"RandomizationTiming i SelectionTiming per tant, a efectes\n\t" +
						"pràctics el que es recomana és tractar-los igual.\n\t" +
						"Igualarem selectionTiming i randomizationTiming\n\t");
				sequencing.randomizationTiming = selectionTimingType;
				randomizationTimingBool = true;
			}//No es donarà mai aquesta condició perquè el parser automàticament agafa el valor never encara 
			//	hi hagi cap valor. 
			/*
			else if(!randomizationTimingBool && !selectionTimingBool){
				System.out.println("[ERROR3/WARNING] No serveix de res tindre activat el\n\t" +
						"<randomizationControls> si tens el randomizationTiming = never.\n\t" +
						"T'assigno a randomizationTiming = OnEachNewAttempt. Si no és això\n\t" +
						"el que volies especifica-ho directament en el imssmanifest.xml.");
				sequencing.randomizationTiming = RandomizationType.onEachNewAttempt;
			}*/
		}		
		else{
			if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING)
				System.out.println("[WARNING] No s'ha aplicat cap canvi en el " +
					"RandomizeControls");
			correcte = false;
		}
		
		return correcte;		
	}
	
	private RandomizationType searchRandomizeType(String searchedType){
		//	Tractem l'Enumeració... és un merder!
		RandomizationType sortida = null;
		RandomizationType[] randomizationArray =  RandomizationType.values();
		for (int i  = 0 ; i < randomizationArray.length; i++){					
			if (randomizationArray[i].toString().equals(searchedType)){
				sortida = randomizationArray[i];
				continue;
			}
		}
		return sortida;
	}
	
	public boolean getIsAllCorrect(){
		return correct;		
	}	


}

