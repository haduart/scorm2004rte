// $Id: DOMParser.java,v 1.26 2008/01/31 14:23:37 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.validator;


import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Abstract_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	ResourcesCollection;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Root_Item;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	SequencingCollection;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.MetadataInformation;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Sequencing;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.SequencingCondition;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.SequencingRules;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.
	Elements.Types.OperatorType;
import edu.url.lasalle.campus.scorm2004rte.server.
	DataBase.DynamicData.CMIDataModel;
import edu.url.lasalle.campus.scorm2004rte.server.
	DataBase.DynamicData.CMIObjectives;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.ClusterDataModel;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.ClusterMap;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.
	DynamicData.TreeAnnotations;
import edu.url.lasalle.campus.scorm2004rte.server.
	DataBase.DynamicData.UserObjective;

import edu.url.lasalle.campus.scorm2004rte.system.Constants;

import edu.url.lasalle.campus.scorm2004rte.server.validator.
	concreteParsers.ParseItem;
import edu.url.lasalle.campus.scorm2004rte.server.validator.
	concreteParsers.ParseResource;
import edu.url.lasalle.campus.scorm2004rte.server.validator.
	concreteParsers.ParseSequencing;
import edu.url.lasalle.campus.scorm2004rte.server.validator.
	concreteParsers.ParseMetadata;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;

/**
 * $Id: DOMParser.java,v 1.26 2008/01/31 14:23:37 ecespedes Exp $
 *
 * <b>Títol:</b> DOMParser <br><br>.
 * <b>Descripció:</b>
 *
 * @author Eduard Céspedes i Borràs / Enginyeria La Salle / haduart@gmail.com
 * @version 1.0 $Revision: 1.26 $ $Date: 2008/01/31 14:23:37 $
 * $Log: DOMParser.java,v $
 * Revision 1.26  2008/01/31 14:23:37  ecespedes
 * Millorant el sistema.
 *
 * Revision 1.25  2008/01/08 11:50:21  ecespedes
 * Ultimes modificacions de l'OverallSequencingProcess
 *
 * Revision 1.24  2008/01/03 15:05:32  ecespedes
 * Arreglats elements del CMIDataModel i començant a crear un nou test.
 *
 * Revision 1.23  2007/12/21 17:12:44  ecespedes
 * Implementant CourseManager.OverallSequencingProcess
 *
 * Revision 1.22  2007/12/20 20:45:53  ecespedes
 * Implementat l'Objective Map
 *
 * Revision 1.21  2007/12/17 15:27:48  ecespedes
 * Fent MapInfo. Bug en els Leaf_Items
 *
 * Revision 1.20  2007/12/13 15:25:12  ecespedes
 * Problemes amb el sistema d'arbre de clusters.
 * Falla l'ObjectiveStatusKnown.
 *
 * Revision 1.19  2007/12/10 22:03:32  ecespedes
 * Implementant les funcions per buscar un item concret i retornar-lo
 * al CourseManager perquè el tracti.
 *
 * Revision 1.18  2007/12/09 22:32:16  ecespedes
 * Els objectius dels clusters es tracten i es guarden de manera diferent.
 *
 * Revision 1.17  2007/12/05 13:36:59  ecespedes
 * Arreglat bug limitConditions i PreConditions
 *
 * Revision 1.16  2007/12/03 17:58:51  ecespedes
 * Implementat un attemptCount que ens servirà per l'attemptLimit.
 *
 * Revision 1.15  2007/11/19 07:34:15  ecespedes
 * Millores en els RollupRules.
 *
 * Revision 1.14  2007/11/15 15:24:04  ecespedes
 * Implementat el ObjectiveInterface en els Leaf_Item.
 *
 * Revision 1.13  2007/11/13 15:59:58  ecespedes
 * Treballant sobre el sistema per "linkar" els Objectives de l'estructura
 * Sequencing de l'arbre amb els Objectives de l'usuari.
 * Millorat TreeAnnotations (step 2 de 3)
 *
 * Revision 1.12  2007/11/12 13:00:21  ecespedes
 * Arreglant elements del seqüenciament.
 * Ja qüasi està implementat el TreeAnnotation.
 *
 * Revision 1.11  2007/11/08 16:33:09  ecespedes
 * Començant a Implementar el OverallSequencingProcess, que serà
 * les funcions del CourseManager, Abstract/Root/Leaf Item que aplicaran
 * realment el seqüenciament SCORM1.3
 *
 * Revision 1.10  2007/10/31 12:57:01  ecespedes
 * Començant a crear tot el sistema de gestió dels arbres:
 * + Creant l'interfífice DataAccess que ens servirà tan
 * 	pel parser com pel SGBD.
 * - Falta que el ParserHandler/GestorBD implementi l'interfície.
 *
 * Revision 1.9  2007/10/30 14:07:41  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Començant a crear el sistema de gestió dels cursos:
 * - CourseAdministrator i CourseManager.
 *
 * Revision 1.8  2007/10/29 14:56:10  ecespedes
 * Trobat un bug en el sistema recursiu de detecció de seqüenciament
 * i de creació del mateix a partir dels leaf items.
 *
 * Revision 1.7  2007/10/29 09:45:39  ecespedes
 * Enmig d'un problema de l'eclipse... actualizant la versió actual.
 *
 * Revision 1.6  2007/10/29 07:30:55  ecespedes
 * Arreglant errors i solucionant problemes a l'hora de crear les
 * estructures que guardaran les dades serialitzades dels
 * usuaris.
 *
 * Revision 1.5  2007/10/26 22:25:21  ecespedes
 * Ara el parser crea l'estructura bàsica de l'usuari.
 * Arreglat diversos errors, checkstyles i recursivitats.
 *
 * Revision 1.4  2007/10/25 21:52:05  ecespedes
 * Arreglant petits errors i el checkstyle.
 *
 * Revision 1.3  2007/10/25 12:42:14  ecespedes
 * Actualitzant els Objectives i el model de dades de l'usuari.
 *
 * Revision 1.2  2007/10/24 10:52:56  ecespedes
 * Solucionat un petit problema que quedava pendent i després he començat a 
 * 	retocar algo del checkstyle. 
 * 
 * Revision 1.1  2007/10/22 20:22:15  msegarra
 * Creació de l'arbre CVS del projecte.
 *
 */

