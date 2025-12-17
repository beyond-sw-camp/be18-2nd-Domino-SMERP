package com.domino.smerp.logging.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(
    basePackages = "com.domino.smerp.logging.repository"
)
public class ElasticsearchConfig {
}