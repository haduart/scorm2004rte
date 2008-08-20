CREATE DATABASE IF NOT EXISTS scorm2004BETA DEFAULT CHARSET=utf8;
USE scorm2004BETA;

  DROP TABLE IF EXISTS `scorms`;
  CREATE TABLE `scorms` (
	`scorm_id` 		int(11) UNSIGNED NOT NULL AUTO_INCREMENT, 	#Identificador del paquet scorm.
	`scorm_id_text` 	TEXT(10) NOT NULL, 				#Identificador real del paquet scorm.	
	`new_screen` 		BIT(1) default 0,	#Yes = 1, False = 0 #Indicador per mostrar el contingut del paquet en una nova pantalla del navegador o no.
	`import_date` 		TIMESTAMP default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, # timestamp automàtic de l'hora de l'importació	
	PRIMARY KEY (scorm_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `metadata`;
  CREATE TABLE `metadata` (
	`scorm_id`	int(11) UNSIGNED NOT NULL,	#Identificador del paquet scorm
	`version`	VARCHAR(10),		#Versió del manifest del paquet
	`title`		VARCHAR(30) NOT NULL,	#Títol que descriu el paquet.
	`description`	TEXT,			#Descripció del paquet
	`requeriments`	TEXT,			#Requeriments per poder visualitzar els continguts (requirement, installationRemarks i otherPlatformRequirements)
	`end_user`	VARCHAR(30),		#El tipus d'usuari a qui va dirigit el paquet.
	`educative_environment`	VARCHAR(30),	#El tipus d'entorn educacional(s) que va dirigit el contingut(context).
	`author`	TEXT,			#Autor(s) del paquet.
	`copyright`	TEXT,			#LLicencies. 
	`learning_duration`	VARCHAR(10),	#Duració estimada de l'aprenentatge.	
	PRIMARY KEY(scorm_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `studentsClass`;
  CREATE TABLE `studentsClass` (
	`student_id`	int(11) UNSIGNED NOT NULL,	#identificador de l'usuari
	`organization_id`	int(11) UNSIGNED NOT NULL,	#identificador del curs	(de l'organització)

	`global_student_id`	int(11) UNSIGNED NOT NULL,	#l'identificador de l'expedient?
	`satisfied`	BIT(1) default 0,		#potser ja està satisfeta
	`completed`	BIT(1) default 0,		#per saber si està completat
	`progress_measure`	DECIMAL(5,3) default 0.0,	#per saber la nota
	`visits`	integer default 0,
	`progression_status` DECIMAL(5,3) default 0.0,
	PRIMARY KEY (student_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `scormStudent_1`; 	#La sintaxis seria `scormStudent_$scorm_id`
  CREATE TABLE `scormStudent_1` (
	`student_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`scorm_structure`	BLOB,	#100 ks com a maxim? Aquí és on es guardarà l'objecte serialitzat amb l'estructura de l'estudiant.
	PRIMARY KEY (student_id)
  ) ENGINE = MyISAM;
 
#  DROP TABLE IF EXISTS `scormStudent`; 		#Aquesta serà la taula MERGE, i serà per on buscaran els usuaris. 
#  CREATE TABLE `scormStudent` (
#	`student_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
#	`organization_id`	int(11) UNSIGNED NOT NULL,
#	`scorm_structure`	BLOB,	
#	PRIMARY KEY (student_id, scorm_id)
#  ) ENGINE = MERGE UNION = (scormStudent_1,scormStudent_2);

  DROP TABLE IF EXISTS `organizations`;
  CREATE TABLE `organizations` (
	`organization_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`structure`		VARCHAR(200),		#Descriu l'estructura de l'organització	
	`scorm_id`		int(11) NOT NULL,	#Identificador del paquet scorm amb el que està associat
	`organization_item`	int(11) NOT NULL,	#Aquest serà l'item que representarà la nostra organització. 
	`activate_sequencing` 	BIT(1) default 1, 	#Yes = 1, False = 0 #Indicador per activar o desactivar el seqüenciament del paquet.
	`import_date` 		TIMESTAMP default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, # timestamp automàtic de l'hora de l'importació	
	`beginning_date`	datetime,	#Data i hora en que es pot accedir al paquet
	`ending_date` 		datetime,	#Data i hora en que ha de caducar el paquet
	PRIMARY KEY (organization_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `items`;
  CREATE TABLE `items` (
	`item_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`item_id_text`	VARCHAR(20),			#L'identificador en text de l'item... eliminable?
	`leaf`		BIT(1) default 1 NOT NULL, 	#Ens indicarà si l'item és una "fulla" (sco o item) o no. 
	`is_visible`		BIT(1) default 1,	#'Y' == 1 ,N == 0 #Mostra, o no, l'organització.
	`title`		VARCHAR(200),			#Títol de l'item. 
	`countComments`	TINYINT NOT NULL default 0,	#Contador del número de comentaris associats amb l'item. 
	`parent_item_id`	int(11) UNSIGNED NOT NULL default 0,	#El "pare" d'aquest item. 
	`organization_id`	int(11) UNSIGNED NOT NULL,	#L'organització a la que pertany. 
	`adlnavPresentation_id`	int(11) UNSIGNED,	#si tenim adlnav: aquí hi haurà l'identificador. 	
	`adlseqSequencing_id`	int(11) UNSIGNED,	#si tenim seqüenciament aquí tindrem el seu apuntador. 
	`adlseqControlMode_id`	int(11) UNSIGNED,	#si tenim controlMode aquí hi haurà l'identificador, de lo contrari serà NULL.
	`adlseqConstrainedChoiceConsiderations_id`	int(11) UNSIGNED, #si tenim constrainedChoiceConsiderations aquí hi haurà l'identificador. 
	`optional_data_item_id`	int(11) UNSIGNED,	#si tenim dades opcionals aquí hi haurà l'identificador.
       `rollupConsiderations_Id` int(11) UNSIGNED,	#si tenim dades opcionals aquí hi haurà l'identificador.
	PRIMARY KEY (item_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `optional_data_items`;
  CREATE TABLE `optional_data_items` (
	`optional_data_item_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`parameters`	VARCHAR(500),		#Paràmetres que s'envien al recurs al llançament d'aquest.
	`dataFromLMS`	VARCHAR(500),		#Dades que pot necessitar el recurs després de ser llançat.
	`completionThreshold`	DECIMAL(5,3),	#Llindar per determinar si un recurs ha estat completat o no.
	`time_limit_action` enum('EXITMESSAGE','EXITNOMESSAGE','CONTINUEMESSAGE','CONTINUENOMESSAGE') default 'CONTINUENOMESSAGE',
	`max_time_allowed`	int UNSIGNED,		#Temps màxim acumulat permés per accedir al recurs
	`RandomizationTiming`	enum('never','once', 'onEachNewAttempt') default 'never'
	`selectCount` int UNSIGNED default 0,
	PRIMARY KEY (optional_data_item_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `comments`;
  CREATE TABLE `comments`(
	`comment_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`item_id`	int(11) UNSIGNED NOT NULL,
	`timestamp`	TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,	#Punt en el temps en que s'ha creat o editat.
	`scorm_id`	int(11) UNSIGNED NOT NULL,
	`comment`	TEXT NOT NULL,				#Text del comentari.
	`author_id`	int(11) UNSIGNED NOT NULL,		#Autor del comentari, el seu identificador.
	`comments_from`	enum('LMS','LEARNER') NOT NULL default 'LEARNER', #cmi.comments_from_lms o cmi.comments_from_learner
	`is_read`	BIT(1) default 0,			#enum('Y','N') #Indicador per informar si s'ha consultat el comentari.
	`location`	VARCHAR(30),				#cmi.comments_from_lms.n.location() ?
	PRIMARY KEY (comment_id)
  ) ENGINE = MyISAM;

  #A partir d'aqui totes les taules relacionades amb el Seqüenciament,
  #que seran: 
  #	adlseqConstrainedChoiceConsiderations,
  #	adlnavPresentation,
  #	adlseqControlMode,
  #	adlseqSequencing,
  #	adlSequencingRules,
  #	adlseqRollupRule,
  #	objectives

  DROP TABLE IF EXISTS `adlseqConstrainedChoiceConsiderations`;
  CREATE TABLE `adlseqConstrainedChoiceConsiderations` (
	`adlseqConstrainedChoiceConsiderations_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`preventActivation`	BIT(1) default 0,	#Per saber si el preventActivation està activat. 
	`constrainChoice`	BIT(1) default 0 NOT NULL,	#Per saber si el constrainChoice està activat. 
	`previous_item`		int(11) UNSIGNED,	#Si el constrainChoice està activat aquest item estarà actiu.
	`next_item`		int(11) UNSIGNED,	#Si el constrainChoice està activat aquest item estarà actiu.
	PRIMARY KEY (adlseqConstrainedChoiceConsiderations_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `adlnavPresentation`;
  CREATE TABLE `adlnavPresentation` (
	`adlnavPresentation_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`hidePrevious`	BIT(1) default 0,	#si està seleccionat (1 == true) aleshores és visible
	`hideContinue`	BIT(1) default 0, 	#si està seleccionat (1 == true) aleshores és visible
	`hideExit`	BIT(1) default 0,	#si està seleccionat (1 == true) aleshores és visible
	`hideExitAll`	BIT(1) default 0,	#si està seleccionat (1 == true) aleshores és visible
	`hideAbandon`	BIT(1) default 0,	#si està seleccionat (1 == true) aleshores és visible
	`hideAbandonAll`	BIT(1) default 0,	#si està seleccionat (1 == true) aleshores és visible
	`hideSuspendAll`	BIT(1) default 0,	#si està seleccionat (1 == true) aleshores és visible
	PRIMARY KEY (adlnavPresentation_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `adlseqControlMode`;
  CREATE TABLE `adlseqControlMode` (
	`adlseqControlMode_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`choice`	BIT(1) default 1 NOT NULL,	#true = 1, false=0 #Indica que fer una tria del seqüenciament està permès (o prohibit). 
	`choiceExit`	BIT(1) default 1 NOT NULL,	#true = 1, false=0 #Indica si un fill d'aquesta activitat pot finalitzar. També ho podriem amagar amb el adlnav:Presentation.exit = false. 
	`flow`		BIT(1) default 1 NOT NULL,	#true = 1, false=0 #Ens indicarà si pot "fluïr", en cas d'estar desactivat no ajudarem en res al seqüenciament...
	`forwardOnly`	BIT(1) default 0 NOT NULL,	#true = 1, false=0 #Indica si només és pot anar cap endavant. 
	`useCurrentAttemptObjectiveInfo`	BIT(1) default 1 NOT NULL,	#true = 1, false=0 #És per marcar si es pot (o no es pot) utilitzar la informació sobre el progrés dels objectius.
	`useCurrentAttemptProgressInfo`		BIT(1) default 1 NOT NULL,	#true = 1, false=0 #Indica si es guardarà la informació sobre els intents. 
	PRIMARY KEY(adlseqControlMode_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `adlseqSequencing`;
  CREATE TABLE `adlseqSequencing` (
	`adlseqSequencing_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`IDRef`		int(11) UNSIGNED,	#Si apunta a un altre "sequencing" que està a un sequencingCollection aquí tindrem el seu identificador.
	`attemptLimit`	TINYINT UNSIGNED,	#Aquest valor indica el màxim nombre d’intents per l’activitat. Està dintre dle adlseq:limitCondition
	`attemptAbsoluteDurationLimit`	DECIMAL(10,2),	#S’especifica la duració màxima que se li permet a un alumne perdre en un intent d’una activitat. 
	`rollupObjectiveSatisfied`	BIT(1) default 1,	#rollupObjectiveSatisfied es de adlseqSequencing
	`rollupProgressCompletion`	BIT(1) default 1,	#rollupProgressCompletion es de adlseqSequencing
	`objectiveMeasureWeight`	DECIMAL(5,3),	#objectiveMeasureWeight es de adlseqSequencing
	`objectives_id`			int(11) UNSIGNED,	#indicarem l'objective amb el que treballarà tan el sequenciament com el rollup com el limitCondition.
	`isSequencingCollection`	BIT(1) NOT NULL default 0, #Ens dirà si aquest seqüenciament forma part d'un SequencingCollection.
	 
	PRIMARY KEY(adlseqSequencing_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `objectives`;
  CREATE TABLE `objectives` (
	`objectives_id`		int(11) UNSIGNED NOT NULL  AUTO_INCREMENT,
	`objective_name`	VARCHAR(20) NOT NULL,	#identificador real, el nom que trobem al metadata. 
	`item_id`		int(11) UNSIGNED NOT NULL,	#identificador del node en el que està.
	`satisfiedByMeasure`	BIT(1) default 0 NOT NULL,
	`minNormalizedMeasure`	DECIMAL(3,2),
	`isPrimaryObjective`	BIT(1) default 1 NOT NULL,
	`mapInfo_id`		int(11) UNSIGNED DEFAULT 0, #identificador de la taula mapInfo.

	PRIMARY KEY(objectives_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `mapInfo`;
  CREATE TABLE `mapInfo` (
	`mapInfo_id`		int(11) UNSIGNED NOT NULL  AUTO_INCREMENT,
	`targetObjectiveID`	VARCHAR(20) NOT NULL,	#identificador real, el nom que trobem al metadata. 	
	`readSatisfiedStatus`	BIT(1) default 1 NOT NULL,
	`readNormalizedMeasure`	BIT(1) default 1 NOT NULL,
	`writeSatisfiedStatus`	BIT(1) default 0 NOT NULL,
	`writeNormalizedMeasure` BIT(1) default 0 NOT NULL,
	PRIMARY KEY(mapInfo_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `adlseqSequencingRules`;
  CREATE TABLE `adlseqSequencingRules` (
	`adlseqSequencingRules_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`adlseqSequencing_id`		int(11) UNSIGNED NOT NULL,
	`conditionRule`		enum('preConditionRule','postConditionRule','exitConditionRule') NOT NULL,
	`conditionCombination`	BIT(1) NOT NULL default 1, #{ 1 = all | 0 = any } 
	`measureThreshold`	DECIMAL(5,3),	#Aquest valor és utilitzat com a llindar durant la condició d’avaluació.
	`condition_always`	BIT(2) NOT NULL default 0,	#{0 = NULL, 1 = true, 2 = falç }Si la condició és 'always' no cal matar-si més, sempre serà true. 
	`condition_satisfied`	BIT(2) NOT NULL default 0,	#{0 = NULL, 1 = true, 2 = falç }si és true és que UNA de les condicions és per 'satisfied'
	`condition_completed`	BIT(2) NOT NULL default 0,	#{0 = NULL, 1 = true, 2 = falç }si és true és que UNA de les condicions és per 'completed'
	`condition_attempted`	BIT(2) NOT NULL default 0,	#{0 = NULL, 1 = true, 2 = falç }si és true és que UNA de les condicions és per 'attempted'
	`condition_attemptLimitExceeded`	BIT(2) NOT NULL default 0, #{0 = NULL, 1 = true, 2 = falç }si és true és que UNA de les condicions és per 'attemptLimitExceeded'
	`condition_objectiveMeasureGreaterThan`	BIT(2) NOT NULL default 0, #{0 = NULL, 1 = true, 2 = falç }si és true és que UNA de les condicions és per 'objectiveMeasureGreaterThan'
	`condition_objectiveMeasureLessThan`	BIT(2) NOT NULL default 0, #{0 = NULL, 1 = true, 2 = falç }si és true és que UNA de les condicions és per 'objectiveMeasureLessThan'
	`ruleAction`	enum('skip','disabled','hiddenFromChoice','stopForwardTransversal','exit','exitParent','exitAll','retry','retryAll','continue','previous') NOT NULL,
	`referencedObjective`	VARCHAR(20),
	PRIMARY KEY(adlseqSequencingRules_id,adlseqSequencing_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `adlseqRollupRule`;
  CREATE TABLE `adlseqRollupRule` (
	`adlseqSequencing_id`	int(11) UNSIGNED NOT NULL,
	`adlseqRollupRule_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,#Com que en poden haver-hi varies regles per un sequenciament...
	`childActivitySet`	enum('none','any','all','atLeastCount','atLeastPercent') default 'all',
	`minimumCount`		TINYINT,	#Aquest valor serà necessari si utilitzem la condició 'atLeastCount'. 
	`minimumPercent`	DECIMAL(3,2),	#Aquest valor serà necessari si utilitzem la condició 'atLeastPercent'. 
	`condition_never`	BIT(2) default 0 NOT NULL, #{0 = NULL, 1 = true, 2 = falç }Si la condició és 'never' no cal matar-si més, sempre serà false. 
	`condition_satisfied`	BIT(2) default 0 NOT NULL, #{0 = NULL, 1 = true, 2 = falç }si és true és que UNA de les condicions és per 'satisfied'
	`condition_completed`	BIT(2) default 0 NOT NULL, #{0 = NULL, 1 = true, 2 = falç }si és true és que UNA de les condicions és per 'completed'
	`condition_attempted`	BIT(2) default 0 NOT NULL, #{0 = NULL, 1 = true, 2 = falç }si és true és que UNA de les condicions és per 'attempted'
	`condition_attemptedLimitExceeded`	BIT(2) default 0 NOT NULL, #{0 = NULL, 1 = true, 2 = falç } si és true és que UNA de les condicions és per 'attemptLimitExceeded'
	`rollupAction`	ENUM('satisfied','notSatisfied','completed','incomplete') NOT NULL,
	PRIMARY KEY(adlseqSequencing_id, adlseqRollupRule_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `rollupConsiderations`;
  CREATE TABLE `rollupConsiderations` (
	`rollupConsiderations_id` int(11) UNSIGNED NOT NULL,
	`requiredForSatisfied`	enum('always', 'ifAttempted', 'ifNotSkipped', 'ifNotSuspended') default 'always'
	`requiredForNotSatisfied` enum('always', 'ifAttempted', 'ifNotSkipped', 'ifNotSuspended') default 'always'
	`requiredForCompleted`	enum('always', 'ifAttempted', 'ifNotSkipped', 'ifNotSuspended') default 'always'
	`requiredForIncomplete`	enum('always', 'ifAttempted', 'ifNotSkipped', 'ifNotSuspended') default 'always'
	`measureSatisfactionIfActive` Bit(1) default 1,

	PRIMARY KEY(rollupConsiderations_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `resources`;
  CREATE TABLE `resources` (
	`resources_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`type`		VARCHAR(50),	#si és "webcontent" o què és.
	`scorm_type`	BIT(1) default 0 NOT NULL,	#si es 1 serà ASSET, 0 serà SCO. Si es un "dependency" també ho marquem com a Asset.
	`scorm_id`	int(11) UNSIGNED NOT NULL,	#Referència al paquet scorm
	`item_id`	int(11) UNSIGNED NOT NULL,
	PRIMARY KEY(resources_id)
  ) ENGINE = MyISAM;

  # A partir d'aquí les taules que estan vinculades a un possible sistema
  # distribuit de l'enmagatzemament, que serien:
  #	files,
  #	server_resources,
  #	scorm_server_resources

  DROP TABLE IF EXISTS `files`;
  CREATE TABLE `files` (
	`files_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`href`		VARCHAR(100),
	`server_resources_id`	int(11) UNSIGNED,	#En cas de que aquest arxiu no és trobi en aquesta màquina...
	`resources_id`		int(11),
	PRIMARY KEY(files_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `server_resources`;
  CREATE TABLE `server_resources` (
	`server_resources_id`	int(11) UNSIGNED NOT NULL, #-> hauria de ser AUTO_INCREMENT però no ho podem tindre si també tenim el 'ON UPDATE'.
	`server_name`		VARCHAR(20) NOT NULL,		#El nom del servidor, també podria ser un àlies. 
	`connection_port`	TINYINT UNSIGNED NOT NULL,	#Identifica el port pel qual s'estableix la connexió
	`connection_type`	ENUM('ftp','http','tftp','ssh') NOT NULL,
	`connection_login`	VARCHAR(10),	#En cas de necessitar login per autenticar-se. 
	`connection_password`	VARCHAR(50),	#En cas de necessitar password per autenticar-se. Xifrar amb AES.
	`update_time`		TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`description`		TEXT,
	`company`		TEXT,
	`contact_address`	VARCHAR(30),
	`files_id`		int(11) UNSIGNED, #files_id es de server_resources
	PRIMARY KEY(files_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `scorm_server_resources`;
  CREATE TABLE `scorm_server_resources` (
	`scorm_id` 		int(11) UNSIGNED NOT NULL,
	`server_resources_id`	int(11) UNSIGNED NOT NULL,
	PRIMARY KEY(scorm_id,server_resources_id)
  ) ENGINE = MyISAM;



