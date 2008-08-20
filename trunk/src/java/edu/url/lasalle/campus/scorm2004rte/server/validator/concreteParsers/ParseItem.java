// $Id: ParseItem.java,v 1.21 2008/01/08 11:50:21 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.validator.concreteParsers;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Node;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Abstract_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Root_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Leaf_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Sequencing;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.SequencingCondition;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.SequencingRules;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Types.OperatorType;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.
	CMIDataModel;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.CMIObjectives;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.ClusterMap;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.TreeAnnotations;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMTreeUtility;

/** <!-- Javadoc -->
 * $Id: ParseItem.java,v 1.21 2008/01/08 11:50:21 ecespedes Exp $ 
 * <b>Títol:</b> ParseItem <br><br>
 * <b>Descripció:</b> Aquesta classe parseja els Items i crea <br />
 * l'estructura Abstract_Item (Leaf_Item o Root_Item), entrant<br />
 * tots els elements.  <br />
 * <br />
 * <b>Companyia</b> Departament d'Aprenentage Semipresencial (LaSalleOnLine
 * Enginyeries).<br>
 * @see SCORM2004 Content Agregation Model 3rt Edition
 * @author Eduard Céspedes i Borràs / LaSalle / ecespedes@salleurl.edu
 * @version Versió $Revision: 1.21 $ $Date: 2008/01/08 11:50:21 $
 * $Log: ParseItem.java,v $
 * Revision 1.21  2008/01/08 11:50:21  ecespedes
 * Ultimes modificacions de l'OverallSequencingProcess
 *
 * Revision 1.20  2008/01/07 15:59:21  ecespedes
 * Implementat 'deliveryControls' i el 'hideLMSUI' del Presentation.
 *
 * Revision 1.19  2008/01/03 15:05:32  ecespedes
 * Arreglats elements del CMIDataModel i començant a crear un nou test.
 *
 * Revision 1.18  2007/12/17 15:27:48  ecespedes
 * Fent MapInfo. Bug en els Leaf_Items
 *
 * Revision 1.17  2007/12/11 15:29:27  ecespedes
 * Arreglant bugs i optimitzant solucions.
 *
 * Revision 1.16  2007/12/09 22:32:16  ecespedes
 * Els objectius dels clusters es tracten i es guarden de manera diferent.
 *
 * Revision 1.15  2007/12/05 13:36:59  ecespedes
 * Arreglat bug limitConditions i PreConditions
 *
 * Revision 1.14  2007/11/29 15:54:35  ecespedes
 * Implementant les SequencingRules (part 1)
 *
 * Revision 1.13  2007/11/27 15:34:25  ecespedes
 * Arreglats bugs relacionats amb el Rollup. Creat nou joc de testos.
 *
 * Revision 1.12  2007/11/19 07:34:15  ecespedes
 * Millores en els RollupRules.
 *
 * Revision 1.11  2007/11/15 15:24:04  ecespedes
 * Implementat el ObjectiveInterface en els Leaf_Item.
 *
 * Revision 1.10  2007/11/13 15:59:59  ecespedes
 * Treballant sobre el sistema per "linkar" els Objectives de l'estructura
 * Sequencing de l'arbre amb els Objectives de l'usuari.
 * Millorat TreeAnnotations (step 2 de 3)
 *
 * Revision 1.9  2007/11/12 13:00:21  ecespedes
 * Arreglant elements del seqüenciament.
 * Ja qüasi està implementat el TreeAnnotation.
 *
 * Revision 1.8  2007/11/08 16:33:09  ecespedes
 * Començant a Implementar el OverallSequencingProcess, que serà
 * les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
 * realment el seqüenciament SCORM1.3
 *
 * Revision 1.7  2007/10/30 14:07:42  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Començant a crear el sistema de gestió dels cursos:
 * - CourseAdministrator i CourseManager.
 *
 */

