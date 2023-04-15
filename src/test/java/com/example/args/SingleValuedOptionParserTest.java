package com.example.args;

import com.example.args.exceptions.InsufficientArgumentsExceptions;
import com.example.args.exceptions.TooManyArgumentsExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Annotation;

import static java.util.Arrays.asList;

public class SingleValuedOptionParserTest {

    //    sad path:
    //    Bool: -l t / -l t f
    //    Integer: -p / -p 8080 8081

    @Test
    public void should_not_accept_extra_argument_for_single_valued_option() {
        TooManyArgumentsExceptions e = Assertions.assertThrows(TooManyArgumentsExceptions.class, () -> {
            new SingleOptionParser<Integer>(0, Integer::parseInt).parse(asList("-p", "8080", "8081"), option("p"));
        });

        Assertions.assertEquals("p", e.getOption());
    }

    // 原本只考慮到 -p 後面沒接參數的情況，但是經由對測試的實現發現 -p -l 也是insufficient_argument的其中一種情況
    @ParameterizedTest
    @ValueSource(strings = {"-p -l", "-p"})
    public void should_not_accept_insufficient_argument_for_single_valued_option(String arguments) {
        InsufficientArgumentsExceptions e = Assertions.assertThrows(InsufficientArgumentsExceptions.class, () -> {
            new SingleOptionParser<Integer>(0, Integer::parseInt).parse(asList(arguments.split(" ")), option("p"));
        });

        Assertions.assertEquals("p", e.getOption());
    }

    @Test
    public void should_set_default_value_to_0_for_int_option() {
        Assertions.assertEquals(0, new SingleOptionParser<Integer>(0, Integer::parseInt).parse(asList(), option("p")));
    }
    //    String: -d / -d /usr/logs /usr/vars

    // 當初在拆分任務列表時是利用功能點也就是參數類型去拆分的
    // 但經過一步步測試與重構會發現其實string和int類型都可以共用single valued option去實現
    // 因此這個測試是和上方處理int的測試是完全等效的
    @Test
    public void should_not_accept_extra_argument_for_string_single_valued_option() {
        TooManyArgumentsExceptions e = Assertions.assertThrows(TooManyArgumentsExceptions.class, () -> {
            new SingleOptionParser<>("", String::valueOf).parse(asList("-d", "/usr/logs", "/usr/vars"), option("d"));
        });

        Assertions.assertEquals("d", e.getOption());
    }

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
