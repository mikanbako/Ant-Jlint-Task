/* Jlint Ant Task
    Copyright (C) 2012 Keita Kita

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.github.mikanbako.ant.jlinttask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * Message filter.
     */
    private String mMessageFilter;

    /**
     * File of message filter.
     */
    private File mMessageFilterFile;

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
     * Set the message filter.
     *
     * @param messageFilter Message filter.
     */
    public void setMessageFilter(String messageFilter) {
        mMessageFilter = messageFilter;
    }

    /**
     * Set the file of message filter.
     *
     * @param messageFilterFile File of message filter.
     */
    public void setMessageFilterFile(File messageFilterFile) {
        mMessageFilterFile = messageFilterFile;
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

        executor.setOptions(getOptions());

        if (mSourceDirectory != null) {
            executor.setSourceDirectory(getSourceDirectory());
        }

        int exitCode;
        try {
            StringBuilder output = new StringBuilder();
            StringBuilder error = new StringBuilder();

            exitCode = executor.execute(output, error);

            log(error.toString());

            if (exitCode == 0) {
                outputResult(output.toString());
            }
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

        // Check messageFilterFile attribute.
        if (mMessageFilterFile != null && !mMessageFilterFile.isFile()) {
            throw new BuildException(mMessageFilterFile.getAbsolutePath() +
                    " must be a file.");
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
     * Get options of Jlint.
     *
     * @return Set that contains Jlint options
     * @throws BuildException If this method cannot get options
     */
    private Set<String> getOptions() {
        HashSet<String> options = new HashSet<String>();

        if (mMessageFilter != null) {
            options.addAll(OptionParser.parse(mMessageFilter));
        }

        if (mMessageFilterFile != null) {
            Reader reader = null;

            try {
                reader = createReader(mMessageFilterFile);
                options.addAll(
                        OptionParser.parse(reader));
            } catch (IOException e) {
                throw new BuildException(
                        "messageFilterFile attribute is invalid.", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new BuildException("I/O error occurred.", e);
                    }
                }
            }
        }

        return options;
    }

    /**
     * Create {@link Reader} for the file.
     *
     * @param file File to read.
     * @return {@link Reader} for the file.
     * @throws IOException If this method cannot create a {@link Reader}
     */
    private Reader createReader(File file) throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file),
                        Charset.forName("ASCII")));
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
