// $Id: AdlnavPresentation.java,v 1.6 2008/01/18 18:07:24 ecespedes Exp $
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
 * $Id: AdlnavPresentation.java,v 1.6 2008/01/18 18:07:24 ecespedes Exp $
 * <b>T�tol:</b> AdlnavPresentation <br><br>
 * <b>Descripci�:</b> Aquesta classe �s l'estructura que guarda els elements<br>
 * de presentaci� de la navegaci�.<br><br>
 * <b>Code:</b><br>
 * <i>
 * <adlnav:presentation><br>
 * 		<adlnav:navigationInterface><br>
 * 			<adlnav:hideLMSUI>continue</adlnav:hideLMSUI><br>
 *          <adlnav:hideLMSUI>previous</adlnav:hideLMSUI><br>
 *      </adlnav:navigationInterface><br>
 * </adlnav:presentation><br>
 * </i> 
 * 
 * @see SCORM2004 Sequencing & Navigation 3rt Edition
 * @author Eduard C�spedes i Borr�s / LaSalle / ecespedes@salleurl.edu
 * @version $Id
 * $Log: AdlnavPresentation.java,v $
 * Revision 1.6  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral�lel amb el proc�s de desenvolupament del gestor de BD.
 *
 * Revision 1.5  2008/01/08 11:50:21  ecespedes
 * Ultimes modificacions de l'OverallSequencingProcess
 *
 * Revision 1.4  2008/01/07 15:59:22  ecespedes
 * Implementat 'deliveryControls' i el 'hideLMSUI' del Presentation.
 *
 * Revision 1.3  2007/12/05 09:33:04  xgumara
 * Implementat m�tode equals i hashCode.
 *
 * Revision 1.2  2007/11/11 22:22:06  ecespedes
 * Creat l'estructura TreeAnnotations, que �s per marcar si un item
 * s'ha de tornar a comprovar el seu seq�enciament abans d'enviar
 *  a l'usuari.
 *
 */

public class AdlnavPresentation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6427784322563072154L;
	/**
	 * Amaga el but� de previ.
	 */
	public boolean hidePrevious = false;
	/**
	 * Amaga el but� de Continuar.
	 */
	public boolean hideContinue = false;
	/**
	 * Amaga el but� de Exit.
	 */
	public boolean hideExit = false;
	/**
	 * Amaga el but� de Abandonar.
	 */
	public boolean hideAbandon 	= false;
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (obj.getClass() != this.getClass())) {
                return false;
            }
            AdlnavPresentation ap = (AdlnavPresentation) obj;
            return (hidePrevious == ap.hidePrevious
                    && hideContinue == ap.hideContinue
                    && hideExit == ap.hideExit
                    && hideAbandon == ap.hideAbandon);
        }
        
        @Override
        public int hashCode() {
            return System.identityHashCode(this);
        }
}

