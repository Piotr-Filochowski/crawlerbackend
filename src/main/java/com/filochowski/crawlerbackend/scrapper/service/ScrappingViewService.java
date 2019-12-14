package com.filochowski.crawlerbackend.scrapper.service;

import com.filochowski.crawlerbackend.scrapper.entity.RequestEntity;
import com.filochowski.crawlerbackend.scrapper.exception.RecordNotFoundException;
import com.filochowski.crawlerbackend.scrapper.model.ListOfRequests;
import com.filochowski.crawlerbackend.scrapper.model.viewRequest.ScrappingRequestView;
import com.filochowski.crawlerbackend.scrapper.repository.RequestPositionRepository;
import com.filochowski.crawlerbackend.scrapper.repository.RequestRepository;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ScrappingViewService {

  private final RequestRepository requestRepository;
  private final RequestPositionRepository requestPositionRepository;
  private final GoogleAPISerivce googleAPISerivce;


  public ListOfRequests viewRequests(String username) {
    log.info(username);
    List<RequestEntity> requestEntities = requestRepository.findByUsername(username).orElseThrow(RecordNotFoundException::new);
    return createResponse(requestEntities);
  }

  private ListOfRequests createResponse(List<RequestEntity> requestEntities) {
    List<ScrappingRequestView> scrappingRequests = new LinkedList<>();
    requestEntities.forEach((requestEntity) -> {
      ScrappingRequestView request = new ScrappingRequestView();
      request.setTopic(requestEntity.getTopic());
      request.setUrls(new LinkedList<>());
      request.setId(requestEntity.getRequestId().toString());
      request.setComment(requestEntity.getComment());
      requestEntity.getRequestPositions().forEach((requestPositionEntity -> {
        request.getUrls().add(requestPositionEntity.getUrl());
      }));
      scrappingRequests.add(request);
    });
    ListOfRequests listOfRequests = new ListOfRequests();
    listOfRequests.setScrappingRequestList(scrappingRequests);
    return listOfRequests;
  }

}
