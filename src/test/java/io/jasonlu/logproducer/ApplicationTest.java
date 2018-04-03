package io.jasonlu.logproducer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
// Mock表示使用模拟的应用服务器，默认值就是这个
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ApplicationTest {

    private String logPath = "/tmp/log/logger-printer.log";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void tearDown() {
        new File(logPath).delete();
    }

    @Test
    public void testContext() {
        assertNotNull(context);
    }

    @Test
    public void testStartProducer() throws Exception {
        MvcResult result = mockMvc.perform(post("/log-producer")
                .param("logPerSecond", "10")
                .param("total", "100")
                .param("length", "10")
        ).andExpect(status().isOk())
        .andDo(print())
        .andReturn();

        String progressId = result.getResponse().getContentAsString();
        assertNotNull(progressId);
        assertFalse(progressId.equals(""));

        mockMvc.perform(get("/progress/{id}", progressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.running").value(true))
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.beginAt").isNotEmpty())
                .andDo(print());

        Thread.sleep(10000);
        mockMvc.perform(get("/progress/{id}", progressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finished").value(true))
                .andExpect(jsonPath("$.count").value(100))
                .andExpect(jsonPath("$.endAt").isNotEmpty())
                .andDo(print());
    }

    @Test
    public void testCheckNotExistProgress() throws Exception {
        mockMvc.perform(get("/progress/1234"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testCheckWithWrongProgressId() throws Exception {
        mockMvc.perform(post("/log-producer")
                .param("logPerSecond", "10")
                .param("total", "100")
        );

        mockMvc.perform(get("/progress/wrongId"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testStartLoggerProducer() throws Exception {
        MvcResult result = mockMvc.perform(post("/log-producer")
                .param("logPerSecond", "10")
                .param("total", "100")
                .param("length", "10")
                .param("printer", "loggerPrinter")
        ).andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String progressId = result.getResponse().getContentAsString();
        assertNotNull(progressId);
        assertFalse(progressId.equals(""));
    }
}
