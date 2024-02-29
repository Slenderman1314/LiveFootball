package ifp.project.livefootball.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "AppDatabase", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS teams (idTeams INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , name VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS players (idPlayer INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, playerName VARCHAR ,idTeam INTEGER, team VARCHAR, FOREIGN KEY (idTeam) REFERENCES teams(idTeams))");
        db.execSQL("CREATE TABLE IF NOT EXISTS footballMatch (idMatch INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , idLocalTeam teams, localScore INTEGER,guestScore INTEGER , idGuestTeam teams, localTeamName String, guestTeamName, localYellowCards INTEGER, guestYellowCards INTEGER, FOREIGN KEY (idLocalTeam) REFERENCES teams(idTeams), FOREIGN KEY (idGuestTeam) REFERENCES teams(idTeams))");
        db.execSQL("CREATE TABLE IF NOT EXISTS user (idUser INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, userName VARCHAR, password VARCHAR, userType VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {


    }

    public String getPass(String name, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("SELECT username FROM user");
        if (db.equals(name)) {
            db.execSQL("SELECT password FROM user WHERE userName LIKE " + name);
            pass = db.toString();
        }
        return pass;
    }

    public String getUser(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("SELECT name FROM user");
        if (db.equals(name)) {
            name = db.toString();
        }
        return name;
    }

    public void insertUser(String name, String pass, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO users VALUES (null, name, pass, role)");
    }

    public ArrayList<String> getMatches() {
        ArrayList<String> matchList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT localScore, guestScore FROM footballMatch INNER JOIN teams ON footballMatch.localTeamName= teams.name INNER JOIN teams ON footballMatch.guestTeamName= teams.name WHERE idGuestTeam= idTeam AND idLocalTeam= idTeam", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                matchList.add(cursor.getString(0) + "-" + cursor.getString(1));
                cursor.moveToNext();
            }
        }
        return matchList;
    }

    public ArrayList<String> getTeams() {
        ArrayList<String> teamList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT name FROM teams", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                teamList.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        return teamList;
    }

    public ArrayList<String> getPlayers() {
        ArrayList<String> playerList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT playerName FROM players INNER JOIN teams ON player.team= teams.name WHERE idTeam= idTeams", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                playerList.add(cursor.getString(0) + "--" + cursor.getString(1));
                cursor.moveToNext();
            }
        }
        return playerList;
    }
}
