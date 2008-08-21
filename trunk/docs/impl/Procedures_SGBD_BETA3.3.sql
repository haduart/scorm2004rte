
################################################################################
##   		##   		  insert*   		##   		      ##
################################################################################

# CALL insertScorms( _scorm_id_text TEXT(10), _activate_sequencing BIT(1), _new_screen BIT(1));
DROP PROCEDURE IF EXISTS `insertScorms`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertScorms`(IN _scorm_id_text TEXT(10), IN _new_screen BIT(1))
BEGIN
  INSERT INTO scorms SET scorm_id_text = _scorm_id_text, new_screen = _new_screen;
	select LAST_INSERT_ID(); 
END $
DELIMITER ;

#CALL insertItems( _item_id_text VARCHAR(20), _leaf BIT(1), _title VARCHAR(200), _parent_item_id integer, _organization_id integer, _is_visible BIT(1));
DROP PROCEDURE IF EXISTS `insertItems`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertItems`( IN _item_id_text VARCHAR(20), IN _leaf BIT(1), IN _title VARCHAR(200), IN _father_item_id integer, IN _organization_id integer, IN _is_visible BIT(1))
BEGIN
INSERT INTO items SET item_id_text = _item_id_text, leaf = _leaf, title = _title, father_item_id = _father_item_id, organization_id = _organization_id, is_visible = _is_visible;
select LAST_INSERT_ID();
END $
DELIMITER ;

# CALL insertOrganizations(_scorm_id integer, _structure VARCHAR(200),_is_visible BIT(1), _item_id_text VARCHAR(20), _title VARCHAR(200), _studentTable VARCHAR(50));
DROP PROCEDURE IF EXISTS `insertOrganizations`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertOrganizations`( IN _scorm_id integer, IN _structure VARCHAR(200), IN _is_visible BIT(1), IN _item_id_text VARCHAR(20), IN _title VARCHAR(200), IN _studentTable VARCHAR(50))
BEGIN
	CALL insertItems( _item_id_text, 0 , _title, 0, 0, _is_visible);
	INSERT INTO organizations SET scorm_id = _scorm_id, organization_item = LAST_INSERT_ID() , structure = _structure, studentTable = _studentTable;
	#select LAST_INSERT_ID();
	UPDATE items 
		SET organization_id = LAST_INSERT_ID() 
		WHERE organization_id = 0;
	
	SELECT organizations.organization_id, items.item_id
		FROM organizations, items
		WHERE organizations.organization_item = items.item_id
		AND organizations.scorm_id = scorms.scorm_id;
END $
DELIMITER ;

#CALL insertOptional_data_items(_parameters VARCHAR(500), _dataFromLMS VARCHAR(500), _completionThreshold DECIMAL(5,3), _time_limit_action enum('EXITMESSAGE','EXITNOMESSAGE','CONTINUEMESSAGE','CONTINUENOMESSAGE'), _max_time_allowed integer, _RandomizationTiming enum('never','once', 'onEachNewAttempt'), _selectCount int UNSIGNED) 
DROP PROCEDURE IF EXISTS `insertOptional_data_items`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertOptional_data_items`(IN _parameters VARCHAR(500), IN _dataFromLMS VARCHAR(500), IN _completionThreshold DECIMAL(5,3), IN _time_limit_action enum('EXITMESSAGE','EXITNOMESSAGE','CONTINUEMESSAGE','CONTINUENOMESSAGE'), IN _max_time_allowed integer, IN _RandomizationTiming enum('never','once', 'onEachNewAttempt'), IN _selectCount int UNSIGNED)
BEGIN
	INSERT INTO optional_data_items SET parameters = _parameters, dataFromLMS = _dataFromLMS, completionThreshold = _completionThreshold, time_limit_action = _time_limit_action, max_time_allowed = _max_time_allowed, RandomizationTiming = _RandomizationTiming, selectCount = _selectCount;
	SELECT LAST_INSERT_ID();
	UPDATE items 
		SET optional_data_item_id = LAST_INSERT_ID() 
		WHERE item_id = _item_id;
END $
DELIMITER ;

