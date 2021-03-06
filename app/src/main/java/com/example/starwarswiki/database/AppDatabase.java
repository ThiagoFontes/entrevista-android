package com.example.starwarswiki.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.starwarswiki.structural.FavLogItem;
import com.example.starwarswiki.structural.Person;
import com.example.starwarswiki.structural.Planet;
import com.example.starwarswiki.structural.Specie;

@Database(entities = {Person.class, Planet.class, Specie.class, FavLogItem.class}, version = 1,  exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract PeopleDAO peopleDAO();

    public abstract PlanetDAO planetDAO();

    public abstract SpecieDAO specieDAO();

    public abstract FavLogDAO favLogDAO();

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder( context.getApplicationContext(), AppDatabase.class, "app-database").build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sDatabaseCallback  = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new populateDBAsync(INSTANCE).execute();
        }
    };


    private static class populateDBAsync extends AsyncTask {
        private final PeopleDAO peopleDAO;

        public populateDBAsync(AppDatabase db) {
            peopleDAO = db.peopleDAO();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            peopleDAO.removeAllPerson();
            return null;
        }
    }
}
