// $Id: Translator.java,v 1.4 2007/12/04 13:46:42 toroleo Exp $
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

import java.util.logging.Level;

import edu.url.lasalle.campus.scorm2004rte.util.
		exception.NotFoundPropertiesException;


/**
 * $Id: Translator.java,v 1.4 2007/12/04 13:46:42 toroleo Exp $
 * 
 * Translator.
 * Classe per traduir textos a partir d'un 
 * identificador de classe i d'un idioma determiat.
 * 
 * @author M.Reyes La Salle mtoro@salle.url.edu
 * @version 1.0 31/10/2007 $Revision: 1.4 $ $Date: 2007/12/04 13:46:42 $
 * $Log: Translator.java,v $
 * Revision 1.4  2007/12/04 13:46:42  toroleo
 * Modificació per que els idiomes en que es tradueixin
 * els missatges de l'aplicació s'agafin del fitxer
 * de configuració i així sigui més senzill
 * de modificar.
 *
 * Revision 1.3  2007/12/03 14:55:18  toroleo
 * S'ha afegit el meu e-mail a la capcelera de la classe.
 *
 * Revision 1.2  2007/11/14 06:35:09  msegarra
 * Adaptació dels noms dels fitxers dels properties d'idiomes
 * i de les constants de "Translator" al codi d'idiomes ISO 639-1.
 *
 * Revision 1.1  2007/11/09 15:35:04  toroleo
 * Translator.
 * Classe per traduir textos a partir d'un
 * identificador de classe i d'un idioma determiat.
 *
 */

public class Translator {
	
	/** Fitxer on estan guardats els missatges 
	 * a mostrar en el programa.	 */
	private String messageFile = null;
	
	/** Objecte que recopila i guarda els textos dels 
	 * missatges de l'idioma escollit. 	 */
	private CollectionProperties cpMessage = null;
	
	/**
	 * Array de idiomes que es poden fer servir 
	 * en tota l'aplicació per mostrar missatges. 
	 */
	private String[] languages = null;
	
	

	/**
	 * Constructor.
	 *
	 */
	public Translator() { 
		
		//Es recupera el nom del fitxer d'on estan 
		//els textos dels missatges
		ini();
				
	} 
	
	/**
	 *  Constructor.
	 *  
	 *  Crea el traductor amb l'idioma indicat.
	 *  En el cas que l'idioma indicat no sigui Català, Castellà o Anglès
	 *  es faran les traduccions en Anglès.
	 *  
	 * @param aLanguage Llenguatge al qual es volen 
	 * 					traduir els textos. 
	 * 					Pot ser SPANISH, CATALAN o ENGLISH.
	 */
	public Translator(final String aLanguage) {

		//Es recupera el nom del fitxer d'on estan 
		//els textos dels missatges
		ini();

		if (messageFile != null) {
			//no han hagut problemes i s'ha recuperat el nom 
			//del fitxer de missatges

			//es comproba que l'idioma indicat és un dels esperats
			//sino es traduirà en Anglès.
			//String idioma = validateLanguage(aLanguage);
			String idioma = validateLanguage(aLanguage);
		
			//Al nom del fitxer dels missatges se li afegeix 
	        //l'idioma i l'extensió .properties
	        String file = messageFile + idioma + ".properties";
	        
	        //es recuperen els missatges del fitxer amb llenguatge escollit.
	        cpMessage = new CollectionProperties(file);			
			

		}
		
	} 
	
	/**
	 * Tradueix el missatge, que pertany al identificador passat, 
	 * a l'idioma que s'indica al mètode.
	 * 
	 * @param anIdText Identificador del missatge a traduir.
	 * @param aLanguage Llengua a la que es vol traduir el missatge.
	 * @return Missatge traduït a l'idioma indicat.
	 * 			O null en cas de que hi hagi algun problema.
	 * @throws NotFoundPropertiesException És llença un error en 
	 * 		  el cas que no es trobi cap missatge amb l'identificador passat.
	 */
	public final String translate(final String anIdText,
								final String aLanguage) 
								throws NotFoundPropertiesException {

		if (anIdText == null) {
			//si no s'indica un identificador de text 
			//no es pot fer la traducció
			
			String message = "No s'ha especificat cap codi de missatge." 
							+ " Per tant no es farà cap traducció.";
			LogControl logControl = LogControl.getInstance();
			logControl.writeMessage(Level.SEVERE, 
						Translator.class.getName(), message);
			
			return null;
		}
		
		//es comproba que l'idioma indicat és un dels esperats
		//sino es traduirà en Anglès.
		String idioma = validateLanguage(aLanguage);
		
		//Al nom del fitxer dels missatges se li afegeix 
        //l'idioma i l'extensió .properties
        String file = messageFile + idioma + ".properties";
		
		//es recuperen els missatges del fitxer amb llenguatge escollit.
        CollectionProperties cp = new CollectionProperties(file);
		
		//es retorna el missatge traduït
		return cp.getPropiedad(anIdText);
		
	}

