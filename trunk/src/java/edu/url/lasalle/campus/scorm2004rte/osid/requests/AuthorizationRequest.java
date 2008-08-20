// $Id: AuthorizationRequest.java,v 1.7 2008/02/08 12:29:48 msegarra Exp $
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
package edu.url.lasalle.campus.scorm2004rte.osid.requests;


import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Vector;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Service;

import org.campusproject.components.AgentAdminComponent;
import org.campusproject.components.AgentComponent;
import org.campusproject.components.AuthenticationComponent;
import org.campusproject.components.AuthorizationComponent;
import org.campusproject.components.IdComponent;
import org.campusproject.osid.agent.Agent;
import org.campusproject.osid.agent.AgentIterator;
import org.campusproject.osid.agent.Group;
import org.campusproject.osid.id.Id;
import org.osid.agent.AgentException;
import org.osid.authentication.AuthenticationException;
import org.osid.authorization.AuthorizationException;
import org.osid.id.IdException;
import org.osid.shared.SharedException;

import edu.url.lasalle.campus.scorm2004rte.util.
							exception.NotFoundPropertiesException;
import edu.url.lasalle.campus.scorm2004rte.util.exception.RequestException;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;
import edu.url.lasalle.campus.scorm2004rte.util.
								resource.ServiceConnection;

/**
 * $Id: AuthorizationRequest.java,v 1.7 2008/02/08 12:29:48 msegarra Exp $
 * AuthorizationRequest
 * Gestion de las peticiones del servei Authorization.
 * Via per saber qui está autoritzat a fer que i quan.
 * 
 * @author M.Reyes Enginyeria La Salle mtoro@salle.url.edu
 * @version 1.0 27/09/2007 $Revision: 1.7 $ $Date: 2008/02/08 12:29:48 $
 * $Log: AuthorizationRequest.java,v $
 * Revision 1.7  2008/02/08 12:29:48  msegarra
 * Adaptacions al nou dummy v6.
 *
 * Revision 1.6  2008/01/30 15:51:04  toroleo
 * Eliminació imports no necessaris
 *
 * Revision 1.5  2008/01/25 16:12:17  toroleo
 * S'ha afegit els mètodes:
 *  -getUserAuthorized()
 *  -getUserAuthorizedAs(String)
 *
 * Revision 1.4  2008/01/23 15:16:08  toroleo
 * Deprecated del mètode "isUserAuthorized"
 * i nova versió adaptada al dummy v5 del mètode
 * "isAuthorized”.
 *
 * Revision 1.3  2008/01/16 21:25:17  toroleo
 * Modificació para l'adaptació de la classe a la
 * versió 5 del dummy de la UOC.
 *
 * Revision 1.2  2007/12/03 14:46:06  toroleo
 * S'ha afegit el meu e-mail a la capcelera de la classe.
 *
 * Revision 1.1  2007/11/22 09:46:50  toroleo
 * Implementació capa OSID consumidora
 *
 *
 */
public class AuthorizationRequest {

	/** Objecte del servei al que se li faran 
	 * les peticions. */
	//private AuthorizationComponent authorization = null;
	/**	Missatge d'error a propagar. */
	private String messageError = null;
	
	/**
	 * Constructor.
	 */
	public AuthorizationRequest() {
		
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		
		//Es crea una conexió al servei
		Service service;
		ServiceConnection sc = new ServiceConnection();    
		
	    try {
	    
	    	//S'inicia la conexió amb el servei 
	    	service = sc.connect();
			//uoc posible modificación cuando nos pasen el wsdl de los osids
	    	//s'indica la interficie a fer servir
			authorization =	service.getPort(AuthorizationComponent.class);
		
	    } catch (MalformedURLException e) {
	    	//S'escriu el missatge d'error al fitxer de logs.
			messageError = "MalFormedURL " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					AuthorizationRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			authorization = null;
	    	
		} catch (NotFoundPropertiesException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "NotFoundProperties " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					AuthorizationRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			authorization = null;
			
		}
		*/
		
	}
	 
