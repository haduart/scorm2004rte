// $Id: AgentRequest.java,v 1.8 2008/02/08 12:29:48 msegarra Exp $
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
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Service;

import org.campusproject.components.AgentComponent;
import org.campusproject.components.AuthenticationComponent;
import org.campusproject.components.IdComponent;
import org.campusproject.osid.agent.Agent;
import org.campusproject.osid.agent.AgentIterator;
import org.campusproject.osid.agent.Group;
import org.campusproject.osid.agent.GroupType;
import org.campusproject.osid.id.Id;
import org.osid.agent.AgentException;
import org.osid.authentication.AuthenticationException;
import org.osid.id.IdException;

import edu.url.lasalle.campus.scorm2004rte.util.exception.
		NotFoundPropertiesException;
import edu.url.lasalle.campus.scorm2004rte.util.exception.RequestException;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;
import edu.url.lasalle.campus.scorm2004rte.util.resource.ServiceConnection;


/**
 * $Id: AgentRequest.java,v 1.8 2008/02/08 12:29:48 msegarra Exp $
 * AgentRequest.
 * Gestiona les peticions del servei Agent.
 * Representa a un usuari o procés que invoca un servei.
 * Amb aquesta classe es poden recuperar les dades
 * d'aquest usuari o servei. 
 *  
 * @author M.Reyes Enginyeria La Salle mtoro@salle.url.edu
 * @version 1.0 19/09/2007 $Revision: 1.8 $ $Date: 2008/02/08 12:29:48 $
 * $Log: AgentRequest.java,v $
 * Revision 1.8  2008/02/08 12:29:48  msegarra
 * Adaptacions al nou dummy v6.
 *
 * Revision 1.7  2008/01/30 15:51:04  toroleo
 * Eliminació imports no necessaris
 *
 * Revision 1.6  2008/01/25 16:11:33  toroleo
 * Modificació de la funció createGroupType()
 * per poder-la crida des de fora.
 *
 * Revision 1.5  2008/01/23 15:19:03  toroleo
 * S'han marcat com a deprecated tots els mètodes que
 * no es fan servir.
 *
 * Revision 1.4  2008/01/23 15:14:16  toroleo
 * Modificació nom de l'empresa.
 *
 * Revision 1.3  2008/01/16 21:25:17  toroleo
 * Modificació para l'adaptació de la classe a la
 * versió 5 del dummy de la UOC.
 *
 * Revision 1.2  2007/12/03 14:50:47  toroleo
 * S'ha afegit el nou mètode “getGroupOfUser” que permetrà
 * recuperar el nom del grup al que pertany l'agent. A més
 * s'ha afegit el meu e-mail a la capcelera de la classe.
 *
 * Revision 1.1  2007/11/22 09:46:50  toroleo
 * Implementació capa OSID consumidora
 *
 *
 */
public class AgentRequest {

	/** Objecte del servei al que se li faran 
	 * les peticions. */
	//private AgentComponent agent = null;
	/**	Missatge d'error a propagar. */
	private String messageError = null;
	
    
	/**
	 * Constructor.
	 */
	public AgentRequest() {
		
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
	    	agent = service.getPort(AgentComponent.class);  

	    } catch (MalformedURLException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "MalFormedURL " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					AgentRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			agent = null;
		
	    } catch (NotFoundPropertiesException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "NotFoundProperties " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					AgentRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			agent = null;
	
	    } catch (ClassNotFoundException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "ClassNotFound " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					AgentRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			agent = null;
		} 
	    
