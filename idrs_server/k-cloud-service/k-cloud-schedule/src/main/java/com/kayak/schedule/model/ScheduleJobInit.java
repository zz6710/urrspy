package com.kayak.schedule.model;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.kayak.schedule.biz.QuartzBiz;
import org.springframework.stereotype.Component;

/**
 * 项目启动时初始化定时任务
 * Title:  ast_web
 * Description:  描述
 * Company:	kayak
 * Makedate: 2015-12-23 下午4:28:21
 *
 * @author lixiao
 */

@Component
public class ScheduleJobInit {

    @Resource(name = "quartzBiz")
    private QuartzBiz quartzBiz;

    @PostConstruct
    public void jobInit() {
        try {
            quartzBiz.jobInit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
