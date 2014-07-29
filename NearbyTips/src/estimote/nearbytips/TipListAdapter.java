package estimote.nearbytips;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用于显示tip条目的ListView适配器
 * 
 * @author ZHANG Fan
 *
 */
public class TipListAdapter extends BaseAdapter{
	
	public List<Tip> tipList;
	private LayoutInflater inflater;
	
	public TipListAdapter(Context context)
	{
		this.inflater = LayoutInflater.from(context);
		this.tipList = new ArrayList<Tip>();
	}
	
	@Override
	public int getCount() {
		return this.tipList.size();
	}

	@Override
	public Object getItem(int position) {
		return tipList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = this.LayoutView(position, view, parent);
		if(view != null)
			this.bind(tipList.get(position), view);
		return view;
	}
	public void refresh(Collection<Tip> newtips)
	{
		
		this.tipList.addAll(newtips);
		notifyDataSetChanged();
	}
	public void readAlready(int position)
	{
		tipList.get(position).isRead = true;
		notifyDataSetChanged();
	}
	
	private View LayoutView(int position, View view, ViewGroup parent)
	{
		if(view == null)
		{
			view = this.inflater.inflate(R.layout.tip_layout, null);
			view.setTag(new ViewHolder(view));
		}
		return view;
	}
	
	private void bind(Tip tip, View view)
	{
		ViewHolder holder= (ViewHolder)view.getTag();
		holder.tip_image.setBackground(tip.tip_image);
		holder.tip_name.setText(tip.tip_name);
		if(tip.isRead == true)
			holder.tip_isRead.setBackgroundResource(R.drawable.tip_read);
		
	}
	static class ViewHolder {
		final ImageView tip_image;
		final ImageView tip_isRead;
		final TextView tip_name;

		ViewHolder(View view) {
			
			this.tip_image = (ImageView)view.findViewWithTag("tip_image");
			this.tip_isRead = (ImageView)view.findViewWithTag("tip_isread");
			this.tip_name = (TextView)view.findViewWithTag("tip_name");
		}
	}

}

 
