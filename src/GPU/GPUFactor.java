package GPU;

import java.io.IOException;
import java.nio.ByteOrder;

import org.bridj.Pointer;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLMem.Usage;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;
import com.nativelibs4java.util.IOUtils;

public class GPUFactor {
	
	public static void factor_gpu_TEST(long[] todo) throws IOException{
		System.out.println("Setting up GPU Programm...");
		CLContext context = JavaCL.createBestContext();
        CLQueue queue = context.createDefaultQueue();
        ByteOrder byteOrder = context.getByteOrder();
        
        int n = todo.length;
        Pointer<Long> aPtr = Pointer.allocateLongs(n).order(byteOrder);
        
        for(int i = 0; i < n ; i++){
        	aPtr.set(i, todo[i]);
        }
        
        CLBuffer<Long> a = context.createBuffer(Usage.Input, aPtr);
        
        String src = IOUtils.readText(GPUFactor.class.getResource("GPUTest.cl"));
        CLProgram program = context.createProgram(src);
        
        System.out.println("Setting up finished!");
        System.out.println("Starting GPU Programm!");
        Long startTime = System.currentTimeMillis();
        CLKernel factorKernel = program.createKernel("factor_test");
        factorKernel.setArgs(a, n);
        CLEvent addEvt = factorKernel.enqueueNDRange(queue, new int[] { n });
        Long endTime = System.currentTimeMillis();
        System.out.println("GPU Programm finished!");
        System.out.println("Total Time GPU needed: " + (endTime-startTime) + "ms");
        factorKernel.release();
        program.release();
        a.release();
        addEvt.release();
        queue.release();
        context.release();
	}
}
