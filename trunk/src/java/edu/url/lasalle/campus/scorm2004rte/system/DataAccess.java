// $Id: DataAccess.java,v 1.8 2008/03/31 13:56:19 ecespedes Exp $
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

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Abstract_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Root_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
	MetadataInformation;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.
	UserObjective;

/**
* $Id: DataAccess.java,v 1.8 2008/03/31 13:56:19 ecespedes Exp $
* <b>Títol:</b> DataAccess <br><br>
* <b>Descripció:</b> És la interficie que ens ajudarà a separar tot el <br>
* sistema de recollida de les dades (si utilitzem parser o base de dades) <br>
* del que realment interessa que és demanar els valors que toca. 
*
* @author Eduard Céspedes i Borràs /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versió $Revision: 1.8 $ $Date: 2008/03/31 13:56:19 $
* $Log: DataAccess.java,v $
* Revision 1.8  2008/03/31 13:56:19  ecespedes
* Depurant objectives secundaris
*
* Revision 1.7  2008/02/28 16:15:23  ecespedes
* Millorant el sistema (8)
*
* Revision 1.6  2008/01/31 20:18:58  ecespedes
* Millorant el sistema (3)
*
* Revision 1.5  2008/01/30 17:41:15  ecespedes
* Versió final: organització  serialitzada.
*
* Revision 1.4  2007/11/23 15:04:58  ecespedes
* El GestorBD ja implementa l'interfície DataAccess.
*
* Revision 1.3  2007/11/07 13:15:08  ecespedes
* Creat tot el sistema que controla els DataAccess, per tal
* que el CourseAdministrator pugui subministrar-lo quan un
* UserObjective li demani.
*
* Revision 1.2  2007/11/05 19:42:39  ecespedes
* Començant a implementar el motor del seqüenciament.
* S'aniran fent testos per tal de veure diversos 'request'
* per tal d'emular el que seria un usuari que interactua.
*
* Revision 1.1  2007/10/31 12:57:01  ecespedes
* Començant a crear tot el sistema de gestió dels arbres:
* + Creant l'interfífice DataAccess que ens servirà tan
* 	pel parser com pel SGBD.
* - Falta que el ParserHandler/GestorBD implementi l'interfície.
*
*/
public interface DataAccess {
	/**
	 * Ens retornarà algun paràmetre descriptiu sobre el mètode d'accés 
	 * a les dades. 
	 * 
	 * @return String Type, optional.
	 */
	String getDescription();
	/**
	 * Podem tindre tants DataAccess com vulguem i podran conviure tots
	 * en el mateix sistema sense problemes, però cadascún haurà de tindre
	 * un identificador propi i únic que el diferencii dels altres DataAccess.
	 * Exemples:
	 * 		+ ParserHandler -> 0
	 * 		+ GestorBD 		-> 1
	 * 
	 * @return int Type: L'identificador del DataAccess.
	 */
	int getDataAccessID();
	/**
	 * Retornem l'organització des del principi, que no 
	 * vol dir que enviem TOTA l'organització. Si estem 
	 * parsejant el més segur és que directament retornem tota
	 * l'estructura però si estem accedint a una BD el més segur 
	 * es que no, i molt menys si fós un altre mètode. 
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @return Root_Item : Retorna el primer node que és el de l'organització.
	 */
	Root_Item 	getFirstOrganization(long identifierID);
	/**
	 * Retornem l'organització des del principi, que no 
	 * vol dir que enviem TOTA l'organització. De fet amb 
	 * el paràmetre numLevels obliguem a que ens envii un 
	 * MÍNIM de nivells carregats.  
	 *
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @param numLevels : nombre de nivells que volem forçar a carregar.
	 * @return Root_Item : Retorna el primer node que és el de l'organització.
	 */
	Root_Item 	getFirstOrganization(long identifierID, int numLevels);
	/**
	 * Retornem l'organització des del principi amb TOTS
	 * els items carregats en memòria. 
	 *
	 * @param identifierID : identificador del curs dintre del dataAccess. 
	 * @return Root_Item : Retorna el primer node que és el de l'organització.
	 */
	Root_Item 	getAllTreeOrganization(long identifierID);
	/**
	 * Ens retorna la metadata.
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess. 
	 * @return MetadataInformation
	 */
	MetadataInformation	getMetadata(long identifierID);
	/**
	 * Ens retorna l'estructura per defecte d'un usuari per la 
	 * organització indicada. 
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @return UserObjective : Retornem tot en format UserObjective.
	 */
	UserObjective	getUserDefaultData(long identifierID);
	
	/**
	 * Carregarà les dades del node actual i en cas de que aquest
	 * fós un Cluster li afegiria els seus fills però sense carregar
	 * les seves dades.
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @param itemIdentifierID : identificador de l'item dintre del dataAccess.
	 * @return Abstract_Item : El nodel que hem demanat que ens omplis.
	 */
	Abstract_Item	loadTreeNode(long identifierID,
			long itemIdentifierID);
	/**
	 * Carregarà les dades del node actual i en cas de que aquest
	 * fós un Cluster tans nivells de fills com ens indiqui 'numLevels'.
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @param itemIdentifierID : identificador de l'item dintre del dataAccess.
	 * @param numLevels : indica el número de nivells que volem carregar.
	 * @return Abstract_Item : El nodel que hem demanat que ens omplis.
	 */
	Abstract_Item	loadTreeNode(long identifierID,
			long itemIdentifierID, int numLevels);
	
