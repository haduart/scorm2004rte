// $Id: RollupRule.java,v 1.6 2008/01/18 18:07:24 ecespedes Exp $
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
import java.util.ArrayList;
import java.util.Collection;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Types.ChildActivitySetType;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Types.RollupActionType;

/** <!-- Javadoc -->
 * $Id: RollupRule.java,v 1.6 2008/01/18 18:07:24 ecespedes Exp $
 * <b>Títol:</b> RollupRule <br><br>
 * <b>Descripció:</b> Aquesta classe guarda les dades <b>d'una sola regla</b> 
 * de 'RollUp'.<br><br>
 * 
 * @see SCORM2004 Sequencing & Navigation 3rt Edition
 * @author Eduard Céspedes i Borràs / LaSalle / ecespedes@salle.url.edu
 * @version Versió $Revision: 1.6 $ $Date: 2008/01/18 18:07:24 $
 * $Log: RollupRule.java,v $
 * Revision 1.6  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral·lel amb el procés de desenvolupament del gestor de BD.
 *
 * Revision 1.5  2007/11/20 15:24:34  ecespedes
 * Les RollupRules ja estan implementades.
 * Modificació del ObjectiveInterface.
 *
 * Revision 1.4  2007/11/19 15:26:58  ecespedes
 * Treballant sobre el RollupRule (2 de 3)
 *
 * Revision 1.3  2007/11/19 07:34:15  ecespedes
 * Millores en els RollupRules.
 *
 * Revision 1.2  2007/11/15 15:24:04  ecespedes
 * Implementat el ObjectiveInterface en els Leaf_Item.
 *
 */


public class RollupRule implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3438027102400993322L;
	/**
	 * El tipus que ens defineix la forma en la que és combinen
	 * les condicions.
	 * 
	 * @author Eduard Céspedes i Borràs (AKA ecespedes)
	 */
	public static enum 	conditionCombinationType {
		
		/**
		 * S'han de complir totes les condicions (AND lògica).
		 */
		all,
		/**
		 * S'ha de complir alguna de les condicions (OR lògica).
		 * 
		 */
		any };
		
	/**
	 * Ens indicarà el tipus de combinació que controla les condicions.
	 * 
	 * required. Default "all".
	 */
	public conditionCombinationType conditionCombination =
			conditionCombinationType.all;
		
	/**
	 * <b>childActivitySet</b> <i>(optional, default all)</i>: Aquest 
	 * atribut indica quins <br />
	 * valors de les dades seran utilitzats per avaluar la condició 
	 * de rollup. <br /> 
	 * <i>Valors { all | any | none | atLeastCount | atLeastPercent } </i>.
	 * <br />
	 */
	public ChildActivitySetType childActivitySet = ChildActivitySetType.all;
	/**
	 * At Least Count – If at least the number, indicated by the 
	 * Rollup Minimum Count element, of the included activities have
	 * a Condition Combination that evaluates to True, then apply the
	 * specified Rollup Action. 
	 * 
	 * int Type, default 0
	 */
	public int minimumCount = 0;
	/**
	 * At Least Percent – If at least the percentage, indicated 
	 * by the Rollup Minimum Percent element, of the included activities
	 * have a Condition Combination that True, then apply the specified
	 * Rollup Action.
	 *  
	 *  double Type. default 0.0
	 */
	public double minimumPercent = 0.0;
	/**
	 * Les condicions de Rollup les enmagatzemarem en aquest array.
	 */	
	public Collection < RollupCondition > condition =
		new ArrayList < RollupCondition > ();
	/**
	 * 
	 * public enum rollupActionType { 
	 * satisfied , 
	 * notSatisfied , 
	 * completed , 
	 * incomplete  };.
	 *
	 */	
	public RollupActionType rollupAction =
		RollupActionType.notSatisfied;
}


