// $Id: DOMValidate.java,v 1.2 2007/10/30 14:07:42 ecespedes Exp $
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

/** <!-- Javadoc -->
 * $Id: DOMValidate.java,v 1.2 2007/10/30 14:07:42 ecespedes Exp $
 * <b>T�tol:</b> DOMParser <br><br>
 * <b>Descripci�:</b> 
 * <br><br>
 * @author Eduard C�spedes i Borr�s / Enginyeria LaSalle / haduart@gmail.com
 * @version Versi� $Revision: 1.2 $ $Date: 2007/10/30 14:07:42 $
 * $Log: DOMValidate.java,v $
 * Revision 1.2  2007/10/30 14:07:42  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Comen�ant a crear el sistema de gesti� dels cursos:
 * - CourseAdministrator i CourseManager.
 *
 */

import edu.url.lasalle.campus.scorm2004rte.system.Constants;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import org.w3c.dom.Document;

import org.xml.sax.ErrorHandler;
/**
 * Aquesta classe s'encarrega de Validar els documents xml contra 
 * els seus schemas. Si hi ha algun error sint�ctic o sem�ntic que 
 * el llenguatge de l'xml no permet aquest validador ens ho trobar�
 * i ens n'informar�. De lo contrari ens retornar� un 'Document' que
 * no deixa de ser el Node inicial del qual penjar� tota l'estructura
 * d'arbre de l'xml. 
 * 
 * @author ecespedes
 *
 */
public class DOMValidate implements ErrorHandler {
	//Definici� de les constats. 
	/**
	 * Constant String Type, 
	 * "http://apache.org/xml/features/continue-after-fatal-error".
	 */
	public static final String XML_FEATURE_CONTINUE_AFTER_FATAL_ERROR =
		"http://apache.org/xml/features/continue-after-fatal-error";
	/**
	 * Constant String Type, 
	 * "http://apache.org/xml/features/validation/schema-full-checking".
	 */
	public static final String XML_FEATURE_SCHEMA_FULL_CHECKING =
		"http://apache.org/xml/features/validation/schema-full-checking";
	/**
	 * Constant String Type, 
	 * "http://java.sun.com/xml/jaxp/properties/schemaLanguage".
	 */
	public static final String XML_ATTRIBUTE_SCHEMALANGUAGE =
		"http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	/**
	 * Constant String Type, 
	 * "http://www.w3.org/2001/XMLSchema".
	 */
	public static final String XML_ATTRIBUTE_XMLSCHEMA =
		"http://www.w3.org/2001/XMLSchema";
	
	//Variables privades. 
	/**
	 * La 'f�brica' del validador. 
	 * (DocumentBuilderFactory Type, default null)
	 */
	private DocumentBuilderFactory factory = null;	
	/**
	 * �s el document que ens retornar� si tot va b�.
	 * (Document Type, required)
	 */
	private Document document;
	/**
	 * Bole� que ens inidicar� si s'ha validat el Document.
	 * (boolean Type, default false)
	 */
	private boolean validatedDocument = false; 
	
