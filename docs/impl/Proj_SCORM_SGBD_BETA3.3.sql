CREATE DATABASE IF NOT EXISTS scorm2004BETA DEFAULT CHARSET=utf8;
USE scorm2004BETA;

  DROP TABLE IF EXISTS `scorms`;
  CREATE TABLE `scorms` (
	`scorm_id` 		int(11) UNSIGNED NOT NULL AUTO_INCREMENT, 	#Identificador del paquet scorm.
	`scorm_id_text` 	TEXT(10) NOT NULL, 				#Identificador real del paquet scorm.	
	`new_screen` 		BIT(1) default 0,	#Yes = 1, False = 0 #Indicador per mostrar el contingut del paquet en una nova pantalla del navegador o no.
	`import_date` 		TIMESTAMP default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, # timestamp autom�tic de l'hora de l'importaci�	
	PRIMARY KEY (scorm_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `metadata`;
  CREATE TABLE `metadata` (
	`scorm_id`	int(11) UNSIGNED NOT NULL,	#Identificador del paquet scorm
	`version`	VARCHAR(10),		#Versi� del manifest del paquet
	`title`		VARCHAR(30) NOT NULL,	#T�tol que descriu el paquet.
	`description`	TEXT,			#Descripci� del paquet
	`requeriments`	TEXT,			#Requeriments per poder visualitzar els continguts (requirement, installationRemarks i otherPlatformRequirements)
	`end_user`	VARCHAR(30),		#El tipus d'usuari a qui va dirigit el paquet.
	`educative_environment`	VARCHAR(30),	#El tipus d'entorn educacional(s) que va dirigit el contingut(context).
	`author`	TEXT,			#Autor(s) del paquet.
	`copyright`	TEXT,			#LLicencies. 
	`learning_duration`	VARCHAR(10),	#Duraci� estimada de l'aprenentatge.	
	PRIMARY KEY(scorm_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `studentsClass`;
  CREATE TABLE `studentsClass` (
	`student_id`	int(11) UNSIGNED NOT NULL,	#identificador de l'usuari
	`organization_id`	int(11) UNSIGNED NOT NULL,	#identificador del curs	(de l'organitzaci�)

	`global_student_id`	int(11) UNSIGNED NOT NULL,	#l'identificador de l'expedient?
	`satisfied`	BIT(1) default 0,		#potser ja est� satisfeta
	`completed`	BIT(1) default 0,		#per saber si est� completat
	`progress_measure`	DECIMAL(5,3) default 0.0,	#per saber la nota
	`visits`	integer default 0,
	`progression_status` DECIMAL(5,3) default 0.0,
	PRIMARY KEY (student_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `scormStudent_1`; 	#La sintaxis seria `scormStudent_$scorm_id`
  CREATE TABLE `scormStudent_1` (
	`student_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`scorm_structure`	BLOB,	#100 ks com a maxim? Aqu� �s on es guardar� l'objecte serialitzat amb l'estructura de l'estudiant.
	PRIMARY KEY (student_id)
  ) ENGINE = MyISAM;
 
#  DROP TABLE IF EXISTS `scormStudent`; 		#Aquesta ser� la taula MERGE, i ser� per on buscaran els usuaris. 
#  CREATE TABLE `scormStudent` (
#	`student_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
#	`organization_id`	int(11) UNSIGNED NOT NULL,
#	`scorm_structure`	BLOB,	
#	PRIMARY KEY (student_id, scorm_id)
#  ) ENGINE = MERGE UNION = (scormStudent_1,scormStudent_2);

  DROP TABLE IF EXISTS `organizations`;
  CREATE TABLE `organizations` (
	`organization_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`structure`		VARCHAR(200),		#Descriu l'estructura de l'organitzaci�	
	`scorm_id`		int(11) NOT NULL,	#Identificador del paquet scorm amb el que est� associat
	`organization_item`	int(11) NOT NULL,	#Aquest ser� l'item que representar� la nostra organitzaci�. 
	`activate_sequencing` 	BIT(1) default 1, 	#Yes = 1, False = 0 #Indicador per activar o desactivar el seq�enciament del paquet.
	`import_date` 		TIMESTAMP default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, # timestamp autom�tic de l'hora de l'importaci�	
	`beginning_date`	datetime,	#Data i hora en que es pot accedir al paquet
	`ending_date` 		datetime,	#Data i hora en que ha de caducar el paquet
	PRIMARY KEY (organization_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `items`;
  CREATE TABLE `items` (
	`item_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`item_id_text`	VARCHAR(20),			#L'identificador en text de l'item... eliminable?
	`leaf`		BIT(1) default 1 NOT NULL, 	#Ens indicar� si l'item �s una "fulla" (sco o item) o no. 
	`is_visible`		BIT(1) default 1,	#'Y' == 1 ,N == 0 #Mostra, o no, l'organitzaci�.
	`title`		VARCHAR(200),			#T�tol de l'item. 
	`countComments`	TINYINT NOT NULL default 0,	#Contador del n�mero de comentaris associats amb l'item. 
	`parent_item_id`	int(11) UNSIGNED NOT NULL default 0,	#El "pare" d'aquest item. 
	`organization_id`	int(11) UNSIGNED NOT NULL,	#L'organitzaci� a la que pertany. 
	`adlnavPresentation_id`	int(11) UNSIGNED,	#si tenim adlnav: aqu� hi haur� l'identificador. 	
	`adlseqSequencing_id`	int(11) UNSIGNED,	#si tenim seq�enciament aqu� tindrem el seu apuntador. 
	`adlseqControlMode_id`	int(11) UNSIGNED,	#si tenim controlMode aqu� hi haur� l'identificador, de lo contrari ser� NULL.
	`adlseqConstrainedChoiceConsiderations_id`	int(11) UNSIGNED, #si tenim constrainedChoiceConsiderations aqu� hi haur� l'identificador. 
	`optional_data_item_id`	int(11) UNSIGNED,	#si tenim dades opcionals aqu� hi haur� l'identificador.
       `rollupConsiderations_Id` int(11) UNSIGNED,	#si tenim dades opcionals aqu� hi haur� l'identificador.
	PRIMARY KEY (item_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `optional_data_items`;
  CREATE TABLE `optional_data_items` (
	`optional_data_item_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`parameters`	VARCHAR(500),		#Par�metres que s'envien al recurs al llan�ament d'aquest.
	`dataFromLMS`	VARCHAR(500),		#Dades que pot necessitar el recurs despr�s de ser llan�at.
	`completionThreshold`	DECIMAL(5,3),	#Llindar per determinar si un recurs ha estat completat o no.
	`time_limit_action` enum('EXITMESSAGE','EXITNOMESSAGE','CONTINUEMESSAGE','CONTINUENOMESSAGE') default 'CONTINUENOMESSAGE',
	`max_time_allowed`	int UNSIGNED,		#Temps m�xim acumulat perm�s per accedir al recurs
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

  #A partir d'aqui totes les taules relacionades amb el Seq�enciament,
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
	`preventActivation`	BIT(1) default 0,	#Per saber si el preventActivation est� activat. 
	`constrainChoice`	BIT(1) default 0 NOT NULL,	#Per saber si el constrainChoice est� activat. 
	`previous_item`		int(11) UNSIGNED,	#Si el constrainChoice est� activat aquest item estar� actiu.
	`next_item`		int(11) UNSIGNED,	#Si el constrainChoice est� activat aquest item estar� actiu.
	PRIMARY KEY (adlseqConstrainedChoiceConsiderations_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `adlnavPresentation`;
  CREATE TABLE `adlnavPresentation` (
	`adlnavPresentation_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`hidePrevious`	BIT(1) default 0,	#si est� seleccionat (1 == true) aleshores �s visible
	`hideContinue`	BIT(1) default 0, 	#si est� seleccionat (1 == true) aleshores �s visible
	`hideExit`	BIT(1) default 0,	#si est� seleccionat (1 == true) aleshores �s visible
	`hideExitAll`	BIT(1) default 0,	#si est� seleccionat (1 == true) aleshores �s visible
	`hideAbandon`	BIT(1) default 0,	#si est� seleccionat (1 == true) aleshores �s visible
	`hideAbandonAll`	BIT(1) default 0,	#si est� seleccionat (1 == true) aleshores �s visible
	`hideSuspendAll`	BIT(1) default 0,	#si est� seleccionat (1 == true) aleshores �s visible
	PRIMARY KEY (adlnavPresentation_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `adlseqControlMode`;
  CREATE TABLE `adlseqControlMode` (
	`adlseqControlMode_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`choice`	BIT(1) default 1 NOT NULL,	#true = 1, false=0 #Indica que fer una tria del seq�enciament est� perm�s (o prohibit). 
	`choiceExit`	BIT(1) default 1 NOT NULL,	#true = 1, false=0 #Indica si un fill d'aquesta activitat pot finalitzar. Tamb� ho podriem amagar amb el adlnav:Presentation.exit = false. 
	`flow`		BIT(1) default 1 NOT NULL,	#true = 1, false=0 #Ens indicar� si pot "flu�r", en cas d'estar desactivat no ajudarem en res al seq�enciament...
	`forwardOnly`	BIT(1) default 0 NOT NULL,	#true = 1, false=0 #Indica si nom�s �s pot anar cap endavant. 
	`useCurrentAttemptObjectiveInfo`	BIT(1) default 1 NOT NULL,	#true = 1, false=0 #�s per marcar si es pot (o no es pot) utilitzar la informaci� sobre el progr�s dels objectius.
	`useCurrentAttemptProgressInfo`		BIT(1) default 1 NOT NULL,	#true = 1, false=0 #Indica si es guardar� la informaci� sobre els intents. 
	PRIMARY KEY(adlseqControlMode_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `adlseqSequencing`;
  CREATE TABLE `adlseqSequencing` (
	`adlseqSequencing_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`IDRef`		int(11) UNSIGNED,	#Si apunta a un altre "sequencing" que est� a un sequencingCollection aqu� tindrem el seu identificador.
	`attemptLimit`	TINYINT UNSIGNED,	#Aquest valor indica el m�xim nombre d�intents per l�activitat. Est� dintre dle adlseq:limitCondition
	`attemptAbsoluteDurationLimit`	DECIMAL(10,2),	#S�especifica la duraci� m�xima que se li permet a un alumne perdre en un intent d�una activitat. 
	`rollupObjectiveSatisfied`	BIT(1) default 1,	#rollupObjectiveSatisfied es de adlseqSequencing
	`rollupProgressCompletion`	BIT(1) default 1,	#rollupProgressCompletion es de adlseqSequencing
	`objectiveMeasureWeight`	DECIMAL(5,3),	#objectiveMeasureWeight es de adlseqSequencing
	`objectives_id`			int(11) UNSIGNED,	#indicarem l'objective amb el que treballar� tan el sequenciament com el rollup com el limitCondition.
	`isSequencingCollection`	BIT(1) NOT NULL default 0, #Ens dir� si aquest seq�enciament forma part d'un SequencingCollection.
	 
	PRIMARY KEY(adlseqSequencing_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `objectives`;
  CREATE TABLE `objectives` (
	`objectives_id`		int(11) UNSIGNED NOT NULL  AUTO_INCREMENT,
	`objective_name`	VARCHAR(20) NOT NULL,	#identificador real, el nom que trobem al metadata. 
	`item_id`		int(11) UNSIGNED NOT NULL,	#identificador del node en el que est�.
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
	`measureThreshold`	DECIMAL(5,3),	#Aquest valor �s utilitzat com a llindar durant la condici� d�avaluaci�.
	`condition_always`	BIT(2) NOT NULL default 0,	#{0 = NULL, 1 = true, 2 = fal� }Si la condici� �s 'always' no cal matar-si m�s, sempre ser� true. 
	`condition_satisfied`	BIT(2) NOT NULL default 0,	#{0 = NULL, 1 = true, 2 = fal� }si �s true �s que UNA de les condicions �s per 'satisfied'
	`condition_completed`	BIT(2) NOT NULL default 0,	#{0 = NULL, 1 = true, 2 = fal� }si �s true �s que UNA de les condicions �s per 'completed'
	`condition_attempted`	BIT(2) NOT NULL default 0,	#{0 = NULL, 1 = true, 2 = fal� }si �s true �s que UNA de les condicions �s per 'attempted'
	`condition_attemptLimitExceeded`	BIT(2) NOT NULL default 0, #{0 = NULL, 1 = true, 2 = fal� }si �s true �s que UNA de les condicions �s per 'attemptLimitExceeded'
	`condition_objectiveMeasureGreaterThan`	BIT(2) NOT NULL default 0, #{0 = NULL, 1 = true, 2 = fal� }si �s true �s que UNA de les condicions �s per 'objectiveMeasureGreaterThan'
	`condition_objectiveMeasureLessThan`	BIT(2) NOT NULL default 0, #{0 = NULL, 1 = true, 2 = fal� }si �s true �s que UNA de les condicions �s per 'objectiveMeasureLessThan'
	`ruleAction`	enum('skip','disabled','hiddenFromChoice','stopForwardTransversal','exit','exitParent','exitAll','retry','retryAll','continue','previous') NOT NULL,
	`referencedObjective`	VARCHAR(20),
	PRIMARY KEY(adlseqSequencingRules_id,adlseqSequencing_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `adlseqRollupRule`;
  CREATE TABLE `adlseqRollupRule` (
	`adlseqSequencing_id`	int(11) UNSIGNED NOT NULL,
	`adlseqRollupRule_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,#Com que en poden haver-hi varies regles per un sequenciament...
	`childActivitySet`	enum('none','any','all','atLeastCount','atLeastPercent') default 'all',
	`minimumCount`		TINYINT,	#Aquest valor ser� necessari si utilitzem la condici� 'atLeastCount'. 
	`minimumPercent`	DECIMAL(3,2),	#Aquest valor ser� necessari si utilitzem la condici� 'atLeastPercent'. 
	`condition_never`	BIT(2) default 0 NOT NULL, #{0 = NULL, 1 = true, 2 = fal� }Si la condici� �s 'never' no cal matar-si m�s, sempre ser� false. 
	`condition_satisfied`	BIT(2) default 0 NOT NULL, #{0 = NULL, 1 = true, 2 = fal� }si �s true �s que UNA de les condicions �s per 'satisfied'
	`condition_completed`	BIT(2) default 0 NOT NULL, #{0 = NULL, 1 = true, 2 = fal� }si �s true �s que UNA de les condicions �s per 'completed'
	`condition_attempted`	BIT(2) default 0 NOT NULL, #{0 = NULL, 1 = true, 2 = fal� }si �s true �s que UNA de les condicions �s per 'attempted'
	`condition_attemptedLimitExceeded`	BIT(2) default 0 NOT NULL, #{0 = NULL, 1 = true, 2 = fal� } si �s true �s que UNA de les condicions �s per 'attemptLimitExceeded'
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
	`type`		VARCHAR(50),	#si �s "webcontent" o qu� �s.
	`scorm_type`	BIT(1) default 0 NOT NULL,	#si es 1 ser� ASSET, 0 ser� SCO. Si es un "dependency" tamb� ho marquem com a Asset.
	`scorm_id`	int(11) UNSIGNED NOT NULL,	#Refer�ncia al paquet scorm
	`item_id`	int(11) UNSIGNED NOT NULL,
	PRIMARY KEY(resources_id)
  ) ENGINE = MyISAM;

  # A partir d'aqu� les taules que estan vinculades a un possible sistema
  # distribuit de l'enmagatzemament, que serien:
  #	files,
  #	server_resources,
  #	scorm_server_resources

  DROP TABLE IF EXISTS `files`;
  CREATE TABLE `files` (
	`files_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`href`		VARCHAR(100),
	`server_resources_id`	int(11) UNSIGNED,	#En cas de que aquest arxiu no �s trobi en aquesta m�quina...
	`resources_id`		int(11),
	PRIMARY KEY(files_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `server_resources`;
  CREATE TABLE `server_resources` (
	`server_resources_id`	int(11) UNSIGNED NOT NULL, #-> hauria de ser AUTO_INCREMENT per� no ho podem tindre si tamb� tenim el 'ON UPDATE'.
	`server_name`		VARCHAR(20) NOT NULL,		#El nom del servidor, tamb� podria ser un �lies. 
	`connection_port`	TINYINT UNSIGNED NOT NULL,	#Identifica el port pel qual s'estableix la connexi�
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



