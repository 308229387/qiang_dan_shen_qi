package com.huangye.commonlib.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

abstract class TestTT<T extends Object> {
	private Class<T> clazz;

	public TestTT() {
		Type modelType = getClass().getGenericSuperclass();
		if ((modelType instanceof ParameterizedType)) {
			Type[] modelTypes = ((ParameterizedType) modelType).getActualTypeArguments();
			clazz = ((Class) modelTypes[0]);
		}
		System.out.println(clazz);
	}

	public T find(int id) {
		return (T) getEntityManager().find(clazz, id);
	}

	public EntityManager getEntityManager() {
		return new EntityManager();
	}
}