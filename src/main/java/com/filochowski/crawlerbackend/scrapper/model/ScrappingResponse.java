package com.filochowski.crawlerbackend.scrapper.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScrappingResponse {

  private List<UrlInfoResponse> urlInfos;

}
