// $Id: scormrte_api.js,v 1.5 2008/03/03 13:00:15 msegarra Exp $
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2007 Enginyeria La Salle. Universitat Ramon Llull.
 * This file is part of SCORM2004RTE.
 *
 * SCORM2004RTE is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details, currently published at 
 * http://www.gnu.org/copyleft/gpl.html or in the gpl.txt in the license folder of this distribution.
 *
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA 02110-1301, USA.
 *
 * Author: Marc Segarra, Enginyeria La Salle, Universitat Ramon Llull
* You may contact the author at msegarra@salle.url.edu
* And the copyright holder at: semipresencial@salle.url.edu | Psg. Bonanova, 8 - 08022 Barcelona
 */

/********************************************************************************
* Aquest fitxer implementa l'API Adapter que permet la comunicació entre el LMS (Learning 
* Management System) i el recurs del tipus SCO (Sharable Content Object).
* L'API està implementada amb classes JavaScript i permet la comunicació asíncrona entre 
* l'aplicació de servidor (el LMS mitjançant sequencingEngine.jsp)  i l'SCO llançat al client.
*
* References: ADL SCORM [www.adlnet.org]
********************************************************************************/
 
// Define exception/error codes
NO_ERROR = "0";
GENERAL_EXCEPTION = "101";
GENERAL_INITIALIZATION_FAILURE = "102";
ALREADY_INITIALIZED = "103";
CONTENT_INSTANCE_TERMINATED = "104";
GENERAL_TERMINATION_FAILURE = "111";
TERMINATION_BEFORE_INITIALIZATION = "112";
TERMINATION_AFTER_TERMINATION = "113";
RECEIVED_DATA_BEFORE_INITIALIZATION = "122";
RECEIVED_DATA_AFTER_TERMINATION = "123";
STORE_DATA_BEFORE_INITIALIZATION = "132";
STORE_DATA_AFTER_TERMINATION = "133";
COMMIT_BEFORE_INITIALIZATION = "142";
COMMIT_AFTER_TERMINATION = "143";
GENERAL_ARGUMENT_ERROR = "201";
GENERAL_GET_FAILURE = "301";
GENERAL_SET_FAILURE = "351";
GENERAL_COMMIT_FAILURE = "391";
UNDEFINED_DATA_MODEL_ELEMENT = "401";
UNIMPLEMENTED_DATA_MODEL_ELEMENT = "402";
DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED = "403";
DATA_MODEL_ELEMENT_IS_READ_ONLY = "404";
DATA_MODEL_ELEMENT_IS_WRITE_ONLY = "405";
DATA_MODEL_ELEMENT_TYPE_MISMATCH = "406";
DATA_MODEL_ELEMENT_VALUE_OUT_OF_RANGE = "407";
GENERAL_LMS_FAILURE = "1000";

/**
 * La classe implementa un validador que comprova que siguin correctes els valors assignats als 
 * elements del model de dades CMI durant les crides SetValue() de l'API.
 *
 * References: 
 *              ADL SCORM [www.adlnet.org]
 *              AICC CMI Data Model [www.aicc.org]
 *              Language Tags [www.w3.org/International/articles/language-tags]
 *              Date and Time Formats [www.w3.org/TR/xmlschema-2/#isoformats]
 *              DateParser (Date parser for ISO 8601 format ) 
 *              [http://dev.w3.org/cvsweb/java/classes/org/w3c/util/]
 *
 * Observations:
 *	- No hi ha gestió dels delimitadors "case_matters", "order_matters", "[.]", "[,]" i "[:]" 
 * 	  usats en l'element cmi.interactions.n.correct_responses.n.pattern del Model de Dades.
 *	  Sample: SetValue(“cmi.interactions.0.correct_responses.0.pattern”, 
 *	  “{order_matters=false}[.]drink coffee[,][.]eat cereal”);
 *	- No s'ha implementat el mètode de validació per als tipus de valor short_identifier_type 
 *	  ja que només s'utilitza per a la col·lecció d'elements del cmi.interactions i aquesta no està
 *	  implementada per aquesta versió.
 */
/*
 * Constructor del validador dels elements del model de dades.
 */
function DataModelValidation() {}

/*
 * isStateType(string name, string value)
 * Inputs: 
 *	name - Un string amb l'element del model de dades.
 *	value - Un string amb el valor a comprovar de l'element.
 * Output: Un booleà per indicar si el valor entrat és del tipus state (true) o no (false).
 *
 * Comprova si el valor entrat és del tipus state.
 */
DataModelValidation.prototype.isStateType = function(name, value) {
	
	if (name == "completion_status") {
		if (value == "completed" || value == "incomplete" || 
			value == "not attempted" || value == "unkown") {
			return true;
		}
		return false;
	} else if (name == "success_status") {
		if (value == "passed" || value == "failed" || 
			value == "unknown") {
			return true;
		}
		return false;
	} else if (name == "exit") {
		if (value == "" || value == "time-out" || 
			value == "suspend" || value == "logout" || 
			value == "normal") {
			return true;
		}
		return false;
	} else if (name == "audio_captioning") {
		if (value == "-1" || value == "0" || value == "1") {
			return true;
		}
		return false;
	} else if (name == "request") {
		if (value == "continue" || value == "previous" || 
			value == "choice" || value == "exit" || 
			value == "exitAll" || value == "abandon" || 
			value == "abandonAll" || value == "_none_") {
			return true;
		} else {
			// comprovar el format del delimitador: {target=<STRING>}<navigation request>
			var pattern = new RegExp(/^\{target=[\w\W]+\}(\contineu|previous|choice|exit|exitAll|abandon|abandonAll|_none_)$/);
			return pattern.test(value);
		}
	}
	return false;
}

/*
 * isCharacterstring250Type(string value)
 * Inputs: 	Una cadena amb el valor a comprovar.
 * Output: 	Un booleà per indicar si el valor entrat és del tipus characterstring - SPM:250 (true) 
 * 		o no (false).
 *
 * Comprova si el valor entrat és del tipus characterstring - SPM:250.
 */
DataModelValidation.prototype.isCharacterstring250Type = function(value) {
	
	if (value.length <= 250) {
		return true;
	}
	return false;
}

/*
 * isCharacterstring1000Type(string value)
 * Inputs: 	Una cadena amb el valor a comprovar.
 * Output: 	Un booleà per indicar si el valor entrat és del tipus characterstring - SPM:1000 (true) 
 * 		o no (false).
 *
 * Comprova si el valor entrat és del tipus characterstring - SPM:1000.
 */
DataModelValidation.prototype.isCharacterstring1000Type = function(value) {
	
	if (value.length <= 1000) {
		return true;
	}
	return false;
}

/*
 * isCharacterstring4000Type(string value)
 * Inputs: 	Una cadena amb el valor a comprovar.
 * Output: 	Un booleà per indicar si el valor entrat és del tipus characterstring - SPM:4000 (true) 
 * 		o no (false).
 *
 * Comprova si el valor entrat és del tipus characterstring - SPM:4000.
 */
