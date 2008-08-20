// $Id: ObjectiveInterface.java,v 1.10 2007/12/13 15:25:12 ecespedes Exp $
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

import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.
	UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;

/**
* $Id: ObjectiveInterface.java,v 1.10 2007/12/13 15:25:12 ecespedes Exp $
* <b>T�tol:</b> ObjectiveInterface<br><br>
* <b>Descripci�:</b> Aquesta interf�cie far� que tot el proc�s de <br>
* demanar els valors que realment ens interessen pel seq�enciament<br>
* sigui molt m�s transparent. <br>
* Em de recordar que la majoria de "variables" podran tindre tres<br>
* valors: { true, false, unknown }
* <br><br>
* 
* Classes que l'Implementaran:
* <ol> 
* <li> Abstract_Item
* <li> Sequencing
* <li> ObjectiveHandler
* <li> Objective 
*</ol>
*<br>
* @author Eduard C�spedes i Borr�s /Enginyeria La Salle/ ecespedes@salle.url.edu
* @version Versi� $Revision: 1.10 $ $Date: 2007/12/13 15:25:12 $
* $Log: ObjectiveInterface.java,v $
* Revision 1.10  2007/12/13 15:25:12  ecespedes
* Problemes amb el sistema d'arbre de clusters.
* Falla l'ObjectiveStatusKnown.
*
* Revision 1.9  2007/12/11 16:00:43  ecespedes
* Suprimint par�metres: ObjectiveInterface el par�metre RollupRule.
*
* Revision 1.8  2007/12/09 22:32:16  ecespedes
* Els objectius dels clusters es tracten i es guarden de manera diferent.
*
* Revision 1.7  2007/12/03 17:58:51  ecespedes
* Implementat un attemptCount que ens servir� per l'attemptLimit.
*
* Revision 1.6  2007/11/26 15:12:36  ecespedes
* Ja controlem el Progress Measure.
*
* Revision 1.5  2007/11/20 15:24:34  ecespedes
* Les RollupRules ja estan implementades.
* Modificaci� del ObjectiveInterface.
*
* Revision 1.4  2007/11/19 15:26:58  ecespedes
* Treballant sobre el RollupRule (2 de 3)
*
* Revision 1.3  2007/11/15 15:24:04  ecespedes
* Implementat el ObjectiveInterface en els Leaf_Item.
*
* Revision 1.2  2007/11/14 12:33:36  ecespedes
* Implementada l'interface: ObjectiveInterface, de manera que ara totes
* les classes vinculades al seq�enciament l'implementen.
*
*/
public interface ObjectiveInterface {
	/**
	 * Si volem saber si una activitat est� satisfeta preguntarem 
	 * passant com a par�metre afirmmative = TRUE, d'aquesta manera
	 * si ens retorna un Passed voldr� dir que s� que est� satisfeta.
	 * 
	 * Si volem saber si una activitat NO est� satisfeta preguntarem
	 * passant com a par�metre afirmative = FALSE. D'aquesta manera
	 * si ens retorna un Passed voldr� dir que NO est� satisfeta.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param afirmative : boolean Type { True:satisfied, False:notSatisfied }
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	 */
	Constants.SequencingStatus satisfied(
			UserObjective userObjective,
			boolean afirmative);
	
	/**
	 * Si volem saber si una activitat est� completada preguntarem 
	 * passant com a par�metre afirmmative = TRUE, d'aquesta manera
	 * si ens retorna un Passed voldr� dir que s� que est� completed.
	 * 
	 * Si volem saber si una activitat �s incomplete preguntarem
	 * passant com a par�metre afirmative = FALSE. D'aquesta manera
	 * si ens retorna un Passed voldr� dir que �s incomplete.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param afirmative : boolean Type { True:completed, False:incomplete }
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	 */
	Constants.SequencingStatus completed(
			UserObjective userObjective,
			boolean afirmative);
	
	/**
	 * Ens ha de retorar un boole� indicant-nos si l'activitat ha
	 * estat accedida (passed), no ha estat accedida (failed) o 
	 * no ho sabem (unknown).
	 * 
	 * A aquesta funci� li passarem tota la regla de RollupRule perqu�
	 * cada 'node' pugui agafar tots els par�metres necessaris per 
	 * tractar la condici�: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos �ltims valors tamb� haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 
	Constants.SequencingStatus attempted(UserObjective userObjective);	
	/**
	 * Ens ha de retorar un boole� indicant-nos si �s coneix 
	 * la mesura dels objectius (Objective Measure == passed),
	 * no hi ha mesura (failed) o no ho sabem (unknown).
	 * 
	 * A aquesta funci� li passarem tota la regla de RollupRule perqu�
	 * cada 'node' pugui agafar tots els par�metres necessaris per 
	 * tractar la condici�: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos �ltims valors tamb� haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	Constants.SequencingStatus objectiveMeasureKnown(
			UserObjective userObjective);
	/**
	 * Si objectiveMeasureKnown �s igual a 'passed' aleshores aquesta
	 * funci� ens retornar� aquesta mesura, que ser� un flotant.
	 * 
	 * A aquesta funci� li passarem tota la regla de RollupRule perqu�
	 * cada 'node' pugui agafar tots els par�metres necessaris per 
	 * tractar la condici�: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos �ltims valors tamb� haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Double: Puntuaci� amb un m�xim de quatre decimals.
	*/ 	
	Double objectiveMeasure(UserObjective userObjective);
	/**
	 * Ens ha de retorar un boole� indicant-nos si �s coneix 
	 * l'estat de l'objectiu (passed), si no el coneixem (failed)
	 * o si no ho sabem (unknown).
	 * 
	 * A aquesta funci� li passarem tota la regla de RollupRule perqu�
	 * cada 'node' pugui agafar tots els par�metres necessaris per 
	 * tractar la condici�: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos �ltims valors tamb� haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	Constants.SequencingStatus objectiveStatusKnown(
			UserObjective userObjective);
	/**
	 * Ens ha de retorar un boole� indicant-nos si �s coneix 
	 * la mesura de la progressi� dels objectius (passed),
	 * no hi ha mesura (failed) o no ho sabem (unknown).
	 * 
	 * A aquesta funci� li passarem tota la regla de RollupRule perqu�
	 * cada 'node' pugui agafar tots els par�metres necessaris per 
	 * tractar la condici�: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos �ltims valors tamb� haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/ 	
	Constants.SequencingStatus activityProgressKnown(
			UserObjective userObjective);
	/**
	 * Amb aquesta funci� ens solucionem la vida a l'hora de passar aquest
	 * par�metre entre classes que implementin aquesta interf�cie. 
	 * 
	 * @return boolean Type. 
	 */
	boolean getSatisfiedByMeasure();
	/**
	 * Amb aquesta funci� ens solucionem la vida a l'hora de passar aquest
	 * valor entre classes que implementin aquesta interf�cie.
	 * 
	 * @return Double Type.
	 */
	Double getMinNormalizedMeasure();
	
	
	//		------------------------------------------------
	
	//public boolean isSatisfiedByMeasure();
	//public double getMinNormalizedMeasure();
	//public void setMinNormalizedMeasure(double minNormalizedMeasure);
		
	
}

