package com.example.stud.musicapp.Favorites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.stud.musicapp.R;
import com.example.stud.musicapp.database.Favorite;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Realm realm = Realm.getDefaultInstance();

        RealmResults<Favorite> favorites = realm
                .where(Favorite.class)
                .sort("date", Sort.DESCENDING)
                .findAll();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();

        return true;
    }
}
