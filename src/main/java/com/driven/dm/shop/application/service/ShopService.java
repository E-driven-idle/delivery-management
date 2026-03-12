package com.driven.dm.shop.application.service;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.global.exception.ApiErrorCode;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopCategory;
import com.driven.dm.shop.domain.entity.ShopStatus;
import com.driven.dm.shop.infrastructure.GeocodingClient;
import com.driven.dm.shop.infrastructure.GeocodingClient.GeoPoint;
import com.driven.dm.shop.infrastructure.repository.ShopRepository;
import com.driven.dm.shop.presentation.dto.request.ShopCreateRequest;
import com.driven.dm.shop.presentation.dto.request.ShopUpdateRequest;
import com.driven.dm.shop.presentation.dto.response.AdminShopListResponse;
import com.driven.dm.shop.presentation.dto.response.ShopCreateResponse;
import com.driven.dm.shop.presentation.dto.response.ShopListResponse;
import com.driven.dm.shop.presentation.dto.response.ShopResponse;
import com.driven.dm.shop.presentation.dto.response.ShopUpdateResponse;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserRole;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final GeocodingClient geocodingClient;
    private final GeometryFactory geometryFactory =  new GeometryFactory();

    @Transactional
    public ShopCreateResponse createShop(SecurityUser securityUser,
        ShopCreateRequest shopCreateRequest) {

        User user = getUser(securityUser);

        boolean isPrivileged =
            user.getRole().equals(UserRole.OWNER)
            || user.getRole().equals(UserRole.MANAGER)
            || user.getRole().equals(UserRole.MASTER);

        if (!isPrivileged) {
            throw new AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }

        Point point;

        GeoPoint geo = geocodingClient.convert(shopCreateRequest.address());

        point = geometryFactory.createPoint(
            new Coordinate(geo.longitude(), geo.latitude())
        );
        point.setSRID(4326);

        Shop shop = Shop.of(user, shopCreateRequest, point);
        Shop createdShop = shopRepository.save(shop);

        return ShopCreateResponse.from(createdShop);
    }

    @Transactional(readOnly = true)
    public Page<ShopListResponse> shopList(int page, int size, Sort.Direction direction) {

        Pageable pageable = getPageable(page, size, direction);

        Page<Shop> shopList = shopRepository.findByStatusNot(ShopStatus.DELETED, pageable);

        return shopList.map(shop -> ShopListResponse.builder()
            .shopName(shop.getShopName())
            .status(shop.getStatus())
            .description(shop.getDescription())
            .category(shop.getCategory().toString())
            .avgRating(shop.getAvgRating())
            .fullAddress(shop.getAddress() != null ? shop.getAddress() : "")
            .build());

    }

    @Transactional(readOnly = true)
    public ShopResponse selectShop(UUID id) {
        Shop shop = getShop(id);

        if (shop.getStatus().equals(ShopStatus.DELETED)) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        return ShopResponse.builder()
            .shopName(shop.getShopName())
            .description(shop.getDescription())
            .category(shop.getCategory())
            .avgRating(shop.getAvgRating())
            .shopStatus(shop.getStatus())
            .build();
    }

    @Transactional(readOnly = true)
    public Page<ShopListResponse> searchByShopName(String shopName, int page, int size,
        Sort.Direction direction) {

        Pageable pageable = getPageable(page, size, direction);

        Page<Shop> shops = shopRepository.findByShopNameContainingAndStatusNot(shopName,
            ShopStatus.DELETED, pageable);

        return shops.map(shop -> ShopListResponse.builder()
            .shopName(shop.getShopName())
            .status(shop.getStatus())
            .description(shop.getDescription())
            .category(shop.getCategory().toString())
            .avgRating(shop.getAvgRating())
            .fullAddress(shop.getAddress() != null ? shop.getAddress() : "")
            .build());

    }

    @Transactional(readOnly = true)
    public Page<ShopListResponse> searchByCategory(ShopCategory category, int page, int size, Sort.Direction direction) {

        Pageable pageable = getPageable(page, size, direction);

        Page<Shop> shopList = shopRepository.findByCategoryAndStatusNot(category,
            ShopStatus.DELETED, pageable);

        return shopList.map(shop -> ShopListResponse.builder()
            .shopName(shop.getShopName())
            .status(shop.getStatus())
            .description(shop.getDescription())
            .category(shop.getCategory().toString())
            .avgRating(shop.getAvgRating())
            .fullAddress(shop.getAddress() != null ? shop.getAddress(): "")
            .build());
    }

    public Page<ShopResponse> searchShopsByAddress(String address, int radiusKm, int page, int size) {

        GeoPoint geo = geocodingClient.convert(address);

        Point point = geometryFactory.createPoint(
            new Coordinate(geo.longitude(), geo.latitude())
        );

        point.setSRID(4326);

        int radiusMeter = radiusKm * 1000;

        Pageable pageable = PageRequest.of(page, size);

        Page<Shop> shops = shopRepository.findShopsWithinRadius(point, radiusMeter, pageable);

        return shops.map(ShopResponse::from);
    }

    @Transactional
    public ShopUpdateResponse updateShop(UUID id, SecurityUser securityUser,
        ShopUpdateRequest shopUpdateRequest) {
        Shop shop = getShop(id);
        User user = getUser(securityUser);

        if (shop.getStatus().equals(ShopStatus.DELETED)) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        boolean isPrivileged =
            isOwner(user, shop)
            || user.getRole().equals(UserRole.MANAGER)
            || user.getRole().equals(UserRole.MASTER);

        if (!isPrivileged) {
            throw new AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }

        shop.update(shopUpdateRequest.getShopName(), shopUpdateRequest.getDescription(),
            shopUpdateRequest.getStatus(), shopUpdateRequest.getCategory());
        Shop updatedShop = shopRepository.save(shop);

        return ShopUpdateResponse.builder()
            .shopName(updatedShop.getShopName())
            .description(updatedShop.getDescription())
            .shopStatus(updatedShop.getStatus())
            .category(updatedShop.getCategory())
            .build();
    }

    @Transactional
    public void deleteShop(UUID id, SecurityUser securityUser) {

        Shop shop = getShop(id);
        User user = getUser(securityUser);

        boolean privileges = isOwner(user, shop)
            || user.getRole().equals(UserRole.MANAGER)
            || user.getRole().equals(UserRole.MASTER);

        if (!privileges) {
            throw new AppException(ApiErrorCode.FORBIDDEN);
        }

        Shop deleteShop = shop.deleteShop(user.getId());
        shopRepository.save(deleteShop);
    }

    @Transactional
    public Page<AdminShopListResponse> adminShopList(int page, int size, Direction direction) {

        Pageable pageable = getPageable(page, size, direction);

        Page<Shop> shopList = shopRepository.findAll(pageable);

        return shopList.map(shop -> AdminShopListResponse.builder()
            .shopId(shop.getId())
            .status(shop.getStatus())
            .shopName(shop.getShopName())
            .description(shop.getDescription())
            .category(shop.getCategory().toString())
            .avgRating(shop.getAvgRating())
            .fullAddress(shop.getAddress() != null ? shop.getAddress(): "")
            .build());
    }

    private Pageable getPageable(int page, int size, Sort.Direction direction) {
        int safePage = Math.max(0, page);
        int safeSize = (size <= 0) ? 10 : size;
        Sort.Direction safeDir = (direction == null) ? Sort.Direction.DESC : direction;

        return PageRequest.of(safePage, safeSize, Sort.by(safeDir, "createdAt"));
    }

    private User getUser(SecurityUser securityUser) {

        return userRepository.findById(securityUser.getId()).orElseThrow(
            () -> new AppException(UserErrorCode.USER_NOT_FOUND));
    }

    private Shop getShop(UUID shopId) {

        return shopRepository.findById(shopId).orElseThrow(
            () -> new AppException(ShopErrorCode.SHOP_NOT_FOUND)
        );
    }

    private boolean isOwner(User user, Shop shop) {

        if (!user.getRole().equals(UserRole.OWNER)
            || !user.getId().equals(shop.getOwner().getId())) {
            return false;
        } else {
            return true;
        }
    }
}
