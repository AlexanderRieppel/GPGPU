package Controller;

import java.io.IOException;

import GPU.GPUFactor;

public class GPGPU_Controller {
	public static void main(String[] args){
		System.out.println("Loading Programm...");
		long startt = System.currentTimeMillis();
		int n = 100000;
		long[] todo = new long[n];
		long add = (long) (Math.random()*100+1);
		todo[0] = (long) (Math.random()*100+1);
		for(int i = 1; i < n; i++){
			todo[i] = todo[i-1] + add;
		}
		long endt = System.currentTimeMillis();
		System.out.println("Loading Programm finished!");
		System.out.println("Needed " + (endt - startt) + "ms");
		try {
			GPUFactor.factor_gpu_TEST(todo);
		} catch (IOException e) {
		}
	}

}