	/**
	 * Tradueix el missatge, que pertany al identificador passat,
	 * a l'idioma amb que es va crear la instancia d'aquesta classe.
	 * 
	 * @param anIdText Identificador del missatge a traduir.
	 * @return Missatge traduït a l'idioma indicat.
	 * 			O null en cas de que hi hagi algun problema.
	 * @throws NotFoundPropertiesException És llença un error en 
	 * 		  el cas que no es trobi cap missatge amb l'identificador passat.
	 */
	public final String translate(final String anIdText) 
			throws NotFoundPropertiesException {
		
		if (anIdText == null) {
			//si no s'indica un identificador de text 
			//no es pot fer la traducció
			
			String message = "No s'ha especificat cap codi de missatge." 
							+ " Per tant no es farà cap traducció.";
			LogControl logControl = LogControl.getInstance();
			logControl.writeMessage(Level.SEVERE, 
						Translator.class.getName(), message);
			
			return null;
		}
		
		//Es comprova que ho hagi hagut cap error al crear l'objecte 
		//que llegeix els missatges.
		if (cpMessage == null) {
			
			String message = "No s'havia definit anteriorment l'idioma"
				+ " per fer les traduccions. Les traduccions es"
				+ " farana en Anglès.";

			LogControl logControl = LogControl.getInstance();
			logControl.writeMessage(Level.WARNING, 
						Translator.class.getName(), message);
						
			//Al nom del fitxer dels missatges se li afegeix 
	        //l'idioma i l'extensió .properties
	        String file = messageFile + languages[0] + ".properties";
			
			//es recuperen els missatges del fitxer amb llenguatge escollit.
	        cpMessage = new CollectionProperties(file);
			
		}
		
		//es retorna el missatge traduït
		return cpMessage.getPropiedad(anIdText);
	}
	
	/**
	 * Busca el nom del fitxer on estan guardats els 
	 * textos del missatges.
	 *
	 */
	private void ini() {
				
		try {
		
			//s'extreu el nom del fitxer properties d'on 
			//s'aniran a buscar les traduccions dels textos.
			CollectionProperties cpConfig = new CollectionProperties();
	        messageFile = cpConfig.getPropiedad("messagefile.name");
	        
	        //Es recuperen els idiomes que es faran servir a l'aplicació.
			CollectionProperties cp = new CollectionProperties();
			String strLanguages;
			strLanguages = cp.getPropiedad("idiomes");
			languages = strLanguages.split(",");
			
		} catch (NotFoundPropertiesException e) {
			//No s'ha pogut agafar del fitxer de properties
			//la propietat dessitjada.
			
			String message = "No s'ha pogut accedir la propietat lofgile.path"
							+ "del fitxer config.properties. \n "
							+ "Per tant no es podran traduir els textos.";
			
			LogControl logControl = LogControl.getInstance();
			logControl.writeMessage(Level.SEVERE, 
									Translator.class.getName(), message);
			
			//s'indica que no hi ha fitxer dels missatges
			//traduïts
			messageFile = null;
		}
		
	}
	
	/**
	 * Valida que l'idioma indicat sigui Català, 
	 * Castellà o Anglès. En cas contrari escriu
	 * un missatge de warning en el fitxer de 
	 * logs i agafa l'Anglès com a idioma per fer les traduccions.
	 * 
	 * @param aLanguage Llenguatge en el que es vol fer les traduccions.
	 * @return Retorna l'idioma passat en cas que fos un dels
	 * 				   idiomes reconeguts o el primer dels definits al fitxer
	 * 				   de configuració.
	 */
	private String validateLanguage(final String aLanguage) {
		
		//es comprova si existeix l'idioma indicat
		boolean exist = false;
		
		for (int i = 0; i < languages.length; i++) {
			if (languages[i].equals(aLanguage)) {
				exist = true;
				i = languages.length;
			}
		}

		if (exist) {
			//és un dels idiomas 
			//definits al fitxer de configuració
			return aLanguage;
		} else {
			//no és un dels idiomas 
			//definits al fitxer de configuració
			//es retorna el primer idioma definit.
			return languages[0];
		}
			
	}
	
}
