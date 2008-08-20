// $Id: Abstract_Item.java,v 1.27 2008/04/02 14:27:27 ecespedes Exp $
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.  
 */

package edu.url.lasalle.campus.scorm2004rte.server.ActivityTree;


import java.io.Serializable;
import java.util.Stack;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
	AdlnavPresentation;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements
	.ObjectiveInterface;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements
	.Sequencing;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase
	.DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.DataAccess;


/** <!-- Javadoc -->
 * $Id: Abstract_Item.java,v 1.27 2008/04/02 14:27:27 ecespedes Exp $
 * <b>T�tol:</b> Abstract_Item <br><br>
 * <b>Descripci�:</b> Aquesta �s la classe abstracta que ens definir� <br> 
 * tota l'estructura de l'arbre d'activitats. Heredaran aquesta classe<br>
 * les classes Leaf_Item i Root_Item. <br><br>
 * <b>Companyia</b> Departament d'Aprenentage Semipresencial (LaSalleOnLine
 * Enginyeries).<br>
 * $Revision: 1.27 $ $Date: 2008/04/02 14:27:27 $
 * 
 * @author Eduard C�spedes i Borr�s / LaSalle / ecespedes@salleurl.edu
 * @version Versi� $Revision: 1.27 $ $Date: 2008/04/02 14:27:27 $
 * $Log: Abstract_Item.java,v $
 * Revision 1.27  2008/04/02 14:27:27  ecespedes
 * Depurant objectives secundaris
 *
 * Revision 1.26  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral�lel amb el proc�s de desenvolupament del gestor de BD.
 *
 * Revision 1.25  2008/01/08 11:50:21  ecespedes
 * Ultimes modificacions de l'OverallSequencingProcess
 *
 * Revision 1.24  2008/01/07 15:59:22  ecespedes
 * Implementat 'deliveryControls' i el 'hideLMSUI' del Presentation.
 *
 * Revision 1.23  2008/01/03 15:05:32  ecespedes
 * Arreglats elements del CMIDataModel i comen�ant a crear un nou test.
 *
 * Revision 1.22  2007/12/28 16:35:27  ecespedes
 * Implementat l'OverallSequencingProcess.
 *
 * Revision 1.21  2007/12/27 15:02:24  ecespedes
 * Reunificant totes les crides del seq�enciament.
 *
 * Revision 1.20  2007/12/21 17:12:44  ecespedes
 * Implementant CourseManager.OverallSequencingProcess
 *
 * Revision 1.19  2007/12/14 12:56:13  ecespedes
 * Solucionat bugs i problemes derivats del nou concepte 'ObjectiveCluster'
 *
 * Revision 1.18  2007/12/11 15:29:27  ecespedes
 * Arreglant bugs i optimitzant solucions.
 *
 * Revision 1.17  2007/12/10 22:03:32  ecespedes
 * Implementant les funcions per buscar un item concret i retornar-lo
 * al CourseManager perqu� el tracti.
 *
 * Revision 1.16  2007/11/29 15:54:35  ecespedes
 * Implementant les SequencingRules (part 1)
 *
 * Revision 1.15  2007/11/27 15:34:25  ecespedes
 * Arreglats bugs relacionats amb el Rollup. Creat nou joc de testos.
 *
 * Revision 1.14  2007/11/19 07:34:15  ecespedes
 * Millores en els RollupRules.
 *
 * Revision 1.13  2007/11/15 15:24:04  ecespedes
 * Implementat el ObjectiveInterface en els Leaf_Item.
 *
 * Revision 1.12  2007/11/14 12:33:36  ecespedes
 * Implementada l'interface: ObjectiveInterface, de manera que ara totes
 * les classes vinculades al seq�enciament l'implementen.
 *
 * Revision 1.11  2007/11/11 22:22:06  ecespedes
 * Creat l'estructura TreeAnnotations, que �s per marcar si un item
 * s'ha de tornar a comprovar el seu seq�enciament abans d'enviar
 *  a l'usuari.
 *
 * Revision 1.10  2007/11/09 12:58:58  ecespedes
 * Creant m�todes perqu� aix� poguem rec�rrer m�s r�pidament
 * l'estructura de l'arbre.
 *
 * Revision 1.9  2007/11/08 16:33:09  ecespedes
 * Comen�ant a Implementar el OverallSequencingProcess, que ser�
 * les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
 * realment el seq�enciament SCORM1.3
 *
 * Revision 1.8  2007/10/31 12:57:01  ecespedes
 * Comen�ant a crear tot el sistema de gesti� dels arbres:
 * + Creant l'interf�fice DataAccess que ens servir� tan
 * 	pel parser com pel SGBD.
 * - Falta que el ParserHandler/GestorBD implementi l'interf�cie.
 *
 * Revision 1.7  2007/10/30 14:07:42  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Comen�ant a crear el sistema de gesti� dels cursos:
 * - CourseAdministrator i CourseManager.
 *
 */
