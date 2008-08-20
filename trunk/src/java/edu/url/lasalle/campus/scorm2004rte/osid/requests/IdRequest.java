// $Id: IdRequest.java,v 1.3 2008/01/16 21:25:17 toroleo Exp $
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

import org.campusproject.components.AuthenticationComponent;
import org.campusproject.components.AuthorizationComponent;
import org.campusproject.components.IdComponent;
import org.campusproject.osid.id.Id;
import org.osid.authentication.AuthenticationException;
import org.osid.authorization.AuthorizationException;
import org.osid.id.IdException;

import edu.url.lasalle.campus.scorm2004rte.util.exception.NotFoundPropertiesException;
import edu.url.lasalle.campus.scorm2004rte.util.exception.RequestException;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;
import edu.url.lasalle.campus.scorm2004rte.util.resource.ServiceConnection;

/**
 * $Id: IdRequest.java,v 1.3 2008/01/16 21:25:17 toroleo Exp $
 * IdRequest
 * Gestion de las peticiones del servei Identifier.
 * Generar identificadors únics.
 * 
 * @author M.Reyes La salle mtoro@salle.url.edu
 * @version 1.0 27/09/2007 $Revision: 1.3 $ $Date: 2008/01/16 21:25:17 $
 * $Log: IdRequest.java,v $
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
public class IdRequest {

	/** Objecte del servei al que se li faran 
	 * les peticions. */
	//private IdComponent identifier = null;
	/**	Missatge d'error a propagar. */
	private String messageError = null;
	
	/**
	 * Constructor.
	 */
	public IdRequest() {
		
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
			identifier =	service.getPort(IdComponent.class);
		
	    } catch (MalformedURLException e) {
	    	//S'escriu el missatge d'error al fitxer de logs.
			messageError = "MalFormedURL " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					IdRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			identifier = null;
	    	
		} catch (NotFoundPropertiesException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "NotFoundProperties " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					IdRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			identifier = null;
			
		}
		*/
		
	}	
	 
	 
	/**
	 * A partir d'un número d'identificació crea un identificador
	 * únic en cas de que el número facilitat no existeixi ja.
	 *  
	 * @param aNumId Número d'identificació
	 * @return Retorna l'objecte identificador en cas de que existeixi.
	 * 		   Null en cas de hagi hagut algun problema.
	 * maiye preguntar que pasa si el identificador ya existe.
	 * maiye segun dummy te retorna el objeto id existente.
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.
	 */
	public final Id createId(final String aNumId) 
							throws RequestException, RemoteException {
		
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		
		if (identifier == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("IdRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei
		
		//es fa la petició del servei
		Id id = identifier.createId(aNumId);
		//Id id = (Id) identifier.createId(aNumId);
		return id;
		} 
		*/
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		HttpServletRequest request = null;
		IdComponent identifier = new IdComponent(request);
		
		try {
			
			return (Id) identifier.createId(aNumId);
			
		} catch (IdException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "IdException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				IdRequest.class.getName(), messageError);
			
			return null;
		}
					
	} 
	
	/**
	 * 
	 * Obté l'objecte identificador a partir d'un número 
	 * d'identificació donat.
	 * 
	 * @param aNumId Número d'identificació.
	 * @return Retorna l'objecte identificador del numero 
	 * 		   d'identificació passat.
	 * 		   Null en cas de hagi hagut algun problema.
	 * Maiye si no existe el identificador que devuelve?
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 * @throws RemoteException Es llença quan hi ha hagut algun problema en la 
	 * 						petició del servei remot.
	 */
	public final Id getId(final String aNumId) 
						throws RequestException, RemoteException {

		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
			
		if (identifier == null) {  //hi ha hagut problemes amb la connexió
		throw new RequestException("IdRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei
		
		//es fa la petició del servei
		Id id = (Id)identifier.getId(aNumId);
		return id;
		} 
		
		*/
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		HttpServletRequest request = null;
		IdComponent identifier = new IdComponent(request);
		
		try {
			
			return (Id) identifier.getId(aNumId);
			
		} catch (IdException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "IdException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				IdRequest.class.getName(), messageError);
			
			return null;
		}
	}

	
}
