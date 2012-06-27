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

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Test {@link JlintExecutor}.
 */
public class JlintExecutorTest extends TestCase {
    private static final File JLINT_EXECUTABLE = new File(".");

    private CommandRecordingExecutor mCommandExecutor;

    private StringBuilder mResultStringBuilder;

    private StringBuilder mErrorStringBuilder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mCommandExecutor = new CommandRecordingExecutor();
        mResultStringBuilder = new StringBuilder();
        mErrorStringBuilder = new StringBuilder();
    }

    /**
     * Create {@link JlintExecutor}.
     *
     * @param jlintExecutable Jlint executable file
     * @param classFiles Class files
     * @return {@link JlintExecutor}
     */
    private JlintExecutor createJlintExecutor(
            File jlintExecutable, List<File> classFiles) {
        return new JlintExecutor(
                mCommandExecutor, jlintExecutable, classFiles);
    }

    /**
     * Test with minimum arguments.
     *
     * Command is "<Jlint executable> <class file> ...".
     */
    @Test
    public void testMinimumArguments() throws Exception {
        List<File> classFiles = Arrays.asList(new File("a"), new File("b"));

        JlintExecutor executor = createJlintExecutor(
                JLINT_EXECUTABLE, classFiles);
        executor.execute(mResultStringBuilder, mErrorStringBuilder);

        List<String> recordedCommand = mCommandExecutor.getRecordedCommand();
        assertEquals(
                JLINT_EXECUTABLE.getAbsolutePath(),
                recordedCommand.get(0));
        assertEquals(
                classFiles.get(0).getAbsolutePath(),
                recordedCommand.get(1));
        assertEquals(
                classFiles.get(0).getAbsolutePath(),
                recordedCommand.get(1));
    }


    /**
     * Test with source directory.
     *
     * Command is "<Jlint executable> -source <source directory>
     *  <class file> ...".
     */
    @Test
    public void testWithSourceDirectory() throws Exception {
        File sourceDirectory = new File("d");
        List<File> classFiles = Collections.singletonList(new File("a"));

        JlintExecutor executor = createJlintExecutor(
                JLINT_EXECUTABLE, classFiles);
        executor.setSourceDirectory(sourceDirectory);
        executor.execute(mResultStringBuilder, mErrorStringBuilder);

        List<String> recordedCommand = mCommandExecutor.getRecordedCommand();
        assertEquals(
                JLINT_EXECUTABLE.getAbsolutePath(), recordedCommand.get(0));
        assertEquals("-source", recordedCommand.get(1));
        assertEquals(
                sourceDirectory.getAbsolutePath(), recordedCommand.get(2));
        assertEquals(
                classFiles.get(0).getAbsolutePath(), recordedCommand.get(3));
    }

    /**
     * Test with some options.
     *
     * Command is "<Jlint executable> <option> ... <class file> ...".
     */
    @Test
    public void testWithOptions() throws Exception {
        List<File> classFiles = Collections.singletonList(new File("a"));
        HashSet<String> options = new HashSet<String>();
        options.add("+all");
        options.add("-data_flow");

        JlintExecutor executor = createJlintExecutor(JLINT_EXECUTABLE,
                classFiles);
        executor.setOptions(options);
        executor.execute(mResultStringBuilder, mErrorStringBuilder);

        List<String> recordedCommand = mCommandExecutor.getRecordedCommand();
        assertEquals(
                JLINT_EXECUTABLE.getAbsolutePath(), recordedCommand.get(0));
        assertTrue(recordedCommand.subList(1, 3).contains("+all"));
        assertTrue(recordedCommand.subList(1, 3).contains("-data_flow"));
        assertEquals(classFiles.get(0).getAbsolutePath(),
                recordedCommand.get(3));
    }
}
