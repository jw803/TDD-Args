package com.example.args.exceptions;

public class UnsupportedOptionTypeException extends RuntimeException{
    final private String option;

    final private Class<?> parameterType;

    public UnsupportedOptionTypeException(String option, Class<?> parameterType) {
        super(option);
        this.option = option;
        this.parameterType = parameterType;
    }

    public String getOption() {
        return option;
    }

    public Class<?> getParameterType() { return parameterType; }
}
