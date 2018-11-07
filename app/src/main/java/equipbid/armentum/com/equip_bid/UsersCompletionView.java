package equipbid.armentum.com.equip_bid;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Sample token completion view for basic contact info
 *
 * Created on 9/12/13.
 * @author mgod
 */
public class UsersCompletionView extends TokenCompleteTextView<User> {

    public UsersCompletionView(Context context) {
        super(context);
    }

    public UsersCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UsersCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(User user) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TokenTextView token = (TokenTextView) l.inflate(R.layout.user_token, (ViewGroup) getParent(), false);
        token.setText(user.getName());
        return token;
    }

    @Override
    protected User defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        int index = completionText.indexOf('@');
        if (index == -1) {
            return new User(completionText, completionText);
        } else {
            return new User(completionText.substring(0, index), completionText);
        }
    }
}
