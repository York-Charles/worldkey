package com.worldkey;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


public class WorldkeyApplicationTests {

	@Test
	public void contextLoads() {
		List<String> aaa = new ArrayList<String>();
		aaa.add("1");
		aaa.add("2");
		aaa.add("3");
		aaa.add("4");
		aaa.add("5");
		aaa.add("6");
		aaa.add("7");
		aaa.add("8");
		for(String s : aaa){
			if(s.equals("5")){
				aaa.remove(5);
				break;
//				aaa.remove(s);
			}
		}
		System.out.println(aaa);
	}

}
