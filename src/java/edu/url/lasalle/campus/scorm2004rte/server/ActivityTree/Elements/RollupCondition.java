// $Id: RollupCondition.java,v 1.3 2008/01/18 18:07:24 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements;

import java.io.Serializable;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
	Types.ConditionType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
	Types.OperatorType;

/** <!-- Javadoc -->
 * $Id: RollupCondition.java,v 1.3 2008/01/18 18:07:24 ecespedes Exp $
 * <b>T�tol:</b> RollupCondition <br><br>
 * <b>Descripci�:</b> Simplement �s una enumeraci� amb els elements de la 
 * condici� que pot tindre les regles de RollUp i un operador boolea per 
 * indicar si ho neguem o no.<br><br>
 * 
 * @see SCORM2004 Sequencing & Navigation 3rt Edition
 * @author Eduard C�spedes i Borr�s / LaSalle / ecespedes@salleurl.edu
 * @version Versi� $Revision: 1.3 $ $Date: 2008/01/18 18:07:24 $
 * $Log: RollupCondition.java,v $
 * Revision 1.3  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral�lel amb el proc�s de desenvolupament del gestor de BD.
 *
 * Revision 1.2  2007/11/15 15:24:04  ecespedes
 * Implementat el ObjectiveInterface en els Leaf_Item.
 *
 */

public class RollupCondition implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9037049684461311858L;
	/**
	 * ConditionType Type.
	 */
	public ConditionType condition;
	/**
	 * OperatorType Type.
	 */
	public OperatorType operator = OperatorType.NoOp;
}

