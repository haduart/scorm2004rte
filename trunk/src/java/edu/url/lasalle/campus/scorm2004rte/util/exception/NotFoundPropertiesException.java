// $Id: NotFoundPropertiesException.java,v 1.3 2007/12/03 14:55:18 toroleo Exp $
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
* $Id: NotFoundPropertiesException.java,v 1.3 2007/12/03 14:55:18 toroleo Exp $
* 
*  NotFoundPropertiesException.
*  Excepció que indica que no se ha trobat
*  la propietat buscada en el fitxer indicat.
*
* @author M.Reyes La Salle mtoro@salle.url.edu
* @version 1.0 05/09/2007 $Revision: 1.3 $ $Date: 2007/12/03 14:55:18 $
* $Log: NotFoundPropertiesException.java,v $
* Revision 1.3  2007/12/03 14:55:18  toroleo
* S'ha afegit el meu e-mail a la capcelera de la classe.
*
* Revision 1.2  2007/11/09 15:36:16  toroleo
* Canvi de la capcelera.
*
*
*/
public class NotFoundPropertiesException extends Exception {

	/** */
	private static final long serialVersionUID = 1L;
	/** Nom del paràmetre al que es vol accedir. */
	private String nameParameter;

	/**
 	* Constructor.
 	*
 	* @param aNameParameter nom del paràmetre que causa l'excepció.
 	*/
	public NotFoundPropertiesException(final String aNameParameter) {
		super("Falta el paràmetre de configuració: '" + aNameParameter + "'");
		this.nameParameter = aNameParameter;
	}

	/**
	 * Retorna el nom del paràmetre que ha causat l'excepció.
	 * 
	 * @return Un String amb el nom del paràmetre que ha causat l'excepció.
	 */
	public final String getNombreParametro() {
	  return nameParameter;
	}

}
