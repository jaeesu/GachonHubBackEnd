package com.example.gachonhub.common.exception;

public interface ErrorUtil {
    public static final String NOT_FOUND_CONTENT_ID = "해당 번호의 콘텐츠가 존재하지 않습니다.";
    public static final String NOT_FOUND_USER_ID = "해당 아이디의 사용자를 찾을 수 없습니다.";
    public static final String NOT_FOUND_USER_NICKNAME = "해당 닉네임의 사용자를 찾을 수 없습니다.";

    public static final String NOT_FOUND_USER_ID_IN_GROUP = "해당 아이디의 사용자를 찾을 수 없습니다.";
    public static final String NOT_FOUND_GROUP_ID = "해당 아이디의 그룹을 찾을 수 없습니다.";
    public static final String NOT_CORRECT_USER_ID = "접근 권한이 없는 사용자입니다.";

}
