package aksiom.currencycalc.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import aksiom.currencycalc.R;
import aksiom.currencycalc.realm.Currency;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aksiom on 6/30/2016.
 */
public class CurrencyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SIMPLE = 1;
    public static final int DETAIL = 2;
    public static final int SIMPLE_WRAP = 3;

    private boolean isSingleMode;
    private OnItemClickListener onItemSelected;
    private int selectedItem = 0;
    private List<Currency> currencies = new ArrayList<>();
    private int viewHolderType;

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public class CurrencyComplexViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        @BindView(R.id.tv_currency_code)
        public TextView tvCurrencyCode;
        @BindView(R.id.tv_currency_code_label)
        public TextView tvCurrencyCodeLabel;
        @BindView(R.id.tv_buying_rate)
        public TextView tvBuyingRate;
        @BindView(R.id.tv_median_rate)
        public TextView tvMedianRate;
        @BindView(R.id.tv_selling_rate)
        public TextView tvSellingRate;

        public CurrencyComplexViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            handleClick(v, getLayoutPosition());
        }
    }

    public class CurrencySimpleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        @BindView(R.id.tv_currency_code)
        public TextView tvCurrencyCode;
        @BindView(R.id.tv_currency_info)
        public TextView tvCurrencyInfo;

        public CurrencySimpleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            handleClick(v, getLayoutPosition());

        }
    }

    private void handleClick(View v, int position) {
        if(isSingleMode){
            notifyItemChanged(selectedItem);
            ((CardView)v).setCardBackgroundColor(v.getContext().getResources().getColor(R.color.itemSelected));
            selectedItem = position;
            notifyItemChanged(selectedItem);
        }
        if(onItemSelected != null) onItemSelected.onClick(position);
    }

    public void setOnClickListener(OnItemClickListener onItemSelected){
        this.onItemSelected = onItemSelected;
    }

    public CurrencyRecyclerAdapter(List<Currency> currencies, int viewHolderType) {
        this(currencies,viewHolderType,false);
    }

    public CurrencyRecyclerAdapter(List<Currency> currencies, int viewHolderType,
                                   boolean singleModeSelection) {
        this.isSingleMode = singleModeSelection;
        this.currencies = currencies;
        this.viewHolderType = viewHolderType;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewHolderType) {
            default:
            case SIMPLE:
                View vSimple = inflater.inflate(R.layout.currency_simple_item, parent, false);
                viewHolder = new CurrencySimpleViewHolder(vSimple);
                break;
            case SIMPLE_WRAP:
                View vSimpleWrap = inflater.inflate(R.layout.currency_simple_wrap_item, parent, false);
                viewHolder = new CurrencySimpleViewHolder(vSimpleWrap);
                break;
            case DETAIL:
                View vComplex = inflater.inflate(R.layout.currency_detail_item, parent, false);
                viewHolder = new CurrencyComplexViewHolder(vComplex);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (viewHolderType) {
            default:
            case SIMPLE_WRAP:
            case SIMPLE:
                CurrencySimpleViewHolder vhs = (CurrencySimpleViewHolder)holder;
                vhs.tvCurrencyCode.setText(currencies.get(position).getCurrencyCode());
                vhs.tvCurrencyInfo.setText(createCurrencyInfo(position));
                break;
            case DETAIL:
                CurrencyComplexViewHolder vhc = (CurrencyComplexViewHolder)holder;
                vhc.tvCurrencyCode.setText(currencies.get(position).getCurrencyCode());
                vhc.tvCurrencyCodeLabel.setText(createCurrencyInfo(position));
                vhc.tvBuyingRate.setText(currencies.get(position).getBuyingRate());
                vhc.tvMedianRate.setText(currencies.get(position).getMedianRate());
                vhc.tvSellingRate.setText(currencies.get(position).getSellingRate());
                break;
        }

        if(isSingleMode){
            if(selectedItem == position){
                ((CardView)holder.itemView).setCardBackgroundColor(holder.itemView.getContext().getResources()
                        .getColor(R.color.itemSelected));
            }else{
                ((CardView)holder.itemView).setCardBackgroundColor(Color.WHITE);
            }
        }
    }

    private String createCurrencyInfo(int position) {
        String currencyInfo2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            currencyInfo2 = java.util.Currency.getInstance(currencies.get(position)
                    .getCurrencyCode()).getSymbol() + " " + java.util.Currency
                    .getInstance(currencies.get(position).getCurrencyCode()).getDisplayName();
        }else{
            currencyInfo2 = java.util.Currency.getInstance(currencies.get(position)
                    .getCurrencyCode()).getSymbol();
        }
        return currencyInfo2;
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }
}
