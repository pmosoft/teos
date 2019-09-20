package com.sccomz.java.comm.db;


public class TabInfo {

    //-------------------------------
    // JDBC    
    //-------------------------------
    JdbcInfo jdbcInfo = new JdbcInfo();

    //-------------------------------
    // TABLE    
    //-------------------------------
    String stsNm        = "";
    String jdbcNm       = "";
    String owner        = "";
    String tabNm        = "";
    String tabHnm       = "";
    int    colId        =  0;
    String colNm        = "";
    String colHnm       = "";
    String dataTypeDesc = "";
    String nullable     = "";
    String pk           = "";
    String dataTypeNm   = "";
    int    len          =  0;
    int    decimalCnt   =  0;
    long   tabRows      =  0;
    String tabRegDt     = "";
    String tabRegDt2    = "";
    String tabUpdDt     = "";
    String tabUpdDt2    = "";
    String regDtm       = "";
    String regUsrId     = "";
    String updDtm       = "";
    String updUsrId     = "";

    //-------------------------------
    // TABLE 조회 조건    
    //-------------------------------
    boolean chk          = false;
                         
    boolean chkSelect    = false;
    String txtSelect     = "";
    
    boolean chkWhere     = false;
    String txtWhere      = "";
    String whereColTab   = "";
    // in 조건문의 params
    boolean chkWhereTabs = false;
    String whereTabs     = "";
    String whereInTabs   = "";

    String orderBy       = "";
    String ascDesc       = "";

    //-------------------------------
    // 동적쿼리 조건    
    //-------------------------------
    String qry           = "";
    String cntQry        = "";
    boolean isChgDate    = false;

    //-------------------------------
    // 데이터 추출 조건    
    //-------------------------------
    String pathFileNm    = "";
    int limitCnt = 50000;
    
    //-------------------------------
    // 테이블생성쿼리    
    //-------------------------------
    String tarDb         = "";
    String tarJdbcNm     = "";

