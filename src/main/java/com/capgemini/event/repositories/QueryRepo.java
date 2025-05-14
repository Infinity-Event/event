package com.capgemini.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QueryRepo extends JpaRepository<Query, Long>{

}
