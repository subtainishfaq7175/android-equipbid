package equipbid.armentum.com.equip_bid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Darshan on 11/6/2017.
 */

public class TokenTextView extends TextView {

    public TokenTextView(Context context) {
        super(context);
    }

    public TokenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setCompoundDrawablesWithIntrinsicBounds(0, 0, selected ? R.mipmap.cancel : 0, 0);
        setCompoundDrawablePadding(7);
    }
}

