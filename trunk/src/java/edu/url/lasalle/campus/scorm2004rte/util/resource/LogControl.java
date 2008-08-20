// $Id: LogControl.java,v 1.6 2007/12/13 15:17:50 toroleo Exp $
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

import java.io.IOException;
import java.text.DateFormat;
import java.util.Timer;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import sun.reflect.LangReflectAccess;

import edu.url.lasalle.campus.scorm2004rte.util.
		exception.NotFoundPropertiesException;

/**
 * $Id: LogControl.java,v 1.6 2007/12/13 15:17:50 toroleo Exp $
 *
 * LogControl.
 * Classe per generar i escriure el fitxer dels
 * logs amb els errors que han succeit.
 *
 * El path i nom del fitxer d'errors s'agafar� del
 * config.properties de l'aplicaci�. El nom indicat 
 * ha de ser sense extensi� i aquesta classe li 
 * afegir� la data i hora de creaci� a m�s de 
 * l'extensi� .txt. Aix� doncs, si el nom es �log� 
 * el fitxer que crea la classe LogControl 
 * ser� �log29-10-2007_10:05:03.txt�
 * 
 * L'entrada del path estar� indicada per la
 * clau �logfile.path�.
 *
 * L'estil del fitxer d'errors ser� un text pla.
 *
 *  Estructura del fitxer d'error:
 *  	[Data] [Hora] edu.url.lasalle.util.resource.LogControl writeLog
 *  	[Level]: [Classe] 
 *  	[Missatge]
 *  on:
 *  	Data: data en la que se ha produ�t el missatge.
 *  	Hora: hora en la que se ha produ�t el missatge.
 *  	Level: tipus de missatge. Si es tracta d'un missatge
 *  		informatiu (INFO) una advert�ncia (Warning), 
 *  		un error greu (Severe).
 *   	Classe: classe que ha provocat l'error.
 *   	Missatge: text del missatge d'error.
 *
 * @author M.Reyes La Salle mtoro@salle.url.edu
 * @version 1.0 24/10/2007 $Revision: 1.6 $ $Date: 2007/12/13 15:17:50 $
 * $Log: LogControl.java,v $
 * Revision 1.6  2007/12/13 15:17:50  toroleo
 * Correcci� d'un problema amb la creaci�
 * dels objectes de creaci� d'error.
 *
 * Revision 1.5  2007/12/10 14:44:48  toroleo
 * Se li han tret codi comentat innecessari.
 *
 * Revision 1.4  2007/12/04 13:46:42  toroleo
 * Modificaci� per que els idiomes en que es tradueixin
 * els missatges de l'aplicaci� s'agafin del fitxer
 * de configuraci� i aix� sigui m�s senzill
 * de modificar.
 *
 * Revision 1.3  2007/12/03 14:55:18  toroleo
 * S'ha afegit el meu e-mail a la capcelera de la classe.
 *
 * Revision 1.2  2007/11/09 15:32:10  toroleo
 * Canvis:
 *   -Modificaci� del fitxer de logs generat per la classe. 
 *   Ara en el nom s'indica el dia i hora de la generaci�.
 *   -Adaptaci� als canvis de la classe CollectionProperties.
 *   -Adaptaci� de les funcions d'escriptura per que en cas 
 *   de que els par�metres de Level, ClassName i missatge
 *   no estiguin instanciats posi els seg�ents valors per
 * defecte: Leve = Info, ClassName = Class not defined i message = ��
 *   -Nova funcionalitat, que s'escriu en el fitxer de logs 
 *   a partir d'un codi de missatge i un idioma.
 *   -Nova funcionalitat per actualitzar peri�dicament els
 *   missatges que s'han llegit i s'han guardat a mem�ria amb 
 *   el contingut dels fitxers properties. Per si des de
 *   que es van guardar per primera vegada els missatges fins al 
 *   dia seg�ent s'ha variat el seu contingut.
 *
 */
public final class LogControl {
	
