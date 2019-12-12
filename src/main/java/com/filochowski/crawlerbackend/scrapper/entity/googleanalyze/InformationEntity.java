package com.filochowski.crawlerbackend.scrapper.entity.googleanalyze;

import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "information", schema = "google")
public class InformationEntity {

  @Id
  @GenericGenerator(name = "id_information", strategy = "uuid2")
  @GeneratedValue(generator = "uuid")
  private UUID informationId;

  @Column(name = "name", length = 2000)
  private String name;

  @Column(name ="type")
  private String type;

  @Column(name ="type_description")
  private String typeDescription;


  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "id_information")
  private List<InformationMetadataEntity> informationMetadataEntities;

  @Column(name ="salience")
  private Float salience;
}
