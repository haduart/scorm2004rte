// $Id: GestorBD.java,v 1.22 2008/03/31 13:56:19 ecespedes Exp $
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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;


import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Abstract_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Root_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.MetadataInformation;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.DataAccess;


/** <!-- Javadoc -->
 * $Id: GestorBD.java,v 1.22 2008/03/31 13:56:19 ecespedes Exp $
 * <b>Títol:</b> GestorBD <br><br>
 * <b>Descripció:</b> <br><br> 
 * <b>Companyia</b> Departament d'Aprenentage Semipresencial (LaSalleOnLine
 * Enginyeries).<br>
 *
 * @author Eduard Céspedes i Borràs / LaSalle / ecespedes@salle.url.edu
 * @version Versió $Revision: 1.22 $ $Date: 2008/03/31 13:56:19 $
 * $Log: GestorBD.java,v $
 * Revision 1.22  2008/03/31 13:56:19  ecespedes
 * Depurant objectives secundaris
 *
 * Revision 1.21  2008/02/28 16:15:23  ecespedes
 * Millorant el sistema (8)
 *
 * Revision 1.20  2008/02/14 14:54:33  ecespedes
 * Millorant el sistema (5)
 *
 * Revision 1.19  2008/02/01 18:16:48  toroleo
 * Adaptació a la nova base de dades.
 *
 * Revision 1.18  2008/02/01 03:07:32  ecespedes
 * Versió Beta Final amb la BD Serialitzada.
 *
 * Revision 1.17  2008/01/31 20:18:58  ecespedes
 * Millorant el sistema (3)
 *
 * Revision 1.16  2008/01/31 16:20:29  ecespedes
 * Millorant el sistema (2)
 *
 * Revision 1.15  2008/01/31 10:08:37  msegarra
 * Afegit dues noves consultes:
 * - getIdOrganizationsQuery
 * - getCompletedPercentUserOrganizationQuery
 *
 * Revision 1.14  2008/01/30 17:41:15  ecespedes
 * Versió final: organització  serialitzada.
 *
 * Revision 1.13  2008/01/29 18:01:41  ecespedes
 * Implementant versió serialitzada de l'organització.
 *
 * Revision 1.12  2008/01/24 15:41:11  ecespedes
 * Dissenyant un nou sistema per gestionar les Bases de dades (DataAccess)
 *
 * Revision 1.11  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral·lel amb el procés de desenvolupament del gestor de BD.
 *
 * Revision 1.10  2008/01/08 11:50:21  ecespedes
 * Ultimes modificacions de l'OverallSequencingProcess
 *
 * Revision 1.9  2007/12/27 15:02:24  ecespedes
 * Reunificant totes les crides del seqüenciament.
 *
 * Revision 1.8  2007/12/21 15:42:05  toroleo
 * S'ha tret un missatge que sortia per consola.
 * Eliminació de imports que no es feien servir.
 * S'ha afegit:
 *  -Consulta per fer la conversió de
 *  l'identificador d'usuari utilitzat al sistema
 *  central a l'identificador usat al subsistema
 *  scorm.
 *  -Consulta per l'obtenció del nom d'una
 *  organització.
 *
 * Revision 1.7  2007/12/13 15:22:31  toroleo
 * S'ha afegit:
 *  -La busqueda de les dades d'un curs.
 *  -La busqueda de tots els estudiants d'un curs
 *
 * Revision 1.6  2007/11/29 15:54:35  ecespedes
 * Implementant les SequencingRules (part 1)
 *
 * Revision 1.5  2007/11/23 15:04:58  ecespedes
 * El GestorBD ja implementa l'interfície DataAccess.
 *
 * Revision 1.4  2007/11/23 12:02:22  ecespedes
 * Començant a tocar el GestorBD en serio -> primer UserObjective serialitzat.
 *
 * Revision 1.3  2007/11/07 13:15:08  ecespedes
 * Creat tot el sistema que controla els DataAccess, per tal
 * que el CourseAdministrator pugui subministrar-lo quan un
 * UserObjective li demani.
 *
 * Revision 1.2  2007/10/31 12:57:01  ecespedes
 * Començant a crear tot el sistema de gestió dels arbres:
 * + Creant l'interfífice DataAccess que ens servirà tan
 * 	pel parser com pel SGBD.
 * - Falta que el ParserHandler/GestorBD implementi l'interfície.
 *
 */
public final class GestorBD extends DBConnector implements DataAccess {
		
	/**
	 * És la variable que instanciarà l'objecte de GestorBD.
	 */
	private static GestorBD gestorBD = null;
		
	
	/**
	 * Constant Type String.
	 * SQL Insert.
	 * "INSERT INTO coursestudent_1(student_id) VALUES (?)";
	 */	
	private static final String SQL_INSERT_COURSESTUDENT =		
		"INSERT INTO coursestudent_1(student_id) VALUES (?)";
	/**
	 * Constant Type String.
	 * SQL Insert.
	 * "INSERT INTO coursestudent_1(student_id,scorm_structure) VALUES (?,?)";
	 */	
	private static final String SQL_INSERT_COURSESTUDENT_SCORM_STRUCTURE =
		"INSERT INTO coursestudent_1(student_id,scorm_structure) VALUES (?,?)";	
	
