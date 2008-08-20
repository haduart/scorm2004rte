// $Id: ParseFile.java,v 1.2 2007/10/30 14:07:42 ecespedes Exp $
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
 * MA  02110-1301, USA.  
 */

package edu.url.lasalle.campus.scorm2004rte.server.validator.concreteParsers;

import java.io.File;

import org.w3c.dom.Node;

import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMTreeUtility;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Files;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;



public class ParseFile {
	private Node file;
	private String path;
	private String href;		
	private Files files = new Files();
	
	private boolean error;
	private String error_message = "";
	
	public ParseFile(Node newFile, String newPath){
		path = newPath;
		file = newFile;
		href = DOMTreeUtility.getAttributeValue(file, Constants.HREF);
		if (href.length() == 0){
			if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES)
				System.out.println("[ERROR] a l'agafar l'href d'un file!!");
		}			
		if (Constants.DEBUG_INFO || Constants.DEBUG_RESOURCES)
			System.out.println("\t[File(Node newFile)]\t"+href);
		testFile();
		files.href = href;
	}	
	
	private boolean testFile(){
		boolean error;
		String completePath = path + href;		
		File pathFile = new File(completePath);
		error = pathFile.exists();
		if (!error) {
			if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES)
				System.out.println("[ERROR] El fitxer '"+href+"' no existeix o no es " +
					"troba a la direcció '" + completePath + "'");
		}
		else{
			if (Constants.DEBUG_INFO || Constants.DEBUG_RESOURCES)
				System.out.println("\t\t[File testFile()]\t"+completePath+" [TROBAT]");
		}
		return error;
	}	
	public String getHref(){
		return href;
	}
	public Files getFile(){
		return files;
	}
	public boolean getError(){
		return error;
	}
	public String getErrorMessage(){
		return error_message;
	}
}
