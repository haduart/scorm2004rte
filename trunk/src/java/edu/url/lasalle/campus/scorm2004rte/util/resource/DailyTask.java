// $Id: DailyTask.java,v 1.4 2007/12/13 15:17:50 toroleo Exp $
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

import java.util.TimerTask;

import edu.url.lasalle.campus.scorm2004rte.util.exception.
		NotFoundPropertiesException;


/**
 *
 * DailyTask.
 * Classe per realitzar les tasques que s'han de fer diariament
 * per part de l'objecte LogControl.
 * 
 * @author M.Reyes La Salle mtoro@salle.url.edu
 * @version 1.0 24/10/2007 $Revision: 1.4 $ $Date: 2007/12/13 15:17:50 $
 * $Log: DailyTask.java,v $
 * Revision 1.4  2007/12/13 15:17:50  toroleo
 * Correcció d'un problema amb la creació
 * dels objectes de creació d'error.
 *
 * Revision 1.3  2007/12/04 13:46:42  toroleo
 * Modificació per que els idiomes en que es tradueixin
 * els missatges de l'aplicació s'agafin del fitxer
 * de configuració i així sigui més senzill
 * de modificar.
 *
 * Revision 1.2  2007/12/03 14:55:18  toroleo
 * S'ha afegit el meu e-mail a la capcelera de la classe.
 *
 * Revision 1.1  2007/11/09 15:34:42  toroleo
 * Classe per realitzar les tasques que s'han de fer diariament
 * per part de l'objecte LogControl.
 *
 */
public class DailyTask extends TimerTask {
	
	/**
	 * Constructor.
	 *
	 */
	public DailyTask() {
		
	}
	
	/**
	 * Actualització dels objectes de traducció al Català, 
	 * Castellà i Anglès de forma diaria.
	 * 
	 */
	public final  void run() {
		
		//Array d'objectes de traducció dels missatges
		//a escriure en el fitxer de logs.
		Translator[] arTranslator = null;		
		
		try {
			
			//Es recuperen els idiomes que es faran servir a l'aplicació.
			CollectionProperties cp = new CollectionProperties();
			String strLanguages;
			strLanguages = cp.getPropiedad("idiomes");
			String[] languages = strLanguages.split(",");
			
			arTranslator = new Translator[languages.length];

			//es crean els objectes dels diferents idiomes
			//per la traducció dels missatges  		
			for (int i = 0; i < languages.length; i++) {
				arTranslator[i] = new Translator(languages[i]);
			}

			LogControl logControl = LogControl.getInstance();
			logControl.setTranslator(arTranslator);
			
		} catch (NotFoundPropertiesException e) {
			System.err.println("S'ha produït un error al intentar"
				+ " preparar els idiomes per les traduccions dels "
				+ " missatges que s'escriuren al fitxer de logs ");
		}
			
	}
	

}
