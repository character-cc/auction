package com.example.auction;

import com.example.auction.entity.Bid;
import com.example.auction.entity.Item;
import com.example.auction.entity.User;
import com.example.auction.entity.UserSecurity;
import com.example.auction.repository.BidRepository;
import com.example.auction.repository.ItemRepository;
import com.example.auction.service.BidService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BidServiceTest {

    @InjectMocks
    private BidService bidService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BidRepository bidRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private UserSecurity userSecurity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup security context
        userSecurity = new UserSecurity(new User());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(userSecurity);
    }

    @Test
    void placeBid_ShouldPlaceBidSuccessfully() {
        // Arrange
        Long itemId = 1L;
        Double bidAmount = 150.0;

        Item item = new Item();
        item.setId(itemId);
        when(itemRepository.findByIdWithLock(itemId)).thenReturn(Optional.of(item));
        when(bidRepository.findHighestBidByItemId(itemId)).thenReturn(100.0);

        // Act
        Map<String, Object> response = bidService.placeBid(itemId, bidAmount);

        // Assert
        assertTrue((Boolean) response.get("success"));
        assertEquals(bidAmount, response.get("newHighestBid"));
        verify(bidRepository, times(1)).save(any(Bid.class));
    }

    @Test
    void placeBid_ShouldFailWhenBidIsLowerThanHighest() {
        // Arrange
        Long itemId = 1L;
        Double bidAmount = 80.0;

        Item item = new Item();
        item.setId(itemId);
        when(itemRepository.findByIdWithLock(itemId)).thenReturn(Optional.of(item));
        when(bidRepository.findHighestBidByItemId(itemId)).thenReturn(100.0);

        // Act
        Map<String, Object> response = bidService.placeBid(itemId, bidAmount);

        // Assert
        assertFalse((Boolean) response.get("success"));
        assertEquals(100.0, response.get("newHighestBid"));
        verify(bidRepository, never()).save(any(Bid.class)); // Không lưu bid mới nếu thất bại
    }

    @Test
    void placeBid_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long itemId = 1L;
        Double bidAmount = 150.0;

        when(itemRepository.findByIdWithLock(itemId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            bidService.placeBid(itemId, bidAmount);
        });

        assertEquals("Item not found with id: " + itemId, thrown.getMessage());
    }

    @Test
    void placeBid_ShouldHandleOptimisticLockException() {
        // Arrange
        Long itemId = 1L;
        Double bidAmount = 150.0;

        Item item = new Item();
        item.setId(itemId);
        when(itemRepository.findByIdWithLock(itemId)).thenReturn(Optional.of(item));

        // Giả lập xung đột phiên bản
        doThrow(new ObjectOptimisticLockingFailureException(Item.class, itemId))
                .when(bidRepository).save(any(Bid.class));

        // Act
        Map<String, Object> response = bidService.placeBid(itemId, bidAmount);

        // Assert
        assertFalse((Boolean) response.get("success"));
        assertEquals("Failed to place bid. Item has been updated by another transaction. Please try again.",
                response.get("message"));
    }
}
