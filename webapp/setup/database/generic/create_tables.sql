CREATE TABLE CMS_USERS (
	USER_ID VARCHAR2(36) NOT NULL,
	USER_NAME VARCHAR2(64) NOT NULL,
	USER_PASSWORD VARCHAR2(32) NOT NULL,
	USER_DESCRIPTION VARCHAR2(255) NOT NULL,
	USER_FIRSTNAME VARCHAR2(50) NOT NULL,
	USER_LASTNAME VARCHAR2(50) NOT NULL,
	USER_EMAIL VARCHAR2(100) NOT NULL,
	USER_LASTLOGIN DATE NOT NULL,
	USER_FLAGS INT NOT NULL,
	USER_INFO LONG RAW,
	USER_ADDRESS VARCHAR2(100) NOT NULL,
	USER_TYPE INT NOT NULL,
	PRIMARY KEY(USER_ID),
	UNIQUE(USER_NAME)
);

CREATE TABLE CMS_PROJECTS (
	PROJECT_ID INT NOT NULL,
	USER_ID VARCHAR2(36) NOT NULL,
	GROUP_ID VARCHAR2(36) NOT NULL,
	MANAGERGROUP_ID VARCHAR2(36) NOT NULL,
	TASK_ID INT NOT NULL,
	PROJECT_NAME VARCHAR2(64) NOT NULL,
	PROJECT_DESCRIPTION VARCHAR2(255) NOT NULL,
	PROJECT_FLAGS INT NOT NULL,
	PROJECT_CREATEDATE DATE NOT NULL,
	PROJECT_TYPE INT NOT NULL,
	PRIMARY KEY (PROJECT_ID),
	UNIQUE(PROJECT_NAME,PROJECT_CREATEDATE)
);

CREATE TABLE CMS_BACKUP_PROJECTS (
	VERSION_ID INT NOT NULL,
	PROJECT_ID INT NOT NULL,
	PROJECT_NAME VARCHAR2(64) NOT NULL,
	PROJECT_PUBLISHDATE DATE,
	PROJECT_PUBLISHED_BY VARCHAR2(36) NOT NULL,
	PROJECT_PUBLISHED_BY_NAME VARCHAR2(167),
	USER_ID VARCHAR2(36) NOT NULL,
	USER_NAME VARCHAR2(167),
	GROUP_ID VARCHAR2(36) NOT NULL,
	GROUP_NAME VARCHAR2(64),
	MANAGERGROUP_ID VARCHAR2(36) NOT NULL,
	MANAGERGROUP_NAME VARCHAR2(64),
	PROJECT_DESCRIPTION VARCHAR2(255) NOT NULL,
	PROJECT_CREATEDATE DATE NOT NULL,
	PROJECT_TYPE INT NOT NULL,
	TASK_ID INT NOT NULL,
	PRIMARY KEY(VERSION_ID)
);

CREATE TABLE CMS_PROJECTRESOURCES (
	PROJECT_ID INT NOT NULL,
	RESOURCE_PATH VARCHAR2(1024) NOT NULL,
	PRIMARY KEY(PROJECT_ID, RESOURCE_PATH)
);

CREATE TABLE CMS_BACKUP_PROJECTRESOURCES (
	VERSION_ID INT NOT NULL,
	PROJECT_ID INT NOT NULL,
	RESOURCE_PATH VARCHAR2(1024) NOT NULL,
	PRIMARY KEY (VERSION_ID, PROJECT_ID, RESOURCE_PATH)
);

CREATE TABLE CMS_OFFLINE_PROPERTYDEF (
	PROPERTYDEF_ID INT NOT NULL,
	PROPERTYDEF_NAME VARCHAR2(64) NOT NULL,
	PROPERTYDEF_MAPPING_TYPE INT NOT NULL,
	PRIMARY KEY(PROPERTYDEF_ID),
	UNIQUE(PROPERTYDEF_NAME, PROPERTYDEF_MAPPING_TYPE)
);

