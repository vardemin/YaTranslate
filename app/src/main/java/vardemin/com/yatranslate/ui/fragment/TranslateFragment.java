package vardemin.com.yatranslate.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vardemin.com.yatranslate.App;
import vardemin.com.yatranslate.R;
import vardemin.com.yatranslate.presenter.TranslatePresenter;
import vardemin.com.yatranslate.models.HistoryItem;
import vardemin.com.yatranslate.models.LangPair;
import vardemin.com.yatranslate.models.TranslateItem;
import vardemin.com.yatranslate.view.TranslateView;

public class TranslateFragment extends Fragment implements TranslateView {

    @Inject
    public TranslatePresenter presenter;

    @BindView(R.id.progress_translate)
    ProgressBar progress;

    @BindView(R.id.txt_translate_input)
    EditText inputTxt;

    @BindView(R.id.txt_translate_result)
    TextView resultTxt;

    @BindView(R.id.btn_lang_primary)
    Button btnLangPrimary;

    @BindView(R.id.btn_lang_secondary)
    Button btnLangSecondary;

    private Handler textHandler;
    private Runnable runnable;

    private final TextWatcher translateTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable arg0) {
            runnable = () -> {
              callTranslate(inputTxt.getText().toString());
            };
            textHandler.postDelayed(runnable, 1000);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (runnable!=null){
                textHandler.removeCallbacks(runnable);
                runnable=null;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((App)context.getApplicationContext()).getTranslateComponent().inject(this);
        //setRetainInstance(true);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App)getActivity().getApplicationContext()).getTranslateComponent().inject(this);
        textHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translate, container, false);
        ButterKnife.bind(this, rootView);
        inputTxt.addTextChangedListener(translateTextWatcher);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.attachView(this);
        callInit();
    }

    @Override
    public void onStop() {
        presenter.detachView();
        super.onStop();
    }

    @Override
    public void callTranslate(String text) {
        if(text.length()>0)
            presenter.translate(text);
    }

    @Override
    public void onTranslated(TranslateItem translating) {
        String restulText ="";
        for (String str: translating.getTexts())
            restulText = restulText.concat(str.concat("\n"));
        resultTxt.setText(restulText);
    }

    @Override
    public void callInit() {
        presenter.initView();
    }

    @OnClick(R.id.btn_lang_swap)
    @Override
    public void callSwapLangs() {
        presenter.onSwapLangs();
    }

    @OnClick({R.id.btn_lang_primary, R.id.btn_lang_secondary})
    public void callChooseLang(View v) {
        LangsDialog dialog;
        switch (v.getId()){
            case R.id.btn_lang_primary:
                dialog = LangsDialog.newInstance(true);
                dialog.show(getActivity().getSupportFragmentManager(),LangsDialog.TAG);
                break;
            case R.id.btn_lang_secondary:
                dialog = LangsDialog.newInstance(false);
                dialog.show(getActivity().getSupportFragmentManager(),LangsDialog.TAG);
                break;
        }
    }

    @Override
    public void onLastTranslate(HistoryItem historyItem) {
        btnLangPrimary.setText(historyItem.getPrimary().getName());
        btnLangSecondary.setText(historyItem.getSecondary().getName());
        inputTxt.setText(historyItem.getSourceText());
        resultTxt.setText(historyItem.getTranslated());
    }

    @Override
    public void onLangPair(LangPair langPair) {
        btnLangPrimary.setText(langPair.getPrimary().getName());
        btnLangSecondary.setText(langPair.getSecondary().getName());
        callTranslate(inputTxt.getText().toString());
    }

    @Override
    public void onLoading(boolean state) {
        progress.setVisibility(state?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
}
