package com.stouduo.dcw;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stouduo.dcw.domain.Const;
import com.stouduo.dcw.domain.User;
import com.stouduo.dcw.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class DcwApplicationTests {
    MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationConnect;

    @Before
    public void setUp() throws JsonProcessingException {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();

    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testLogin() {

    }

    @Test
    public void signup() {

    }

    @Autowired
    UserService userService;

    @Test
    public void saveUser() {
        User user = new User();
        user.setUsername("stouduo");
        user.setPassword("123456");
        user.setEmail("stouduo@qq.com");
        user.setTel("15629807887");
        user.setRoles(Const.ROLE_ADMIN);
        userService.save(user);
    }

    @Test
    public void testEmail() {
    }

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("[{|,]\"(.*?)\":");
        Matcher m = pattern.matcher("{\"account.png\":\"/uploadfiles/2017-11-09/account.png\",\"account.png\":\"/uploadfiles/2017-11-09/account.png\",\"account.png\":\"/uploadfiles/2017-11-09/account.png\"}");
        while (m.find()) {
            System.out.println(m.group());
        }
    }
}
