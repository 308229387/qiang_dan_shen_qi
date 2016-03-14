package com.huangye.commonlib.model;

/**
 * 最基类的view model层
 * @author szx
 *
 */
public class HYBaseModel {

	public TAG type;
	
	public enum TAG{
		LOGIN,LOADMORE,REFRESH
	};
}
