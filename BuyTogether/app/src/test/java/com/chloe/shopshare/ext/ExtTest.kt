package com.chloe.shopshare.ext


import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.notify.NotifyType
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

class ExtTest{
    @Mock
    lateinit var mockApplication: MyApplication
    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        MyApplication.instance = mockApplication
    }

    @Test
    fun getWeek_thursday_returnThursday() {
        val day = Calendar.THURSDAY
        val result = getWeek(day)
        assertThat(result, `is`("星期四") )
    }

    @Test
    fun getDayWeek_returnYesterday(){
        val target = 1623661090072
        val result = target.toDisplayTimeGap()
        assertThat(result, `is`("昨天"))
    }



    @Test
    fun toDisplayNotifyContent_success_returnSuccessNotification(){
        println(MyApplication.instance)
        val notifyType =  NotifyType.STATUS_CHANGE_TO_GATHER_SUCCESS

        val result = notifyType.toDisplayNotifyContent("Title")
        assertThat(result, `is`("您參與的團購 : Title 已經成團囉 ! 快去看看團購詳情吧 ! "))

    }

    @Test
    fun getProductList_fourProduct(){
        val products = listOf(Product("明治 貓熊夾心餅乾組",1),
            Product("特趣迷你巧克力",3),
            Product("韓味不二 海苔酥",1),
            Product("北海鱈魚香絲",5)
        )
        val result = getProductList(products)
        assertThat(result, `is`("明治 貓熊夾心餅乾組 X 1,特趣迷你巧克力 X 3,韓味不二 海苔酥 X 1,北海鱈魚香絲 X 5"))
    }

}