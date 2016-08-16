package wuba.zhaobiao.message.model;

import com.huangyezhaobiao.enums.TitleBarType;
import com.huangyezhaobiao.view.TitleMessageBarLayout;

import wuba.zhaobiao.common.model.BaseModel;
import wuba.zhaobiao.message.fragment.MessageFragment;

/**
 * Created by 58 on 2016/8/16.
 */
public class MessageModel extends BaseModel implements TitleMessageBarLayout.OnTitleBarClickListener{

    private MessageFragment context;

    public MessageModel(MessageFragment context){
        this.context =context;
    }


    @Override
    public void onTitleBarClicked(TitleBarType type) {

    }

    @Override
    public void onTitleBarClosedClicked() {

    }
}