public abstract class Abstract_Item implements ObjectiveInterface, Serializable {
	/**
	 * Contindr� l'objecte que ens proporcionar� les dades quan 
	 * haguem de fer un 'load'.
	 */
	protected DataAccess dataAccess = null;
	/**
	 * Ser� l'identificador que l'autoIncrement de la BD utilitza 
	 * per determinar la organitzaci� de forma �nica.
	 */
	protected int organizationID = 0;
	/**
	 * Ser� l'identificador que l'autoIncrement de la BD utilitza 
	 * per determinar aquest item de forma �nica.
	 */
	public int itemID = 0;
	
	/**
	 * Per tindre una relaci� amb el creador/pare. 
	 * Per si ens ha de passar algun par�metre. 
	 * Com que �s d'�s freq�ent millor accedir directament a 
	 * la variable que crear m�todes d'acc�s. 
	 */
	public Abstract_Item father = null;
	/**
	 * Ens indicar� si �s o no �s una organitzaci�.
	 * �s m�s fiable que no comparar amb father == null.
	 */
	public boolean isOrganization = false;
	/**
	 * Per tindre coneixement de si �s fulla o no.
	 */
	private boolean isLeaf = true;
	/**
	 * Ens indicar� si aquest item ja est� carregat o no. 
	 * Si no ho est�, quan intentem fer una petici� (get, set, proces, update..)
	 * autom�ticament es cridar� al gestor de BD, �s carregaran totes les dades
	 * i lodaded ser� true.
	 */
	private boolean loaded = false;
			
	/**
	 * Si hasSequencingCollection() == true aleshores sequencingCollection 
	 * contindr� la refer�ncia. 
	 */					
	private SequencingCollection sequencingCollection = null;
	/**
	 * El resourcesCollection ser� �nic per una organitzaci� <br />
	 * per tant ens haurem d'anar "passant" la refer�ncia de pares <br />
	 * a fills.<br />
	 * @author Eduard C�spedes i Borr�s
	 */
	private ResourcesCollection resourcesCollection = null;
	
	/**
	 * Com a identificador mantindrem l'String que hem agafat del manifest.
	 */							
	private String identifier = null;
	/**
	 * Si t� parameters ho enmagatzemarem en aquesta variable. 
	 * 'parameters' �s un atribut opcional de l'Item. 
	 * (String Type, default = null)
	 */
	private String parameters = "";
	/**
	 * Aquest atribut ens indicar� si �s visible o n�. 
	 * 'isvisible' �s un atribut opcional de l'Item.
	 * (boolean Type, default true)
	 */
	private boolean isvisible = true;
	
//	private Comment[] comment = null;
	
	/**
	 * Si hasSequencing == false farem un seq�enciament linial normal. 
	 * Els motius poden ser que el deliveryControls.Tracket = false o que
	 * no hi hagi cap etiqueta "sequencing" en tot el manifest. 
	 */					
	private boolean hasSequencing = false;
	/**
	 * Variable que contindr� el seq�enciament de l'Item (en cas de
	 * que en tingui).
	 * (Sequencing Type, default null)
	 */				
	private Sequencing sequencing = null;	
	/**
	 * En aquesta classe enmagatzemem les dades sobre la presentaci�
	 * de l'usuari. B�sicament sobre quins botons s'han de mostrar.
	 */
	public AdlnavPresentation adlnavPresentation = new AdlnavPresentation();
	
