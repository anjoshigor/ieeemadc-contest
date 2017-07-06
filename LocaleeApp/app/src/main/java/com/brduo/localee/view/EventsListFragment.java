package com.brduo.localee.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brduo.localee.controller.EventAdapter;
import com.brduo.localee.R;
import com.brduo.localee.controller.EventsController;
import com.brduo.localee.controller.LocaleeAPI;
import com.brduo.localee.model.Event;
import com.brduo.localee.model.EventResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsListFragment extends Fragment {

    private List<Event> events;
    private RecyclerView eventListRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.fragment_events_list);
        events = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_events_list, viewGroup, false);

        eventListRecycler = (RecyclerView) rootView.findViewById(R.id.event_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());

        eventListRecycler.setHasFixedSize(true);
        eventListRecycler.setLayoutManager(layoutManager);

        getAllEvents();

        EventAdapter adapter = new EventAdapter(events);
        eventListRecycler.setAdapter(adapter);


        return rootView;
    }

    void getAllEvents() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(LocaleeAPI.LOCALEE_BASE_URL)
                .build();

        LocaleeAPI api = retrofit.create(LocaleeAPI.class);

        Call<EventResponse> call = api.getEvents();
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful()) {
                    for (Event event : response.body().data) {
                        events.add(event);
                    }
                } else {
                    Log.e("RETROFIT", "Erro na listagem de eventos");
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e("RETROFIT", t.getMessage());
            }
        });
    }
/*
    void fakeData() {
        Calendar cal = Calendar.getInstance();
        this.events = new ArrayList<>();
        events.add(new Event(
                4,
                "Concerto-Tributo a Belchior no Espaço Cultural da Paraíba",
                "Espaço Cultural De João Pessoa - Funesc Pb",
                "http://funesc.pb.gov.br/wp-content/uploads/2017/06/belchioralucinacao1-800x445.jpg",
                Calendar.getInstance(),
                "https://www.facebook.com/events/1173293659465739/"
        ));
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        events.add(new Event(
                5,
                "2º PARAÍBA EXPO TATTOO",
                "Espaço Cultural De João Pessoa - Funesc Pb",
                "https://www.wscom.com.br/arqs/noticias/480_350/201606280223480000003593.jpg",
                cal,
                "https://www.facebook.com/events/346149145800088/"
        ));events.add(new Event(
                6,
                "Concerto-Tributo a Belchior no Espaço Cultural da Paraíba",
                "Espaço Cultural De João Pessoa - Funesc Pb",
                "http://funesc.pb.gov.br/wp-content/uploads/2017/06/belchioralucinacao1-800x445.jpg",
                Calendar.getInstance(),
                "https://www.facebook.com/events/1173293659465739/"
        ));
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        events.add(new Event(
                7,
                "2º PARAÍBA EXPO TATTOO",
                "Espaço Cultural De João Pessoa - Funesc Pb",
                "https://www.wscom.com.br/arqs/noticias/480_350/201606280223480000003593.jpg",
                cal,
                "https://www.facebook.com/events/346149145800088/"
        ));events.add(new Event(
                8,
                "Concerto-Tributo a Belchior no Espaço Cultural da Paraíba",
                "Espaço Cultural De João Pessoa - Funesc Pb",
                "http://funesc.pb.gov.br/wp-content/uploads/2017/06/belchioralucinacao1-800x445.jpg",
                Calendar.getInstance(),
                "https://www.facebook.com/events/1173293659465739/"
        ));
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        events.add(new Event(
                9,
                "2º PARAÍBA EXPO TATTOO",
                "Espaço Cultural De João Pessoa - Funesc Pb",
                "https://www.wscom.com.br/arqs/noticias/480_350/201606280223480000003593.jpg",
                cal,
                "https://www.facebook.com/events/346149145800088/"
        ));
    }*/
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_events_list, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
