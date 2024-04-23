package ifp.project.livefootball.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import ifp.project.livefootball.User;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "AppDatabase", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS teams (idTeams INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , name VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS players (idPlayer INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, playerName VARCHAR ,idTeam INTEGER, team VARCHAR, FOREIGN KEY (idTeam) REFERENCES teams(idTeams))");
        db.execSQL("CREATE TABLE IF NOT EXISTS footballMatch (idMatch INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , idLocalTeam teams, localScore INTEGER,guestScore INTEGER , idGuestTeam teams, localTeamName String, guestTeamName, localYellowCards INTEGER, guestYellowCards INTEGER, FOREIGN KEY (idLocalTeam) REFERENCES teams(idTeams), FOREIGN KEY (idGuestTeam) REFERENCES teams(idTeams))");
        db.execSQL("CREATE TABLE IF NOT EXISTS users (idUser INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, userName VARCHAR, password VARCHAR, userType VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public String getPass(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM users WHERE userName = ?", new String[]{name});

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex("password");
            if (nameColumnIndex != -1) {
                return cursor.getString(nameColumnIndex);
            }
            return cursor.getString(nameColumnIndex);
        } else {
            return null;
        }
    }

    public String getUser(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE userName = ?", new String[]{name});

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex("userName");
            if (nameColumnIndex != -1) {
                return cursor.getString(nameColumnIndex);
            }
        }
        cursor.close();
        return null;
    }

    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", user.getName());
        contentValues.put("password", user.getPass());
        contentValues.put("userType", user.getRole());
        db.insert("users", null, contentValues);
    }

    /**public ArrayList<String> getMatches() {
        ArrayList<String> matchList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String query = "SELECT fm.localScore, fm.guestScore " +
                "FROM footballMatch fm " +
                "INNER JOIN teams t1 ON fm.localTeamName = t1.name " +
                "INNER JOIN teams t2 ON fm.guestTeamName = t2.name " +
                "WHERE t1.idTeams = t2.idTeams";
        cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                matchList.add(cursor.getString(0) + "-" + cursor.getString(1));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return matchList;
    }**/

    public ArrayList<String> getMatches() {
        ArrayList<String> matchList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String query = "SELECT fm.idMatch, t1.name, t2.name " +
                "FROM footballMatch fm " +
                "INNER JOIN teams t1 ON fm.idLocalTeam = t1.idTeams " +
                "INNER JOIN teams t2 ON fm.idGuestTeam = t2.idTeams";
        cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                matchList.add("ID: " + cursor.getString(0) + ", Local Team: " + cursor.getString(1) + ", Guest Team: " + cursor.getString(2));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return matchList;
    }


    public ArrayList<String> getTeams() {
        ArrayList<String> teamList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT name FROM teams", null);
        cursor.moveToLast();
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
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT playerName FROM players INNER JOIN teams ON player.team= teams.name WHERE idTeam= idTeams", null);
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                playerList.add(cursor.getString(0) + "--" + cursor.getString(1));
                cursor.moveToNext();
            }
        }
        return playerList;
    }

    public void printUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                int nameColumnIndex = cursor.getColumnIndex("userName");
                int passColumnIndex = cursor.getColumnIndex("password");
                int roleColumnIndex = cursor.getColumnIndex("userType");

                String name = cursor.getString(nameColumnIndex);
                String pass = cursor.getString(passColumnIndex);
                String role = cursor.getString(roleColumnIndex);
                System.out.println("User: " + name + ", Pass: " + pass + ", Role: " + role);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
