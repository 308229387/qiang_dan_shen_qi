package wuba.zhaobiao.grab.fragment;

import wuba.zhaobiao.common.fragment.BaseFragment;
import wuba.zhaobiao.grab.model.BusinessOpportunityModel;

/**
 * Created by SongYongmeng on 2016/9/5.
 * 描    述：商机fragment
 */
public class BusinessOpportunityFragment extends BaseFragment<BusinessOpportunityModel> {

    @Override
    protected BusinessOpportunityModel createModel() {
        return new BusinessOpportunityModel();
    }
}
