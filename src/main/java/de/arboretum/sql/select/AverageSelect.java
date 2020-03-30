package de.arboretum.sql.select;

import de.arboretum.sql.QueryTokens;
import de.arboretum.sql.SqlQueryUtils;

public class AverageSelect extends ColumnSelect {

    AverageSelect(final String column) {
        super(column);
    }

    @Override
    public QueryTokens toQueryTokens() {
        return QueryTokens.of(new StringBuilder("avg(")
                .append(SqlQueryUtils.tickName(columnName))
                .append(")").toString());
    }
}
