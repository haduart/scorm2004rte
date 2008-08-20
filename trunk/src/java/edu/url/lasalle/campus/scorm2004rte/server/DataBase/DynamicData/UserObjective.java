// $Id: UserObjective.java,v 1.17 2008/01/30 17:41:15 ecespedes Exp $
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
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * $Id: UserObjective.java,v 1.17 2008/01/30 17:41:15 ecespedes Exp $
 * 
 * UserObjective serà la classe principal de l'usuari respecte un 
 * determinat curs. Contindrà una taula amb tots els models de dades
 * del curs. 
 * 
 * @author Eduard Céspedes i Borràs /Enginyeria LaSalle/ ecespedes@salle.url.edu
 * 
 * @version 1.0 $Revision: 1.17 $ $Date: 2008/01/30 17:41:15 $
 * $Log: UserObjective.java,v $
 * Revision 1.17  2008/01/30 17:41:15  ecespedes
 * Versió final: organització  serialitzada.
 *
 * Revision 1.16  2008/01/08 11:50:21  ecespedes
 * Ultimes modificacions de l'OverallSequencingProcess
 *
 * Revision 1.15  2008/01/07 15:59:21  ecespedes
 * Implementat 'deliveryControls' i el 'hideLMSUI' del Presentation.
 *
 * Revision 1.14  2007/12/20 20:45:53  ecespedes
 * Implementat l'Objective Map
 *
 * Revision 1.13  2007/12/17 15:27:48  ecespedes
 * Fent MapInfo. Bug en els Leaf_Items
 *
 * Revision 1.12  2007/12/14 12:56:13  ecespedes
 * Solucionat bugs i problemes derivats del nou concepte 'ObjectiveCluster'
 *
 * Revision 1.11  2007/12/10 11:50:27  ecespedes
 * Començant a juntar les peces del procés de seqüenciament.
 *
 * Revision 1.10  2007/12/09 22:32:16  ecespedes
 * Els objectius dels clusters es tracten i es guarden de manera diferent.
 *
 * Revision 1.9  2007/11/23 15:04:58  ecespedes
 * El GestorBD ja implementa l'interfície DataAccess.
 *
 * Revision 1.8  2007/11/13 15:59:59  ecespedes
 * Treballant sobre el sistema per "linkar" els Objectives de l'estructura
 * Sequencing de l'arbre amb els Objectives de l'usuari.
 * Millorat TreeAnnotations (step 2 de 3)
 *
 * Revision 1.7  2007/11/11 22:22:06  ecespedes
 * Creat l'estructura TreeAnnotations, que és per marcar si un item
 * s'ha de tornar a comprovar el seu seqüenciament abans d'enviar
 *  a l'usuari.
 *
 * Revision 1.6  2007/11/08 16:33:09  ecespedes
 * Començant a Implementar el OverallSequencingProcess, que serà
 * les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
 * realment el seqüenciament SCORM1.3
 *
 * Revision 1.5  2007/11/07 13:15:08  ecespedes
 * Creat tot el sistema que controla els DataAccess, per tal
 * que el CourseAdministrator pugui subministrar-lo quan un
 * UserObjective li demani.
 *
 * Revision 1.4  2007/10/31 12:57:01  ecespedes
 * Començant a crear tot el sistema de gestió dels arbres:
 * + Creant l'interfífice DataAccess que ens servirà tan
 * 	pel parser com pel SGBD.
 * - Falta que el ParserHandler/GestorBD implementi l'interfície.
 *
 * Revision 1.3  2007/10/30 14:07:42  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Començant a crear el sistema de gestió dels cursos:
 * - CourseAdministrator i CourseManager.
 *
 * Revision 1.2  2007/10/26 22:25:21  ecespedes
 * Ara el parser crea l'estructura bàsica de l'usuari.
 * Arreglat diversos errors, checkstyles i recursivitats.
 *
 *
 */
public class UserObjective implements Serializable {
	
	/**
	 * Serveix per serialitzar l'objecte.
	 * Alerta! A mida que és vagi modificant aquesta classe
	 * s'haurà d'anar re-generant aquest número!
	 */
	private static final long serialVersionUID = -2389658697680923369L;
	