DataModelValidation.prototype.isCharacterstring4000Type = function(value) {
	
	if (value.length <= 4000) {
		return true;
	}
	return false;
}

/*
 * isLongIdentifierType(string value)
 * Inputs: 	Una cadena amb el valor a comprovar.
 * Output: 	Un booleà per indicar si el valor entrat és del tipus long_identifier_type (true) o no (false).
 *
 * Comprova si el valor entrat és del tipus characterstring - SPM:4000.
 */
DataModelValidation.prototype.isLongIdentifierType = function(value) {
	
	if (value.length <= 4000) {
		return true;
	}
	return false;
}

/*
 * isIntegerType(string value)
 * Inputs: 	Un string amb el valor a comprovar.
 * Output: 	Un booleà per indicar si el valor entrat és del tipus integer (true) o no (false).
 *
 * Comprova si el valor entrat és del tipus integer.
 */
DataModelValidation.prototype.isIntegerType = function(value) {
	
	var pattern = new RegExp(/^\-?\d+$/);
	if (pattern.test(value)) {
		return true;	
	}
	return false;
}

/*
 * isPositiveIntegerType(string value)
 * Inputs: 	Una cadena amb el valor a comprovar.
 * Output: 	Un booleà per indicar si el valor entrat és del tipus integer_non_negative (true) o no (false).
 *
 * Comprova si el valor entrat és del tipus integer_non_negative.
 */
DataModelValidation.prototype.isPositiveIntegerType = function(value) {
	
	var pattern = new RegExp(/^\d+$/);
	if (pattern.test(value)) {
		return true;	
	}
	return false;
}

/*
 * isRealRangeType(integer min, integer max, string value)
 * Inputs: 
 *	min - Un integer amb el valor mínim del rang.
 *	max - Un integer amb el valor mínim del rang.
 *	value - Un string amb el valor a comprovar.
 * Output: Un booleà per indicar si el valor entrat és del tipus real(10,7) range(X..X) (true) o no (false).
 *
 * Comprova si el valor entrat és del tipus real(10,7) range(X..X).
 */
DataModelValidation.prototype.isRealRangeType = function(min, max, value) {
	
	var pattern = new RegExp(/^\-?\d+\.?\d*$/);
	if (pattern.test(value)) {
        if (min != null && max == null && value >= min) {
			return true;
        } else if (min == null && max != null && value <= max) {
        	return true;
        } else if (min != null && max != null && 
			value >= min && value <= max) {
			return true;
        } else if (min == null && max == null) {
			return true;
		}
	}
	return false;
}

/*
 * isTimeIntervalType(string value)
 * Inputs:	Una cadena amb el valor a comprovar.
 * Output:	Un booleà per indicar si el valor entrat és del tipus timeinterval(second,10,2) (true) o 
 * 		no (false).
 *
 * Comprova si el valor entrat és del tipus timeinterval(second,10,2).
 * Example:	P1Y3M2DT3H (P[yY][mM][dD][T[hH][mM][s[.s]S]])
 */
DataModelValidation.prototype.isTimeIntervalType = function(value) {
	// P[yY][mM][dD][T[hH][mM][s[.s]S]]
	var pattern = new RegExp(/^P(([0-9]+Y)?([0-9]+M)?([0-9]+D)?)?(T([0-9]+H)?([0-9]+M)?([0-9]+(\.[0-9]{1,2})?S)?)?$/);
	if (pattern.test(value)) {
		return true;	
	}
	return false;
}

/*
 * isLanguageType(string value)
 * Inputs:	Una cadena amb el valor a comprovar.
 * Output:	Un booleà per indicar si el valor entrat és del tipus language_type (true) o no (false).
 *
 * Comprova si el valor entrat és del tipus language_type.
 * Attention: S'ha d'implementar de cara al futur.
 *                   Només utilitzat per l'element cmi.learner_preference.language
 */
DataModelValidation.prototype.isLanguageType = function(value) {
	return true;
}

/**
 * La classe implementa un conjunt d'utilitats per manipular els formats de temps i dates segons la 
 * norma ISO8601.
 *
 * References: 
 *              ADL SCORM [www.adlnet.org]
 *              AICC CMI Data Model [www.aicc.org]
 *              Date and Time Formats [www.w3.org/TR/xmlschema-2/#isoformats]
 */
/*
 * Constructor de les utilitats dels formats de temps i dates.
 */
function DateTimeUtils() {
	// Identifies the time that the learner has started the current learner session
	this.startTime = 0;
}

/*
 * initSessionTime()
 *
 * Inicialitza el temps d'inici de la sessió de l'estudiant en un SCO.
 */
DateTimeUtils.prototype.initSessionTime = function() {
	this.startTime = new Date().getTime();
}

/*
 * calculateSessionTime()
 * Output:	Un string amb el temps de la duració de la sessió de l'estudiant segons el format 
 * 		ISO8601.
 * 		Sample: P[yY][mM][dD][T[hH][mM][s[.s]S]]
 *
 * Calcula la duració de la sessió de l'estudiant en un SCO segons la ISO8601.
 *
 * Note: Cal executar initSessionTime() per inicialitzar la variable startTime.
 */
DateTimeUtils.prototype.calculateSessionTime = function() {
	if (this.startTime != 0 ) {
		var currentTime = new Date().getTime();
		var seconds = ((currentTime - this.startTime) / 1000);
		var centiseconds = Math.round(seconds * 100);
		return this.centisecondsToISODuration(centiseconds);
	} else {
		return "PT0H0M0S";
	}
}

/*
 * centisecondsToISODuration(integer centiseconds)
 * Inputs:	Un enter amb el nombre de centisegons a convertir.
 * Output:	Un string amb la conversió de centisegons al format ISO8601 duration.
 *
 * Converteix els centisegons entrats al format ISO8601 duration.
 * Sample: 373304 centiseconds = PT1H2M13.04S
 *
 * Observations:
 * 	-  SCORM i IEEE 1484.11.1 requereixen una precissió en centisegons.
 */
