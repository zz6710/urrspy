package com.kayak.rpt.zz.feedback.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "trFileresultsService",table = "app_zz_file_results")
public class TrFileresults {
    @GraphQLField(kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "文件类型", sql = "file_type = $S{fileType}" ,field = "file_type")
   private String fileType;
   @GraphQLField(kkhtml = "KFieldText", label = "文件名称", sql = "filename = $S{filename}" ,field = "filename")
   private String filename;
   @GraphQLField(kkhtml = "KFieldText", label = "序号", sql = "fileno = $S{fileno}" ,field = "fileno")
   private String fileno;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "错误信息", sql = "errormsg = $S{errormsg}" ,field = "errormsg")
   private String errormsg;
   @GraphQLField(kkhtml = "KFieldText", label = "错误码", sql = "errorcode = $S{errorcode}" ,field = "errorcode")
   private String errorcode;

    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;

    @GraphQLField(kkhtml = "KFieldText", label = "报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;
    @GraphQLField(kkhtml = "KFieldText", label = "表名", sql = "sys_table_name = $S{sysTableName}" ,field = "sys_table_name")
    private String sysTableName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getSysTableName() {
        return sysTableName;
    }

    public void setSysTableName(String sysTableName) {
        this.sysTableName = sysTableName;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
  	public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
  	public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
  	public String getFileno() {
        return fileno;
    }

    public void setFileno(String fileno) {
        this.fileno = fileno;
    }
  	public String getRegisterSerno() {
        return registerSerno;
    }

    public void setRegisterSerno(String registerSerno) {
        this.registerSerno = registerSerno;
    }
  	public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }
  	public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(String crtTime) {
        this.crtTime = crtTime;
    }

}