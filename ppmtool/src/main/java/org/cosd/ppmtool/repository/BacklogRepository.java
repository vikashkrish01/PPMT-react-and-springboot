package org.cosd.ppmtool.repository;

import org.cosd.ppmtool.entity.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {
	
	Backlog findByProjectIdentifier(String Identifier);
}
