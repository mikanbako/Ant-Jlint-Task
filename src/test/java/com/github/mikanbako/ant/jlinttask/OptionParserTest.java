package com.github.mikanbako.ant.jlinttask;

import java.util.Arrays;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Test for {@link OptionParser}.
 */
public class OptionParserTest extends TestCase {
    /**
     * Test when no option.
     *
     * Result is empty.
     */
    @Test
    public void testNoOption() {
        Set<String> result = OptionParser.parse("");

        assertTrue(result.isEmpty());
    }

    /**
     * Test with single option.
     *
     * Result contains the option only.
     */
    @Test
    public void testSingleOption() {
        Set<String> result = OptionParser.parse("+all");

        assertEquals(1, result.size());
        assertTrue(result.contains("+all"));
    }

    /**
     * Test with multiple options.
     *
     * Result contains the options only.
     */
    @Test
    public void testMultipleOption() {
        Set<String> result = OptionParser.parse("-all +data_flow");

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("-all", "+data_flow")));
    }

    /**
     * Test with options string that contains white space.
     *
     * Result contains the options only.
     */
    @Test
    public void testOptionsWithWhitespace() {
        Set<String> result = OptionParser.parse(" +all -data_flow ");

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("+all", "-data_flow")));
    }
}
