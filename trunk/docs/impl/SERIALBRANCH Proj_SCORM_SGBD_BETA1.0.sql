CREATE DATABASE IF NOT EXISTS scorm2004Serialized DEFAULT CHARSET=utf8;
USE scorm2004Serialized;

  DROP TABLE IF EXISTS `scorms`;
  CREATE TABLE `scorms` (
	`scorm_id` 		int(11) UNSIGNED NOT NULL AUTO_INCREMENT, 	#Identificador del paquet scorm.
	`scorm_id_text` 	VARCHAR(100) NOT NULL, 				#Identificador real del paquet scorm.
	`new_screen` 		BIT(1) default 0,	#Yes = 1, False = 0 #Indicador per mostrar el contingut del paquet en una nova pantalla del navegador o no.
	`import_date` 		TIMESTAMP default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, # timestamp automàtic de l'hora de l'importació	
	PRIMARY KEY (scorm_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `metadata`;
  CREATE TABLE `metadata` (
	`scorm_id`	int(11) UNSIGNED NOT NULL,	#Identificador del paquet scorm
	`version`	VARCHAR(10),		#Versió del manifest del paquet
	`title`		VARCHAR(30),	#Títol que descriu el paquet.
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
	`student_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,	#identificador de l'usuari
	`organization_id`	int(11) UNSIGNED NOT NULL,	#identificador del curs	(de l'organització)

	`global_student_id`	VARCHAR(100) NOT NULL,	#l'identificador de l'expedient?
	`satisfied`	BIT(1) default 0,		#potser ja està satisfeta
	`completed`	BIT(1) default 0,		#per saber si està completat
	`progress_measure`	DECIMAL(6,3) default 0.0,	#per saber la nota (en %!)
	`visits`	integer default 0,
	`last_Access` TIMESTAMP default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`last_item_title` VARCHAR(500), 
	`completed_percent`	DECIMAL(6,3) default 0.0,
	PRIMARY KEY (student_id, organization_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `permission`;
  CREATE TABLE `permission` (
	`scorm_id`	int(11) UNSIGNED NOT NULL,	#identificador del curs	(de l'scorm)
	`student_id`	int(11) UNSIGNED NOT NULL,	#identificador de l'usuari
	`typeOfPermis`	enum('LECTURA','ADMINISTRACIO') NOT NULL default 'LECTURA',
	PRIMARY KEY (scorm_id, student_id)
  ) ENGINE = MyISAM;  


#  DROP TABLE IF EXISTS `courseStudent_1`; 	#La sintaxis seria `scormStudent_$scorm_id`
#  CREATE TABLE `courseStudent_1` (
#	`student_id`	int(11) UNSIGNED NOT NULL,
#	`scorm_structure`	BLOB,	#100 ks com a maxim? Aquí és on es guardarà l'objecte serialitzat amb l'estructura de l'estudiant.
#	PRIMARY KEY (student_id)
#  ) ENGINE = MyISAM;
 

  DROP TABLE IF EXISTS `organizations`;
  CREATE TABLE `organizations` (
	`organization_id`	int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`structure`		VARCHAR(200),		#Descriu l'estructura de l'organització	
	`title`			VARCHAR(200),			
	`scorm_id`		int(11) NOT NULL,	#Identificador del paquet scorm amb el que està associat
	`organization_item`	int(11) NOT NULL,	#Aquest serà l'item que representarà la nostra organització. 
	#`studentTable`		VARCHAR(50) NOT NULL,
	`activate_sequencing` 	BIT(1) default 1, 	#Yes = 1, False = 0 #Indicador per activar o desactivar el seqüenciament del paquet.
	`import_date` 		TIMESTAMP default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, # timestamp automàtic de l'hora de l'importació	
	`beginning_date`	datetime,	#Data i hora en que es pot accedir al paquet
	`ending_date` 		datetime,	#Data i hora en que ha de caducar el paquet
	`deleted_structure`	BIT(1) default 0	#D'aquesta manera marcarem si el curs té estructura o ha estat eliminada.
	PRIMARY KEY (organization_id)
  ) ENGINE = MyISAM;

  DROP TABLE IF EXISTS `SerialOrganizations`;
  CREATE TABLE `SerialOrganizations` (
	`organization_id`	int(11) UNSIGNED NOT NULL,
	`blobOrganization`	BLOB,	#100 ks com a maxim? Aquí és on es guardarà l'objecte serialitzat amb l'estructura de l'estudiant.
	PRIMARY KEY (organization_id)
  ) ENGINE = MyISAM;

