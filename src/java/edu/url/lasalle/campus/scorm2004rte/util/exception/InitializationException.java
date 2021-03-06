// $Id: InitializationException.java,v 1.2 2008/01/31 10:12:56 msegarra Exp $
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
* $Id: InitializationException.java,v 1.2 2008/01/31 10:12:56 msegarra Exp $
* La classe t� com a prop�sit definir una excepci� per a quan s'hagi 
* d'indicar que hi ha hagut un problema a l'inicialitzar els JSPs. 
* @author Marc Segarra / Enginyeria La Salle / msegarra@salle.url.edu
* @version Versi� $Revision: 1.2 $ $Date: 2008/01/31 10:12:56 $
* $Log: InitializationException.java,v $
* Revision 1.2  2008/01/31 10:12:56  msegarra
* *** empty log message ***
*
* Revision 1.1  2007/12/10 14:21:40  msegarra
* Implementa una excepci� per a quan s'hagi d'indicar que hi ha hagut un problema a l'inicialitzar els JSPs.
*
*/
public class InitializationException extends Exception{
    /*
     * The serial version UID
     */
    private static final long serialVersionUID = 1L;
    
    public InitializationException() {
        super();
    }
    /**
     * Constructor amb par�metres.
     * 
     * @param aNameRequest nom del par�metre que causa l'excepci�.
     * @param aMessageError explicaci� de l'error que s'ha produit
     */
    public InitializationException(final String messageError) {
        super(messageError);
    }
}