DateTimeUtils.prototype.centisecondsToISODuration = function(centiseconds) {
  	var YEAR_TO_CENTISECONDS = 3155760000;
	var MONTH_TO_CENTISECONDS = 262980000;
	var DAY_TO_CENTISECONDS = 8640000;
	var HOUR_TO_CENTISECONDS = 360000;
	var MINUTE_TO_CENTISECONDS = 6000;
	var SECONDS_TO_CENTISECONDS = 100;
  	var years = 0, months = 0, days = 0, hours = 0, minutes = 0, 
		seconds = 0;
	var duration = "P";
  	
	// A partir dels centisegons es calculen els anys, messos, dies, hores, minuts i segons
	years = Math.floor(centiseconds / YEAR_TO_CENTISECONDS);
	centiseconds -= years * YEAR_TO_CENTISECONDS;
	months = Math.floor(centiseconds / MONTH_TO_CENTISECONDS);
	centiseconds -= months * MONTH_TO_CENTISECONDS;
	days = Math.floor(centiseconds / DAY_TO_CENTISECONDS);
	centiseconds -= days * DAY_TO_CENTISECONDS;
	hours = Math.floor(centiseconds / HOUR_TO_CENTISECONDS);
    centiseconds -= hours * HOUR_TO_CENTISECONDS;
    minutes = Math.floor(centiseconds / MINUTE_TO_CENTISECONDS);
    centiseconds -= minutes * MINUTE_TO_CENTISECONDS;
	seconds = Math.floor(centiseconds / SECONDS_TO_CENTISECONDS);
	
  	// Construeix l'string duration segons el format de temps ISO8601 duration
  	if (years > 0) { 
		duration += years + "Y";
	}
  	if (months > 0) {
		duration += months + "M";
	}
  	if (days > 0) { 
		duration += days + "D";
	}
  	if (hours > 0 || minutes > 0 || centiseconds > 0) {
    	duration += "T";
    	if (hours > 0) { 
			duration += hours + "H";
		}
    	if (minutes > 0) {
			duration += minutes + "M";
		}
    	if (centiseconds > 0) { 
			duration += seconds + "S";
		}
  	}
  	if (duration == "P") { 
		duration = "PT0H0M0S";
	}
	return duration;
}

/**
 * La classe implementa l'estructura d'un objectiu del model de dades CMI. 
 *
 * References: 
 *              ADL SCORM [www.adlnet.org]
 *              AICC CMI Data Model [www.aicc.org]
 */
/*
 * Constructor que inicialitza els atributs de l'element cmi.objective.
 */
function Objective() {
	this.id = "";
	this.score_raw = "";
	this.score_min = "";
	this.score_max = "";
	this.score_scaled = "";
	this.success_status = "unknown";
	this.completion_status = "unknown";
	this.progress_measure = "";
	this.description = "";
}

/**
 * La classe implementa els elements del model de dades CMI 1.0 i SCORM 2004 ADL Navigation. 
 *
 * References: 
 *              ADL SCORM [www.adlnet.org]
 *              AICC CMI Data Model [www.aicc.org]
 *
 * Observations:
 *	- No implementa la categoria cmi.interactions ni els seus respectius elements.
 *	- Tampoc implementa les categories comments_from_learner ni ccomments_from_lms.
 */
/*
 * Constructor que inicialitza tots els elements del model de dades.
 */
function DataModel() {
	// CMI Data Model
	this.completion_status = "unknown";
	this.completion_threshold = "";
	this.credit = "credit";
	this.entry = "ab-initio";
	this.exit = "";
	this.launch_data = "";
	this.learner_id = "";
	this.learner_name = "";
	this.audio_level = "1";
	this.language = "";
	this.delivery_speed = "1";
	this.audio_captioning = "0";
	this.location = "";
	this.max_time_allowed = "";
	this.mode = "normal";
	this.progress_measure = "";
	this.scaled_passing_score = "";
	this.score_raw = "";
	this.score_min = "";
	this.score_max = "";
	this.score_scaled = "";
	this.session_time = "PT0H0M0S";
	this.success_status = "unknown";
	this.suspend_data = "";
	this.time_limit_action = "continue,no message";
	this.total_time = "PT0H0M0S";
	this.objectives = new Array();
	// CMI Data Model Version
	this.DATA_MODEL_VERSION = "1.0";
	// ADL Navigation Data Model
	this.request = "continue"; //"_none_";
	this.request_continue = "unknown";
	this.request_previous = "unknown";
	this.request_choice = "unknown";
	// Validator
	this.DataModelValidation = new DataModelValidation();
}

/*
 * isValide(string type, string value, string name)
 * Inputs: 
 *	type - Un string amb el tipus del valor de l'element a validar.
 *	value - Un string amb el valor de l'element a validar.
 *	name - Un string amb el nom de l'element a validar.
 * Output:	Un booleà per indicar si el valor entrat d'un element del model de dades és válid (true) 
 * 		o no (false).
 *
 * Comprova si el valor entrat d'un element del model de dades és vàlid.
 */
DataModel.prototype.isValide = function(type, value, name) {
	
	if (type == "state") {
		return this.DataModelValidation.isStateType(name, value);
	}
	if (type == "integer") {
		return this.DataModelValidation.isIntegerType(value);
	}
	if (type == "integer_non_negative") {
		return this.DataModelValidation.isPositiveIntegerType(value);
	}
	if (type == "characterstring [SPM:250]") {
		return this.DataModelValidation.isCharacterstring250Type(value);
	}
	if (type == "characterstring [SPM:1000]") {
		return this.DataModelValidation.isCharacterstring1000Type(value);
	}
	if (type == "characterstring [SPM:4000]") {
		return this.DataModelValidation.isCharacterstring4000Type(value);
	}
	if (type == "long_identifier_type") {
		return this.DataModelValidation.isLongIdentifierType(value);
	}
	if (type == "real(10,7) range(0..N)") {
		return this.DataModelValidation.isRealRangeType(0, null, value);
	}
	if (type == "real(10,7) range(0..1)") {
		return this.DataModelValidation.isRealRangeType(0, 1, value);
	}
	if (type == "real(10,7) range(N..N)") {
		return this.DataModelValidation.isRealRangeType(null, null, value);
	}
	if (type == "real(10,7) range(-1..1)") {
		return this.DataModelValidation.isRealRangeType(-1, 1, value);
	}
	if (type == "timeinterval(second,10,2)") {
		return this.DataModelValidation.isTimeIntervalType(value);
	}
	if (type == "language_type") {
		return this.DataModelValidation.isLanguageType(value);
	}
	return false;
}

/**
 * La classe implementa un contenidor per als codis d'error. 
 *
 * References: 
 *              ADL SCORM [www.adlnet.org]
 */
function Error() {
	this.title = "";
	this.description = "";
}

/**
 * La classe implementa els codis d'error de l'API Adapter. 
 *
 * Observations: 
 * 	- Els valors de l'array dels codis d'error s'inicialitzaran en el JSP on es carregui l'API.
 */
function ErrorCodes() {
	this.ERRORS_NUMBER = 26;
	this.error = new Array(this.ERRORS_NUMBER);
}

/**
 * La classe implementa l'API Adapter per a la comunicació entre el LMS i els SCOs. 
 *
 * References: 
 *              ADL SCORM [www.adlnet.org]
 */
/*
 * Constructor que inicialitza els atributs de l'API Adapter.
 */	
