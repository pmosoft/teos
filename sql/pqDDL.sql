-----------------------------------------
-- I_SCHEDULE
-----------------------------------------

-- DROP TABLE public.schedule;
CREATE TABLE PUBLIC.I_SCHEDULE (
	SCHEDULE_ID NUMERIC NULL,
	TYPE_CD VARCHAR(10) NULL,
	SCENARIO_ID NUMERIC NULL,
	USER_ID VARCHAR(13) NULL,
	PRIORITIZE VARCHAR(20) NULL,
	PROCESS_CD VARCHAR(20) NULL,
	PROCESS_MSG VARCHAR(1500) NULL,
	SCENARIO_PATH VARCHAR(256) NULL,
	REG_DT DATE NULL,
	MODIFY_DT DATE NULL,
	RETRY_CNT NUMERIC NULL,
	SERVER_ID VARCHAR(10) NULL,
	BIN_X_CNT NUMERIC NULL,
	BIN_Y_CNT NUMERIC NULL,
	RU_CNT NUMERIC NULL,
	ANALYSIS_WEIGHT NUMERIC NULL,
	PHONE_NO VARCHAR(12) NULL,
	RESULT_TIME NUMERIC NULL,
	TILT_PROCESS_TYPE NUMERIC NULL,
	GEOMETRYQUERY_SCHEDULE_ID NUMERIC NULL,
	RESULT_BIT BPCHAR(8) NULL,
	INTERWORKING_INFO VARCHAR(200) NULL
);

CREATE INDEX I_SCHEDULE_IDX ON PUBLIC.I_SCHEDULE USING BTREE (SCENARIO_ID, SCHEDULE_ID);
ALTER TABLE PUBLIC.I_SCHEDULE OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_SCHEDULE TO POSTGRES;

-----------------------------------------
-- I_SCENARIO
-----------------------------------------
-- DROP TABLE PUBLIC.I_SCENARIO;

CREATE TABLE PUBLIC.I_SCENARIO (
	SCENARIO_ID NUMERIC NULL,
	SCENARIO_NM VARCHAR(200) NULL,
	USER_ID VARCHAR(13) NULL,
	SYSTEM_ID NUMERIC NULL,
	NETWORK_TYPE NUMERIC NULL,
	SIDO_CD VARCHAR(10) NULL,
	SIGUGUN_CD VARCHAR(10) NULL,
	DONG_CD VARCHAR(10) NULL,
	SIDO VARCHAR(30) NULL,
	SIGUGUN VARCHAR(30) NULL,
	DONG VARCHAR(30) NULL,
	STARTX NUMERIC NULL,
	STARTY NUMERIC NULL,
	ENDX NUMERIC NULL,
	ENDY NUMERIC NULL,
	FA_MODEL_ID NUMERIC NULL,
	FA_SEQ NUMERIC NULL,
	SCENARIO_DESC VARCHAR(500) NULL,
	USE_BUILDING NUMERIC NULL,
	USE_MULTIFA NUMERIC NULL,
	PRECISION NUMERIC NULL,
	PWRCTRLCHECKPOINT NUMERIC NULL,
	MAXITERATIONPWRCTRL NUMERIC NULL,
	RESOLUTION NUMERIC NULL,
	MODEL_RADIUS NUMERIC NULL,
	REG_DT DATE NULL,
	MODIFY_DT DATE NULL,
	UPPER_SCENARIO_ID NUMERIC NULL,
	FLOORBUILDING NUMERIC NULL,
	FLOOR NUMERIC NULL,
	FLOORLOSS NUMERIC NULL,
	SCENARIO_SUB_ID NUMERIC NULL,
	SCENARIO_SOLUTION_NUM NUMERIC NULL,
	LOSS_TYPE NUMERIC NULL,
	RU_CNT NUMERIC NULL,
	MODIFY_YN BPCHAR(1) NULL DEFAULT 'N'::BPCHAR,
	BATCH_YN BPCHAR(1) NULL DEFAULT 'N'::BPCHAR,
	TM_STARTX NUMERIC NULL,
	TM_STARTY NUMERIC NULL,
	TM_ENDX NUMERIC NULL,
	TM_ENDY NUMERIC NULL,
	REAL_DATE VARCHAR(10) NULL,
	REAL_DRM_FILE VARCHAR(100) NULL,
	REAL_COMP VARCHAR(16) NULL,
	REAL_CATT VARCHAR(10) NULL,
	REAL_CATY VARCHAR(1) NULL,
	BLD_TYPE VARCHAR(20) NULL,
	RET_PERIOD NUMERIC NULL,
	RET_END_DATETIME VARCHAR(10) NULL,
	BUILDINGANALYSIS3D_YN VARCHAR(1) NULL,
	BUILDINGANALYSIS3D_RESOLUTION NUMERIC NULL,
	AREA_ID NUMERIC NULL,
	BUILDINGANALYSIS3D_RELATED_YN VARCHAR(1) NULL,
	RELATED_ANALYSIS_COVLIMITRSRP NUMERIC NULL
);
CREATE INDEX I_SCENARIO_IDX ON PUBLIC.I_SCENARIO USING BTREE (SCENARIO_ID);

