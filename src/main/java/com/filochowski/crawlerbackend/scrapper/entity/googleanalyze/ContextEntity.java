package com.filochowski.crawlerbackend.scrapper.entity.googleanalyze;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "context", schema = "google")
public class ContextEntity {

  @Id
  @GenericGenerator(name = "id_context", strategy = "uuid2")
  @GeneratedValue(generator = "uuid")
  private UUID contextId;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "id_request_position")
//  private RequestPositionEntity requestPositionEntity;

  @Column(name = "name")
  private String name;

  @Column(name = "confidence")
  private Float confidence;

}
