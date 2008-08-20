// $Id: SequencingCondition.java,v 1.4 2008/01/18 18:07:24 ecespedes Exp $
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
	Types.OperatorType;

/** <!-- Javadoc -->
 * <b>Títol:</b> SequencingCondition <br><br>
 * <b>Descripció:</b> Simplement és una enumeració amb els elements de la <br>
 * condició que pot tindre les regles de seqüenciament i un operador boolea<br>
 *  per indicar si ho neguem o no.<br><br>
 * 
 * @see SCORM2004 Sequencing & Navigation 3rt Edition
 * @author Eduard Céspedes i Borràs / LaSalle / ecespedes@salle.url.edu
 * @version 0.1
 */

public class SequencingCondition implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6923557747902111509L;
	/**
	 * Aquest tipus defineix el tipus de condició que estem
	 * tractant.
	 * 
	 * @author Eduard Céspedes i Borràs (AKA ecespedes)
	 */
	public static enum conditionType {
		/**
		 * S'avaluarà sempre.
		 */
		always,
		/**
		 * satisfied: si es satisfà la condició.
		 */
		satisfied,
		/**
		 * completed: Si està completat.
		 */
		completed,
		/**
		 * attempted: Si s'hi ha accedit un cop (si està satisfeta 
		 * però no completa).
		 */
		attempted,
		/**
		 * attemptLimitExceeded: Si s'ha superat el temps límit.
		 */
		attemptLimitExceeded,
		/**
		 * objectiveMeasureGreaterThan: Si el resultat de l'objectiu és
		 * més gran que el measureThreshold.
		 */
		objectiveMeasureGreaterThan,
		/**
		 * objectiveMeasureLessThan: Si el resultat de l'objectiu és
		 * més petit que el measureThreshold.
		 */
		objectiveMeasureLessThan,
		/**
		 * objectiveStatusKnown : Es complirà si coneixem l'estat de 
		 * l'objectiu.
		 */
		objectiveStatusKnown,
		/**
		 * objectiveMeasureKnown : Es complirà si coneixem la mesura de 
		 * l'objectiu.
		 */
		objectiveMeasureKnown,
		/**
		 * activityProgressKnown : Es complirà si coneixem la mesura de
		 * l'activitat.
		 */
		activityProgressKnown,
		/**
		 * Fora del marge de temps? gens clar!
		 * TODO: outsideAvailableTimeRange
		 */
		outsideAvailableTimeRange
		};
		
	/**
	 * <b>condition</b> <i>(required, default always)</i>: <br />
	 * Aquest atribut representa la condició actual per 
	 * aquesta condició : { <br />
	 * <b>satisfied :</b> S’avalua a true si l’estat de progressió 
	 * i l’estat de satisfacció<br />  
	 *  associats a l’activitat estan a true. <br />
	 * <b>objectiveStatusKnown :</b> S’avalua a true si l’estat de progressió 
	 * està a true <br />
	 * <b>objectiveMeasureKnown : </b> S’avalua a true si l’estat de 
	 * la mesura està a true <br />
	 * <b>objectiveMeasureGreaterThan : </b> S’avalua a true si l’estat 
	 * de la mesura està a true<br />  
	 *   i si la mesurà de normalització supera el llindar (el Threshold).<br />
	 * <b>objectiveMesureLessThan : </b> S’avalua a true si l’estat de 
	 * la mesura està a true<br />  
	 *   i si la mesurà de normalització es inferior al 
	 *   llindar (el Threshold).<br />
	 * <b>completed </b>: S’avalua a true si l’estat dels intents 
	 * (l’Attempted) és true i si<br />  
	 *  l’estat de completat també està a true.<br />
	 * <b>activityProgressKnown : </b> S’avalua a cert si l’estat de 
	 * progressió s’avalua a <br />  
	 *  true i si l’estat dels accessos (attempted) també s’avalua a true.<br />
	 * <b>attempted </b>: S’avalua a cert si l’estat de la progressió és true 
	 * i si el contador<br />
	 *  d’una activitat es positiu (o sigui, si s’ha accedit). <br /> 
	 * <b>attemptLimitExceeded </b>: S’avalua a cert si l’estat del progrés per
	 *  l’activitat<br />
	 *  és true i el contador d’accessos es igual o superior a la condició límit
	 *   d’accessos per una activitat. <br />
	 * <b>always  </b>:Sempre s’avalua a cert <br />  
	 * 
	 * @author Eduard Céspedes i Borràs
	 */	
	public conditionType condition = conditionType.always;
	/**
	 * <b>operator</b> <i>(optional, default noOp)</i>: <br />
	 * Serveix per negar una condició o deixar-la tal qual<br />
	 * :<b> { not | noOp }</b>.<br /> 
	 * 
	 * @author Eduard Céspedes i Borràs
	 */	
	public OperatorType operator = OperatorType.NoOp;
	/**
	 * <b>mesureThreshold</b> <i>(optional, default 0.0)</i>: <br />
	 * Aquest valor és utilitzat com a llindar durant la <br />
	 * condició d’avaluació. <br />
	 * 
	 * @author Eduard Céspedes i Borràs
	 */
	public double measureThreshold = 0.0;
	/**
	 * <b>referencedObjective</b><i> (optional)</i>: <br />
	 * Indica si la condició fa referència a un altre element <br />
	 * durant l’avaluació de la condició. <br />
	 * 
	 * @author Eduard Céspedes i Borràs
	 */
	public String referencedObjective = null;
		
}