function API() {
	this.initialized = false;
	this.terminated = false;
	this.error = NO_ERROR;
	// Data Model Elements
	this.dataModel = new DataModel();
	// Error Codes
	this.errorCodes = new ErrorCodes();
	// Parameters for the sequencing
	this.parameters = new Array();
	// Rsource type
	this.type = "unknown";
	this.orgId;
	this.courseId;
	// Enabled items list for implementation of "adl.nav.request_valid.choice" element
	this.enabledItems = "";
	// Date and time utils
	this.utils = new DateTimeUtils();
}

/*
 * init()
 *
 * Inicialitza els atributs de l'API Adapter cada vegada que el LMS llança per pantalla un 
 * objecte d'aprenentatge.
 */
API.prototype.init = function() {
	this.initialized = false;
	this.terminated = false;
	this.error = NO_ERROR;
	this.dataModel = new DataModel();
	this.parameters = new Array();
	this.type = "asset";
	this.utils = new DateTimeUtils();
	// inicialitza el temps d'inici de la sessió de l'estudiant
	this.utils.initSessionTime();
}

/*
 * Initialize(string param)
 * Inputs:	Una cadena buida.
 * Output:	Un booleà per indicar si s'ha iniciat amb èxit (true) la comunicació entre l'SCO i 
 * 		l'API Adapter o no (false).
 *
 * Mètode invocat per l'SCO per iniciar la comunicació amb l'API Adapter.
 */
API.prototype.Initialize = function(param) {
	var result = "false";
	this.type = "sco";
	
	if (this.terminated) {
		// error 104 - contingut finalitzat
		this.error = CONTENT_INSTANCE_TERMINATED;
	} else {
		if (this.initialized) {
			// error 103 - ja inicialitzat
			this.error = ALREADY_INITIALIZED;
		} else {
			if (param != null && param != "") {
				// error 201 - paràmetre invàlid
				this.error = GENERAL_ARGUMENT_ERROR; 
			} else {
				this.initialized = true;
				result = "true";
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
			}
		}
	}
	return result;
}

/*
 * Terminate(string param)
 * Inputs:	Una cadena buida.
 * Output:	Un booleà per indicar si s'ha finalitzat amb èxit la comunicació entre l'SCO i l'API (true)
 * 		o no (false).
 *
 * Indica a l'API Adapter que l'SCO vol finalitzar la comunicació amb el LMS.
 */
API.prototype.Terminate = function(param) {
	var result = "false";

	if (this.terminated) {
		// error 113 - ja finalitzat
		this.error = TERMINATION_AFTER_TERMINATION;
	} else {
		if (!this.initialized) {
			// error 112 - no inicialitzat
			this.error = TERMINATION_BEFORE_INITIALIZATION;
		} else {
			if (param != null && param != "") {
				// error 201 - paràmetre invàlid
				this.error = GENERAL_ARGUMENT_ERROR; 
			} else {
				if (this.dataModel.exit == "suspend") {
					this.dataModel.entry = "resume";
				} else {
					this.dataModel.entry = "";
				}							
				// l'API calcula la duració de la sessió de l'estudiant en l'SCO en el cas que l'SCO
				// no ho hagi fet
				if (this.dataModel.session_time == "PT0H0M0S") {
					this.dataModel.session_time = 
						this.utils.calculateSessionTime();
				}
				// emmagatzema els elements del seguiment de l'estudiant en l'SCO en un array  per 
				// ser enviats com a paràmetres al sequencing engine JSP
				this.parameters.push("org=" + this.orgId);
				this.parameters.push("course=" + this.courseId);
				this.parameters.push("type=" + this.type);
				this.parameters.push("completion_status=" + this.dataModel.completion_status);
				this.parameters.push("entry=" + this.dataModel.entry);
				this.parameters.push("exit=" + this.dataModel.exit);
				this.parameters.push("audio_level=" + this.dataModel.audio_level);
				this.parameters.push("language=" + this.dataModel.language);
				this.parameters.push("delivery_speed=" + this.dataModel.delivery_speed);
				this.parameters.push("audio_captioning=" + this.dataModel.audio_captioning);
				this.parameters.push("location=" + this.dataModel.location);
				this.parameters.push("mode=" + this.dataModel.mode);
				this.parameters.push("progress_measure=" + this.dataModel.progress_measure);
				this.parameters.push("score.raw=" + this.dataModel.score_raw);
				this.parameters.push("score.min=" + this.dataModel.score_min);
				this.parameters.push("score.max=" + this.dataModel.score_max);
				this.parameters.push("score.scaled=" + this.dataModel.score_scaled);
				this.parameters.push("session_time=" + this.dataModel.session_time);
				this.parameters.push("success_status=" + this.dataModel.success_status);
				this.parameters.push("suspend_data=" + this.dataModel.suspend_data);
				this.parameters.push("learner_id=" + this.dataModel.learner_id);
				this.parameters.push("learner_name=" + this.dataModel.learner_name);
				this.parameters.push("request=" + this.dataModel.request);
				this.parameters.push("num_obj=" + this.dataModel.objectives.length);
				if (this.dataModel.objectives.length > 0) {
					for (var i = 0; i < this.dataModel.objectives.length; i++) {
						this.parameters.push("obj." + i + ".id=" + this.dataModel.objectives[i].id);
						this.parameters.push("obj." + i + ".raw=" + this.dataModel.objectives[i].score_raw);
						this.parameters.push("obj." + i + ".min=" + this.dataModel.objectives[i].score_min);
						this.parameters.push("obj." + i + ".max=" + this.dataModel.objectives[i].score_max);
						this.parameters.push("obj." + i + ".scaled=" + this.dataModel.objectives[i].score_scaled);
						this.parameters.push("obj." + i + ".success=" + this.dataModel.objectives[i].success_status);
						this.parameters.push("obj." + i + ".completion=" + this.dataModel.objectives[i].completion_status);
						this.parameters.push("obj." + i + ".progress=" + this.dataModel.objectives[i].progress_measure);
						this.parameters.push("obj." + i + ".description=" + this.dataModel.objectives[i].description);
					}
				}
				
				this.terminated = true;
				result = "true";
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
			
				// invoca el sequencing engine i envia les dades del seguiment de l'usuari a l'SCO
				var iframe = window.document.getElementById("adlIframe");
				this.launchSNEngine(iframe, this.parameters.join("&"));
			}
		}
	}
	return result;
}

/*
 * launchSNEngine(string iframe, string parameters)
 * Inputs: 
 *	iframe - Un string amb la referència de l'iframe del contingut on s'invoca el seqüenciament.
 *	parameters - Un array amb els paràmetres del seguiment de l'SCO per ser enviats al 
 * 		motor de seqüenciament.
 *
 * Invoca el motor de seqüenciament perquè llanci el pròxim SCO.
 */
API.prototype.launchSNEngine = function(iframe, parameters) {
	setTimeout(function() { iframe.src = "sequencingEngine.jsp?" 
		+ parameters; }, 1000);
}

