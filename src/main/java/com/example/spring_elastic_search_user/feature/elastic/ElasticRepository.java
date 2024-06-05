package com.example.spring_elastic_search_user.feature.elastic;

import com.example.spring_elastic_search_user.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticRepository extends ElasticsearchRepository<User,String> {
}
