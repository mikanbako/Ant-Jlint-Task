/* Jlint Ant Task
    Copyright (C) 2012 Keita Kita

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
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
