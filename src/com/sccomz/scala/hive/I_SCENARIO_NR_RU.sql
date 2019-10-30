-----------------------------------------                                                                                                              
-- I_SCENARIO_NR_RU                                                                                                                                              
-----------------------------------------                                                                                                              
DROP TABLE PUBLIC.I_SCENARIO_NR_RU;                                                                                                                 
                                                                                                                                                       
CREATE TABLE PUBLIC.I_SCENARIO_NR_RU (                                                                                                                                                                                                                                                                                                             
  SCENARIO_ID                   INT                                                                                                                        
, ENB_ID                        STRING                                                                                                                        
, PCI                           INT                                                                                                                       
, PCI_PORT                      INT                                                                                                                      
, RU_ID                         STRING                                                                                                                      
, MAKER                         STRING                                                                                                                        
, SECTOR_ORD                    INT                                                                                                                      
, REPEATERATTENUATION           INT                                                                                                                        
, REPEATERPWRRATIO              INT                                                                                                                        
, RU_SEQ                        INT                                                                                                                        
, RADIUS                        INT                                                                                                                        
, FEEDER_LOSS                   INT                                                                                                                         
, NOISEFLOOR                    INT                                                                                                                        
, CORRECTION_VALUE              INT                                                                                                                        
, FADE_MARGIN                   INT                                                                                                                        
, XPOSITION                     STRING                                                                                                                        
, YPOSITION                     STRING                                                                                                                        
, HEIGHT                        INT                                                                                                                        
, SITE_ADDR                     STRING                                                                                                                        
, TYPE                          STRING                                                                                                                        
, STATUS                        STRING                                                                                                                        
, SISUL_CD                      STRING                                                                                                                        
, MSC                           INT                                                                                                                        
, BSC                           INT                                                                                                                        
, NETWORKID                     INT                                                                                                                        
, USABLETRAFFICCH               INT                                                                                                                        
, SYSTEMID                      INT                                                                                                                        
, RU_TYPE                       INT                                                                                                                        
, FA_MODEL_ID                   INT                                                                                                                        
, NETWORK_TYPE                  INT                                                                                                                        
, RESOLUTION                    INT                                                                                                                        
, FA_SEQ                        INT                                                                                                                        
, SITE_STARTX                   FLOAT                                                                                                                        
, SITE_STARTY                   FLOAT                                                                                                                        
, SITE_ENDX                     FLOAT                                                                                                                        
, SITE_ENDY                     FLOAT                                                                                                                        
, X_BIN_CNT                     INT                                                                                                                        
, Y_BIN_CNT                     INT                                                                                                                        
)