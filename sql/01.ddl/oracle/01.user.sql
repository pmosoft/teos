-----------------------
-- SQLPULS ����
-----------------------
sqlplus system/1

-----------------------
-- ���̺� �����̽�
-----------------------
DROP TABLESPACE TEOS_DATA INCLUDING CONTENTS;
--DROP TABLESPACE IDEX1;

CREATE TABLESPACE TEOS_DATA  DATAFILE 'D:/DOCUMENTS/GDRIVE_EXT/DB/ORACLE/TEOS_DATA.DBF' SIZE 10000M;
//CREATE TABLESPACE INDEX1 DATAFILE 'D:/DOCUMENTS/GDRIVE_EXT/DB/ORACLE/TEOS_INDEX.DBF' SIZE 5000M;

-----------------------
-- ��������
-----------------------
-- SELECT * FROM DBA_USERS WHERE USERNAME

DROP USER cellplan CASCADE;

CREATE USER cellplan IDENTIFIED BY cell_2012 DEFAULT TABLESPACE TEOS_DATA;

-----------------------
-- ���ѻ���
-----------------------
GRANT CREATE SESSION TO cellplan WITH ADMIN OPTION;
GRANT CREATE TABLE TO cellplan WITH ADMIN OPTION;
GRANT CREATE VIEW TO cellplan WITH ADMIN OPTION;
GRANT CONNECT,RESOURCE TO cellplan;
GRANT CREATE PUBLIC SYNONYM TO cellplan WITH ADMIN OPTION;
GRANT CREATE PUBLIC DATABASE LINK TO cellplan WITH ADMIN OPTION;
GRANT CREATE ANY JOB TO cellplan;

sqlplus "/ as sysdba"
@d:/app/Administrator/product/11.2.0/dbhome_1/rdbms/admin/dbmsobtk.sql
@d:/app/Administrator/product/11.2.0/dbhome_1/rdbms/admin/prvtobtk.plb
/

-----------------------
-- �α� ���� ���� ����
-----------------------
CREATE OR REPLACE DIRECTORY cellplan AS 'D:/DOCUMENTS/GDRIVE_EXT/DB/ORACLE/log_file';
GRANT READ ON DIRECTORY cellplan TO PUBLIC;
GRANT WRITE ON DIRECTORY cellplan TO PUBLIC;

-------------------------------
-- ��ȣȭ ���̺� ���� �ο�
-------------------------------
--GRANT EXECUTE ON DBMS_CRYPTO TO cellplan;

-----------------------
-- ���̺� ������ �̰�
-----------------------
-- all
exp system/ora123@ORCL_RBA file=d:/dump1.dmp owner=cellplan direct=y buffer=10240000 grants=y compress=n constraints=y indexes=y rows=y triggers=n feedback=10000 log=d:/dump1.log
imp system/manager1 file=d:/dump1.dmp fromuser=cellplan touser=cellplan commit=y ignore=y buffer=10240000 grants=y constraints=y indexes=y rows=y feedback=10000 log=d:/dump1_imp.log

-- table
exp system/manager1 file=d:/dump1.dmp direct=y buffer=10240000 grants=y compress=n constraints=y indexes=y rows=y triggers=n tables=TSKGHFD01_T20120416_09,TSKGHFE01_T20120416_09,TSKGHFF09_T20120416_09,TSKGHFF08_T20120416_09,TSKGHFF01_T20120416_09,TSKGHFC05_T20120416_09,TSKGHFB01_T20120416_09,TSKGHFE01_T20120404_15,TSKGHFD01_T20120404_15,TSKGHFF09_T20120404_15,TSKGHFF08_T20120404_15,TSKGHFF01_T20120404_15,TSKGHFB01_T20120404_15,TSKGHFC05_T20120404_15,TSKGHFE01_T20120406_09,TSKGHFD01_T20120406_09,TSKGHFF09_T20120406_09,TSKGHFF08_T20120406_09,TSKGHFF01_T20120406_09,TSKGHFC05_T20120406_09,TSKGHFB01_T20120406_09,TSKGHFD01_T20120409_17,TSKGHFE01_T20120409_17,TSKGHFF09_T20120409_17,TSKGHFF08_T20120409_17,TSKGHFF01_T20120409_17,TSKGHFC05_T20120409_17,TSKGHFB01_T20120409_17 feedback=10000 log=d:/dump1.log
imp system/manager1 file=d:/dump1.dmp fromuser=system touser=system commit=y ignore=y buffer=10240000 grants=y constraints=y indexes=y rows=y tables=TSKGHFD01_T20120416_09,TSKGHFE01_T20120416_09,TSKGHFF09_T20120416_09,TSKGHFF08_T20120416_09,TSKGHFF01_T20120416_09,TSKGHFC05_T20120416_09,TSKGHFB01_T20120416_09,TSKGHFE01_T20120404_15,TSKGHFD01_T20120404_15,TSKGHFF09_T20120404_15,TSKGHFF08_T20120404_15,TSKGHFF01_T20120404_15,TSKGHFB01_T20120404_15,TSKGHFC05_T20120404_15,TSKGHFE01_T20120406_09,TSKGHFD01_T20120406_09,TSKGHFF09_T20120406_09,TSKGHFF08_T20120406_09,TSKGHFF01_T20120406_09,TSKGHFC05_T20120406_09,TSKGHFB01_T20120406_09,TSKGHFD01_T20120409_17,TSKGHFE01_T20120409_17,TSKGHFF09_T20120409_17,TSKGHFF08_T20120409_17,TSKGHFF01_T20120409_17,TSKGHFC05_T20120409_17,TSKGHFB01_T20120409_17 feedback=10000 log=d:/dump1_imp.log

