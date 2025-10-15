package com.driven.dm.cart.application.service;

import com.driven.dm.cart.application.exception.CartErrorCode;
import com.driven.dm.cart.domain.entity.Cart;
import com.driven.dm.cart.domain.entity.CartItem;
import com.driven.dm.cart.infrastructure.repository.CartItemRepository;
import com.driven.dm.cart.infrastructure.repository.CartReadRepository;
import com.driven.dm.cart.infrastructure.repository.CartRepository;
import com.driven.dm.cart.presentation.dto.request.AddItemRequest;
import com.driven.dm.cart.presentation.dto.request.UpdateQtyRequest;
import com.driven.dm.cart.presentation.dto.response.CartItemResponse;
import com.driven.dm.cart.presentation.dto.response.CartResponse;
import com.driven.dm.cart.presentation.dto.response.ShopCartItemDto;
import com.driven.dm.cart.presentation.dto.response.UserCartSummaryDto;
import com.driven.dm.cart.presentation.dto.response.UserCartsResponse;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.menu.infrastructure.repository.MenuRepository;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.repository.ShopRepository;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartReadRepository cartReadRepository;

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;


    @Transactional
    public CartItemResponse addItem(UUID userId, UUID shopId, AddItemRequest req) {
        if (req.getQuantity() <= 0) {
            throw new AppException(CartErrorCode.INVALID_QUANTITY);
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        Shop shop = shopRepository.selectShop(shopId)
            .orElseThrow(() -> new AppException(ShopErrorCode.SHOP_NOT_FOUND));
        if (shop == null || shop.getDeletedAt() != null) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        Menu menu = menuRepository.findById(req.getMenuId())
            .orElseThrow(() -> new AppException(CartErrorCode.INVALID_MENU));

        Cart cart = cartRepository
            .findByUser_IdAndShop_IdAndDeletedAtIsNull(userId, shopId)
            .orElseGet(() -> cartRepository.save(Cart.of(user, shop)));

        Cart.MenuSnapshot snapshot =
            new Cart.MenuSnapshot(menu, menu.getId(), menu.getMenuName(), menu.getMenuPrice());

        CartItem item = cart.addOrIncrease(snapshot, req.getQuantity());

        if (item.getId() == null) {
            item = cartItemRepository.saveAndFlush(item);
        } else {
            cartRepository.saveAndFlush(cart);
        }

        return CartItemResponse.from(item);
    }

    public CartResponse getShopCart(UUID userId, UUID shopId, int page, int size) {

        PageRequest pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));

        Page<ShopCartItemDto> itemPage =
            cartReadRepository.findShopCartItems(userId, shopId, pageable);

        if (itemPage.isEmpty()) {
            throw new AppException(CartErrorCode.CART_NOT_FOUND);
        }

        long cartTotal = cartReadRepository.sumShopCartTotal(userId, shopId);

        Shop shop = shopRepository.selectShop(shopId)
            .orElseThrow(() -> new AppException(ShopErrorCode.SHOP_NOT_FOUND));
        if (shop == null || shop.getDeletedAt() != null) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        return CartResponse.builder()
            .shopName(shop.getShopName())
            .items(itemPage.getContent())
            .cartTotal(cartTotal)
            .page(itemPage.getNumber())
            .size(itemPage.getSize())
            .totalItems(itemPage.getTotalElements())
            .totalPages(itemPage.getTotalPages())
            .build();
    }

    public UserCartsResponse getUserCarts(UUID userId, int page, int size) {
        PageRequest pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));

        Page<UserCartSummaryDto> summaryPage =
            cartReadRepository.findUserCartSummaries(userId, pageable);

        if (summaryPage.isEmpty()) {
            throw new AppException(CartErrorCode.CART_NOT_FOUND);
        }

        long grandTotal = cartReadRepository.sumUserGrandTotal(userId);

        List<UserCartSummaryDto> rows = summaryPage.getContent();

        return UserCartsResponse.builder()
            .userId(userId)
            .carts(rows)
            .grandTotal(grandTotal)
            .page(summaryPage.getNumber())
            .size(summaryPage.getSize())
            .totalShops(summaryPage.getTotalElements())
            .totalPages(summaryPage.getTotalPages())
            .build();
    }


    @Transactional
    public CartItemResponse updateQuantity(UUID userId, UUID shopId, UUID cartItemId,
        UpdateQtyRequest req) {
        if (req.getQuantity() <= 0) {
            throw new AppException(CartErrorCode.INVALID_QUANTITY);
        }

        CartItem item = cartItemRepository
            .findByIdAndCart_User_IdAndDeletedAtIsNull(cartItemId, userId)
            .orElseThrow(() -> new AppException(CartErrorCode.CART_ITEM_NOT_FOUND));

        Cart cart = item.getCart();
        if (!cart.getUser().getId().equals(userId) || !cart.getShop().getId().equals(shopId)) {
            throw new AppException(CartErrorCode.CART_ACCESS_DENIED);
        }

        item.updateQuantity(req.getQuantity());

        return CartItemResponse.from(item);
    }


    @Transactional
    public UUID deleteCartItem(UUID userId, UUID shopId, UUID cartItemId) {
        int result = cartItemRepository.softDeleteOne(userId, shopId, cartItemId);
        if (result == 0) {
            throw new AppException(CartErrorCode.CART_ITEM_NOT_FOUND);
        }

        long left = cartItemRepository.countAliveItemsByUserAndShop(userId, shopId);

        if (left == 0) {
            cartRepository.softDeleteByUserAndShop(userId, shopId);
        }

        return cartItemId;
    }

    @Transactional
    public void deleteShopCart(UUID userId, UUID shopId) {
        Cart cart = cartRepository
            .findByUser_IdAndShop_IdAndDeletedAtIsNull(userId, shopId)
            .orElseThrow(() -> new AppException(CartErrorCode.CART_NOT_FOUND));

        cartItemRepository.softDeleteAllByCartId(userId, cart.getId());
        cartRepository.softDeleteByUserAndShop(userId, shopId);
    }

    @Transactional
    public void deleteAllCarts(UUID userId) {
        cartItemRepository.softDeleteAllByUser(userId);
        cartRepository.softDeleteAllByUser(userId);
    }
}