CREATE TABLE CMS_ONLINE_PROPERTYDEF (
	PROPERTYDEF_ID INT NOT NULL,
	PROPERTYDEF_NAME VARCHAR2(64) NOT NULL,
	PROPERTYDEF_MAPPING_TYPE INT NOT NULL,
	PRIMARY KEY(PROPERTYDEF_ID),
	UNIQUE(PROPERTYDEF_NAME, PROPERTYDEF_MAPPING_TYPE)
);

CREATE TABLE CMS_BACKUP_PROPERTYDEF (
	PROPERTYDEF_ID INT NOT NULL,
	PROPERTYDEF_NAME VARCHAR2(64) NOT NULL,
	PROPERTYDEF_MAPPING_TYPE INT NOT NULL,
	PRIMARY KEY(PROPERTYDEF_ID),
	UNIQUE(PROPERTYDEF_NAME, PROPERTYDEF_MAPPING_TYPE)
);

CREATE TABLE CMS_OFFLINE_PROPERTIES (
	PROPERTY_ID INT NOT NULL,
	PROPERTYDEF_ID INT NOT NULL,
	PROPERTY_MAPPING_ID VARCHAR2(36) NOT NULL,
	PROPERTY_MAPPING_TYPE INT NOT NULL,	
	PROPERTY_VALUE VARCHAR2(255) NOT NULL,
	PRIMARY KEY(PROPERTY_ID)
);

CREATE TABLE CMS_ONLINE_PROPERTIES (
	PROPERTY_ID INT NOT NULL,
	PROPERTYDEF_ID INT NOT NULL,
	PROPERTY_MAPPING_ID VARCHAR2(36) NOT NULL,
	PROPERTY_MAPPING_TYPE INT NOT NULL,	
	PROPERTY_VALUE VARCHAR2(255) NOT NULL,
	PRIMARY KEY(PROPERTY_ID)
);

CREATE TABLE CMS_BACKUP_PROPERTIES (
	PROPERTY_ID INT NOT NULL,
	PROPERTYDEF_ID INT NOT NULL,
	PROPERTY_MAPPING_ID VARCHAR2(36) NOT NULL,
	PROPERTY_MAPPING_TYPE INT NOT NULL,		
	PROPERTY_VALUE VARCHAR2(255) NOT NULL,
	VERSION_ID INT,
	PRIMARY KEY(PROPERTY_ID)
);

CREATE TABLE CMS_OFFLINE_RESOURCES (
	RESOURCE_ID VARCHAR2(36) NOT NULL,
	RESOURCE_TYPE INT NOT NULL,
	RESOURCE_FLAGS INT NOT NULL,
	DATE_CREATED DATE NOT NULL,
	DATE_LASTMODIFIED DATE NOT NULL,
	USER_CREATED VARCHAR2(36) NOT NULL,
	USER_LASTMODIFIED VARCHAR2(36) NOT NULL,
	PROJECT_LASTMODIFIED INT NOT NULL,
	RESOURCE_STATE INT NOT NULL,
	RESOURCE_SIZE INT NOT NULL,
	SIBLING_COUNT INT NOT NULL,
	PRIMARY KEY(RESOURCE_ID)
);

CREATE TABLE CMS_ONLINE_RESOURCES (
	RESOURCE_ID VARCHAR2(36) NOT NULL,
	RESOURCE_TYPE INT NOT NULL,
	RESOURCE_FLAGS INT NOT NULL,
	DATE_CREATED DATE NOT NULL,
	DATE_LASTMODIFIED DATE NOT NULL,
	USER_CREATED VARCHAR2(36) NOT NULL,
	USER_LASTMODIFIED VARCHAR2(36) NOT NULL,
	PROJECT_LASTMODIFIED INT NOT NULL,
	RESOURCE_STATE INT NOT NULL,
	RESOURCE_SIZE INT NOT NULL,
	SIBLING_COUNT INT NOT NULL,
	PRIMARY KEY(RESOURCE_ID)
);

