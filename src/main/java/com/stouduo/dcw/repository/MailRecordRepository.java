package com.stouduo.dcw.repository;

import com.stouduo.dcw.domain.MailRecord;
import org.springframework.data.repository.CrudRepository;

public interface MailRecordRepository extends CrudRepository<MailRecord,String> {
    MailRecord findByToken(String token);
}