	    */
		
	}
	
	
	/**
	 * Obté les dades de l'usuari que està connectat.
	 * 
	 * @return Retorna les dades de l'usuari o
	 * 		   Null en cas de que hi hagués algun problema.
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.	
	 */
	public final Agent getAgent() 
						throws RequestException, RemoteException {
		
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		Maiye segun el dummy el servicio puede tener constructor 
		con parámetros OJO!!
		
		if (agent == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("AgentRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei

			//es fa la petició del servei
	 		 //es recupera l'objecte identificador de l'usuari que està
			 //connectat al sistema
			AuthenticationRequest authenticationRequest = 
											new AuthenticationRequest();
			Id agentId = authenticationRequest.getUserId();
			
			 //es fa la petició per recuperar les dades de l'usuari
			Agent dadesAgent = (Agent)agent.getAgent(agentId);
			return dadesAgent;
			
		} 
		*/
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		HttpServletRequest request = null;
		AuthenticationComponent authentication = 
						new AuthenticationComponent(request);
		AgentComponent agent = new AgentComponent(request);
		Agent dadesAgent = null;
		
		
		Id agentId;
		try {
			//s'obté l'identificador de l'usuari que està connectat
			agentId = (Id) authentication.getUserId();
			
			//s'obté les dades de l'agent connectat
			dadesAgent = (Agent) agent.getAgent(agentId);
			
		} catch (AuthenticationException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AuthenticacionException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AgentRequest.class.getName(), messageError);
		} catch (AgentException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AgentException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AgentRequest.class.getName(), messageError);		
		}
						
		return dadesAgent;

	}
	
	
	/**
	 * Obté les dades del usuari que té el número 
	 * d'identificació passat.
	 * 
	 * @deprecated
	 * @param anIdUser Número d'identificació de l'usuari.
	 * @return Retorna les dades de l'usuari o
	 * 		   Null en cas de que hi hagués algun problema.
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.	
	 */
	public final Agent getAgent(final String anIdUser) 
						throws RequestException, RemoteException {
		
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		Maiye segun el dummy el servicio puede tener constructor 
		con parámetros OJO!!
		
		if (agent == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("AgentRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei

			//es fa la petició del servei
	 		 //es recupera l'objecte identificador de l'usuari del 
 			 //que es volen les dades
			IdRequest idRequest = 
								new IdRequest();
			Id agentId = idRequest.getId(anIdUser);
			
			 //es fa la petició per recuperar les dades de l'usuari
			Agent dadesAgent = (Agent)agent.getAgent(agentId);
			return dadesAgent;
			
		} 
		*/
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		HttpServletRequest request = null;
		IdComponent id = new IdComponent(request);
		AgentComponent agent = new AgentComponent(request);
		Agent dadesAgent = null;
		
		
		Id agentId;
		try {
			agentId = (Id) id.getId(anIdUser);
			dadesAgent = (Agent) agent.getAgent(agentId);
		} catch (IdException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "IdException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AgentRequest.class.getName(), messageError);
			
		} catch (AgentException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AgentException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AgentRequest.class.getName(), messageError);		
		}
						
		return dadesAgent;

	}

	/**
	 * Comprova si l'usuari que està connectat al sistema pertany 
	 * a un grup concret o a algun dels seus subgrups.
	 * 
	 * @deprecated
	 * @param anIdGroup Número d'identificació del grup al 
	 * 					que es vol saber si pertany o no l'usuari.
	 * @param aSubGroup Indica si es vol fer la recerca no 
	 * 					només al grup si no també als subgrups.
	 * @return	True en cas de que l'usuari pertanyi al grup.
	 * 			False en cas contrari.
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.	
	 */
	public final boolean containsGrup(final String anIdGroup, 
						 final boolean aSubGroup) 
						 throws RequestException, RemoteException {

		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		Maiye segun el dummy el servicio puede tener constructor 
		con parámetros OJO!!
		
		if (agent == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("AgentRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei
			
			//es fa la petició del servei
			 //es fa la petició de recuperació de dades de l'usuari passat
			Agent dataAgent = getAgent();
			
			 //es fa la petició per obtenir l'identificador del grup
			IdRequest idRequest = 
						new IdRequest();
			Id groupId = idRequest.getId(anIdGroup);
			
			 //es fa la petició per recuperar les dades del grup
			Group group = (Group) agent.getGroup(groupId);
			
			 //es fa la recerca de l'usuari dins del grup
			boolean contained = group.contains(dataAgent, aSubGroup);
			
			return contained;
		
		} 
		*/
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		boolean contained = false;
		Agent dataAgent = null;
		Group dataGroup = null;
		Id groupId = new Id();
		HttpServletRequest request = null;
		IdComponent idComponent = new IdComponent(request);
		AgentComponent agentComponent = new AgentComponent(request);
		
		
		//es recuperen les dades de l'agent
		dataAgent = (Agent) getAgent();
		
		if (dataAgent != null) {
		

			try {

				//es busca l'identificador del grup
				groupId = (Id) idComponent.getId(anIdGroup);
				//es recuperen les dades del grup
				dataGroup = (Group) agentComponent.getGroup(groupId);
				
				if (dataGroup != null) {
					//es comprova si el grup conté a l'agent
					contained = dataGroup.contains(dataAgent, aSubGroup);
				}
				
			} catch (IdException e) {
				//S'escriu el missatge d'error al fitxer de logs.
				messageError = "IdException " + e.getMessage();
				LogControl log = LogControl.getInstance();
				log.writeMessage(Level.SEVERE, 
					AgentRequest.class.getName(), messageError);
			} catch (AgentException e1) {
				//S'escriu el missatge d'error al fitxer de logs.
				messageError = "AgentException " + e1.getMessage();
				LogControl log = LogControl.getInstance();
				log.writeMessage(Level.SEVERE, 
					AgentRequest.class.getName(), messageError);
			} 
		
		}
	
		return contained;
	}

	
	/**
	 * Comprova si un usuari en concret pertany a un grup
	 * concret o a algun dels seus subgrups.
	 *  
	 * @deprecated
	 * @param anIdUser Número d'identificació de l'usuari 
	 * 				   del que es vol comprovar la seva 
	 * 				   pertinença a un grup.
	 * @param anIdGroup Número d'identificació del grup al 
	 * 					que es vol saber si pertany o no l'usuari.
	 * @param aSubGroup Indica si es vol fer la recerca no 
	 * 					només al grup si no també als subgrups.
	 * @return	True en cas de que l'usuari pertanyi al grup.
	 * 			False en cas contrari.
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.	
	 */
	public final boolean containsGrup(final String anIdUser, 
						final String anIdGroup, final boolean aSubGroup) 
						throws RequestException, RemoteException {
		
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		Maiye segun el dummy el servicio puede tener constructor 
		con parámetros OJO!!
		
		if (agent == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("AgentRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei

			//es fa la petició del servei
			 //es fa la petició de recuperació de dades de l'usuari passat
			Agent dataAgent = getAgent(anIdUser);
					
			 //es fa la petició per obtenir l'identificador del grup
			IdRequest idRequest = 
						new IdRequest();
			Id groupId = idRequest.getId(anIdGroup);
			
			 //es fa la petició per recuperar les dades del grup
			Group group = (Group) agent.getGroup(groupId);
			
			 //es fa la recerca de l'usuari dins del grup
			boolean contained = group.contains(dataAgent, aSubGroup);
			
			return contained;
			
		} 
		*/
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		boolean contained = false;
		Agent dataAgent = null;
		Group dataGroup = null;
		Id groupId = new Id();
		HttpServletRequest request = null;
		IdComponent idComponent = new IdComponent(request);
		AgentComponent agentComponent = new AgentComponent(request);
		
		
		//es recuperen les dades de l'agent
		dataAgent = (Agent) getAgent(anIdUser);
		
		if (dataAgent != null) {

			try {

				//es busca l'identificador del grup
				groupId = (Id) idComponent.getId(anIdGroup);
				//es recuperen les dades del grup
				dataGroup = (Group) agentComponent.getGroup(groupId);
				
				if (dataGroup != null) {
					//es comprova si el grup conté a l'agent
					contained = dataGroup.contains(dataAgent, aSubGroup);
				}
				
			} catch (IdException e) {
				//S'escriu el missatge d'error al fitxer de logs.
				messageError = "IdException " + e.getMessage();
				LogControl log = LogControl.getInstance();
				log.writeMessage(Level.SEVERE, 
					AgentRequest.class.getName(), messageError);
			} catch (AgentException e1) {
				//S'escriu el missatge d'error al fitxer de logs.
				messageError = "AgentException " + e1.getMessage();
				LogControl log = LogControl.getInstance();
				log.writeMessage(Level.SEVERE, 
					AgentRequest.class.getName(), messageError);
			} 
		
		}
		
		return contained;
	}

	
	/**
	 * Recupera tots els agents que pertanyen al grup especificat 
	 * i/o als seus subgrups.
	 * 
	 * @deprecated
	 * @param anIdGroup Número d'identificació del grup del 
	 * 					que es vol recuperar tots els agents.
	 * @param aSubGroup Indica si es vol fer la recerca no 
	 * 					només al grup si no també als subgrups.
	 * @return	Retorna el conjunt d'agents que pertanyen al grup.
	 * maiye, mirar que pasa si no hay agentes asociados al grupo.
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.
	 */
	public final AgentIterator getAgentsGroup(final String anIdGroup,
			final boolean aSubGroup) 
			throws RequestException, RemoteException {

		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		Maiye segun el dummy el servicio puede tener constructor 
		con parámetros OJO!!
		
		if (agent == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("AgentRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei
		
			//es fa la petició del servei
		
			 //es fa la petició per obtenir l'identificador del grup
			IdRequest idRequest = 
						new IdRequest();
			Id groupId = idRequest.getId(anIdGroup);
			
			 //es fa la petició per recuperar les dades del grup
			Group group = (Group) agent.getGroup(groupId);
			
			 //es fa la recerca de l'usuari dins del grup
			AgentIterator agentSet = 
			 		(AgentIterator) group.getMembers(aSubGroup);
			
			return agentSet;
		
		} 
		*/
	
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		Group dataGroup = null;
		AgentIterator agentSet = null;
		Id groupId = new Id();
		HttpServletRequest request = null;
		IdComponent idComponent = new IdComponent(request);
		AgentComponent agentComponent = new AgentComponent(request);
		
		
		try {

			//es busca l'identificador del grup
			groupId = (Id) idComponent.getId(anIdGroup);
			//es recuperen les dades del grup
			dataGroup = (Group) agentComponent.getGroup(groupId);
			
			if (dataGroup != null) {
				//es recuperen els agents que formen part del grup
				agentSet = (AgentIterator) dataGroup.getMembers(aSubGroup);
			}
			
		} catch (IdException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "IdException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AgentRequest.class.getName(), messageError);
		} catch (AgentException e1) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AgentException " + e1.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				AgentRequest.class.getName(), messageError);
		} 
		
		return agentSet;
	}

	///////////////

	/**
	 * Recupera el rol al que juga l'agent dins del sistema.
	 * 
	 * Maiye podría ser que fuese fuera con la versión definitiva del webService
	 * Ya q no tiene mucho sentido según la estructura y forma de trabajar del 
	 * Authorization.
	 * 
	 * @return	Nom del rol que té l'usuari. Pot ser:
	 * 			TEACHER,STUDENT,ADMIN,COURS o GROUP
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.	
	 */
	public final String getAgentRoll() 
						 throws RequestException, RemoteException {

		//Maiye es una forma un poco burda. Mirar si se podria hacer de
		//otra forma más fina
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		
		if (isAgentOfRoll("TEACHER")) {
			return "TEACHER";
		}
		if (isAgentOfRoll("STUDENT")) {
			return "STUDENT";
		}
		if (isAgentOfRoll("ADMIN")) {
			return "ADMIN";
		}
		if (isAgentOfRoll("COURSE")) {
			return "COURSE";
		}
		if (isAgentOfRoll("GROUP")) {
			return "GROUP";
		}
		
		return null;
		
	}

	
	/**
	 * Comprova si l'usuari connectat juga el rol passat en el sistema.
	 * 
	 * Maiye podría ser que fuese fuera con la versión definitiva del webService
	 * Ya q no tiene mucho sentido según la estructura y forma de trabajar del 
	 * Authorization. 
	 * 
	 * @param aRoll	Rol al que és vol comprovar si juga l'usuari connectat.
	 * @return	True en cas de que l'usuari tingui el rol passat.
	 * 			False en cas contrari.
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.	
	 */
	public final boolean isAgentOfRoll(final String aRoll) 
						 throws RequestException, RemoteException {
		
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		Maiye segun el dummy el servicio puede tener constructor 
		con parámetros OJO!!
		
		if (agent == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("AgentRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei
			
			//es crea l'objecte tipus del rol al que volem saber si 
			//pertany l'usuari
			GroupType groupType = createGroupType(aRoll);
			
			if (groupType == null){
				return false;
			}

			//es fa la petició del servei
			 //es fa la petició de recuperació de dades de l'usuari passat
			Agent dataAgent = getAgent();

			//es comprova que hi hagin dades de l'agent
			if (dataAgent != null) { //hi han
				//es recuperen tots els grups que siguin 
				//del tipus del Rol passat
				AgentIterator agentIterator = 
					(AgentIterator) agent.getGroupsByType(groupType);

				Group dataGroup = null;
				
				//es comproven tots els grups en busca de l'agent
				while (agentIterator.hasNextAgent()) {
					//es recuperen les dades del grup
					dataGroup = (Group) agentIterator.nextAgent();
					//es comprova si l'agent forma part del grup
					if (dataGroup.contains(dataAgent, false)) {
						return true;
					}
				} //del while (agentIterator.hasNextAgent())
				
			} //del if (dataAgent != null)
			
			return false;
		
		} 
		*/
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		
		//es crea l'objecte tipus del rol al que volem saber si pertany l'usuari
		GroupType groupType = createGroupType(aRoll);
		
		if (groupType == null) {
			return false;
		}
		
		Agent dataAgent = null;
		Group dataGroup = null;
		AgentIterator agentIterator = null;
		HttpServletRequest request = null;
		AgentComponent agentComponent = new AgentComponent(request);
		
		
		//es recuperen les dades de l'agent
		dataAgent = (Agent) getAgent();
		
		if (dataAgent != null) {
		

			try {

				//es recuperen tots els grups que siguin 
				//del tipus del Rol passat
				agentIterator = 
					(AgentIterator) agentComponent.getGroupsByType(groupType);

				//es comproven tots els grups en busca de l'agent
				while (agentIterator.hasNextAgent()) {
					//es recuperen les dades del grup
					dataGroup = (Group) agentIterator.nextAgent();
					//es comprova si l'agent forma part del grup
					/*if (dataGroup.contains(dataAgent, false)) {
						return true;
					}*/
					AgentIterator agentGroupIt = (AgentIterator) 
												dataGroup.getMembers(false);
					
					while (agentGroupIt.hasNextAgent()) {
						Agent agentGroup = agentGroupIt.nextAgent();
						if (dataAgent.equals(agentGroup)) {
							return true;
						}
					}
					
				}
				
				
			} catch (AgentException e1) {
				//S'escriu el missatge d'error al fitxer de logs.
				messageError = "AgentException " + e1.getMessage();
				LogControl log = LogControl.getInstance();
				log.writeMessage(Level.SEVERE, 
					AgentRequest.class.getName(), messageError);
			} //del try catch
		
		} //del if (dataAgent != null)
	
		return false;
	}	
	
	/**
	 * Crea un objecte GroupType amb les les característiques 
	 * 			típiques del tipus indicat.
	 * 
	 * uoc esto puede cambiar en cualquier momento.
	 * 
	 * @param aType	Indica de que tipus ha de ser el GroupType.
	 * 				Els tipus admesos són:
	 * 					TEACHER, STUDENT, ADMIN, COURSE i GROUP 
	 * @return	Un objecte GroupType amb les característiques 
	 * 			típiques del tipus indicat al cridar el mètode.
	 */
	public final GroupType createGroupType(final String aType) {
		
		if ((aType == null) || ("".equals(aType))) {
			return null;
		}
		
		GroupType groupType = null;
		
		
		if ("TEACHER".equals(aType)) {			
			//(authority, domain, keyword, description)
			groupType = new GroupType("CAMPUSPROJECT.ORG", "AGENT",
									"TEACHER", "Grup de professors");
			
		} else if ("STUDENT".equals(aType)) {			
			//(authority, domain, keyword, description)
			groupType = new GroupType("CAMPUSPROJECT.ORG", "AGENT",
									"STUDENT", "Grup d'estudiants");
			
		} else if ("ADMIN".equals(aType)) {
			//(authority, domain, keyword, description)
			groupType = new GroupType("CAMPUSPROJECT.ORG", "AGENT",
									"ADMIN", "Grup d'administradors");
			
		} else if ("COURSE".equals(aType)) {
			//(authority, domain, keyword, description)
			groupType = new GroupType("CAMPUSPROJECT.ORG", "AGENT",
									"COURSE", "Classe o curs");
			
		} else if ("GROUP".equals(aType)) {
			//(authority, domain, keyword, description)
			groupType = new GroupType("CAMPUSPROJECT.ORG", "AGENT", 
									"GROUP", "Grup genric");
		}
		
		return groupType;
		
	}
}

