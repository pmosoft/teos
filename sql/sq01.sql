SELECT 'UPDATE TDACM00080 SET TAB_HNM='''+A.TAB_HNM+''' , COL_HNM=''+A.COL_HNM+'' WHERE JDBC_NM = ''EOSLOH'' AND TAB_NM=''+B.TAB_NM+'' AND COL_NM=''+B.COL_NM+'';'
FROM TDACM00080 A, 
     TDACM00080 B
WHERE A.JDBC_NM = 'EOS_DEV'
AND   B.JDBC_NM = 'EOSLOH'
AND   A.TAB_NM = B.TAB_NM
AND   A.COL_NM = B.COL_NM
;


CREATE TABLE TDACM00060 (
 CD_GRP         VARCHAR(20)  NOT NULL --COMMENT '코드그룹'       -- META
,CD_GRP_NM      VARCHAR(20)      NULL --COMMENT '코드그룹명'      -- 메타
,CD_ID          VARCHAR(20)  NOT NULL --COMMENT '코드아이디'     -- COL_STS_CD
,CD_ID_NM       VARCHAR(20)      NULL --COMMENT '코드아이디명'    -- 컬럼상태코드
,CD             VARCHAR(20)  NOT NULL --COMMENT '코드'          -- 01
,CD_NM          VARCHAR(500)     NULL --COMMENT '코드명'        -- 요청
,CD_DESC        VARCHAR(500)     NULL --COMMENT '코드설명'
,CD_TY_CD       CHAR(1)          NULL --COMMENT '코드유형코드'    -- 1:필드 2:화면 3:프로그램
,CD_SEQ         INT              NULL --COMMENT '코드순서'       
,REG_DTM        VARCHAR(14)      NULL --COMMENT '등록일시'
,REG_USR_ID     VARCHAR(20)      NULL --COMMENT '등록자'
,UPD_DTM        VARCHAR(14)      NULL --COMMENT '변경일시'
,UPD_USR_ID     VARCHAR(20)      NULL --COMMENT '변경자'
,PRIMARY KEY(CD_GRP,CD_ID,CD)
);

DROP TABLE TDACM00060;



SELECT * FROM TDACM00060
;


SELECT * FROM TDACM00061;

DELETE FROM TDACM00062;

INSERT INTO TDACM00060 
SELECT  
 CD_GRP    
,CD_GRP_NM 
,CD_ID     
,CD_ID_NM  
,CD        
,CD_NM     
,CD_DESC   
,CD_TY_CD  
,0 CD_SEQ    
,REG_DTM   
,REG_USR_ID
,UPD_DTM   
,UPD_USR_ID
FROM TDACM00062;

