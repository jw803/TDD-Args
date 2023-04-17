package com.example.args;

import com.example.args.exceptions.InsufficientArgumentsExceptions;
import com.example.args.exceptions.TooManyArgumentsExceptions;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

class SingleOptionParser<T> implements OptionParser<T> {
    Function<String, T> valueParser;

    T defaultValue;

    public SingleOptionParser(T defaultValue, Function<String, T> valueParser) {
        this.defaultValue = defaultValue;
        this.valueParser = valueParser;
    }

    @Override
    public T parse(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());
        if(index  == -1) return defaultValue;

        List<String> values = getFlagValues(arguments, index);

        if (values.size() < 1) throw new InsufficientArgumentsExceptions(option.value());
        if (values.size() > 1) throw new TooManyArgumentsExceptions(option.value());
        String value = arguments.get(index + 1);
        return valueParser.apply(value);
    }

    private static List<String> getFlagValues(List<String> arguments, int index) {
        int followingFlag = IntStream.range(index + 1, arguments.size())
                .filter(it -> arguments.get(it).startsWith("-"))
                .findFirst().orElse(arguments.size());

        List<String> values = arguments.subList(index + 1, followingFlag);
        return values;
    }
}
