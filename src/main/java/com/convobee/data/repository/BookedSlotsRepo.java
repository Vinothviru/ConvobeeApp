package com.convobee.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.convobee.data.entity.BookedSlots;

public interface BookedSlotsRepo extends JpaRepository<BookedSlots, Integer>{

}
