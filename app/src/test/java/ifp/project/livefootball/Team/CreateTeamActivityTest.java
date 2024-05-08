package ifp.project.livefootball.Team;

import static org.junit.Assert.*;

import org.junit.Test;

import ifp.project.livefootball.R;

public class CreateTeamActivityTest {

    @Test
    public void NotNull(){
        CreateTeamActivity createTeam= new CreateTeamActivity();
        String name= createTeam.getString(R.id.editText1_createTeam);
        assertNotNull(name);
    }
}