-- KGHPKG_LOG_DIR
-----------------------
-- ���̺� ����
-----------------------
-----------
-- DROP
-----------
--SELECT 'DROP TABLE '||REFERENCED_NAME||';' FROM INFU01 WHERE REFERENCED_NAME LIKE 'TSK%' ORDER BY 1

-- INST1
SELECT 'DROP '||OBJECT_TYPE||' '||OBJECT_NAME||';'
FROM ALL_OBJECTS
WHERE OBJECT_NAME LIKE 'TSKG%'


------------
-- CREATE
------------
SELECT 'CREATE TABLE '||REFERENCED_NAME||' TABLESPACE DATA1 AS SELECT * FROM INST1.'||REFERENCED_NAME||'@KGH001;'
FROM INFU01
WHERE REFERENCED_NAME LIKE 'TSK%'
AND REFERENCED_NAME NOT IN (
 'TSKGHFE01'
,'TSKGHFD01'
,'TSKGHFD02'
,'TSKGHFD05'
,'TSKGHFD06'
,'TSKGHFD11'
,'TSKGHFC41'
,'TSKGHFF01'
,'TSKGHFF05'
,'TSKGHSA04'
,'TSKGHSE07'
,'TSKGHSE16'
,'TSKGHSE17'
,'TSKGHSE18'
,'TSKGHSE19'
,'TSKGHSE54'
,'TSKGHSE64'
)
ORDER BY 1

CREATE TABLE TSKGHFE01 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHFE01@KGH001 WHERE ü������ IN ('20120426','20120427');
CREATE TABLE TSKGHFD01 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHFD01@KGH001 WHERE ó������� IN ('20120426','20120427');
CREATE TABLE TSKGHFD02 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHFD02@KGH001 WHERE ó������� IN ('20120426','20120427');
CREATE TABLE TSKGHFD05 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHFD05@KGH001 WHERE �򰡳���� IN ('20120426','20120427');
CREATE TABLE TSKGHFD06 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHFD06@KGH001 WHERE �򰡳���� IN ('20120426','20120427');
CREATE TABLE TSKGHFD11 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHFD11@KGH001 WHERE ó������� IN ('20120426','20120427');
CREATE TABLE TSKGHFC41 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHFC41@KGH001 WHERE �򰡳���� IN ('20120426','20120427');
CREATE TABLE TSKGHFF01 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHFF01@KGH001 WHERE ó������� IN ('20120426','20120427');
CREATE TABLE TSKGHFF05 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHFF05@KGH001 WHERE �򰡳���� IN ('20120426','20120427');
CREATE TABLE TSKGHSA04 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHSA04@KGH001 WHERE ó������� IN ('20120426','20120427');
CREATE TABLE TSKGHSE07 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHSE07@KGH001 WHERE �ŷ������ IN ('20120426','20120427');
CREATE TABLE TSKGHSE16 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHSE16@KGH001 WHERE �򰡳���� IN ('20120426','20120427');
CREATE TABLE TSKGHSE17 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHSE17@KGH001 WHERE �򰡳���� IN ('20120426','20120427');
CREATE TABLE TSKGHSE18 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHSE18@KGH001 WHERE �򰡳���� IN ('20120426','20120427');
CREATE TABLE TSKGHSE19 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHSE19@KGH001 WHERE �򰡳���� IN ('20120426','20120427');
CREATE TABLE TSKGHSE54 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHSE54@KGH001 WHERE ���س���� IN ('20120426','20120427');
CREATE TABLE TSKGHSE64 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHSE64@KGH001 WHERE ���س���� IN ('20120426','20120427');

