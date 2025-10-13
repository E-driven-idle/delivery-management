package com.driven.dm.cart.application.service;

import com.driven.dm.cart.application.exception.CartErrorCode;
import com.driven.dm.cart.domain.entity.Cart;
import com.driven.dm.cart.domain.entity.CartItem;
import com.driven.dm.cart.infrastructure.repository.CartItemRepository;
import com.driven.dm.cart.infrastructure.repository.CartReadRepository;
import com.driven.dm.cart.infrastructure.repository.CartRepository;
import com.driven.dm.cart.presentation.dto.request.AddItemRequest;
import com.driven.dm.cart.presentation.dto.response.CartItemResponse;
import com.driven.dm.cart.presentation.dto.response.CartResponse;
import com.driven.dm.cart.presentation.dto.response.ShopCartItemDto;
import com.driven.dm.cart.presentation.dto.response.UserCartSummaryDto;
import com.driven.dm.cart.presentation.dto.response.UserCartsResponse;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.menu.domain.repository.MenuRepository;
import com.driven.dm.menu.infrastructure.repository.MenuJpaRepository;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.repository.ShopRepository;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.SUPPORTS)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;

    private final CartReadRepository cartReadRepository;

    @Transactional
    public CartItemResponse addItem(UUID userId, UUID shopId, AddItemRequest req) {
        if (req.getQuantity() <= 0) {
            throw new AppException(CartErrorCode.INVALID_QUANTITY);
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        Shop shop = shopRepository.selectShop(shopId);
        if (shop == null) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        Menu menu = menuRepository.selectMenu(req.getMenuId())
            .orElseThrow(() -> new AppException(CartErrorCode.INVALID_MENU));

        Cart cart = cartRepository.findByUser_IdAndShop_Id(userId, shopId)
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
        PageRequest pageable = PageRequest.of(page, size);

        Cart cart = cartRepository.findByUser_IdAndShop_Id(userId, shopId)
            .orElseThrow(() -> new AppException(CartErrorCode.CART_NOT_FOUND));

        if (!cart.getUser().getId().equals(userId)) {
            throw new AppException(CartErrorCode.CART_ACCESS_DENIED);
        }

        Page<ShopCartItemDto> itemPage =
            cartReadRepository.findShopCartItems(userId, shopId, pageable);

        long cartTotal = cartReadRepository.sumShopCartTotal(userId, shopId);

        return CartResponse.builder()
            .shopName(cart.getShop().getShopName())
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
}
