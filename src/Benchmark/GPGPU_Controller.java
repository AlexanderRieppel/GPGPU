package Benchmark;

import java.io.IOException;

public class GPGPU_Controller {
	public static void main(String[] args){
		sort_test();
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
	public static void foctor_test(){
		System.out.println("Loading Factor Test Programm...");
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
			System.err.println("Error in GPU Test!!");
			System.exit(0);
		}
		outTime(Factorisierung.CPU_factor(todo));
	}
	public static void sort_test(){
		System.out.println("Loading Sort Test Programm...");
		long startt = System.currentTimeMillis();
		int n = 100000;
		long[] todo = new long[n];
		for(int i = 1; i < n; i++){
			todo[i] =(long) (Math.random()*100+1);
		}
		long endt = System.currentTimeMillis();
		System.out.println("Loading Programm finished!");
		System.out.println("Needed " + (endt - startt) + "ms");
		try {
			outTime(Sortieren.GPU_sort(todo));
		} catch (IOException e) {
			System.err.println("Error in GPU Test!!");
			System.exit(0);
		}
		outTime(Sortieren.CPU_sort(todo));
	}
}
