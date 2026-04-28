package com.Findme.controller;

import com.Findme.entity.LostItem;
import com.Findme.service.LostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lost-items")
@CrossOrigin(origins = "*")
public class LostItemController {
    
    @Autowired
    private LostItemService lostItemService;
    
    // POST - Create a new lost item
    @PostMapping
    public ResponseEntity<LostItem> createLostItem(@RequestBody LostItem lostItem) {
        try {
            LostItem createdItem = lostItemService.createLostItem(lostItem);
            return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    // GET - Get all lost items
    @GetMapping
    public ResponseEntity<List<LostItem>> getAllLostItems() {
        List<LostItem> items = lostItemService.getAllLostItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    
    // GET - Get lost item by ID
    @GetMapping("/{id}")
    public ResponseEntity<LostItem> getLostItemById(@PathVariable Long id) {
        Optional<LostItem> item = lostItemService.getLostItemById(id);
        return item.map(lostItem -> new ResponseEntity<>(lostItem, HttpStatus.OK))
                  .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // PUT - Update lost item
    @PutMapping("/{id}")
    public ResponseEntity<LostItem> updateLostItem(@PathVariable Long id, @RequestBody LostItem lostItemDetails) {
        LostItem updatedItem = lostItemService.updateLostItem(id, lostItemDetails);
        if (updatedItem != null) {
            return new ResponseEntity<>(updatedItem, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // DELETE - Delete lost item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLostItem(@PathVariable Long id) {
        boolean deleted = lostItemService.deleteLostItem(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // GET - Search lost items
    @GetMapping("/search")
    public ResponseEntity<List<LostItem>> searchLostItems(@RequestParam String q) {
        List<LostItem> items = lostItemService.searchLostItems(q);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    
    // GET - Find by item type
    @GetMapping("/search/name")
    public ResponseEntity<List<LostItem>> findByItemType(@RequestParam String itemType) {
        List<LostItem> items = lostItemService.findByItemType(itemType);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    
    // GET - Find by location
    @GetMapping("/search/location")
    public ResponseEntity<List<LostItem>> findByLocation(@RequestParam String location) {
        List<LostItem> items = lostItemService.findByLocation(location);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
} 