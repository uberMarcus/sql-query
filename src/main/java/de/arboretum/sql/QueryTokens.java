package de.arboretum.sql;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class QueryTokens {

    private final String queryFragment;
    private final List<Object> queryValues;

    private QueryTokens(final String fragment, final List<Object> values) {
        queryFragment = Objects.requireNonNull(fragment, "query can not be null");
        queryValues = Collections.unmodifiableList(values);
    }

    public static QueryTokens of(final String query) {
        return new QueryTokens(query, Collections.emptyList());
    }

    public static QueryTokens of(final String query, final List<Object> values) {
        return new QueryTokens(query, values);
    }

    public List<Object> getQueryValues() {
        return queryValues;
    }

    public String getQuery() {
        return queryFragment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("queryFragment", queryFragment)
                .append("queryValues", queryValues)
                .toString();
    }
}
