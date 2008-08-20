// $Id: DOMTreeUtility.java,v 1.2 2007/10/30 14:07:42 ecespedes Exp $
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

/*******************************************************************************
**
** Advanced Distributed Learning Co-Laboratory (ADL Co-Lab) grants you
** ("Licensee") a non-exclusive, royalty free, license to use, modify and
** redistribute this software in source and binary code form, provided that
** i) this copyright notice and license appear on all copies of the software;
** and ii) Licensee does not utilize the software in a manner which is
** disparaging to ADL Co-Lab.
**
** This software is provided "AS IS," without a warranty of any kind.  ALL
** EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
** ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
** OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED.  ADL Co-Lab AND ITS LICENSORS
** SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
** USING, MODIFYING OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES.  IN NO
** EVENT WILL ADL Co-Lab OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE,
** PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
** INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE
** THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE
** SOFTWARE, EVEN IF ADL Co-Lab HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH
** DAMAGES.
**
*******************************************************************************/

import java.util.*;

import org.w3c.dom.*;


/*** <!-- Javadoc -->
 * $Id: DOMTreeUtility.java,v 1.2 2007/10/30 14:07:42 ecespedes Exp $
 * 
 * <strong>Filename:</strong> DOMTreeUtility.java<br><br>
 *
 * <strong>Description:</strong>The DOM Tree Utility provides the ability to
 * access subsets of the DOM tree.  This class serves as a utility class for
 * DOM trees manipulation.  <br><br>
 *
 * <strong>Design Issues:</strong> None<br>
 *
 * <strong>Implementation Issues:</strong> None<br><br>
 *
 * <strong>Known Problems:</strong> None<br><br>
 *
 * <strong>Side Effects:</strong> None<br><br>
 *
 * <strong>References:</strong> None<br><br>
 *
 * @author ADL Technical Team
   @author Eduard Céspedes i Borràs / Enginyeria LaSalle / haduart@gmail.com
 * @version Versió $Revision: 1.2 $ $Date: 2007/10/30 14:07:42 $
 * $Log: DOMTreeUtility.java,v $
 * Revision 1.2  2007/10/30 14:07:42  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Començant a crear el sistema de gestió dels cursos:
 * - CourseAdministrator i CourseManager.
 *
 */

public class DOMTreeUtility {
   /**
    * This method returns the desired node which is determined by the
    * provided node name and namespace.
    *
    * @param iNode The provided node structure to be traversed.
    * @param iNodeName The name of the node being searched for. 
    *
    * @return Node The desired node.
    */
   public static Node getNode(final Node iNode, final String iNodeName) {
      Node result = null;
      
      if (iNode != null) {
         // Get the children of the current node
         NodeList children = iNode.getChildNodes();

         // If there are children, loop through the children nodes looking
         // for the appropriate node
         if (children != null) {        	 
            for (int i = 0; i < children.getLength(); i++) {
               // Get the child node
               Node currentChild = children.item(i);

               // Get the current child node's local name
               String currentChildName = currentChild.getLocalName();

               if (currentChildName != null) {
                  // Determine if the current child node is the one that
                  // is being looked for
                  if (currentChildName.equalsIgnoreCase(iNodeName)) {
                     result = currentChild;
                     break;
                  }
               }
            } // end looping of children
         }
      }
      // return the resulting vector of nodes
      return result;
   }

   /**
    * This method returns an ordered list containing the desired nodes which is
    * determined by the provided node name and namespace.
    *
    * @param iNode The provided node structure to be traversed.
    * @param iNodeName The name of the node being searched for.
    * 
    * @return An ordered list containing the desired nodes.
    */
   public static Vector getNodes(final Node iNode, final String iNodeName) {
      // Create a vector to hold the results of the method
      Vector < Node > result = new Vector < Node >();

      // Check to see if the input node is null
      if (iNode != null) {
         // Get the set of child nodes of the input node
         NodeList children = iNode.getChildNodes();

         // If there are children nodes loop through them looking for
         // the node matching the name and namespace
         if (children != null) {
            int numChildren = children.getLength();

            // Loop over the children searching for the desired node
            for (int i = 0; i < numChildren; i++) {
               // get the current child in the list
               Node currentChild = children.item(i);

               // Get the local name of the child node
               String currentChildName = currentChild.getLocalName();

               if (currentChildName != null) {
                  // Determine if the current child node is the one that
                  // is being looked for
                  if (currentChildName.equalsIgnoreCase(iNodeName)) {
                     result.add(currentChild);
                  }
               }
            }
         }
      }
      return result;
   }
   
