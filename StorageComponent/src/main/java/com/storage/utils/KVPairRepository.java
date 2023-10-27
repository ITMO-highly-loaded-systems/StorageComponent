package com.storage.utils;

import com.storage.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KVPairRepository extends JpaRepository<KVPair, Integer> {
    public KVPair findByKey(String key);
}
