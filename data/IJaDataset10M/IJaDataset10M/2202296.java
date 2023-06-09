package net.jadoth.persistence;

import net.jadoth.lang.concurrent.VM;
import sun.misc.Unsafe;

public class MainTestCopyUnits
{
	static final int SIZE = (1<<25) + 1;
	static final byte[] BYTES0 = new byte[SIZE];
	static final byte[] BYTES1 = new byte[SIZE];

	static final Unsafe vm = VM.getUnsafe();

	static final long BABO       = Unsafe.ARRAY_BYTE_BASE_OFFSET;

	public static void main(final String[] args)
	{
		for(int r = 1_000_000; r --> 0;){
			long tStart, tStop;
			tStart = System.nanoTime();
			copy(SIZE);
			tStop = System.nanoTime();
			System.out.println("Elapsed Time: " + new java.text.DecimalFormat("00,000,000,000").format(tStop - tStart));
		}
	}


	public static void copy(final long size)
	{
		vm.copyMemory(BYTES0, BABO, BYTES1, BABO, size);
	}
}
