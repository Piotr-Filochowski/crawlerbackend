package com.filochowski.crawlerbackend.scrapper.service;

import com.filochowski.crawlerbackend.scrapper.entity.RequestEntity;
import com.filochowski.crawlerbackend.scrapper.entity.RequestPositionEntity;
import com.filochowski.crawlerbackend.scrapper.entity.googleanalyze.ContextEntity;
import com.filochowski.crawlerbackend.scrapper.entity.googleanalyze.InformationEntity;
import com.filochowski.crawlerbackend.scrapper.entity.googleanalyze.InformationMetadataEntity;
import com.filochowski.crawlerbackend.scrapper.model.EntityInfoMetadataResponse;
import com.filochowski.crawlerbackend.scrapper.model.EntityInfoResponse;
import com.filochowski.crawlerbackend.scrapper.model.ScrappingRequest;
import com.filochowski.crawlerbackend.scrapper.model.ScrappingResponse;
import com.filochowski.crawlerbackend.scrapper.model.UrlInfoResponse;
import com.filochowski.crawlerbackend.scrapper.repository.RequestPositionRepository;
import com.filochowski.crawlerbackend.scrapper.repository.RequestRepository;
import com.google.cloud.language.v1.AnnotateTextResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.Entity;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@Slf4j
public class ScrappingService {

  private final RequestRepository requestRepository;
  private final RequestPositionRepository requestPositionRepository;
  private final GoogleAPISerivce googleAPISerivce;

  public ScrappingResponse handleScrappingRequest(ScrappingRequest scrappingRequest){
    log.info(scrappingRequest.getTopic() + " " + scrappingRequest.getUrls().size());
    RequestEntity requestEntity = saveRequestToDatabase(scrappingRequest);
    manageRequest(requestEntity);
    return createResponse(requestEntity);
  }

  private RequestEntity saveRequestToDatabase(ScrappingRequest scrappingRequest) {
    String username = getUsername();
    log.info("username: " + username);
    RequestEntity requestEntity = new RequestEntity();
    requestEntity.setRequestPositions(new LinkedList<>());
    requestEntity.setTopic(scrappingRequest.getTopic());
    for (String url : scrappingRequest.getUrls()) {
      if (url.length() == 0 || url == null) {
        continue;
      }
      RequestPositionEntity requestPositionEntity = new RequestPositionEntity();
      requestPositionEntity.setUrl(url);
      requestPositionEntity.setRequestEntity(requestEntity);
      requestEntity.getRequestPositions().add(requestPositionEntity);
    }
    return requestRepository.save(requestEntity);
  }

