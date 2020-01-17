package test.com.duogesi.controller; 

import com.duogesi.Utils.Httputils;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.HashMap;

/** 
* itemservlet Tester. 
* 
* @author <Authors name> 
* @since <pre>���� 9, 2019</pre> 
* @version 1.0 
*/ 
public class itemservletTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: get(items items) 
* 
*/ 
@Test
public void testGet() throws Exception {
    Httputils httputils=new Httputils();
    HashMap map=new HashMap();
//    map.put("qiyungang","深圳");
//    map.put("mudigang","FTW1");
    httputils.doPost("http://localhost:8091/elogistic/items/redis_get.do",map,"UTF-8");
} 


} 