	/**
	 * �s el title de l'Item (no confondre amb l'identificador
	 * que tamb� �s un String!). 
	 * (String Type, default null)
	 */
	private String title = null;	
	/**
	 * M�tode que retornar� una inst�ncia de si mateix.
	 * En principi nom�s l'ha de cridar
	 * @return Abstract_Item : this
	 */
	public final Abstract_Item getInstance() {
		return this;
	}	
	/**
	 * M�tode molt important, ja que ens diu si �s o no �s
	 * un item full (SCO/Asset o un Cluster).
	 *  
	 * @return boolean : true == Leaf; false == Cluster.
	 */
	public final boolean getIsLeaf() {
		return isLeaf;
	}
	/**
	 * Assignem un nou valor a la variable isLeaf (indicant
	 * si �s o no �s un node fulla). Te�ricament aquest m�tode
	 * nom�s el pot invocar el parser o el SGBD.
	 * 
	 * @param newIsLeaf : boolean type; true == Leaf; false == Cluster.
	 */
	public final void setIsLeaf(final boolean newIsLeaf) {
		isLeaf = newIsLeaf;
	}
	/**
	 * Asigna l'identificador a l'Item. Camp obligatori!
	 * Te�ricament aquest m�tode nom�s el pot invocar el
	 * parser o el SGBD.
	 * 
	 * @param nouIdentifier : String Type, required.
	 */
	public final void setIdentifier(final String nouIdentifier) {
		identifier = nouIdentifier;
	}
	/**
	 * Ens retorna l'identificador de l'Item. 
	 * 
	 * @return String Type, required.
	 */
	public final String getIdentifier() {
		return identifier;
	}
	/**
	 * Assigna els par�metres a l'Item. Camp opcional.
	 * Te�ricament aquest m�tode nom�s el pot invocar el
	 * parser o el SGBD.
	 * 
	 * @param nouParameters : String Type, optional. 
	 */
	public final void setParameters(final String nouParameters) {
		parameters = nouParameters;
	}
	/**
	 * Ens retorna els par�metres. Null si no n'hi ha. 
	 * 
	 * @return String Type, default null.
	 */
	public final String getParameters() {
		return parameters;
	}
	/**
	 * Ens assigna la variable isVisible. �s obligatoria i per defecte
	 * ser� true. De ser false l'item no seria visible. 
	 * 
	 * @param nouIsvisible : boolean Type, default true.
	 */
	public final void setIsVisible(final boolean nouIsvisible) {
		isvisible = nouIsvisible;
	}
	/**
	 * Ens retorna el camp isVisible. Obligatori. L'haurem llegit
	 * del manifest i el processar� l'LMS per determinar si mostra 
	 * o no mostra un contingut. 
	 * 
	 * @return boolean Type, default true.
	 */
	public final boolean getIsVisible() { 
		return isvisible;
	}
	/**
	 * Assigna el camp Title. Par�metre obligatori.
	 * Te�ricament aquest m�tode nom�s el pot invocar
	 * el parser o el SGBD.
	 * 
	 * @param nouTitle : String Type.
	 */		
	public final void setTitle(final String nouTitle) {
		title = nouTitle;
	}
	
