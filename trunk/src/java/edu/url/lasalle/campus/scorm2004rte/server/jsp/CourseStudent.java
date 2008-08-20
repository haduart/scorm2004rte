// $Id: CourseStudent.java,v 1.1 2008/01/03 15:11:32 toroleo Exp $
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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * $Id: CourseStudent.java,v 1.1 2008/01/03 15:11:32 toroleo Exp $
 * 
 * CourseStudent.
 * Classe per guardar la informació d'un estudiant 
 * per un curs concret.
 * 
 * Es guarden les dades de:
 * 		-Nom de l'estudiant.
 *		-Identificador de l'estudiant, login.
 * 		-Data de l'última visita.
 * 		-Hora de l'última visita.
 *		-Número de visites que ha fet l'estudiant.
 *		-Array de les dades de l'organització (Identificacor, nom,
 * 		 percentatge de completat).
 *		-Nom de l'últim scorm visitat per l'estudiant.
 *		-Posició, en l'array d'organitzacions, 
 * 		 de l'organització a la que pertany 
 *		 l'ultim scorm visitat.
 * 
 * @author M.Reyes Enginyeria La Salle mtoro@salle.url.edu
 * @version 1.0 02/01/2007 $Revision: 1.1 $ $Date: 2008/01/03 15:11:32 $
 * $Log: CourseStudent.java,v $
 * Revision 1.1  2008/01/03 15:11:32  toroleo
 *  Implementació per guardar la informació
 *  d'un estudiant per un curs concret.
 *
 */
public class CourseStudent implements Comparable<CourseStudent> {

	
	/** Nom de l'estudiant. */
	private String name = "";
	
	/** Identificador de l'estudiant, login. */
	private String idStudent = "";
	
	/** Data de l'última visita. */
	private Date lastDateVisit = null;
	
	/** Hora de l'última visita. */
	private Time lastTimeVisit = null;
	
	/** Número de visites que ha fet l'estudiant. */
	private int numVisits = 0;
	
	/**
	 * Array de les dades de l'organització (Identificacor, nom,
	 * percentatge de completat).
	 * Cada posició d'aquest ArrayList té el format String[] següent
	 * [Identificador, Title, Completat].
	 */
	private ArrayList<String[]> dataOrganization = null;
	
	/** Nom de l'últim scorm visitat per l'estudiant. */
	private String nameScorm = "";
	
	/**
	 * Posició, en l'array d'organitzacions, 
	 * de l'organització a la que pertany 
	 * l'ultim scorm visitat. 
	 */
	private int posOrgScorm = 0;
	
		
	/**
	 * Constructor.
	 *
	 */
	public CourseStudent() {
		
	}
	
	
	/**
	 * Maiye comentarios.
	 * 
	 * @param anCourseStudent Maiye comentarios.
	 * @return Maiye comentarios.
	 */
	public final int compareTo(final CourseStudent anCourseStudent) {
		return this.name.compareTo(anCourseStudent.name);
	}


	/**
	 * @return the dataOrganization
	 */
	public final ArrayList<String[]> getDataOrganization() {
		return dataOrganization;
	}


	/**
	 * @param aDataOrganization the dataOrganization to set
	 */
	public final void setDataOrganization(
				final ArrayList<String[]> aDataOrganization) {
		this.dataOrganization = aDataOrganization;
	}


	/**
	 * @return the idStudent
	 */
	public final String getIdStudent() {
		return idStudent;
	}


	/**
	 * @param anIdStudent the idStudent to set
	 */
	public final void setIdStudent(final String anIdStudent) {
		this.idStudent = anIdStudent;
	}


	/**
	 * @return the lastDateVisit
	 */
	public final Date getLastDateVisit() {
		return lastDateVisit;
	}
	
	/**
	 * @return the lastDateVisit
	 */
	public final String getLastDateVisitAsString() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
		return sdfDate.format(lastDateVisit);
	}

	/**
	 * @param aLastDateVisit the lastDateVisit to set
	 */
	public final void setLastDateVisit(final Date aLastDateVisit) {
		this.lastDateVisit = aLastDateVisit;
	}


	/**
	 * @return the lastTimeVisit
	 */
	public final Time getLastTimeVisit() {
		return lastTimeVisit;
	}

	/**
	 * @return the lastTimeVisit
	 */
	public final String getLastTimeVisitAsString() {
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
		return sdfTime.format(lastTimeVisit);
	}

	/**
	 * @param aLastTimeVisit the lastTimeVisit to set
	 */
	public final void setLastTimeVisit(final Time aLastTimeVisit) {
		this.lastTimeVisit = aLastTimeVisit;
	}


	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}


	/**
	 * @param aName the name to set
	 */
	public final void setName(final String aName) {
		this.name = aName;
	}


	/**
	 * @return the nameScorm
	 */
	public final String getNameScorm() {
		return nameScorm;
	}


	/**
	 * @param aNameScorm the nameScorm to set
	 */
	public final void setNameScorm(final String aNameScorm) {
		this.nameScorm = aNameScorm;
	}


	/**
	 * @return the numVisits
	 */
	public final int getNumVisits() {
		return numVisits;
	}


	/**
	 * @param aNumVisits the numVisits to set
	 */
	public final void setNumVisits(final int aNumVisits) {
		this.numVisits = aNumVisits;
	}


	/**
	 * @return the posOrgScorm
	 */
	public final int getPosOrgScorm() {
		return posOrgScorm;
	}


	/**
	 * @param aPosOrgScorm the posOrgScorm to set
	 */
	public final void setPosOrgScorm(final int aPosOrgScorm) {
		this.posOrgScorm = aPosOrgScorm;
	}
	
	

}
