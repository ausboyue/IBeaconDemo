package cn.icheny.ibeacon.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * adapter 没什么好解释的，基础的玩意，一看就懂
 *
 * @author cheny
 */
public class CommodityAdapter extends BaseAdapter {

    private List<Commodity> commodities;
    private LayoutInflater mInflator;
    private Activity mContext;

    public CommodityAdapter(Activity c) {
        super();
        mContext = c;
        commodities = new ArrayList<Commodity>();
        mInflator = mContext.getLayoutInflater();
    }

    public void addCommodity(Commodity commodity) {
        if (commodity == null)
            return;

        for (int i = 0; i < commodities.size(); i++) {
            String commodityId = commodities.get(i).getCommodityId();
            if (commodityId.equals(commodity.getCommodityId())) {
                commodities.add(i + 1, commodity);
                commodities.remove(i);
                return;
            }
        }
        commodities.add(commodity);

    }

    public Commodity getDevice(int position) {
        return commodities.get(position);
    }

    public void clear() {
        commodities.clear();
    }

    @Override
    public int getCount() {
        return commodities.size();
    }

    @Override
    public Object getItem(int i) {
        return commodities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = mInflator.inflate(R.layout.item_commodity_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Commodity commodity = commodities.get(i);

        viewHolder.commodity_id.setText(commodity.getCommodityId());

        viewHolder.commodity_icon.setImageResource(commodity.getIconId());
        viewHolder.commodity_desc.setText(commodity.getDesc());

        viewHolder.commodity_distance.setText("距您约:" + commodity.getDistance() + "米");
        return view;
    }

    class ViewHolder {
        TextView commodity_id;
        ImageView commodity_icon;
        TextView commodity_desc;
        TextView commodity_distance;

        public ViewHolder(View view) {
            commodity_id = view.findViewById(R.id.commodity_id);
            commodity_icon = view.findViewById(R.id.commodity_icon);
            commodity_desc = view.findViewById(R.id.commodity_desc);
            commodity_distance = view.findViewById(R.id.commodity_distance);
        }
    }
}