/*
 * Commit(string param)
 * Inputs:	Una cadena buida.
 * Output:	Un booleà per indicar si s'ha realitzat amb èxit l'emmagatzematge
 * 		de les dades de l'SCO al LMS (true) o no (false). 
 *
 * Indica a l'API Adapter que emmagatzemi persistentment les dades de l'SCO a la BDD.
 *
 * Observations:
 *	- Seguint l'especificació al peu de la lletra, hauria d'enviar al LMS les dades del model de 
 *	  dades per ésser emmagatzemades, però, en aquesta implementació, això és realitzarà quan 
 * 	  l'SCO finalitzi la comunicació amb l'API.
 */
API.prototype.Commit = function(param) {
	var result = "false";

	if (this.terminated) {
		// error 143 - intentar emmagatzemar dades després d'haver finalitzat
		this.error = COMMIT_AFTER_TERMINATION;
	} else {
		if (!this.initialized) {
			// error 142 - no s'ha inicialitzat i no pot emmagatzemar
			this.error = COMMIT_BEFORE_INITIALIZATION;
		} else {
			if (param != null && param != "") {
				// error 201 - paràmetre invàlid
				this.error = GENERAL_ARGUMENT_ERROR; 
			} else { 
				result = "true";
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
			}
		}
	}

	return result;
}
	
/*
 * SetValue(string name, string value)
 * Inputs: 
 *	name - Un string amb el nom de la categoria o element.
 *	value - Un string amb el valor que s'assigna a l'element o categoria.
 * Output:	Un booleà per indicar si s'ha realitzat correctament la petició de l'assignació (true) 
 * 		o no (false).
 *
 * Envia el valor de l'element assignat al model de dades.
 */	
API.prototype.SetValue = function(name, value) {	
	var result = "false";
	
	if (this.terminated) {
		// error 133 - intentar assignar després d'haver finalitzat
		this.error = STORE_DATA_AFTER_TERMINATION;
	} else if (!this.initialized) {
		// error 132 - no s'ha inicialitzat i no es pot assignar
		this.error = STORE_DATA_BEFORE_INITIALIZATION;
	} else if (name == null || name == "" || value == null) {
		// error 201 - paràmetre invàlid
		this.error = GENERAL_ARGUMENT_ERROR; 
	} else { 
		// emmagatzema els membres de la petició en l'ordre seqüencial en que es processa
		var members = name.split(".");
		if (members.length > 1 && (members[0] == "cmi" || members[0] == "adl")) {
			// valida el valor de l'element i si és correcte, assigna aquest valor a l'element del model de dades CMI
			if (members[1] == "completion_status") {
				if (this.dataModel.isValide("state", value, members[1])) {
					this.dataModel.completion_status = value;
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return "true";
				}
				// error 406 - tipus de l'element incorrecte 
				this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
				return "false";
			} else if (members[1] == "exit") {
				if (this.dataModel.isValide("state", value, members[1])) {
					this.dataModel.exit = value;
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return "true";
				}
				// error 406 - tipus de l'element incorrecte 
				this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
				return "false";
			} else if (members[1] == "location") {
				if (this.dataModel.isValide("characterstring [SPM:1000]", 
						value, members[1])) {
					this.dataModel.location = value;
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return "true";
				}
				// error 406 - tipus de l'element incorrecte 
				this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
				return "false";
			} else if (members[1] == "progress_measure") {
				if (this.dataModel.isValide("real(10,7) range(0..1)", 
						value, members[1])) {
					this.dataModel.progress_measure = value;
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return "true";
				}
				// error 406 - tipus de l'element incorrecte 
				this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
				return "false";
			} else if (members[1] == "session_time") {
				if (this.dataModel.isValide("timeinterval(second,10,2)", 
						value, members[1])) {
					this.dataModel.session_time = value;
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return "true";
				}
				// error 406 - tipus de l'element incorrecte 
				this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
				return "false";
			} else if (members[1] == "success_status") {
				if (this.dataModel.isValide("state", value, members[1])) {
					this.dataModel.success_status = value;
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return "true";
				}
				// error 406 - tipus de l'element incorrecte 
				this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
				return "false";
			} else if (members[1] == "suspend_data") {
				if (this.dataModel.isValide("characterstring [SPM:4000]", 
						value, members[1])) {
					this.dataModel.suspend_data = value;
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return "true";
				}
				// error 406 - tipus de l'element incorrecte 
				this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
				return "false";
			} else if (members[1] == "objectives" && members.length > 3) {
				if (this.dataModel.isValide("integer_non_negative", 
						members[2], null)) {
					if (members[2] < this.dataModel.objectives.length) {
						if (members.length > 4) {
							return this.setObjectiveValue(members[3] + "." + 
								members[4], value, members[2]);
						} else {
							return this.setObjectiveValue(members[3], 
								value, members[2]);
						}
					} else {
						// error 407 - valor consultat fora de rang
						this.error = DATA_MODEL_ELEMENT_VALUE_OUT_OF_RANGE;
					}
				} else {
					// error 406 - tipus de l'element incorrecte 
					this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
				}
				return "false";
			} else if (members[1] == "learner_preference" && members.length > 2) {
				if (members[2] == "audio_level") {
					if (this.dataModel.isValide("real(10,7) range(0..N)", 
							value, members[2])) {
						this.dataModel.audio_level = value;
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
						return "true";
					}
					// error 406 - tipus de l'element incorrecte 
					this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
					return "false";	
				} else if (members[2] == "language") {
					if (this.dataModel.isValide("language_type", value, 
							members[2])) {
						this.dataModel.language = value;
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
						return "true";
					}
					// error 406 - tipus de l'element incorrecte 
					this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
					return "false";
				} else if (members[2] == "delivery_speed") {
					if (this.dataModel.isValide("real(10,7) range(0..N)", 
							value, members[2])) {
						this.dataModel.delivery_speed = value;
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
						return "true";
					}
					// error 406 - tipus de l'element incorrecte 
					this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
					return "false";
				} else if (members[2] == "audio_captioning") {
					if (this.dataModel.isValide("state", value, members[2])) {
						this.dataModel.audio_captioning = value;
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
						return "true";
					}
					// error 406 - tipus de l'element incorrecte 
					this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
					return "false";
				}
			} else if (members[1] == "score" && members.length > 2) {
				if (members[2] == "raw") {
					if (this.dataModel.isValide("real(10,7) range(N..N)", 
							value, members[2])) {
						this.dataModel.score_raw = value;
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
						return "true";
					}
					// error 406 - tipus de l'element incorrecte 
					this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
					return "false";
				} else if (members[2] == "min") {
					if (this.dataModel.isValide("real(10,7) range(N..N)", 
							value, members[2])) {
						this.dataModel.score_min = value;
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
						return "true";
					}
					// error 406 - tipus de l'element incorrecte 
					this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
					return "false";
				} else if (members[2] == "max") {
					if (this.dataModel.isValide("real(10,7) range(N..N)", 
							value, members[2])) {
						this.dataModel.score_max = value;
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
						return "true";
					}
					// error 406 - tipus de l'element incorrecte 
					this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
					return "false";
				} else if (members[2] == "scaled") {
					if (this.dataModel.isValide("real(10,7) range(-1..1)", 
							value, members[2])) {
						this.dataModel.score_scaled = value;
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
						return "true";
					}
					// error 406 - tipus de l'element incorrecte 
					this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
					return "false";
				}
			} else if (members[1] == "nav" && members.length > 2) {
				if (members[2] == "request") {
					if (this.dataModel.isValide("state", value, members[2])) {
						this.dataModel.request = value;
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
						return "true";
					}
					// error 406 - tipus de l'element incorrecte 
					this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
					return "false";
				}
			} else if (members[1] == "completion_threshold" || 
					members[1] == "credit" || 
					members[1] == "entry" ||
					members[1] == "launch_data" ||
					members[1] == "learner_id" ||
					members[1] == "learner_name" ||
					members[1] == "max_time_allowed" ||
					members[1] == "mode" ||
					members[1] == "scaled_passing_score" ||
					members[1] == "time_limit_action" ||
					members[1] == "total_time") {
				// error 404 - element només de lectura
				this.error = DATA_MODEL_ELEMENT_IS_READ_ONLY;
				return "false";
			}
			// error 401 - element no definit
			this.error = UNDEFINED_DATA_MODEL_ELEMENT;
		}
	}
	return result;
}