#CALL insertAdlseqSequencing(_item_id integer, _IDRef integer, _attemptLimit integer, _attemptAbsoluteDurationLimit	DECIMAL(10,2), _rollupObjectiveSatisfied BIT(1), IN _rollupProgressCompletion BIT(1), _objectiveMeasureWeight DECIMAL(5,3));
DROP PROCEDURE IF EXISTS `insertAdlseqSequencing`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertAdlseqSequencing`(  IN _item_id integer, IN _IDRef integer, IN _attemptLimit integer, IN _attemptAbsoluteDurationLimit	DECIMAL(10,2), IN _rollupObjectiveSatisfied BIT(1), IN _rollupProgressCompletion BIT(1), IN _objectiveMeasureWeight DECIMAL(5,3))
BEGIN
	INSERT INTO adlseqSequencing SET IDRef = _IDRef, attemptLimit = _attemptLimit, attemptAbsoluteDurationLimit = _attemptAbsoluteDurationLimit, rollupObjectiveSatisfied = _rollupObjectiveSatisfied, rollupProgressCompletion = _rollupProgressCompletion, objectiveMeasureWeight = _objectiveMeasureWeight;
	SELECT LAST_INSERT_ID();
	UPDATE items 
		SET adlseqSequencing_id = LAST_INSERT_ID() 
		WHERE item_id = _item_id;
END $
DELIMITER ;

#CALL insertAdlseqSequencingCollection(_item_id integer, _IDRef integer, _attemptLimit integer, _attemptAbsoluteDurationLimit	DECIMAL(10,2), _rollupObjectiveSatisfied BIT(1), IN _rollupProgressCompletion BIT(1), _objectiveMeasureWeight DECIMAL(5,3));
DROP PROCEDURE IF EXISTS `insertAdlseqSequencingCollection`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertAdlseqSequencing`(  IN _item_id integer, IN _IDRef integer, IN _attemptLimit integer, IN _attemptAbsoluteDurationLimit	DECIMAL(10,2), IN _rollupObjectiveSatisfied BIT(1), IN _rollupProgressCompletion BIT(1), IN _objectiveMeasureWeight DECIMAL(5,3))
BEGIN
	INSERT INTO adlseqSequencing SET IDRef = _IDRef, attemptLimit = _attemptLimit, attemptAbsoluteDurationLimit = _attemptAbsoluteDurationLimit, rollupObjectiveSatisfied = _rollupObjectiveSatisfied, rollupProgressCompletion = _rollupProgressCompletion, objectiveMeasureWeight = _objectiveMeasureWeight;
	SELECT LAST_INSERT_ID();	
END $
DELIMITER ;

#CALL insertAdlnavPresentation(_item_id integer,_hidePrevious BIT(1), _hideContinue BIT(1), _hideExit BIT(1), _hideAbandon BIT(1)); 
DROP PROCEDURE IF EXISTS `insertAdlnavPresentation`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertAdlnavPresentation`(IN _item_id integer, IN _hidePrevious BIT(1), IN _hideContinue BIT(1), IN _hideExit BIT(1), IN _hideAbandon BIT(1))
BEGIN
	INSERT INTO adlnavPresentation SET hidePrevious = _hidePrevious, hideContinue = _hideContinue, hideExit = _hideExit, hideAbandon = _hideAbandon;
	SELECT LAST_INSERT_ID();
	UPDATE items 
		SET adlnavPresentation_id = LAST_INSERT_ID() 
		WHERE item_id = _item_id;
END $
DELIMITER ;

#CALL insertResources( _type VARCHAR(50), _scorm_type BIT(1), _scorm_id integer, _item_id integer);
DROP PROCEDURE IF EXISTS `insertResources`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertResources`( IN _type VARCHAR(50), IN _scorm_type BIT(1), IN _scorm_id integer, IN _item_id integer)
BEGIN
INSERT INTO resources SET type = _type, scorm_type = _scorm_type, scorm_id = _scorm_id, item_id = _item_id;
SELECT LAST_INSERT_ID();
END $
DELIMITER ;

