package de.arboretum.sql.select;

import de.arboretum.sql.QueryTokens;
import de.arboretum.sql.SqlQueryUtils;

import java.util.Collections;

public class Like extends ColumnCondition {

    private final String column;
    private final Object value;

    Like(final String name, final String value) {
        column = name;
        this.value = value;
    }

    @Override
    public QueryTokens toQueryTokens() {

        final StringBuilder sb = new StringBuilder(SqlQueryUtils.tickName(column))
                .append(" LIKE ?");
        return QueryTokens.of(sb.toString(), Collections.singletonList(value));
    }
}
