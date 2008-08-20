// $Id: PrePostSequencing.java,v 1.5 2008/03/03 13:02:02 msegarra Exp $
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
 * 
 * Author: Marc Segarra, Enginyeria La Salle, Universitat Ramon Llull
 * You may contact the author at msegarra@salle.url.edu
 * And the copyright holder at: semipresencial@salle.url.edu
 */

package edu.url.lasalle.campus.scorm2004rte.server.jsp;

import java.util.Iterator;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;

import edu.url.lasalle.campus.scorm2004rte.system.JSPUtils;
import edu.url.lasalle.campus.scorm2004rte.system.CourseAdministrator;
import edu.url.lasalle.campus.scorm2004rte.system.CourseManager;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.
    UserObjective;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.
    CMIObjectives;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.
    TreeAnnotations;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.ADLNav;
import edu.url.lasalle.campus.scorm2004rte.util.exception.NavTreeException;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;

/**
 * $Id: PrePostSequencing.java,v 1.5 2008/03/03 13:02:02 msegarra Exp $
 *
 * La classe implementa mètodes pre i post sequencing per ser utilitzades
 * en el sequencingEngine.jsp de cara a la comunicació entre el LMS i l'API
 * Adapter i el tractament de les dades que es transmiteixen.<br />
 * Quan l'usuari finalitzi la navegació per un recurs del tipus SCO o Asset, 
 * l'API Adapter enviarà les dades dels elements del model de dades utilitzades 
 * durant la sessió d'aquell recurs perquè siguin emmagatzemades en el sistema 
 * de cara al seguiment de l'usuari per aquests recursos d'aprenentatge. A 
 * continuació, mitjançant les decisions del motor de seqüenciament, es 
 * procedirà al llançament del següent recurs a mostrar per pantalla a 
 * l'usuari. 
 *
 * @author Marc Segarra / Enginyeria La Salle / msegarra@salle.url.edu
 * @version 1.0 $Revision: 1.5 $ $Date: 2008/03/03 13:02:02 $
 * $Log: PrePostSequencing.java,v $
 * Revision 1.5  2008/03/03 13:02:02  msegarra
 * Modificació del mecanisme d'emmagatzematge de l'element session_time del model de dades CMI.
 *
 * Revision 1.4  2008/02/20 15:25:55  msegarra
 * Implementat el mecanisme de funcionament per a peticions adl.nav.request_valid.choice.
 *
 * Revision 1.3  2008/02/01 07:00:13  msegarra
 * Millora de la classe.
 *
 * Revision 1.2  2008/01/31 16:20:29  ecespedes
 * Millorant el sistema (2)
 *
 * Revision 1.1  2008/01/31 10:11:38  msegarra
 * Primera implementació.
 *
 *
 */
public class PrePostSequencing {
    /**
     * Identificador de l'ítem de l'anterior llançament.
     */    
    //private String previousItemId = "unkown";
    
    /**
     * Identificador del pròxim ítem a llançar.
     */
    //private String nextItemId = "unknown";
    
    /**
     * Ruta i nom del fitxer del recurs a llançar.
     */
    private String resource;
    
    /**
     * Petició de navegació de l'usuari.
     */
    private ADLNav userRequest = new ADLNav();
    
    /**
     * Tipus de missatge.
     */
    public static enum messageType {
        /**
         * Missatge d'avís que s'ha arribat al final del 
         * recorregut de l'arbre.
         */
        end,
        /**
         * Missatge nomal de càrrega del recurs.
         */
        normal
    }; 
    
