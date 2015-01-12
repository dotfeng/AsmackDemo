package net.fengg.asmack.demo.adapter;

import java.util.List;

import net.fengg.asmack.demo.R;

import org.jivesoftware.smack.RosterEntry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FriendsAdap extends BaseAdapter {
	private List<RosterEntry> list;
	private Context context;

	public FriendsAdap(Context context, List<RosterEntry> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View conView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holdr;
		if (conView == null) {
			conView = LayoutInflater.from(context).inflate(
					R.layout.item_friend, parent, false);
			holdr = new ViewHolder();
			holdr.fName = (TextView) conView.findViewById(R.id.tv_fname);
			conView.setTag(holdr);
		} else {
			holdr = (ViewHolder) conView.getTag();
		}

		holdr.fName.setText(list.get(arg0).getUser());
		return conView;
	}

	public static class ViewHolder {
		public TextView fName;
	}
}
