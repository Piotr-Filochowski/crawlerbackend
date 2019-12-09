package com.filochowski.crawlerbackend.scrapper.service;

import com.filochowski.crawlerbackend.scrapper.model.ScrappingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrappingService {

  public void handleScrappingRequest(ScrappingRequest scrappingRequest){
    log.info(scrappingRequest.getTopic() + " " + scrappingRequest.getUrls().size());
  }

}
