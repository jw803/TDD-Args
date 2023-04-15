package com.example.args.exceptions;

public class TooManyArgumentsExceptions extends RuntimeException {
    private String option;

    public TooManyArgumentsExceptions(String option) {
        super(option);
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
