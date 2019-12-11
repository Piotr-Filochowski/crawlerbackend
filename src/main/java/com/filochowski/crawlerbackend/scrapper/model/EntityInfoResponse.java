package com.filochowski.crawlerbackend.scrapper.model;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EntityInfoResponse {

  String id;
  String name;
  String type;
  Float salience;
  List<EntityInfoMetadataResponse> metadata;

}
