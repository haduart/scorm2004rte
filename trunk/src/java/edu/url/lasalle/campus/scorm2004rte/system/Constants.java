// $Id: Constants.java,v 1.11 2007/12/10 11:50:27 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.system;

/**
 * $Id: Constants.java,v 1.11 2007/12/10 11:50:27 ecespedes Exp $
 *
 * La classe t� com a prop�sit emmagatzemar les constants generals del projecte.
 *
 * @author Eduard C�spedes / Enginyeria La Salle / haduart@gmail.com
 * @version 1.0 $Revision: 1.11 $ $Date: 2007/12/10 11:50:27 $
 * $Log: Constants.java,v $
 * Revision 1.11  2007/12/10 11:50:27  ecespedes
 * Comen�ant a juntar les peces del proc�s de seq�enciament.
 *
 * Revision 1.10  2007/12/05 13:36:59  ecespedes
 * Arreglat bug limitConditions i PreConditions
 *
 * Revision 1.9  2007/11/28 08:02:32  msegarra
 * Actualitzaci� de noves constants per al seu �s en l'aplicaci� web.
 *
 * Revision 1.8  2007/11/27 15:34:25  ecespedes
 * Arreglats bugs relacionats amb el Rollup. Creat nou joc de testos.
 *
 * Revision 1.7  2007/11/19 15:26:58  ecespedes
 * Treballant sobre el RollupRule (2 de 3)
 *
 * Revision 1.6  2007/11/08 16:33:09  ecespedes
 * Comen�ant a Implementar el OverallSequencingProcess, que ser�
 * les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
 * realment el seq�enciament SCORM1.3
 *
 * Revision 1.5  2007/11/05 19:42:39  ecespedes
 * Comen�ant a implementar el motor del seq�enciament.
 * S'aniran fent testos per tal de veure diversos 'request'
 * per tal d'emular el que seria un usuari que interactua.
 *
 * Revision 1.4  2007/10/29 07:30:55  ecespedes
 * Arreglant errors i solucionant problemes a l'hora de crear les
 * estructures que guardaran les dades serialitzades dels
 * usuaris.
 *
 * Revision 1.3  2007/10/26 22:25:21  ecespedes
 * Ara el parser crea l'estructura b�sica de l'usuari.
 * Arreglat diversos errors, checkstyles i recursivitats.
 *
 * Revision 1.2  2007/10/24 10:33:10  ecespedes
 * Pujant la versi� actual del scorm2004rte.
 *
 * Revision 1.1  2007/10/22 20:22:15  msegarra
 * Creaci� de l'arbre CVS del projecte.
 *
 */
public class Constants {
	/**
	 * Constant boolean Type.
	 */    
	public static final boolean DEBUG_ERRORS = true;
	/**
	 * Constant boolean Type.
	 */
	public static final boolean DEBUG_WARNINGS = true;
	/**
	 * Constant boolean Type.
	 */
	public static final boolean DEBUG_WARNINGS_LOW = true;
	/**
	 * Constant boolean Type.
	 */
	public static final boolean DEBUG_INFO = false;
	/**
	 * Constant boolean Type.
	 */
	public static final boolean DEBUG_METADATA = false;
	/**
	 * Constant boolean Type.
	 */
	public static final boolean DEBUG_RESOURCES = false;
	/**
	 * Constant boolean Type.
	 */
	public static final boolean DEBUG_SEQUENCING = true;
	/**
	 * Constant boolean Type.
	 */
	public static final boolean DEBUG_ITEMS = false;
	/**
	 * Constant boolean Type.
	 */
	public static final boolean DEBUG_NAVIGATION = true;
	/**
	 * Constant boolean Type.
	 */
	public static final boolean DEBUG_NAVIGATION_LOW = false;
	
	/**
	 * Constant boolean Type.
	 */    
    public static final String FALSE = "false";
    /**
	 * Constant boolean Type.
	 */
    public static final String TRUE = "true";   
    
    /**
	 * Constant String Type, "identifier".
	 */
    public static final String IDENTIFIER           =   "identifier";
    /**
	 * Constant String Type, "organization".
	 */
    public static final String ORGANIZATION         =   "organization";
    /**
	 * Constant String Type, "title".
	 */
    public static final String TITLE                =   "title";
    /**
	 * Constant String Type, "item".
	 */
    public static final String ITEM                 =   "item";
    /**
	 * Constant String Type, "sequencing".
	 */
    public static final String SEQUENCING           =   "sequencing";
    /**
	 * Constant String Type, "objectives".
	 */
    public static final String OBJECTIVES           =   "objectives";
    /**
	 * Constant String Type, "primaryObjective".
	 */
    public static final String PRIMARYOBJECTIVE     =   "primaryObjective";
    /**
	 * Constant String Type, "objective".
	 */
    public static final String OBJECTIVE            =   "objective";
    /**
	 * Constant String Type, "resource".
	 */
    public static final String RESOURCE             =   "resource";
    /**
	 * Constant String Type, "identifier".
	 */
    public static final String RES_SCORMTYPE        =   "scormType";
    /**
	 * Constant String Type, "identifier".
	 */
    public static final String HREF                 =   "href";
    /**
	 * Constant String Type, "identifier".
	 */
    public static final String FILE                 =   "file";
    /**
	 * Constant String Type, "identifier".
	 */
    public static final String ID                   =   "ID";
    /**
	 * Constant String Type, "identifier".
	 */
    public static final String IDREF                =   "IDRef";
    /**
     * Aquesta enumeraci� ens dir� si una variable del seq�enciament
     * est� superada (Passed), fallada (Failed) o simplement no en 
     * tenim informaci� (Unknown).
     * 
     * @author Eduard C�spedes i Borr�s (AKA ecespedes)
     *
     */
    public static enum SequencingStatus {
    	/**
    	 * Unknown : No en tenim informaci�.
    	 */
        Unknown,
        /**
    	 * Failed : AKA False.
    	 */
        Failed,
        /**
    	 * Passed : AKA True.
    	 */
        Passed
    }
    
    /**
     * Constant String Type.
     * The configuration file name of the web application.
     */
    public static final String CONFIG_FILE_NAME = "config.properties";
    
    /**
     * Constant int Type.
     * Tamany m�xim del llindar d'emmagatzematge en mem�ria (300 Kbytes).
     * Si un fitxer que es pugi (ServletFileUpload) al servidor est� per sota 
     * del llindar, s'emmagatzemar� en mem�ria. En cas contrari, s'emmagatzemar� 
     * temporalment en disc. 
     */
    public static final int MAX_SIZE_MEMORY = 307200;
}
