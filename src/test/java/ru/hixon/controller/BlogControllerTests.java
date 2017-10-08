package ru.hixon.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.hixon.dao.Blog;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogControllerTests {

    @LocalServerPort
    private int port;

    private final String title = UUID.randomUUID().toString();

    private final String body = UUID.randomUUID().toString();

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void fullScenario() throws Exception {
        storeTest();
        findTest();
        selectAllTest();
        deleteTest();
        findAfterRemovingTest();
    }

    public void storeTest() throws Exception {
        Blog blog = new Blog(title, body);
        ResponseEntity<Void> response
                = restTemplate.postForEntity("http://localhost:" + port + "/blog/",
                blog,
                Void.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    public void findTest() throws Exception {
        ResponseEntity<Blog> blogEntity
                = restTemplate.getForEntity("http://localhost:" + port + "/blog/" + title,
                Blog.class);

        Assert.assertEquals(blogEntity.getStatusCode(), HttpStatus.OK);

        Blog blog = blogEntity.getBody();
        Assert.assertEquals(blog.getBody(), body);
        Assert.assertEquals(blog.getTitle(), title);
    }

    public void selectAllTest() throws Exception {
        ResponseEntity<String> blogsEntity
                = restTemplate.getForEntity("http://localhost:" + port + "/blog/",
                String.class);

        Assert.assertEquals(HttpStatus.OK, blogsEntity.getStatusCode());

        List<Blog> blogs
                = new ObjectMapper().readValue(blogsEntity.getBody(),
                new TypeReference<List<Blog>>() { });

        Blog findedBlog = blogs.stream()
                .filter(b -> b.getBody().equals(body) && b.getTitle().equals(title))
                .findAny()
                .orElse(null);

        Assert.assertNotNull(findedBlog);
    }

    public void deleteTest() throws Exception {
        restTemplate.delete("http://localhost:" + port + "/blog/" + title);
    }

    public void findAfterRemovingTest() throws Exception {
        ResponseEntity<Blog> blogEntity
                = restTemplate.getForEntity("http://localhost:" + port + "/blog/" + title,
                Blog.class);

        Assert.assertEquals(blogEntity.getStatusCode(), HttpStatus.OK);

        Blog blog = blogEntity.getBody();
        Assert.assertNull(blog);
    }
}