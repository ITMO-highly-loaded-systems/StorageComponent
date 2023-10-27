package com.storage.service;

import com.storage.Entities.KVPair;
import com.storage.utils.DBOperations;
import com.storage.utils.KVPairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageComponentService implements DBOperations<KVPair> {
    KVPairRepository repository;

    public StorageComponentService(KVPairRepository repository_) {
        repository = repository_;
    }

    public void set(KVPair pair) {
        repository.save(pair);
    }

    public KVPair get(String key) {
        return repository.findByKey(key);
    }

}