	/**
	 * El constructor per defecte.
	 */
	public DOMValidate() {		
	}
	/**
	 * El constructor per amb un par�metre que ser� on es trobar�
	 * el document XML.
	 * 
	 * @param iXMLFile : String Type
	 */
	public DOMValidate(final String iXMLFile) {
		if (!validateXML(iXMLFile)) {
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] en DOMValidate!");
			}
		}
	}
	/**
	 * El constructor per amb un par�metre que ser� on es trobar�
	 * el document XML.
	 * 
	 * @param iXMLFile : File Type
	 */
	public DOMValidate(final File iXMLFile) {
		if (!validateXML(iXMLFile)) {
			//Hi ha un error al validar
			if (Constants.DEBUG_ERRORS) {
				System.out.println("[ERROR] en DOMValidate!");
			}
		}
	}	
	/** <!-- Javadoc -->
     * <code>isValidated()</code> Ens servir� per saber si el document est� 
     * validat o no. Aix� ser� especialment �til per controlar que no fem un
     * getDocument() i que rebem un null. 
     * 
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap pel moment. 
     * <br />
     * <b>�ltima Modificaci�:</b><br />
     * 2007-09-19
     * 
     * @param void 
     * @return boolean: True si est� validat i False si no ho est�.  
     */
	public final boolean isValidated() {
		return validatedDocument;
	}
	/** <!-- Javadoc -->
     * <code>setValidated()</code> Ens servir� per marcar si el document est� 
     * validat o no. Aix� ser� especialment �til per controlar que no fem un
     * getDocument() i que rebem un null. 
     * 
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap pel moment. 
     * <br />
     * <b>�ltima Modificaci�:</b><br />
     * 2007-09-19
     * 
     * @param validated : True si est� validat i false si no ho est�.
     */
	private void setValidated(final boolean validated) {
		validatedDocument = validated;
	}
	/** <!-- Javadoc -->
     * <code>getDocument()</code> Ens retorna el document que ser� l'estructura 
     * on estar� enmagatzemat tot l'arbre de l'XML. 
     * 
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap pel moment. 
     * <br />
     * <b>�ltima Modificaci�:</b><br />
     * 2007-09-19
     * 
     * @return Document 
     */
	public final Document getDocument() {
		return document;
	}
	/** <!-- Javadoc -->
     * <code>setDocument()</code> Classe p�blica que assigna un document nou. 
     * 
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap pel moment. 
     * <br />
     * <b>�ltima Modificaci�:</b><br />
     * 2007-09-19
     * 
     * @param nouDocument : Document Type 
     */
	public final void setDocument(final Document nouDocument) {
		document = nouDocument;
	}
	/** <!-- Javadoc -->
     * <code>getBuilder()</code> Classe interna que retorna el DocumentBuilder.
     * 
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Arreglar el control d'errors. 
     * <br />
     * <b>�ltima Modificaci�:</b><br />
     * 2007-09-19
     * 
     * @return DocumentBuilder 
     */
	private DocumentBuilder getBuilder() {
		DocumentBuilder builder = null;
		try {
			builder = configParser().newDocumentBuilder();
			builder.setErrorHandler(new DOMValidate());
			return builder;
		} catch (ParserConfigurationException x) {
			x.printStackTrace();
			System.exit(0);
		}
		return null;
	}
	/** <!-- Javadoc -->
     * <code>validateXML()</code> Valida el fitxer xml.
     * 
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap trobat pel moment. 
     * <br />
     * <b>�ltima Modificaci�:</b><br />
     * 2007-09-19
     * 
     * @param iXMLFile : iXMLFile �s el nom del fitxer imssmanifest.xml. 
     * @return boolean : Indicant si la operaci� ha sigut correcta o no. 
     */
	public final boolean validateXML(final String iXMLFile) {
		boolean correct = false;
		setValidated(false);
		try {
			correct = true;
			setDocument(getBuilder().parse(new File(iXMLFile)));
			setValidated(true);
        } catch (IOException x) {
			x.printStackTrace();
		} catch (SAXException x) {
			x.printStackTrace();
		}
		return correct;
	}	
	/** <!-- Javadoc -->
     * <code>validateXML()</code> Valida el fitxer xml.
     * 
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap trobat pel moment. 
     * <br />
     * <b>�ltima Modificaci�:</b><br />
     * 2007-09-19
     * 
     * @param iXMLFile : iXMLFile �s el nom del fitxer imssmanifest.xml. 
     * @return boolean: Indicant si la operaci� ha sigut correcta o no. 
     */
	public final boolean validateXML(final File iXMLFile) {
		boolean correct = false;
		setValidated(false);
		try {
			correct = true;
			setDocument(getBuilder().parse(iXMLFile));
			setValidated(true);
		} catch (IOException x) {
			x.printStackTrace();
		} catch (SAXException x) {
			x.printStackTrace();
		}
		return correct;
	}
	/** <!-- Javadoc -->
     * <code>configParser()</code> Implementa el constructor del parser
     * (DocumentBuilderFactory) configurat per validar el 
     * <code>imssmanifest.xml</code>.
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap trobat pel moment. 
     * <br />
     * <b>�ltima Modificaci�:</b><br />
     * 2007-09-19
     * 
     * @return DocumentBuilderFactory
     */
	public final DocumentBuilderFactory configParser() {        
		if (factory == null) {
			factory = DocumentBuilderFactory.newInstance();
        
			factory.setValidating(true);   
			factory.setNamespaceAware(true);
			try {
				factory.setFeature(XML_FEATURE_CONTINUE_AFTER_FATAL_ERROR,
						false);
				factory.setFeature(XML_FEATURE_SCHEMA_FULL_CHECKING,  true);
			} catch (ParserConfigurationException x) {
				x.printStackTrace();
			}
			factory.setAttribute(XML_ATTRIBUTE_SCHEMALANGUAGE,
					XML_ATTRIBUTE_XMLSCHEMA);
		}
        return factory;
	}
	/** <!-- Javadoc -->
     * <code>warning()</code> Implementa 
     * <code>org.xml.sax.ErrorHandler.warning.</code>
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap trobat pel moment. 
     * <br />
     * <b>�ltima Modificaci�:</b><br />
     * 2007-09-19
     * 
     * @param ex : org.xml.sax.SAXParseException Type
     * @throws org.xml.sax.SAXException :
     */
	public final void warning(final org.xml.sax.SAXParseException ex)
	throws org.xml.sax.SAXException  {
		if (Constants.DEBUG_WARNINGS) {
			System.out.println("\n[WARNING]: " + ex.getMessage());
		}
		ex.printStackTrace();
	}	
	/** <!-- Javadoc -->
     * <code>error()</code> Implementa 
     * <code>org.xml.sax.ErrorHandler.error.</code>
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap trobat pel moment. 
     * <br />
     * <b>�ltima Modificaci�:</b><br />
     * 2007-09-19
     * 
     * @param ex : org.xml.sax.SAXParseException Type
     * @throws org.xml.sax.SAXException :
     */
	public final void error(final org.xml.sax.SAXParseException ex)
	throws org.xml.sax.SAXException  {
		if (Constants.DEBUG_ERRORS) {
			System.out.println("\n[ERROR]: " + ex.getMessage());
		}
		ex.printStackTrace();
	}	
	/** <!-- Javadoc -->
     * <code>fatalError()</code> Implementa 
     * <code>org.xml.sax.ErrorHandler.fatalError.</code>
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap trobat pel moment. 
     * <br />
     * <b>�ltima Modificaci�:</b><br />
     * 2007-09-19
     * 
     * @param ex : org.xml.sax.SAXParseException Type
     * @throws org.xml.sax.SAXException :
     */
	public final void fatalError(final org.xml.sax.SAXParseException ex)  
    throws org.xml.sax.SAXException  { 
		if (Constants.DEBUG_ERRORS) {
			System.out.println("\n[ERROR]Fatal error: " + ex.getMessage());
		}
		ex.printStackTrace();
	}
	
	
	
}


