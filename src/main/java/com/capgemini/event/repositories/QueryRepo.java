package com.capgemini.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.capgemini.event.entities.Query;

public interface QueryRepo extends JpaRepository<Query, Long>{

}
