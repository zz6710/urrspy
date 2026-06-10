package com.kayak.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PrometheusConfigration {

  @Value("${spring.application.name}")
  private String applicationName;

//  @Bean
//  MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
////	  executeBatchJob();
//    return registry -> registry.config().commonTags("applicationName", applicationName);
//  }
//
//  void executeBatchJob()  {
//	     CollectorRegistry registry = new CollectorRegistry();
//	     Gauge duration = Gauge.build()
//	         .name("my_batch_job_duration_seconds").help("Duration of my batch job in seconds.").register(registry);
//	     Gauge.Timer durationTimer = duration.startTimer();
//	     try {
//
//	       Gauge lastSuccess = Gauge.build()
//	           .name("my_batch_job_last_success").help("Last time my batch job succeeded, in unixtime.").register(registry);
//	       lastSuccess.setToCurrentTime();
//	     } finally {
//	       durationTimer.setDuration();
//	       PushGateway pg = new PushGateway("10.1.20.88:9091");
//	       try {
//			pg.pushAdd(registry, "applicationName");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.error("PushGateway add Error{}",e);
//		}
//	     }
//	   }

}
