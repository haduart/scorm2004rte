// $Id: ParserHandlerTest.java,v 1.3 2008/01/03 15:05:32 ecespedes Exp $
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

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;

import edu.url.lasalle.campus.scorm2004rte.server.DataBase.GestorBD;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.TreeAnnotations;
import edu.url.lasalle.campus.scorm2004rte.server.
	DataBase.DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.CourseAdministrator;
import edu.url.lasalle.campus.scorm2004rte.system.CourseManager;
import edu.url.lasalle.campus.scorm2004rte.system.DataAccess;
import edu.url.lasalle.campus.scorm2004rte.system.DataAccessRepository;
import edu.url.lasalle.campus.scorm2004rte.system.ParserHandler;

import junit.framework.TestCase;


/**
 * $Id: ParserHandlerTest.java,v 1.3 2008/01/03 15:05:32 ecespedes Exp $
 * Descripció: JUnit Test Class.
 * 
 * @author Eduard Céspedes i Borràs /Enginyeria La Salle/ecespedes@salle.url.edu
 * @version Versió $Revision: 1.3 $ $Date: 2008/01/03 15:05:32 $
 * $Log: ParserHandlerTest.java,v $
 * Revision 1.3  2008/01/03 15:05:32  ecespedes
 * Arreglats elements del CMIDataModel i començant a crear un nou test.
 *
 * Revision 1.2  2007/12/27 15:02:24  ecespedes
 * Reunificant totes les crides del seqüenciament.
 *
 * Revision 1.1  2007/11/27 15:34:25  ecespedes
 * Arreglats bugs relacionats amb el Rollup. Creat nou joc de testos.
 *
 * Revision 1.11  2007/11/26 15:12:36  ecespedes
 * Ja controlem el Progress Measure.
 *
 * Revision 1.10  2007/11/23 15:04:58  ecespedes
 * El GestorBD ja implementa l'interfície DataAccess.
 *
 * Revision 1.9  2007/11/23 12:02:22  ecespedes
 * Començant a tocar el GestorBD en serio -> primer UserObjective serialitzat.
 *
 * Revision 1.8  2007/11/13 15:59:59  ecespedes
 * Treballant sobre el sistema per "linkar" els Objectives de l'estructura
 * Sequencing de l'arbre amb els Objectives de l'usuari.
 * Millorat TreeAnnotations (step 2 de 3)
 *
 * Revision 1.7  2007/11/12 13:00:21  ecespedes
 * Arreglant elements del seqüenciament.
 * Ja qüasi està implementat el TreeAnnotation.
 *
 * Revision 1.6  2007/11/11 22:22:06  ecespedes
 * Creat l'estructura TreeAnnotations, que és per marcar si un item
 * s'ha de tornar a comprovar el seu seqüenciament abans d'enviar
 *  a l'usuari.
 *
 * Revision 1.5  2007/11/08 16:33:09  ecespedes
 * Començant a Implementar el OverallSequencingProcess, que serà
 * les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
 * realment el seqüenciament SCORM1.3
 *
 * Revision 1.4  2007/11/08 07:33:59  ecespedes
 * Testejat que realment funcioni tot el CourseAdministrator i
 * DataAccessRepository.
 *
 * Revision 1.3  2007/11/07 13:15:08  ecespedes
 * Creat tot el sistema que controla els DataAccess, per tal
 * que el CourseAdministrator pugui subministrar-lo quan un
 * UserObjective li demani.
 *
 * Revision 1.2  2007/11/06 16:33:27  ecespedes
 * Fets els testos de benchmark per saber la quantitat de memòria
 * que necessita el sistema i com respondrien les optimitzacions.
 *
 * Revision 1.1  2007/11/05 19:42:39  ecespedes
 * Començant a implementar el motor del seqüenciament.
 * S'aniran fent testos per tal de veure diversos 'request'
 * per tal d'emular el que seria un usuari que interactua.
 *
 */

public class ParserHandlerTest extends TestCase {
	/**
	 * nouHandler serà l'instància que utilitzarem en els testos. 
	 */
	protected ParserHandler nouHandler;
	
