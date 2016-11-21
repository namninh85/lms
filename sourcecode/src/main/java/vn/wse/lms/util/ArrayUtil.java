
package vn.wse.lms.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;

import vn.wse.lms.exception.SystemException;

/**
 * @author hcmvvc
 *
 */
@SuppressWarnings({"unchecked","rawtypes"})
public final class ArrayUtil {
    private ArrayUtil(){
        
    }

    public static List<String> toList(String[] arr) {
    	List<String> result = new ArrayList<String>();
    	if (arr != null) {
    		for (String it : arr) {
    			result.add(it.trim());
    		}
    	}
    	return result;
    }
    
    public static String toString(String[] arr) {
    	String result = "";
    	if (arr != null) {
    		for (String it : arr) {
    			result = result + it.trim();
    		}
    	}
    	return result;
    }
    
    public static Set<String> toSet(String[] arr) {
    	Set<String> result = new HashSet<String>();
    	if (arr != null) {
    		for (String it : arr) {
    			result.add(it.trim());
    		}
    	}
    	return result;
    }
    
    public static String toString(Object [] objs) {
        StringBuffer result = new StringBuffer("[");
        if (objs != null) {
            for(int i = 0; i < objs.length; i++){
                Object obj = objs[i];
                if(obj != null){
	                if(obj.getClass().isArray()){
	                    result.append(toString((Object[]) obj));
	                    
	                }else{
	                    result.append("(");
	                    result.append(obj.toString());
	                    result.append(")");
	                }
                }else {
                	result.append("(null)");
                }
                
                if(i < (objs.length - 1)){
                    result.append(",");
                }
            }
        }
        result.append("]");
        return result.toString();
    }
    
    // retrieve the property of the object in the objList with "propertyName" and insert the property in an array.
	public static Object[] getPropertiesToArray(List objList, String propertyName){
    	Object[] result = null;
    	
    	if(objList == null || objList.isEmpty()){
    		return null;
    	}
    	
    	try {
			Class claze =  PropertyUtils.getPropertyType(objList.get(0), propertyName);
			result = (Object[]) Array.newInstance(claze, objList.size());
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new SystemException(e);
		}
    	
    	getPropertiesToArray(result, objList, propertyName);
    	
    	return result;
    }
    
    
    
    
    private static void getPropertiesToArray(Object[] array, List objList, String propertyName){
    	for(int i = 0; i < objList.size(); i++){
    		try {
				Object value = PropertyUtils.getProperty(objList.get(i), propertyName);
				array[i] = value;
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new SystemException(e);
			}
    	}
    }

    //if a property of obj an is array and the corresponding get method return null, 
    // use an empty array(the length of this array equals the parameter "len") as parameter to call the corresponding set method.
    public static Object initAllNullArray(Object obj, int len){
    	return initAllArray(obj, len, true);
    }
    
    public static Object initAllArray(Object obj, int len){
    	return initAllArray(obj, len, false);
    }
    
    private static Object initAllArray(Object obj, int len, boolean clearWhenIsNull){
        Method [] methods = obj.getClass().getMethods();

        for (int i = 0; i < methods.length; i++) {
            Method getMethod = methods[i];
            if (getMethod.getName().startsWith("get") && 
                getMethod.getParameterTypes().length == 0 &&
                Modifier.isPublic( getMethod.getModifiers()) && 
                getMethod.getReturnType().isArray()) {
                Object returnObj = null;
                
                Class getClaze = getMethod.getReturnType(); 
                try {
                    
                    String setMethodName = getMethod.getName().substring(3);
                    
                    setMethodName = "set" + setMethodName;
                    
                    Method setMethod = obj.getClass().getMethod(setMethodName, new Class[]{getMethod.getReturnType()});
                    if(setMethod != null &&
                       setMethod.getParameterTypes().length == 1 &&
                       Modifier.isPublic(setMethod.getModifiers()) &&
                       getClaze.isAssignableFrom(setMethod.getParameterTypes()[0]) ){
                        
                    	if(clearWhenIsNull){
	                        returnObj = getMethod.invoke(obj, new Object[0]);
	                        
	                        if(returnObj == null){
	                            setMethod.invoke(obj, new Object[]{Array.newInstance(getClaze.getComponentType(), len)});
	                        }
                    	} else {
                    		setMethod.invoke(obj, new Object[]{Array.newInstance(getClaze.getComponentType(), len)});
                    	}
                    }
                } catch (NoSuchMethodException e){
                    continue;
                } catch (IllegalAccessException | InvocationTargetException e){
                    throw new SystemException(e);
                }
            }
        }
        return obj;
    }
    
	public static Set cloneSet(Set set){
    	Set result = null;
    	
    	if(set == null){
    		return result;
    	}
    	
    	
    	if(set instanceof Cloneable){
    		try {
				Object obj = MethodUtils.invokeExactMethod(set, "clone", null);
				if(obj instanceof Set){
					result = (Set) obj;
				}
				
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				// that means this object does not support clone method
			}
    		
    	}
    	
    	// if cannot use clone() to copy, create a new set
    	if(result == null){
    		result = new HashSet();
    		result.addAll(set);
    	}
    	
    	return result;
    }
}