	/**
	 * Comprova que un usuari té permisos per fer 
	 * una determinada acció (funció) sobre un determinat objecte (qualificador)
	 * 
	 * Maiye que diferencia hay entre esta función y la siguiente?
	 * No he entendido el funcionamiento de esta. Esperar a ver como
	 * lo hace en la versión última de la UOC. Agafa l'usuari que està 
	 * connectat
	 * 
	 * @deprecated
	 * @param aFunctionId  Identificador de l'acció que es pot 
	 * 					   realitzar (crear, editar...)
	 * @param aQualifierId Identificador de l'objecte sobre el que es 
	 * 						pot realitzar l'operació (un curs,...).
	 * @return Retorna true en cas de tingui permisos per fer l'acció 
	 * 		   sobre l'objecte especificat.
	 * 		   False en cas contrari.
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.
	 */
	public final boolean isUserAuthorized(final String aFunctionId, 
							final String aQualifierId) 
							throws RequestException, RemoteException {
		/*Maiye esta función no le encuentro el sentido. Está puesta en 
		 el documento versión 8 de los OSIDS pero no se como funciona 
		 ni para que. No se que diferencia podría tener
		 con la siguiente. 
		 */
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		
		if (authorization == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("AuthorizationRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei
		
		//es fa la petició del servei
		 //es fa la petició per recuperar l'objecte identificador del
		 //Function i del Qualifier
		IdRequest idRequest = new IdRequest();
		Id functionId = (Id) idRequest.getId(aFunctionId);
		Id qualifierId = (Id) idRequest.getId(aQualifierId);
		
		 //es fa la petició per saber si l'usuari té permisos
		boolean flag = authorization.isUserAuthorized(functionId,
										qualifierId);
		return flag;
		} 
		*/
		//no la entiendo
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		HttpServletRequest request = null;
		IdComponent idComponent  = new IdComponent(request);
		AuthorizationComponent authorization = 
								new AuthorizationComponent(request);
		boolean authorizated = false;
		Id functionId = null;
		Id qualifierId = null;
		
		//maiye el que está conectado al sistema
		try {
		
			functionId = (Id) idComponent.getId(aFunctionId);
			qualifierId = (Id) idComponent.getId(aQualifierId);
			authorizated = 
				 	authorization.isUserAuthorized(functionId, qualifierId);
		
		} catch (AuthorizationException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AuthorizationException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AuthorizationRequest.class.getName(), messageError);			
		
		} catch (IdException e1) {		
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "IdException " + e1.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AuthorizationRequest.class.getName(), messageError);	
		}
		
		
		return authorizated;
		
	} 
	
	/**
	 * Comprova si l'agent, usuari, té permisos per una determinada 
	 * acció (funció) sobre un determinat objecte (qualificador). 
	 * 
	 * @deprecated
	 * @param aFunctionId  Identificador de l'acció que es pot 
	 * 					   realitzar (crear, editar...)
	 * @param aQualifierId Identificador de l'objecte sobre el que es 
	 * 						pot realitzar l'operació (un curs,...).
	 * @return Retorna true en cas de tingui permisos per fer l'acció 
	 * 		   sobre l'objecte especificat.
	 * 		   False en cas contrari.
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.	
	 */
	public final boolean isAuthorized(final String aFunctionId,
						final String aQualifierId) 
						throws RequestException, RemoteException {
		
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
			
		if (authorization == null) {  //hi ha hagut problemes amb la connexió
		throw new RequestException("AuthorizationRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei
		
		//es fa la petició del servei
 		 //es recupera l'identificador de l'usuari que està ara en el sistema
		AuthenticationRequest authenticationRequest = 
							new AuthenticationRequest();
		Id agentId = authenticationRequest.getUserId();
		
		 //es fa la petició per recuperar l'objecte identificador del
		 //Function i del Qualifier
		IdRequest idRequest = new IdRequest();
		Id functionId = (Id) idRequest.getId(aFunctionId);
		Id qualifierId = (Id) idRequest.getId(aQualifierId);
		
		 //es fa la petició per saber si l'usuari anteror té o no permisos
		boolean authorized = authorization.isAuthorized(agentId,functionId,
								qualifierId);
		return authorized;
		} 
		
		*/
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		HttpServletRequest request = null;
		AuthorizationComponent authorization = 
								new AuthorizationComponent(request);
		AuthenticationComponent authentication = 
								new AuthenticationComponent(request);
		IdComponent idComponent = new IdComponent(request);
		boolean authorizated = false;
		Id functionId = null;
		Id qualifierId = null;
		
		Id agentId;
		try {
			functionId = (Id) idComponent.getId(aFunctionId);
			qualifierId = (Id) idComponent.getId(aQualifierId);
			agentId = (Id) authentication.getUserId();
			authorizated = 
				authorization.isAuthorized(agentId, functionId, qualifierId);
		
		} catch (AuthorizationException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AuthorizationException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AuthorizationRequest.class.getName(), messageError);
			
		} catch (AuthenticationException e1) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AuthenticationException " + e1.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AuthorizationRequest.class.getName(), messageError);
			
		} catch (IdException e2) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "IdException " + e2.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AuthorizationRequest.class.getName(), messageError);
		
		}
		
		return authorizated;

		}
	
	/**
	 * Comprova si l'agent, usuari, té permisos d'actuació en l'aplicació
	 * en el context en el que aquesta s'executa (en el curs des del que
	 * s'ha llançat l'aplicació) i quin rol té. 
	 * 
	 * @return En cas de que l'usuari tingui permisos d'actuació:
	 * 			El rol que juga l'usuari pel curs en el que s'està executant
	 * 			 l'aplicació.
	 * 		    O Null en cas contrari.
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.	
	 */
	public final String isAuthorized() 
						throws RequestException, RemoteException {
		
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		 uoc no está del todo implementado ya que la uoc no lo tiene
		 hecho en el dummy y no ha especificadocomo podrá funcionar.
		 	
		if (authorization == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("AuthorizationRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei
		
			//es fa la petició del servei
	 		 //es recuperen les dades de l'usuari que està ara 
	 		 //en el sistema
			AgentRequest agentRequest = new AgentRequest();
			Agent dataAgent = agentRequest.getAgent();
			
			 //es fa la petició per recuperar la instància del grup del curs 
			 //en el que està registrada i treballant la nostre aplicació		
			Group group = (Group) authorization.getInstanceContainerGroup();
			 //es recuperen els diferents grups, com els de professors 
			 //i alumnes, que té el curs
			// uoc falta que lo definan group.getGroups(false);
			 //Es mira si en quin d'aquest grups hi és l'usuari i el 
			 //rol que té. uoc falta hacer
			String roll = null;
			
			return roll;
		} //del if (authorization == null)
		
		*/
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		String roll = null;	
		Agent dataAgent;
		HttpServletRequest request = null;
		AuthorizationComponent authorization = 
								new AuthorizationComponent(request);
		AgentRequest agentRequest = new AgentRequest();
		
		try {
			//Maiye esto es una chapuza para sortear los problemas
			//de no funcionamiento del dummy.
			
			//es recuperen les dades de l'agent connectat al sistema
			dataAgent = agentRequest.getAgent();

			//es comprova si hi ha usuari connectat o s'ha entrat com
			//usuari anònim
			/*if ("Anonymous".equals(dataAgent.getDisplayName())) {
				//usuari anònim
				return roll;
			}*/
			
			//es recupera la instància del curs en el s'està 
			//executant l'aplicació
			//Group group = (Group) authorization.getInstanceContainerGroup();
			AgentAdminComponent aac = new AgentAdminComponent(null);
			IdComponent ic = new IdComponent(null);
			Group group = (Group) aac.getGroup(ic.getId("grid3"));
			
			boolean contains = false;
			
			AgentIterator agentGroupIt = (AgentIterator) 
			group.getMembers(false);

			while (agentGroupIt.hasNextAgent()) {
				Agent agentGroup = agentGroupIt.nextAgent();
				if (dataAgent.equals(agentGroup)) {
					contains = true;
					break;
				}
			}
			
			//es comprova si l'agent pertany al curs
			//if (group.contains(dataAgent, false)) {
			if (contains) {
				//pertany
				
				//es comprova si es profesor o alumne del curs 
				if (agentRequest.isAgentOfRoll("TEACHER")) {  
					roll = "TEACHER";
				} else if (agentRequest.isAgentOfRoll("STUDENT")) {
					roll = "STUDENT";
				} 				
			//} // del if (group.contains(dataAgent, false))
			}
			
				
		
	/*	} catch (AuthorizationException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AuthorizationException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AuthorizationRequest.class.getName(), messageError);
		*/	
		} catch (AgentException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AgentException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AuthorizationRequest.class.getName(), messageError);

		} catch (SharedException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "SharedException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AuthorizationRequest.class.getName(), messageError);
		}
		
		
		return roll;

		}

	/**
	 * Comprova si l'usuari està autentificat al sistema  
	 * i retorna tots els grups que estan 
	 * relacionats al curs que està executant l'aplicació.
	 * 
	 * Per exemple podria retornar el grup dels professors del curs
	 * i el dels estudiants. 
	 * 
	 * @return En cas de que l'usuari estigui autentificat retorna
	 * 			els grups que estiguin relacionats al curs.
	 * 		    O Null en cas contrari o en cas de que no hi hagin grups
	 * 			relacinats al curs.
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.	
	 */
	public final AgentIterator getUsersAuthorized() 
						throws RequestException, RemoteException {
		
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		 uoc no está del todo implementado ya que la uoc no lo tiene
		 hecho en el dummy y no ha especificadocomo podrá funcionar.
			
		if (authorization == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("AuthorizationRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei

			//es fa la petició del servei
	 		 //es recuperen les dades per saber si l'usuari està connectat
			 //al sistema (autentificat) o a entrat com a usuari anònim
			AuthenticationRequest authenticationComponent =
												new AuthenticationRequest();
			if (!authenticationComponent.isUserAuthenticated()) {
				//l'usuari no està autentificat.
				return null;
			}
			
			 //es fa la petició per recuperar la instància del grup del curs 
			 //en el que està registrada i treballant la nostre aplicació		
			Group group = (Group) authorization.getInstanceContainerGroup();
			 //es recuperen els diferents grups, com els de professors 
			 //i alumnes, que té el curs
			// uoc falta que lo definan group.getGroups(false); el resultado 
			//de esto es lo que se retornará
			return null;
		} //del if (authorization == null)
		
		*/
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		AgentIterator agentIterator = null;
		HttpServletRequest request = null;
		Vector<Agent> vectorGroups = new Vector<Agent>();
		AuthenticationRequest authenticationRequest = 
									new AuthenticationRequest();
		AgentComponent agentComponent = new AgentComponent(request);
		
		try {
			//Maiye esto es una chapuza para sortear los problemas
			//de no funcionamiento del dummy.

			
			//es comprova si l'usuari està autentificat al sistema
			if (!authenticationRequest.isUserAuthenticated()) {
				return null;
			}
			
			//es recupera la instància del curs 
			//Group group = (Group) authorization.getInstanceContainerGroup();

			//es crea un identificador del grup de professors 
			Id idTeacher = new Id();
			idTeacher.setIdString("grid4");
			
			//es recuperen les dades del grup professors
			Group teachers = (Group) agentComponent.getGroup(idTeacher);
			
			//es crea un identificador del grup de estudiants
			Id idStudent = new Id();
			idStudent.setIdString("grid5");

			//es recuperen les dades del grup estudiants
			Group students = (Group) agentComponent.getGroup(idStudent);
			
			//es crea un vector amb els dos grups del curs
			vectorGroups.add(teachers);
			vectorGroups.add(students);
			
			//es crea un iterador dels grups per treballar mijor amb ells
			agentIterator  = new AgentIterator(vectorGroups);
			
		} catch (AgentException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AgentException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AuthorizationRequest.class.getName(), messageError);
		} catch (SharedException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "SharedException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AuthorizationRequest.class.getName(), messageError);
		}	
		
		return agentIterator;

		}
	
	/**
	 * Comprova si l'usuari està autentificat al sistema i retorna 
	 * tots els agents autoritzats al curs que tenen el rol indicat.
	 * 
	 * Per exemple podria retornar els agents del grup dels professors.
	 * 
	 * @param aRoll Rol que han de jugar els agents autoritzats 
	 * 				 al curs que es volen recuperar.
	 * 
	 * @return Iterador amb els agents que estan autoritzats al 
	 * 			curs amb el rol indicat. 
	 * 			Els rols poden ser:
	 * 				TEACHER, STUDENT, COURSE, ADMIN, GROUP
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.	
	 */
	public final AgentIterator getUsersAuthorizedAs(final String aRoll) 
						throws RequestException, RemoteException {
		
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		 uoc no está del todo implementado ya que la uoc no lo tiene
		 hecho en el dummy y no ha especificadocomo podrá funcionar.
			
		if (authorization == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("AuthorizationRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei

			//es comprova que s'hagi passat una cadena indicant el rol
			//i que sigui un rol dels definits
			if ((aRoll == null) || ("".equals(aRoll)) ||
				(!"TEACHER".equals(aRoll)) &&
				(!"STUDENT".equals(aRoll)) &&
				(!"ADMIN".equals(aRoll)) &&
				(!"GROUP".equals(aRoll)) &&
				(!"COURSE".equals(aRoll))) {
				return null;
			}
			
			//es fa la petició del servei
	 		 //es recuperen les dades per saber si l'usuari està connectat
			 //al sistema (autentificat) o a entrat com a usuari anònim
			AuthenticationRequest authenticationComponent =
												new AuthenticationRequest();
			if (!authenticationComponent.isUserAuthenticated()) {
				//l'usuari no està autentificat.
				return null;
			}
			
			 //es fa la petició per recuperar la instància del grup del curs 
			 //en el que està registrada i treballant la nostre aplicació		
			Group group = (Group) authorization.getInstanceContainerGroup();
			 //es recuperen els diferents grups, com els de professors 
			 //i alumnes, que té el curs
			// uoc falta que lo definan group.getGroups(false); el resultado 
			//de esto es lo que se retornará
			
			//Maiye falta recorrer los grupos para sólo devolver el grupo 
			//con el rol que nos interesa.
			
			return null;
		} //del if (authorization == null)
		
		*/
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		AgentIterator agentIterator = null;
		HttpServletRequest request = null;
		AuthenticationRequest authenticationRequest = 
									new AuthenticationRequest();
		AgentComponent agentComponent = new AgentComponent(request);
		
		try {
			//Maiye esto es una chapuza para sortear los problemas
			//de no funcionamiento del dummy.

			
			//es comprova si l'usuari està autentificat al sistema
			if (!authenticationRequest.isUserAuthenticated()) {
				return null;
			}
			
			//es comprova que s'hagi passat una cadena indicant el rol
			if ((aRoll == null) || ("".equals(aRoll))) {
				return null;
			}
			
			//es comprova si el rol es professor o estudiant
			if ("TEACHER".equals(aRoll)) {
				//el rol es professor. Es retornen els 
				//agents que juguen aquest rol
				
				//es crea un identificador del grup de professors 
				Id idTeacher = new Id();
				idTeacher.setIdString("grid4");
				
				//es recuperen les dades del grup professors
				Group teachers = (Group) agentComponent.getGroup(idTeacher);
				
				agentIterator = (AgentIterator) teachers.getMembers(false);
				
			} else if ("STUDENT".equals(aRoll)) {
				//el rol es estudiant. Es retornen els 
				//agents que juguen aquest rol
				
				//es crea un identificador del grup de estudiants
				Id idStudent = new Id();
				idStudent.setIdString("grid5");

				//es recuperen les dades del grup estudiants
				Group students = (Group) agentComponent.getGroup(idStudent);
				
				agentIterator = (AgentIterator) students.getMembers(false);
			}
						
		} catch (AgentException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AgentException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AuthorizationRequest.class.getName(), messageError);
		} catch (SharedException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "SharedException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AuthorizationRequest.class.getName(), messageError);
		}	
		
		return agentIterator;

		}

}
