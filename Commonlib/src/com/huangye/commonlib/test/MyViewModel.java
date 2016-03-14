package com.huangye.commonlib.test;//package com.huangye.commonlib.test;
//
//import com.huangye.commonlib.vm.ListStorageViewModel;
//import com.huangye.commonlib.vm.callback.StorageVMCallBack;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.TextView;
//
//public class MyViewModel extends ListStorageViewModel{
//
//	public MyViewModel(Context context, StorageVMCallBack callback) {
//		super(context, callback);
//		// TODO Auto-generated constructor stub
//	}
//
//
//	TextView text ;
//	View rootView ;
//	
//	
////	public MyViewModel(View view, Context context) {
////		
////    	super(view, context);
//////		
//////		model = new MyStoreModel(context, this);
//////		
//////		rootView = view;
//////		
//////		text = (TextView) rootView.findViewById(R.id.dage);
//////		
//////		
//////		model.getData("title", 10, 1);
////		// TODO Auto-generated constructor stub
////	}
//
//
//	
////	public MyViewModel(View rootView ,Context context ,) {
////		
////		MyStoreModel model = new MyStoreModel(context, this);
////		
////		
////		super(context,model);
////		this.rootView = rootView;
////		rootView.findViewById(R.id.dage);
////		// TODO Auto-generated constructor stub
////	}
//	
////	public <T> MyViewModel(View rootView , Context context, ListStorageBaseModel<DecorationBean> model) {
////		
////		model = new MyStoreModel(context, this);
////		
////		super(context, model);
////		// TODO Auto-generated constructor stub
////		text = (TextView) rootView.findViewById(R.id.dage);
////	}
//
//	
//	@Override
//	public void getDataSuccess(Object o) {
//		// TODO Auto-generated method stub
//		text.setText("haha");
//	}
//
//
//	@Override
//	public void getDataFailure() {
//		// TODO Auto-generated method stub
//		
//	}
//}
