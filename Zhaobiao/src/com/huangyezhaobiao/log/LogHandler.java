package com.huangyezhaobiao.log;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.huangyezhaobiao.utils.FileUtils;

public class LogHandler extends Handler{
	private Context context;
	public LogHandler(Context context) {
		this.context = context;
	}
	@Override
    public void handleMessage(Message msg) {
		
		Bundle data = msg.getData();
        String json = (String) data.get("json");
      //  Log.e("ashenqqq", "json:"+json);
        FileUtils.write(context,json);
    }

}
