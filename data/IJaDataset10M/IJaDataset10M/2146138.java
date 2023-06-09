package se.sics.mspsim.core;

public class InterruptMultiplexer implements InterruptHandler {

    int interruptBits;

    MSP430Core cpu;

    int vector;

    public InterruptMultiplexer(MSP430Core cpu, int vector) {
        this.cpu = cpu;
        this.vector = vector;
    }

    public void updateInterrupt(boolean value, int bit) {
        if (value) interruptBits |= 1 << bit; else interruptBits &= ~(1 << bit);
        cpu.flagInterrupt(vector, this, interruptBits > 0);
    }

    public void interruptServiced(int vector) {
    }

    public String getName() {
        return "InterruptMultiplexer vector " + vector + " bits: " + interruptBits;
    }
}
