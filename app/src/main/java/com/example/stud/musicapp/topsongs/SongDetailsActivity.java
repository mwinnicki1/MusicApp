package com.example.stud.musicapp.topsongs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;
import com.example.stud.musicapp.R;
import com.example.stud.musicapp.api.ApiService;
import com.example.stud.musicapp.api.Track;
import com.example.stud.musicapp.api.Tracks;

import com.bumptech.glide.Glide;
import com.example.stud.musicapp.database.Favorite;

import java.util.Date;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongDetailsActivity extends AppCompatActivity {

    public static final String TRACK = "track";
    public static final String ARTIST = "artist";
    public static final String TRACK_ID = "track_id";

    String track;
    String artist;
    int trackId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);

        Intent intent = getIntent();
        track = intent.getStringExtra(TRACK);
        artist = intent.getStringExtra(ARTIST);
        trackId = intent.getIntExtra(TRACK_ID, 0);

        getSupportActionBar().setTitle(track);
        getSupportActionBar().setSubtitle(artist);

        ApiService.getService().getTrack(trackId).enqueue(new Callback<Tracks>() {
            @Override
            public void onResponse(Call<Tracks> call, Response<Tracks> response) {
                Tracks tracks = response.body();
                if (tracks != null && tracks.track.size() > 0) {
                    showData(tracks.track.get(0));
                }
            }
            @Override
            public void onFailure(Call<Tracks> call, Throwable t) {
                Toast.makeText(SongDetailsActivity.this, "Bład pobierania danych" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite_menu, menu);
        return true;
    }

    private void addRemoveFavourite() {
        Realm realm = Realm.getDefaultInstance();
        Favorite favorite = realm
                .where(Favorite.class)
                .equalTo("trackId", trackId)
                .findFirst();

        if(favorite == null){
            addToFavorites(realm);
        } else {
            removeFromFavorites(realm);
        }
    }

    private void removeFromFavorites(Realm realm, Final Favorite favorite) {
        realm.executeTransaction( new Realm.Transaction() {
            @Override
            public void execute( @NonNull Realm realm) {
                favorite.deleteFromRealm();
                Toast. makeText (SongDetailsActivity. this , "Usunięto z ulubionych" ,
                        Toast. LENGTH_SHORT ).show();
            }
        });
    }

    private void addToFavorites(Realm realm) {
        realm.executeTransaction( new Realm.Transaction() {
            @Override
            public void execute( @NonNull Realm realm) {
                Favorite favorite = realm.createObject(Favorite. class );
                favorite.setArtist( artist );
                favorite.setTrack( track );
                favorite.setTrackId( trackId );
                favorite.setDate( new Date());

                Toast. makeText (SongDetailsActivity. this , "Dodano do ulubionych" ,
                        Toast. LENGTH_SHORT ).show();
            }
        });

    }


    private void showData(Track track) {
        TextView tvAlbum = findViewById(R.id.tvAlbum);
        TextView tvGenre = findViewById(R.id.tvGenre);
        TextView tvStyle = findViewById(R.id.tvStyle);
        TextView tvDescription = findViewById(R.id.tvDescription);

        tvAlbum.setText(track.strAlbum);
        tvGenre.setText(track.strGenre);
        tvStyle.setText(track.strStyle);
        tvDescription.setText(track.strDescriptionEN);

        if (track.strTrackThumb != null && !track.strTrackThumb.isEmpty()) {
            ImageView ivThumb = findViewById(R.id.ivThumb);
            Glide.with(this).load(track.strTrackThumb).into(ivThumb);
        }

    }
}