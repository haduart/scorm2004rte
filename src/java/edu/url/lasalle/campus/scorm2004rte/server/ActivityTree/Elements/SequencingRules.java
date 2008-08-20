// $Id: SequencingRules.java,v 1.4 2008/01/18 18:07:24 ecespedes Exp $
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

package edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/** <!-- Javadoc -->
 * $Id: SequencingRules.java,v 1.4 2008/01/18 18:07:24 ecespedes Exp $
 * <b>Títol:</b> SequencingRules <br><br>
 * <b>Descripció:</b> Aquesta classe implementa guarda les dades<br> 
 * <b>d'una sola regla</b> de seqüenciament.<br><br> 
 * 
 * @see SCORM2004 Sequencing & Navigation 3rt Edition
 * @author Eduard Céspedes i Borràs / LaSalle / ecespedes@salleurl.edu
 * @version Versió $Revision: 1.4 $ $Date: 2008/01/18 18:07:24 $
 * $Log: SequencingRules.java,v $
 * Revision 1.4  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral·lel amb el procés de desenvolupament del gestor de BD.
 *
 * Revision 1.3  2007/11/13 15:59:59  ecespedes
 * Treballant sobre el sistema per "linkar" els Objectives de l'estructura
 * Sequencing de l'arbre amb els Objectives de l'usuari.
 * Millorat TreeAnnotations (step 2 de 3)
 *
 * Revision 1.2  2007/11/12 13:00:08  ecespedes
 * Arreglant elements del seqüenciament.
 * Ja quasi està implementat el TreeAnnotation.
 *
 */
public class SequencingRules implements Serializable {
	
	
	//Definim els tipus -------------------
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7788458916715012844L;


	/**
	 * Aquest element representa la condició que és avaluada.
	 * 
	 * @author Eduard Céspedes i Borràs (AKA ecespedes)
	 */
	public static enum 	conditionRuleType {
		/**
		 * Si és una pre-condició.
		 */
		preConditionRule,
		/**
		 * Si és una post-condició.
		 */
		postConditionRule,
		/**
		 * Si és una condició de sortida.
		 */
		exitConditionRule };
	
	/**
	 * El tipus que ens defineix la forma en la que és combinen
	 * les condicions.
	 * 
	 * @author Eduard Céspedes i Borràs (AKA ecespedes)
	 */
	public static enum 	conditionCombinationType {
		/**
		 * S'han de complir totes les condicions (AND lògica).
		 */
		all,
		/**
		 * S'ha de complir alguna de les condicions (OR lògica).
		 * 
		 */
		any };
	
	/**
	 * Ens defineix el tipus d'acció que s'efectuarà en cas de que la
	 * condició sigui evaluada a certa.
	 * 
	 * @author Eduard Céspedes i Borràs (AKA ecespedes)
	 *
	 */
	public static enum 	ruleActionType {
		/**
		 * skip: Saltem al següent item. 
		 * 
		 * tipus de Condició: preConditionRule
		 */
		skip,
		/**
		 * disabled: És deshabilita l'item.
		 * 
		 * tipus de Condició: preConditionRule
		 */
		disabled,
		/**
		 * hiddenFromChoice: S'amaga perquè no pugui ser triada.
		 * 
		 * tipus de Condició: preConditionRule
		 */
		hiddenFromChoice,
		/**
		 * stopForwardTransversal: The activity will prevent activities
		 * following it (in a preorder traversal of the tree) from being
		 * considered candidates for delivery.
		 *  
		 * tipus de Condició: preConditionRule
		 */
		stopForwardTransversal,
		/**
		 * exit: Surt de manera incondicional.
		 * 
		 * tipus de Condició: exitConditionRule
		 */
		exit,
		/**
		 * Fa que el pare envii un exit.
		 * 
		 * Nota: gens clar!
		 * 
		 * tipus de Condició: postConditionRule
		 */
		exitParent,
		/**
		 * exitAll: Finalitza tot.
		 * tipus de Condició: postConditionRule
		 */
		exitAll,
		/**
		 * retry: Si és un cluster fà que és reinicii, o sigui, 
		 * que és torni a enviar el primer item del cluster.
		 * 
		 * tipus de Condició: postConditionRule
		 */
		retry,
		/**
		 * retryAll: S'envia un exit i un start. O sigui que tornem 
		 * a començar.
		 * 
		 * tipus de Condició: postConditionRule
		 */
		retryAll,
		/**
		 * continue: Continua, seria un "next".
		 * 
		 * tipus de Condició: postConditionRule
		 */
		_continue,
		/**
		 * previous: Envia l'item previ.
		 * 
		 * tipus de Condició: postConditionRule
		 */
		previous };
		
	//Definim les variables -------------------
		
	/**
	 * Ens indicarà el tipus de condició que és: pre/post o exit.
	 */
	public conditionRuleType conditionRule;
	/**
	 * Ens indicarà el tipus de combinació que controla les condicions.
	 * 
	 * required. Default "all".
	 */
	public conditionCombinationType conditionCombination =
		conditionCombinationType.all;
	/**
	 * Ens indica el tipus de condició que s'efectuarà si s'evalua a certa
	 * la condició.
	 * 
	 * required. Default "continue"
	 */
	public ruleActionType ruleAction =
		ruleActionType._continue;
	

	/**
	 * Com que podem tindre 'n' condicions dintre d'un pre/post/ o exit doncs
	 * les guardarem en aquest Collection.
	 */
	public 	Collection < SequencingCondition > condition =
		new ArrayList < SequencingCondition > ();
	
}
