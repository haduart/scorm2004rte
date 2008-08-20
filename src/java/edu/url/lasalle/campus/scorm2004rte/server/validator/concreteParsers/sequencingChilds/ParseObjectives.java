// $Id: ParseObjectives.java,v 1.7 2008/01/18 18:07:24 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.validator.
	concreteParsers.sequencingChilds;

import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Node;

import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
		ObjectiveHandler;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
		Objective;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.MapInfo;

import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMTreeUtility;

/**
 * Parsejarà els Objectives, tan els primaris com els secundaris.
 * 
 * @author Eduard Céspedes i Borràs (AKA ecespedes)
 *
 */
public class ParseObjectives {
	
	/**
	 * Ens indicarà si té o no té un primaryObjective.
	 * (boolean Type, default false)
	 */
	private boolean hasPrimaryObjective = false;	
	/**
	 * Ens indicarà si té o no té un primaryObjective.
	 * (ObjectiveHandler Type, default new ObjectiveHandler())
	 */
	private ObjectiveHandler objectiveHandler = new ObjectiveHandler();

	/**
	 * L'identificador de l'Item en el que estem.
	 */
	private String itemID;
	/**
	 * Ens indicarà si té mapInfo.
	 * boolean Type.
	 * Default false.
	 */
	private boolean hasMapInfo = false;
	/**
	 * És el constructor per defecte.
	 * 
	 * @param objectivesNode : (Node Type)
	 * @param currentItemID  : (String Type)
	 */
	public ParseObjectives(
			final Node objectivesNode,
			final String currentItemID) {
		
		//		parsegem els fills...
		Vector objInObjectives = DOMTreeUtility.getALLNodes(objectivesNode);
		
		itemID = currentItemID;
		
		if (objInObjectives != null) {
			for (Iterator objectivesIterator = objInObjectives.iterator(); 
			objectivesIterator.hasNext();) {
				Node ssNode = (Node) objectivesIterator.next();
				if (ssNode == null) {
					if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) {
						System.out.println("ERROR a l'agafar " 
								+ "un fill dintre del <Objectives>!!");
					}
				} else {				
					analizeNode(ssNode);					
				}
			}
			if (!hasPrimaryObjective) {
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) {
					System.out.println("[ERROR] Fa falta " 
							+ "com a mínim un PrimaryObjective!");
				}
			}
			objectiveHandler.setLoaded();
		}		
	}
	
	/**
	 * Analitza els "fills" dels 'objectives'.
	 * 	 
	 * @param childNode : (Node Type)
	 */
	private void analizeNode(final Node childNode) {
		String childName = childNode.getLocalName();
		if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
			System.out.println("\t\t[Objectives child]: " + childName);
		}
		
		if (childName.equals(Constants.PRIMARYOBJECTIVE)) {
			if (!hasPrimaryObjective) {				
				ParseObjective parsejaPrimary = new ParseObjective(childNode);
				if (parsejaPrimary.objective != null) {
					hasPrimaryObjective = true;
					
					parsejaPrimary.objective.itemID = itemID;
									
					objectiveHandler.setPrimaryObjective( 
						parsejaPrimary.objective);					
				} else {
					if (Constants.DEBUG_WARNINGS 
							|| Constants.DEBUG_SEQUENCING) {
						System.out.println("[WARNING] Hem trobat un " 
								+ "PrimaryObjective però estava buit?!");
					}
				}				
			} else {
				if (Constants.DEBUG_ERRORS || Constants.DEBUG_SEQUENCING) {
					System.out.println("[ERROR] No pot ser que tinguem dos "
							+ "primaryObjectives dintre "
							+ "d'un mateix <objectives>");
				}
			}
			
		} else if (childName.equals(Constants.OBJECTIVE)) {
			ParseObjective parsejaObjectiu = 
				new ParseObjective(childNode);
			
			if (parsejaObjectiu.objective != null) {	
				parsejaObjectiu.objective.itemID = itemID;
				objectiveHandler.addObjective(
						parsejaObjectiu.objective.ObjectiveID,
						parsejaObjectiu.objective);
				
			} else {
				if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING) {
					System.out.println("[WARNING] Hem trobat un Objective "
							+ "però estava buit?!");
				}
			}
		} else {
			if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING) {
				System.out.println("\t[WARNING] El Node '"
						+ childName + "' no l'estem controlant.");
			}
		}		
		
	}	
	
	/**
	 * Retorna el controlador dels objectius.
	 * 
	 * @return ObjectiveHandler
	 */
	public final ObjectiveHandler getObjectiveHandler() {
		return objectiveHandler;
	}
	/**
	 * Ens indicarà si té mapInfo aquest objectiu o no.
	 * 
	 * @return boolean Type.
	 */
	public final boolean getHasMapInfo() {
		return hasMapInfo;
	}

	/**
	 * Classe privada que parsejarà els Objective concrets que 
	 * ens anem trobant en el procés de parsejat del imssmanifest.xml.
	 * 
	 * @author Eduard Céspedes i Borràs (AKA ecespedes)
	 *
	 */
	private class ParseObjective {
		/**
		 * Aquesta serà la variable que "retornarem".
		 * Objective Type.
		 */
		public Objective objective = new Objective();
		
		/**
		 * Constant String Type.
		 * "objectiveID"
		 */
		private static final String OBJECTIVEID = "objectiveID";
		/**
		 * Constant String Type.
		 * "satisfiedByMeasure"
		 */
		private static final String SATISFIEDBYMEASURE = "satisfiedByMeasure";
		
		/**
		 * Constant String Type.
		 * "minNormalizedMeasure"
		 */
		private static final String MINNORMALIZEDMEASURE =
			"minNormalizedMeasure";
		/**
		 * Constant String Type.
		 * "mapInfo"
		 */
		private static final String MAPINFO = "mapInfo";
		/**
		 * Constant String Type.
		 * "targetObjectiveID"
		 */
		private static final String TARGETOBJECTIVEID = "targetObjectiveID";
		/**
		 * Constant String Type.
		 * "readSatisfiedStatus"
		 */
		private static final String READSATISFIEDSTATUS = "readSatisfiedStatus";
		/**
		 * Constant String Type.
		 * "readNormalizedMeasure"
		 */
		private static final String READNORMALIZEDMEASURE =
			"readNormalizedMeasure";
		/**
		 * Constant String Type.
		 * "writeSatisfiedStatus"
		 */
		private static final String WRITESATISFIEDSTATUS =
			"writeSatisfiedStatus";
		/**
		 * Constant String Type.
		 * "writeNormalizedMeasure"
		 */
		private static final String WRITENORMALIZEDMEASURE =
			"writeNormalizedMeasure";
		
		/**
		 * Constructor de la classe privada.
		 * 
		 * @param nodeObjective : Node Type
		 */
		public ParseObjective(final Node nodeObjective) {
			//			parsegem els atributs, si és que en té. 
			objective.ObjectiveID = 
				DOMTreeUtility.getAttributeValue(nodeObjective, OBJECTIVEID);
			if (objective.ObjectiveID.length() == 0) {				
				if (Constants.DEBUG_WARNINGS_LOW 
						&& Constants.DEBUG_SEQUENCING) {
					System.out.println("Atribut ObjectiveID no trobat.");
				}
			}
			String satisfiedByMeasure = 
				DOMTreeUtility.getAttributeValue(
						nodeObjective, SATISFIEDBYMEASURE);
			
			if (objective.ObjectiveID.length() == 0) {				
				if (Constants.DEBUG_WARNINGS_LOW 
						&& Constants.DEBUG_SEQUENCING) {					
					System.out.println("Atribut satisfiedByMeasure no trobat.");
				}
			} else {
				objective.satisfiedByMeasure =
					satisfiedByMeasure.equals(Constants.FALSE) ? false : true;
			}
			
			//			parsegem els fills...
			Vector elementsInObjective =
				DOMTreeUtility.getALLNodes(nodeObjective);
			
			if (elementsInObjective != null) {
				for (Iterator objectiveElementIterator =
					elementsInObjective.iterator();
					objectiveElementIterator.hasNext();) {
					
					Node ssNode = (Node) objectiveElementIterator.next();
					if (ssNode == null) {
						if (Constants.DEBUG_ERRORS 
								|| Constants.DEBUG_SEQUENCING) {
							System.out.println("[ERROR] a l'agafar un fill "
									+ "dintre del <Objectives>!!");
						}
					} else {				
						analizeObjectiveChild(ssNode);
					}
				}
			}			
		}
		/**
		 * Funció privada que analitza el valor concret dels paràmetres
		 * del Objective, de manera que va creat l'objecte Objective
		 * assignant-li els valors a les seves variables.
		 * 
		 * @param childNode : Note Type
		 */
		private void analizeObjectiveChild(final Node childNode) {
			String childName = childNode.getLocalName();
			
			if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
				System.out.println("\t\t\t\t[Objective child]: " + childName);
			}
			
			if (childName.equals(MINNORMALIZEDMEASURE)) {
				String minNMValor = DOMTreeUtility.getNodeValue(childNode);
				if (minNMValor.length() == 0) {
					if (Constants.DEBUG_WARNINGS_LOW 
							&& Constants.DEBUG_SEQUENCING) {
						System.out.println(
								"Atribut minNormalizedMeasure no trobat.");
					}
				}
				
				objective.minNormalizedMeasure =
					new Double(minNMValor).doubleValue();
				
				if (Constants.DEBUG_INFO || Constants.DEBUG_SEQUENCING) {
					System.out.println("\t\t\t\t\tValue: " 
							+ objective.minNormalizedMeasure);
				}
			} else if (childName.equals(MAPINFO)) {
				MapInfo nouMapInfo = new MapInfo();
				hasMapInfo = true;
				
				//	parsegem els atributs...
				Vector attrInMapInfo =
					DOMTreeUtility.getALLAttribute(childNode);
				
				if (attrInMapInfo != null) {
					for (Iterator attrMapInfoIterator =
						attrInMapInfo.iterator();
						attrMapInfoIterator.hasNext();) {
						
						Node ssNode = (Node) attrMapInfoIterator.next();
						if (ssNode == null) {
							if (Constants.DEBUG_ERRORS 
									|| Constants.DEBUG_SEQUENCING) {
								System.out.println("[ERROR] a l'agafar"
										+ " un fill dintre del <Objectives>!!");
							}
						} else {
							if (Constants.DEBUG_INFO 
									|| Constants.DEBUG_SEQUENCING) {
								System.out.println("\t\t\t\t\t"
										+ ssNode.getNodeName()
										+ " " + ssNode.getNodeValue());
							}
							
							if (ssNode.getNodeName().
									equals(TARGETOBJECTIVEID)) {
								nouMapInfo.targetObjectiveID = 
									ssNode.getNodeValue();
							} else if (ssNode.getNodeName().
									equals(READSATISFIEDSTATUS)) {
								nouMapInfo.readSatisfiedStatus =
									ssNode.getNodeValue().
									equals(Constants.FALSE) ? false : true;
							} else if (ssNode.getNodeName().
									equals(READNORMALIZEDMEASURE)) {
								nouMapInfo.readNormalizedMeasure =
									ssNode.getNodeValue().
									equals(Constants.FALSE) ? false : true;
							} else if (ssNode.getNodeName().
									equals(WRITESATISFIEDSTATUS)) {
								nouMapInfo.writeSatisfiedStatus =
									ssNode.getNodeValue().
									equals(Constants.FALSE) ? false : true;
							} else if (ssNode.getNodeName().
									equals(WRITENORMALIZEDMEASURE)) {
								nouMapInfo.writeNormalizedMeasure =
									ssNode.getNodeValue().
									equals(Constants.FALSE) ? false : true;
							} else {
								if (Constants.DEBUG_WARNINGS 
										|| Constants.DEBUG_SEQUENCING) {
									
									System.out.println("\t[WARNING] El Node '"
											+ childName 
											+ "' no l'estem controlant.");
								}
							}	
							
						}
					}
				}
				objective.mapInfo = nouMapInfo;
				objective.hasMapInfo = true;
			} else {
				if (Constants.DEBUG_WARNINGS || Constants.DEBUG_SEQUENCING) {
					System.out.println("\t[WARNING] El Node '"
							+ childName 
							+ "' no l'estem controlant.");
				}
			}			
		}
	}
}

