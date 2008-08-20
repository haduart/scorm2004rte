// $Id: NavTree.java,v 1.1 2008/01/31 10:11:38 msegarra Exp $
/*
 * Copyright (c) 2007 Enginyeria La Salle. Universitat Ramon Llull.
 * This file is part of SCORM2004RTE.
 *
 * SCORM2004RTE is free software; you can redistribute it and/or modify
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
 * MA 02110-1301, USA.
 */

package edu.url.lasalle.campus.scorm2004rte.server.jsp;

import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.TreeAnnotations;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.CMIDataModel;
import edu.url.lasalle.campus.scorm2004rte.system.Initialization;
import edu.url.lasalle.campus.scorm2004rte.system.CourseAdministrator;
import edu.url.lasalle.campus.scorm2004rte.util.exception.NotFoundPropertiesException;
import edu.url.lasalle.campus.scorm2004rte.util.exception.NavTreeException;
import edu.url.lasalle.campus.scorm2004rte.util.resource.CollectionProperties;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;

/**
 * $Id: NavTree.java,v 1.1 2008/01/31 10:11:38 msegarra Exp $
 *
 * La classe t� com a prop�sit crear un arbre din�mic mitjan�ant la llibreria
 * GPL dhtmlxTree i gestionar-lo per al seu �s com a arbre de navegaci� SCORM.
 * A partir de les dades de l'objecte TreeAnnotations que emmagatzemma els 
 * estats dels �tems (visible?, accessible?, s'ha accedit?, etc.) de l'arbre 
 * d'activitats es crea l'arbre de navegaci� que permetr� a l'usuari navegar 
 * pels continguts d'aprenentatge.
 *
 * @author Marc Segarra / Enginyeria La Salle / msegarra@salle.url.edu
 * @version 1.0 $Revision: 1.1 $ $Date: 2008/01/31 10:11:38 $
 * $Log: NavTree.java,v $
 * Revision 1.1  2008/01/31 10:11:38  msegarra
 * Primera implementaci�.
 *
 *
 */
public class NavTree {
    /**
     * StringBuffer amb les sent�ncies javascript per crear i gestionar 
     * l'arbre DHTML en el JSP.
     */
    private StringBuffer dhtmlTree;
    
    /**
     * String amb el nom del fitxer de la icona de l'actual �tem seleccionat.
     */
    private String selectedItemIcon;
    
    /**
     * String amb el nom del fitxer de la icona dels �tems desactivats, 
     * accedits i completats.
     */
    private String disabledCompletedItemIcon;
    
    /**
     * String amb el nom del fitxer de la icona dels �tems desactivats, 
     * accedits i incomplerts.
     */
    private String disabledIncompletedItemIcon;
    
    /**
     * String amb el nom del fitxer de la icona dels �tems desactivats i 
     * no accedits.
     */
    private String disabledItemIcon;
    
    /**
     * String amb el nom del fitxer de la icona dels �tems activats, accedits 
     * i completats.
     */
    private String enabledCompletedItemIcon;
    
    /**
     * String amb el nom del fitxer de la icona dels �tems activats, accedits 
     * i incomplerts.
     */
    private String enabledIncompletedItemIcon;
    
    /**
     * String amb els estils de l'�tem seleccionat.
     */
    private String selectedItemStyle;
    
    /**
     * String amb els estils dels �tems desactivats.
     */
    private String disabledItemStyle;
    
    /**
     * String amb els estils dels �tems activats i accedits.
     */
    private String enabledAccessedItemStyle;
    
    
    /**
     * Constructor per defecte.
     * Inicialitza els atributs de la classe.
     */
    public NavTree() throws NavTreeException {
       // inicialitza el buffer
       dhtmlTree = new StringBuffer();
       
       try {
           // objecte per accedir al fitxer de configuraci� properties
           // de l'aplicaci�
           CollectionProperties cp = new CollectionProperties();
           
           // consulta les icones i els estils dels �tems
           selectedItemIcon = cp.getPropiedad("selecteditem.icon");
           selectedItemStyle = cp.getPropiedad("selecteditem.style");
           disabledCompletedItemIcon = 
               cp.getPropiedad("disabledcompleteditem.icon");
           disabledIncompletedItemIcon = 
               cp.getPropiedad("disabledincompleteditem.icon");
           disabledItemIcon = cp.getPropiedad("disableditem.icon");
           disabledItemStyle = cp.getPropiedad("disableditem.style");
           enabledCompletedItemIcon = 
               cp.getPropiedad("enabledcompleteditem.icon");
           enabledIncompletedItemIcon = 
               cp.getPropiedad("enabledincompleteditem.icon");
           enabledAccessedItemStyle = 
               cp.getPropiedad("enabledaccesseditem.style");
       } catch (NotFoundPropertiesException nfpex) {
           LogControl logControl = LogControl.getInstance();
           logControl.writeMessage(Level.WARNING, 
                                   NavTree.class.getName(), 
                                   nfpex.getMessage());
       } catch (Exception ex) {
           throw new NavTreeException(ex.getMessage());
       }   
    }

