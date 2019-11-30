package Lesson_5;

public class ParallelThread extends Thread {
    private float[] array;
    private String name;
    public ParallelThread(float[] _arr, String _name){
        this.array = _arr;
        this.name  = _name;
    }

    public void run() {
        System.out.println(this.name + " start processing.");
        for (int i = 0; i < array.length; i++) {
            this.array[i] = (float)(array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println(this.name + " finished.");
    }

    public float[] getArray(){
        return this.array;
    }
}
