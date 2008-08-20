// $Id: RequestException.java,v 1.2 2007/12/03 14:55:18 toroleo Exp $
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
 * $Id: RequestException.java,v 1.2 2007/12/03 14:55:18 toroleo Exp $
 * RequestException
 * Excepció que indica que la petició 
 * feta ha tingut algun tipus de problema 
 * al ser procesada.
 * 
 * @author M.Reyes La Salle mtoro@salle.url.edu
 * @version v1.0 25/09/2007 $Revision: 1.2 $ $Date: 2007/12/03 14:55:18 $
 * $Log: RequestException.java,v $
 * Revision 1.2  2007/12/03 14:55:18  toroleo
 * S'ha afegit el meu e-mail a la capcelera de la classe.
 *
 * Revision 1.1  2007/11/22 09:46:50  toroleo
 * Implementació capa OSID consumidora
 *
 *
 */
public class RequestException extends Exception {
	
	/**	 */
	private static final long serialVersionUID = 1L;
	/** Nom de la petició que ha provocat l'exepció. */
	private String nameRequest;
	/** Missatge d'error a propagar. */
	private String messageError;

	/**
	 * Constructor.
	 * 
	 * @param aNameRequest nom del paràmetre que causa l'excepció.
	 * @param aMessageError explicació de l'error que s'ha produit
	 */
	public RequestException(final String aNameRequest, 
				final String aMessageError) {
	  super("La petició: '" + aNameRequest + "' Ha provocat l'error: '" 
			  +  aMessageError + "'");
	  this.nameRequest = aNameRequest;
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
