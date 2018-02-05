package ly.school.adapter;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class LeaderAdapter extends PagerAdapter{
List<View> mlv;

	public LeaderAdapter(List<View> lv) {
	super();
	this.mlv = lv;
}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlv.size();
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==(View)arg1;
	}


	@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		  container.removeView(mlv.get(position));
		}

	@Override
		public Object instantiateItem(ViewGroup container, int position) {
		View v=mlv.get(position);
		container.addView(v);
		return v;
		
		}
	

}
