package vardemin.com.yatranslate.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import vardemin.com.yatranslate.App;
import vardemin.com.yatranslate.R;
import vardemin.com.yatranslate.ui.adapter.HistoryAdapter;
import vardemin.com.yatranslate.di.history.HistoryQualifier;
import vardemin.com.yatranslate.models.HistoryItem;
import vardemin.com.yatranslate.presenter.HistoryPresenter;
import vardemin.com.yatranslate.ui.listener.HistoryListListener;
import vardemin.com.yatranslate.view.HistoryView;


public class HistoryFragment extends Fragment implements SearchView.OnQueryTextListener, HistoryView, HistoryListListener{
    @Inject
    @HistoryQualifier
    public HistoryPresenter presenter;

    @BindView(R.id.recycler_history)
    RecyclerView recycler;
    private HistoryAdapter adapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((App)context.getApplicationContext()).getHistoryComponent().inject(this);
        //setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_search, menu);
        //SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete: clear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    public  void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void onLoading(boolean state) {

    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void init(List<HistoryItem> historyItems) {
        adapter = new HistoryAdapter(historyItems, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    @Override
    public void loadList(List<HistoryItem> historyItems) {
        adapter.setList(historyItems);
    }

    @Override
    public void clear() {
        presenter.removeAll();
    }

    @Override
    public void search(String str) {
        presenter.searchHistory(str);
    }

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActionTranslate(HistoryItem historyItem) {
        presenter.onNavigateTranslate(historyItem);
    }

    @Override
    public void onActionHistory(HistoryItem historyItem) {
        presenter.onChangeHistoryItem(historyItem);
    }

}
