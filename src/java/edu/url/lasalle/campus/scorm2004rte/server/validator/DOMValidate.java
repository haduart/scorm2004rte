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
 * <b>Títol:</b> DOMParser <br><br>
 * <b>Descripció:</b> 
 * <br><br>
 * @author Eduard Céspedes i Borràs / Enginyeria LaSalle / haduart@gmail.com
 * @version Versió $Revision: 1.2 $ $Date: 2007/10/30 14:07:42 $
 * $Log: DOMValidate.java,v $
 * Revision 1.2  2007/10/30 14:07:42  ecespedes
 * Arreglat tots els bugs relacionats amb l'UserObjective.
 * Començant a crear el sistema de gestió dels cursos:
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
 * els seus schemas. Si hi ha algun error sintàctic o semàntic que 
 * el llenguatge de l'xml no permet aquest validador ens ho trobarà
 * i ens n'informarà. De lo contrari ens retornarà un 'Document' que
 * no deixa de ser el Node inicial del qual penjarà tota l'estructura
 * d'arbre de l'xml. 
 * 
 * @author ecespedes
 *
 */
public class DOMValidate implements ErrorHandler {
	//Definició de les constats. 
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
	 * La 'fàbrica' del validador. 
	 * (DocumentBuilderFactory Type, default null)
	 */
	private DocumentBuilderFactory factory = null;	
	/**
	 * És el document que ens retornarà si tot va bé.
	 * (Document Type, required)
	 */
	private Document document;
	/**
	 * Boleà que ens inidicarà si s'ha validat el Document.
	 * (boolean Type, default false)
	 */
	private boolean validatedDocument = false; 
	
	/**
	 * El constructor per defecte.
	 */
	public DOMValidate() {		
	}
	/**
	 * El constructor per amb un paràmetre que serà on es trobarà
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
	 * El constructor per amb un paràmetre que serà on es trobarà
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
     * <code>isValidated()</code> Ens servirà per saber si el document està 
     * validat o no. Això serà especialment útil per controlar que no fem un
     * getDocument() i que rebem un null. 
     * 
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap pel moment. 
     * <br />
     * <b>Última Modificació:</b><br />
     * 2007-09-19
     * 
     * @param void 
     * @return boolean: True si està validat i False si no ho està.  
     */
	public final boolean isValidated() {
		return validatedDocument;
	}
	/** <!-- Javadoc -->
     * <code>setValidated()</code> Ens servirà per marcar si el document està 
     * validat o no. Això serà especialment útil per controlar que no fem un
     * getDocument() i que rebem un null. 
     * 
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap pel moment. 
     * <br />
     * <b>Última Modificació:</b><br />
     * 2007-09-19
     * 
     * @param validated : True si està validat i false si no ho està.
     */
	private void setValidated(final boolean validated) {
		validatedDocument = validated;
	}
	/** <!-- Javadoc -->
     * <code>getDocument()</code> Ens retorna el document que serà l'estructura 
     * on estarà enmagatzemat tot l'arbre de l'XML. 
     * 
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap pel moment. 
     * <br />
     * <b>Última Modificació:</b><br />
     * 2007-09-19
     * 
     * @return Document 
     */
	public final Document getDocument() {
		return document;
	}
	/** <!-- Javadoc -->
     * <code>setDocument()</code> Classe pública que assigna un document nou. 
     * 
     * <br /><br />
     * <b>Problemas:</b> <br/>
     * Cap pel moment. 
     * <br />
     * <b>Última Modificació:</b><br />
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
     * <b>Última Modificació:</b><br />
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
     * <b>Última Modificació:</b><br />
     * 2007-09-19
     * 
     * @param iXMLFile : iXMLFile és el nom del fitxer imssmanifest.xml. 
     * @return boolean : Indicant si la operació ha sigut correcta o no. 
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
     * <b>Última Modificació:</b><br />
     * 2007-09-19
     * 
     * @param iXMLFile : iXMLFile és el nom del fitxer imssmanifest.xml. 
     * @return boolean: Indicant si la operació ha sigut correcta o no. 
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
     * <b>Última Modificació:</b><br />
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
     * <b>Última Modificació:</b><br />
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
     * <b>Última Modificació:</b><br />
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
     * <b>Última Modificació:</b><br />
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


