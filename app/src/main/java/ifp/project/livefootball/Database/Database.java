package ifp.project.livefootball.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import ifp.project.livefootball.Account.User;
import ifp.project.livefootball.Team.Teams;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "AppDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS teams (idTeams INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , name VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS players (idPlayer INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, playerName VARCHAR ,idTeam INTEGER, team VARCHAR, FOREIGN KEY (idTeam) REFERENCES teams(idTeams))");
        db.execSQL("CREATE TABLE IF NOT EXISTS footballMatch (idMatch INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , idLocalTeam teams, idGuestTeam teams, localScore INTEGER, guestScore INTEGER, localTeamName String, guestTeamName String, localYellowCards INTEGER, guestYellowCards INTEGER, FOREIGN KEY (idLocalTeam) REFERENCES teams(idTeams), FOREIGN KEY (idGuestTeam) REFERENCES teams(idTeams))");
        db.execSQL("CREATE TABLE IF NOT EXISTS users (idUser INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, userName VARCHAR, password VARCHAR, userType VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    // Método de inserción para equipos
    public void insertTeam(String teamName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", teamName);
        db.insert("teams", null, contentValues);
    }

    // Método de actualización para equipos
    public void updateTeamName(int teamId, String newTeamName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", newTeamName);
        db.update("teams", contentValues, "idTeams = ?", new String[]{String.valueOf(teamId)});
        db.close();
    }

    // Método de inserción para jugadores
    public void insertPlayer(String playerName, int idTeam, String teamName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("playerName", playerName);
        contentValues.put("idTeam", getTeamId(teamName)); // Obtener el ID del equipo
        contentValues.put("team", teamName); // Agregar el nombre del equipo
        db.insert("players", null, contentValues);
    }

    // Método de actualización para jugadores
    public void updatePlayerTeam(String oldPlayerName, String newPlayerName, int newTeamId, String newTeamName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("playerName", newPlayerName);
        contentValues.put("idTeam", newTeamId);
        contentValues.put("team", newTeamName); // Agregar esta línea para actualizar el nombre del equipo
        String whereClause = "playerName = ?";
        String[] whereArgs = {oldPlayerName};
        db.update("players", contentValues, whereClause, whereArgs);
    }

    // Método de inserción para partidos
    public void insertMatch(int localTeamId, int guestTeamId, String nameLocalTeam, String nameGuestTeam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idLocalTeam", localTeamId);
        contentValues.put("idGuestTeam", guestTeamId);
        contentValues.put("localTeamName", nameLocalTeam);
        contentValues.put("guestTeamName", nameGuestTeam);
        db.insert("footballMatch", null, contentValues);
    }

    // Método de actualización para partidos
    public void updateMatch(int matchId, int localTeamId, int guestTeamId, String nameLocalTeam, String nameGuestTeam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idLocalTeam", localTeamId);
        contentValues.put("idGuestTeam", guestTeamId);
        contentValues.put("localTeamName", nameLocalTeam);
        contentValues.put("guestTeamName", nameGuestTeam);
        String whereClause = "idMatch = ?";
        String[] whereArgs = {String.valueOf(matchId)};
        db.update("footballMatch", contentValues, whereClause, whereArgs);
    }

    // Método de eliminación para partidos
    public void deleteMatch(int idMatch) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("footballMatch", "idMatch = ?", new String[]{String.valueOf(idMatch)});
    }

    // Método para conseguir el password del usuario
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

    // Método para conseguir el usuario
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

    // Método para inseertar un usuario nuevo
    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", user.getName());
        contentValues.put("password", user.getPass());
        contentValues.put("userType", user.getRole());
        db.insert("users", null, contentValues);
    }

    // Método para listar partidos
    public ArrayList<String> getMatches() {
        ArrayList<String> matchList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String query = "SELECT fm.idMatch, fm.localTeamName, fm.guestTeamName FROM footballMatch fm";
        cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String matchInfo = "ID Partido: " + cursor.getString(0) + " - Equipo Local: " + cursor.getString(1) + " - Equipo Visitante: " + cursor.getString(2);
                matchList.add(matchInfo);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return matchList;
    }

    // Método para listar equipos
    public ArrayList<Teams> getTeams() {
        ArrayList<Teams> teamList = new ArrayList<Teams>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT idTeams, name FROM teams", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Teams team = new Teams(cursor.getInt(0), cursor.getString(1));
                teamList.add(team);
                cursor.moveToNext();
            }
        }
        return teamList;
    }

    // Método para conseguir el id de un equipo
    private int getTeamId(String teamName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT idTeams FROM teams WHERE name = ?", new String[]{teamName});
        try {
            int columnIndex = cursor.getColumnIndex("idTeams");
            if (cursor.moveToFirst() && columnIndex != -1) {
                return cursor.getInt(columnIndex);
            }
            return -1; // Equipo no encontrado
        } finally {
            cursor.close();
        }
    }

    // Método para conseguir el equipo de un jugador
    public String getPlayerTeam(String playerName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT team FROM players WHERE playerName = ?", new String[]{playerName});
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        cursor.close();
        return null;
    }

    // Método para listar jugadores
    public ArrayList<String> getPlayers() {
        ArrayList<String> playerList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT playerName FROM players", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                playerList.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return playerList;
    }
}