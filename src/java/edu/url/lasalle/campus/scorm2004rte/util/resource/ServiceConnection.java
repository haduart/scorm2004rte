// $Id: ServiceConnection.java,v 1.2 2007/12/03 14:55:18 toroleo Exp $
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
package edu.url.lasalle.campus.scorm2004rte.util.resource;

import java.net.URL;
import java.net.MalformedURLException;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import edu.url.lasalle.campus.scorm2004rte.util.exception.NotFoundPropertiesException;

/**
 * $Id: ServiceConnection.java,v 1.2 2007/12/03 14:55:18 toroleo Exp $
 * ServiceConnection.
 * Implementa la connexió a un servei d'un web service fent servir jaxws.
 * 
 * @author M.Reyes La Salle mtoro@salle.url.edu
 * @version 1.0 19/09/2007 $Revision: 1.2 $ $Date: 2007/12/03 14:55:18 $
 * $Log: ServiceConnection.java,v $
 * Revision 1.2  2007/12/03 14:55:18  toroleo
 * S'ha afegit el meu e-mail a la capcelera de la classe.
 *
 * Revision 1.1  2007/11/22 09:46:50  toroleo
 * Implementació capa OSID consumidora
 *
 *
 */
public class ServiceConnection {

	//variables de propietats de connexió al webservice
	/** Propietat on es guarda l'adreça en la que es troba el 
	 * fitxer wsdl del webservicela url del WebService. */
	private String propURL = "service.url";
	/** Propietat on es guarda el nom del servei. */
	private String propServiceName = "service.name";
	/** Propietat on es guarda la url del WebService. */
	private String propNamespace = "service.namespace";
	
	/**
	 * Constructor
	 * 
	 * Construeix una connexió amb els valors per defecte 
	 * de nom de la propietat de l'adreça del fitxer wsdl
	 * (“service.url”), nom del servei (“service.name”) 
	 * i namespace (“service.namespace”) necessaris 
	 * per la connexió. I amb el valor passat de la interficie
	 * que defineix el servei
	 *
	 */
	public ServiceConnection() { }
	
	/**
	 * Constructor
	 * 
	 * Construeix una connexió amb els valors inicials 
	 * especificats per els noms de les propietats
	 * a agafar del fitxer de configuració per la connexió
	 * i la ruta i nom de la interficie del servei a fer servir.
	 * 
	 * @param anUrl			Adreça en la que es troba el 
	 * 						fitxer wsdl del webservice
	 * @param aServiceName	Nom del servei
	 * @param aNamespace	Namespace en el que es pot trobar el WebService
	 * 
	 */
	public ServiceConnection(final String anUrl, final String aServiceName,
							 final String aNamespace) {
		propURL = anUrl;
		propServiceName = aServiceName;
		propNamespace = aNamespace;
	}
	
	/**
	 * Es connecta a un servei que es defineix 
	 * amb els paràmetres passat al constructor .
	 * 
	 * @return Service vista del WebService per part del client
	 * @throws NotFoundPropertiesException Es llença en el cas 
	 * 				de no trobar la propietat que es va indicar 
	 * 				per la connexió amb el servei.
	 * @throws MalformedURLException Es llença en el cas de que 
	 * 				la url per accedir al wsdl del servei no estigui 
	 * 				ben formada.
	 */
	public final Service connect() throws NotFoundPropertiesException, 
							MalformedURLException {

	    	//s'agafen els paràmetres necessaris per connectar-se
			//amb el webservice
		    CollectionProperties collectionProperties = 
		    						new CollectionProperties();
	    	String urlstr = 
	    			collectionProperties.getPropiedad(propURL);
	    	String nameServiceStr = 
	    			collectionProperties.getPropiedad(propServiceName);
	    	String namespaceServiceStr = 
	    			collectionProperties.getPropiedad(propNamespace);

		    // S'especifica la ubicació del fitxer wsdl
	    	URL url = new URL(urlstr);

		    //s'indica el namespace en el que es troba el servei 
	    	//i el nom d'aquest
		    //això crea un identificador únic del servei
	    	QName qname = new QName(namespaceServiceStr, nameServiceStr);

	    	//es crea una instancia de la vista del WebService 
	    	//per part del client
	    	//identificat pel url i el qname abans creat
	    	Service service = Service.create(url, qname);
	    	
	    	return service;
		
	}
	
}
