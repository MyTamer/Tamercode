package com.ibm.celldt.environment.cellsimulator.core.common;

/**
 * Describes a target created from the environment. This class contains only attributes that apply how the environment
 * interacts with Eclipse. Cell Simulator specific attributes are stored in the proper configuration class from the Cell
 * Simulator Control.
 * 
 * @author Daniel Felix Ferber
 * @since 1.2
 */
public class TargetConfig {

    private String loginUserName;

    private String loginPassword;

    private int loginTimeout;

    private String simulatorCipherType;

    private boolean consoleShowLinux;

    private boolean consoleShowSimulator;

    private int loginPort;

    private String systemWorkspace;

    private boolean automaticNetworkConfiguration;

    private boolean automaticPortConfiguration;

    public boolean isConsoleShowLinux() {
        return consoleShowLinux;
    }

    public void setConsoleShowLinux(boolean consoleShowLinux) {
        this.consoleShowLinux = consoleShowLinux;
    }

    public boolean isConsoleShowSimulator() {
        return consoleShowSimulator;
    }

    public void setConsoleShowSimulator(boolean consoleShowSimulator) {
        this.consoleShowSimulator = consoleShowSimulator;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public int getLoginTimeout() {
        return loginTimeout;
    }

    public void setLoginTimeout(int loginTimeout) {
        this.loginTimeout = loginTimeout;
    }

    public int getLoginPort() {
        return this.loginPort;
    }

    public void setLoginPort(int loginPort) {
        this.loginPort = loginPort;
    }

    public boolean getDoAutomaticNetworkConfiguration() {
        return automaticNetworkConfiguration;
    }

    public void setDoAutomaticNetworkConfiguration(boolean automaticNetworkConfiguration) {
        this.automaticNetworkConfiguration = automaticNetworkConfiguration;
    }

    public boolean getDoAutomaticPortConfiguration() {
        return automaticPortConfiguration;
    }

    public void setDoAutomaticPortConfiguration(boolean automaticPortConfiguration) {
        this.automaticPortConfiguration = automaticPortConfiguration;
    }

    public String getSystemWorkspace() {
        return systemWorkspace;
    }

    public void setSystemWorkspace(String systemWorkspace) {
        this.systemWorkspace = systemWorkspace;
    }

    public String getSimulatorCipherType() {
        return simulatorCipherType;
    }

    public void setSimulatorCipherType(String simulatorCipherType) {
        this.simulatorCipherType = simulatorCipherType;
    }
}
