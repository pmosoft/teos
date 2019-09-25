--DROP TABLE CELLPLAN.ANALYSIS_LIST;
CREATE TABLE CELLPLAN.ANALYSIS_LIST
(
 SCENARIO_ID           NUMBER NOT NULL
,ANALYSIS_TYPE         NUMBER  
,PATHLOSS              NUMBER  
,BESTSERVER            NUMBER  
,RSRP                  NUMBER  
,RSRQ                  NUMBER  
,RSSI                  NUMBER  
,SINR                  NUMBER  
,HANDOVER              NUMBER  
,THROUGHPUT            NUMBER  
,DETAIL_FUN_NM         VARCHAR2(20)  
,PILOT_IO              NUMBER  
,COVER_RSRP            NUMBER  
,COVER_RSSI            NUMBER  
,COVER_SINR            NUMBER  
,COVER_RSSI_AND_SINR   NUMBER  
,COVER_RSRP_VAL        NUMBER  
,COVER_RSSI_VAL        NUMBER  
,COVER_SINR_VAL        NUMBER  
,ERP                   NUMBER  
,TRAFFIC               NUMBER  
,LOS                   NUMBER  
,LOS_BESTSERVER        NUMBER  
,UL_MOBILE_ERP         NUMBER  
,UL_RSSI               NUMBER  
,UL_SINR               NUMBER  
,MULTI_BAND_THROUGHPUT NUMBER  
,MULTI_BAND_COVERAGE   NUMBER  
,CONSTRAINT ANALYSIS_LIST_PK PRIMARY KEY(SCENARIO_ID)
);
--DROP TABLE CELLPLAN.ANALYSIS_RESULT;
CREATE TABLE CELLPLAN.ANALYSIS_RESULT
(
 SCENARIO_ID           NUMBER NOT NULL
,ANALYSIS_TYPE         VARCHAR2(10) NOT NULL
,SERVER_ID             VARCHAR2(10)  
,RESULT_TIME           NUMBER  
,CPU_USE               NUMBER  
,MEMORY_USE            NUMBER  
,START_TIME            DATE  
,END_TIME              DATE  
,CONSTRAINT ANALYSIS_RESULT_PK PRIMARY KEY(SCENARIO_ID,ANALYSIS_TYPE)
);
--DROP TABLE CELLPLAN.COLOR;
CREATE TABLE CELLPLAN.COLOR
(
 SCENARIO_ID           NUMBER NOT NULL
,COLOR_SET_ID          NUMBER NOT NULL
,ANALYSIS_ITEM_TYPE    NUMBER NOT NULL
,DRAWINGSTYLE          NUMBER  
,CONSTRAINT COLOR_PK PRIMARY KEY(SCENARIO_ID,COLOR_SET_ID,ANALYSIS_ITEM_TYPE)
);
--DROP TABLE CELLPLAN.COLOR_USER;
CREATE TABLE CELLPLAN.COLOR_USER
(
 SCENARIO_ID           NUMBER NOT NULL
,USER_ID               VARCHAR2(13) NOT NULL
,COLOR_SET_ID          NUMBER NOT NULL
,COLOR_SET_ORD         NUMBER NOT NULL
,ANALYSIS_TYPE         VARCHAR2(10)  
,USE_YN                CHAR(1)  
,COLOR_CNT             NUMBER  
,LIMIT1                NUMBER  
,COLOR1                NUMBER  
,LIMIT2                NUMBER  
,COLOR2                NUMBER  
,LIMIT3                NUMBER  
,COLOR3                NUMBER  
,LIMIT4                NUMBER  
,COLOR4                NUMBER  
,LIMIT5                NUMBER  
,COLOR5                NUMBER  
,LIMIT6                NUMBER  
,COLOR6                NUMBER  
,LIMIT7                NUMBER  
,COLOR7                NUMBER  
,LIMIT8                NUMBER  
,COLOR8                NUMBER  
,LIMIT9                NUMBER  
,COLOR9                NUMBER  
,LIMIT10               NUMBER  
,COLOR10               NUMBER  
,LIMIT11               NUMBER  
,COLOR11               NUMBER  
,LIMIT12               NUMBER  
,COLOR12               NUMBER  
,LIMIT13               NUMBER  
,COLOR13               NUMBER  
,LIMIT14               NUMBER  
,COLOR14               NUMBER  
,LIMIT15               NUMBER  
,COLOR15               NUMBER  
,LIMIT16               NUMBER  
,COLOR16               NUMBER  
,RATE1                 NUMBER  
,RATE2                 NUMBER  
,RATE3                 NUMBER  
,RATE4                 NUMBER  
,RATE5                 NUMBER  
,RATE6                 NUMBER  
,RATE7                 NUMBER  
,RATE8                 NUMBER  
,RATE9                 NUMBER  
,RATE10                NUMBER  
,RATE11                NUMBER  
,RATE12                NUMBER  
,RATE13                NUMBER  
,RATE14                NUMBER  
,RATE15                NUMBER  
,RATE16                NUMBER  
,LEGEND_NAME           VARCHAR2(100)  
,CONSTRAINT COLOR_USER_PK PRIMARY KEY(SCENARIO_ID,USER_ID,COLOR_SET_ID,COLOR_SET_ORD)
);
--DROP TABLE CELLPLAN.DU;
CREATE TABLE CELLPLAN.DU
(
 SCENARIO_ID           NUMBER NOT NULL
,ENB_ID                VARCHAR2(10) NOT NULL
,E_NODEB_NM            VARCHAR2(100)  
,PCI_CNT               NUMBER  
,STRMAKER              VARCHAR2(30)  
,REG_DT                DATE  
,CONSTRAINT DU_PK PRIMARY KEY(SCENARIO_ID,ENB_ID)
);
--DROP TABLE CELLPLAN.GEO_GEOMETRYQUERY;
CREATE TABLE CELLPLAN.GEO_GEOMETRYQUERY
(
 SCHEDULE_ID           NUMBER NOT NULL
,ANALYSIS_TYPE         VARCHAR2(10) NOT NULL
,SCENARIO_ID           NUMBER  
,AREA_ID               NUMBER  
,INPUT                 CLOB  
,RESULT                VARCHAR2(4000)  
,RESULT_TYPE           NUMBER NOT NULL
,STATUS                VARCHAR2(1)  
,SOLUTION_AREA_ID      NUMBER  
,CONSTRAINT GEO_GEOMETRYQUERY_PK PRIMARY KEY(SCHEDULE_ID,ANALYSIS_TYPE,RESULT_TYPE)
);
--DROP TABLE CELLPLAN.LTESECTORPARAMETER;
CREATE TABLE CELLPLAN.LTESECTORPARAMETER
(
 SCENARIO_ID           NUMBER NOT NULL
,ENB_ID                VARCHAR2(10) NOT NULL
,PCI                   NUMBER NOT NULL
,PCI_PORT              NUMBER NOT NULL
,RU_ID                 VARCHAR2(48) NOT NULL
,MAXUSER               NUMBER  
,TOTALPWRWATT          NUMBER  
,TOTALPWRDBM           NUMBER  
,RSPWRWATT             NUMBER  
,RSPWRDBM              NUMBER  
,PBCHPWRWATT           NUMBER  
,PBCHPWRDBM            NUMBER  
,PDSCHPWRWATT          NUMBER  
,PDSCHPWRDBM           NUMBER  
,DLICILOAD             NUMBER  
,ANTENNACOUNT          NUMBER  
,DLMIMOTYPE            NUMBER  
,DLMIMOGAIN            NUMBER  
,ULMIMOTYPE            NUMBER  
,ULMIMOGAIN            NUMBER  
,BEAMFORMING           NUMBER  
,SEIZEDTRAFFICERLANG1  NUMBER  
,SEIZEDTRAFFICERLANG2  NUMBER  
,REG_DT                DATE  
,CONSTRAINT LTESECTORPARAMETER_PK PRIMARY KEY(SCENARIO_ID,ENB_ID,PCI,PCI_PORT,RU_ID)
);
--DROP TABLE CELLPLAN.LTESYSTEM;
CREATE TABLE CELLPLAN.LTESYSTEM
(
 SCENARIO_ID            NUMBER NOT NULL
,DUPLEXTYPE             NUMBER  
,FREQUENCYBAND          NUMBER  
,TOTALBANDWIDTH         NUMBER  
,FRAMECONFIGURATION     NUMBER  
,SUBCARRIERSPACING      NUMBER  
,CYCLICPREFIX           NUMBER  
,NUMBEROFSUBCARRIERS    NUMBER  
,NUMBEROFRBS            NUMBER  
,NUMBEROFOFDMSYMBOLS    NUMBER  
,NUMBEROFRSSYMBOLS      NUMBER  
,NUMBEROFPDCCHSYMBOLS   NUMBER  
,NUMBEROFPUCCHRBS       NUMBER  
,DLDIVSELCINRTHRESHOLD  NUMBER  
,DLBEAMFORMINGGAIN      NUMBER  
,ULDIVSELCINRTHRESHOLD  NUMBER  
,ULBEAMFORMINGGAIN      NUMBER  
,TURNAROUNDFACTOR       NUMBER  
,ORTHOGONALFACTOR       NUMBER  
,HOSTARTCINR            NUMBER  
,HOENDCINR              NUMBER  
,HOMARGIN               NUMBER  
,ULPWRCTRLTGTCINR       NUMBER  
,ULPWRCTRLSTEP          NUMBER  
,DLCOVLIMITRSSI         NUMBER  
,DLCOVERAGELIMITCINR    NUMBER  
,ULCOVERAGELIMITCINR    NUMBER  
,MAXUSERPERSECTOR       NUMBER  
,SELECTCOVERAGEANALYSIS NUMBER  
,DLINTERCELL            NUMBER  
,DLINTRACELL            NUMBER  
,ULINTERCELL            NUMBER  
,ULINTRACELL            NUMBER  
,TECHTYPE               NUMBER  
,DLCOVLIMITRSRP         NUMBER  
,DLCOVLIMITRSRQ         NUMBER  
,DLCOVLIMITRSRP_YN      VARCHAR2(1)  
,SPREADFACTOR           NUMBER(2,0)  
,CONSTRAINT LTESYSTEM_PK PRIMARY KEY(SCENARIO_ID)
);
--DROP TABLE CELLPLAN.LTETRAFFIC;
CREATE TABLE CELLPLAN.LTETRAFFIC
(
 SCENARIO_ID            NUMBER NOT NULL
,DLCODINGRATE1          NUMBER  
,DLCODINGRATE2          NUMBER  
,DLCODINGRATE3          NUMBER  
,DLCODINGRATE4          NUMBER  
,DLCODINGRATE5          NUMBER  
,DLCODINGRATE6          NUMBER  
,DLCODINGRATE7          NUMBER  
,DLCODINGRATE8          NUMBER  
,DLCODINGRATE9          NUMBER  
,DLCODINGRATE10         NUMBER  
,DLCODINGRATE11         NUMBER  
,DLCODINGRATE12         NUMBER  
,DLCODINGRATE13         NUMBER  
,DLCODINGRATE14         NUMBER  
,DLCODINGRATE15         NUMBER  
,DLSPECTRALEFFICIENCY1  NUMBER  
,DLSPECTRALEFFICIENCY2  NUMBER  
,DLSPECTRALEFFICIENCY3  NUMBER  
,DLSPECTRALEFFICIENCY4  NUMBER  
,DLSPECTRALEFFICIENCY5  NUMBER  
,DLSPECTRALEFFICIENCY6  NUMBER  
,DLSPECTRALEFFICIENCY7  NUMBER  
,DLSPECTRALEFFICIENCY8  NUMBER  
,DLSPECTRALEFFICIENCY9  NUMBER  
,DLSPECTRALEFFICIENCY10 NUMBER  
,DLSPECTRALEFFICIENCY11 NUMBER  
,DLSPECTRALEFFICIENCY12 NUMBER  
,DLSPECTRALEFFICIENCY13 NUMBER  
,DLSPECTRALEFFICIENCY14 NUMBER  
,DLSPECTRALEFFICIENCY15 NUMBER  
,DLREQUIREDCINR1        NUMBER  
,DLREQUIREDCINR2        NUMBER  
,DLREQUIREDCINR3        NUMBER  
,DLREQUIREDCINR4        NUMBER  
,DLREQUIREDCINR5        NUMBER  
,DLREQUIREDCINR6        NUMBER  
,DLREQUIREDCINR7        NUMBER  
,DLREQUIREDCINR8        NUMBER  
,DLREQUIREDCINR9        NUMBER  
,DLREQUIREDCINR10       NUMBER  
,DLREQUIREDCINR11       NUMBER  
,DLREQUIREDCINR12       NUMBER  
,DLREQUIREDCINR13       NUMBER  
,DLREQUIREDCINR14       NUMBER  
,DLREQUIREDCINR15       NUMBER  
,DLMCSSUPPORT1          NUMBER  
,DLMCSSUPPORT2          NUMBER  
,DLMCSSUPPORT3          NUMBER  
,DLMCSSUPPORT4          NUMBER  
,DLMCSSUPPORT5          NUMBER  
,DLMCSSUPPORT6          NUMBER  
,DLMCSSUPPORT7          NUMBER  
,DLMCSSUPPORT8          NUMBER  
,DLMCSSUPPORT9          NUMBER  
,DLMCSSUPPORT10         NUMBER  
,DLMCSSUPPORT11         NUMBER  
,DLMCSSUPPORT12         NUMBER  
,DLMCSSUPPORT13         NUMBER  
,DLMCSSUPPORT14         NUMBER  
,DLMCSSUPPORT15         NUMBER  
,ULCODINGRATE1          NUMBER  
,ULCODINGRATE2          NUMBER  
,ULCODINGRATE3          NUMBER  
,ULCODINGRATE4          NUMBER  
,ULCODINGRATE5          NUMBER  
,ULCODINGRATE6          NUMBER  
,ULCODINGRATE7          NUMBER  
,ULCODINGRATE8          NUMBER  
,ULCODINGRATE9          NUMBER  
,ULCODINGRATE10         NUMBER  
,ULCODINGRATE11         NUMBER  
,ULCODINGRATE12         NUMBER  
,ULCODINGRATE13         NUMBER  
,ULCODINGRATE14         NUMBER  
,ULCODINGRATE15         NUMBER  
,ULSPECTRALEFFICIENCY1  NUMBER  
,ULSPECTRALEFFICIENCY2  NUMBER  
,ULSPECTRALEFFICIENCY3  NUMBER  
,ULSPECTRALEFFICIENCY4  NUMBER  
,ULSPECTRALEFFICIENCY5  NUMBER  
,ULSPECTRALEFFICIENCY6  NUMBER  
,ULSPECTRALEFFICIENCY7  NUMBER  
,ULSPECTRALEFFICIENCY8  NUMBER  
,ULSPECTRALEFFICIENCY9  NUMBER  
,ULSPECTRALEFFICIENCY10 NUMBER  
,ULSPECTRALEFFICIENCY11 NUMBER  
,ULSPECTRALEFFICIENCY12 NUMBER  
,ULSPECTRALEFFICIENCY13 NUMBER  
,ULSPECTRALEFFICIENCY14 NUMBER  
,ULSPECTRALEFFICIENCY15 NUMBER  
,ULREQUIREDCINR1        NUMBER  
,ULREQUIREDCINR2        NUMBER  
,ULREQUIREDCINR3        NUMBER  
,ULREQUIREDCINR4        NUMBER  
,ULREQUIREDCINR5        NUMBER  
,ULREQUIREDCINR6        NUMBER  
,ULREQUIREDCINR7        NUMBER  
,ULREQUIREDCINR8        NUMBER  
,ULREQUIREDCINR9        NUMBER  
,ULREQUIREDCINR10       NUMBER  
,ULREQUIREDCINR11       NUMBER  
,ULREQUIREDCINR12       NUMBER  
,ULREQUIREDCINR13       NUMBER  
,ULREQUIREDCINR14       NUMBER  
,ULREQUIREDCINR15       NUMBER  
,ULMCSSUPPORT1          NUMBER  
,ULMCSSUPPORT2          NUMBER  
,ULMCSSUPPORT3          NUMBER  
,ULMCSSUPPORT4          NUMBER  
,ULMCSSUPPORT5          NUMBER  
,ULMCSSUPPORT6          NUMBER  
,ULMCSSUPPORT7          NUMBER  
,ULMCSSUPPORT8          NUMBER  
,ULMCSSUPPORT9          NUMBER  
,ULMCSSUPPORT10         NUMBER  
,ULMCSSUPPORT11         NUMBER  
,ULMCSSUPPORT12         NUMBER  
,ULMCSSUPPORT13         NUMBER  
,ULMCSSUPPORT14         NUMBER  
,ULMCSSUPPORT15         NUMBER  
,CONSTRAINT LTETRAFFIC_PK PRIMARY KEY(SCENARIO_ID)
);
--DROP TABLE CELLPLAN.LTE_IO_ANALYSIS;
CREATE TABLE CELLPLAN.LTE_IO_ANALYSIS
(
 SCENARIO_ID            NUMBER NOT NULL
,MAX_SERVER             NUMBER  
,SERVER_ORD             NUMBER  
,SERVER_ID              NUMBER  
,BIN_COUNT              NUMBER  
,RSSI_RU_DBM            NUMBER  
,RSSI_RU_WATT           NUMBER  
);
--DROP TABLE CELLPLAN.MOBILE_PARAMETER;
CREATE TABLE CELLPLAN.MOBILE_PARAMETER
(
 SCENARIO_ID            NUMBER NOT NULL
,MOBILE_ID              NUMBER NOT NULL
,TYPE                   NUMBER NOT NULL
,MOBILENAME             VARCHAR2(30) NOT NULL
,MAKER                  VARCHAR2(30)  
,MINPOWER               NUMBER  
,MAXPOWER               NUMBER  
,MOBILEGAIN             NUMBER  
,NOISEFLOOR             NUMBER  
,HEIGHT                 NUMBER  
,BODYLOSS               NUMBER  
,BUILDINGLOSS           NUMBER  
,CARLOSS                NUMBER  
,FEEDERLOSS             NUMBER  
,NOISEFIGURE            NUMBER  
,DIVERSITYGAIN          NUMBER  
,ANTENNAGAIN            NUMBER  
,RX_LAYER               NUMBER  
,CONSTRAINT MOBILE_PARAMETER_PK PRIMARY KEY(SCENARIO_ID,MOBILE_ID,TYPE)
);
--DROP TABLE CELLPLAN.NRSECTORPARAMETER;
CREATE TABLE CELLPLAN.NRSECTORPARAMETER
(
 SCENARIO_ID               NUMBER NOT NULL
,ENB_ID                    NUMBER NOT NULL
,PCI                       NUMBER NOT NULL
,PCI_PORT                  NUMBER NOT NULL
,RU_ID                     VARCHAR2(40) NOT NULL
,TXPWRWATT                 NUMBER  
,TXPWRDBM                  NUMBER  
,TXEIRPWATT                NUMBER  
,TXEIRPDBM                 NUMBER  
,RETXEIRPWATT              NUMBER  
,RETXEIRPDBM               NUMBER  
,TRXCOUNT                  NUMBER  
,DLMODULATION              NUMBER  
,DLMIMOTYPE                NUMBER  
,DLMIMOGAIN                NUMBER  
,ULMODULATION              NUMBER  
,ULMIMOTYPE                NUMBER  
,ULMIMOGAIN                NUMBER  
,LOSBEAMFORMINGLOSS        NUMBER  
,NLOSBEAMFORMINGLOSS       NUMBER  
,HANDOVERCALLDROPTHRESHOLD NUMBER  
,POWERCOMBININGGAIN        NUMBER  
,BEAMMISMATCHMARGIN        NUMBER  
,ANTENNAGAIN               NUMBER  
,FOLIAGELOSS               NUMBER  
,TXLAYER                   NUMBER  
,RXLAYER                   NUMBER  
);
--DROP TABLE CELLPLAN.NRSYSTEM;
CREATE TABLE CELLPLAN.NRSYSTEM
(
 SCENARIO_ID                   NUMBER NOT NULL
,SYSTEM_ID                     NUMBER  
,FA_SEQ                        NUMBER  
,BANDWIDTH_PER_CC              NUMBER  
,NUMBER_OF_CC                  NUMBER  
,NUMBER_OF_SC_PER_RB           NUMBER  
,TOTALBANDWIDTH                NUMBER  
,SUBCARRIERSPACING             NUMBER  
,RB_PER_CC                     NUMBER  
,RADIOFRAMELENGTH              NUMBER  
,SUBFRAMELENGTH                NUMBER  
,NO_SLOTPERRADIOFRAME          NUMBER  
,SLOTLENGTH                    NUMBER  
,NO_OFDMSYMBOLPERSUBFRAME      NUMBER  
,FRAMECONFIGURATION            NUMBER  
,DLDATARATIO                   NUMBER  
,ULDATARATIO                   NUMBER  
,DLBLER                        NUMBER  
,ULBLER                        NUMBER  
,DIVERSITYGAINRATIO            NUMBER  
,DLINTRACELL                   NUMBER  
,DLINTERCELL                   NUMBER  
,ULINTRACELL                   NUMBER  
,ULINTERCELL                   NUMBER  
,DLCOVERAGELIMITRSRP           NUMBER  
,INTERFERENCEMARGIN            NUMBER  
,NRGAINOVERLTE                 NUMBER  
,PENETRATIONLOSS               NUMBER  
,SLOT_CONFIGURATION            NUMBER  
,DLCOVERAGELIMITRSRPLOS        NUMBER  
,DLOH                          NUMBER  
,ULOH                          NUMBER  
,DLSINROFFSET                  NUMBER  
,TECHTYPE                      NUMBER  
,RAYTRACING_REFLECTION         NUMBER  
,RAYTRACING_DIFFRACTION        NUMBER  
,RAYTRACING_SCATTERING         NUMBER  
,RELATED_ANALYSIS_COVLIMITRSRP NUMBER  
,ENV_ROADSIDE_TREE_YN          VARCHAR2(1)  
,DLCOVLIMITRSRP_YN             VARCHAR2(1)  
,ANT_CATEGORY                  VARCHAR2(20)  
,ANT_NM                        VARCHAR2(150)  
,CONSTRAINT NRSYSTEM_PK PRIMARY KEY(SCENARIO_ID)
);
--DROP TABLE CELLPLAN.NRUETRAFFIC;
CREATE TABLE CELLPLAN.NRUETRAFFIC
(
 SCENARIO_ID                   NUMBER  
,SYSTEM_ID                     NUMBER  
,FA_SEQ                        NUMBER  
,DLUL_TYPE                     NUMBER  
,RX_MODULATION                 NUMBER  
,RX_LAYER                      NUMBER  
,MODULATION1                   NUMBER  
,MODULATION2                   NUMBER  
,MODULATION3                   NUMBER  
,MODULATION4                   NUMBER  
,MODULATION5                   NUMBER  
,MODULATION6                   NUMBER  
,MODULATION7                   NUMBER  
,MODULATION8                   NUMBER  
,MODULATION9                   NUMBER  
,MODULATION10                  NUMBER  
,MODULATION11                  NUMBER  
,MODULATION12                  NUMBER  
,MODULATION13                  NUMBER  
,MODULATION14                  NUMBER  
,MODULATION15                  NUMBER  
,LAYER1                        NUMBER  
,LAYER2                        NUMBER  
,LAYER3                        NUMBER  
,LAYER4                        NUMBER  
,LAYER5                        NUMBER  
,LAYER6                        NUMBER  
,LAYER7                        NUMBER  
,LAYER8                        NUMBER  
,LAYER9                        NUMBER  
,LAYER10                       NUMBER  
,LAYER11                       NUMBER  
,LAYER12                       NUMBER  
,LAYER13                       NUMBER  
,LAYER14                       NUMBER  
,LAYER15                       NUMBER  
,CODERATE1                     NUMBER  
,CODERATE2                     NUMBER  
,CODERATE3                     NUMBER  
,CODERATE4                     NUMBER  
,CODERATE5                     NUMBER  
,CODERATE6                     NUMBER  
,CODERATE7                     NUMBER  
,CODERATE8                     NUMBER  
,CODERATE9                     NUMBER  
,CODERATE10                    NUMBER  
,CODERATE11                    NUMBER  
,CODERATE12                    NUMBER  
,CODERATE13                    NUMBER  
,CODERATE14                    NUMBER  
,CODERATE15                    NUMBER  
,SNR1                          NUMBER  
,SNR2                          NUMBER  
,SNR3                          NUMBER  
,SNR4                          NUMBER  
,SNR5                          NUMBER  
,SNR6                          NUMBER  
,SNR7                          NUMBER  
,SNR8                          NUMBER  
,SNR9                          NUMBER  
,SNR10                         NUMBER  
,SNR11                         NUMBER  
,SNR12                         NUMBER  
,SNR13                         NUMBER  
,SNR14                         NUMBER  
,SNR15                         NUMBER  
);
--DROP TABLE CELLPLAN.NR_CANDIDATE_PARAMETER;
CREATE TABLE CELLPLAN.NR_CANDIDATE_PARAMETER
(
 SCENARIO_ID                   NUMBER NOT NULL
,TARGET_LOS_RATE               NUMBER  
,LIMIT_RU_COUNT_BY_LOS_RATE    NUMBER  
,LIMIT_RU_COUNT_BY_LTE_RU      NUMBER  
,REG_DT                        DATE  
,CONSTRAINT NR_CANDIDATE_PARAMETER_PK PRIMARY KEY(SCENARIO_ID)
);
--DROP TABLE CELLPLAN.NR_CANDIDATE_SITE_INFO;
CREATE TABLE CELLPLAN.NR_CANDIDATE_SITE_INFO
(
 SCENARIO_ID                   NUMBER  
,SRC_TYPE                      VARCHAR2(10)  
,POLE_ID                       NUMBER  
,BD_SEQ                        NUMBER  
,CANDIDATE_RANK                NUMBER  
,POLE_XPOS                     NUMBER  
,POLE_YPOS                     NUMBER  
,BD_XPOS_CENTER                NUMBER  
,BD_YPOS_CENTER                NUMBER  
,DISTANCE                      NUMBER  
,BD_HEIGHT                     NUMBER  
,BD_FLOOR_MAX                  NUMBER  
,BD_TYPE                       VARCHAR2(50)  
,RU_ID                         VARCHAR2(50)  
,ENB_ID                        NUMBER  
,PCI                           NUMBER  
,RU_SEQ                        NUMBER  
,SECTOR                        NUMBER  
,PCI_PORT                      NUMBER  
,SITE_NAME                     VARCHAR2(250)  
,SISUL_CD                      VARCHAR2(50)  
,LOS_C                         NUMBER  
,LOS_RATE                      NUMBER  
,LOS_INC_RATE                  NUMBER  
,LOS_DUP_RATE                  NUMBER  
,REG_DT                        DATE  
,UPDATE_DT                     DATE  
);
--DROP TABLE CELLPLAN.RU;
CREATE TABLE CELLPLAN.RU
(
 SCENARIO_ID                   NUMBER NOT NULL
,ENB_ID                        VARCHAR2(10) NOT NULL
,PCI                           NUMBER NOT NULL
,PCI_PORT                      NUMBER NOT NULL
,RU_ID                         VARCHAR2(48) NOT NULL
,MAKER                         VARCHAR2(30)  
,SITE_TYPE                     CHAR(18)  
,PAIR_ENODEB                   NUMBER  
,REPEATERATTENUATION           NUMBER  
,REPEATERPWRRATIO              NUMBER  
,RU_NM                         VARCHAR2(100)  
,FA_SEQ                        NUMBER NOT NULL
,SECTOR_ORD                    NUMBER NOT NULL
,RU_SEQ                        NUMBER  
,RRH_SEQ                       NUMBER  
,REG_DT                        DATE  
,SWING_YN                      NUMBER  
,ANT_CHK_YN                    NUMBER  
,TILT_YN                       NUMBER  
,FA_SEQ_ORG                    NUMBER  
,CONSTRAINT RU_PK PRIMARY KEY(SCENARIO_ID,ENB_ID,PCI,PCI_PORT,RU_ID,FA_SEQ,SECTOR_ORD)
);
--DROP TABLE CELLPLAN.RU_ANTENA;
CREATE TABLE CELLPLAN.RU_ANTENA
(
 SCENARIO_ID                   NUMBER NOT NULL
,ENB_ID                        VARCHAR2(10) NOT NULL
,PCI                           NUMBER NOT NULL
,PCI_PORT                      NUMBER NOT NULL
,RU_NM                         VARCHAR2(100) NOT NULL
,ANTENA_NM                     VARCHAR2(100) NOT NULL
,ORIENTATION                   NUMBER  
,TILTING                       NUMBER  
,ANTENA_ORD                    NUMBER NOT NULL
,RU_ID                         VARCHAR2(48) NOT NULL
,ANTENA_SEQ                    NUMBER  
,REG_DT                        DATE  
,ANTENA_STANDARD_NM            VARCHAR2(100)  
,ET_TILTING                    NUMBER  
,USER_TILTING                  NUMBER  
,LIMIT_TILTING                 NUMBER  
,RET_ID                        VARCHAR2(50)  
,CONSTRAINT RU_ANTENA_PK PRIMARY KEY(SCENARIO_ID,ENB_ID,PCI,PCI_PORT,ANTENA_NM,ANTENA_ORD,RU_ID)
);
--DROP TABLE CELLPLAN.SCENARIO;
CREATE TABLE CELLPLAN.SCENARIO
(
 SCENARIO_ID                   NUMBER NOT NULL
,SCENARIO_NM                   VARCHAR2(200)  
,USER_ID                       VARCHAR2(13)  
,SYSTEM_ID                     NUMBER  
,NETWORK_TYPE                  NUMBER  
,SIDO_CD                       VARCHAR2(10)  
,SIGUGUN_CD                    VARCHAR2(10)  
,DONG_CD                       VARCHAR2(10)  
,SIDO                          VARCHAR2(30)  
,SIGUGUN                       VARCHAR2(30)  
,DONG                          VARCHAR2(30)  
,STARTX                        NUMBER  
,STARTY                        NUMBER  
,ENDX                          NUMBER  
,ENDY                          NUMBER  
,FA_MODEL_ID                   NUMBER  
,FA_SEQ                        NUMBER  
,SCENARIO_DESC                 VARCHAR2(500)  
,USE_BUILDING                  NUMBER  
,USE_MULTIFA                   NUMBER  
,PRECISION                     NUMBER  
,PWRCTRLCHECKPOINT             NUMBER  
,MAXITERATIONPWRCTRL           NUMBER  
,RESOLUTION                    NUMBER  
,MODEL_RADIUS                  NUMBER  
,REG_DT                        DATE  
,MODIFY_DT                     DATE  
,UPPER_SCENARIO_ID             NUMBER  
,FLOORBUILDING                 NUMBER  
,FLOOR                         NUMBER  
,FLOORLOSS                     NUMBER  
,SCENARIO_SUB_ID               NUMBER  
,SCENARIO_SOLUTION_NUM         NUMBER  
,LOSS_TYPE                     NUMBER  
,RU_CNT                        NUMBER  
,MODIFY_YN                     CHAR(1)  
,BATCH_YN                      CHAR(1)  
,TM_STARTX                     NUMBER  
,TM_STARTY                     NUMBER  
,TM_ENDX                       NUMBER  
,TM_ENDY                       NUMBER  
,REAL_DATE                     VARCHAR2(10)  
,REAL_DRM_FILE                 VARCHAR2(100)  
,REAL_COMP                     VARCHAR2(16)  
,REAL_CATT                     VARCHAR2(10)  
,REAL_CATY                     VARCHAR2(1)  
,BLD_TYPE                      VARCHAR2(20)  
,RET_PERIOD                    NUMBER  
,RET_END_DATETIME              VARCHAR2(10)  
,BUILDINGANALYSIS3D_YN         VARCHAR2(1)  
,BUILDINGANALYSIS3D_RESOLUTION NUMBER  
,AREA_ID                       NUMBER  
,BUILDINGANALYSIS3D_RELATED_YN VARCHAR2(1)  
,RELATED_ANALYSIS_COVLIMITRSRP NUMBER  
,CONSTRAINT SCENARIO_PK PRIMARY KEY(SCENARIO_ID)
);
--DROP TABLE CELLPLAN.SCHEDULE;
CREATE TABLE CELLPLAN.SCHEDULE
(
 SCHEDULE_ID                   NUMBER NOT NULL
,TYPE_CD                       VARCHAR2(10)  
,SCENARIO_ID                   NUMBER NOT NULL
,USER_ID                       VARCHAR2(13) NOT NULL
,PRIORITIZE                    VARCHAR2(20)  
,PROCESS_CD                    VARCHAR2(20)  
,PROCESS_MSG                   VARCHAR2(1500)  
,SCENARIO_PATH                 VARCHAR2(256)  
,REG_DT                        DATE  
,MODIFY_DT                     DATE  
,RETRY_CNT                     NUMBER  
,SERVER_ID                     VARCHAR2(10)  
,BIN_X_CNT                     NUMBER  
,BIN_Y_CNT                     NUMBER  
,RU_CNT                        NUMBER  
,ANALYSIS_WEIGHT               NUMBER  
,PHONE_NO                      VARCHAR2(12)  
,RESULT_TIME                   NUMBER  
,TILT_PROCESS_TYPE             NUMBER  
,GEOMETRYQUERY_SCHEDULE_ID     NUMBER  
,RESULT_BIT                    CHAR(8)  
,INTERWORKING_INFO             VARCHAR2(200)  
,CONSTRAINT SCHEDULE_PK PRIMARY KEY(SCHEDULE_ID,USER_ID)
);
--DROP TABLE CELLPLAN.SECTOR;
CREATE TABLE CELLPLAN.SECTOR
(
 SCENARIO_ID                   NUMBER NOT NULL
,ENB_ID                        VARCHAR2(20) NOT NULL
,PCI                           NUMBER NOT NULL
,SECTOR_STATUS                 NUMBER  
,SECTOR_TYPE                   NUMBER  
,SECTOR_ORD                    NUMBER NOT NULL
,BTS                           NUMBER  
,FA_SEQ                        NUMBER  
,CONSTRAINT SECTOR_PK PRIMARY KEY(SCENARIO_ID,ENB_ID,PCI,SECTOR_ORD)
);
--DROP TABLE CELLPLAN.SITE;
CREATE TABLE CELLPLAN.SITE
(
 SCENARIO_ID                   NUMBER  
,ENB_ID                        VARCHAR2(10)  
,PCI                           NUMBER  
,PCI_PORT                      NUMBER  
,SITE_NM                       VARCHAR2(100)  
,XPOSITION                     VARCHAR2(40)  
,YPOSITION                     VARCHAR2(40)  
,HEIGHT                        NUMBER  
,BLT_HEIGHT                    NUMBER  
,TOWER_HEIGHT                  NUMBER  
,SITE_ADDR                     VARCHAR2(500)  
,TYPE                          VARCHAR2(10)  
,CORRECTION_VALUE              NUMBER  
,FEEDER_LOSS                   NUMBER  
,FADE_MARGIN                   NUMBER  
,STATUS                        VARCHAR2(10)  
,MSC                           NUMBER  
,BSC                           NUMBER  
,NETWORKID                     NUMBER  
,USABLETRAFFICCH               NUMBER  
,SYSTEMID                      NUMBER  
,STRYPOS                       VARCHAR2(20)  
,STRXPOS                       VARCHAR2(20)  
,ATTENUATION                   NUMBER  
,SITE_GUBUN                    VARCHAR2(10)  
,RU_ID                         VARCHAR2(48)  
,RADIUS                        NUMBER  
,NOISEFLOOR                    NUMBER  
,FA_SEQ                        VARCHAR2(20)  
,RU_TYPE                       NUMBER  
,REG_DT                        DATE  
,SISUL_CD                      VARCHAR2(50)  
,TM_XPOSITION                  VARCHAR2(40)  
,TM_YPOSITION                  VARCHAR2(40)  
,RU_DIV_CD                     NUMBER  
);
--DROP TABLE CELLPLAN.SITE_DRAW_OPTION;
CREATE TABLE CELLPLAN.SITE_DRAW_OPTION
(
 COLOR_SECTOR                  VARCHAR2(30)  
,COLOR_RU_LINE                 VARCHAR2(30)  
,DRAW_DU_SYMBOL                VARCHAR2(10)  
,DRAW_RU_SYMBOL                VARCHAR2(10)  
,DRAW_DU_NAME                  VARCHAR2(50)  
,DRAW_RU_NAME                  VARCHAR2(50)  
,DRAW_DU_SECTOR                VARCHAR2(10)  
,DRAW_SECTOR_CELLID            VARCHAR2(10)  
,DRAW_SECTOR_FA                VARCHAR2(10)  
,DRAW_DU_LINE                  VARCHAR2(10)  
,LINE_WIDTH                    VARCHAR2(10)  
,SCENARIO_ID                   NUMBER  
,USER_ID                       VARCHAR2(13)  
,REG_DT                        DATE  
,PAIR_RU_SITE                  VARCHAR2(30)  
,PAIR_RU_SECTOR                VARCHAR2(30)  
,COLOR_SITE                    VARCHAR2(30)  
,PCI_NAME                      VARCHAR2(20)  
,MODE3_NAME                    VARCHAR2(20)  
,COLOR_SECTOR_B                VARCHAR2(20)  
,COLOR_BRANCH                  VARCHAR2(20)  
,COLOR_MODE1                   VARCHAR2(20)  
,COLOR_MODE2                   VARCHAR2(20)  
,COLOR_MODE3                   VARCHAR2(20)  
,MIBOS_COLOR_SECTOR            VARCHAR2(20)  
,MIBOS_COLOR_SITE              VARCHAR2(20)  
,MIBOS_COLOR_SECTOR_B          VARCHAR2(20)  
,MIBOS_COLOR_BRANCH            VARCHAR2(20)  
,MIBOS_COLOR_MODE1             VARCHAR2(20)  
,MIBOS_COLOR_MODE2             VARCHAR2(20)  
,MIBOS_COLOR_MODE3             VARCHAR2(20)  
,COLOR_MIBOS_RU_LINE           VARCHAR2(20)  
,FONT_SIZE                     NUMBER  
);
--DROP TABLE CELLPLAN.SWING_PARAMETER;
CREATE TABLE CELLPLAN.SWING_PARAMETER
(
 SCENARIO_ID                   NUMBER NOT NULL
,TARGET_PT_X                   NUMBER  
,TARGET_PT_Y                   NUMBER  
,TARGET_RADIUS                 NUMBER  
,TARGET_EXT_RADIUS             NUMBER  
,ONLY_TILT_SOLUTION_YN         NUMBER  
,BUILDING_WEIGHT_YN            NUMBER  
,ANTENNA_ANALYSIS_YN           NUMBER  
,MAX_BUILDING_YN               NUMBER  
,STATISTICS_AREA_YN            NUMBER  
,SWING_NORMAL_YN               NUMBER  
,SWING_RSRP_YN                 NUMBER  
,SWING_SINR_YN                 NUMBER  
,SWING_SERVING_YN              NUMBER  
,SWING_ITERATION_TYPE          NUMBER  
,SWING_ITERATION               NUMBER  
,CANDIDATE_ANTENNA_1           VARCHAR2(250)  
,CANDIDATE_ANTENNA_2           VARCHAR2(250)  
,CANDIDATE_ANTENNA_3           VARCHAR2(250)  
,MAX_ORIENTATION               NUMBER  
,ORIENTATION_RADIUS            NUMBER  
,MAX_BUILDING_HEIGHT           NUMBER  
,MAX_TILT_WIDTH                NUMBER  
,RSRP_THRESHOLD                NUMBER  
,SINR_THRESHOLD                NUMBER  
,SWING_THRESHOLD               NUMBER  
,OFFSET_RSRP                   NUMBER  
,OFFSET_SINR                   NUMBER  
,OFFSET_RSRP_RSRP              NUMBER  
,OFFSET_RSRP_SINR              NUMBER  
,OFFSET_SINR_RSRP              NUMBER  
,OFFSET_SINR_SINR              NUMBER  
,TILT_SOLUTION_YN              NUMBER  
,TILT_SOLUTION_TYPE            NUMBER  
,ORG_TILT_SOLUTION_YN          NUMBER  
,TILT_NORMAL_YN                NUMBER  
,TILT_SINR_YN                  NUMBER  
,TILT_RSSI_YN                  NUMBER  
,FINE_TILT_THRESHOLD           NUMBER  
,START_TIME                    VARCHAR2(20)  
,END_TIME                      VARCHAR2(20)  
,ORG_BIN_THRESHOLD             NUMBER  
,SERVING_OFFSET_RSRP           NUMBER  
,SERVING_OFFSET_SINR           NUMBER  
,SERVING_OFFSET_BIN            NUMBER  
,SERVING_BIN_ANALYSIS          NUMBER  
,MULTIBAND_SWING_TYPE          NUMBER  
,MULTIBAND_SPOT_USE_YN         VARCHAR2(1)  
,MULTIBAND_SPOT_SIZE           NUMBER  
,MULTIBAND_SPOT_RANGE          NUMBER  
,MULTIBAND_SPOT_RU_CNT         NUMBER  
,MULTIBAND_2ND_METHOD          NUMBER  
,MULTIBAND_2ND_SITE_LIMIT      NUMBER  
,MULTIBAND_2ND_SITE_AUTO       VARCHAR2(1)  
,SERVING_BIN_THRESHOLD         NUMBER  
,APT_ANALYSIS_YN               VARCHAR2(1)  
,CONSTRAINT SWING_PARAMETER_PK PRIMARY KEY(SCENARIO_ID)
);
--DROP TABLE CELLPLAN.TILT_BUILDING_DENSITY;
CREATE TABLE CELLPLAN.TILT_BUILDING_DENSITY
(
 SCENARIO_ID                   NUMBER NOT NULL
,TILT_BH_6_DIS_10              NUMBER  
,TILT_BH_6_DIS_20              NUMBER  
,TILT_BH_6_DIS_30              NUMBER  
,TILT_BH_6_DIS_40              NUMBER  
,TILT_BH_6_DIS_50              NUMBER  
,TILT_BH_6_DIS_60              NUMBER  
,TILT_BH_6_DIS_60A             NUMBER  
,TILT_BH_9_DIS_10              NUMBER  
,TILT_BH_9_DIS_20              NUMBER  
,TILT_BH_9_DIS_30              NUMBER  
,TILT_BH_9_DIS_40              NUMBER  
,TILT_BH_9_DIS_50              NUMBER  
,TILT_BH_9_DIS_60              NUMBER  
,TILT_BH_9_DIS_60A             NUMBER  
,TILT_BH_12_DIS_10             NUMBER  
,TILT_BH_12_DIS_20             NUMBER  
,TILT_BH_12_DIS_30             NUMBER  
,TILT_BH_12_DIS_40             NUMBER  
,TILT_BH_12_DIS_50             NUMBER  
,TILT_BH_12_DIS_60             NUMBER  
,TILT_BH_12_DIS_60A            NUMBER  
,TILT_BH_15_DIS_10             NUMBER  
,TILT_BH_15_DIS_20             NUMBER  
,TILT_BH_15_DIS_30             NUMBER  
,TILT_BH_15_DIS_40             NUMBER  
,TILT_BH_15_DIS_50             NUMBER  
,TILT_BH_15_DIS_60             NUMBER  
,TILT_BH_15_DIS_60A            NUMBER  
,TILT_BH_20_DIS_10             NUMBER  
,TILT_BH_20_DIS_20             NUMBER  
,TILT_BH_20_DIS_30             NUMBER  
,TILT_BH_20_DIS_40             NUMBER  
,TILT_BH_20_DIS_50             NUMBER  
,TILT_BH_20_DIS_60             NUMBER  
,TILT_BH_20_DIS_60A            NUMBER  
,TILT_BH_20A_DIS_10            NUMBER  
,TILT_BH_20A_DIS_20            NUMBER  
,TILT_BH_20A_DIS_30            NUMBER  
,TILT_BH_20A_DIS_40            NUMBER  
,TILT_BH_20A_DIS_50            NUMBER  
,TILT_BH_20A_DIS_60            NUMBER  
,TILT_BH_20A_DIS_60A           NUMBER  
,REG_DT                        DATE  
,CONSTRAINT TILT_BUILDING_DENSITY_PK PRIMARY KEY(SCENARIO_ID)
);
--DROP TABLE CELLPLAN.TILT_NR_ANT_LIST;
CREATE TABLE CELLPLAN.TILT_NR_ANT_LIST
(
 SCENARIO_ID                   NUMBER NOT NULL
,ANTENA_SEQ                    NUMBER NOT NULL
,ANTENA_NM                     VARCHAR2(150)  
,ANTENA_STANDARD_NM            VARCHAR2(150)  
,DEF_TILT                      NUMBER  
,REG_DT                        DATE  
,CONSTRAINT TILT_NR_ANT_LIST_PK PRIMARY KEY(SCENARIO_ID,ANTENA_SEQ)
);
--DROP TABLE CELLPLAN.TILT_NR_PARAMETER;
CREATE TABLE CELLPLAN.TILT_NR_PARAMETER
(
 SCENARIO_ID                   NUMBER NOT NULL
,ANT_MAKER                     VARCHAR2(50)  
,ANT_TYPE                      VARCHAR2(20)  
,BASE_SCENARIO_TYPE            NUMBER  
,ANT_BEAM_OPTIMIZE_YN          VARCHAR2(1)  
,E_TILT_OPTIMIZE_YN            VARCHAR2(1)  
,E_TILT_TYPE                   VARCHAR2(2)  
,USER_DEFINED_E_TILT           VARCHAR2(1000)  
,M_TILT_TYPE                   VARCHAR2(2)  
,USER_DEFINED_M_TILT           VARCHAR2(1000)  
,REG_DT                        DATE  
,CONSTRAINT TILT_NR_PARAMETER_PK PRIMARY KEY(SCENARIO_ID)
);
--DROP TABLE CELLPLAN.TILT_RUNRESULT;
CREATE TABLE CELLPLAN.TILT_RUNRESULT
(
 SCENARIO_ID                   VARCHAR2(20)  
,SCENARIO_NM                   VARCHAR2(100)  
,SITE_NM                       VARCHAR2(256)  
,SITE_ID                       VARCHAR2(100)  
,SITE_TYPE                     NUMBER  
,ENB_ID                        VARCHAR2(20)  
,PCI                           NUMBER  
,PCI_PORT                      NUMBER  
,RU_ID                         VARCHAR2(20)  
,ANTENNA_NM                    VARCHAR2(300)  
,ANT_ID                        NUMBER  
,ORIENTATION                   NUMBER  
,TILTING                       NUMBER  
,POINTX                        NUMBER  
,POINTY                        NUMBER  
,SITE_POSX                     NUMBER  
,SITE_POSY                     NUMBER  
,ADJSITE_NM                    VARCHAR2(256)  
,ADJSITE_ID                    VARCHAR2(256)  
,HEIGHT                        NUMBER  
,BUILDING_HEIGHT               NUMBER  
,TOWER_HEIGHT                  NUMBER  
,ADJSITE_HEIGHT                NUMBER  
,DISTANCE                      NUMBER  
,POINT_DISTANCE                NUMBER  
,TILT_TYPE                     VARCHAR2(20)  
,ANTENNA_SEQ                   NUMBER  
);
--DROP TABLE CELLPLAN.TILT_SCENARIO_EVO;
CREATE TABLE CELLPLAN.TILT_SCENARIO_EVO
(
 SCENARIO_ID                   NUMBER  
,SCENARIO_NM                   VARCHAR2(100)  
,DIST_STANDARD                 NUMBER  
,RADIUS                        NUMBER  
,TILT1                         NUMBER  
,TILT2                         NUMBER  
,TILT3                         NUMBER  
,TILT4                         NUMBER  
,TILT5                         NUMBER  
,TILT6                         NUMBER  
,TILT7                         NUMBER  
,TILT8                         NUMBER  
,TILT9                         NUMBER  
,TILT10                        NUMBER  
,MCFIX                         NUMBER  
,TILT_ANGLE                    NUMBER  
,THRESHOLDRSSI1                NUMBER  
,THRESHOLDRSSI2                NUMBER  
,THRESHOLDRSSI3                NUMBER  
,DIFFRACTION_LOSS              NUMBER  
,SAMPLING_INTERVAL             NUMBER  
,MIN_DISTANCE                  NUMBER  
,TILT_PCI                      NUMBER  
,TILT_HEIGHT_10                NUMBER  
,TILT_HEIGHT_20                NUMBER  
,TILT_HEIGHT_30                NUMBER  
,TILT_HEIGHT_40                NUMBER  
,TILT_IN_ANGLE_A_65            NUMBER  
,TILT_IN_ANGLE_A_100           NUMBER  
,TILT_IN_ANGLE_A_140           NUMBER  
,TILT_IN_ANGLE_A_180           NUMBER  
,TILT_IN_ANGLE_B_65            NUMBER  
,TILT_IN_ANGLE_B_100           NUMBER  
,TILT_IN_ANGLE_B_140           NUMBER  
,TILT_POWER                    NUMBER  
,TILT_GAIN                     NUMBER  
,TILT_DIST_100_UD              NUMBER  
,TILT_DIST_150_UD              NUMBER  
,TILT_DIST_200_UD              NUMBER  
,TILT_DIST_250_UD              NUMBER  
,TILT_DIST_300_UD              NUMBER  
,TILT_DIST_350_UD              NUMBER  
,TILT_DIST_400_UD              NUMBER  
,TILT_DIST_450_UD              NUMBER  
,TILT_DIST_500_UD              NUMBER  
,TILT_DIST_550_UD              NUMBER  
,TILT_DIST_600_UD              NUMBER  
,TILT_DIST_600_OV              NUMBER  
,TILT_ADDED                    NUMBER  
,TILT_HEIGHT_10_UD             NUMBER  
,TILT_ANTENNA_01               NUMBER  
,TILT_ANTENNA_02               NUMBER  
,TILT_ANTENNA_03               NUMBER  
,TILT_LIMIT_ELEV_A_1           NUMBER  
,TILT_ELEV_A_1                 NUMBER  
,TILT_LIMIT_ELEV_A_2           NUMBER  
,TILT_ELEV_A_2                 NUMBER  
,TILT_LIMIT_ELEV_A_3           NUMBER  
,TILT_ELEV_A_3                 NUMBER  
,TILT_LIMIT_ELEV_A_4           NUMBER  
,TILT_ELEV_A_4                 NUMBER  
,TILT_LIMIT_ELEV_B_1           NUMBER  
,TILT_ELEV_B_1                 NUMBER  
,TILT_LIMIT_ELEV_B_2           NUMBER  
,TILT_ELEV_B_2                 NUMBER  
,TILT_LIMIT_ELEV_B_3           NUMBER  
,TILT_ELEV_B_3                 NUMBER  
,TILT_LIMIT_ELEV_B_4           NUMBER  
,TILT_ELEV_B_4                 NUMBER  
,TILT_BEAM_HEIGHT_10_UD        NUMBER  
,TILT_BEAM_HEIGHT_10           NUMBER  
,TILT_BEAM_HEIGHT_20           NUMBER  
,TILT_BEAM_HEIGHT_30           NUMBER  
,TILT_BEAM_HEIGHT_40           NUMBER  
);
--DROP TABLE CELLPLAN.TILT_SCENARIO_FINE;
CREATE TABLE CELLPLAN.TILT_SCENARIO_FINE
(
 SCENARIO_ID                   NUMBER  
,SCENARIO_NM                   VARCHAR2(100)  
,DIST_STANDARD                 NUMBER  
,RADIUS                        NUMBER  
,TILT1                         NUMBER  
,TILT2                         NUMBER  
,TILT3                         NUMBER  
,TILT4                         NUMBER  
,TILT5                         NUMBER  
,TILT6                         NUMBER  
,TILT7                         NUMBER  
,TILT8                         NUMBER  
,TILT9                         NUMBER  
,TILT10                        NUMBER  
,MCFIX                         NUMBER  
,TILT_ANGLE                    NUMBER  
,THRESHOLDRSSI1                NUMBER  
,THRESHOLDRSSI2                NUMBER  
,THRESHOLDRSSI3                NUMBER  
,DIFFRACTION_LOSS              NUMBER  
,SAMPLING_INTERVAL             NUMBER  
,MIN_DISTANCE                  NUMBER  
,TILT_PCI                      NUMBER  
,TILT_HEIGHT_10_UD             NUMBER  
,TILT_HEIGHT_10                NUMBER  
,TILT_HEIGHT_20                NUMBER  
,TILT_HEIGHT_30                NUMBER  
,TILT_HEIGHT_40                NUMBER  
,TILT_IN_ANGLE_A_65            NUMBER  
,TILT_IN_ANGLE_A_100           NUMBER  
,TILT_IN_ANGLE_A_140           NUMBER  
,TILT_IN_ANGLE_A_180           NUMBER  
,TILT_IN_ANGLE_B_65            NUMBER  
,TILT_IN_ANGLE_B_100           NUMBER  
,TILT_IN_ANGLE_B_140           NUMBER  
,TILT_POWER                    NUMBER  
,TILT_GAIN                     NUMBER  
,TILT_DIST_100_UD              NUMBER  
,TILT_DIST_150_UD              NUMBER  
,TILT_DIST_200_UD              NUMBER  
,TILT_DIST_250_UD              NUMBER  
,TILT_DIST_300_UD              NUMBER  
,TILT_DIST_350_UD              NUMBER  
,TILT_DIST_400_UD              NUMBER  
,TILT_DIST_450_UD              NUMBER  
,TILT_DIST_500_UD              NUMBER  
,TILT_DIST_550_UD              NUMBER  
,TILT_DIST_600_UD              NUMBER  
,TILT_DIST_600_OV              NUMBER  
,TILT_ANTENNA_01               NUMBER  
,TILT_ANTENNA_02               NUMBER  
,TILT_ANTENNA_03               NUMBER  
,TILT_ADDED                    NUMBER  
,TILT_LIMIT_ELEV_A_1           NUMBER  
,TILT_ELEV_A_1                 NUMBER  
,TILT_LIMIT_ELEV_A_2           NUMBER  
,TILT_ELEV_A_2                 NUMBER  
,TILT_LIMIT_ELEV_A_3           NUMBER  
,TILT_ELEV_A_3                 NUMBER  
,TILT_LIMIT_ELEV_A_4           NUMBER  
,TILT_ELEV_A_4                 NUMBER  
,TILT_LIMIT_ELEV_B_1           NUMBER  
,TILT_ELEV_B_1                 NUMBER  
,TILT_LIMIT_ELEV_B_2           NUMBER  
,TILT_ELEV_B_2                 NUMBER  
,TILT_LIMIT_ELEV_B_3           NUMBER  
,TILT_ELEV_B_3                 NUMBER  
,TILT_LIMIT_ELEV_B_4           NUMBER  
,TILT_ELEV_B_4                 NUMBER  
,TILT_BEAM_HEIGHT_10_UD        NUMBER  
,TILT_BEAM_HEIGHT_10           NUMBER  
,TILT_BEAM_HEIGHT_20           NUMBER  
,TILT_BEAM_HEIGHT_30           NUMBER  
,TILT_BEAM_HEIGHT_40           NUMBER  
);
--DROP TABLE CELLPLAN.TILT_TARGET_INFO;
CREATE TABLE CELLPLAN.TILT_TARGET_INFO
(
 SCENARIO_ID                   VARCHAR2(20)  
,MAIN_ENB_ID                   VARCHAR2(20)  
,MAIN_PCI                      NUMBER  
,MAIN_PCI_PORT                 NUMBER  
,MAIN_RU_ID                    VARCHAR2(20)  
,MAIN_RU_NM                    VARCHAR2(150)  
,MAIN_ANTENNA_CNT              NUMBER  
,MAIN_ANTENNA_ORD              NUMBER  
,MAIN_ANTENNA_ID               NUMBER  
,MAIN_ANTENNA_GAIN             NUMBER  
,MAIN_POWER                    NUMBER  
,MAIN_DONOR_SITE_NAME          VARCHAR2(150)  
,MAIN_DONOR_ENB_ID             VARCHAR2(20)  
,MAIN_DONOR_SECTOR_ID          NUMBER  
,MAIN_HEIGHT                   NUMBER  
,MAIN_INSIDE_ANGLE             NUMBER  
,TARGET_ENB_ID                 VARCHAR2(20)  
,TARGET_PCI                    NUMBER  
,TARGET_PCI_PORT               NUMBER  
,TARGET_RU_ID                  VARCHAR2(20)  
,TARGET_RU_NM                  VARCHAR2(150)  
,TARGET_ANTENNA_CNT            NUMBER  
,TARGET_ANTENNA_ORD            NUMBER  
,TARGET_ANTENNA_ID             NUMBER  
,TARGET_ANTENNA_GAIN           NUMBER  
,TARGET_POWER                  NUMBER  
,TARGET_DONOR_SITE_NAME        VARCHAR2(150)  
,TARGET_DONOR_ENB_ID           VARCHAR2(20)  
,TARGET_DONOR_SECTOR_ID        NUMBER  
,TARGET_INSIDE_ANGLE           NUMBER  
,IS_CROSS_ANGLE                NUMBER  
,INSIDE_ANGLE                  NUMBER  
,DIFF_POWER                    NUMBER  
,DIFF_GAIN                     NUMBER  
,DISTANCE                      NUMBER  
,PCI_ADJ                       NUMBER  
,HEIGHT_ADJ                    NUMBER  
,INSIDE_ANGLE_ADJ              NUMBER  
,POWER_ADJ                     NUMBER  
,GAIN_ADJ                      NUMBER  
,DIST_ADJ                      NUMBER  
,ANTENNA_ADJ                   NUMBER  
,ADDED_ADJ                     NUMBER  
,ELEVATION_ADJ                 NUMBER  
,BEAM_HEIGHT_ADJ               NUMBER  
,TILT_ORD                      NUMBER  
,TILT_BASE                     NUMBER  
);