	/**
	 * Constant Type String.
	 * SQL Select.
	 * Consulta per recuperar tots els estudiants d'un curs.
	 * 
	 *
	private static final String SQL_SELECT_SCORM =
		"SELECT i.title, sc.global_student_id, o.organization_id,"
		+ " sc.visits, sc.last_Access, sc.completed_percent, sc.last_item_title"
		+ " FROM organizations o, items i, studentsClass sc"
		+ " WHERE o.scorm_id = ? AND o.organization_id = i.organization_id"
		+ " AND sc.organization_id = o.organization_id;";
	**/	
	private static final String SQL_SELECT_SCORM =
		"SELECT sc.global_student_id, o.organization_id,"
		+ " sc.visits, sc.last_Access, sc.completed_percent, sc.last_item_title"
		+ " FROM organizations o, studentsClass sc"
		+ " WHERE o.scorm_id = ? "
		+ " AND sc.organization_id = o.organization_id;";

	/*private static final String SQL_SELECT_SCORM =
		"SELECT o.title, sc.global_student_id, o.organization_id,"
		+ " sc.visits, sc.last_Access, sc.completed_percent, sc.last_item_title"
		+ " FROM organizations o, studentsClass sc"
		+ " WHERE o.scorm_id = ? "
		+ " AND sc.organization_id = o.organization_id;"; */
		
	/**
	 * Constant Type String.
	 * SQL Select.
	 * Consulta per recuperar el nom del scorm.
	 */
	private static final String SQL_SELECT_NAME_SCORM =
		"SELECT scorm_id_text FROM scorms WHERE scorm_id = ?;";
	/**
	 * Constant Type String.
	 * SQL Select.
	 * Consulta per recuperar el nom del scorm.
	 */
	private static final String SQL_SELECT_ID_SCORM =
		"SELECT scorm_id FROM scorms WHERE scorm_id_text like ?;";
		
	/**
	 * Constant Type String.
	 * SQL Select.
	 * Consulta per fer la conversió de l'identificador
	 * del sistema central a l'identificador del subsistema
	 * scorm.
	 */
	private static final String SQL_SELECT_ID_USER_SCORM =
		"SELECT student_id FROM studentsclass WHERE global_student_id = ?";
	
	/**
	 * Constant Type String.
	 * SQL Select.
	 * Consulta per fer la conversió de l'identificador
	 * del sistema central a l'identificador del subsistema
	 * scorm.
	 */
	private static final String SQL_SELECT_NAME_ORGANIZATION =
		"SELECT title FROM organizations WHERE organization_id = ?";
	
    /**
     * Constant Type String.
     * SQL Select.
     * Consulta per conèixer les organitzacions que pertanyen a un
     * curs scorm predeterminat.
     */
    private static final String SQL_SELECT_ID_ORGANIZATIONS =
        "SELECT organization_id FROM organizations WHERE scorm_id = ?";
    
    /**
     * Constant Type String.
     * SQL Select.
     * Consulta per conèixer el percentatge de completat d'una organització
     * d'un usuari.
     */
    private static final String SQL_SELECT_COMPLETED_USER_ORG =
        "SELECT completed_percent FROM studentsclass " 
    	+ "WHERE organization_id = ? AND global_student_id = ?";
	
	/**
	 * El constructor per defecte serà privat ja que és tracta 
	 * d'un singleton, per tant tindrem una única instància 
	 * d'aquesta classe en memòria. 
	 *
	 */
	private GestorBD() {        
        super();
	}
	
	/**
	 * Es recupera la instància de l'objecte GestorBD 
	 * ja sigui creant ara la instància o recuperant la ja feta anteriorment.
	 * 
	 * @return Instància de l'objecte GestorBD.
	 */
	public static synchronized GestorBD getInstance() {
		
		if (gestorBD == null) {
			gestorBD = new GestorBD();
		}
		return gestorBD;
		
	}
	
