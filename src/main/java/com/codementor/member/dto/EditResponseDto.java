package com.codementor.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditResponseDto {
    private boolean success;
    private String errorCode;
    private String message;
    private List<String> errorCodes;
    private List<String> messages;
    private String data;

}
