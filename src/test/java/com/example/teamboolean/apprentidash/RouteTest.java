package com.example.teamboolean.apprentidash;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RouteTest {

    @InjectMocks
    private ApprentiDashController apprentiDashController;
    private MockMvc mockMvc;

    @Before
    public void setup(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");

        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ApprentiDashController())
                                        .setViewResolvers(viewResolver)
                                        .build();
    }

    @Test
    public void test_root() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void test_signup() throws Exception {
        this.mockMvc.perform(get("/signup")).andExpect(status().isOk());
    }


    @Test
    public void test_recordHour() throws Exception {
        this.mockMvc.perform(get("/recordHour")).andExpect(status().isOk());
    }


}
