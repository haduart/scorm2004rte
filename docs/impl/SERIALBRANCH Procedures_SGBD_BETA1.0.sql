
################################################################################
##   		##   		  insert*   		##   		      ##
################################################################################

# CALL insertScorms( _scorm_id_text TEXT(10), _new_screen BIT(1));
DROP PROCEDURE IF EXISTS `insertScorms`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertScorms`(IN _scorm_id_text TEXT(10), IN _new_screen BIT(1))
BEGIN
  INSERT INTO scorms SET scorm_id_text = _scorm_id_text, new_screen = _new_screen;
	select LAST_INSERT_ID() as LASTID; 
END $
DELIMITER ;

# CALL insertOrganizations(_scorm_id integer, _structure VARCHAR(200),_is_visible BIT(1), _item_id_text VARCHAR(20), _title VARCHAR(200));
DROP PROCEDURE IF EXISTS `insertOrganizations`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertOrganizations`( IN _scorm_id integer, IN _structure VARCHAR(200), IN _is_visible BIT(1), IN _item_id_text VARCHAR(20), IN _title VARCHAR(200), IN _dateTimeStart DATETIME, IN _dateTimeEnd DATETIME)
BEGIN
	INSERT INTO organizations SET scorm_id = _scorm_id, organization_item = LAST_INSERT_ID() , structure = _structure, title = _title, beginning_date = _dateTimeStart, ending_date = _dateTimeEnd;
	select LAST_INSERT_ID() as LASTID;
END $
DELIMITER ;

#CALL insertMetadata( _scorm_id integer, _version VARCHAR(10), _title VARCHAR(30), _description TEXT, _requeriments TEXT, _end_user VARCHAR(30), _educative_environment VARCHAR(30), _author TEXT, _learning_duration VARCHAR(10), _copyright VARCHAR(20) );
DROP PROCEDURE IF EXISTS `insertMetadata`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertMetadata`( IN _scorm_id integer, IN _version VARCHAR(10), IN _title VARCHAR(30), IN _description TEXT, IN _requeriments TEXT, IN _end_user VARCHAR(30), IN _educative_environment VARCHAR(30), IN _author TEXT, IN _learning_duration VARCHAR(10), IN _copyright VARCHAR(20) )
BEGIN
INSERT INTO metadata SET scorm_id = _scorm_id, version = _version, title =_title, description = _description, requeriments = _requeriments, end_user = _end_user, educative_environment = _educative_environment, author = _author, learning_duration = _learning_duration, copyright = _copyright;
select LAST_INSERT_ID() as LASTID;
END $
DELIMITER ;

#CALL insertStudentsClass( _organization_id integer, _global_student_id VARCHAR(100));
DROP PROCEDURE IF EXISTS `insertStudentsClass`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertStudentsClass`( IN _organization_id integer, IN _global_student_id VARCHAR(100) )
BEGIN
INSERT INTO studentsClass SET organization_id = _organization_id, global_student_id = _global_student_id;
select LAST_INSERT_ID() as LASTID;
END $
DELIMITER ;

#CALL insertSerialOrganizations (_organization_id integer, _BLOB BLOB):
DROP PROCEDURE IF EXISTS `insertSerialOrganizations`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertSerialOrganizations`(IN _organization_id integer, IN _BLOB BLOB)
BEGIN
INSERT INTO SerialOrganizations SET organization_id = _organization_id, blobOrganization = _BLOB;
select LAST_INSERT_ID() as LASTID;
END $
DELIMITER ;

#CALL createCourseStudent (_courseStudent VARCHAR(20)):


################################################################################
##   		##   		  update*   		##   		      ##
################################################################################

#Marquem la organització com a llegida o no llegida.
#	CALL updateOrganizations_isVisible(_organization_id integer, _is_visible BIT(1));
DROP PROCEDURE IF EXISTS `updateOrganizations_isVisible`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateOrganizations_isVisible`( IN _organization_id integer, IN _is_visible BIT(1) )
BEGIN
	UPDATE organizations 
	SET is_visible = _is_visible 
	WHERE organization_id = _organization_id;
END $
DELIMITER ;

