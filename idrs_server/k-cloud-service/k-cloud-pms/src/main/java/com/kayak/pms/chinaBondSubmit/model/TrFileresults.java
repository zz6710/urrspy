package com.kayak.pms.chinaBondSubmit.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "trFileresultsService",table = "app_zz_file_results")
public class TrFileresults {
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

}