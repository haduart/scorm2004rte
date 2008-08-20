// $Id: Files.java,v 1.4 2008/01/18 18:07:24 ecespedes Exp $
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
 * <b>T�tol:</b> Files <br><br>
 * <b>Descripci�:</b> Aquesta classe �s l'estructura dels fitxers que 
 * formen els recursos (resources) tant dels SCO's com dels ASETS.<br><br>
 * <b>Companyia</b> Departament d'Aprenentage Semipresencial (LaSalleOnLine
 * Enginyeries).<br>
 * @see SCORM2004 Content Aggregation Model 3rt Edition
 * @author Eduard C�spedes i Borr�s / LaSalle / ecespedes@salleurl.edu
 * @version 0.1
 */

public class Files implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5584494500235679239L;

	/**
	 * �s la direcci� del fitxer on est� localitzat. �s el que al final 
	 * ens interessar� perqu� ser� el que veur� l'usuari.
	 */
	public String href = null;
	
	/* 
	 * Aquesta estructura no tindr� sentit si finalment no s'implementa el 
	 * Server_Resources!
	 * Aqu� hi anirien moltes m�s par�metres per identificar el servidor. 
	 * @20070821 1436
	 * TODO Mirar si finalment ens cal o no ens cal tindre aquesta classe.
	 */ 
	
	/**
	 * Ens far� refer�ncia a un servidor concret on estaran enmagatzemades 
	 * les dades.
	 */
	public int server_resources_id = 0;
	
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (obj.getClass() != this.getClass())) {
                return false;
            }
            Files files = (Files) obj;
            return (href.equals(files.href)
                    && server_resources_id == files.server_resources_id);
        }
        
        @Override
        public int hashCode() {            
            return System.identityHashCode(this);
        }
}

