package com.filochowski.crawlerbackend.scrapper.api;

import com.filochowski.crawlerbackend.scrapper.model.ScrappingRequest;
import com.filochowski.crawlerbackend.scrapper.service.ScrappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(path = "/scrap")
@RequiredArgsConstructor
public class ScrapperController {

  private final ScrappingService scrappingService;

  @PostMapping
  public void addScrappingRequest(@RequestBody ScrappingRequest scrappingRequest){
    scrappingService.handleScrappingRequest(scrappingRequest);
  }

}
