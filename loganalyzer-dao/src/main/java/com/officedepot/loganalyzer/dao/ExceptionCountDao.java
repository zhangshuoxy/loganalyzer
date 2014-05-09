package com.officedepot.loganalyzer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.officedepot.loganalyzer.domain.ExceptionCount;

/**
 * Created by CH on 4/16/14.
 */
public interface ExceptionCountDao extends PagingAndSortingRepository<ExceptionCount, String> {
	
	@Query("select new ExceptionCount(e.server, e.timestamp, sum(e.count)) from ExceptionCount e "
			+ "where e.timestamp between :startDate and :endDate "
			+ "group by e.server, e.timestamp "
			+ "order by e.server, e.timestamp asc")
	List<ExceptionCount> findByTimestampBetween(
			@Param("startDate") Long startDate, 
			@Param("endDate") Long endDate);
	
	@Query("select e from ExceptionCount e "
			+ "where e.server = :server and e.timestamp between :startDate and :endDate "
			+ "order by e.timestamp asc")
	List<ExceptionCount> findByServerAndTimestampBetweenOrderByTimestampAsc(
			@Param("server") String server, 
			@Param("startDate") Long startDate, 
			@Param("endDate") Long endDate);
	
	@Query("select e from ExceptionCount e "
			+ "where e.server = :server and e.exception = :exception and e.timestamp between :startDate and :endDate "
			+ "order by e.timestamp asc")
	List<ExceptionCount> findByServerAndExceptionAndTimestampBetweenOrderByTimestampAsc(
			@Param("server") String server, 
			@Param("exception") String exception, 
			@Param("startDate") Long startDate, 
			@Param("endDate") Long endDate);
}
