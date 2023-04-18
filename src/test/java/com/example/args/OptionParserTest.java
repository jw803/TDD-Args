package com.example.args;

import com.example.args.exceptions.InsufficientArgumentsExceptions;
import com.example.args.exceptions.TooManyArgumentsExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import static java.util.Arrays.asList;

public class OptionParserTest {

    @Nested
    class UnaryOptionParser {
        // Sad Path
        @Test
        public void should_not_accept_extra_argument_for_single_valued_option() {
            TooManyArgumentsExceptions e = Assertions.assertThrows(TooManyArgumentsExceptions.class, () -> {
                OptionParsers.unary(0, Integer::parseInt).parse(asList("-p", "8080", "8081"), option("p"));
            });

            Assertions.assertEquals("p", e.getOption());
        }

        // Sad Path
        // 原本只考慮到 -p 後面沒接參數的情況，但是經由對測試的實現發現 -p -l 也是insufficient_argument的其中一種情況
        @ParameterizedTest
        @ValueSource(strings = {"-p -l", "-p"})
        public void should_not_accept_insufficient_argument_for_single_valued_option(String arguments) {
            InsufficientArgumentsExceptions e = Assertions.assertThrows(InsufficientArgumentsExceptions.class, () -> {
                OptionParsers.unary(0, Integer::parseInt).parse(asList(arguments.split(" ")), option("p"));
            });

            Assertions.assertEquals("p", e.getOption());
        }

        // Default Value
        @Test
        public void should_set_default_value_to_0_for_int_option() {
            Function<String, Object> whatever = (it) -> null;
            Object defaultValue = new Object();
            Assertions.assertSame(defaultValue, OptionParsers.unary(defaultValue, whatever).parse(asList(), option("p")));
        }
        //    String: -d / -d /usr/logs /usr/vars


        // Happy Path
        @Test
        public void should_parse_value_if_flag_present() {
            Object parsed = new Object();
            Function<String, Object> parse = (it) -> parsed;
            Object whatever = new Object();
            Assertions.assertSame(parsed, OptionParsers.unary(whatever, parse).parse(asList("-p", "8080"), option("p")));
        }

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

    @Nested
    class BooleanOptionParserTest {
        // Sad Path
        @Test
        public void should_not_accept_extra_argument_for_boolean_option() {
            TooManyArgumentsExceptions e = Assertions.assertThrows(TooManyArgumentsExceptions.class, () -> {
                OptionParsers.bool().parse(asList("-l", "t"), option("l"));
            });

            Assertions.assertEquals("l", e.getOption());
        }

        // Default Value
        // 會發現這是多餘的測試，因為參數大於一個以上其實對於測試都是相同的(這部分是當初在想需求得時候沒想清楚，在寫測試後才意識到)
        @Test
        public void should_not_accept_extra_arguments_for_boolean_option() {
            TooManyArgumentsExceptions e = Assertions.assertThrows(TooManyArgumentsExceptions.class, () -> {
                OptionParsers.bool().parse(asList("-l", "t", "f"), option("l"));
            });

            Assertions.assertEquals("l", e.getOption());
        }

        // 可以發現這個針對default value的測試是直接通過的，原因是因為之前在ArgsTest就已經測到一模一樣的情境
        // 也有可能是當初在想任務列表的時候想多了或是在實現某部分功能順便也把這部分的功能也實現了
        @Test
        public void should_set_default_value_to_false_if_option_not_present() {
            Assertions.assertFalse(OptionParsers.bool().parse(asList(), option("l")));
        }

        // Happy path
        // 將happy path 從 ArgsTest移過來，因為這項測試放在ArgsTest怪怪的，應該要分門別類地放
        @Test
        public void should_set_default_value_to_true_if_option_present() {
            Assertions.assertTrue(OptionParsers.bool().parse(asList("-l"), option("l")));
        }

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
}