	/** Instancia de la classe. */
	private static LogControl instanceLog = null;

	/** Objecte per escriure els missatges d'error
	 * en el fitxer que s'indicar� posteriorment.
	 */	
	private static Logger theLogger = 
		 Logger.getLogger(LogControl.class.getName());
	
	/** Fitxer al qual es volcar�n els missatges de error. */
	private FileHandler fileHandler;
	
	/** Flag per indicar si a l'hora de crear l'objecte ha hagut
	 * algun problema i no es podr� escriure en el fitxer.
	 */
	private boolean error = false;
	
	/** Nom del fitxer de logs. */
	private String logFile = "";
	
	/**
	 * Array d'objectes de traducci� dels missatges
	 * a escriure en el fitxer de logs.
	 */
	private Translator[] arTranslator = null;
	
	/**
	 * Array de idiomes que es poden fer servir 
	 * en tota l'aplicaci� per mostrar missatges. 
	 */
	private String[] languages = null;
	
	/**	Objecte per realitzar operacions que 
	 * s'han de fer di�riament per tal 
	 * d'actualitzar les dades que es tenen a mem�ria. */
	private DailyTask dailyTask = null;
	
	
	
	/**
	 * Constructor.
	 */
	private LogControl() {
		//es configura l'objecte Logger per que els missatges
		//d'error s'escriguin en el fitxer i amb el 
		//format dessitjats
		iniFileLog();		
		
		if (!error) {  //no hi ha hagut cap problema al crear el fitxer
			
			//es perparen els objectes de traducci� 
			//pels missatges a gravar
			prepareTranslator();
			
		}
	}
	
	/**
	 * Es recupera la inst�ncia de l'objecte LogControl 
	 * ja sigui creant ara la inst�ncia o recuperant la ja feta anteriorment.
	 * 
	 * @return Inst�ncia de l'objecte LogControl.
	 */
	public static synchronized LogControl getInstance() {
		
		if (instanceLog == null) {
			instanceLog = new LogControl();
		}
		return instanceLog;
		
	}
	
	/**
	 * Canvia el valor de l'objecte de traducci� de l'idioma indicat.
	 * 
	 * @param aTranslator Objecte de traducci�. 
	 * @param aLanguage Idioma del traductor. 
	 * 					Posici� en que es troba l'idioma en el String
	 * 					de idiomes del fitxer de configuraci�.
	 */
	public void setTranslator(final Translator aTranslator, 
							  final int aLanguage) {
		this.arTranslator[aLanguage] = aTranslator;
	}
	
	/**
	 * Canvia el valor de l'objecte de traducci� de l'idioma indicat.
	 * 
	 * @param aTranslator Array dels objectes de traducci�. 
	 */
	public void setTranslator(final Translator[] aTranslator) {
		this.arTranslator = aTranslator;
	}
	
	/**
	 * Escriu el missatge en el fitxer de logs.
	 * 
	 * @param aLevel	 Tipus o categoria del missatge que 
	 * 					 s'escriur� al fitxer. En cas de no estar 
	 * 					 instanciat s'agafar� per defecte Level.Info
	 * @param aClassName Nom de la classe que escriu el missatge.
	 * 					 En cas de no estar instanciat es posar� 
	 * 					 per defecte "Class not defined."
	 * @param aMessage 	 Missatge a escriure al fitxer.
	 * 					 En cas de no estar instanciat es posar� 
	 * 					 per defecte ""
	 * 
	 */
	public void writeMessage(final Level aLevel, final String aClassName,
			final String aMessage) {
		
		if (error) {
			//S'ha produ�t un error al generar el fitxer de logs.
			System.err.println("No s'ha pogut escriure en "
					 + "el fitxer de logs: \n   " + logFile);
			return;
		}
		
		
		 //no hi ha succe�t cap problema al crear l'objecte
		 //LogControl i es pot escriure en el fitxer				 
	
		Level level = aLevel;
		String className = aClassName;
		String message = aMessage;
		
		 //es mira si algun dels par�metres passats no est� instanciat
		 if (level == null) {
			 level = Level.INFO;
		 }
		 
		 if ((className == null)  || ("".equals(className))) {
			 className = "Class not defined";
		 }
		 
		 if (message == null) {
			 message = "";
		 }

		 //s'escriu en el fitxer				 
		 theLogger.log(level, 
				 className.concat(":\n ").concat(message));
		
			
	}
	
