package de.arboretum.sql.select;

import de.arboretum.sql.QueryTokens;
import de.arboretum.sql.SqlQueryUtils;

import java.util.Collections;

public class EqualsCrc32 extends ColumnCondition {

    private final String column;
    private final Object value;

    EqualsCrc32(final String name, final Object value) {
        column = name;
        this.value = value;
    }

    @Override
    public QueryTokens toQueryTokens() {

        final StringBuilder sb = new StringBuilder(SqlQueryUtils.tickName(column))
                .append(" = CRC32(?)");
        return QueryTokens.of(sb.toString(), Collections.singletonList(value));
    }
}
