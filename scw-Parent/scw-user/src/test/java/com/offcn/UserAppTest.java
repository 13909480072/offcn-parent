package com.offcn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserApp.class})
public class UserAppTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
   public void contextLoads(){
        stringRedisTemplate.opsForValue().set("msg","redis中文测试");
    }

}