	/**
	 * Escriu el missatge, que correspon al codi 
	 * de missatge i a l'idioma indicats, en el fitxer de logs.
	 * 
	 * @param aLevel Tipus o categoria del missatge que 
	 * 					 s'escriur� al fitxer. En cas de no estar 
	 * 					 instanciat s'agafar� per defecte Level.Info
	 * @param aClassName Nom de la classe que escriu el missatge.
	 * 					 En cas de no estar instanciat es posar� 
	 * 					 per defecte "Class not defined".
	 * @param anIdMessage Codi del missatge a escriure.
	 * @param aLanguage Idioma en el que s'ha descriure el missatge.
	 * 					El idiomes poden ser els indicats en el fitxer 
	 * 				 	de configuraci�. Podrien ser per exemple es (Espanyol)
	 */
	public void writeMessage(final Level aLevel, final String aClassName, 
			final String anIdMessage, final int aLanguage) {
		
		if (error) {
			//S'ha produ�t un error al generar el fitxer de logs.
			System.err.println("No s'ha pogut escriure en "
					 + "el fitxer de logs: \n   " + logFile);
			return;
		}
		
		 //no hi ha succe�t cap problema al crear l'objecte
		 //LogControl i es pot escriure en el fitxer				 
	
		Level level = aLevel;
		String className = aClassName;
		String message = "";
				
		//es mira si algun dels par�metres passats no est� instanciat
		if (level == null) {
		 level = Level.INFO;
		}
		 
		if ((className == null)  || ("".equals(className))) {
		 className = "Class not defined";
		}
		 
		try {
			 
			//es busca l'obejecte de traducci� per l'idioma
			//indicat.
			int posLanguages = -1;
			
			for (int i = 0; i < languages.length; i++) {
				if (languages[i].equals(aLanguage)) {
					posLanguages = i;
					i = languages.length;
				}
			}
		
			//es comprova si s'ha trobat l'idioma
			if (posLanguages < 0) {
				//s'ha trobat l'idioma.

				//es traduexi el missatge
				arTranslator[posLanguages].translate(anIdMessage);
				
				 //s'escriu en el fitxer				 
				 theLogger.log(level, 
						 className.concat(":\n ").concat(message));
			} else {
				String msg = "**" + LogControl.class.getName() + ": \n "  
					+ "L'idioma " + aLanguage 
					+ " per fer la traducci� no es reconeix."
					+ " No s'ha pogut escriure el "
					+ "missatge correctament."
					+ "\n El missatge era: " + anIdMessage
					+ "\n El missatge b� de la classe: " + className
					+ " i es un tipus de missatge: " + level;
				theLogger.log(Level.INFO, msg);
			}
			 
		} catch (NotFoundPropertiesException e) {
			String msg = "**" + LogControl.class.getName() + ": \n "  
						+ "El identificador de missatge " + anIdMessage 
						+ " no existeix. No s'ha pogut escriure el "
						+ "missatge correctament."
						+ "\n El missatge b� de la classe: " + className
						+ " i es un tipus de missatge: " + level;
			
			theLogger.log(Level.INFO, msg); 
		}
		
		
	}
	
