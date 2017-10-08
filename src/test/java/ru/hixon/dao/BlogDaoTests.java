package ru.hixon.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogDaoTests {

    @Autowired
    private BlogDao blogDao;

    @Before
    public void erase() {
        blogDao.selectAll().stream()
                .forEach(blog -> blogDao.delete(blog));
    }

    @Test
    public void storeBlogTest() {
        String title = UUID.randomUUID().toString();

        blogDao.store(new Blog(title, "body1"));

        Blog blog = blogDao.findByTitle(title).orElse(null);

        Assert.assertNotNull(blog);
        Assert.assertEquals(blog.getTitle(), title);
        Assert.assertEquals(blog.getBody(), "body1");
    }

    @Test
    public void deleteBlogTest() {
        String title = UUID.randomUUID().toString();

        blogDao.store(new Blog(title, "body1"));

        Blog blog = blogDao.findByTitle(title).orElse(null);

        Assert.assertNotNull(blog);

        blogDao.delete(blog);

        blog = blogDao.findByTitle(title).orElse(null);

        Assert.assertNull(blog);
    }

    @Test
    public void selectAll() {
        String title1 = UUID.randomUUID().toString();
        blogDao.store(new Blog(title1, "body1"));

        String title2 = UUID.randomUUID().toString();
        blogDao.store(new Blog(title2, "body2"));

        Map<String, Blog> blogs = blogDao.selectAll().stream()
                .collect(Collectors.toMap(
                        Blog::getTitle,
                        Function.identity()
                ));

        Assert.assertEquals(title1, blogs.get(title1).getTitle());
        Assert.assertEquals("body1", blogs.get(title1).getBody());

        Assert.assertEquals(title2, blogs.get(title2).getTitle());
        Assert.assertEquals("body2", blogs.get(title2).getBody());
    }
}