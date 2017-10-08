package ru.hixon.dao;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hixon.gen.tables.records.BlogRecord;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BlogDao {

    private final Logger log = LoggerFactory.getLogger(BlogDao.class);

    private static final Function<BlogRecord, Blog> BLOG_RECORD_TO_BLOG_MAPPER =
            record -> new Blog(record.get(ru.hixon.gen.tables.Blog.BLOG.TITLE),
                    record.get(ru.hixon.gen.tables.Blog.BLOG.BODY));

    @Autowired
    private DSLContext dsl;

    @Transactional
    public void store(@NotNull Blog blog) {
        Objects.requireNonNull(blog, "blog");

        log.info("store(): blog={}", blog);

        dsl.insertInto(ru.hixon.gen.tables.Blog.BLOG)
                .set(ru.hixon.gen.tables.Blog.BLOG.TITLE, blog.getTitle())
                .set(ru.hixon.gen.tables.Blog.BLOG.BODY, blog.getBody())
                .execute();
    }

    @Transactional
    public void delete(@NotNull Blog blog) {
        Objects.requireNonNull(blog, "blog");
        delete(blog.getTitle());
    }

    @Transactional
    public void delete(@NotNull String title) {
        Objects.requireNonNull(title, "title");

        log.info("delete(): title={}", title);

        dsl.deleteFrom(ru.hixon.gen.tables.Blog.BLOG)
                .where(ru.hixon.gen.tables.Blog.BLOG.TITLE.eq(title))
                .execute();
    }

    public List<Blog> selectAll() {
        log.info("selectAll()");

        return Arrays.stream(dsl.selectFrom(ru.hixon.gen.tables.Blog.BLOG).fetchArray())
                .map(BLOG_RECORD_TO_BLOG_MAPPER)
                .collect(Collectors.toList());
    }

    public Optional<Blog> findByTitle(@NotNull String title) {
        Objects.requireNonNull(title, "title");

        log.info("findBytitle(): title={}", title);

        return dsl.selectFrom(ru.hixon.gen.tables.Blog.BLOG)
                .where(ru.hixon.gen.tables.Blog.BLOG.TITLE.eq(title))
                .fetchOptional()
                .map(BLOG_RECORD_TO_BLOG_MAPPER);
    }
}
