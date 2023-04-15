package com.example.args;

import com.example.args.exceptions.InsufficientArgumentsExceptions;
import com.example.args.exceptions.TooManyArgumentsExceptions;

import java.util.List;
import java.util.function.Function;

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
        if (index + 1 >= arguments.size() ||
                arguments.get(index + 1).startsWith("-")) throw new InsufficientArgumentsExceptions(option.value());
        if (index + 2 < arguments.size() &&
                !arguments.get(index + 2).startsWith("-")) throw new TooManyArgumentsExceptions(option.value());
        String value = arguments.get(index + 1);
        return valueParser.apply(value);
    }
}