	/**
	 * Serà l'identificador del DataAccess que conté l'organització
	 * d'aquest UserObjective.
	 * En el cas de parsejar tindrem un ID mentres duri el parsejat
	 * i un altre de diferent quan ho guardem a la BD (o on sigui).
	 */
	public int dataAccessID = 0;
	
	/**
	 * Aquest identificador serà únic d	intre del sistema d'execució. 
	 * No s'enmagatzemarà a la BD ja que serà re-assignat cada 
	 * vegada que l'usuari entri al sistema. 
	 * Nota! No ens interessa tant saber qui és aquest usuari sinó 
	 * més bé saber que l'usuari amb identificador 'userIdentifier'
	 * te unes dades en memoria i que actualment s'esta processant 
	 * la seva petició o actualment està a l'espera d'un request, etc.
	 */
	public long userIdentifier = 0;
	/**
	 * Aquest camp està guardat a la BD de MySQL a la taula
	 * studentsClass.global_student_id (VARCHAR(100)).
	 * 
	 * String Type.
	 */
	public String userOSIDIdentifier = null;
	/**
	 * Serà l'Identificador de l'organització a la que pertany.
	 * Cal recordar que els identificadors seran Strings que 
	 * hauran estat assigntats pel manifest. 
	 * (String Type, default null)
	 */
	public String organizationName = null;
	/**
	 * Aquest serà l'identificador real, o sigui, el identificador
	 * únic que serà resultat del autoincrement de la BD.
	 * Si l'identificador és 0 voldrà dir que estem treballant amb el parser.
	 */
	public long organizationID = 0;
			
	/**
	 * Guardarem una taula única per cada usuari on anirem quines parts
	 * de l'arbre hem de comprovar per enviar el menú correcte a l'usuari,
	 * segons el seqüenciament d'aquest.
	 * 
	 * String: Serà l'identificador de l'Item, tan node com cluster.
	 * TreeAnnotations: L'anotació per aquell item.
	 */
	public Map < String, TreeAnnotations > treeAnnotations =
		new LinkedHashMap < String, TreeAnnotations > ();
	/**
	 * Guardarem una taula amb tots els items que siguin cluster i a més a més
	 * si té més d'un objective també és guardarà aquí. És una extensió del 
	 * treeAnnotations, la idea és reduir el càlcul del seqüenciament a través
	 * de l'arbre (Abstract_Item) al mínim. 
	 */
	public Hashtable < String, ClusterMap > clusterTree =
		new Hashtable < String, ClusterMap > ();
	/**
	 * Aquí hi guardarem els objectives globals.
	 * ClusterMap Type.
	 */
	public ClusterMap globalObjectiveMap =
		new ClusterMap();
	/**
	 * La tupla serà <"Identificador numéric", 
	 * 						estructura dataModel associada>.
	 */
	public Map < String, CMIDataModel > dataModel =
		new Hashtable < String, CMIDataModel > ();
 
	/**
	 * El Current_Item_Position ens ha de "referenciar" amb 
	 * l'identificador del Abstract_Item (Abstract_Identification). 
	 * (String Type, default null)
	 */
	public String currentItem = null;
	/**
	 * El Current_Item_Position ens guardarà el href 
	 * (juntament amb els 'parameters') del currentItem.
	 * Més que res per si la API ha de demanar-ho diverses vegades. 
	 *  
	 * (String Type, default null)
	 */
	public String currentHref = null;
	/**
	 * Amb aquest booleà indicarem si l'SCO que estem llençant està 
	 * completat o no.
	 */
	public boolean currentIsCompleted = false;
	/**
	 * Volem saber en tot moment quin % del curs està completat.
	 * Per això el que farem serà guardar en aquesta variable el número
	 * d'items fulla que tenim incomplets, de manera que a mida que 
	 * anem completant els items anirem restant aquest valor, fins 
	 * que tinguem 0, que voldrà dir que és l'últim SCO. 
	 */
	public int remainInComplete = 0;
	/**
	 * En principi tindrem un únic adlnav que ens indicarà quina resposta ens
	 * ha proporcinat l'usuari (quin request), així com si ha sol·licitat 
	 * finalitzar amb un Exit o un Suspend/Suspend All. 
	 */
	public ADLNav adlnav = new ADLNav();
	
}


