// $Id: MemorySheduler.java,v 1.3 2008/04/02 14:27:27 ecespedes Exp $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;


/**
* $Id: MemorySheduler.java,v 1.3 2008/04/02 14:27:27 ecespedes Exp $
* <b>Títol:</b> MemorySheduler<br><br>
* <b>Descripció:</b> Aquesta classe fà de temporitzador, avisant als <br>
* CourseManagers cada vegada que passa el temps per tal que aquests <br>
* comprovin el seu estat i d'aquesta manera puguin portar a terme <br>
* optimitzacions de la memòria si no<br> 
* hi ha cap estudiant.<br>
* <br><br> 
*
* @author Eduard Céspedes i Borràs/Enginyeria La Salle/ecespedes@salle.url.edu
* @version Versió $Revision: 1.3 $ $Date: 2008/04/02 14:27:27 $
* $Log: MemorySheduler.java,v $
* Revision 1.3  2008/04/02 14:27:27  ecespedes
* Depurant objectives secundaris
*
* Revision 1.2  2008/02/28 16:15:23  ecespedes
* Millorant el sistema (8)
*
* Revision 1.1  2008/02/25 09:12:58  ecespedes
* *** empty log message ***
*
*/
public final class MemorySheduler extends Observable implements Runnable  {
	/**
	 * Constant Static Final long.
	 *  TEMPS = 3000000
	 *  Em sembla que és mitja hora.
	 */ 
	//private static final long TEMPS = 30000;
	private static final long TEMPS = 3000000;
	/**
	 * Aquesta variable serà accedidida des de l'exterior de manera
	 * que un courseManager podrà indicar-li al MemorySheduler si ha 
	 * de fer anar el Garbage Collector.
	 */
	private Boolean optimize = false;
	 /**
	  * Variable per marcar el temps.
	  */
	private long delay = TEMPS;	 
	/**
	 * És la variable que instanciarà l'objecte de CourseAdministrator.
	 */
	private static MemorySheduler memorySheduler = null;
	
	/**
	 * Aqui guardarem els CourseManagers que hi ha en "actiu" en el sistema. 
	 * De manera que a mida que cada vegada que passi el temps anirem avisant 
	 * als courseManagers perquè facin un testeig sobre els seus usuaris i així 
	 * puguin alliberar memòria en cas de fer falta.
	 */
	ArrayList < CourseManager > observers =
		new ArrayList < CourseManager > ();
	
	
	/**
	 * Variable que representa el Thread actual.
	 */
	private volatile Thread blinker;
	/**
	 * D'aquesta manera intentarem sincronitzar a l'hora d'accedir als 
	 * observers.
	 * 
	 * @return ArrayList < CourseManager > Type.
	 */
	private synchronized ArrayList < CourseManager > getObservers() {
		return observers;
	}
	/**
	 * D'aquesta manera els CourseManager poden activar el Garbage Collector
	 * en cas de que hagi ho necessitin.
	 *
	 */
	public synchronized void activeGarbageCollector() {
		optimize = true;
	}
	/**
	 * Amb aquest mètode afegirem nous CourseManagers al llistat.
	 * Comprovem que no estigui instanciat el CourseManager.
	 * 
	 * @param nouCourseManager : CourseManager Type.
	 * @return boolean Type.
	 */
	public boolean addObserver(final CourseManager nouCourseManager) {
		if (getObservers().isEmpty()) {
			getObservers().add(nouCourseManager);
			return true;
		} else {
			for (Iterator nouIter = getObservers().iterator();
				nouIter.hasNext();) {
				if (nouCourseManager == ((CourseManager) nouIter.next())) {
					return false;
				} //DEL if (nouCourseManager == ((CourseManager)nouIter.next()))
			} //DEL for (Iterator nouIter = observers.iterator()...)
			getObservers().add(nouCourseManager);
			return true;
		} //DEL if (observers.isEmpty())
	}
	/**
	 * Mètode per borrar un CourseManager del Sheduler per tal que l'avisi 
	 * el proper cop.
	 * @param courseManager : CourseManager Type
	 * @return boolean Type.
	 */
	public boolean  removeObserver(final CourseManager courseManager) {
        return getObservers().remove(courseManager);
	}
	/**
	 * Notificarem a tots els Observadors de que ja ha passat el temps.
	 * Nota! Podem modificar el temps del delay amb setDelay(long timeInMilis).
	 */
	public void notifyObservers() {
		
		boolean removed = false;
        Iterator i = getObservers().iterator();        
        while (i.hasNext()) {
        	CourseManager o = (CourseManager) i.next();
        	
        	removed = o.update(this, "AVIS");
        	if (removed) {        		
        		System.out.println("Deletiiiiiiing");        		
        		CourseAdministrator.getInstance().removeCourseManagerInstance(
        				o.dataAccess.getDataAccessID(),
        				o.getIdentifier());
        		i.remove();
        	}        	
        }            
        /**
         * Si hem "netejat" algo aleshores cridarem al Garbage Collector.
         */
        if (optimize) {
        	System.gc();
        	optimize = false;
        }
	}
	/**
	 * D'aquesta manera modifiquem el temps de delay.
	 * 
	 * @param timeInMilis : long Type.
	 */
	public void setDelay(final long timeInMilis) {
		if (timeInMilis != 0) {
			delay = timeInMilis;			
		}				
	}
	/**
	 * Retornem el valor del delay. Per si volem fer alguna comprovació. 
	 * @return long type.
	 */
	public long getDelay() {
		return delay;
	}
	/**
	 * Hem hagut de sobreescriure el mètode start() del java.lang.Thread per 
	 * arrancar el Thread de forma "segura" i així assegurar-nos que quan 
	 * volguem el podrem parar amb la mateixa seguretat.
	 * 
	 * http://java.sun.com/j2se/1.4.2/docs/
	 * 		guide/misc/threadPrimitiveDeprecation.html
	 */
	public void start() {
		if (blinker == null) {
			blinker = new Thread(this);
			blinker.start();      
		}
    }
	/**
	 * Fem el constructor privat, de manera que en el sistema només tindrem
	 * un sheduler.
	 */
	private MemorySheduler() {
	}
	/**
	 * Implementem el mètode públic getInstance per tal de tindre un mètode
	 * públic per instanciar la classe.
	 * @return MemorySheduler Instance.
	 */
	public static synchronized MemorySheduler getInstance() {
		if (memorySheduler == null) {
			memorySheduler = new MemorySheduler();			
		}
		return memorySheduler;
	}
	/**
	 * Mètode stop inplementat per assegurar que el thread s'atura adequadament
	 * i que funciona. No és pot utilitzar directament el mètode .stop() perquè 
	 * està deprecated. 
	 * 
	 * http://java.sun.com/j2se/1.4.2/docs/
	 * 		guide/misc/threadPrimitiveDeprecation.html
	 *
	 */
    public void stopCourseManagerThread() {
    	blinker = null; //blinker.stop();  // UNSAFE!
    }    

    /**
     * Implementa el mètode Run().
     */
	public void run() {
		System.out.println("[MemorySheduler]Run()-preWhile()");
		while (blinker != null) {
			System.out.println("[MemorySheduler]Run()-entry");
			try {				
				blinker.sleep(delay);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			} //DEL catch (InterruptedException e)
			System.out.println("[MemorySheduler]Run()-endLoop:" + delay);
			setChanged();
			notifyObservers();
		} //DEL while (blinker != null)
	} //DEL run()

}
