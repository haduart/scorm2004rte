// $Id: ParseResource.java,v 1.1 2007/10/24 10:33:10 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.validator.concreteParsers;

import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Node;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Files;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Resource;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMTreeUtility;
import edu.url.lasalle.campus.scorm2004rte.server.validator.concreteParsers.ParseFile;


public class ParseResource {
	private Node resourceNode;
		
	private String identifier;
	private String scormType;
	//private Collection files = new ArrayList();
	private String href;
	
	private Resource resource = new Resource();
	
	private boolean error;
	private String error_message;
	
	private boolean searchHref(String href){		
		for (Iterator iteratorSearcher =  resource.getFileIterator(); iteratorSearcher.hasNext();){
			if (href.equals(((Files)iteratorSearcher.next()).href))					
				return true;			
		}
		return false;
	}	
	
	public boolean getError(){
		return error;
	}
	
	public String getErrorMessage(){
		return error_message;
	}
	public Resource getResource(){
		return resource;
	}
	
	public ParseResource(Node newResource, String path){
		
		resourceNode = newResource;
		
		identifier = DOMTreeUtility.getAttributeValue(resourceNode, Constants.IDENTIFIER);
		if (identifier.length() == 0){
			if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES)
				System.out.println("[ERROR] a l'agafar l'identificador dels recursos!!");
		}
		resource.setIdentifier(identifier);
		
		scormType = DOMTreeUtility.getAttributeValue(resourceNode, Constants.RES_SCORMTYPE);
		if (scormType.length() == 0){
			if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES)
				System.out.println("ERROR a l'agafar l'scormType dels recursos!!");
		}
		resource.setScormType(scormType);
		
		href = DOMTreeUtility.getAttributeValue(resourceNode, Constants.HREF);
		if (href.length() == 0){
			if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES)
				System.out.println("ERROR a l'agafar l'href dels recursos!!");
		}
		resource.setHref(href);
		
		Vector filesInResources = DOMTreeUtility.getNodes(resourceNode, Constants.FILE);
		
		if (Constants.DEBUG_INFO || Constants.DEBUG_RESOURCES)
			System.out.println("[Resource-INICI]\t"+identifier+"\t"+scormType+"\t"+filesInResources.size());
					
		for (Iterator filesIterator = filesInResources.iterator(); 
					filesIterator.hasNext();)
		{	
			Node nouFile = (Node)filesIterator.next();				
			if (nouFile == null){
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES)
					System.out.println("[ERROR] a l'agafar nouFile d'un resource!!");
			}
			else{
				ParseFile nouParseFile = new ParseFile(nouFile,path);
				//files.add();
				resource.addFiles(nouParseFile.getFile());
			}
		}
		
		if (!searchHref(href)){
			if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES)
				System.out.println("[ERROR] dintre del resource '"+identifier +
					"'!!\n\tNo he trobat cap file que tingui '"+href+"'");
		}
		
		if (Constants.DEBUG_INFO || Constants.DEBUG_RESOURCES)
			System.out.println("[Resource-FINAL]");
			
	}		
}

