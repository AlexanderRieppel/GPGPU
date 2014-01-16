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

public class Factorisierung {
//	public static void main(String[] args) {
//		int zuTeilen = (int) (Math.random()*100+1);
//		int n = 100000;
//		long startt = System.currentTimeMillis();
//		for(int i = 0; i< n;i++){
//			//Factorisierung.factor(zuTeilen);
//			zuTeilen += 13;
//		}
//		long endt = System.currentTimeMillis();
//		System.out.println("Total time needed: " + (endt-startt) + "ms");
//	}
	public static long CPU_factor(long[] todo){
		System.out.println("Starting CPU program!");
		System.out.println("Working...");
		long startt = System.currentTimeMillis();
		int i;
		boolean firstTime = true;

		//System.out.print(zuTeilen + " = ");
		for(int id = 0; id < todo.length;id++){
			long zuTeilen = todo[id];
			for (i = 2; i <= zuTeilen;) {
				if (zuTeilen % i == 0) {
					if (!firstTime) {
						//System.out.print(" * ");
					} else {
						firstTime = false;
					}
					//System.out.print(i);
					zuTeilen = zuTeilen / i;
				}
				if (zuTeilen % i != 0)
					i++;
			}
		}
		long endt = System.currentTimeMillis();
		System.out.println("CPU program finished!");
		return (endt-startt);
	}
	public static long GPU_factor(long[] todo) throws IOException{
		//System.out.println("Setting up GPU program...");
		CLContext context = JavaCL.createBestContext();
        CLQueue queue = context.createDefaultQueue();
        ByteOrder byteOrder = context.getByteOrder();
        
        int n = todo.length;
        Pointer<Long> aPtr = Pointer.allocateLongs(n).order(byteOrder);
        
        for(int i = 0; i < n ; i++){
        	aPtr.set(i, todo[i]);
        }
        
        CLBuffer<Long> a = context.createBuffer(Usage.Input, aPtr);
        
        String src = IOUtils.readText(Factorisierung.class.getResource("GPUTest.cl"));
        CLProgram program = context.createProgram(src);
        
        //System.out.println("Setting up finished!");
        System.out.println("Starting GPU program!");
        System.out.println("Working...");
        Long startTime = System.currentTimeMillis();
        CLKernel factorKernel = program.createKernel("factor_test");
        factorKernel.setArgs(a, n);
        CLEvent addEvt = factorKernel.enqueueNDRange(queue, new int[] { n });
        Long endTime = System.currentTimeMillis();
        System.out.println("GPU program finished!");
        factorKernel.release();
        program.release();
        a.release();
        addEvt.release();
        queue.release();
        byteOrder = null;
        context.release();
        return (endTime-startTime);
	}
}