#CALL insertAdlseqControlMode(_item_id integer, _choice BIT(1), _choiceExit BIT(1), _flow BIT(1), _forwardOnly BIT(1), _useCurrentAttemptObjectiveInfo BIT(1), _useCurrentAttemptProgressInfo BIT(1));
DROP PROCEDURE IF EXISTS `insertAdlseqControlMode`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertAdlseqControlMode`( IN _item_id integer,IN _choice BIT(1), IN _choiceExit BIT(1), IN _flow BIT(1), IN _forwardOnly BIT(1), IN _useCurrentAttemptObjectiveInfo BIT(1), IN _useCurrentAttemptProgressInfo BIT(1))
BEGIN
	INSERT INTO adlseqControlMode SET choice = _choice, choiceExit = _choiceExit, flow = _flow, forwardOnly = _forwardOnly, useCurrentAttemptObjectiveInfo = _useCurrentAttemptObjectiveInfo, useCurrentAttemptProgressInfo = _useCurrentAttemptProgressInfo;
	UPDATE items 
		SET adlseqControlMode_id = LAST_INSERT_ID() 
		WHERE item_id = _item_id;
END $
DELIMITER ;

#CALL insertPrimaryObjectives( _adlseqSequencing_id integer, _objective_name VARCHAR(20), _item_id integer, _satisfiedByMeasure BIT(1), _minNormalizedMeasure DECIMAL(3,2));
DROP PROCEDURE IF EXISTS `insertPrimaryObjectives`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertPrimaryObjectives`( IN _adlseqSequencing_id integer, IN _objective_name VARCHAR(20), IN _item_id integer, IN _satisfiedByMeasure BIT(1), IN _minNormalizedMeasure DECIMAL(3,2))
BEGIN
	INSERT INTO objectives SET objective_name = _objective_name, item_id = _item_id, satisfiedByMeasure = _satisfiedByMeasure, minNormalizedMeasure = _minNormalizedMeasure;
	SELECT LAST_INSERT_ID(); 
	UPDATE adlseqSequencing 
		SET objectives_id = LAST_INSERT_ID() 
		WHERE adlseqSequencing_id = _adlseqSequencing_id;
END $
DELIMITER ;

#CALL insertObjectives(_objective_name VARCHAR(20), _item_id integer, _satisfiedByMeasure BIT(1), _minNormalizedMeasure DECIMAL(3,2));
DROP PROCEDURE IF EXISTS `insertObjectives`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertObjectives`(IN _objective_name VARCHAR(20), IN _item_id integer, IN _satisfiedByMeasure BIT(1), IN _minNormalizedMeasure DECIMAL(3,2))
BEGIN
	INSERT INTO objectives SET objective_name = _objective_name, item_id = _item_id, satisfiedByMeasure = _satisfiedByMeasure, minNormalizedMeasure = _minNormalizedMeasure;
	SELECT LAST_INSERT_ID(); 	
END $
DELIMITER ;

#CALL insertAdlseqSequencingRules( _adlseqSequencing_id integer, _conditionRule enum('preConditionRule','postConditionRule','exitConditionRule'), _conditionCombination BIT(1), _measureThreshold DECIMAL(5,3), _condition_always BIT(2), _condition_satisfied BIT(2), _condition_completed BIT(2), _condition_attempted BIT(2), _condition_attemptLimitExceeded BIT(2), _condition_objectiveMeasureGreaterThan BIT(2), _condition_objectiveMeasureLessThan BIT(2), _ruleAction enum('skip','disabled','hiddenFromChoice','stopForwardTransversal','exit','exitParent','exitAll','retry','retryAll','continue','previous'));	
DROP PROCEDURE IF EXISTS `insertAdlseqSequencingRules`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertAdlseqSequencingRules`( IN _adlseqSequencing_id integer, IN _conditionRule enum('preConditionRule','postConditionRule','exitConditionRule'), IN _conditionCombination BIT(1), IN _measureThreshold DECIMAL(5,3), IN _condition_always BIT(2), IN _condition_satisfied BIT(2), IN _condition_completed BIT(2), IN _condition_attempted BIT(2), IN _condition_attemptLimitExceeded BIT(2), IN _condition_objectiveMeasureGreaterThan BIT(2), IN _condition_objectiveMeasureLessThan BIT(2), IN _ruleAction enum('skip','disabled','hiddenFromChoice','stopForwardTransversal','exit','exitParent','exitAll','retry','retryAll','continue','previous') )
BEGIN
INSERT INTO adlseqSequencingRules SET adlseqSequencing_id = _adlseqSequencing_id, conditionRule = _conditionRule, conditionCombination = _conditionCombination, measureThreshold = _measureThreshold, condition_always = _condition_always,condition_satisfied = _condition_satisfied, condition_completed = _condition_completed, condition_attempted = _condition_attempted, condition_attemptLimitExceeded = _condition_attemptLimitExceeded, condition_objectiveMeasureGreaterThan = _condition_objectiveMeasureGreaterThan, condition_objectiveMeasureLessThan = _condition_objectiveMeasureLessThan, ruleAction = _ruleAction;
select LAST_INSERT_ID();
END $
DELIMITER ;
	
