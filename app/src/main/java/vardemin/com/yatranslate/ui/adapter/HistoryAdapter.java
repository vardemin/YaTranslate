package vardemin.com.yatranslate.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vardemin.com.yatranslate.R;
import vardemin.com.yatranslate.models.HistoryItem;
import vardemin.com.yatranslate.ui.listener.HistoryListListener;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    /**
     * Data List
     */
    private List<HistoryItem> historyItems;
    /**
     * HistoryItem Actions listener
     */
    private HistoryListListener historyListListener;

    public HistoryAdapter(List<HistoryItem> historyItems, HistoryListListener historyListListener) {
        this.historyItems = historyItems;
        this.historyListListener = historyListListener;
    }

    /**
     * Refer adapter data to new data List
     * @param historyItems new data List
     */
    public void setList(List<HistoryItem> historyItems){
        this.historyItems = historyItems;
        notifyDataSetChanged();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, null);
        HistoryViewHolder result = new HistoryViewHolder(view);

        return result;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.originTxt.setText(historyItems.get(position).getSourceText());
        holder.translatedTxt.setText(historyItems.get(position).getTranslated());
        holder.dateTxt.setText(historyItems.get(position).getDateStr());
        holder.langTxt.setText(historyItems.get(position).getLang());
        if(historyItems.get(position).isFavorite()) {
            holder.historyBtn.setImageResource(R.drawable.ic_action_history_true);
        }
        holder.historyBtn.setOnClickListener(view -> {
            historyListListener.onActionHistory(historyItems.get(position));
        });
        holder.navTranslateBtn.setOnClickListener(view -> {
            historyListListener.onActionTranslate(historyItems.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btn_history) ImageButton historyBtn;
        @BindView(R.id.btn_navigate_translate) ImageButton navTranslateBtn;
        @BindView(R.id.txt_history_origin) TextView originTxt;
        @BindView(R.id.txt_history_translated) TextView translatedTxt;
        @BindView(R.id.txt_history_lang) TextView langTxt;
        @BindView(R.id.txt_history_date) TextView dateTxt;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
