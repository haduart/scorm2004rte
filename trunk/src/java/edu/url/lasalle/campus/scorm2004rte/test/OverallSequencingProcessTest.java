// $Id: OverallSequencingProcessTest.java,v 1.23 2008/04/22 18:18:07 ecespedes Exp $
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
package edu.url.lasalle.campus.scorm2004rte.test;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	GestorBD;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.ClusterDataModel;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.ClusterMap;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.TreeAnnotations;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.CourseAdministrator;
import edu.url.lasalle.campus.scorm2004rte.system.CourseManager;
import edu.url.lasalle.campus.scorm2004rte.system.DataAccessRepository;
import edu.url.lasalle.campus.scorm2004rte.system.MemorySheduler;
import edu.url.lasalle.campus.scorm2004rte.system.ParserHandler;

import junit.framework.TestCase;

/**
* $Id: OverallSequencingProcessTest.java,v 1.23 2008/04/22 18:18:07 ecespedes Exp $
* <b>Títol:</b> OverallSequencingProcessTest<br><br>
* <b>Descripció:</b> JUnit Test Class.<br><br> 
*
* @author Eduard Céspedes i Borràs/ Enginyeria La Salle /ecespedes@salle.url.edu
* @version Versió $Revision: 1.23 $ $Date: 2008/04/22 18:18:07 $
* $Log: OverallSequencingProcessTest.java,v $
* Revision 1.23  2008/04/22 18:18:07  ecespedes
* Arreglat bugs en el seqüenciament i els objectius secundaris mapejats.
*
* Revision 1.22  2008/04/02 14:27:27  ecespedes
* Depurant objectives secundaris
*
* Revision 1.21  2008/03/31 13:56:19  ecespedes
* Depurant objectives secundaris
*
* Revision 1.20  2008/02/28 16:15:23  ecespedes
* Millorant el sistema (8)
*
* Revision 1.19  2008/02/25 09:12:58  ecespedes
* *** empty log message ***
*
* Revision 1.18  2008/02/18 15:50:47  ecespedes
* Implementats Threads per als CourseManagers
*
* Revision 1.17  2008/02/18 15:01:28  ecespedes
* Millorant el sistema (7)
*
* Revision 1.16  2008/02/15 12:02:19  ecespedes
* Millorant el sistema (6)
*
* Revision 1.15  2008/02/14 14:54:33  ecespedes
* Millorant el sistema (5)
*
* Revision 1.14  2008/02/12 15:43:22  ecespedes
* Millorant el sistema (4)
*
* Revision 1.13  2008/02/01 03:07:32  ecespedes
* Versió Beta Final amb la BD Serialitzada.
*
* Revision 1.12  2008/01/31 14:23:37  ecespedes
* Millorant el sistema.
*
* Revision 1.11  2008/01/30 19:35:08  ecespedes
* BUG Arreglat.
*
* Revision 1.10  2008/01/30 17:41:15  ecespedes
* Versió final: organització  serialitzada.
*
* Revision 1.9  2008/01/29 18:01:41  ecespedes
* Implementant versió serialitzada de l'organització.
*
* Revision 1.8  2008/01/24 15:41:11  ecespedes
* Dissenyant un nou sistema per gestionar les Bases de dades (DataAccess)
*
* Revision 1.7  2008/01/18 18:07:24  ecespedes
* Serialitzades TOTES les classes per tal de que els altres puguin fer proves
* en paral·lel amb el procés de desenvolupament del gestor de BD.
*
* Revision 1.6  2008/01/10 06:58:29  ecespedes
* Últimes modificacions de l'overallSequencingProcess.
*
* Revision 1.5  2008/01/08 17:42:46  ecespedes
* Actualitzant el model de dades a partir del seqüenciament.
*
* Revision 1.4  2008/01/08 11:50:21  ecespedes
* Ultimes modificacions de l'OverallSequencingProcess
*
* Revision 1.3  2008/01/07 15:59:21  ecespedes
* Implementat 'deliveryControls' i el 'hideLMSUI' del Presentation.
*
* Revision 1.2  2008/01/04 11:35:32  ecespedes
* Implementant l'OverallSequencingProcessTest sobre el linear_Choice.
*
* Revision 1.1  2008/01/03 15:05:32  ecespedes
* Arreglats elements del CMIDataModel i començant a crear un nou test.
*
* 
*/
public class OverallSequencingProcessTest extends TestCase {
	/**
	 * És per no fer una relació amb el SequencingStatus.Unknown.
	 * int Type = 0
	 */
	private static final int UNKNOWN = 0;
	/**
	 * És per no fer una relació amb el SequencingStatus.Passed.
	 * int Type = 1
	 */
	private static final int PASSED = 1;
	/**
	 * És per no fer una relació amb el SequencingStatus.Failed.
	 * int Type = 2
	 */
	private static final int FAILED = 2;
	/**
	 * Aquest serà el valor inicial o en cas d'error s'assignarà 
	 * aquest valor a les variables. De manera que el seqüenciament
	 * quan rebi un -1 el que farà serà fer un rollup i actualitzar
	 * aquesta variable. 
	 * 
	 * int Type = -1
	 */
	private static final int NULL = -1;
	/**
	 * Amb aquesta variable controlarem els testos dels cursos.
	 * Més que res per no fer testos sobre cursos pels quals no està
	 * preparat!!!
	 */
	private String currentCourseManifest = ARGS6;
	/**
	 * TODO: Paràmetres!!
	 * D'aquesta manera indicarem el curs que volem llegir!
	 */
	private static final int ORGANITACIO_ACTUAL = 5;
	/**
	 * TODO: Indicarem si s'ha de llegir o no.
	 */
	private static final boolean WRITE_TO_DB = true;
	/**
	 * TODO: Indicarem si s'ha d'escriure l'usuari o no.
	 */
	private static final boolean WRITE_USER_TO_DB = false;
	/**
	 * private Constant String Type.
	 * Té un seqüenciament linial modificat perquè tingui una mica més
	 * de gràcia.
	 * 
	 * Photoshop_Linear_Choice
	 */
	private static final String ARGS4 = 
		"D:\\SCORM\\SCORM2004_Examples_Photoshop_1_1\\" 
		+ "Photoshop_Linear_Choice\\imsmanifest.xml";
	/**
	 * private Constant String Type.
	 * Té un seqüenciament linial modificat perquè tingui una mica més
	 * de gràcia.
	 * 
	 * Photoshop_Linear_SEQ-MODIFIED20071119
	 */
	private static final String ARGS3 = 
		"D:\\SCORM\\SCORM2004_Examples_Photoshop_1_1\\" 
		+ "Photoshop_Linear_SEQ-MODIFIED20071119\\imsmanifest.xml";
	/**
	 * private Constant String Type.
	 * Té un seqüenciament linial modificat perquè tingui una mica més
	 * de gràcia.
	 * 
	 * Photoshop_Linear
	 */
	private static final String ARGS5 = 
		"D:\\SCORM\\SCORM2004_Examples_Photoshop_1_1\\" 
		+ "Photoshop_Linear\\imsmanifest.xml";
		
