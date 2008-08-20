// $Id: ChildActivitySetType.java,v 1.3 2008/01/18 18:07:24 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.Types;

import java.io.Serializable;

/**
* $Id: ChildActivitySetType.java,v 1.3 2008/01/18 18:07:24 ecespedes Exp $
* <b>Títol:</b> ChildActivitySetType<br><br>
* <b>Descripció:</b> És una classe enum que ens servirà per parsejar<br>
* l'abast de la condició del rollup, si afecta a tots els fills<br>
* seria 'all', si amb un que ho compleixi ja n'hi ha prou (any), etc.
* <br><br> 
*
* @author Eduard Céspedes i Borràs /Enginyeria LaSalle/ ecespedes@salle.url.edu
* @version Versió $Revision: 1.3 $ $Date: 2008/01/18 18:07:24 $
* $Log: ChildActivitySetType.java,v $
* Revision 1.3  2008/01/18 18:07:24  ecespedes
* Serialitzades TOTES les classes per tal de que els altres puguin fer proves
* en paral·lel amb el procés de desenvolupament del gestor de BD.
*
* Revision 1.2  2007/11/19 15:26:58  ecespedes
* Treballant sobre el RollupRule (2 de 3)
*
*/
public enum ChildActivitySetType implements Serializable {
	/**
	 * Si cap 'fill' ho compleix.
	 */
	none,
	/**
	 * Si algun 'fill' ho compleix.
	 */
	any,
	/**
	 * Si tots els 'fills' ho compleixen.
	 */
	all,
	/**
	 * Si com a minim X 'fills' ho compleixen.
	 * On X serà un enter >= 0 i vindrà determinat per 
	 * l'atribut minimumCount del RollupRule.  
	 */
	atLeastCount,
	/**
	 * Si com a minim X% de 'fills' ho compleixen.
	 * On X serà un enter [ min 0, max 100 ] i vindrà determinat per 
	 * l'atribut minimumPercent del RollupRule.  
	 */
	atLeastPercent
}
