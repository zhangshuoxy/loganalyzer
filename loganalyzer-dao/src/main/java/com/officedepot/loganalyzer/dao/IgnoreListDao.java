package com.officedepot.loganalyzer.dao;

import com.officedepot.loganalyzer.domain.IgnoreList;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by CH on 4/16/14.
 */
public interface IgnoreListDao extends PagingAndSortingRepository<IgnoreList, String> {

}
