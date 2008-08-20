// $Id: CMIDataModel.java,v 1.15 2008/01/18 18:07:24 ecespedes Exp $
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

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import java.io.Serializable;

/** <!-- Javadoc -->
 * $Id: CMIDataModel.java,v 1.15 2008/01/18 18:07:24 ecespedes Exp $
 * <b>Títol:</b> CMIDataModel <br><br>
 * <b>Descripció:</b> Aquesta classe és l'estructura que guarda els elements 
 * d'un Objective.<br/>
 * Serà utilitzat per les classes UserObjectives, Abstract_Item, <br /> 
 * Root_Item i Leaf_Item. 
 * <br>Aquesta classe s'ha de poder serialitzar perquè s'haurà de <br />
 * convertir en un objecte Blob dintre de MySQL.<br><br>
 *  
 * @see SCORM2004 Sequencing & Navigation 3rt Edition
 * @author Eduard Céspedes i Borràs / LaSalle / ecespedes@salleurl.edu
 * @version Versió $Revision: 1.15 $ $Date: 2008/01/18 18:07:24 $
 * $Log: CMIDataModel.java,v $
 * Revision 1.15  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral·lel amb el procés de desenvolupament del gestor de BD.
 *
 * Revision 1.14  2008/01/08 17:42:46  ecespedes
 * Actualitzant el model de dades a partir del seqüenciament.
 *
 * Revision 1.13  2008/01/08 11:50:21  ecespedes
 * Ultimes modificacions de l'OverallSequencingProcess
 *
 * Revision 1.12  2008/01/07 15:59:21  ecespedes
 * Implementat 'deliveryControls' i el 'hideLMSUI' del Presentation.
 *
 * Revision 1.11  2008/01/04 11:35:32  ecespedes
 * Implementant l'OverallSequencingProcessTest sobre el linear_Choice.
 *
 * Revision 1.10  2008/01/03 15:05:32  ecespedes
 * Arreglats elements del CMIDataModel i començant a crear un nou test.
 *
 * Revision 1.9  2007/12/28 16:35:27  ecespedes
 * Implementat l'OverallSequencingProcess.
 *
 * Revision 1.8  2007/12/04 15:47:14  ecespedes
 * limitConditions implementat i preConditions testejat.
 *
 * Revision 1.7  2007/11/23 12:02:22  ecespedes
 * Començant a tocar el GestorBD en serio -> primer UserObjective serialitzat.
 *
 * Revision 1.6  2007/11/15 15:24:04  ecespedes
 * Implementat el ObjectiveInterface en els Leaf_Item.
 *
 * Revision 1.5  2007/11/13 15:59:59  ecespedes
 * Treballant sobre el sistema per "linkar" els Objectives de l'estructura
 * Sequencing de l'arbre amb els Objectives de l'usuari.
 * Millorat TreeAnnotations (step 2 de 3)
 *
 * Revision 1.4  2007/11/12 13:00:21  ecespedes
 * Arreglant elements del seqüenciament.
 * Ja qüasi està implementat el TreeAnnotation.
 *
 * Revision 1.3  2007/10/30 14:07:42  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Començant a crear el sistema de gestió dels cursos:
 * - CourseAdministrator i CourseManager.
 *
 */

public class CMIDataModel implements Serializable {
		
	/**
	 * Modificar a mida que varii aquest codi. (18-01-2008)
	 */
	private static final long serialVersionUID = 9213906674173256668L;
	/**
	 * Aquesta variable és purament de control però ens anirà perfecte
	 * perquè des del nivell de la classe Objective sapiguem si estem
	 * analitzant l'objective primary o un secundari. Diferencia?
	 *   Un objective primari pot tindre el seu resultat no només en el 
	 *   CMIObjectives sinó també en el CMIDataModel.
	 */
	public String primaryObjectiveID = null;
	
	//	------------------ Valors útils pels ASSETS ------------------ {	
	/**
	 * Número de vegades que l'usuari ha accedit a aquest SCO.
	 * Ens serà útil pel limitConditions: attemptLimit.
	 * (int Type, default 0)
	 * 
	 * Asset Required
	 */
	public int  activityAttemptCount = 0;	
	/**
	 * Primer cop que va accedir l'usuari a aquest SCO.
	 * (Date Type, default null)
	 * 
	 * Asset Required
	 */
	public Date firstAttemptedTime = null;
	/**
	 * Últim cop que l'usuari ha accedit a aquest SCO.
	 * (Date Type, default null)
	 * 
	 * Asset Required
	 */
	public Date lastAttemptedTime = null;
	
