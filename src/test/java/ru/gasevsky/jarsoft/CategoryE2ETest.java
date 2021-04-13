package ru.gasevsky.jarsoft;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestClientException;
import ru.gasevsky.jarsoft.model.Category;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;

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
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createTest() throws IOException {
        Category category = loadResource("category.create.json", Category.class);

        ResponseEntity<Category> re = restTemplate
                .postForEntity(baseUrl + serverPort + categoryUrl, category, Category.class);
        Assert.assertEquals(HttpStatus.CREATED, re.getStatusCode());
        Assert.assertNotNull(re.getBody());

        Category result = re.getBody();
        compareCategoriesIgnoreId(category, result);

        Assert.assertThrows(RestClientException.class, () -> {
            restTemplate
                    .postForEntity(baseUrl + serverPort + categoryUrl, category, Category.class);
        });
    }

    @Test
    public void getTest() {
        Category category = loadResource("category.getAll.json", Category.class);
        restTemplate.postForEntity(baseUrl + serverPort + categoryUrl, category, Category.class);


        ResponseEntity<List> re = restTemplate
                .getForEntity(baseUrl + serverPort + categoryUrl, List.class);
        Assert.assertEquals(HttpStatus.OK, re.getStatusCode());
        Assert.assertNotNull(re.getBody());

        List list = re.getBody();
        Assert.assertTrue(list.size() > 0);
        LinkedHashMap<String, Object> map =(LinkedHashMap<String, Object>) list.get(0);
        Category result = objectMapper.convertValue(map, Category.class);

        compareCategoriesIgnoreId(category, result);
    }

    private <T> T loadResource(String resource, Class<T> cl) {
        String url = "classpath:" + resource;
        Resource res = resourceLoader.getResource(url);
        try (Reader reader = new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8)) {
            String json = FileCopyUtils.copyToString(reader);
            return objectMapper.readValue(json, cl);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private void compareCategoriesIgnoreId(Category expected, Category actual) {
        Assert.assertNotNull(expected);
        Assert.assertNotNull(expected.getName());
        Assert.assertNotNull(expected.getReqName());
        Assert.assertNotNull(expected.getDeleted());
        Assert.assertNotNull(actual);
        Assert.assertNotNull(actual.getId());
        Assert.assertNotNull(actual.getName());
        Assert.assertNotNull(actual.getReqName());
        Assert.assertNotNull(actual.getDeleted());
        Assert.assertNotEquals(0, (int) actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getReqName(), actual.getReqName());
        Assert.assertEquals(expected.getDeleted(), actual.getDeleted());
    }
}

