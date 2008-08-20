// $Id: ServicesTest.java,v 1.8 2008/02/08 15:52:17 toroleo Exp $
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
package edu.url.lasalle.campus.scorm2004rte.test;

import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Vector;

import org.campusproject.components.AgentComponent;
import org.campusproject.osid.agent.Agent;
import org.campusproject.osid.agent.AgentIterator;
import org.campusproject.osid.agent.Group;
import org.campusproject.osid.agent.GroupType;
import org.campusproject.osid.id.Id;
//import org.campusproject.osid.shared.PropertiesIterator;
import org.campusproject.utils.ConfigurationProperties;
import org.osid.agent.AgentException;
import org.osid.authorization.AuthorizationException;
import org.osid.dictionary.DictionaryException;
import org.osid.id.IdException;
import org.osid.shared.SharedException;

import edu.url.lasalle.campus.scorm2004rte.osid.requests.AgentRequest;
import edu.url.lasalle.campus.scorm2004rte.osid.requests.AuthenticationRequest;
import edu.url.lasalle.campus.scorm2004rte.osid.requests.AuthorizationRequest;
import edu.url.lasalle.campus.scorm2004rte.osid.requests.ConfigurationRequest;
import edu.url.lasalle.campus.scorm2004rte.util.exception.RequestException;
import edu.url.lasalle.campus.scorm2004rte.util.main.InicializationDummy;
import junit.framework.TestCase;

/**
 * $Id: ServicesTest.java,v 1.8 2008/02/08 15:52:17 toroleo Exp $
 * ServicesTest.
 * Realitza els tests de les diferents classes 
 * dels serveis del OKI-BUS utilitzats. 
 *  
 * @author M.Reyes Enginyeria La Salle mtoro@salle.url.edu
 * @version 1.0 07/01/2008 $Revision: 1.8 $ $Date: 2008/02/08 15:52:17 $
 * $Log: ServicesTest.java,v $
 * Revision 1.8  2008/02/08 15:52:17  toroleo
 * Eliminaci� imports innecessaris.
 *
 * Revision 1.7  2008/02/08 12:22:57  msegarra
 * Proves JUnit adaptades al nou dummy v6.
 *
 * Revision 1.6  2008/01/30 15:54:13  toroleo
 * Noves proves pels m�todes:
 *  -getProperty del ConfigurationRequest
 *
 * Revision 1.5  2008/01/30 13:55:54  toroleo
 * Noves proves pels m�todes:
 * -modifyAgents del InicializationDummy
 *
 * Revision 1.4  2008/01/25 16:13:09  toroleo
 * Noves proves pels m�todes:
 *  -getUsersAuthorized()
 *  -getUserAuthorizedAs()
 * del AuthorizationRequest
 *
 * Revision 1.3  2008/01/23 15:17:12  toroleo
 * Noves proves pels m�todes:
 *  -isAgentOfRoll del AgentRequest
 *  -isAuthorized() del AuthorizationRequest
 *
 * Revision 1.2  2008/01/18 09:55:16  toroleo
 * Afegida funci� de test testGetAgentRol.
 *
 * Revision 1.1  2008/01/16 21:24:33  toroleo
 * Proves Junit per verifica el bon funcionament dels
 * serveis de AuthenticationRequest i dels m�todes
 * getAgent i isAgentOfRoll.
 *
 * 
 */
public class ServicesTest extends TestCase {
	
	/** Objecte per provar el servei d'autenticaci�. */
	private AuthenticationRequest authenticationRequest;
	
	/** Objecte per provar el servei d'autoritzaci�. */
	private AuthorizationRequest authorizationRequest;
	
	/** Objecte per provar el servei Agent.	 */
	private AgentRequest agentRequest;
	
	/** Objecte per provar el servei Configuration.	 */
	private ConfigurationRequest configurationtRequest;
	
	/**
	 *  Objecte per llegir i recuperar el contingut del fitxer 
	 * configuration.bin.	 
	 */
	private ConfigurationProperties configurationProperties;
	
	/**
	 * Objecte per modificar l'escenari de proves.
	 * El que es modifica �s la informaci� enmagatzemada 
	 * al fitxer configuration.bin
	 */
	private Properties properties;
	
	/**
	 * Constructor.
	 * 
	 * @param name	Nom que se li donar� al test.
	 */
	public ServicesTest(final String name) {
		super(name);
	}
	
