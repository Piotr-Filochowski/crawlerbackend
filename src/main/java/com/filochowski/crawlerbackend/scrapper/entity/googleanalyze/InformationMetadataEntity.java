package com.filochowski.crawlerbackend.scrapper.entity.googleanalyze;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "information_metadata", schema = "google")
public class InformationMetadataEntity {

  @Id
  @GenericGenerator(name = "id_metadata", strategy = "uuid2")
  @GeneratedValue(generator = "uuid")
  private UUID idMetadata;

  @Column(name = "key")
  private String key;

  @Column(name = "value")
  private String value;

}
