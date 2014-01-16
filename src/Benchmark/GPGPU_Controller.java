package Benchmark;

import java.io.IOException;

public class GPGPU_Controller {
	public static void main(String[] args){
		System.out.println("Loading Programm...");
		long startt = System.currentTimeMillis();
		int n = 1000;
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
			outTime(Factorisierung.GPU_factor(todo));
		} catch (IOException e) {
		}
		outTime(Factorisierung.CPU_factor(todo));
		outTime(100);
		outTime(1500);
		outTime(100000);
	}
	public static void outTime(long timeneed){
		double time=(double)timeneed;
		if(time > 1000){
			time = time/1000.0;
			if(time > 60.0){
				time = time/60;
				System.out.println("Time needed: " + time + "m (" + timeneed + "ms)");
			}else
				System.out.println("Time needed: " + time + "s (" + timeneed + "ms)");
		}else
			System.out.println("Time needed: " + time + "ms");
	}
}