CREATE TABLE CMS_BACKUP_RESOURCES (
	BACKUP_ID VARCHAR2(36) NOT NULL,
	RESOURCE_ID VARCHAR2(36) NOT NULL,
	RESOURCE_TYPE INT NOT NULL,
	RESOURCE_FLAGS INT NOT NULL,
	DATE_CREATED DATE NOT NULL,
	DATE_LASTMODIFIED DATE NOT NULL,
	USER_CREATED VARCHAR2(36) NOT NULL,
	USER_LASTMODIFIED VARCHAR2(36) NOT NULL,
	PROJECT_LASTMODIFIED INT NOT NULL,
	RESOURCE_STATE INT NOT NULL,
	RESOURCE_SIZE INT NOT NULL,
	SIBLING_COUNT INT NOT NULL,
	VERSION_ID INT NOT NULL,
	USER_CREATED_NAME VARCHAR2(64) NOT NULL,
	USER_LASTMODIFIED_NAME VARCHAR2(64) NOT NULL,
	PRIMARY KEY(BACKUP_ID),
	UNIQUE(VERSION_ID,RESOURCE_ID)
);

CREATE TABLE CMS_OFFLINE_CONTENTS (
	CONTENT_ID VARCHAR2(36) NOT NULL,
	RESOURCE_ID VARCHAR2(36) NOT NULL,
	FILE_CONTENT LONG RAW NOT NULL,
	PRIMARY KEY (CONTENT_ID)
);

CREATE TABLE CMS_ONLINE_CONTENTS (
	CONTENT_ID VARCHAR2(36) NOT NULL,
	RESOURCE_ID VARCHAR2(36) NOT NULL,
	FILE_CONTENT LONG RAW NOT NULL,
	PRIMARY KEY (CONTENT_ID)
);

CREATE TABLE CMS_BACKUP_CONTENTS (
	BACKUP_ID VARCHAR2(36) NOT NULL,
	CONTENT_ID VARCHAR2(36) NOT NULL,
	RESOURCE_ID VARCHAR2(36) NOT NULL,
	FILE_CONTENT LONG RAW NOT NULL,
	VERSION_ID INT,
	PRIMARY KEY (BACKUP_ID)
);

CREATE TABLE CMS_GROUPS (
	GROUP_ID VARCHAR2(36) NOT NULL,
	PARENT_GROUP_ID VARCHAR2(36) NOT NULL,
	GROUP_NAME VARCHAR2(64) NOT NULL,
	GROUP_DESCRIPTION VARCHAR2(255) NOT NULL,
	GROUP_FLAGS INT NOT NULL,
	PRIMARY KEY(GROUP_ID),
	UNIQUE(GROUP_NAME)
);

CREATE TABLE CMS_SYSTEMID (
	TABLE_KEY VARCHAR2(255) NOT NULL,
	ID INT NOT NULL,
	PRIMARY KEY (TABLE_KEY)
);

CREATE TABLE CMS_EXPORT_DEPENDENCIES (
	LINK_ID INT NOT NULL,
	RESOURCENAME VARCHAR2(255),
	UNIQUE(LINK_ID, RESOURCENAME)
);

CREATE TABLE CMS_GROUPUSERS (
	GROUP_ID VARCHAR2(36) NOT NULL,
	USER_ID VARCHAR2(36) NOT NULL,
	GROUPUSER_FLAGS INT NOT NULL
);

CREATE TABLE CMS_Task (
	AUTOFINISH INT,
	ENDTIME DATE,
	ESCALATIONTYPEREF INT,
	ID INT NOT NULL,
	INITIATORUSERREF VARCHAR2(36),
	MILESTONEREF INT,
	NAME VARCHAR(254),
	ORIGINALUSERREF VARCHAR2(36),
	AGENTUSERREF VARCHAR2(36),
	PARENT INT,
	PERCENTAGE VARCHAR(50),
	PERMISSION VARCHAR(50),
	PRIORITYREF INT DEFAULT '2',
	ROLEREF VARCHAR2(36),
	ROOT INT,
	STARTTIME DATE,
	STATE INT,
	TASKTYPE INT,
	TIMEOUT DATE,
	WAKEUPTIME DATE,
	HTMLLINK VARCHAR(254),
	ESTIMATETIME INT DEFAULT '86400',
	PRIMARY KEY(ID)
);

CREATE TABLE CMS_TaskType (
	AUTOFINISH INT,
	ESCALATIONTYPEREF INT,
	HTMLLINK VARCHAR(254),
	ID INT NOT NULL,
	NAME VARCHAR(50),
	PERMISSION VARCHAR(50),
	PRIORITYREF INT,
	ROLEREF VARCHAR2(36),
	PRIMARY KEY(ID)
);