    boolean isExtract = true;
    boolean isLoad = true;
    
    
    public JdbcInfo getJdbcInfo() {
        return jdbcInfo;
    }
    public void setJdbcInfo(JdbcInfo jdbcInfo) {
        this.jdbcInfo = jdbcInfo;
    }
    public String getStsNm() {
        return stsNm;
    }
    public void setStsNm(String stsNm) {
        this.stsNm = stsNm;
    }
    public String getJdbcNm() {
        return jdbcNm;
    }
    public void setJdbcNm(String jdbcNm) {
        this.jdbcNm = jdbcNm;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getTabNm() {
        return tabNm;
    }
    public void setTabNm(String tabNm) {
        this.tabNm = tabNm;
    }
    public String getTabHnm() {
        return tabHnm;
    }
    public void setTabHnm(String tabHnm) {
        this.tabHnm = tabHnm;
    }
    public int getColId() {
        return colId;
    }
    public void setColId(int colId) {
        this.colId = colId;
    }
    public String getColNm() {
        return colNm;
    }
    public void setColNm(String colNm) {
        this.colNm = colNm;
    }
    public String getColHnm() {
        return colHnm;
    }
    public void setColHnm(String colHnm) {
        this.colHnm = colHnm;
    }
    public String getDataTypeDesc() {
        return dataTypeDesc;
    }
    public void setDataTypeDesc(String dataTypeDesc) {
        this.dataTypeDesc = dataTypeDesc;
    }
    public String getNullable() {
        return nullable;
    }
    public void setNullable(String nullable) {
        this.nullable = nullable;
    }
    public String getPk() {
        return pk;
    }
    public void setPk(String pk) {
        this.pk = pk;
    }
    public String getDataTypeNm() {
        return dataTypeNm;
    }
    public void setDataTypeNm(String dataTypeNm) {
        this.dataTypeNm = dataTypeNm;
    }
    public int getLen() {
        return len;
    }
    public void setLen(int len) {
        this.len = len;
    }
    public int getDecimalCnt() {
        return decimalCnt;
    }
    public void setDecimalCnt(int decimalCnt) {
        this.decimalCnt = decimalCnt;
    }
    public long getTabRows() {
        return tabRows;
    }
    public void setTabRows(long tabRows) {
        this.tabRows = tabRows;
    }
    public String getTabRegDt() {
        return tabRegDt;
    }
    public void setTabRegDt(String tabRegDt) {
        this.tabRegDt = tabRegDt;
    }
    public String getTabRegDt2() {
        return tabRegDt2;
    }
    public void setTabRegDt2(String tabRegDt2) {
        this.tabRegDt2 = tabRegDt2;
    }
    public String getTabUpdDt() {
        return tabUpdDt;
    }
    public void setTabUpdDt(String tabUpdDt) {
        this.tabUpdDt = tabUpdDt;
    }
    public String getTabUpdDt2() {
        return tabUpdDt2;
    }
    public void setTabUpdDt2(String tabUpdDt2) {
        this.tabUpdDt2 = tabUpdDt2;
    }
    public String getRegDtm() {
        return regDtm;
    }
    public void setRegDtm(String regDtm) {
        this.regDtm = regDtm;
    }
    public String getRegUsrId() {
        return regUsrId;
    }
    public void setRegUsrId(String regUsrId) {
        this.regUsrId = regUsrId;
    }
    public String getUpdDtm() {
        return updDtm;
    }
    public void setUpdDtm(String updDtm) {
        this.updDtm = updDtm;
    }
    public String getUpdUsrId() {
        return updUsrId;
    }
    public void setUpdUsrId(String updUsrId) {
        this.updUsrId = updUsrId;
    }
    public boolean isChk() {
        return chk;
    }
    public void setChk(boolean chk) {
        this.chk = chk;
    }
    public boolean isChkSelect() {
        return chkSelect;
    }
    public void setChkSelect(boolean chkSelect) {
        this.chkSelect = chkSelect;
    }
    public String getTxtSelect() {
        return txtSelect;
    }
    public void setTxtSelect(String txtSelect) {
        this.txtSelect = txtSelect;
    }
    public boolean isChkWhere() {
        return chkWhere;
    }
    public void setChkWhere(boolean chkWhere) {
        this.chkWhere = chkWhere;
    }
    public String getTxtWhere() {
        return txtWhere;
    }
    public void setTxtWhere(String txtWhere) {
        this.txtWhere = txtWhere;
    }
    public String getWhereColTab() {
        return whereColTab;
    }
    public void setWhereColTab(String whereColTab) {
        this.whereColTab = whereColTab;
    }
    public boolean isChkWhereTabs() {
        return chkWhereTabs;
    }
    public void setChkWhereTabs(boolean chkWhereTabs) {
        this.chkWhereTabs = chkWhereTabs;
    }
    public String getWhereTabs() {
        return whereTabs;
    }
    public void setWhereTabs(String whereTabs) {
        this.whereTabs = whereTabs;
    }
    public String getWhereInTabs() {
        return whereInTabs;
    }
    public void setWhereInTabs(String whereInTabs) {
        this.whereInTabs = whereInTabs;
    }
    public String getOrderBy() {
        return orderBy;
    }
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    public String getAscDesc() {
        return ascDesc;
    }
    public void setAscDesc(String ascDesc) {
        this.ascDesc = ascDesc;
    }
    public String getQry() {
        return qry;
    }
    public void setQry(String qry) {
        this.qry = qry;
    }
    public String getCntQry() {
        return cntQry;
    }
    public void setCntQry(String cntQry) {
        this.cntQry = cntQry;
    }
    public String getPathFileNm() {
        return pathFileNm;
    }
    public void setPathFileNm(String pathFileNm) {
        this.pathFileNm = pathFileNm;
    }
    public int getLimitCnt() {
        return limitCnt;
    }
    public void setLimitCnt(int limitCnt) {
        this.limitCnt = limitCnt;
    }
    public boolean isChgDate() {
        return isChgDate;
    }
    public void setChgDate(boolean isChgDate) {
        this.isChgDate = isChgDate;
    }
    public String getTarDb() {
        return tarDb;
    }
    public void setTarDb(String tarDb) {
        this.tarDb = tarDb;
    }
    public String getTarJdbcNm() {
        return tarJdbcNm;
    }
    public void setTarJdbcNm(String tarJdbcNm) {
        this.tarJdbcNm = tarJdbcNm;
    }
    public boolean isExtract() {
        return isExtract;
    }
    public void setExtract(boolean isExtract) {
        this.isExtract = isExtract;
    }
    public boolean isLoad() {
        return isLoad;
    }
    public void setLoad(boolean isLoad) {
        this.isLoad = isLoad;
    }   
    
}
