package com.example.args;

import com.example.args.exceptions.IllegalOptionException;
import com.example.args.exceptions.InsufficientArgumentsExceptions;
import com.example.args.exceptions.TooManyArgumentsExceptions;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

class OptionParsers {
    public static OptionParser<Boolean> bool() {
        return (arguments, option) ->
                getFlagValues(arguments, option, 0).map(it -> true).orElse(false);
    }

    public static <T> OptionParser<T> unary(T defaultValue, Function<String, T> valueParser) {
        return (arguments, option) -> getFlagValues(arguments, option, 1)
                .map(it -> parseValue(option, it.get(0), valueParser)).orElse(defaultValue);
    }

    private static Optional<List<String>> getFlagValues(List<String> arguments, Option option, int expectedSize) {
        Optional<List<String>> argumentList;
        int index = arguments.indexOf("-" + option.value());
        if (index == -1) return Optional.empty();

        List<String> values = getFlagValues(arguments, index);

        if (values.size() < expectedSize) throw new InsufficientArgumentsExceptions(option.value());
        if (values.size() > expectedSize) throw new TooManyArgumentsExceptions(option.value());

        return Optional.of(values);
    }

    private static <T> T parseValue(Option option, String value, Function<String, T> valueParser) {
        try {
            return valueParser.apply(value);
        } catch (Exception e) {
            throw new IllegalOptionException(option.value());
        }
    }

    private static List<String> getFlagValues(List<String> arguments, int index) {
        return arguments.subList(index + 1, IntStream.range(index + 1, arguments.size())
                .filter(it -> arguments.get(it).startsWith("-"))
                .findFirst().orElse(arguments.size()));
    }
}
