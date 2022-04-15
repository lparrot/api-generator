package fr.lauparr.apigenerator.dao;

import fr.lauparr.apigenerator.entities.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
	@Query("select u from User u where u.username = :username")
	User findUserByUsername(@Param("username") String username);
}