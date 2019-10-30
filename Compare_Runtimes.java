import java.util.Random;

class Concurrency_SingleThread implements Runnable {

    @Override
    public void run() {
        int sum = 0;
        long startTime = System.nanoTime();

        for (int i = 0; i < Compare_Runtimes.size; i++) {
            sum += Compare_Runtimes.bigBoy[i];

        }

        long endTime = System.nanoTime();
        long runtimeSingle = endTime - startTime;

        Compare_Runtimes.runtimeSingle = runtimeSingle;
        Compare_Runtimes.sumSingle = sum;
    }
}

class Concurrency_MultiThread_Yin implements Runnable {  //First half of array
    @Override
    public void run() {
        int sum = 0;
        long startTime = System.nanoTime();

        for (int i = 0; i < (Compare_Runtimes.size / 2); i++) {
            sum += Compare_Runtimes.bigBoy[i];
        }

        long endTime = System.nanoTime();

        Compare_Runtimes.sumMultiYin = sum;
        Compare_Runtimes.runtimeMultiYin = (endTime - startTime);
    }
}

class Concurrency_MultiThread_Yang implements Runnable { //Second half of array
    @Override
    public void run() {
        int sum = 0;
        long startTime = System.nanoTime();

        for (int i = (Compare_Runtimes.size / 2); i < Compare_Runtimes.size; i++) {
            sum += Compare_Runtimes.bigBoy[i];
        }
        long endTime = System.nanoTime();

        Compare_Runtimes.sumMultiYang = sum;
        Compare_Runtimes.runtimeMultiYang = (endTime - startTime);
    }
}


public class Compare_Runtimes {

    static int size = 200000000;
    static int[] bigBoy = new int[size];
    static long sumSingle = 0;
    static long sumMultiYin = 0;
    static long sumMultiYang = 0;
    static long runtimeSingle = 0;
    static long runtimeMultiYin = 0;
    static long runtimeMultiYang = 0;
    static long runtimeMultiFinal = 0;

    public static void main(String[] args) {

        System.out.print("\nProgram Started: Prepare to compare!\n\nNow filling array of 200,000,000... ");

        //Fill BigBoy array
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            bigBoy[i] = r.nextInt((10 - 1) + 1) + 1; //Random int between 1-10 inclusive
        }
        System.out.println("Done!\n\n\n");


        //Initialization and launch of single threaded process
        System.out.print(" ~ Single-Threaded Process ~\n   Processing... ");

        Thread singleThread = new Thread(new Concurrency_SingleThread());
        singleThread.start();
        try {
            singleThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Done!\n");
        System.out.println("Sum of all random numbers: " + sumSingle);
        System.out.println("   -Elapsed time in NanoSeconds: " + runtimeSingle + "\n");


        //Initialization and launch of multi threaded process
        System.out.print("\n\n ~ Multi-Threaded Process ~\n   Processing... ");
        long startTime = System.nanoTime();

        Thread multiThread1 = new Thread(new Concurrency_MultiThread_Yin());
        Thread multiThread2 = new Thread(new Concurrency_MultiThread_Yang());

        multiThread1.start();
        multiThread2.start();

        try {
            multiThread1.join();
            multiThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();

        //Print messages breaking down multi results
        System.out.println("Done!\n");
        System.out.println("Sum of all random numbers: " + (sumMultiYin + sumMultiYang));
        System.out.print("   -Elapsed time in NanoSeconds: ");
        if (runtimeMultiYin > runtimeMultiYang) { //This picks the longer multi runtime, as it would be the most accurate
            runtimeMultiFinal = runtimeMultiYin;
            System.out.println(runtimeMultiYin);
        }
        else {
            runtimeMultiFinal = runtimeMultiYang;
            System.out.println(runtimeMultiYang);
        }


        //Final analysis, The good stuff
        System.out.println("\n\n\n ~ Final Results ~");
        if (runtimeMultiFinal > runtimeSingle) {
            System.out.println("Single-Threaded process is faster by " + (runtimeMultiFinal - runtimeSingle) + " nanoseconds.");
        }
        else if (runtimeMultiFinal < runtimeSingle) {
            System.out.println("Multi-Threaded process is faster by " + (runtimeSingle - runtimeMultiFinal) + " nanoseconds.");
        }
        else {
            System.out.println("It was a tie, can you believe it?! Me either... Time: " + runtimeSingle);
        }
    }
}