package wildcodeschool.fr.starwarsapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_VOLLEY = "VolleyDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://swapi.co/api/planets/";

        final List<PlanetModel> planets = new ArrayList<>();

        RecyclerView planetList = findViewById(R.id.planetList);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        planetList.setLayoutManager(layoutManager);

        final PlanetAdapter adapter = new PlanetAdapter(planets);
        planetList.setAdapter(adapter);

        planetList.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, planetList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // TODO : retrouver la plète sur laquel j'ai cliqué grace à sa position
                String planetName = planets.get(position).getName();
                Toast.makeText(MainActivity.this, planetName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        JsonObjectRequest planetsRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject planet = results.getJSONObject(i);
                                String name = planet.getString("name");
                                String climate = planet.getString("climate");
                                String diameter = planet.getString("diameter");
                                int diameterValue = Integer.parseInt(diameter);
                                PlanetModel planetModel = new PlanetModel(name, climate, diameterValue);
                                planets.add(planetModel);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG_VOLLEY, "onErrorResponse: ", error);
                    }
                });


// Add the request to the RequestQueue.
        queue.add(planetsRequest);
    }
}