/*
 * setObjectiveValue(string name, string value, int index)
 * Inputs: 
 *	name - Un string amb el nom de la categoria o element.
 *	value - Un string amb el valor que s'assigna a l'element o categoria.
 *	index - Un integer amb l'índex de l'array d'objectius.
 * Output: Un booleà per indicar si s'ha produït l'assignació (true) o no (false).
 *
 * Assigna un valor a un element de la categoria cmi.objective.
 *
 * Observations:
 * 	- Falta implementar l'element ID correctament. Per ara no es permet afegir nous objectius des 
 *	  d'un SCO, només consultar els objectius existents en el LMS definits en el manifest.
 */
API.prototype.setObjectiveValue = function(name, value, index) {
	var result = "false";
	
	if (name == "score.raw") {
		if (this.dataModel.isValide("real(10,7) range(N..N)", value, name)) {
			this.dataModel.objectives[index].score_raw = value;
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
			return "true";
		}
		// error 406 - tipus de l'element incorrecte 
		this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
		return "false";
	} else if (name == "score.min") {
		if (this.dataModel.isValide("real(10,7) range(N..N)", value, name)) {
			this.dataModel.objectives[index].score_min = value;
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
			return "true";
		}
		// error 406 - tipus de l'element incorrecte 
		this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
		return "false";
	} else if (name == "score.max") {
		if (this.dataModel.isValide("real(10,7) range(N..N)", value, name)) {
			this.dataModel.objectives[index].score_max = value;
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
			return "true";
		}
		// error 406 - tipus de l'element incorrecte 
		this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
		return "false";
	} else if (name == "score.scaled") {
		if (this.dataModel.isValide("real(10,7) range(-1..1)", value, name)) {
			this.dataModel.objectives[index].score_scaled = value;
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
			return "true";
		}
		// error 406 - tipus de l'element incorrecte 
		this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
		return "false";
	} else if (name == "success_status") {
		if (this.dataModel.isValide("state", value, name)) {
			this.dataModel.objectives[index].success_status = value;
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
			return "true";
		}
		// error 406 - tipus de l'element incorrecte 
		this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
		return "false";
	} else if (name == "completion_status") {
		if (this.dataModel.isValide("state", value, name)) {
			this.dataModel.objectives[index].completion_status = value;
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
			return "true";
		}
		// error 406 - tipus de l'element incorrecte 
		this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
		return "false";
	} else if (name == "progress_measure") {
		if (this.dataModel.isValide("real(10,7) range(0..1)", value, name)) {
			this.dataModel.objectives[index].progress_measure = value;
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
			return "true";
		}
		// error 406 - tipus de l'element incorrecte 
		this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
		return "false";
	} else if (name == "description") {
		if (this.dataModel.isValide("characterstring [SPM:250]", value, name)) {
			this.dataModel.objectives[index].description = value;
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
			return "true";
		}
		// error 406 - tipus de l'element incorrecte 
		this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
		return "false";
	} else if (name == "id") {
		/*
		if (this.dataModel.isValide("long_identifier_type", value, name)) {
			this.dataModel.objectives[index].id = value;
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
			return "true";
		}
		
		// error 406 - tipus de l'element incorrecte 
		this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
		return "false";
		*/
		return "true";
	}
	// error 401 - element no definit
	this.error = UNDEFINED_DATA_MODEL_ELEMENT;
	
	return result;
}

/*
 * GetValue(string name)
 * Inputs: 	Un string amb el nom de la categoria o element.
 * Output:	Un string amb el valor de l'element sol·licitat si s'ha realitzat la petició de la consulta
 * 		amb èxit.
 *
 * Consulta el valor de l'element del model de dades sol·licitat en la petició.
 */