  private String getUsername() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      return ((UserDetails) principal).getUsername();
    } else {
      return principal.toString();
    }
  }

  private void manageRequest(RequestEntity requestEntity) {
    for (RequestPositionEntity requestPosition : requestEntity.getRequestPositions()) {
      try {
        String pageText = scrapPage(requestPosition.getUrl());
        if (pageText == null) {
          requestPosition.setInformationEntities(new LinkedList<>());
          requestPosition.setContextEntities(new LinkedList<>());
          requestPosition.setSuccess(Boolean.FALSE);
          continue;
        } else {
          requestPosition.setSuccess(Boolean.TRUE);
        }
        AnnotateTextResponse response = googleAPISerivce.annotateText(pageText);
        manageCategories(requestPosition, response);
        manageInformationEntities(requestPosition, response);
      } catch (IOException e) {
        log.info(requestPosition.getUrl() + " IOException with that");
      }
    }
  }

  private void manageCategories(RequestPositionEntity requestPosition, AnnotateTextResponse response) {
    List<ClassificationCategory> categoriesList = response.getCategoriesList();
    for (ClassificationCategory classificationCategory : categoriesList) {
      ContextEntity contextEntity = new ContextEntity();
      contextEntity.setName(classificationCategory.getName());
      contextEntity.setConfidence(classificationCategory.getConfidence());
      if (requestPosition.getContextEntities() == null) {
        requestPosition.setContextEntities(new LinkedList<>());
      }
      requestPosition.getContextEntities().add(contextEntity);
    }
    requestPositionRepository.save(requestPosition);
  }

  private String scrapPage(String url) throws IOException {
    try {
      Document doc = Jsoup.connect(url).get();
      Elements elements = doc.children().select("*");
      StringBuilder stringBuilder = new StringBuilder();
      for (Element el : elements) {
        if (el.hasText() /*&& el.ownText().length() > 1*/) {
          stringBuilder.append(el.ownText() + " ");
        }
      }
      return stringBuilder.toString();
    } catch (Exception e) {
      log.error(url + " page content wasnt occured");
      return null;
    }
  }

  private void manageInformationEntities(RequestPositionEntity requestPosition, AnnotateTextResponse response) {
    List<Entity> entitiesList = response.getEntitiesList();
    if (requestPosition.getInformationEntities() == null) {
      requestPosition.setInformationEntities(new LinkedList<>());
    }
    entitiesList.forEach(entity -> {
      InformationEntity informationEntity = new InformationEntity();
      informationEntity.setName(validateEntityNameLength(entity.getName()));
      informationEntity.setSalience(entity.getSalience());
      informationEntity.setType(entity.getType().name());
      informationEntity.setTypeDescription(entity.getType().getDescriptorForType().getFullName());
      informationEntity.setInformationMetadataEntities(new LinkedList<>());
      entity.getMetadataMap().forEach((key, value) -> {
        InformationMetadataEntity informationMetadataEntity = new InformationMetadataEntity();
        informationMetadataEntity.setKey(key);
        informationMetadataEntity.setValue(value);
        informationEntity.getInformationMetadataEntities().add(informationMetadataEntity);
      });
      requestPosition.getInformationEntities().add(informationEntity);
    });
    requestPositionRepository.save(requestPosition);
  }

  private String validateEntityNameLength(String name) {
    if (name.length() > 2000) {
      return name.substring(0, 1999);
    }
    return name;
  }

  private ScrappingResponse createResponse(RequestEntity requestEntity) {
    ScrappingResponse scrappingResponse = new ScrappingResponse();
    scrappingResponse.setUrlInfos(new LinkedList<>());
    requestEntity.getRequestPositions().forEach(requestPosition -> scrappingResponse.getUrlInfos().add(mapRequestPositionToRespone(requestPosition)));
    return scrappingResponse;
  }

  private UrlInfoResponse mapRequestPositionToRespone(RequestPositionEntity requestPosition){
    UrlInfoResponse urlInfoResponse = new UrlInfoResponse();
    urlInfoResponse.setContext(new LinkedList<>());
    urlInfoResponse.setEntitties(new LinkedList<>());
    urlInfoResponse.setId(requestPosition.getRequestPositionId().toString());
    urlInfoResponse.setUrl(requestPosition.getUrl());
    if (requestPosition.getContextEntities() == null) {
      requestPosition.setContextEntities(new LinkedList<>());
    }
    requestPosition.getContextEntities().forEach(context -> urlInfoResponse.getContext().add(context.getName()));
    requestPosition.getInformationEntities().forEach(informationEntity -> urlInfoResponse.getEntitties().add(mapEntityToResponse(informationEntity)));
    return urlInfoResponse;
  }

  private EntityInfoResponse mapEntityToResponse(InformationEntity informationEntity){
    EntityInfoResponse entityInfoResponse = new EntityInfoResponse();
    entityInfoResponse.setId(informationEntity.getInformationId().toString());
    entityInfoResponse.setName(informationEntity.getName());
    entityInfoResponse.setSalience(informationEntity.getSalience());
    entityInfoResponse.setType(informationEntity.getType());
    entityInfoResponse.setMetadata(new LinkedList<>());
    if (informationEntity.getInformationMetadataEntities() == null) {
      informationEntity.setInformationMetadataEntities(new LinkedList<>());
    }
    informationEntity.getInformationMetadataEntities().forEach(metadataEntity -> entityInfoResponse.getMetadata().add(mapMetadataToResponse(metadataEntity)));
    return entityInfoResponse;
  }

  private EntityInfoMetadataResponse mapMetadataToResponse(InformationMetadataEntity metadataEntity) {
    EntityInfoMetadataResponse entityInfoMetadataResponse = new EntityInfoMetadataResponse();
    entityInfoMetadataResponse.setId(metadataEntity.getIdMetadata().toString());
    entityInfoMetadataResponse.setKey(metadataEntity.getKey());
    entityInfoMetadataResponse.setValue(metadataEntity.getValue());
    return entityInfoMetadataResponse;
  }

}
