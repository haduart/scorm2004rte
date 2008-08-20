// $Id: Initialization.java,v 1.4 2008/02/08 12:29:09 msegarra Exp $
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

package edu.url.lasalle.campus.scorm2004rte.system;

import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.logging.Level;
import java.rmi.RemoteException;

import org.campusproject.osid.agent.Agent;
import org.osid.shared.SharedException;

import edu.url.lasalle.campus.scorm2004rte.osid.requests.AuthenticationRequest;
import edu.url.lasalle.campus.scorm2004rte.osid.requests.AgentRequest;
import edu.url.lasalle.campus.scorm2004rte.osid.requests.AuthorizationRequest;
import edu.url.lasalle.campus.scorm2004rte.osid.requests.ConfigurationRequest;
import edu.url.lasalle.campus.scorm2004rte.util.exception.RequestException;
import edu.url.lasalle.campus.scorm2004rte.util.exception.
    NotFoundPropertiesException;
import edu.url.lasalle.campus.scorm2004rte.util.resource.CollectionProperties;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;
import edu.url.lasalle.campus.scorm2004rte.util.resource.Translator;
import edu.url.lasalle.campus.scorm2004rte.util.exception.
    InitializationException;


/**
* $Id: Initialization.java,v 1.4 2008/02/08 12:29:09 msegarra Exp $
* La classe t� com a prop�sit realitzar les inicialitzaciones b�siques 
* dels JSPs.
* @author Marc Segarra / Enginyeria La Salle / msegarra@salle.url.edu
* @version Versi� $Revision: 1.4 $ $Date: 2008/02/08 12:29:09 $
* $Log: Initialization.java,v $
* Revision 1.4  2008/02/08 12:29:09  msegarra
* Adaptacions a la implementaci� de l'idioma per les proves del nou dummy v6.
*
* Revision 1.3  2008/02/01 06:58:27  msegarra
* Implementat com obtenir el codi d'idiomes de l'usuari.
*
* Revision 1.2  2008/01/31 10:12:26  msegarra
* Nova actualitzaci�.
*
* Revision 1.1  2007/12/10 09:36:22  msegarra
* Creaci� de la classe que t� com a prop�sit realitzar les inicialitzaciones 
* est�ndards dels JSPs.
*
*/
public class Initialization {
    /**
     * Objecte amb les dades de configuraci� de l'usuari que es 
     * connecta al sistema.
     */
    private Agent agent;
    
    /**
     * String que indica el rol associat a l'usuari.
     */
    private String role;
    
    /**
     * String amb el codi d'idiomes predeterminat ISO 639-1 de 
     * l'usuari en la plataforma.
     */
    private String language;
    
    /**
     * String que indica la ruta de les imatges.
     */
    private String imagesPath;
    
    /**
     * String que indica la ruta del fitxer d'estils.
     */
    private String stylesPath;
    
    /**
     * String que indica la ruta del fitxer de scripts.
     */
    private String scriptsPath;
    
    /**
     * String propietats generals de les finestres d'ajuda.
     */
    private String windowProperties;
    
    /**
     * String amb l'amplada de les finestres d'ajuda.
     */
    private String windowWidth;
    
    /**
     * String amb l'al�ada de les finestres d'ajuda.
     */
    private String windowHeight;
    
    /**
     * Objecte amb les traduccions sol�licitades en la p�gina.
     */
    private HashMap <String, String>translations;
    
