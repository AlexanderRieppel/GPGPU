package Benchmark;

import java.io.IOException;
import java.util.Arrays;

import com.nativelibs4java.opencl.CLDevice;
import com.nativelibs4java.opencl.CLDevice.Type;
import com.nativelibs4java.opencl.CLException;
import com.nativelibs4java.opencl.CLPlatform;
import com.nativelibs4java.opencl.JavaCL;
/**
 * This Class Manages the userinput and controlls the tests
 * @author Dominik Backhausen, Alexander Rieppel
 */
public class GPGPU_Controller {
	private static long startp,timefc, timefg,timesc, timesg;
	public static void main(String[] args){
		startp = System.currentTimeMillis();
		timefc = 0;
		timefg=0;
		timesc = 0;
		timesg=0;
		try{
			boolean wronginput = false;
			if(!(args.length >= 1)){
				wronginput =true;
			}else{
				if(args[0].equalsIgnoreCase("d")){
					bTest(1000,5000);
				}else if(args[0].equalsIgnoreCase("fd")){
					fTest(1000,5000);
				}
				else if(args[0].equalsIgnoreCase("sd")){
					sTest(1000,5000);
				}else if(args[0].equalsIgnoreCase("b")){
					int n = Integer.parseInt(args[1]);
					int size = Integer.parseInt(args[2]);
					bTest(n,size);
				}
				else if(args[0].equalsIgnoreCase("f")){
					int n = Integer.parseInt(args[1]);
					int size = Integer.parseInt(args[2]);
					fTest(n,size);
				}
				else if(args[0].equalsIgnoreCase("s")){
					int n = Integer.parseInt(args[1]);
					int size = Integer.parseInt(args[2]);
					sTest(n,size);
				}else
					wronginput = true;
			}
			if(wronginput){
				System.out.println("Please enter one of the following options:");
				System.out.println("The default options with 1000 Tests(N) and 5000 ArraySize");
				System.out.println("<d> Testing both algorithm");
				System.out.println("<fd> Testing the fact algorithm");
				System.out.println("<sd> Testing the sort algorithm");
				System.out.println("The custom options with custom arraysizes");
				System.out.println("<b> <N> <ArraySize> Testing both algorithm ");
				System.out.println("<f> <N> <ArraySize> Testing the fact algorithm");
				System.out.println("<s> <N> <ArraySize> Testing the sort algorithm");
			}
		}catch(NumberFormatException e){
			System.err.println("Please enter Numbers");
		}catch(InterruptedException e) {
			System.err.println("Thread Error!");
		}catch(CLException e){
			System.err.println("Out of Ressources!");
		}
	}
	/**
	 * This Tests both test types
	 * @param size sizes of the Test Arrays
	 * @throws InterruptedException
	 */
	public static void bTest(int n, int size) throws InterruptedException{
		showHW();
		System.out.println("Starting tests");
		String[] outf = new String[n];
		String[] outs = new String[n];
		for(int i = 0; i < n;i++){
			System.out.println("Starting factor test " + (i+1)+ " with ArraySize: " + size);
			outf[i]=factor_test(size);
			System.out.println();
			//Thread.sleep(5000);
		}
		for(int i = 0; i < n;i++){
			System.out.println("Starting sort test " + (i+1)+ " with ArraySize: " + size);
			outs[i]=sort_test(size);
			System.out.println();
			//Thread.sleep(5000);
		}
		System.out.println("All tests finished!\n");
		System.out.println("Showing results for factor tests..");
		for(int i = 0; i < outf.length;i++){
			System.out.println("Test " + (i+1) + ". with arraysize " + size + ": " + outf[i]);
		}
		System.out.println("Average Time GPU: " + outTime((timefg/n)));
		System.out.println("Average Time CPU: " + outTime((timefc/n)) + "\n");
		System.out.println("Showing Results for Sort Tests..");
		for(int i = 0; i < outs.length;i++){
			System.out.println("Test " + (i+1) + ". with arraysize " + size + ": " + outs[i]);
		}
		System.out.println("Average Time GPU: " + outTime((timesg/n)));
		System.out.println("Average Time CPU: " + outTime((timesc/n)) + "\n");
		System.out.println("Overall Programm runtime: " + outTime((System.currentTimeMillis()-startp)));
		System.out.println("Benchmark finished!");
	}
	/**
	 * This runs only the factor test
	 * @param size sizes of the Test Arrays
	 * @throws InterruptedException
	 */
	public static void fTest(int n, int size) throws InterruptedException{
		showHW();
		System.out.println("Starting tests");
		String[] outf = new String[n];
		for(int i = 0; i < n;i++){
			System.out.println("Starting factor test " + (i+1)+ " with arraysize: " + size);
			outf[i]=factor_test(size);
			System.out.println();
			//Thread.sleep(5000);
		}
		System.out.println("All tests finished!");
		System.out.println("Showing results for factor tests..");
		for(int i = 0; i < outf.length;i++){
			System.out.println("Test " + (i+1) + ". with arraysize " + size + ": " + outf[i]);
		}
		System.out.println("Average Time GPU: " + outTime((timefg/n)));
		System.out.println("Average Time CPU: " + outTime((timefc/n)) + "\n");
		System.out.println("Overall Programm runtime: " + outTime((System.currentTimeMillis()-startp)));
		System.out.println("Benchmark finished!");
	}
	/**
	 * This runs only the sort test
	 * @param size sizes of the Test Arrays
	 * @throws InterruptedException
	 */
	public static void sTest(int n, int size) throws InterruptedException{
		showHW();
		System.out.println("Starting tests");
		String[] outs = new String[n];
		for(int i = 0; i < n;i++){
			System.out.println("Starting sort test " + (i+1)+ " with arraysize: " + size);
			outs[i]=sort_test(size);
			System.out.println();
			//Thread.sleep(5000);
		}
		System.out.println("All tests finished!");
		System.out.println("Showing results for sort tests..");
		for(int i = 0; i < outs.length;i++){
			System.out.println("Test " + (i+1) + ". with arraysize " + size + ": " + outs[i]);
		}
		System.out.println("Average Time GPU: " + outTime((timesg/n)));
		System.out.println("Average Time CPU: " + outTime((timesc/n)) + "\n");
		System.out.println("Overall Programm runtime: " + outTime((System.currentTimeMillis()-startp)));
		System.out.println("Benchmark finished!");
	}
	/**
	 * This convert ms to ms, s  or m
	 * @param timeneed time to convert
	 * @return converted time
	 */
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
	/**
	 * This is one factor test
	 * @param n size of the List
	 * @return
	 */
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
		System.out.println("This took " + (endt - startt) + "ms");
		long gput =0, cput=0;
		try {
			gput = Factorisierung.GPU_factor(todo);
			System.out.println("Time needed: " + gput);
		} catch (IOException e) {
			System.err.println("Error in GPU test!!");
			System.exit(0);
		}
		cput = Factorisierung.CPU_factor(todo);
		System.out.println("Time needed: " + cput);
		openres(todo);
		timefc += cput;
		timefg += gput;
		return "CPU: " + outTime(cput) + " GPU: " + outTime(gput);
	}
	/**
	 * This is one sort test
	 * @param n size of the List
	 * @return
	 */
	public static String sort_test(int n){
		System.out.println("Loading sort test program...");
		long startt = System.currentTimeMillis();
		long[] todo = new long[n];
		for(int i = 1; i < n; i++){
			todo[i] =(long) (Math.random()*1000000000+1);
		}
		long endt = System.currentTimeMillis();
		System.out.println("Loading program finished!");
		System.out.println("This took " + (endt - startt) + "ms");
		long gput = 0, cput;
		try {
			gput = Sortieren.GPU_sort(todo);
			System.out.println("Time needed: " + gput);
		} catch (IOException e) {
			System.err.println("Error in GPU Test!!");
			System.exit(0);
		}
		cput = Sortieren.CPU_sort(todo);
		System.out.println("Time needed: " + cput);
		timesc += cput;
		timesg += gput;
		openres(todo);
		return "CPU: " + outTime(cput) + " GPU: " + outTime(gput);
	}
	/**
	 * This sould open the Ressources
	 * @param todo
	 */
	public static void openres(long[] todo){
		Arrays.fill(todo, 0);
		todo =null;
	}
	/**
	 * This show the HW on which the test is running
	 */
	public static void showHW(){
		System.out.println("Tests run on:");
		for(CLPlatform p : JavaCL.listPlatforms())
			for(CLDevice d1 :p.listAllDevices(false))
				System.out.println(d1.getName());
		System.out.println();
	}
}