    /**
     * Crea l'arbre din�mic i l'inicialitza.
     * @param layerId String amb l'identificador de la capa "DIV" d'un 
     * JSP on es mostrar� l'arbre.
     * @param path String amb la ruta de la ubicaci� de les imatges de 
     * les icones.
     */
    public final void initTree(final String layerId, final String path) {
        // crea i inicialitza l'arbre dhtmlxTree
        dhtmlTree.append("tree = new dhtmlXTreeObject(document." 
                + "getElementById('" + layerId + "'), '100%', '80%', 0);\n");
        dhtmlTree.append("tree.setImagePath('" + path + "');\n");
        dhtmlTree.append("tree.enableCheckBoxes(false);\n");
        dhtmlTree.append("tree.setOnClickHandler(function(id){" 
                + "launchSelectedContent(id);});\n");
    }
    
    /**
     * Crea la jerarquia de nodes de l'arbre din�mic a partir de l'objecte 
     * treeAnnotations de l'usuari.
     * @param data UserObjective amb les dades de l'usuari per a una 
     * determinada organitzaci� d'un curs.
     */
    public final void loadTree(final UserObjective data) 
            throws NavTreeException {
        try {
            // consulta les dades del primer �tem del treeAnnotations
            // que ser� l'�tem pare - l'organitzaci�
            Map.Entry e = (Map.Entry) data.treeAnnotations.entrySet().
                iterator().next();
            int[] num = {0};
            // crea la jerarquia de nodes de l'arbre
            createTree(data.treeAnnotations, data.dataModel, 0, e.getKey().
                    toString(), data.currentItem, num);     
        } catch (Exception ex) {
            throw new NavTreeException(ex.getMessage());
        }        
    }   
    
    /**
     * Elimina la jerarquia dels nodes de l'arbre din�mic.
     */
    public final void deleteTree() {
        // buida el buffer
        dhtmlTree.delete(0, dhtmlTree.length());
        // esborra els nodes de l'arbre de navegaci�
        dhtmlTree.append("tree.deleteChildItems(0);\n");
    }
    
