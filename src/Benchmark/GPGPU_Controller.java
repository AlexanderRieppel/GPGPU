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
			System.out.println("Please enter one of the following options:");
			System.out.println("The default options with arraysize 10,100,1000,10000,100000");
			System.out.println("<d> Testing both algorithm");
			System.out.println("<fd> Testing the fact algorithm");
			System.out.println("<sd> Testing the sort algorithm");
			System.out.println("The custom options with custom arraysizes");
			System.out.println("<b> [<ArraySize1>..] Testing both algorithm ");
			System.out.println("<f> [<ArraySize1>..] Testing the fact algorithm");
			System.out.println("<s> [<ArraySize1>..] Testing the sort algorithm");
		}
	}
	public static void bTest(int[] size){
		System.out.println("Starting tests");
		String[] outf = new String[size.length];
		String[] outs = new String[size.length];
		for(int i = 0; i < size.length;i++){
			System.out.println("Starting factor test " + (i+1)+ " with ArraySize: " + size[i]);
			outf[i]=factor_test(size[i]);
		}
		for(int i = 0; i < size.length;i++){
			System.out.println("Starting sort test " + (i+1)+ " with ArraySize: " + size[i]);
			outs[i]=sort_test(size[i]);
		}
		System.out.println("All tests finished!");
		System.out.println("Showing results for factor tests..");
		for(int i = 0; i < outf.length;i++){
			System.out.println("Test " + (i+1) + ". with arraysize " + size[i] + ": " + outf[i]);
		}
		System.out.println("Showing Results for Sort Tests..");
		for(int i = 0; i < outs.length;i++){
			System.out.println("Test " + (i+1) + ". with arraysize " + size[i] + ": " + outs[i]);
		}
		System.out.println("Benchmark finished!");
	}
	public static void fTest(int[] size){
		System.out.println("Starting tests");
		String[] outf = new String[size.length];
		for(int i = 0; i < size.length;i++){
			System.out.println("Starting factor test " + (i+1)+ " with arraysize: " + size[i]);
			outf[i]=factor_test(size[i]);
		}
		System.out.println("All tests finished!");
		System.out.println("Showing results for factor tests..");
		for(int i = 0; i < outf.length;i++){
			System.out.println("Test " + (i+1) + ". with arraysize " + size[i] + ": " + outf[i]);
		}		
		System.out.println("Benchmark finished!");
	}
	public static void sTest(int[] size){
		System.out.println("Starting tests");
		String[] outs = new String[size.length];
		for(int i = 0; i < size.length;i++){
			System.out.println("Starting sort test " + (i+1)+ " with arraysize: " + size[i]);
			outs[i]=sort_test(size[i]);
		}
		System.out.println("All tests finished!");
		System.out.println("Showing results for sort tests..");
		for(int i = 0; i < outs.length;i++){
			System.out.println("Test " + (i+1) + ". with arraysize " + size[i] + ": " + outs[i]);
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
		System.out.println("Loading factor test program...");
		long startt = System.currentTimeMillis();
		long[] todo = new long[n];
		long add = (long) (Math.random()*100+1);
		todo[0] = (long) (Math.random()*100+1);
		for(int i = 1; i < n; i++){
			todo[i] = todo[i-1] + add;
		}
		long endt = System.currentTimeMillis();
		System.out.println("Loading program finished!");
		System.out.println("Needed " + (endt - startt) + "ms");
		String gput = "", cput;
		try {
			gput = outTime(Factorisierung.GPU_factor(todo));
			System.out.println("Time needed: " + gput);
		} catch (IOException e) {
			System.err.println("Error in GPU test!!");
			System.exit(0);
		}
		cput = outTime(Factorisierung.CPU_factor(todo));
		System.out.println("Time needed: " + cput);
		return "CPU: " + cput + " GPU: " + gput;
	}
	public static String sort_test(int n){
		System.out.println("Loading sort test program...");
		long startt = System.currentTimeMillis();
		long[] todo = new long[n];
		for(int i = 1; i < n; i++){
			todo[i] =(long) (Math.random()*100+1);
		}
		long endt = System.currentTimeMillis();
		System.out.println("Loading program finished!");
		System.out.println("Needed " + (endt - startt) + "ms");
		String gput = "", cput;
		try {
			gput = outTime(Sortieren.GPU_sort(todo));
			System.out.println("Time needed: " + gput);
		} catch (IOException e) {
			System.err.println("Error in GPU Test!!");
			System.exit(0);
		}
		cput = outTime(Sortieren.CPU_sort(todo));
		System.out.println("Time needed: " + cput);
		return "CPU: " + cput + " GPU: " + gput;
	}
}
