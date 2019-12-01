package Lesson_5;

public class Main {
    static final int size = 10000000;
    float[] arr = new float[size];

    public static void main(String[] args) {
        Main t = new Main();
        t.initArray();
        t.singleTheread(t.arr);
        t.initArray();
        t.multiThread(t.arr, 5);
        t.initArray();
        t.multiThreadV2(t.arr, 5);
    }

    void initArray(){
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1;
        }
    }

    void singleTheread(float[] _arr){
        long start = System.currentTimeMillis();
        for (int i = 0; i < _arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println("Single thread job was done in: "+ (System.currentTimeMillis() - start) + " miliseconds.");
    }

    void multiThread(float[] _arr, int _threadCount){
        int startPosition = 0;
        int endPosition   = 0;
        int grpSize = (size % _threadCount == 0)?(size / _threadCount):(size / _threadCount) + 1;
        ParallelThread[] threads = new ParallelThread[_threadCount];

        long start = System.currentTimeMillis();
        // split into array set
        for (int i = 0; i < _threadCount; i++) {
            if (i == _threadCount - 1)
                grpSize = size - startPosition;
            float[] temp = new float[grpSize];
            System.arraycopy(_arr, startPosition, temp, 0, grpSize);
            ParallelThread t = new ParallelThread(temp, "Thread_"+i, startPosition);
            threads[i] = t;
            t.start();

            startPosition += grpSize;
        }
        //wait until all processes would be finished
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // collect result data
        arr = processResult(threads, size);

        System.out.println("Multithread job was done in: "+ (System.currentTimeMillis() - start) + " miliseconds.");
    }

    void multiThreadV2(float[] _arr, int _threadCount){
        ParallelThreadV2[] threads = new ParallelThreadV2[_threadCount];
        long start = System.currentTimeMillis();
        // Start threads
        for (int i = 0; i < _threadCount; i++) {
            ParallelThreadV2 t = new ParallelThreadV2(_arr, "TRD_"+i, i, _threadCount);
            threads[i] = t;
            t.start();
        }
        //wait until all processes would be finished
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        arr = processResult(threads, size);

        System.out.println("Multithread job was done in: "+ (System.currentTimeMillis() - start) + " miliseconds.");
    }

    public float[] processResult(Thread[] _threads, int _resultSize){
        float[] result = new float[_resultSize];
        if (_threads.getClass().getComponentType().equals(ParallelThread.class)) {
            ParallelThread[] threads = (ParallelThread[]) _threads;
            int endPosition = 0;
            for (int i = 0; i < threads.length; i++) {
                System.arraycopy(threads[i].getArray(), 0, result, endPosition, threads[i].getArray().length);
                endPosition += threads[i].getArray().length;
            }
        }else if (_threads.getClass().getComponentType().equals(ParallelThreadV2.class)){
            ParallelThreadV2[] threads = (ParallelThreadV2[]) _threads;
            int endPosition = 0;
            for (int i = 0; i < threads.length; i++) {
                System.arraycopy(threads[i].getArray(), 0, result, endPosition, threads[i].getArray().length);
                endPosition += threads[i].getArray().length;
            }
        }
        return result;
    }
}
