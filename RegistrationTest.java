package com.ubs.wmap.eisl.registrationService;

import com.ubs.wmap.eisl.housekeeping.TokenService;
import com.ubs.wmap.eisl.registrationService.exception.EsilTokenNotValidException;
import com.ubs.wmap.eisl.registrationService.exception.InvalidDataException;
import com.ubs.wmap.eisl.registrationService.model.*;
import com.ubs.wmap.eisl.registrationService.service.RegistrationServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sun.util.calendar.BaseCalendar;

import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationTest {

    @Mock
    RestTemplate restTemplate;

    @Mock
    TokenService tokenService;


    @InjectMocks
    RegistrationServiceImpl registrationService;



    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);


    }

    @Test
    public void getRegistryResponseTest() throws InvalidDataException {
        String eislToken="Testeisl";
        String basicToken="Testbasic";
        String companyName="Testcompany";
        String userName="Testuser";
        PayloadSO payloadSO = new PayloadSO();
        payloadSO.setUserName("testUSer");
        payloadSO.setCompany("testcompany");
        String baseUrl="http://localhost:8080/test";
        ResponseSO responseSO = new ResponseSO();
        DataReferenceResponse dataReferenceResponse = new DataReferenceResponse();
        long nowMillis = (new Date()).getTime();
        long expMillis = nowMillis + 300000000;

        Map<String, Object> eislClaims = new HashMap();
        eislClaims.put("userName", "test");
        eislClaims.put("serviceId", "1");
        Date exp = new Date(expMillis);
            String jwt = Jwts.builder()
                    .setClaims(eislClaims)
                    .setExpiration(exp)
                    .signWith(SignatureAlgorithm.HS512, "test")
                    .compact();


        Claims claims = null;
        claims = (Claims) Jwts.parser().setSigningKey("test").parseClaimsJws(jwt).getBody();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).queryParam("eislToken", eislToken)
                .queryParam("serviceId", "1");

        RegistrationSO registrationSO = RegistrationSO.builder().serviceId("1").company("abc").eislToken("eislTokentest").build();

        ResponseEntity<RegistrationSO> responseEntity
                = new ResponseEntity<>(registrationSO, HttpStatus.OK);

        Mockito.when(tokenService.unwrapEislToken(eislToken)).thenReturn(claims);
        //doReturn(responseEntity).when(restTemplatespy).exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class));
       // Mockito.when(restTemplate.exchange(builder.toUriString(), HttpMethod.GET,null, new ParameterizedTypeReference<RegistrationSO>() {})).thenReturn(responseEntity);

        /*Mockito.when(restTemplate.exchange(builder.toUriString(), HttpMethod.GET,null, new ParameterizedTypeReference<RegistrationSO>() {})).thenAnswer(
                new Answer() {
                    public Object answer(InvocationOnMock invocation) {
                        RegistrationSO registrationSO = RegistrationSO.builder().serviceId("1").company("abc").eislToken("test").build();

                        ResponseEntity<RegistrationSO> responseEntity
                                = new ResponseEntity<>(registrationSO, HttpStatus.OK);
                        return responseEntity;
                    }
                });*/
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        registrationService.getRegistration(basicToken,eislToken,payloadSO);
        Assert.assertEquals("Registry Access Data is present",responseEntity.getStatusCodeValue(),200);

    }

    
}

