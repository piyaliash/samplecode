package com.ubs.wmap.eisl.registrationService.service;


import com.ubs.wmap.eisl.registrationService.exception.*;
import com.ubs.wmap.eisl.registrationService.model.*;
import com.ubs.wmap.eisl.registrationService.util.EislUtil;
import com.ubs.wmap.eisl.registrationService.util.MessageSourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.ws.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.sun.javafx.fxml.expression.Expression.add;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

@SuppressWarnings("deprecation")
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceDataRestTest {

    @Mock
    RestTemplate restTemplate;

    @Mock
    EislUtil eislUtil;

    @Mock
    MessageSourceUtil messages;

    @InjectMocks
    RegistrationServiceDataRest registrationServiceDataRest;

    @Test
    public void  testgetRegistryResponse() throws DataNotFoundException, EislTokenNotValidException, RegistrationServiceException, BadRequestException {
        RegistrationSO dto = RegistrationSO.builder().build();

        final String url = "http://localhost:5050/eisl/Test";

        HttpHeaders headers = new HttpHeaders();
        headers.add("basicToken", "Test");
        ResponseEntity<RegistrationSO> resp = new ResponseEntity<RegistrationSO>(dto, HttpStatus.OK);
        resp.getBody().setUserId("1");
        Mockito.when(restTemplate.exchange(anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(resp);
        assertEquals("1", registrationServiceDataRest.getRegistryResponse(url, "eislToken","role").getBody().getUserId());
    }

    @Test (expected = RegistrationServiceException.class)
    public void  testExceptionThrownDuringRegistryResponse() throws DataNotFoundException, EislTokenNotValidException, RegistrationServiceException, BadRequestException {
        RegistrationSO dto = RegistrationSO.builder().build();

        final String url = "http://localhost:5050/eisl/Test";

        HttpHeaders headers = new HttpHeaders();
        headers.add("basicToken", "Test");
        Mockito.when(restTemplate.exchange(anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenThrow(RestClientException.class);
        registrationServiceDataRest.getRegistryResponse(url, "eislToken","role");
    }


    @Test
    public void  testgetRegistryResponseforPost() throws DataNotFoundException, EislTokenNotValidException, RegistrationServiceException, BadRequestException {
        RegistrationSO dto = RegistrationSO.builder().build();

        final String url = "http://localhost:5050/eisl/Test";

        HttpHeaders headers = new HttpHeaders();
        headers.add("basicToken", "Test");
        ResponseEntity<RegistrationSO> resp = new ResponseEntity<RegistrationSO>(dto, HttpStatus.OK);
        resp.getBody().setUserId("1");
        Mockito.when(restTemplate.exchange(anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(resp);
        assertEquals("1", registrationServiceDataRest.getRegistryResponseforPost(url, "eislToken","role").getBody().getUserId());
    }

    @Test (expected = EislTokenNotValidException.class)
    public void  testExceptionThrownDuringgetRegistryResponseforPost() throws DataNotFoundException, EislTokenNotValidException, RegistrationServiceException, BadRequestException {
        RegistrationSO dto = RegistrationSO.builder().build();

        final String url = "http://localhost:5050/eisl/Test";

        HttpHeaders headers = new HttpHeaders();
        headers.add("basicToken", "Test");
        ResponseEntity<RegistrationSO> resp = new ResponseEntity<RegistrationSO>(dto, HttpStatus.OK);
        resp.getBody().setUserId("1");
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        Mockito.when(restTemplate.exchange(anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenThrow(ex);
        registrationServiceDataRest.getRegistryResponseforPost(url, "eislToken","role");
    }

    @Test
    public void testpostRegistryResponse() throws DataNotFoundException, EislTokenNotValidException, RegistrationServiceException, DataException, BadRequestException {
        RegistrationSO dto = RegistrationSO.builder().build();

        final String url = "http://localhost:5050/eisl/Test";

        HttpHeaders headers = new HttpHeaders();
        headers.add("basicToken", "Test");
        MultiValueMap<String, RegistrationSO> map = new LinkedMultiValueMap<String, RegistrationSO>();
        map.add("payload", dto);
        HttpEntity<RegistrationSO> requestEntity = new HttpEntity<>(dto);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("eislToken", "Test");
        ResponseEntity<RegistrationSO> resp = new ResponseEntity<RegistrationSO>(dto, HttpStatus.OK);
        resp.getBody().setUserId("1");
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(resp);
        assertEquals("1", registrationServiceDataRest.postRegistryResponse(url, dto, "Test").getUserId());
    }

    @Test(expected = RegistrationServiceException.class)
    public void testRestClientExceptionForPostRegistryResponse() throws DataNotFoundException, EislTokenNotValidException, RegistrationServiceException, DataException, BadRequestException {
        RegistrationSO dto = RegistrationSO.builder().build();

        final String url = "http://localhost:5050/eisl/Test";

        HttpHeaders headers = new HttpHeaders();
        headers.add("basicToken", "Test");
        MultiValueMap<String, RegistrationSO> map = new LinkedMultiValueMap<String, RegistrationSO>();
        map.add("payload", dto);
        HttpEntity<RegistrationSO> requestEntity = new HttpEntity<>(dto);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("eislToken", "Test");
        ResponseEntity<RegistrationSO> resp = new ResponseEntity<RegistrationSO>(dto, HttpStatus.OK);
        resp.getBody().setUserId("1");
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenThrow(RestClientException.class);
        registrationServiceDataRest.postRegistryResponse(url, dto, "Test");
    }

    @Test
    public void testputRegistryResponse() throws DataNotFoundException, EislTokenNotValidException, RegistrationServiceException, DataException, BadRequestException {
        RegistrationSO dto = RegistrationSO.builder().build();

        final String url = "http://localhost:5050/eisl/Test";

        HttpHeaders headers = new HttpHeaders();
        headers.add("basicToken", "Test");
        MultiValueMap<String, RegistrationSO> map = new LinkedMultiValueMap<String, RegistrationSO>();
        map.add("payload", dto);
        HttpEntity<RegistrationSO> requestEntity = new HttpEntity<>(dto);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("eislToken", "Test");
        ResponseEntity<RegistrationSO> resp = new ResponseEntity<RegistrationSO>(dto, HttpStatus.OK);
        resp.getBody().setUserId("1");
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(resp);
        assertEquals("1", registrationServiceDataRest.putRegistryResponse(url, dto, "Test").getUserId());
    }

    @Test(expected = RegistrationServiceException.class)
    public void testRestClientExceptionForPutRegistryResponse() throws DataNotFoundException, EislTokenNotValidException, RegistrationServiceException, DataException, BadRequestException {
        RegistrationSO dto = RegistrationSO.builder().build();

        final String url = "http://localhost:5050/eisl/Test";

        HttpHeaders headers = new HttpHeaders();
        headers.add("basicToken", "Test");
        MultiValueMap<String, RegistrationSO> map = new LinkedMultiValueMap<String, RegistrationSO>();
        map.add("payload", dto);
        HttpEntity<RegistrationSO> requestEntity = new HttpEntity<>(dto);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("eislToken", "Test");
        ResponseEntity<RegistrationSO> resp = new ResponseEntity<RegistrationSO>(dto, HttpStatus.OK);
        resp.getBody().setUserId("1");
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenThrow(RestClientException.class);
        registrationServiceDataRest.putRegistryResponse(url, dto, "Test");
    }

    @Test
    public void testgetEventResponse() throws DataNotFoundException, BadRequestException, RegistrationServiceException, EislTokenNotValidException {
        Map<String, Object> eventSO=new HashMap<>();
        eventSO.put("events","{\"eventId\":3002,\"serviceId\":\"eisl-account-create\",\"serviceUrl\":\"http://a302-1138-2384.ash.pwj.com:8090/api/eisl/mule/v1/dataingestion\",\"eventTopic\":\"eisl-account-create\",\"createdBy\":\"user\",\"createdDate\":\"2019-05-28T16:25:07.834+0000\"}");
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        ResponseEntity<String> resp = new ResponseEntity<String>(String.valueOf(eventSO),HttpStatus.OK);
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(resp);
        assertEquals("Events Data Present",true, registrationServiceDataRest.getEventsResponse(url, eislToken,"testServiceId").getBody().toString().contains("eisl-account-create"));
    }

    @Test (expected = RegistrationServiceException.class)
    public void testgetEventResponsethrowException() throws DataNotFoundException, BadRequestException, RegistrationServiceException, EislTokenNotValidException {
        Map<String, Object> eventSO=new HashMap<>();
        eventSO.put("events","{\"eventId\":3002,\"serviceId\":\"eisl-account-create\",\"serviceUrl\":\"http://a302-1138-2384.ash.pwj.com:8090/api/eisl/mule/v1/dataingestion\",\"eventTopic\":\"eisl-account-create\",\"createdBy\":\"user\",\"createdDate\":\"2019-05-28T16:25:07.834+0000\"}");
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String serviceId="testServiceId";
        ResponseEntity<String> resp = new ResponseEntity<String>(String.valueOf(eventSO),HttpStatus.OK);
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenThrow(RestClientException.class);
        registrationServiceDataRest.getEventsResponse(url, eislToken,"testServiceId");
    };

    @Test
    public void testgetEventsResponseforGet() throws DataNotFoundException, BadRequestException, RegistrationServiceException, EislTokenNotValidException {
        Map<String, Object> eventSO=new HashMap<>();
        eventSO.put("events","{\"eventId\":3002,\"serviceId\":\"eisl-account-create\",\"serviceUrl\":\"http://a302-1138-2384.ash.pwj.com:8090/api/eisl/mule/v1/dataingestion\",\"eventTopic\":\"eisl-account-create\",\"createdBy\":\"user\",\"createdDate\":\"2019-05-28T16:25:07.834+0000\"}");
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        ResponseEntity<String> resp = new ResponseEntity<String>(String.valueOf(eventSO),HttpStatus.OK);
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(resp);
        assertEquals("Events Data Present",true, registrationServiceDataRest.getEventsResponseforGet(url, eislToken,"testServiceId").toString().contains("eisl-account-create"));
    }

    @Test
    public void testgetEventsResponseforGetthrowException() throws DataNotFoundException, BadRequestException, RegistrationServiceException, EislTokenNotValidException {
        Map<String, Object> eventSO=new HashMap<>();
        eventSO.put("events","{\"eventId\":3002,\"serviceId\":\"eisl-account-create\",\"serviceUrl\":\"http://a302-1138-2384.ash.pwj.com:8090/api/eisl/mule/v1/dataingestion\",\"eventTopic\":\"eisl-account-create\",\"createdBy\":\"user\",\"createdDate\":\"2019-05-28T16:25:07.834+0000\"}");
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String serviceId="testServiceId";
        ResponseEntity<String> resp = new ResponseEntity<String>(String.valueOf(eventSO),HttpStatus.OK);
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenThrow(RestClientException.class);
        assertEquals("",registrationServiceDataRest.getEventsResponseforGet(url, eislToken,"testServiceId"));

    }

    @Test
    public void testgetDataResponse() throws DataNotFoundException, EislTokenNotValidException {
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String dataId = "testId";
        ResponseEntity<String> resp = new ResponseEntity<String>(eislToken,HttpStatus.OK);
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(resp);
        assertEquals(resp.getBody(),registrationServiceDataRest.getDataResponse(url, eislToken, dataId));
    }

    @Test
    public void testgetDataResponseException() throws DataNotFoundException, EislTokenNotValidException {
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String dataId = "testId";
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenThrow(RestClientException.class);
        assertEquals("",registrationServiceDataRest.getDataResponse(url, eislToken, dataId));
    }

    @Test (expected = EislTokenNotValidException.class)
    public void testgetDataResponseUnauthorizedException() throws DataNotFoundException, EislTokenNotValidException {
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String dataId = "testId";
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenThrow(ex);
        registrationServiceDataRest.getDataResponse(url, eislToken, dataId);
    }

    @Test
    public void testgetExceptionResponse() throws DataNotFoundException, EislTokenNotValidException {
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String exceptionId = "testId";
        ResponseEntity<String> resp = new ResponseEntity<String>("Test",HttpStatus.OK);
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(resp);
        assertEquals(resp.getBody(),registrationServiceDataRest.getExceptionsResponse(url, eislToken, exceptionId));
    }

    @Test
    public void testgetExceptionResponseException() throws DataNotFoundException, EislTokenNotValidException {
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String exceptionId = "testId";
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenThrow(RestClientException.class);
        assertEquals("",registrationServiceDataRest.getExceptionsResponse(url, eislToken, exceptionId));
    }

    @Test (expected = EislTokenNotValidException.class)
    public void testgetExceptionResponseUnauthorizedException() throws DataNotFoundException, EislTokenNotValidException {
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String exceptionId = "testId";
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenThrow(ex);
       registrationServiceDataRest.getExceptionsResponse(url, eislToken, exceptionId);
    }

    @Test
    public void testBuildRegistration() {
        PayloadSO payload = new PayloadSO();
        payload.setUserName("testUserName");
        payload.setCompany("testCompany");
        payload.setRole("testRole");
        assertEquals("testUserName",registrationServiceDataRest.buildRegistration(payload,"eislToken").getUserName());
    }

    @Test
    public void testdeleteRegistration() throws BadRequestException, DataNotFoundException, EislTokenNotValidException {
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String registrationId = "testRegistrationId";

        ResponseSO responseSO = new ResponseSO();

        ResponseEntity<ResponseSO> resp = new ResponseEntity<ResponseSO>(responseSO,HttpStatus.OK);


        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(resp);
        assertEquals(resp.getStatusCode().toString(),registrationServiceDataRest.getDeleteResponseForRegistryAccessService(url, eislToken,registrationId));
    }

    @Test
    public void testdeleteRegistrationwithException() throws BadRequestException, DataNotFoundException, EislTokenNotValidException {
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String registrationId = "testRegistrationId";

        ResponseSO responseSO = new ResponseSO();

        ResponseEntity<ResponseSO> resp = new ResponseEntity<ResponseSO>(responseSO,HttpStatus.OK);


        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenThrow(RestClientException.class);
        assertEquals("",registrationServiceDataRest.getDeleteResponseForRegistryAccessService(url, eislToken,registrationId));
    }

    @Test
    public void testcheckEventsData() throws BadRequestException, DataNotFoundException, EislTokenNotValidException, RegistrationServiceException {
        Map<String, Object> eventSO=new HashMap<>();
        eventSO.put("events","{\"eventId\":3002,\"serviceId\":\"eisl-account-create\",\"serviceUrl\":\"http://a302-1138-2384.ash.pwj.com:8090/api/eisl/mule/v1/dataingestion\",\"eventTopic\":\"eisl-account-create\",\"createdBy\":\"user\",\"createdDate\":\"2019-05-28T16:25:07.834+0000\"}");
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String serviceId="testServiceId";
        ResponseEntity<String> resp = new ResponseEntity<String>(String.valueOf(eventSO),HttpStatus.OK);
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(resp);
        assertEquals("Events Data Present",true,registrationServiceDataRest.checkEventsData(eislToken,url,serviceId).getBody().contains("eisl-account-create"));
    }

    @Test (expected=DataNotFoundException.class)
    public void testcheckEventsDataWhenDataNotFound() throws BadRequestException, DataNotFoundException, EislTokenNotValidException, RegistrationServiceException {
        Map<String, Object> eventSO=new HashMap<>();
        eventSO.put("events","{\"eventId\":3002,\"serviceId\":\"eisl-account-create\",\"serviceUrl\":\"http://a302-1138-2384.ash.pwj.com:8090/api/eisl/mule/v1/dataingestion\",\"eventTopic\":\"eisl-account-create\",\"createdBy\":\"user\",\"createdDate\":\"2019-05-28T16:25:07.834+0000\"}");
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String serviceId="testServiceId";
        ResponseEntity<String> resp = new ResponseEntity<String>(String.valueOf(eventSO),HttpStatus.NOT_FOUND);
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(resp);
        registrationServiceDataRest.checkEventsData(eislToken,url,serviceId);
    }

    @Test
    public void testcheckForDataAndException() throws BadRequestException, DataNotFoundException, EislTokenNotValidException, RegistrationServiceException {
        final String url = "http://localhost:5050/eisl/Test";
        String eislToken = "testEisl";
        String serviceId = "testServiceId";
        ResponseEntity<String> resp = new ResponseEntity<String>("Test", HttpStatus.OK);
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.any(ParameterizedTypeReference.class))).thenReturn(resp);
        registrationServiceDataRest.checkForDataAndException(serviceId,eislToken,url,url);
    }

    @Test
    public void testbuildResponseMap(){
        RegistrationSO dto = RegistrationSO.builder().build();
        dto.setRegistrationId(1L);
        Map<String, Object> registration=new HashMap<>();
        registration.put("registration",registration);
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> exception =new HashMap<>();
        Map<String, Object> eventSO=new HashMap<>();
        eventSO.put("events","{\"eventId\":3002,\"serviceId\":\"eisl-account-create\",\"serviceUrl\":\"http://a302-1138-2384.ash.pwj.com:8090/api/eisl/mule/v1/dataingestion\",\"eventTopic\":\"eisl-account-create\",\"createdBy\":\"user\",\"createdDate\":\"2019-05-28T16:25:07.834+0000\"}");
        assertEquals("Response present",false,registrationServiceDataRest.buildResponseMap(registration,eventSO,data,exception).isEmpty());
    }
}
