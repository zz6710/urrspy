package ${package}.controller;

import com.kayak.core.action.BaseController;
import com.kayak.subject.service.${model}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ${model}Controller extends BaseController {

    @Autowired
    private ${model}Service ${lowHeadModel}Service;

    @RequestMapping(value = "/upload${model}.json", produces = {"application/json;charset=UTF-8"})
    public String upload${model}(@RequestParam(value = "file") MultipartFile file) {
        Map<String, Object> params = new HashMap<>();
        String message;
        try {
            message = ${lowHeadModel}Service.import${model}(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }

}
