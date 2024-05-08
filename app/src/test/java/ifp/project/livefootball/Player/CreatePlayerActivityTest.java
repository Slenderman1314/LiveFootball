package ifp.project.livefootball.Player;

import static org.junit.Assert.*;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ifp.project.livefootball.R;
import ifp.project.livefootball.Team.CreateTeamActivity;
import ifp.project.livefootball.Team.Teams;

public class CreatePlayerActivityTest {

    @Test
    public void NameNotNull(){
        CreatePlayerActivity createPlayer= new CreatePlayerActivity();
        String name= createPlayer.getString(R.id.editText1_playerCreate);
        assertNotNull(name);
    }

    @Test
    public void SpinnerNotNull(){
        CreateTeamActivity createTeam= new CreateTeamActivity();
        String spinner= createTeam.getString(R.id.spinner_team);
        assertNotEquals(spinner, "Seleccione equipo");

    }

}