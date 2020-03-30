package de.arboretum.sql;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SqlQueryUtils {

    private static final Pattern VALID_CHARACTERS = Pattern.compile("[a-zA-Z0-9_]+");
    private static final Predicate<String> NAME_VALIDATOR = s -> s != null && VALID_CHARACTERS.matcher(s).matches();
    private static final String TICK = "`";

    public static String tickName(final String text) {
        return new StringBuilder(TICK).append(text).append(TICK).toString();
    }

    public static QueryTokens combine(final List<QueryTokens> tokens) {

        return QueryTokens.of(tokens.stream()
                        .map(QueryTokens::getQuery)
                        .collect(Collectors.joining(",")),
                tokens.stream()
                        .flatMap(queryTokens -> queryTokens.getQueryValues().stream())
                        .collect(Collectors.toList()));
    }

    public static String requiredValid(final String name) throws IllegalArgumentException {
        if (NAME_VALIDATOR.test(name)) {
            return name;
        }
        throw new IllegalArgumentException(String.format("'%s'  contains illegal characters", name));
    }
}
