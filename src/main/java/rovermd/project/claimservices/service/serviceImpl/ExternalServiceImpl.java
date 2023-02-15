package rovermd.project.claimservices.service.serviceImpl;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import rovermd.project.claimservices.dto.*;
import rovermd.project.claimservices.exception.ResourceNotFoundException;
import rovermd.project.claimservices.service.ExternalService;


@Service
public class ExternalServiceImpl implements ExternalService {

//    Long tenantId=TenantContext.getCurrentTenant();

    WebClient webClient = WebClient.create("http://192.168.210.9:8080/api/");
    WebClient webClient2 = WebClient.create("http://192.168.210.9:7778/scrubber/");
    WebClient webClient3 = WebClient.create("http://192.168.210.9:7777/api/");
    WebClient webClient4 = WebClient.create("http://192.168.210.9:7788/api/");



    @Override
    public InsuranceDTO getInsuranceDetailsById(String insId) {

        InsuranceDTO ins = webClient.get()
                .uri("professionalpayer/find/" + insId)
                .header("X-TenantID", "6")//String.valueOf(TenantContext.getCurrentTenant()))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,response-> Mono.error(new ResourceNotFoundException("Insurance","InsuranceID", insId == null ? null : Long.valueOf(insId))))
                .bodyToMono(InsuranceDTO.class).block();

        System.out.println(ins.getPayerName());

        return ins;
    }

    @Override
    public DoctorDTO getDoctorDetailsById(long docId) {
        return webClient.get()
                .uri("doctor/find/" + docId)
                .header("X-TenantID", "6")//String.valueOf(TenantContext.getCurrentTenant()))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,response-> Mono.error(new ResourceNotFoundException("Doctor","DoctorID", docId)))
                .bodyToMono(DoctorDTO.class).block();
    }

    public PatientDto getPatientDetailsById(PatientReqDto patreqDto) {
        return webClient2.post()
                .header("X-TenantID", "6")//String.valueOf(TenantContext.getCurrentTenant()))
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(patreqDto))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,response-> Mono.error(new ResourceNotFoundException("Patient","PatientRegID", patreqDto.getPatientRegId())))
                .bodyToMono(PatientDto.class).block();
    }

    @Override
    public ClientDTO getClientDetailsById(long id) {
        return webClient3.get()
                .uri("facility/info/" + id)
                .header("X-TenantID", "6")//String.valueOf(TenantContext.getCurrentTenant()))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,response-> Mono.error(new ResourceNotFoundException("Client","ClientID", id)))
                .bodyToMono(ClientDTO.class).block();
    }

    @Override
    public CompanyDTO getCompanyDetailsById(long id) {
        return webClient4.get()
                .uri("company/credential/find/" + id)
                .header("X-TenantID", "6")//String.valueOf(TenantContext.getCurrentTenant()))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,response-> Mono.error(new ResourceNotFoundException("Company Credentials","Company ID", id)))
                .bodyToMono(CompanyDTO.class).block();
    }


}