#CALL insertAdlseqRollupRule( _adlseqSequencing_id integer, _childActivitySet	enum('none','any','all','atLeastCount','atLeastPercent'), _minimumCount TINYINT, _minimumPercent	DECIMAL(3,2), _condition_never BIT(2), _condition_satisfied BIT(2), _condition_completed BIT(2), _condition_attempted BIT(2), _condition_attemptLimitExceeded BIT(2), _rollupAction	ENUM('satisfied','notSatisfied','completed','incomplete'));
DROP PROCEDURE IF EXISTS `insertAdlseqRollupRule`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertAdlseqRollupRule`( IN _adlseqSequencing_id integer, IN _childActivitySet	enum('none','any','all','atLeastCount','atLeastPercent'), IN _minimumCount TINYINT, IN _minimumPercent	DECIMAL(3,2), IN _condition_never BIT(2), IN _condition_satisfied BIT(2), IN _condition_completed BIT(2), IN _condition_attempted BIT(2), IN _condition_attemptedLimitExceeded BIT(2), IN _rollupAction	ENUM('satisfied','notSatisfied','completed','incomplete'))
BEGIN
	INSERT INTO adlseqRollupRule SET adlseqSequencing_id = _adlseqSequencing_id, childActivitySet = _childActivitySet, minimumCount = _minimumCount, minimumPercent = _minimumPercent, condition_never = _condition_never, condition_satisfied = _condition_satisfied, condition_completed = _condition_completed, condition_attempted = _condition_attempted, condition_attemptedLimitExceeded = _condition_attemptedLimitExceeded, rollupAction = _rollupAction;
	select LAST_INSERT_ID();
END $
DELIMITER ;

#CALL insertAdlseqConstrainedChoiceConsiderations(_item_id integer, _preventActivation BIT(1), _constrainChoice BIT(1), _previous_item integer, _next_item integer);
DROP PROCEDURE IF EXISTS `insertAdlseqConstrainedChoiceConsiderations`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertAdlseqConstrainedChoiceConsiderations`( IN _item_id integer, IN _preventActivation BIT(1), IN _constrainChoice BIT(1), IN _previous_item integer, IN _next_item integer )
BEGIN
INSERT INTO adlseqConstrainedChoiceConsiderations SET preventActivation = _preventActivation, constrainChoice = _constrainChoice, previous_item = _previous_item, next_item = _next_item;
UPDATE items 
		SET adlseqConstrainedChoiceConsiderations_id = LAST_INSERT_ID() 
		WHERE item_id = _item_id;
END $
DELIMITER ;

#CALL insertFiles( _href VARCHAR(100), _server_resources_id integer, _resources_id integer ); 	
DROP PROCEDURE IF EXISTS `insertFiles`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertFiles`( IN _href VARCHAR(100), IN _server_resources_id integer, IN _resources_id integer )
BEGIN
INSERT INTO files SET href = _href, server_resources_id = _server_resources_id , resources_id = _resources_id;
select LAST_INSERT_ID();
END $
DELIMITER ;

#CALL insertComments( _item_id integer, _scorm_id integer, _comment TEXT, _author integer, _is_read BIT(1), _location VARCHAR(30));
DROP PROCEDURE IF EXISTS `insertComments`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertComments`( IN _item_id integer, IN _scorm_id integer, IN _comment TEXT, IN _author integer, IN _is_read BIT(1), IN _location VARCHAR(30) )
BEGIN
	INSERT INTO comments SET item_id = _item_id, scorm_id = _scorm_id, comment = _comment, author = _author, is_read = _is_read, location =_location;
	select LAST_INSERT_ID();
	CALL updateItems_countComments(_item_id);
