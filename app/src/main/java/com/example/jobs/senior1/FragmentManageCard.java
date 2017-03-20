package com.example.jobs.senior1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class FragmentManageCard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "FragmentManageCard";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FragmentManageCard() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentManageCard.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentManageCard newInstance(String param1, String param2) {
        FragmentManageCard fragment = new FragmentManageCard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Add your menu entries here
//        inflater.inflate(R.menu.add_card
//                , menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.creaCard) {
//
//            Intent intent = new Intent(getActivity(), CreateCardActivity.class);
//            startActivity(intent);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_manage_card, container, false);
        loadCardList(view);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateCardActivity.class);
                startActivity(intent);
//                Snackbar.make(view, "Exchange Feature", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        return view;
    }


    private void loadCardList(final View view){
        SharedPreferences sp = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<CardListResponse>> call = apiService.getCardList(sp.getString("accId","0"));
        call.enqueue(new Callback<List<CardListResponse>>() {
            @Override
            public void onResponse(Call<List<CardListResponse>>call, Response<List<CardListResponse>> response) {
                final List<CardListResponse> list = response.body();

                Log.d(TAG, "cardList: " +list.size());

                final CardListAdapter adapter = new CardListAdapter(getActivity().getApplicationContext(), list);
                ListView listView = (ListView)view.findViewById(R.id.listView1);
                listView.setAdapter(adapter);

                view.findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
                if(list.size()==0) {
                    view.findViewById(R.id.noCard).setVisibility(View.VISIBLE);
//                    view.findViewById(R.id.arrow).setVisibility(View.VISIBLE);
                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                        Intent intent = new Intent(getActivity(), CardDetailActivity.class);
                        Log.d(TAG,adapter.getId(arg2));
                        intent.putExtra("cardId", adapter.getId(arg2));
                        intent.putExtra("isOwner", "yes");

                        intent.putExtra("view", "false");
                      //  Log.d("cardId",arg2+""+adapter.getId(arg2));
                        startActivity(intent);
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                        final int pos, long id) {
                        CharSequence menu[] = new CharSequence[] {"Edit Card","Delete Card"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Choose action");
                        builder.setItems(menu, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on colors[which]
                                if(which==0) {
                                    Intent intent = new Intent(getActivity(), EditDetailActivity.class);
                                    intent.putExtra("cardId", adapter.getId(pos));
                                    //  intent.putExtra("from", "manage");
                                    //  Log.d("cardId",arg2+""+adapter.getId(arg2));
                                    startActivity(intent);
                                }
                                if(which==1){

                                    new AlertDialog.Builder(getActivity())
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle("Confirmation")
                                            .setMessage("Delete this card?")
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    deleteCard(adapter,pos);
                                                }

                                            })
                                            .setNegativeButton("Cancel", null)
                                            .show();

                                }
                            }
                        });
                        builder.show();
                        return true;
                    }
                });
                Log.d(TAG, "done");
            }

            @Override
            public void onFailure(Call<List<CardListResponse>>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                Log.e(TAG, "Reload");
                loadCardList(view);

                Toast toast = Toast.makeText( getContext(), "Sever don't response ... Please wait", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void deleteCard(CardListAdapter adapter, int pos){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<ResponseBody> call = apiService.deleteCard(adapter.getId(pos));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {

                Toast toast = Toast.makeText( getActivity().getApplicationContext(), "Delete Complete", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<ResponseBody>call, Throwable t) {
                // Log error here since request failed
                Toast toast = Toast.makeText( getActivity().getApplicationContext(), "Delete Fail", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


}
