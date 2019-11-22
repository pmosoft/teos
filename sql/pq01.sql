-------------------------------------------------
-- SCHEDULE
-------------------------------------------------
SELECT A.SCHEDULE_ID
     , A.SCENARIO_ID
     , A.REG_DT
     , A.TYPE_CD
     , A.PROCESS_CD
     , A.PROCESS_MSG
     , B.SCENARIO_NM
     , B.SIDO
     , B.SIGUGUN
     , B.DONG
     , ROUND((B.TM_ENDX-B.TM_STARTX) * (B.TM_ENDY-B.TM_STARTY),0) AS AREA
     , A.BIN_X_CNT
     , A.BIN_Y_CNT
     , A.RU_CNT
     , ROUND(A.RESULT_TIME,0) AS RESULT_TIME
     , B.RESOLUTION
     , B.BUILDINGANALYSIS3D_RESOLUTION     -- 3D분석Resolution
     , B.BUILDINGANALYSIS3D_YN             -- 3D분석여부
     , A.SCENARIO_PATH
FROM   SCHEDULE A
     , SCENARIO B
WHERE  A.SCENARIO_ID = B.SCENARIO_ID
--AND    A.PROCESS_CD = '20001'
--AND    TYPE_CD IN ('SC001','SC051')
AND    A.SCENARIO_ID = 5104574
ORDER BY REG_DT DESC
;

select * FROM   SCHEDULE where SCHEDULE_ID = 8463234;

select * FROM   SCHEDULE where SCHEDULE_ID = 8463235;

select * FROM   SCHEDULE where SCHEDULE_ID = 8460064;

select * FROM   SCENARIO where SCENARIO_ID = 5104574;

select * FROM   MOBILE_PARAMETER where SCENARIO_ID = 5104574;

select COUNT(*) FROM   SCENARIO_NR_RU where SCENARIO_ID = '5113566';

select * from JOB_DIS where SCENARIO_ID = 5104574;

select * FROM   SCHEDULE where SCHEDULE_ID = 8463234;

select * FROM SCHEDULE where SCHEDULE_ID = 8463234;

select * FROM LOS_BLD_RESULT
where schedule_id = 8463235
;

select * from JOB_DIS_ETL where SCHEDULE_ID = '8463235';

delete from JOB_DIS_ETL where SCHEDULE_ID = '8463235';

select * FROM LOS_BLD_RESULT
where schedule_id = 8463235

--scenario_id=5113766
--schedule_id = 8463234 (2D분석)
--schedule_id = 8463235 (3D분석)

select *
  from RESULT_NR_2D_LOS_RU RSLT
 where  RSLT.ru_id = 1011760123
 ;




select * from result_nr_bf_scen_header where schedule_id = 8463235
;

