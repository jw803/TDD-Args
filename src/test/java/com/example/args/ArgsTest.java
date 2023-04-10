package com.example.args;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArgsTest {
    //    single option
    //    Bool: -i
    //    Integer: -p 8080
    //    String: -d /usr/logs
    //
    //    multi options: -l -p 8080 -d /usr/logs
    //
    //    sad path:
    //    Bool: -l t / -l t f
    //    Integer: -p / -p 8080 8081
    //    String: -d / -d /usr/logs /usr/vars
    //
    //    default value:
    //    Bool: false
    //    Integer: 0
    //    String: ""

    @Test
    public void should_example_1() {
        Options options = Args.parse(Options.class, "-l", "-p", "8080", "-d", "/usr/logs");
        assertTrue(options.logging());
        assertEquals(8080, options.port());
        assertEquals("/usr/logs", options.directory());
    }

    record Options(@Option("l") boolean logging, @Option("p") int port, @Option("d") String directory) {
    }

    @Test
    public void should_example_2() {
        ListOptions options = Args.parse(ListOptions.class, "-g", "this", "is", "a", "list", "-d", "1", "2", "-3");

        assertArrayEquals(new String[]{"this", "is", "a", "list"}, options.group());
        assertArrayEquals(new int[]{1, 2, -3}, options.decimals());
    }

    record ListOptions(@Option( "g") String[] group, @Option("d") int[] decimals) {
    }
}
