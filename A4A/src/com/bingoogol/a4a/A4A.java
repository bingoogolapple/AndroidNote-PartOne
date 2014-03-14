package com.bingoogol.a4a;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.lang.reflect.Field;

public class A4A {
    public static void autowire(Activity thisClass, Class<?> baseClass) throws A4AException{
        Class<?> clazz = thisClass.getClass();
        autowireViewsForClass(thisClass, clazz);
        //Do this for all classes in the inheritance chain, until we get to the base class
        while(baseClass.isAssignableFrom(clazz.getSuperclass())){
            clazz = clazz.getSuperclass();
            autowireViewsForClass(thisClass, clazz);
        }
    }

    public static int getLayoutResourceByAnnotation(Object thisClass, Context thisActivity, Class<?> baseClass) {
        A4ALayout layoutAnnotation = thisClass.getClass().getAnnotation(A4ALayout.class);
        Class<?> clazz = thisClass.getClass();
        while(layoutAnnotation == null && baseClass.isAssignableFrom(clazz.getSuperclass())){
            clazz = clazz.getSuperclass();
            layoutAnnotation = clazz.getAnnotation(A4ALayout.class);
        }
        if(layoutAnnotation == null){
            return 0;
        }
        if(layoutAnnotation.value() != 0){
            return layoutAnnotation.value();
        }
        String className = thisClass.getClass().getSimpleName();
        int layoutId = thisActivity.getResources().getIdentifier(className, "layout", thisActivity.getPackageName());
        return layoutId;
    }

    public static void saveFieldsToBundle(Bundle bundle, Object thisClass, Class<?> baseClass){
        Class<?> clazz = thisClass.getClass();
        while(baseClass.isAssignableFrom(clazz)){
            for(Field field : clazz.getDeclaredFields()){
                if(field.isAnnotationPresent(A4ASaveInstance.class)){
                    field.setAccessible(true);
                    try {
                        bundle.putSerializable(clazz.getName() + field.getName(), (Serializable) field.get(thisClass));
                    }
                    catch (ClassCastException e){
                        Log.w("A4A", "The field \"" + field.getName() + "\" was not saved and may not be Serializable.");
                    }
                    catch (Exception e){
                        //Could not put this field in the bundle.
                        Log.w("A4A", "The field \"" + field.getName() + "\" was not added to the bundle");
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    public static void loadFieldsFromBundle(Bundle bundle, Object thisClass, Class<?> baseClass){
        if(bundle == null){
            return;
        }
        Class<?> clazz = thisClass.getClass();
        while(baseClass.isAssignableFrom(clazz)){
            for(Field field : clazz.getDeclaredFields()){
                if(field.isAnnotationPresent(A4ASaveInstance.class)){
                    field.setAccessible(true);
                    try {
                        Object fieldVal = bundle.get(clazz.getName() + field.getName());
                        if(fieldVal != null){
                            field.set(thisClass, fieldVal);
                        }
                    } catch (Exception e){
                        //Could not get this field from the bundle.
                        Log.w("A4A", "The field \"" + field.getName() + "\" was not retrieved from the bundle");
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Autowire views for a fragment.  This method works in as similar way to {@code autowire(Activity thisClass, Class<?> baseClass)}
     * but for a Fragment instead of Activity. This will work with both an Android Fragment a Support Library Fragment.
     *
     * @param thisClass This fragment class.  The type is Object to work around android backwards compatibility 
     * with the API, as Fragment can come from the core API or from the support library.
     * @param baseClass The Fragment's base class.  Allows inherited views.
     * @param contentView The Fragment's main content view
     * @param context Context for the fragment's activity. Generally, this should be {@code getActivity()}
     * @throws A4AException Indicates that there was an issue autowiring a view to an annotated field. Will not be thrown if required=false
     * on the {@link A4AView} annotation.
     */
    public static void autowireFragment(Object thisClass, Class<?> baseClass, View contentView, Context context) throws A4AException{
        Class<?> clazz = thisClass.getClass();
        autowireViewsForFragment(thisClass, clazz, contentView, context);
        //Do this for all classes in the inheritance chain, until we get to this class
        while(baseClass.isAssignableFrom(clazz.getSuperclass())){
            clazz = clazz.getSuperclass();
            autowireViewsForFragment(thisClass, clazz, contentView, context);
        }
    }

    /**
     * Autowire a custom view class. Load the sub views for the custom view using the {@link A4AView} annotation.
     * Inheritance structures are supported.
     * @param thisClass This Android View class to be autowired.
     * @param baseClass The views parent, allowing inherited views to be autowired, if necessary. If there is no custom
     * base class, just use this custom view's class.
     * @param context Context
     * @throws A4AException Indicates that there was an issue autowiring a view to an annotated field. 
     * Will not be thrown if required=false on the {@link A4AView} annotation.
     */
    public static void autowireView(View thisClass, Class<?> baseClass, Context context) throws A4AException{
        autowireFragment(thisClass, baseClass, thisClass, context);
    }

    private static void autowireViewsForFragment(Object thisFragment, Class<?> clazz, View contentView, Context context){
        for (Field field : clazz.getDeclaredFields()){
            if(!field.isAnnotationPresent(A4AView.class)){
                continue;
            }
            if(!View.class.isAssignableFrom(field.getType())){
                continue;
            }
            A4AView A4AView = field.getAnnotation(A4AView.class);
            int resId = A4AView.value();
            if(resId == 0){
                String viewId = A4AView.id();
                if(A4AView.id().equals("")){
                    viewId = field.getName();
                }
                resId = context.getResources().getIdentifier(viewId, "id", context.getPackageName());
            }
            try {
                View view = contentView.findViewById(resId);
                if(view == null){
                    if(!A4AView.required()){
                        continue;
                    }else{
                        throw new A4AException("No view resource with the id of " + resId + " found. "
                                +" The required field " + field.getName() + " could not be autowired" );
                    }
                }
                field.setAccessible(true);
                field.set(thisFragment,view);
            } catch (Exception e){
                throw new A4AException("Cound not Autowire A4AView: " + field.getName() + ". " + e.getMessage());
            }
        }
    }

    private static void autowireViewsForClass(Activity thisActivity, Class<?> clazz){
        for (Field field : clazz.getDeclaredFields()){
            if(!field.isAnnotationPresent(A4AView.class)){
                continue;
            }
            if(!View.class.isAssignableFrom(field.getType())){
                continue;
            }
            A4AView A4AView = field.getAnnotation(A4AView.class);
            int resId = A4AView.value();
            if(resId == 0){
                String viewId = A4AView.id();
                if(A4AView.id().equals("")){
                    viewId = field.getName();
                }
                resId = thisActivity.getResources().getIdentifier(viewId, "id", thisActivity.getPackageName());
            }
            try {
                View view = thisActivity.findViewById(resId);
                if(view == null){
                    if(!A4AView.required()){
                        continue;
                    }else{
                        throw new A4AException("No view resource with the id of " + resId + " found. "
                                +" The required field " + field.getName() + " could not be autowired" );
                    }
                }
                field.setAccessible(true);
                field.set(thisActivity,view);
            } catch (Exception e){
                throw new A4AException("Cound not Autowire A4AView: " + field.getName() + ". " + e.getMessage());
            }
        }
    }
}
