package estimote.seekshop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 用于显示地图，其中包括长宽缩放比
 * @author ZHANG Fan
 *
 */
public class MapView extends ImageView {

	private Drawable test;
	public float widthscale;
	public float heightscale;

	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		test = context.getResources().getDrawable(R.drawable.test);
		this.setImageDrawable(test);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		widthscale = (this.getRight()-this.getLeft()+0.0f)/test.getIntrinsicWidth();
		heightscale = (this.getBottom()-this.getTop()+0.0f)/test.getIntrinsicHeight();
		super.onDraw(canvas);
	}

	
	
	

}
