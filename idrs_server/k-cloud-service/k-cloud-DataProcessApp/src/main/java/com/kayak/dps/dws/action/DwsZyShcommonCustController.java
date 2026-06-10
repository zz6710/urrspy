package com.kayak.dps.dws.action;

import com.kayak.core.action.BaseController;
import com.kayak.dps.dws.service.DwsZyShcommonCustService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @className: DwsZyShcommonCust
 * @description: 上海国际集团共同客户名录  dws 表 action
 */
@RestController
@RequiredArgsConstructor
public class DwsZyShcommonCustController extends BaseController {

    private final DwsZyShcommonCustService shcommonCustService;

    /**
     * @methodName upload
     * @description 客户集中度排序上传
     * @param file 上产文件
     * @return String
     */
    @PostMapping(path = "/shcommonCust/excelUpload.json",produces = {"application/json;charset=UTF-8"})
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        shcommonCustService.uploadData(file);
        return updateSuccess("导入成功");
    }

}
