package com.huangye.commonlib.listop;
/**
 * list model中进行的操作,写成接口的形式是因为要规范操作的名字---策略模式
 * @author shenzhixin
 *
 */
public interface ListNetModelOp {
	public void loadMore();
	public void refresh();
	public void loadCache();
}
