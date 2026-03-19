package com.example.candiflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardStatsDTO {
    private long total;
    private long applied;
    private long interview;
    private long offer;
    private long rejected;
    private double interviewRate;
    private double offerRate;
}
