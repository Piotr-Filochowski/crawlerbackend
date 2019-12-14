package com.filochowski.crawlerbackend.scrapper.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "request", schema = "request")
public class RequestEntity {

  @Id
  @GenericGenerator(name = "id_request", strategy = "uuid2")
  @GeneratedValue(generator = "uuid")
  private UUID requestId;

  @Column(name = "topic")
  private String topic;

  @OneToMany(mappedBy = "requestEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RequestPositionEntity> requestPositions;

  @Column(name = "username")
  private String username;

  @Column(name = "comment")
  private String comment;

  @PostConstruct
  private void postConstruct() {
    if (requestPositions == null) {
      requestPositions = new LinkedList<>();
    }
  }
}
