package com.acev.api.dtos.dashboard;

public record DashboardDTO(Long members,
                           Long congregants,
                           Integer agrupes,
                           Integer ministries,
                           Long membersInAgrupes,
                           Long congregantsInAgrupes,
                           Long membersInMinistries) {
}