	//------  IMPLEMENTA els mètodes de l'Interface DataAccess  ------
	
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
	public int getDataAccessID() {
		return DATAACCESSID;
	}
	/**
	 * Funció per testejar la BD!!!
	 *  
	 * @return boolean Type.
	 *
	public boolean testQuery() {
		
		try {
			Statement stmt = conn.createStatement();
			@SuppressWarnings("unused")
			ResultSet rs = 
				stmt.executeQuery("SELECT * FROM organizations;");			
		} catch (SQLException e) {
			System.out.println("Ha petat la consulta!!!!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	**/	
	
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
	  * @throws Exception : Llança una excepció si hi ha un error.
	  */
	private int writeJavaObject(
			final String courseStudentName,
			final int studentId,
			final Object object) 
	 throws Exception {		
		String sqlUpdateScormStructure =
			"UPDATE " + courseStudentName
			+ " set scorm_structure = ? WHERE student_id = ?";
		PreparedStatement pstmt =
			conn.prepareStatement(sqlUpdateScormStructure);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(baos);
		oout.writeObject(object); //serializing employee
		    
		// set input parameters
		pstmt.setBytes(1, baos.toByteArray());		    
		pstmt.setInt(2, studentId);
		pstmt.executeUpdate();

		// get the generated key for the id
		ResultSet rs = pstmt.getGeneratedKeys();
		int id = -1;
		if (rs.next()) {
			id = rs.getInt(1);
		}
		
		rs.close();
		pstmt.close();
		return id;
	}
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
	public int insertUserObjective(
			final String courseStudentName,
			final int studentId,
			final Object object) { 
		int id = -1;
		String sqlUpdateScormStructure =
			"INSERT INTO " + courseStudentName
			+ " SET student_id = ?, scorm_structure = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlUpdateScormStructure);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(baos);
			oout.writeObject(object); //serializing employee
			    
			// set input parameters
			pstmt.setInt(1, studentId);
			pstmt.setBytes(2, baos.toByteArray());
			pstmt.executeUpdate();

			// get the generated key for the id
			ResultSet rs = pstmt.getGeneratedKeys();
			
			if (rs.next()) {
				id = rs.getInt(1);
			}
			
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return id;
	}
	private int writeJavaObjectDISK(
			final String courseStudentName,
			final int studentId,
			final Object object) throws Exception {
		Date dataActual = new Date();
		FileOutputStream f = new FileOutputStream("C:\\tmp\\" 
				+ "UserObjective-"
				+ dataActual.getHours()
				+ dataActual.getMinutes()
				+ dataActual.getSeconds());
        ObjectOutputStream s = new ObjectOutputStream(f); 
        //String company = "My Good Company"; 
        s.writeObject(object); 
        s.flush(); 
        return 1;
        
	}
	public String writeJavaCouseManager_ItemDISK(
			final Object object) throws Exception {
		Date dataActual = new Date();
		String nom = "CourseManager-"
			+ dataActual.getHours()
			+ dataActual.getMinutes()
			+ dataActual.getSeconds();
		
		FileOutputStream f = new FileOutputStream("C:\\tmp\\" 
				+ nom);
		
        ObjectOutputStream s = new ObjectOutputStream(f); 
        //String company = "My Good Company"; 
        s.writeObject(object); 
        s.flush(); 
        return nom;        
	}
	/**
	 * Llegim de disc! Funció temporal per testejar les optimitzacions
	 * de memòria del sistema. 
	 * Descripció: Serialitzem tot el curs i el guardem al disc de
	 * manera que poguem contar sense marge d'error el que ocupa un curs
	 * amb el nostre mètode.
	 * 
	 * @param object : Object Type
	 * @return String Type
	 * @throws Exception :
	 */
	public String writeJavaAbstract_ItemDISK(
			final Object object) throws Exception {
		Date dataActual = new Date();
		String nom = "AbstractItem-"
			+ dataActual.getHours()
			+ dataActual.getMinutes()
			+ dataActual.getSeconds();
		
		FileOutputStream f = new FileOutputStream("C:\\tmp\\" 
				+ nom);
		
        ObjectOutputStream s = new ObjectOutputStream(f); 
        //String company = "My Good Company"; 
        s.writeObject(object); 
        s.flush(); 
        return nom;        
	}
	/**
	 * Llegim de disc! Funció temporal per testejar les optimitzacions
	 * de memòria del sistema. 
	 * Descripció: Serialitzem tot el curs i el guardem al disc de
	 * manera que poguem contar sense marge d'error el que ocupa un curs
	 * amb el nostre mètode.
	 * 
	 * @param nom : String Type
	 * @return Object Type
	 * @throws Exception :
	 */
	public Object readJavaAbstract_ItemDISK(
			final String nom) 
	throws Exception {
		
		FileInputStream in = new FileInputStream("C:\\tmp\\" 
				+ nom); 
        ObjectInputStream s = new ObjectInputStream(in); 
         
        return s.readObject();         
	}
	
