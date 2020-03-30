package de.arboretum.sql.select;

import de.arboretum.sql.QueryTokens;
import de.arboretum.sql.SqlQueryUtils;

public class PlainColumnSelect extends ColumnSelect {

    PlainColumnSelect(final String columnName) {
        super(columnName);
    }

    @Override
    public QueryTokens toQueryTokens() {
        return QueryTokens.of(SqlQueryUtils.tickName(columnName));
    }
}