CREATE TABLE CMS_TaskLog (
	COMENT LONG,
	EXTERNALUSERNAME VARCHAR(254),
	ID INT NOT NULL,
	STARTTIME DATE,
	TASKREF INT,
	USERREF INT,
	TYPE INT DEFAULT '0',
	PRIMARY KEY(ID)
);

CREATE TABLE CMS_TaskPar (
	ID INT NOT NULL,
	PARNAME VARCHAR(50),
	PARVALUE VARCHAR(50),
	REF INT,
	PRIMARY KEY(ID)
);
 
CREATE TABLE CMS_WEBUSERS (
	USER_ID VARCHAR2(36) NOT NULL,
	USER_MEMBER_ID VARCHAR(255),
	USER_SALUTATION VARCHAR(255),
	USER_TITLE VARCHAR(255),
	USER_PWD VARCHAR(255),   
	USER_PWD_QUESTION VARCHAR(255),   
	USER_PWD_ANSWER VARCHAR(255),        
	USER_CITY VARCHAR(255),
	USER_POSTCODE VARCHAR(255),
	USER_STATE VARCHAR(255),
	USER_COUNTRY VARCHAR(255),
	USER_ADDRESS_TYPE INT,
	USER_BIRTHDAY DATE,
	USER_PHONE VARCHAR(255),
	USER_FAX VARCHAR(255),
	USER_MOBILE VARCHAR(255),
	USER_ACCEPT INT,   
	USER_RECOMMENDED_BY VARCHAR(255), 
	USER_PROFESSION VARCHAR(255),
	USER_COMPANY VARCHAR(255),
	USER_DEPARTMENT VARCHAR(255),
	USER_POSITION VARCHAR(255),         
	USER_ACCOUNT_NUMBER VARCHAR(255),
	USER_BANK_NUMBER VARCHAR(255),
	USER_BANK VARCHAR(255),
	USER_NEWSLETTER VARCHAR(255),   
	USER_EXTRAINFO_1 VARCHAR(255),
	USER_EXTRAINFO_2 VARCHAR(255),
	USER_EXTRAINFO_3 VARCHAR(255),
	USER_EXTRAINFO_4 VARCHAR(255),
	USER_EXTRAINFO_5 VARCHAR(255),
	USER_EXTRAINFO_6 VARCHAR(255),
	USER_EXTRAINFO_7 VARCHAR(255),
	USER_EXTRAINFO_8 VARCHAR(255),
	USER_EXTRAINFO_9 VARCHAR(255),
	USER_EXTRAINFO_10 VARCHAR(255),
	USER_EXTRAINFO_11 VARCHAR(255),
	USER_EXTRAINFO_12 VARCHAR(255),
	USER_EXTRAINFO_13 VARCHAR(255),
	USER_EXTRAINFO_14 VARCHAR(255),
	USER_EXTRAINFO_15 VARCHAR(255),
	USER_EXTRAINFO_16 VARCHAR(255),
	USER_EXTRAINFO_17 VARCHAR(255),
	USER_EXTRAINFO_18 VARCHAR(255),
	USER_EXTRAINFO_19 BLOB,
	USER_EXTRAINFO_20 BLOB,
	USER_PICTURE BLOB,
	USER_PICTURE_NAME VARCHAR(255),         
	USER_CREATE_DATE DATE,
	USER_LASTCHANGE_BY VARCHAR(255),
	USER_LASTCHANGE_DATE DATE,
	LOCKSTATE INT,
	PRIMARY KEY(USER_ID)
);

CREATE TABLE CMS_ONLINE_ACCESSCONTROL (
	RESOURCE_ID VARCHAR2(36) NOT NULL,
	PRINCIPAL_ID VARCHAR2(36) NOT NULL,
	ACCESS_ALLOWED INT,
	ACCESS_DENIED INT,
	ACCESS_FLAGS INT,
	PRIMARY KEY(RESOURCE_ID, PRINCIPAL_ID)
);