END $
DELIMITER ;

#CALL insertMetadata( _scorm_id integer, _version VARCHAR(10), _title VARCHAR(30), _description TEXT, _requeriments TEXT, _end_user VARCHAR(30), _educative_environment VARCHAR(30), _author TEXT, _learning_duration VARCHAR(10), _beginning_date datetime, _ending_date datetime);
DROP PROCEDURE IF EXISTS `insertMetadata`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertMetadata`( IN _scorm_id integer, IN _version VARCHAR(10), IN _title VARCHAR(30), IN _description TEXT, IN _requeriments TEXT, IN _end_user VARCHAR(30), IN _educative_environment VARCHAR(30), IN _author TEXT, IN _learning_duration VARCHAR(10), IN _beginning_date datetime, IN _ending_date datetime )
BEGIN
INSERT INTO metadata SET scorm_id = _scorm_id, `version` = _version, title =_title, description = _description, requeriments = _requeriments, end_user = _end_user, educative_environment = _educative_environment, author = _author, learning_duration = _learning_duration, beginning_date = _beginning_date, ending_date = _ending_date;
select LAST_INSERT_ID();
END $
DELIMITER ;

#CALL insertStudentsClass( _organization_id integer, _global_student_id VARCHAR(100));
DROP PROCEDURE IF EXISTS `insertStudentsClass`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertStudentsClass`( IN _organization_id integer, IN _global_student_id VARCHAR(100) )
BEGIN
	INSERT INTO studentsClass SET organization_id = _organization_id, global_student_id = _global_student_id;
	SELECT LAST_INSERT_ID();
END $
DELIMITER ;


################################################################################
##   		##   		  update*   		##   		      ##
################################################################################

#Marquem el comentari com a llegit.
#	CALL updateComment_isRead(_comment_id integer);
DROP PROCEDURE IF EXISTS `updateComments_isRead`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateComments_isRead`( IN _comment_id integer )
BEGIN
	UPDATE comments 
	SET is_read = 1 
	WHERE comment_id = _comment_id;
END $
DELIMITER ;

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

#Incrementem el número de comentaris que té un item
#	CALL updateItems_countComments(_item_id integer);
DROP PROCEDURE IF EXISTS `updateItems_countComments`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateItems_countComments`( IN _item_id integer )
BEGIN
	UPDATE items 
	SET countComments = countComments + 1 
	WHERE item_id = _item_id;
END $
DELIMITER ;

#Modifiquem les dades de l'usuari
#Nota! 
#  Suposem que només s'actualitzaran aquestes dades al final d'una sessió 
#  de l'usuari! Ja que és quan incrementem el número de visites de l'usuari.

#	CALL updateStudentsClass(_organization_id integer, _student_id integer, _satisfied BIT(1), _completed BIT(1), _progress_measure DECIMAL(5,3), _last_item_title VARCHAR(30), _completed_percent DECIMAL(5,3));
DROP PROCEDURE IF EXISTS `updateStudentsClass`;
DELIMITER $
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateStudentsClass`( IN _organization_id integer, IN _student_id integer, IN _satisfied BIT(1), IN _completed BIT(1), IN _progress_measure DECIMAL(5,3), IN _last_item_title VARCHAR(30), IN _completed_percent	DECIMAL(5,3))
BEGIN
	UPDATE studentsClass 
	SET satisfied = _satisfied, completed = _completed, progress_measure = _measure, last_item_title = _last_item_title, completed_percent = _completed_percent, visits = visits + 1
	WHERE organization_id = _organization_id AND student_id = _student_id;
END $
DELIMITER ;

################################################################################
##   		##   		  select*   		##   		      ##
################################################################################

	
