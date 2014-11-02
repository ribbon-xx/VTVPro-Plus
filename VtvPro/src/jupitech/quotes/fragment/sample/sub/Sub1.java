package jupitech.quotes.fragment.sample.sub;

import mdn.vtvplus.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Sub1 extends Fragment {
	TextView tv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_sub, container, false);
		tv = (TextView) view.findViewById(R.id.tvText);
		update();
		return view;
	}
	
	public void update() {
		tv.setText("SUB 1");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