DROP TABLE TSKGHFE01;
DROP TABLE TSKGHFD01;
DROP TABLE TSKGHFD02;
DROP TABLE TSKGHFD05;
DROP TABLE TSKGHFD06;
DROP TABLE TSKGHFD11;
DROP TABLE TSKGHFC41;
DROP TABLE TSKGHFF05;
DROP TABLE TSKGHSE07;
DROP TABLE TSKGHSE16;
DROP TABLE TSKGHSE17;
DROP TABLE TSKGHSE18;
DROP TABLE TSKGHSE19;
DROP TABLE TSKGHSE54;
DROP TABLE TSKGHSE64;



-- KGH000Y
CREATE TABLE TSKGHFE01 TABLESPACE DATA1 AS SELECT * FROM KGH000Y.TSKGHFE01@KGH001 WHERE ü������ IN ('20110111','20110112');
CREATE TABLE TSKGHFD01 TABLESPACE DATA1 AS SELECT * FROM KGH000Y.TSKGHFD01@KGH001 WHERE ó������� IN ('20110111','20110112');
CREATE TABLE TSKGHFF05 TABLESPACE DATA1 AS SELECT * FROM KGH000Y.TSKGHFF05@KGH001 WHERE �򰡳���� IN ('20110111','20110112');
CREATE TABLE TSKGHFF01 TABLESPACE DATA1 AS SELECT * FROM KGH000Y.TSKGHFF01@KGH001 WHERE ó������� IN ('20110111','20110112');



INSERT INTO TSKGHFE01 SELECT * FROM INST1.TSKGHFE01@KGH001 WHERE ü������ IN ('20120427','20120427');
INSERT INTO TSKGHFD01 SELECT * FROM INST1.TSKGHFD01@KGH001 WHERE ó������� IN ('20120427','20120427');
INSERT INTO TSKGHFD02 SELECT * FROM INST1.TSKGHFD02@KGH001 WHERE ó������� IN ('20120427','20120427');
INSERT INTO TSKGHFD05 SELECT * FROM INST1.TSKGHFD05@KGH001 WHERE �򰡳���� IN ('20120427','20120427');
INSERT INTO TSKGHFD06 SELECT * FROM INST1.TSKGHFD06@KGH001 WHERE �򰡳���� IN ('20120427','20120427');
INSERT INTO TSKGHFD11 SELECT * FROM INST1.TSKGHFD11@KGH001 WHERE ó������� IN ('20120427','20120427');
INSERT INTO TSKGHFC41 SELECT * FROM INST1.TSKGHFC41@KGH001 WHERE �򰡳���� IN ('20120427','20120427');
INSERT INTO TSKGHFF05 SELECT * FROM INST1.TSKGHFF05@KGH001 WHERE �򰡳���� IN ('20120427','20120427');
INSERT INTO TSKGHSA04 SELECT * FROM INST1.TSKGHSA04@KGH001 WHERE ó������� IN ('20120427','20120427');
INSERT INTO TSKGHSE07 SELECT * FROM INST1.TSKGHSE07@KGH001 WHERE �ŷ������ IN ('20120427','20120427');
INSERT INTO TSKGHSE16 SELECT * FROM INST1.TSKGHSE16@KGH001 WHERE �򰡳���� IN ('20120427','20120427');
INSERT INTO TSKGHSE17 SELECT * FROM INST1.TSKGHSE17@KGH001 WHERE �򰡳���� IN ('20120427','20120427');
INSERT INTO TSKGHSE18 SELECT * FROM INST1.TSKGHSE18@KGH001 WHERE �򰡳���� IN ('20120427','20120427');
INSERT INTO TSKGHSE19 SELECT * FROM INST1.TSKGHSE19@KGH001 WHERE �򰡳���� IN ('20120427','20120427');
INSERT INTO TSKGHSE54 SELECT * FROM INST1.TSKGHSE54@KGH001 WHERE ���س���� IN ('20120427','20120427');
INSERT INTO TSKGHSE64 SELECT * FROM INST1.TSKGHSE64@KGH001 WHERE ���س���� IN ('20120427','20120427');


