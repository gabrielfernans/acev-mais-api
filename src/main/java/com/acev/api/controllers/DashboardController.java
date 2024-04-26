package com.acev.api.controllers;

import com.acev.api.dtos.dashboard.DashboardDTO;
import com.acev.api.enums.MemberTypeEnum;
import com.acev.api.services.AgrupeService;
import com.acev.api.services.MinistryService;
import com.acev.api.services.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dashboard")
@RequestMapping("/api/dashboard")
@RestController
public class DashboardController {

  @Autowired
  PersonService personService;

  @Autowired
  AgrupeService agrupeService;

  @Autowired
  MinistryService ministryService;

  @GetMapping()
  public ResponseEntity<DashboardDTO> getDashboardData() {
    Long members = personService.getCountByMemberType(MemberTypeEnum.MEMBER);
    Long congregants = personService.getCountByMemberType(MemberTypeEnum.CONGREGANT);
    Integer agrupes = agrupeService.getAllAgrupes().size();
    Integer ministries = ministryService.getAllMinistries().size();
    Long membersInAgrupes = personService.getCountInAgrupesByMemberType(MemberTypeEnum.MEMBER);
    Long congregantsInAgrupes = personService.getCountInAgrupesByMemberType(MemberTypeEnum.CONGREGANT);
    Long membersInMinistries = personService.getCountInMinistries();

    DashboardDTO dashboardDTO = new DashboardDTO(
          members,
          congregants,
          agrupes,
          ministries,
          membersInAgrupes,
          congregantsInAgrupes,
          membersInMinistries);
    return ResponseEntity.status(HttpStatus.OK).body(dashboardDTO);
  }
}
