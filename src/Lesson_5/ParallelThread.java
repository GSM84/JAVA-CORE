package Lesson_5;

public class ParallelThread extends Thread {
    private float[] array;
    private int shift;
    private String name;
    public ParallelThread(float[] _arr, String _name, int _shift){
        this.array = _arr;
        this.name  = _name;
        this.shift = _shift;
    }

    public void run() {
        System.out.println(this.name + " start processing.");
        for (int i = 0; i < array.length; i++) {
            this.array[i] = (float)(array[i] * Math.sin(0.2f + shift / 5) * Math.cos(0.2f + shift / 5) * Math.cos(0.4f + shift / 2));
            shift++;
        }
        System.out.println(this.name + " finished.");
    }

    public float[] getArray(){
        return this.array;
    }
}
