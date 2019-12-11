package com.filochowski.crawlerbackend.scrapper.repository;

import com.filochowski.crawlerbackend.scrapper.entity.googleanalyze.ContextEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContextRepository extends JpaRepository<ContextEntity, String > {

}
