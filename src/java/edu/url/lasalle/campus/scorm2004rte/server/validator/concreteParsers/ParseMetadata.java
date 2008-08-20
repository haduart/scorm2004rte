// $Id: ParseMetadata.java,v 1.2 2008/01/31 14:23:37 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.validator.concreteParsers;

import java.io.File;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import edu.url.lasalle.campus.scorm2004rte.server.
	ActivityTree.Elements.MetadataInformation;
import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.server.
	validator.DOMTreeUtility;
import edu.url.lasalle.campus.scorm2004rte.server.
	validator.DOMValidate;

/**
* $Id: ParseMetadata.java,v 1.2 2008/01/31 14:23:37 ecespedes Exp $
* <b>Títol:</b> ParseMetadata<br><br>
* <b>Descripció:</b> Aquesta classe s'encarrega de parsejar la metadata<br>
* del imssmanifest.xml. <br><br> 
*
* @author Eduard Céspedes i Borràs/Enginyeria La Salle/ecespedes@salle.url.edu
* @version Versió $Revision: 1.2 $ $Date: 2008/01/31 14:23:37 $
* $Log: ParseMetadata.java,v $
* Revision 1.2  2008/01/31 14:23:37  ecespedes
* Millorant el sistema.
*
*/
public class ParseMetadata {
	/**
	 * Objecte MetadataInformation que anirem omplint mentres
	 * parsejem.
	 */
	private MetadataInformation metadata = new MetadataInformation();
		
