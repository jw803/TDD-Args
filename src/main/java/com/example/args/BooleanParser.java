package com.example.args;

import java.util.List;

class BooleanParser implements OptionParser<Boolean> {

    @Override
    public Boolean parse(List<String> arguments, Option option) {
        return SingleOptionParser.getFlagValues(arguments, option, 0).map(it -> true).orElse(false);
    }
}