public class ParseItem {
	/**
	 * És una taula que contindrà un string (que farà de key)
	 * amb l'identificador de l'Item i després una estructura
	 * CMIDataModel que és precisament l'estructura on guardarem
	 * totes les dades que genera/necessita un usuari per tractar
	 * un SCO.
	 */
	private Hashtable < String, CMIDataModel > nouHashtable =
		new Hashtable < String, CMIDataModel >();
	/**
	 * És l'estructura que serà el UserObjective.clsterTree< >().
	 */
	public Hashtable < String, ClusterMap > nouClusterTree =
		new Hashtable < String, ClusterMap > ();
	/**
	 * Aquí hi guardarem els objectes globals. 
	 */
	public ClusterMap globalMap = new ClusterMap();
	/**
	 * Aquí guardarem l'anotació per l'item actual, i a la vegada anirem 
	 * afegint les anotacions dels seus fills (en cas de ser un cluster).
	 * 
	 * String: Serà la clau d'aquest hash i serà l'identificador de l'Item.
	 */
	private LinkedHashMap < String , TreeAnnotations > treeAnnotations =
		new LinkedHashMap < String, TreeAnnotations > ();
		//new Hashtable < String, TreeAnnotations > ();
	
	/**
	 * En aquesta variable anirem fent les anotacions de l'Item
	 * actual.
	 */
	private TreeAnnotations currentTreeAnnotations = new TreeAnnotations();
	
	/**
	 * Aquesta variable és accessible des de getItem().
	 */
	private Abstract_Item currentItem;
	/**
	 * Si és una fulla omplirem primer l'estructura
	 * Leaf_Item amb els seus mètodes particulars.
	 */
	private Leaf_Item leafItem;
	/**
	 * Si és una fulla omplirem primer l'estructura
	 * Root_Item amb els seus mètodes particulars.
	 */
	private Root_Item rootItem;
	/**
	 * Booleà per saber si és o no és un node 'fulla'.
	 */
	private boolean isLeaf = false;
	/**
	 * Booleà per saber si té o no té seqüenciament aquest
	 * Item o qualsevol dels seus fills. 
	 */
	private boolean hasSequencing = false;
	/**
	 * Ens servirà per guardar-ho en el dataModel.
	 */
	private String timeLimitAction = null;
	/**
	 * Ens servirà per guardar el DataFromLMS 
	 * al dataModel (que s'anomenarà launchData).
	 */
	private String launchData = null;
	/**
	 * Ens servirà per guardar-ho en el dataModel.
	 */
	private String completionThreshold = null;
	/**
	 * Parsejarem un Item, tan si és un node fulla com si és un 
	 * cluster, retornant un Abstract_Item que podrà ser qualsevol
	 * dels dos. 
	 * 
	 * @param itemNode : Node actual que estem tractant.
	 * @param father : Item pare, per si necessitem buscar informació.
	 */
	public ParseItem(final Node itemNode, final Abstract_Item father) {
		
		/**
		 * Busquem l'atribut "identifierref" ja que aquest ens determinarà si és
		 * un node fulla o no.
		 */
		String identifierref = 
			DOMTreeUtility.getAttributeValue(itemNode, "identifierref");
		
		if (identifierref.length() == 0) {
			if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
				System.out.print("\n\t[Item cluster father:"
						+ father.getIdentifier() + "]");
			}
			rootItem = new Root_Item();
			rootItem.father = father;
			rootItem.setIsLeaf(false);
			rootItem.setLoaded(true);
			isLeaf = false;
		} else {
			if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
				System.out.print("\n\t[Item fulla father:"
						+ father.getIdentifier() + "]");
			}
			isLeaf = true;
			leafItem = new Leaf_Item();
			leafItem.father = father;
			leafItem.setIsLeaf(true);
			leafItem.setLoaded(false);
			leafItem.setIdentifierref(identifierref);
			
