package com.empexchng.empexchng.repository;

import com.empexchng.empexchng.model.CompanyPolicy;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyPolicyRepository extends JpaRepository<CompanyPolicy, Long> {
  // Versioning
  CompanyPolicy findByEmployer_EmployerIdAndVersionNo(String employerId, Integer versionNo);
  List<CompanyPolicy> findByEmployer_EmployerIdOrderByVersionNoDesc(String employerId);


@Query("select coalesce(max(p.versionNo),0) from CompanyPolicy p where p.employer.employerId = :employerId")
  Integer getMaxVersion(@Param("employerId") String employerId);

  @Modifying
  @Query("delete from CompanyPolicy p where p.employer.employerId = :employerId and p.versionNo < :minVersion")
  int deleteOlderThan(@Param("employerId") String employerId, @Param("minVersion") Integer minVersion);
}