	/**
	 * Retorna el title d'un Item. Te�ricament no pot ser null
	 * perqu� �s un camp obligatori.
	 * 
	 * @return String Type. 
	 */
	public final String getTitle() {
		return title;
	}
	
	
	/**
	 * �s una optimitzaci�. Assignarem un nou valor a 'loaded'. Assignarem
	 * a true quan tinguem totes les dades de l'item en mem�ria i assignarem
	 * a false quan lliberem mem�ria. 
	 * @param newLoadedValue : (boolean Type) Nou valor de 'loaded'
	 */
	public final void setLoaded(final boolean newLoadedValue) {
		loaded = newLoadedValue;
	}
	/**
	 * �s una optimitzaci�. Ens indicar� si tot l'item ja est� carregat
	 * en mem�ria, de no ser aix� hauriem de carregar totes les dades del
	 * SGBD.
	 * @return boolean
	 */
	public final boolean getLoaded() {
		return loaded;
	}	
	/**
	 * Ens indica si t� o no t� sequencingCollection aquest "curs".
	 * @return boolean
	 */
	public final boolean hasSequencingCollection() {
		if (sequencingCollection == null) {
			if (father == null) {
				return false;
			} else {
				return father.hasSequencingCollection();
			}
		} else {
			return true;
		}
	}
	/**
	 * Assigna el sequencingCollection a aquest item. En principi nom�s
	 * hauria de ser utilitzat pel parser o pel SGBD.
	 * @param nouSequencingCollection : Assignem el nou SequencingCollection.
	 * (SequencingCollection Type)
	 */
	public final void setSequencingCollection(
			final SequencingCollection nouSequencingCollection) {
		sequencingCollection = nouSequencingCollection;				
	}
	/**
	 * Ens retorna el sesourcesCollection. Fa una b�squeda recursiva 
	 * pujant per l'arbre. 
	 * @return sequencingCollection (SequencingCollection Type)
	 */
	public final SequencingCollection getSequencingCollection() {
		if (sequencingCollection == null) {
			if (father == null) { 
				return null;
			} else {
				sequencingCollection = father.getSequencingCollection();
				return sequencingCollection;
			}
		} else {
			return sequencingCollection;
		}
	}
	/**
	 * Assigna la Col�lecci� de Recursos. �s un camp obligatori ja que 
	 * sense aquest un item fulla no pot accedir als seus recursos, i 
	 * aquesta col�lecci� li ha de passar el seu pare, per tant un cluster
	 * tamb� ha de fer refer�ncia a aquest 'resourcesCollection'.
	 * 
	 * @param nouResourcesCollection : ResourcesCollection Type,
	 * required. 
	 */
	public final void setResourcesCollection(
			final ResourcesCollection nouResourcesCollection) {
		
		resourcesCollection = nouResourcesCollection;				
	}
	/**
	 * Ens retorna el resourcesCollection. Fa una b�squeda recursiva 
	 * pujant per l'arbre. 
	 * @return resourcesCollection (ResourceCollection Type)
	 */
	public final ResourcesCollection getResourcesCollection() {
		if (resourcesCollection == null) {
			if (father == null) {
				return null;
			} else {
				resourcesCollection = father.getResourcesCollection();
				return resourcesCollection;
			}
		} else {
			return resourcesCollection;
		}
	}
	/**
	 * Ens insdica si t� seq�enciament o no. 
	 * @return hasSequencing (boolean Type)
	 */
	public final boolean hasSequencing() {
		return hasSequencing;
	}
	/**
	 * Ens inicialitza el seq�enciament d'aquest Item.
	 * 
	 * @param nouSequencing : Sequencing Type
	 */
	public final void setSequencing(final Sequencing nouSequencing) {
		//activateSequencing();
		hasSequencing = true;
		if (sequencing == null) {
			sequencing = nouSequencing;			
		}		
	}
	/**
	 * Retorna el seq�enciament de l'item actual.
	 * @return sequencing (Sequencing Type)
	 */
	public final Sequencing getSequencing() {
		if (hasSequencing) {
			if (loaded) {
				return sequencing;
			} else {
				/*
				 * TODO: Carregar les dades del seq�enciament!
				 * i marcar loaded com a true 
				 */				
				return sequencing;
			}
		} else {
			return null;
		}
	}
	/**
	 * Retornem el par�metre de l'ObjectiveMeasureWeight del
	 * Rollup, d'aquesta manera el "pare" de l'item actual podr�
	 * saber si contribuim a la mesura. 
	 * 
	 * @return double : 0 si no contribuim, != 0 si contribuim.
	 */
	public final double getRollupObjectiveMeasureWeight() {
		if (getSequencing() == null) {
			return 0;
		} else {
			return getSequencing().rollupObjectiveMeasureWeight;
		}
		
	}
	/**
	 * Retornem el par�metre de l'ObjectiveSatisfied del
	 * Rollup, d'aquesta manera el "pare" de l'item actual podr�
	 * saber si contribuim a la mesura. 
	 * 
	 * @return double : 0 si no contribuim, != 0 si contribuim.
	 */
	public final boolean getRollupObjectiveSatisfied() {
		if (getSequencing() == null) {
			return false;
		} else {
			return getSequencing().rollupObjectiveSatisfied;
		}
		
	}
	/**
	 * Retornem el par�metre del ProgressCompletion del
	 * Rollup, d'aquesta manera el "pare" de l'item actual podr�
	 * saber si contribuim a la mesura. 
	 * 
	 * @return double : 0 si no contribuim, != 0 si contribuim.
	 */
	public final boolean getRollupProgressCompletion() {
		if (getSequencing() == null) {
			return false;
		} else {
			return getSequencing().rollupProgressCompletion;
		}
		
	}
	/**
	 * Funci� recursiva. Busca el seu pare fins trobar alg� que tingui
	 * seq�enciament. 
	 */
	public abstract void activateSequencing();
	/**
	 * Funci� que utilitzarem per transmetre de forma recursiva el
	 * DataAccess utilitzat cap als seus fills. 
	 * 
	 * @param newDataAccess : DataAccess Type.
	 * @param newOrganizationID : L'identificador de l'organitzaci�.
	 */
	public abstract void transmitDataAccess(
			DataAccess newDataAccess, int newOrganizationID);

