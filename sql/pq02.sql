DROP TABLE PUBLIC.I_RU;

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
