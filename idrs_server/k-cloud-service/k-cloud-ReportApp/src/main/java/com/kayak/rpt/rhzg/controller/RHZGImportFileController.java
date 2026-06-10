package com.kayak.rpt.rhzg.controller;


import com.kayak.core.action.BaseController;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.component.ExcelComponent;
import com.kayak.rpt.rhzg.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/rhzg")
public class RHZGImportFileController extends BaseController {

    @Autowired
    private ExcelComponent excelComponent;

    @Autowired
    private ZG01Service zg01Service;
    @Autowired
    private ZG02Service zg02Service;
    @Autowired
    private ZG03Service zg03Service;
    @Autowired
    private ZG04Service zg04Service;
    @Autowired
    private ZG05Service zg05Service;
    @Autowired
    private ZG06Service zg06Service;
    @Autowired
    private ZG07Service zg07Service;
    @Autowired
    private ZG08Service zg08Service;
    @Autowired
    private ZG09Service zg09Service;
    @Autowired
    private ZG10Service zg10Service;
    @Autowired
    private ZG11Service zg11Service;


    @RequestMapping(value = "/uploadZG01.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG01(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
//        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        params.put("queryDate", request.getParameter("queryDate").replace("-", ""));
           params.put("createDate", DateUtil.getNowDate());
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG01")) {
//            return updateFailure("导入文件名必须是ZG01开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZg01(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }


    @RequestMapping(value = "/uploadZG02.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG02(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
//        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        params.put("queryDate", request.getParameter("queryDate").replace("-", ""));
           params.put("reportDate", request.getParameter("reportDate").replace("-", ""));
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG02")) {
//            return updateFailure("导入文件名必须是ZG02开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZg02(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }

    @RequestMapping(value = "/uploadZG03.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG03(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
//        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        params.put("queryDate", request.getParameter("queryDate").replace("-", ""));
        params.put("reportDate", request.getParameter("reportDate").replace("-", ""));
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG03")) {
//            return updateFailure("导入文件名必须是ZG03开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZg03(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }
    @RequestMapping(value = "/uploadZG04.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG04(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("reportDate", request.getParameter("reportDate").replace("-", ""));
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG04")) {
//            return updateFailure("导入文件名必须是ZG04开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZg04(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }



    @RequestMapping(value = "/uploadZG05.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG05(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG05")) {
//            return updateFailure("导入文件名必须是ZG05开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZg05(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }

    @RequestMapping(value = "/uploadZG06.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG06(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG06")) {
//            return updateFailure("导入文件名必须是ZG06开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZg06(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }
    @RequestMapping(value = "/uploadZG07.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG07(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG07")) {
//            return updateFailure("导入文件名必须是ZG07开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZg07(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }



    @RequestMapping(value = "/uploadZG08.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG08(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG08")) {
//            return updateFailure("导入文件名必须是ZG08开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZg08(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }

    @RequestMapping(value = "/uploadZG09.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG09(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG09")) {
//            return updateFailure("导入文件名必须是ZG09开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZg09(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }

    @RequestMapping(value = "/uploadZG10.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG10(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG10")) {
//            return updateFailure("导入文件名必须是ZG10开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZg10(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }

    @RequestMapping(value = "/uploadZG11.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG11(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG11")) {
//            return updateFailure("导入文件名必须是ZG11开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZg11(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }

    @RequestMapping(value = "/uploadZG12.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG12(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG12")) {
//            return updateFailure("导入文件名必须是ZG12开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZG12(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }
    @RequestMapping(value = "/uploadZG13.json", produces = {"application/json;charset=UTF-8"})
    public String uploadZG13(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("ZG13")) {
//            return updateFailure("导入文件名必须是ZG13开头！");
//        }
        String errMsg = "";

        try {
            errMsg = excelComponent.importZG13(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }


}
