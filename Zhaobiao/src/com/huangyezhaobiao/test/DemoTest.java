package com.huangyezhaobiao.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.activity.LoginActivity;

/**
 * Created by shenzhixin on 2015/12/28.
 */
public class DemoTest extends ActivityUnitTestCase<LoginActivity>{
    Intent mIntent;

    public DemoTest() {
        super(LoginActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mIntent = new Intent(getInstrumentation().getTargetContext(),LoginActivity.class);

    }

    @MediumTest
    public void testLoginText(){
        startActivity(mIntent,null,null);
        Button button = (Button) getActivity().findViewById(R.id.loginbutton);
        assertEquals("the text equals","帮助",button.getText());
    }
}
