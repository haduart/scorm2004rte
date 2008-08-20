// $Id: MapInfo.java,v 1.6 2008/04/22 18:18:07 ecespedes Exp $
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


/** <!-- Javadoc -->
 * $Id: MapInfo.java,v 1.6 2008/04/22 18:18:07 ecespedes Exp $ 
 * <b>Títol:</b> MapInfo <br /><br />
 * <b>Descripció:</b> És el contenidor de la descripció mapejada de<br>
 * l'objectiu. Això defineix el mapeix de la informació de l'objectiu<br>
 * local d'una activitat de i per a un objectiu global compartit.<br>
 * <br>
 * Per llegir Objectives Maps (read{SatisfiedStatus|NormalizedMeasure}):<br>
 * 	Si existeixen múltiples elements <mapInfo> per a un objectiu,<br>
 * aleshores només un <mapInfo> pot tindre el readSatisfiedStatus a true.<br>
 * El mateix succeeix per a readNormalizedMeasure.<br>
 * <br>
 * Per escriure Objectives Maps (write{SatisfiedStatus|NormalizedMeasure}):<br>
 * Per una activitat, si hi ha múltiples objectius que tenen l'element<br> 
 * <mapInfo> compartint el mateix targetObjectiveID, aleshores només un dels<br>
 * objectius pot tindre el writeNormalizedMeasure a true. El mateix succeeix<br>
 * amb l'atribut writeSatisfiedStatus. 
 * <br><br>
 *
 * @author Eduard Céspedes i Borràs /Enginyeria La Salle/ ecespedes@salleurl.edu
 * @version Versió $Revision: 1.6 $ $Date: 2008/04/22 18:18:07 $
 * $Log: MapInfo.java,v $
 * Revision 1.6  2008/04/22 18:18:07  ecespedes
 * Arreglat bugs en el seqüenciament i els objectius secundaris mapejats.
 *
 * Revision 1.5  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral·lel amb el procés de desenvolupament del gestor de BD.
 *
 * Revision 1.4  2007/12/17 15:27:48  ecespedes
 * Fent MapInfo. Bug en els Leaf_Items
 *
 * Revision 1.3  2007/12/05 09:34:19  xgumara
 * Implementat mètode equals i hashCode.
 *
 * Revision 1.2  2007/11/11 22:22:06  ecespedes
 * Creat l'estructura TreeAnnotations, que és per marcar si un item
 * s'ha de tornar a comprovar el seu seqüenciament abans d'enviar
 *  a l'usuari.
 *
 *
 */
public class MapInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -117257960857968122L;
	/**
	 * targetObjectiveID: L'identificador de l'objecte global compartit.
	 * 
	 * String Type: required.
	 */
	public String targetObjectiveID = null;
	/**
	 * readSatisfiedStatus: Es llegirà la informació de l'objecte global 
	 * compartit i se li assignarà a l'objecte local. En aquest cas es 
	 * llegirà l'estat de progrès.
	 *  
	 * boolean Type: Default true.
	 */
	public boolean readSatisfiedStatus = true;
	/**
	 * writeSatisfiedStatus: Es transferirà l'estat de progrès de l'objecte
	 * local a l'objecte global compartit.
	 *  
	 * boolean Type: Default false.
	 */
	public boolean writeSatisfiedStatus = false;
	/**
	 * readNormalizedMeasure: Es llegirà la informació de l'objecte global 
	 * compartit i se li assignarà a l'objecte local.
	 *  
	 * boolean Type: Default true.
	 */
	public boolean readNormalizedMeasure = true;
	/**
	 * writeNormalizedMeasure: Igual però amb el valor de la mesura 
	 * normalitzada.
	 *  
	 * boolean Type: Default false.
	 */
	public boolean writeNormalizedMeasure = false;
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (obj.getClass() != this.getClass())) {
                return false;
            }
            MapInfo mapInfo = (MapInfo) obj;
            return (readNormalizedMeasure == mapInfo.readNormalizedMeasure
                    && readSatisfiedStatus == mapInfo.readSatisfiedStatus
                    && targetObjectiveID.equals(mapInfo.targetObjectiveID)
                    && writeNormalizedMeasure == mapInfo.writeNormalizedMeasure
                    && writeSatisfiedStatus == mapInfo.writeSatisfiedStatus);
        }
        
        @Override
        public int hashCode() {            
            return System.identityHashCode(this);
        }
}