public class DOMParser {
	//Constants per aquesta classe	
	/**
	 * "manifest" (String Constant Type).
	 */
	private static final String MANIFEST 			= 	"manifest";
	/**
	 * "metadata" (String Constant Type).
	 */
	private static final String METADATA 			= 	"metadata";
	/**
	 * "organizations" (String Constant Type).
	 */
	private static final String ORGANIZATIONS 		= 	"organizations";
	/**
	 * "resources" (String Constant Type).
	 */
	private static final String RESOURCES 			= 	"resources";
	/**
	 * "sequencingCollection" (String Constant Type).
	 */
	private static final String SEQUENCINGCOLLECTION = 	"sequencingCollection";
	/**
	 * "imsmanifest.xml" (String Constant Type).
	 */
	private static final String IMSMANIFEST 		= 	"imsmanifest.xml";
	/**
	 * "file:" (String Constant Type).
	 */
	private static final String DIRECTORY_FILE		=	"file:";
	
//	Variables
	/**
	 * En aquesta variable anirem fent les anotacions de l'Item
	 * actual.
	 */
	private TreeAnnotations currentTreeAnnotations = new TreeAnnotations();
	
	/**
	 * Variable per fer el registre de logs.
	 */
	//private LogControl log = LogControl.getInstance();		
	/**
	 * És la variable que retornem. 
	 * (Collection[Root_Item] Type)
	 */
	private Collection < Root_Item > organizations =
		new ArrayList < Root_Item >();
	/**
	 * Ens servirà per saber si hi ha hagut un error
	 * al parsejar i per tant si no hi ha cap valor
	 * en el 'organizations'.
	 */
	private boolean errorInOrganizations = false;
	/**
	 * Creem l'estructura inicial de l'usuari.
	 */
	private Collection < UserObjective > userObjective =
		new ArrayList < UserObjective >();
	/**
	 * La informació de la Metadata. 
	 * TODO Buscar algun lloc on guardar-ho!
	 */
	private MetadataInformation metadataInformation = new MetadataInformation();
	/**
	 * Variable temporal on guardarem els resources fins que els assignem
	 * a l'organització.
	 * (ResourcesCollection Type)
	 */
	private ResourcesCollection resourcesCollection = new ResourcesCollection();
	/**
	 * Variabler per saber si hem trobat un sequencingCollection.
	 * (boolean Type, default false)
	 */
	private boolean hasSequencingCollection = false;
	/**
	 * Variable temporal on guardarem el sequencingCollection fins que no
	 * l'assignem a l'organització. 
	 */
	private SequencingCollection sequencingCollectionObject;
	/**
	 * Variable temporal on enmagatzemarem el path on es troba el manifest. 
	 * Aquesta variable també la passarem al parser dels resources perquè
	 * pugui comprovar si existeixen els fitxers o no. 
	 */
	private String path;
	
