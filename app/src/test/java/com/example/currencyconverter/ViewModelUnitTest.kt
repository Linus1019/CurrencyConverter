package com.example.currencyconverter

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.example.currencyconverter.models.Currency
import com.example.currencyconverter.viewModels.MainViewModel
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.text.DecimalFormat

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ViewModelUnitTest {
    lateinit var mainViewModel: MainViewModel

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainViewModel = MainViewModel()
    }

    @Test
    fun mainViewModelTest() {
        //API 호출에 성공해서 환율정보 불러오기 성공
        assertNotNull(mainViewModel.exchangeRateInfo.value)

        val observer = mock(Observer::class.java) as Observer<String>
        mainViewModel.transferValue.observeForever(observer)

        mainViewModel.transferValue.value = "-1"

        val currencyObserver = mock(Observer::class.java) as Observer<Currency>
        mainViewModel.selectedCurrency.observeForever(currencyObserver)

        mainViewModel.selectedCurrency.value = Currency(Currency.Code.JPY, "JPY", 20.0)

        //송금액의 총합계가 0보다 작은경우 AMOUNT_ERROR 코드가 설정
        assertEquals(mainViewModel.errorCode.value, MainViewModel.ErrorCode.AMOUNT_ERROR)

        mainViewModel.transferValue.value = "1"
        mainViewModel.selectedCurrency.value =
            Currency(
                Currency.Code.KRW,
                "KRW",
                mainViewModel.exchangeRateInfo.value!!.currencyKRW
            )

        val amountObserver = mock(Observer::class.java) as Observer<String>
        mainViewModel.amount.observeForever(amountObserver)

        assertNotNull(mainViewModel.amount.value)

        val pattern = "###,###,##0.00"

        //수취금액 = 송금액 * 환율
        assertEquals(mainViewModel.amount.value!!,
                DecimalFormat(pattern).format(mainViewModel.selectedCurrency.value!!.value *
                        mainViewModel.transferValue.value!!.toDouble())
        )
    }
}
