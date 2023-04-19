package com.example.args.exceptions;

public class IllegalOptionException extends RuntimeException{
    private String option;

    public IllegalOptionException(String option) {
        super(option);
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
