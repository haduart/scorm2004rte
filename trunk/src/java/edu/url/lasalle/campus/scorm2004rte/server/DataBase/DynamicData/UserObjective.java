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
 * UserObjective ser� la classe principal de l'usuari respecte un 
 * determinat curs. Contindr� una taula amb tots els models de dades
 * del curs. 
 * 
 * @author Eduard C�spedes i Borr�s /Enginyeria LaSalle/ ecespedes@salle.url.edu
 * 
 * @version 1.0 $Revision: 1.17 $ $Date: 2008/01/30 17:41:15 $
 * $Log: UserObjective.java,v $
 * Revision 1.17  2008/01/30 17:41:15  ecespedes
 * Versi� final: organitzaci�  serialitzada.
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
 * Comen�ant a juntar les peces del proc�s de seq�enciament.
 *
 * Revision 1.10  2007/12/09 22:32:16  ecespedes
 * Els objectius dels clusters es tracten i es guarden de manera diferent.
 *
 * Revision 1.9  2007/11/23 15:04:58  ecespedes
 * El GestorBD ja implementa l'interf�cie DataAccess.
 *
 * Revision 1.8  2007/11/13 15:59:59  ecespedes
 * Treballant sobre el sistema per "linkar" els Objectives de l'estructura
 * Sequencing de l'arbre amb els Objectives de l'usuari.
 * Millorat TreeAnnotations (step 2 de 3)
 *
 * Revision 1.7  2007/11/11 22:22:06  ecespedes
 * Creat l'estructura TreeAnnotations, que �s per marcar si un item
 * s'ha de tornar a comprovar el seu seq�enciament abans d'enviar
 *  a l'usuari.
 *
 * Revision 1.6  2007/11/08 16:33:09  ecespedes
 * Comen�ant a Implementar el OverallSequencingProcess, que ser�
 * les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
 * realment el seq�enciament SCORM1.3
 *
 * Revision 1.5  2007/11/07 13:15:08  ecespedes
 * Creat tot el sistema que controla els DataAccess, per tal
 * que el CourseAdministrator pugui subministrar-lo quan un
 * UserObjective li demani.
 *
 * Revision 1.4  2007/10/31 12:57:01  ecespedes
 * Comen�ant a crear tot el sistema de gesti� dels arbres:
 * + Creant l'interf�fice DataAccess que ens servir� tan
 * 	pel parser com pel SGBD.
 * - Falta que el ParserHandler/GestorBD implementi l'interf�cie.
 *
 * Revision 1.3  2007/10/30 14:07:42  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Comen�ant a crear el sistema de gesti� dels cursos:
 * - CourseAdministrator i CourseManager.
 *
 * Revision 1.2  2007/10/26 22:25:21  ecespedes
 * Ara el parser crea l'estructura b�sica de l'usuari.
 * Arreglat diversos errors, checkstyles i recursivitats.
 *
 *
 */
public class UserObjective implements Serializable {
	
	/**
	 * Serveix per serialitzar l'objecte.
	 * Alerta! A mida que �s vagi modificant aquesta classe
	 * s'haur� d'anar re-generant aquest n�mero!
	 */
	private static final long serialVersionUID = -2389658697680923369L;
	
	/**
	 * Ser� l'identificador del DataAccess que cont� l'organitzaci�
	 * d'aquest UserObjective.
	 * En el cas de parsejar tindrem un ID mentres duri el parsejat
	 * i un altre de diferent quan ho guardem a la BD (o on sigui).
	 */
	public int dataAccessID = 0;
	
	/**
	 * Aquest identificador ser� �nic d	intre del sistema d'execuci�. 
	 * No s'enmagatzemar� a la BD ja que ser� re-assignat cada 
	 * vegada que l'usuari entri al sistema. 
	 * Nota! No ens interessa tant saber qui �s aquest usuari sin� 
	 * m�s b� saber que l'usuari amb identificador 'userIdentifier'
	 * te unes dades en memoria i que actualment s'esta processant 
	 * la seva petici� o actualment est� a l'espera d'un request, etc.
	 */
	public long userIdentifier = 0;
	/**
	 * Aquest camp est� guardat a la BD de MySQL a la taula
	 * studentsClass.global_student_id (VARCHAR(100)).
	 * 
	 * String Type.
	 */
	public String userOSIDIdentifier = null;
	/**
	 * Ser� l'Identificador de l'organitzaci� a la que pertany.
	 * Cal recordar que els identificadors seran Strings que 
	 * hauran estat assigntats pel manifest. 
	 * (String Type, default null)
	 */
	public String organizationName = null;
	/**
	 * Aquest ser� l'identificador real, o sigui, el identificador
	 * �nic que ser� resultat del autoincrement de la BD.
	 * Si l'identificador �s 0 voldr� dir que estem treballant amb el parser.
	 */
	public long organizationID = 0;
			
	/**
	 * Guardarem una taula �nica per cada usuari on anirem quines parts
	 * de l'arbre hem de comprovar per enviar el men� correcte a l'usuari,
	 * segons el seq�enciament d'aquest.
	 * 
	 * String: Ser� l'identificador de l'Item, tan node com cluster.
	 * TreeAnnotations: L'anotaci� per aquell item.
	 */
	public Map < String, TreeAnnotations > treeAnnotations =
		new LinkedHashMap < String, TreeAnnotations > ();
	/**
	 * Guardarem una taula amb tots els items que siguin cluster i a m�s a m�s
	 * si t� m�s d'un objective tamb� �s guardar� aqu�. �s una extensi� del 
	 * treeAnnotations, la idea �s reduir el c�lcul del seq�enciament a trav�s
	 * de l'arbre (Abstract_Item) al m�nim. 
	 */
	public Hashtable < String, ClusterMap > clusterTree =
		new Hashtable < String, ClusterMap > ();
	/**
	 * Aqu� hi guardarem els objectives globals.
	 * ClusterMap Type.
	 */
	public ClusterMap globalObjectiveMap =
		new ClusterMap();
	/**
	 * La tupla ser� <"Identificador num�ric", 
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
	 * El Current_Item_Position ens guardar� el href 
	 * (juntament amb els 'parameters') del currentItem.
	 * M�s que res per si la API ha de demanar-ho diverses vegades. 
	 *  
	 * (String Type, default null)
	 */
	public String currentHref = null;
	/**
	 * Amb aquest boole� indicarem si l'SCO que estem llen�ant est� 
	 * completat o no.
	 */
	public boolean currentIsCompleted = false;
	/**
	 * Volem saber en tot moment quin % del curs est� completat.
	 * Per aix� el que farem ser� guardar en aquesta variable el n�mero
	 * d'items fulla que tenim incomplets, de manera que a mida que 
	 * anem completant els items anirem restant aquest valor, fins 
	 * que tinguem 0, que voldr� dir que �s l'�ltim SCO. 
	 */
	public int remainInComplete = 0;
	/**
	 * En principi tindrem un �nic adlnav que ens indicar� quina resposta ens
	 * ha proporcinat l'usuari (quin request), aix� com si ha sol�licitat 
	 * finalitzar amb un Exit o un Suspend/Suspend All. 
	 */
	public ADLNav adlnav = new ADLNav();
	
}


