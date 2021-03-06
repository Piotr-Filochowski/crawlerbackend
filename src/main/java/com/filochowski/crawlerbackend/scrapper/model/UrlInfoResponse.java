package com.filochowski.crawlerbackend.scrapper.model;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UrlInfoResponse {

  String id;
  String url;
  List<String> context;
  List<EntityInfoResponse> entitties;

}
