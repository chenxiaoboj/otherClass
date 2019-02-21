package bazi.star.dao;

import bazi.star.dto.TouristInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chenx 2019-02-21 16:36
 */
public interface TouristInfoDao extends JpaRepository<TouristInfo,Integer> {
    
}
