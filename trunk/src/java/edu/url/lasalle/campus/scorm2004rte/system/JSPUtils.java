package edu.url.lasalle.campus.scorm2004rte.system;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.sql.SQLException;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import org.osid.agent.AgentException;
import org.osid.shared.SharedException;
import org.campusproject.osid.agent.Agent;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.url.lasalle.campus.scorm2004rte.osid.requests.AgentRequest;
import edu.url.lasalle.campus.scorm2004rte.util.exception.
    NotFoundPropertiesException;
import edu.url.lasalle.campus.scorm2004rte.util.exception.RequestException;
import edu.url.lasalle.campus.scorm2004rte.util.resource.LogControl;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.GestorBD;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.
    UserObjective;
import edu.url.lasalle.campus.scorm2004rte.server.validator.DOMParser;
import edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements.
    MetadataInformation;
import edu.url.lasalle.campus.scorm2004rte.util.resource.CollectionProperties;
import edu.url.lasalle.campus.scorm2004rte.util.resource.UnZipper;


public class JSPUtils {
    /**
     * Objecte amb les dades de configuració de l'usuari que es 
     * connecta al sistema.
     */
    private Agent agent;
    
    /**
     * String amb la ruta del directori del curs SCORM.
     */
    private String coursePath;
    
    /**
	* Encara que tinguem diverses organitzacions totes tindran la
	* mateixa metadata.
	*/
	private MetadataInformation metadata = new MetadataInformation();
    
    /**
     * 
     *
     */
    public JSPUtils() {
        
    }
    
    /**
     * Importa el paquet zip amb el curs SCORM al servidor i emmagatzema a 
     * memòria la metadata del curs.
     * @param request Objecte HttpServletRequest amb la petició enviada pel
     * formulari amb contingut binari de "createCourse.jsp".
     * @return String amb l'identificador del curs.
     */
    public final String importCourse(final HttpServletRequest request) {
        String courseId = "-1";
        
        // puja el fitxer del curs SCORM al servidor i el descomprimeix
        // si tot és correcte, el curs s'emmagatzema a la BDD
        if (fileUpload(request)) {
            /*** 
             * MARRÓ EDUARD!!!
             * l'atribut coursePath conté el pathname únic d'on està el curs
             * SCORM descomprimit. 
             * Exemple: "c:\files\courses\3F99F6160262843784249F34" 
             * 
             * Faltaria "crear" el curs a la base de dades: validar el manifest,
             * emmagatzemar les dades del curs (metadata, etc) i retornar 
             * l'identificador del curs en string.
             * En aquesta fase no caldria crear el curs a la BDD, només 
             * emmagatzemant a memòria la metadata n'hi hauria prou ja després
             * d'executar aquest mètode el professor se li mostrarà un formulari
             * amb la metadata del curs i podrà escollir entre acceptar i crear
             * el curs o cancel·lar-ho tot.
             ****/
        	        	
        	DOMParser nouParser = new DOMParser(
        			coursePath + "\\imsmanifest.xml");
        	
        	metadata = nouParser.getMetadataInformation();
        	
        	/*
        	ParserHandler nouHandler = new ParserHandler(
        			coursePath,
    				"JUnitTestDefaultName" + (new Random()).nextInt(1000));
    		*/
        }
        System.out.println("COURSE PATH: " + coursePath);
        return courseId;
    }
    
    /**
     * Emmagatzema el curs a la BDD amb les modificacions de la metadata  
     * del curs indicades al formulari enviat prèviament.
     * @param request Objecte HttpServletRequest amb la petició enviada pel
     * formulari amb contingut ascii de "createCourse.jsp".
     * @return
     */
    public final boolean createCourse(final HttpServletRequest request) {
        
        
        return true;
    }
    
