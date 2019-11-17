package Lesson_1.Marathon;

public class Course {
    private Obstacle[] course;
    public Course(Obstacle... _course){
        this.course = _course;
    }

    void passCoure(Team _team){
        for (Competitor c : _team.getParticipants()) {
            for (Obstacle o : this.course) {
                o.doIt(c);
                if (!c.isOnDistance())
                    break;
            }
        }
    }
}
