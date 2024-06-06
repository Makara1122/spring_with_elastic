package com.example.spring_elastic_search_user.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.spring_elastic_search_user.domain.User;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

public class ESUtils {


    public static Supplier<Query> createSupplierAutoSuggest(String patialUserName){
        Supplier<Query> query = () -> Query.of(q -> q.match(createAutoSuggestMatchQuery(patialUserName)));
        return query;
    }

    public static MatchQuery createAutoSuggestMatchQuery(String partialUserName) {
        val authoSuggestQuery = new MatchQuery.Builder();


        return authoSuggestQuery.
                field("name").
                query(partialUserName).
                analyzer("standard").
                build();
    }

    public static Supplier<Query> createSupplierAutoSuggest2(String patialUserName){
        Supplier<Query> query = () -> Query.of(q -> q.match(createAutoSuggestMatchQuery2(patialUserName)));
        return query;
    }

    public static MatchQuery createAutoSuggestMatchQuery2(String partialUserName) {
        val authoSuggestQuery = new MatchQuery.Builder();


        return authoSuggestQuery.
                field("email").
                query(partialUserName).
                analyzer("standard").
                build();
    }

    public static Supplier<Query> supplier() {
        Supplier<Query> supplier = () -> Query.of(s -> s.matchAll(matchAllQuery()));
        return supplier;
    }

    public static MatchAllQuery matchAllQuery() {
        val matchQuery = new MatchAllQuery.Builder();
        return matchQuery.build();
    }

    public static WildcardQuery wildcardQuery(String name, String wildCardPattern) {
        val wildCardQuery = new WildcardQuery.Builder();
        return wildCardQuery.field(name).wildcard(wildCardPattern).build();
    }

    public static Supplier<Query> wildcardSupplier(String fieldName,String wildCardPattern) {
        Supplier<Query> supplier = () -> Query.of(q -> q.wildcard(wildcardQuery(fieldName,wildCardPattern)));
        return  supplier;
    }

    public static Supplier<Query> queryAll(String query) {
        Supplier<Query> supplier = () -> Query.of(q -> q.multiMatch(createMultiMatchQuery(query)));
        return supplier;
    }
    // i want to query all by one string argument
    public static Query queryMatch(String query) {

        val query2 = new Query.Builder();

        return query2.build();
    }

    public static MultiMatchQuery createMultiMatchQuery(String query) {
        val multiMatchQuery = new MultiMatchQuery.Builder();
        return multiMatchQuery
                .query(query)
                .fields("name", "email", "address") // Add fields you want to search
                .build();
    }
}