	/**
	 * private Constant String Type.
	 * El model més complicat de seqüenciament, amb un 
	 * SequencingCollection i diversos post,pre i exitConditions.
	 */
	private static final String ARGS1 =
		"D:\\Campus_Development\\workspace\\Examples\\com\\sun\\"
		+ "java\\tutorials\\example2\\manifest\\imsmanifest.xml";
	/**
	 * private Constant String Type.
	 * Model "real" d'un curs de La Salle, amb dues organitzacions
	 * i un munt d'items. Sense seqüenciament de cap tipus. 
	 */
	private static final String ARGS2 =
		"D:\\Campus_Development\\workspace\\Examples\\com\\sun\\"
		+ "java\\tutorials\\DCSE_2007_2\\imsmanifest.xml";
	/**
	 * private Constant String Type.
	 * No té cap tipus de seqüenciament. El curs més simple que ens 
	 * podem trobar.
	 */
	private static final String ARGS3 = 
		"D:\\SCORM\\SCORM2004_Examples_Photoshop_1_1\\"
		+ "Photoshop_None\\imsmanifest.xml";
	/**
	 * private Constant String Type.
	 * Té un seqüenciament linial modificat perquè tingui una mica més
	 * de gràcia.
	 */
	private static final String ARGS4 = 
		"D:\\SCORM\\SCORM2004_Examples_Photoshop_1_1\\" 
		+ "Photoshop_Linear_SEQ-MODIFIED20071119\\imsmanifest.xml";
	/**
	 * @param name : String[] Type
	 */
	public ParserHandlerTest(final String name) {
		super(name);
	}
	
	/**
	 * Teòricament és per probar el constructor... a la pràctica
	 * ho utilitzarem per fer probes de seqüencimanet i analitzar el 
	 * request que ens retorna devant de cada petició de navegació
	 * per part nostra. 
	 */
	public final void testParserHandler() {
		UserObjective cUseObject = null;
		System.out.println("\npublic final void testParserHandler()");		
				
		Hashtable < Integer, CourseManager > courseManagers =
			new Hashtable < Integer, CourseManager > ();
		Hashtable < Integer, UserObjective > userObjectives =
			new Hashtable < Integer, UserObjective > ();
		
		/**
		 * Registrem el DataAccess en el repositori (DataAccessRepository).
		 * TODO Aquest registre, qui ho ha de fer?
		 * Reply: de moment a l'hora de fer el ParserHandler ell 
		 * mateix és registra en el DataAccessRepository, per tant
		 * si l'intentem de tornar a registrar ens donarà un avís 
		 * dient-nos que ja existeix aquesta entrada.
		 */
		DataAccessRepository.
			getInstance().registerDataAccess(
					nouHandler.getDataAccessID(), (DataAccess) nouHandler);
				
		for (int i = 0; i < nouHandler.getNumOfOrganizations(); i++) {
			
			/**
			 * Encara no tenim ni tan sols el UserObjective per defecte
			 * d'aquell curs, però el podem accedir sabent l'identificador
			 * de l'organització i l'identificador del dataAccess.
			 */
			userObjectives.put(i,
					CourseAdministrator.getInstance().
						getUserObjective(nouHandler.getDataAccessID(), i));
			
			/**
			 * Representa que el userObject ja el teniem guardat
			 * serialitzat a la BD. 
			 */						
			cUseObject = userObjectives.get(i);
			
			assertNotNull(cUseObject);
			
			/**
			 * Test per probar la BD!!
			 * TODO : Serialitzem el UserObjective
			 */
			GestorBD nouGestor = GestorBD.getInstance();
			System.out.println("[OK] getInstance()");
			boolean rs = nouGestor.testQuery();
			System.out.println("[" + rs + "] testQuery()");
			try {
				cUseObject = entryUserObjectiveTestData(cUseObject);
				cUseObject.adlnav = null;				
				//nouGestor.saveUserData(1, 1, cUseObject);
				UserObjective cUseObject2 = nouGestor.getUserData(1, 1);
				cUseObject = cUseObject2;
				System.out.println("Sortida: " + cUseObject2.organizationName);
			} catch (Exception e) {				
				e.printStackTrace();
			}
			
			/**
			 * Amb les dades que teniem del UserObjective li demanem 
			 * el CourseManager corresponent al CourseAdministrator.
			 */
			CourseManager nouCourseManager =
				CourseAdministrator.getInstance().
					getCourseManagerInstance(
							cUseObject.dataAccessID, cUseObject.organizationID);
			
			System.out.println("[" + i + "]Users[1]:" 
					+ nouCourseManager.getCurrentNumberUsers());
			
			assertNotNull(nouCourseManager);
						
			courseManagers.put(i, nouCourseManager);
		}		
		
		//Comença la part dels testos...
		CourseManager cManager = courseManagers.get(0);
		/** 
		 * He comentat aquesta línia perquè agafaré un UserObjective
		 * que ja té una mica de seqüenciament ficat pel mig.
		 * TODO: L'hem agafat de la BDD.
		 */
		//UserObjective cUseObject = userObjectives.get(0);
		 
		System.out.println("\t\t------------ Start Test ------------");
		
		System.out.println("\tCourseManager Information:\n\t"
				+ "\t[" + cManager.getIdentifier() + "]\t"
				+ cManager.getMetadata().identifier + "\t" 
				+ cManager.getOrganizationCluster().getIdentifier());
				
		/**
		 * PAS 1:
		 * 	Fem el primer accés, el ADLNav.request == null i el 
		 *  UserObjective.currentItem == null.
		 *  
		 *  Ens ha de retornar el primer asset disponible.
		 */
		//cUseObject = cManager.overallSequencingProcess(cUseObject);
		//printTreeAnnotations(cUseObject);
		/*
		 * TODO Comprovar com ens ha deixat el UserObjective i el ADLNav.
		 * (A veure si ho ha fet tot bé).
		 */
		
		System.out.println("\t\t------------ End Test ------------");
		//Finalitza la part dels testos...
		
		System.out.println("END-testParserHandler()");		
		printMemoryStatus();
		System.out.println("Intentem guanyar memòria...");
		cManager.removeAllTreeOrganization();
		printMemoryStatus();
	}
	
