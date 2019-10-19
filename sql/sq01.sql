SELECT 'UPDATE TDACM00080 SET TAB_HNM='''+A.TAB_HNM+''' , COL_HNM=''+A.COL_HNM+'' WHERE JDBC_NM = ''EOSLOH'' AND TAB_NM=''+B.TAB_NM+'' AND COL_NM=''+B.COL_NM+'';'
FROM TDACM00080 A, 
     TDACM00080 B
WHERE A.JDBC_NM = 'EOS_DEV'
AND   B.JDBC_NM = 'EOSLOH'
AND   A.TAB_NM = B.TAB_NM
AND   A.COL_NM = B.COL_NM
;


CREATE TABLE TDACM00060 (
 CD_GRP         VARCHAR(20)  NOT NULL --COMMENT '�ڵ�׷�'       -- META
,CD_GRP_NM      VARCHAR(20)      NULL --COMMENT '�ڵ�׷��'      -- ��Ÿ
,CD_ID          VARCHAR(20)  NOT NULL --COMMENT '�ڵ���̵�'     -- COL_STS_CD
,CD_ID_NM       VARCHAR(20)      NULL --COMMENT '�ڵ���̵��'    -- �÷������ڵ�
,CD             VARCHAR(20)  NOT NULL --COMMENT '�ڵ�'          -- 01
,CD_NM          VARCHAR(500)     NULL --COMMENT '�ڵ��'        -- ��û
,CD_DESC        VARCHAR(500)     NULL --COMMENT '�ڵ弳��'
,CD_TY_CD       CHAR(1)          NULL --COMMENT '�ڵ������ڵ�'    -- 1:�ʵ� 2:ȭ�� 3:���α׷�
,CD_SEQ         INT              NULL --COMMENT '�ڵ����'       
,REG_DTM        VARCHAR(14)      NULL --COMMENT '����Ͻ�'
,REG_USR_ID     VARCHAR(20)      NULL --COMMENT '�����'
,UPD_DTM        VARCHAR(14)      NULL --COMMENT '�����Ͻ�'
,UPD_USR_ID     VARCHAR(20)      NULL --COMMENT '������'
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

