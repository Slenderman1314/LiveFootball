package ifp.project.livefootball.Team;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.MainMenu.MainMenuActivity;
import ifp.project.livefootball.Player.EditPlayerActivity;
import ifp.project.livefootball.R;

public class ListTeamActivity extends AppCompatActivity {

    private Database db;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView1;
    private Button boton1;
    private String textContent;
    protected Intent changeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_team);

        listView1 = (ListView) findViewById(R.id.listView1_listTeam);
        boton1 = (Button) findViewById(R.id.boton1_inicioListTeam);
        db = new Database(this);
        arrayList = db.getTeams();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView1.setAdapter(arrayAdapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textContent = adapterView.getItemAtPosition(i).toString();
                changeActivity = new Intent(ListTeamActivity.this, EditTeamActivity.class);
                changeActivity.putExtra("TEAM", textContent);
                startActivity(changeActivity);
                finish();
            }
        });

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige al usuario al menú
                changeActivity = new Intent(ListTeamActivity.this, MainMenuActivity.class);
                startActivity(changeActivity);
                finish();
            }
        });
    }
}