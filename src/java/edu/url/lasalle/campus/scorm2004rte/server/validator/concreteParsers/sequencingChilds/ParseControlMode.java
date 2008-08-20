// $Id: ParseControlMode.java,v 1.2 2007/10/24 10:52:56 ecespedes Exp $
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

import org.w3c.dom.Node;
import org.w3c.dom.Attr;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.AdlseqControlMode;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMTreeUtility;

public class ParseControlMode {
	//Constants locals
	private static final String CHOICE 		= 	"choice";
	private static final String CHOICEEXIT 	= 	"choiceExit";
	private static final String FLOW 		= 	"flow";
	private static final String FORWARDONLY = 	"forwardOnly";
	private static final String USECURRENTATTEMPTOBJECTIVEINFO 	= "useCurrentAttemptObjectiveInfo";
	private static final String USECURRENTATTEMPTPROGRESSINFO 	= "useCurrentAttemptProgressInfo";
	
	public AdlseqControlMode adlseqControlMode = new AdlseqControlMode();
	
	public ParseControlMode(Node controlModeNode){
		
		Vector attrVector = DOMTreeUtility.getALLAttribute(controlModeNode);
		if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
			System.out.println("\t[ControlMode-START]");
		for (Iterator attrIterator = attrVector.iterator(); 
				attrIterator.hasNext();)
		{
			Attr CMAttr = (Attr)attrIterator.next();
			if (CMAttr == null){
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING)
					System.out.println("[ERROR] a l'agafar un atribut dintre del ControlMode!!");
			}
			else{
				if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
					System.out.println("\t\t[ControlMode]: "+CMAttr.getLocalName()+"\t"+CMAttr.getNodeValue());
				analizeAttr(CMAttr);
			}
		}
		if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING)
			System.out.println("\t[ControlMode-END]");
	}
	private void analizeAttr(Attr attrLocal){
		String attrName = attrLocal.getLocalName();
		String attrValue = attrLocal.getNodeValue();
				
		if (attrName.equals(CHOICE)){
			adlseqControlMode.choice = attrValue.equals(Constants.FALSE) ? false : true;			
		}
		else if (attrName.equals(CHOICEEXIT)){
			adlseqControlMode.choiceExit = attrValue.equals(Constants.FALSE) ? false : true;			
		}
		else if (attrName.equals(FLOW)){
			adlseqControlMode.flow = attrValue.equals(Constants.FALSE) ? false : true;
		}
		else if (attrName.equals(FORWARDONLY)){
			adlseqControlMode.forwardOnly= attrValue.equals(Constants.FALSE) ? false : true;			
		}
		else if (attrName.equals(USECURRENTATTEMPTOBJECTIVEINFO)){
			adlseqControlMode.useCurrentAttemptObjectiveInfo= attrValue.equals(Constants.FALSE) ? false : true;			
		}
		else if (attrName.equals(USECURRENTATTEMPTPROGRESSINFO)){
			adlseqControlMode.useCurrentAttemptProgressInfo= attrValue.equals(Constants.FALSE) ? false : true;			
		}	
		else{
			if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING)
				System.out.println("\t[WARNING] El Atribut '"+attrName+"' no l'estem controlant.");
		}		
	}
}