	/**
	 * Constructor del parsejador del Metadata.
	 * 
	 * @param newMetadata : Node Type.
	 * @param homePath : String Type.
	 * @param nouMetadataInformation : MetadataInformation.
	 */
	public ParseMetadata(
			final Node newMetadata, 
			final String homePath, 
			final MetadataInformation nouMetadataInformation) {
		
		metadata = nouMetadataInformation;
		Node lom = null;
		
		
		/**
		 * Mirem si és local o global
		 */
		Node location = DOMTreeUtility.getNode(newMetadata, "location");
		
		if (location != null) {
			if (Constants.DEBUG_INFO || Constants.DEBUG_METADATA) {
				System.out.println("[INFO] La Metadata és externa.");
			}
			
			String homeExtension = DOMTreeUtility.getNodeValue(location);
			if (homeExtension.length() == 0) {
				if (Constants.DEBUG_WARNINGS || Constants.DEBUG_METADATA) {
					System.out.println("[WARNING] El camp location està buit.");
				}
			} else {
				String completePath = homePath + homeExtension;
				boolean founded = testMetadataFile(completePath);
				if (!founded) {
					if (Constants.DEBUG_ERRORS || Constants.DEBUG_METADATA) {
						System.out.println("[ERROR] El fitxer '"
								+ completePath + "' no existeix!");
					}
					System.exit(-1);
				} else {
					if (Constants.DEBUG_INFO || Constants.DEBUG_METADATA) {
						System.out.println("[INFO] Trobat fitxer '"
								+ completePath + "'.[CORRECTE]");
					}
					
					DOMValidate validator = new DOMValidate(completePath);		
					if (validator.isValidated()) {
						Document document = validator.getDocument();
						
						lom = DOMTreeUtility.getNode(document, "lom");
						
					} else {
						if (Constants.DEBUG_ERRORS 
								|| Constants.DEBUG_METADATA) {
							System.out.println("[ERROR isValidated()] " 
									+ "El fitxer '"
									+ completePath 
									+ "' no s'ha validat correctament!!");
						}
					}
				}
			}			
		} else {
			if (Constants.DEBUG_INFO || Constants.DEBUG_METADATA) {
				System.out.println("[INFO] La Metadata NO és externa.");
			}
			
			lom = DOMTreeUtility.getNode(newMetadata, "lom");			
		}		
		
		if (lom == null) {
			if (Constants.DEBUG_ERRORS 
					|| Constants.DEBUG_METADATA) {
				System.out.println("[ERROR] Error inesperat,"
						+ " no hem trobat l'element <lom>.");
			}
		} else {
			Node general = DOMTreeUtility.getNode(lom, "general");
			if (general == null) {
				if (Constants.DEBUG_ERRORS 
						|| Constants.DEBUG_METADATA) {
					System.out.println("[ERROR] Error inesperat,"
							+ " no hem trobat l'element <general>.");
				}
			}
			generalInformation(general);
			
			Node technical = DOMTreeUtility.getNode(lom, "technical");
			if (technical == null) {
				if (Constants.DEBUG_ERRORS 
						|| Constants.DEBUG_METADATA) {
					System.out.println("[ERROR] Error inesperat, "
							+ "no hem trobat l'element <technical>.");
				}
			}
			technicalInformation(technical);
			
			Node educational = DOMTreeUtility.getNode(lom, "educational");
			if (educational == null) {
				if (Constants.DEBUG_ERRORS 
						|| Constants.DEBUG_METADATA) {
					System.out.println("[ERROR] Error "
							+ "inesperat, no hem trobat l'element"
							+ " <educational>.");
				}
			}
			educationalInformation(educational);
			
			Node lifeCycle = DOMTreeUtility.getNode(lom, "lifeCycle");
			if (lifeCycle == null) {
				if (Constants.DEBUG_ERRORS 
						|| Constants.DEBUG_METADATA) {
					System.out.println("[ERROR] Error inesperat, "
							+ "no hem trobat l'element <lifeCycle>.");
				}
			}
			lifeCycleInformation(lifeCycle);
			
			Node rights = DOMTreeUtility.getNode(lom, "rights");
			if (rights == null) {
				if (Constants.DEBUG_ERRORS 
						|| Constants.DEBUG_METADATA) {
					System.out.println("[ERROR] Error inesperat"
							+ ", no hem trobat l'element <rights>.");
				}
			}
			rightsInformation(rights);
		}							
		
	}
	/**
	 * Agafem els valors de "generalInformation".
	 * 
	 * @param node : Node Type
	 */	
	private void generalInformation(final Node node) {
		
		Node title = DOMTreeUtility.getNode(node, "title");
		if (title == null) {
			if (Constants.DEBUG_WARNINGS_LOW 
					&& Constants.DEBUG_METADATA) {
				System.out.println("[WARNING_LOW] No hem "
						+ "trobat l'element <title>.");
			}
		} else {
			String nouTitle = takeStringInformation(title, "string");
			if (nouTitle.length() > 0) {
				metadata.title = nouTitle;
				if (Constants.DEBUG_INFO 
						|| Constants.DEBUG_METADATA) {
					System.out.println("[INFO] Metadata title:\t"
							+ metadata.title);
				}
			} //DEL if (nouTitle.length() > 0)
		} //DEL if (title == null)
		
		Node description = 
			DOMTreeUtility.getNode(node, "description");
		if (description == null) {
			if (Constants.DEBUG_WARNINGS_LOW 
					&& Constants.DEBUG_METADATA) {
				System.out.println("[WARNING_LOW] No hem "
						+ "trobat l'element <description>.");
			}
		} else {
			String nouDescription = 
				takeStringInformation(description, "string");
			
			if (nouDescription.length() > 0) {
				metadata.description = nouDescription;
				if (Constants.DEBUG_INFO 
						|| Constants.DEBUG_METADATA) {
					System.out.println("[INFO] Metadata Description:\t" 
							+ metadata.description);
				}
			} //DEL if (nouDescription.length() > 0)
		} //DEL if (description == null)
	}	
	/**
	 * Agafem els valors de "technicalInformation".
	 * 
	 * @param node : Node Type
	 */
	private void technicalInformation(final Node node) {
		Node requirement = DOMTreeUtility.getNode(node, "requirement");
		String nouRequirement = new String("");
		
		if (requirement == null) {
			if (Constants.DEBUG_WARNINGS_LOW 
					&& Constants.DEBUG_METADATA) {
				System.out.println("[WARNING_LOW] No hem trobat "
						+ "l'element <requirement>.");
			}
		} else {
			Vector orComposite = 
				DOMTreeUtility.getNodes(requirement, "orComposite");
			
			for (Iterator orComposIterator = orComposite.iterator(); 
					orComposIterator.hasNext();) {
				
				Node nouOrComposite = (Node) orComposIterator.next();
				Node name = DOMTreeUtility.getNode(nouOrComposite, "name");
				nouRequirement = nouRequirement 
				+ takeStringInformation(name, "value");
				
				Node minimumVersion = 
					DOMTreeUtility.getNode(nouOrComposite, "minimumVersion");
				if (minimumVersion == null) {
					if (Constants.DEBUG_WARNINGS_LOW 
							&& Constants.DEBUG_METADATA) {
						System.out.println("[WARNING_LOW] No hem "
								+ "trobat l'element <minimumVersion>.");
					}
				} else {
					String minimumVersionString =
						DOMTreeUtility.getNodeValue(minimumVersion);
					if (minimumVersionString.length() > 0) {
						nouRequirement = nouRequirement 
						+ " " + minimumVersionString; 
					}
				} //DEL if (minimumVersion == null)
				
				Node maximumVersion =
					DOMTreeUtility.getNode(nouOrComposite, "maximumVersion");
				if (maximumVersion == null) {
					if (Constants.DEBUG_WARNINGS_LOW 
							&& Constants.DEBUG_METADATA) {
						System.out.println("[WARNING_LOW] No hem trobat"
								+ " l'element <maximumVersion>.");
					}
				} else {
					String maximumVersionString = 
						DOMTreeUtility.getNodeValue(maximumVersion);
					if (maximumVersionString.length() > 0) {
						nouRequirement = 
							nouRequirement + "-" + maximumVersionString; 
					}
				} //DEL if (maximumVersion == null)
				nouRequirement = nouRequirement + ", ";
			} //DEL for (Iterator orComposIterator = orComposite.iterator()
			
			Node otherPlatformRequirements =
				DOMTreeUtility.getNode(node, "otherPlatformRequirements");
			if (otherPlatformRequirements == null) {
				if (Constants.DEBUG_WARNINGS_LOW 
						&& Constants.DEBUG_METADATA) {
					System.out.println("[WARNING_LOW] " 
							+ "No hem trobat l'element "
							+ "<otherPlatformRequirements>.");
				}
			} else {
				String nouOtherPlatformRequirements =
					takeStringInformation(
							otherPlatformRequirements, "string");
				if (nouOtherPlatformRequirements.length() > 0) {
					nouRequirement = 
						nouRequirement + "\n" 
						+ nouOtherPlatformRequirements;
				}				
			} //DEL if (otherPlatformRequirements == null)
		}
		
		metadata.requirement = nouRequirement;
		
		if (Constants.DEBUG_INFO || Constants.DEBUG_METADATA) {
			System.out.println("[INFO] Metadata requirement:\t" 
					+ metadata.requirement);
		}		
	}
	/**
	 * Agafem els valors de "rightsInformation".
	 * 
	 * @param node : Node Type
	 */
	private void rightsInformation(final Node node) {
		//		mirar si es "yes" o "no"
		Node copyrightAndOtherRestrictions = 
			DOMTreeUtility.getNode(
					node, "copyrightAndOtherRestrictions");
		if (copyrightAndOtherRestrictions == null) {
			if (Constants.DEBUG_WARNINGS_LOW 
					&& Constants.DEBUG_METADATA) {
				System.out.println("[WARNING_LOW] No hem trobat l'element" 
						+ " <copyrightAndOtherRestrictions>.");
			}
		} else {
			Node description = DOMTreeUtility.getNode(node, "description");
			if (description == null) {
				if (Constants.DEBUG_WARNINGS_LOW 
						&& Constants.DEBUG_METADATA) {
					System.out.println("[WARNING_LOW] " 
							+ "No hem trobat l'element <description>.");
				}
			} else {
				String nouDescription = 
					takeStringInformation(description, "string");
				if (nouDescription.length() > 0) {
					metadata.copyright = nouDescription;
				}
			}				
		}
		
		if (Constants.DEBUG_INFO || Constants.DEBUG_METADATA) {
			System.out.println("[INFO] Metadata rights:\t" 
					+ metadata.copyright);
		}
	}
	/**
	 * Agafem els valors de "educationalInformation".
	 * 
	 * @param node : Node Type
	 */
	private void educationalInformation(final Node node) {
		Node intendedEndUserRole = 
			DOMTreeUtility.getNode(node, "intendedEndUserRole");
		
		if (intendedEndUserRole == null) {
			if (Constants.DEBUG_WARNINGS_LOW 
					&& Constants.DEBUG_METADATA) {
				System.out.println("[WARNING_LOW] " 
						+ "No hem trobat l'element <intendedEndUserRole>.");
			}
		} else {
			
			String nouIntendedEndUserRole =
				takeStringInformation(intendedEndUserRole, "value");
			if (nouIntendedEndUserRole.length() > 0) {
				metadata.intendedEndUserRole = nouIntendedEndUserRole;
				
				if (Constants.DEBUG_INFO || Constants.DEBUG_METADATA) {
					System.out.println("[INFO] Metadata " 
							+ "intendedEndUserRole:\t" 
							+ metadata.intendedEndUserRole);
				}
			}
		}
		
		Node context = DOMTreeUtility.getNode(node, "context");
		if (context == null) {
			if (Constants.DEBUG_WARNINGS_LOW 
					&& Constants.DEBUG_METADATA) {
				System.out.println("[WARNING_LOW] "
						+ "No hem trobat l'element <context>.");
			}
		} else {
			String nouContext = takeStringInformation(context, "value");
			if (nouContext.length() > 0) {
				metadata.educationalContext = nouContext;
				if (Constants.DEBUG_INFO 
						|| Constants.DEBUG_METADATA) {
					System.out.println("[INFO] " 
							+ "Metadata educationalContext:\t" 
							+ metadata.educationalContext);
				}
			}
		}
		
		Node typicalLearningTime = 
			DOMTreeUtility.getNode(node, "typicalLearningTime");
		if (typicalLearningTime == null) {
			
			if (Constants.DEBUG_WARNINGS_LOW 
					&& Constants.DEBUG_METADATA) {
				System.out.println("[WARNING_LOW] " 
						+ "No hem trobat l'element <typicalLearningTime>.");
			}
		} else {
			
			String nouTypicalLearningTime =
				takeStringInformation(typicalLearningTime, "duration");
			
			if (nouTypicalLearningTime.length() > 0) {
				metadata.typicalLearningTime = nouTypicalLearningTime;
				if (Constants.DEBUG_INFO 
						|| Constants.DEBUG_METADATA) {
					System.out.println("[INFO] Metadata typicalLearningTime:\t" 
							+ metadata.typicalLearningTime);
				}
			}
		}
		
		Node language = DOMTreeUtility.getNode(node, "language");
		if (language == null) {
			if (Constants.DEBUG_WARNINGS_LOW 
					&& Constants.DEBUG_METADATA) {
				System.out.println("[WARNING_LOW] " 
						+ "No hem trobat l'element <language>.");
			}
		} else {
			String nouLanguage = takeStringInformation(language, "string");
			if (nouLanguage.length() > 0) {
				metadata.educationalLanguage = nouLanguage;
				if (Constants.DEBUG_INFO 
						|| Constants.DEBUG_METADATA) {
					System.out.println("[INFO] Metadata " 
							+ "educationalLanguage:\t" 
							+ metadata.educationalLanguage);
				}
			} //DEL if (nouLanguage.length() > 0)
		}		
	}
	/**
	 * Treu l'informació sobre el "lifeCycleInformation".
	 * 
	 * @param node : Node Type.
	 */
	private void lifeCycleInformation(final Node node) {
		Node contribute = DOMTreeUtility.getNode(node, "contribute");
		if (contribute == null) {
			if (Constants.DEBUG_WARNINGS_LOW 
					&& Constants.DEBUG_METADATA) {
				System.out.println("[WARNING_LOW] " 
						+ "No hem trobat l'element <contribute>.");
			}
		} else {
			
			String entityString = takeStringInformation(contribute, "entity");	
			if (entityString.length() > 0) {
				String authorString = new String("");
				boolean passedFN = false;
				
				StringTokenizer authorStringTokenizer =
					new StringTokenizer(entityString, "\r\n");
				
		    	while (authorStringTokenizer.hasMoreTokens()) {
		    		StringTokenizer st = 
		    			new StringTokenizer(
		    					authorStringTokenizer.nextToken(), ":");
					while (st.hasMoreTokens()) {
						String currentToken = st.nextToken();
						if (passedFN) {
							if (authorString.length() == 0) {
								authorString  = authorString + currentToken;
							} else {
								authorString  = authorString 
									+ "\n" + currentToken;
							}							
							passedFN = false;
						} else  {
							if (currentToken.equals("FN") 
									|| currentToken.equals("\r\nFN")) {
								passedFN = true;
							}							
						}
					}
		    	}
		    	metadata.author = authorString;
		    	
		    	if (Constants.DEBUG_INFO || Constants.DEBUG_METADATA) {
					System.out.println("[INFO] Metadata Author:\t" 
							+ metadata.author);
		    	}
		    	
			}
		}
	}
	/**
	 * Agafa l'informació en format de caràcters.
	 * 
	 * @param node : Node Type.
	 * @param displaySeparator : String Type.
	 * @return String Type.
	 */
	private String takeStringInformation(
			final Node node, final String displaySeparator) {
		
		String requestString = null;
		requestString = DOMTreeUtility.getNodeValue(node);		
		if (requestString.length() == 0) {
			Vector llistatNodes = 
				DOMTreeUtility.getNodes(node, displaySeparator);
			
			boolean firstRead = true;
			for (Iterator nodeIterator = llistatNodes.iterator(); 
					nodeIterator.hasNext();) {				
				Node stringNode = (Node) nodeIterator.next();
				if (firstRead) {
					requestString = DOMTreeUtility.getNodeValue(stringNode);
					firstRead  = false;
				} else {
					String nouString = DOMTreeUtility.getNodeValue(stringNode);
					requestString = requestString + "\n" + nouString;
				} //DEL if (firstRead)
			} //DEL for (...llistatNodes.iterator()...)
		} //DEL if (requestString.length() == 0)
		return requestString;
	}		
	/**
	 * Comprova si hi ha una metadata externa.
	 * 
	 * @param completePath : String Type.
	 * @return boolean Type.
	 */
	private boolean testMetadataFile(
			final String completePath) {
		boolean error;		
		File pathFile = new File(completePath);
		error = pathFile.exists();		
		return error;
	}
	/**
	 * Retorna el objecte MetadataInformation.
	 * 
	 * @return MetadataInformation Type.
	 */
	public final MetadataInformation getMetadata() {
		return metadata;
	}
}
