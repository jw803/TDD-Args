package com.example.args;

import com.example.args.exceptions.InsufficientArgumentsExceptions;
import com.example.args.exceptions.TooManyArgumentsExceptions;

import java.util.List;

class BooleanParser implements OptionParser<Boolean> {

    @Override
    public Boolean parse(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());
        if(index == -1) return false;

        List<String> values = SingleOptionParser.getFlagValues(arguments, index);

        if (values.size() < 0) throw new InsufficientArgumentsExceptions(option.value());
        if (values.size() > 0) throw new TooManyArgumentsExceptions(option.value());
        return true;
    }
}