	/**
	 * Configura el ParserHandler.
	 */
	protected void setUp() {
		
		printMemoryStatus();
		
		nouHandler = new ParserHandler(ARGS4);
		
		printMemoryStatus();
		//memoryBenchMarc();
		
	}
	/**
	 * Per probar si realment serveix alliberar memòria...
	 *
	 */
	private void memoryBenchMarc() {
		final int size = 100;
		final int numSize = 100;
		
		System.out.println("memoryBenchMarc()...");
		printMemoryStatus();
		
		System.out.println("Loading " + size 
				+ " complete tree sequencing structures...");
		
		System.out.println("Loading " + size * numSize 
				+ " users structures...");
		
		CourseManager[] tmpMemoryTest = new CourseManager[size];
		//UserObjective[][] usobMemoryTest = new UserObjective[size][numSize];
		
		for (int i = 1; i < size; i++) {
			ParserHandler tmpParser = new ParserHandler(ARGS1);
			tmpMemoryTest[i] =
				new CourseManager((DataAccess) tmpParser, 1);
			tmpMemoryTest[i].getMetadata();
			tmpMemoryTest[i].getOrganizationCluster();
			/*
			for (int j = 1; j < numSize; j++) {
				usobMemoryTest[i][j] =
					tmpMemoryTest[i].getUserDefault();				
			}
			*/
		}		
		printMemoryStatus();
		
		System.out.println("Intentem guanyar memòria ...");
				
		for (int i = 1; i < size; i++) {
			tmpMemoryTest[i].removeAllTreeOrganization();
			tmpMemoryTest[i].removeMetadata();
			tmpMemoryTest[i] = null;
		}
		tmpMemoryTest = null;
		System.gc();
		
		printMemoryStatus();
		
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
	 * Funció que només serveix per printar el TreeAnnotations.
	 * 
	 * @param usObj : UserObjective Type
	 */
	private void printTreeAnnotations(final UserObjective usObj) {
		System.out.println("\t--------- TreeAnnotations [" 
				+ usObj.treeAnnotations.size() + "] ---------");		
		Collection < TreeAnnotations > tACollect =
			usObj.treeAnnotations.values();
		
		for (Iterator tAIterator = usObj.treeAnnotations.keySet().iterator();
			tAIterator.hasNext();) {
			
			String keyString = (String) tAIterator.next();
			TreeAnnotations current = usObj.treeAnnotations.get(keyString);
			
			System.out.println("[[" + keyString + "] " 
					+ "currentView: " 
					+ current.currentView.toString()
					//+ " invariantItem: " + current.invariantItem 
					+ " isAccessed: " + current.isAccessed 
					+ " sons: " + current.sons.toString()					
					+ "' ]" //);
					+ "\n->\tTitle: '" + current.title + "'");					
		}		
	}
	
	/***
	 * Omplim dades aleatòries per fer un test i saber com va l'Usuari.
	 * @param cUserObject : El userObjective de l'usuari.
	 * @return UserObjective Type.
	 */
	private UserObjective entryUserObjectiveTestData(
			final UserObjective cUserObject) {
		UserObjective rUObject = cUserObject;
		Calendar calendar = new GregorianCalendar();
				
		calendar.add(Calendar.DATE, -20);
		rUObject.dataModel.get("LESSON1").firstAttemptedTime =
			calendar.getTime();		
		rUObject.dataModel.get("LESSON1").activityAttemptCount = 9;
		calendar.add(Calendar.DATE, +1);
		rUObject.dataModel.get("LESSON1").lastAttemptedTime =
			calendar.getTime();
		
		rUObject.dataModel.get("LESSON2").firstAttemptedTime =
			calendar.getTime();		
		rUObject.dataModel.get("LESSON2").activityAttemptCount = 2;
		calendar.add(Calendar.DATE, +1);
		rUObject.dataModel.get("LESSON2").lastAttemptedTime =
			calendar.getTime();
		
		rUObject.dataModel.get("LESSON3").firstAttemptedTime =
			calendar.getTime();		
		rUObject.dataModel.get("LESSON3").activityAttemptCount = 7;
		calendar.add(Calendar.DATE, +1);
		rUObject.dataModel.get("LESSON3").lastAttemptedTime =
			calendar.getTime();
		
		rUObject.dataModel.get("LESSON4").firstAttemptedTime = null;			
		rUObject.dataModel.get("LESSON4").activityAttemptCount = 0;
		rUObject.dataModel.get("LESSON4").lastAttemptedTime = null;
		
		rUObject.dataModel.get("LESSON5").firstAttemptedTime = null;			
		rUObject.dataModel.get("LESSON5").activityAttemptCount = 0;
		rUObject.dataModel.get("LESSON5").lastAttemptedTime = null;
		
		rUObject.dataModel.get("LESSON6").firstAttemptedTime = null;			
		rUObject.dataModel.get("LESSON6").activityAttemptCount = 0;
		rUObject.dataModel.get("LESSON6").lastAttemptedTime = null;
		
		rUObject.dataModel.get("LESSON7").firstAttemptedTime = null;			
		rUObject.dataModel.get("LESSON7").activityAttemptCount = 0;
		rUObject.dataModel.get("LESSON7").lastAttemptedTime = null;
		
		rUObject.dataModel.get("LESSON8").firstAttemptedTime = null;			
		rUObject.dataModel.get("LESSON8").activityAttemptCount = 0;
		rUObject.dataModel.get("LESSON8").lastAttemptedTime = null;
		
		
		
		rUObject.dataModel.get("QUESTION1").firstAttemptedTime =
			calendar.getTime();		
		rUObject.dataModel.get("QUESTION1").activityAttemptCount = 2;
		rUObject.dataModel.get("QUESTION1").completionStatus = "completed";
		rUObject.dataModel.get("QUESTION1").successStatus = "passed";
		rUObject.dataModel.get("QUESTION1").scoreScaled = "1";
		calendar.add(Calendar.DATE, +1);
		rUObject.dataModel.get("QUESTION1").lastAttemptedTime =
			calendar.getTime();
		
		
		rUObject.dataModel.get("QUESTION2").firstAttemptedTime =
			calendar.getTime();		
		rUObject.dataModel.get("QUESTION2").activityAttemptCount = 1;
		rUObject.dataModel.get("QUESTION2").completionStatus = "incomplete";
		rUObject.dataModel.get("QUESTION2").successStatus = "passed";
		rUObject.dataModel.get("QUESTION2").scoreScaled = "0.2";
		calendar.add(Calendar.DATE, +1);
		rUObject.dataModel.get("QUESTION2").lastAttemptedTime =
			calendar.getTime();
		
		rUObject.dataModel.get("QUESTION3").firstAttemptedTime = null;
		rUObject.dataModel.get("QUESTION3").activityAttemptCount = 0;
		rUObject.dataModel.get("QUESTION3").completionStatus = "unknown";
		rUObject.dataModel.get("QUESTION3").successStatus = "unknown";
		rUObject.dataModel.get("QUESTION3").lastAttemptedTime = null;
		
		rUObject.dataModel.get("QUESTION4").firstAttemptedTime = null;
		rUObject.dataModel.get("QUESTION4").activityAttemptCount = 0;
		rUObject.dataModel.get("QUESTION4").completionStatus = "unknown";
		rUObject.dataModel.get("QUESTION4").successStatus = "unknown";
		rUObject.dataModel.get("QUESTION4").lastAttemptedTime = null;
		
		rUObject.dataModel.get("QUESTION5").firstAttemptedTime = null;
		rUObject.dataModel.get("QUESTION5").activityAttemptCount = 0;
		rUObject.dataModel.get("QUESTION5").completionStatus = "unknown";
		rUObject.dataModel.get("QUESTION5").successStatus = "unknown";
		rUObject.dataModel.get("QUESTION5").lastAttemptedTime = null;
		
		rUObject.dataModel.get("QUESTION6").firstAttemptedTime = null;
		rUObject.dataModel.get("QUESTION6").activityAttemptCount = 0;
		rUObject.dataModel.get("QUESTION6").completionStatus = "unknown";
		rUObject.dataModel.get("QUESTION6").successStatus = "unknown";
		rUObject.dataModel.get("QUESTION6").lastAttemptedTime = null;
		
		rUObject.dataModel.get("QUESTION7").firstAttemptedTime =
			calendar.getTime();		
		rUObject.dataModel.get("QUESTION7").activityAttemptCount = 2;
		rUObject.dataModel.get("QUESTION7").completionStatus = "completed";
		rUObject.dataModel.get("QUESTION7").successStatus = "passed";	
		rUObject.dataModel.get("QUESTION7").scoreScaled = "1";
		calendar.add(Calendar.DATE, +1);
		rUObject.dataModel.get("QUESTION7").lastAttemptedTime =
			calendar.getTime();		
		
		rUObject.dataModel.get("QUESTION8").firstAttemptedTime =
			calendar.getTime();		
		rUObject.dataModel.get("QUESTION8").activityAttemptCount = 1;
		rUObject.dataModel.get("QUESTION8").completionStatus = "incomplete";
		rUObject.dataModel.get("QUESTION8").successStatus = "failed";	
		rUObject.dataModel.get("QUESTION8").scoreScaled = "0";
		calendar.add(Calendar.DATE, +1);
		rUObject.dataModel.get("QUESTION8").lastAttemptedTime =
			calendar.getTime();
		
		
		rUObject.dataModel.get("QUESTION9").firstAttemptedTime =
			calendar.getTime();		
		rUObject.dataModel.get("QUESTION9").activityAttemptCount = 3;
		rUObject.dataModel.get("QUESTION9").completionStatus = "completed";
		rUObject.dataModel.get("QUESTION9").successStatus = "passed";
		rUObject.dataModel.get("QUESTION9").scoreScaled = "0.75";
		calendar.add(Calendar.DATE, +1);
		rUObject.dataModel.get("QUESTION9").lastAttemptedTime =
			calendar.getTime();		
		
		
		/*
			System.out.println("Current DATE: " + currentDate.toString()
				+ "\nDATE:" + calendar.getTime().toString());
		*/
		return rUObject;
	}
		
}

