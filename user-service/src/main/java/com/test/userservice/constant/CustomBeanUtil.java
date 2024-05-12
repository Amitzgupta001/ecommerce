package com.test.userservice.constant;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class CustomBeanUtil {


    public static void copyPropertiesNotNull(Object dest, Object orig) throws InvocationTargetException, IllegalAccessException {
        NullAwareBeanUtilsBean.getInstance().copyProperties(dest, orig);
    }

    private static class NullAwareBeanUtilsBean extends BeanUtilsBean {

        private static NullAwareBeanUtilsBean nullAwareBeanUtilsBean;

        NullAwareBeanUtilsBean() {
            super(new ConvertUtilsBean(), new PropertyUtilsBean() {
                @Override
                public PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {
                    return BeanUtils.getPropertyDescriptors(beanClass);
                }

                @Override
                public PropertyDescriptor getPropertyDescriptor(Object bean, String name) {
                    return BeanUtils.getPropertyDescriptor(bean.getClass(), name);
                }
            });
        }

        public static NullAwareBeanUtilsBean getInstance() {
            if (nullAwareBeanUtilsBean == null) {
                nullAwareBeanUtilsBean = new NullAwareBeanUtilsBean();
            }
            return nullAwareBeanUtilsBean;
        }

        @Override
        public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
            if (value == null) return;
            super.copyProperty(bean, name, value);
        }
    }

}
