package com.filochowski.crawlerbackend.scrapper.repository;

import com.filochowski.crawlerbackend.scrapper.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, String> {

}
