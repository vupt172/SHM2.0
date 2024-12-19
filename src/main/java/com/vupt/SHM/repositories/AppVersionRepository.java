package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
    @Query("SELECT av FROM AppVersion av WHERE av.id = (SELECT MAX(av2.id) FROM AppVersion av2)")
    AppVersion findAppVersionWithMaxId();
}

