package com.officedepot.loganalyzer.dao;

import com.officedepot.loganalyzer.domain.Server;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by CH on 4/16/14.
 */
public interface ServerDao extends PagingAndSortingRepository<Server, String> {

}
