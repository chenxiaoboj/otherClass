package bazi.star.dao;

import bazi.star.dto.ExceptionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chenx 2019-02-21 16:36
 */
public interface ExceptionInfoDao extends JpaRepository<ExceptionInfo,Integer> {
}
