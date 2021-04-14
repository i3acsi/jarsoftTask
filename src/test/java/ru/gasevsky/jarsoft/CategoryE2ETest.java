package ru.gasevsky.jarsoft;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import ru.gasevsky.jarsoft.model.Category;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@Slf4j
public class CategoryE2ETest {
    private String baseUrl = "http://localhost:";
    private String categoryUrl = "/category/";
    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void createTest() throws IOException {
        Category category = testUtil.loadResource("category.create.json", Category.class);

        ResponseEntity<Category> re = restTemplate
                .postForEntity(baseUrl + serverPort + categoryUrl, category, Category.class);
        Assert.assertEquals(HttpStatus.CREATED, re.getStatusCode());
        Assert.assertNotNull(re.getBody());

        Category result = re.getBody();
        testUtil.compareCategoriesIgnoreId(category, result);

        Assert.assertThrows(RestClientException.class, () -> {
            restTemplate
                    .postForEntity(baseUrl + serverPort + categoryUrl, category, Category.class);
        });
    }

    @Test
    public void getTest() {
        Category[] categoryList = testUtil.loadResource("category.getAll.json", Category[].class);
        for (Category category : categoryList) {
            restTemplate.postForEntity(baseUrl + serverPort + categoryUrl, category, Category.class);
        }

        ResponseEntity<Category[]> re = restTemplate
                .getForEntity(baseUrl + serverPort + categoryUrl, Category[].class);
        Assert.assertEquals(HttpStatus.OK, re.getStatusCode());
        Assert.assertNotNull(re.getBody());

        Category[] categoryResultList = re.getBody();
        testUtil.deepCategoryArrayComparing(categoryList, categoryResultList, true);

        String toSearch = "search/" + categoryList[0].getName().replaceAll("\\d", "");
        re = restTemplate
                .getForEntity(baseUrl + serverPort + categoryUrl + toSearch, Category[].class);
        Assert.assertEquals(HttpStatus.OK, re.getStatusCode());
        Assert.assertNotNull(re.getBody());

        categoryResultList = re.getBody();
        testUtil.deepCategoryArrayComparing(categoryList, categoryResultList, true);
    }

}

