// $Id: SaveAllDB.java,v 1.2 2008/01/25 15:05:27 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.DataBase;

import edu.url.lasalle.campus.scorm2004rte.system.SaveDataInterface;

/**
* $Id: SaveAllDB.java,v 1.2 2008/01/25 15:05:27 ecespedes Exp $
* <b>Títol:</b> SaveAllDB<br><br>
* <b>Descripció:</b> Aquesta classe guardarà tot l'arbre d'activitats<br>
* per tant, en principi, estarà pensat perquè només es guardi un cop<br>
* que serà el primer.<br><br> 
*
* @author Eduard Céspedes i Borràs/Enginyeria La Salle/ecespedes@salle.url.edu
* @version Versió $Revision: 1.2 $ $Date: 2008/01/25 15:05:27 $
* $Log: SaveAllDB.java,v $
* Revision 1.2  2008/01/25 15:05:27  ecespedes
* Implementant les interficies per les BD.
*
* Revision 1.1  2008/01/24 15:41:11  ecespedes
* Dissenyant un nou sistema per gestionar les Bases de dades (DataAccess)
*
*/
public class SaveAllDB extends DBConnector implements SaveDataInterface {
	
	
	
	/**
	 * CALL insertScorms( _scorm_id_text TEXT(10), 
	 * 		_activate_sequencing BIT(1), _new_screen BIT(1));
	 * 
	 * Retorna: 	select LAST_INSERT_ID();
	 * 
	 */
	
	
	/**
	 * CALL insertMetadata(_scorm_id INTEGER, _version VARCHAR(10), 
	 * 		_title VARCHAR(30), _description TEXT, _requeriments TEXT,
	 * 		_end_user VARCHAR(30), _educative_environment VARCHAR(30), 
	 * 		_author TEXT, _learning_duration VARCHAR(10), 
	 * 		_beginning_date DATETIME, _ending_date DATETIME);
	 * 
	 * Retorna: 	select LAST_INSERT_ID();
	 */

}
