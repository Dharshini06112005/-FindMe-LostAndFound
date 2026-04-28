package com.Findme.repository;

import com.Findme.entity.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Long> {
    
    // Find items by type (case-insensitive)
    List<LostItem> findByItemTypeContainingIgnoreCase(String itemType);
    
    // Find items by location (case-insensitive)
    List<LostItem> findByLocationContainingIgnoreCase(String location);
    
    // Find items by description (case-insensitive)
    List<LostItem> findByDescriptionContainingIgnoreCase(String description);

    // Find items by status
    List<LostItem> findByStatus(String status);
    
    // Custom search query for multiple fields
    @Query("SELECT li FROM LostItem li WHERE " +
           "LOWER(li.itemType) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(li.location) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(li.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<LostItem> searchItems(@Param("searchTerm") String searchTerm);
} 