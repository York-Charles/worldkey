package com.worldkey.util;

public class ArrayDeleteUtil {
	
	 public static String[] delete(int index, String array[]) {
	        //数组的删除其实就是覆盖前一位
		 String[] arrNew = new String[array.length - 1];
	        for (int i = index; i < array.length - 1; i++) {
	            array[i] = array[i + 1];
	        }
	        System.arraycopy(array, 0, arrNew, 0, arrNew.length);
	        return arrNew;
	    }

}
