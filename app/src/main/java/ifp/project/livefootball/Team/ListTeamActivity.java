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
import ifp.project.livefootball.R;

public class ListTeamActivity extends AppCompatActivity {

    private Database db;
    private ArrayList <String> arrayList= new ArrayList<String>();
    private ArrayAdapter <String> arrayAdapter;
    private ListView listView1;
    private Button button1;
    private String textContent;
    protected Intent changeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_team);

        listView1= (ListView) findViewById(R.id.listView1_listTeam);
        button1= (Button) findViewById(R.id.button1_listTeam);

        db= new Database(this);
        arrayList= db.getTeams();

        arrayAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView1.setAdapter(arrayAdapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textContent= adapterView.getItemAtPosition(i).toString();
                changeActivity= new Intent(ListTeamActivity.this, EditTeamActivity.class);
                changeActivity.putExtra("TEAM", textContent).toString();
                finish();
                startActivity(changeActivity);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity= new Intent(ListTeamActivity.this, CreateTeamActivity.class);
                finish();
                startActivity(changeActivity);
            }
        });
    }
}