// $Id: DrawHTMLTableException.java,v 1.1 2007/12/13 15:29:07 toroleo Exp $
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
 * $Id: DrawHTMLTableException.java,v 1.1 2007/12/13 15:29:07 toroleo Exp $
 * DrawHTMLTableException
 * Excepció que indica que l'intent 
 * de dibuixar una taula HTML ha anat malament.
 * 
 * @author M.Reyes La Salle mtoro@salle.url.edu
 * @version v1.0 10/12/2007 $Revision: 1.1 $ $Date: 2007/12/13 15:29:07 $
 * $Log: DrawHTMLTableException.java,v $
 * Revision 1.1  2007/12/13 15:29:07  toroleo
 * Implementa l'excepció per quan hi han
 * problemes al dibuixar una taula HTML.
 *
 * 	
 */
public class DrawHTMLTableException extends Exception {
	
	/**	 */
	private static final long serialVersionUID = 1L;
	/** Nom de la petició que ha provocat l'exepció. */
	private String nameRequest;
	/** Missatge d'error a propagar. */
	private String messageError;

	/**
	 * Constructor.
	 * 
	 * @param aMessageError explicació de l'error que s'ha produit
	 */
	public DrawHTMLTableException(final String aMessageError) {
	  super("Al intentar crear la taula HTML s'ha produït el següent error: '" 
			  +  aMessageError + "'");
	  this.messageError = aMessageError;
	}

	/**
	 * Obtenir el missatge d'error que s'ha produit.
	 * 
	 * @return missatge d'error.
	 */
	public final String getMessageError() {
		return messageError;
	}

	/**
	 * Obtenir el nom de la petició que ha provocat l'error.
	 * 
	 * @return missatge d'error que ha provocat l'error.
	 */
	public final String getNameRequest() {
		return nameRequest;
	}

	

}
