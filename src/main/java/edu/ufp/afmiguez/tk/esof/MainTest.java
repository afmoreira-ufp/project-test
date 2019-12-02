package edu.ufp.afmiguez.tk.esof;

import edu.ufp.afmiguez.tk.esof.models.*;
import edu.ufp.afmiguez.tk.esof.models.pt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;

public class MainTest {

    private static void isPT(int port){
        String collegeName="faculdade";
        String degreeName="curso";
        String explainerName="explicador";
        String appointmentName="atendimento";

           Faculdade college=new Faculdade(collegeName);
            Curso degree=new Curso(degreeName);
            Explicador explainer=new Explicador(explainerName);
            Atendimento appointment=new Atendimento(explainer);

        String baseUrl="http://localhost:"+port+"/";
        Logger logger= LoggerFactory.getLogger("main");
        RestTemplate restTemplate = new RestTemplate();

        logger.info("Start test: create new College. Should be successful");


        ResponseEntity<Faculdade> collegeResponseEntity=restTemplate.postForEntity(baseUrl+"/"+collegeName,college,Faculdade.class);
        assertTrue(collegeResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(collegeResponseEntity.getBody());
        assertNotNull(collegeResponseEntity.getBody().getId());

        logger.info("End test: create new College");

        logger.info("Start test: try to create existing College. Should fail");

        try {
            restTemplate.postForEntity(baseUrl + "/"+collegeName, college, Faculdade.class);
        }catch (HttpClientErrorException ignored){

        }
        logger.info("End test: try to create existing College");

        logger.info("Start test: create new Degree. Should be successful");

        ResponseEntity<Curso> degreeResponseEntity=restTemplate.postForEntity(baseUrl+"/"+degreeName+"/"+collegeName,degree,Curso.class);
        assertTrue(degreeResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(degreeResponseEntity.getBody());
        assertNotNull(degreeResponseEntity.getBody().getId());

        logger.info("End test: create new Degree");

        logger.info("Start test: try to create existing College. Should fail");

        try {
            restTemplate.postForEntity(baseUrl+"/"+degreeName+"/"+collegeName,degree,Curso.class);
        }catch (HttpClientErrorException ignored){

        }

        logger.info("End test: try to create existing College");

        logger.info("Start test: create new Explainer. Should be successful");

        ResponseEntity<Explicador> explainerResponseEntity=restTemplate.postForEntity(baseUrl+"/"+explainerName,explainer,Explicador.class);
        Explicador resultExplainer=explainerResponseEntity.getBody();
        assertNotNull(resultExplainer);
        assertNotNull(resultExplainer.getId());

        logger.info("End test: create new Explainer");

        logger.info("Start test: try to create existing Explainer. Should fail");


        try {
            restTemplate.postForEntity(baseUrl+"/"+explainerName,explainer,Explicador.class);
        }catch (HttpClientErrorException ignored){

        }

        logger.info("End test: try to create existing Explainer");

        logger.info("Start test: Associate explainer to a degree. Should be successful");

        HttpEntity<Explicador> explainerHttpEntity=new HttpEntity<>(explainer);
        explainerResponseEntity=restTemplate.exchange(baseUrl+"/"+explainerName+"/"+degreeName, HttpMethod.PUT,explainerHttpEntity,Explicador.class);
        assertTrue(explainerResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(explainerResponseEntity.getBody());
        assertNotNull(explainerResponseEntity.getBody().getCurso());

        explainerResponseEntity=restTemplate.getForEntity(baseUrl+"/"+explainerName+"/"+explainerName,Explicador.class);
        assertTrue(explainerResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(explainerResponseEntity.getBody());
        assertNotNull(explainerResponseEntity.getBody().getCurso());

        logger.info("End test: Associate explainer to a degree. Should be successful");

        logger.info("Start test: Associate a non existing explainer to a existing degree. Should fail");


        try {
            restTemplate.exchange(baseUrl+"/"+explainerName+"/"+degreeName,HttpMethod.PUT,new HttpEntity<>(new Explicador("some explainer")),Explicador.class);
        }catch (HttpClientErrorException ignored){

        }

        logger.info("End test: Associate a non existing explainer to a existing degree");

        logger.info("Start test: Associate an existing explainer to a non existing degree. Should fail");

        try {
            restTemplate.exchange(baseUrl+"/"+explainerName+"/someDegree",HttpMethod.PUT,explainerHttpEntity,Explicador.class);
        }catch (HttpClientErrorException ignored){

        }

        logger.info("End test: Associate an existing explainer to a non existing degree");


        logger.info("Start test: Get all explainers. Should be successful");

        ResponseEntity<List> responseEntity=restTemplate.getForEntity(baseUrl+"/"+explainerName, List.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody().isEmpty());
        assertEquals(1,responseEntity.getBody().size());

        logger.info("End test: Get all explainers");


        logger.info("Start test: Add some availability to explainer. Should be successful");

        Disponibilidade schedule1=new Disponibilidade();
        schedule1.setDiaDaSemana(LocalDate.now().getDayOfWeek());
        schedule1.setInicio(LocalTime.of(10,0));
        schedule1.setFim(LocalTime.of(12,0));

        explainer.addSchedule(schedule1);

        explainerResponseEntity=restTemplate.exchange(baseUrl+"/"+explainerName,HttpMethod.PUT,new HttpEntity<>(explainer),Explicador.class);
        assertTrue(explainerResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(explainerResponseEntity.getBody());
        assertNotNull(explainerResponseEntity.getBody().getDisponibilidades());
        assertEquals(1,explainerResponseEntity.getBody().getDisponibilidades().size());

        logger.info("End test: Add some availability to explainer");

        logger.info("Start test: Create a new appointment. Should be successful");

        appointment.setInicio(LocalDateTime.of(LocalDate.now(),LocalTime.of(10,0)));
        appointment.setFim(LocalDateTime.of(LocalDate.now(),LocalTime.of(10,0)));


        ResponseEntity<Atendimento> appointmentResponseEntity=restTemplate.postForEntity(baseUrl+"/"+appointmentName,appointment,Atendimento.class);
        assertTrue(appointmentResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(appointmentResponseEntity.getBody());

        logger.info("End test: Create a new appointment");

        logger.info("Start test: Create invalid appointments. Should fail");

        Atendimento invalidAppointment=new Atendimento();
        invalidAppointment.setExplicador(explainer);
        invalidAppointment.setInicio(LocalDateTime.of(LocalDate.now(),LocalTime.of(8,0)));
        invalidAppointment.setFim(LocalDateTime.of(LocalDate.now(),LocalTime.of(9,0)));

        try {
            restTemplate.postForEntity(baseUrl+"/"+appointmentName,invalidAppointment,Atendimento.class);
        }catch (HttpClientErrorException ignored){

        }

        invalidAppointment.setExplicador(explainer);
        invalidAppointment.setInicio(LocalDateTime.of(LocalDate.now(),LocalTime.of(9,0)));
        invalidAppointment.setFim(LocalDateTime.of(LocalDate.now(),LocalTime.of(10,0)));

        try {
            restTemplate.postForEntity(baseUrl+"/"+appointmentName,invalidAppointment,Atendimento.class);
        }catch (HttpClientErrorException ignored){

        }

        invalidAppointment.setExplicador(explainer);
        invalidAppointment.setInicio(LocalDateTime.of(LocalDate.now(),LocalTime.of(11,0)));
        invalidAppointment.setFim(LocalDateTime.of(LocalDate.now(),LocalTime.of(13,0)));

        try {
            restTemplate.postForEntity(baseUrl+"/"+appointmentName,invalidAppointment,Atendimento.class);
        }catch (HttpClientErrorException ignored){

        }

        invalidAppointment.setExplicador(explainer);
        invalidAppointment.setInicio(LocalDateTime.of(LocalDate.now(),LocalTime.of(12,0)));
        invalidAppointment.setFim(LocalDateTime.of(LocalDate.now(),LocalTime.of(13,0)));

        try {
            restTemplate.postForEntity(baseUrl+"/"+appointmentName,invalidAppointment,Atendimento.class);
        }catch (HttpClientErrorException ignored){

        }

        logger.info("End test: Create invalid appointments");
        logger.info("All tests OK");

    }

    private static void isEN(int port){

        String collegeName="college";
        String degreeName="degree";
        String explainerName="explainer";
        String appointmentName="appointment";

        College college=new College(collegeName);
        Degree degree=new Degree(degreeName);
        Explainer explainer=new Explainer(explainerName);
        Appointment appointment=new Appointment(explainer);

        String baseUrl="http://localhost:"+port+"/";
        Logger logger= LoggerFactory.getLogger("main");
        RestTemplate restTemplate = new RestTemplate();

        logger.info("Start test: create new College. Should be successful");


        ResponseEntity<College> collegeResponseEntity=restTemplate.postForEntity(baseUrl+"/"+collegeName,college,College.class);
        assertTrue(collegeResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(collegeResponseEntity.getBody());
        assertNotNull(collegeResponseEntity.getBody().getId());

        logger.info("End test: create new College");

        logger.info("Start test: try to create existing College. Should fail");

        try {
            restTemplate.postForEntity(baseUrl + "/"+collegeName, college, College.class);
        }catch (HttpClientErrorException ignored){

        }
        logger.info("End test: try to create existing College");

        logger.info("Start test: create new Degree. Should be successful");

        ResponseEntity<Degree> degreeResponseEntity=restTemplate.postForEntity(baseUrl+"/"+degreeName+"/"+collegeName,degree,Degree.class);
        assertTrue(degreeResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(degreeResponseEntity.getBody());
        assertNotNull(degreeResponseEntity.getBody().getId());

        logger.info("End test: create new Degree");

        logger.info("Start test: try to create existing College. Should fail");

        try {
            restTemplate.postForEntity(baseUrl+"/"+degreeName+"/"+collegeName,degree,Degree.class);
        }catch (HttpClientErrorException ignored){

        }

        logger.info("End test: try to create existing College");

        logger.info("Start test: create new Explainer. Should be successful");

        ResponseEntity<Explainer> explainerResponseEntity=restTemplate.postForEntity(baseUrl+"/"+explainerName,explainer,Explainer.class);
        Explainer resultExplainer=explainerResponseEntity.getBody();
        assertNotNull(resultExplainer);
        assertNotNull(resultExplainer.getId());

        logger.info("End test: create new Explainer");

        logger.info("Start test: try to create existing Explainer. Should fail");


        try {
            restTemplate.postForEntity(baseUrl+"/"+explainerName,explainer,Explainer.class);
        }catch (HttpClientErrorException ignored){

        }

        logger.info("End test: try to create existing Explainer");

        logger.info("Start test: Associate explainer to a degree. Should be successful");

        HttpEntity<Explainer> explainerHttpEntity=new HttpEntity<>(explainer);
        explainerResponseEntity=restTemplate.exchange(baseUrl+"/"+explainerName+"/"+degreeName, HttpMethod.PUT,explainerHttpEntity,Explainer.class);
        assertTrue(explainerResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(explainerResponseEntity.getBody());
        assertNotNull(explainerResponseEntity.getBody().getDegree());

        explainerResponseEntity=restTemplate.getForEntity(baseUrl+"/"+explainerName+"/"+explainerName,Explainer.class);
        assertTrue(explainerResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(explainerResponseEntity.getBody());
        assertNotNull(explainerResponseEntity.getBody().getDegree());

        logger.info("End test: Associate explainer to a degree. Should be successful");

        logger.info("Start test: Associate a non existing explainer to a existing degree. Should fail");


        try {
            restTemplate.exchange(baseUrl+"/"+explainerName+"/"+degreeName,HttpMethod.PUT,new HttpEntity<>(new Explainer("some explainer")),Explainer.class);
        }catch (HttpClientErrorException ignored){

        }

        logger.info("End test: Associate a non existing explainer to a existing degree");

        logger.info("Start test: Associate an existing explainer to a non existing degree. Should fail");

        try {
            restTemplate.exchange(baseUrl+"/"+explainerName+"/someDegree",HttpMethod.PUT,explainerHttpEntity,Explainer.class);
        }catch (HttpClientErrorException ignored){

        }

        logger.info("End test: Associate an existing explainer to a non existing degree");


        logger.info("Start test: Get all explainers. Should be successful");

        ResponseEntity<List> responseEntity=restTemplate.getForEntity(baseUrl+"/"+explainerName, List.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody().isEmpty());
        assertEquals(1,responseEntity.getBody().size());

        logger.info("End test: Get all explainers");


        logger.info("Start test: Add some availability to explainer. Should be successful");

        Schedule schedule1=new Schedule();
        schedule1.setDayOfWeek(LocalDate.now().getDayOfWeek());
        schedule1.setStart(LocalTime.of(10,0));
        schedule1.setEnd(LocalTime.of(12,0));

        explainer.addSchedule(schedule1);

        explainerResponseEntity=restTemplate.exchange(baseUrl+"/"+explainerName,HttpMethod.PUT,new HttpEntity<>(explainer),Explainer.class);
        assertTrue(explainerResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(explainerResponseEntity.getBody());
        assertNotNull(explainerResponseEntity.getBody().getSchedules());
        assertEquals(1,explainerResponseEntity.getBody().getSchedules().size());

        logger.info("End test: Add some availability to explainer");

        logger.info("Start test: Create a new appointment. Should be successful");

        appointment.setStartTime(LocalDateTime.of(LocalDate.now(),LocalTime.of(10,0)));
        appointment.setExpectedEndTime(LocalDateTime.of(LocalDate.now(),LocalTime.of(10,0)));


        ResponseEntity<Appointment> appointmentResponseEntity=restTemplate.postForEntity(baseUrl+"/"+appointmentName,appointment,Appointment.class);
        assertTrue(appointmentResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(appointmentResponseEntity.getBody());

        logger.info("End test: Create a new appointment");

        logger.info("Start test: Create invalid appointments. Should fail");

        Appointment invalidAppointment=new Appointment();
        invalidAppointment.setExplainer(explainer);
        invalidAppointment.setStartTime(LocalDateTime.of(LocalDate.now(),LocalTime.of(8,0)));
        invalidAppointment.setExpectedEndTime(LocalDateTime.of(LocalDate.now(),LocalTime.of(9,0)));

        try {
            restTemplate.postForEntity(baseUrl+"/"+appointmentName,invalidAppointment,Appointment.class);
        }catch (HttpClientErrorException ignored){

        }

        invalidAppointment.setExplainer(explainer);
        invalidAppointment.setStartTime(LocalDateTime.of(LocalDate.now(),LocalTime.of(9,0)));
        invalidAppointment.setExpectedEndTime(LocalDateTime.of(LocalDate.now(),LocalTime.of(10,0)));

        try {
            restTemplate.postForEntity(baseUrl+"/"+appointmentName,invalidAppointment,Appointment.class);
        }catch (HttpClientErrorException ignored){

        }

        invalidAppointment.setExplainer(explainer);
        invalidAppointment.setStartTime(LocalDateTime.of(LocalDate.now(),LocalTime.of(11,0)));
        invalidAppointment.setExpectedEndTime(LocalDateTime.of(LocalDate.now(),LocalTime.of(13,0)));

        try {
            restTemplate.postForEntity(baseUrl+"/"+appointmentName,invalidAppointment,Appointment.class);
        }catch (HttpClientErrorException ignored){

        }

        invalidAppointment.setExplainer(explainer);
        invalidAppointment.setStartTime(LocalDateTime.of(LocalDate.now(),LocalTime.of(12,0)));
        invalidAppointment.setExpectedEndTime(LocalDateTime.of(LocalDate.now(),LocalTime.of(13,0)));

        try {
            restTemplate.postForEntity(baseUrl+"/"+appointmentName,invalidAppointment,Appointment.class);
        }catch (HttpClientErrorException ignored){

        }

        logger.info("End test: Create invalid appointments");
        logger.info("All tests OK");


    }

    public static void main(String[] args) {

        int port=8080;
        if(args.length==0){
            System.out.println("You should choose between PT or EN version");
            return;
        }

        if(args.length>1){
            try{
                port=Integer.parseInt(args[1]);
            }catch (NumberFormatException nfe){
                nfe.printStackTrace();
            }
        }

        boolean isPT=args[0].equalsIgnoreCase("pt");
        if(isPT){
            isPT(port);
        }else{
            isEN(port);
        }



    }
}
