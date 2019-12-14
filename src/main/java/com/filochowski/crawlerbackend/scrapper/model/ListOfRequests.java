package com.filochowski.crawlerbackend.scrapper.model;

import com.filochowski.crawlerbackend.scrapper.model.viewRequest.ScrappingRequestView;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListOfRequests {

  List<ScrappingRequestView> scrappingRequestList;

}
