// $Id: TreeAnnotations.java,v 1.6 2007/12/28 16:35:27 ecespedes Exp $
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
import java.util.LinkedList;


/** <!-- Javadoc -->
 * $Id: TreeAnnotations.java,v 1.6 2007/12/28 16:35:27 ecespedes Exp $ 
 * <b>Títol:</b> TreeAnnotations <br /><br />
 * <b>Descripció:</b> Aquesta classe serà una anotació ràpida sobre un item<br>
 * concret, de manera que fen una comprovació ràpida a aquesta classe<br>
 * no tinguem que anar a mirar el seqüenciament de tot l'arbre<br>
 * (Abstrat_Item). I fins i tot d'aquesta manera no hauriem de tindre'l<br>
 * tot desplegat en memòria si no és absolutament imprescindible. <br> 
 * <br><br>
 *
 * @author Eduard Céspedes i Borràs /Enginyeria La Salle/ ecespedes@salleurl.edu
 * @version Versió $Revision: 1.6 $ $Date: 2007/12/28 16:35:27 $
 * $Log: TreeAnnotations.java,v $
 * Revision 1.6  2007/12/28 16:35:27  ecespedes
 * Implementat l'OverallSequencingProcess.
 *
 * Revision 1.5  2007/12/05 13:36:59  ecespedes
 * Arreglat bug limitConditions i PreConditions
 *
 * Revision 1.4  2007/11/15 15:24:04  ecespedes
 * Implementat el ObjectiveInterface en els Leaf_Item.
 *
 * Revision 1.3  2007/11/13 15:59:59  ecespedes
 * Treballant sobre el sistema per "linkar" els Objectives de l'estructura
 * Sequencing de l'arbre amb els Objectives de l'usuari.
 * Millorat TreeAnnotations (step 2 de 3)
 *
 * Revision 1.2  2007/11/12 13:00:21  ecespedes
 * Arreglant elements del seqüenciament.
 * Ja qüasi està implementat el TreeAnnotation.
 *
 * Revision 1.1  2007/11/11 22:22:06  ecespedes
 * Creat l'estructura TreeAnnotations, que és per marcar si un item
 * s'ha de tornar a comprovar el seu seqüenciament abans d'enviar
 *  a l'usuari.
 *
 *
 */
public class TreeAnnotations implements Serializable {
	/**
	 * Modificar a mida que varii aquest codi. (28-12-2007)
	 */
	private static final long serialVersionUID = 3608320150878307724L;
			
	/**
	 * És el tipus estàtic que ens indicarà de forma ràpida si 
	 * el currentView és visible, invisible o està disabled.
	 * 
	 * @author Eduard Céspedes i Borràs (AKA ecespedes)
	 */
	public static enum viewType {
		/**
		 * Si és visible.
		 */
		visible,
		/**
		 * Si és invisible.
		 */
		notVisible, 
		/**
		 * Si està deshabilitat.
		 */
		disabled };	
	
	/**
	 * Ens indica si l'actual item és visible, invisible o està disabled.
	 */
	public viewType currentView = TreeAnnotations.viewType.visible;
	/**
	 * Identificador de l'ítem.
	 * String Type.
	 */
	public String itemID;
	/**
	 * Títol de l'ítem. 
	 * String Type.
	 */
	public String title;
	/**
	 * Ens indicarà si aquest item està sempre invariant o si, al
	 * contrari, és pot modificar en algun moment de la navegació.
	 */
	//public boolean invariantItem = true;
	/**
	 * Si té MapInfo aleshores tindrem que comprovar sempre el seu
	 * seqüenciament, excepte si no ha sigut accedit i és tracta d'un
	 * LeafItem. 
	 * Serà un LeafItem sempre que el Collection sons sigui de size 0.
	 */
	public boolean hasMapInfo = false;
	/**
	 * Ja ha sigut accedit? Si no ha sigut mai accedit no ens haurem de
	 * preocupar gaire, al contrari, si ha sigut accedit haurem de vigilar
	 *  qui valor té i si depen d'algun dels seus fills.
	 */
	public boolean isAccessed = false;
	/**
	 * La variable 'tracked' ens indicarà si s'ha de guardar l'estat
	 * de l'SCO o pel contrari no em de fer ni cas i no guardar res,
	 * faci el que faci l'usuari.
	 * 
	 */
	//public boolean deliveryTracked = true;
	/**
	 * Si és un cluster aqui indicarem el nom dels seus fills.
	 */	
	public LinkedList < String > sons = new LinkedList < String > ();

}