	/**
	 * 
	 * Prepara l'entorn de proves. S'executa abans de 
	 * llen�ar cada un dels m�todes de test
	 * 
	 * @throws Exception Es llen�a un error en cas de que
	 * 					 hagi algun problema al preparar 
	 * 					 l'entorn de proves
	 */
	public final void setUp() throws Exception {
		//se ejecuta justo antes de cada m�todo test
		//es crean els objectes
		authenticationRequest = new AuthenticationRequest();
		authorizationRequest = new AuthorizationRequest();
		agentRequest = new AgentRequest();
		configurationProperties = ConfigurationProperties.getInstance();
		
		try {
			InicializationDummy.install();
		} catch (DictionaryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		configurationtRequest = ConfigurationRequest.getInstance();
		
		
		//es recupera l'objecte properties amb la informaci� del
		//fitxer configuration.bin
		properties = configurationProperties.getProperties();
		
		//es configura l'entorn de proves
		try {
			
			InicializationDummy.createAgents();
			InicializationDummy.createGroups();
		
		} catch (AgentException e) {
			System.out.println("Error al crear els agents. Error: " + e);
		} catch (IdException e) {
			System.out.println("Error al crear els grups. Error: " + e);
		} catch (AuthorizationException e) {
			System.out.println("Error al crear els grups. Error: " + e);
		/*} catch (DictionaryException e) {
			System.out.println("Error al crear els par�metres d'inst�ncia"
							   + " i d'aplicaci�. Error: " + e);*/
		}

	}
	
	/**
	 * Codi a ejecutar despr�s de llen�ar els
	 * m�todes de test.
	 * 
	 * Maiye Cambiar comentario.
	 * 
	 * @throws Exception Maiye falta comentario.
	 */
	public void tearDown() throws Exception {
		//se ejecuta justo despu�s de cada m�todo test
		properties.setProperty("group_grid2_agents", "agid2,agid3");
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * isUserAuthenticated() del AuthenticationRequest.
	 * 
	 * Comprova que el m�tode retorna true, autentificat,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * La linea per especificar aix� �s aquesta:
	 *    authentication_authenticated=true
	 *    
	 */
	public final void testUserAuthenticated() {
		
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat.
			properties.setProperty("authentication_authenticated", "true");
			
			boolean authenticated = authenticationRequest.isUserAuthenticated();
			System.out.println("[TestUserAuthenticated] Authenticated: " 
								+ authenticated);
			assertTrue(authenticated);
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		}
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * isUserAuthenticated() del AuthenticationRequest.
	 * 
	 * Comprova que el m�tode retorna false, no autentificat,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * La linea per especificar aix� �s aquesta:
	 *    authentication_authenticated=false
	 *    
	 */
	public final void testUserNotAuthenticated() {
		
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//no est� autenticat.
			properties.setProperty("authentication_authenticated", "false");
			
			boolean authenticated = authenticationRequest.isUserAuthenticated();
			System.out.println("[TestUserNotAuthenticated] Authenticated: " 
								+ authenticated);
			assertFalse(authenticated);
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		}
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getUserID() del AuthenticationRequest.
	 * 
	 * Comprova que el m�tode retorna l'identificador 
	 * agid1 com a usuari connectat al sistema,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid1
	 *
	 */
	public final void testGetUserIdAgid1() {
		
		try {

			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid1.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid1");
			
			Id idAuthenticated  = authenticationRequest.getUserId();
			System.out.println("[testGetUserIdAgid1] Id user authenticated: " 
							+ idAuthenticated.getIdString());
			assertEquals("agid1", idAuthenticated.getIdString());
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (SharedException e) {
			fail("No deber�a haber fallado");
		}
		
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getUserID() del AuthenticationRequest.
	 * 
	 * Comprova que el m�tode retorna l'identificador 
	 * agid5 (usuari an�nim) perque l'usuari que fa les peticions
	 * no est� conectat al sistema,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=false
	 *  authentication_anonymousId=agid5
	 *
	 */
	public final void testGetUserIdUserNotAuthenticated() {
		
		try {

			//es modifica l'escenari per indicar que l'usuari
			//no est� autenticat i que l'identificador de l'usuari
			//an�nim �s agid5.
			properties.setProperty("authentication_authenticated", "false");
			properties.setProperty("authentication_anonymousId", "agid5");
			
			Id idAuthenticated  = authenticationRequest.getUserId();
			System.out.println("[testGetUserIdUserNotAuthenticated]"
							+ " Id user authenticated: " 
							+ idAuthenticated.getIdString());
			assertEquals("agid5", idAuthenticated.getIdString());
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (SharedException e) {
			fail("No deber�a haber fallado");
		}
		
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getAgent() del AgentRequest.
	 * 
	 * Comprova que el m�tode retorna les dades de
	 * l'usuari agid6 com usuari connectat al sistema,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid6
	 *  
	 *  agent_agid6_ag6type_authority=CAMPUSPROJECT.ORG
	 *  agent_agid6_ag6type_description=ag6des
	 *  agent_agid6_ag6type_domain=Agent
	 *  agent_agid6_ag6type_keyword=PERSON
	 *  agent_agid6_name=Sebastian
	 *  agent_agid6_type=ag6type
	 *
	 */
	public final void testGetAgentAgid6() {
		
		try {

			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid6.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid6");
			//es crea l'objecte de l'agent que s'espera rebre
			Id idAgentExpected = new Id();
			idAgentExpected.setIdString("agid6");
			Agent agentExpected = new Agent(idAgentExpected, "Sebastian", 
								AgentComponent.PERSON, null);
			
			Agent agent = agentRequest.getAgent();
			
			System.out.println("[testGetAgentAgid6] Id agent: " 
							+ agent.getId().getIdString()
							+ " Name agent: "
							+ agent.getDisplayName());
			
			assertEquals(agentExpected, agent);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (SharedException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getAgent() del AgentRequest.
	 * 
	 * Comprova que el m�tode retorna les dades de
	 * l'usuari agid5 (usuari an�nim) perqu� no hi ha cap usuari
	 * autentificat al sistema,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=false
	 *  authentication_anonymousId=agid5
	 *  
	 *  agent_agid5_ag5type_authority=CAMPUSPROJECT.ORG
	 *  agent_agid5_ag5type_description=anondes
	 *  agent_agid5_ag5type_domain=Agent
	 *  agent_agid5_ag5type_keyword=PERSON
	 *  agent_agid5_name=Anonymous
	 *  agent_agid5_type=ag5type
	 *
	 */
	public final void testGetAgentUserNotAuthenticated() {
		
		try {

			//es modifica l'escenari per indicar que l'usuari
			//no est� autenticat i que l'identificador de l'usuari
			//an�nim �s agid5.
			properties.setProperty("authentication_authenticated", "false");
			properties.setProperty("authentication_anonymousId", "agid5");
			
			//es crea l'objecte de l'agent que s'espera rebre
			Id idAgentExpected = new Id();
			idAgentExpected.setIdString("agid5");
			Agent agentExpected = new Agent(idAgentExpected, "Anonymous", 
											AgentComponent.PERSON, null);
			
			Agent agent = agentRequest.getAgent();
			
			System.out.println("[testGetAgentUserNotAuthenticated] Id agent: " 
							+ agent.getId().getIdString()
							+ " Name agent: "
							+ agent.getDisplayName());
			
			assertEquals(agentExpected, agent);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (SharedException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * isAgentOfRoll() del AgentRequest.
	 * 
	 * Comprova que el m�tode retorna true indicant que 
	 * l'usuari connectat al sistema, agid6, t� el rol "STUDENT",
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid6
	 *  
	 *  agent_agid6_ag6type_authority=CAMPUSPROJECT.ORG
	 *  agent_agid6_ag6type_description=ag6des
	 *  agent_agid6_ag6type_domain=Agent
	 *  agent_agid6_ag6type_keyword=PERSON
	 *  agent_agid6_name=Sebastian
	 *  agent_agid6_type=ag6type
	 *  
	 *  group_grid4_agents=agid6,agid4
	 *  group_grid4_description=estudiants mates
	 *  group_grid4_gr4type_authority=CAMPUSPROJECT.ORG
	 *  group_grid4_gr4type_description=Studiants del campus
	 *  group_grid4_gr4type_domain=Agent
	 *  group_grid4_gr4type_keyword=STUDENT
	 *  group_grid4_name=group4
	 *  group_grid4_type=gr4type
	 *
	 */
	public final void testAgentOfRollAgid6Student() {
		
		try {

			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid6.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid6");
			
			boolean isRol = agentRequest.isAgentOfRoll("STUDENT");
			
			System.out.println("[testAgentOfRollAgid6Student]"
							+ " Agid6 have a roll STUDENT: " 
							+ isRol);
			
			assertTrue(isRol);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * isAgentOfRoll() del AgentRequest.
	 * 
	 * Comprova que el m�tode retorna false indicant que 
	 * l'usuari connectat al sistema, agid6, no t� el rol "TEACHER",
	 * ja que al fitxer configuration.bin ho hem especificat aix�, 
	 * tot dient que �s "STUDENT".
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid6
	 *  
	 *  agent_agid6_ag6type_authority=CAMPUSPROJECT.ORG
	 *  agent_agid6_ag6type_description=ag6des
	 *  agent_agid6_ag6type_domain=Agent
	 *  agent_agid6_ag6type_keyword=PERSON
	 *  agent_agid6_name=Sebastian
	 *  agent_agid6_type=ag6type
	 *  
	 *  group_grid4_agents=agid6,agid4
	 *  group_grid4_description=estudiants mates
	 *  group_grid4_gr4type_authority=CAMPUSPROJECT.ORG
	 *  group_grid4_gr4type_description=Studiants del campus
	 *  group_grid4_gr4type_domain=Agent
	 *  group_grid4_gr4type_keyword=STUDENT
	 *  group_grid4_name=group4
	 *  group_grid4_type=gr4type
	 *
	 */
	public final void testAgentOfRollAgid1Teacher() {
		
		try {

			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid1.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid6");
			
			boolean isRol = agentRequest.isAgentOfRoll("TEACHER");
			
			System.out.println("[testAgentOfRollAgid6Teacher]"
							+ " Agid6 have a roll TEACHER: " 
							+ isRol);
			
			assertFalse(isRol);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * isAgentOfRoll() del AgentRequest.
	 * 
	 * Comprova que el m�tode retorna false indicant que 
	 * l'usuari connectat al sistema, agid6, no t� el rol "UNKNOW",
	 * perqu� aquest rol no existeix i perqu� al fitxer
	 * configuration.bin hem especificat aix� 
	 * que �s "STUDENT" de la seg�ent manera:
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid6
	 *  
	 *  agent_agid6_ag6type_authority=CAMPUSPROJECT.ORG
	 *  agent_agid6_ag6type_description=ag6des
	 *  agent_agid6_ag6type_domain=Agent
	 *  agent_agid6_ag6type_keyword=PERSON
	 *  agent_agid6_name=Sebastian
	 *  agent_agid6_type=ag6type
	 *  
	 *  group_grid4_agents=agid6,agid4
	 *  group_grid4_description=estudiants mates
	 *  group_grid4_gr4type_authority=CAMPUSPROJECT.ORG
	 *  group_grid4_gr4type_description=Studiants del campus
	 *  group_grid4_gr4type_domain=Agent
	 *  group_grid4_gr4type_keyword=STUDENT
	 *  group_grid4_name=group4
	 *  group_grid4_type=gr4type
	 *
	 */
	public final void testAgentOfRollAgid6RollUnknown() {
		
		try {

			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid1.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid6");
			
			boolean isRol = agentRequest.isAgentOfRoll("UNKNOW");
			
			System.out.println("[testAgentOfRollAgid6Student]"
							+ " Agid6 have a roll UNKNOW: " 
							+ isRol);
			
			assertFalse(isRol);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getAgentRoll() del AgentRequest.
	 * 
	 * Comprova que el m�tode retorna "STUDENT" indicant que 
	 * l'usuari connectat al sistema, agid6, t� el rol "STUDENT",
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid6
	 *  
	 *  agent_agid6_ag6type_authority=CAMPUSPROJECT.ORG
	 *  agent_agid6_ag6type_description=ag6des
	 *  agent_agid6_ag6type_domain=Agent
	 *  agent_agid6_ag6type_keyword=PERSON
	 *  agent_agid6_name=Sebastian
	 *  agent_agid6_type=ag6type
	 *  
	 *  group_grid4_agents=agid6,agid4
	 *  group_grid4_description=estudiants mates
	 *  group_grid4_gr4type_authority=CAMPUSPROJECT.ORG
	 *  group_grid4_gr4type_description=Studiants del campus
	 *  group_grid4_gr4type_domain=Agent
	 *  group_grid4_gr4type_keyword=STUDENT
	 *  group_grid4_name=group4
	 *  group_grid4_type=gr4type
	 *
	 */
	public final void testGetAgentRollAgid6Student() {
		try {

			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid1.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid6");
			
			String rol = agentRequest.getAgentRoll();
			
			System.out.println("[testGetAgentRollAgid6Student]"
							+ " Agid6 have a roll: " 
							+ rol);
			
			assertEquals("STUDENT", rol);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
	}
		
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getAgentRoll() del AgentRequest.
	 * 
	 * 	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getAgentRoll() del AgentRequest.
	 * 
	 * Comprova que el m�tode retorna "Null" ja que no hi ha 
	 * cap usuari autentificat,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=false
	 *  authentication_anonymousId=agid5
	 *  
	 */
	public final void testGetAgentRollAnonymous() {
		try {

			//es modifica l'escenari per indicar que l'usuari
			//no est� autenticat i que l'identificador de l'usuari
			//an�nim �s agid5.
			properties.setProperty("authentication_authenticated", "false");
			properties.setProperty("authentication_anonymousId", "agid5");
			
			String rol = agentRequest.getAgentRoll();
			
			System.out.println("[testGetAgentRollAnonymous]"
							+ " Rol when anyone are authenticated: " 
							+ rol);
			
			assertNull(rol);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * isAuthorized() del AuthorizationRequest.
	 * 
	 * Comprova que el m�tode retorna "STUDENT" indicant que 
	 * l'usuari connectat al sistema, agid6, t� autoritzaci�
	 * d'actuaci� en el curs pel que s'executa l'aplicai� i
	 * t� el rol "STUDENT",
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid6
	 *  
	 *  group_grid4_agents=agid6,agid4
	 *  group_grid4_description=estudiants mates
	 *  group_grid4_gr4type_authority=CAMPUSPROJECT.ORG
	 *  group_grid4_gr4type_description=Studiants del campus
	 *  group_grid4_gr4type_domain=Agent
	 *  group_grid4_gr4type_keyword=STUDENT
	 *  group_grid4_name=group4
	 *  group_grid4_type=gr4type
	 */
	public final void testAuthorizedAgid6Student() {
		
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid6.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid6");
			
			String authorizedRoll = authorizationRequest.isAuthorized();
			
			System.out.println("[testAuthorizedAgid6Student] Authorized rol: " 
								+ authorizedRoll);
			assertEquals("STUDENT", authorizedRoll);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
		
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * isAuthorized() del AuthorizationRequest.
	 * 
	 * Comprova que el m�tode retorna "TEACHER" indicant que 
	 * l'usuari connectat al sistema, agid3, t� autoritzaci�
	 * d'actuaci� en el curs pel que s'executa l'aplicai� i
	 * t� el rol "TEACHER",
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid3
	 * 
	 * group_grid2_agents=agid2,agid3
	 * group_grid2_description=Professors de fisica
	 * group_grid2_gr2type_authority=CAMPUSPROJECT.ORG
	 * group_grid2_gr2type_description=Professors del campus
	 * group_grid2_gr2type_domain=Agent
	 * group_grid2_gr2type_keyword=TEACHER
	 * group_grid2_name=group2
	 * group_grid2_type=gr2type
	 * 
	 */
	public final void testAuthorizedAgid3Teacher() {
		
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid3.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid3");
			
			String authorizedRoll = authorizationRequest.isAuthorized();
			
			System.out.println("[testAuthorizedAgid3Teacher] Authorized rol: " 
								+ authorizedRoll);
			assertEquals("TEACHER", authorizedRoll);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
		
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * isAuthorized() del AuthorizationRequest.
	 * 
	 * Comprova que el m�tode retorna "Null" indicant que 
	 * l'usuari no t� autoritzaci� d'actuaci� en el 
	 * curs pel que s'executa l'aplicai�,
	 * aix� �s perque no hi ha cap usuari autenticat al sistema
	 * i l'usuari an�nim no t� autoritzaci�.
	 * Tot aix� sespefica al fitxer configuration.bin de la 
	 * seg�ent manera:
	 *  authentication_authenticated=false
	 *  authentication_anonymousId=agid5
	 *  
	 */
	public final void testNotAuthorizedUserNotAuthenticated() {
		
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//no est� autenticat i que l'identificador de l'usuari
			//an�nim �s agid5.
			properties.setProperty("authentication_authenticated", "false");
			properties.setProperty("authentication_anonymousId", "agid5");
			
			String authorizedRoll = authorizationRequest.isAuthorized();
			
			System.out.println("[testNotAuthorizedUserNotAuthenticated]"
								+ "Authorized rol: " + authorizedRoll);
			
			assertNull(authorizedRoll);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
		
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * isAuthorized() del AuthorizationRequest.
	 * 
	 * Comprova que el m�tode retorna "null" indicant que 
	 * l'usuari connectat al sistema, agid1, no t� autoritzaci�
	 * d'actuaci� en el curs pel que s'executa l'aplicai�,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid1
	 * 
	 * 
	 * agent_agid1_ag1type_authority=CAMPUSPROJECT.ORG
	 * agent_agid1_ag1type_description=anondes
	 * agent_agid1_ag1type_domain=Agent
	 * agent_agid1_ag1type_keyword=PERSON
	 * agent_agid1_name=Frank
	 * agent_agid!_type=ag1type
	 * 
	 */
	public final void testNoAuthorizedAgid1() {
		
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid1.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid1");
			
			String authorizedRoll = authorizationRequest.isAuthorized();
			
			System.out.println("[testNoAuthorizedAgid1]"
								+ " Authorized rol: " + authorizedRoll);
			
			assertNull(authorizedRoll);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
		
	}
	
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getUserAuthorized() del AuthorizationRequest.
	 * 
	 * Comprova que el m�tode retorna els grups que t� el
	 * curs pel que s'executa l'aplicai�, tenent en compte que 
	 * l'usuari est� autentificat,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid6
	 * 
	 *  group grid1 (instance container group)
	 *  group_grid1_agents=grid2,grid3
	 *  group_grid1_description=curs de fisica
	 *  group_grid1_gr1type_authority=CAMPUSPROJECT.ORG
	 *  group_grid1_gr1type_description=Classe o curs
	 *  group_grid1_gr1type_domain=AGENT
	 *  group_grid1_gr1type_keyword=COURSE
	 *  group_grid1_name=group1
	 *  group_grid1_type=gr1type
	 * 
	 * group_grid2_agents=agid2,agid3
	 * group_grid2_description=professors fisica
	 * group_grid2_gr2type_authority=CAMPUSPROJECT.ORG
	 * group_grid2_gr2type_description=Grup de professors
	 * group_grid2_gr2type_domain=AGENT
	 * group_grid2_gr2type_keyword=TEACHER
	 * group_grid2_name=group2
	 * group_grid2_type=gr2type
	 * 
	 * group_grid3_agents=agid6,agid4
	 * group_grid3_description=estudiants fisica
	 * group_grid3_gr3type_authority=CAMPUSPROJECT.ORG
	 * group_grid3_gr3type_description=Grup d'estudiants
	 * group_grid3_gr3type_domain=AGENT
	 * group_grid3_gr3type_keyword=STUDENT
	 * group_grid3_name=group3
	 * group_grid3_type=gr3type
	 * 
	 */
	public final void testGetUsersAuthorized() {
		Vector<Agent> vectorGroupsObtain = new Vector<Agent>();
		
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid6.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid6");
			
			//es crea el grup de professors esperat
			Id idTeacher = new Id();
			idTeacher.setIdString("grid4");
			GroupType teacherType = new GroupType("CAMPUSPROJECT.ORG", "AGENT",
											"TEACHER", "Grup de professors");
			Group teachers = new Group(idTeacher, "group2", 
									teacherType, "professors fisica", null);

			//es crea el grup de estudiants esperat
			Id idStudent = new Id();
			idStudent.setIdString("grid5");
			
			GroupType studentType = new GroupType("CAMPUSPROJECT.ORG", "AGENT",
												"STUDENT", "Grup d'estudiants");
			Group students = new Group(idStudent, "group3", 
									studentType, "estudiants fisica", null);

			//es crea el vector per afegir els grups esperats
			Vector<Agent> vectorGroupsExpected = new Vector<Agent>();
			
			vectorGroupsExpected.add(teachers);
			vectorGroupsExpected.add(students);
			
			//es fa la petici� que es vol testejar
			AgentIterator groupsIterator = 
								authorizationRequest.getUsersAuthorized();
			
			Group group = null;
			System.out.println("[testGetUsersAuthorized]"
					+ " Grups: ");
			while (groupsIterator.hasNextAgent()) {
				group = (Group) groupsIterator.nextAgent();
				vectorGroupsObtain.add(group);
				System.out.println("  -" + group.getDisplayName());	
			}
			
			assertEquals(vectorGroupsExpected, vectorGroupsObtain);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		} catch (SharedException e) {
			fail("No deber�a haber fallado");
		}
		
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getUserAuthorized() del AuthorizationRequest.
	 * 
	 * Comprova que el m�tode retorna null perqu� l'usuari no
	 * est� autentificat al sistema,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=false
	 *  authentication_anonymousId=agid5
	 * 
	 */
	public final void testGetUsersAuthorizedUserNotAuthenticated() {
				
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//no est� autenticat i que l'identificador de l'usuari
			//an�nim �s agid5.
			properties.setProperty("authentication_authenticated", "false");
			properties.setProperty("authentication_anonymousId", "agid5");
			
			//es fa la petici� que es vol testejar
			AgentIterator groupsIterator = 
								authorizationRequest.getUsersAuthorized();
		
			System.out.println("[testGetUsersAuthorizedUserNotAuthenticated]"
					+ " Grups trobats: " + groupsIterator);
			
			assertNull(groupsIterator);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
		
	}
	

	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getUserAuthorized() del AuthorizationRequest.
	 * 
	 * Comprova que el m�tode retorna els grups que t� el
	 * curs pel que s'executa l'aplicai�, tenent en compte que 
	 * l'usuari est� autentificat per� no t� permisos d'actuaci�,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid1
	 * 
	 *  group grid1 (instance container group)
	 *  group_grid1_agents=grid2,grid3
	 *  group_grid1_description=curs de fisica
	 *  group_grid1_gr1type_authority=CAMPUSPROJECT.ORG
	 *  group_grid1_gr1type_description=Classe o curs
	 *  group_grid1_gr1type_domain=AGENT
	 *  group_grid1_gr1type_keyword=COURSE
	 *  group_grid1_name=group1
	 *  group_grid1_type=gr1type
	 * 
	 * group_grid2_agents=agid2,agid3
	 * group_grid2_description=professors fisica
	 * group_grid2_gr2type_authority=CAMPUSPROJECT.ORG
	 * group_grid2_gr2type_description=Grup de professors
	 * group_grid2_gr2type_domain=AGENT
	 * group_grid2_gr2type_keyword=TEACHER
	 * group_grid2_name=group2
	 * group_grid2_type=gr2type
	 * 
	 * group_grid3_agents=agid6,agid4
	 * group_grid3_description=estudiants fisica
	 * group_grid3_gr3type_authority=CAMPUSPROJECT.ORG
	 * group_grid3_gr3type_description=Grup d'estudiants
	 * group_grid3_gr3type_domain=AGENT
	 * group_grid3_gr3type_keyword=STUDENT
	 * group_grid3_name=group3
	 * group_grid3_type=gr3type
	 * 
	 */
	public final void testGetUsersAuthorizedUserNotAuthorized() {
		Vector<Agent> vectorGroupsObtain = new Vector<Agent>();
		
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid6.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid1");
		
			
			//es crea el grup de professors esperat
			Id idTeacher = new Id();
			idTeacher.setIdString("grid4");
			GroupType teacherType = new GroupType("CAMPUSPROJECT.ORG", "AGENT",
											"TEACHER", "Grup de professors");
			Group teachers = new Group(idTeacher, "group2", 
									teacherType, "professors fisica", null);

			//es crea el grup de estudiants esperat
			Id idStudent = new Id();
			idStudent.setIdString("grid5");
			
			GroupType studentType = new GroupType("CAMPUSPROJECT.ORG", "AGENT",
												"STUDENT", "Grup d'estudiants");
			Group students = new Group(idStudent, "group3", 
									studentType, "estudiants fisica", null);

			//es crea el vector per afegir els grups esperats
			Vector<Agent> vectorGroupsExpected = new Vector<Agent>();
			
			vectorGroupsExpected.add(teachers);
			vectorGroupsExpected.add(students);
			
			//es fa la petici� que es vol testejar
			AgentIterator groupsIterator = 
								authorizationRequest.getUsersAuthorized();
			
			Group group = null;
			System.out.println("[testGetUsersAuthorizedUserNotAuthorized]"
					+ " Grups: ");
			while (groupsIterator.hasNextAgent()) {
				group = (Group) groupsIterator.nextAgent();
				vectorGroupsObtain.add(group);
				System.out.println("  -" + group.getDisplayName());	
			}
			
			assertEquals(vectorGroupsExpected, vectorGroupsObtain);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		} catch (SharedException e) {
			fail("No deber�a haber fallado");
		}

	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getUserAuthorizedAs() del AuthorizationRequest.
	 * 
	 * Comprova que el m�tode retorna els agents amb 
	 * el rol indicat que t� el curs pel que s'executa 
	 * l'aplicai�, tenent en compte que 
	 * l'usuari est� autentificat,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid1
	 *  
	 * group grid1 (instance container group)
	 * group_grid1_agents=grid2,grid3
	 * group_grid1_description=curs de fisica
	 * group_grid1_gr1type_authority=CAMPUSPROJECT.ORG
	 * group_grid1_gr1type_description=Classe o curs
	 * group_grid1_gr1type_domain=AGENT
	 * group_grid1_gr1type_keyword=COURSE
	 * group_grid1_name=group1
	 * group_grid1_type=gr1type
	 *  
	 * group_grid2_agents=agid2,agid3
	 * group_grid2_description=professors fisica
	 * group_grid2_gr2type_authority=CAMPUSPROJECT.ORG
	 * group_grid2_gr2type_description=Grup de professors
	 * group_grid2_gr2type_domain=AGENT
	 * group_grid2_gr2type_keyword=TEACHER
	 * group_grid2_name=group2
	 * group_grid2_type=gr2type
	 * 
	 * agent_agid2_ag2type_authority=CAMPUSPROJECT.ORG
	 * agent_agid2_ag2type_description=Identifica una persona
	 * agent_agid2_ag2type_domain=AGENT
	 * agent_agid2_ag2type_keyword=PERSON
	 * agent_agid2_name=Angeles
	 * agent_agid2_type=ag2type
	 * agent_agid3_ag3type_authority=CAMPUSPROJECT.ORG
	 * agent_agid3_ag3type_description=Identifica una persona
	 * agent_agid3_ag3type_domain=AGENT
	 * agent_agid3_ag3type_keyword=PERSON
	 * agent_agid3_name=Juan
	 * agent_agid3_type=ag3type
	 * 
	 */
	public final void testGetUsersAuthorizedAsTeacher() {
		Vector<Agent> vectorAgentsObtain = new Vector<Agent>();
		
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid1.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid1");
		
			
			//es crea l'agent agid2
			Id idTeacher = new Id();
			idTeacher.setIdString("agid4");
			Agent teacherF = new Agent(idTeacher, "Angeles",
									   AgentComponent.PERSON, null);
			
			//es crea l'agent agid3
			idTeacher.setIdString("agid5");
			Agent teacherS = new Agent(idTeacher, "Juan", 
										AgentComponent.PERSON, null);

			//es crea el vector per afegir els grups esperats
			Vector<Agent> vectorAgentsExpected = new Vector<Agent>();
			
			vectorAgentsExpected.add(teacherF);
			vectorAgentsExpected.add(teacherS);
			
			//es fa la petici� que es vol testejar
			AgentIterator groupsIterator = authorizationRequest
											.getUsersAuthorizedAs("TEACHER");
			
			Agent agent = null;
			System.out.println("[testGetUsersAuthorizedAsTeacher]"
					+ " Grups: ");
			while (groupsIterator.hasNextAgent()) {
				agent = (Agent) groupsIterator.nextAgent();
				vectorAgentsObtain.add(agent);
				System.out.println("  -" + agent.getDisplayName());	
			}
			
			assertEquals(vectorAgentsExpected, vectorAgentsObtain);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		} catch (SharedException e) {
			fail("No deber�a haber fallado");
		}
		
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getUserAuthorizedAs() del AuthorizationRequest.
	 *
	 * Comprova que el m�tode retorna null perqu� el 
	 * rol passat �s un dels tipos especificats per� no
	 * hi ha cap grup d'aquest rol associat al curs.
	 * 
	 */
	public final void testGetUsersAuthorizedAsRollNotInCourse() {
				
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid6.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid6");
			
			//es fa la petici� que es vol testejar
			AgentIterator agentsIterator = authorizationRequest.
											getUsersAuthorizedAs("ADMIN");
		
			System.out.println("[testGetUsersAuthorizedAsUnKnownRoll]"
					+ " Agents trobats: " + agentsIterator);
			
			assertNull(agentsIterator);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
		
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getUserAuthorizedAs() del AuthorizationRequest.
	 *
	 * Comprova que el m�tode retorna null perqu� el 
	 * rol passat no �s un dels tipos especificats.
	 * 
	 */
	public final void testGetUsersAuthorizedAsUnKnownRoll() {
				
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//est� autenticat i que l'identificador d'aquest �s agid6.
			properties.setProperty("authentication_authenticated", "true");
			properties.setProperty("authentication_userId", "agid6");
			
			//es fa la petici� que es vol testejar
			AgentIterator agentsIterator = authorizationRequest.
											getUsersAuthorizedAs("UNKNOWN");
		
			System.out.println("[testGetUsersAuthorizedAsUnKnownRoll]"
					+ " Agents trobats: " + agentsIterator);
			
			assertNull(agentsIterator);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
		
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getUserAuthorizedAs() del AuthorizationRequest.
	 * 
	 * Comprova que el m�tode retorna null perqu� l'usuari no
	 * est� autentificat al sistema,
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=false
	 *  authentication_anonymousId=agid5
	 * 
	 */
	public final void testGetUsersAuthorizedAsNotAuthenticated() {
				
		try {
			
			//es modifica l'escenari per indicar que l'usuari
			//no est� autenticat i que l'identificador de l'usuari
			//an�nim �s agid5.
			properties.setProperty("authentication_authenticated", "false");
			properties.setProperty("authentication_anonymousId", "agid5");
			
			//es fa la petici� que es vol testejar
			AgentIterator agentsIterator = authorizationRequest.
											getUsersAuthorizedAs("TEACHER");
		
			System.out.println("[testGetUsersAuthorizedAsNotAuthenticated]"
					+ " Agents trobats: " + agentsIterator);
			
			assertNull(agentsIterator);
			
		} catch (RequestException e) {
			fail("No deber�a haber fallado");
		} catch (RemoteException e) {
			fail("No deber�a haber fallado");
		}
		
	}
	
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getProperty() del ConfigurationRequest.
	 * 
	 * Comprova que el m�tode retorna see com a valor 
	 * del par�metre de configuraci� "request",
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid6
	 * 
	 * # configuration keys (instance and aplication)
	 * configuration_confid1_app_properties=contextApp
	 * configuration_confid1_instance_properties=languageId
	 * # values of configuration keys
	 * configuration_confid1_instance_languageId="ca"
	 * configuration_confid1_app_contextApp=/modulsSalle
	 * 
	 */
	public final void testGetPropertyInstanceRequest() {
				
		//es modifica l'escenari per indicar que l'usuari
		//est� autenticat i que l'identificador d'aquest �s agid1.
		properties.setProperty("authentication_authenticated", "true");
		properties.setProperty("authentication_userId", "agid6");
		
		String value = configurationtRequest.getProperty("languageId");
		
		System.out.println("[testGetPropertyInstanceRequest]"
				+ " Valor de la propietat languageId: " + value);
		
		assertEquals("ca", value);	
		
	}
	
	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getProperty() del ConfigurationRequest.
	 * 
	 * Comprova que el m�tode retorna "/scorm2004rte_M21"
	 * com a valor del par�metre de configuraci� "contextApp",
	 * ja que al fitxer configuration.bin ho hem especificat aix�.
	 * Les lines que especifiquen aix� s�n aquestes:
	 *  authentication_authenticated=true
	 *  authentication_userId=agid6
	 * 
	 * # configuration keys (instance and aplication)
	 * configuration_confid1_app_properties=nxContextApp
	 * configuration_confid1_instance_properties=languageId
	 * # values of configuration keys
	 * configuration_confid1_instance_languageId=ca
	 * configuration_confid1_app_nxContextAppp=/modulsSalle
	 * 
	 */
	public final void testGetPropertyInstanceContextAPP() {
				
		//es modifica l'escenari per indicar que l'usuari
		//est� autenticat i que l'identificador d'aquest �s agid1.
		properties.setProperty("authentication_authenticated", "true");
		properties.setProperty("authentication_userId", "agid6");
		
		String value = configurationtRequest.getProperty("nxContextApp");
		
		System.out.println("[testGetPropertyInstanceContextAPP]"
				+ " Valor de la propietat contextAPP: " + value);
		
		assertEquals("/modulsSalle", value);	
		
	}

	/**
	 * Test per provar el bon funcionament del m�tode 
	 * getProperty() del ConfigurationRequest.
	 * 
	 * Comprova que el m�tode retorna null perqu�
	 * no hi ha cap par�metre que es digui "unknown"
	 * Al fitxer configuration.bin hem especificat lo 
	 * seg�ent:
	 * 
	 *  authentication_authenticated=true
	 *  authentication_userId=agid1
	 * 
	 * # configuration keys (instance and aplication)
	 * configuration_confid1_app_properties=contextApp
	 * configuration_confid1_instance_properties=request
	 * # values of configuration keys
	 * configuration_confid1_instance_request=see
	 * configuration_confid1_app_contextApp=/scorm2004rte_M21
	 * 
	 */
	/*public final void testGetPropertyInstanceUnKnown() {
				
		//es modifica l'escenari per indicar que l'usuari
		//est� autenticat i que l'identificador d'aquest �s agid1.
		properties.setProperty("authentication_authenticated", "true");
		properties.setProperty("authentication_userId", "agid1");
		
		String value = configurationtRequest.getProperty("unknown");
		
		System.out.println("[testGetPropertyInstanceRequest]"
				+ " Valor de la propietat unkown: " + value);
		
		assertNull(value);
		
	}*/
	
	
	/**
	 * M�tode principal per iniciar tots els test del 
	 * serveis del OKI-BUS.
	 * 
	 * @param args	Arguments d'entrada per l'execuci�
	 * 				del test.
	 */
	public static void main(final String[] args) {
		junit.swingui.TestRunner.run(ServicesTest.class);
	}
}
