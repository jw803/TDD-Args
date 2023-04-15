package com.example.args.exceptions;

public class InsufficientArgumentsExceptions extends RuntimeException {
    private String option;

    public InsufficientArgumentsExceptions(String option) {
        super(option);
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
