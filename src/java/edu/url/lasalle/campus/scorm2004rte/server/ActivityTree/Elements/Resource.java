// $Id: Resource.java,v 1.3 2008/01/18 18:07:24 ecespedes Exp $
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
import java.util.Iterator;

import edu.url.lasalle.campus.scorm2004rte.system.Constants;

/**
* $Id: Resource.java,v 1.3 2008/01/18 18:07:24 ecespedes Exp $
* <b>Títol:</b> Resource<br><br>
* <b>Descripció:</b> Contindrà un "resource", o sigui, <br>
* el conjunt de fitxers que s'han d'enviar a l'usuari.<br><br> 
* Example: <br>
* <resource identifier="RESOURCE_QUESTION1"  
* 	adlcp:scormType="sco" type="webcontent" href="Question1.htm" />
* <br><br>
*
* @author Eduard Céspedes i Borràs /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versió $Revision: 1.3 $ $Date: 2008/01/18 18:07:24 $
* $Log: Resource.java,v $
* Revision 1.3  2008/01/18 18:07:24  ecespedes
* Serialitzades TOTES les classes per tal de que els altres puguin fer proves
* en paral·lel amb el procés de desenvolupament del gestor de BD.
*
* Revision 1.2  2007/11/08 16:33:09  ecespedes
* Començant a Implementar el OverallSequencingProcess, que serà
* les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
* realment el seqüenciament SCORM1.3
*
*/
public class Resource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 769685592953034247L;


	/**
	 * Serà on guardarem el conjunt de fitxers.
	 * ArrayList Type.
	 */
	private Collection < Files > files = new ArrayList < Files > ();
	
	
	/**
	 * L'identificador del Resource, molt útil quan haguem de trobar-ne
	 * un de concret.
	 * String Type.
	 * Required.
	 */
	private String identifier = null;
	/**
	 * Aqui es ons indicarà si és un SCO o no.
	 * Boolean Type: False == "asset" , True == "sco"
	 * Required.
	 */
	private boolean isSCO = false;
	/**
	 * El default Href, serà el que enviarem a l'usuari en un primer moment.
	 * File Type.
	 * Required.
	 */
	private Files defaultHref = null;
	/**
	 * Per saber si s'ha carregat en memòria o no.
	 * Utilitzarem l'optimitzador per aquesta tasca.
	 * boolean Type. 
	 * Default false.
	 */
	private boolean isLoaded = false;
	/**
	 * Ens busca un fitxer segons la referència que li passem.
	 *  
	 * @param hrefID : String Type.
	 * @return boolean Type
	 */
	private boolean searchFile(final String hrefID) {
		for (Iterator filesIterator = files.iterator(); 
				filesIterator.hasNext();) {
			Files filesByName = (Files) filesIterator.next();
			if (filesByName.href.length() > 0) {
				if (filesByName.href.equals(hrefID)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Creem un nou Fitxer només passatn-li la direcció.
	 * 
	 * @param nouHref : String Type.
	 */
	public final void setHref(final String nouHref) {
		Files nouFile = new Files();
		nouFile.href = nouHref;
		addFiles(nouFile);
	}
	/**
	 * Ens anirà afegints fitxers dintre d'un Resource Concret.
	 * En principi només l'hauria d'utilitzar un DataAccess.
	 * 
	 * @param newFile : Files Type.
	 */
	public final void addFiles(final Files newFile) {
		if (files.isEmpty()) {
			defaultHref = newFile;
		}
		if (!searchFile(newFile.href)) {
			files.add(newFile);
		} else {
			if ((Constants.DEBUG_WARNINGS_LOW || Constants.DEBUG_RESOURCES) 
					&& !defaultHref.href.equals(newFile.href)) {
				
				System.out.println("[WARNING] El fitxer "
						+ newFile.href
						+ " està repetit!");
			}
		}
	}
	/**
	 * Està tot carregat? Ens retorna el booleà que ens ho indica.
	 * 
	 * @return boolean Type: True == loaded, False = not Loaded. 
	 */
	public final boolean getIsLoaded() {
		return isLoaded;
	}
	/**
	 * <b>INCOMPLETE!!!</b>
	 * TODO A part de ficar isLoaded = true hauríem de cridar al Gestor de BD
	 * perquè ens donguès els files d'aquest resource.
	 *
	 */
	public final void setLoaded() {
		isLoaded = true;
	}
	/**
	 * <b>INCOMPLETE!!!</b>
	 * TODO A part de ficar isLoaded = false hauríem d'eliminar el Collection
	 * per alliberar memòria.
	 *
	 */
	public final void setUnloaded() {
		isLoaded = false;
	}
	/**
	 * Ens retorna l'identificador del Resource.
	 * 
	 * @return String Type.
	 */
	public final String getIdentifier() {
		return identifier;
	}
	/**
	 * Assigna un nou identificador al Resource. Això teòricament
	 * només ho hauria de poder fer un DataAccess.
	 * 
	 * @param newIdentifier : String Type.
	 */
	public final void setIdentifier(final String newIdentifier) {
		identifier = newIdentifier;
	}
	/**
	 * Ens retorna el tipus del Scorm. 
	 * @return boolean Type: False == "asset" , True == "sco"
	 */
	public final boolean getIsSCO() {
		return isSCO;
	}
	/**
	 * Assigna un valor al isSCO, per saber si és o no és 
	 * un SCO.
	 *  
	 * @param scormType : String Type{ "asset" , "sco" }
	 */
	public final void setScormType(final String scormType) {		
		if (scormType.toLowerCase().equals("asset")) {
			isSCO = false;
		} else {
			isSCO = true;
		}
	}
	/**
	 * Ens retorna el Fitxer per defecte. 
	 * 
	 * @return File Type. Not Null
	 */
	public final Files getDefaultFile() {
		return defaultHref;
	}
	/**
	 * Ens retorna un Iterador perquè qui vulgui 
	 * pugui buscar per ell mateix en el Collection.
	 * 
	 * @return Iterator < Files >
	 */
	public final Iterator < Files > getFileIterator() {
		return files.iterator();
	}

}

