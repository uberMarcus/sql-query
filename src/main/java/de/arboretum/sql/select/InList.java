package de.arboretum.sql.select;

import de.arboretum.sql.QueryTokens;
import de.arboretum.sql.SqlQueryUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InList extends ColumnCondition {

    private final String column;
    private final Set<Object> values = new TreeSet<>();

    InList(final String name, final Collection<Object> values) {
        column = name;
        this.values.addAll(values);
    }

    @Override
    public QueryTokens toQueryTokens() {

        final StringBuilder sb = new StringBuilder(SqlQueryUtils.tickName(column))
                .append(" IN ").append("(").append(values.stream().map(v -> "?")
                        .collect(Collectors.joining(", ")))
                .append(")");
        return QueryTokens.of(sb.toString(), new ArrayList<>(values));
    }
}
