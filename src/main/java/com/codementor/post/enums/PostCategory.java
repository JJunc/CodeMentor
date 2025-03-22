package com.codementor.post.enums;

public enum PostCategory {
    JOB("취업"),
    BACKEND("백엔드"),
    FRONTEND("프론트엔드"),
    FREE("자유게시판");

    String postCategory;

    PostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getPostCategory() {
        return postCategory;
    }
}
