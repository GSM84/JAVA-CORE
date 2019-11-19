package Lesson_2.Enum;

public enum DayOfWeek {
    MONDAY("Понедельник", 40),
    TUESDAY("Вторник", 32),
    WEDNESDAY("Среда", 24),
    THURSDAY("Четверг",16),
    FRIDAY("Пятница",8),
    SATURDAY("Суббота",0),
    SUNDAY("Воскресенье",0);

    private String name;
    private int workingHours;

    DayOfWeek(String _name, int _workingHours){
        this.name = _name;
        this.workingHours = _workingHours;
    }

    @Override
    public String toString() {
        switch (this){
            case SATURDAY:
            case SUNDAY: return this.name + ", выходной день, рабочая неделя еще не началась.";
            default:
                return this.name + ", до конца недели осталось " + this.workingHours + " рабочих часов.";
        }
    }
}
