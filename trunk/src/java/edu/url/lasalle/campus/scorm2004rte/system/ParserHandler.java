// $Id: ParserHandler.java,v 1.14 2008/03/31 13:56:19 ecespedes Exp $
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Abstract_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Leaf_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Root_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
	MetadataInformation;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.GestorBD;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.
	UserObjective;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMParser;
import edu.url.lasalle.campus.scorm2004rte.system.tools.ParserIdentifier;

/**
* $Id: ParserHandler.java,v 1.14 2008/03/31 13:56:19 ecespedes Exp $
* <b>T�tol:</b> ParserHandler <br><br>
* <b>Descripci�:</b> Aquesta classe s'encarregar� de totes les passes
* del parsejat cridant a les diverses classes que s'ocupen de cada 
* part concreta del proc�s. 
*
* @author Eduard C�spedes i Borr�s /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versi� $Revision: 1.14 $ $Date: 2008/03/31 13:56:19 $
* $Log: ParserHandler.java,v $
* Revision 1.14  2008/03/31 13:56:19  ecespedes
* Depurant objectives secundaris
*
* Revision 1.13  2008/02/28 16:15:23  ecespedes
* Millorant el sistema (8)
*
* Revision 1.12  2008/02/15 12:02:19  ecespedes
* Millorant el sistema (6)
*
* Revision 1.11  2008/02/14 14:54:33  ecespedes
* Millorant el sistema (5)
*
* Revision 1.10  2008/02/01 03:07:32  ecespedes
* Versi� Beta Final amb la BD Serialitzada.
*
* Revision 1.9  2008/01/31 14:23:37  ecespedes
* Millorant el sistema.
*
* Revision 1.8  2008/01/30 17:41:15  ecespedes
* Versi� final: organitzaci�  serialitzada.
*
* Revision 1.7  2008/01/29 18:01:41  ecespedes
* Implementant versi� serialitzada de l'organitzaci�.
*
* Revision 1.6  2008/01/18 18:07:24  ecespedes
* Serialitzades TOTES les classes per tal de que els altres puguin fer proves
* en paral�lel amb el proc�s de desenvolupament del gestor de BD.
*
* Revision 1.5  2007/11/23 15:04:58  ecespedes
* El GestorBD ja implementa l'interf�cie DataAccess.
*
* Revision 1.4  2007/11/07 13:15:08  ecespedes
* Creat tot el sistema que controla els DataAccess, per tal
* que el CourseAdministrator pugui subministrar-lo quan un
* UserObjective li demani.
*
* Revision 1.3  2007/11/06 16:33:27  ecespedes
* Fets els testos de benchmark per saber la quantitat de mem�ria
* que necessita el sistema i com respondrien les optimitzacions.
*
* Revision 1.2  2007/11/05 19:42:39  ecespedes
* Comen�ant a implementar el motor del seq�enciament.
* S'aniran fent testos per tal de veure diversos 'request'
* per tal d'emular el que seria un usuari que interactua.
*
* Revision 1.1  2007/10/31 12:57:01  ecespedes
* Comen�ant a crear tot el sistema de gesti� dels arbres:
* + Creant l'interf�fice DataAccess que ens servir� tan
* 	pel parser com pel SGBD.
* - Falta que el ParserHandler/GestorBD implementi l'interf�cie.
*
*/

public class ParserHandler { //implements DataAccess { //, Serializable{
		
	/**
	 * Constant String Type.
	 * Optional.
	 * "ParserHandler: Dades temporals obtingudes de l'imsmanifest.xml."
	 */
	private static final String DESCRIPTION =
		"ParserHandler: Dades temporals obtingudes de l'imsmanifest.xml.";
	/**
	 * Constant int Type.
	 * Required.
	 * Description: L'identificador del DataAccess.
	 * 
	 * Nota! El Parser Ser� dels pocs que no necessitar� un identificador
	 * "fix" perqu� tot el que faci ser� temporal, per aix� en comptes de
	 * ser una constant ser� un valor que ens donar� el ParserIdentifier.
	 */
	private int dataAccessID = 0;
	/**
	 * D'aquesta manera el ParserHandler guardar� directament
	 * a la BD un cop hagi finalitzat el seu parseig. 
	 */
	private GestorBD nouGestor = GestorBD.getInstance();
		
