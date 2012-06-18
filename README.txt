Developing...

An Ant task for Jlint.

Required software :

    * Jlint 3.1.2 (http://jlint.sourceforge.net/)
    * Java Runtime Environment 1.6 or above.
    * Ant 1.7 or above.

Usage:

1. Add jlint_task-xxx.jar to classpath.

2. Write taskdef tag to your build.xml by the below.

    <taskdef name="jlint"
        classname="com.github.mikanbako.ant.jlinttask.JlintTask" />

3. Write a jlint tag to your build.xml.

    The jlint element has some attributes :

        * executable (Required) : Path of executable file of Jlint.
        * outputFile (Optional) : Path of file in which
            Jlint output will be saved. By default,
            the output is displayed by Ant.
        * sourceDirectory (Optional) : Path of directory contains source file.
            This attribute is the same of -source option of Jlint.

    And the jlint element has nested elements :

        * fileset (Required) : FileSet type of Ant.
          This FileSet provides class files to Jlint.

Example :

    * Jlint executable file is /usr/local/bin/jlint.
    * Source directory is src.
    * Class files are in bin.
    * Jlint output will be saved to jlint_result.txt.

    <taskdef name="jlint"
        classname="com.github.mikanbako.ant.jlinttask.JlintTask" />

    <jlint executable="/usr/local/bin/jlint" sourceDirectory="src"
            outputFile="jlint_result.txt">
        <fileset dir="bin">
            <include name="**/*.class" />
        </fileset>
    </jlint>
