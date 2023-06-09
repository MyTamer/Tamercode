package org.jikesrvm;

/**
 * This error is thrown when the VM encounters an operation
 * that is not yet implemented.
 */
public class VM_UnimplementedError extends VirtualMachineError {

    /**
   * Constructs a new instance of this class with its
   * walkback filled in.
   */
    public VM_UnimplementedError() {
        super();
    }

    /**
   * Constructs a new instance of this class with its
   * walkback and message filled in.
   * @param detailMessage message to fill in
   */
    public VM_UnimplementedError(java.lang.String detailMessage) {
        super(detailMessage + ": not implemented");
    }

    private static final long serialVersionUID = 1L;
}
