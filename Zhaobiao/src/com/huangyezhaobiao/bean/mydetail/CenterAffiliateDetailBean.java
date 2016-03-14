package com.huangyezhaobiao.bean.mydetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.huangyezhaobiao.bean.popdetail.QDDetailBaseBean;
import com.huangyezhaobiao.fragment.QiangDanBaseFragment;
import com.huangyezhaobiao.inter.MDConstans;
import com.huangyezhaobiao.utils.ActivityUtils;
import com.huangyezhaobiao.utils.DetailsLogBeanUtils;
import com.huangyezhaobiao.utils.LogUtils;
import com.huangyezhaobiao.utils.MDUtils;
import com.huangyezhaobiao.view.ZhaoBiaoDialog;

/**
 * Created by 58 on 2015/8/17.
 * 抢单中心页面的招商加盟detailBean
 */
public class CenterAffiliateDetailBean extends QDDetailBaseBean {
    private Context mContext;
    private ImageView iv_tels;
    private TextView tv_consultCategory;
    private TextView tv_budget;
    private TextView tv_investKeywords;
    private TextView tv_investIndusty;
    private TextView tv_investCity;
    private TextView tv_jobIndusty;
    private TextView tv_jobTitle;
    private TextView tv_jobExperience;
    private TextView tv_shopExperience;
    private TextView tv_special;
    private TextView tv_clientPhone;

    private String name;
    private String consultCategory;
    private String budget;
    private String investKeywords;
    private String investIndusty;
    private String investCity;
    private String jobIndusty;
    private String jobTitle;
    private String jobExperience;
    private String shopExperience;
    private String special;
    private String clientPhone;
    private ZhaoBiaoDialog dialog;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConsultCategory() {
        return consultCategory;
    }

    public void setConsultCategory(String consultCategory) {
        this.consultCategory = consultCategory;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getInvestKeywords() {
        return investKeywords;
    }

    public void setInvestKeywords(String investKeywords) {
        this.investKeywords = investKeywords;
    }

    public String getInvestIndusty() {
        return investIndusty;
    }

    public void setInvestIndusty(String investIndusty) {
        this.investIndusty = investIndusty;
    }

    public String getInvestCity() {
        return investCity;
    }

    public void setInvestCity(String investCity) {
        this.investCity = investCity;
    }

    public String getJobIndusty() {
        return jobIndusty;
    }

    public void setJobIndusty(String jobIndusty) {
        this.jobIndusty = jobIndusty;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobExperience() {
        return jobExperience;
    }

    public void setJobExperience(String jobExperience) {
        this.jobExperience = jobExperience;
    }

    public String getShopExperience() {
        return shopExperience;
    }

    public void setShopExperience(String shopExperience) {
        this.shopExperience = shopExperience;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    /**
     * private ImageView iv_tels;
     * <p/>
     * <p/>
     * <p/>
     * <p/>
     * <p/>
     * <p/>
     * private TextView  tv_special;
     * private TextView  tv_clientPhone;
     *
     * @param context
     * @return
     */
    @Override
    public View initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_affiliates_details, null);
        tv_budget = (TextView) view.findViewById(R.id.budget);
        tv_clientPhone = (TextView) view.findViewById(R.id.clientPhone);
        tv_consultCategory = (TextView) view.findViewById(R.id.consultCategory);
        tv_investCity = (TextView) view.findViewById(R.id.investCity);
        tv_investKeywords = (TextView) view.findViewById(R.id.investKeywords);
        tv_investIndusty = (TextView) view.findViewById(R.id.investIndusty);
        tv_jobIndusty = (TextView) view.findViewById(R.id.jobIndusty);
        tv_jobTitle = (TextView) view.findViewById(R.id.jobTitle);
        tv_jobExperience = (TextView) view.findViewById(R.id.jobExperience);
        tv_shopExperience = (TextView) view.findViewById(R.id.shopExperience);
        tv_special = (TextView) view.findViewById(R.id.special);
        iv_tels = (ImageView) view.findViewById(R.id.iv_tels);
        fillDatas();
        return view;
    }

    /**
     * 填充数据
     */
    private void fillDatas() {
        tv_budget.setText(budget);
        tv_consultCategory.setText(consultCategory);
        tv_special.setText(special);
        tv_clientPhone.setText(clientPhone);
        tv_investCity.setText(investCity);
        tv_investKeywords.setText(investKeywords);
        tv_investIndusty.setText(investIndusty);
        tv_jobIndusty.setText(jobIndusty);
        tv_jobTitle.setText(jobTitle);
        tv_jobExperience.setText(jobExperience);
        tv_shopExperience.setText(shopExperience);
        iv_tels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialog(mContext);
                dialog.show();
            }
        });
    }


    private void initDialog(Context context) {
        if (dialog == null) {
            dialog = new ZhaoBiaoDialog(context, context.getString(R.string.hint), context.getString(R.string.make_sure_tel));
            dialog.setOnDialogClickListener(new ZhaoBiaoDialog.onDialogClickListener() {

                @Override
                public void onDialogOkClick() {
                    LogUtils.LogE("assssshenaaa", "bidid:" + DetailsLogBeanUtils.bean.getBidID() + ",cateid:" + DetailsLogBeanUtils.bean.getCateID());
                    ActivityUtils.goToDialActivity(mContext, clientPhone);
                    MDUtils.OrderDetailsPageMD(QiangDanBaseFragment.orderState, DetailsLogBeanUtils.bean.getCateID() + "", orderId + "", MDConstans.ACTION_DOWN_TEL, clientPhone);
                    dialog.dismiss();
                    dialog = null;
                }

                @Override
                public void onDialogCancelClick() {
                    dialog.dismiss();
                    dialog = null;
                }
            });
        }
    }
}