	/**
	 * A partir del "document" del manifest retorna el path del directori.
	 * @param document (Document Type)
	 * @return String
	 */
	private String finalPath(final Document document) {
		String localPath = new String("");
		for (StringTokenizer fracToken = new StringTokenizer(
				document.getBaseURI(), "/");
			fracToken.hasMoreTokens();) {
			String tmpString = fracToken.nextToken();
			if ((!tmpString.equals(IMSMANIFEST)) 
					&& (!tmpString.equals(DIRECTORY_FILE))) {
				localPath = localPath + tmpString + "/";					
			}				
		}
		if (Constants.DEBUG_INFO) {
			System.out.println("finalPath(Document document): " + localPath);
		}
		return localPath;
	}
	/**
	 * Constructor de la classe DOMParser. 
	 * @param imsmanifest :És l'String de la direcció del manifest. 
	 */
	public DOMParser(final String imsmanifest) {		
		
		DOMValidate validator = new DOMValidate(imsmanifest);		
		if (validator.isValidated()) {
			Document document = validator.getDocument();
			path = finalPath(document);			
			errorInOrganizations =
				parseDocument(document);
						
			if (!errorInOrganizations) {
				if (Constants.DEBUG_ERRORS) {
					/*
					log.writeMessage(Level.SEVERE, DOMParser.class.getName(),
							"[ERROR] quan estava parsejant" 
							+ " tranquilament...!!!!");
					*/				
					System.out.println("[ERROR] quan estava parsejant" 
							+ " tranquilament...!!!!");
							
				}
			} else {
				if (Constants.DEBUG_INFO) {
					/*
					log.writeMessage(Level.INFO, DOMParser.class.getName(),
							"[INFO] Document parsejat" 
							+ " correctament.");
					*/
					System.out.println("[INFO] Document parsejat" 
							+ " correctament.");
				}
			}
		} else {
			if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) {
				System.out.println("[ERROR isValidated()] El fitxer '" 
						+ imsmanifest + "' no s'ha validat correctament!!");
			}
		}			
	}
	
	/**
	 * És la funció principal, la que parsejarà realment tot el document. 
	 * @param document : És el document que guarda el manifest.
	 * @return boolean : False == error; True == tot correcte.
	 */
	private boolean parseDocument(final Document document) {
		
		Node manifest = DOMTreeUtility.getNode(document, MANIFEST);
				
		if (manifest == null) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] No s'ha trobat " 
						+ "el manifest. No tenim punt inicial.");
			}
			return false;
		}
		if (!manifest.getLocalName().equals(MANIFEST)) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] No s'ha trobat el" 
						+ " manifest. No tenim punt inicial.");
			}
			return false;
		}		
		
		String identifier = DOMTreeUtility.getAttributeValue(manifest,
				Constants.IDENTIFIER);
		if (identifier.length() == 0) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] No s'ha trobat" 
						+ " l'atribut identifier' en <manifest>.");
			}
			return false;
		} else {
			metadataInformation.identifier = identifier;
			if (Constants.DEBUG_INFO || Constants.DEBUG_METADATA) {
				System.out.println("[INFO] identifier : " 
						+ metadataInformation.identifier);
			}
		}
		
		String version = DOMTreeUtility.getAttributeValue(manifest, "version");
		if (version.length() == 0) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] No s'ha trobat l'atribut " 
						+ "'version' en <manifest>.");
			}
			return false;
		} else {
			metadataInformation.version = version;
			if (Constants.DEBUG_INFO || Constants.DEBUG_METADATA) {
				System.out.println("[INFO] version : " 
						+ metadataInformation.version);
			}
			
		}
		
		
		Node metadata = DOMTreeUtility.getNode(manifest, METADATA);
		if (metadata == null) {
			if (Constants.DEBUG_WARNINGS || Constants.DEBUG_METADATA) {
				System.out.println("[WARNING]" 
						+ " No he trobat el metadata!");
			}
		} else {
			ParseMetadata metadataValidator = 
				new ParseMetadata(metadata, path, metadataInformation);
			metadataInformation = metadataValidator.getMetadata();
			
		}
		// -------------------------------- Parsegem els resources {
		Node resources = DOMTreeUtility.getNode(manifest, RESOURCES);
		if (resources == null) {
			if (Constants.DEBUG_ERRORS || Constants.DEBUG_RESOURCES) {
				System.out.println("[ERROR] No hem trobat " 
						+ "<resources>!");						
			}
			return false;
		}
		
		Vector resourceInResources = DOMTreeUtility.getNodes(resources,
				Constants.RESOURCE);
			
		for (Iterator resourcesIterator = resourceInResources.iterator(); 
				resourcesIterator.hasNext();) {
			Node resourceNode = (Node) resourcesIterator.next();
			if (resourceNode == null) {
				if (Constants.DEBUG_ERRORS) {
					System.out.println("[ERROR] a l'agafar" 
							+ " els nodes de resources!!");
				}
			} else {
				ParseResource nouParseResource = new ParseResource(resourceNode,
						path);
				resourcesCollection.addResource(nouParseResource.getResource());
			}
		}
		// } Parsegem els resources	--------------------------------
				
		// -------------------------------- Parsegem el sequencingCollection {
		Node sequencingCollection = DOMTreeUtility.getNode(manifest,
				SEQUENCINGCOLLECTION);
		if (sequencingCollection == null) {
			hasSequencingCollection = false;
			if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
				System.out.println("[INFO]No tenim sequencingCollection.");
			}
		} else {
			hasSequencingCollection = true;
			sequencingCollectionObject = new SequencingCollection();
			Vector seqInSequencing = DOMTreeUtility.getNodes(
					sequencingCollection, Constants.SEQUENCING);
			
			for (Iterator sequencingIterator = seqInSequencing.iterator(); 
					sequencingIterator.hasNext();) {
				Node sequencingNode = (Node)  sequencingIterator.next();
				if (sequencingNode == null) {
					if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) {
						System.out.println("[ERROR] a l'agafar "
								+ "el sequenciament" 
								+ " dintre del SequencingCollection!!");
					}
				} else {
					/**
					 * tmpCMIDataModel no l'enmagatzemarem enlloc, 
					 * només serveix perquè el parser del seqüenciament
					 * l'ompli indiscriminadament.
					 */
					CMIDataModel tmpCMIDataModel =
						new CMIDataModel();
					ClusterMap tmpClusterMap = new ClusterMap();
					
					ParseSequencing nouParseSequencing = 
						new ParseSequencing(
								sequencingNode, 
								tmpCMIDataModel,
								tmpClusterMap,
								identifier,
								false,
								new ClusterMap());
										
					if (!nouParseSequencing.hasID()) {
						if (Constants.DEBUG_ERRORS 
								|| Constants.DEBUG_SEQUENCING) {
							System.out.println("[ERROR] És un " 
									+ "camp obligatori si estem dintre d'un"
									+ " SequencingCollection!!");
						}
					} else if (nouParseSequencing.hasIDRef()) {
						if (Constants.DEBUG_WARNINGS 
								|| Constants.DEBUG_SEQUENCING) {
							System.out.println("[WARNING] Un Sequencing "
									+ "que estigui dintre d'un "
									+ "SequencingCollection no podrà fer "
									+ "referència a cap altre Sequencing!!");
						}
					} else {
						sequencingCollectionObject.
								addSequencing(nouParseSequencing.
										sequencing);						
					}					
				}
			}			
		}		
		// } Parsegem el sequencingCollection --------------------------------
			
		
		// -------------------------------- Parsegem el Organizations {
		Node organizationsNode = 
			DOMTreeUtility.getNode(manifest, ORGANIZATIONS);
		
		if (organizationsNode == null) {
			if (Constants.DEBUG_ERRORS || Constants.DEBUG_ITEMS) {
				System.out.println("[ERROR]No tenim o no "
						+ "s'ha trobat cap Organització!");
			}
			return false;
		}
		
		Vector organInOrganizations = DOMTreeUtility.
			getALLNodes(organizationsNode);
		
		for (Iterator organizationIterator = organInOrganizations.iterator(); 
			organizationIterator.hasNext();) {
			Node ssNode =
				(Node) organizationIterator.next();
			
			if (ssNode == null) {
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_ITEMS) {
					System.out.println("[ERROR] a l'agafar"
							+ " una <Organization>!!");
				}
				return false;
			} else if (!ssNode.getLocalName().equals("organization")) {
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_ITEMS) {
					System.out.println("[ERROR] S'esperava un"
							+ " element <Organization>"
							+ " i hem trobat un <"
							+ ssNode.getLocalName() + ">!!");
				}
				return false;
			} else {				
				//parsegem els atributs, n'ha de tenir, sinó malament. 
				String identifierItem = 
					DOMTreeUtility.getAttributeValue(
							ssNode, Constants.IDENTIFIER);
											
				if (identifierItem.length() == 0) {
					if (Constants.DEBUG_WARNINGS_LOW || Constants.DEBUG_ITEMS) {
						System.out.println("[WARNINGS_LOW] Atribut "
								+ "'identifier' no trobat.");
					}
				}
				/**
				 * Creem el UserObjective per aquesta organització.
				 */
				UserObjective nouUserObjective = new UserObjective();
				nouUserObjective.organizationName = identifierItem;
									
				//Inicialitzem l'organització
				Root_Item organizationItem = new Root_Item();
				organizationItem.father = null;
				organizationItem.setIsLeaf(false);
				organizationItem.isOrganization = true;
				organizationItem.setLoaded(true);
				organizationItem.setIdentifier(identifierItem);
				if (hasSequencingCollection) {
					organizationItem.setSequencingCollection(
							sequencingCollectionObject);
				}
				organizationItem.setResourcesCollection(resourcesCollection);
				ClusterMap globalMap = new ClusterMap();
								
				//Agafem el title
				Node titleNode =
					DOMTreeUtility.getNode(ssNode, Constants.TITLE);
				
				if (titleNode == null) {
					if (Constants.DEBUG_WARNINGS_LOW || Constants.DEBUG_ITEMS) {
						System.out.println("[WARNINGS_LOW] Atribut "
								+ "Title no trobat.");
					}
					return false;
				} else {
					String realTitle = DOMTreeUtility.getNodeValue(titleNode);
					if (realTitle.length() == 0) {				
						if (Constants.DEBUG_WARNINGS_LOW 
								|| Constants.DEBUG_ITEMS) {
							System.out.println("[WARNINGS_LOW] Atribut"
									+ " Title no trobat.");
						}
						return false;
					}
					currentTreeAnnotations.title = realTitle;
					organizationItem.setTitle(realTitle);
				}				
				
				//Printem per pantalla per tindre una orientació...
				if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
					System.out.println("\n\n[organization "
							+ organizationItem.getIdentifier() + "\t title:'"
							+ organizationItem.getTitle() + "' ]\n");
				}
				
				//Agafem el seqüenciament, si és que n'hi ha...
				Node seqInOrganization = 
					DOMTreeUtility.getNode(ssNode, Constants.SEQUENCING);
				
				if (seqInOrganization == null) {
					if (Constants.DEBUG_WARNINGS || Constants.DEBUG_ITEMS) {
						System.out.println("[WARNING], No hem pogut "
								+ "agafar el sequenciament dintre de " 
								+ "l'Organization!!");
					}
				} else {
					/**
					 * tmpCMIDataModel no l'enmagatzemarem enlloc, 
					 * només serveix perquè el parser del seqüenciament
					 * l'ompli indiscriminadament.
					 */
					CMIDataModel tmpCMIDataModel = new CMIDataModel();
					ClusterMap tmpClusterMap  = new ClusterMap();
					
					ParseSequencing nouParseSequencing =
						new ParseSequencing(
								seqInOrganization,
								tmpCMIDataModel,
								tmpClusterMap,
								identifier,
								false,
								globalMap);
					
					nouParseSequencing.sequencing.itemProperty =
						organizationItem;
					
					if (nouParseSequencing.hasID()) {
						if (Constants.DEBUG_WARNINGS || Constants.DEBUG_ITEMS) {
							System.out.println("[WARNING] No té sentit"
									+ " que un seqüenciament tingui " 
									+ "identificador si no està dintre"
									+ " d'un SequencingCollection!!");
						}
					} else {
						if (nouParseSequencing.hasIDRef()) {
							if (sequencingCollectionObject.searchSequencing(
									nouParseSequencing.sequencing.IDRef) 
										== null) {
								if (Constants.DEBUG_ERRORS 
										|| Constants.DEBUG_ITEMS) {
									
									System.out.println("[ERROR] No s'ha "
											+ "trobat cap <sequencing> amb "
											+ "l'identificador " 
											+ nouParseSequencing.
												sequencing.IDRef
											+ " dintre" 
											+ " del SequencingCollection!!");
								}
								return false;
							} else {
								if (Constants.DEBUG_INFO 
										|| Constants.DEBUG_ITEMS) {
									System.out.println("[INFO]IDRef='"
											+ nouParseSequencing.
												sequencing.IDRef
											+ "' trobat amb"
											+ "èxit dintre del"
											+ " SequencingCollection.");
								}
								
								/**
								 * Analitzem el seqüenciament per saber 
								 * com serà la vista actual.
								 */
								currentTreeAnnotations.currentView =
									analizeCurrentView(nouParseSequencing.
												sequencing,
												currentTreeAnnotations.
													currentView);
								
								currentTreeAnnotations.currentView =
									analizeCurrentView(
											sequencingCollectionObject.
											searchSequencing(
													nouParseSequencing.
														sequencing.IDRef),
											currentTreeAnnotations.currentView);
								/*
								if (currentTreeAnnotations.currentView 
										!= TreeAnnotations.viewType.visible) {
									currentTreeAnnotations.invariantItem =
										false;
								} else {
									currentTreeAnnotations.invariantItem =
										analizeInvariantItem(
												nouParseSequencing.sequencing);
									
									if (currentTreeAnnotations.invariantItem) {
										
										currentTreeAnnotations.invariantItem =
											analizeInvariantItem(
													sequencingCollectionObject.
													searchSequencing(
															nouParseSequencing.
															sequencing.IDRef));
										
									}
									
								}
								*/
							}												
						} else {
							
							/**
							 * Analitzem el seqüenciament per saber 
							 * com serà la vista actual.
							 */
							currentTreeAnnotations.currentView =
								analizeCurrentView(
										nouParseSequencing.sequencing,
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
							organizationItem.setSequencing(
									nouParseSequencing.sequencing);
							
						}
					}
				}				
				
				//Ara a tractar tots els items!!!
				Vector itemsInOrganization = DOMTreeUtility.getNodes(
						ssNode, Constants.ITEM);
				
				boolean hasAnotherSequencing = false;
				Hashtable < String, CMIDataModel > nouHashMap =
					new Hashtable < String, CMIDataModel >();
				Hashtable < String, ClusterMap > clusterTree =
					new Hashtable < String, ClusterMap > ();
				
				/**
				 * Aquesta variable és temporal i la utilitzarem únicament 
				 * perquè puguem conservar l'ordre original 
				 * "pare -> fills -> nets".
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
							System.out.println("[ERROR]a l'agafar un <item>" 
									+ " dintre d'un <organization>!!");
						}
						return false;
					} else {			
						
						ParseItem nouParseItem = new ParseItem(
								itemConcreteNode,
								organizationItem);
						
						currentTreeAnnotations.sons.add(
								nouParseItem.getItem().getIdentifier());
						
						globalMap.objective.putAll(
								nouParseItem.getGlobalMap().objective);
						
						tmpTreeAnnotations.putAll(
								nouParseItem.getTreeAnnotations());
						
						hasAnotherSequencing = 
							nouParseItem.getHasSequencing();
						/**
						 * Printem els CMI* i Copiem!!!!
						 */
						nouHashMap = printCMIElements(
								nouHashMap,
								nouParseItem.getHastTable(),
								false);
						clusterTree = printClusterElements(
								clusterTree,
								nouParseItem.getClusterTree(),
								false);
						
						Abstract_Item tmpOrganization = 
							(Abstract_Item) nouParseItem.getItem();
						
						if (tmpOrganization.hasSequencing()) {
							if (Constants.DEBUG_WARNINGS_LOW 
									&& Constants.DEBUG_SEQUENCING) {
								System.out.println("[INFO_LOW] Generem " 
										+ "seqüenciament per l'organització del"
										+ " procés de parsejat.");	
							}
							
						}				
						
						
						organizationItem.addItem(tmpOrganization);
						
					}
				}
				if (hasAnotherSequencing || hasSequencingCollection) {
					organizationItem.activateSequencing();
				}
				/**
				 * El printCMIElements només serà per printar, de debug.
				 */
				printCMIElements(
						new Hashtable < String, CMIDataModel >(),
							nouHashMap,
							true);
				ClusterMap orgCluster = clusterTree.get(identifierItem);
				if (orgCluster == null) {
					orgCluster = new ClusterMap();
					clusterTree.put(identifierItem, orgCluster);
				}
				fillClusterTree(clusterTree);
				printClusterElements(
						new Hashtable < String, ClusterMap >(),
						clusterTree,
						true);
				
				nouUserObjective.dataModel = nouHashMap;
				nouUserObjective.clusterTree = clusterTree;
				nouUserObjective.remainInComplete =
					nouUserObjective.dataModel.size();
				currentTreeAnnotations.itemID = identifierItem;
				nouUserObjective.treeAnnotations.put(
						identifierItem, currentTreeAnnotations);
				
				nouUserObjective.treeAnnotations.putAll(tmpTreeAnnotations);
				nouUserObjective.globalObjectiveMap = globalMap;
				printGlobalElements(globalMap);
				userObjective.add(nouUserObjective);
				
				organizations.add(organizationItem);
			}
			// } Parsegem el Organizations --------------------------------
			
		}
		
		if (Constants.DEBUG_INFO || Constants.DEBUG_ITEMS) {
			System.out.println("\t\t[organizations]\n"
					+ "organizations.size(): " + organizations.size()
					+ "\nhasSequencing(): " 
					+ organizations.iterator().next().hasSequencing()
					+ "\ngetIdentifier(): " 
					+ organizations.iterator().next().getIdentifier());			
		}
		if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
			System.out.println("[INFO][userObjective]\t"
					+ "userObjective.size: " + userObjective.size());
		}	
		
		if (Constants.DEBUG_INFO) {
			System.out.println("[INFO]Hem arribat fins al final " 
					+ "del procés de parsejat.");	
		}
		
		return true;
	}
	
	/**
	 * Printem i fem una copia de la HashTable!!!
	 * 
	 * @param nouHashMap : És la taula que acumula els resultats.
	 * @param currentHashMap : És la taula temporal que volem copiar. 
	 * @param print : boolean Type; Per controlar quan volem imprimir.
	 * @return Hashtable < String, CMIDataModel > Type
	 */
	private Hashtable < String, CMIDataModel > printCMIElements(
			final Hashtable < String, CMIDataModel > nouHashMap,
			final Hashtable < String, CMIDataModel > currentHashMap,
			final boolean print) {
		/**
		 * Printem els CMI* i Copiem!!!!
		 */
		if (currentHashMap != null) {
			
			Enumeration < String > claus = 
				currentHashMap.keys();
			Enumeration < CMIDataModel > elements = 
				currentHashMap.elements();
			
			if ((Constants.DEBUG_SEQUENCING || Constants.DEBUG_INFO) 
					&& print) {
				System.out.println("\t##### UserObjective #####");
			}
			while (claus.hasMoreElements()) {
				String novaClau = claus.nextElement();
				CMIDataModel nouElement = elements.nextElement();
				if ((Constants.DEBUG_SEQUENCING || Constants.DEBUG_INFO) 
						&& print) {
					System.out.println("[" + novaClau + "] ->" 
							+ nouElement.cmiObjectives.size());					
				}
				
				for (Iterator itt = 
					nouElement.cmiObjectives.values().iterator();
						itt.hasNext();) {
					CMIObjectives nouObjective =
						(CMIObjectives) itt.next();
					
					if ((Constants.DEBUG_SEQUENCING || Constants.DEBUG_INFO)
							&& print) {
						System.out.println("\t->" 
								+ nouObjective.id);
					}
				}
				
				nouHashMap.put(novaClau, nouElement);
			}
		}
		return nouHashMap;
	}
	/**
	 * Ens retorna les estructures UserObjectives (una per cada 
	 * organització que tinguem). Aquestes estructures seran les 
	 * que es serialitzaran i aniran a parar a la BD.
	 * Aquestes, en concret, seràn les estructures que serviran de 
	 * "model" de manera que cada nou usuari tindra inicialment una 
	 * copia d'aquesta estructura.
	 * 
	 * @return Collection < UserObjective > Type. 
	 */
	public final Collection < UserObjective > getUserObjective() {
		return userObjective;
	}
	/**
	 * Ens retorna les organitzacions que hem parsejat.
	 * Si hi ha hagut un error serà null.
	 * 
	 * @return Collection < Root_Item > : 'organizations'
	 */
	public final Collection < Root_Item > getOrganizatons() {
		if (!errorInOrganizations) {
			return null;
		} else {		
			return organizations;
		}
	}
	/**
	 * Ens retornarà 'true' si tot és correcte i 'false' si hi ha 
	 * algun error.
	 * 
	 * @return boolean : 'errorInOrganizations'
	 */
	public final boolean getIsParsingCorrect() {
		return errorInOrganizations;
	}
	/**
	 * Ens retorna la metadata que hem parsejat. 
	 * 
	 * @return MetadataInformation Type. 
	 */
	public final MetadataInformation getMetadataInformation() {
		return metadataInformation;
	}
	/**
	 * Ens retornarà un boolean indicant-nos si aquest curs 
	 * té un SequencingCollection o no. 
	 * Cal dir que l'estructura sequencingCollection la tindrem 
	 * dintre de les organitzacions. 
	 * 
	 * @return boolean : 'hasSequencingCollection'
	 */
	public final boolean getHasSequencingCollection() {
		return hasSequencingCollection;		
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
	 * Printem els Objectives Map que tenim en el sistema (que són els
	 * objectes globals).
	 * 
	 * @param nouElement : ClusterMap Type
	 */
	private void printGlobalElements(
			final ClusterMap nouElement) {
				
		//ClusterMap nouElement = elements.nextElement();
				
		System.out.println("####### Global Objectives ("
				+ nouElement.objective.size() + ") #######");				
		
		Enumeration < String > clausObjective =
			nouElement.objective.keys();
		Iterator < ClusterDataModel > clusterDMobjective =
			nouElement.objective.values().iterator();
		
		while (clausObjective.hasMoreElements()) {
			String noClau = clausObjective.nextElement();
			ClusterDataModel nDM =
				clusterDMobjective.next();
			System.out.println("\t->" 
					+ noClau + "\n"
					+ "'\tsatisfied: '"
					+ convertIntStr(nDM.satisfied)
					+ "'\tMeasure: '"
					+ nDM.objectiveMeasure
					+ "'");
		}			
	}
	/**
	 * Fem una conversió del valor numéric al seu equivalent
	 * d'String perquè sigui més entenedor a l'hora de printar-ho.
	 * 
	 * @param number : int Type
	 * @return String Type
	 */
	private String convertIntStr(final int number) {
		final int unknown = 0;
		final int passed = 1;
		final int failed = 2;
		final int nullMap = -1;
		
		switch (number) {
		case nullMap:
			return "NULL";
		case passed:
			return "PASSED";
		case failed:
			return "FAILED";
		case unknown:
			return "UNKNOWN";
		default :
			return "[ERROR]!";			
		}
	}
	/**
	 * Aquesta funció acabarà d'omplir el ClusterTree. Això vol dir 
	 * que aquells que tenen el seu "espai" creat però que no tenen 
	 * cap objective definit sota demanda pel manifest, li crearem un 
	 * "objective virtual" que tindrà com a nom el propi item i que 
	 * enmagatzemarà les dades referents al mateix.
	 * 
	 * @param cluster : És passa per referència aquesta variable que 
	 * serà modificada i retornada.
	 */
	private void fillClusterTree(
			final Hashtable < String, ClusterMap > cluster) {
		for (Iterator it = cluster.keySet().iterator(); it.hasNext();) {
			String nStri = (String) it.next();
			if (cluster.get(nStri).objective.size() == 0) {				
				cluster.get(nStri).objective.put(nStri, new ClusterDataModel());
			}
		}		
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