    /**
     * Processa la petició enviada per l'API Adapter emmagatzemant el 
     * seguiment de l'usuari per l'objecte d'aprenentatge i realitzant
     * el procés de seqüenciament.
     * @param request Objecte HttpServletRequest amb la petició enviada per 
     * l'API Adapter.
     * @param utils Objecte InitializationUtilites amb dades de l'usuari. 
     */
    public final void processRequest(final HttpServletRequest request, 
            final JSPUtils utils) {
       
        try {
            /**
             * PAS 1: Recupera les dades de l'usuari per a una determinada
             * organització d'un curs.
             */
            CourseManager courseManager = CourseAdministrator.getInstance().
                getCourseManagerInstance(1, Long.parseLong(request.
                        getParameter("org")));
            UserObjective userObjective = 
                courseManager.getUser(utils.getUserId()); 
            
            /**
             * PAS 2: Es consulta l'identificador de l'ítem del recurs llançat 
             * actualment.
             */
            String currentId = userObjective.currentItem;

            /**
             * PAS 3: S'emmagatzemen les dades del recurs de la sessió de 
             * l'estudiant en el LMS en el cas que sigui del tipus SCO i el 
             * cmi.mode estigui en mode normal
             */
            if (request.getParameter("type").equals("sco")
                    && userObjective.dataModel.get(currentId).mode.
                    equals("normal")) {
                fillupUserOjective(userObjective, request, currentId);
            }
            /**
             * PAS 4: S'emmagatzemen les dades de l'ADL Navigation i s'executa
             * el motor del seqüenciament.
             */
            // si el recurs no ha estat llançat, no es calcula el seqüenciament
            if (!request.getParameter("type").equals("unknown")) {
                if (userObjective.adlnav == null) {
                    userObjective.adlnav = new ADLNav();
                }
                // s'assigna l'ADL Navigation a la petició de l'usuari
                userRequest = userObjective.adlnav;
                
                /**
                 * PAS 4.1: S'emmagatzema el valor de l'element adl.nav.request.
                 */
                userObjective.adlnav.request = request.getParameter("request");
               
                // si el recurs actual llançat és un asset, s'indica que 
                // ha estat completat
                if (request.getParameter("type").equals("asset")) {
                    userObjective.dataModel.get(currentId).
                        completionStatus = "completed";
                }
                
                // s'emmagatzema el temps de la sessió de l'usuari
                userObjective.dataModel.get(currentId).
                    sessionTime = request.getParameter("session_time");
                System.out.println("*** SESSION TIME: " + request.getParameter("session_time"));
                /**
                 * PAS 4.2: Es realitzen els processos de seqüenciament a 
                 * partir de les dades enviades per l'API Adapter de la sessió 
                 * de l'usuari amb l'objecte d'aprenentatge.
                 */
                courseManager.overallSequencingProcess(userObjective);

                // si es produeix algun error durant el seqüenciament, aquest
                // es registra al log
                if (userObjective.adlnav != null 
                        && userObjective.adlnav.lmsException != "") {
                    LogControl logControl = LogControl.getInstance();
                    logControl.writeMessage(Level.INFO, 
                            PrePostSequencing.class.getName(), 
                            userObjective.adlnav.lmsException);
                }
            }
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    PrePostSequencing.class.getName(), 
                                    ex.getMessage());
        }
    }
    
    /**
     * Omple l'objecte UserObjective amb les dades rebudes de la petició
     * enviada per l'API Adapter al finalitzar la sessió d'un objecte
     * d'aprenentatge, en aquest cas, un SCO. 
     * @param userObjective Objecte UserObjective amb les dades de 
     * l'usuari per a una determinada organització d'un curs.
     * @param request Objecte HttpServletRequest amb la petició enviada per 
     * l'API Adapter després de finalitzar la sessió d'un objecte 
     * d'aprenentatge.
     * @param itemId String identificador de l'ítem actual del recurs 
     * llançat. 
     */
    private void fillupUserOjective(final UserObjective 
            userObjective, final HttpServletRequest request, 
            final String itemId) {
        /**
         * PAS 3.1: S'emmagatzemen els valors dels elements del model 
         * de dades CMI que informen del seguiment.
         */
        // cmi.completion_status
        userObjective.dataModel.get(itemId).completionStatus = 
            request.getParameter("completion_status");
        // cmi.success_status
        userObjective.dataModel.get(itemId).successStatus = 
            request.getParameter("success_status");
        // cmi.score.max
        userObjective.dataModel.get(itemId).scoreMax = 
            request.getParameter("score.max");   
        // cmi.score.min
        userObjective.dataModel.get(itemId).scoreMin = 
            request.getParameter("score.min");
        // cmi.score.raw
        userObjective.dataModel.get(itemId).scoreRaw = 
            request.getParameter("score.raw");
        // cmi.score.scaled
        userObjective.dataModel.get(itemId).scoreScaled = 
            request.getParameter("score.scaled");
        // cmi.session_time
        //userObjective.dataModel.get(itemId).sessionTime = 
            //request.getParameter("session_time");
        // cmi.progress_measure
        userObjective.dataModel.get(itemId).progressMeasure = 
            request.getParameter("progress_measure");
        
        /**
         * PAS 3.2: S'emmagatzemen les dades de l'estat de l'SCO.
         */
        // cmi.entry
        userObjective.dataModel.get(itemId).entry = 
            request.getParameter("entry");
        // cmi.exit
        userObjective.dataModel.get(itemId).exit = 
            request.getParameter("exit");
        // cmi.location
        userObjective.dataModel.get(itemId).location = 
            request.getParameter("location");
        // cmi.mode
        userObjective.dataModel.get(itemId).mode = 
            request.getParameter("mode");
        // cmi.suspend_data
        userObjective.dataModel.get(itemId).suspendData = 
            request.getParameter("suspend_data"); 
        
        /**
         * PAS 3.3: S'emmagatzemen les preferències de l'usuari
         * en aquest SCO.
         */
        // cmi.preferences.audio_captioning
        userObjective.dataModel.get(itemId).preferenceAudioCaptioning = 
            request.getParameter("audio_captioning");
        // cmi.preferences.audio_level
        userObjective.dataModel.get(itemId).preferenceAudioLevel = 
            request.getParameter("audio_level");
        // cmi.preferences.delivery_speed
        userObjective.dataModel.get(itemId).preferenceDeliverySpeed = 
            request.getParameter("delivery_speed");
        // cmi.preferences.language
        userObjective.dataModel.get(itemId).preferenceLanguage = 
            request.getParameter("language");
        
        /**
         * PAS 3.4: S'emmagatzemen les dades dels objectius.
         * @Observation Falta implementar que es puguin crear nous 
         * objectius des dels SCOs sense que estiguin definits en el manifest.
         */
        int num = Integer.parseInt(request.getParameter("num_obj"));
        if (num > 0) {
            String objId;
            for (int i = 0; i < num; i++) {
                objId = request.getParameter("obj." + i + ".id");
                // cmi.objectives.n.scaled
                userObjective.dataModel.get(itemId).cmiObjectives.
                    get(objId).scoreScaled = request.getParameter("obj." 
                            + i + ".scaled");
                // cmi.objectives.n.score.raw
                userObjective.dataModel.get(itemId).cmiObjectives.
                    get(objId).scoreRaw = request.getParameter("obj." 
                            + i + ".raw");
                // cmi.objectives.n.score.min
                userObjective.dataModel.get(itemId).cmiObjectives.
                    get(objId).scoreMin = request.getParameter("obj." 
                            + i + ".min");
                // cmi.objectives.n.score.max
                userObjective.dataModel.get(itemId).cmiObjectives.
                    get(objId).scoreMax = request.getParameter("obj." 
                            + i + ".max");
                // cmi.objectives.n.success_status
                userObjective.dataModel.get(itemId).cmiObjectives.
                    get(objId).successStatus = request.getParameter("obj." 
                            + i + ".success");
                // cmi.objectives.n.completion_status
                userObjective.dataModel.get(itemId).cmiObjectives.
                    get(objId).completionStatus = request.getParameter("obj." 
                            + i + ".completion");
                // cmi.objectives.n.progress_measure
                userObjective.dataModel.get(itemId).cmiObjectives.
                    get(objId).progressMeasure = request.getParameter("obj." 
                            + i + ".progress");
                // cmi.objectives.n.description
                userObjective.dataModel.get(itemId).cmiObjectives.
                    get(objId).description = request.getParameter("obj." 
                            + i + ".description");
            }
        }
    }
    
    /**
     * Inicialitza l'arbre de navegació, els botons de navegació, l'API Adapter
     * i el model de dades de la sessió de l'objecte d'aprenentatge que es
     * llançarà abans de llançar-lo per pantalla.<br />
     * Aquest mètode s'ha d'invocar després d'haver realitzat el seqüenciament.  
     * @param request Objecte HttpServletRequest amb la petició enviada per 
     * l'API Adapter després de finalitzar la sessió d'un objecte 
     * d'aprenentatge.
     * @param utils Objecte InitializationUtilites amb dades de l'usuari. 
     * @return String amb el codi javascript d'inicialització dels elements de
     * l'entorn d'execució SCORM (botons, arbre de navegació, API...) per 
     * llançar l'objecte d'aprenentatge.
     */
    public final String processLaunching(final HttpServletRequest request, 
            final JSPUtils utils) {
        // buffer amb les sentències javascript a ser executades en el JSP
        StringBuffer code = new StringBuffer();
        try {
            /**
             * PAS 1: Recupera les dades de l'usuari per a una determinada
             * organització d'un curs.
             */
            CourseManager courseManager = CourseAdministrator.getInstance().
                getCourseManagerInstance(1, Long.parseLong(request.
                    getParameter("org")));
            UserObjective userObjective = 
                courseManager.getUser(utils.getUserId()); 
            
            // si l'usuari ha realitzat una petició de sortida de la 
            // presentació dels continguts, s'abandona la sessió d'aprenentatge
            if (userRequest.request.equals("exit") 
                    || userRequest.request.equals("exitAll")
                    || userRequest.request.equals("abandon")
                    || userRequest.request.equals("abandonAll")
                    || userRequest.request.equals("suspendAll") 
                    || (userObjective.adlnav != null && 
                            userObjective.adlnav.lmsException.equals("Exit"))) {
                // redirecciona a la pàgina principal del curs
                code.append("window.parent.location = 'viewCourse.jsp?course=" 
                        + request.getParameter("course") + "';\n");
            } else {    
                /**
                 * PAS 2: Sentències javascript per inicialitzar l'API
                 * Adapter i l'arbre de navegació.
                 */
                // referència a l'API Adapter
                code.append("var api = window.parent.API_1484_11;\n");
                // inicialitza els atributs de l'API
                code.append("api.init();\n");
                // referència a l'arbre de navegació
                code.append("var tree = window.parent.tree;\n");
                // incialitza el tamany del text
                code.append("window.parent.sizeText = 1;\n");
                
                /**
                 * PAS 3: Pinta l'arbre i els botons de navegació segons
                 * el que indiqui l'ADLNav.
                 */
                // crea l'arbre de navegació
                NavTree cTree = new NavTree();
                // comprova que l'ADLNav estigui inicialitzat
                if (userObjective.adlnav == null) {
                    userObjective.adlnav = new ADLNav();
                    System.err.println("*** NO TE ADLNAV I N'HAURIA DE TENIR!!! ***");
                }
                if (userObjective.adlnav.showTreeChoice) {
                    // mostra l'arbre de navegació
                    cTree.deleteTree();
                    cTree.loadTree(userObjective);
                    code.append(cTree.getTree());
                    code.append("window.parent.document." +
                            "getElementById('treeContainer')" 
                            + ".style.visibility = 'visible';\n");
                } else {
                    // oculta l'arbre de navegació
                    cTree.deleteTree();
                    code.append(cTree.getTree());
                    code.append("window.parent.document." +
                            "getElementById('treeContainer')" 
                            + ".style.visibility = 'hidden';\n");
                }
                if (userObjective.adlnav.choiceExit) {
                    // mostra el botó "exit"
                    code.append("window.parent.document.getElementById('exit')." 
                            + "style.display = 'inline';\n");
                } else {
                    // oculta el botó de "exit"
                    code.append("window.parent.document.getElementById('exit')." 
                            + "style.display = 'none';\n");
                }
                if (userObjective.adlnav.choicePrevious) {
                    // mostra el botó "previous"
                    code.append("window.parent.document.getElementById('previous')" 
                            + ".style.display = 'inline';\n");
                } else {
                    // oculta el botó "previous"
                    code.append("window.parent.document.getElementById('previous')" 
                            + ".style.display = 'none';\n");
                }
                if (userObjective.currentHref != null 
                        && userObjective.adlnav.choiceContinue) {
                    // mostra el botó "continue"
                    code.append("window.parent.document.getElementById('continue')" 
                            + ".style.display = 'inline';\n");
                } else {
                    // oculta el botó "continue"
                    code.append("window.parent.document.getElementById('continue')" 
                            + ".style.display = 'none';\n");
                }
                
                /**
                 * PAS 4: Inicialitza els elements del model de dades de 
                 * l'API Adapter de cara a la sessió del pròxim recurs a
                 * llançar.
                 */
                 // actualitza els valors dels elements del model de dades 
                 // de l'API
                 fillupAPI(userObjective, code, utils);
                    
                 /**
                  * PAS 5: Llança el recurs que pertoca calculat pel motor
                  * de seqüenciament o seleccionat per l'usuari.
                  */
                 resource = userObjective.currentHref;
                 System.out.println("+++ Resource: " + resource);
                 if (resource != null) {
                     // && !previousItemId.equals(userObjective.currentItem)
                     // llança el pròxim recurs a mostrar
                     code.append("document.location.href='courses/Photoshop/" + resource + "';\n");
                 } 
            }
        } catch (NavTreeException ntex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.INFO, 
                                    PrePostSequencing.class.getName(), 
                                    ntex.getMessage());
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    PrePostSequencing.class.getName(), 
                                    ex.getMessage());
        }
        return code.toString();
    }
    
    /**
     * Actualitza els valors dels elements del model de dades de l'API
     * Adapter de cara a la sessió del pròxim recurs d'aprenentatge que
     * es llançarà. 
     * @param userObjective Objecte UserObjective amb les dades de 
     * l'usuari per a una determinada organització d'un curs.
     * @param code StringBuffer amb codi javascript a imprimir en el JSP.
     * @param utils Objecte InitializationUtilites amb dades de l'usuari.
     */
    private void fillupAPI(final UserObjective userObjective, 
            final StringBuffer code, final JSPUtils utils) {
        // consulta l'identificador de l'ítem a llançar calculat amb 
        // anterioritat pel motor de seqüenciament
        String nextItemId = userObjective.currentItem;
        
        /**
         * PAS 4.1: S'actualitzen els valors dels elements del model de 
         * dades CMI de l'API Adapter.
         */
        if (userObjective.dataModel.get(nextItemId).entry.
                equals("resume")) {           
            code.append("api.dataModel.suspend_data='" + userObjective.
                    dataModel.get(nextItemId).suspendData + "';");
        }
        code.append("api.dataModel.location='" 
                + userObjective.dataModel.get(nextItemId).location + "';\n");
        code.append("api.dataModel.completion_threshold='" 
                + userObjective.dataModel.get(nextItemId)
                .completionThreshold + "';\n");
        code.append("api.dataModel.credit='" 
                + userObjective.dataModel.get(nextItemId).credit + "';\n");
        code.append("api.dataModel.entry='" 
                + userObjective.dataModel.get(nextItemId).entry + "';\n");
        code.append("api.dataModel.launch_data='" 
                + userObjective.dataModel.get(nextItemId).
                launchData + "';\n");
        code.append("api.dataModel.max_time_allowed='" 
                + userObjective.dataModel.get(nextItemId).
                maxTimeAllowed + "';\n");
        code.append("api.dataModel.mode='" 
                + userObjective.dataModel.get(nextItemId).mode + "';\n");
        code.append("api.dataModel.scaled_passing_score='" 
                + userObjective.dataModel.get(nextItemId).
                scaledPassingScore + "';\n");
        code.append("api.dataModel.time_limit_action='" 
                + userObjective.dataModel.get(nextItemId).
                timeLimitAction + "';\n");
        code.append("api.dataModel.total_time='" 
                + userObjective.dataModel.get(nextItemId).
                totalTime + "';\n");
        code.append("api.dataModel.audio_captioning='" 
                + userObjective.dataModel.get(nextItemId).
                preferenceAudioCaptioning + "';\n");
        code.append("api.dataModel.audio_level='" 
                + userObjective.dataModel.get(nextItemId).
                preferenceAudioLevel + "';\n");
        code.append("api.dataModel.delivery_speed='" 
                + userObjective.dataModel.get(nextItemId).
                preferenceDeliverySpeed + "';\n");
        code.append("api.dataModel.language='" 
                + userObjective.dataModel.get(nextItemId).
                preferenceLanguage + "';\n");
        code.append("api.dataModel.learner_name='" 
                + utils.getUserName() + "';\n");
        code.append("api.dataModel.learner_id='" 
                + utils.getUserId() + "';\n");
        
        /**
         * PAS 4.2: S'actualitzen els objectius del model de dades CMI 
         * de l'API Adapter.
         */
        if (!userObjective.dataModel.get(nextItemId)
                .cmiObjectives.isEmpty()) {
            Iterator objectives = userObjective.dataModel.
                get(nextItemId).cmiObjectives.values().iterator();
            
            CMIObjectives objective = null;
            while (objectives.hasNext()) { 
                objective = (CMIObjectives) objectives.next();
                code.append("var obj = new Objective();\n");
                code.append("obj.id = '" + objective.id + "';\n");
                code.append("obj.success_status = '" 
                        + objective.successStatus + "';\n");
                code.append("obj.score_scaled = '" 
                        + objective.scoreScaled + "';\n");
                code.append("api.dataModel.objectives.push(obj);\n");
            }
        }
        /**
         * PAS 4.3: S'actualitzen els valors dels elements del model de 
         * dades ADL Navigation de l'API Adapter.
         * @Observation: Millorar la implementació del adl.nav.request_valid.choice
		 * en futures versions.
         */
        code.append("api.dataModel.request_continue='" 
                + userObjective.adlnav.requestValidContinue + "';\n");
        code.append("api.dataModel.request_previous='" 
                + userObjective.adlnav.requestValidPrevious + "';\n");
        
        // consulta les dades dels ítems del treeAnnotations per comprovar
        // quins ítems estan desactivats per a l'implementació de l'element 
        // adl.nav.request_valid.choice
        Iterator annotations = userObjective.treeAnnotations.values().
            iterator();
        TreeAnnotations itemData = null;
        String enabledItemsList = "";
        while (annotations.hasNext()) { 
            itemData = (TreeAnnotations) annotations.next();
            if (itemData.currentView != TreeAnnotations.viewType.disabled) {
                enabledItemsList += itemData.itemID + "[,]";
            }
        } 
        // assigna els valors dels identificadors dels ítems acivats a l'API
        code.append("api.enabledItems = '" + enabledItemsList + "';\n");
    }
    
    /**
     * Consulta si el missatge a mostrar és del tipus normal.
     * @return Booleà indicant si és un missatge normal o no.
     */
    public final boolean isNormalTypeMessage() {
        if (resource != null) {
            return true;
        }
        return false;
    }
}
