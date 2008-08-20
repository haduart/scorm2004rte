// $Id: CMIObjectives.java,v 1.5 2007/12/28 16:35:27 ecespedes Exp $
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

import java.io.Serializable;

/**
 * $Id: CMIObjectives.java,v 1.5 2007/12/28 16:35:27 ecespedes Exp $
 * Contindrà el model de dades de l'usuari respecte a un Objecte determinat
 * (indicat per 'id'). Aqueste 'Objective' nomès serà utilitzat pel 
 * seqüenciament si, i només si, 'id'.equals('ObjectiveID') == true.
 * 
 * @author Eduard Céspedes i Borràs
 * @version Versió $Revision: 1.5 $ $Date: 2007/12/28 16:35:27 $
 * $Log: CMIObjectives.java,v $
 * Revision 1.5  2007/12/28 16:35:27  ecespedes
 * Implementat l'OverallSequencingProcess.
 *
 * Revision 1.4  2007/11/15 15:24:04  ecespedes
 * Implementat el ObjectiveInterface en els Leaf_Item.
 *
 * Revision 1.3  2007/10/30 14:07:42  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Començant a crear el sistema de gestió dels cursos:
 * - CourseAdministrator i CourseManager.
 *
 */
public class CMIObjectives implements Serializable {
	
	
	/**
	 * Modificar a mida que varii aquest codi. (28-12-2007)
	 */
	private static final long serialVersionUID = -5002626527730752675L;
	
	/**
	 * Identificador de l'Objective. 
	 */
	public String id;
	/**
	 * cmi.objectives.n.score.scaled:
	 * Number that reflects the performance of the learner for the objective.
	 */
	public String scoreScaled = "unknown";
	/**
	 * cmi.objectives.n.score.raw:
	 * Number that reflects the performance of the learner, for the objective,
	 * relative to the range bounded by the values of min and max.
	 */
	public String scoreRaw = "unknown";
	/**
	 * cmi.objectives.n.score.min:
	 * Minimum value, for the objective, in the range of the raw score.
	 */
	public String scoreMin = "0";
	/**
	 * cmi.objectives.n.score.max;
	 * Maximum value, for the objective, in the range of the raw score.
	 */
	public String scoreMax = "10";
	/**
	 * cmi.objectives.n.score.success_status:
	 * Indicates whether the learner has mastered the objective.
	 * (state (passed, failed, unknown), RW)
	 */
	public String successStatus = "unknown";
	/**
	 * cmi.objectives.n.score.completion_status:
	 * Indicates whether the learner has completed the associated objective.
	 * (state (completed, incomplete, not attempted, unknown), RW)
	 */
	public String completionStatus = "unknown";
	/**
	 * cmi.objectives.n.score.progress_measure:
	 * Measure of the progress the learner has made toward completing the
	 * objective. 
	 */
	public String progressMeasure = "";
	/**
	 * cmi.objectives.n.score.description:
	 * Provides a brief information description of the objective.
	 */
	public String description = "";
	
}

