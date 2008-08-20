// $Id: DBConnector.java,v 1.5 2008/02/12 15:43:22 ecespedes Exp $
/*
 * Copyright (c) [yyyy] [TITULAR]
 * This file is part of [SSSSS].  
 * 
 * [SSSS] is free software; you can redistribute it and/or modify 
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  
 * 02110-1301, USA.  
 */
package edu.url.lasalle.campus.scorm2004rte.server.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.url.lasalle.campus.scorm2004rte.util.
	exception.NotFoundPropertiesException;
import edu.url.lasalle.campus.scorm2004rte.util.
	resource.CollectionProperties;

/**
* $Id: DBConnector.java,v 1.5 2008/02/12 15:43:22 ecespedes Exp $
* <b>Títol:</b> DBConnector<br><br>
* <b>Descripció:</b> Aquesta classe establirà una connexió amb <br>
* la BD, de manera que la classe que guarda i la classe que llegeix<br>
* l'extendran. <br><br> 
*
* @author Eduard Céspedes i Borràs/Enginyeria La Salle/ecespedes@salle.url.edu
* @version Versió $Revision: 1.5 $ $Date: 2008/02/12 15:43:22 $
* $Log: DBConnector.java,v $
* Revision 1.5  2008/02/12 15:43:22  ecespedes
* Millorant el sistema (4)
*
* Revision 1.4  2008/02/01 18:39:19  toroleo
* Parametrització de les dades de la
* base de dades.
*
* Revision 1.3  2008/02/01 03:07:32  ecespedes
* Versió Beta Final amb la BD Serialitzada.
*
* Revision 1.2  2008/01/29 18:01:41  ecespedes
* Implementant versió serialitzada de l'organització.
*
* Revision 1.1  2008/01/24 15:41:11  ecespedes
* Dissenyant un nou sistema per gestionar les Bases de dades (DataAccess)
*
*/
public class DBConnector {
	/**
	 * Constant String Type.
	 * "root"
	 */
	//private static String userName = "root";
	private static String userName = null;
	/**
	 * Constant String Type. 
	 */
    //private static String password = "admin";
	private static String password = null;
    /**
     * Constant String Type.
     * "jdbc:mysql://localhost/test?user=" + userName + "&password="
     * 		+ password;
     */
    //private static String url = "jdbc:mysql://localhost/scorm2004serialized";
	private static String url = null;
	/**
	 * Constant int Type.
	 * Required.
	 * Description: L'identificador del DataAccess.
	 */
    protected static final int DATAACCESSID = 1;
    /**
	 * Constant String Type.
	 * Required.
	 * Description del DataAccess.
	 * 		"MySQL Database. We serialize and save the 
	 * 		UserObjective and the Abstract_Item of the 
	 * 		organization root node.";
	 */
    protected static final String DESCRIPTION = "MySQL Database. "
    	+ "We serialize and save the UserObjective and the Abstract_Item "
    	+ "of the organization root node.";
    
    /**
	 * Creem el connector per la base de dades. 
	 */
	protected Connection conn = null;
	
	/**
	 * El constructor per defecte serà privat ja que és tracta 
	 * d'un singleton, per tant tindrem una única instància 
	 * d'aquesta classe en memòria. 
	 */
	protected DBConnector() {
        try {
        	CollectionProperties cp = new CollectionProperties();
	      	userName = cp.getPropiedad("dbconnector.userName");
	      	password = cp.getPropiedad("dbconnector.password");
	      	//userName = "root";
	      	//password = "admin123";
	      	url = cp.getPropiedad("dbconnector.url");
	      	//url = "jdbc:mysql://localhost/scorm2004serialized";
	      	
			Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (NotFoundPropertiesException e) {
        	System.out.println("[DBConnector-" 
					+ "NotFoundPropertiesException]DBConnector()");
			e.printStackTrace();		
	    } catch (InstantiationException e) {
			System.out.println("[DBConnector-" 
					+ "InstantiationException]DBConnector()");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("[DBConnector-" 
					+ "IllegalAccessException]DBConnector()");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("[DBConnector-" 
					+ "ClassNotFoundException]DBConnector()");
			e.printStackTrace();
		}
        try {
			conn = DriverManager.getConnection(url, userName, password);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        System.out.println("Database connection established");
	}	

}
