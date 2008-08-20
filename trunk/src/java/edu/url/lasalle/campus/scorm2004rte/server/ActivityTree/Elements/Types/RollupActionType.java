// $Id: RollupActionType.java,v 1.3 2008/01/18 18:07:24 ecespedes Exp $
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
* $Id: RollupActionType.java,v 1.3 2008/01/18 18:07:24 ecespedes Exp $
* <b>Títol:</b> RollupActionType<br><br>
* <b>Descripció:</b> Enumeració que ens indicarà quina acció s'ha de<br>
* fer en cas de que es compleixi la condició del Rollup. <br><br> 
*
* @author Eduard Céspedes i Borràs /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versió $Revision: 1.3 $ $Date: 2008/01/18 18:07:24 $
* $Log: RollupActionType.java,v $
* Revision 1.3  2008/01/18 18:07:24  ecespedes
* Serialitzades TOTES les classes per tal de que els altres puguin fer proves
* en paral·lel amb el procés de desenvolupament del gestor de BD.
*
* Revision 1.2  2007/11/19 15:26:58  ecespedes
* Treballant sobre el RollupRule (2 de 3)
*
*/
public enum RollupActionType implements Serializable {
	/**
	 * Pujarem la condició de 'satisfied'.
	 */
	satisfied,
	/**
	 * Pujarem la condició de 'notSatisfied'.
	 */
	notSatisfied,
	/**
	 * Pujarem la condició de 'completed'.
	 */
	completed,
	/**
	 * Pujarem la condició de 'incomplete'.
	 */
	incomplete
}

