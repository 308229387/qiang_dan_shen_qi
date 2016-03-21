package com.huangyezhaobiao;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by 58 on 2016/3/21.
 */
public class LoginEditFilter implements InputFilter {
    private Context context;
    public LoginEditFilter(Context context,String str){
        this.context = context;
    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Log.e("shenzhixinEdittext", "source:" + source + ",start:" + start + ",end:" + end + ",dest:" + dest.toString() + ",dstart:" + dstart + ",dend:" + dend);
        if(!isInvalid(source)){
            return dest.subSequence(dstart, dstart)+source.toString();
        }
        Toast.makeText(context,"不能输入空格或者回车",Toast.LENGTH_SHORT).show();
        return dest.subSequence(dstart, dend);
    }

    /**
     *字符串是不是非法的
     * @param source
     * @return
     */
    private boolean isInvalid(CharSequence source){
        if(" ".equals(source) || "\\n".equals(source)){
            return true;
        }
        return false;
    }
}