			if (leafItem.getResourcesCollection().
					searchResource(identifierref) == null) {
				
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_ITEMS) {
					System.out.println("[ERROR]No s'ha trobat '"
							+ identifierref + "' en el <Resources>!");
				}
			} else {
				if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
					System.out.println("identifierref='"
							+ identifierref + "' trobat amb" 
							+ " èxit dintre del ResourcesCollection.");
				}				
			}
		}		
		String identifier = 
			DOMTreeUtility.getAttributeValue(itemNode, "identifier");
		
		if (identifier.length() == 0) {
			if (Constants.DEBUG_ERRORS || Constants.DEBUG_ITEMS) {
				System.out.println("[ERROR]No s'ha trobat"
						+ " l'identificador!");
			}
		} else { 
			if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
				System.out.print("\tidentifier:"
						+ identifier + "\n\t");
			}
		}
		if (isLeaf) {
			leafItem.setIdentifier(identifier);
		} else {
			rootItem.setIdentifier(identifier);
		}
		
		String parameters = DOMTreeUtility.
				getAttributeValue(itemNode, "parameters");
		
		if (parameters.length() == 0) {
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_ITEMS) {
				System.out.println("Atribut 'parameters' no trobat");
			}						
		} else {
			if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
				System.out.print("\tparameters:'" + parameters + "'");
			}
		}
		if (isLeaf) {
			leafItem.setParameters(parameters);
		} else {
			rootItem.setParameters(parameters);
		}
		
		String isvisible =
			DOMTreeUtility.getAttributeValue(itemNode, "isvisible");
		
		if (isvisible.length() == 0) {
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_ITEMS) {
				System.out.println("Atribut 'parameters' no trobat");
			}					
		} else {
			if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
				System.out.print("\tisvisible:'" + isvisible + "'");
			}
		}
		
		boolean bIsVisible = isvisible.equals(Constants.FALSE) ? false : true;
		if (!bIsVisible) {
			currentTreeAnnotations.currentView =
				TreeAnnotations.viewType.notVisible;
		}
		if (isLeaf) {			
			leafItem.setIsVisible(bIsVisible);
		} else {
			rootItem.setIsVisible(bIsVisible);
		}
		
		//Agafem el title
		Node titleNode = DOMTreeUtility.getNode(itemNode, Constants.TITLE);
		if (titleNode == null) {
			if (Constants.DEBUG_ERRORS || Constants.DEBUG_ITEMS) {
				System.out.println("[ERROR]Atribut Title no trobat.");
			}
		} else {						
			String realTitle = DOMTreeUtility.getNodeValue(titleNode);
			if (realTitle.length() == 0) {				
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_ITEMS) {
					System.out.println("[ERROR]Atribut Title no trobat.");
				}
			} else {
				if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
					System.out.print("\tTitle:'" + realTitle + "'");
				}
			}
			
			currentTreeAnnotations.title = realTitle;
			
			if (isLeaf) {				
				leafItem.setTitle(realTitle);
			} else {
				rootItem.setTitle(realTitle);
			}			
		}
		
		//		Agafem el timeLimitAction
		Node timeLimitActionNode =
			DOMTreeUtility.getNode(itemNode, "timeLimitAction");
		
		if (timeLimitActionNode == null) {
			if (Constants.DEBUG_WARNINGS_LOW 
					&& Constants.DEBUG_ITEMS) {
				System.out.println("Atribut timeLimitAction no trobat.");
			}
		} else {
			String realtimeLimitAction = DOMTreeUtility.getNodeValue(titleNode);
			if (realtimeLimitAction.length() == 0) {				
				if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
					System.out.println("Atribut timeLimitAction no trobat.");
				}
			} else {
				if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
					System.out.print("\ttimeLimitAction:'" 
							+ realtimeLimitAction + "'");
				}
			}
			timeLimitAction = realtimeLimitAction;
					
		}
		
		//	Agafem el dataFromLMS
		Node dataFromLMS = DOMTreeUtility.getNode(itemNode, "dataFromLMS");
		if (dataFromLMS == null) {
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_ITEMS) {
				System.out.println("Atribut dataFromLMS no trobat.");	
			}
		} else {
			String realdataFromLMS = DOMTreeUtility.getNodeValue(dataFromLMS);
			if (realdataFromLMS.length() == 0) {				
				if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_ITEMS) {
					System.out.println("Atribut dataFromLMS no trobat.");
				}
			} else {
				if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
					System.out.print("\tdataFromLMS:'"
							+ realdataFromLMS + "'");
				}
			}
			launchData = realdataFromLMS;
		}
		
		//		Agafem el completionThreshold
		Node completionThresholdNode =
			DOMTreeUtility.getNode(itemNode, "completionThreshold");
		
		if (completionThresholdNode == null) {
			if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_ITEMS) {
				System.out.println("Atribut completionThreshold no trobat.");
			}
		} else {
			String realcompletionThreshold =
				DOMTreeUtility.getNodeValue(completionThresholdNode);
			
			if (realcompletionThreshold.length() == 0) {				
				if (Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_ITEMS) {
					System.out.println("Atribut completionThreshold"
							+ " no trobat.");
				}
			} else {
				if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
					System.out.print("\tcompletionThreshold:'" 
							+ realcompletionThreshold + "'");
				}
			}
			completionThreshold = realcompletionThreshold;
			/*
			if (isLeaf) {				
				leafItem.setCompletionThreshold(realcompletionThreshold);
			} else {
				rootItem.setCompletionThreshold(realcompletionThreshold);
			}
			*/
		}
		
		/**
		 * Agafem el adlnavPresentation
		 * <adlnav:presentation>
		 * 		<adlnav:navigationInterface>
		 * 			<adlnav:hideLMSUI>continue</adlnav:hideLMSUI>
		 * 			<adlnav:hideLMSUI>previous</adlnav:hideLMSUI>
		 * 		</adlnav:navigationInterface>
		 * </adlnav:presentation>
		 */
		Node presentationNode = 
			DOMTreeUtility.getNode(itemNode, "presentation");
		if (presentationNode != null) {
			Node navigationInterfaceNode = 
				DOMTreeUtility.getNode(presentationNode, "navigationInterface");
			if (navigationInterfaceNode != null) {
				Vector hideLMSUIVector =
					DOMTreeUtility.getNodes(
							navigationInterfaceNode, "hideLMSUI");
				if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
					System.out.println("\t[adlnavPresentation-START]");
				}
				for (Iterator hideLMSUIIterator = hideLMSUIVector.iterator(); 
					hideLMSUIIterator.hasNext();) {
					Node ssNode = (Node) hideLMSUIIterator.next();
					if (!ssNode.getLocalName().equals("hideLMSUI")) {
						if (Constants.DEBUG_WARNINGS || Constants.DEBUG_INFO
								|| Constants.DEBUG_SEQUENCING) {
							System.out.println("[WARNINGS] Hem trobat '"
									+ ssNode.getLocalName() 
									+ "' i no sabem que és!?!");
							
						}						
					} else {
						String buttonToHide =
							DOMTreeUtility.getNodeValue(ssNode);
						if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
							System.out.println("\t\t[adlnavPresentation]: " 
									+ buttonToHide);
						}
						if (buttonToHide.equals("continue")) {
							if (isLeaf) {				
								leafItem.adlnavPresentation.hideContinue = true;
							} else {
								rootItem.adlnavPresentation.hideContinue = true;
							}							
						} else if (buttonToHide.equals("previous")) {
							if (isLeaf) {				
								leafItem.adlnavPresentation.hidePrevious = true;
							} else {
								rootItem.adlnavPresentation.hidePrevious = true;
							}							
						} else if (buttonToHide.equals("abandon") 
								|| buttonToHide.equals("abandonAll")) {
							if (isLeaf) {				
								leafItem.adlnavPresentation.hideAbandon = true;
							} else {
								rootItem.adlnavPresentation.hideAbandon = true;
							}
						} else if (buttonToHide.equals("exit")
								|| buttonToHide.equals("exitAll")
								|| buttonToHide.equals("suspend")
								|| buttonToHide.equals("suspendAll")) {
							if (isLeaf) {				
								leafItem.adlnavPresentation.hideExit = true;
							} else {
								rootItem.adlnavPresentation.hideExit = true;
							}
						} else {
							if (Constants.DEBUG_WARNINGS || Constants.DEBUG_INFO
									|| Constants.DEBUG_SEQUENCING) {
								System.out.println("[WARNINGS] No hem "
										+ "reconegut '" + buttonToHide 
										+ "'. No el tractem.");
								
							}
						} //DEL if (buttonToHide.equals("continue"))
					} //DEL if (!ssNode.getLocalName().equals("hideLMSUI"))
				} //DEL for ( ... hideLMSUIIterator.hasNext();)
				if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
					System.out.println("\t[adlnavPresentation-END]");
				}
			} //DEL if (navigationInterfaceNode != null)
		} //DEL if (presentationNode != null)
				
		//		Agafem el seqüenciament, si és que n'hi ha...
		Node seqInItem = 
			DOMTreeUtility.getNode(itemNode, Constants.SEQUENCING);		
		if (seqInItem == null) {
			if ((Constants.DEBUG_WARNINGS_LOW && Constants.DEBUG_ITEMS) 
					|| (Constants.DEBUG_WARNINGS_LOW 
							&& Constants.DEBUG_SEQUENCING)) {
				System.out.println("[WARNINGS_LOW][" 
						+ identifier 
						+ "]\tNo hem pogut agafar el sequenciament dintre de " 
						+ "l'Item!!");
			}
			if (isLeaf) {
				/**
				 * Si no tenim seqüenciament és igual. De totes formes
				 * haurem de "reservar" espai per enmagatzemar les variables,
				 * en cas de ser un SCO.
				 */
				CMIDataModel tmpCMIDataModel = new CMIDataModel();
				if (launchData != null) {
					tmpCMIDataModel.launchData = launchData;
				}
				if (completionThreshold != null) {
					tmpCMIDataModel.completionThreshold = completionThreshold;
				}
				if (timeLimitAction != null) {
					tmpCMIDataModel.timeLimitAction = timeLimitAction;
				}
				nouHashtable.put(identifier, tmpCMIDataModel);				
			} else {
				/**
				 * Si no tenim seqüenciament és igual. De totes formes
				 * haurem de "reservar" espai per enmagatzemar les variables
				 * sobre l'estat del cluster.
				 */
				ClusterMap tmpClusterMap = new ClusterMap();
				
				nouClusterTree.put(identifier, tmpClusterMap);
			}
			
		} else {
			
			hasSequencing = true;
			
			/**
			 * tmpCMIDataModel no l'enmagatzemarem enlloc, 
			 * només serveix perquè el parser del seqüenciament
			 * l'ompli indiscriminadament.
			 */
			CMIDataModel tmpCMIDataModel = new CMIDataModel();
			ClusterMap tmpClusterMap  = new ClusterMap();
			
			ParseSequencing nouParseSequencing = null;
			if (launchData != null) {
				tmpCMIDataModel.launchData = launchData;
			}
			if (completionThreshold != null) {
				tmpCMIDataModel.completionThreshold = completionThreshold;
			}
			if (timeLimitAction != null) {
				tmpCMIDataModel.timeLimitAction = timeLimitAction;
			}
			if (isLeaf) {				
				nouParseSequencing = 
					new ParseSequencing(
							seqInItem, 
							tmpCMIDataModel,
							tmpClusterMap,
							leafItem.getIdentifier(),
							true,
							globalMap);
				nouParseSequencing.sequencing.itemProperty = leafItem;
				if (nouParseSequencing.sequencing.attemptAbsoluteDurationLimit 
						!= null) {
					tmpCMIDataModel.maxTimeAllowed =
						nouParseSequencing.sequencing.
							attemptAbsoluteDurationLimit.toString();
				}
			} else {				
				nouParseSequencing = 
					new ParseSequencing(
							seqInItem, 
							tmpCMIDataModel,
							tmpClusterMap,
							rootItem.getIdentifier(),
							false,
							globalMap);
				nouParseSequencing.sequencing.itemProperty = rootItem;
			}			
			
			//tmpCMIDataModel = nouParseSequencing.cmiDataModel;
			if (isLeaf) {
				nouHashtable.put(identifier, tmpCMIDataModel);				
			} else {
				nouClusterTree.put(identifier, tmpClusterMap);
				/*
				if (tmpCMIDataModel.cmiObjectives.size() > 0) {
					nouHashtable.put(identifier, tmpCMIDataModel);
				}
				*/
			}
			
			
			if (nouParseSequencing.hasID()) {
				if (Constants.DEBUG_WARNINGS || Constants.DEBUG_ITEMS) {
					System.out.println("[WARNING] No té sentit "
							+ "que un seqüenciament tingui " 
							+ "identificador si no està dintre d'un "
							+ "SequencingCollection!!");
				}
			} else if (nouParseSequencing.hasIDRef()) {
				Abstract_Item abstItem = null;
				if (isLeaf) {
					abstItem = leafItem;					
				} else {
					abstItem = rootItem;							
				}
				
				if (abstItem.hasSequencingCollection()) {
					if (abstItem.getSequencingCollection().
							searchSequencing(nouParseSequencing.
									sequencing.IDRef) == null) {
						
						if (Constants.DEBUG_ERRORS || Constants.DEBUG_ITEMS) {
							System.out.println("[ERROR] No s'ha trobat"
									+ " cap <sequencing> amb " 
									+ "l'identificador "
									+ nouParseSequencing.sequencing.IDRef
									+ " dintre" 
									+ " del SequencingCollection!!");
						}
						System.exit(-1);
					} else {
						if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
							System.out.println("[INFO]IDRef='"
									+ nouParseSequencing.sequencing.IDRef
									+ "' trobat amb" 
									+ "èxit dintre del SequencingCollection.");
						}
						
						/**
						 * Analitzem el seqüenciament per saber com 
						 * serà la vista actual.
						 */
						currentTreeAnnotations.currentView =
							analizeCurrentView(nouParseSequencing.sequencing,
									currentTreeAnnotations.currentView);
						
						currentTreeAnnotations.currentView =
							analizeCurrentView(
									abstItem.getSequencingCollection().
									searchSequencing(nouParseSequencing.
											sequencing.IDRef),
									currentTreeAnnotations.currentView);
						/*
						if (currentTreeAnnotations.currentView 
								!= TreeAnnotations.viewType.visible) {
							currentTreeAnnotations.invariantItem = false;
						} else {
							currentTreeAnnotations.invariantItem =
								analizeInvariantItem(
										nouParseSequencing.sequencing);
							
							if (currentTreeAnnotations.invariantItem) {
								
								currentTreeAnnotations.invariantItem =
									analizeInvariantItem(
											abstItem.getSequencingCollection().
											searchSequencing(nouParseSequencing.
													sequencing.IDRef));
								
							}
							
						}
						*/
						abstItem.setSequencing(nouParseSequencing.sequencing);
						/*
						if (isLeaf) {				
							leafItem.setSequencing(
									nouParseSequencing.sequencing);
						} else {
							rootItem.setSequencing(
									nouParseSequencing.sequencing);
						}
						*/
					}
				}
			} else {
				/**
				 * Analitzem el seqüenciament per saber com serà la vista
				 * actual.
				 */
				currentTreeAnnotations.currentView =
					analizeCurrentView(nouParseSequencing.sequencing,
							currentTreeAnnotations.currentView);
				/*
				if (currentTreeAnnotations.currentView 
						!= TreeAnnotations.viewType.visible) {
					currentTreeAnnotations.invariantItem = false;
				} else {
					currentTreeAnnotations.invariantItem =
						analizeInvariantItem(
								nouParseSequencing.sequencing);
				}
				*/			
				if (isLeaf) {
					/**
					 * Un Asset tindran l'etiqueta Sequencing si 
					 * REALMENT tenen alguna cosa de seqüenciament!
					 * Un SCO en canvi sempre haurà de contribuir en el 
					 * seqüenciament encara que sigui pels valors per
					 * defecte.
					 */
					if (nouParseSequencing.hasReallyANYSequencing() 
							|| leafItem.getIsSCO()) {
						leafItem.setSequencing(nouParseSequencing.sequencing);
					}					
				} else {
					rootItem.setSequencing(nouParseSequencing.sequencing);
				}																
			}					
		}		
		//	Agafar els items
		//		Ara a tractar tots els items!!!		
		if (((Constants.DEBUG_INFO || Constants.DEBUG_ITEMS)) && !isLeaf) {
			System.out.print("\n");
		}
		
		Vector itemsInOrganization =
			DOMTreeUtility.getNodes(itemNode, Constants.ITEM);
		
		/**
		 * Aquesta variable és temporal i la utilitzarem únicament 
		 * perquè puguem conservar l'ordre original "pare -> fills -> nets".
		 * 
		 */
		Map < String , TreeAnnotations > tmpTreeAnnotations =
			new LinkedHashMap < String, TreeAnnotations > ();
			//new Hashtable < String, TreeAnnotations > ();
		
		for (Iterator itemsIterator = itemsInOrganization.iterator(); 
			itemsIterator.hasNext();) {
			Node itemConcreteNode = (Node) itemsIterator.next();
								
			if (itemConcreteNode == null) {
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_ITEMS) {
					System.out.println("[ERROR] a l'agafar un"
							+ " <item> dintre d'un <organization>!!");
				}
			} else {
				ParseItem nouParseItem = 
					new ParseItem(itemConcreteNode, rootItem);
				
				currentTreeAnnotations.sons.add(
						nouParseItem.getItem().getIdentifier());
				
				boolean hasAnotherSequencing = nouParseItem.getHasSequencing();
				if (hasAnotherSequencing) {
					hasSequencing = true;
				}
				
				nouHashtable =
					printCMIElements(nouHashtable ,
							nouParseItem.getHastTable());
				
				nouClusterTree = printClusterElements(nouClusterTree,
						nouParseItem.getClusterTree(), false);
				 
				Abstract_Item sonItem = nouParseItem.getItem();
				
				globalMap.objective.putAll(nouParseItem.getGlobalMap().objective);
				
				tmpTreeAnnotations.putAll(nouParseItem.getTreeAnnotations());
				
				rootItem.addItem(sonItem);
			}
		}
		currentTreeAnnotations.itemID = identifier;
		treeAnnotations.put(identifier, currentTreeAnnotations);
		
		//		Finalment guardem el parsejat de l'Item en el currentItem.
		if (isLeaf) {
			currentItem =  leafItem;
		} else {
			currentItem = rootItem;
			treeAnnotations.putAll(tmpTreeAnnotations);
		}				
		
	}
	/**
	 * Ens retorna l'Item que estem parsejant. 
	 * @return currentItem : Abstract_Item Type
	 */
	public final Abstract_Item getItem() {
		return currentItem;
	}
	/**
	 * Ens retorna una HastTable on tindrem l'identificador de 
	 * l'item (de l'sco) i la seva estructura amb els seus 
	 * objectives i el corresponent identificador que hem 
	 * agafat del manifest.
	 * 
	 * @return nouHashtable: Hashtable < String, CMIDataModel > 
	 */
	public final Hashtable < String, CMIDataModel > getHastTable() {
		return nouHashtable;
	}
	/**
	 * Des d'aquí retornarem l'array amb tots els clusterTree que hem 
	 * trobat després de parsejar l'actual item i els seus fills.
	 * 
	 * @return Map < String, ClusterMap >
	 */
	public final Hashtable < String, ClusterMap > getClusterTree() {
		return nouClusterTree;
	}
	/**
	 * Retornem el clusterMap amb els objectes globals.
	 * @return ClusterMap Type.
	 */
	public final ClusterMap getGlobalMap() {
		return globalMap;
	}
	/**
	 * Ens retorna un booleà per indicar-nos si té o no té seqüenciament
	 * aquest Item.
	 * Nota! Si qualsevol dels seus fills té seqüenciament el nostre
	 * 'hasSequencing' serà true.
	 * 
	 * @return boolean Type
	 */
	public final boolean getHasSequencing() {
		return hasSequencing;
	}
	/**
	 * Retornem les anotacions del node actual (i a la vegada dels seus
	 * fills). Teòricament només pot fer aquesta crida el ParseItem 
	 * del node pare.
	 * 
	 * String: Identificador dels Items.
	 * 
	 * @return Map < String, TreeAnnotations >
	 */
	public final Map < String, TreeAnnotations > getTreeAnnotations() {
		return treeAnnotations;
	}
	
	/**
	 * Printem i fem una copia de la HashTable!!!
	 * 
	 * @param nouHashMap : És la taula que acumula els resultats.
	 * @param currentHashMap : És la taula temporal que volem copiar. 
	 * @return Hashtable < String, CMIDataModel > Type
	 */
	private Hashtable < String, CMIDataModel > printCMIElements(
			final Hashtable < String, CMIDataModel > nouHashMap,
			final Hashtable < String, CMIDataModel > currentHashMap) {
		/**
		 * Printem els CMI* i Copiem!!!!
		 */
		if (currentHashMap != null) {
			
			Enumeration < String > claus = 
				currentHashMap.keys();
			Enumeration < CMIDataModel > elements = 
				currentHashMap.elements();
			
			while (claus.hasMoreElements()) {
				String novaClau = claus.nextElement();
				CMIDataModel nouElement = elements.nextElement();
				
				if ((Constants.DEBUG_SEQUENCING || Constants.DEBUG_INFO) 
						&& Constants.DEBUG_WARNINGS_LOW 
						&& Constants.DEBUG_NAVIGATION_LOW) {
					System.out.println("ParseItem: [" + novaClau + "] ->" 
							+ nouElement.cmiObjectives.size());
				}
				
				for (Iterator itt = 
					nouElement.cmiObjectives.values().iterator();
						itt.hasNext();) {
					CMIObjectives nouObjective =
						(CMIObjectives) itt.next();
					
					if ((Constants.DEBUG_SEQUENCING || Constants.DEBUG_INFO) 
							&& Constants.DEBUG_WARNINGS_LOW
							&& Constants.DEBUG_NAVIGATION_LOW) {
						System.out.println("ParseItem: \t->" 
								+ nouObjective.id);
					}
				}
				
				nouHashMap.put(novaClau, nouElement);
			}
		}
		return nouHashMap;
	}
	
	/**
	 * Printem i fem una copia de la HashTable!!!
	 * 
	 * @param nouHashMap : És la taula que acumula els resultats.
	 * @param currentHashMap : És la taula temporal que volem copiar. 
	 * @param print : boolean type.
	 * @return Hashtable < String, CMIDataModel > Type
	 */
	private Hashtable < String, ClusterMap > printClusterElements(
			final Hashtable < String, ClusterMap > nouHashMap,
			final Hashtable < String, ClusterMap > currentHashMap,
			final boolean print) {
		
		/**
		 * Printem els CMI* i Copiem!!!!
		 */
		if (currentHashMap != null) {
			
			Enumeration < String > claus = 
				currentHashMap.keys();
			Enumeration < ClusterMap > elements = 
				currentHashMap.elements();
			
			if ((Constants.DEBUG_SEQUENCING || Constants.DEBUG_INFO) 
					&& print) {
				System.out.println("\t##### ClusterTree #####");
			}
			while (claus.hasMoreElements()) {
				
				String novaClau = claus.nextElement();
				ClusterMap nouElement = elements.nextElement();
				if ((Constants.DEBUG_SEQUENCING || Constants.DEBUG_INFO) 
						&& print) {
					System.out.println("[" + novaClau + "] ->" 
							+ nouElement.objective.size());					
				}
				
				Enumeration < String > clausObjective =
					nouElement.objective.keys();
				
				while (clausObjective.hasMoreElements()) {
					String noClau = clausObjective.nextElement();
					System.out.println("\t->" 
							+ noClau);
				}										
				nouHashMap.put(novaClau, nouElement);
			}
		}
		return nouHashMap;
	}
	
	/**
	 * Aquest funció ens analitzarà el seqüenciament per saber si el 
	 * currentView és disabled o notVisible.
	 * 
	 * @param analizeSeq : Sequencing Type.
	 * @param cView : És el TreeAnnotations.viewType actual.
	 * @return TreeAnnotations.viewType : {visible | notVisible | disabled}
	 */
	private TreeAnnotations.viewType analizeCurrentView(
			final Sequencing analizeSeq,
			final TreeAnnotations.viewType cView) {
		
		TreeAnnotations.viewType returnedType =
			TreeAnnotations.viewType.visible;
		for (Iterator seqIterator = analizeSeq.sequencingRules.iterator();
			seqIterator.hasNext();) {
			SequencingRules seqRule = (SequencingRules) seqIterator.next();
			if (seqRule.conditionRule 
					== SequencingRules.conditionRuleType.preConditionRule) {
								
				for (Iterator sConIterator = seqRule.condition.iterator();
					sConIterator.hasNext();) {
					SequencingCondition seqCondition = 
						(SequencingCondition) sConIterator.next();
					if ((seqCondition.condition 
							== SequencingCondition.conditionType.always) 
							&& (seqCondition.operator == OperatorType.NoOp)) {
						if (seqRule.ruleAction 
								== SequencingRules.ruleActionType.disabled) {

							if (cView == TreeAnnotations.viewType.notVisible) {
								return TreeAnnotations.viewType.notVisible;
							} else {
								return TreeAnnotations.viewType.disabled;
							}
							
							
						} else if (seqRule.ruleAction 
								== SequencingRules.ruleActionType.skip) {
							
							return TreeAnnotations.viewType.notVisible;
						}
												
					}
				}
				
			}
		}
		
		return returnedType;
		
	}

	/**
	 * Aquesta funció ens ajudarà per dir-nos si aquest item es mantindrà
	 * invariable al llarg del seqüenciament.
	 * 
	 * Per saber-ho mirarem si té algun MapInfo, perquè de ser així no podem
	 * predir tan fàcilment si serà o no invariable i per tant el marcarem
	 * com a false. 
	 * 
	 * Si té una pre o post condició tampoc no podrem predir-ho sense analitzar
	 * tot el seqüenciament de l'arbre en aquell instant, per tant el marcarem
	 * com a false també.
	 *  
	 * @param analizeSeq : Sequencing Type
	 * @return boolean Type: És mantindrà invariable? True | False.
	 */
	private boolean analizeInvariantItem(final Sequencing analizeSeq) {
		/**
		 * TODO Acabar el analizeInvariantItem(final Sequencing analizeSeq)
		 */
		return true;
	}
}
