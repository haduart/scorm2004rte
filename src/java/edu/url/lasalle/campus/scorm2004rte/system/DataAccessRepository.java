// $Id: DataAccessRepository.java,v 1.2 2007/12/21 17:12:44 ecespedes Exp $
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


package edu.url.lasalle.campus.scorm2004rte.system;

import java.util.HashMap;
import java.util.Map;


/**
* $Id: DataAccessRepository.java,v 1.2 2007/12/21 17:12:44 ecespedes Exp $
* <b>Títol:</b> DataAccessRepository<br><br>
* <b>Descripció:</b> Aquesta Classe serà un sigleton que farà<br>
* de "repositori" de DataAccess, de manera que si volem afegir un <br>
* nou DataAccess, per exemple una nova base de dades, doncs l'únic<br>
* que ens haurem d'assegurar serà que el seu DataAccessID sigui realment<br>
* únic dintre del sistema.<br>
* Aquesta classe serà cridada principalment pel CourseAdministration<br>
* quan un usuari li demani un CourseManager i no tingui ni el CourseManager<br>
* ni el DataAccess controlats.<br><br>
*
* @author Eduard Céspedes i Borràs /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versió $Revision: 1.2 $ $Date: 2007/12/21 17:12:44 $
* $Log: DataAccessRepository.java,v $
* Revision 1.2  2007/12/21 17:12:44  ecespedes
* Implementant CourseManager.OverallSequencingProcess
*
* Revision 1.1  2007/11/07 13:15:08  ecespedes
* Creat tot el sistema que controla els DataAccess, per tal
* que el CourseAdministrator pugui subministrar-lo quan un
* UserObjective li demani.
*
*/
public final class DataAccessRepository {
	/**
	 * És la variable que instanciarà l'objecte de DataAccessRepository.
	 */
	private static DataAccessRepository dataAccessRepository = null;
	
	/**
	 * Ens dirà el número de DataAccess que hi ha actualment
	 * en memòria.
	 */	
	private int currentNumDataAccess = 0;
	
	/**
	 * Quan registrem un nou DataAccess el guardarem en aquest
	 * HashMap on la clau serà precisament l'Identificador únic
	 * del DataAccess (accessible des de DataAccees.getDataAccessID()).
	 */
	private Map < Integer, DataAccess > dataAccessMap = null;
	
	/**
	 * El constructor per defecte serà privat ja que és tracta 
	 * d'un singleton, per tant tindrem una única instància 
	 * d'aquesta classe en memòria. 
	 *
	 */
	private DataAccessRepository() {
		dataAccessMap = new HashMap < Integer, DataAccess > ();
		
	}	
	/**
	 * Es recupera la instància de l'objecte DataAccessRepository 
	 * ja sigui creant ara la instància o recuperant la ja feta anteriorment.
	 * 
	 * @return Instància de l'objecte DataAccessRepository.
	 */
	public static synchronized DataAccessRepository getInstance() {
		
		if (dataAccessRepository == null) {
			dataAccessRepository = new DataAccessRepository();
		}
		return dataAccessRepository;
		
	}	
	
	/**
	 * Registrarem un nou DataAccess perquè sigui accessible des de tot
	 * el sistema i els usuaris que hi pertanyen puguin accedir als seus
	 * cursos.
	 * Si hi ha un error, com per exemple que ja tinguem un DataAccess 
	 * registrat amb l'Identificador que estem donant, ens retornarà 
	 * false i no és realitzarà el registre.
	 * 
	 * @param dataAccessID : L'identficiador únic del DataAccess.
	 * @param dataAccess : El dataAccess que volem registrar.
	 * @return boolean Type: False si n'ho s'ha realitzat el registre.
	 */
	public boolean registerDataAccess(
			final int dataAccessID, final DataAccess dataAccess) {
		
		/**
		 * Si ja hi ha DataAccess registrats el que farem serà vigilar
		 * que no estigui repetit l'identificador.
		 */		
		if (currentNumDataAccess > 0) {
			DataAccess tmpDataAccess = dataAccessMap.get(dataAccessID);
			if (tmpDataAccess != null) {
				if (Constants.DEBUG_ERRORS) {
					System.out.println("[ERROR] DataAccessRepository:"
							+ " Ja hi ha un DataAccess amb aquest "
							+ "identificador!");
				}
				return false;
			}
		}
		
		dataAccessMap.put(dataAccessID, dataAccess);
		currentNumDataAccess++;
		return true;
	}
	/**
	 * Ens esborrarà un DataAccess del repository, de manera que els 
	 * seus cursos ja no siguin accessibles per cap usuari.
	 * Un error podria ser si intentem esborrar un DataAccess que 
	 * no es troba registrat (no existeix res per aquest ID).
	 * 
	 * @param dataAccessID : L'identificador únic del DataAccess.
	 * @return boolean : False si hi ha hagut un error.
	 */
	public boolean removeDataAccess(final int dataAccessID) {
		/**
		 * Si ja hi ha DataAccess registrats el que farem serà vigilar
		 * que no estigui repetit l'identificador.
		 */		
		if (currentNumDataAccess > 0) {
			DataAccess tmpDataAccess = dataAccessMap.get(dataAccessID);
			if (tmpDataAccess == null) {
				if (Constants.DEBUG_ERRORS) {
					System.out.println("[ERROR] DataAccessRepository:"
							+ " No hi ha cap DataAccess amb aquest "
							+ "identificador!");
				}
				return false;
			}
			dataAccessMap.remove(dataAccessID);		
			currentNumDataAccess--;
		} else {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] DataAccessRepository:"
						+ " No hi ha cap DataAccess en el repository!");
			}
			return false;
		}
		
		return true;
	}
	/**
	 * Ens dirà quans DataAccess registrats hi ha.
	 * 
	 * @return int Type: currentNumOfParsers
	 */
	public int getCurrentNumDataAccess() {
		return currentNumDataAccess;
	}
	/**
	 * Ens retornarà el DataAccess registrat amb el dataAccessID
	 * que estem passant per paràmetre. Si no existeix o no el trobem
	 * ens retornarà un null.
	 * 
	 * @param dataAccessID : Identificador del DataAccess que volem.
	 * @return DataAccess : Si hi ha un error serà null.
	 */
	public DataAccess getDataAccess(final int dataAccessID) {
		
		if (currentNumDataAccess > 0) {
			DataAccess tmpDataAccess = dataAccessMap.get(dataAccessID);
			if (tmpDataAccess == null) {
				if (Constants.DEBUG_ERRORS) {
					System.out.println("[ERROR] DataAccessRepository:"
							+ " No hi ha cap DataAccess amb aquest "
							+ "identificador!");
				}
				return null;
			}
			return tmpDataAccess;		
			
		} else {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] DataAccessRepository:"
						+ " No hi ha cap DataAccess en el repository!");
			}
			return null;
		}
	}


}
