package com.kayak.clear.in;

import com.kayak.clear.req.P001Req;
import com.kayak.clear.service.P001Service;
import com.kayakwise.kcloud.batch.model.resp.BatchTaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 测试<br>
 *
 * @POST 表示创建资源<br>
 * @GET 表示获取资源<br>
 * @PUT 表示更新资源<br>
 * @DELETE 表示删除资源
 */

@RequestMapping("/P001")
@Scope("prototype")
@RestController
public class P001 {

	@Autowired
	private P001Service service;

	@RequestMapping(method = RequestMethod.POST)
	public BatchTaskResponse process(@RequestBody P001Req request) {
		return service.doProcess(request);
	}

}
