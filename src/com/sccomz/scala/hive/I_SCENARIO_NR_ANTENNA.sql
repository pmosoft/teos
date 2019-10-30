-----------------------------------------
-- I_SCENARIO_NR_ANTENNA
-----------------------------------------
DROP TABLE PUBLIC.I_SCENARIO_NR_ANTENNA;

CREATE TABLE PUBLIC.I_SCENARIO_NR_ANTENNA (
  SCENARIO_ID                   INT
, ANTENA_SEQ                    INT
, RU_ID                         STRING
, ANTENA_NM                     STRING
, ORIENTATION                   INT
, TILTING                       INT
, ANTENA_ORD                    INT
, LIMIT_TILTING                 INT
, RU_SEQ                        INT
)