package com.kayak.system.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.util.Tools;
import com.kayak.system.dao.ServerModelDao;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerModelAction extends BaseController {

    @Autowired
    private ServerModelDao serverModelDao;

    @PostMapping("/base/ServerModel/getAppNames.json")
    public String getAppNames() throws Exception {
        return updateSuccess(serverModelDao.getAppNames());
    }

    @PostMapping("/base/ServerModel/getModelNames.json")
    public String getModelNames(@RequestBody String body) throws Exception {
        return updateSuccess(serverModelDao.getModelNames(Tools.obj2Str(Tools.json2map(new JSONObject(body)).get("appName"))));
    }
}
