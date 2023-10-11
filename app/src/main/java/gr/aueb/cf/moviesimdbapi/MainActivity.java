package gr.aueb.cf.moviesimdbapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gr.aueb.cf.moviesimdbapi.adapters.MovieAdapter;
import gr.aueb.cf.moviesimdbapi.models.Movie;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private RecyclerView recyclerView;
    //private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        recyclerView = findViewById(R.id.recyclerView);
        movieArrayList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().isEmpty()) {
                    editText.setError("Please insert a movie name");
                } else {
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    String url = "https://imdb-api.com/en/API/SearchMovie/k_12345678/" + editText.getText().toString().trim();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                movieArrayList.clear();
                                JSONArray jsonArray = response.getJSONArray("results");

                                for (int i = 0; i < jsonArray.length(); i++ ) {
                                    JSONObject movieJSONObject = jsonArray.getJSONObject(i);
                                    Movie movie = new Movie();
                                    movie.setId(movieJSONObject.getString("id"));
                                    movie.setTitle(movieJSONObject.getString("title"));
                                    movie.setDescription(movieJSONObject.getString("description"));
                                    movie.setImage(movieJSONObject.getString("image"));
                                    movie.setResultType(movieJSONObject.getString("resultType"));
                                    movieArrayList.add(movie);
                                }
                           //     System.out.println("sizeof" + movieArrayList.size());
                                recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movieArrayList));

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }

                    );

                    requestQueue.add(jsonObjectRequest);
                }
            }
        });
    }
}