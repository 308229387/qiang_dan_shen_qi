package com.huangye.commonlib.file;

import android.content.Context;
import android.util.Log;

import com.huangye.commonlib.delegate.StorageCallBack;
import com.huangye.commonlib.sql.SqlUpgradeCallback;
import com.huangye.commonlib.utils.ConditionBean;
import com.huangye.commonlib.utils.LogUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

public class SqlUtils {

	public static SqlUtils sqlUtils;
	public static DbUtils dbUtils;


	public static void initDB(Context context, final SqlUpgradeCallback callback) {
		/*dbUtils = DbUtils.create(context, "huangye", 1, new DbUtils.DbUpgradeListener() {
			@Override
			public void onUpgrade(final DbUtils dbUtil, int oldVersion, int newVersion) {

			}
		});*/
		dbUtils = DbUtils.create(context,"huangye",1,new DbUtils.DbUpgradeListener(){
			public void onUpgrade(DbUtils dbUtil, int oldVersion, int newVersion) {
				LogUtils.LogE("shenzhixinDB", "old:" + oldVersion + ",new:" + newVersion);
				if (newVersion > oldVersion && callback != null) {
					callback.onUpgrade(dbUtil);
				}
			}
		});
	}
			public static SqlUtils getInstance(Context context) {
				if (sqlUtils == null) {
					sqlUtils = new SqlUtils();
					LogUtils.LogE("shenzhixinDB", "getInstance");
					if (dbUtils != null)
						dbUtils.configAllowTransaction(true);//开启事务管理，这样db在多线程操作时就不会出现问题了
				}

				return sqlUtils;
			}

			public void save(Object o, StorageCallBack callback) {
				try {
					Log.e("shenzhixin","aaa:"+(o==null)+"dbutil:"+(dbUtils==null));
					dbUtils.save(o);
					if(callback!=null) {
						callback.insertDataSuccess();
					}
				} catch (DbException e) {
					if(callback!=null) {
						callback.insertDataFailure();
					}
					e.printStackTrace();
				}
			}

			public void saveList(List<Object> list, StorageCallBack callback) {
				try {
					dbUtils.saveAll(list);
					if(callback!=null) {
						callback.insertDataSuccess();
					}
				} catch (DbException e) {
					if(callback!=null) {
						callback.insertDataFailure();
					}
					e.printStackTrace();
				}
			}

			@SuppressWarnings({"unchecked"})
			public <T> T get(Class<T> clazz, String key, String value, StorageCallBack callback) {

				T bean = null;
				try {
					if (null != key && "" != key) {

						Selector select = Selector.from(clazz).where(key, "=", value);

						bean = (T) dbUtils.findFirst(select);

					} else {
						bean = (T) dbUtils.findFirst(clazz);
					}
					if(callback!=null) {
						callback.getDataSuccess(bean);
					}
				} catch (DbException e) {
					if(callback!=null) {
						callback.getDataFailure();
					}
					e.printStackTrace();
				}

				return bean;

			}

			public <T> List<T> getList(Class<T> clazz, String key, String value, StorageCallBack callback) {
				List<T> list = null;
				try {
					if (null != key && "" != key) {
						Selector select = Selector.from(clazz).where(key, "=", value);

						list = dbUtils.findAll(select);

					} else {
						list = dbUtils.findAll(clazz);
					}
					if(callback!=null) {
						callback.getDataSuccess(list);
					}
				} catch (DbException e) {
					if(callback!=null) {
						callback.getDataFailure();
					}
					e.printStackTrace();
				}

				return list;

			}

			public <T> List<T> getListByCondition(Class<T> clazz, List<ConditionBean> conditionList, StorageCallBack callback) {
				List<T> list = null;

				try {
					Selector select = Selector.from(clazz);

					for (int i = 0; i < conditionList.size(); i++) {
						ConditionBean condition = conditionList.get(i);
						WhereBuilder where = WhereBuilder.b(condition.getColumnName(), condition.getOp(), condition.getValue());

						if (i == 0) {
							select.where(where);
						} else {
							select.and(where);
						}

					}
					list = dbUtils.findAll(select);
					if(callback!=null) {
						callback.getDataSuccess(list);
					}
				} catch (DbException e) {
					if(callback!=null) {
						callback.getDataFailure();
					}
					e.printStackTrace();
				}

				return list;

			}



			public <T> List<T> getListPage(Class<T> clazz, String orderBy, int pageSize, int pageNum,
										   StorageCallBack callback) {

				List<T> list = null;
				Selector select = Selector.from(clazz).orderBy(orderBy).limit(pageSize).offset(pageSize * pageNum);

				try {
					list = dbUtils.findAll(select);
					if(callback!=null) {
						callback.getDataSuccess(list);
					}
				} catch (DbException e) {
					if(callback!=null) {
						callback.getDataFailure();
					}
					e.printStackTrace();
				}

				return list;

			}

			public <T> void delete(Class<T> clazz, String key, String value, StorageCallBack callback) {
				try {
					dbUtils.delete(clazz, WhereBuilder.b(key, "==", value));
					if(callback!=null) {
						callback.deleteDataSuccess();
					}
				} catch (DbException e) {
					if(callback!=null) {
						callback.deleteDataFailure();
					}
					e.printStackTrace();
				}
			}

			public <T> void deleteByCondition(Class<T> clazz, List<ConditionBean> conditionList, StorageCallBack callback) {

				try {
					WhereBuilder where = null;

					for (int i = 0; i < conditionList.size(); i++) {
						ConditionBean condition = conditionList.get(i);

						if (i == 0) {
							where = WhereBuilder.b(condition.getColumnName(), condition.getOp(), condition.getValue());
						} else {
							where.and(condition.getColumnName(), condition.getOp(), condition.getValue());
						}

					}

					dbUtils.delete(clazz, where);
					if(callback!=null) {
						callback.deleteDataSuccess();
					}
				} catch (DbException e) {
					if(callback!=null) {
						callback.deleteDataFailure();
					}
					e.printStackTrace();
				}


			}


		}


