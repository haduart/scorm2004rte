// $Id: ConfigurationRequest.java,v 1.2 2008/02/08 12:29:48 msegarra Exp $
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
import java.util.Properties;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Service;

import org.campusproject.components.ConfigurationComponent;
import org.campusproject.osid.shared.StringIterator;
import org.osid.dictionary.DictionaryException;
import org.osid.shared.SharedException;

import edu.url.lasalle.campus.scorm2004rte.util.exception.
		NotFoundPropertiesException;
import edu.url.lasalle.campus.scorm2004rte.util.exception.RequestException;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;
import edu.url.lasalle.campus.scorm2004rte.util.resource.ServiceConnection;


/**
 * $Id: ConfigurationRequest.java,v 1.2 2008/02/08 12:29:48 msegarra Exp $
 * ConfigurationRequest.
 * Gestiona les peticions del servei Configuration.
 * Dona suport alhora de configurar, administrar 
 * i visualitzar els valors de configuració que 
 * tindran les aplicacions i/o els diferents serveis.
 * 
 * @author M.Reyes Enginyeria La Salle mtoro@salle.url.edu
 * @version 1.0 29/01/2008 $Revision: 1.2 $ $Date: 2008/02/08 12:29:48 $
 * $Log: ConfigurationRequest.java,v $
 * Revision 1.2  2008/02/08 12:29:48  msegarra
 * Adaptacions al nou dummy v6.
 *
 * Revision 1.1  2008/01/30 15:52:42  toroleo
 * Implantació capa OSID consumidora. Servei de
 * configuració.
 *
 * 
 */
public final class ConfigurationRequest {

	/** Instancia de la classe. */
	private static ConfigurationRequest instanceConfig = null;
	
	/** Objecte del servei al que se li faran 
	 * les peticions. */
	//private ConfigurationComponent config = null;
	
	/**	Missatge d'error a propagar. */
	private String messageError = null;
	
	/** Propietats de la instància de l'aplicació. */
	private Properties instanceProp = new Properties();
	
	/** Propietats de l'aplicació. */
	private Properties applicationProp = new Properties();
	    
	/**
	 * Constructor.
	 */
	private ConfigurationRequest() {
		
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
	    	config = service.getPort(ConfigurationComponent.class);  
	    	
	    	//es recullen les propietat de configuració tant de la instància 
	    	//de l'aplicació com les de configuració de l'aplicació.
			getPropertiesConfig();
	    	
	    } catch (MalformedURLException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "MalFormedURL " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					ConfigurationRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			config = null;
		
	    } catch (NotFoundPropertiesException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "NotFoundProperties " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					ConfigurationRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			config = null;
	
	    } catch (ClassNotFoundException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "ClassNotFound " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					ConfigurationRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexió, 
			config = null;
	    } catch (RequestException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "RequestException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				ConfigurationRequest.class.getName(), messageError);	
		} 
	    
