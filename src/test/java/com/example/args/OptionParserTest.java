package com.example.args;

import com.example.args.exceptions.IllegalOptionException;
import com.example.args.exceptions.IllegalValueException;
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
    }

    @Nested
    class ListOptionParser{
        //TODO: -g "this" "is"
        @Test
        public void should_parse_list_value() {
            String[] value = OptionParsers.list(String[]::new, String::valueOf).parse(asList("-g", "this", "is"), option("g"));
            Assertions.assertArrayEquals(new String[]{"this", "is"}, value);
        }
        //TODO: default value
        @Test
        public void should_use_empty_array_as_default_value() {
            String[] value = OptionParsers.list(String[]::new, String::valueOf).parse(asList(), option("g"));
            Assertions.assertEquals(0, value.length);
        }
        //TODO: -d a throw exception
        @Test
        public void should_throw_exception_if_value_parser_cannot_parse_value() {
            Function<String, String> parser = (it) -> {
                throw new RuntimeException();
            };
            IllegalValueException e = Assertions.assertThrows(IllegalValueException.class, () ->
                    OptionParsers.list(String[]::new, parser).parse(asList("-g", "this", "is"), option("g")));
            Assertions.assertEquals("g", e.getOption());
            Assertions.assertEquals("this", e.getValue());
        }

        // 剛法發現對於負數會不小心誤認是flag，因此補上測試去修正這個問題
        @Test
        public void should_not_treat_negative_int_as_flag() {
            Assertions.assertArrayEquals(new Integer[]{-1, -2}, OptionParsers.list(Integer[]::new, Integer::parseInt).parse(asList("-g", "-1", "-2"), option("g")));
        }

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
