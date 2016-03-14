package com.huangyezhaobiao.vm;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.huangye.commonlib.model.NetWorkModel;
import com.huangye.commonlib.utils.JsonUtils;
import com.huangye.commonlib.utils.NetBean;
import com.huangye.commonlib.vm.SourceViewModel;
import com.huangye.commonlib.vm.callback.NetWorkVMCallBack;
import com.huangyezhaobiao.bean.tt.RefundBaseBean;
import com.huangyezhaobiao.factory.RefundVMFactory;
import com.huangyezhaobiao.inter.ETInterface;
import com.huangyezhaobiao.model.RefundCloseModel;
import com.huangyezhaobiao.url.URLConstans;
import com.huangyezhaobiao.url.UrlSuffix;


/**
 * Created by shenzhixin on 2015/12/9.
 */
public class RefundCloseVM extends SourceViewModel{
    private ETInterface etInterface;
    private int type = -1;//根据这个type可以判断到底是退单的哪个页面
    private TextWatcher et_textWatcher;
    public RefundCloseVM(NetWorkVMCallBack callBack, Context context) {
        super(callBack, context);
    }

    public void setEtInterface(ETInterface etInterface){
        this.etInterface = etInterface;
    }
    @Override
    protected NetWorkModel initListNetworkModel(Context context) {
        return new RefundCloseModel(this,context);
    }

    public void setRefundType(int type){
        this.type = type;
    }

    public void setOrderId(String orderId){
        t.setRequestURL(URLConstans.URL_REFUND_NOT_OPEN+ UrlSuffix.getRefundCloseTimeSuffix(context,orderId));//设置
    }

    public void getDatas(){
        t.getDatas();
    }


    @Override
    public void onLoadingSuccess(NetBean bean, NetWorkModel model) {
        int status = bean.getStatus();
        if(status==0){
            try {
                //根据传入的type值来确定不同的实体bean
                Class<? extends RefundBaseBean> clazz = RefundVMFactory.getBeanClass(type);
                callBack.onLoadingSuccess(JsonUtils.jsonToObject(bean.getData(), clazz));
            }catch (Exception e){
                callBack.onLoadingError("解析数据有问题了!");
            }
        }else{
            String msg = bean.getMsg();
            if(!TextUtils.isEmpty(msg)){
                callBack.onLoadingError(bean.getMsg());
            }else{
                callBack.onLoadingError("连接失败!");
            }
        }
    }


    public TextWatcher getEditTextChangedListener() {
        if (et_textWatcher == null) {
            et_textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int length = s.length();
                    if(etInterface!=null){
                        etInterface.onChanged(""+(300-length));
                    }
                }
            };
        }
        return et_textWatcher;
    }


}
