package de.arboretum.sql.select;

import de.arboretum.sql.QueryTokens;
import de.arboretum.sql.SqlQueryUtils;
import de.arboretum.sql.ToQueryTokens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Where<T> implements ToQueryTokens {

    private final T query;
    private final List<ColumnCondition> andConditions = new ArrayList();
    private final List<ColumnCondition> orConditions = new ArrayList();

    private Consumer<ColumnCondition> currentConsumer;
    private boolean openBuilder;

    Where(final T query) {
        this.query = query;
    }

    public ColumnCondition.ColumnConditionBuilder<T> column(final String column) {

        if (openBuilder) {
            throw new IllegalStateException("last column condition was not closed.");
        }
        return new ColumnCondition.ColumnConditionBuilder(SqlQueryUtils.requiredValid(column), this);
    }

    @Override
    public QueryTokens toQueryTokens() {

        if (andConditions.isEmpty() && orConditions.isEmpty()) {
            return QueryTokens.of("", Collections.emptyList());
        }

        final StringBuilder sb = new StringBuilder();
        final List<Object> values = new ArrayList<>();

        sb.append(" WHERE ");
        if (!andConditions.isEmpty()) {

            final List<QueryTokens> andStatementValues = andConditions.stream()
                    .map(condition -> condition.toQueryTokens())
                    .collect(Collectors.toList());

            sb.append(andStatementValues.stream()
                    .map(statement -> statement.getQuery())
                    .collect(Collectors.joining(" AND ")));

            andStatementValues.stream()
                    .flatMap(statement -> statement.getQueryValues().stream())
                    .forEach(values::add);

            if (!orConditions.isEmpty()) {
                sb.append(" OR ");
            }
        }

        if (!orConditions.isEmpty()) {

            final List<QueryTokens> orStatementValues = orConditions.stream()
                    .map(condition -> condition.toQueryTokens())
                    .collect(Collectors.toList());

            sb.append(orStatementValues.stream()
                    .map(statement -> statement.getQuery())
                    .collect(Collectors.joining(" OR ")));

            orStatementValues.stream()
                    .flatMap(statement -> statement.getQueryValues().stream())
                    .forEach(values::add);
        }
        return QueryTokens.of(sb.toString(), values);
    }

    T build(final ColumnCondition condition) {

        if (condition == null) {
            throw new IllegalStateException("last condition builder was not closed");
        }

        if (currentConsumer != null) {
            currentConsumer.accept(condition);
        } else {
            andConditions.add(condition);
        }
        return query;
    }

    Where<T> or(final ColumnCondition condition) {

        if (currentConsumer != null) {
            currentConsumer.accept(condition);
        } else {
            orConditions.add(condition);
        }
        openBuilder = false;
        currentConsumer = orConditions::add;
        return this;
    }

    Where<T> and(final ColumnCondition condition) {

        if (currentConsumer != null) {
            currentConsumer.accept(condition);
        } else {
            andConditions.add(condition);
        }
        openBuilder = false;
        currentConsumer = andConditions::add;
        return this;
    }
}
