package ifp.project.livefootball.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import ifp.project.livefootball.Database.Database;
import ifp.project.livefootball.R;

public class RegisterActivity extends AppCompatActivity {
    private Database db;
    protected Intent changeActivity;
    private TextView textView1;
    private TextView textView2;
    private EditText editText1;
    private TextView textView3;
    private EditText editText2;
    private TextView textView4;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private Button button1;
    private Button button2;
    private String TextContent1;
    private String TextContent2;
    private String RadioValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textView1= (TextView) findViewById(R.id.textView1_register);
        textView2= (TextView) findViewById(R.id.textView2_register);
        editText1= (EditText) findViewById(R.id.editText1_register);
        textView3= (TextView) findViewById(R.id.textView3_register);
        editText2= (EditText) findViewById(R.id.editText2_register);
        textView4= (TextView) findViewById(R.id.textView4_register);
        radioButton1= (RadioButton) findViewById(R.id.radioButton1_register);
        radioButton2= (RadioButton) findViewById(R.id.radioButton2_register);
        button1= (Button) findViewById(R.id.button1_register);
        button2= (Button) findViewById(R.id.button2_register);

        db= new Database(this);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextContent1= editText1.getText().toString();
                TextContent2= editText2.getText().toString();


                if ((TextContent1.equals("") || TextContent2.equals("") ) || (TextContent1.equals("") && TextContent2.equals("") )){
                    Toast.makeText(RegisterActivity.this, "Debes rellenar ambos apartados", Toast.LENGTH_SHORT).show();
                } else {
                    if (radioButton1.isChecked()== true){
                        db.insertUser(TextContent1, TextContent2, "Asistente");
                        Toast.makeText(RegisterActivity.this, "La cuenta se creo con exito", Toast.LENGTH_SHORT).show();
                        changeActivity= new Intent(RegisterActivity.this, LogInActivity.class);
                        finish();
                        startActivity(changeActivity);
                    } else if(radioButton2.isChecked()== true){
                        db.insertUser(TextContent1, TextContent2, "Entrenador");
                        Toast.makeText(RegisterActivity.this, "La cuenta se creo con exito", Toast.LENGTH_SHORT).show();
                        changeActivity= new Intent(RegisterActivity.this, LogInActivity.class);
                        finish();
                        startActivity(changeActivity);
                    } else{
                        Toast.makeText(RegisterActivity.this, "Debes indicar cual es el rol que que debes ejercer en la aplicacón.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view){
                changeActivity = new Intent(RegisterActivity.this, LogInActivity.class);
                finish();
                startActivity(changeActivity);
            }

        });

    }
}