	/**
	 * Cada una de les organitzacions les guardarem en aquest
	 * Collection. 
	 */	
	private Collection < Root_Item > organizations =
		new ArrayList < Root_Item > ();
		
	/**
	* Encara que tinguem diverses organitzacions totes tindran la
	* mateixa metadata.
	*/
	private MetadataInformation metadata = null;
		
	/**
	 * Com que podem tindre diverses organitzacions tindrem
	 * per cada una un UserObjective. 
	 */	
	private Collection < UserObjective > userObjectives =
		new ArrayList < UserObjective > ();
	

	public String saveToDISK () throws Exception {		
		return nouGestor.writeJavaAbstract_ItemDISK(
				organizations.iterator().next());
	}
	
	/**
	 * Constructor b�sic.
	 * 
	 * @param imsmanifest : String Type.
	 * @param scormIdIext : String Type.
	 */
	public ParserHandler(final String imsmanifest, final String scormIdIext) {
		dataAccessID = ParserIdentifier.getInstance().getNewParserID();
		
		// TODO Falta implementar el ZipHandler
		DOMParser nouParser = new DOMParser(imsmanifest);
		
		metadata = nouParser.getMetadataInformation();
		
		setParserHandler(
				metadata,
				imsmanifest,
				scormIdIext);
			
	}
	/**
	 * Constructor b�sic amb metadata.
	 * 
	 * @param imsmanifest : String Type.
	 * @param scormIdIext : String Type.
	 * @param newMetadata : MetadataInformation Type.
	 */
	public ParserHandler(
			final MetadataInformation newMetadata,
			final String imsmanifest,
			final String scormIdIext) {
		
		setParserHandler(
				newMetadata,
				imsmanifest,
				scormIdIext);
		
	}
	/**
	 * funci� per entrar les dades amb metadata.
	 * 
	 * @param imsmanifest : String Type.
	 * @param scormIdIext : String Type.
	 * @param newMetadata : MetadataInformation Type.
	 */
	public void setParserHandler(
			final MetadataInformation newMetadata,
			final String imsmanifest,
			final String scormIdIext) {
		
		dataAccessID = ParserIdentifier.getInstance().getNewParserID();
		
		// TODO Falta implementar el ZipHandler
		DOMParser nouParser = new DOMParser(imsmanifest);
		
		Integer scormID = null;
			
		LinkedHashMap < Integer, Integer > organitzacioID =
			new LinkedHashMap < Integer, Integer > ();
		LinkedHashMap < Integer, Integer > studentID =
			new LinkedHashMap < Integer, Integer > ();
				
		if (nouParser.getIsParsingCorrect()) {
			/**
			 * Registrem el gestor de MySQL
			 */
			DataAccessRepository.
				getInstance().registerDataAccess(
						nouGestor.getDataAccessID(), nouGestor);
			
			organizations =
				nouParser.getOrganizatons();
			//metadata = nouParser.getMetadataInformation();
			/**
			scormID = nouGestor.insertScorms(
							metadata.identifier, false);
			**/
			try {
				scormID = nouGestor.getScormIdentificationQUERY(
						newMetadata.identifier);
				//scormID = nouGestor.getScormIdentificationQUERY(scormIdIext);
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
			if (scormID == null) {
				/**
				scormID = nouGestor.insertScorms(
						scormIdIext, false);
				**/
				scormID = nouGestor.insertScorms(
						newMetadata.identifier, false);
				
			}
			//nouGestor.get
			
			int contador = 1;
			for (Iterator iteratorOrganiza = organizations.iterator();
			iteratorOrganiza.hasNext();) {
				Root_Item novaOrganitzacio = 
					(Root_Item) iteratorOrganiza.next();
				
				
				
				if (scormID == 0) {
					if (Constants.DEBUG_ERRORS) {
						System.out.println("[ERROR]insertScorms(...)");
					}					
				} else {
					String metadataTitle = newMetadata.title;
					if ((newMetadata.title == null)
							|| newMetadata.title.length() == 0) {
						metadataTitle = newMetadata.identifier;
						
					}
					if (nouGestor.insertMetadata(scormID,
							newMetadata.version,
							metadataTitle, newMetadata.description, 
							newMetadata.requirement, 
							newMetadata.intendedEndUserRole,
							newMetadata.educationalContext,
							newMetadata.author, newMetadata.typicalLearningTime,
							newMetadata.copyright) == 0) {
						if (Constants.DEBUG_ERRORS) {
							System.out.println("[ERROR]insertMetadata(...)");
						}
					}
					organitzacioID.put(contador, 
							nouGestor.insertOrganizations(
							scormID, "", true, 
							novaOrganitzacio.getIdentifier(), 
							novaOrganitzacio.getTitle()));
					
					if (organitzacioID.get(contador) == 0) {
						if (Constants.DEBUG_ERRORS) {
							System.out.println(
									"[ERROR]insertOrganizations(...)");
						}						
					} else {
						/**
						 * No volem serialitzar el DataAccess.
						 */
						novaOrganitzacio.transmitDataAccess(
								null, organitzacioID.get(contador));
						
						if (nouGestor.insertSerialOrganizations(
								organitzacioID.get(contador),
								(Object) novaOrganitzacio) == 0) {
							if (Constants.DEBUG_ERRORS) {
								System.out.println("[ERROR]"
										+ "insertSerialOrganizations(...)");
							}							
						} //if (..insertSerialOrganizations.. == 0)
						
						studentID.put(contador,
								nouGestor.insertStudentsClass(
								organitzacioID.get(contador), "DefaultUSER"));
						if (studentID.get(contador) == 0) {
							if (Constants.DEBUG_ERRORS) {
								System.out.println("[ERROR]"
										+ "insertStudentsClass(...)");
							}							
						} //DEL if (...insertStudentsClass() == 0)
						if (!nouGestor.createCourseStudent(
								"coursestudent_" 
								+ organitzacioID.get(contador).intValue())) {
							if (Constants.DEBUG_ERRORS) {
								System.out.println("[ERROR]"
										+ "createCourseStudent(...)");
							}		
						} //DEL if (!nouGestor.createCourseStudent ...)
					} //DEL if (organitzacioID == 0)
				} //DEL if (scormID == 0)				
				contador++;
			} //DEL for (...iteratorOrganiza.hasNext();
			
			Collection < UserObjective > tmpUserObjectives =
				nouParser.getUserObjective();
			
			contador = 1;
			for (Iterator iteratorUserObjectives = tmpUserObjectives.iterator();
				iteratorUserObjectives.hasNext();) {
				UserObjective tmpUObj =
					(UserObjective) iteratorUserObjectives.next();
							
				tmpUObj.organizationID = organitzacioID.get(contador);
				tmpUObj.dataAccessID = nouGestor.getDataAccessID();
				
				/**
				 * L'hem d'insertar perqu� l'overallSequencingProcess demanar�
				 * dades al getUserDefault(). Per aix� farem aquest "trapi" 
				 * d'insertar-lo i despr�s actualitzar-lo amb les dades del 
				 * seq�enciament correctament insertades.
				 *
				try {
					tmpUObj.adlnav = null;
					nouGestor.insertUserObjective(
							"coursestudent_" 
							+ organitzacioID.get(contador).intValue(),
							studentID.get(contador), (Object) tmpUObj);
				}  catch (Exception e) {
					e.printStackTrace();
				}
				*/
				
				/**
				 * Creem un Course manager per inicialitzar el 
				 * userObjective, per tant simplement l'utilitzarem
				 * per fer correr l'overallSequencingProcess un 
				 * cop i d'aquesta manera que no tingui el 
				 * currentItem == null.
				 */
				CourseManager nouCourseManager =
					CourseAdministrator.getInstance().
						getCourseManagerInstance(
								tmpUObj.dataAccessID, 
								tmpUObj.organizationID);
				
				Leaf_Item firstNode = 
					((Root_Item) nouCourseManager.getOrganizationCluster()).
						firstLeafItem(tmpUObj);
				
				tmpUObj.currentItem = firstNode.getIdentifier();
				tmpUObj.currentHref = 
					firstNode.getResource().getDefaultFile().href
					+ firstNode.getParameters();
				
				
				//nouCourseManager.overallSequencingProcess(tmpUObj);
				
				tmpUObj.adlnav = null;
				/*
				tmpUObj.courseStudentTable = 
					"coursestudent_" + scormID.get(contador).intValue();
				*/
				try {
					tmpUObj.adlnav = null;
					nouGestor.insertUserObjective(
							"coursestudent_" 
							+ organitzacioID.get(contador).intValue(),
							studentID.get(contador), (Object) tmpUObj);
				}  catch (Exception e) {
					e.printStackTrace();
				}
				/*
				try {					
					nouGestor.updateUserObjective("coursestudent_" 
							+ organitzacioID.get(
									contador).intValue(), 1, (Object) tmpUObj);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				*/				
				userObjectives.add(tmpUObj);
				contador++;				
			} //DEL for (...iteratorUserObjectives.hasNext();)
			
			metadata = newMetadata;
			
			/**
			 * TODO : Borrar!!!
			 * Registrem el parserHandler... tot i que no l'utilitzarem.
			 *
			DataAccessRepository.
				getInstance().registerDataAccess(dataAccessID, this);
			*/
		} //DEL if (nouParser.getIsParsingCorrect())
		
	}
	/**
	 * Quan el Garbage Collector lliberi la mem�ria d'aquest objecte
	 * aleshores decrementarem el contador que mant� el ParserIdentifier
	 * perqu� no ens conti.
	 * @throws Throwable :
	 */
	public final void finalize() throws Throwable {
	    try {
	    	ParserIdentifier.getInstance().unregisterParser();
	    } finally {
	        super.finalize();
	    }
	}
	
	/**
	 * Retornarem l'iterador de les organitzacions que hem tingut.
	 * @return Iterator < Root_Item > 
	 */
	public final Iterator < Root_Item > getOrganizationIterator() {
		return organizations.iterator();		
	}
	/**
	 * Retornarem l'iterador dels UserObjective que hem tingut.
	 * @return Iterator < UserObjective > 
	 */
	public final Iterator < UserObjective > getUserObjectivesIterator() {
		return userObjectives.iterator();		
	}
	/**
	 * Retorna el n�mero d'organitzacions que hem tingut.
	 * @return int : size()
	 */
	public final int getNumOfOrganizations() {
		return organizations.size();
	}
	/**
	 * Ens retorna l'organitzaci� segons el seu identificador que
	 * vindr� donat segons l'ordre en el que es trobi. 
	 * 
	 * @param organizationID : int Type. Identificador de l'organitzaci�.
	 * @return Root_Item : Retorna l'organitzaci� que s'ha demanat. 
	 */
	public final Root_Item getOrganization(final long organizationID) {
		Root_Item retOrganization = null;
		Iterator organizationIterator = this.getOrganizationIterator();
		
		if (organizationID < this.getNumOfOrganizations()
				&& organizationID >= 0) {
			for (int i = 0; i <= organizationID; i++) {
				if (organizationIterator.hasNext()) {
					retOrganization = (Root_Item) organizationIterator.next();
				}								
			}
		}		
		return retOrganization;		
	}
	/**
	 * Ens retorna l'UserObjective segons el seu identificador que
	 * vindr� donat segons l'ordre en el que es trobi. 
	 * 
	 * @param organizationID : int Type. Identificador de l'organitzaci�.
	 * @return UserObjective : Retorna l'UserObjective que s'ha demanat. 
	 */
	public final UserObjective getUserObjectives(final long organizationID) {
		UserObjective retUserObjective = null;
		Iterator userObjectiveIterator = this.getUserObjectivesIterator();
		
		if (organizationID <= this.getNumOfOrganizations()
				&& organizationID >= 0) {
			for (int i = 0; i <= organizationID; i++) {
				if (userObjectiveIterator.hasNext()) {
					retUserObjective =
						(UserObjective) userObjectiveIterator.next();
				}								
			}
		}		
		return retUserObjective;		
	}
	
	
	//	------  IMPLEMENTA els m�todes de l'Interface DataAccess  ------
	/**
	 * Ens retornar� algun par�metre descriptiu sobre el m�tode d'acc�s 
	 * a les dades. 
	 * 
	 * @return String Type, optional.
	 */
	public final String getDescription() {		
		return DESCRIPTION;
	}
	
	/**
	 * Retornem l'organitzaci� des del principi amb TOTS
	 * els items carregats en mem�ria. 
	 *
	 * @param identifierID : identificador del curs dintre del dataAccess. 
	 * @return Root_Item : Retorna el primer node que �s el de l'organitzaci�.
	 */
	public final Root_Item getAllTreeOrganization(final long identifierID) {
		return getOrganization(identifierID);
	}	
	/**
	 * Retornem l'organitzaci� des del principi, que no 
	 * vol dir que enviem TOTA l'organitzaci�. Si estem 
	 * parsejant el m�s segur �s que directament retornem tota
	 * l'estructura per� si estem accedint a una BD el m�s segur 
	 * es que no, i molt menys si f�s un altre m�tode. 
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @return Root_Item : Retorna el primer node que �s el de l'organitzaci�.
	 */
	public final Root_Item getFirstOrganization(final long identifierID) {
		return getOrganization(identifierID);
	}
	/**
	 * Retornem l'organitzaci� des del principi, que no 
	 * vol dir que enviem TOTA l'organitzaci�. De fet amb 
	 * el par�metre numLevels obliguem a que ens envii un 
	 * M�NIM de nivells carregats.  
	 *
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @param numLevels : nombre de nivells que volem for�ar a carregar.
	 * @return Root_Item : Retorna el primer node que �s el de l'organitzaci�.
	 */
	public final Root_Item getFirstOrganization(
			final long identifierID,
			final int numLevels) {		
		return getOrganization(identifierID);
	}
	/**
	 * Ens retorna la metadata.
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess. 
	 * @return MetadataInformation
	 */
	public final MetadataInformation getMetadata(final long identifierID) {		
		return metadata;
	}
	/**
	 * Ens retorna l'estructura per defecte d'un usuari per la 
	 * organitzaci� indicada. 
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @return UserObjective : Retornem tot en format UserObjective.
	 */
	public final UserObjective getUserDefaultData(final long identifierID) {
		return getUserObjectives(identifierID);
	}
	/**
	 * Podem tindre tants DataAccess com vulguem i podran conviure tots
	 * en el mateix sistema sense problemes, per� cadasc�n haur� de tindre
	 * un identificador propi i �nic que el diferencii dels altres DataAccess.
	 * Exemples:
	 * 		+ ParserHandler -> 0
	 * 		+ GestorBD 		-> 1
	 * 
	 * @return int Type: L'identificador del DataAccess.
	 */
	public final int getDataAccessID() {
		return dataAccessID;
	}
	/**
	 * Carregar� les dades del node actual i en cas de que aquest
	 * f�s un Cluster li afegiria els seus fills per� sense carregar
	 * les seves dades.
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @param itemIdentifierID : identificador de l'item dintre del dataAccess.
	 * @return Abstract_Item : El nodel que hem demanat que ens omplis.
	 */
	public final Abstract_Item loadTreeNode(
			final long identifierID, 
			final long itemIdentifierID) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Carregar� les dades del node actual i en cas de que aquest
	 * f�s un Cluster tans nivells de fills com ens indiqui 'numLevels'.
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @param itemIdentifierID : identificador de l'item dintre del dataAccess.
	 * @param numLevels : indica el n�mero de nivells que volem carregar.
	 * @return Abstract_Item : El nodel que hem demanat que ens omplis.
	 */
	public final Abstract_Item loadTreeNode(
			final long identifierID,
			final long itemIdentifierID,
			final int numLevels) {
		// TODO Auto-generated method stub
		return null;
	}	
	/**
	 * Ens retorna l'estructura per defecte!!
	 * El ParserHandler no t� m�s usuaris que els per defecte, 
	 * per tant no busquis m�s!
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @param studentID : Identificador de l'usuari dintre del DataAccess.
	 * @return UserObjective : Retornem tot en format UserObjective.
	 */
	public final UserObjective getUserData(
			final long identifierID, final int studentID) {

		return getUserDefaultData(identifierID);
	}
	/**
	 * RETORNA SEMPRE FALSE!! Un parserHandler no guardar� dades que
	 * no siguin les que ha tret de l'xml. 
	 * 
	 * @param identifierID : identificador del curs dintre del dataAccess.
	 * @param studentID : Identificador de l'usuari dintre del DataAccess.
	 * @param userObjective : L'estructura que volem guardar.
	 * @return boolean : True si tot ha anat correcte.
	 */
	public final boolean saveUserData(
			final long identifierID, 
			final int studentID, 
			final UserObjective userObjective) {		
		return false;
	}

}
