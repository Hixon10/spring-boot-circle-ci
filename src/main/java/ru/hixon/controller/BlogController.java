package ru.hixon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.hixon.dao.Blog;
import ru.hixon.dao.BlogDao;

import java.util.List;

@RestController
public class BlogController {

    @Autowired
    private BlogDao blogDao;

    @RequestMapping(value = "/blog/", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Blog> allBlogs() {
        return blogDao.selectAll();
    }

    @RequestMapping(value = "/blog/{title}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Blog findBlog(@PathVariable String title) {
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("title");
        }

        return blogDao.findByTitle(title).orElse(null);
    }

    @RequestMapping(value = "/blog/", method = RequestMethod.POST)
    public void store(@RequestBody Blog blog) {
        if (blog == null) {
            throw new IllegalArgumentException("blog");
        }

        blogDao.store(blog);
    }

    @RequestMapping(value = "/blog/{title}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String title) {
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("title");
        }

        blogDao.delete(title);
    }
}
