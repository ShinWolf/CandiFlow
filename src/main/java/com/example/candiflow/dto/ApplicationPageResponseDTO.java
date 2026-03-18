package com.example.candiflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ApplicationPageResponseDTO {
    private List<ApplicationResponseDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
