package test.com.duogesi.controller; 

import com.duogesi.Utils.Httputils;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.HashMap;

/** 
* orderservlet Tester. 
* 
* @author <Authors name> 
* @since <pre>���� 15, 2019</pre> 
* @version 1.0 
*/ 
public class orderservletTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: order(order order) 
* 
*/ 
@Test
public void testOrder() throws Exception {
    Httputils httputils=new Httputils();
    HashMap map=new HashMap();
    map.put("id","1");
    httputils.doPost("http://localhost:8080/order/make.do", map,"UTF-8");
} 


} 