	/**
	 * private Constant String Type.
	 * Té un seqüenciament linial modificat perquè tingui una mica més
	 * de gràcia.
	 * 
	 * Photoshop_Competency
	 */
	private static final String ARGS6 =
		"D:\\SCORM\\SCORM2004_Examples_Photoshop_1_1\\" 
		+ "Photoshop_Competency\\imsmanifest.xml";		
	
		
	/**
	 * private Constant String Type.
	 * Té un seqüenciament linial modificat perquè tingui una mica més
	 * de gràcia.
	 * 
	 * Photoshop_Constrained_Choice
	 */
	private static final String ARGS7 =
		"D:\\SCORM\\SCORM2004_Examples_Photoshop_1_1\\" 
		+ "Photoshop_Constrained_Choice\\imsmanifest.xml";
	
	/**
	 * private Constant String Type.
	 * Té un seqüenciament linial modificat perquè tingui una mica més
	 * de gràcia.
	 * 
	 * Photoshop_Remediation
	 */
	private static final String ARGS8 =
		"D:\\SCORM\\SCORM2004_Examples_Photoshop_1_1\\" 
		+ "Photoshop_Remediation\\imsmanifest.xml";
		
	/**
	 * nouHandler serà l'instància del parsejador. 
	 */
	private ParserHandler nouHandler;
	/**
	 * nouHandler serà l'instància que utilitzarem en els testos. 
	 */
	private CourseManager cManager;
	/**
	 * cUseObject contindrà les dades de l'usuari amb el que treballarem.
	 */
	private UserObjective cUseObject = null;
	/**
	 * Date Type.
	 */
	private Date dataInicial = null;
	
	/**
	 * Configura el ParserHandler i el CourseManager.
	 * @throws Exception :
	 */
	protected void setUp() throws Exception {
		dataInicial = new Date();
		Random rand = new Random();
		
		if (WRITE_TO_DB) {
			nouHandler = new ParserHandler(
					currentCourseManifest,
					"JUnitTestDefaultName" + rand.nextInt(1000));
		}		
	}
	
	protected void tearDown() throws Exception {
		Date dataFinal = new Date(); 
		long elapsedTime = dataFinal.getTime() 
		- dataInicial.getTime();
		System.out.println("Total time elapsed: " + elapsedTime
				+ " ms");		
	}

