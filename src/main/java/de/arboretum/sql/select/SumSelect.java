package de.arboretum.sql.select;

import de.arboretum.sql.QueryTokens;
import de.arboretum.sql.SqlQueryUtils;

public class SumSelect extends ColumnSelect {

    SumSelect(final String column) {
        super(column);
    }

    @Override
    public QueryTokens toQueryTokens() {
        return QueryTokens.of(new StringBuilder("sum(")
                .append(SqlQueryUtils.tickName(columnName))
                .append(")").toString());
    }
}
