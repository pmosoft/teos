DROP TABLE I_RESULT_NR_2D_PATHLOSS_RU;

CREATE EXTERNAL TABLE I_RESULT_NR_2D_PATHLOSS_RU (
  SCENARIO_ID                      INT
, RU_ID                            STRING
, RX_TM_XPOS                       INT
, RX_TM_YPOS                       INT
, RZ                               FLOAT
, LOS                              INT
, PATHLOSS                         FLOAT
, IS_UMI_MODEL                     INT
, DIST2D                           FLOAT
, DIST3D                           FLOAT
, DISTBP                           FLOAT
, HBS                              FLOAT
, HUT                              FLOAT
, SCHEDULE_ID                      INT
)

SELECT * FROM I_RESULT_NR_2D_PATHLOSS_RU;