CREATE TABLE CMS_OFFLINE_ACCESSCONTROL (
	RESOURCE_ID VARCHAR2(36) NOT NULL,
	PRINCIPAL_ID VARCHAR2(36) NOT NULL,
	ACCESS_ALLOWED INT,
	ACCESS_DENIED INT,
	ACCESS_FLAGS INT,
	PRIMARY KEY(RESOURCE_ID, PRINCIPAL_ID)
);
   
CREATE TABLE CMS_OFFLINE_STRUCTURE (
	STRUCTURE_ID VARCHAR2(36) NOT NULL,
	RESOURCE_ID VARCHAR2(36) NOT NULL,
	RESOURCE_PATH VARCHAR2(1024) NOT NULL,
	STRUCTURE_STATE INT NOT NULL,
	DATE_RELEASED DATE NOT NULL,
	DATE_EXPIRED DATE NOT NULL,
	PRIMARY KEY(STRUCTURE_ID)
);

CREATE TABLE CMS_ONLINE_STRUCTURE (
	STRUCTURE_ID VARCHAR2(36) NOT NULL,
	RESOURCE_ID VARCHAR2(36) NOT NULL,
	RESOURCE_PATH VARCHAR2(1024) NOT NULL,
	STRUCTURE_STATE INT NOT NULL,	
	DATE_RELEASED DATE NOT NULL,
	DATE_EXPIRED DATE NOT NULL,
	PRIMARY KEY(STRUCTURE_ID)
);

CREATE TABLE CMS_BACKUP_STRUCTURE (
	BACKUP_ID VARCHAR2(36) NOT NULL,
	VERSION_ID INT NOT NULL,
	STRUCTURE_ID VARCHAR2(36) NOT NULL,
	RESOURCE_ID VARCHAR2(36) NOT NULL,
	RESOURCE_PATH VARCHAR2(1024) NOT NULL,
	STRUCTURE_STATE INT NOT NULL,
	DATE_RELEASED DATE NOT NULL,
	DATE_EXPIRED DATE NOT NULL,
	PRIMARY KEY(BACKUP_ID)
);   
 
CREATE TABLE CMS_PUBLISH_HISTORY (
	PUBLISH_ID VARCHAR(36) NOT NULL,
	TAG_ID INT NOT NULL,
	STRUCTURE_ID VARCHAR2(36) NOT NULL,
	RESOURCE_ID VARCHAR2(36) NOT NULL,
	RESOURCE_PATH VARCHAR2(1024) NOT NULL,
	RESOURCE_STATE INT NOT NULL,
	RESOURCE_TYPE INT NOT NULL,
	SIBLING_COUNT INT NOT NULL,
	MASTER_ID VARCHAR(36) NOT NULL,
	CONTENT_DEFINITION_NAME VARCHAR(128),
	CONSTRAINT PK_PUBLISH_HISTORY PRIMARY KEY(PUBLISH_ID,TAG_ID,STRUCTURE_ID,MASTER_ID) USING INDEX TABLESPACE ${indexTablespace}
);

CREATE TABLE CMS_STATICEXPORT_LINKS (
	LINK_ID VARCHAR(36) NOT NULL,
	RESOURCE_PATH VARCHAR(1024) NOT NULL,
	LINK_TYPE INT NOT NULL,
	LINK_PARAMETER TEXT,
	LINK_TIMESTAMP BIGINT,
	PRIMARY KEY(LINK_ID)	
);



CREATE INDEX IDX_PUBLISH_HISTORY_01 
	ON CMS_PUBLISH_HISTORY(PUBLISH_ID);

CREATE INDEX IDX_GROUPS_01 
	ON CMS_GROUPS(PARENT_GROUP_ID);

CREATE INDEX IDX_GROUPUSERS_01 
	ON CMS_GROUPUSERS(GROUP_ID);
	
CREATE INDEX IDX_GROUPUSERS_02 
	ON CMS_GROUPUSERS(USER_ID);

CREATE INDEX IDX_PROJECTS_01 
	ON CMS_PROJECTS(GROUP_ID);
	
CREATE INDEX IDX_PROJECTS_02 
	ON CMS_PROJECTS(MANAGERGROUP_ID);
	
CREATE INDEX IDX_PROJECTS_03 
	ON CMS_PROJECTS(USER_ID);
	
