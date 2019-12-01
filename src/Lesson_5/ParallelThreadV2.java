package Lesson_5;

import java.util.Arrays;

public class ParallelThreadV2 extends Thread{
    private float[] source;
    private float[] dest;
    private String  name;
    private int     threadNum;
    private int     threadCount;
    private int     grpSize;
    private int     startPos;
    private int     endPos;

    public ParallelThreadV2(float[] _arr, String _name, int _threadNum, int _threadCount){
        this.source      = _arr;
        this.name        = _name;
        this.threadNum   = _threadNum;
        this.threadCount = _threadCount;
    }

    public void run() {
        System.out.println(this.name + " start processingv2.");
        grpSize = (source.length % threadCount == 0)?(source.length / threadCount):(source.length / threadCount) + 1;
        startPos = threadNum * grpSize;
        if (threadNum == (threadCount - 1))
            endPos = source.length;
        else
            endPos = startPos + grpSize;

        dest = Arrays.copyOfRange(source, startPos, endPos);
        for (int i = 0; i < dest.length; i++) {
            this.dest[i] = (float)(dest[i] * Math.sin(0.2f + startPos / 5) * Math.cos(0.2f + startPos / 5) * Math.cos(0.4f + startPos / 2));
            startPos++;
        }
        System.out.println(this.name + " v2 finished.");
    }

    public float[] getArray(){
        return this.dest;
    }
}
