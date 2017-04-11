package netCar.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration   
({"/spring-application.xml"}) //加载配置文件  
public class CacheTest {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	private static final String NET_CAR = "netCar";
	
	@Test
	public void testAdd(){
		
		redisTemplate.opsForHash().increment(NET_CAR,"test",1);
		String json = (String)redisTemplate.opsForHash().get(NET_CAR, "test");
		if(json != null && !"".equals(json)){
			//return JsonUtils.str2Obj(json, String.class);
			System.out.println(json);
		}
	}
		
	
}
	
		
