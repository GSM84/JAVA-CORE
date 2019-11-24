package Lesson_1.Marathon;

class ToMuchParticipants extends Throwable {
    int count;
    public ToMuchParticipants(int _count) {
        this.count = _count;
    }

    @Override
    public String toString() {
        return "Слишком много участников в команде " + this.count + ". Максимальный размер конмады 4.";
    }
}
