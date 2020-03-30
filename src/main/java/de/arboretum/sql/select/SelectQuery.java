package de.arboretum.sql.select;

import de.arboretum.sql.QueryTokens;
import de.arboretum.sql.SqlQueryUtils;
import de.arboretum.sql.ToQueryTokens;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectQuery implements ToQueryTokens {

    private final String tableName;
    private final List<QueryTokens> columns;
    private final boolean selectAll;
    private final boolean distinct;
    private final Where<?> where;
    private final int limit;

    private final QueryTokens queryTokens;

    private SelectQuery(final SelectQueryBuilder builder) {
        tableName = builder.tableName;
        columns = Collections.unmodifiableList(builder.columns);
        selectAll = builder.selectAll;
        distinct = builder.distinct;
        where = builder.where;
        limit = builder.limit;

        queryTokens = generate(this);
    }

    private static QueryTokens generate(final SelectQuery query) {

        final StringBuilder sb = new StringBuilder("SELECT");
        final List<Object> values = new ArrayList<>();

        if (query.distinct) {
            sb.append(" DISTINCT");
        }

        if (query.selectAll) {
            sb.append(" *");
        } else {
            final QueryTokens tokens = SqlQueryUtils.combine(query.columns);
            sb.append(" ").append(tokens.getQuery());
            values.addAll(tokens.getQueryValues());
        }
        sb.append(" FROM ").append(SqlQueryUtils.tickName(query.tableName));

        final QueryTokens whereStatementValues = query.where.toQueryTokens();
        sb.append(whereStatementValues.getQuery());
        values.addAll(whereStatementValues.getQueryValues());

        if (query.limit > -1) {
            sb.append(" LIMIT ?");
            values.add(query.limit);
        }
        return QueryTokens.of(sb.toString(), values);
    }

    public static SelectQueryBuilder from(final String tableName) {
        return new SelectQueryBuilder(SqlQueryUtils.requiredValid(tableName));
    }

    public SelectQueryExecutor fromDataSource(final DataSource source) {
        return new SelectQueryExecutor(source, toQueryTokens());
    }

    @Override
    public QueryTokens toQueryTokens() {
        return queryTokens;
    }

    public static class SelectQueryBuilder {

        private final String tableName;
        private final List<QueryTokens> columns = new ArrayList<>();
        private final Where<SelectQueryBuilder> where;
        private boolean selectAll = false;
        private boolean distinct = false;
        private int limit = -1;

        private SelectQueryBuilder(final String tableName) {
            this.tableName = tableName;
            where = new Where<>(this);
        }

        public SelectQueryBuilder selectAll() {
            selectAll = true;
            return this;
        }

        public Where<SelectQueryBuilder> where() {
            return where;
        }

        public SelectQueryBuilder selectColumn(final String column) {
            columns.add(new PlainColumnSelect(SqlQueryUtils.requiredValid(column)).toQueryTokens());
            return this;
        }

        public SelectAsBuilder selectAvg(final String column) {
            return new SelectAsBuilder(this, new AverageSelect(column).toQueryTokens());
        }

        public SelectQueryBuilder limit(final int val) {
            limit = val;
            return this;
        }

        public SelectAsBuilder selectSum(final String column) {
            return new SelectAsBuilder(this, new SumSelect(column).toQueryTokens());
        }


        SelectQueryBuilder selectColumn(final QueryTokens column) {
            columns.add(column);
            return this;
        }

        public SelectQueryBuilder distinct() {
            distinct = true;
            return this;
        }

        public SelectQuery buildSelect() {
            return new SelectQuery(this);
        }
    }
}
