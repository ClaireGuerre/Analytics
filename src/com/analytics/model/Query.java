package com.analytics.model;

import java.util.Objects;

public class Query {

    private String content;

    public Query(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return Objects.equals(content, query.getContent());
    }

    @Override
    public int hashCode() {

        return Objects.hash(content);
    }
}