	//	} ------------------ END-Valors útils pels ASSETS ------------------

	/**
	 * Tots els Objectives primaris o secundaris que hi hagin, tan per que
	 * els ha creat el propi manifest com perquè l'SCO els ha generat.
	 */
	public Map < String, CMIObjectives > cmiObjectives =
		new Hashtable < String, CMIObjectives > ();
	/**
	 * cmi.score.completion_status:
	 * Indicates whether the learner has completed the SCO.
	 * (state (completed, incomplete, not attempted, unknown), RW)
	 */
	public String completionStatus = "unknown";
	/**
	 * cmi.success_status:
	 * Indicates whether the learner has mastered the SCO.
	 * (state (passed, failed, unknown), RW)
	 */
	public String successStatus = "unknown";
	/**
	 * cmi.score.scaled:
	 * Number that reflects the performance of the learner for the SCO.
	 */
	public String scoreScaled = "unknown";
	/**
	 * cmi.score.raw:
	 * Number that reflects the performance of the learner, for the SCO,
	 * relative to the range bounded by the values of min and max.
	 */
	public String scoreRaw = "unknown";
	/**
	 * cmi.score.min:
	 * Minimum value, for the SCO, in the range of the raw score.
	 */
	public String scoreMin = "0";
	/**
	 * cmi.score.max;
	 * Maximum value, for the objective, in the range of the raw sCO.
	 */
	public String scoreMax = "10";
	/**
	 * cmi.progress_measure:
	 * Measure of the progress the learner has made toward completing the
	 * SCO. 
	 */
	public String progressMeasure = "";
	/**
	 * cmi.location:
	 * The learner's current location in the SCO.
	 * Nota: Aquest valor serà controlat per l'SCO si realment l'interessa.
	 */
	public String location = "";
	/**
	 * cmi.suspend_data:
	 * Provides space to store and retrieve data between learner sessions. 
	 */
	public String suspendData = "";
	/**
	 * cmi.exit:
	 * Indicates how or why the learner left the SCO.
	 * (state (timeout, suspend, logout, normal, ""))
	 * (W)
	 */
	public String exit = "";
	/**
	 * cmi.session_time:
	 * Amount of time that the learner has spent in the current 
	 * learner session for this SCO.
	 * (WO)
	 * Nota: Aquest valor serà controlat per l'SCO si realment l'interessa.
	 * Nota2: Alerta amb el format! javax.xml.datatype.duration. 
	 */
	public String sessionTime = "PT0S";
	/**
	 * cmi.total_time:
	 * Sum of all of the learner's session times accumulated in the 
	 * current learner attempt.
	 * (RO)
	 * Nota: Aquest valor serà controlat per l'SCO si realment l'interessa.
	 * Nota2: Alerta amb el format! javax.xml.datatype.duration. 
	 */
	public String totalTime = "PT0S";
	/**
	 * cmi.scaled_passing_score:
	 * Scaled passing score required to master the SCO.
	 * 
	 * Nota: Aquest valor serà controlat per l'SCO si realment l'interessa.
	 * 
	 * Nota!
	 * TODO:If the imsss:satisfiedByMeasure attribute is set to true, then the
	 * imsss:minNormalizedMeasure element shall be used by the value 
	 * provided in the LMS to initialize the cmi.scaled_passing_score.
	 */
	public String scaledPassingScore = "";
	/**
	 * És el dataFromLMS del manifest.	 * 
	 */
	public String launchData = "";
	
	public String credit = "credit";
	
	public String entry = "ab-initio";	

	public String preferenceAudioLevel = "1";
	public String preferenceLanguage = "";
	public String preferenceDeliverySpeed = "1";
	public String preferenceAudioCaptioning = "0";
	/**
	 * Nota! Inicialitzar a partir del attemptAbsoluteDurationLimit!
	 * Per tant és feina de l'LMS.
	 */
	public String maxTimeAllowed = "";
	
	public String mode = "normal";
	/**
	 * És un camp opcional de l'item. El valor s'agafa del 
	 * manifest però és el propi SCO si s'encarregarà d'agafar-lo
	 * si el vol i el necessita.
	 * (String Type, default null)
	 * 
	 * Nota! Inicialitzat per l'LMS
	 */	
	public String completionThreshold = "";
	/**
	 * Aquest atribut ens indicarà el temps Limit. 
	 * És un atribut opcinal que no serà tractat per l'LMS, 
	 * l'encarregat serà el propi SCO.
	 * (String Type, default null)
	 * 
	 * Nota! Inicialitzat per l'LMS
	 */
	public String timeLimitAction = "continue,no message";
	
}