ANALYZE TABLE TSKGHFE01 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHFD01 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHFD02 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHFD05 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHFD06 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHFD11 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHFC41 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHFF05 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHSE07 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHSE16 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHSE17 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHSE18 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHSE19 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHSE54 COMPUTE STATISTICS;
ANALYZE TABLE TSKGHSE64 COMPUTE STATISTICS;


COMMIT;


DELETE FROM TSKGHFE01 WHERE ü������ IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHFD01 WHERE ó������� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHFD02 WHERE ó������� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHFD05 WHERE �򰡳���� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHFD06 WHERE �򰡳���� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHFD11 WHERE ó������� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHFC41 WHERE �򰡳���� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHFF05 WHERE �򰡳���� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHSE07 WHERE �ŷ������ IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHSE16 WHERE �򰡳���� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHSE17 WHERE �򰡳���� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHSE18 WHERE �򰡳���� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHSE19 WHERE �򰡳���� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHSE54 WHERE ���س���� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');
DELETE FROM TSKGHSE64 WHERE ���س���� IN ('20120501','20120502','20120503','20111102','20110105','20110106','20110113');


------------
-- GRANT
------------

SELECT 'GRANT ALL ON '||OBJECT_NAME||' TO KGH001;'
FROM ALL_OBJECTS
WHERE OBJECT_NAME LIKE 'TSK%'
ORDER BY 1

------------------------
-- ���۾� ���̺� ����
------------------------
CREATE TABLE TSKGHFA41 TABLESPACE DATA1 AS SELECT * FROM INST1.TSKGHFA41@KGH001 WHERE ROWNUM = 1;

GRANT ALL ON TSKGHFA41 TO KGH001;

CREATE TABLE GTKGHBO01 TABLESPACE DATA1 AS SELECT * FROM INST1.GTKGHBO01@KGH001;
GRANT ALL ON GTKGHBO01 TO KGH001;



------------
-- INDEX
------------

SELECT TABLE_NAME,INDEX_NAME,REPLACE(SUBSTR(I2,INSTR(I2,'('),LENGTH(I2)),')',''),I2
FROM  (
       SELECT   REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(I1,',)',')'),',)',')'),',)',')'),',)',')'),',)',')'),',)',')'),',)',')'),',)',')'),',)',')'),',)',')') I2
               ,TABLE_NAME,INDEX_NAME
       FROM   (
               SELECT 'CREATE '||DECODE(MAX(A.UNIQUENESS),'NONUNIQUE','',MAX(A.UNIQUENESS))||' INDEX '||' '||INDEX_NAME||' ON '||MAX(A.TABLE_OWNER)||'.'||MAX(A.TABLE_NAME)||'('
                      ||MAX(CASE A.COLUMN_POSITION WHEN  1 THEN COLUMN_NAME ELSE NULL END)||','
                      ||MAX(CASE A.COLUMN_POSITION WHEN  2 THEN COLUMN_NAME ELSE NULL END)||','
                      ||MAX(CASE A.COLUMN_POSITION WHEN  3 THEN COLUMN_NAME ELSE NULL END)||','
                      ||MAX(CASE A.COLUMN_POSITION WHEN  4 THEN COLUMN_NAME ELSE NULL END)||','
                      ||MAX(CASE A.COLUMN_POSITION WHEN  5 THEN COLUMN_NAME ELSE NULL END)||','
                      ||MAX(CASE A.COLUMN_POSITION WHEN  6 THEN COLUMN_NAME ELSE NULL END)||','
                      ||MAX(CASE A.COLUMN_POSITION WHEN  7 THEN COLUMN_NAME ELSE NULL END)||','
                      ||MAX(CASE A.COLUMN_POSITION WHEN  8 THEN COLUMN_NAME ELSE NULL END)||','
                      ||MAX(CASE A.COLUMN_POSITION WHEN  9 THEN COLUMN_NAME ELSE NULL END)||','
                      ||MAX(CASE A.COLUMN_POSITION WHEN 10 THEN COLUMN_NAME ELSE NULL END)||',) TABLESPACE INDEX1;' I1
                      ,MAX(A.TABLE_NAME) TABLE_NAME
                      ,A.INDEX_NAME  INDEX_NAME
                      --,MAX(COLUMN_POSITION)
               FROM  (SELECT A.TABLE_NAME,A.COLUMN_NAME,A.INDEX_NAME,A.COLUMN_POSITION,B.UNIQUENESS,A.TABLE_OWNER
                      FROM   ALL_IND_COLUMN A,
                             ALL_INDEX B
                      WHERE A.TABLE_OWNER = 'INST1' AND B.TABLE_OWNER = 'INST1'
                      AND   A.TABLE_NAME LIKE 'TSKGH%'
                      AND   A.TABLE_NAME IN (SELECT REFERENCED_NAME FROM INFU01 WHERE REFERENCED_NAME LIKE 'TSK%')
                      AND   A.INDEX_NAME = B.INDEX_NAME
                      ORDER BY A.INDEX_NAME,A.COLUMN_POSITION) A
               GROUP BY A.INDEX_NAME
               )
       )

