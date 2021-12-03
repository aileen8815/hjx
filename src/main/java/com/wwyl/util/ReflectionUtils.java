package com.wwyl.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author Fyun Li
 */
@SuppressWarnings("unchecked")
public final class ReflectionUtils {

	private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	private ReflectionUtils() {
	}

	public static Object getFieldValue(final Object object, final String fieldName) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}

		makeAccessible(field);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	public static void setFieldValue(final Object object, final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}

		makeAccessible(field);

		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
	}

	protected static Field getDeclaredField(final Object object, final String fieldName) {
		Assert.notNull(object, "object cannot be empty");
		return getDeclaredField(object.getClass(), fieldName);
	}

	protected static Field getDeclaredField(final Class clazz, final String fieldName) {
		Assert.notNull(clazz, "clazz cannot be empty");
		Assert.hasText(fieldName, "fieldName");
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// Field not in object
			}
		}
		return null;
	}

	protected static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	public static Object forceInvokeMethod(Object object, String methodName, Object... params) throws NoSuchMethodException {
		Assert.notNull(object);
		Assert.hasText(methodName);
		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}

		Class clazz = object.getClass();
		Method method = null;
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException e) {
				// 方法不在当前类定义,继续向上转型
			}
		}

		if (method == null) {
			throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName);
		}

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = null;
		try {
			result = method.invoke(object, params);
		} catch (Exception e) {
			org.springframework.util.ReflectionUtils.handleReflectionException(e);
		}
		method.setAccessible(accessible);
		return result;
	}

	public static List<Field> getDeclaredFieldsByType(Object object, Class fieldType) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(fieldType)) {
				list.add(field);
			}
		}
		return list;
	}

	public static List<Field> getAllFieldsByType(Object object, Class fieldType) {
		List<Field> list = new ArrayList<Field>();
		for (Class superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			Field[] fields = superClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.getType().isAssignableFrom(fieldType)) {
					list.add(field);
				}
			}
		}
		return list;
	}

	public static Class getPropertyType(Class type, String propertyName) throws NoSuchFieldException {
		return getDeclaredField(type, propertyName).getType();
	}

	public static String getGetterMethodName(Class type, String propertyName) {
		Assert.notNull(type, "Type required");
		Assert.hasText(propertyName, "FieldName required");

		if (type.getName().equals("boolean")) {
			return "is" + StringUtils.capitalize(propertyName);
		} else {
			return "get" + StringUtils.capitalize(propertyName);
		}
	}

	public static Method getGetterMethod(Class type, String propertyName) {
		try {
			return type.getMethod(getGetterMethodName(type, propertyName));
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static Class getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	public static Class getSuperClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn("{}'s superclass not ParameterizedType", clazz.getSimpleName());
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn("{} not set the actual class on superclass generic parameter", clazz.getSimpleName());
			return Object.class;
		}
		return (Class) params[index];
	}

	public static Object simpleClone(Object obj) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		Object result = obj.getClass().newInstance();
		BeanUtils.copyProperties(result, obj);
		return result;
	}

}