    /**
     * Carrega els fitxers ZIPs enviats pel formulari de "createCourse.jsp" 
     * al servidor. 
     * @param request Objecte HttpServletRequest amb la petició enviada per
     * un formulari amb contingut "multipart/form-data". 
     * @return Retorna un booleà; TRUE si s'ha importat i creat correctament 
     * el curs o FALSE si no es compleix la premisa anterior.
     */
    private final boolean fileUpload(final HttpServletRequest request) {
        String fileDir = null, temporalPath = null, coursesPath = null;
        int maxSizeMemory = 0;
        
        try {
            // objecte per accedir al fitxer de configuració properties
            // de l'aplicació
            CollectionProperties cp = new CollectionProperties();
            // retorna el directori temporal on es guardaran els paquets 
            // SCORM que s'importin
            temporalPath = cp.getPropiedad("uploadfiles.path");
            // retorna el directori on es pengen els cursos descomprimits
            coursesPath = cp.getPropiedad("courses.path");
            // retorna el tamany màxim permès del fitxer a importar
            maxSizeMemory = 
                Integer.parseInt(cp.getPropiedad("uploadfiles.maxsize"));
            
            // si és necessari, crea el directori temporal per a penjar 
            // el paquet SCORM
            File tmpDirectory = new File(temporalPath);
            boolean existTempDirectory = tmpDirectory.exists();
            if (!existTempDirectory) {
                existTempDirectory = tmpDirectory.mkdirs();
            }
            // si el directori temporal existeix, es realitza tot el procés 
            // de pujar el fitxer
            if (existTempDirectory) {
                if (ServletFileUpload.isMultipartContent(request)) {
                    // es crea el servlet upload per a processar la pujada de 
                    // fitxers i una instància FileItem (factory) per a
                    // emmagatzemar-los temporalment
                    DiskFileItemFactory factory = 
                        new DiskFileItemFactory(Constants.MAX_SIZE_MEMORY, 
                                tmpDirectory);
                    ServletFileUpload upload = new ServletFileUpload(factory);
                    
                    List <FileItem> items = upload.parseRequest(request);
                    Iterator <FileItem>iter = items.iterator();
                    if (iter.hasNext()) {
                        FileItem item = (FileItem) iter.next();
                        if (!item.isFormField()) {
                            if (item.getSize() > 0 
                                    && item.getSize() < maxSizeMemory) {
                                // s'emmagatzema el fitxer al servidor
                                fileDir = temporalPath + item.getName();
                                File uploadedFile = new File(fileDir);
                                item.write(uploadedFile);
                                // si s'ha guardat correctament, es descomprimeix 
                                // el fitxer amb el curs SCORM i retorna true
                                if (uploadedFile.exists()) {
                                    // descomprimeix el paquet SCORM
                                    UnZipper util = new UnZipper();
                                    String directoryOut = coursesPath  
                                        + randomStringGenerator() + File.separatorChar;
                                    boolean unzipped = 
                                        util.unzip(fileDir, directoryOut, maxSizeMemory);                                                                      
                                    // esborra el fixer zip
                                    uploadedFile.delete();
                                    
                                    if (unzipped) {
                                        coursePath = directoryOut;
                                        return true;
                                    }
                                } else {
                                    if (Constants.DEBUG_WARNINGS) {
                                        System.err.println("WARNING [" + JSPUtils.class.getName() 
                                                + "::fileUpload] El fitxer '" + fileDir 
                                                + "' no s'ha penjat en el servidor.");
                                    }
                                }
                            } else {
                                if (Constants.DEBUG_WARNINGS) {
                                    System.err.println("WARNING [" + JSPUtils.class.getName() 
                                            + "::fileUpload] El fitxer zip '" + fileDir 
                                            + "' supera el tamany permès (" + maxSizeMemory + ").");
                                }
                            }
                        }
                    }
                }
            } else {
                if (Constants.DEBUG_WARNINGS) {
                    System.err.println("WARNING [" + JSPUtils.class.getName() 
                            + "::fileUpload] No existeix el directori temporal '" 
                            + temporalPath + "'.");
                }
            }
        } catch (NotFoundPropertiesException nfpex) {
            if (Constants.DEBUG_ERRORS) {
                System.err.println(nfpex.getMessage());
                nfpex.printStackTrace();
            }
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.SEVERE, 
                                    JSPUtils.class.getName(), 
                                    nfpex.getMessage());
            return false;
        } catch (FileUploadException fuex) {
            String message = "ERROR [" + JSPUtils.class.getName() + "::fileUpload] Pujant " 
                + "el fitxer zip '" + fileDir + "' al servidor en el directori temporal.";
            if (Constants.DEBUG_ERRORS) {
                System.err.println(message);
                fuex.printStackTrace();
            }
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.SEVERE, 
                    JSPUtils.class.getName(), 
                                    message);
            return false;
        } catch (IOException ioex) {
            String message = "ERROR [" + JSPUtils.class.getName() + "::fileUpload] Accedint " 
                + "al directori temporal o al fitxer zip ubicat en '" + temporalPath + "'.";
            if (Constants.DEBUG_ERRORS) {
                System.err.println(message);
                ioex.printStackTrace();
            }
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.SEVERE, 
                                    JSPUtils.class.getName(), 
                                    message);
            return false;
        } catch (Exception ex) {
            String message = "ERROR [" + JSPUtils.class.getName() + "::fileUpload] General: " 
                + ex.getMessage();
            if (Constants.DEBUG_ERRORS) {
                System.err.println(message);
                ex.printStackTrace();
            }
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.SEVERE, 
                                    JSPUtils.class.getName(), 
                                    message);
            return false;
        }
        return false;
    }
       
    /**
     * Genera un string mitjançant dos nombres hexadecimals 
     * (base 16) aleatoris.
     * @return String amb un nombre aleatori.
     */
    private String randomStringGenerator() {
        Random random =  new Random();

        String number = Long.toHexString(random.nextLong()) 
            + Long.toHexString(random.nextLong());
        
        return number.toUpperCase();
    }
    
    /**
     * Consulta el nom de l'usuari.
     * @return String amb el nom de l'usuari.
     */
    public final String getUserName() {
        try {
            if (agent == null) {
                // crea la petició de l'Agent per consultar les 
                // dades de configuració de l'usuari
                AgentRequest request = new AgentRequest(); 
                // es retorna l'agent amb les dades de l'usuari
                agent = request.getAgent();
            }
            return agent.getDisplayName();
        } catch (AgentException aex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    aex.getMessage());
            return "";
        } catch (RemoteException rex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    rex.getMessage());
            return "";
        } catch (RequestException rqex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    rqex.getMessageError());
            return "";   
        }
    }
    
    /**
     * Consulta l'identificador de l'agent de l'usuari.
     * @return String amb l'identificador de l'agent de l'usuari.
     */
    public final String getUserId() {
        try {
            if (agent == null) {
                // crea la petició de l'Agent per consultar les 
                // dades de configuració de l'usuari
                AgentRequest request = new AgentRequest(); 
                // es retorna l'agent amb les dades de l'usuari
                agent = request.getAgent();
            }
            return agent.getId().getIdString();
        } catch (AgentException aex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    aex.getMessage());
            return "";
        } catch (RemoteException rex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    rex.getMessage());
            return "";
        } catch (RequestException rqex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    rqex.getMessageError());
            return "";   
        } catch (SharedException e) {
        	LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    e.getMessage());
            return "";
		}
    }
    
    /**
     * Retorna el títol del metadata actual. 
     * @return String
     */
    public final String getCourseTitleFORM() {
    	return metadata.title;
    }
    /**
     * Consulta el títol del curs.
     * @param courseId String amb l'identificador del curs.
     * @return String amb el títol del curs.
     */
    public final String getCourseTitle(final String courseId) {
    	
        try {
            // recupera la metadata del curs
            MetadataInformation metadata2 =
            	CourseAdministrator.getInstance().
                getCourseManagerInstance(1, Long.parseLong(courseId)).
                    getMetadata();
                        
            if (metadata2.title == null 
            		|| metadata2.title.length() == 0) {
            	return "SENSE TITOL, ID: " + courseId + "";            	
            }
            return metadata2.title;
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    ex.getMessage());
            return "";
        }
    }
    
    /**
     * Retorna el títol del metadata actual. 
     * @return String
     */
    public final String getCourseDescriptionFORM() {
    	return metadata.description;
    }
    
    /**
     * Consulta el text descriptiu del curs.
     * @param courseId String amb l'identificador del curs.
     * @return String amb la descripció del curs.
     */
    public final String getCourseDescription(final String courseId) {
        try {
            // recupera la metadata del curs
             MetadataInformation metadata2 =
            	 CourseAdministrator.getInstance().
                 getCourseManagerInstance(1, Long.parseLong(courseId)).
                    getMetadata();
            return metadata2.description;
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    ex.getMessage());
            return "";
        }
    }
    
    /**
     * Retorna el títol del metadata actual. 
     * @return String
     */
    public final String getCourseRequirementFORM() {
    	return metadata.requirement;
    }
    
    /**
     * 
     * @param courseId String amb l'identificador del curs.
     * @return
     */
    public final String getCourseRequirement(final String courseId) {
        try {
            // recupera la metadata del curs
             MetadataInformation metadata2 =
            	 CourseAdministrator.getInstance().
                getCourseManagerInstance(1, Long.parseLong(courseId)).
                    getMetadata();
            return metadata2.requirement;
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    ex.getMessage());
            return "";
        }
    }
    
    /**
     * Retorna el títol del metadata actual. 
     * @return String
     */
    public final String getCourseAuthorFORM() {
    	return metadata.author;
    }
    /**
     * 
     * @param courseId String amb l'identificador del curs.
     * @return
     */
    public final String getCourseAuthor(final String courseId) {
        try {
            // recupera la metadata del curs
             MetadataInformation metadata2 =
            	 CourseAdministrator.getInstance().
            	 getCourseManagerInstance(1, Long.parseLong(courseId)).
            	 getMetadata();
            return metadata2.author;
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    ex.getMessage());
            return "";
        }
    }
    
    /**
     * Retorna el títol del metadata actual. 
     * @return String
     */
    public final String getCourseTypicalLearningTimeFORM() {
    	return metadata.typicalLearningTime;
    }
    /**
     * 
     * @param courseId String amb l'identificador del curs.
     * @return
     */
    public final String getCourseTypicalLearningTime(final String courseId) {
        try {
            // recupera la metadata del curs
            MetadataInformation metadata2 =
            	 CourseAdministrator.getInstance().
                 getCourseManagerInstance(1, Long.parseLong(courseId)).
                    getMetadata();
            return metadata2.typicalLearningTime;
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    ex.getMessage());
            return "";
        }
    }
    
    /**
     * Retorna el títol del metadata actual. 
     * @return String
     */
    public final String getCourseEndUserRoleFORM() {
    	return metadata.intendedEndUserRole;
    }
    /**
     * 
     * @param courseId String amb l'identificador del curs.
     * @return
     */
    public final String getCourseEndUserRole(final String courseId) {
        try {
            // recupera la metadata del curs
            MetadataInformation metadata2 = CourseAdministrator.getInstance().
                getCourseManagerInstance(1, Long.parseLong(courseId)).
                    getMetadata();
            return metadata2.intendedEndUserRole;
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    ex.getMessage());
            return "";
        }
    }
    
    /**
     * Retorna el títol del metadata actual. 
     * @return String
     */
    public final String getCourseCopyrightFORM() {
    	return metadata.copyright;
    }
    
    /**
     * 
     * @param courseId String amb l'identificador del curs.
     * @return
     */
    public final String getCourseCopyright(final String courseId) {
        try {
            // recupera la metadata del curs
            MetadataInformation metadata2 =
            	CourseAdministrator.getInstance().
                getCourseManagerInstance(1, Long.parseLong(courseId)).
                    getMetadata();
            return metadata2.copyright;
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    ex.getMessage());
            return "";
        }
    }
    /**
     * Retorna el títol del metadata actual. 
     * @return String
     */
    public final String getCourseEducationalContextFORM() {
    	return metadata.educationalContext;
    }
    
    /**
     * 
     * @param courseId String amb l'identificador del curs.
     * @return
     */
    public final String getCourseEducationalContext(final String courseId) {
        try {
            // recupera la metadata del curs
            MetadataInformation metadata2 =
            	CourseAdministrator.getInstance().
                getCourseManagerInstance(1, Long.parseLong(courseId)).
                    getMetadata();
            return metadata2.educationalContext;
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    ex.getMessage());
            return "";
        }
    }
    /**
     * Retorna el títol del metadata actual. 
     * @return String
     */
    public final String getCourseManifestIdFORM() {
    	return metadata.identifier;
    }
    
    /**
     * 
     * @param courseId String amb l'identificador del curs.
     * @return
     */
    public final String getCourseManifestId(final String courseId) {
        try {
            // recupera la metadata del curs
            MetadataInformation metadata2 =
            	CourseAdministrator.getInstance().
                getCourseManagerInstance(1, Long.parseLong(courseId)).
                    getMetadata();
            return metadata2.identifier;
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    ex.getMessage());
            return "";
        }
    }
    
    /**
     * 
     * @param organizationId
     * @return
     */
    public final String getOrganizationTitle(final String organizationId) {
        try {
            if (agent == null) {
                // crea la petició de l'Agent per consultar les 
                // dades de configuració de l'usuari
                AgentRequest request = new AgentRequest(); 
                // es retorna l'agent amb les dades de l'usuari
                agent = request.getAgent();
            }
            UserObjective userObjective = CourseAdministrator.
                getInstance().getCourseManagerInstance(1, 
                        Long.parseLong(organizationId)).
                            getUser(agent.getId().getIdString());
            
            
            return userObjective.organizationName;
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    ex.getMessage());
            return "";
        } catch (AgentException aex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    Initialization.class.getName(), 
                                    aex.getMessage());
            return "";
        } catch (SharedException e) {
        	LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    Initialization.class.getName(), 
                                    e.getMessage());
            return "";
		}
    }
    
    /**
     * 
     * @param courseId String amb l'identificador del curs.
     * @return
     */
    public List<Integer> getOrganizationsId(final String courseId) {
        try {
            // s'estableix connexió amb la base de dades
            GestorBD databaseManager = GestorBD.getInstance();
            ResultSet rs = databaseManager.getIdOrganizationsQuery(
                        Integer.parseInt(courseId));
            List<Integer> list = new ArrayList<Integer>();
            while (rs.next()) {
                list.add(new Integer(rs.getInt("organization_id")));
            }
            return list;
        } catch (SQLException sqlex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    sqlex.getMessage());
            return null;
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    ex.getMessage());
            return null;
        }
    }
    
    /**
     * 
     * @param organizationId
     * @return
     */
    public final float getCompletedPercentUserOrganization(final String 
            organizationId) {
        try {
        	if (agent == null) {
                // crea la petició de l'Agent per consultar les 
                // dades de configuració de l'usuari
                AgentRequest request = new AgentRequest(); 
                // es retorna l'agent amb les dades de l'usuari
                agent = request.getAgent();
            }
            // s'estableix connexió amb la base de dades
            GestorBD databaseManager = GestorBD.getInstance();
            
            return databaseManager.
                getCompletedPercentUserOrganizationQuery(
                        Integer.parseInt(organizationId), 
                        agent.getId().getIdString());
        } catch (SQLException sqlex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    sqlex.getMessage());
            return -1.0f;
        } catch (Exception ex) {
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    ex.getMessage());
            return -1.0f;
        } catch (AgentException e) {
        	LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    e.getMessage());
            return -1.0f;
		} catch (SharedException e) {
			LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.WARNING, 
                                    JSPUtils.class.getName(), 
                                    e.getMessage());
            return -1.0f;
		}
    }
}