	/**
	 * Ens retorna l'estructura d'un usuari per la 
	 * organització indicada. 
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @param studentID : Identificador de l'usuari (OSID).
	 * @return UserObjective : Retornem tot en format UserObjective.
	 */
	UserObjective getUserData(long identifierID, String studentID);
	
	/**
	 * Guardarem l'informació de l'estructura d'un usuari per la 
	 * organització indicada. 
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @param studentID : Identificador de l'usuari dintre del DataAccess.
	 * @param userObjective : L'estructura que volem guardar.
	 * @return boolean : True si tot ha anat correcte.
	 */
	boolean saveUserData(
			long identifierID, int studentID, UserObjective userObjective);
	
	/**
	 * Ens comprova si un usuari està a la BD, per tant si després
	 * haurem d'actualitzar o si li haurem de crear les dades. 
	 * 
	 * @param identifierID : identificador de la organització
	 * 		dintre del dataAccess.
	 * @param globalStudentID : Identificador de l'usuari (OSID).
	 * @return int : Retornem l'identificador de l'usuari. 
	 * 		En cas de que no estigui creat encara retornarem 0.
	 */
	int testUserGlobalID(
			long identifierID, String globalStudentID);
	
	/**
	  * Actualitza l'objecte serialitzat (scorm_structure) de 
	  * l'usuari amb l'identificador indicat.
	  * 
	  * Nota! Fa un update, per tant ha d'existir l'usuari!
	  * 
	  * @param courseStudentName : El nom de la taula en que ho guardem.
	  * @param studentId : L'identificador de l'usuari.(int Type) 
	  * @param object : Retornarà l'UserObjective, caldrà fer un cast.
	  * @return int Type: Retornarà l'identificador del resultset. 
	  */
	int insertUserObjective(
			String courseStudentName, int studentId, Object object);
	/**
	 * MySQL Example: 
	 * CALL insertStudentsClass( _organization_id integer, 
	 * 		_global_student_id VARCHAR(100)).
	 * 
	 * @param organizationId : identificador de l'organització.
	 * @param globalStudentId : identificador global de l'estudiant 
	 * (serà el que rebrem per OSIDs).
	 * @return int : identificador de l'entrada actual. 
	 */
	int insertStudentsClass(
			long organizationId,
			String globalStudentId);
	
	/**
	 * MySQL Example: 
	 * CALL updateStudentsClass( _organization_id integer, 
	 * 		_student_id integer, _satisfied BIT(1), 
	 * 		_completed BIT(1), _progress_measure DECIMAL(5,3), 
	 * 		_last_item_title VARCHAR(30), 
	 * 		_completed_percent	DECIMAL(5,3)).
	 * 
	 * 
	 * @return boolean : True/False si hi ha hagut algun error. 
	 */
	boolean updateStudentsClass(
			long organizationId, int studentId, 
			boolean satisfied, boolean completed,
			double progressMeasure, String lastItemTitle,
			double completedPercent);
	/**
	 * MySQL Example: 
	 * CALL updateStudentsClass( _organization_id integer, 
	 * 		_student_id integer, _satisfied BIT(1), 
	 * 		_completed BIT(1), _progress_measure DECIMAL(5,3), 
	 * 		_last_item_title VARCHAR(30),
	 * 		_completed_percent	DECIMAL(5,3)).
	 * 
	 * 
	 * @return boolean : True/False si hi ha hagut algun error. 
	 */
	boolean updateStudentsClassLASTITEM(
			long organizationId, int studentId, 
			boolean satisfied, boolean completed,
			double progressMeasure,
			double completedPercent);
	
	
	/**
	 * Java Call Example:
	 * int writeJavaObject("coursestudent_1", studentID, userObjective). 
	 * 
	 * @param tableName : String Type. El nom de la taula on és 
	 * 	guarda.
	 * @param studentID : int Type.
	 * 	UserObjective serialitzada.
	 * @param object : Object Type.
	 * @return int : identificador de l'entrada a la taula. 
	 */
	int updateUserObjectiveDataAccess(
			long tableName,
			String studentID,
			Object object);
	
	/**
	 * Aquesta funció testejarà si existeix un curs SCORM per al DataAccess
	 * que tenim per defecte.
	 * @param nameIdentificator : String Type. 
	 * @return boolean Type: Retornà true si existeix, false si no.
	 */
	boolean existSCORMCourse(
			final String nameIdentificator);
	 /**
     * Aquesta funció elimina un curs per complert, amb totes les seves
     * dades privades.
     * 
     * @param organizationID : long Type.
     * @return boolean : True si tot ha anat be i False si ha fallat algo.
     */
	boolean removeAllSCORMCourse(
    		final long organizationID);
	/**
	 * Aquesta funció servirà per eliminar la part del seqüenciament d'un curs.
	 * O sigui que eliminarem tots els elements que conformen el motor i 
	 * l'estructura d'un curs: L'arbre d'activitats amb tots els elements de 
	 * seqüenciament i tot el DataModel de l'usuari per aquell curs. 
	 * D'aquesta manerà només guardarem l'informació "final" d'aquest curs, 
	 * amb les notes i qualificacions finals dels usuaris.
     * 
     * @param organizationID : long Type.
     * @return boolean : True si tot ha anat be i False si ha fallat algo.
     */
	boolean removeSequencingSCORMCourse(
    		final long organizationID);
		

}