	/**
	 * Es recupera el nom del fitxer de logs i es prepara
	 * per comen�ar a escriure en ell, tot indicant el tipus 
	 * de format que tindr�. 
	 *
	 */
	private void iniFileLog() {
		
		try {
			//s'agafa la data i hora del moment de la creaci� del LogControl
			Calendar calendar = new GregorianCalendar();
	        Date date = calendar.getTime();
	        DateFormat localFormat = DateFormat.getDateTimeInstance();
	        	        
	    	//s'extreu el path del fitxer on s'escriuran els missatges
	        //d'error        
	        CollectionProperties cp = new CollectionProperties();
	        logFile = cp.getPropiedad("logfile.path");

	    	//se l'afegeix al nom del fitxer la data i hora de creaci�
	    	logFile = logFile 		
	    	+ localFormat.format(date).replace(" ", "_").replace(":", "-")
	    	+ ".txt";
	    	
			fileHandler = new FileHandler(logFile);
			
			//s'indica que el format del fitxer a crear ser� un texte
			//pla
			fileHandler.setFormatter(new SimpleFormatter());

			//s'indica que la sortida dels errors ha de ser al fitxer 
			//abans indicat.
			theLogger.addHandler(fileHandler);
			
		} catch (SecurityException e) {
			System.err.println("S'ha produ�t un error al generar el "
					+ "fitxer de logs: \n  " + logFile);
			error = true;
		} catch (IOException e) {
			System.err.println("S'ha produ�t un error al generar el "
					+ "fitxer de logs: \n  " + logFile);
			error = true;
		} catch (NotFoundPropertiesException e) {
			System.err.println("S'ha produ�t un error al generar el "
					+ "fitxer de logs: \n  " + logFile);
			error = true;
		}

	}
	
	/**
	 * Crea els objectes de traducci� en els idiomes
	 * en els que treballar l'aplicaci�. 
	 * A m�s llen�a el dailyTask perqu� peri�dicament
	 * es vagin actualitzant els objectes de traducci�
	 * amb els possibles canvis que hi hagin en els
	 * missatges a traduir.
	 */
	private void prepareTranslator() {

		try {
			//Es recuperen els idiomes que es faran servir a l'aplicaci�.
			CollectionProperties cp = new CollectionProperties();
			String strLanguages;
			strLanguages = cp.getPropiedad("idiomes");
			languages = strLanguages.split(",");
			
			arTranslator = new Translator[languages.length];
			
			//es crean els objectes dels diferents idiomes
			//per la traducci� dels missatges  
			for (int i = 0; i < languages.length; i++) {
				arTranslator[i] = new Translator(languages[i]);
			}
			
			//Constants d'inicialitzaci� del �rellotge� per la
			//realitzaci� de les tasques diaries
			final int fONEDAY = 1;  	//D�a d'inici de les tasques diaries
			final int fFOURAM = 4;  	//Hora d'inici de les tasques diaries
			final int fZEROMINUTES = 0; //Minut d'inici de les tasques diaries
			//per�ode de realitzaci� de les tasques diaries 
			//86400000 milisegons corresponen a 24 hores
			final long fONCEPERDAY = 1000 * 60 * 60 * 24;

			Timer timer = new Timer();
	    	dailyTask = new DailyTask();
	    	Date data = new Date();
	    	
	    	//Es prepara la data y hora de execuci� di�ria de les tasques.
	        Calendar calendar = new GregorianCalendar();
		    calendar.add(Calendar.DATE, fONEDAY);
		    Calendar result = new GregorianCalendar(
		      calendar.get(Calendar.YEAR),
		      calendar.get(Calendar.MONTH),
		      calendar.get(Calendar.DATE),
		      fFOURAM,
		      fZEROMINUTES
		    );
	    	
		    data = result.getTime();
		    
		    //Es llen�a el temporitzador amb les tasques a realitzar, 
		    //la data y hora de la primera execuci� i el per�ode d'execuci�.
	    	timer.schedule(dailyTask, data, fONCEPERDAY);
	    	
		} catch (NotFoundPropertiesException e) {
			System.err.println("S'ha produ�t un error al intentar"
					+ " preparar els idiomes per les traduccions dels "
					+ " missatges que s'escriuren al fitxer de logs " 
					+ logFile);
			error = true;
		}

	}
		
}
