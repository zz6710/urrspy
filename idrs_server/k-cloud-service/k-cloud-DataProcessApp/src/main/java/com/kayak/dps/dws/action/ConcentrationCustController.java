package com.kayak.dps.dws.action;

import com.kayak.core.action.BaseController;
import com.kayak.dps.dws.service.ConcentrationCustService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @className: ConcentrationCustController
 * @description: 客户集中度 控制层
 * @author: lc-renxw
 * @date: 2024/10/28 9:24
 * @version: 1.0
 */
@RequiredArgsConstructor
@RestController
public class ConcentrationCustController extends BaseController {

    private final ConcentrationCustService concentrationCustService;

    /**
    * @methodName upload
    * @description 客户集中度排序上传
    * @param file 上产文件
    * @param acdDt 时间
    * @return String
    */
    @PostMapping(path = "/concentration/excelUpload.json",produces = {"application/json;charset=UTF-8"})
    public String upload(@RequestParam("file")MultipartFile file,@RequestParam("dealDate")String acdDt) throws Exception {
        concentrationCustService.uploadData(file,acdDt);
        return updateSuccess("导入成功");
    }


}
