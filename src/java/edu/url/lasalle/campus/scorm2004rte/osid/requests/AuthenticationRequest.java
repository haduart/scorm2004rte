// $Id: AuthenticationRequest.java,v 1.5 2008/01/30 15:51:46 toroleo Exp $
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
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Service;

import org.campusproject.components.AuthenticationComponent;
import org.campusproject.osid.id.Id;
import org.osid.authentication.AuthenticationException;


//import edu.campus.osid.uoc.prova.AuthenticationComponent;
import edu.url.lasalle.campus.scorm2004rte.util.exception.
		NotFoundPropertiesException;
import edu.url.lasalle.campus.scorm2004rte.util.exception.RequestException;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;
import edu.url.lasalle.campus.scorm2004rte.util.resource.ServiceConnection;


/**
 * $Id: AuthenticationRequest.java,v 1.5 2008/01/30 15:51:46 toroleo Exp $
 * AuthenticationRequest
 * Gestiona les peticions del servei Authentication.
 * Verificar si un usuari está autoritzat a entrar en
 * el sistema.
 * 
 * @author M.Reyes Enginyeria La Salle mtoro@salle.url.edu
 * @version 1.0 19/09/2007 $Revision: 1.5 $ $Date: 2008/01/30 15:51:46 $
 * $Log: AuthenticationRequest.java,v $
 * Revision 1.5  2008/01/30 15:51:46  toroleo
 * Modificació d'un comentari.
 *
 * Revision 1.4  2008/01/23 15:14:16  toroleo
 * Modificació nom de l'empresa.
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
public class AuthenticationRequest {

	/** Objecte del servei al que se li faran 
	 * les peticions. */
	//private AuthenticationComponent authentication = null;
	/**	Missatge d'error a propagar. */
	private String messageError = null;
    
	/**
	 * Constructor.
	 */
	public AuthenticationRequest() {
		
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
	    	authentication = service.getPort(AuthenticationComponent.class);  

	    } catch (MalformedURLException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "MalFormedURL " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					AuthenticationRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			authentication = null;
		
	    } catch (NotFoundPropertiesException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "NotFoundProperties " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					AuthenticationRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			authentication = null;
	
	    } catch (ClassNotFoundException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "ClassNotFound " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					AuthenticationRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			authentication = null;
		}  
		
		*/
	}
	
	/**
	 * Petició per verificar si l'usuari que s'indica en 
	 * el LDAP està realment autentificat o no al sistema.
	 * 
	 * @return true si té autorització, false en cas contrari
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 */
	public final boolean isUserAuthenticated() 
						throws RequestException {
		
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		Maiye segun el dummy el servicio puede tener constructor 
		con parámetros OJO!!
		
		if (authentication == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("AuthenticationRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei

			//es fa la petició del servei
			boolean authenticated = authentication.isUserAuthenticated();
			return authenticated;
		} 
		*/
		
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		//instancia del objecte de authentication dummy
		HttpServletRequest request = null;
		AuthenticationComponent authentication = 
							new AuthenticationComponent(request);
		boolean authenticated = false;
		
		try {
			authenticated = authentication.isUserAuthenticated();
		} catch (AuthenticationException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AuthenticationException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					AuthenticationRequest.class.getName(), messageError);
		}
		
		return authenticated;
	}
	
	/**
	 * Petició per obtenir l'identificador de l'usuari que està al sistema.
	 * 
	 * @return	L'identificador de l'usuari que està connectat al 
	 * 			sistema o, si l'usuari no està autentificat al sistema,
	 * 			l'identificador de l'usuari anònim.
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 */
	public final Id getUserId() throws RequestException {

		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		Maiye segun el dummy el servicio puede tener constructor 
		con parámetros OJO!!
				
		if (authentication == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("AuthenticationRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei

			//es fa la petició del servei
			Id idAgent = authentication.getUserId();
			return idAgent;
		} 
		
		*/
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		//instancia del objecte de authentication dummy
		HttpServletRequest request = null;
		AuthenticationComponent authentication = 
						new AuthenticationComponent(request);

		try {
			return (Id) authentication.getUserId();
		} catch (AuthenticationException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "AuthenticationException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					AuthenticationRequest.class.getName(), messageError);
			return null;
		}
		
		
	}
}
