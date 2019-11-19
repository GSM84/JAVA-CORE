package Lesson_2;

import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String[][] mtx = {
                {"1","a","1","1"},
                {"1","1","1","1"},
                {"1","1","a","1"},
                {"1","1","1","1"}
                };
        try {
            System.out.println(calculateSum(mtx));
        }catch (MyArraySizeException e){
            System.err.println(e);
        }
    }

    static int calculateSum(String[][] _matrix) throws MyArraySizeException {
        int totalSum = 0;
        if (_matrix.length != 4 || _matrix[0].length != 4)
            throw new MyArraySizeException("Не корректная размерность массива. Матрица должна быть 4 х 4.");

        for (int i = 0; i < _matrix.length; i++) {
            for (int j = 0; j < _matrix[i].length; j++) {
                try {
                    if (!Pattern.matches("^\\d+$", _matrix[i][j])) {
                        throw new MyArrayDataException(i, j, "Некооректное значение элемента");
                    } else
                        totalSum += Integer.valueOf(_matrix[i][j]);
                }catch (MyArrayDataException e){
                    System.err.println(e);
                }
            }
        }
        return totalSum;
    }
}
