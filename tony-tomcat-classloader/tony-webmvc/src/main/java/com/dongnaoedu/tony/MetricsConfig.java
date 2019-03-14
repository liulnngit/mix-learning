package com.dongnaoedu.tony;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricRegistry.MetricSupplier;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.dongnaoedu.tony.utils.TomcatUtils;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurer;

import metrics_influxdb.InfluxdbReporter;
import metrics_influxdb.api.protocols.InfluxdbProtocols;

@Configuration
@EnableMetrics // 启用度量器功能,支持注解
public class MetricsConfig implements MetricsConfigurer {

	@Override
	public MetricRegistry getMetricRegistry() {
		// 度量器的容器
		return new MetricRegistry();
	}

	@Override
	public void configureReporters(MetricRegistry metricRegistry) {
		// 将结果输出到console
		ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.SECONDS).build();
		// 三秒输出一次
		consoleReporter.start(3, TimeUnit.SECONDS);

		// 将结果输出到 influx数据库进行存储
		ScheduledReporter scheduledReporter = InfluxdbReporter.forRegistry(metricRegistry)
				// 指定数据库地址，用户名和密码
				.protocol(InfluxdbProtocols.http("60.205.209.106", 8086, "root", "root", "mydb"))
				.convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.SECONDS).skipIdleMetrics(false)
				// 指定数据来自哪个机器
				.tag("host", "tony_1").build();
		scheduledReporter.start(5, TimeUnit.SECONDS);
	}

	@Override
	public HealthCheckRegistry getHealthCheckRegistry() {
		return null;
	}

	// 方法级别的监控，通过注解即可
	// 自定义监控或者是代码级别的监控，自定义metrics对象

	// 监控tomcat线程池- 正在处理的线程数量tomcatThreadNums
	// 类似的可以拓展出 内存监控，Queue队列监控，缓存大小监控等等场景
	@Bean
	public Gauge<Integer> tomcatThreadNums(MetricRegistry metricRegistry) {
		Gauge gauge = metricRegistry.gauge("tomcatThreadNums", new MetricSupplier<Gauge>() {

			@Override
			public Gauge newMetric() {
				return new Gauge<Integer>() {
					@Override
					public Integer getValue() {
						// 此处返回tomcat线程池中的线程数
						try {
							return TomcatUtils.getThreadNums();
						} catch (Exception e) {
							e.printStackTrace();
						}
						return 0;
					}
				};
			}
		});
		return gauge;
	}

	// 监控内存 - jvmMemmory
	@Bean
	public Gauge<Integer> jvmMemmory(MetricRegistry metricRegistry) {
		Gauge gauge = metricRegistry.gauge("jvmMemmory", new MetricSupplier<Gauge>() {

			@Override
			public Gauge newMetric() {
				return new Gauge<Long>() {
					@Override
					public Long getValue() {
						// 此处返回 jvm 占用内存
						Runtime run = Runtime.getRuntime();
						long used = run.totalMemory() - run.freeMemory();
						// 字节转兆
						long usedMem = used / 1024 / 1024;
						return usedMem;
					}
				};
			}
		});
		return gauge;
	}

}
