package com.example.spring_elastic_search_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.example.spring_elastic_search_user.feature.elastic")
@EnableJpaRepositories(basePackages = "com.example.spring_elastic_search_user.feature.user")
public class SpringElasticSearchUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringElasticSearchUserApplication.class, args);
    }

}
