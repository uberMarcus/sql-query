package de.arboretum.sql.select;

import de.arboretum.sql.QueryTokens;
import de.arboretum.sql.ResultSetConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SelectQueryExecutor {

    private static final Logger LOGGER = LogManager.getLogger(SelectQueryExecutor.class);
    private final DataSource dataSource;
    private final QueryTokens queryTokens;

    SelectQueryExecutor(final DataSource dataSource, final QueryTokens queryTokens) {
        this.dataSource = dataSource;
        this.queryTokens = queryTokens;
    }

    public <T> Collection<T> execute(final ResultSetConverter<T> resultSetConverter) {

        final List<T> result = new LinkedList<>();

        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement ps = connection.prepareStatement(queryTokens.getQuery())) {

            int index = 1;
            for (final Object o : queryTokens.getQueryValues()) {
                ps.setObject(index++, o);
            }

            try (final ResultSet rs = ps.executeQuery()) {
                while (!rs.isClosed() && rs.next()) {
                    result.add(resultSetConverter.convert(rs));
                }
            }

        } catch (final SQLException e) {
            LOGGER.error("exception reading result for query: {}", queryTokens, e);
        }
        return result;
    }
}
