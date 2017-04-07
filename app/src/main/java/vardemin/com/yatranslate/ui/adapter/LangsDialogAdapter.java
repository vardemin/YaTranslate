package vardemin.com.yatranslate.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vardemin.com.yatranslate.R;
import vardemin.com.yatranslate.models.Lang;
import vardemin.com.yatranslate.ui.listener.LangsListListener;

public class LangsDialogAdapter extends RecyclerView.Adapter<LangsDialogAdapter.LangsDialogViewHolder> {

    private List<Lang> langList;
    private LangsListListener listListener;

    public LangsDialogAdapter(List<Lang> langList, LangsListListener listListener) {
        this.langList = langList;
        this.listListener = listListener;
    }


    @Override
    public LangsDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lang, null);
        LangsDialogViewHolder result = new LangsDialogViewHolder(view);
        view.setOnClickListener(view1 -> {
            int adapterPos = result.getAdapterPosition();
            if(adapterPos != RecyclerView.NO_POSITION) {
                listListener.onLangChosen(langList.get(adapterPos));
            }
        });
        return result;
    }

    @Override
    public void onBindViewHolder(LangsDialogViewHolder holder, int position) {
        int truePos = holder.getAdapterPosition();
        holder.code.setText(langList.get(truePos).getCode());
        holder.name.setText(langList.get(truePos).getName());
    }

    @Override
    public int getItemCount() {
        return langList.size();
    }


    public static class LangsDialogViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_lang_code) TextView code;
        @BindView(R.id.item_lang_name) TextView name;

        public LangsDialogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
