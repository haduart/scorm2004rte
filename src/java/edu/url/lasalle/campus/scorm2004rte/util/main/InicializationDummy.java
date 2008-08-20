// $Id: InicializationDummy.java,v 1.3 2008/02/08 15:59:25 toroleo Exp $
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
package edu.url.lasalle.campus.scorm2004rte.util.main;


import java.util.Properties;

import org.campusproject.components.AgentAdminComponent;
import org.campusproject.components.ConfigurationAdminComponent;
import org.campusproject.components.IdComponent;
import org.campusproject.osid.agent.Agent;
import org.campusproject.osid.agent.Group;
import org.campusproject.utils.ConfigurationProperties;
import org.osid.agent.AgentException;
import org.osid.authorization.AuthorizationException;
import org.osid.dictionary.DictionaryException;
import org.osid.id.IdException;

/**
 * $Id: InicializationDummy.java,v 1.3 2008/02/08 15:59:25 toroleo Exp $
 * 
 * InicializationDummy.
 * Inicialitza els casos de prova pel Dummy. 
 * 
 * @author M.Reyes Enginyeria La Salle mtoro@salle.url.edu
 * @version 1.0 20/11/2007 $Revision: 1.3 $ $Date: 2008/02/08 15:59:25 $
 * $Log: InicializationDummy.java,v $
 * Revision 1.3  2008/02/08 15:59:25  toroleo
 * Nou mètode iniDummy.
 *
 * Revision 1.2  2008/02/08 12:24:24  msegarra
 * Inicialitzacions adaptades al nou dummy v6.
 *
 * Revision 1.1  2008/01/30 13:56:49  toroleo
 * Inicialització del dummy per tenir
 * els agents amb el properties instanciat
 *
 * 
 */
public class InicializationDummy {

	 /**
     * Constructor.
     *
     */
    protected InicializationDummy() {
        // prevents calls from subclass}
        throw new UnsupportedOperationException();
    }
  
    /**
     * 
     * Inicialitza el fitxer del dummy tot indicant que hi ha 
     * usuari connectat al sistema i que és l'usuari amb 
     * identificador agid2. A més crea els agents i grups 
     * necessaris per fer proves.
     * 
     * @throws AuthorizationException  Es llença quan hi ha hagut
     * 						algun problema amb la manipulació
     * 						dels identificadors.
     * @throws IdException Es llença quan hi ha hagut
     * 						algun problema amb les peticions 
     * 						d'autorització.
     * @throws AgentException  Es llença quan hi ha hagut
     * 						algun problema amb la manipulació
     * 						dels agents.
     *
     */
    public static void iniDummy() throws AgentException, 
    				IdException, AuthorizationException {
    	
    	createAgents();
		createGroups();
    	
    	//Objecte per llegir i recuperar el contingut del fitxer 
    	// configuration.bin.
    	 ConfigurationProperties configurationProperties =
    		 				ConfigurationProperties.getInstance();
    	 
    	 //es recupera l'objecte properties amb la informació del
 		//fitxer configuration.bin
 		Properties properties;
 		properties = configurationProperties.getProperties();
 		
 		properties.setProperty("authentication_authenticated", "true");
		properties.setProperty("authentication_userId", "agid2");
 		
    }
    
    /**
     * Creació dels agents per passar el joc de proves 
     * sobre el dummy.
     * 
     * @throws AgentException Es llença quan hi ha hagut
     * 						algun problema amb la manipulació
     * 						dels agents.
     */
    public static void createAgents() throws AgentException {

			AgentAdminComponent aac = new AgentAdminComponent(null);

			//creació del agid2
			aac.createAgent("Angeles", AgentAdminComponent.PERSON, null);
			//creació del agid3
			aac.createAgent("Juan", AgentAdminComponent.PERSON, null);
			//creació del agid4
			aac.createAgent("Mercedes", AgentAdminComponent.PERSON, null);
			//creació del agid5
			aac.createAgent("Anonymous", AgentAdminComponent.PERSON, null);
			//creació del agid6
			aac.createAgent("Sebastian", AgentAdminComponent.PERSON, null);
			
			//creació del agid7
			aac.createAgent("AppSalle", AgentAdminComponent.APPLICATION,
							 null);
			
						
	}
    
    
    /**
     * Creació dels diferents grups amb els que 
     * jugarem al dummy. A cada grup se li afegeixen
     * els agentsque pertanyeran a ell.
     * 
     * @throws AgentException Es llença quan hi ha hagut
     * 						algun problema amb la manipulació
     * 						dels agents.
     * @throws IdException Es llença quan hi ha hagut
     * 						algun problema amb la manipulació
     * 						dels identificadors.
     * @throws AuthorizationException Es llença quan hi ha hagut
     * 						algun problema amb les peticions 
     * 						d'autorització.
     */
    public static void createGroups() 
    				throws AgentException, IdException, 
    					   AuthorizationException {

    	Agent agent = null;
    	AgentAdminComponent aac = new AgentAdminComponent(null);
    	IdComponent ic = new IdComponent(null);
    	
    	
    	//es crea el grup dels professors
    	Group courseGroup = (Group) 
    				aac.createGroup("group1", AgentAdminComponent.COURSE,
    								"curs de fisica", null);
    				//( displayName, groupType, description, properties )
    	agent = (Agent) aac.getAgent(ic.getId("agid2"));
    	courseGroup.add(agent);
    	agent = (Agent) aac.getAgent(ic.getId("agid3"));
    	courseGroup.add(agent);
    	agent = (Agent) aac.getAgent(ic.getId("agid4"));
    	courseGroup.add(agent);
    	agent = (Agent) aac.getAgent(ic.getId("agid6"));
    	courseGroup.add(agent);
    	agent = (Agent) aac.getAgent(ic.getId("agid7"));
    	courseGroup.add(agent);


    	//es crea el grup dels professors
    	Group teachersGroup = (Group) 
    				aac.createGroup("group2", AgentAdminComponent.TEACHER,
    								"professors fisica", null);
    				//( displayName, groupType, description, properties )
    	
    	agent = (Agent) aac.getAgent(ic.getId("agid2"));
    	teachersGroup.add(agent);
    	agent = (Agent) aac.getAgent(ic.getId("agid3"));
    	teachersGroup.add(agent);
    	
    	//es crea el grup dels estudiants
    	Group studentsGroup = (Group) 
					aac.createGroup("group3", AgentAdminComponent.STUDENT, 
									"estudiants fisica", null);
					//( displayName, groupType, description, properties )

    	agent = (Agent) aac.getAgent(ic.getId("agid4"));
    	studentsGroup.add(agent);
    	agent = (Agent) aac.getAgent(ic.getId("agid6"));
    	studentsGroup.add(agent);
/*    	
    	//es crea el grup dels administradors
    	Group adminGroup = (Group) 
					aac.createGroup("group4", AgentAdminComponent.ADMIN,
									"administrador del sistema", null);
					//( displayName, groupType, description, properties )

    	agent = (Agent) aac.getAgent(ic.getId("agid1"));
    	adminGroup.add(agent);

    	
    	//es crea el grup dels grups genèrics
    	Group group = (Group) 
					aac.createGroup("group5", AgentAdminComponent.GROUP,
									"description grupo Group", null);
					//( displayName, groupType, description, properties )

    	agent = (Agent) aac.getAgent(ic.getId("agid1"));
    	group.add(agent);    	
    	*/
    	}
    