------------
-- ANALYZE
------------
SELECT 'ANALYZE TABLE '||OBJECT_NAME||' COMPUTE STATISTICS;'
FROM ALL_OBJECTS
WHERE OBJECT_NAME LIKE 'TSK%'
ORDER BY 1


---------------------------------
-- ������ ����
---------------------------------

SELECT 'CREATE SEQUENCE '||SEQUENCE_OWNER||'.'||SEQUENCE_NAME||' INCREMENT BY '||INCREMENT_BY||' START WITH '||LAST_NUMBER||' MAXVALUE '||MAX_VALUE||' MINVALUE '||MIN_VALUE||' NOCYCLE CACHE 2 NOORDER;'     FROM ALL_SEQUENCES WHERE SEQUENCE_OWNER = 'INST1';

SELECT 'GRANT ALL ON  '||SEQUENCE_OWNER||'.'||SEQUENCE_NAME||' TO KGH001;' FROM ALL_SEQUENCES WHERE SEQUENCE_OWNER = 'INST1';


SELECT * FROM ALL_SEQUENCES WHERE SEQUENCE_OWNER = 'INST1';

CREATE SEQUENCE INST1.SQ_KGHFD01_SEQ INCREMENT BY 1 START WITH 10000000005653 MAXVALUE 99999999999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER ;



CREATE SEQUENCE INST1.SQ_KGHFD01_SEQ INCREMENT BY 1 START WITH 10000000965021 MAXVALUE 99999999999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHFD04_SEQ INCREMENT BY 1 START WITH 10000000000001 MAXVALUE 99999999999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHFD05_SEQ INCREMENT BY 1 START WITH 10000000001299 MAXVALUE 99999999999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHFD06_SEQ INCREMENT BY 1 START WITH 10000000004394 MAXVALUE 99999999999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHFE01_SEQ INCREMENT BY 1 START WITH 716101 MAXVALUE 999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHFE08_SEQ INCREMENT BY 1 START WITH 149971 MAXVALUE 999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHFE15_SEQ INCREMENT BY 1 START WITH 10000000000902 MAXVALUE 99999999999999 MINVALUE 10000000000001 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHFE16_SEQ INCREMENT BY 1 START WITH 10000000000747 MAXVALUE 99999999999999 MINVALUE 10000000000000 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHFE29_SEQ INCREMENT BY 1 START WITH 10000000001376 MAXVALUE 99999999999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHFE30_SEQ INCREMENT BY 1 START WITH 10000000001485 MAXVALUE 99999999999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHFF01_SEQ INCREMENT BY 1 START WITH 10959405 MAXVALUE 99999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSB10_SEQ INCREMENT BY 1 START WITH 1175903 MAXVALUE 9999999 MINVALUE 1000001 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSB21_SEQ INCREMENT BY 1 START WITH 111418794 MAXVALUE 999999999 MINVALUE 100000001 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSC03_SEQ INCREMENT BY 1 START WITH 1 MAXVALUE 99999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSC21_SEQ INCREMENT BY 1 START WITH 10002144 MAXVALUE 99999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSC90_SEQ INCREMENT BY 1 START WITH 7412303 MAXVALUE 9999999 MINVALUE 0 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSD21_SEQ INCREMENT BY 1 START WITH 197 MAXVALUE 999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSD22_SEQ INCREMENT BY 1 START WITH 91313 MAXVALUE 999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSD45_SEQ INCREMENT BY 1 START WITH 18425 MAXVALUE 99999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE03_SEQ INCREMENT BY 1 START WITH 1 MAXVALUE 999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE04_SEQ INCREMENT BY 1 START WITH 1 MAXVALUE 999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE05_SEQ INCREMENT BY 1 START WITH 1 MAXVALUE 999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE06_SEQ INCREMENT BY 1 START WITH 1 MAXVALUE 999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE13_SEQ INCREMENT BY 1 START WITH 1 MAXVALUE 999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE14_SEQ INCREMENT BY 1 START WITH 1 MAXVALUE 999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE15_SEQ INCREMENT BY 1 START WITH 1 MAXVALUE 999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE23_SEQ INCREMENT BY 1 START WITH 1 MAXVALUE 999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE32_SEQ INCREMENT BY 1 START WITH 65 MAXVALUE 999999999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE34_SEQ INCREMENT BY 1 START WITH 1 MAXVALUE 999999999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE36_SEQ INCREMENT BY 1 START WITH 43 MAXVALUE 999999999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE37_SEQ INCREMENT BY 1 START WITH 1 MAXVALUE 999999999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE38_SEQ INCREMENT BY 1 START WITH 19 MAXVALUE 99999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;
CREATE SEQUENCE INST1.SQ_KGHSE71_SEQ INCREMENT BY 1 START WITH 15 MAXVALUE 9999999 MINVALUE 1 NOCYCLE CACHE 2 NOORDER;

