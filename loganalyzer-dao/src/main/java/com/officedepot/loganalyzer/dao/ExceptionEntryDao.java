package com.officedepot.loganalyzer.dao;

import com.officedepot.loganalyzer.domain.EntryId;
import com.officedepot.loganalyzer.domain.ExceptionEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by CH on 4/16/14.
 */
public interface ExceptionEntryDao extends PagingAndSortingRepository<ExceptionEntry, EntryId> {

    @Query("select e from ExceptionEntry e where e.id.server = :server and e.exception = :exception")
    Page<ExceptionEntry> findByServerAndException(@Param("server") String server, @Param("exception") String exception, Pageable pageable);
    
    @Query("select e from ExceptionEntry e where e.id.server = :server and e.exception = :exception and e.id.timestamp between :startDate and :endDate")
    Page<ExceptionEntry> findByServerAndException(
    		@Param("server") String server, 
    		@Param("exception") String exception, 
    		@Param("startDate") Long startDate, 
    		@Param("endDate") Long endDate, 
    		Pageable pageable);
    
    @Query("select e from ExceptionEntry e where e.id.server = :server and e.exception = :exception and e.location = :location and e.id.timestamp between :startDate and :endDate")
    Page<ExceptionEntry> findByServerAndExceptionAndLocation(
    		@Param("server") String server, 
    		@Param("exception") String exception, 
    		@Param("location") String location, 
    		@Param("startDate") Long startDate, 
    		@Param("endDate") Long endDate, 
    		Pageable pageable);
    
    @Query("select e from ExceptionEntry e where e.id.server = :server and e.exception = :exception and e.id.timestamp between :startDate and :endDate order by e.id.timestamp desc")
    List<ExceptionEntry> findByServerAndException(
    		@Param("server") String server, 
    		@Param("exception") String exception, 
    		@Param("startDate") Long startDate, 
    		@Param("endDate") Long endDate);

    @Query("select e from ExceptionEntry e where e.id.server = :server")
    List<ExceptionEntry> findByServer(@Param("server") String server, Pageable pageable);

    @Query("select count(e) from ExceptionEntry e where e.id.server = :server and e.exception = :exception and e.id.timestamp > :timestamp")
    Long getExceptionCountSince(@Param("server") String server, @Param("exception") String exception, @Param("timestamp") Date timestamp);
}
