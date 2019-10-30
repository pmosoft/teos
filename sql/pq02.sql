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
