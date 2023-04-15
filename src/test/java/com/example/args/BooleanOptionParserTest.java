package com.example.args;

import com.example.args.exceptions.TooManyArgumentsExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static java.util.Arrays.asList;

public class BooleanOptionParserTest {

    //    sad path:
    //    Bool: -l t / -l t f

    @Test
    public void should_not_accept_extra_argument_for_boolean_option() {
        TooManyArgumentsExceptions e = Assertions.assertThrows(TooManyArgumentsExceptions.class, () -> {
            new BooleanParser().parse(asList("-l", "t"), option("l"));
        });

        Assertions.assertEquals("l", e.getOption());
    }

    // 會發現這是多餘的測試，因為參數大於一個以上其實對於測試都是相同的(這部分是當初在想需求得時候沒想清楚，在寫測試後才意識到)
    @Test
    public void should_not_accept_extra_arguments_for_boolean_option() {
        TooManyArgumentsExceptions e = Assertions.assertThrows(TooManyArgumentsExceptions.class, () -> {
            new BooleanParser().parse(asList("-l", "t", "f"), option("l"));
        });

        Assertions.assertEquals("l", e.getOption());
    }

    // 可以發現這個針對default value的測試是直接通過的，原因是因為之前在ArgsTest就已經測到一模一樣的情境
    // 也有可能是當初在想任務列表的時候想多了或是在實現某部分功能順便也把這部分的功能也實現了
    @Test
    public void should_set_default_value_to_false_if_option_not_present() {
        Assertions.assertFalse(new BooleanParser().parse(asList(), option("l")));
    }

    //    Integer: -p / -p 8080 8081
    //    String: -d / -d /usr/logs /usr/vars
    //
    //    default value:
    //    Bool: false
    //    Integer: 0
    //    String: ""


    static Option option(String value) {
        return new Option() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return Option.class;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }
}