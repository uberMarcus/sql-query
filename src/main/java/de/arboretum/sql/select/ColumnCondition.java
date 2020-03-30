package de.arboretum.sql.select;

import de.arboretum.sql.ToQueryTokens;

import java.util.Arrays;

public abstract class ColumnCondition implements ToQueryTokens {

    private static final String FORWARD_TICK = "`";

    protected ColumnCondition() {
        /* protected constructor */
    }

    public static class ColumnConditionBuilder<T> {

        private final String column;
        private final Where<T> where;
        private ColumnCondition condition;

        ColumnConditionBuilder(final String column, final Where<T> where) {
            this.column = column;
            this.where = where;
        }

        public ColumnConditionBuilder<T> valueIsEqualTo(final String value) {
            return valueIsEqualToObject(value);
        }

        public ColumnConditionBuilder<T> valueIsEqualTo(final int value) {
            return valueIsEqualToObject(value);
        }

        public ColumnConditionBuilder<T> valueIsLike(final String value) {
            condition = new Like(column, value);
            return this;
        }

        public ColumnConditionBuilder<T> valueIsInList(final Object... objects) {
            condition = new InList(column, Arrays.asList(objects));
            return this;
        }

        public ColumnConditionBuilder<T> valueIsEqualToCrc32of(final String value) {
            condition = new EqualsCrc32(column, value);
            return this;
        }

        public ColumnConditionBuilder<T> valueIsEqualToBinaryOf(final String value) {
            condition = new EqualsBinary(column, value);
            return this;
        }

        public T buildWhere() {
            return where.build(condition);
        }

        public Where<T> and() {
            return where.and(condition);
        }

        public Where<T> or() {
            return where.or(condition);
        }

        private ColumnConditionBuilder<T> valueIsEqualToObject(final Object value) {
            condition = new Equals(column, value);
            return this;
        }
    }
}
