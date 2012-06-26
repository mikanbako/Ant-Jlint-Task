Jlint Ant Task - An Ant task for Jlint

Required software :

    * Jlint 3.1.2 (http://jlint.sourceforge.net/)
    * Java Runtime Environment 1.6 or above.
    * Ant 1.7 or above.

Usage :

1. Add jlint_task-xxx.jar to classpath of Ant.

2. Write taskdef tag to your build.xml by the below.

    <taskdef name="jlint"
        classname="com.github.mikanbako.ant.jlinttask.JlintTask" />

3. Write a jlint tag to your build.xml.

    The jlint element has attributes :

        * executable (Required) : Path of executable file of Jlint.
        * outputFile (Optional) : Path of file in which
            Jlint output will be saved. By default,
            the output is displayed by Ant.
        * sourceDirectory (Optional) : Path of directory contains source file.
            This attribute is the same of -source option of Jlint.
        * messageFilter (Optional) : Configuration of message filtering.
            This attribute is the same of message filtering options.
            (See "Message Filtering" for detail)
        * messageFilterFile (Optional) : Configuration file for message
            filtering. (See "Message Filtering" for detail)

    And the jlint element has nested elements :

        * fileset (Required) : FileSet type of Ant.
          This FileSet provides class files to Jlint.

Message Filtering :

    You can filter reporting by Jlint by two ways.

        1. Use messageFilter attribute of jlint tag.
        2. Use messageFilterFile attribute of jlint tag.

    1. Use messageFilter attribute of jlint tag.

        Filtering is defined in build.xml.

        Write options of Jlint that filter messages to the messageFilter
        attribute.

        Example :

            <jlint executable="/usr/local/bin/jlint" sourceDirectory="src"
                    messageFilter="-all +data_flow">
                <fileset dir="bin">
                    <include name="**/*.class" />
                </fileset>
            </jlint>

    2.  Use messageFilterFile attribute of jlint tag.

        Filtering is defined in an external file.

        Write options of Jlint that filter messages to a file specified by
        the messageFilterFile attribute.

        The file lists the options separated by white spaces.

        Example :

            build.xml :

                <jlint executable="/usr/local/bin/jlint" sourceDirectory="src"
                        messageFilterFile="message_filter.txt">
                    <fileset dir="bin">
                        <include name="**/*.class" />
                    </fileset>
                </jlint>

            message_filter.txt :

                -all
                +data_flow

    If the messageFilter and the messageFilterFile attribute are both
    specified, options from the messageFilterFile attributes are added to
    options from the messageFilter attribute.

Example :

    * Jlint executable file is /usr/local/bin/jlint.
    * Source directory is src.
    * Class files are in bin.
    * Jlint output will be saved to jlint_result.txt.
    * Jlint message filter is written in message_filter.txt.

    <taskdef name="jlint"
        classname="com.github.mikanbako.ant.jlinttask.JlintTask" />

    <jlint executable="/usr/local/bin/jlint" sourceDirectory="src"
            messageFilterFile="message_filter.txt"
            outputFile="jlint_result.txt">
        <fileset dir="bin">
            <include name="**/*.class" />
        </fileset>
    </jlint>
