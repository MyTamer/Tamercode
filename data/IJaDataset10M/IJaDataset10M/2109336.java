package com.ibm.icu.dev.tool;

/**
 * A command-line option.  A UOption specifies the name of an option
 * and whether or not it takes an argument.  It is a mutable object
 * that later contains the option argument, if any, and a boolean
 * flag stating whether the option was seen or not.
 *
 * The static method parseArgs() takes an array of command-line
 * arguments and an array of UOptions and parses the command-line
 * arguments.
 *
 * This deliberately resembles the icu4c file uoption.[ch].
 */
public class UOption {

    public String longName;

    public String value;

    public Fn optionFn;

    public Object context;

    public char shortName;

    public int hasArg;

    public boolean doesOccur;

    public static final int NO_ARG = 0;

    public static final int REQUIRES_ARG = 1;

    public static final int OPTIONAL_ARG = 2;

    public interface Fn {

        int handle(UOption option);
    }

    /**
     * Create a UOption with the given attributes.
     */
    public static UOption create(String aLongName, char aShortName, int hasArgument) {
        return new UOption(aLongName, aShortName, hasArgument);
    }

    /**
     * Create a UOption with the given attributes.
     * Synonym for create(), for C compatibility.
     */
    public static UOption DEF(String aLongName, char aShortName, int hasArgument) {
        return create(aLongName, aShortName, hasArgument);
    }

    public static UOption HELP_H() {
        return create("help", 'h', NO_ARG);
    }

    public static UOption HELP_QUESTION_MARK() {
        return create("help", '?', NO_ARG);
    }

    public static UOption VERBOSE() {
        return create("verbose", 'v', NO_ARG);
    }

    public static UOption QUIET() {
        return create("quiet", 'q', NO_ARG);
    }

    public static UOption VERSION() {
        return create("version", 'V', NO_ARG);
    }

    public static UOption COPYRIGHT() {
        return create("copyright", 'c', NO_ARG);
    }

    public static UOption DESTDIR() {
        return create("destdir", 'd', REQUIRES_ARG);
    }

    public static UOption SOURCEDIR() {
        return create("sourcedir", 's', REQUIRES_ARG);
    }

    public static UOption ENCODING() {
        return create("encoding", 'e', REQUIRES_ARG);
    }

    public static UOption ICUDATADIR() {
        return create("icudatadir", 'i', REQUIRES_ARG);
    }

    public static UOption PACKAGE_NAME() {
        return create("package-name", 'p', REQUIRES_ARG);
    }

    public static UOption BUNDLE_NAME() {
        return create("bundle-name", 'b', REQUIRES_ARG);
    }

    /**
     * Java Command line argument parser.
     *
     * This function takes the argv[] command line and a description of
     * the program's options in form of an array of UOption structures.
     * Each UOption defines a long and a short name (a string and a character)
     * for options like "--foo" and "-f".
     *
     * Each option is marked with whether it does not take an argument,
     * requires one, or optionally takes one. The argument may follow in
     * the same argv[] entry for short options, or it may always follow
     * in the next argv[] entry.
     *
     * An argument is in the next argv[] entry for both long and short name
     * options, except it is taken from directly behind the short name in
     * its own argv[] entry if there are characters following the option letter.
     * An argument in its own argv[] entry must not begin with a '-'
     * unless it is only the '-' itself. There is no restriction of the
     * argument format if it is part of the short name options's argv[] entry.
     *
     * The argument is stored in the value field of the corresponding
     * UOption entry, and the doesOccur field is set to 1 if the option
     * is found at all.
     *
     * Short name options without arguments can be collapsed into a single
     * argv[] entry. After an option letter takes an argument, following
     * letters will be taken as its argument.
     *
     * If the same option is found several times, then the last
     * argument value will be stored in the value field.
     *
     * For each option, a function can be called. This could be used
     * for options that occur multiple times and all arguments are to
     * be collected.
     *
     * All options are removed from the argv[] array itself. If the parser
     * is successful, then it returns the number of remaining non-option
     * strings.  (Unlike C, the Java argv[] array does NOT contain
     * the program name in argv[0].)
     *
     * An option "--" ends option processing; everything after this
     * remains in the argv[] array.
     *
     * An option string "-" alone is treated as a non-option.
     *
     * If an option is not recognized or an argument missing, then
     * the parser returns with the negative index of the argv[] entry
     * where the error was detected.
     *
     * @param argv this parameter is modified
     * @param start the first argument in argv[] to examine.  Must be
     * 0..argv.length-1.  Arguments from 0..start-1 are ignored.
     * @param options this parameter is modified
     * @return the number of unprocessed arguments in argv[], including
     * arguments 0..start-1.
     */
    public static int parseArgs(String argv[], int start, UOption options[]) {
        String arg;
        int i = start, remaining = start;
        char c;
        boolean stopOptions = false;
        while (i < argv.length) {
            arg = argv[i];
            if (!stopOptions && arg.length() > 1 && arg.charAt(0) == '-') {
                c = arg.charAt(1);
                UOption option = null;
                arg = arg.substring(2);
                if (c == '-') {
                    if (arg.length() == 0) {
                        stopOptions = true;
                    } else {
                        int j;
                        for (j = 0; j < options.length; ++j) {
                            if (options[j].longName != null && arg.equals(options[j].longName)) {
                                option = options[j];
                                break;
                            }
                        }
                        if (option == null) {
                            syntaxError("Unknown option " + argv[i]);
                        }
                        option.doesOccur = true;
                        if (option.hasArg != NO_ARG) {
                            if (i + 1 < argv.length && !(argv[i + 1].length() > 1 && argv[i + 1].charAt(0) == '-')) {
                                option.value = argv[++i];
                            } else if (option.hasArg == REQUIRES_ARG) {
                                syntaxError("Option " + argv[i] + " lacks required argument");
                            }
                        }
                    }
                } else {
                    for (; ; ) {
                        int j;
                        for (j = 0; j < options.length; ++j) {
                            if (c == options[j].shortName) {
                                option = options[j];
                                break;
                            }
                        }
                        if (option == null) {
                            syntaxError("Unknown option '" + c + "' in " + argv[i]);
                        }
                        option.doesOccur = true;
                        if (option.hasArg != NO_ARG) {
                            if (arg.length() != 0) {
                                option.value = arg;
                                break;
                            } else if (i + 1 < argv.length && !(argv[i + 1].length() > 1 && argv[i + 1].charAt(0) == '-')) {
                                option.value = argv[++i];
                                break;
                            } else if (option.hasArg == REQUIRES_ARG) {
                                syntaxError("Option -" + c + " lacks required argument");
                            }
                        }
                        option = null;
                        if (arg.length() == 0) break;
                        c = arg.charAt(0);
                        arg = arg.substring(1);
                    }
                }
                if (option != null && option.optionFn != null && option.optionFn.handle(option) < 0) {
                    syntaxError("Option handler failed for " + argv[i]);
                }
                ++i;
            } else {
                argv[remaining++] = arg;
                ++i;
            }
        }
        return remaining;
    }

    /**
     * Convenient method.
     */
    public static int parseArgs(String argv[], UOption options[]) {
        return parseArgs(argv, 0, options);
    }

    /**
     * Constructor.
     */
    private UOption(String aLongName, char aShortName, int hasArgument) {
        longName = aLongName;
        shortName = aShortName;
        hasArg = hasArgument;
    }

    /**
     * Throw an exception indicating a syntax error.
     */
    private static void syntaxError(String message) {
        throw new IllegalArgumentException("Error in argument list: " + message);
    }
}