    /**
     * M�tode recursiu que crea la jerarquia de l'arbre din�mic.
     * @param annotations Objecte TreeAnnotations amb les anotacions dels 
     * estats dels �tems de l'arbre d'activitats d'un usuari d'una 
     * organitzaci� en concret.
     * @param dataModel Objecte CMIDataModel amb el model de dades CMI.
     * @param fatherId Integer identificador del node pare de l'arbre dhtml.
     * @param itemId String identificador de l'�tem de l'arbre d'activitats a 
     * tractar.
     * @param currentItem String amb l'identificador de l'�tem actual 
     * seleccionat.
     * @param nodeId Integer identificador del node a crear de l'arbre dhtml.
     */
    private void createTree(final Map <String, TreeAnnotations> annotations, 
            final Map <String, CMIDataModel> dataModel,
            final int fatherId, final String itemId, final String currentItem, 
            final int[] nodeId) {
         
        TreeAnnotations item = annotations.get(itemId);
        if (item.currentView != TreeAnnotations.viewType.notVisible) {
            LinkedList sonsList = item.sons;
            nodeId[0]++;
            int idPare = nodeId[0];
            // si l'�tem del treeAnnotations t� fills, crea el node pare i 
            // invoca la recursivitat
            if (sonsList.size() > 0) {
                dhtmlTree.append("tree.insertNewChild(" + fatherId + ", " 
                + nodeId[0] + ", '" + item.title + "', 0, 0, 0, 0, 'CHILD');\n");
                for (int i = 0; i < sonsList.size(); i++) {
                    createTree(annotations, dataModel, idPare, (String) sonsList.get(i), 
                            currentItem, nodeId);
                }
            } else { // si �s un �tem fulla, crea el node fulla amb l'estil 
                     // corresponent a l'estat (accedit, invisible...) de l'�tem
                dhtmlTree.append("tree.insertNewChild(" + fatherId + ", " 
                  + nodeId[0] + ", '" + item.title + "', 0, 0, 0, 0, '');\n");
                
                if (currentItem != null && currentItem.equals(itemId)) {
                    // pinta la icona del node seleccionat actualment
                    dhtmlTree.append("tree.setItemImage2(" + nodeId[0] 
                      + ", '" + selectedItemIcon + "'" 
                      + ", '" + selectedItemIcon + "'" 
                      + ", '" + selectedItemIcon + "');\n");
                    dhtmlTree.append("tree.setItemStyle(" + nodeId[0] 
                      + ", '" + selectedItemStyle + "');\n");
                    dhtmlTree.append("tree.focusItem(" + nodeId[0] + ");\n");
                } else {
                    if (item.currentView == TreeAnnotations.viewType.disabled) {
                        if (item.isAccessed) {
                            if (dataModel.get(itemId).completionStatus.equals("completed")) {
                                // pinta el node desactivat per� que ha estat 
                                // accedit i completat amb �xit
                                dhtmlTree.append("tree.setItemImage2(" + nodeId[0] 
                                  + ", '" + disabledCompletedItemIcon + "'" 
                                  + ", '" + disabledCompletedItemIcon + "'" 
                                  + ", '" + disabledCompletedItemIcon + "');\n");
                            } else {
                                // pinta el node desactivat per� que ha estat 
                                // accedit i no completat
                                dhtmlTree.append("tree.setItemImage2(" + nodeId[0] 
                                  + ", '" + disabledIncompletedItemIcon + "'" 
                                  + ", '" + disabledIncompletedItemIcon + "'" 
                                  + ", '" + disabledIncompletedItemIcon + "');\n");
                            }
                        } else {
                            // pinta el node desactivat per� que no ha estat 
                            // accedit mai
                            dhtmlTree.append("tree.setItemImage2(" + nodeId[0] 
                              + ", '" + disabledItemIcon + "'" 
                              + ", '" + disabledItemIcon + "'" 
                              + ", '" + disabledItemIcon + "');\n");
                        }
                        dhtmlTree.append("tree.setItemStyle(" + nodeId[0] 
                          + ", '" + disabledItemStyle + "');\n");
                    } else {
                        // assigna al node de l'arbre l'identificador real de 
                        // l'�tem de l'arbre d'activitats
                        dhtmlTree.append("tree.setUserData(" + nodeId[0] + ", " 
                          + "'itemID', '" + item.itemID + "');\n");
                        if (item.isAccessed) {
                            if (dataModel.get(itemId).completionStatus.equals("completed")) {
                                // pinta el node activat, accedit i completat 
                                dhtmlTree.append("tree.setItemImage2(" + nodeId[0] 
                                  + ", '" + enabledCompletedItemIcon + "'" 
                                  + ", '" + enabledCompletedItemIcon + "'" 
                                  + ", '" + enabledCompletedItemIcon + "');\n");
                            } else {
                                // pinta el node activat, accedit i incomplert
                                dhtmlTree.append("tree.setItemImage2(" + nodeId[0] 
                                  + ", '" + enabledIncompletedItemIcon + "'" 
                                  + ", '" + enabledIncompletedItemIcon + "'" 
                                  + ", '" + enabledIncompletedItemIcon + "');\n");
                            }
                            dhtmlTree.append("tree.setItemStyle(" + nodeId[0] 
                              + ", '" + enabledAccessedItemStyle + "');\n");
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Retorna les sent�ncies javascript de l'arbre din�mic per ser dibuixat
     * en un JSP.
     *
     * @return String amb les sent�ncies javascript de l'arbre din�mic.
     */
    public final String getTree() {
        return dhtmlTree.toString();
    }
}