// $Id: SaveDataInterface.java,v 1.2 2008/01/25 15:05:27 ecespedes Exp $
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
package edu.url.lasalle.campus.scorm2004rte.system;

import java.util.HashMap;

/**
* $Id: SaveDataInterface.java,v 1.2 2008/01/25 15:05:27 ecespedes Exp $
* <b>Títol:</b> SaveDataInterface<br><br>
* <b>Descripció:</b> Aquesta Intefície servirà per separar <br>
* l'implementació de l'estructura, per tal que després poguem <br>
* tindre diverses bases de dades que simplement implementant <br>
* aquesta inteficie i la de DataAccess n'hi hagi prou per què<br>
* el sistemi funcioni.<br><br> 
*
* @author Eduard Céspedes i Borràs/Enginyeria La Salle/ecespedes@salle.url.edu
* @version Versió $Revision: 1.2 $ $Date: 2008/01/25 15:05:27 $
* $Log: SaveDataInterface.java,v $
* Revision 1.2  2008/01/25 15:05:27  ecespedes
* Implementant les interficies per les BD.
*
* Revision 1.1  2008/01/24 15:41:11  ecespedes
* Dissenyant un nou sistema per gestionar les Bases de dades (DataAccess)
*
*/
public interface SaveDataInterface {
	
	/**
	 * Aquesta funció ens servirà per indicar-li al gestor de BD
	 * que començarem a fer una escriptura. I com que serà un procés
	 * transaccional potser aniria bé atutar totes les escriptures fins
	 * que no s'acabi l'actual.
	 * 
	 * @return boolean Type: True si està activat i false si ja està en ús.
	 */
	boolean starting();
	/**
	 * Aquesta funció ens servirà per indicar-li al gestor de BD
	 * que hem acabat de fer l'escriptura. 
	 * 
	 * @return boolean Type: True si s'ha desactivat, false si error.
	 */
	boolean ending();
	
	/**
	 * MySQL Example:
	 * CALL insertScorms( _scorm_id_text TEXT(10), 
	 * 		_activate_sequencing BIT(1), _new_screen BIT(1)).
	 *  
	 * @param scormIdIext : String Type
	 * @param activateSequencing : Boolean Type
	 * @param newScreen : Boolean Type
	 * @return int : Identificador de l'element qeu acabem d'entrar. 
	 */
	int insertScorms(String scormIdIext, Boolean activateSequencing,
			Boolean newScreen);
	
	/**
	 * MySQL Example:
	 * CALL insertMetadata(_scorm_id INTEGER, _version VARCHAR(10), 
	 * 		_title VARCHAR(30), _description TEXT, _requeriments TEXT,
	 * 		_end_user VARCHAR(30), _educative_environment VARCHAR(30), 
	 * 		_author TEXT, _learning_duration VARCHAR(10), 
	 * 		_beginning_date DATETIME, _ending_date DATETIME).
	 * 
	 * @param scormId : Identificador del curs scorm.
	 * @param version : String Type
	 * @param title : Títol. String Type.
	 * @param description : String Type.
	 * @param requeriments : String Type.
	 * @param endUser : String Type.
	 * @param educativeEnvironment : String Type
	 * @param author : String Type. Si n'hi ha més d'un estaran
	 * 		concatenats.
	 * @param learningDuration : String Type.
	 * @param beginningDATETIME : String Type.
	 * @param endingDATETIME : String Type.
	 * @return int Type: Identificador del metadata.
	 */
	int insertMetadata(int scormId, String version, 
			String title, String description,
			String requeriments, String endUser,
			String educativeEnvironment, String author,
			String learningDuration, String beginningDATETIME,
			String endingDATETIME);
	
	/**
	 * MySQL Example:
	 * CALL insertOrganizations(_scorm_id integer, 
	 * 		_structure VARCHAR(200),_is_visible BIT(1), 
	 * 		_item_id_text VARCHAR(20), _title VARCHAR(200), 
	 * 		_studentTable VARCHAR(50)).
	 * 
	 * @param scormId : Identificador del curs Scorm.
	 * @param structure : String Type.
	 * @param isVisible : Boolean Type.
	 * @param itemIdText : String Type.
	 * @param title : String Type.
	 * @param studentTable : String Type.
	 * @return HashMap < String, Integer > : La clau serà "Organization"
	 * 		i "Item". En el cas de MySQL la taula Organització serà 
	 * 		diferent de la taula Items, tot i que l'Organització la 
	 * 		tractarem com si fós un Item més.  
	 */
	HashMap < String, Integer > insertOrganizations(int scormId,
			String structure, Boolean isVisible,
			String itemIdText, String title,
			String studentTable);
	
	/**
	 * MySQL Example: 
	 * CALL insertStudentsClass( _organization_id integer, 
	 * 		_global_student_id integer).
	 * 
	 * @param organizationId : identificador de l'organització.
	 * @param globalStudentId : identificador global de l'estudiant 
	 * (serà el que rebrem per OSIDs).
	 * @return int : identificador de l'entrada actual. 
	 */
	int insertStudentsClass(int organizationId, String globalStudentId);
	
	/**
	 * Java Call Example:
	 * int writeJavaObject("coursestudent_1", studentID, userObjective). 
	 * 
	 * @param tableName : String Type. El nom de la taula on és 
	 * 	guarda.
	 * @param studentID : int Type.
	 * @param userObjective : Object Type. Serà l'estructura 
	 * 	UserObjective serialitzada.
	 * @return int : identificador de l'entrada a la taula.
	 */
	int insertUserObjective(String tableName, 
			int studentID, Object userObjective);
	