    /**
     * Es donen d'alta els paràmetres de configuració de 
     * l'aplicació i de la instancia.
     * 
     * @throws DictionaryException 	Es llença quan hi ha hagut
     * 						algun problema amb la configuració
     * 						de l'aplicació.
     */
    public static void install() throws DictionaryException {
    	ConfigurationAdminComponent cc = new ConfigurationAdminComponent(null);

        //Paràmetres d'aplicació.
    	String contextApp = "/modulsSalle";
       /* System.setProperty("nxContextApp", contextApp);
        System.setProperty("ccContextApp", contextApp);*/
        cc.addProperty("nxContextApp", contextApp,
        				ConfigurationAdminComponent.APP_PROPERTY);
       

        //Paràmetres d'instància.
    	// indica l'idioma per defecte
    	cc.addProperty("languageId", "ca",
    					ConfigurationAdminComponent.INSTANCE_PROPERTY);
    	
        cc.addProperty("idLanguage", "ca",
                ConfigurationAdminComponent.INSTANCE_PROPERTY);

    	// indica la instància de la classe per defecte [1-5]
    	cc.addProperty("classId", "1", 
    					ConfigurationAdminComponent.INSTANCE_PROPERTY);
    		
    	 
    	// indica el tutor de la classe qui s'encarrega d'administrar
    	//l'aplicació
    	cc.addProperty("tutorId", "agid6", 
    					ConfigurationAdminComponent.INSTANCE_PROPERTY);
    		
    	
    	// indica les presentacions de la classe
    	cc.addProperty("ppts",
    					"aula_virtual.swf|suggeriments.swf|semipresencial.swf|",
    						ConfigurationAdminComponent.INSTANCE_PROPERTY);
    		
    }
    
    /**
     * Modifica les propietats dels agents.
     * 
     * @throws AgentException Es llença quan hi ha hagut
     * 						algun problema amb la manipulació
     * 						dels agents.
     * @throws IdException Es llença quan hi ha hagut
     * 						algun problema amb la manipulació
     * 						dels identificadors.
     * @throws ConfigurationException Es llença quan hi ha hagut
     * 						algun problema amb la configuració 
     * 						de les dades. 
     * 
     */
 /* Maiye con la versión 6 del dummy no se pueden actualizar las properties
   de los agentes.
    
   public static void modifyAgents()
    				throws AgentException, IdException, 
    					   ConfigurationException {

    	Agent agent = null;
    	AgentAdminComponent aac = new AgentAdminComponent(null);
    	IdComponent ic = new IdComponent(null);
    	Properties prop = new Properties(null);
    	//Type type = new Type("CAMPUSPROJECT.ORG", "PROPERTY","PERSON","Propietats de persona");
    	
    	
    	agent = (Agent) aac.getAgent(ic.getId("agid2"));
    	prop.put("languageId", "es");
    	prop.put("mail", "angeles@salle.url.edu");
    	agent.setProperties(prop);
    	
    	agent = (Agent) aac.getAgent(ic.getId("agid3"));
    	prop = null;
    	prop = new Properties(null);
    	prop.put("languageId", "ca");
    	prop.put("mail", "juan@salle.url.edu");
    	agent.setProperties(prop);
    	
    	agent = (Agent) aac.getAgent(ic.getId("agid1"));
    	prop = null;
    	prop = new Properties(null);
    	prop.put("languageId", "en");
    	prop.put("mail", "cristobal@salle.url.edu");
    	agent.setProperties(prop);
    	
    	agent = (Agent) aac.getAgent(ic.getId("agid4"));
    	prop = null;
    	prop = new Properties(null);
    	prop.put("languageId", "es");
    	prop.put("mail", "pau@salle.url.edu");
    	agent.setProperties(prop);
    	    	
    	} */
}
