package Benchmark;

import java.io.IOException;

public class GPGPU_Controller {
	public static void main(String[] args){
		boolean wronginput = false;
		if(!(args.length >= 1)){
			wronginput =true;
		}else{
			if(args[0].equalsIgnoreCase("d")){
				int[] s = {10,100,1000,10000,100000};
				bTest(s);
			}else if(args[0].equalsIgnoreCase("fd")){
				int[] s = {10,100,1000,10000,100000};
				fTest(s);
			}
			else if(args[0].equalsIgnoreCase("sd")){
				int[] s = {10,100,1000,10000,100000};
				sTest(s);
			}else if(args[0].equalsIgnoreCase("b")){
				int[] s = new int[args.length-1];
				for(int i = 1; i < args.length;i++)
					s[i-1] = Integer.parseInt(args[i]);
				bTest(s);
			}
			else if(args[0].equalsIgnoreCase("f")){
				int[] s = new int[args.length-1];
				for(int i = 1; i < args.length;i++)
					s[i-1] = Integer.parseInt(args[i]);
				fTest(s);
			}
			else if(args[0].equalsIgnoreCase("s")){
				int[] s = new int[args.length-1];
				for(int i = 1; i < args.length;i++)
					s[i-1] = Integer.parseInt(args[i]);
				sTest(s);
			}else
				wronginput = true;
		}
		if(wronginput){
			System.out.println("Please Enter one of the following Options:");
			System.out.println("The Default Options with ArraySize 10,100,1000,10000,100000");
			System.out.println("<d> Test bothe Allgorithmen");
			System.out.println("<fd> Test the Fact Allgorithmen");
			System.out.println("<sd> Test the Sort Allgorithmen");
			System.out.println("The Custom Options with custom ArraySizes");
			System.out.println("<b> [<ArraySize1>..] Test bothe Allgorithmen ");
			System.out.println("<f> [<ArraySize1>..] Test the Fact Allgorithmen");
			System.out.println("<s> [<ArraySize1>..] Test the Sort Allgorithmen");
		}
	}
	public static void bTest(int[] size){
		System.out.println("Starting Tests");
		String[] outf = new String[size.length];
		String[] outs = new String[size.length];
		for(int i = 0; i < size.length;i++){
			System.out.println("Starting Factor Test " + (i+1)+ " with ArraySize: " + size[i]);
			outf[i]=factor_test(size[i]);
		}
		for(int i = 0; i < size.length;i++){
			System.out.println("Starting Sort Test " + (i+1)+ " with ArraySize: " + size[i]);
			outs[i]=sort_test(size[i]);
		}
		System.out.println("All Tests Finised!");
		System.out.println("Showing Results for Factor Tests..");
		for(int i = 0; i < outf.length;i++){
			System.out.println("Test " + (i+1) + ". with ArraySize " + size[i] + ": " + outf[i]);
		}
		System.out.println("Showing Results for Sort Tests..");
		for(int i = 0; i < outs.length;i++){
			System.out.println("Test " + (i+1) + ". with ArraySize " + size[i] + ": " + outs[i]);
		}
		System.out.println("Benchmark finished!");
	}
	public static void fTest(int[] size){
		System.out.println("Starting Tests");
		String[] outf = new String[size.length];
		for(int i = 0; i < size.length;i++){
			System.out.println("Starting Factor Test " + (i+1)+ " with ArraySize: " + size[i]);
			outf[i]=factor_test(size[i]);
		}
		System.out.println("All Tests Finised!");
		System.out.println("Showing Results for Factor Tests..");
		for(int i = 0; i < outf.length;i++){
			System.out.println("Test " + (i+1) + ". with ArraySize " + size[i] + ": " + outf[i]);
		}		
		System.out.println("Benchmark finished!");
	}
	public static void sTest(int[] size){
		System.out.println("Starting Tests");
		String[] outs = new String[size.length];
		for(int i = 0; i < size.length;i++){
			System.out.println("Starting Sort Test " + (i+1)+ " with ArraySize: " + size[i]);
			outs[i]=sort_test(size[i]);
		}
		System.out.println("All Tests Finised!");
		System.out.println("Showing Results for Sort Tests..");
		for(int i = 0; i < outs.length;i++){
			System.out.println("Test " + (i+1) + ". with ArraySize " + size[i] + ": " + outs[i]);
		}
		System.out.println("Benchmark finished!");
	}
	public static String outTime(long timeneed){
		String ret;
		double time=(double)timeneed;
		if(time > 1000){
			time = time/1000.0;
			if(time > 60.0){
				time = time/60;
				ret = time + "m (" + timeneed + "ms)";
			}else
				ret = time + "s (" + timeneed + "ms)";
		}else
			ret= time + "ms";
		return ret;
	}
	public static String factor_test(int n){
		System.out.println("Loading Factor Test Programm...");
		long startt = System.currentTimeMillis();
		long[] todo = new long[n];
		long add = (long) (Math.random()*100+1);
		todo[0] = (long) (Math.random()*100+1);
		for(int i = 1; i < n; i++){
			todo[i] = todo[i-1] + add;
		}
		long endt = System.currentTimeMillis();
		System.out.println("Loading Programm finished!");
		System.out.println("Needed " + (endt - startt) + "ms");
		String gput = "", cput;
		try {
			gput = outTime(Factorisierung.GPU_factor(todo));
			System.out.println("Time Needed: " + gput);
		} catch (IOException e) {
			System.err.println("Error in GPU Test!!");
			System.exit(0);
		}
		cput = outTime(Factorisierung.CPU_factor(todo));
		System.out.println("Time Needed: " + cput);
		return "CPU: " + cput + " GPU: " + gput;
	}
	public static String sort_test(int n){
		System.out.println("Loading Sort Test Programm...");
		long startt = System.currentTimeMillis();
		long[] todo = new long[n];
		for(int i = 1; i < n; i++){
			todo[i] =(long) (Math.random()*100+1);
		}
		long endt = System.currentTimeMillis();
		System.out.println("Loading Programm finished!");
		System.out.println("Needed " + (endt - startt) + "ms");
		String gput = "", cput;
		try {
			gput = outTime(Sortieren.GPU_sort(todo));
			System.out.println("Time Needed: " + gput);
		} catch (IOException e) {
			System.err.println("Error in GPU Test!!");
			System.exit(0);
		}
		cput = outTime(Sortieren.CPU_sort(todo));
		System.out.println("Time Needed: " + cput);
		return "CPU: " + cput + " GPU: " + gput;
	}
}
