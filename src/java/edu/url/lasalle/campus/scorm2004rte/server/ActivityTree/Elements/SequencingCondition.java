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
 * <b>T�tol:</b> SequencingCondition <br><br>
 * <b>Descripci�:</b> Simplement �s una enumeraci� amb els elements de la <br>
 * condici� que pot tindre les regles de seq�enciament i un operador boolea<br>
 *  per indicar si ho neguem o no.<br><br>
 * 
 * @see SCORM2004 Sequencing & Navigation 3rt Edition
 * @author Eduard C�spedes i Borr�s / LaSalle / ecespedes@salle.url.edu
 * @version 0.1
 */

public class SequencingCondition implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6923557747902111509L;
	/**
	 * Aquest tipus defineix el tipus de condici� que estem
	 * tractant.
	 * 
	 * @author Eduard C�spedes i Borr�s (AKA ecespedes)
	 */
	public static enum conditionType {
		/**
		 * S'avaluar� sempre.
		 */
		always,
		/**
		 * satisfied: si es satisf� la condici�.
		 */
		satisfied,
		/**
		 * completed: Si est� completat.
		 */
		completed,
		/**
		 * attempted: Si s'hi ha accedit un cop (si est� satisfeta 
		 * per� no completa).
		 */
		attempted,
		/**
		 * attemptLimitExceeded: Si s'ha superat el temps l�mit.
		 */
		attemptLimitExceeded,
		/**
		 * objectiveMeasureGreaterThan: Si el resultat de l'objectiu �s
		 * m�s gran que el measureThreshold.
		 */
		objectiveMeasureGreaterThan,
		/**
		 * objectiveMeasureLessThan: Si el resultat de l'objectiu �s
		 * m�s petit que el measureThreshold.
		 */
		objectiveMeasureLessThan,
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
		activityProgressKnown,
		/**
		 * Fora del marge de temps? gens clar!
		 * TODO: outsideAvailableTimeRange
		 */
		outsideAvailableTimeRange
		};
		
	/**
	 * <b>condition</b> <i>(required, default always)</i>: <br />
	 * Aquest atribut representa la condici� actual per 
	 * aquesta condici� : { <br />
	 * <b>satisfied :</b> S�avalua a true si l�estat de progressi� 
	 * i l�estat de satisfacci�<br />  
	 *  associats a l�activitat estan a true. <br />
	 * <b>objectiveStatusKnown :</b> S�avalua a true si l�estat de progressi� 
	 * est� a true <br />
	 * <b>objectiveMeasureKnown : </b> S�avalua a true si l�estat de 
	 * la mesura est� a true <br />
	 * <b>objectiveMeasureGreaterThan : </b> S�avalua a true si l�estat 
	 * de la mesura est� a true<br />  
	 *   i si la mesur� de normalitzaci� supera el llindar (el Threshold).<br />
	 * <b>objectiveMesureLessThan : </b> S�avalua a true si l�estat de 
	 * la mesura est� a true<br />  
	 *   i si la mesur� de normalitzaci� es inferior al 
	 *   llindar (el Threshold).<br />
	 * <b>completed </b>: S�avalua a true si l�estat dels intents 
	 * (l�Attempted) �s true i si<br />  
	 *  l�estat de completat tamb� est� a true.<br />
	 * <b>activityProgressKnown : </b> S�avalua a cert si l�estat de 
	 * progressi� s�avalua a <br />  
	 *  true i si l�estat dels accessos (attempted) tamb� s�avalua a true.<br />
	 * <b>attempted </b>: S�avalua a cert si l�estat de la progressi� �s true 
	 * i si el contador<br />
	 *  d�una activitat es positiu (o sigui, si s�ha accedit). <br /> 
	 * <b>attemptLimitExceeded </b>: S�avalua a cert si l�estat del progr�s per
	 *  l�activitat<br />
	 *  �s true i el contador d�accessos es igual o superior a la condici� l�mit
	 *   d�accessos per una activitat. <br />
	 * <b>always  </b>:Sempre s�avalua a cert <br />  
	 * 
	 * @author Eduard C�spedes i Borr�s
	 */	
	public conditionType condition = conditionType.always;
	/**
	 * <b>operator</b> <i>(optional, default noOp)</i>: <br />
	 * Serveix per negar una condici� o deixar-la tal qual<br />
	 * :<b> { not | noOp }</b>.<br /> 
	 * 
	 * @author Eduard C�spedes i Borr�s
	 */	
	public OperatorType operator = OperatorType.NoOp;
	/**
	 * <b>mesureThreshold</b> <i>(optional, default 0.0)</i>: <br />
	 * Aquest valor �s utilitzat com a llindar durant la <br />
	 * condici� d�avaluaci�. <br />
	 * 
	 * @author Eduard C�spedes i Borr�s
	 */
	public double measureThreshold = 0.0;
	/**
	 * <b>referencedObjective</b><i> (optional)</i>: <br />
	 * Indica si la condici� fa refer�ncia a un altre element <br />
	 * durant l�avaluaci� de la condici�. <br />
	 * 
	 * @author Eduard C�spedes i Borr�s
	 */
	public String referencedObjective = null;
		
}

