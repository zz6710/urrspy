package com.kayak.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author yuanjinqiao
 * @description 监听项目启动后
 * @create 2022-10-11 10:58
 **/
@Component
@Slf4j
public class ApplicationRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("flowable工作流已启动");
    }
}