GRANT ALL ON  INST1.SQ_KGHFD01_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHFD04_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHFD05_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHFD06_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHFE01_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHFE08_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHFE15_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHFE16_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHFE29_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHFE30_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHFF01_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSB10_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSB21_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSC03_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSC21_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSC90_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSD21_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSD22_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSD45_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE03_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE04_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE05_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE06_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE13_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE14_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE15_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE23_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE32_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE34_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE36_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE37_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE38_SEQ TO KGH001;
GRANT ALL ON  INST1.SQ_KGHSE71_SEQ TO KGH001;


-----------------------
-- func PROC ����
-----------------------
SELECT 'DROP '||OBJECT_TYPE||' '||OBJECT_NAME||';'
FROM ALL_OBJECTS
WHERE (OBJECT_NAME LIKE 'PC_KGH%'
OR OBJECT_NAME LIKE 'FT_KGH%'
OR OBJECT_NAME LIKE 'PK_KGH%'
)
AND OBJECT_TYPE NOT IN ('PACKAGE BODY')

-----------------------
-- func ����
-----------------------
sqlplus KGH001/ybk34910@CTR

sqlplus KGH001/ybk34910@CTR @etc.bat

d:\d\Documents\��������\�ҽ�\sqlplus KGH001/ybk34910

-- etc.bat
@d:\d\Documents\��������\�ҽ�\sp\bak\201206290912\PK_KGH_USER.sql;
@d:\d\Documents\��������\�ҽ�\sp\bak\201206290912\PK_KGH_USER_TYPE.sql;
exit;

-----------------------
-- log
-----------------------
cd /cygdrive/d/oracle_xp/log_file
tail -f PC_KGH_CP_JL_00.20120629

-----------------------
-- ���
-----------------------
exp system/manager1 file=d:/dump1.dmp direct=y buffer=10240000 grants=y compress=n constraints=y indexes=y rows=y triggers=n owner=INST1 feedback=10000 log=d:/dump1.log
--imp system/manager1 file=d:/dump1.dmp fromuser=INST1 touser=KGH001 commit=y ignore=y buffer=10240000 grants=y constraints=y indexes=y rows=y feedback=10000 log=d:/dump1_imp.log
imp system/manager1 file=d:/dump1.dmp fromuser=INST1 touser=INST1 commit=y ignore=y buffer=10240000 grants=y constraints=y indexes=y rows=y feedback=10000 log=d:/dump1_imp.log

