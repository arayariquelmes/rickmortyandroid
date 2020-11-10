package cl.inacap.rickmorty.fragments;

import android.app.Person;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cl.inacap.rickmorty.R;
import cl.inacap.rickmorty.adapters.PersonajesListAdapter;
import cl.inacap.rickmorty.dto.Personaje;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class PersonajesFragment extends Fragment {

    private RequestQueue queue;
    private List<Personaje> personajes = new ArrayList<>();
    private PersonajesListAdapter adaptador;
    private ListView listView;
    public PersonajesFragment() {
        // Required empty public constructor
    }


    public void onResume(){
        super.onResume();
        queue= Volley.newRequestQueue(this.getActivity());
        this.listView = getView().findViewById(R.id.personajes_list);
        this.adaptador = new PersonajesListAdapter(this.getActivity(), R.layout.list_personajes
                , this.personajes);
        this.listView.setAdapter(this.adaptador);
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET
                , "https://rickandmortyapi.com/api/character"
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    try {
                        personajes.clear();
                        personajes.addAll(Arrays
                                .asList(new Gson().fromJson(response.getString("results")
                                        , Personaje[].class)));

                    }catch(Exception ex){
                        personajes = null;
                        Log.e("ERROR_PAPU",ex.toString());
                    }finally{
                        adaptador.notifyDataSetChanged();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                personajes = null;
                adaptador.notifyDataSetChanged();
            }
        });
        queue.add(jsonReq);
        Log.i("ON_RESUME","On resume papu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personajes, container, false);
    }
}