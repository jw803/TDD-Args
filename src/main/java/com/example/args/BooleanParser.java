package com.example.args;

import com.example.args.exceptions.TooManyArgumentsExceptions;

import java.util.List;

class BooleanParser implements OptionParser<Boolean> {

    @Override
    public Boolean parse(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());
        if (index + 1 < arguments.size() &&
                !arguments.get(index + 1).startsWith("-")) throw new TooManyArgumentsExceptions(option.value());
        return index != -1;
    }
}
