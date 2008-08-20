//$Id: ParserIdentifier.java,v 1.1 2007/11/07 13:15:08 ecespedes Exp $
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
* $Id: ParserIdentifier.java,v 1.1 2007/11/07 13:15:08 ecespedes Exp $
* <b>T�tol:</b> ParserIdentifier<br><br>
* <b>Descripci�:</b> Aquesta classe �s un Singleton que ens donar� <br>
* identificadors �nics per cada Parser que hi hagi assignat en <br>
* mem�ria. <br><br>
* 
* @author Eduard C�spedes i Borr�s /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versi� $Revision: 1.1 $ $Date: 2007/11/07 13:15:08 $
* $Log: ParserIdentifier.java,v $
* Revision 1.1  2007/11/07 13:15:08  ecespedes
* Creat tot el sistema que controla els DataAccess, per tal
* que el CourseAdministrator pugui subministrar-lo quan un
* UserObjective li demani.
*
*/
public final class ParserIdentifier {
	/**
	 * �s la variable que instanciar� l'objecte de ParserIdentifier.
	 */
	private static ParserIdentifier parserIdentifier = null;
	/**
	 * Ens donar� el n�mero actual de Identificador.
	 */
	private int currentParserID = -1;
	/**
	 * Ens dir� el n�mero de Parsers que hi ha actualment
	 * en mem�ria.
	 */	
	private int currentNumOfParsers = 0;
	
	/**
	 * El constructor per defecte ser� privat ja que �s tracta 
	 * d'un singleton, per tant tindrem una �nica inst�ncia 
	 * d'aquesta classe en mem�ria. 
	 *
	 */
	private ParserIdentifier() {		
		
	}	
	/**
	 * Es recupera la inst�ncia de l'objecte ParserIdentifier 
	 * ja sigui creant ara la inst�ncia o recuperant la ja feta anteriorment.
	 * 
	 * @return Inst�ncia de l'objecte ParserIdentifier.
	 */
	public static synchronized ParserIdentifier getInstance() {
		
		if (parserIdentifier == null) {
			parserIdentifier = new ParserIdentifier();
		}
		return parserIdentifier;
		
	}
	/**
	 * Ens retorna un nou identificador �nic.
	 * 
	 * @return long Type: dataAccessID
	 */
	public int getNewParserID() {
		if ((currentNumOfParsers == 0) && (currentParserID > 0)) {
			currentParserID = -1;
		}		
		currentParserID++;
		currentNumOfParsers++;
		
		return currentParserID;
		
	}
	/**
	 * El Parser li notifica al ParserIdentifier que ja s'ha eliminat,
	 * d'aquesta manera podem "reciclar" identificadors.  
	 *
	 */
	public void unregisterParser() {
		currentNumOfParsers--;
		if ((currentNumOfParsers == 0) && (currentParserID > 0)) {
			currentParserID = -1;
		}
	}
	/**
	 * Ens dir� quans Parsers registrats hi ha, que si tot va b�
	 * ser� el mateix n�mero de Parsers que tindrem en mem�ria.
	 * 
	 * @return int Type: currentNumOfParsers
	 */
	public int getCurrentNumOfParsers() {
		return currentNumOfParsers;
	}

}