	/**
	 * Serà el test principal des d'on ho farem totes les crides.
	 */
	public final void testCourseManager() {
		System.out.println(
				"\n\n\tpublic final void testCourseManager()");
		printMemoryStatus();						
				
		Hashtable < Integer, CourseManager > courseManagers =
			new Hashtable < Integer, CourseManager > ();
				
		/**
		 * Utilitzarem la BD per agafar un userObjective concret.
		 */
		GestorBD nouGestor = GestorBD.getInstance();
		/**	
		 * 	Registrem el gestor de MySQL 
		 */		
		DataAccessRepository.
			getInstance().registerDataAccess(
					nouGestor.getDataAccessID(), nouGestor);
		
		/**
		for (int i = 0; i < nouHandler.getNumOfOrganizations(); i++) {
		**/
			/**
			 * Utilitzarem la BD per agafar un userObjective concret.
			 */
			/**
			GestorBD nouGestor = GestorBD.getInstance();
			**/
			CourseManager nouCourseManager = null;
			try {
				/**
				 * D'aquesta manera ens ho dóna el ParserHandler.
				 */
				cUseObject = CourseAdministrator.getInstance().
					getUserObjective(nouGestor.getDataAccessID(),
							ORGANITACIO_ACTUAL);
				/**/
				/**
				 * Amb les dades que teniem del UserObjective li demanem 
				 * el CourseManager corresponent al CourseAdministrator.
				 */
				nouCourseManager =
					CourseAdministrator.getInstance().
						getCourseManagerInstance(
								cUseObject.dataAccessID, 
								cUseObject.organizationID);
				
				/**
				 * L'arrancarem en el CourseAdministrator.
				 */
				//MemorySheduler.getInstance().start();
				MemorySheduler.getInstance().setDelay(600000);
				//MemorySheduler.getInstance().addObserver(nouCourseManager);
				
				
				//cUseObject = nouCourseManager.getUser("ecespedes");
				
				/**/
				/**
				 * TODO: SGBD-> { Save | Read }
				 * Agafem un UserObjective que ja tenim creat a la BD.
				 */
				//cUseObject = nouGestor.getUserData(1, 1);
				/*
				cUseObject = nouGestor.getUserDefaultData(
						cUseObject.organizationID);
				*/
				cUseObject = nouCourseManager.getUserDefault();
				
				//entryUserObjectiveTestData(cUseObject, nouCourseManager);
				/**
				  nouGestor.saveUserData(1, 1, cUseObject);
				**/		
				System.out.println("[OK] Loaded From DataBase-> " 
						+ cUseObject.organizationName);
				/**/
			} catch (Exception e) {
				e.printStackTrace();
			}			
			/**
			System.out.println("[" + i + "]Users[1]:" 
					+ nouCourseManager.getCurrentNumberUsers());
			
			assertNotNull(nouCourseManager);
					
			courseManagers.put(i, nouCourseManager);
			**/
		/**
		} //DEL for (int i = 0; i < nouHandler.getNumOfOrganizations(); i++)
		**/
		//Comença la part dels testos...
		/**
		cManager = courseManagers.get(0);		
		**/
		cManager = nouCourseManager;
		
		try {
			/**
			 //Torna a ficar com a privada!!!
			cManager.getIdentifier();
			cManager.getMetadata();			
			nouGestor.writeJavaCouseManager_ItemDISK(cManager);
			**/
			/**
			cManager.getOrganizationCluster();
			**/		
			/**
			String nom = nouHandler.saveToDISK();
			**/
			//cManager = new CourseManager((DataAccess) nouGestor, 0);
			/**
			cManager = 
				(CourseManager) nouGestor.readJavaAbstract_ItemDISK(
				"CourseManager-191230");
			**/
			/**
			cManager.organizationCluster = null;
			**/
			/**
			cManager.organizationCluster = 
				(Root_Item) nouGestor.readJavaAbstract_ItemDISK(nom);
			**/
			/**
			cManager.organizationCluster = 
				(Root_Item) nouGestor.readJavaAbstract_ItemDISK(
						"AbstractItem-191230");
			cUseObject = 
				(UserObjective) nouGestor.readJavaAbstract_ItemDISK(
				"UserObjective-191230");
			
			cManager.getOrganizationCluster();
			**/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		printTreeAnnotations(cUseObject);
		 
		System.out.println("\t\t------------ Start Test ------------");
		/**
		System.out.println("\tCourseManager Information:\n\t"
				+ "\t[" + cManager.getIdentifier() + "]\t"
				+ cManager.getMetadata().identifier + "\t" 
				+ cManager.getOrganizationCluster().getIdentifier());
		**/
		/**
		 * TODO: TEST 1
		 * Farem el mateix que en el test 7 l'únic que cridarem
		 * a la funció del CourseManager directament.
		 * 
		 *  L'Item serà el QUESTION9A.
		 */
		if (!currentCourseManifest.equals(ARGS6)) {
			System.out.println("\n################## TEST 1 ##################" 
					+ "\nPrimer accés");
			
			if (!test1()) {
				System.out.println("\tTEST 1 -> FAILED");			
			} else {
				
				System.out.println("\tTEST 1 -> PASSED");
			}			
		}	
		
		if (currentCourseManifest.equals(ARGS6)) {
			System.out.println("\n################## " 
					+ "TEST for Photoshop_Competency ##################" 
					+ "\n");
			
			if (!test2()) {
				System.out.println("\tTEST 1 -> FAILED");			
			} else {
				
				System.out.println("\tTEST 1 -> PASSED");
			}
			
		}
		
		System.out.println("\t\t------------ End Test ------------");
		//Finalitza la part dels testos...
		
		System.out.println("END-testParserHandler()");		
		
		System.out.println("Intentem guanyar memòria...");
		cManager.removeAllTreeOrganization();
		printMemoryStatus();
		
	}	
	/**
	 * L'Item serà el QUESTION9A.
	 * @return boolean : True si funciona, False si falla
	 */
	private boolean test1() {
		
		//System.out.println("\n\t####### STEP 1 #######");
		//System.out.println("->currentItem=NULL");
		//System.out.println("->adlnav.request=NULL");
		//String href = cManager.overallSequencingProcess(cUseObject);
				
		
		String href = cUseObject.currentHref;
		
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		
		int complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		
		double percentatge = (((double)(complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		
		System.out.println("\n\t####### STEP 2 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * INTRO
		 */
		cUseObject.dataModel.get("INTRO").firstAttemptedTime = null;
		cUseObject.dataModel.get("INTRO").activityAttemptCount = 0;
		cUseObject.dataModel.get("INTRO").totalTime = "PT3M";
		cUseObject.dataModel.get("INTRO").completionStatus = "completed";
		//cUseObject.dataModel.get("INTRO").successStatus = "passed";
		//cUseObject.dataModel.get("INTRO").scoreScaled = "0.75";
		cUseObject.dataModel.get("INTRO").lastAttemptedTime = null;
		cUseObject.treeAnnotations.get("INTRO").isAccessed = false;
				
		cUseObject.adlnav.request = "continue";
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
				
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		
		/**
		 * Esperem a fer ni que sigui una sola dada per crear l'usuari.
		 */
		if (!currentCourseManifest.equals(ARGS7)) {		
			System.out.println("\n[ALERT]Aquest test no està " 
					+ "preparat per al curs:\n\t"
					+ currentCourseManifest);
			if (WRITE_USER_TO_DB) {
				System.out.println("Tot i així guardarem el nou usuari; 'agid2'"
						+ "\n\n");
				cUseObject.userOSIDIdentifier = "agid2";
				cUseObject.adlnav.request = "exit";
				String tmphref = null;
				try {
					tmphref = cManager.overallSequencingProcess(cUseObject);
				} catch (Exception e) {
					e.printStackTrace();				
				}
				return tmphref != null;
			} else {
				System.out.println("\n");
				return false;
			} //DEL if (WRITE_USER_TO_DB)
		} //DEL if (!currentCourseManifest.equals(ARGS4)) 
		
		
		
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		
		percentatge = (((double)(complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		
		System.out.println("\n\t####### STEP 3 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * LESSON1
		 */
		cUseObject.dataModel.get("LESSON1").firstAttemptedTime = null;
		cUseObject.dataModel.get("LESSON1").activityAttemptCount = 0;
		cUseObject.dataModel.get("LESSON1").totalTime = "PT3M";
		
		cUseObject.dataModel.get("LESSON1").completionStatus = "completed";
		//cUseObject.dataModel.get("INTRO").successStatus = "passed";
		//cUseObject.dataModel.get("INTRO").scoreScaled = "0.75";
		cUseObject.dataModel.get("LESSON1").lastAttemptedTime = null;
		cUseObject.treeAnnotations.get("LESSON1").isAccessed = false;
				
		cUseObject.adlnav.request = "continue";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		//printTreeAnnotations(cUseObject);
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		percentatge = (((double) (complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		
		System.out.println("\n\t####### STEP 4 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * LESSON2
		 */
		cUseObject.dataModel.get("LESSON2").firstAttemptedTime = null;
		cUseObject.dataModel.get("LESSON2").activityAttemptCount = 0;
		cUseObject.dataModel.get("LESSON2").totalTime = "PT3M";
		cUseObject.dataModel.get("LESSON2").completionStatus = "completed";
		cUseObject.dataModel.get("LESSON2").lastAttemptedTime = null;
		cUseObject.treeAnnotations.get("LESSON2").isAccessed = false;
				
		cUseObject.adlnav.request = "previous";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		//printTreeAnnotations(cUseObject);
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		percentatge = (((double) (complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		System.out.println("\n\t####### STEP 5 #######");
		/**
		 * No cal que modifiquem les dades per tindre un estat 
		 * conegut del LESSON1
		 */
		cUseObject.adlnav.request = "{target=LESSON4}choice";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		//printTreeAnnotations(cUseObject);
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		percentatge = (((double) (complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		System.out.println("\n\t####### STEP 6 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * LESSON4
		 */
		cUseObject.dataModel.get("LESSON4").firstAttemptedTime = null;
		cUseObject.dataModel.get("LESSON4").activityAttemptCount = 0;
		cUseObject.dataModel.get("LESSON4").totalTime = "PT3M";		
		cUseObject.dataModel.get("LESSON4").lastAttemptedTime = null;
		cUseObject.dataModel.get("LESSON4").completionStatus = "completed";
		cUseObject.treeAnnotations.get("LESSON4").isAccessed = false;
				
		cUseObject.adlnav.request = "continue";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		//printTreeAnnotations(cUseObject);
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		percentatge = (((double) (complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		
		System.out.println("\n\t####### STEP 7 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * QUESTION1
		 */
		cUseObject.dataModel.get("QUESTION1").firstAttemptedTime = null;
		cUseObject.dataModel.get("QUESTION1").activityAttemptCount = 0;
		cUseObject.dataModel.get("QUESTION1").totalTime = "PT3M";		
		cUseObject.dataModel.get("QUESTION1").lastAttemptedTime = null;
		cUseObject.treeAnnotations.get("QUESTION1").isAccessed = false;
		cUseObject.dataModel.get("QUESTION1").completionStatus = "completed";
		cUseObject.dataModel.get("QUESTION1").successStatus = "passed";
		cUseObject.dataModel.get("QUESTION1").scoreScaled = "0.75";
				
		cUseObject.adlnav.request = "continue";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		printClusterElements(cUseObject.clusterTree);
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		percentatge = (((double) (complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		
		System.out.println("\n\t####### STEP 8 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * QUESTION2
		 */
		cUseObject.dataModel.get("QUESTION2").firstAttemptedTime = null;
		cUseObject.dataModel.get("QUESTION2").activityAttemptCount = 0;
		cUseObject.dataModel.get("QUESTION2").totalTime = "PT3M";		
		cUseObject.dataModel.get("QUESTION2").lastAttemptedTime = null;
		cUseObject.treeAnnotations.get("QUESTION2").isAccessed = false;
		cUseObject.dataModel.get("QUESTION2").completionStatus = "completed";
		cUseObject.dataModel.get("QUESTION2").successStatus = "passed";
		cUseObject.dataModel.get("QUESTION2").scoreScaled = "0.75";
				
		cUseObject.adlnav.request = "continue";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		printClusterElements(cUseObject.clusterTree);
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		percentatge = (((double) (complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		
		System.out.println("\n\t####### STEP 9 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * QUESTION3
		 */
		cUseObject.dataModel.get("QUESTION3").firstAttemptedTime = null;
		cUseObject.dataModel.get("QUESTION3").activityAttemptCount = 0;
		cUseObject.dataModel.get("QUESTION3").totalTime = "PT3M";		
		cUseObject.dataModel.get("QUESTION3").lastAttemptedTime = null;
		cUseObject.treeAnnotations.get("QUESTION3").isAccessed = false;
		cUseObject.dataModel.get("QUESTION3").completionStatus = "completed";
		cUseObject.dataModel.get("QUESTION3").successStatus = "passed";
		cUseObject.dataModel.get("QUESTION3").scoreScaled = "0.75";
				
		cUseObject.adlnav.request = "continue";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		printClusterElements(cUseObject.clusterTree);
		
		System.out.println("\n\t####### STEP 10 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * EXIT
		 */
		if (WRITE_USER_TO_DB) {
			cUseObject.userOSIDIdentifier = "agid2";
			cUseObject.adlnav.request = "exit";
		} else {
			cUseObject.adlnav.request = "continue";			
		}
		
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		return true;
	}		
	
	/**
	 * Per provar el Competency!
	 * @return
	 */
	private boolean test2() {
		
		//System.out.println("\n\t####### STEP 1 #######");
		//System.out.println("->currentItem=NULL");
		//System.out.println("->adlnav.request=NULL");
		//String href = cManager.overallSequencingProcess(cUseObject);
				
		
		String href = cUseObject.currentHref;
		
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		
		int complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		
		double percentatge = (((double)(complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		
		System.out.println("\n\t####### STEP 2 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * INTRO
		 */
		cUseObject.dataModel.get("INTRO").firstAttemptedTime = null;
		cUseObject.dataModel.get("INTRO").activityAttemptCount = 0;
		cUseObject.dataModel.get("INTRO").totalTime = "PT3M";
		cUseObject.dataModel.get("INTRO").completionStatus = "completed";
		//cUseObject.dataModel.get("INTRO").successStatus = "passed";
		//cUseObject.dataModel.get("INTRO").scoreScaled = "0.75";
		cUseObject.dataModel.get("INTRO").lastAttemptedTime = null;
		cUseObject.treeAnnotations.get("INTRO").isAccessed = false;
				
		cUseObject.adlnav.request = "continue";
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
				
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
				
		
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		
		percentatge = (((double)(complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		
		System.out.println("\n\t####### STEP 3 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * LESSON1
		 */
		cUseObject.dataModel.get("PRETEST").firstAttemptedTime = null;
		cUseObject.dataModel.get("PRETEST").activityAttemptCount = 0;
		cUseObject.dataModel.get("PRETEST").totalTime = "PT3M";
		
		cUseObject.dataModel.get("PRETEST").completionStatus = "completed";
		cUseObject.dataModel.get("PRETEST").successStatus = "passed";
		//cUseObject.dataModel.get("PRETEST").scoreScaled = "0.75";
		cUseObject.dataModel.get("PRETEST").scoreRaw = "6";
		cUseObject.dataModel.get("PRETEST").lastAttemptedTime = null;
		cUseObject.treeAnnotations.get("PRETEST").isAccessed = false;

		//##	Objectives Secundaris ##
		cUseObject.dataModel.get("PRETEST").cmiObjectives.get("PRIMARYOBJ").completionStatus = "completed";
		cUseObject.dataModel.get("PRETEST").cmiObjectives.get("PRIMARYOBJ").successStatus = "passed";
		//cUseObject.dataModel.get("PRETEST").scoreScaled = "0.75";
		cUseObject.dataModel.get("PRETEST").cmiObjectives.get("PRIMARYOBJ").scoreRaw = "6";
		
		cUseObject.dataModel.get("PRETEST").cmiObjectives.get("obj_module_1").completionStatus = "completed";
		cUseObject.dataModel.get("PRETEST").cmiObjectives.get("obj_module_1").successStatus = "passed";
		//cUseObject.dataModel.get("PRETEST").scoreScaled = "0.75";
		cUseObject.dataModel.get("PRETEST").cmiObjectives.get("obj_module_1").scoreRaw = "6";
		
		cUseObject.dataModel.get("PRETEST").cmiObjectives.get("obj_module_2").completionStatus = "completed";
		cUseObject.dataModel.get("PRETEST").cmiObjectives.get("obj_module_2").successStatus = "passed";
		//cUseObject.dataModel.get("PRETEST").scoreScaled = "0.75";
		cUseObject.dataModel.get("PRETEST").cmiObjectives.get("obj_module_2").scoreRaw = "6";
		
		cUseObject.dataModel.get("PRETEST").cmiObjectives.get("obj_module_3").completionStatus = "completed";
		cUseObject.dataModel.get("PRETEST").cmiObjectives.get("obj_module_3").successStatus = "failed";
		//cUseObject.dataModel.get("PRETEST").scoreScaled = "0.75";
		cUseObject.dataModel.get("PRETEST").cmiObjectives.get("obj_module_3").scoreRaw = "2";
		
		
		cUseObject.adlnav.request = "continue";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		//printTreeAnnotations(cUseObject);
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		percentatge = (((double) (complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		
		System.out.println("\n\t####### STEP 4 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * LESSON2
		 */
		cUseObject.dataModel.get("LESSON2").firstAttemptedTime = null;
		cUseObject.dataModel.get("LESSON2").activityAttemptCount = 0;
		cUseObject.dataModel.get("LESSON2").totalTime = "PT3M";
		cUseObject.dataModel.get("LESSON2").completionStatus = "completed";
		cUseObject.dataModel.get("LESSON2").lastAttemptedTime = null;
		cUseObject.treeAnnotations.get("LESSON2").isAccessed = false;
				
		cUseObject.adlnav.request = "previous";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		//printTreeAnnotations(cUseObject);
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		percentatge = (((double) (complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		System.out.println("\n\t####### STEP 5 #######");
		/**
		 * No cal que modifiquem les dades per tindre un estat 
		 * conegut del LESSON1
		 */
		cUseObject.adlnav.request = "{target=LESSON4}choice";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		//printTreeAnnotations(cUseObject);
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		percentatge = (((double) (complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		System.out.println("\n\t####### STEP 6 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * LESSON4
		 */
		cUseObject.dataModel.get("LESSON4").firstAttemptedTime = null;
		cUseObject.dataModel.get("LESSON4").activityAttemptCount = 0;
		cUseObject.dataModel.get("LESSON4").totalTime = "PT3M";		
		cUseObject.dataModel.get("LESSON4").lastAttemptedTime = null;
		cUseObject.dataModel.get("LESSON4").completionStatus = "completed";
		cUseObject.treeAnnotations.get("LESSON4").isAccessed = false;
				
		cUseObject.adlnav.request = "continue";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		//printTreeAnnotations(cUseObject);
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		percentatge = (((double) (complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		
		System.out.println("\n\t####### STEP 7 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * QUESTION1
		 */
		cUseObject.dataModel.get("QUESTION1").firstAttemptedTime = null;
		cUseObject.dataModel.get("QUESTION1").activityAttemptCount = 0;
		cUseObject.dataModel.get("QUESTION1").totalTime = "PT3M";		
		cUseObject.dataModel.get("QUESTION1").lastAttemptedTime = null;
		cUseObject.treeAnnotations.get("QUESTION1").isAccessed = false;
		cUseObject.dataModel.get("QUESTION1").completionStatus = "completed";
		cUseObject.dataModel.get("QUESTION1").successStatus = "passed";
		cUseObject.dataModel.get("QUESTION1").scoreScaled = "0.75";
				
		cUseObject.adlnav.request = "continue";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		printClusterElements(cUseObject.clusterTree);
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		percentatge = (((double) (complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		
		System.out.println("\n\t####### STEP 8 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * QUESTION2
		 */
		cUseObject.dataModel.get("QUESTION2").firstAttemptedTime = null;
		cUseObject.dataModel.get("QUESTION2").activityAttemptCount = 0;
		cUseObject.dataModel.get("QUESTION2").totalTime = "PT3M";		
		cUseObject.dataModel.get("QUESTION2").lastAttemptedTime = null;
		cUseObject.treeAnnotations.get("QUESTION2").isAccessed = false;
		cUseObject.dataModel.get("QUESTION2").completionStatus = "completed";
		cUseObject.dataModel.get("QUESTION2").successStatus = "passed";
		cUseObject.dataModel.get("QUESTION2").scoreScaled = "0.75";
				
		cUseObject.adlnav.request = "continue";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		printClusterElements(cUseObject.clusterTree);
		complets = (cUseObject.dataModel.size() 
				- cUseObject.remainInComplete);
		percentatge = (((double) (complets * 100)) / cUseObject.dataModel.size());
		System.out.println("Items Completats: " +  complets
				+ "\tPercentatge: " + percentatge);
		
		System.out.println("\n\t####### STEP 9 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * QUESTION3
		 */
		cUseObject.dataModel.get("QUESTION3").firstAttemptedTime = null;
		cUseObject.dataModel.get("QUESTION3").activityAttemptCount = 0;
		cUseObject.dataModel.get("QUESTION3").totalTime = "PT3M";		
		cUseObject.dataModel.get("QUESTION3").lastAttemptedTime = null;
		cUseObject.treeAnnotations.get("QUESTION3").isAccessed = false;
		cUseObject.dataModel.get("QUESTION3").completionStatus = "completed";
		cUseObject.dataModel.get("QUESTION3").successStatus = "passed";
		cUseObject.dataModel.get("QUESTION3").scoreScaled = "0.75";
				
		cUseObject.adlnav.request = "continue";
				
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		if (href == null) {
			return false;
		} else {
			System.out.println("\tHref: " + href);
		}
		System.out.println("Botonera: Continue(" 
				+ cUseObject.adlnav.choiceContinue + "-" 
				+ cUseObject.adlnav.requestValidContinue + ")\tPrevious("
				+ cUseObject.adlnav.choicePrevious + "-" 
				+ cUseObject.adlnav.requestValidPrevious + ")\tExit("
				+ cUseObject.adlnav.choiceExit + ")\tShowTree("
				+ cUseObject.adlnav.showTreeChoice + ")");
		printClusterElements(cUseObject.clusterTree);
		
		System.out.println("\n\t####### STEP 10 #######");
		/**
		 * Modifiquem les dades per tindre un estat conegut del
		 * EXIT
		 */
		if (WRITE_USER_TO_DB) {
			cUseObject.userOSIDIdentifier = "agid2";
			cUseObject.adlnav.request = "exit";
		} else {
			cUseObject.adlnav.request = "continue";			
		}
		
		System.out.println("->currentItem=" + cUseObject.currentItem);
		System.out.println("->currentHref=" + cUseObject.currentHref);
		System.out.println("->adlnav.request= '" 
				+ cUseObject.adlnav.request + "'");
		
		href = cManager.overallSequencingProcess(cUseObject);
		return true;
	}		
	/**
	
	/**
	 * Funció que només serveix per printar el TreeAnnotations.
	 * 
	 * @param usObj : UserObjective Type
	 */
	private void printTreeAnnotations(final UserObjective usObj) {
		System.out.println("\t--------- TreeAnnotations [" 
				+ usObj.treeAnnotations.size() + "] ---------");
		/*
		printItem(usObj, 
				usObj.treeAnnotations.keySet().iterator().next(),
				"" , 1, 1);
		*/
		for (Iterator tAIterator = usObj.treeAnnotations.keySet().iterator();
			tAIterator.hasNext();) {
			
			String keyString = (String) tAIterator.next();
			TreeAnnotations current = usObj.treeAnnotations.get(keyString);
			
			System.out.println("[[" + current.itemID + "] " 
					+ "currentView: " 
					+ current.currentView.toString()
					//+ " invariantItem: " + current.invariantItem 
					+ " isAccessed: " + current.isAccessed 
					+ " sons: " + current.sons.toString()					
					+ "' ]");
					//+ "\n->\tTitle: '" + current.title + "'");
		}
	}
	/**
	 * Demostrar que és pot printar de forma recursiva per aconseguir
	 * un marge exacte depenent del nivell i la posició. 
	 * 
	 * @param usObj : UserObjective
	 * @param id : String Type
	 * @param mostraNumeracio : String Type
	 * @param count : int Type
	 * @param level : int Type
	 */
	private void printItem(final UserObjective usObj,
			final String id,
			final String mostraNumeracio,
			final int count,
			final int level) {
		
		int nouCont = count;
		int nouLevel = level;
		System.out.print("\n");
		for (int i = 1; i <= level; i++) {
			System.out.print("\t");
		}
		System.out.print(mostraNumeracio + ".");
		System.out.print(count);	
		
		System.out.print("[[" + usObj.treeAnnotations.get(id).itemID + "] " 
				+ "currentView: " 
				+ usObj.treeAnnotations.get(id).currentView.toString()
				+ " isAccessed: " + usObj.treeAnnotations.get(id).isAccessed 
				+ " sons: " + usObj.treeAnnotations.get(id).sons.toString()
				+ "' ]");
		nouLevel++;
		nouCont = 0;
		if (usObj.treeAnnotations.get(id).sons.size() > 0) {
			for (Iterator tAIterator =
				usObj.treeAnnotations.get(id).sons.iterator();
				tAIterator.hasNext();) {
				String nou = (String) tAIterator.next();
				printItem(usObj, nou, mostraNumeracio + "." + count,
						++nouCont, nouLevel);
			}
		}							
	}
	/**
	 * Ens printarà la memòria total i la que ens queda disponible.
	 *
	 */
	private void printMemoryStatus() {
		System.out.println("[Total Memory: "
				+ Runtime.getRuntime().totalMemory()
				+ "] [Free Memory: "				
				+ Runtime.getRuntime().freeMemory()
				+ "] [Max Memory: "				
				+ Runtime.getRuntime().maxMemory()
				+ "] [Num CPU: "				
				+ Runtime.getRuntime().availableProcessors()
				+ "]");
	}
	/**
	 * Printem i fem una copia de la HashTable!!!
	 * 
	 * @param currentHashMap : És la taula temporal que volem copiar. 
	 */
	private void printClusterElements(
			final Hashtable < String, ClusterMap > currentHashMap) {
		
		/**
		 * Printem els CMI* i Copiem!!!!
		 */
		if (currentHashMap != null) {
			
			Enumeration < String > claus = 
				currentHashMap.keys();
			Enumeration < ClusterMap > elements = 
				currentHashMap.elements();
			
			System.out.println("\t##### ClusterTree #####");
			
			while (claus.hasMoreElements()) {
				
				String novaClau = claus.nextElement();
				ClusterMap nouElement = elements.nextElement();
				
				System.out.println("[" + novaClau + "] ->"
						+ nouElement.objective.size());				
				
				Enumeration < String > clausObjective =
					nouElement.objective.keys();
				Iterator < ClusterDataModel > clusterDMobjective =
					nouElement.objective.values().iterator();
				
				while (clausObjective.hasMoreElements()) {
					String noClau = clausObjective.nextElement();
					ClusterDataModel nDM =
						clusterDMobjective.next();
					System.out.println("\t->" 
							+ noClau + "\n"
							+ "\tattempted: '"
							+ convertIntStr(nDM.attempted)
							+ "'\tcompleted: '"
							+ convertIntStr(nDM.completed)
							+ "'\tsatisfied: '"
							+ convertIntStr(nDM.satisfied)
							+ "'\tMeasure: '"
							+ nDM.objectiveMeasure
							+ "'");
				}
			}
		}		
	}
	/**
	 * Printem els Objectives Map que tenim en el sistema (que són els
	 * objectes globals).
	 * 
	 * @param nouElement : ClusterMap Type
	 */
	private void printGlobalElements(
			final ClusterMap nouElement) {
				
		System.out.println("####### Global Objectives ("
				+ nouElement.objective.size() + ") #######");		
		
		Enumeration < String > clausObjective =
			nouElement.objective.keys();
		Iterator < ClusterDataModel > clusterDMobjective =
			nouElement.objective.values().iterator();
		
		while (clausObjective.hasMoreElements()) {
			String noClau = clausObjective.nextElement();
			ClusterDataModel nDM =
				clusterDMobjective.next();
			System.out.println("\t->" 
					+ noClau + "\n"
					+ "'\tsatisfied: '"
					+ convertIntStr(nDM.satisfied)
					+ "'\tMeasure: '"
					+ nDM.objectiveMeasure
					+ "'");
		}		
			
	}
	/**
	 * Fem una conversió del valor numéric al seu equivalent
	 * d'String perquè sigui més entenedor a l'hora de printar-ho.
	 * 
	 * @param number : int Type
	 * @return String Type
	 */
	private String convertIntStr(final int number) {
		switch (number) {
		case NULL:
			return "NULL";
		case PASSED:
			return "PASSED";
		case FAILED:
			return "FAILED";
		case UNKNOWN:
			return "UNKNOWN";
		default :
			return "[ERROR]!";			
		}
	}
	
	
}
