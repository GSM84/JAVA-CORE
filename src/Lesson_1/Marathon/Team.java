package Lesson_1.Marathon;

public class Team {
    private String name;
    private Competitor[] participants;

    public Team(String _name, Competitor... _participant) throws ToMuchParticipants {
        this.name = _name;
        if (_participant.length <= 4){
            this.participants = _participant;
        }else{
            throw new ToMuchParticipants(_participant.length);
        }
    }

    public void printTeamInfo(){
        for (Competitor c : this.participants) {
            c.info();
        }
    }

    public void pritSuccessParticipants(){
        for (Competitor c : this.participants) {
            if (c.isOnDistance())
                c.info();
        }
    }

    public Competitor[] getParticipants(){
        return this.participants;
    }

}
