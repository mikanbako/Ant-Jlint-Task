/*
 * Copyright 2012 Keita Kita
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mikanbako.ant.jlinttask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * Ant Task class that executes Jlint.
 */
public final class JlintTask extends Task {
    /**
     * Path of executable file of Jlint.
     */
    private File mJlintExecutable;

    /**
     * Path of directory that contains source files.
     */
    private File mSourceDirectory;

    /**
     * Path of file written result.
     */
    private File mOutputFile;

    /**
     * Path of class files analyzed by Jlint.
     */
    private final ArrayList<FileSet> mClassFileSets = new ArrayList<FileSet>();

    /**
     * Set the path of Jlint.
     *
     * @param jlintPath The path of Jlint
     */
    public void setExecutable(File jlintPath) {
        mJlintExecutable = jlintPath;
    }

    /**
     * Set the path of directory that contains source files.
     *
     * @param sourceDirectory The path of directory that contains source files
     */
    public void setSourceDirectory(File sourceDirectory) {
        mSourceDirectory = sourceDirectory;
    }

    /**
     * Set the path of file written result.
     *
     * @param outputFile Path of file written result.
     */
    public void setOutputFile(File outputFile) {
        mOutputFile = outputFile;
    }

    /**
     * Add {@link FileSet} that contains class files.
     *
     * @param fileSet {@link FileSet} that contains class file
     */
    public void addFileSet(FileSet fileSet) {
        mClassFileSets.add(fileSet);
    }

    @Override
    public void execute() {
        log("Executing Jlint.");

        // Check attributes and nested elements.

        checkProperties();

        // Execute command.

        JlintExecutor executor = new JlintExecutor(
                getJlintExecutable(), getClassFiles());

        if (mSourceDirectory != null) {
            executor.setSourceDirectory(getSourceDirectory());
        }

        int exitCode;
        StringBuilder output = new StringBuilder();
        try {
            exitCode = executor.execute(output);

            outputResult(output.toString());
        } catch (IOException e) {
            throw new BuildException(e);
        } catch (InterruptedException e) {
            throw new BuildException(e);
        }

        if (exitCode != 0) {
            throw new BuildException("Exit code is " + exitCode);
        }

        log("Executing Jlint is finished.");
    }

    /**
     * Check whether properties are valid.
     *
     * @throws BuildException Properties are invalid
     */
    private void checkProperties() {
        // Check executable attribute.

        if (mJlintExecutable == null) {
            throw new BuildException("executable attribute is required.");
        }
        if (!mJlintExecutable.canExecute()) {
            throw new BuildException(mJlintExecutable.getAbsolutePath() +
                    " must be an executable file.");
        }

        // Check sourceDirectory attribute.

        if (mSourceDirectory != null && !mSourceDirectory.isDirectory()) {
            throw new BuildException(mSourceDirectory.getAbsolutePath() +
                    " must be a directory.");
        }

        // Check outputFile attribute.

        if (mOutputFile != null && mOutputFile.isDirectory()) {
            throw new BuildException(mOutputFile.getAbsolutePath() +
                    " must not be directory.");
        }

        // Check nested fileset elements.

        if (mClassFileSets.isEmpty()) {
            throw new BuildException("Nested fileset elements are required.");
        }
    }

    /**
     * Get canonical Jlint executable file.
     *
     * @return Canonical Jlint executable file
     * @throws BuildException If this method cannot get the file
     */
    private File getJlintExecutable() {
        try {
            return mJlintExecutable.getCanonicalFile();
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    /**
     * Get canonical source directory.
     *
     * @return Canonical source directory
     * @throws IllegalStateException If source directory is not specified
     * @throws BuildException If this method cannot get the directory
     */
    private File getSourceDirectory() {
        if (mSourceDirectory == null) {
            throw new IllegalStateException(
                    "sourceDirectory is not specified.");
        }

        try {
            return mSourceDirectory.getCanonicalFile();
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    /**
     * Get canonical class files.
     *
     * @return Canonical class files
     */
    private List<File> getClassFiles() {
        ArrayList<File> classFiles = new ArrayList<File>();

        for (FileSet fileSet : mClassFileSets) {
            DirectoryScanner directoryScanner =
                    fileSet.getDirectoryScanner(getProject());

            File baseDirectory = directoryScanner.getBasedir();
            for (String file : directoryScanner.getIncludedFiles()) {
                File absoluteFile = new File(baseDirectory, file);

                try {
                    classFiles.add(absoluteFile.getCanonicalFile());
                } catch (IOException e) {
                    throw new BuildException(e);
                }
            }
        }

        return classFiles;
    }

    /**
     * Output the result.
     *
     * @param result The result
     * @throws FileNotFoundException If outputFile is not created or cannot
     *  be written
     */
    private void outputResult(String result) throws FileNotFoundException {
        // If outputFile is not specified, output result to log.

        if (mOutputFile == null) {
            log(result);

            return;
        }

        // If outputFile is specified, output result to the file.

        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(mOutputFile),
                        Charset.defaultCharset()));
        try {
            writer.append(result);
        } finally {
            writer.close();
        }

        log("Result is " + mOutputFile.getAbsolutePath());
    }
}