    /**
     * Constructor per defecte.
     * Inicialitza els atributs de la classe.
     * @throws InitializationException Es llan�a l'excepci� en el cas que 
     * es produeixi un error.
     */
    public Initialization() throws InitializationException {
        try {
            // es crea la petici� de l'Agent per consultar les 
            // dades de configuraci� de l'usuari
            AgentRequest request = new AgentRequest(); 
            // es retorna l'agent amb les dades de l'usuari
            agent = request.getAgent();
           
            // se li assigna el rol de l'usuari en l'aplicaci� 
            // en el context del domini en que s'executa
            AuthorizationRequest authorizationRequest = 
                new AuthorizationRequest();
            role = authorizationRequest.isAuthorized();
            System.out.println("*** ROLE: " + role);
            
            // consulta l'idioma per defecte de l'usuari en la 
            // plataforma, en el cas que no retorni res  
            // s'agafa l'idioma per defecte de l'activitat
            /*PropertiesIterator iterator = 
                (PropertiesIterator) agent.getProperties();
            Properties property = iterator.nextProperties();*/
            /* @Observation: caldr� validar si �s un codi d'idiomes 
             * v�lid */
            //language = (String) property.getProperty("idLanguage");
            if (language == null) {
                ConfigurationRequest configurationRequest = 
                    ConfigurationRequest.getInstance();
                
                language = configurationRequest.getProperty("idLanguage");
            }
            // objecte per accedir al fitxer de configuraci� properties
            // de l'aplicaci�
            CollectionProperties cp = new CollectionProperties();
            
            // retorna el directori i nom del fitxer d'estils
            stylesPath = cp.getPropiedad("stylesfile.path");
            
            // retorna el directori i nom del fitxer de scripts
            scriptsPath = cp.getPropiedad("scriptsfile.path");
            
            // retorna la ruta on estan ubicades les imatges
            imagesPath = cp.getPropiedad("images.path");
            
            // retorna les propietats de les finestres d'ajuda
            windowProperties = cp.getPropiedad("helpwindow.properties");
            
            // retorna l'amplada de les finestres d'ajuda
            windowWidth = cp.getPropiedad("helpwindow.width");
            
            // retorna l'al�ada de les finestres d'ajuda
            windowHeight = cp.getPropiedad("helpwindow.height");
        } catch (RemoteException rex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    Initialization.class.getName(), 
                                    rex.getMessage());
            throw new InitializationException(rex.getMessage());
        } catch (RequestException rqex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    Initialization.class.getName(), 
                                    rqex.getMessageError());
            throw new InitializationException(rqex.getMessage());
            
        } catch (NotFoundPropertiesException nfpex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    Initialization.class.getName(), 
                                    nfpex.getMessage());
            throw new InitializationException(nfpex.getMessage());
        } /*catch (SharedException aex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    Initialization.class.getName(), 
                                    aex.getMessage());
            throw new InitializationException(aex.getMessage());
        }*/
    }
     
    /**
     * Llan�a una excepci� per ser capturada per la interf�cie.
     * @param id String amb l'identificador de la descripci� de la causa del 
     * llan�ament de l'excepci�.<br/>
     * Identificadors definits:<br/>
     * "userNotAuthenticated": L'usuari no s'ha autenticat a la plataforma.
     * "userNotHavePermissionAccess": L'usuari no t� permisos d'acc�s al recurs.
     * @throws InitializationException Es llan�a l'excepci� en el cas que 
     * es produeixi un error.
     */
    public final void throwException(final String id) 
                throws InitializationException {
        try {
            Translator translator = new Translator(language);
        
            if (id.equalsIgnoreCase("userNotAuthenticated")) {
                throw new InitializationException(translator.
                        translate("error.userNotAuthenticated"));
            } else if (id.equalsIgnoreCase("userNotHaveAccessPermission")) {
                throw new InitializationException(translator.
                        translate("error.userNotHaveAccessPermission"));
            } else if (id.equalsIgnoreCase("userNotHaveAdministrationPermission")) {
                throw new InitializationException(translator.
                        translate("error.userNotHaveAdministrationPermission"));
            } else {
                throw new InitializationException(translator.
                        translate("error.unknownedException"));
            }
        } catch (NotFoundPropertiesException nfpex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    Initialization.class.getName(), 
                                    nfpex.getMessage());
            throw new InitializationException(nfpex.getMessage());
        } catch (InitializationException iex) {
            throw new InitializationException(iex.getMessage());
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    Initialization.class.getName(), 
                                    ex.getMessage());
            throw new InitializationException(ex.getMessage());
        }
    }
    
    /**
     * Realitza les traduccions d'un conjunt de paraules clau a l'idioma 
     * predeterminat de l'usuari.
     * @param ids String amb el valor de les paraules clau a traduir.
     * @throws InitializationException Es llan�a l'excepci� en el cas que 
     * es produeixi un error.
     */
    public final void setTranslations(final String ids)
            throws InitializationException {
        try {           
            // s'inicialitza el traductor a partir de l'idioma predeterminat
            // de l'usuari        
            Translator translator = new Translator(language);
            // s'obt� un hashmap amb la relaci� entre els identificadors 
            // dels strings i les seves traduccions            
            StringTokenizer tokenizer = new StringTokenizer(ids, ",");         
            translations = new HashMap <String, String>(tokenizer.countTokens());
            String token;
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
                translations.put(token, translator.translate(token));
            }
        } catch (NotFoundPropertiesException nfpex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    Initialization.class.getName(), 
                                    nfpex.getMessage());
            throw new InitializationException(nfpex.getMessage());
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    Initialization.class.getName(), 
                                    ex.getMessage());
            throw new InitializationException(ex.getMessage());
        }
    }
    
    /**
     * Retorna les traduccions d'un conjunt de paraules clau a l'idioma 
     * predeterminat de l'usuari.
     * @return Un HashMap amb les traduccions de les paraules clau.
     */
    public final HashMap <String, String> getTranslations() {
        return translations;
    }
    
    /**
     * Indica si l'usuari s'ha autenticat a la plataforma o no.
     * @return Un boole�; true en el cas que l'usuari estigui autenticat o 
     * false en el cas que no ho estigui.
     * @throws InitializationException Es llan�a l'excepci� en el cas que 
     * es produeixi un error.
     */
    public final boolean isAuthenticated() throws InitializationException {
                
        try {
            // es crea la petici� de l'autenticaci� per comprovar si 
            // l'usuari est� autenticat i es retorna la resposta
            AuthenticationRequest request = new AuthenticationRequest();
            return request.isUserAuthenticated();
        } catch (RequestException rex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    Initialization.class.getName(), 
                                    rex.getMessageError());
            throw new InitializationException(rex.getMessage());
        } 
    }
    
    /**
     * Consulta si l'usuari t� rol de "professor" en el domini 
     * de la plataforma.
     * @return Un boole�; true, si l'usuari t� rol de professor  
     * o false, si no el t�.
     */
    public final boolean isTeacher() {
        if (role != null && role.equalsIgnoreCase("teacher")) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Consulta si l'usuari t� rol de "administrador" en el domini
     * de la plataforma.
     * @return Un boole�; true si l'usuari t� rol d'administrador 
     * o false, si no el t�.
     */
    public final boolean isAdmin() {
        if (role != null && role.equalsIgnoreCase("admin")) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Consulta si l'usuari t� rol de "alumne" en el domini de
     * la plataforma.
     * @return Un boole�; true si l'usuari t� rol d'estudiant o 
     * false, si no el t�.
     */
    public final boolean isStudent() {
        if (role != null && role.equalsIgnoreCase("student")) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Consulta si l'usuari pertany al rol de "an�nim".
     * @return Un boole�; true si l'usuari t� rol an�nim o 
     * false, si no en t�.
     */
    public final boolean isAnonymous() {
        // consultar si �s del grup alumne
        if (role != null && role.equalsIgnoreCase("anonymous")) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Consulta si l'usuari t� permisos d'administraci� en el 
     * curs del sistema.
     * @return Un boole�; true si l'usuari t� permisos d'administraci� 
     * o false si no en t�.
     */
    public final boolean hasAdministrationPermission() {
        // consulta si t� rol de professor o administrador
        /*if (isAdmin() || isTeacher()) {
            return true;
        } else {
            return false;
        }*/
        return true;
    }
    
    /**
     * Consulta si l'usuari t� permisos d'acc�s en el curs 
     * del sistema.
     * @return Un boole�; true si l'usuari t� acc�s, o false 
     * si no en t�.
     */
    public final boolean hasAccessPermission() {
        // consulta si t� rol de professor o admin o estudiant
        /*if (isAdmin() || isTeacher() || isStudent()) {
            return true;
        } else {
            return false;
        }*/
        return true;
    }
    
    /**
     * Retorna l'ubicaci� dels fitxers d'estils de l'aplicaci�.
     * @return String amb la ruta dels fitxers d'estils.
     */
    public final String getStylesPath() {
        return stylesPath;
    }
    
    /**
     * Retorna l'ubicaci� dels fitxers javascripts de l'aplicaci�.
     * @return String amb la ruta i nom del fitxer dels scripts.
     */
    public final String getScriptsPath() {
        return scriptsPath;
    }
    
    /**
     * Retorna l'ubicaci� dels fitxers gr�fics de l'aplicaci�.
     * @return String amb la ruta i nom del fitxer dels scripts.
     */
    public final String getImagesPath() {
        return imagesPath;
    }
    
    /**
     * Retorna les propietats de la finestra flotant d'ajuda.
     * @return String amb les propietats de la finestra.
     */
    public final String getWindowProperties() {
        return windowProperties;
    }
    
    /**
     * Retorna l'amplada de la finestra flotant d'ajuda.
     * @return String amb l'amplada de la finestra.
     */
    public final String getWindowWidth() {
        return windowWidth;
    }
    
    /**
     * Retorna l'al�ada de la finestra flotant d'ajuda.
     * @return String amb l'al�ada de la finestra.
     */
    public final String getWindowHeight() {
        return windowHeight;
    }
}
