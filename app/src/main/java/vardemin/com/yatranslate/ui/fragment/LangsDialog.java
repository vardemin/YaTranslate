package vardemin.com.yatranslate.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import vardemin.com.yatranslate.App;
import vardemin.com.yatranslate.R;
import vardemin.com.yatranslate.models.Lang;
import vardemin.com.yatranslate.presenter.LangsDialogPresenter;
import vardemin.com.yatranslate.ui.adapter.LangsDialogAdapter;
import vardemin.com.yatranslate.ui.listener.LangsListListener;
import vardemin.com.yatranslate.view.LangsDialogView;


public class LangsDialog extends DialogFragment implements LangsDialogView, LangsListListener {
    public static final String TAG = "LangsDialog";

    private boolean isPrimary;
    private LangsDialogPresenter presenter;
    private static final String PRIMARY_KEY = "isPirmary";

    @BindView(R.id.recycler_langs_dialog)
    RecyclerView recycler;

    private LangsDialogAdapter adapter;

    /**
     * Get inited Langs Dialog with args
     * @param isPrimary flag is primary
     * @return Langs Dialog
     */
    public static LangsDialog newInstance(boolean isPrimary) {
        LangsDialog dialog = new LangsDialog();
        Bundle args = new Bundle();
        args.putBoolean(PRIMARY_KEY, isPrimary);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrimary = getArguments().getBoolean(PRIMARY_KEY);
        presenter = new LangsDialogPresenter(isPrimary, ((App)getActivity().getApplicationContext()).getYaTranslateComponent().localDataRepository());
        adapter = new LangsDialogAdapter(presenter.getLangsExcept(),this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_langs, container, false);
        ButterKnife.bind(this,view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        presenter.initView();
    }

    @Override
    public void onPause(){
        super.onPause();
        presenter.detachView();
    }



    @Override
    public void onLoading(boolean state) {

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void close() {
        dismiss();
    }

    @Override
    public void onLangChosen(Lang lang) {
        presenter.onLangChosen(lang);
    }
}
