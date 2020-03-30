package de.arboretum.sql.select;

import de.arboretum.sql.SqlQueryUtils;
import de.arboretum.sql.ToQueryTokens;

public abstract class ColumnSelect implements ToQueryTokens {

    protected final String columnName;

    protected ColumnSelect(final String columnName) {
        this.columnName = SqlQueryUtils.requiredValid(columnName);
    }
}
