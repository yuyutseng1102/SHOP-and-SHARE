package com.chloe.shopshare.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chloe.shopshare.MainViewModel
import com.chloe.shopshare.chat.ChatViewModel
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.detail.item.DetailDescriptionViewModel
import com.chloe.shopshare.home.item.HomeHostViewModel
import com.chloe.shopshare.home.item.HomeMainViewModel
import com.chloe.shopshare.home.item.HomeRequestViewModel
import com.chloe.shopshare.host.item.HostConditionViewModel
import com.chloe.shopshare.like.LikeViewModel
import com.chloe.shopshare.login.LoginViewModel
import com.chloe.shopshare.manage.groupmessage.GroupMessageViewModel
import com.chloe.shopshare.notify.NotifyViewModel
import com.chloe.shopshare.profile.ProfileViewModel
import com.chloe.shopshare.request.RequestViewModel
import com.chloe.shopshare.search.SearchViewModel

/** Factory for all ViewModels.**/
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(private val repository: Repository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository)

                isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository)

                isAssignableFrom(HomeMainViewModel::class.java) -> HomeMainViewModel(repository)

                isAssignableFrom(HomeHostViewModel::class.java) -> HomeHostViewModel(repository)

                isAssignableFrom(HomeRequestViewModel::class.java) -> HomeRequestViewModel(repository)

                isAssignableFrom(RequestViewModel::class.java) -> RequestViewModel(repository)

                isAssignableFrom(HostConditionViewModel::class.java) -> HostConditionViewModel()

                isAssignableFrom(DetailDescriptionViewModel::class.java) -> DetailDescriptionViewModel(repository)

                isAssignableFrom(LikeViewModel::class.java) -> LikeViewModel(repository)

                isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repository)

                isAssignableFrom(GroupMessageViewModel::class.java) -> GroupMessageViewModel(repository)

                isAssignableFrom(NotifyViewModel::class.java) -> NotifyViewModel(repository)

                isAssignableFrom(ChatViewModel::class.java) -> ChatViewModel(repository)

                isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(repository)

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