exp system/manager1 file=d:/dump1.dmp direct=y buffer=10240000 grants=y compress=n constraints=y indexes=y rows=y triggers=n tables=TSKGHFD01_T20120416_09,TSKGHFE01_T20120416_09,TSKGHFF09_T20120416_09,TSKGHFF08_T20120416_09,TSKGHFF01_T20120416_09,TSKGHFC05_T20120416_09,TSKGHFB01_T20120416_09,TSKGHFE01_T20120404_15,TSKGHFD01_T20120404_15,TSKGHFF09_T20120404_15,TSKGHFF08_T20120404_15,TSKGHFF01_T20120404_15,TSKGHFB01_T20120404_15,TSKGHFC05_T20120404_15,TSKGHFE01_T20120406_09,TSKGHFD01_T20120406_09,TSKGHFF09_T20120406_09,TSKGHFF08_T20120406_09,TSKGHFF01_T20120406_09,TSKGHFC05_T20120406_09,TSKGHFB01_T20120406_09,TSKGHFD01_T20120409_17,TSKGHFE01_T20120409_17,TSKGHFF09_T20120409_17,TSKGHFF08_T20120409_17,TSKGHFF01_T20120409_17,TSKGHFC05_T20120409_17,TSKGHFB01_T20120409_17 feedback=10000 log=d:/dump1.log
imp system/manager1 file=d:/dump1.dmp fromuser=system touser=system commit=y ignore=y buffer=10240000 grants=y constraints=y indexes=y rows=y tables=TSKGHFD01_T20120416_09,TSKGHFE01_T20120416_09,TSKGHFF09_T20120416_09,TSKGHFF08_T20120416_09,TSKGHFF01_T20120416_09,TSKGHFC05_T20120416_09,TSKGHFB01_T20120416_09,TSKGHFE01_T20120404_15,TSKGHFD01_T20120404_15,TSKGHFF09_T20120404_15,TSKGHFF08_T20120404_15,TSKGHFF01_T20120404_15,TSKGHFB01_T20120404_15,TSKGHFC05_T20120404_15,TSKGHFE01_T20120406_09,TSKGHFD01_T20120406_09,TSKGHFF09_T20120406_09,TSKGHFF08_T20120406_09,TSKGHFF01_T20120406_09,TSKGHFC05_T20120406_09,TSKGHFB01_T20120406_09,TSKGHFD01_T20120409_17,TSKGHFE01_T20120409_17,TSKGHFF09_T20120409_17,TSKGHFF08_T20120409_17,TSKGHFF01_T20120409_17,TSKGHFC05_T20120409_17,TSKGHFB01_T20120409_17 feedback=10000 log=d:/dump1_imp.log


-----------------------
-- DB LINK
-----------------------
PUBLIC  KGHPKG.REGRESS.RDBMS.DEV.US.ORACLE.COM  KGHPKG  DSKGHT01        2012. 2. 14 ���� 2:18:44
PUBLIC  KGH001.REGRESS.RDBMS.DEV.US.ORACLE.COM  KGH001  DSKGHT01        2012. 2. 14 ���� 2:18:54


SELECT * FROM ALL_DB_LINKS;

DROP PUBLIC DATABASE LINK DB04;


CREATE PUBLIC DATABASE LINK KGHPKG
CONNECT TO KGHPKG
IDENTIFIED BY olA89656
USING 'DSKGHT01'

CREATE PUBLIC DATABASE LINK KGH001
CONNECT TO KGH001
IDENTIFIED BY ybk34910
USING 'DSKGHT01'

CREATE PUBLIC DATABASE LINK PROD_DSKGHA01_SEL
CONNECT TO KGH000Y
IDENTIFIED BY ebP62078
USING 'DSKGHA01'

CREATE PUBLIC DATABASE LINK DSKGHA01
CONNECT TO KGH000Y
IDENTIFIED BY ebP62078
USING 'DSKGHA01'

CREATE PUBLIC DATABASE LINK STOCKTS2
CONNECT TO CAT000
IDENTIFIED BY CAT000
USING 'STOCKTS2'


DROP PUBLIC DATABASE LINK PROD_DSKGHA01_SEL;


CREATE PUBLIC DATABASE LINK PROD_DSKGHA01_SEL
CONNECT TO S004941
IDENTIFIED BY luT89693
USING 'DSKGHA01'


DROP PUBLIC DATABASE LINK DSKGHA01;


CREATE PUBLIC DATABASE LINK DSKGHA01
CONNECT TO S004941
IDENTIFIED BY luT89693
USING 'DSKGHA01'

-----------------------
-- ���� ��ġ
-----------------------

2012.07.04 01��

PK_KGH_GV �Ʒ����� ���� ��ġ(PC_KGH_CP_LOG_FILE I_���ϸ� NULL ���� ����)
    �α����ϸ�        VARCHAR(50):= 'PSH';
    �α�����          VARCHAR(100):= 'PSH';

2012.07.04 02�� ���������� INVALID ������Ʈ �� ������ ����
