package com.officedepot.loganalyzer.dao;

import com.officedepot.loganalyzer.domain.EntryId;
import com.officedepot.loganalyzer.domain.MessageEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by CH on 4/16/14.
 */
public interface MessageEntryDao extends PagingAndSortingRepository<MessageEntry, EntryId> {

    @Query("select e from MessageEntry e where e.id.server = :server")
    Page<MessageEntry> findByServer(@Param("server") String server, Pageable pageable);

}
