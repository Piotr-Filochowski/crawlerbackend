package com.filochowski.crawlerbackend.scrapper.repository;

import com.filochowski.crawlerbackend.scrapper.entity.RequestPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestPositionRepository extends JpaRepository<RequestPositionEntity, String> {

}
