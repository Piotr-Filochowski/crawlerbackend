package com.filochowski.crawlerbackend.scrapper.repository;

import com.filochowski.crawlerbackend.scrapper.entity.RequestEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, String> {

  @Query("SELECT re FROM RequestEntity re WHERE re.username = :username")
  Optional<List<RequestEntity>> findByUsername(@Param("username") String username);

}
