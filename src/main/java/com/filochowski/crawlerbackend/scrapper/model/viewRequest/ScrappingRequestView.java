package com.filochowski.crawlerbackend.scrapper.model.viewRequest;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScrappingRequestView {

  String id;
  String topic;
  List<String> urls;
  String comment;

}
