// $Id: SequencingCollection.java,v 1.3 2008/01/18 18:07:24 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.ActivityTree;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.url.lasalle.campus.scorm2004rte.server.
	ActivityTree.Elements.Sequencing;

/**
* $Id: SequencingCollection.java,v 1.3 2008/01/18 18:07:24 ecespedes Exp $
* <b>Títol:</b> SequencingCollection<br><br>
* <b>Descripció:</b> Aquesta classe contindrà tots els seqüenciaments <br>
* "globals", que en un moment donat han de ser accessibles des de qualsevol<br>
* lloc dintre de l'estructura de l'arbre. <br><br> 
*
* @author Eduard Céspedes i Borràs /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versió $Revision: 1.3 $ $Date: 2008/01/18 18:07:24 $
* $Log: SequencingCollection.java,v $
* Revision 1.3  2008/01/18 18:07:24  ecespedes
* Serialitzades TOTES les classes per tal de que els altres puguin fer proves
* en paral·lel amb el procés de desenvolupament del gestor de BD.
*
* Revision 1.2  2007/11/13 15:59:59  ecespedes
* Treballant sobre el sistema per "linkar" els Objectives de l'estructura
* Sequencing de l'arbre amb els Objectives de l'usuari.
* Millorat TreeAnnotations (step 2 de 3)
*
*/
public class SequencingCollection implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8849325785309495019L;
	/**
	 * Estructura on guardarem els seqüenciaments segons
	 * l'identificador dels mateixos. 
	 */
	private Map < String, Sequencing > sequencings = 
		new HashMap < String, Sequencing > ();
	
	/**
	 * El constructor per defecte.
	 *
	 */
	public SequencingCollection() {
		
	}
	/**
	 * Constructor on li passem un seqüenciament inicial.
	 * 
	 * @param nouSequencing : Sequencing Type.
	 */
	public SequencingCollection(final Sequencing nouSequencing) {
		sequencings.put(nouSequencing.ID, nouSequencing);		
	}
	
	/** 
	 * @param id : Nom identificador del sequencing. 
	 * @return Sequencing: Si troba un seqüenciament que encaixi 
	 * amb el nom ens el retorna.
	 */
	public final Sequencing searchSequencing(final String id) {
		return sequencings.get(id);
		/*
		for (Iterator sequencingIterator = sequencings.iterator(); 
					sequencingIterator.hasNext();){
			Sequencing sequencingByName = (Sequencing)sequencingIterator.next();
			if(sequencingByName.ID.length() > 0)
				if(sequencingByName.ID.equals(id))
					return sequencingByName;
		}
		return null;
		*/
	}
	/**	 
	 * @param sequencing : Passem el seqüenciament que volem afegir 
	 * a la "col·lecció".
	 */
	public final void addSequencing(final Sequencing sequencing) {
		sequencings.put(sequencing.ID, sequencing);	
	}
	
}

