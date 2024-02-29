package ifp.project.livefootball.Team;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.R;

public class CreateTeamActivity extends AppCompatActivity {

    private EditText editText1;
    private ListView listView1;

    private Database db;
    protected Intent changeActivity;

    private String textContent;
    private ArrayList <String> arrayList= new ArrayList<String>();
    private ArrayAdapter <String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        editText1= (EditText) findViewById(R.id.editText1_createTeam);
        listView1= (ListView) findViewById(R.id.listView1_createTeam);

        db= new Database(this);
        arrayList= db.getPlayers();

        arrayAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView1.setAdapter(arrayAdapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}