	/**
	 * MySQL Example:
	 * CALL insertItems( _item_id_text VARCHAR(20), _leaf BIT(1),
	 * 		_title VARCHAR(200), _father_item_id integer, 
	 * 		_organization_id integer, _is_visible BIT(1)).
	 * 
	 * @param itemIdText : String Type.
	 * @param leaf : boolean Type.
	 * @param title : String Type.
	 * @param fatherItemId : int Type.
	 * @param organizationId : int Type.
	 * @param isVisible : boolean Type.
	 * @return int : Identificador de l'item actual. 
	 */
	int insertItems(String itemIdText, boolean leaf,
			String title, int fatherItemId,
			int organizationId, boolean isVisible);
	
	/**
	 * MySQL Example:
	 * CALL insertPrimaryObjectives( _adlseqSequencing_id integer,
	 * 		_objective_name VARCHAR(20), _item_id integer, 
	 * 		_satisfiedByMeasure BIT(1), 
	 * 		_minNormalizedMeasure DECIMAL(3,2)).
	 * 
	 * @param adlseqSequencingId : int Type. Identificador del
	 * 	seqüenciament al que pertany l'objective Primary.
	 * @param objectiveName : String Type. Nom de l'objectiu.
	 * @param itemId : int Type. Identificador de l'item.
	 * @param satisfiedByMeasure : boolean Type.
	 * @param minNormalizedMeasure : double Type. 
	 * @return int : Identificador de l'Objective.
	 */
	int insertPrimaryObjectives(int adlseqSequencingId,
			String objectiveName, int itemId,
			boolean satisfiedByMeasure,
			double minNormalizedMeasure);
	
	/**
	 * MySQL Example:
	 * CALL insertObjectives(_objective_name VARCHAR(20), 
	 * 		_item_id integer, 
	 * 		_satisfiedByMeasure BIT(1), 
	 * 		_minNormalizedMeasure DECIMAL(3,2)).
	 *  
	 * Amb aquesta funció entrarem els objectives secundaris.
	 *  
	 * @param objectiveName : String Type. Nom de l'objectiu.
	 * @param itemId : int Type. Identificador de l'item.
	 * @param satisfiedByMeasure : boolean Type.
	 * @param minNormalizedMeasure : double Type. 
	 * @return int : Identificador de l'Objective.
	 */
	int insertObjectives(
			String objectiveName, int itemId,
			boolean satisfiedByMeasure,
			double minNormalizedMeasure);
	
	/**
	 * MySQL Example:
	 * CALL insertOptional_data_items(
	 * 		_parameters VARCHAR(500), _dataFromLMS VARCHAR(500),
	 * 		 _completionThreshold DECIMAL(5,3), 
	 * 		_time_limit_action enum('EXITMESSAGE',
	 * 			'EXITNOMESSAGE','CONTINUEMESSAGE',
	 * 			'CONTINUENOMESSAGE'), 
	 * 		_max_time_allowed integer, 
	 * 		_RandomizationTiming enum('never','once', 
	 * 				'onEachNewAttempt'), 
	 * 		_selectCount int UNSIGNED). 
	 * 
	 * @param parameters : String Type.
	 * @param dataFromLMS : String Type.
	 * @param completionThreshold : double Type.
	 * @param timeLimitAction : String Type.
	 * @param maxTimeAllowed : int Type.
	 * @param randomizationTiming : String Type.
	 * @param selectCount : int Type.
	 * @return int : Identificador de la taula.
	 */
	int insertOptionalDataItems(String parameters, String dataFromLMS,
			double completionThreshold, String timeLimitAction,
			int maxTimeAllowed, String randomizationTiming, 
			int selectCount);
	/**
	 * MySQL Exemple: 
	 * CALL insertAdlnavPresentation(_item_id integer,
	 * 		_hidePrevious BIT(1), _hideContinue BIT(1), 
	 * 		_hideExit BIT(1), _hideAbandon BIT(1)). 
	 * 
	 * @param itemId : int Type. Identificador de l'item.
	 * @param hidePrevious : Boolean Type.
	 * @param hideContinue : Boolean Type.
	 * @param hideExit : Boolean Type.
	 * @param hideAbandon : Boolean Type.
	 * @return int : Identificador dintre de la taula. 
	 */
	int insertAdlnavPresentation(int itemId,
			boolean hidePrevious, boolean hideContinue,
			boolean hideExit, boolean hideAbandon);
	/**
	 * MySQL Example:
	 * CALL insertAdlseqControlMode(_item_id integer, 
	 * 		_choice BIT(1), _choiceExit BIT(1), 
	 * 		_flow BIT(1), _forwardOnly BIT(1), 
	 * 		_useCurrentAttemptObjectiveInfo BIT(1), 
	 * 		_useCurrentAttemptProgressInfo BIT(1)).
	 * 
	 * @param itemId : int Type. Identificador de l'item.
	 * @param choice : boolean Type.
	 * @param choiceExit : boolean Type.
	 * @param flow : boolean Type.
	 * @param forwardOnly : boolean Type.
	 * @param useCurrentAttemptObjectiveInfo : boolean Type.
	 * @param useCurrentAttemptProgressInfo : boolean Type.
	 * @return int : Identificador dintre de la taula
	 */
	int insertAdlseqControlMode(int itemId,
			boolean choice, boolean choiceExit,
			boolean flow, boolean forwardOnly,
			boolean useCurrentAttemptObjectiveInfo,
			boolean useCurrentAttemptProgressInfo);
	
}
