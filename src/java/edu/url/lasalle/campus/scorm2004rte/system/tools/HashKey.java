// $Id: HashKey.java,v 1.1 2007/11/07 13:15:08 ecespedes Exp $
/*
 * Copyright (c) 2007 Enginyeria La Salle. Universitat Ramon Llull.
 * This file is part of SCORM2004RTE.
 *
 * SCORM2004RTE is free software; you can redistribute it and/or modify
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
 * MA 02110-1301, USA.
 */

package edu.url.lasalle.campus.scorm2004rte.system.tools;

/**
* $Id: HashKey.java,v 1.1 2007/11/07 13:15:08 ecespedes Exp $
* <b>Títol:</b> HashKey<br><br>
* <b>Descripció:</b> Serà la classe que generarà les claus per utilitzar-les<br>
* amb el HashMap del CourseAdministrator.
* <br><br> 
*
* @author Eduard Céspedes i Borràs /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versió $Revision: 1.1 $ $Date: 2007/11/07 13:15:08 $
* $Log: HashKey.java,v $
* Revision 1.1  2007/11/07 13:15:08  ecespedes
* Creat tot el sistema que controla els DataAccess, per tal
* que el CourseAdministrator pugui subministrar-lo quan un
* UserObjective li demani.
*
*/
public class HashKey {
	/**
	 * Constant int Type.
	 * 		29
	 */
	private static final int TWENTYNINE = 29;
	/**
	 * Constant int Type.
	 * 		32
	 */
	private static final int THIRTYTWO = 32;
	
	/**
	 * L'identificador local del DataAccess sobre 
	 * el que calcularem el hash.
	 */
	private final int dAccessID;
	/**
	 * L'identificador local de l'organització sobre 
	 * el que calcularem el hash.
	 */
    private final long orgID;
    /**
     * L'enter que farà de clau hash pel HashMap.
     */
    private final int hash;

    /**
     * Ens retornarà la clau única segons l'identificador del DataAccess
     * i l'identificador de l'organització dintre del mateix DataAccess.
     * Hem de pensar que podriem tindre el mateix identificador d'organització
     * repetit segons el lloc on l'anem a buscar (el DataAccess).
     * 
     * @param dataAccessID : L'identificador del tipus de DataAccess.
     * @param organizationID : L'identificador de l'organització organizationID
     */
    public HashKey(final int dataAccessID, final long organizationID) {
    	dAccessID = dataAccessID;
    	orgID = organizationID;
        hash = hash();
    }

    /**
     * Methods are overriden properly according to the Java spec.
     * Spec says "If two objects are equal according to the equals(Object)
     * method, then calling the hashCode method on each of the two objects
     * must produce the same integer result."
     * 
     * @see http://www.jroller.com/talipozturk/entry/hashmap_and_hashing
     * 
     * @param o : Object Type; l'Objecte a comparar.
     * @return boolean Type: Ens indicarà si són iguals o no.
     */
    public final boolean equals(final Object o) {
        if (this == o) {
        	return true;
        }
        if (!(o instanceof HashKey)) {
        	return false;
        }

        final HashKey goodKey = (HashKey) o;

        if (dAccessID != goodKey.dAccessID) {
        	return false;
        }
        if (orgID != goodKey.orgID) {
        	return false;
        }

        return true;
    }

    /**
     * Calculem el hash fent moviment de bits per optimitzar
     * el càlcul.
     * 
     * @return int Value
     */
    public final  int hash() {
        int result;
        result = dAccessID;
        result = TWENTYNINE * result + (int) (orgID ^ (orgID >>> THIRTYTWO));
        return result;
    }
    /**
     * Methods are overriden properly according to the Java spec.
     * Spec says "If two objects are equal according to the equals(Object)
     * method, then calling the hashCode method on each of the two objects
     * must produce the same integer result."
     * 
     * @see http://www.jroller.com/talipozturk/entry/hashmap_and_hashing
     *  
     * @return int Type: Ens retornarà la "clau" en si.
     */
    public final int hashCode() {
        return hash;
    }

}