ALTER TABLE PUBLIC.I_SCENARIO OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_SCENARIO TO POSTGRES;


-----------------------------------------
-- I_DU
-----------------------------------------
-- DROP TABLE PUBLIC.I_DU;
CREATE TABLE PUBLIC.I_DU (
 SCENARIO_ID           NUMERIC NOT NULL
,ENB_ID                VARCHAR(10) NOT NULL
,E_NODEB_NM            VARCHAR(100)  
,PCI_CNT               NUMERIC  
,STRMAKER              VARCHAR(30)  
,REG_DT                DATE  
);

CREATE INDEX I_DU_IDX ON PUBLIC.I_DU USING BTREE (SCENARIO_ID, ENB_ID);
ALTER TABLE PUBLIC.I_DU OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_DU TO POSTGRES;

-----------------------------------------
-- I_RU
-----------------------------------------
-- DROP TABLE PUBLIC.I_RU;

CREATE TABLE PUBLIC.I_RU
(
 SCENARIO_ID                   NUMERIC NOT NULL
,ENB_ID                        VARCHAR(10) NOT NULL
,PCI                           NUMERIC NOT NULL
,PCI_PORT                      NUMERIC NOT NULL
,RU_ID                         VARCHAR(48) NOT NULL
,MAKER                         VARCHAR(30)  
,SITE_TYPE                     CHAR(18)  
,PAIR_ENODEB                   NUMERIC  
,REPEATERATTENUATION           NUMERIC  
,REPEATERPWRRATIO              NUMERIC  
,RU_NM                         VARCHAR(100)  
,FA_SEQ                        NUMERIC NOT NULL
,SECTOR_ORD                    NUMERIC NOT NULL
,RU_SEQ                        NUMERIC  
,RRH_SEQ                       NUMERIC  
,REG_DT                        DATE  
,SWING_YN                      NUMERIC  
,ANT_CHK_YN                    NUMERIC  
,TILT_YN                       NUMERIC  
,FA_SEQ_ORG                    NUMERIC  
);

CREATE INDEX I_RU_IDX ON PUBLIC.I_RU USING BTREE (SCENARIO_ID,ENB_ID,PCI,PCI_PORT,RU_ID,FA_SEQ,SECTOR_ORD);
ALTER TABLE PUBLIC.I_RU OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_RU TO POSTGRES;


-----------------------------------------
-- I_SITE
-----------------------------------------
-- DROP TABLE PUBLIC.I_SITE;

CREATE TABLE PUBLIC.I_SITE
(
 SCENARIO_ID                   NUMERIC  
,ENB_ID                        VARCHAR(10)  
,PCI                           NUMERIC  
,PCI_PORT                      NUMERIC  
,SITE_NM                       VARCHAR(100)  
,XPOSITION                     VARCHAR(40)  
,YPOSITION                     VARCHAR(40)  
,HEIGHT                        NUMERIC  
,BLT_HEIGHT                    NUMERIC  
,TOWER_HEIGHT                  NUMERIC  
,SITE_ADDR                     VARCHAR(500)  
,TYPE                          VARCHAR(10)  
,CORRECTION_VALUE              NUMERIC  
,FEEDER_LOSS                   NUMERIC  
,FADE_MARGIN                   NUMERIC  
,STATUS                        VARCHAR(10)  
,MSC                           NUMERIC  
,BSC                           NUMERIC  
,NETWORKID                     NUMERIC  
,USABLETRAFFICCH               NUMERIC  
,SYSTEMID                      NUMERIC  
,STRYPOS                       VARCHAR(20)  
,STRXPOS                       VARCHAR(20)  
,ATTENUATION                   NUMERIC  
,SITE_GUBUN                    VARCHAR(10)  
,RU_ID                         VARCHAR(48)  
,RADIUS                        NUMERIC  
,NOISEFLOOR                    NUMERIC  
,FA_SEQ                        VARCHAR(20)  
,RU_TYPE                       NUMERIC  
,REG_DT                        DATE  
,SISUL_CD                      VARCHAR(50)  
,TM_XPOSITION                  VARCHAR(40)  
,TM_YPOSITION                  VARCHAR(40)  
,RU_DIV_CD                     NUMERIC  
);
CREATE INDEX I_SITE_IDX1 ON PUBLIC.I_SITE USING BTREE (ENB_ID,PCI,PCI_PORT,RU_ID);
CREATE INDEX I_SITE_IDX2 ON PUBLIC.I_SITE USING BTREE (YPOSITION,XPOSITION);
CREATE INDEX I_SITE_IDX3 ON PUBLIC.I_SITE USING BTREE (TYPE);
CREATE INDEX I_SITE_IDX4 ON PUBLIC.I_SITE USING BTREE (SCENARIO_ID);
ALTER TABLE PUBLIC.I_SITE OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_SITE TO POSTGRES;

