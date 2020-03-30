package de.arboretum.sql;

import de.arboretum.sql.select.SelectQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SelectQueryTest {

    @Test
    public void testSelectAll() {

        final String table = "example_table_001";

        final SelectQuery selectQuery = SelectQuery.from(table)
                .selectAll()
                .buildSelect();

        final String query = selectQuery.toQueryTokens().getQuery();
        final List<Object> values = selectQuery.toQueryTokens().getQueryValues();

        assertEquals("SELECT * FROM `example_table_001`", query);
        assertEquals(0, values.size());
    }

    @Test
    public void testSelectDistinct() {

        final String table = "example_table_001";
        final String firstColumn = "column_001";
        final String secondColumn = "column_002";

        final SelectQuery selectQuery = SelectQuery.from(table)
                .distinct()
                .selectColumn(firstColumn)
                .selectColumn(secondColumn)
                .buildSelect();

        final String query = selectQuery.toQueryTokens().getQuery();
        final List<Object> values = selectQuery.toQueryTokens().getQueryValues();

        assertEquals("SELECT DISTINCT `column_001`,`column_002` FROM `example_table_001`", query);
        assertEquals(0, values.size());
    }

    @Test
    public void testIllegalCharacters() {

        try {
            SelectQuery.from("Bobby; DROP TABLES;");
            fail("previous method should have thrown an exception");
        } catch (final IllegalArgumentException e) {
            assertEquals("'Bobby; DROP TABLES;'  contains illegal characters", e.getMessage());
        }
    }

    @Test
    public void testAvgQuery() {

        final String table = "example_table_001";
        final String firstColumn = "column_001";

        final SelectQuery selectQuery = SelectQuery.from(table)
                .selectAvg(firstColumn).as("avg_column_001")
                .buildSelect();

        final String query = selectQuery.toQueryTokens().getQuery();
        final List<Object> values = selectQuery.toQueryTokens().getQueryValues();

        assertEquals("SELECT avg(`column_001`) as `avg_column_001` FROM `example_table_001`", query);
        assertEquals(0, values.size());
    }

    @Test
    public void testSumQuery() {

        final String table = "example_table_001";
        final String firstColumn = "column_001";

        final SelectQuery selectQuery = SelectQuery.from(table)
                .selectSum(firstColumn).as("sum_column_001")
                .buildSelect();

        final String query = selectQuery.toQueryTokens().getQuery();
        final List<Object> values = selectQuery.toQueryTokens().getQueryValues();

        assertEquals("SELECT sum(`column_001`) as `sum_column_001` FROM `example_table_001`", query);
        assertEquals(0, values.size());
    }

    @Test
    public void testWhereWithId() {

        final String table = "example_table_001";
        final String firstColumn = "column_001";
        final String secondColumn = "column_002";

        final SelectQuery selectQuery = SelectQuery.from(table)
                .selectColumn(firstColumn)
                .selectColumn(secondColumn)
                .where()
                .column(firstColumn).valueIsEqualTo(1)
                .or()
                .column(secondColumn).valueIsEqualTo("test")
                .buildWhere()
                .limit(1)
                .buildSelect();

        final String query = selectQuery.toQueryTokens().getQuery();
        final List<Object> values = selectQuery.toQueryTokens().getQueryValues();

        assertEquals("SELECT `column_001`,`column_002` FROM `example_table_001` " +
                "WHERE `column_001` = ? OR `column_002` = ? LIMIT ?", query);
        assertEquals(3, values.size());
        assertEquals(1, selectQuery.toQueryTokens().getQueryValues().get(0));
        assertEquals("test", selectQuery.toQueryTokens().getQueryValues().get(1));
        assertEquals(1, selectQuery.toQueryTokens().getQueryValues().get(2));
    }

}
