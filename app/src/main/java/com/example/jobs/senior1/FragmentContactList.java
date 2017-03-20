package com.example.jobs.senior1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentContactList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "FragmentContactList";
    ArrayList<String> list2;
    ArrayList<CardListResponse> cardList;
    SharedPreferences sp;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int c;

    public FragmentContactList() {
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
        c = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        loadCardList(view);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExchangeActivity.class);
                startActivity(intent);
//                Snackbar.make(view, "Exchange Feature", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        return view;
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        super.onCreateOptionsMenu(menu, R.menu.main2);
//        getMenuInflater().inflate(R.menu.main2, menu);
//        return true;
//    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.main2, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_action) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("search", list2);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadCardList(final View view) {
        sp = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
//        boolean isLoad = false;
//        ApiInterface apiService =
//                ApiClient.getClient().create(ApiInterface.class);
//        cardList = new ArrayList<>();
//        Call<List<ContactListResponse>> call = apiService.getContactList(sp.getString("accId", "0"));
        if (sp.getString("accId", "0").compareTo("0") == 0) {

            Intent intent2 = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent2);
        }
        if (ContactList.cardList == null) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
//            ContactList.cardList = new ArrayList<>();
            Call<ArrayList<CardListResponse>> call = apiService.getContactList(sp.getString("accId", "0"));
            call.enqueue(new Callback<ArrayList<CardListResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<CardListResponse>> call, Response<ArrayList<CardListResponse>> response) {
                    try {
                        Log.d(TAG,"New CL Size: "+response.body().size());
                        ContactList.getInstance(response.body());
                        loadView(view);
                    } catch (Exception e) {

                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.server_fail, Toast.LENGTH_SHORT);
                        toast.show();
//                        Log.e(TAG, "Reload: " + TAG);
                        Log.e(TAG, e.getMessage());
//                        loadCardList(getView());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<CardListResponse>> call, Throwable t) {

                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.server_fail, Toast.LENGTH_SHORT);
                    toast.show();
                    Log.e(TAG, "Reload: " + TAG);
                    loadCardList(getView());
                }
            });
        } else {

            Log.d(TAG,"old cl");
            loadView(view);
        }

    }
//
//    @Override
//    public void onResume() {
//        ContactList.destroy();
//        Log.d(TAG,"onResume");
//        super.onResume();
//        // put your code here...
//    }

    private void loadView(View view) {
        Log.d(TAG,"Size: "+ContactList.cardList.size() );
        final ContactListAdapter adapter = new ContactListAdapter(getActivity().getApplicationContext(), ContactList.cardList);

        final ListView listView = (ListView) view.findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        if (ContactList.cardList.size() == 0) {

            view.findViewById(R.id.noContact).setVisibility(View.VISIBLE);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(getActivity(), CardDetailActivity.class);
                intent.putExtra("cardId", ((ContactListAdapter) listView.getAdapter()).getId(arg2));
                intent.putExtra("from", "contact");
                Log.d(TAG, arg2 + "" + adapter.getId(arg2));
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                CharSequence menu[] = new CharSequence[]{"Delete Card"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose action");
                builder.setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if (which == 0) {

                            new AlertDialog.Builder(getActivity())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Confirmation")
                                    .setMessage("Delete this card?")
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            deleteCard(adapter, pos);
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
//        Log.d(TAG, "done");

        view.findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void deleteCard(ContactListAdapter adapter, int pos) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Map<String, String> data = new HashMap<>();
        data.put("bizCardId", adapter.getId(pos));
        data.put("accId", sp.getString("accId", "0"));
        Log.d(TAG, "Del:" + adapter.getId(pos) + " " + sp.getString("accId", "0"));
        Call<ResponseBody> call = apiService.deleteContact(data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Intent intent = new Intent(getActivity(), MainActivity.class);
                onDestroyView();
                ContactList.destroy();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                onDestroy();
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Delete Complete", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Delete Fail", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
