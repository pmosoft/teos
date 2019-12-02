--------------------------------------------------------------------------
--                         스케줄 정책 관련 테이블
--------------------------------------------------------------------------

SELECT * FROM SCHEDULE WHERE SCHEDULE_ID = 8460178;
SELECT * FROM SCHEDULE_EXT WHERE SCHEDULE_ID = 8460178;
SELECT * FROM SCHEDULE_STEP WHERE SCHEDULE_ID = 8460178;

SELECT * FROM SCHEDULE WHERE SCENARIO_ID = 5105173;

SELECT CASE WHEN COUNT(*) = 1 THEN 'Y' ELSE 'N' END AS BD_YN
FROM   SCHEDULE 
WHERE  SCENARIO_ID = 5113972 
AND    TYPE_CD     = 'SC051'
;

SELECT * FROM SCHEDULE WHERE SCHEDULE_ID = 8463246;




