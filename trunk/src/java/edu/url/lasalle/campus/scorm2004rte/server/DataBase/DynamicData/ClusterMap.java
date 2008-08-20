// $Id: ClusterMap.java,v 1.2 2007/12/11 15:29:27 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData;

import java.io.Serializable;
import java.util.Hashtable;

/**
* $Id: ClusterMap.java,v 1.2 2007/12/11 15:29:27 ecespedes Exp $
* <b>Títol:</b> ClusterMap<br><br>
* <b>Descripció:</b> És una classe privada que l'única utilitzat serà <br>
* que ens gestionarà un HashMap de tots els objectius que hi podem <br>
* haver dintre d'un item que sigui cluster (primaris i secundaris).<br><br> 
*
* @author Eduard Céspedes i Borràs /Enginyeria La Salle/ecespedes@salle.url.edu
* @version Versió $Revision: 1.2 $ $Date: 2007/12/11 15:29:27 $
* $Log: ClusterMap.java,v $
* Revision 1.2  2007/12/11 15:29:27  ecespedes
* Arreglant bugs i optimitzant solucions.
*
*/
public class ClusterMap implements Serializable {
	/**
	 * Generated serial on 09-12-2007.
	 */
	private static final long serialVersionUID = -1264981215501545299L;
	/**
	 * Array dels objectius d'un item.
	 */
	public Hashtable < String, ClusterDataModel > objective =
		new Hashtable < String, ClusterDataModel > ();
	/**
	 * Constructor per defecte.
	 */
	public ClusterMap() {
		
	}
	
	
}	


