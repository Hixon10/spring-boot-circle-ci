package ru.hixon.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Blog {

    private final String title;

    private final String body;

    @JsonCreator
    public Blog(@JsonProperty("title") @NotNull String title,
                @JsonProperty("body") @NotNull String body) {
        this.title = Objects.requireNonNull(title, "title");
        this.body = Objects.requireNonNull(body, "body");
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
