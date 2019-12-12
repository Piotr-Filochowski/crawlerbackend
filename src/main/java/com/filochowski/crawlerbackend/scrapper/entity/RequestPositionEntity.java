package com.filochowski.crawlerbackend.scrapper.entity;

import com.filochowski.crawlerbackend.scrapper.entity.googleanalyze.ContextEntity;
import com.filochowski.crawlerbackend.scrapper.entity.googleanalyze.InformationEntity;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "request_position", schema = "request")
public class RequestPositionEntity {

  @Id
  @GenericGenerator(name = "id_request_position", strategy = "uuid2")
  @GeneratedValue(generator = "uuid")
  private UUID requestPositionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_request")
  private RequestEntity requestEntity;

  @Column(name ="url")
  private String url;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "id_request_position")
  private List<ContextEntity> contextEntities;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "id_request_position")
  private List<InformationEntity> informationEntities;

  @Column(name = "success")
  private Boolean success;

}
