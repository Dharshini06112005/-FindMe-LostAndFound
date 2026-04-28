package com.Findme.service;

import com.Findme.entity.LostItem;
import com.Findme.repository.LostItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LostItemService {
    
    @Autowired
    private LostItemRepository lostItemRepository;
    
    // Create a new lost item
    public LostItem createLostItem(LostItem lostItem) {
        return lostItemRepository.save(lostItem);
    }
    
    // Get all lost items
    public List<LostItem> getAllLostItems() {
        return lostItemRepository.findAll();
    }
    
    // Get lost item by ID
    public Optional<LostItem> getLostItemById(Long id) {
        return lostItemRepository.findById(id);
    }
    
    // Update lost item
    public LostItem updateLostItem(Long id, LostItem lostItemDetails) {
        Optional<LostItem> optionalLostItem = lostItemRepository.findById(id);
        if (optionalLostItem.isPresent()) {
            LostItem existingItem = optionalLostItem.get();
            existingItem.setItemType(lostItemDetails.getItemType());
            existingItem.setLocation(lostItemDetails.getLocation());
            existingItem.setDescription(lostItemDetails.getDescription());
            existingItem.setDate(lostItemDetails.getDate());
            existingItem.setStatus(lostItemDetails.getStatus());
            existingItem.setContactEmail(lostItemDetails.getContactEmail());
            if (lostItemDetails.getImageBase64() != null) {
                existingItem.setImageBase64(lostItemDetails.getImageBase64());
            }
            return lostItemRepository.save(existingItem);
        }
        return null;
    }
    
    // Delete lost item
    public boolean deleteLostItem(Long id) {
        if (lostItemRepository.existsById(id)) {
            lostItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Search lost items
    public List<LostItem> searchLostItems(String searchTerm) {
        return lostItemRepository.searchItems(searchTerm);
    }
    
    // Find by item type
    public List<LostItem> findByItemType(String itemType) {
        return lostItemRepository.findByItemTypeContainingIgnoreCase(itemType);
    }
    
    // Find by location
    public List<LostItem> findByLocation(String location) {
        return lostItemRepository.findByLocationContainingIgnoreCase(location);
    }

    // Find by status
    public List<LostItem> findByStatus(String status) {
        return lostItemRepository.findByStatus(status);
    }
} 