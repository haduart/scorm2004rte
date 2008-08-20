// $Id: ConditionType.java,v 1.5 2008/01/18 18:07:24 ecespedes Exp $
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
* $Id: ConditionType.java,v 1.5 2008/01/18 18:07:24 ecespedes Exp $
* <b>T�tol:</b> ConditionType<br><br>
* <b>Descripci�:</b> Enumeraci� que ens servir� per saber quina condici�<br>
* s'est� efectuant.<br><br> 
*
* @author Eduard C�spedes i Borr�s /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versi� $Revision: 1.5 $ $Date: 2008/01/18 18:07:24 $
* $Log: ConditionType.java,v $
* Revision 1.5  2008/01/18 18:07:24  ecespedes
* Serialitzades TOTES les classes per tal de que els altres puguin fer proves
* en paral�lel amb el proc�s de desenvolupament del gestor de BD.
*
* Revision 1.4  2007/11/20 15:24:34  ecespedes
* Les RollupRules ja estan implementades.
* Modificaci� del ObjectiveInterface.
*
* Revision 1.3  2007/11/19 15:26:58  ecespedes
* Treballant sobre el RollupRule (2 de 3)
*
*/
public enum ConditionType implements Serializable {	
	/**
	 * satisfied : nom�s si l'activitat est� satisfeta.  
	 */
	satisfied,
	/**
	 * completed : nom�s si l'activitat est� complerta. 
	 */
	completed,
	/**
	 * attempted : nom�s si ha estat accedida. 
	 */
	attempted,
	/**
	 * attemptLimitExceeded : nom�s si s'ha accedit un temps l�mit. 
	 */
	attemptLimitExceeded,
	/**
	 * objectiveStatusKnown : Es complir� si coneixem l'estat de 
	 * l'objectiu.
	 */
	objectiveStatusKnown,
	/**
	 * objectiveMeasureKnown : Es complir� si coneixem la mesura de 
	 * l'objectiu.
	 */
	objectiveMeasureKnown,
	/**
	 * activityProgressKnown : Es complir� si coneixem la mesura de
	 * l'activitat.
	 */
	activityProgressKnown
	
}
