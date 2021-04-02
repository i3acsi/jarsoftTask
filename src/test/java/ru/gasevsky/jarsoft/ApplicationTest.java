package ru.gasevsky.jarsoft;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.gasevsky.jarsoft.controller.BannerController;
import ru.gasevsky.jarsoft.controller.BidController;
import ru.gasevsky.jarsoft.controller.CategoryController;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class ApplicationTest {
    protected MockMvc mockMvc;
    protected BannerController bannerController;
    protected CategoryController categoryController;
    protected BidController bidController;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc =
                MockMvcBuilders
                        .webAppContextSetup(context)
                        .build();
    }

    @Autowired
    public void setLoginController(BannerController bannerController) {
        this.bannerController = bannerController;
    }

    @Autowired
    public void setLoginController(CategoryController categoryController) {
        this.categoryController = categoryController;
    }

    @Autowired
    public void setLoginController(BidController bidController) {
        this.bidController = bidController;
    }

}