CREATE INDEX IDX_PROJECTS_04 
	ON CMS_PROJECTS(PROJECT_NAME);
	
CREATE INDEX IDX_PROJECTS_05 
	ON CMS_PROJECTS(TASK_ID);
	
CREATE INDEX IDX_PROJECTS_06 
	ON CMS_PROJECTS (PROJECT_FLAGS);
	
CREATE INDEX IDX_OFFLINE_RESOURCES_01 
	ON CMS_OFFLINE_RESOURCES (RESOURCE_TYPE);
  
CREATE INDEX IDX_ONLINE_RESOURCES_01 
	ON CMS_ONLINE_RESOURCES (RESOURCE_TYPE);

CREATE INDEX IDX_OFFLINE_STRUCTURE_01 
	ON CMS_OFFLINE_STRUCTURE (STRUCTURE_ID,RESOURCE_PATH);
	
CREATE INDEX IDX_OFFLINE_STRUCTURE_03 
	ON CMS_OFFLINE_STRUCTURE (STRUCTURE_ID,RESOURCE_ID);
	
CREATE INDEX IDX_OFFLINE_STRUCTURE_04 
	ON CMS_OFFLINE_STRUCTURE (STRUCTURE_STATE);
	
CREATE INDEX IDX_OFFLINE_STRUCTURE_05 
	ON CMS_OFFLINE_STRUCTURE (RESOURCE_ID);

CREATE INDEX IDX_ONLINE_STRUCTURE_01 
	ON CMS_ONLINE_STRUCTURE (STRUCTURE_ID,RESOURCE_PATH);
	
CREATE INDEX IDX_ONLINE_STRUCTURE_03 
	ON CMS_ONLINE_STRUCTURE (STRUCTURE_ID,RESOURCE_ID);
	
CREATE INDEX IDX_ONLINE_STRUCTURE_04 
	ON CMS_ONLINE_STRUCTURE (STRUCTURE_STATE);
	
CREATE INDEX IDX_ONLINE_STRUCTURE_05 
	ON CMS_ONLINE_STRUCTURE (RESOURCE_ID);  
	
CREATE INDEX IDX_OFFLINE_PROPERTIES_01 
	ON CMS_OFFLINE_PROPERTIES (PROPERTYDEF_ID,PROPERTY_MAPPING_ID,PROPERTY_MAPPING_TYPE);
	
CREATE INDEX IDX_OFFLINE_PROPERTIES_02 
	ON CMS_OFFLINE_PROPERTIES (PROPERTYDEF_ID);

CREATE INDEX IDX_ONLINE_PROPERTIES_01 
	ON CMS_ONLINE_PROPERTIES (PROPERTYDEF_ID,PROPERTY_MAPPING_ID,PROPERTY_MAPPING_TYPE);
	
CREATE INDEX IDX_ONLINE_PROPERTIES_02 
	ON CMS_ONLINE_PROPERTIES (PROPERTYDEF_ID);

CREATE INDEX IDX_OFFLINE_PROPERTYDEF_01 
	ON CMS_OFFLINE_PROPERTYDEF (PROPERTYDEF_MAPPING_TYPE);
	
CREATE INDEX IDX_ONLINE_PROPERTYDEF_01 
	ON CMS_ONLINE_PROPERTYDEF (PROPERTYDEF_MAPPING_TYPE);

CREATE INDEX IDX_SYSTEMID_01 
	ON CMS_SYSTEMID(TABLE_KEY,ID);

CREATE INDEX IDX_TASK_01 
	ON CMS_TASK(PARENT);
	
CREATE INDEX IDX_TASK_02 
	ON CMS_TASK(TASKTYPEREF);

CREATE INDEX IDX_TASKLOG_01
	ON CMS_TASKLOG(TASKREF);
	
CREATE INDEX IDX_TASKLOG_02 
	ON CMS_TASKLOG(USERREF);

CREATE INDEX IDX_TASKPAR_01 
	ON CMS_TASKPAR(REF);
	
CREATE INDEX IDX_PROJECTRESOURCES_01 
	ON CMS_PROJECTRESOURCES(RESOURCE_PATH); 
