package org.example.data.repository;

import org.example.data.model.Order;
import org.example.data.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {


}