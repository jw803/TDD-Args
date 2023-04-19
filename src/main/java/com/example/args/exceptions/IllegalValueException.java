package com.example.args.exceptions;

public class IllegalValueException extends RuntimeException{
    final private String option;

    final private String value;

    public IllegalValueException(String option, String value) {
        super(option);
        this.option = option;
        this.value = value;
    }

    public String getOption() {
        return option;
    }

    public String getValue() { return value; }
}
