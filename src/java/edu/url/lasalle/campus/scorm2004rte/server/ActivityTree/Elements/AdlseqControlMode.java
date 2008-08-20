// $Id: AdlseqControlMode.java,v 1.8 2008/01/18 18:07:24 ecespedes Exp $
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
 * $Id: AdlseqControlMode.java,v 1.8 2008/01/18 18:07:24 ecespedes Exp $
 * <b>Títol:</b> AdlseqControlMode <br><br>
 * <b>Descripció:</b> Aquesta classe és l'estructura que guarda els 
 * elements per controlar el mode del seqüenciament. <br><br> 
 * 
 * @see SCORM2004 Sequencing & Navigation 3rt Edition
 * @author Eduard Céspedes i Borràs /Enginyeria LaSalle/ecespedes@salle.url.edu 
 * @version Versió $Revision: 1.8 $ $Date: 2008/01/18 18:07:24 $
 * $Log: AdlseqControlMode.java,v $
 * Revision 1.8  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral·lel amb el procés de desenvolupament del gestor de BD.
 *
 * Revision 1.7  2008/01/08 11:50:21  ecespedes
 * Ultimes modificacions de l'OverallSequencingProcess
 *
 * Revision 1.6  2008/01/07 15:59:22  ecespedes
 * Implementat 'deliveryControls' i el 'hideLMSUI' del Presentation.
 *
 * Revision 1.5  2007/12/09 22:32:16  ecespedes
 * Els objectius dels clusters es tracten i es guarden de manera diferent.
 *
 * Revision 1.4  2007/12/05 09:33:32  xgumara
 * Implementat mètode equals i hashCode.
 *
 * Revision 1.3  2007/11/13 15:59:59  ecespedes
 * Treballant sobre el sistema per "linkar" els Objectives de l'estructura
 * Sequencing de l'arbre amb els Objectives de l'usuari.
 * Millorat TreeAnnotations (step 2 de 3)
 *
 * Revision 1.2  2007/11/12 13:00:08  ecespedes
 * Arreglant elements del seqüenciament.
 * Ja quasi està implementat el TreeAnnotation.
 *
 */
public class AdlseqControlMode implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2009821907139862483L;
	
	/**
	 * choice: Indica que fer una tria del seqüenciament està
	 * permès (o prohibit).
	 * 
	 *  boolean Type. Default True.
	 */
	public boolean choice = true;
	/**
	 * choiceExit: Indica si un fill d'aquesta activitat pot finalitzar.
	 * 
	 *  boolean Type. Default True.
	 */
	public boolean choiceExit = true;
	
	/** 
	 * flow (optional, default false): Si el Sequencing Control Flow (o sigui,
	 * aquesta opció) està a true aleshores l'LMS ha de proporcionar mecanismes
	 * perquè l'usuari pugui "Continuar" o "Retrocedir" (Continue, Previous).
	 * 
	 * boolean Type, Default False.
	 */
	public boolean flow = false;
	/**
	 * forwardOnly: Indica si un fill d'aquesta activitat pot anar
	 * només endavant.
	 * 
	 *  boolean Type. Default False.
	 */
	public boolean forwardOnly = false;
	/**
	 * useCurrentAttemptObjectiveInfo: És per marcar si es pot (o no es pot)
	 * utilitzar la informació sobre el progrès dels objectius.
	 * 
	 * boolean Type. Default True.
	 */
	public boolean useCurrentAttemptObjectiveInfo = true;
	/**
	 * useCurrentAttemptProgressInfo: Indica si es guardarà la informació
	 * sobre els intents.
	 * 
	 *  boolean Type. Default True.
	 */
	public boolean useCurrentAttemptProgressInfo = true;

        /**
         * Sobreescribim el mètode equals perquè ens interessa que compari
         * les variables i ens retorni si són iguals o no.
         * 
         * @return boolean Type.
         */
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (obj.getClass() != this.getClass())) {
                return false;
            }
            AdlseqControlMode acm = (AdlseqControlMode) obj;
            return (choice == acm.choice
                    && choiceExit == acm.choiceExit
                    && flow == acm.flow
                    && forwardOnly == acm.forwardOnly
                    && useCurrentAttemptObjectiveInfo 
                    	== acm.useCurrentAttemptObjectiveInfo
                    && useCurrentAttemptProgressInfo 
                    	== acm.useCurrentAttemptProgressInfo);
        }
        
        /**
         * Sobreescribim el hashcode perquè és imprescindible fer-ho si 
         * sobreescribim el equals, tot i que deixem el hashcode igual que
         * estava.
         * 
         * @return int : És el codi hash.
         */
        public int hashCode() {
            return System.identityHashCode(this);
        }
}