#Modifiquem les dades de l'usuari
#Nota! 
#  Suposem que només s'actualitzaran aquestes dades al final d'una sessió 
#  de l'usuari! Ja que és quan incrementem el número de visites de l'usuari.
#	CALL updateStudentsClass( _organization_id integer, _student_id integer, _satisfied BIT(1), _completed BIT(1), _progress_measure DECIMAL(5,3), _last_item_title VARCHAR(30), _completed_percent	DECIMAL(5,3))
DROP PROCEDURE IF EXISTS `updateStudentsClass`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateStudentsClass`( IN _organization_id integer, IN _student_id integer, IN _satisfied BIT(1), IN _completed BIT(1), IN _progress_measure DECIMAL(6,3), IN _last_item_title VARCHAR(500), IN _completed_percent	DECIMAL(6,3))
BEGIN
	UPDATE studentsClass 
	SET satisfied = _satisfied, completed = _completed, progress_measure = _progress_measure, last_item_title = _last_item_title, completed_percent = _completed_percent, visits = visits + 1
	WHERE organization_id = _organization_id AND student_id = _student_id;
END $
DELIMITER ;

#Modifiquem les dades de l'usuari
#Nota! 
#  Suposem que només s'actualitzaran aquestes dades al final d'una sessió 
#  de l'usuari! Ja que és quan incrementem el número de visites de l'usuari.
#	CALL updateStudentsClassLASTITEM( _organization_id integer, _student_id integer, _satisfied BIT(1), _completed BIT(1), _progress_measure DECIMAL(5,3), _completed_percent DECIMAL(5,3))
DROP PROCEDURE IF EXISTS `updateStudentsClassLASTITEM`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateStudentsClassLASTITEM`( IN _organization_id integer, IN _student_id integer, IN _satisfied BIT(1), IN _completed BIT(1), IN _progress_measure DECIMAL(6,3), IN _completed_percent	DECIMAL(6,3))
BEGIN
	UPDATE studentsClass 
	SET satisfied = _satisfied, completed = _completed, progress_measure = _progress_measure, completed_percent = _completed_percent, visits = visits + 1
	WHERE organization_id = _organization_id AND student_id = _student_id;
END $
DELIMITER ;

################################################################################
##   		##   		  select*   		##   		      ##
################################################################################

#	CALL selectTestGlobalStudent( _global_student_id VARCHAR(100), _organization_id integer);
DROP PROCEDURE IF EXISTS `selectTestGlobalStudent`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `selectTestGlobalStudent`( IN _global_student_id VARCHAR(100), IN _organization_id integer)
BEGIN
	SELECT student_id as STUDENT 
	FROM studentsClass
	WHERE global_student_id like _global_student_id 
		AND organization_id = _organization_id;
END $
DELIMITER ;


################################################################################
##   		##   		  delete*   		##   		      ##
################################################################################

#	CALL selectTestGlobalStudent( _global_student_id VARCHAR(100), _organization_id integer);
DROP PROCEDURE IF EXISTS `deleteALLCourse`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteALLCourse`(IN _organization_id integer)
BEGIN
	DELETE scorms, metadata, organizations, SerialOrganizations, studentsClass
	FROM scorms, metadata, organizations, SerialOrganizations, studentsClass
	WHERE metadata.scorm_id = scorms.scorm_id  AND scorms.scorm_id = organizations.scorm_id AND organizations.organization_id = _organization_id AND organizations.organization_id = SerialOrganizations.organization_id AND organizations.organization_id = studentsClass.organization_id;

	#select _tablename;

	#DROP TABLE _tablename ; #No li mola, s'haura de fer el drop 'a ma'.

END $
DELIMITER ;

#	CALL selectTestGlobalStudent( _global_student_id VARCHAR(100), _organization_id integer);
DROP PROCEDURE IF EXISTS `deleteSequencingCourse`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteSequencingCourse`(IN _organization_id integer)
BEGIN
	DELETE SerialOrganizations
	FROM scorms, metadata, organizations, SerialOrganizations, studentsClass
	WHERE metadata.scorm_id = scorms.scorm_id  AND scorms.scorm_id = organizations.scorm_id AND organizations.organization_id = _organization_id AND organizations.organization_id = SerialOrganizations.organization_id AND organizations.organization_id = studentsClass.organization_id;

END $
DELIMITER ;
