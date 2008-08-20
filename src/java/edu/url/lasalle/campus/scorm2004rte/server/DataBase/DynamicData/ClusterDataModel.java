// $Id: ClusterDataModel.java,v 1.2 2007/12/13 15:25:12 ecespedes Exp $
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
 * $Id: ClusterDataModel.java,v 1.2 2007/12/13 15:25:12 ecespedes Exp $ * 
 * <b>Títol:</b> ClusterDataModel <br /><br />
 * <b>Descripció:</b> En aquesta classe hi guardarem un objective, ja sigui<br>
 * un definit sota demanda pel manifest o un que crearem automàticament per <br>
 * enmagatzemar l'estat del cluster i d'aquesta manera evitar-nos baixar a <br>
 * nivell dels fills per saber l'estat.<br><br>
 * 
 * @author Eduard Céspedes i Borràs /Enginyeria LaSalle/ ecespedes@salle.url.edu
 * 
 * @version 1.0 $Revision: 1.2 $ $Date: 2007/12/13 15:25:12 $
 * $Log: ClusterDataModel.java,v $
 * Revision 1.2  2007/12/13 15:25:12  ecespedes
 * Problemes amb el sistema d'arbre de clusters.
 * Falla l'ObjectiveStatusKnown.
 *
 * Revision 1.1  2007/12/09 22:32:16  ecespedes
 * Els objectius dels clusters es tracten i es guarden de manera diferent.
 * 
 */
public final class ClusterDataModel implements  Serializable {

	/**
	 * Modificar a mida que varii aquest codi. (13-12-2007)
	 */
	private static final long serialVersionUID = 41115711549582494L;
	
	/**
	 * És per no fer una relació amb el SequencingStatus.Unknown.
	 * int Type = 0
	 */
	private static final int UNKNOWN = 0;
	/**
	 * És per no fer una relació amb el SequencingStatus.Passed.
	 * int Type = 1
	 */
	private static final int PASSED = 1;
	/**
	 * És per no fer una relació amb el SequencingStatus.Failed.
	 * int Type = 2
	 */
	private static final int FAILED = 2;
	/**
	 * Aquest serà el valor inicial o en cas d'error s'assignarà 
	 * aquest valor a les variables. De manera que el seqüenciament
	 * quan rebi un -1 el que farà serà fer un rollup i actualitzar
	 * aquesta variable. 
	 * 
	 * int Type = -1
	 */
	private static final int NULL = -1;
	
	/**
	 * Ens ha de retorar un booleà indicant-nos si és coneix 
	 * la mesura de la progressió dels objectius (passed),
	 * no hi ha mesura (failed) o no ho sabem (unknown).
	 * 
	 * A aquesta funció li passarem tota la regla de RollupRule perquè
	 * cada 'node' pugui agafar tots els paràmetres necessaris per 
	 * tractar la condició: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos últims valors també haurem d'agafar minimumCount i 
	 * el minimumPercent.	 * 
	*/ 	
	public int activityProgressKnown = NULL;
	/**
	 * Ens ha de retorar un booleà indicant-nos si l'activitat ha
	 * estat accedida (passed), no ha estat accedida (failed) o 
	 * no ho sabem (unknown).
	 * 
	 * A aquesta funció li passarem tota la regla de RollupRule perquè
	 * cada 'node' pugui agafar tots els paràmetres necessaris per 
	 * tractar la condició: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos últims valors també haurem d'agafar minimumCount i 
	 * el minimumPercent. 
	 * 
	*/ 
	public int attempted = NULL;
	/**
	 * Ens ha de retorar un booleà indicant-nos si l'activitat està
	 * completada (passed), està incompleta (failed) o no ho sabem
	 * (unknown).
	 *  
	 */
	public int completed = NULL;
	/**
	 * Si objectiveMeasureKnown és igual a 'passed' aleshores aquesta
	 * funció ens retornarà aquesta mesura, que serà un flotant.
	 * 
	*/
	public Double objectiveMeasure = null;
	/**
	 * Ens ha de retorar un booleà indicant-nos si és coneix 
	 * la mesura dels objectius (Objective Measure == passed),
	 * no hi ha mesura (failed) o no ho sabem (unknown).
	 *  
	*/
	public int objectiveMeasureKnown = NULL;
	/**
	 * Ens ha de retorar un booleà indicant-nos si és coneix 
	 * l'estat de l'objectiu (passed), si no el coneixem (failed)
	 * o si no ho sabem (unknown).
	 * 
	*/
	public int objectiveStatusKnown = NULL;
	/**
	 * Ens ha de retorar un booleà indicant-nos si l'activitat està
	 * satisfeta (passed), no ha estat satisfeta (failed) o no ho 
	 * sabem (unknown).
	 * 
	 */
	public int satisfied = NULL;

}

