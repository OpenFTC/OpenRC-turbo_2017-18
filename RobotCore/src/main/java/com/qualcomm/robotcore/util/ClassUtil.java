/*
 * Copyright (c) 2015 Robert Atkinson
 *
 *    Ported from the Swerve library by Craig MacFarlane
 *    Based upon contributions and original idea by dmssargent.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Robert Atkinson, Craig MacFarlane nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.qualcomm.robotcore.util;

import android.content.res.Resources;

import com.qualcomm.robotcore.R;

import org.firstinspires.ftc.robotcore.external.Predicate;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectAgent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ClassUtil {

    static public List<Constructor> getDeclaredConstructors(Class<?> clazz)
    {
        Constructor<?>[] constructors;
        try {
            constructors = clazz.getDeclaredConstructors();
        }
        catch (Exception|LinkageError e) {
            constructors = new Constructor[0];
        }
        List<Constructor> result = new LinkedList<Constructor>();
        result.addAll(Arrays.asList(constructors));
        return result;
    }

    static public List<Method> getDeclaredMethods(Class<?> clazz)
    {
        Method[] methods;
        try {
            methods = clazz.getDeclaredMethods();
        }
        catch (Exception|LinkageError e) {
            methods = new Method[0];
        }
        List<Method> result = new LinkedList<Method>();
        result.addAll(Arrays.asList(methods));
        return result;
    }

    static public List<Method> getDeclaredMethodsIncludingSuper(Class<?> clazz)
    {
        if (clazz.getSuperclass() == null) {
            return getDeclaredMethods(clazz);
        } else {
            List<Method> result = getDeclaredMethodsIncludingSuper(clazz.getSuperclass());
            result.addAll(getDeclaredMethods(clazz));
            return result;
        }
    }

    /** Answers whether one class is or inherits from another */
    public static boolean inheritsFrom(Class baseClass, Class targetClass)
    {
        while (baseClass != null) {
            // If we've found who we're looking for, then we're golden
            if (baseClass == targetClass) {
                return true;
            }

            // In case target is an interface we need to explore the interface chain as well.
            if (targetClass.isInterface()) {
                for (Class intf : baseClass.getInterfaces()) {
                    if (inheritsFrom(intf, targetClass))
                        return true;
                }
            }

            // Try heading up one level
            baseClass = baseClass.getSuperclass();
        }
        return false;
    }

    public static Method getHiddenMethod(Class clazz, String methodName, Class<?>... types)
    {
        try {
            Method method = clazz.getMethod(methodName, types);
            method.setAccessible(true);
            return method;
        }
        catch (NoSuchMethodException e) {
            RobotLog.ee(WifiDirectAgent.TAG, e, "method not found: %s", methodName);
            return null;
        }
    }

    public static Method getHiddenMethod(Object receiver, String methodName, Class<?>... types)
    {
        return getHiddenMethod(receiver.getClass(), methodName, types);
    }

    public static Object invoke(Object receiver, Method method, Object... args)
    {
        try {
            return method.invoke(receiver, args);
        }
        catch (InvocationTargetException e) {
            Throwable throwable = e.getCause();
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException)throwable;
            }
            else {
                throw new RuntimeException(String.format("exception in %s#%s", method.getDeclaringClass().getSimpleName(), method.getName()), throwable);
            }
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("access denied in %s#%s", method.getDeclaringClass().getSimpleName(), method.getName()), e);
        }
    }

    /** Searches the inheritance tree for the first class for which the predicate returns true */
    public static boolean searchInheritance(Class clazz, Predicate<Class<?>> predicate)
    {
        return searchInheritance(clazz, predicate, new HashSet<Class<?>>());
    }

    private static boolean searchInheritance(Class clazz, Predicate<Class<?>> predicate, Set<Class<?>> visited)
    {
        while (clazz != null) {

            if (visited.contains(clazz)) {
                // We've already done (or are doing) everything here and above
                return false;
            }
            visited.add(clazz);

            if (predicate.test(clazz)) {
                return true;
            }

            // Search all the interfaces we extend or implement
            for (Class intf : clazz.getInterfaces()) {
                if (searchInheritance(intf, predicate, visited))
                    return true;
            }

            // Try heading up one level of implementation
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    public static int getStringResId(String resName, Class<?> c)
    {
        Resources resources = AppUtil.getDefContext().getResources();
        int result = resources.getIdentifier(resName, "string", AppUtil.getDefContext().getPackageName());
        return result;
    }

    /**
     * If string starts with "@string/", the remainder is a string
     * resource we are to look up and return. Otherwise, we are just
     * to return the string.
     */
    public static String decodeStringRes(String string)
    {
        String prefix = "@string/";
        if (string.startsWith(prefix)) {
            // Suffix is the resource name
            String suffix = string.substring(prefix.length());
            int resId = ClassUtil.getStringResId(suffix, R.string.class);
            return AppUtil.getDefContext().getString(resId);
        }
        else {
            return string;
        }
    }

}
