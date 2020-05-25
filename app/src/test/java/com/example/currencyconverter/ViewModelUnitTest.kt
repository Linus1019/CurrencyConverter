package com.example.currencyconverter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.currencyconverter.models.Currency
import com.example.currencyconverter.viewModels.MainViewModel
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ViewModelUnitTest {

    lateinit var mainViewModel: MainViewModel

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setViewModel() {
        MockitoAnnotations.initMocks(this)
        this.mainViewModel = MainViewModel()
    }

    @Test
    fun mainViewModelTest() {

        val observer = mock(Observer::class.java) as Observer<String>
        mainViewModel.transferValue.observeForever(observer)

        val currencyObserver = mock(Observer::class.java) as Observer<Currency>
        mainViewModel.selectedCurrency.observeForever(currencyObserver)

        assertTrue(mainViewModel.errorCode.value != MainViewModel.ErrorCode.API_ERROR)
        //API 호출에 성공해서 환율정보 불러오기 성공
        assertTrue(mainViewModel.exchangeRateInfo.value != null)

        mainViewModel.transferValue.postValue("-1")
        mainViewModel.selectedCurrency.postValue(Currency(Currency.Code.JPY, "JPY", 20.0))

        //송금액의 총합계가 0보다 작은경우 AMOUNT_ERROR 코드가 설정
        assertTrue(mainViewModel.errorCode.value != MainViewModel.ErrorCode.AMOUNT_ERROR)
        //입력값은 0보다 크거나 10000보다 작아야 함
        assertTrue(mainViewModel.errorCode.value == MainViewModel.ErrorCode.INPUT_ERROR)
    }
}