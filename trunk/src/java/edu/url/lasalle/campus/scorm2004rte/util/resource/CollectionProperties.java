// $Id: CollectionProperties.java,v 1.6 2007/12/03 14:55:18 toroleo Exp $
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

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.
		util.exception.NotFoundPropertiesException;

/**
 * $Id: CollectionProperties.java,v 1.6 2007/12/03 14:55:18 toroleo Exp $
 * 
 * CollectionProperties.
 * Llegeix i recull la informació del fitxer properties 
 * que se li indiqui o en el seu defecte del fitxer config.properties 
 * 
 * Aquesta informaci es guarda en un HasMap que es pot
 * consultar en qualsevol moment.
 * 
 * @author M.Reyes La Salle mtoro@salle.url.edu
 * @version 1.0 05/09/2007 $Revision: 1.6 $ $Date: 2007/12/03 14:55:18 $
 * $Log: CollectionProperties.java,v $
 * Revision 1.6  2007/12/03 14:55:18  toroleo
 * S'ha afegit el meu e-mail a la capcelera de la classe.
 *
 * Revision 1.5  2007/11/28 08:15:06  msegarra
 * Solucionat el problema del path relatiu que es produïa en intentar
 * llegir els fitxers properties mitjançant l'utilització d'aquesta 
 * classe a l'aplicació web.
 *
 * Revision 1.4  2007/11/09 15:10:22  toroleo
 * *** empty log message ***
 *
 * Revision 1.3  2007/11/09 15:09:09  toroleo
 * Canvi de la capcelera per que segueixi l'estàndard. 
 * Canvi de tipus de classe, ara ja no es una classe tipus utilities 
 * si no una normal.
 *
 */
public class CollectionProperties {

	/** Fitxer de propietats del que s'agafarà la informació. */
	private String propertieFile = Constants.CONFIG_FILE_NAME;
	/** Map hash que contindrà totes les 
	 * propietats del fitxer de configuració.*/
	//private static HashMap properties = null;
	private HashMap properties = null;
	
	  /**
	   * Constructor.
	   *
	   * Construeix l'objecte accedint al fitxer de 
	   * properties per defecte, config.properties.
	   *
	   */
	  public CollectionProperties() {
		  ini();
	  }

	  /**
	   * Constructor.
	   * 
	   * Construeix l'objecte accedint al fitxer de 
	   * properties que s'indica en el paràmetre
	   * 
	   * @param aNameFile Nom del fitxer properties del
	   * 				 que es vol agafar la informació.
	   */
	  public CollectionProperties(final String aNameFile) {
		  propertieFile = aNameFile;
		  ini();
	  }
	  
	  /**
	   * 
	   * Accedeix al fitxer de properties i agafa totes les 
	   * dades d'aquest per inicialitzar la variable propietats 
	   * amb aquest contingut.
	   *
	   */
	  private void ini() {
		  	
		    try {
		      FileInputStream f =
		        new FileInputStream(this.getClass().getClassLoader()
                        .getResource(propertieFile).getFile());

		      Properties propietatsTemporals = new Properties();
		      propietatsTemporals.load(f);
		      f.close();
              properties = new HashMap(propietatsTemporals);		      
		    } catch (Exception e) {
		      /* Manejo de excepciones
		       * Código temporal hasta saber que hacer con las excepciones
		       * esté código puede lanzar excepciones? que pasa si hay
		       * una excepción? Se supone que no.
		       */
		    	
		    	//s'inicialitza el HasMap de les dades del fitxer de 
		    	//configuració
		    	properties = null;
		    	
		    	System.err.print("CollectionProperties Error: " 
                        + e.getMessage());
		    	
		    }
			  
	  }
	  
	  /**
	   * Retorna el valor que té el parametre demanat al fitxer de
	   * configuració. 
	   * 
	   * @param aName nom del paràmetre a consultar.
	   * @return string amb el valor associat al paràmetre en el 
	   * 		 fitxer de configuració.
	   * @throws NonExistPropertieException
	   */
	  public final String getPropiedad(final String aName)
	        throws NotFoundPropertiesException {

		 String valor = null;
		  
	    if (properties != null) {
	    	valor = (String) properties.get(aName);
	    }

	    if (valor == null) {
	      throw new NotFoundPropertiesException(aName);
	    }

	    return valor;
	  }
      
      public HashMap getProperties()
      {
          return properties;
      }
}
