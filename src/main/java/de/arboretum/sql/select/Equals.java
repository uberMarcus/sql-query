package de.arboretum.sql.select;

import de.arboretum.sql.QueryTokens;
import de.arboretum.sql.SqlQueryUtils;

import java.util.Collections;

public class Equals extends ColumnCondition {

    private final String column;
    private final Object value;

    Equals(final String name, final Object value) {
        column = name;
        this.value = value;
    }

    @Override
    public QueryTokens toQueryTokens() {

        final StringBuilder sb = new StringBuilder(SqlQueryUtils.tickName(column))
                .append(" = ?");
        return QueryTokens.of(sb.toString(), Collections.singletonList(value));
    }
}
