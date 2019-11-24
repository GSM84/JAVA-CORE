package Lesson_2;

public class MyArrayDataException extends Throwable {
    private int xAxis;
    private int yAxis;
    public MyArrayDataException(int _x, int _y, String _msg) {
        super(_msg);
        this.xAxis = _x;
        this.yAxis = _y;
    }

    @Override
    public String toString() {
        return super.getMessage() + ". Координаты x " + this.xAxis + " y " + this.yAxis;
    }
}