	   */
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		
		try {
			
			//es recullen les propietat de configuració tant de la 
			//instància de l'aplicació com les de configuració de l'aplicació.
			getPropertiesConfig();
			
		} catch (RequestException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "RequestException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				ConfigurationRequest.class.getName(), messageError);	
		}
		
	}
	
	/**
	 * Es recupera la instància de l'objecte ConfigurationRequest 
	 * ja sigui creant ara la instància o recuperant la ja feta anteriorment.
	 * 
	 * @return Instància de l'objecte ConfigurationRequest.
	 */
	public static synchronized ConfigurationRequest getInstance() {
		
		if (instanceConfig == null) {
			instanceConfig = new ConfigurationRequest();
		}
		return instanceConfig;
		
	}
	
	/**
	 * Recupera les propietats de la instància i de l'aplicació.
	 * 
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexió al servei.
	 */
	private void getPropertiesConfig() throws RequestException {
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos más claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		Maiye segun el dummy el servicio puede tener constructor 
		con parámetros OJO!!
		if (config == null) {  //hi ha hagut problemes amb la connexió
			throw new RequestException("ConfigurationRequest", messageError);
		} else { //no ha hagut cap problema amb la connexió al servei

			StringIterator keyInstanceIter = null;
			StringIterator keyApplicationIter = null;
			
			try {
				
				//es recuperen tots els paràmetres d'instancia 
				//(nom i valor del paràmetre)
				 //petició per recuperar totes les keys d'instància
				keyInstanceIter = (StringIterator) config.getInstanceProperties();
				
				 //es comprova si hi han claus dels paràmetres d'instància
				if (keyInstanceIter != null){
					//es crea un properties amb totes les claus dels 
					//paràmetres d'instancia i els seus valors associats
					while (keyInstanceIter.hasNextString()) {
						String key = keyInstanceIter.nextString();
						//uoc supone que no habran repeticiones de claves
						//petició per recuperar el valor d'un paràmetre d'instància
						String value = config.getProperty(key);
						instanceProp.put(key, value);
					}
				}
				
				//es recuperen tots els paràmetres d'aplicació 
				//(nom i valor del paràmetre)
				 //petició per recuperar totes les keys d'applicació
				keyApplicationIter = (StringIterator) config.getAppProperties();
				
				 //es comprova si hi han claus dels paràmetres d'aplicació
				if (keyApplicationIter != null){
					//es crea un properties amb totes les claus dels 
					//paràmetres d'aplicació i els seus valors associats
					while (keyApplicationIter.hasNextString()) {
						String key = keyApplicationIter.nextString();
						//uoc supone que no habran repeticiones de claves
						//petició per recuperar el valor d'un paràmetre d'aplicació
						String value = config.getProperty(key);
						instanceProp.put(key, value);
					}
				}
				

				
			} catch (DictionaryException e1) {
				//S'escriu el missatge d'error al fitxer de logs.
				messageError = "DictionaryException " + e1.getMessage();
				LogControl log = LogControl.getInstance();
				log.writeMessage(Level.SEVERE, 
					ConfigurationRequest.class.getName(), messageError);
			} catch (SharedException e) {
				//S'escriu el missatge d'error al fitxer de logs.
				messageError = "SharedException " + e.getMessage();
				LogControl log = LogControl.getInstance();
				log.writeMessage(Level.SEVERE, 
					ConfigurationRequest.class.getName(), messageError);
			}
		
		} 
		
		*/
		
		//uoc a cambiar cuando nos pasen la versión del webservice OSIDs
		ConfigurationComponent config;
		StringIterator keyInstanceIter = null;
		StringIterator keyApplicationIter = null;
		try {
			config = new ConfigurationComponent(null);
			
			//es recuperen tots els paràmetres d'instancia 
			//(nom i valor del paràmetre)
			 //es recuperen totes les keys d'instància
			keyInstanceIter = (StringIterator) config.getInstanceProperties();
			
			 //es comprova si hi han claus dels paràmetres d'instància
			if (keyInstanceIter == null) {
				instanceProp = null;
			} else {
				//es crea un properties amb totes les claus dels 
				//paràmetres d'instancia i els seus valors associats
				while (keyInstanceIter.hasNextString()) {
					String key = keyInstanceIter.nextString();
					//uoc supone que no habran repeticiones de claves
					String value = config.getProperty(key);
					instanceProp.put(key, value);
				}
			} // del if (keyInstanceIter == null) {
			
			//es recuperen tots els paràmetres d'aplicació 
			//(nom i valor del paràmetre)
			 //es recuperen totes les keys d'instància
			keyApplicationIter = (StringIterator) config.getAppProperties();
			
			 //es comprova si hi han claus dels paràmetres d'aplicació
			if (keyApplicationIter == null) {
				applicationProp = null;
			} else {
				//es crea un properties amb totes les claus dels 
				//paràmetres d'aplicació i els seus valors associats
				while (keyApplicationIter.hasNextString()) {
					String key = keyApplicationIter.nextString();
					//uoc supone que no habran repeticiones de claves
					String value = config.getProperty(key);
					applicationProp.put(key, value);
				}
			}
			
		
		} catch (DictionaryException e1) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "DictionaryException " + e1.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				ConfigurationRequest.class.getName(), messageError);
		} catch (SharedException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "SharedException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				ConfigurationRequest.class.getName(), messageError);
		}


		
	}

	/**
	 * Retorna el valor de la propietat que es busca. 
	 * 
	 * La recerca la fa primer comprovant si és un paràmetre
	 * d'instància, i en cas que no ho sigui, ho busca com 
	 * a paràmetre d'aplicació. Si no provés la propietat
	 * retornaria null.
	 * 
	 * @param aKey Identificador de la propietat 
	 * 				que s'està buscant.
	 * @return Retorna el valor de la propietat en cas 
	 * 			de que la trobi o null en cas contrari.
	 */
	public String getProperty(final String aKey) {

		String value = null;
		
		//es comprova si el paràmetre passat té algun valor.
		if ((aKey == null) || ("".equals(aKey))) {
			//no en té.
			return null;
		}

		//es busca la propietat al grup de les propietats
		//d'instància
		value = (String) instanceProp.getProperty(aKey);
	
	
		//es comprova si s'ha trobat la propietat al
		//grup de propietats d'instància
		if (value != null) {
			//s'ha trobat
			return value;
		}
		
		//es busca la propietat al grup de les propietats
		//d'aplicació
		value = (String) applicationProp.getProperty(aKey);
		
		return value;
	}
		
}