API.prototype.GetValue = function(name) {
	var result = "";
	
	if (this.terminated) {
		// error 123 - intentar consultar després d'haver finalitzat
		this.error = RECEIVED_DATA_AFTER_TERMINATION;
	} else if (!this.initialized) {
		// error 122 - no s'ha inicialitzat i no es pot consultar
		this.error = RECEIVED_DATA_BEFORE_INITIALIZATION;
	} else if (name == null || name == "") {
		// error 201 - paràmetre invàlid
		this.error = GENERAL_ARGUMENT_ERROR; 
	} else {
		// emmagatzema els membres de la petició en l'ordre sequencial en que es processa
		var members = name.split(".");
		if (members.length > 1 && (members[0] == "cmi" || members[0] == "adl")) {
			// retorna el valor de l'element del model de dades CMI consultat
			if (members[1] == "_version") {
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
				return this.dataModel.DATA_MODEL_VERSION;
			} else if (members[1] == "completion_status") {
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
				return this.dataModel.completion_status;
			} else if (members[1] == "completion_threshold") {
				if (this.dataModel.completion_threshold == "") {
					// error 403 - element no inicialitzat
					this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
				} else {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
				}	
				return this.dataModel.completion_threshold;
			} else if (members[1] == "credit") {
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
				return this.dataModel.credit;
			} else if (members[1] == "entry") {
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
				return this.dataModel.entry;
			} else if (members[1] == "exit" || 
					members[1] == "session_time") {
				// error 405 - element només d'escriptura 
				this.error = DATA_MODEL_ELEMENT_IS_WRITE_ONLY;
				return "";
			} else if (members[1] == "launch_data") {
				if (this.dataModel.launch_data == "") {
					// error 403 - element no inicialitzat
					this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
				} else {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
				}	
				return this.dataModel.launch_data;
			} else if (members[1] == "learner_id") {
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
				return this.dataModel.learner_id;
			} else if (members[1] == "learner_name") {
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
				return this.dataModel.learner_name;
			} else if (members[1] == "location") {
				if (this.dataModel.location == "") {
					// error 403 - element no inicialitzat
					this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
				} else {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
				}	
				return this.dataModel.location;
			} else if (members[1] == "max_time_allowed") {
				if (this.dataModel.max_time_allowed == "") {
					// error 403 - element no inicialitzat
					this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
				} else {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
				}	
				return this.dataModel.max_time_allowed;
			} else if (members[1] == "mode") {
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
				return this.dataModel.mode;
			} else if (members[1] == "progress_measure") {
				if (this.dataModel.progress_measure == "") {
					// error 403 - element no inicialitzat
					this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
				} else {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
				}	
				return this.dataModel.progress_measure;
			} else if (members[1] == "scaled_passing_score") {
				if (this.dataModel.scaled_passing_score == "") {
					// error 403 - element no inicialitzat
					this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
				} else {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
				}	
				return this.dataModel.scaled_passing_score;
			} else if (members[1] == "success_status") {
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
				return this.dataModel.success_status;
			} else if (members[1] == "suspend_data") {
				if (this.dataModel.suspend_data == "") {
					// error 403 - element no inicialitzat
					this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
				} else {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
				}	
				return this.dataModel.suspend_data;
			} else if (members[1] == "time_limit_action") {
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
				return this.dataModel.time_limit_action;
			} else if (members[1] == "total_time") {
				// error 0 - no s'ha produït cap error
				this.error = NO_ERROR;
				return this.dataModel.total_time;
			} else if (members[1] == "learner_preference" && members.length > 2) {
				if (members[2] == "_children") {	
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return "audio_level,language,delivery_speed,audio_captioning";
				} else if (members[2] == "audio_level") {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return this.dataModel.audio_level;
				} else if (members[2] == "language") {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return this.dataModel.language;
				} else if (members[2] == "delivery_speed") {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return this.dataModel.delivery_speed;
				} else if (members[2] == "audio_captioning") {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return this.dataModel.audio_captioning;
				}
			} else if (members[1] == "score" && members.length > 2) {
				if (members[2] == "_children") {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return "raw,min,max,scaled";
				} else if (members[2] == "raw") {
					if (this.dataModel.score_raw == "") {
						// error 403 - element no inicialitzat
						this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
					} else {
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
					}	
					return this.dataModel.score_raw;
				} else if (members[2] == "min") {
					if (this.dataModel.score_min == "") {
						// error 403 - element no inicialitzat
						this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
					} else {
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
					}	
					return this.dataModel.score_min;
				} else if (members[2] == "max") {
					if (this.dataModel.score_max == "") {
						// error 403 - element no inicialitzat
						this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
					} else {
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
					}	
					return this.dataModel.score_max;
				} else if (members[2] == "scaled") {
					if (this.dataModel.score_scaled == "") {
						// error 403 - element no inicialitzat
						this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
					} else {
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
					}	
					return this.dataModel.score_scaled;
				}
			} else if (members[1] == "objectives" && members.length > 2) {
				if (this.dataModel.isValide("integer_non_negative", members[2], null)) {
					if (members[2] < this.dataModel.objectives.length) {
						if (members.length > 4) {
							return this.getObjectiveValue(members[3] + "." + 
								members[4], members[2]);
						} else {
							return this.getObjectiveValue(members[3], members[2]);
						}
					} else {
						// error 407 - valor consultat fora de rang
						this.error = DATA_MODEL_ELEMENT_VALUE_OUT_OF_RANGE;
					}
				} else {
					if ((members[2] == "_count" || members[2] == "_children") 
						&& members.length == 3) {
						return this.getObjectiveValue(members[2], null);
					} else {
						// error 406 - tipus de l'element incorrecte 
						this.error = DATA_MODEL_ELEMENT_TYPE_MISMATCH;
					}
				}
				return "false";
			} else if (members[1] == "nav" && members.length > 2) {
				if (members[2] == "request") {
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return this.dataModel.request;
				} else if (members[2] == "request_valid" && members.length > 3) {
					if (members[3] == "continue") {
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
						return this.dataModel.request_continue;
					} else if (members[3] == "previous") {
						// error 0 - no s'ha produït cap error
						this.error = NO_ERROR;
						return this.dataModel.request_previous;
					} else if (members[3] == "choice" && members.length > 4) {
						// comprova el format del delimitador: {target=<ID_STRING>}
						// GetValue(“adl.nav.request_valid.choice.{target=intro}”)
						var pattern = 
							new RegExp(/^\{target=[\w\W]+\}$/);
						if (pattern.test(members[4])) {
							var idsList = this.enabledItems.split("[,]");
							members[4] = members[4].replace("{target=", "");
							members[4] = members[4].replace("}", "");
							// cerca l'identificador de la petició {target=ID} en l'array d'identificadors
							// dels ítems accessibles de l'arbre d'activitats
							for (var i = 0; i < idsList.length; i++) {
								if (idsList[i] == value) {
									// error 0 - no s'ha produït cap error
									this.error = NO_ERROR;
									return "true";
								}
							}
						}
						// error 301 - s'ha produït un error durant la consulta
						this.error = GENERAL_GET_FAILURE;
						return "false";
					}	
				}
			} else if (members[1] == "interactions" && members.length > 2) {
				if (members[2] == "_children") {	
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return "";
				} 
			} else if (members[1] == "comments_from_learner" && members.length > 2) {
				if (members[2] == "_children") {	
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return "";
				} 
			} else if (members[1] == "comments_from_lms" && members.length > 2) {
				if (members[2] == "_children") {	
					// error 0 - no s'ha produït cap error
					this.error = NO_ERROR;
					return "";
				} 
			}
			// error 401 - element no definit
			this.error = UNDEFINED_DATA_MODEL_ELEMENT;
		}
	}
	return result;
}

/*
 * getObjectiveValue(string name, int index)
 * Inputs: 
 *	name - Un string amb el nom de la categoria o element.
 *	index - Un integer amb l'índex de l'array d'objectius.
 * Output: 	Un string amb el valor de l'element sol·licitat si s'ha realitzat la petició de la consulta 
 * 		amb èxit.
 *
 * Consulta un valor d'un element de la categoria cmi.objective.
 */ 
