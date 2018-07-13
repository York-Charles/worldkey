package com.worldkey.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SensitiveWord implements Serializable {
    private Integer id;
    /**
     * 敏感词
     */
    @NotNull(message = "敏感词不能为空")
    private String word;
    /**
     * 类型，true为敏感词，false为停顿符
     */
    private Boolean isWord;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setIsWord(Boolean isWord) {
        this.isWord = isWord;
    }

    public Boolean getIsWord() {
        return isWord;
    }

    @Override
    public String toString() {
        return "SensitiveWord{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", isWord=" + isWord +
                '}';
    }
}