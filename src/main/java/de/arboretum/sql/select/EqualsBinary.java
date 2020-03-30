package de.arboretum.sql.select;

import de.arboretum.sql.QueryTokens;
import de.arboretum.sql.SqlQueryUtils;

import java.util.Collections;

public class EqualsBinary extends ColumnCondition {

    private final String column;
    private final Object value;

    EqualsBinary(final String name, final Object value) {
        column = name;
        this.value = value;
    }

    @Override
    public QueryTokens toQueryTokens() {

        final StringBuilder sb = new StringBuilder(SqlQueryUtils.tickName(column))
                .append(" = BINARY ?");
        return QueryTokens.of(sb.toString(), Collections.singletonList(value));
    }
}
