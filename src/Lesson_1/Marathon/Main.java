package Lesson_1.Marathon;

public class Main {
    public static void main(String[] args) {
        /*Competitor[] competitors = {new Human("Боб"), new Cat("Барсик"), new Dog("Бобик")};
        Obstacle[] course = {new Cross(80), new Water(2), new Wall(1), new Cross(120)};
        for (Competitor c : competitors) {
            for (Obstacle o : course) {
                o.doIt(c);
                if (!c.isOnDistance()) break;
            }
        }
        for (Competitor c : competitors) {
            c.info();
        }
         */
        Team team;
        Course course = new Course(new Cross(80), new Water(2), new Wall(1), new Cross(120));
        try{
            team = new Team("NewTeam", new Human("Боб"), new Cat("Барсик"), new Dog("Бобик"));
            team.printTeamInfo();
            course.passCoure(team);
            team.pritSuccessParticipants();
        } catch (ToMuchParticipants e) {
            System.err.println(e);
        }
    }
}