package com.chloe.shopshare.host

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.data.source.FakeTestRepository
import com.chloe.shopshare.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class HostViewModelTest{
    private lateinit var repository : FakeTestRepository
    private lateinit var viewModel: HostViewModel
    private lateinit var userId: String
    @Mock
    lateinit var mockApplication: MyApplication
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        MyApplication.instance = mockApplication
        repository = FakeTestRepository()
        viewModel = HostViewModel(repository, null)
    }

    @Test
    fun convertCategoryTitleToInt_woman_return101(){
        val title = "女裝"
        viewModel.convertCategoryTitleToValue(title)
        assertThat(viewModel.category.getOrAwaitValue(), `is`(101))
    }
}