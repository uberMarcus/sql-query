package de.arboretum.sql.select;

import de.arboretum.sql.QueryTokens;
import de.arboretum.sql.SqlQueryUtils;

public class SelectAsBuilder {

    private final SelectQuery.SelectQueryBuilder queryBuilder;
    private final QueryTokens previousSelect;

    SelectAsBuilder(final SelectQuery.SelectQueryBuilder queryBuilder, final QueryTokens previousSelect) {
        this.queryBuilder = queryBuilder;
        this.previousSelect = previousSelect;
    }

    public SelectQuery.SelectQueryBuilder as(final String name) {

        return queryBuilder.selectColumn(QueryTokens.of(new StringBuilder(previousSelect.getQuery())
                        .append(" as ").append(SqlQueryUtils.tickName(SqlQueryUtils.requiredValid(name))).toString(),
                previousSelect.getQueryValues()));

    }
}
