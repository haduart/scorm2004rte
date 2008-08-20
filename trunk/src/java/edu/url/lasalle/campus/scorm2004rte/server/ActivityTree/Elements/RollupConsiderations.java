// $Id: RollupConsiderations.java,v 1.4 2008/01/18 18:07:24 ecespedes Exp $
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

/**
* $Id: RollupConsiderations.java,v 1.4 2008/01/18 18:07:24 ecespedes Exp $
* <b>Tï¿½tol:</b> RollupConsiderations<br><br>
* <b>Descripciï¿½:</b> ï¿½s el contenidor per les descripcions <br>
* de quan una activitat ha de ser inclosa en el procï¿½s de rollup.
* <br>
* @see (3.9. Rollup Consideration Controls)
*  ï¿½ 2004 Sequencing and Navigation (SN) Version 1.3.2 - DRAFT
* <br><br> 
*
* @author Eduard Cï¿½spedes i Borrï¿½s /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versiï¿½ $Revision: 1.4 $ $Date: 2008/01/18 18:07:24 $
* $Log: RollupConsiderations.java,v $
* Revision 1.4  2008/01/18 18:07:24  ecespedes
* Serialitzades TOTES les classes per tal de que els altres puguin fer proves
* en paral·lel amb el procés de desenvolupament del gestor de BD.
*
* Revision 1.3  2007/12/05 09:34:34  xgumara
* Implementat mètode equals i hashCode.
*
* Revision 1.2  2007/11/09 12:58:58  ecespedes
* Creant mï¿½todes perquï¿½ aixï¿½ poguem recï¿½rrer mï¿½s rï¿½pidament
* l'estructura de l'arbre.
*
*/
public class RollupConsiderations implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7704794531734174241L;

	/**
	 * Ens defineix el Vocabulary que poden tindre les 
	 * variables: requiredForSatisfied, requiredForNotSatisfied,
	 * requiredForCompleted, requiredForIncomplete.
	 * 
	 * @author Eduard Cï¿½spedes i Borrï¿½s (AKA ecespedes)
	 *
	 */
	public static enum rollupConsiderationsType {
		/**
		 * The child always contributes to the rollup evaluation
		 * of its parent. 
		 */
		always , 
		/**
		 * The child contributes to the rollup evaluation
		 * of its parent if it has been attempted
		 * at the time of the evaluation.
		 */
		ifAttempted ,
		/**
		 * The child contributes to the rollup evaluation
		 * of its parent if it is not skipped 
		 * at the time of the evaluation. 
		 */
		ifNotSkipped ,
		/**
		 * The child contributes to the rollup evaluation
		 * of its parent if it has been attempted
		 * but is not suspended at the time of
		 * the evaluation.
		 */
		ifNotSuspended };
	/**
	 * Indicates when the activityï¿½s tracking 
	 * information contributes to the rolled-up
	 * Satisfied status of its parent.
	 * 
	 * (rollupConsiderationsType, Default always)
	 */	
	public RollupConsiderations.rollupConsiderationsType 
		requiredForSatisfied =
			RollupConsiderations.rollupConsiderationsType.always;
	/**
	 * Indicates when the activityï¿½s tracking 
	 * information contributes to the rolled-up Not
	 * Satisfied status of its parent. 
	 * 
	 * (rollupConsiderationsType, Default always)
	 */
	public RollupConsiderations.rollupConsiderationsType 
		requiredForNotSatisfied  = 
			RollupConsiderations.rollupConsiderationsType.always;
	/**
	 * Indicates when the activityï¿½s tracking
	 * information contributes to the rolled-up
	 * Completed status of its parent.
	 * 
	 * (rollupConsiderationsType, Default always) 
	 */
	public RollupConsiderations.rollupConsiderationsType
		requiredForCompleted  = 
			RollupConsiderations.rollupConsiderationsType.always;
	/**
	 * Indicates when the activityï¿½s tracking
	 * information contributes to the rolled-up
	 * Incomplete status of its parent.
	 * 
	 * (rollupConsiderationsType, Default always)
	 */
	public RollupConsiderations.rollupConsiderationsType
		requiredForIncomplete  = 
			RollupConsiderations.rollupConsiderationsType.always;
	/**
	 * Indicates that the activityï¿½s rolled-up measure
	 * should be evaluated against the activityï¿½s
	 * Minimum Normalized Measure even if the
	 * activity is still active. 
	 */
	public boolean measureSatisfactionIfActive = true;
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (obj.getClass() != this.getClass())) {
                return false;
            }
            RollupConsiderations roll = (RollupConsiderations) obj;
            return (measureSatisfactionIfActive == roll.measureSatisfactionIfActive
                    && requiredForCompleted.equals(roll.requiredForCompleted)
                    && requiredForIncomplete.equals(roll.requiredForIncomplete)
                    && requiredForNotSatisfied.equals(roll.requiredForNotSatisfied)
                    && requiredForSatisfied.equals(roll.requiredForSatisfied));
        }
        
        @Override
        public int hashCode() {
            // TODO: hashCode for MapInfo
            return 0;
        }
}