	 /**
	  * Extreu l'objecte serialitzat (scorm_structure) de la
	  * base de dades segons l'identificador d'usuari que li
	  * passis.
	  * 
	  * @param id : L'identificador de l'usuari.(int Type) 
	  * @param courseStudentName : El nom de la taula en que ho guardem.
	  * @return Object : Retornarà el UserObjective, caldrà fer un cast.
	  * @throws Exception : Llança una excepció si hi ha un error.
	  */
	 private Object readJavaObject(
			 final String courseStudentName,
			 final String id)
	 throws Exception {
		 /*
		 String sqlSelectScormStructure =
			 "SELECT scorm_structure FROM "
			 + courseStudentName + " WHERE student_id = ?";
		*/
		 String sqlSelectScormStructure = 
			 "SELECT scorm_structure FROM "
			 + courseStudentName + " as course , "
			 + "studentsclass as student " 
			 + "WHERE student.global_student_id like ? AND  " 
			 + "student.student_id = course.student_id";
			
		 Object returnedObject = null;
		 
		 PreparedStatement pstmt =
			 conn.prepareStatement(sqlSelectScormStructure);
		 		 
		 pstmt.setString(1, id);
		 ResultSet rs = pstmt.executeQuery();
		    if (rs.next()) {	           
	            Blob blob = rs.getBlob("scorm_structure");
	            
	            // Get bytes from the BLOB using a stream
	            InputStream is = blob.getBinaryStream();
	            ObjectInputStream oIn = new ObjectInputStream(is);
	            returnedObject = oIn.readObject();	            
	        }
		    return returnedObject;
	 }
	public Root_Item getAllTreeOrganization(long identifierID) {
		return null;
	}
	public Root_Item getFirstOrganization(long identifierID, int numLevels) {
		return null;
	}
	/**
	 * Retornem la descripció del DataAccess actual.
	 * REQUIRED for the DataAccess Interface.
	 * @return String Type.
	 */
	public String getDescription() {
		return DESCRIPTION;
	}
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
	public Root_Item getFirstOrganization(final long identifierID) {
		 String sqlSelectScormStructure =
			 "SELECT blobOrganization FROM SerialOrganizations" 
			 + " WHERE organization_id = ?";			
		 Object returnedObject = null;
		 
		 PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlSelectScormStructure);
		
		 		 
		 pstmt.setLong(1, identifierID);
		 ResultSet rs = pstmt.executeQuery();
		    if (rs.next()) {	           
	            Blob blob = rs.getBlob("blobOrganization");
	            
	            // Get bytes from the BLOB using a stream
	            InputStream is = blob.getBinaryStream();
	            ObjectInputStream oIn = new ObjectInputStream(is);
	            returnedObject = oIn.readObject();	            
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		    return (Root_Item) returnedObject;
	}
	/**
	 * Ens retorna la metadata.
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess. 
	 * @return MetadataInformation
	 */
	public MetadataInformation getMetadata(final long identifierID) {		
		MetadataInformation novaMetadata = new MetadataInformation();
		
		 try {
			 
			 PreparedStatement pstmt =
				 conn.prepareStatement("SELECT title, version," 
						 + " description, requeriments, end_user," 
						 + " educative_environment, author, copyright," 
						 + " learning_duration FROM metadata " 
						 + " WHERE scorm_id = 1");
			 
			 //pstmt.setLong(1, identifierID);
			 ResultSet rs = pstmt.executeQuery();
			 if (rs.next()) {
				 novaMetadata.title = rs.getString("title");
				 novaMetadata.author = rs.getString("author");
				 novaMetadata.copyright = rs.getString("copyright");
				 novaMetadata.description = rs.getString("description");
				 novaMetadata.educationalContext =
					 rs.getString("educative_environment");
				 novaMetadata.intendedEndUserRole =
					 rs.getString("end_user");
				 novaMetadata.requirement = rs.getString("requeriments");
				 novaMetadata.typicalLearningTime =
					 rs.getString("learning_duration");
				 novaMetadata.version =
					 rs.getString("version");
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		return novaMetadata;
	}
	/**
	 * Ens retorna l'estructura d'un usuari per la 
	 * organització indicada. 
	 * 
	 * @param identifierID : identificador de la organització
	 * 		dintre del dataAccess.
	 * @param studentID : Identificador de l'usuari dintre del DataAccess.
	 * @return UserObjective : Retornem tot en format UserObjective.
	 */
	public UserObjective getUserData(
			final long identifierID, final String studentID) {
		UserObjective retUser = null;
		try {
			retUser =
				(UserObjective) readJavaObject(
						"coursestudent_" + identifierID, studentID);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return retUser;
	}
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
	public int testUserGlobalID(
			final long identifierID, final String globalStudentID) {
		
		int returnValue = 0;
		String sqlUpdateScormStructure =
			"CALL selectTestGlobalStudent( ? , ? )";
						    		
		try {
			PreparedStatement pstmt =
				conn.prepareStatement(sqlUpdateScormStructure);
			
			// set input parameters
			pstmt.setString(1, globalStudentID);
			pstmt.setLong(2, identifierID);		    
															
			pstmt.executeQuery();
			
			// get the generated key for the id
			ResultSet rs = pstmt.getResultSet();
			
			if (rs.next()) {
				returnValue = rs.getInt("STUDENT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return returnValue;		
	}
	/**
	 * Aquesta funció testejarà si existeix un curs SCORM per al DataAccess
	 * que tenim per defecte.
	 * @param nameIdentificator : String Type. 
	 * @return boolean Type: Retornà true si existeix, false si no.
	 */
	public boolean existSCORMCourse(final String nameIdentificator) {
		try {
			return (getScormIdentificationQUERY(nameIdentificator) != null);
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}		
	}
	/**
	 * Crearem una nova taula pel curs actual, el nom 
	 * de la qual estarà definida pel paràmetre "tableName".
	 * 
	 * @param tableName : String Type.
	 * @return boolean : true si tot va bé i false si no.
	 */
	public boolean createCourseStudent(
			final String tableName) {
		boolean resultBoolean = true;
		
		try {
			Statement stmt = conn.createStatement();
			int rs = 
				stmt.executeUpdate("CREATE TABLE `" 
						+ tableName + "`("
						+ "`student_id`	int(11) UNSIGNED" 
						+ " NOT NULL,"
						+ "`scorm_structure`	BLOB, "
						+ "PRIMARY KEY (student_id)"
						+ ") ENGINE = MyISAM;");
			/*
			ResultSet rs = 
				stmt.executeQuery("CREATE TABLE '" 
						+ tableName + "'("
						+ "`student_id`	int(11) UNSIGNED" 
						+ " NOT NULL,"
						+ "`scorm_structure`	BLOB, "
						+ "PRIMARY KEY (student_id)"
						+ ") ENGINE = MyISAM;");
			*/			
		} catch (SQLException e) {
			System.out.println("Ha petat la consulta!!!!");
			e.printStackTrace();
			return false;
		}		
		
		return resultBoolean;
	}	
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
	 * @param copyright : String Type.
	 * @return int Type: Identificador del metadata.
	 */
	public int insertMetadata(
			final int scormId, 
			final String version, 
			final String title, 
			final String description,
			final String requeriments, 
			final String endUser,
			final String educativeEnvironment, 
			final String author,
			final String learningDuration, 
			final String copyright) {
		
		int returnValue = 0;
		String sqlUpdateScormStructure =
			"CALL insertMetadata( ? , " 
			+ " ? , ? , ? , ? , ? , ? , ? , ? , ? )";
						    		
		try {
			PreparedStatement pstmt =
				conn.prepareStatement(sqlUpdateScormStructure);
			
			// set input parameters
			pstmt.setInt(1, scormId);		    
			pstmt.setString(2, version);
			pstmt.setString(3, title);
			pstmt.setString(4, description);
			pstmt.setString(5, requeriments);
			pstmt.setString(6, endUser);
			pstmt.setString(7, educativeEnvironment);
			pstmt.setString(8, author);
			pstmt.setString(9, learningDuration);
			pstmt.setString(10 , copyright);
			
			pstmt.executeUpdate();
			
			// get the generated key for the id
			ResultSet rs = pstmt.getResultSet();
			
			if (rs.next()) {
				returnValue = rs.getInt("LASTID");
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}	
		
		return returnValue;
		
	}
	
	/**
	 * MySQL Example:
	 * CALL insertScorms( _scorm_id_text TEXT(10), 
	 * 		_new_screen BIT(1)).
	 *  
	 * @param scormIdIext : String Type 
	 * @param newScreen : Boolean Type
	 * @return int : Identificador de l'element qeu acabem d'entrar. 
	 */
	public int insertScorms(
			final String scormIdIext, 
			final Boolean newScreen) {
		int returnValue = 0;
		
		String sqlUpdateScormStructure =
			"CALL insertScorms( ? , ? )";
						    		
		try {
			PreparedStatement pstmt =
				conn.prepareStatement(sqlUpdateScormStructure);
			
			// set input parameters
			pstmt.setString(1, scormIdIext);		    
			pstmt.setBoolean(2, newScreen);
						
			pstmt.executeUpdate();
			
			// get the generated key for the id
			ResultSet rs = pstmt.getResultSet();
				
			if (rs.next()) {
				returnValue = rs.getInt("LASTID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return returnValue;		
	}
	
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
	 * @return int :   
	 */
	public int insertOrganizations(
			final int scormId,
			final String structure, 
			final Boolean isVisible,
			final String itemIdText, 
			final String title,
			final String dateTimeStart,
			final String dateTimeEnd) {
		int returnValue = 0;
		
		String sqlUpdateScormStructure =
			"CALL insertOrganizations( ? , ? , ? , ? , ? , ? , ? )";
						    		
		try {
			PreparedStatement pstmt =
				conn.prepareStatement(sqlUpdateScormStructure);
			
			// set input parameters
			pstmt.setInt(1, scormId);		    
			pstmt.setString(2, structure);
			pstmt.setBoolean(3, isVisible);
			pstmt.setString(4, itemIdText);
			pstmt.setString(5, title);	
			pstmt.setString(6, dateTimeStart);
			pstmt.setString(7, dateTimeEnd);
			
			pstmt.executeUpdate();
			
			// get the generated key for the id
			ResultSet rs = pstmt.getResultSet();
			
			if (rs.next()) {
				returnValue = rs.getInt("LASTID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return returnValue;		
	}
	
	
	/**
	 * MySQL Example:
	 * CALL insertSerialOrganizations (
	 * 		_organization_id integer, 
	 * 		_BLOB BLOB).
	 * 
	 * @param organizationId : Int Type.
	 * @param blobOrganization : Object Type. 
	 * @return int :   
	 */
	public int insertSerialOrganizations(
			final int organizationId,
			final Object blobOrganization) {
		int returnValue = 0;
		
		String sqlUpdateScormStructure =
			"CALL insertSerialOrganizations( ? , ? )";
						    		
		try {
			PreparedStatement pstmt =
				conn.prepareStatement(sqlUpdateScormStructure);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(baos);
			oout.writeObject(blobOrganization); //serializing employee
			    
			// set input parameters
			pstmt.setInt(1, organizationId);
			pstmt.setBytes(2, baos.toByteArray());
			
									
			pstmt.executeUpdate();
			
			// get the generated key for the id
			ResultSet rs = pstmt.getResultSet();
			
			if (rs.next()) {
				returnValue = rs.getInt("LASTID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return returnValue;		
	}
	
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
	public int insertStudentsClass(
			final long organizationId, 
			final String globalStudentId) {
		int returnValue = 0;
		String sqlUpdateScormStructure =
			"CALL insertStudentsClass( ? , ? )";
						    		
		try {
			PreparedStatement pstmt =
				conn.prepareStatement(sqlUpdateScormStructure);
			
			// set input parameters
			pstmt.setLong(1, organizationId);		    
			pstmt.setString(2, globalStudentId);
												
			pstmt.executeUpdate();
			
			// get the generated key for the id
			ResultSet rs = pstmt.getResultSet();
			
			if (rs.next()) {
				returnValue = rs.getInt("LASTID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return returnValue;
	}
	
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
	public boolean updateStudentsClass(
			final long organizationId, final int studentId, 
			final boolean satisfied, final boolean completed,
			final double progressMeasure, final String lastItemTitle,
			final double completedPercent) {
		String sqlUpdateScormStructure =
			"CALL updateStudentsClass( ? , ? , ? , ? , ? , ? , ?)";
						    		
		try {
			PreparedStatement pstmt =
				conn.prepareStatement(sqlUpdateScormStructure);
			
			// set input parameters
			pstmt.setLong(1, organizationId);
			pstmt.setInt(2, studentId);
			pstmt.setBoolean(3, satisfied);
			pstmt.setBoolean(4, completed);
			pstmt.setDouble(5, progressMeasure);
			pstmt.setString(6, lastItemTitle);
			pstmt.setDouble(7, completedPercent);
												
			pstmt.executeUpdate();
						
			@SuppressWarnings("unused")
			SQLWarning rs = pstmt.getWarnings();
						
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	
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
	public boolean updateStudentsClassLASTITEM(
			final long organizationId, final int studentId, 
			final boolean satisfied, final boolean completed,
			final double progressMeasure,
			final double completedPercent) {
		String sqlUpdateScormStructure =
			"CALL updateStudentsClassLASTITEM( ? , ? , ? , ? , ? , ?)";
						    		
		try {
			PreparedStatement pstmt =
				conn.prepareStatement(sqlUpdateScormStructure);
			
			// set input parameters
			pstmt.setLong(1, organizationId);
			pstmt.setInt(2, studentId);
			pstmt.setBoolean(3, satisfied);
			pstmt.setBoolean(4, completed);
			pstmt.setDouble(5, progressMeasure);
			pstmt.setDouble(6, completedPercent);
												
			pstmt.executeUpdate();
						
			@SuppressWarnings("unused")
			SQLWarning rs = pstmt.getWarnings();
						
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	/**
	 * Ens retorna l'estructura d'un usuari per la 
	 * organització indicada. 
	 * 
	 * @param identifierID : ID de la organització dintre del dataAccess.
	 * @return UserObjective : Retornem tot en format UserObjective.
	 */
	public UserObjective getUserDefaultData(final long identifierID) {
		UserObjective retUser = null;		
		try {
			retUser =
				(UserObjective) readJavaObject(
						"coursestudent_" + identifierID, "DefaultUser");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return retUser;
	}
	public Abstract_Item loadTreeNode(long identifierID, long itemIdentifierID) {
		return null;
	}
	public Abstract_Item loadTreeNode(long identifierID, long itemIdentifierID, int numLevels) {
		return null;
	}
	
	/**
	 * Java Call Example:
	 * int writeJavaObject("coursestudent_1", studentID, userObjective). 
	 * 
	 * @param tableName : nom de l'organització 
	 * 	guarda.
	 * @param studentID : int Type.
	 * @param userObjective : Object Type. Serà l'estructura 
	 * 	UserObjective serialitzada.
	 * @return int : identificador de l'entrada a la taula.
	 */
	public int updateUserObjectiveDataAccess(
			final long tableName,
			final String studentID,
			final Object object)  {
		
		int id = -1;
		/*	
		try {
			writeJavaObject(tableName, studentID, userObjective);
			//writeJavaObjectDISK("coursestudent_1", studentID, userObjective);
		} catch (Exception e) {
			e.printStackTrace();			
		}
		*/		
		String sqlUpdateScormStructure =
			"UPDATE coursestudent_" + tableName
			+ ", studentsClass set coursestudent_" + tableName 
			+ ".scorm_structure = ? WHERE  coursestudent_"
			+ tableName + ".student_id =" 
			+ " studentsClass.student_id AND " 
			+ "studentsClass.global_student_id = ?";
				
		
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sqlUpdateScormStructure);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(baos);
			oout.writeObject(object); //serializing employee
			    
			// set input parameters
			pstmt.setBytes(1, baos.toByteArray());		    
			pstmt.setString(2, studentID);
			pstmt.executeUpdate();

			// get the generated key for the id
			ResultSet rs = pstmt.getGeneratedKeys();
			
			if (rs.next()) {
				id = rs.getInt(1);
			}			
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
		return id;
	}
	
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
	public int updateUserObjective(
			final String tableName,
			final int studentID,
			final Object object)  {
		
		int id = -1;
				
		try {
			id = writeJavaObject(tableName, studentID, object);
			//writeJavaObjectDISK("coursestudent_1", studentID, userObjective);
		} catch (Exception e) {
			e.printStackTrace();			
		}	
		return id;
	}
	
	
	/**
	 * Guardarem l'informació de l'estructura d'un usuari per la 
	 * organització indicada. 
	 * 
	 * @param identifierID : identificador de la organització 
	 * 		dintre del dataAccess.
	 * @param studentID : Identificador de l'usuari dintre del DataAccess.
	 * @param userObjective : L'estructura que volem guardar.
	 * @return boolean : True si tot ha anat correcte.
	 */
	public boolean saveUserData(
			final long identifierID,
			final int studentID,
			final UserObjective userObjective) {
		userObjective.adlnav = null;
		try {
			writeJavaObject(
					"coursestudent_" + identifierID,
					studentID, userObjective);
			//writeJavaObjectDISK("coursestudent_1", studentID, userObjective);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Consulta per saber tots els alumnes d'un curs.
	 * 
	 * @param anIdCourse Identificador del curs del que es
	 * 					 vol llistar tots els alumnes.
	 * @return	Resultset amb el identificadors dels alumnes 
	 * 			del curs i dades del curs.
	 * @throws SQLException Llança una excepció si ha hagut 
	 * 		   un problema al fer la consulta.
	 */
	public ResultSet getCourseQuery(final int anIdCourse) throws SQLException {
		 
		 PreparedStatement pstmt =
			 conn.prepareStatement(SQL_SELECT_SCORM);
		 		 
		 pstmt.setInt(1, anIdCourse);
		 return pstmt.executeQuery();
		 
	}
	
	/**
	 * Consulta per saber el nom del scorm.
	 * 
	 * @param anIdCourse Identificador del curs del que es
	 * 					 vol saber el seu nom.
	 * @return	Nom del curs que correspon a l'identidicador
	 * 			 passat.
	 * @throws SQLException Llança una excepció si ha hagut 
	 * 		   un problema al fer la consulta.
	 */
	public String getNameCourseQuery(final int anIdCourse) throws SQLException {
		
		 PreparedStatement pstmt =
			 conn.prepareStatement(SQL_SELECT_NAME_SCORM);
		 
		 pstmt.setInt(1, anIdCourse);
		 
		 ResultSet rs = pstmt.executeQuery();
		 if (rs.next()) {
			 //s'ha trobat el curs buscat.
			 return rs.getString("scorm_id_text");	 
		 } else {
			 //no s'ha trobat el curs buscat.
			 return null;
		 }
		 		 	 
	}
	/**
	 * Consulta per saber l'identficador de l'scorm.
	 * 
	 * @param nameCourse Identificador de Text del curs del que es
	 * 					 vol saber el seu nom.
	 * @return	Nom del curs que correspon a l'identidicador
	 * 			 passat.
	 * @throws SQLException Llança una excepció si ha hagut 
	 * 		   un problema al fer la consulta.
	 */
	public Integer getScormIdentificationQUERY(
			final String nameCourse) throws SQLException {
		PreparedStatement pstmt =
			 conn.prepareStatement(SQL_SELECT_ID_SCORM);
		 
		 pstmt.setString(1, nameCourse);
		 
		 ResultSet rs = pstmt.executeQuery();
		 if (rs.next()) {
			 //s'ha trobat el curs buscat.
			 return rs.getInt("scorm_id");	 
		 } else {
			 //no s'ha trobat el curs buscat.
			 return null;
		 }		
	}
	

	/**
	 * Consulta per saber el nom de l'organització.
	 * 
	 * @param anIdOrganization Identificador de l'organització
	 * 							 del que es vol saber el seu nom.
	 * @return	Nom de l'organització que correspon a l'identidicador
	 * 			 passat.
	 * @throws SQLException Llança una excepció si ha hagut 
	 * 		   un problema al fer la consulta.
	 */
	public String getNameOrganizationQuery(final long anIdOrganization)
					throws SQLException {
		 
		 PreparedStatement pstmt =
			 conn.prepareStatement(SQL_SELECT_NAME_ORGANIZATION);
		 		 
		 pstmt.setLong(1, anIdOrganization);
		 ResultSet rs = pstmt.executeQuery();
		 if (rs.next()) {
			 //s'ha trobat el curs buscat.
			 return rs.getString("title");	 
		 } else {
			 //no s'ha trobat el curs buscat.
			 return null;
		 }
		 		 	 
	}
	
	/**
	 * Consulta per fer la conversió de l'identificador de l'estudiant
	 * del sistema central al subsistema scorm.
	 * 
	 * @param anIdStudent	Identificador de l'estudiant al sistema
	 * 						 central.
	 * @return	Identificador de l'estudiant al subsistema scorm.
	 * @throws SQLException Llança una excepció si ha hagut 
	 * 		   un problema al fer la consulta.
	 */
	public int getIdStudentScormQuery(final String anIdStudent)
				throws SQLException {
		 
		 PreparedStatement pstmt =
			 conn.prepareStatement(SQL_SELECT_ID_USER_SCORM);
		 		 
		 pstmt.setString(1, anIdStudent);
		 ResultSet rs = pstmt.executeQuery();
		 if (rs.next()) {
			 //s'ha trobat el curs buscat.
			 return rs.getInt("student_id");	 
		 } else {
			 //no s'ha trobat el curs buscat.
			 return 0;
		 }
		 		 	 
	}
	
    /**
     * Consulta per saber totes les organitzacions d'un curs.
     * 
     * @param anIdCourse Identificador del curs que es vol llistar 
     * les organitzacions que el formen.
     * @return Resultset amb els identificadors de les organitzacions 
     *  del curs.
     * @throws SQLException Llança una excepció si ha hagut 
     *         un problema al fer la consulta.
     */
    public ResultSet getIdOrganizationsQuery(final int anIdCourse)
                    throws SQLException {
         
         PreparedStatement pstmt =
             conn.prepareStatement(SQL_SELECT_ID_ORGANIZATIONS);
                 
         pstmt.setInt(1, anIdCourse);
         return pstmt.executeQuery();                    
    }
    
    /**
     * Consulta per saber el percentatge de completat d'un usuari 
     * en una organització.
     * 
     * @param anIdOrganization Identificador de l'organització.
     * @return  Percentatge de completat al sistema.
     * @throws SQLException Llança una excepció si ha hagut 
     *         un problema al fer la consulta.
     */
    public float getCompletedPercentUserOrganizationQuery(final int 
            anIdOrganization, final String anUser) throws SQLException {
         
         PreparedStatement pstmt =
             conn.prepareStatement(SQL_SELECT_COMPLETED_USER_ORG);
                 
         pstmt.setInt(1, anIdOrganization);
         pstmt.setString(2, anUser);
         ResultSet rs = pstmt.executeQuery();
         if (rs.next()) {
             return rs.getFloat("completed_percent");     
         } else {
             return 0;
         }
    }
    /**
     * 
     * @param organizationID
     * @return
     */
    private boolean deleteCourse(
			final long organizationID) {
		
		String sqlUpdateScormStructure =
			"CALL deleteALLCourse( ? )";
						    		
		try {
			PreparedStatement pstmt =
				conn.prepareStatement(sqlUpdateScormStructure);
			
			// set input parameters
			pstmt.setLong(1, organizationID);		    
															
			pstmt.executeQuery();
			
			// get the generated key for the id
			int rs = pstmt.getUpdateCount();
			
			if (rs > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return false;		
	}
    
    /**
     * 
     * @param organizationID
     * @return
     */
    private boolean deleteSequencingCourse(
			final long organizationID) {
		
		String sqlUpdateScormStructure =
			"CALL deleteSequencingCourse( ? )";
						    		
		try {
			PreparedStatement pstmt =
				conn.prepareStatement(sqlUpdateScormStructure);
			
			// set input parameters
			pstmt.setLong(1, organizationID);		    
															
			pstmt.executeQuery();
			
			// get the generated key for the id
			int rs = pstmt.getUpdateCount();
			
			if (rs > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return false;		
	}
    
    /**
     * 
     * @param organizationID
     * @return
     */
    private boolean dropCourseStudentTable(
			final long organizationID) {
		
		String sqlUpdateScormStructure =
			"drop table coursestudent_" + organizationID;
						    		
		try {
			PreparedStatement pstmt =
				conn.prepareStatement(sqlUpdateScormStructure);
			
			// set input parameters
			pstmt.setLong(1, organizationID);		    
															
			pstmt.executeQuery();
			
			// get the generated key for the id
			ResultSet rs = pstmt.getResultSet();
			SQLWarning warning = rs.getWarnings();
			if (warning == null) {
				return true;
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return false;		
	}
    /**
     * Aquesta funció elimina un curs per complert, amb totes les seves
     * dades privades.
     * 
     * @param organizationID : long Type.
     * @return boolean : True si tot ha anat be i False si ha fallat algo.
     */
    public boolean removeAllSCORMCourse(
    		final long organizationID) {
    	
    	if (deleteCourse(organizationID)) {
    		if (dropCourseStudentTable(organizationID)) {
    			return true;
    		}
    	}
    	return false;    	
    }
    
    /**
     * Aquesta funció elimina un curs per complert, amb totes les seves
     * dades privades.
     * 
     * @param organizationID : long Type.
     * @return boolean : True si tot ha anat be i False si ha fallat algo.
     */
    public boolean removeSequencingSCORMCourse(
    		final long organizationID) {
    	
    	if (deleteSequencingCourse(organizationID)) {
    		if (dropCourseStudentTable(organizationID)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
}

