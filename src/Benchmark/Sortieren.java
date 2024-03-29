package Benchmark;

import java.io.IOException;
import java.nio.ByteOrder;

import org.bridj.Pointer;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;
import com.nativelibs4java.opencl.CLMem.Usage;
import com.nativelibs4java.util.IOUtils;
/**
 * This class contains the static methods to tests the sort algorithm on GPU and CPU
 * @author Dominik Backhausen, Alexander Rieppel
 *
 */
public class Sortieren {
	/**
	 * CPU factor test
	 * @param todo array to test
	 * @return
	 */
	public static long CPU_sort(long[] todo){
		System.out.println("Starting CPU program!");
		System.out.println("Working...");
		long startt = System.currentTimeMillis();
		
		for (int i = 0; i < todo.length - 1; i++) {
			for (int j = i + 1; j < todo.length; j++) {
				if (todo[i] > todo[j]) {
					long temp = todo[i];
					todo[i] = todo[j];
					todo[j] = temp;
				}
			}
		}
		
		long endt = System.currentTimeMillis();
		System.out.println("CPU program finished!");
		return (endt-startt);
	}
	/**
	 * GPU factor test
	 * @param todo array to test
	 * @return
	 */
	public static long GPU_sort(long[] todo) throws IOException{
		//System.out.println("Setting up GPU program...");
		CLContext context = JavaCL.createBestContext();
        CLQueue queue = context.createDefaultQueue();
        ByteOrder byteOrder = context.getByteOrder();
        
        int n = todo.length;
        Pointer<Long> aPtr = Pointer.allocateLongs(n).order(byteOrder);
        
        for(int i = 0; i < n ; i++){
        	aPtr.set(i, todo[i]);
        }
        
        CLBuffer<Long> a = context.createBuffer(Usage.InputOutput, aPtr);
        
        String src = IOUtils.readText(Sortieren.class.getResource("GPUTest.cl"));
        CLProgram program = context.createProgram(src);
        
       // System.out.println("Setting up finished!");
        System.out.println("Starting GPU program!");
        System.out.println("Working...");
        Long startTime = System.currentTimeMillis();
        CLKernel factorKernel = program.createKernel("sort_test");
        factorKernel.setArgs(a, n);
        CLEvent addEvt = factorKernel.enqueueNDRange(queue, new int[] { n });
        Long endTime = System.currentTimeMillis();
        
        addEvt.release();
        factorKernel.release();
        program.release();
        a.release();
        aPtr.clearValidBytes();
        aPtr.release();
        queue.release();
        context.release();
        System.out.println("GPU program finished!");
        return (endTime-startTime);
	}
}