   /** <!-- Javadoc -->
    * <code>getALLNodes()</code> Ens retornarà tots els fills d'un node, sempre
    * que aquests tinguin un format correcte (no siguin null, si siguin textos 
    * en blanc).En aquesta funció suprimim els nodes #TEXT!
    * 
    * <br /><br />
    * <b>Problemas:</b> <br/>
    * Cap, però s'ha de vigilar perquè aquesta funció no està pensada per
    * retornar nodes #TEXT ja que asumirem que no contenen res útil 
    * per nosaltres. 
    * <br />
    * <b>Última Modificació:</b><br />
    * 2007-09-27
    * 
    * @param iNode : Node Type
    * @return Vector<Node> : Contindrà tots els fills.  
    */
   public static Vector < Node > getALLNodes(final Node iNode) {
	   // Create a vector to hold the results of the method
	   Vector < Node > result = new Vector < Node >();
	   
	   // Check to see if the input node is null
	   if (iNode != null) {
		   // Get the set of child nodes of the input node
		   NodeList children = iNode.getChildNodes();
		   
		   // If there are children nodes loop through them looking for
		   // the node matching the name and namespace
		   if (children != null) {
			   int numChildren = children.getLength();
			   // Loop over the children searching for the desired node
			   for (int i = 0; i < numChildren; i++) {
				   // get the current child in the list
				   Node currentChild = children.item(i);
				   				   
				   if (currentChild != null) {
					   if ((currentChild.getNodeType() != Node.TEXT_NODE)
							   && (currentChild.getNodeType()
									   != Node.CDATA_SECTION_NODE)
							   && (currentChild.getNodeType() 
									   != Node.COMMENT_NODE)) {
						   
						   result.add(currentChild);
					   }
				   }
			   }
		   }
	   }
	   return result;
   }



   /**
    * This method gets the text node of an input node.
    *
    * @param iNode : The node that contains the desired text node
    *
    * @return The desired value of the node.
    */
   public static String getNodeValue(final Node iNode) {
      // Create a string to hold the results.
      String value = new String();

      // Check to make sure node is not null
      if (iNode != null) {
         // Get a list of the children of the input node
         NodeList children = iNode.getChildNodes();
         int numChildren = children.getLength();

         // Cycle through all children of node to get the text
         if (children != null) {
            for (int i = 0; i < numChildren; i++) {
               // make sure we have a text element
               if ((children.item(i).getNodeType() == Node.TEXT_NODE)
            		   || (children.item(i).getNodeType() 
            				   == Node.CDATA_SECTION_NODE)) {
            	   
                  value = value + children.item(i).getNodeValue().trim();
               }
            } // end looping over the children nodes
         }
      }

      // Return the value of the node.
      return value;
   }

   /**
    * This method returns the attribute of the given node whose name matches
    * the named value (iAttributeName) and a particular namespace
    * (iNamespaceForAttr).
    *
    * @param iNode The element containing the attribute
    * @param iAttributeName The name of the attribute being retrieved
    *
    * @return The attribute matching the name and namespace
    */
   public static Attr getAttribute(final Node iNode,
		   final String iAttributeName) {
	   
      Attr result = null;      

      // Determine if the node is null
      if (iNode != null) {
         // If the node is not null, then get the list of attributes from
         // the node
         NamedNodeMap attrList = iNode.getAttributes();

         int numAttr = attrList.getLength();

         Attr currentAttrNode = null;
         String currentNodeName = null;

         // Loop through the attributes and get their values assuming
         // that the multiplicity of each attribute is 1 and only 1.
         for (int k = 0; k < numAttr; k++) {
            // Get the attribute
            currentAttrNode = (Attr) attrList.item(k);

            // Get the local name of the attribute
            currentNodeName = currentAttrNode.getLocalName();

            // First check to see if the current node is the one with the
            // same Local Name as the value we are looking for (iAttributeName)
            if (currentNodeName.equalsIgnoreCase(iAttributeName)) {
               // We have found a node that shares the same name as the
               // node we are looking for (iAttributeName).
               // Matching attribute found
               result = currentAttrNode;
               break;
            }
         } // end for loop
      }

      return result;
   }
   
