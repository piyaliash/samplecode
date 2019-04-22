package com.ubs.wmap.eisl.registrationService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubs.wmap.eisl.housekeeping.TokenService;
import com.ubs.wmap.eisl.registrationService.exception.DataNotFoundException;
import com.ubs.wmap.eisl.registrationService.exception.InvalidDataException;
import com.ubs.wmap.eisl.registrationService.model.*;
import com.ubs.wmap.eisl.registrationService.service.RegistrationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RegistrationServiceControllerTest {

    @Autowired
    RegistrationServiceController registrationServiceController;

//    @Autowired
//    private ObjectMapper mapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RegistrationServiceImpl registrationService;

    @LocalServerPort
    private int port;

    @Value("${registration.service.registryAccessEndpoint}")
    private String registryAccessEndpoint;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);


    }
    @Test
    public void getRegistry() throws InvalidDataException, DataNotFoundException {
        RoleRequestVO roleRequestVO = new RoleRequestVO();
        roleRequestVO.setConsume("TestConsume");
        roleRequestVO.setPublish("publish");


        Set<ColumnReferenceRequestVO> columnReferences = new HashSet<>();

        Set<RowReferenceRequestVO> rowReferences = new HashSet<>();

        ColumnReferenceRequestVO columnReferenceRequestVO = new ColumnReferenceRequestVO();
        columnReferenceRequestVO.setName("name");
        columnReferenceRequestVO.setType("type");
        columnReferences.add(columnReferenceRequestVO);

        RowReferenceRequestVO rowReferenceRequestVO = new RowReferenceRequestVO();
        rowReferenceRequestVO.setName("rowName");
        rowReferenceRequestVO.setType("type");
        rowReferences.add(rowReferenceRequestVO);


        RegistrationSO registrationSO = RegistrationSO.builder()
                .userId("testUser1")
                .serviceId("TestService3")
                .company("testComp1")
                .eislToken("eislTokentest")
                .dataEntitlement("dataSent")
                .userName("user1")
                .columnReferences(columnReferences)
                .rowReferences(rowReferences)
                .role(roleRequestVO)
                .build();


        ResponseSO responseSO = new ResponseSO();
        BeanUtils.copyProperties(registrationSO, responseSO);

        String basicToken = tokenService.init("user1","password","srevice1");
        String eislToken = tokenService.init("user1","service1","roles");

        Mockito.when(registrationService.getRegistryResponse(registryAccessEndpoint, basicToken, eislToken)).thenReturn(registrationSO);

        Mockito.when(registrationService.getRegistration(basicToken,eislToken)).thenReturn(responseSO);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + basicToken);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        String uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/application/json/eisl/registrations/v1/registrations")
                .queryParam("eislToken", eislToken)
                .queryParam("companyName","companyName")
                .queryParam("userName","user1")
                .toUriString();
        ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.GET, request, String.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response);


    }


}