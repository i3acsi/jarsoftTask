package ru.gasevsky.jarsoft.controller;

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
public class CategoryControllerTest {
    private String baseUrl = "http://localhost:";
    private String categoryUrl = "/category/";
    private String searchUrl = categoryUrl + "search/";
    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void createTest() {
        Category category = testUtil.loadResource("category.create.json", Category.class);

        ResponseEntity<Category> re = restTemplate
                .postForEntity(baseUrl + serverPort + categoryUrl, category, Category.class);
        Assert.assertEquals(HttpStatus.CREATED, re.getStatusCode());
        Assert.assertNotNull(re.getBody());

        Category result = re.getBody();
        testUtil.compareCategoriesIgnoreId(category, result);

        Assert.assertThrows(RestClientException.class, () -> restTemplate
                .postForEntity(baseUrl + serverPort + categoryUrl, category, Category.class));
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
        testUtil.categoryArrayComparing(categoryList, categoryResultList);

        String nameToSearch = categoryList[0].getName().replaceAll("\\d", "");
        re = restTemplate
                .getForEntity(baseUrl + serverPort + searchUrl + nameToSearch, Category[].class);
        Assert.assertEquals(HttpStatus.OK, re.getStatusCode());
        Assert.assertNotNull(re.getBody());

        categoryResultList = re.getBody();
        testUtil.deepCategoryArrayComparing(categoryList, categoryResultList, true);
    }

    @Test
    public void updateTest() {
        String newName = "updated";
        String newReqName = "updatedReq";
        Category category = testUtil.loadResource("category.update.json", Category.class);
        Category result = restTemplate.postForObject(baseUrl + serverPort + categoryUrl, category, Category.class);
        String nameBefore = result.getName();
        String reqBefore = result.getReqName();
        testUtil.compareCategoriesIgnoreId(category, result);

        result.setName(newName);
        result.setReqName(newReqName);
        restTemplate.put(baseUrl + serverPort + categoryUrl, result);

        ResponseEntity<Category[]> re = restTemplate
                .getForEntity(baseUrl + serverPort + searchUrl + nameBefore, Category[].class);
        Assert.assertEquals(HttpStatus.OK, re.getStatusCode());
        Assert.assertNotNull(re.getBody());

        Category[] searchForNameBeforeUpdate = re.getBody();
        Assert.assertEquals(0, searchForNameBeforeUpdate.length);

        re = restTemplate
                .getForEntity(baseUrl + serverPort + searchUrl + newName, Category[].class);
        Assert.assertEquals(HttpStatus.OK, re.getStatusCode());
        Assert.assertNotNull(re.getBody());

        Category[] searchForNameAfterUpdate = re.getBody();
        Assert.assertEquals(1, searchForNameAfterUpdate.length);
        Assert.assertEquals(result, searchForNameAfterUpdate[0]);
    }
}