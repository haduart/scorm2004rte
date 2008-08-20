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
 * i visualitzar els valors de configuraci� que 
 * tindran les aplicacions i/o els diferents serveis.
 * 
 * @author M.Reyes Enginyeria La Salle mtoro@salle.url.edu
 * @version 1.0 29/01/2008 $Revision: 1.2 $ $Date: 2008/02/08 12:29:48 $
 * $Log: ConfigurationRequest.java,v $
 * Revision 1.2  2008/02/08 12:29:48  msegarra
 * Adaptacions al nou dummy v6.
 *
 * Revision 1.1  2008/01/30 15:52:42  toroleo
 * Implantaci� capa OSID consumidora. Servei de
 * configuraci�.
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
	
	/** Propietats de la inst�ncia de l'aplicaci�. */
	private Properties instanceProp = new Properties();
	
	/** Propietats de l'aplicaci�. */
	private Properties applicationProp = new Properties();
	    
	/**
	 * Constructor.
	 */
	private ConfigurationRequest() {
		
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos m�s claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		
		//Es crea una conexi� al servei
		Service service;
		ServiceConnection sc = new ServiceConnection();    
		
	    try {
	    
	    	//S'inicia la conexi� amb el servei 
	    	service = sc.connect();
			//uoc posible modificaci�n cuando nos pasen el wsdl de los osids
	    	//s'indica la interficie a fer servir
	    	config = service.getPort(ConfigurationComponent.class);  
	    	
	    	//es recullen les propietat de configuraci� tant de la inst�ncia 
	    	//de l'aplicaci� com les de configuraci� de l'aplicaci�.
			getPropertiesConfig();
	    	
	    } catch (MalformedURLException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "MalFormedURL " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					ConfigurationRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexi�, 
			config = null;
		
	    } catch (NotFoundPropertiesException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "NotFoundProperties " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					ConfigurationRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexi�, 
			config = null;
	
	    } catch (ClassNotFoundException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "ClassNotFound " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
					ConfigurationRequest.class.getName(), messageError);
			
			//s'indica que ha anat malament la connexi�, 
			config = null;
	    } catch (RequestException e) {
			//S'escriu el missatge d'error al fitxer de logs.
			messageError = "RequestException " + e.getMessage();
			LogControl log = LogControl.getInstance();
			log.writeMessage(Level.SEVERE, 
				ConfigurationRequest.class.getName(), messageError);	
		} 
	    
	   */
		
		//uoc a cambiar cuando nos pasen la versi�n del webservice OSIDs
		
		try {
			
			//es recullen les propietat de configuraci� tant de la 
			//inst�ncia de l'aplicaci� com les de configuraci� de l'aplicaci�.
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
	 * Es recupera la inst�ncia de l'objecte ConfigurationRequest 
	 * ja sigui creant ara la inst�ncia o recuperant la ja feta anteriorment.
	 * 
	 * @return Inst�ncia de l'objecte ConfigurationRequest.
	 */
	public static synchronized ConfigurationRequest getInstance() {
		
		if (instanceConfig == null) {
			instanceConfig = new ConfigurationRequest();
		}
		return instanceConfig;
		
	}
	
	/**
	 * Recupera les propietats de la inst�ncia i de l'aplicaci�.
	 * 
	 * @throws RequestException Es llencen un error en cas que no 
	 * 					s'hagi pogut fer la connexi� al servei.
	 */
	private void getPropertiesConfig() throws RequestException {
		/* Maiye se ha comentado para que los de los subsistemas
		puedan hacer pruebas. Cuando tengamos m�s claro el funcionamiento del
		webservice descomentar y actualizar con los datos que tengamos.
		Maiye segun el dummy el servicio puede tener constructor 
		con par�metros OJO!!
		if (config == null) {  //hi ha hagut problemes amb la connexi�
			throw new RequestException("ConfigurationRequest", messageError);
		} else { //no ha hagut cap problema amb la connexi� al servei

			StringIterator keyInstanceIter = null;
			StringIterator keyApplicationIter = null;
			
			try {
				
				//es recuperen tots els par�metres d'instancia 
				//(nom i valor del par�metre)
				 //petici� per recuperar totes les keys d'inst�ncia
				keyInstanceIter = (StringIterator) config.getInstanceProperties();
				
				 //es comprova si hi han claus dels par�metres d'inst�ncia
				if (keyInstanceIter != null){
					//es crea un properties amb totes les claus dels 
					//par�metres d'instancia i els seus valors associats
					while (keyInstanceIter.hasNextString()) {
						String key = keyInstanceIter.nextString();
						//uoc supone que no habran repeticiones de claves
						//petici� per recuperar el valor d'un par�metre d'inst�ncia
						String value = config.getProperty(key);
						instanceProp.put(key, value);
					}
				}
				
				//es recuperen tots els par�metres d'aplicaci� 
				//(nom i valor del par�metre)
				 //petici� per recuperar totes les keys d'applicaci�
				keyApplicationIter = (StringIterator) config.getAppProperties();
				
				 //es comprova si hi han claus dels par�metres d'aplicaci�
				if (keyApplicationIter != null){
					//es crea un properties amb totes les claus dels 
					//par�metres d'aplicaci� i els seus valors associats
					while (keyApplicationIter.hasNextString()) {
						String key = keyApplicationIter.nextString();
						//uoc supone que no habran repeticiones de claves
						//petici� per recuperar el valor d'un par�metre d'aplicaci�
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
		
		//uoc a cambiar cuando nos pasen la versi�n del webservice OSIDs
		ConfigurationComponent config;
		StringIterator keyInstanceIter = null;
		StringIterator keyApplicationIter = null;
		try {
			config = new ConfigurationComponent(null);
			
			//es recuperen tots els par�metres d'instancia 
			//(nom i valor del par�metre)
			 //es recuperen totes les keys d'inst�ncia
			keyInstanceIter = (StringIterator) config.getInstanceProperties();
			
			 //es comprova si hi han claus dels par�metres d'inst�ncia
			if (keyInstanceIter == null) {
				instanceProp = null;
			} else {
				//es crea un properties amb totes les claus dels 
				//par�metres d'instancia i els seus valors associats
				while (keyInstanceIter.hasNextString()) {
					String key = keyInstanceIter.nextString();
					//uoc supone que no habran repeticiones de claves
					String value = config.getProperty(key);
					instanceProp.put(key, value);
				}
			} // del if (keyInstanceIter == null) {
			
			//es recuperen tots els par�metres d'aplicaci� 
			//(nom i valor del par�metre)
			 //es recuperen totes les keys d'inst�ncia
			keyApplicationIter = (StringIterator) config.getAppProperties();
			
			 //es comprova si hi han claus dels par�metres d'aplicaci�
			if (keyApplicationIter == null) {
				applicationProp = null;
			} else {
				//es crea un properties amb totes les claus dels 
				//par�metres d'aplicaci� i els seus valors associats
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
	 * La recerca la fa primer comprovant si �s un par�metre
	 * d'inst�ncia, i en cas que no ho sigui, ho busca com 
	 * a par�metre d'aplicaci�. Si no prov�s la propietat
	 * retornaria null.
	 * 
	 * @param aKey Identificador de la propietat 
	 * 				que s'est� buscant.
	 * @return Retorna el valor de la propietat en cas 
	 * 			de que la trobi o null en cas contrari.
	 */
	public String getProperty(final String aKey) {

		String value = null;
		
		//es comprova si el par�metre passat t� algun valor.
		if ((aKey == null) || ("".equals(aKey))) {
			//no en t�.
			return null;
		}

		//es busca la propietat al grup de les propietats
		//d'inst�ncia
		value = (String) instanceProp.getProperty(aKey);
	
	
		//es comprova si s'ha trobat la propietat al
		//grup de propietats d'inst�ncia
		if (value != null) {
			//s'ha trobat
			return value;
		}
		
		//es busca la propietat al grup de les propietats
		//d'aplicaci�
		value = (String) applicationProp.getProperty(aKey);
		
		return value;
	}
		
}

