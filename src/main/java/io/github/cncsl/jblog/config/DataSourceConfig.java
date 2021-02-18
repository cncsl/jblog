package io.github.cncsl.jblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 数据源配置
 *
 * @author cncsl
 */
@Configuration
@EnableJpaRepositories(basePackages = "io.github.cncsl.jblog.repository")
public class DataSourceConfig {

}
