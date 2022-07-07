package com.example.YUmarket.di

import com.example.YUmarket.screen.myLocation.MyLocationViewModel
import com.example.YUmarket.data.entity.home.TownMarketEntity
import com.example.YUmarket.data.entity.location.MapSearchInfoEntity
import com.example.YUmarket.data.repository.basket.BasketRepository
import com.example.YUmarket.data.repository.basket.DefaultBasketRepository
import com.example.YUmarket.data.repository.chat.ChatRepository
import com.example.YUmarket.data.repository.chat.DefaultChatRepository
import com.example.YUmarket.data.repository.home.DefaultHomeReviewRepository
import com.example.YUmarket.data.repository.home.HomeReviewRepository
import com.example.YUmarket.data.repository.like.DefaultLikeItemRepository
import com.example.YUmarket.data.repository.like.DefaultLikeMarketRepository
import com.example.YUmarket.data.repository.like.LikeListRepository
import com.example.YUmarket.data.repository.map.DefaultMapApiRepository
import com.example.YUmarket.data.repository.map.DefaultMapRepository
import com.example.YUmarket.data.repository.map.MapApiRepository
import com.example.YUmarket.data.repository.map.MapRepository
import com.example.YUmarket.model.like.LikeItemModel
import com.example.YUmarket.model.like.LikeMarketModel
import com.example.YUmarket.data.repository.myinfo.CSRepository
import com.example.YUmarket.data.repository.myinfo.DefaultCSRepository
import com.example.YUmarket.data.repository.restaurant.DefaultHomeRepository
import com.example.YUmarket.data.repository.restaurant.HomeRepository
import com.example.YUmarket.data.repository.shop.DefaultShopApiRepository
import com.example.YUmarket.data.repository.shop.ShopApiRepository
import com.example.YUmarket.data.repository.store.DefaultStoreRepository
import com.example.YUmarket.data.repository.store.StoreRepository
import com.example.YUmarket.data.repository.suggest.DefaultSuggestRepository
import com.example.YUmarket.data.repository.suggest.SuggestRepository
import com.example.YUmarket.model.homelist.category.HomeListCategory
import com.example.YUmarket.model.like.LikeCategory
import com.example.YUmarket.screen.MainViewModel
import com.example.YUmarket.screen.chat.ChatViewModel
import com.example.YUmarket.screen.home.homedetail.HomeMarketDetailViewModel
import com.example.YUmarket.screen.home.homedetail.menu.HomeMarketMenuViewModel
import com.example.YUmarket.screen.home.homedetail.review.HomeMarketReviewViewModel
import com.example.YUmarket.screen.home.homelist.HomeListViewModel
import com.example.YUmarket.screen.home.homemain.HomeMainViewModel
import com.example.YUmarket.screen.map.MapLocationSetting.MapLocationSettingViewModel
import com.example.YUmarket.screen.myinfo.like.LikeListViewModel
import com.example.YUmarket.screen.map.MapViewModel
import com.example.YUmarket.screen.myinfo.customerservice.list.CSCategory
import com.example.YUmarket.screen.myinfo.customerservice.list.CSListViewModel
import com.example.YUmarket.screen.store.StoreViewModel
import com.example.YUmarket.util.provider.DefaultResourcesProvider
import com.example.YUmarket.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    factory { (homeListCategory: HomeListCategory) ->
        HomeListViewModel(homeListCategory, get())
    }

    /*  CSViewModel 추가  의존성 주입   factory추가
    *   csCategory factory                     */

    factory { (csCategory: CSCategory) ->
        CSListViewModel(csCategory,get())
    }

    viewModel { (townMarketEntity: TownMarketEntity) ->
        HomeMarketDetailViewModel(townMarketEntity)
    }

    viewModel { HomeMarketMenuViewModel(get()) }

    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) ->
        MyLocationViewModel(mapSearchInfoEntity, get())
    }

    viewModel(named(LikeCategory.MARKET)) {
        LikeListViewModel<LikeMarketModel>(
            get(
                qualifier = named("likeMarketRepository")
            )
        )
    }

    viewModel(named(LikeCategory.ITEM)) {
        LikeListViewModel<LikeItemModel>(
            get(
                qualifier = named("likeItemRepository")
            )
        )
    }

    viewModel { HomeMainViewModel(get(),get())}
    viewModel { MainViewModel(get(),get()) }
    viewModel { MapViewModel(get()) }
    viewModel { ChatViewModel(get())}
    viewModel { StoreViewModel(get()) }
    viewModel { MapLocationSettingViewModel(get()) }
    viewModel { HomeMarketReviewViewModel(get()) }

    single<HomeReviewRepository> { DefaultHomeReviewRepository(get()) }
    single<ChatRepository>{ DefaultChatRepository(get()) }
    single<StoreRepository>{ DefaultStoreRepository() }
    single<HomeRepository> { DefaultHomeRepository() }
    single<SuggestRepository> {DefaultSuggestRepository()}

    // mockList 의존성 주입
    single<CSRepository>{ DefaultCSRepository(get()) }
    single<ResourcesProvider> { DefaultResourcesProvider(androidContext()) }

    single { buildOkHttpClient() }
    single { provideGsonConverterFactory() }

    single(named("map")) { provideMapRetrofit(get(), get()) }
    single(named("shop")) { provideShopRetrofit(get(), get()) }
    single { provideMapApiService(get(qualifier = named("map"))) }
    single { provideShopApiService(get(qualifier = named("shop"))) }

    single<MapApiRepository> { DefaultMapApiRepository(get(), get()) }
    single<MapRepository> { DefaultMapRepository() }
    single<ShopApiRepository> { DefaultShopApiRepository(get(), get())}

    single { provideDB(androidContext()) }
    single { provideBasketDao(get()) }
    single { provideLikeItemDao(get()) }
    single { provideLikeMarketDao(get()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }
    single { provideAddressHistoryDao(get()) }

    single<BasketRepository> { DefaultBasketRepository(get(), get()) }

    single<LikeListRepository<LikeMarketModel>>(named("likeMarketRepository")) {
        DefaultLikeMarketRepository(get(), get())
    }

    single<LikeListRepository<LikeItemModel>>(named("likeItemRepository")) {
        DefaultLikeItemRepository(get(), get())
    }
}