-----------------------------------------
-- I_SITE
-----------------------------------------
-- DROP TABLE PUBLIC.I_SCENARIO_NR_RU;

CREATE TABLE PUBLIC.I_SCENARIO_NR_RU
(

 SCENARIO_ID
,ENB_ID     
,PCI        
,PCI_PORT   
,RU_ID      
,MAKER      
,SECTOR_ORD 
,REPEATERATTENUATION
,REPEATERPWRRATIO
,RU_SEQ 
,RADIUS                
,FEEDER_LOSS           
,NOISEFLOOR            
,CORRECTION_VALUE      
,FADE_MARGIN           
,XPOSITION       
,YPOSITION             
,HEIGHT             
,SITE_ADDR             
,TYPE                  
,STATUS                
,SISUL_CD              
,MSC                   
,BSC                   
,NETWORKID             
,USABLETRAFFICCH       
,SYSTEMID              
,RU_TYPE
,FA_MODEL_ID  
,NETWORK_TYPE 
,RESOLUTION   
,FA_SEQ
,SITE_STARTX
,SITE_STARTY
,SITE_ENDX
,SITE_ENDY
,X_BIN_CNT
,Y_BIN_CNT

                                                                                                                                                       
-----------------------------------------                                                                                                              
-- I_MOBILE_PARAMETER                                                                                                                                              
-----------------------------------------                                                                                                              
-- DROP TABLE PUBLIC.I_MOBILE_PARAMETER;                                                                                                                 
CREATE TABLE PUBLIC.I_MOBILE_PARAMETER                                                                                                                   
(
 SCENARIO_ID   NUMERIC NOT NULL
,MOBILE_ID     NUMERIC NOT NULL
,TYPE          NUMERIC NOT NULL
,MOBILENAME    VARCHAR(30) NOT NULL
,MAKER         VARCHAR(30)  
,MINPOWER      NUMERIC  
,MAXPOWER      NUMERIC  
,MOBILEGAIN    NUMERIC  
,NOISEFLOOR    NUMERIC  
,HEIGHT        NUMERIC  
,BODYLOSS      NUMERIC  
,BUILDINGLOSS  NUMERIC  
,CARLOSS       NUMERIC  
,FEEDERLOSS    NUMERIC  
,NOISEFIGURE   NUMERIC  
,DIVERSITYGAIN NUMERIC  
,ANTENNAGAIN   NUMERIC  
,RX_LAYER      NUMERIC  
)                                                                                                                                                       

CREATE INDEX I_MOBILE_PARAMETER_IDX ON PUBLIC.I_MOBILE_PARAMETER USING BTREE (SCENARIO_ID,MOBILE_ID,TYPE);                                            
ALTER TABLE PUBLIC.I_MOBILE_PARAMETER OWNER TO POSTGRES;                                                                                                             
GRANT ALL ON TABLE PUBLIC.I_MOBILE_PARAMETER TO POSTGRES;                                                                                                            


-----------------------------------------                                                                                                              
-- I_SCENARIO_NR_ANTENNA                                                                                                                                              
-----------------------------------------                                                                                                              
-- DROP TABLE PUBLIC.I_MOBILE_PARAMETER;                                                                                                                 
CREATE TABLE PUBLIC.I_SCENARIO_NR_ANTENNA                                                                                                                   
(
 SCENARIO_ID   NUMERIC NOT NULL
,MOBILE_ID     NUMERIC NOT NULL
,TYPE          NUMERIC NOT NULL
,MOBILENAME    VARCHAR(30) NOT NULL
,MAKER         VARCHAR(30)  
,MINPOWER      NUMERIC  
,MAXPOWER      NUMERIC  
,MOBILEGAIN    NUMERIC  
,NOISEFLOOR    NUMERIC  
,HEIGHT        NUMERIC  
,BODYLOSS      NUMERIC  
,BUILDINGLOSS  NUMERIC  
,CARLOSS       NUMERIC  
,FEEDERLOSS    NUMERIC  
,NOISEFIGURE   NUMERIC  
,DIVERSITYGAIN NUMERIC  
,ANTENNAGAIN   NUMERIC  
,RX_LAYER      NUMERIC  
)                                                                                                                                                       

CREATE INDEX I_SCENARIO_NR_ANTENNA_IDX ON PUBLIC.I_SCENARIO_NR_ANTENNA USING BTREE (SCENARIO_ID,MOBILE_ID,TYPE);                                            
ALTER TABLE PUBLIC.I_SCENARIO_NR_ANTENNA OWNER TO POSTGRES;                                                                                                             
GRANT ALL ON TABLE PUBLIC.I_SCENARIO_NR_ANTENNA TO POSTGRES;                                                                                                            