	/**
	 * TODO [ WARNING-Tractament del SEQ & NAV ! ]
	 */
	
	/** 
	 * Aquest m�tode ser� el m�s important ja que ser� el que
	 * tractar� el ADLNav, analitzar� el seq�enciament, actualitzar�
	 * els valors del UserObjective i retornar� un ADLNav amb el 
	 * currentItem correcte.
	 * 
	 * @param learner : UserObjective Type
	 * @return UserObjective : 
	 */
	public abstract UserObjective overallSequencingProcess(
			final UserObjective learner);
	/**
	 * Amb aquest m�tode el que farem ser� implementar un m�tode iteratiu
	 * que ens permetr� mourens r�pidament per l'arbre.
	 * 
	 * @param learner : UserObjective de l'estudiant actual.
	 * @param currentItemID : L'item del que procedim.
	 * @return Leaf_Item : El proper asset o sco.
	 */
	public abstract Leaf_Item nextItem(
			UserObjective learner, String currentItemID);
	/**
	 * Amb aquest m�tode el que farem ser� implementar un m�tode iteratiu
	 * que ens permetr� mourens r�pidament per l'arbre.
	 * 
	 * @param learner : UserObjective de l'estudiant actual.
	 * @param currentItemID : L'item del que procedim.
	 * @return Leaf_Item : L'anterior asset o sco.
	 */
	public abstract Leaf_Item previousItem(
			UserObjective learner, String currentItemID);
	/**
	 * Amb aquesta funci� retornarem el item que estem buscant, i gr�cies
	 * a la pila que li pasem estem limitant la b�squeda. 
	 * 
	 * @param routeMap : Stack < String > routeMap
	 * @param learner : UserObjective Type
	 * @return Leaf_Item : �s l'item que buscavem o null.
	 */
	public abstract Abstract_Item searchMyItem(
			final UserObjective learner,
			final Stack < String > routeMap,
			final boolean postCondition);
	/**
	 * M�tode per saber r�pidament si un item te seg�ent o no.
	 * Cal recordar que si un item NO t� seg�ent �s que ha arribat al 
	 * final, per tant, a menys que el seq�enciament ens faci saltar a 
	 * un altre item, en principi haurem de mostrar un missatge indicant que
	 * s'ha arribat al final del curs i indicar si s'ha completat correctament
	 * i amb quina puntuaci�.
	 * 
	 * @param learner : UserObjective de l'estudiant actual.
	 * @param currentItemID : L'item del que procedim.
	 * @return boolean Type: True si hi ha un altre item, fal� si no n'hi ha.
	 */
	public boolean hasNextItem(
			final UserObjective learner, final String currentItemID) {
		return (nextItem(learner, currentItemID) != null);
	}
	/**
	 * M�tode per saber r�pidament si un item te seg�ent o no.
	 * Cal recordar que si un item NO t� un item previ �s que ha
	 * arribat al principi, per tant se l'hi hauria de deshabilitar
	 * el bot� "Previous" o sin� mostrar-li un missatge inicial
	 * del curs.
	 * 
	 * @param learner : UserObjective de l'estudiant actual.
	 * @param currentItemID : L'item del que procedim.
	 * @return boolean Type: True si hi ha un altre item, fal� si no n'hi ha.
	 */
	protected boolean hasPreviousItem(
			final UserObjective learner, final String currentItemID) {
		return (previousItem(learner, currentItemID) != null);
	}	

}

