// $Id: NavTreeException.java,v 1.1 2008/01/31 10:12:56 msegarra Exp $
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
package edu.url.lasalle.campus.scorm2004rte.util.exception;

/**
* $Id: NavTreeException.java,v 1.1 2008/01/31 10:12:56 msegarra Exp $
* La classe té com a propòsit definir una excepció per a quan s'hagi 
* d'indicar que hi ha hagut un problema al mostrar l'arbre de navegació 
* del RTE. 
* @author Marc Segarra / Enginyeria La Salle / msegarra@salle.url.edu
* @version Versió $
* $Log: NavTreeException.java,v $
* Revision 1.1  2008/01/31 10:12:56  msegarra
* *** empty log message ***
*
* Implementa una excepció per a quan s'hagi d'indicar que hi ha hagut un 
* problema al mostrar l'arbre de navegació.
*
*/
public class NavTreeException extends Exception {
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor per defecte.
     *
     */
    public NavTreeException() {
        super();
    }
    
    /**
     * Constructor amb paràmetres.
     * @param messageError String amb la descrició de l'error.
     */
    public NavTreeException(final String messageError) {
        super(messageError);
    }
}
