package com.storage.Entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Data
@Getter
@Setter
@Table(schema = "highload", name = "kvpair")
public class KVPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "key")
    public String key;
    @Column(name = "value")
    private String value;
    public KVPair() { }
    public KVPair(String key_, String value_) throws NullPointerException{
        if (key_ == null) throw new NullPointerException();
        if (value_ == null) throw new NullPointerException();

        key = key_;
        value = value_;
    }


}
