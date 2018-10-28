package com.jarvis.mapsearchview;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends ListFragment implements SearchView.OnQueryTextListener
        //, MenuItem.OnActionExpandListener
{

    List<String> mAllValues;
    private ArrayAdapter<String> mAdapter;
    private Context mContext;
    private ListView mListView;
    private TextView emptyTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
        setRetainInstance(true);
        //populateList();
    }

    /*
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }
            else if (id == R.id.action_search) {
                //displayView(0);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
        */
    private View searchFragmentViewLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View searchFragmentViewLayout = inflater.inflate(R.layout.search_fragment, container, false);
        mListView = (ListView) searchFragmentViewLayout.findViewById(android.R.id.list);
        mListView.setVisibility(View.GONE);
        emptyTextView = (TextView) searchFragmentViewLayout.findViewById(android.R.id.empty);
        emptyTextView.setVisibility(View.GONE);
        mListView.setEmptyView(emptyTextView);
        //emptyTextView.setText("");
        return searchFragmentViewLayout;
    }

    Menu searchMenu;
    SearchView searchView;
    MenuItem searchItem;
    MenuInflater inflater2;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        searchMenu = menu;
        searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();

        //searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Products");

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //emptyTextView.setText("No Data");
                mListView.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                MyMapFragment myMapFrag = (MyMapFragment) getFragmentManager().findFragmentByTag("MyMapFrag");
                SearchFragment searchFrag = (SearchFragment) getFragmentManager().findFragmentByTag("MyListViewFrag");
                if (myMapFrag.isVisible()) {
                    ft.hide(myMapFrag).commit();
                    Toast.makeText(mContext, "map hidden", Toast.LENGTH_SHORT).show();
                }
                populateList();
                mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, mAllValues);
                setListAdapter(mAdapter);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mListView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                //emptyTextView.setText("");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                MyMapFragment myMapFrag = (MyMapFragment) getFragmentManager().findFragmentByTag("MyMapFrag");
                SearchFragment searchFrag = (SearchFragment) getFragmentManager().findFragmentByTag("MyListViewFrag");

                //if(myMapFrag.isAdded()){
                if (myMapFrag != null) {
                    ft.show(myMapFrag).commit();
                    Toast.makeText(mContext, "map show", Toast.LENGTH_SHORT).show();
                } else {
                    MyMapFragment mapFragment = new MyMapFragment();
                    ft.add(mapFragment, "MyMapFrag").commit();
                }
                //}
                mAllValues.clear();
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }
        List<String> filteredValues = new ArrayList<String>(mAllValues);
        for (String value : mAllValues) {
            if (!value.toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }
        }
        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, filteredValues);
        setListAdapter(mAdapter);
        return false;
    }

    public void resetSearch() {
        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, mAllValues);
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {


        String item = (String) listView.getAdapter().getItem(position);
        //item = item.toLowerCase();
        Toast.makeText(mContext, item, Toast.LENGTH_SHORT).show();
        searchItem.collapseActionView();
        //it is CallBack object
        onItemSelectedPasser.callonItemSelectedListener(item);
        /*
        if (getActivity() instanceof OnItem1SelectedListener) {
            Toast.makeText(mContext, "listenert clicked", Toast.LENGTH_SHORT).show();
            ((OnItem1SelectedListener) getActivity()).callonItem1SelectedListener(item);
        }*/
        //getFragmentManager().popBackStack();
    }

    // It is callback object of interface
    OnItemSelectedListener onItemSelectedPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onItemSelectedPasser = (OnItemSelectedListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnItemSelectedListener {
        void callonItemSelectedListener(String item);
    }


    private void populateList() {
        mAllValues = new ArrayList<>();

        mAllValues.add("Mobile");
        mAllValues.add("Aquarium & Fish");
        mAllValues.add("Smart Phones");
        mAllValues.add("Fashion And Clothes");
        mAllValues.add("Cinemas");
        mAllValues.add("Petrol Pumps");
        mAllValues.add("ATM & Banks");
        mAllValues.add("Restaurant");
        mAllValues.add("Coffee Cafe");
        mAllValues.add("Hospital");
        mAllValues.add("Medical");
        mAllValues.add("Government Office");
    }
}
