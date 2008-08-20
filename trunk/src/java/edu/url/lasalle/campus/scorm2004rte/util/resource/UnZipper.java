// $Id: UnZipper.java,v 1.0 2008/03/13 14:35:42 msegarra Exp $
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

package edu.url.lasalle.campus.scorm2004rte.util.resource;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.Enumeration;

import edu.url.lasalle.campus.scorm2004rte.system.Constants;
import edu.url.lasalle.campus.scorm2004rte.system.JSPUtils;

/**
 * $Id: UnZipper.java,v 1.0 2008/03/13 14:35:42 msegarra Exp $
 *
 * La classe implementa mètodes pre i post sequencing...
 *
 * @author Marc Segarra / Enginyeria La Salle / msegarra@salle.url.edu
 * @version 1.0 $Revision: 1.0 $ $Date: 2008/03/13 14:35:42 $
 * $Log: UnZipper.java,v $
 * Revision 1.0  2008/03/13 14:35:42  msegarra
 * Primera implementació.
 *
 */
public class UnZipper {
    
    /**
     * Descomprimeix els continguts (directoris i fitxers) d'un fitxer ZIP 
     * a la ubicació destí. 
     * @param fileName String amb el nom i path del fitxer ZIP.
     * @param destination String amb el path destí.
     * @return
     */
    public final boolean unzip(final String fileName, final String destination, int maxSize) {
            
        try {
            // buffer per a emmagatzemar temporalment els continguts comprimits 
            // en el fitxer ZIP
            byte[] buffer = new byte[maxSize];
            String tempName = null;
            ZipEntry content = null;
            ZipFile zipFile = new ZipFile(fileName);
            int count = 0;
            
            if (Constants.DEBUG_INFO) {
                showPackageContent(fileName);
            }
            // descomprimeix els continguts del paquet ZIP en el directori destí
            Enumeration < ? extends ZipEntry>contents = zipFile.entries();
            while (contents.hasMoreElements()) {
                content = (ZipEntry) contents.nextElement();
                if (!content.isDirectory()) {
                    tempName = destination + content.getName();
                    tempName = tempName.replace('\\', File.separatorChar);
                    File file = new File(tempName);
                    // si el fitxer està ubicat en un subdirectori del paquet,
                    // es crea el subdirectori en el directori destí si no 
                    // existeix
                    if (file.getParent() != null) {
                        File directory = new File(file.getParent());
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }
                    }
                    // fluxe d'entrada i sortida per a llegir el contingut 
                    // fitxer origen del ZIP i crear el fitxer destí.
                    InputStream in = zipFile.getInputStream(content);
                    OutputStream out = new FileOutputStream(tempName);
                    // crea el nou fitxer destí mitjançant el buffer
                    while ((count = in.read(buffer)) != -1) {
                       out.write(buffer, 0, count);
                    }
                    in.close();
                    out.close();
                }
            }
            return true;
        } catch (ZipException zex) {
            String message = "ERROR [" + UnZipper.class.getName() + "::unzip] Accedint " 
                + "al fitxer zip ubicat en '" + fileName + "' o al directori destí '" 
                + destination + "'.";
            if (Constants.DEBUG_ERRORS) {
                System.err.println(message);
                zex.printStackTrace();
            }
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.SEVERE, 
                                    UnZipper.class.getName(), 
                                    message);
            return false;
        } catch (IOException ioex) {
            String message = "ERROR [" + UnZipper.class.getName() + "::unzip] Accedint " 
                + "al fitxer zip ubicat en '" + fileName + "' o al directori destí '" 
                + destination + "'.";
            if (Constants.DEBUG_ERRORS) {
                System.err.println(message);
                ioex.printStackTrace();
            }
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.SEVERE, 
                                    UnZipper.class.getName(), 
                                    message);
            return false;
        } catch (Exception ex) {
            String message = "ERROR [" + UnZipper.class.getName() + "::unzip] General: " 
                + ex.getMessage();
            if (Constants.DEBUG_ERRORS) {
                System.err.println(message);
                ex.printStackTrace();
            }
            LogControl logControl = LogControl.getInstance();
            logControl.writeMessage(Level.SEVERE, 
                                    UnZipper.class.getName(), 
                                    message);
            return false;
        } 
    }
    
    /**
     * Imprimeix en la consola el contingut d'un fitxer zip.
     * @param fileName String amb el nom i ruta del fitxer zip.
     */
    public final void showPackageContent(final String fileName) {
        
        try {
            System.out.println("**********************************" 
                    + "*********************");
            System.out.println("El paquet '" + fileName 
                    + "' conté aquests fitxers:");
            
            ZipFile zipFile = new ZipFile(fileName);
            Enumeration < ? extends ZipEntry>contents = zipFile.entries();
            while (contents.hasMoreElements()) {
                System.out.println("> " + ((ZipEntry) contents.nextElement())
                        .getName());
            }
            System.out.println("**********************************" 
                    + "*********************");
        } catch (ZipException zex) {
            zex.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
    }
}