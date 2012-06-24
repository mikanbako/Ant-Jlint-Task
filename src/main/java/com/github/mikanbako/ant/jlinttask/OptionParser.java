package com.github.mikanbako.ant.jlinttask;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Parse Jlint options.
 *
 * <p>This class does not support option that has argument.</p>
 */
/* package */ final class OptionParser {
    /**
     * Pattern to split by white space.
     */
    private static final Pattern SPLIT_BY_WHITE_SPACE_PATTERN =
            Pattern.compile("\\s+");

    /**
     * Do not create instance because this is utility class.
     */
    private OptionParser() {
        // no operation.
    }

    /**
     * Parse options from {@link String}.
     *
     * @param optionString Options represented by {@link String}.
     * @return Set of options.
     */
    public static Set<String> parse(String optionString) {
        String[] splittedOptions =
                SPLIT_BY_WHITE_SPACE_PATTERN.split(optionString);

        HashSet<String> options = new HashSet<String>();

        // Add options expected empty string.
        for (String optionCandidate : splittedOptions) {
            if (!optionCandidate.isEmpty()) {
                options.add(optionCandidate);
            }
        }

        return options;
    }
}
