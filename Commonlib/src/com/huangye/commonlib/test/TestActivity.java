package com.huangye.commonlib.test;//package com.huangye.commonlib.test;
//
//import com.huangye.commonlib.R;
//import com.huangye.commonlib.activity.BaseActivity;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//
//public class TestActivity extends BaseActivity {
//	public boolean isConfig = true;
//	public MyViewModel model ;
//    
//	@Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        
//        View rootView = LayoutInflater.from(this).inflate(R.layout.test, null);
//        setContentView(rootView);
//        
//        //model = new MyViewModel(rootView ,this);
////        model = new MyStoreModel(rootView,this, this);
////
////        DecorationBean bean = new DecorationBean();
////        bean.setTitle("hahahahhah");
////        DecorationBean bean1 = new DecorationBean();
////        bean1.setTitle("hahahahhah2");
////        
////        model.putData(bean);
////        
////        model.putData(bean1);
////        
////        model.getData("title", 10, 1);
////        
//    }
//
//    @Override
//	public void initView() {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public void initListener() {
//		// TODO Auto-generated method stub
//		
//	}
////
////	@SuppressWarnings("unchecked")
////	@Override
////	public void getDataSuccess(Object o) {
////		Toast.makeText(getApplicationContext(), ((List<DecorationBean>) o).get(0).getTitle(),
////			     Toast.LENGTH_SHORT).show();
////	}
////
////	@Override
////	public void getDataFailure() {
////		// TODO Auto-generated method stub
////		
////	}
////
////	public static void main (String[] a){
////		System.out.print("haha");
////	}
//
//
//}
