package net.sf.m2javafxc.javafxc.shell;

import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Class with patches copied from plexus-utils with fix for PLX-161,
 * as we can not upgrade plexus-utils until it's upgraded in core Maven
 * </p>
 * 
 * TODO deprecate when plexus-utils 1.2 can be used
 *
 * @author <a href="mailto:carlos@apache.org">Carlos Sanchez</a>
 */
public class Shell {

    private String shellCommand;

    private String[] shellArgs;

    /**
     * Set the command to execute the shell (eg. COMMAND.COM, /bin/bash,...)
     *
     * @param shellCommand
     */
    public void setShellCommand(String shellCommand) {
        this.shellCommand = shellCommand;
    }

    /**
     * Get the command to execute the shell
     *
     * @return
     */
    public String getShellCommand() {
        return shellCommand;
    }

    /**
     * Set the shell arguments when calling a command line (not the executable arguments)
     * (eg. /X /C for CMD.EXE)
     *
     * @param shellArgs
     */
    public void setShellArgs(String[] shellArgs) {
        this.shellArgs = shellArgs;
    }

    /**
     * Get the shell arguments
     *
     * @return
     */
    public String[] getShellArgs() {
        return shellArgs;
    }

    /**
     * Get the command line for the provided executable and arguments in this shell
     *
     * @param executable executable that the shell has to call
     * @param arguments  arguments for the executable, not the shell
     * @return List with one String object with executable and arguments quoted as needed
     */
    public List getCommandLine(String executable, String[] arguments) {
        List commandLine = new ArrayList();
        try {
            StringBuffer sb = new StringBuffer();
            if (executable != null) {
                sb.append(Commandline.quoteArgument(executable));
            }
            for (int i = 0; i < arguments.length; i++) {
                sb.append(" ");
                sb.append(Commandline.quoteArgument(arguments[i]));
            }
            commandLine.add(sb.toString());
        } catch (CommandLineException e) {
            throw new RuntimeException(e);
        }
        return commandLine;
    }

    /**
     * Get the full command line to execute, including shell command, shell arguments,
     * executable and executable arguments
     *
     * @param executable executable that the shell has to call
     * @param arguments  arguments for the executable, not the shell
     * @return List of String objects, whose array version is suitable to be used as argument
     *         of Runtime.getRuntime().exec()
     */
    public List getShellCommandLine(String executable, String[] arguments) {
        List commandLine = new ArrayList();
        if (getShellCommand() != null) {
            commandLine.add(getShellCommand());
        }
        if (getShellArgs() != null) {
            commandLine.addAll(Arrays.asList(getShellArgs()));
        }
        commandLine.addAll(getCommandLine(executable, arguments));
        return commandLine;
    }
}