   /** <!-- Javadoc -->
    * <code>getALLAttribute()</code> Ens retornarà tots els atributs d'un node.
    * <br /><br />
    * <b>Problemas:</b> <br/>
    * --
    * <br />
    * <b>Última Modificació:</b><br />
    * 2007-09-27
    * 
    * @param iNode : Node Type
    * @return Vector<Attr> : Contindrà tots els atributs.  
    */
   public static Vector < Attr > getALLAttribute(final Node iNode) {
	   Vector < Attr > result = new Vector < Attr >();
	   
	   // Determine if the node is null
	   if (iNode != null) {
		   // If the node is not null, then get the list of attributes from
		   // the node
		   NamedNodeMap attrList = iNode.getAttributes();
		   
		   int numAttr = attrList.getLength();
		   
		   Attr currentAttrNode = null;
		   //String currentNodeName = null;
		   
		   // Loop through the attributes and get their values assuming
		   // that the multiplicity of each attribute is 1 and only 1.
		   for (int k = 0; k < numAttr; k++) {
			   // Get the attribute
			   currentAttrNode = (Attr) attrList.item(k);
			   
			   // Get the local name of the attribute
			   //currentNodeName = currentAttrNode.getLocalName();
			   
			   result.add(currentAttrNode);
			   
		   } // end for loop
	   }
	   return result;
   }
   

   /**
    * This method removes the specified attribute from the specified node.
    *
    * @param iNode The node whose attribute is to be removed
    * @param iAttributeName The name of the attribute to be removed
    */
   public static void removeAttribute(final Node iNode,
		   final String iAttributeName) {
	   
      NamedNodeMap attrList = iNode.getAttributes();
      attrList.removeNamedItem(iAttributeName);
   }

   /**
    * This method returns the value of the attribute that matches the
    * attribute name (iAttributeName) and namepace (iNamespaceForAttr) in
    * the node.  This is to cover cases where elements have multiple
    * attributes that have the same local name but come from different
    * namespaces.
    *
    * @param iNode The element containing the attribute
    * @param iAttributeName The name of the attribute being retrieved
    *
    * @return The value the attribute<br>
    */
   public static String getAttributeValue(final Node iNode,
		   final String iAttributeName) {
	   
      String result = "";
      // Get the attribute from the node matching the attribute name
      // and namespace
      Attr theAttribute = getAttribute(iNode, iAttributeName);

      // Make sure the attribute was present for the element
      if (theAttribute != null) {
         // If present, retrieve the value of the attribute
         result = theAttribute.getValue();
      }

      // return the value
      return result;
   }

   /**
    * This method determins if a node in the DOM Tree <code>(iNode)</code> is
    * the node we are looking for.  This is done by comparing the node's
    * local name and namespace with a given node name <code>(iNodeName)</code>
    * and namespace <code>(iNamespace)</code>.
    *
    * @param iNode The Node we are trying to determine if it is the correct
    *              node
    * @param iNodeName The name of the node we are looking for.
    * @param iNamespace The namespace of the node we are looking for.
    *
    * @return A boolean value indicating whether or not this is the
    *         correct node we are looking for
    */
   public static boolean isAppropriateElement(final Node iNode,
		   final String iNodeName, final String iNamespace) {

      boolean result = false;

      if (iNode.getNodeType() == Node.ATTRIBUTE_NODE) {
         if (iNode.getNamespaceURI() == null) {
            // Attribute has been passed in and its namepsace is null, get the
            // attributes parent's namespace
            String parentsNamespace =
            	((Attr) iNode).getOwnerElement().getNamespaceURI();
            
            if ((iNode.getLocalName().equals(iNodeName)) 
            		&& (parentsNamespace.equals(iNamespace))) {
               result = true;
            }
         } else {
            if ((iNode.getLocalName().equals(iNodeName)) 
            		&& (iNode.getNamespaceURI().equals(iNamespace))) {
               result = true;
            }
         }
      } else if ((iNode.getLocalName().equals(iNodeName)) 
    		  && (iNode.getNamespaceURI().equals(iNamespace))) {
         result = true;
      }

      return result;
   }  
}
