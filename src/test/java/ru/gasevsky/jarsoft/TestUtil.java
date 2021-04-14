package ru.gasevsky.jarsoft;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import ru.gasevsky.jarsoft.model.Category;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
class TestUtil {
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    <T> T loadPojoFromMap(LinkedHashMap<String, Object> map, Class<T> cl) {
        return objectMapper.convertValue(map, cl);
    }

    <T> T loadResource(String resource, Class<T> cl) {
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

    void deepCategoryArrayComparing(Category[] expected, Category[] result, boolean ignoreId) {
        Assert.assertNotNull(expected);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.length, result.length);

        List<Category> expectedList = new ArrayList<>(expected.length);
        List<Category> resultList = new ArrayList<>(result.length);

        expectedList.addAll(Arrays.asList(expected));
        resultList.addAll(Arrays.asList(result));

        int c = 0;
        while (!resultList.isEmpty()) {
            Category cRes = resultList.remove(0);
            if (ignoreId) {
                Assert.assertTrue(cRes.getId() > 0);
                cRes.setId(null);
            }
            Assert.assertTrue(expectedList.remove(cRes));
            c++;
        }
        Assert.assertEquals(c, expected.length);
    }

    void compareCategoriesIgnoreId(Category expected, Category actual) {
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