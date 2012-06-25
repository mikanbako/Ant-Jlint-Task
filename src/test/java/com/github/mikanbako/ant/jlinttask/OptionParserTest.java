package com.github.mikanbako.ant.jlinttask;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
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
     * Assert the options are contained in the {@link Set}.
     *
     * @param expectedOptions Expected options
     * @param actualOptions Actual options
     */
    private static void assertContainedOptions(
            List<String> expectedOptions, Set<String> actualOptions) {
        assertEquals(expectedOptions.size(), actualOptions.size());
        assertTrue(actualOptions.containsAll(expectedOptions));
    }

    /**
     * Test with single option.
     *
     * Result contains the option only.
     */
    @Test
    public void testSingleOption() {
        Set<String> result = OptionParser.parse("+all");

        assertContainedOptions(Arrays.asList("+all"), result);
    }

    /**
     * Test with multiple options.
     *
     * Result contains the options only.
     */
    @Test
    public void testMultipleOption() {
        Set<String> result = OptionParser.parse("-all +data_flow");

        assertContainedOptions(Arrays.asList("-all", "+data_flow"), result);
    }

    /**
     * Test with options string that contains white space.
     *
     * Result contains the options only.
     */
    @Test
    public void testOptionsWithWhitespace() {
        Set<String> result = OptionParser.parse(" +all -data_flow ");

        assertContainedOptions(Arrays.asList("+all", "-data_flow"), result);
    }

    /**
     * Test with {@link Reader} that reads options written in a single line.
     *
     * Result contains the read options.
     */
    @Test
    public void testOptionsFromReaderWithSingleLine() throws IOException {
        Set<String> result = OptionParser.parse(
                new StringReader("+all +data_flow"));

        assertContainedOptions(Arrays.asList("+all", "+data_flow"), result);
    }

    /**
     * Test with {@link Reader} that reads options written in multiple lines.
     *
     * Result contains the read options.
     */
    @Test
    public void testOptionsFromReaderWithMultipleLine() throws IOException {
        Set<String> result = OptionParser.parse(
                new StringReader("+all \n -data_flow\n\r\n+bounds\n"));

        assertContainedOptions(
                Arrays.asList("+all", "-data_flow", "+bounds"),
                result);
    }
}