API.prototype.getObjectiveValue = function(name, index) {
	var result = "";
	
	if (name == "_children") {
		// error 0 - no s'ha produït cap error
		this.error = NO_ERROR;
		return "id,score,success_status,completion_status," + 
			"progress_measure,description";
	} else if (name == "_count") {
		// error 0 - no s'ha produït cap error
		this.error = NO_ERROR;
		return this.dataModel.objectives.length;
	} else if (name == "score._children") {
		// error 0 - no s'ha produït cap error
		this.error = NO_ERROR;
		return "raw,min,max,scaled";
	} else if (name == "score.raw") {
		if (this.dataModel.objectives[index].score_raw == "") {
			// error 403 - element no inicialitzat
			this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
		} else {
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
		}	
		return this.dataModel.objectives[index].score_raw;
	} else if (name == "score.min") {
		if (this.dataModel.objectives[index].score_min == "") {
			// error 403 - element no inicialitzat
			this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
		} else {
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
		}	
		return this.dataModel.objectives[index].score_min;
	} else if (name == "score.max") {
		if (this.dataModel.objectives[index].score_max == "") {
			// error 403 - element no inicialitzat
			this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
		} else {
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
		}	
		return this.dataModel.objectives[index].score_max;
	} else if (name == "score.scaled") {
		if (this.dataModel.objectives[index].score_scaled == "") {
			// error 403 - element no inicialitzat
			this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
		} else {
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
		}	
		return this.dataModel.objectives[index].score_scaled;
	} else if (name == "success_status") {
		// error 0 - no s'ha produït cap error
		this.error = NO_ERROR;
		return this.dataModel.objectives[index].success_status;
	} else if (name == "completion_status") {
		// error 0 - no s'ha produït cap error
		this.error = NO_ERROR;
		return this.dataModel.objectives[index].completion_status;
	} else if (name == "progress_measure") {
		if (this.dataModel.objectives[index].progress_measure == "") {
			// error 403 - element no inicialitzat
			this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
		} else {
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
		}	
		return this.dataModel.objectives[index].progress_measure;
	} else if (name == "description") {
		if (this.dataModel.objectives[index].description == "") {
			// error 403 - element no inicialitzat
			this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
		} else {
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
		}	
		return this.dataModel.objectives[index].description;
	} else if (name == "id") {
		if (this.dataModel.objectives[index].id == "") {
			// error 403 - element no inicialitzat
			this.error = DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED;
		} else {
			// error 0 - no s'ha produït cap error
			this.error = NO_ERROR;
		}	
		return this.dataModel.objectives[index].id;
	}
	// error 401 - element no definit
	this.error = UNDEFINED_DATA_MODEL_ELEMENT;
	
	return result;
}

/*
 * GetLastError()
 * Output:	Un string que representa un valor enter que identifica l'error.
 *
 * Retorna l'últim codi d'error que s'hagi produït després d'una crida a una funció de l'API.
 */
API.prototype.GetLastError = function() {
	
	return this.error;
}

/*
 * GetErrorString(int code)
 * Inputs:	Un string que representa un codi d'error.
 * Output:	Un string representant una breu descripció del codi d'error o un string buit si no 
 * 		existeix el codi d'error entrat.
 *
 * Retorna una descripció textual de l'error representat pel codi d'error entrat.
 */
API.prototype.GetErrorString = function(code) {
	
	switch (code) {
		case NO_ERROR:
		case GENERAL_EXCEPTION:
		case GENERAL_INITIALIZATION_FAILURE:
		case ALREADY_INITIALIZED:
		case CONTENT_INSTANCE_TERMINATED:
		case GENERAL_TERMINATION_FAILURE:
		case GENERAL_EXCEPTION:
		case TERMINATION_BEFORE_INITIALIZATION:
		case TERMINATION_AFTER_TERMINATION:
		case RECEIVED_DATA_BEFORE_INITIALIZATION:
		case RECEIVED_DATA_AFTER_TERMINATION:
		case STORE_DATA_BEFORE_INITIALIZATION:
		case STORE_DATA_AFTER_TERMINATION:
		case COMMIT_BEFORE_INITIALIZATION:
		case COMMIT_AFTER_TERMINATION:
		case GENERAL_ARGUMENT_ERROR:
		case GENERAL_GET_FAILURE:
		case GENERAL_SET_FAILURE:
		case GENERAL_COMMIT_FAILURE:
		case UNDEFINED_DATA_MODEL_ELEMENT:
		case UNIMPLEMENTED_DATA_MODEL_ELEMENT:
		case DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED:
		case DATA_MODEL_ELEMENT_IS_READ_ONLY:
		case DATA_MODEL_ELEMENT_IS_WRITE_ONLY:
		case DATA_MODEL_ELEMENT_TYPE_MISMATCH:
		case DATA_MODEL_ELEMENT_VALUE_OUT_OF_RANGE:
		case GENERAL_LMS_FAILURE:
			return this.errorCodes.error[code].title.toUpperCase();
		default:
			return "";
	}
}

/*
 * GetDiagnostic(string code)
 * Inputs:	Un string que representa un codi d'error o una cadena buida per sol·licitar informació 
 * 		de l'últim error.
 * Output:	Un string representant una descripció més detallada de l'error que en el cas d'invocar 
 * 		GetErrorString(code) o una cadena buida si el codi d'error no existeix.
 *
 * Retorna una descripció textual de l'error representat pel codi d'error entrat.
 */
API.prototype.GetDiagnostic = function(code) {
	
	switch (code) {
		case NO_ERROR:
		case GENERAL_EXCEPTION:
		case GENERAL_INITIALIZATION_FAILURE:
		case ALREADY_INITIALIZED:
		case CONTENT_INSTANCE_TERMINATED:
		case GENERAL_TERMINATION_FAILURE:
		case GENERAL_EXCEPTION:
		case TERMINATION_BEFORE_INITIALIZATION:
		case TERMINATION_AFTER_TERMINATION:
		case RECEIVED_DATA_BEFORE_INITIALIZATION:
		case RECEIVED_DATA_AFTER_TERMINATION:
		case STORE_DATA_BEFORE_INITIALIZATION:
		case STORE_DATA_AFTER_TERMINATION:
		case COMMIT_BEFORE_INITIALIZATION:
		case COMMIT_AFTER_TERMINATION:
		case GENERAL_ARGUMENT_ERROR:
		case GENERAL_GET_FAILURE:
		case GENERAL_SET_FAILURE:
		case GENERAL_COMMIT_FAILURE:
		case UNDEFINED_DATA_MODEL_ELEMENT:
		case UNIMPLEMENTED_DATA_MODEL_ELEMENT:
		case DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED:
		case DATA_MODEL_ELEMENT_IS_READ_ONLY:
		case DATA_MODEL_ELEMENT_IS_WRITE_ONLY:
		case DATA_MODEL_ELEMENT_TYPE_MISMATCH:
		case DATA_MODEL_ELEMENT_VALUE_OUT_OF_RANGE:
		case GENERAL_LMS_FAILURE:
			return this.errorCodes.error[code].description;
		case "":
			return this.errorCodes.error[this.error].description;
		default:
			return "";
	}
}