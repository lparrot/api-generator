package fr.lauparr.apigenerator.dao;

import fr.lauparr.apigenerator.entities.Profile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProfileRepository extends PagingAndSortingRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {
}